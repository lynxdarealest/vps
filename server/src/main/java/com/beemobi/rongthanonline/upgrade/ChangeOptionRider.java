package com.beemobi.rongthanonline.upgrade;

import com.beemobi.rongthanonline.command.Command;
import com.beemobi.rongthanonline.command.CommandName;
import com.beemobi.rongthanonline.common.Language;
import com.beemobi.rongthanonline.entity.player.Player;
import com.beemobi.rongthanonline.item.*;
import com.beemobi.rongthanonline.network.Message;
import com.beemobi.rongthanonline.npc.NpcName;
import com.beemobi.rongthanonline.util.Utils;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ChangeOptionRider extends Upgrade {
    public static final int QUANTITY = 1;
    public static final int DIAMOND = 50;
    private static final Logger logger = Logger.getLogger(ChangeOptionRider.class);

    public ChangeOptionRider(UpgradeType type, String name, String command) {
        super(type, name, command);
        notes.add("- Vào túi đồ chọn trang bị hiệp sĩ đã có chỉ số");
        notes.add("- Chọn Ngọc rồng 3 sao");
        notes.add("- Sau đó chọn Làm phép");
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
            if (size != 2) {
                player.addInfo(Player.INFO_RED, Language.ITEM_INVALID);
                return;
            }
            Item[] items = new Item[2];
            for (int i = 0; i < items.length; i++) {
                int index = message.reader().readByte();
                if (index < 0 || index >= player.itemsBag.length) {
                    return;
                }
                Item item = player.itemsBag[index];
                if (item == null) {
                    player.addInfo(Player.INFO_RED, Language.ITEM_INVALID);
                    return;
                }
                if (items[0] == null && item.isItemRider() && item.options.stream().anyMatch(o -> o.template.type == 7)) {
                    items[0] = item;
                }
                if (items[1] == null && item.template.id == ItemName.NGOC_RONG_3_SAO) {
                    items[1] = item;
                }
            }
            if (items[0] == null || items[1] == null) {
                player.addInfo(Player.INFO_RED, Language.ITEM_INVALID);
                return;
            }
            List<ItemOption> itemOptions = items[0].options.stream().filter(o -> o.template.type == 7).toList();
            StringBuilder content = new StringBuilder();
            content.append(String.format("Làm mới %s", items[0].template.name)).append("\n");
            content.append(String.format("Cần sử dụng x%d %s", QUANTITY, items[1].template.name)).append("\n");
            content.append("Sau khi làm phép sẽ xóa bỏ các chỉ số:").append("\n");
            for (ItemOption itemOption : itemOptions) {
                content.append(itemOption.toString()).append("\n");
            }
            content.append("Nhận ngẫu nhiên 1 trong các chỉ số:").append("\n");
            int[] options = EquipCraftingRider.OPTIONS[items[0].template.gender];
            int level = Math.max(1, items[0].getParam(162));
            for (int option : options) {
                int param = EquipCraftingRider.params.get(option);
                for (int i = 1; i < level; i++) {
                    param += Math.max(param / 5, 1);
                }
                content.append(ItemManager.getInstance().itemOptionTemplates.get(option).name.replace("#", param + "")).append("\n");
            }
            List<Command> commands = new ArrayList<>();
            commands.add(new Command(CommandName.CONFIRM_UPGRADE, String.format("Làm phép\n%s KC", Utils.currencyFormat(DIAMOND)), player, items[0].indexUI, items[1].indexUI));
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
            if (player.isBagFull()) {
                player.addInfo(Player.INFO_RED, Language.ME_BAG_FULL);
                return;
            }
            int[] index = new int[]{(int) objects[0], (int) objects[1]};
            Item[] items = new Item[2];
            for (int i = 0; i < items.length; i++) {
                Item item = player.itemsBag[index[i]];
                if (item == null) {
                    player.addInfo(Player.INFO_RED, Language.ITEM_INVALID);
                    return;
                }
                if (items[0] == null && item.isItemRider() && item.options.stream().anyMatch(o -> o.template.type == 7)) {
                    items[0] = item;
                }
                if (items[1] == null && item.template.id == ItemName.NGOC_RONG_3_SAO) {
                    items[1] = item;
                }
            }
            if (items[0] == null || items[1] == null) {
                player.addInfo(Player.INFO_RED, Language.ITEM_INVALID);
                return;
            }
            int quantity = QUANTITY;
            if (items[1].quantity < quantity) {
                player.addInfo(Player.INFO_RED, String.format("Bạn không đủ %s", items[1].template.name));
                return;
            }
            if (player.diamond < DIAMOND) {
                player.addInfo(Player.INFO_RED, "Bạn không đủ kim cương");
                return;
            }
            player.upDiamond(-DIAMOND);
            items[1].quantity -= quantity;
            if (items[1].quantity <= 0) {
                player.itemsBag[items[1].indexUI] = null;
            }
            int optionId = Utils.nextArray(EquipCraftingRider.OPTIONS[items[0].template.gender]);
            items[0].options.removeIf(o -> o.template.type == 7);
            ItemOption itemOption = new ItemOption(optionId, EquipCraftingRider.params.get(optionId));
            items[0].options.add(itemOption);
            ItemOption option = items[0].getOption(162);
            if (option == null) {
                items[0].options.add(new ItemOption(162, 1));
            } else {
                for (int i = 0; i < option.param - 1; i++) {
                    itemOption.param += Math.max(itemOption.param / 5, 1);
                }
            }
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
            if (item != null && ((item.isItemRider() && item.options.stream().anyMatch(o -> o.template.type == 7)) || item.template.id == ItemName.NGOC_RONG_3_SAO)) {
                flags[i] = true;
            }
        }
        return flags;
    }
}