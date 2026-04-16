package com.beemobi.rongthanonline.upgrade;

import com.beemobi.rongthanonline.command.Command;
import com.beemobi.rongthanonline.command.CommandName;
import com.beemobi.rongthanonline.common.Language;
import com.beemobi.rongthanonline.entity.player.Player;
import com.beemobi.rongthanonline.item.Item;
import com.beemobi.rongthanonline.item.ItemManager;
import com.beemobi.rongthanonline.item.ItemOption;
import com.beemobi.rongthanonline.network.Message;
import com.beemobi.rongthanonline.npc.NpcName;
import com.beemobi.rongthanonline.shop.TypePrice;
import com.beemobi.rongthanonline.util.Utils;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

public class RefreshMaxItem extends Upgrade {
    private static final Logger logger = Logger.getLogger(RefreshMaxItem.class);

    private static final long[] COIN = new long[]{100000, 1000000, 10000000, 20000000, 50000000, 100000000, 200000000, 500000000};

    public RefreshMaxItem(UpgradeType type, String name, String command) {
        super(type, name, command);
        notes.add("- Vào túi đồ chọn trang bị cần làm max chỉ số");
        notes.add("- Sau đó chọn thêm 2 trang bị cùng loại khác");
        notes.add("- Cả 3 trang bị yêu cầu cấp tối thiểu là +14");
        notes.add("- Sau đó chọn Tái chế");
    }

    @Override
    public void upgrade(Message message, Player player) {
        player.lockAction.lock();
        try {
            if (player.isBagFull()) {
                player.addInfo(Player.INFO_RED, Language.ME_BAG_FULL);
                return;
            }
            int size = message.reader().readByte();
            if (size != 3) {
                player.addInfo(Player.INFO_RED, "Số lượng trang bị không hợp lệ");
                return;
            }
            Item[] items = new Item[3];
            for (int i = 0; i < items.length; i++) {
                int index = message.reader().readByte();
                if (index < 0 || index >= player.itemsBag.length) {
                    return;
                }
                Item item = player.itemsBag[index];
                if (item == null || item.template.type >= 8 || item.isItemRider() || item.getUpgrade() < 14) {
                    player.addInfo(Player.INFO_RED, Language.ITEM_INVALID);
                    return;
                }
                items[i] = item;
                if (i > 0 && items[i].template.id != items[0].template.id) {
                    player.addInfo(Player.INFO_RED, Language.ITEM_INVALID);
                    return;
                }
            }
            List<Command> commands = new ArrayList<>();
            StringBuilder content = new StringBuilder();
            content.append(String.format("Tái chế %s", items[0].template.name)).append("\n");
            content.append(String.format("Sau khi tái chế sẽ nhận được %s có chỉ số tối đa", items[0].template.name)).append("\n");
            content.append("Cấp độ và Sao pha lê sẽ được bảo toàn theo trang bị được bỏ vào đầu tiên").append("\n");
            content.append("Tỉ lệ thành công 50%").append("\n");
            content.append("Thất bại sẽ mất 2 trang bị bỏ vào sau");
            content.append("\n").append("Trang bị sẽ bị khóa sau khi tái chế");
            commands.add(new Command(CommandName.CONFIRM_UPGRADE, String.format("Tái chế\n%s xu", Utils.formatNumber(COIN[items[0].template.levelRequire / 10])), player, items[0].indexUI, items[1].indexUI, items[2].indexUI));
            commands.add(new Command(CommandName.CANCEL, "Hủy", player));
            player.createMenu(NpcName.ME, content.toString(), commands);
        } catch (Exception ex) {
            logger.error("upgrade", ex);
        } finally {
            player.lockAction.unlock();
        }
    }

    @Override
    public void confirmUpgrade(Player player, Object[] objects) {
        player.lockAction.lock();
        try {
            if (player.isBagFull()) {
                player.addInfo(Player.INFO_RED, Language.ME_BAG_FULL);
                return;
            }
            int[] index = new int[]{(int) objects[0], (int) objects[1], (int) objects[2]};
            Item[] items = new Item[3];
            for (int i = 0; i < items.length; i++) {
                if (index[i] < 0 || index[i] >= player.itemsBag.length) {
                    return;
                }
                Item item = player.itemsBag[index[i]];
                if (item == null || item.template.type >= 8 || item.isItemRider() || item.getUpgrade() < 14) {
                    player.addInfo(Player.INFO_RED, Language.ITEM_INVALID);
                    return;
                }
                items[i] = item;
                if (i > 0 && items[i].template.id != items[0].template.id) {
                    player.addInfo(Player.INFO_RED, Language.ITEM_INVALID);
                    return;
                }
            }
            long coin = COIN[items[0].template.levelRequire / 10];
            if (!player.isEnoughMoney(TypePrice.COIN, coin)) {
                return;
            }
            player.downMoney(TypePrice.COIN, coin);
            player.itemsBag[items[1].indexUI] = null;
            player.itemsBag[items[2].indexUI] = null;
            if (Utils.nextInt(100) > 50) {
                player.addInfo(Player.INFO_RED, "Tái chế thất bại");
            } else {
                Item item = ItemManager.getInstance().createItem(items[0].template.id, 1, true);
                item.randomParam(15, 15);
                int upgrade = items[0].getUpgrade();
                for (int i = 0; i < upgrade; i++) {
                    item.nextUpgrade();
                }
                item.isLock = true;
                ItemOption optionStar = items[0].getOption(67);
                if (optionStar != null) {
                    item.options.add(new ItemOption(67, optionStar.param));
                    ItemOption itemOption = items[0].getOption(68);
                    if (itemOption == null) {
                        item.options.add(new ItemOption(68, 0));
                    } else {
                        item.options.add(new ItemOption(68, itemOption.param));
                        for (ItemOption option : items[0].options) {
                            if (option.template.type == 4) {
                                item.options.add(new ItemOption(option.template.id, option.param));
                            }
                        }
                    }
                }
                item.indexUI = items[0].indexUI;
                player.itemsBag[items[0].indexUI] = item;
                player.addInfo(Player.INFO_YELLOW, "Tái chế thành công");
            }
            player.service.setItemBag();
        } catch (Exception ex) {
            logger.error("confirmUpgrade", ex);
        } finally {
            player.lockAction.unlock();
        }
    }

    @Override
    public boolean[] getIndexCanUpgrade(Player player) {
        boolean[] flags = new boolean[player.itemsBag.length];
        for (int i = 0; i < flags.length; i++) {
            Item item = player.itemsBag[i];
            if (item != null && item.template.type < 8 && item.getUpgrade() >= 14) {
                flags[i] = true;
            }
        }
        return flags;
    }
}
