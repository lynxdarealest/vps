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
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GhepDeoLung extends Upgrade {
    private static final Logger logger = Logger.getLogger(GhepDeoLung.class);

    public GhepDeoLung(UpgradeType type, String name, String command) {
        super(type, name, command);
        notes.add("- Vào túi đồ chọn các Vật phẩm đeo lưng muốn ghép");
        notes.add("- Sau đó chọn Ghép");
        notes.add("(!) Lưu ý:");
        notes.add("- Vật phẩm đeo lưng yêu cầu không có hạn sử dụng");
        notes.add("- Tất cả chỉ số của Vật phẩm đeo lưng sẽ được cộng dồn vào Vật phẩm đeo lưng bỏ vào đầu tiên");
        notes.add("- Vật phẩm đeo lưng sau khi ghép vẫn có thể tách ngược lại");
        notes.add("- Số sao của Vật phẩm đeo lưng sau khi ép sẽ theo trang bị có nhiều sao nhất");
        notes.add("- Chỉ được chọn các Vật phẩm đeo lưng chưa được ghép");
        notes.add("- Có thể chọn thêm Ngọc Rồng 2 sao để giảm Kim cương tiêu tốn");
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
            if (size < 2 || size > 50) {
                player.addInfo(Player.INFO_RED, "Số lượng vật phẩm không hợp lệ");
                return;
            }
            Item[] items = new Item[size];
            ArrayList<Integer> values = new ArrayList<>();
            ArrayList<Integer> duplicates = new ArrayList<>();
            for (int i = 0; i < size; i++) {
                int index = message.reader().readByte();
                if (index < 0 || index >= player.itemsBag.length) {
                    return;
                }
                if (values.contains(index)) {
                    return;
                }
                values.add(index);
                Item item = player.itemsBag[index];
                if (!isValidItem(item, player) || duplicates.contains(item.template.id)) {
                    player.addInfo(Player.INFO_RED, "Vật phẩm không hợp lệ");
                    return;
                }
                duplicates.add(item.template.id);
                items[i] = item;
            }
            if (items[0].template.id == ItemName.NGOC_RONG_2_SAO) {
                player.addInfo(Player.INFO_RED, "Ngọc rồng chỉ được cho vào cuối cùng");
                return;
            }
            StringBuilder content = new StringBuilder();
            content.append(String.format("Cường hóa %s", items[1].template.name)).append("\n");
            if (Arrays.stream(items).anyMatch(i -> i.template.id == ItemName.NGOC_RONG_2_SAO)) {
                if (size <= 2) {
                    player.addInfo(Player.INFO_RED, "Số lượng vật phẩm không hợp lệ");
                    return;
                }
                content.append(String.format("Tiêu tốn %d Kim cương", 50 * (size - 1))).append("\n");
                content.append(String.format("Cần %d Ngọc rồng 2 sao", size - 1)).append("\n");
            } else {
                content.append(String.format("Tiêu tốn %d Kim cương", 200 * size)).append("\n");
            }
            content.append("Tỉ lệ thành công 100%").append("\n");
            List<Command> commands = new ArrayList<>();
            commands.add(new Command(CommandName.CONFIRM_UPGRADE, "OK", player, values));
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
            ArrayList<Integer> values = (ArrayList<Integer>) objects[0];
            ArrayList<Integer> duplicates = new ArrayList<>();
            Item[] items = new Item[values.size()];
            for (int i = 0; i < values.size(); i++) {
                int index = values.get(i);
                if (index < 0 || index >= player.itemsBag.length) {
                    return;
                }
                Item item = player.itemsBag[index];
                if (!isValidItem(item, player) || duplicates.contains(item.template.id)) {
                    player.addInfo(Player.INFO_RED, "Vật phẩm không hợp lệ");
                    return;
                }
                duplicates.add(item.template.id);
                items[i] = item;
            }
            if (items[0].template.id == ItemName.NGOC_RONG_2_SAO) {
                player.addInfo(Player.INFO_RED, "Ngọc rồng chỉ được cho vào cuối cùng");
                return;
            }
            int diamond = 200 * (values.size());
            Item dragon = Arrays.stream(items).filter(i -> i.template.id == ItemName.NGOC_RONG_2_SAO).findFirst().orElse(null);
            if (dragon != null) {
                if (dragon.quantity < values.size() - 1) {
                    player.addInfo(Player.INFO_RED, "Cần ít nhất " + (values.size() - 1) + " " + dragon.template.name);
                    return;
                }
                diamond = 50 * (values.size() - 1);
            }
            if (player.diamond < diamond) {
                player.addInfo(Player.INFO_RED, "Bạn không có đủ Kim cương");
                return;
            }
            if (dragon != null) {
                dragon.quantity -= (values.size() - 1);
                if (dragon.quantity <= 0) {
                    player.itemsBag[dragon.indexUI] = null;
                }
            }
            player.upDiamond(-diamond);
            int maxStar = items[0].getParam(67);
            for (int i = 1; i < values.size(); i++) {
                if (items[i].template.id == ItemName.NGOC_RONG_2_SAO) {
                    continue;
                }
                items[0].options.add(new ItemOption(176, items[i].template.id));
                for (ItemOption option : items[i].options) {
                    if (option.template.id == 67) {
                        maxStar = Math.max(maxStar, option.param);
                    } else if (option.template.id != 68 && option.template.type != 4) {
                        items[0].options.add(new ItemOption(option.template, option.param));
                    }
                }
                player.itemsBag[values.get(i)] = null;
            }
            items[0].options.removeIf(o -> o.template.type == 4 || o.template.id == 68 || o.template.id == 67);
            if (maxStar > 0) {
                items[0].options.add(new ItemOption(67, maxStar));
                items[0].options.add(new ItemOption(68, 0));
            }
            player.service.setItemBag();
            player.addInfo(Player.INFO_YELLOW, "Làm phép thành công");
            player.service.showTab(-1);
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
            if (isValidItem(item, player)) {
                flags[i] = true;
            }
        }
        return flags;
    }

    public boolean isValidItem(Item item, Player player) {
        return item != null && ((item.template.type == ItemType.TYPE_BAG && (item.template.gender == player.gender || item.template.gender == -1) && item.getExpiry() == -1
                && item.options.stream().allMatch(o -> o.template.id != 176 && o.template.id != 105 && o.template.id != 117)) || item.template.id == ItemName.NGOC_RONG_2_SAO);
    }
}
