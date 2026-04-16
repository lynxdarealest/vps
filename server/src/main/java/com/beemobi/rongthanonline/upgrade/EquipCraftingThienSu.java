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

public class EquipCraftingThienSu extends Upgrade {
    private static final Logger logger = Logger.getLogger(EquipCraftingThienSu.class);

    public EquipCraftingThienSu(UpgradeType type, String name, String command) {
        super(type, name, command);
        notes.add("- Vào túi đồ chọn trang bị Thần linh +12 trở lên");
        notes.add("- Chọn Ngọc rồng 3 sao");
        notes.add("- Chọn Đá cấp 12");
        notes.add("- Chọn Tam Linh Thạch");
        notes.add("- Chọn Tinh thạch");
        notes.add("- Sau đó chọn Làm phép");
        notes.add("- Trang bị Thiên sứ nhận được sẽ khóa và mạnh hơn 40-50% so với trang bị Thần linh");
        notes.add("- Số sao pha lê sẽ được bảo toàn");
        notes.add("* Lưu ý: trang bị Thần linh từ +14 trở lên sẽ có chức năng bảo toàn cấp cường hóa nếu sử dụng thêm Thẻ vạn năng");
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
            if (size != 5 && size != 6) {
                player.addInfo(Player.INFO_RED, Language.ITEM_INVALID);
                return;
            }
            Item[] items = new Item[6];
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
                    items[1] = item;
                } else if (item.template.id == ItemName.TAM_LINH_THACH) {
                    items[2] = item;
                } else if (item.template.id == ItemName.DA_12) {
                    items[3] = item;
                } else if (item.template.id == ItemName.TINH_THACH) {
                    items[4] = item;
                } else if (item.template.id == ItemName.THE_VAN_NANG) {
                    items[5] = item;
                } else if (item.isThanLinh() && item.getUpgrade() >= 12) {
                    items[0] = item;
                }
            }
            if (items[0] == null || items[1] == null || items[2] == null || items[3] == null || items[4] == null) {
                player.addInfo(Player.INFO_RED, Language.ITEM_INVALID);
                return;
            }
            ItemTemplate template = ItemManager.getInstance().itemTemplates.values()
                    .stream()
                    .filter(i -> i.type == items[0].template.type && i.levelRequire == 85 && (i.gender == items[0].template.gender || (items[0].template.gender == -1 && i.gender == player.gender)))
                    .findFirst()
                    .orElse(null);
            if (template == null) {
                player.addInfo(Player.INFO_RED, Language.ITEM_INVALID);
                return;
            }
            StringBuilder content = new StringBuilder();
            content.append(String.format("Chế tạo %s", template.name));
            switch (template.gender) {
                case 0:
                    content.append(" (Trái đất)").append("\n");
                    break;
                case 1:
                    content.append(" (Namek)").append("\n");
                    break;
                case 2:
                    content.append(" (Saiyan)").append("\n");
                    break;
            }
            content.append(String.format("Cần sử dụng %d %s", 1, items[1].template.name)).append("\n");
            content.append(String.format("Cần sử dụng %d %s", 100, items[2].template.name)).append("\n");
            content.append(String.format("Cần sử dụng %d %s", 1, items[3].template.name)).append("\n");
            content.append(String.format("Cần sử dụng %d %s", 100, items[4].template.name)).append("\n");
            content.append("Tỉ lệ thành công 20%").append("\n");
            int upgrade = items[0].getUpgrade();
            if (upgrade >= 14 && items[5] != null && upgrade <= items[5].getParam(163)) {
                content.append("Cấp trang bị sẽ được bảo toàn").append("\n");
            } else {
                content.append("Cấp trang bị không được bảo toàn").append("\n");
            }
            if (items[5] != null) {
                content.append(String.format("%s và %s chỉ mất khi làm phép thành công", items[0].template.name, items[5].template.name)).append("\n");
            } else {
                content.append(String.format("%s chỉ mất khi làm phép thành công", items[0].template.name)).append("\n");
            }
            List<Command> commands = new ArrayList<>();
            commands.add(new Command(CommandName.CONFIRM_UPGRADE, "Làm phép\n10tr xu", player,
                    items[0].indexUI, items[1].indexUI, items[2].indexUI, items[3].indexUI, items[4].indexUI,
                    items[5] == null ? -1 : items[5].indexUI));
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
            int[] index = new int[]{(int) objects[0], (int) objects[1], (int) objects[2], (int) objects[3], (int) objects[4], (int) objects[5]};
            Item[] items = new Item[6];
            for (int i = 0; i < items.length; i++) {
                if (i == 5 && index[i] == -1) {
                    continue;
                }
                Item item = player.itemsBag[index[i]];
                if (item == null) {
                    player.addInfo(Player.INFO_RED, Language.ITEM_INVALID);
                    return;
                }
                if (item.template.id == ItemName.NGOC_RONG_3_SAO) {
                    items[1] = item;
                } else if (item.template.id == ItemName.TAM_LINH_THACH) {
                    items[2] = item;
                } else if (item.template.id == ItemName.DA_12) {
                    items[3] = item;
                } else if (item.template.id == ItemName.TINH_THACH) {
                    items[4] = item;
                } else if (item.template.id == ItemName.THE_VAN_NANG) {
                    items[5] = item;
                } else if (item.isThanLinh() && item.getUpgrade() >= 12) {
                    items[0] = item;
                }
            }
            if (items[0] == null || items[1] == null || items[2] == null || items[3] == null || items[4] == null) {
                player.addInfo(Player.INFO_RED, Language.ITEM_INVALID);
                return;
            }
            ItemTemplate template = ItemManager.getInstance().itemTemplates.values()
                    .stream()
                    .filter(i -> i.type == items[0].template.type && i.levelRequire == 85 && (i.gender == items[0].template.gender || (items[0].template.gender == -1 && i.gender == player.gender)))
                    .findFirst()
                    .orElse(null);
            if (template == null) {
                player.addInfo(Player.INFO_RED, Language.ITEM_INVALID);
                return;
            }
            if (items[1].quantity < 1) {
                player.addInfo(Player.INFO_RED, String.format("Bạn không đủ %s", items[2].template.name));
                return;
            }
            if (items[2].quantity < 100) {
                player.addInfo(Player.INFO_RED, String.format("Bạn không đủ %s", items[2].template.name));
                return;
            }
            if (items[3].quantity < 1) {
                player.addInfo(Player.INFO_RED, String.format("Bạn không đủ %s", items[3].template.name));
                return;
            }
            if (items[4].quantity < 100) {
                player.addInfo(Player.INFO_RED, String.format("Bạn không đủ %s", items[4].template.name));
                return;
            }
            int upgrade = items[0].getUpgrade();
            boolean isKeep = false;
            if (items[5] != null) {
                int currentUpgrade = items[5].getParam(163);
                if (currentUpgrade != upgrade) {
                    player.addInfo(Player.INFO_RED, String.format("%s và %s không tương thích về cấp cường hóa",
                            items[0].template.name, items[5].template.name));
                    return;
                }
                if (upgrade < 14) {
                    player.addInfo(Player.INFO_RED, String.format("Yêu cầu %s +14 trở lên", items[0].template.name));
                    return;
                }
                isKeep = true;
            }
            if (player.xu < 10000000) {
                player.addInfo(Player.INFO_RED, "Bạn không đủ xu");
                return;
            }
            player.upXu(-10000000);
            items[1].quantity--;
            if (items[1].quantity <= 0) {
                player.itemsBag[items[1].indexUI] = null;
            }
            items[2].quantity -= 100;
            if (items[2].quantity <= 0) {
                player.itemsBag[items[2].indexUI] = null;
            }
            items[3].quantity--;
            if (items[3].quantity <= 0) {
                player.itemsBag[items[3].indexUI] = null;
            }
            items[4].quantity -= 100;
            if (items[4].quantity <= 0) {
                player.itemsBag[items[4].indexUI] = null;
            }
            boolean isUp = Utils.isPercent(20);
            if (isUp) {
                Item newItem = ItemManager.getInstance().createItem(template.id, 1, false);
                for (ItemOption option : items[0].options) {
                    if (option.template.type == 4 || option.template.id == 68) {
                        continue;
                    }
                    if (option.template.id == 19 || option.template.id == 67) {
                        newItem.options.add(new ItemOption(option.template, option.param));
                    } else {
                        newItem.options.add(new ItemOption(option.template,
                                option.param + (int) ((float) option.param * Utils.nextInt(40, 50) / 100f)));
                    }
                }
                if (isKeep) {
                    items[5].quantity--;
                    if (items[5].quantity <= 0) {
                        player.itemsBag[items[5].indexUI] = null;
                    }
                } else {
                    for (int i = 0; i < upgrade; i++) {
                        newItem.downUpgrade();
                    }
                }
                newItem.isLock = true;
                newItem.indexUI = items[0].indexUI;
                player.itemsBag[newItem.indexUI] = newItem;
                player.addInfo(Player.INFO_YELLOW, "Làm phép thành công");
            } else {
                player.addInfo(Player.INFO_RED, "Làm phép thất bại");
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
            if (item != null && (item.template.id == ItemName.NGOC_RONG_3_SAO
                    || item.template.id == ItemName.TAM_LINH_THACH
                    || item.template.id == ItemName.TINH_THACH || item.template.id == ItemName.DA_12
                    || item.template.id == ItemName.THE_VAN_NANG
                    || (item.isThanLinh() && item.getUpgrade() >= 12))) {
                flags[i] = true;
            }
        }
        return flags;
    }
}
