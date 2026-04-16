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
import java.util.HashMap;
import java.util.List;

public class EquipCraftingRider extends Upgrade {
    public static final int QUANTITY = 100;
    public static final int COIN = 10000000;
    private static final Logger logger = Logger.getLogger(EquipCraftingRider.class);
    public static int[][] OPTIONS = new int[][]{{139, 140, 141, 142, 146, 143, 148}, {139, 140, 141, 142, 146, 144, 149}, {139, 140, 141, 142, 146, 145, 147}};

    public static HashMap<Integer, Integer> params = new HashMap<>();

    static {
        params.put(139, 10);
        params.put(140, 10);
        params.put(141, 10);
        params.put(142, 10);
        params.put(146, 100);
        params.put(143, 15);
        params.put(144, 15);
        params.put(145, 15);
        params.put(147, 15);
        params.put(148, 15);
        params.put(149, 15);
    }

    public EquipCraftingRider(UpgradeType type, String name, String command) {
        super(type, name, command);
        notes.add("- Vào túi đồ chọn trang bị hiệp sĩ chưa có chỉ số");
        notes.add("- Chọn Tinh thạch");
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
                if (items[0] == null && item.isItemRider() && item.options.stream().noneMatch(o -> o.template.type == 7)) {
                    items[0] = item;
                }
                if (items[1] == null && item.template.id == ItemName.TINH_THACH) {
                    items[1] = item;
                }
            }
            if (items[0] == null || items[1] == null) {
                player.addInfo(Player.INFO_RED, Language.ITEM_INVALID);
                return;
            }
            ItemTemplate template = ItemManager.getInstance().itemTemplates.values().stream().filter(i -> i.levelRequire == 1 && i.type == items[0].template.type && (i.gender == items[0].template.gender || i.gender == -1)).findFirst().orElse(null);
            if (template == null) {
                player.addInfo(Player.INFO_RED, Language.CANT_ACTION);
                return;
            }
            StringBuilder content = new StringBuilder();
            content.append(String.format("Nâng cấp %s", items[0].template.name)).append("\n");
            content.append(String.format("Cần sử dụng x%d %s", QUANTITY, items[1].template.name)).append("\n");
            content.append("Các chỉ số cũ, cấp, sao sẽ bị xóa bỏ toàn bộ").append("\n");
            content.append(String.format("Cần sử dụng x%d %s", QUANTITY, items[1].template.name)).append("\n");
            content.append("Sau khi làm phép sẽ nhận ngẫu nhiên 1 trong các chỉ số:").append("\n");
            int[] options = OPTIONS[items[0].template.gender];
            for (int option : options) {
                content.append(ItemManager.getInstance().itemOptionTemplates.get(option).name.replace("#", params.get(option) + "")).append("\n");
            }
            List<Command> commands = new ArrayList<>();
            commands.add(new Command(CommandName.CONFIRM_UPGRADE, String.format("Làm phép\n%s xu", Utils.currencyFormat(COIN)), player, items[0].indexUI, items[1].indexUI));
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
                if (items[0] == null && item.isItemRider() && item.options.stream().noneMatch(o -> o.template.type == 7)) {
                    items[0] = item;
                }
                if (items[1] == null && item.template.id == ItemName.TINH_THACH) {
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
            long coin = COIN;
            if (player.xu < coin) {
                player.addInfo(Player.INFO_RED, "Bạn không đủ xu");
                return;
            }
            ItemTemplate template = ItemManager.getInstance().itemTemplates.values().stream().filter(i -> i.levelRequire == 1 && i.type == items[0].template.type && (i.gender == items[0].template.gender || i.gender == -1)).findFirst().orElse(null);
            if (template == null) {
                player.addInfo(Player.INFO_RED, Language.CANT_ACTION);
                return;
            }
            player.upXu(-coin);
            items[1].quantity -= quantity;
            if (items[1].quantity <= 0) {
                player.itemsBag[items[1].indexUI] = null;
            }
            items[0].options.removeIf(o -> o.template.id != 36);
            int optionId = Utils.nextArray(OPTIONS[items[0].template.gender]);
            List<ItemOption> options = ItemManager.getInstance().itemOptions.get(template.id);
            for (ItemOption option : options) {
                items[0].options.add(new ItemOption(option.template.id, option.param));
            }
            items[0].randomParam(-15, 15);
            items[0].options.add(new ItemOption(optionId, params.get(optionId)));
            switch (items[0].template.gender) {
                case 0:
                    items[0].options.add(new ItemOption(150, 1));
                    break;
                case 1:
                    items[0].options.add(new ItemOption(151, 1));
                    break;
                case 2:
                    items[0].options.add(new ItemOption(152, 1));
                    break;
            }
            items[0].options.add(new ItemOption(162, 1));
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
            if (item != null && ((item.isItemRider() && item.options.stream().noneMatch(o -> o.template.type == 7)) || item.template.id == ItemName.TINH_THACH)) {
                flags[i] = true;
            }
        }
        return flags;
    }
}
