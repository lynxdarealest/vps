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
import java.util.stream.Collectors;

public class UpgradeItemRider extends Upgrade {
    private static final Logger logger = Logger.getLogger(UpgradeItemRider.class);

    public static final int[] PERCENT = {50, 30, 20, 15, 10, 5, 5};

    public static final int DIAMOND = 100;

    public UpgradeItemRider(UpgradeType type, String name, String command) {
        super(type, name, command);
        notes.add("- Vào túi đồ chọn trang bị hiệp sĩ +12 trở lên");
        notes.add("- Chọn trang bị thường cùng loại +12 trở lên, hơn một hoặc bằng cấp trang bị hiệp sĩ (ví dụ trang bị hiệp sĩ không cấp hoặc cấp 1 thì chỉ có thể chọn trang bị 1x +12 hoặc 2x +12)");
        notes.add("- Chọn trang bị hiệp sĩ trắng cùng loại (có thể khác hành tinh)");
        notes.add("- Chọn Ngọc rồng 3 sao");
        notes.add("- Chọn Đá cấp 10");
        notes.add("- Chọn Đá hải lam");
        notes.add("- Chọn Tinh thạch");
        notes.add("- Sau đó chọn Làm phép");
        notes.add("* Lưu ý: trang bị hiệp sĩ từ +14 trở lên sẽ có chức năng bảo toàn cấp cường hóa nếu sử dụng thêm Thẻ vạn năng");
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
            if (size != 7 && size != 8) {
                player.addInfo(Player.INFO_RED, Language.ITEM_INVALID);
                return;
            }
            Item[] items = new Item[8];
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
                if (item.template.id == ItemName.NGOC_RONG_3_SAO) {
                    items[2] = item;
                } else if (item.template.id == ItemName.DA_HAI_LAM) {
                    items[3] = item;
                } else if (item.template.id == ItemName.DA_10) {
                    items[4] = item;
                } else if (item.template.id == ItemName.TINH_THACH) {
                    items[5] = item;
                } else if (item.template.id == ItemName.THE_VAN_NANG) {
                    items[6] = item;
                } else if (item.isItemRider()) {
                    if (item.options.stream().anyMatch(o -> o.template.type == 7)) {
                        items[0] = item;
                    } else {
                        items[7] = item;
                    }
                } else if (item.template.type < 8) {
                    items[1] = item;
                }
            }
            if (items[0] == null || items[1] == null || items[2] == null || items[3] == null || items[4] == null || items[5] == null || items[7] == null
                    || items[0].template.type != items[1].template.type
                    || items[0].template.type != items[7].template.type
                    || (items[0].template.type % 2 == 0 && items[0].template.gender != items[1].template.gender)
                    || items[1].getExpiry() != -1) {
                player.addInfo(Player.INFO_RED, Language.ITEM_INVALID);
                return;
            }
            int[] upgrades = {items[0].getUpgrade(), items[1].getUpgrade()};
            if (upgrades[0] < 12 || upgrades[1] < 12) {
                player.addInfo(Player.INFO_RED, Language.ITEM_INVALID);
                return;
            }
            int level = Math.max(items[0].getParam(162), 1);
            int disLevel = items[1].template.levelRequire / 10 + 1;
            if (items[1].template.levelRequire >= 60 && items[1].template.levelRequire < 70) {
                disLevel = 6;
            }
            if (disLevel != level && disLevel != level + 1) {
                player.addInfo(Player.INFO_RED, Language.ITEM_INVALID);
                return;
            }
            StringBuilder content = new StringBuilder();
            content.append(String.format("Nâng cấp %s", items[0].template.name)).append("\n");
            content.append(String.format("Cần sử dụng x%d %s", level, items[2].template.name)).append("\n");
            content.append(String.format("Cần sử dụng x%d %s", level, items[3].template.name)).append("\n");
            content.append(String.format("Cần sử dụng x%d %s", level, items[4].template.name)).append("\n");
            content.append(String.format("Cần sử dụng x%d %s", 100 + 10 * level, items[5].template.name)).append("\n");
            content.append(String.format("Tỉ lệ thành công %d%%", PERCENT[level - 1])).append("\n");
            content.append("Sau khi làm phép thành công, các chỉ số mới bao gồm:").append("\n");
            Item newItem = items[1].cloneItem();
            int upgrade = newItem.getUpgrade();
            for (int i = 0; i < upgrade; i++) {
                newItem.downUpgrade();
            }
            for (ItemOption option : newItem.options) {
                if (option.template.id == 19) {
                    continue;
                }
                if (option.template.type == 0 || option.template.type == 1 || option.template.type == 2 || option.template.type == 5) {
                    content.append(option.toString()).append(", ");
                }
            }
            if (items[6] != null && items[6].getParam(163) == upgrades[0]) {
                content.append(String.format("Số sao và chỉ số hiệp sĩ được bảo toàn, trang bị vẫn giữ +%d", upgrades[0])).append("\n");
                content.append(String.format("%s chỉ mất khi làm phép thành công", items[6].template.name)).append("\n");
            } else {
                content.append("Số sao và chỉ số hiệp sĩ được bảo toàn, trang bị được đưa về +0").append("\n");
            }
            content.append(String.format("%s chỉ mất khi làm phép thành công", items[1].template.name)).append("\n");
            content.append(String.format("%s sẽ bị khóa sau khi làm phép (thất bại cũng sẽ bị khóa)", items[0].template.name)).append("\n");
            List<Command> commands = new ArrayList<>();
            commands.add(new Command(CommandName.CONFIRM_UPGRADE, String.format("Làm phép\n%s KC", Utils.currencyFormat(DIAMOND)), player,
                    items[0].indexUI, items[1].indexUI, items[2].indexUI, items[3].indexUI, items[4].indexUI, items[5].indexUI,
                    items[6] == null ? -1 : items[6].indexUI, items[7].indexUI));
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
            int[] index = new int[]{(int) objects[0], (int) objects[1], (int) objects[2], (int) objects[3], (int) objects[4],
                    (int) objects[5], (int) objects[6], (int) objects[7]};
            Item[] items = new Item[8];
            for (int i = 0; i < items.length; i++) {
                if (i == 6 && index[i] == -1) {
                    continue;
                }
                Item item = player.itemsBag[index[i]];
                if (item == null) {
                    player.addInfo(Player.INFO_RED, Language.ITEM_INVALID);
                    return;
                }
                if (item.template.id == ItemName.NGOC_RONG_3_SAO) {
                    items[2] = item;
                } else if (item.template.id == ItemName.DA_HAI_LAM) {
                    items[3] = item;
                } else if (item.template.id == ItemName.DA_10) {
                    items[4] = item;
                } else if (item.template.id == ItemName.TINH_THACH) {
                    items[5] = item;
                } else if (item.template.id == ItemName.THE_VAN_NANG) {
                    items[6] = item;
                } else if (item.isItemRider()) {
                    if (item.options.stream().anyMatch(o -> o.template.type == 7)) {
                        items[0] = item;
                    } else {
                        items[7] = item;
                    }
                } else if (item.template.type < 8) {
                    items[1] = item;
                }
            }
            if (items[0] == null || items[1] == null || items[2] == null || items[3] == null || items[4] == null || items[5] == null || items[7] == null
                    || items[0].template.type != items[1].template.type
                    || items[0].template.type != items[7].template.type
                    || (items[0].template.type % 2 == 0 && items[0].template.gender != items[1].template.gender)
                    || items[1].getExpiry() != -1) {
                player.addInfo(Player.INFO_RED, Language.ITEM_INVALID);
                return;
            }
            int[] upgrades = {items[0].getUpgrade(), items[1].getUpgrade()};
            if (upgrades[0] < 12 || upgrades[1] < 12) {
                player.addInfo(Player.INFO_RED, Language.ITEM_INVALID);
                return;
            }
            int level = Math.max(items[0].getParam(162), 1);
            int disLevel = items[1].template.levelRequire / 10 + 1;
            if (items[1].template.levelRequire >= 60 && items[1].template.levelRequire < 70) {
                disLevel = 6;
            }
            if (disLevel != level && disLevel != level + 1) {
                player.addInfo(Player.INFO_RED, Language.ITEM_INVALID);
                return;
            }
            if (items[2].quantity < level) {
                player.addInfo(Player.INFO_RED, String.format("Bạn không đủ %s", items[2].template.name));
                return;
            }
            if (items[3].quantity < level) {
                player.addInfo(Player.INFO_RED, String.format("Bạn không đủ %s", items[3].template.name));
                return;
            }
            if (items[4].quantity < level) {
                player.addInfo(Player.INFO_RED, String.format("Bạn không đủ %s", items[4].template.name));
                return;
            }
            boolean isKeep = false;
            if (items[6] != null) {
                int currentUpgrade = items[6].getParam(163);
                if (currentUpgrade != upgrades[0]) {
                    player.addInfo(Player.INFO_RED, String.format("%s và %s không tương thích về cấp cường hóa", items[0].template.name, items[6].template.name));
                    return;
                }
                if (upgrades[1] < 14) {
                    player.addInfo(Player.INFO_RED, String.format("Yêu cầu %s +14 trở lên", items[1].template.name));
                    return;
                }
                isKeep = true;
            }
            int quantity = 100 + 10 * level;
            if (items[5].quantity < quantity) {
                player.addInfo(Player.INFO_RED, String.format("Bạn không đủ %s", items[5].template.name));
                return;
            }
            if (player.diamond < DIAMOND) {
                player.addInfo(Player.INFO_RED, "Bạn không đủ kim cương");
                return;
            }
            player.upDiamond(-DIAMOND);
            items[2].quantity -= level;
            if (items[2].quantity <= 0) {
                player.itemsBag[items[2].indexUI] = null;
            }
            items[3].quantity -= level;
            if (items[3].quantity <= 0) {
                player.itemsBag[items[3].indexUI] = null;
            }
            items[4].quantity -= level;
            if (items[4].quantity <= 0) {
                player.itemsBag[items[4].indexUI] = null;
            }
            items[5].quantity -= quantity;
            if (items[5].quantity <= 0) {
                player.itemsBag[items[5].indexUI] = null;
            }
            player.itemsBag[items[7].indexUI] = null;
            boolean isUp = Utils.isPercent(PERCENT[level - 1]);
            if (isUp) {
                int upgrade = upgrades[0];
                for (int i = 0; i < upgrade; i++) {
                    items[0].downUpgrade();
                }
                Item newItem = items[1].cloneItem();
                upgrade = newItem.getUpgrade();
                for (int i = 0; i < upgrade; i++) {
                    newItem.downUpgrade();
                }
                HashMap<Integer, Integer> options = new HashMap<>();
                for (ItemOption option : newItem.options) {
                    if (option.template.id == 19) {
                        continue;
                    }
                    if (option.template.type == 7) {
                        option.param += Math.max(option.param / 5, 1);
                    } else if (option.template.type == 0 || option.template.type == 1 || option.template.type == 2 || option.template.type == 5) {
                        options.put(option.template.id, option.param);
                    }
                }
                for (ItemOption option : items[0].options) {
                    if (options.containsKey(option.template.id)) {
                        option.param = options.get(option.template.id);
                    }
                }
                ItemOption option = items[0].getOption(162);
                if (option == null) {
                    items[0].options.add(new ItemOption(162, disLevel));
                } else {
                    option.param = disLevel;
                }
                player.itemsBag[items[1].indexUI] = null;
                if (isKeep) {
                    upgrade = upgrades[0];
                    for (int i = 0; i < upgrade; i++) {
                        items[0].nextUpgrade();
                    }
                    items[6].quantity--;
                    if (items[6].quantity <= 0) {
                        player.itemsBag[items[6].indexUI] = null;
                    }
                }
                player.addInfo(Player.INFO_YELLOW, "Làm phép thành công");
            } else {
                ItemOption option = items[0].getOption(162);
                if (option == null) {
                    items[0].options.add(new ItemOption(162, level));
                }
                player.addInfo(Player.INFO_RED, "Làm phép thất bại");
            }
            items[0].isLock = true;
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
            if (item != null && (item.isItemRider()
                    || item.template.id == ItemName.NGOC_RONG_3_SAO || item.template.id == ItemName.DA_HAI_LAM
                    || item.template.id == ItemName.TINH_THACH || item.template.id == ItemName.DA_10
                    || item.template.id == ItemName.THE_VAN_NANG
                    || (item.template.type < 8 && item.getUpgrade() >= 12))) {
                flags[i] = true;
            }
        }
        return flags;
    }
}
