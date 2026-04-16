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

public class PackAvatar extends Upgrade {
    private static final Logger logger = Logger.getLogger(PackAvatar.class);

    private static final int DIAMOND = 100;

    public PackAvatar(UpgradeType type, String name, String command) {
        super(type, name, command);
        notes.add("- Vào túi đồ chọn Cải trang hoặc Avatar cần ghép thêm chỉ số");
        notes.add("- Chọn Cải trang hoặc Avatar muốn dịch chuyển chỉ số");
        notes.add("- Chọn Tinh thạch");
        notes.add("- Chọn Ngọc rồng 2 sao");
        notes.add("- Trang bị yêu cầu không có hạn sử dụng");
        notes.add("- Sau đó chọn Làm phép");
        notes.add("- Sau khi ghép, Cải trang hoặc Avatar ban đầu sẽ có thêm 1 vài chỉ số của trang bị muốn dịch chuyển");
        notes.add("- Lưu ý: không thể dịch chuyển chỉ số đã có, 1 vài dòng chỉ số sẽ không thể dịch chuyển");
    }

    @Override
    public void upgrade(Message message, Player player) {
        player.lockAction.lock();
        try {
            int size = message.reader().readByte();
            if (size != 4) {
                player.addInfo(Player.INFO_RED, Language.ITEM_INVALID);
                return;
            }
            Item[] items = new Item[4];
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
                if (item.template.type == ItemType.TYPE_AVATAR &&
                        item.options.stream().allMatch(this::isOptionValid)
                        && item.getExpiry() == -1) {
                    if (items[0] == null) {
                        items[0] = item;
                    } else {
                        items[1] = item;
                    }
                } else if (item.template.id == ItemName.TINH_THACH) {
                    items[2] = item;
                } else if (item.template.id == ItemName.NGOC_RONG_2_SAO) {
                    items[3] = item;
                }
            }
            if (items[0] == null || items[1] == null || items[2] == null || items[3] == null) {
                player.addInfo(Player.INFO_RED, Language.ITEM_INVALID);
                return;
            }
            ArrayList<Integer> options = new ArrayList<>();
            for (ItemOption itemOption : items[0].options) {
                options.add(itemOption.template.id);
            }
            ArrayList<ItemOption> itemOptions = new ArrayList<>();
            for (ItemOption itemOption : items[1].options) {
                if (isOptionCanMerge(itemOption) && !options.contains(itemOption.template.id)) {
                    options.add(itemOption.template.id);
                    itemOptions.add(itemOption);
                }
            }
            if (itemOptions.isEmpty()) {
                player.addInfo(Player.INFO_RED, "Không có chỉ số nào cần ghép");
                return;
            }
            StringBuilder content = new StringBuilder();
            content.append(String.format("Ghép %s", items[0].template.name)).append("\n");
            content.append(String.format("Cần sử dụng x%d %s", 100 * itemOptions.size(), items[2].template.name)).append("\n");
            content.append(String.format("Cần sử dụng x%d %s", itemOptions.size(), items[3].template.name)).append("\n");
            content.append("Tỉ lệ thành công 50%").append("\n");
            content.append("Sau khi làm phép thành công, các chỉ số mới bao gồm:").append("\n");
            for (ItemOption option : itemOptions) {
                content.append(option.toString()).append("\n");
            }
            content.append("Thất bại sẽ không mất Cải trang dịch chuyển").append("\n");
            List<Command> commands = new ArrayList<>();
            commands.add(new Command(CommandName.CONFIRM_UPGRADE, String.format("Làm phép\n%s KC", Utils.currencyFormat((long) DIAMOND * itemOptions.size())), player,
                    items[0].indexUI, items[1].indexUI, items[2].indexUI, items[3].indexUI));
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
            int[] index = new int[]{(int) objects[0], (int) objects[1], (int) objects[2], (int) objects[3]};
            Item[] items = new Item[4];
            for (int i = 0; i < items.length; i++) {
                Item item = player.itemsBag[index[i]];
                if (item == null) {
                    player.addInfo(Player.INFO_RED, Language.ITEM_INVALID);
                    return;
                }
                if (item.template.type == ItemType.TYPE_AVATAR &&
                        item.options.stream().allMatch(this::isOptionValid)
                        && item.getExpiry() == -1) {
                    if (items[0] == null) {
                        items[0] = item;
                    } else {
                        items[1] = item;
                    }
                } else if (item.template.id == ItemName.TINH_THACH) {
                    items[2] = item;
                } else if (item.template.id == ItemName.NGOC_RONG_2_SAO) {
                    items[3] = item;
                }
            }
            if (items[0] == null || items[1] == null || items[2] == null || items[3] == null) {
                player.addInfo(Player.INFO_RED, Language.ITEM_INVALID);
                return;
            }
            ArrayList<Integer> options = new ArrayList<>();
            for (ItemOption itemOption : items[0].options) {
                options.add(itemOption.template.id);
            }
            ArrayList<ItemOption> itemOptions = new ArrayList<>();
            for (ItemOption itemOption : items[1].options) {
                if (isOptionCanMerge(itemOption) && !options.contains(itemOption.template.id)) {
                    options.add(itemOption.template.id);
                    itemOptions.add(itemOption);
                }
            }
            if (itemOptions.isEmpty()) {
                player.addInfo(Player.INFO_RED, "Không có chỉ số nào cần ghép");
                return;
            }
            int quantity = 100 * itemOptions.size();
            if (items[2].quantity < quantity) {
                player.addInfo(Player.INFO_RED, String.format("Bạn còn thiếu %d %s", quantity - items[2].quantity, items[2].template.name));
                return;
            }
            if (items[3].quantity < itemOptions.size()) {
                player.addInfo(Player.INFO_RED, String.format("Bạn còn thiếu %d %s", quantity - items[3].quantity, items[3].template.name));
                return;
            }
            int diamond = DIAMOND * itemOptions.size();
            if (player.diamond < diamond) {
                player.addInfo(Player.INFO_RED, "Bạn không có đủ kim cương");
                return;
            }
            player.upDiamond(-diamond);
            items[2].quantity -= quantity;
            if (items[2].quantity <= 0) {
                player.itemsBag[items[2].indexUI] = null;
            }
            items[3].quantity -= itemOptions.size();
            if (items[3].quantity <= 0) {
                player.itemsBag[items[3].indexUI] = null;
            }
            boolean isUp = Utils.isPercent(50);
            if (isUp) {
                player.itemsBag[items[1].indexUI] = null;
                for (ItemOption option : itemOptions) {
                    items[0].options.add(new ItemOption(option.template, option.param));
                }
                player.addInfo(Player.INFO_YELLOW, "Làm phép thành công");
            } else {
                player.addInfo(Player.INFO_YELLOW, "Làm phép thất bại");
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
            if (isValidItem(item)) {
                flags[i] = true;
            }
        }
        return flags;
    }

    public boolean isValidItem(Item item) {
        return item != null && ((item.template.type == ItemType.TYPE_AVATAR && item.getExpiry() == -1 && item.options.stream().allMatch(this::isOptionValid))
                || item.template.id == ItemName.TINH_THACH || item.template.id == ItemName.NGOC_RONG_2_SAO);
    }

    public boolean isOptionValid(ItemOption option) {
        return option.template.id != 105
                && option.template.id != 117
                && option.template.id != 160;
    }

    public boolean isOptionCanMerge(ItemOption option) {
        return (option.template.type == 0 || option.template.type == 3)
                && option.template.id != 67 && option.template.id != 68 && isOptionValid(option);
    }
}
