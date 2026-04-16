package com.beemobi.rongthanonline.upgrade;

import com.beemobi.rongthanonline.command.Command;
import com.beemobi.rongthanonline.command.CommandName;
import com.beemobi.rongthanonline.item.ItemType;
import com.beemobi.rongthanonline.npc.NpcName;
import com.beemobi.rongthanonline.entity.player.Player;
import com.beemobi.rongthanonline.item.Item;
import com.beemobi.rongthanonline.item.ItemName;
import com.beemobi.rongthanonline.item.ItemOption;
import com.beemobi.rongthanonline.network.Message;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

public class EnchantItem extends Upgrade {
    private static final Logger logger = Logger.getLogger(EnchantItem.class);

    public EnchantItem(UpgradeType type, String name, String command) {
        super(type, name, command);
        notes.add("- Vào túi đồ chọn trang bị cần ép Pha Lê");
        notes.add("- Trang bị đã được Pha Lê hóa có thể dùng Sao Pha Lê đã đánh bóng để cường hóa");
        notes.add("- Sau đó chọn Ép Pha Lê");
    }

    @Override
    public void upgrade(Message message, Player player) {
        player.lockAction.lock();
        try {
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
                if (item.isItemBody() && item.template.type != ItemType.TYPE_PET) {
                    if (items[0] == null) {
                        items[0] = item;
                    } else {
                        player.addInfo(Player.INFO_RED, "Chỉ được chọn duy nhất 1 trang bị");
                        return;
                    }
                } else if (item.template.id == ItemName.SAO_PHA_LE_DANH_BONG) {
                    if (items[1] == null) {
                        items[1] = item;
                    } else {
                        player.addInfo(Player.INFO_RED, "Chỉ có thể chọn duy nhất 1 loại Sao pha lê đã được đánh bóng");
                        return;
                    }
                } else {
                    player.addInfo(Player.INFO_RED, "Vật phẩm nâng cấp không hợp lệ");
                    return;
                }
            }
            if (items[0] == null) {
                player.addInfo(Player.INFO_RED, "Bạn chưa chọn trang bị");
                return;
            }
            if (items[0].template.type < 8 && items[0].getUpgrade() < 8) {
                player.addInfo(Player.INFO_RED, "Yêu cầu trang bị từ cấp 8 trở lên");
                return;
            }
            ItemOption[] options = new ItemOption[2];
            options[0] = items[0].getOption(67);
            if (options[0] == null) {
                player.addInfo(Player.INFO_RED, "Trang bị chưa được Pha lê hóa");
                return;
            }
            options[1] = items[0].getOption(68);
            if (options[1] != null && options[1].param >= options[0].param) {
                player.addInfo(Player.INFO_RED, "Trang bị không còn ô trống để ép pha lê");
                return;
            }
            if (items[1] == null) {
                player.addInfo(Player.INFO_RED, "Bạn chưa chọn Sao pha lê");
                return;
            }
            ItemOption optionCrystal = items[1].options.get(0);
            if (optionCrystal == null || optionCrystal.template.type != 4) {
                player.addInfo(Player.INFO_RED, "Sao pha lê đã bị hỏng");
                return;
            }
            int param = items[0].getParam(optionCrystal.template.id);
            List<Command> commands = new ArrayList<>();
            commands.add(new Command(CommandName.CONFIRM_UPGRADE, "Ép", player, items[0].indexUI, items[1].indexUI));
            commands.add(new Command(CommandName.CANCEL, "Hủy", player));
            String stringBuilder = "Sau khi ép:" + "\n" +
                    optionCrystal.template.name.replace("#", String.valueOf(param)) +
                    "\nthay đổi thành\n" +
                    optionCrystal.template.name.replace("#", String.valueOf(param + optionCrystal.param));
            player.createMenu(NpcName.ME, stringBuilder, commands);
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
            items[0] = player.itemsBag[index[0]];
            if (items[0] == null || !items[0].isItemBody() || items[0].template.type == ItemType.TYPE_PET) {
                player.addInfo(Player.INFO_RED, "Bạn chưa chọn trang bị");
                return;
            }
            if (items[0].template.type < 8 && items[0].getUpgrade() < 8) {
                player.addInfo(Player.INFO_RED, "Yêu cầu trang bị từ cấp 8 trở lên");
                return;
            }
            ItemOption[] options = new ItemOption[2];
            options[0] = items[0].getOption(67);
            if (options[0] == null) {
                player.addInfo(Player.INFO_RED, "Trang bị chưa được Pha lê hóa");
                return;
            }
            items[1] = player.itemsBag[index[1]];
            if (items[1] == null || items[1].template.id != ItemName.SAO_PHA_LE_DANH_BONG) {
                player.addInfo(Player.INFO_RED, "Bạn chưa chọn Sao pha lê");
                return;
            }
            options[1] = items[0].getOption(68);
            if (options[1] == null) {
                options[1] = new ItemOption(68, 0);
                items[0].options.add(options[1]);
            }
            if (options[1].param >= options[0].param) {
                player.addInfo(Player.INFO_RED, "Trang bị không còn ô trống để ép pha lê");
                return;
            }
            ItemOption optionCrystal = items[1].options.get(0);
            if (optionCrystal == null || optionCrystal.template.type != 4) {
                player.addInfo(Player.INFO_RED, "Sao pha lê đã bị hỏng");
                return;
            }
            ItemOption option = items[0].getOption(optionCrystal.template.id);
            if (option == null) {
                items[0].options.add(new ItemOption(optionCrystal.template.id, optionCrystal.param));
            } else {
                option.param += optionCrystal.param;
            }
            options[1].param++;
            player.service.refreshItemBag(index[0]);
            player.removeQuantityItemBag(index[1], 1);
            player.addInfo(Player.INFO_YELLOW, "Ép pha lê thành công");
        } catch (Exception ex) {
            logger.error("upgrade", ex);
        } finally {
            player.lockAction.unlock();
        }
    }

    @Override
    public boolean[] getIndexCanUpgrade(Player player) {
        boolean[] flags = new boolean[player.itemsBag.length];
        for (int i = 0; i < flags.length; i++) {
            Item item = player.itemsBag[i];
            if (item != null && ((item.isItemBody() && item.template.type != ItemType.TYPE_PET && item.getParam(68) < item.getParam(67)) || item.template.id == ItemName.SAO_PHA_LE_DANH_BONG)) {
                flags[i] = true;
            }
        }
        return flags;
    }
}
