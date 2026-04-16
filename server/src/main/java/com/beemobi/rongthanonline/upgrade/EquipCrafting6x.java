package com.beemobi.rongthanonline.upgrade;

import com.beemobi.rongthanonline.command.Command;
import com.beemobi.rongthanonline.command.CommandName;
import com.beemobi.rongthanonline.common.Language;
import com.beemobi.rongthanonline.entity.player.Player;
import com.beemobi.rongthanonline.item.Item;
import com.beemobi.rongthanonline.item.ItemManager;
import com.beemobi.rongthanonline.item.ItemName;
import com.beemobi.rongthanonline.item.ItemOption;
import com.beemobi.rongthanonline.network.Message;
import com.beemobi.rongthanonline.npc.NpcName;
import com.beemobi.rongthanonline.util.Utils;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class EquipCrafting6x extends Upgrade {
    private static final Logger logger = Logger.getLogger(EquipCrafting4x.class);

    public static final HashMap<Integer, Object[]> ITEMS;

    static {
        ITEMS = new HashMap<>();
        ITEMS.put(ItemName.GANG_SIEU_NHAN, new Object[]{ItemName.GANG_THAN_LINH, 10000, 1000000000});
        ITEMS.put(ItemName.GANG_SIEU_NAMEK, new Object[]{ItemName.GANG_THAN_NAMEK, 10000, 1000000000});
        ITEMS.put(ItemName.GANG_SIEU_SAYAIN, new Object[]{ItemName.GANG_THAN_SAYIAN, 10000, 1000000000});
        ITEMS.put(ItemName.GIAY_SIEU_NHAN, new Object[]{ItemName.GIAY_THAN_LINH, 5000, 500000000});
        ITEMS.put(ItemName.GIAY_SIEU_NAMEK, new Object[]{ItemName.GIAY_THAN_NAMEK, 5000, 500000000});
        ITEMS.put(ItemName.GIAY_SIEU_SAYAIN, new Object[]{ItemName.GIAY_THAN_SAYIAN, 5000, 500000000});
        ITEMS.put(ItemName.QUAN_SIEU_NHAN, new Object[]{ItemName.QUAN_THAN_LINH, 5000, 500000000});
        ITEMS.put(ItemName.QUAN_SIEU_NAMEK, new Object[]{ItemName.QUAN_THAN_NAMEK, 5000, 500000000});
        ITEMS.put(ItemName.QUAN_SIEU_SAYAIN, new Object[]{ItemName.QUAN_THAN_SAYIAN, 5000, 500000000});
        ITEMS.put(ItemName.AO_SIEU_NHAN, new Object[]{ItemName.AO_THAN_LINH, 5000, 500000000});
        ITEMS.put(ItemName.AO_SIEU_NAMEK, new Object[]{ItemName.AO_THAN_NAMEK, 5000, 500000000});
        ITEMS.put(ItemName.AO_SIEU_SAYAIN, new Object[]{ItemName.AO_THAN_SAYIAN, 5000, 500000000});
        ITEMS.put(ItemName.NGOC_BOI_SIEU_CAP, new Object[]{ItemName.NGOC_BOI_THAN_LINH, 7500, 750000000});
        ITEMS.put(ItemName.NHAN_SIEU_CAP, new Object[]{ItemName.NHAN_THAN_LINH, 7500, 750000000});
        ITEMS.put(ItemName.DAY_CHUYEN_SIEU_CAP, new Object[]{ItemName.DAY_CHUYEN_THAN_LINH, 7500, 750000000});
        ITEMS.put(ItemName.RADAR_SIEU_CAP, new Object[]{ItemName.RADAR_THAN_LINH, 7500, 750000000});
    }

    public EquipCrafting6x(UpgradeType type, String name, String command) {
        super(type, name, command);
        notes.add("- Vào túi đồ chọn trang bị 4x cần chế tạo lên trang bị 6x");
        notes.add("- Sau đó chọn Đá ngũ sắc");
        notes.add("- Trang bị 4x yêu cầu từ +14 trở lên");
        notes.add("- Sau đó chọn Chế tạo");
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
                if (items[0] == null && ITEMS.containsKey(item.template.id)) {
                    items[0] = item;
                }
                if (items[1] == null && item.template.id == ItemName.DA_NGU_SAC) {
                    items[1] = item;
                }
            }
            if (items[0] == null || items[1] == null) {
                player.addInfo(Player.INFO_RED, Language.ITEM_INVALID);
                return;
            }
            if (items[0].getUpgrade() < 14) {
                player.addInfo(Player.INFO_RED, "Yêu cầu trang bị +14 trở lên");
                return;
            }
            Item itemRoot = items[0].cloneItem();
            int upgrade = itemRoot.getUpgrade();
            int num = 0;
            while (upgrade > 0 && num < UpgradeItem.MAX_UPGRADE) {
                upgrade = itemRoot.downUpgrade();
                num++;
            }
            Object[] objects = ITEMS.get(items[0].template.id);
            StringBuilder content = new StringBuilder();
            content.append(String.format("Chế tạo %s", ItemManager.getInstance().itemTemplates.get((int) objects[0]).name)).append("\n");
            content.append(String.format("Cần sử dụng x%s %s và %s xu", Utils.formatNumber((int) objects[1]), items[1].template.name, Utils.formatNumber((int) objects[2]))).append("\n");
            int star = items[0].getParam(67);
            if (star > 0) {
                double coin = 0;
                for (int i = 0; i < star; i++) {
                    coin += (double) Crystallize.XU_CRYSTAL_ITEM[i] * 100 / Crystallize.PERCENT_CRYSTAL_ITEM[i];
                }
                content.append(String.format("Chế tạo đặc biệt sẽ bảo toàn Sao pha lê và cần thêm %s xu hoặc %s KC", Utils.formatNumber((long) coin), Utils.formatNumber(((long) coin) / 1000000))).append("\n");
            }
            content.append("Tỉ lệ chỉ số sẽ được bảo toàn sau khi chế tạo thành công");
            content.append("\n").append("Trang bị sẽ bị khóa sau khi chế tạo");
            List<Command> commands = new ArrayList<>();
            commands.add(new Command(CommandName.CONFIRM_UPGRADE, "Thường", player, 0, items[0].indexUI, items[1].indexUI));
            commands.add(new Command(CommandName.CONFIRM_UPGRADE, "Đặc biệt\n(xu)", player, 1, items[0].indexUI, items[1].indexUI));
            commands.add(new Command(CommandName.CONFIRM_UPGRADE, "Đặc biệt\n(KC)", player, 2, items[0].indexUI, items[1].indexUI));
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
            int type = (int) objects[0];
            int[] index = new int[]{(int) objects[1], (int) objects[2]};
            Item[] items = new Item[2];
            for (int i = 0; i < items.length; i++) {
                if (index[i] < 0 || index[i] >= player.itemsBag.length) {
                    return;
                }
                Item item = player.itemsBag[index[i]];
                if (item == null) {
                    player.addInfo(Player.INFO_RED, Language.ITEM_INVALID);
                    return;
                }
                if (items[0] == null && ITEMS.containsKey(item.template.id)) {
                    items[0] = item;
                }
                if (items[1] == null && item.template.id == ItemName.DA_NGU_SAC) {
                    items[1] = item;
                }
            }
            if (items[0] == null || items[1] == null) {
                player.addInfo(Player.INFO_RED, Language.ITEM_INVALID);
                return;
            }
            if (items[0].getUpgrade() < 14) {
                player.addInfo(Player.INFO_RED, "Yêu cầu trang bị +14 trở lên");
                return;
            }
            Object[] array = ITEMS.get(items[0].template.id);
            int quantity = (int) array[1];
            if (items[1].quantity < quantity) {
                player.addInfo(Player.INFO_RED, String.format("Bạn không đủ %s", items[1].template.name));
                return;
            }
            long coin = (int) array[2];
            int diamond = 0;
            if (type == 1 || type == 2) {
                int star = items[0].getParam(67);
                if (star > 0) {
                    double total = 0;
                    for (int i = 0; i < star; i++) {
                        total += (double) Crystallize.XU_CRYSTAL_ITEM[i] * 100 / Crystallize.PERCENT_CRYSTAL_ITEM[i];
                    }
                    if (type == 1) {
                        coin += (long) total;
                    } else {
                        diamond += (int) (total / 1000000);
                        if (diamond < 1) {
                            diamond = 1;
                        }
                    }
                }
            }
            if (player.xu < coin) {
                player.addInfo(Player.INFO_RED, "Bạn không đủ xu");
                return;
            }
            if (diamond > 0) {
                if (player.diamond < diamond) {
                    player.addInfo(Player.INFO_RED, "Bạn không đủ kim cương");
                    return;
                }
                player.upDiamond(-diamond);
            }
            player.upXu(-coin);
            items[1].quantity -= quantity;
            if (items[1].quantity <= 0) {
                player.itemsBag[items[1].indexUI] = null;
            }
            HashMap<Integer, Integer[]> rates = new HashMap<>();
            Item itemDefault = ItemManager.getInstance().createItem(items[0].template.id, 1, true);
            for (ItemOption option : itemDefault.options) {
                if (!rates.containsKey(option.template.id)) {
                    rates.put(option.template.id, new Integer[]{0, 0});
                }
                rates.get(option.template.id)[0] = option.param;
            }
            Item itemRoot = items[0].cloneItem();
            int upgrade = itemRoot.getUpgrade();
            int num = 0;
            while (upgrade > 0 && num < UpgradeItem.MAX_UPGRADE) {
                upgrade = itemRoot.downUpgrade();
                num++;
            }
            for (ItemOption option : itemRoot.options) {
                if (!rates.containsKey(option.template.id)) {
                    rates.put(option.template.id, new Integer[]{0, 0});
                }
                rates.get(option.template.id)[1] = option.param;
            }
            Item item = ItemManager.getInstance().createItem((int) array[0], 1, true);
            for (ItemOption option : item.options) {
                Integer[] params = rates.getOrDefault(option.template.id, new Integer[]{0, 0});
                if (params[1] <= 0) {
                    int percent = -15;
                    option.param = Math.round(((float) option.param) / 100 * (float) (percent + 100));
                    if (option.param < 1) {
                        option.param = 1;
                    }
                } else if (params[0] > 0) {
                    float param = (float) option.param;
                    option.param = Math.round(Math.min(param * (float) params[1] / (float) params[0], param * 1.14f));
                    if (option.param < 1) {
                        option.param = 1;
                    }
                }
            }
            item.isLock = true;
            if (type == 1 || type == 2) {
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
            }
            item.indexUI = items[0].indexUI;
            player.itemsBag[items[0].indexUI] = item;
            player.service.setItemBag();
            player.addInfo(Player.INFO_YELLOW, String.format("Bạn nhận được %s", item.template.name));
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
            if (item != null && (ITEMS.containsKey(item.template.id) || item.template.id == ItemName.DA_NGU_SAC)) {
                flags[i] = true;
            }
        }
        return flags;
    }
}
