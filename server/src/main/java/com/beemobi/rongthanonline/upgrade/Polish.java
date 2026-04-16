package com.beemobi.rongthanonline.upgrade;

import com.beemobi.rongthanonline.command.Command;
import com.beemobi.rongthanonline.command.CommandName;
import com.beemobi.rongthanonline.common.Language;
import com.beemobi.rongthanonline.common.RandomCollection;
import com.beemobi.rongthanonline.npc.NpcName;
import com.beemobi.rongthanonline.entity.player.Player;
import com.beemobi.rongthanonline.item.Item;
import com.beemobi.rongthanonline.item.ItemManager;
import com.beemobi.rongthanonline.item.ItemName;
import com.beemobi.rongthanonline.network.Message;
import com.beemobi.rongthanonline.util.Utils;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

public class Polish extends Upgrade {
    private static final Logger logger = Logger.getLogger(Polish.class);

    public static final RandomCollection<Integer> options = new RandomCollection<>();

    public Polish(UpgradeType type, String name, String command) {
        super(type, name, command);
        notes.add("- Vào túi đồ chọn Sao Pha Lê chưa được đánh bóng");
        notes.add("- Lệ phí đánh bóng Sao Pha Lê là 10k xu");
        notes.add("- Sau đó chọn Đánh bóng");

        options.add(7, 53);
        options.add(7, 54);
        options.add(7, 55);
        options.add(7, 56);
        options.add(7, 57);
        options.add(7, 58);
        options.add(7, 59);
        options.add(7, 60);
        options.add(7, 61);
        options.add(7, 62);
        options.add(7, 63);
        options.add(7, 64);
        options.add(7, 65);
        options.setDefault(66);
    }

    @Override
    public void upgrade(Message message, Player player) {
        player.lockAction.lock();
        try {
            if (player.isBagFull()) {
                player.addInfo(Player.INFO_RED, Language.ME_BAG_FULL);
                return;
            }
            Item[] items = new Item[2];
            int size = message.reader().readByte();
            if (size <= 0 || size > 2) {
                return;
            }
            for (int i = 0; i < size; i++) {
                int index = message.reader().readByte();
                if (index < 0 || index >= player.itemsBag.length) {
                    return;
                }
                Item item = player.itemsBag[index];
                if (item == null) {
                    return;
                }
                if (item.template.id == ItemName.SAO_PHA_LE) {
                    if (items[0] == null) {
                        items[0] = item;
                    } else {
                        player.addInfo(Player.INFO_RED, "Chỉ được chọn duy nhất 1 loại Sao pha lê");
                        return;
                    }
                } else if (item.template.id == ItemName.BINH_NUOC_PHEP) {
                    if (items[1] == null) {
                        items[1] = item;
                    } else {
                        player.addInfo(Player.INFO_RED, "Chỉ có thể chọn duy nhất 1 loại bình nước phép");
                        return;
                    }
                } else {
                    player.addInfo(Player.INFO_RED, "Vật phẩm nâng cấp không hợp lệ");
                    return;
                }
            }
            if (items[0] == null) {
                player.addInfo(Player.INFO_RED, "Bạn chưa chọn Sao pha lê");
                return;
            }
            if (items[0].getOption(52) == null) {
                player.addInfo(Player.INFO_RED, "Sao Pha Lê đã được đánh bóng");
                return;
            }
            int xu = 10000;
            if (player.xu < xu) {
                player.addInfo(Player.INFO_RED, String.format("Bạn còn thiếu %s xu", Utils.formatNumber(xu - player.xu)));
                return;
            }
            int indexWater = items[1] != null ? items[1].indexUI : -1;
            List<Command> commands = new ArrayList<>();
            commands.add(new Command(CommandName.CONFIRM_UPGRADE, String.format("x1\n%s xu", Utils.formatNumber(xu)), player, items[0].indexUI, indexWater, 1));
            if (items[0].quantity >= 10 && player.getCountItemBagEmpty() >= 10) {
                commands.add(new Command(CommandName.CONFIRM_UPGRADE, String.format("x10\n%s xu", Utils.formatNumber(xu * 10)), player, items[0].indexUI, indexWater, 10));
            }
            player.createMenu(NpcName.ME, "", commands);
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
            int quantity = (int) objects[2];
            if (player.getCountItemBagEmpty() < quantity) {
                player.addInfo(Player.INFO_RED, String.format("Cần ít nhất %d ô trống trong túi đồ", 10));
                return;
            }
            Item[] items = new Item[2];
            int[] index = new int[]{(int) objects[0], (int) objects[1]};
            if (index[0] < 0 || index[0] >= player.itemsBag.length) {
                return;
            }
            items[0] = player.itemsBag[index[0]];
            if (items[0] == null || items[0].template.id != ItemName.SAO_PHA_LE) {
                player.addInfo(Player.INFO_RED, "Bạn chưa chọn Sao pha lê");
                return;
            }
            if (items[0].getOption(52) == null) {
                player.addInfo(Player.INFO_RED, "Sao Pha Lê đã được đánh bóng");
                return;
            }
            if (index[1] != -1) {
                items[1] = player.itemsBag[index[1]];
                if (items[1] == null || items[1].template.id != ItemName.BINH_NUOC_PHEP) {
                    return;
                }
            }
            if (quantity > items[0].quantity || (items[1] != null && quantity > items[1].quantity)) {
                player.addInfo(Player.INFO_RED, "Số lượng không hợp lệ");
                return;
            }
            int xu = 10000 * quantity;
            if (player.xu < xu) {
                player.addInfo(Player.INFO_RED, String.format("Bạn còn thiếu %s xu", Utils.formatNumber(xu - player.xu)));
                return;
            }
            boolean isLucky = false;
            player.upXu(-xu);
            items[0].quantity -= quantity;
            if (items[0].quantity <= 0) {
                player.itemsBag[index[0]] = null;
            }
            if (items[1] != null) {
                isLucky = true;
                items[1].quantity -= quantity;
                if (items[1].quantity <= 0) {
                    player.itemsBag[index[1]] = null;
                }
            }
            if (player.itemsBag[index[0]] == null || (index[1] != -1 && player.itemsBag[index[1]] == null)) {
                player.service.setItemBag();
            } else {
                player.service.refreshQuantityItemBag(index[0], items[0].quantity);
                if (items[1] != null) {
                    player.service.refreshQuantityItemBag(index[1], items[1].quantity);
                }
            }
            while (quantity > 0) {
                Item item = ItemManager.getInstance().createItem(ItemName.SAO_PHA_LE_DANH_BONG, false);
                item.options.add(item.createOptionCrystal(isLucky, false));
                player.addItem(item);
                quantity--;
            }
            player.addInfo(Player.INFO_YELLOW, "Đánh bóng thành công");
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
            if (item != null && item.template.id == ItemName.SAO_PHA_LE) {
                flags[i] = true;
            }
        }
        return flags;
    }
}
