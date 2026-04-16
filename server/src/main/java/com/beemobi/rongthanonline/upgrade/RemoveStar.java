package com.beemobi.rongthanonline.upgrade;

import com.beemobi.rongthanonline.command.Command;
import com.beemobi.rongthanonline.command.CommandName;
import com.beemobi.rongthanonline.common.Language;
import com.beemobi.rongthanonline.entity.player.Player;
import com.beemobi.rongthanonline.item.Item;
import com.beemobi.rongthanonline.item.ItemName;
import com.beemobi.rongthanonline.item.ItemOption;
import com.beemobi.rongthanonline.item.ItemType;
import com.beemobi.rongthanonline.network.Message;
import com.beemobi.rongthanonline.npc.NpcName;
import com.beemobi.rongthanonline.shop.TypePrice;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

public class RemoveStar extends Upgrade {
    private static final Logger logger = Logger.getLogger(RemoveStar.class);

    public RemoveStar(UpgradeType type, String name, String command) {
        super(type, name, command);
        notes.add("- Vào túi đồ chọn vật phẩm cần gỡ Sao Pha Lê");
        notes.add("- Chọn Bùa tẩy chỉ số");
        notes.add("- Sau đó chọn Làm phép");
    }

    @Override
    public void upgrade(Message message, Player player) {
        player.lockAction.lock();
        try {
            int size = message.reader().readByte();
            if (size != 2) {
                player.addInfo(Player.INFO_RED, Language.ITEM_INVALID);
                return;
            }
            Item[] items = new Item[2];
            for (int i = 0; i < size; i++) {
                int index = message.reader().readByte();
                if (index < 0 || index >= player.itemsBag.length) {
                    return;
                }
                Item item = player.itemsBag[index];
                if (item == null) {
                    player.addInfo(Player.INFO_RED, Language.ITEM_INVALID);
                    return;
                }
                if (item.template.id == ItemName.BUA_TAY_CHI_SO) {
                    items[1] = item;
                } else {
                    items[0] = item;
                }
            }
            if (items[0] == null || items[1] == null) {
                player.addInfo(Player.INFO_RED, Language.ITEM_INVALID);
                return;
            }
            ItemOption option = items[0].getOption(68);
            if (option == null || option.param == 0) {
                player.addInfo(Player.INFO_RED, "Vật phẩm không thể gỡ sao hoặc không có sao");
                return;
            }
            StringBuilder content = new StringBuilder();
            content.append(String.format("Gỡ Sao Pha Lê %s", items[0].template.name)).append("\n");
            content.append(String.format("Cần sử dụng %d %s", option.param, items[1].template.name)).append("\n");
            content.append(String.format("Cần sử dụng %d Kim cương", option.param * 100)).append("\n");
            content.append("Các chỉ số sẽ mất:").append("\n");
            for (ItemOption itemOption : items[0].options) {
                if (itemOption.template.type == 4) {
                    content.append(itemOption.toString()).append("\n");
                }
            }
            List<Command> commands = new ArrayList<>();
            commands.add(new Command(CommandName.CONFIRM_UPGRADE, "Làm phép", player, items[0].indexUI, items[1].indexUI));
            commands.add(new Command(CommandName.CANCEL, "Từ chối", player));

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
            int[] index = new int[]{(int) objects[0], (int) objects[1]};
            Item[] items = new Item[2];
            for (int i : index) {
                Item item = player.itemsBag[i];
                if (item == null) {
                    player.addInfo(Player.INFO_RED, Language.ITEM_INVALID);
                    return;
                }
                if (item.template.id == ItemName.BUA_TAY_CHI_SO) {
                    items[1] = item;
                } else {
                    items[0] = item;
                }
            }
            if (items[0] == null || items[1] == null) {
                player.addInfo(Player.INFO_RED, Language.ITEM_INVALID);
                return;
            }
            ItemOption option = items[0].getOption(68);
            if (option == null || option.param == 0) {
                player.addInfo(Player.INFO_RED, "Vật phẩm không thể gỡ sao hoặc không có sao");
                return;
            }
            if (items[1].quantity < option.param) {
                player.addInfo(Player.INFO_RED, String.format("Bạn không đủ %s", items[1].template.name));
                return;
            }
            if (!player.isEnoughMoney(TypePrice.DIAMOND, option.param * 100L)) {
                return;
            }
            player.downMoney(TypePrice.DIAMOND, option.param * 100L);
            items[1].quantity -= option.param;
            if (items[1].quantity <= 0) {
                player.itemsBag[items[1].indexUI] = null;
            }
            option.param = 0;
            items[0].options.removeIf(o -> o.template.type == 4);
            player.service.setItemBag();
            player.addInfo(Player.INFO_YELLOW, "Làm phép thành công");
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
            if (item != null && (item.getStarUse() > 0 || item.template.id == ItemName.BUA_TAY_CHI_SO)) {
                flags[i] = true;
            }
        }
        return flags;
    }

}
