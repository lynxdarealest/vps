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
import com.beemobi.rongthanonline.util.Utils;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class RemoveOption extends Upgrade {
    private static final Logger logger = Logger.getLogger(RemoveOption.class);

    public RemoveOption(UpgradeType type, String name, String command) {
        super(type, name, command);
        notes.add("- Vào túi đồ chọn cải trang hoặc thú cưỡi cần xóa chỉ số");
        notes.add("- Chọn Ngọc rồng 2 sao");
        notes.add("- Chọn Bùa tẩy chỉ số");
        notes.add("- Sau đó chọn Làm phép");
        notes.add("* Lưu ý: không thể làm phép với vật phẩm có hạn sử dụng");
    }

    @Override
    public void upgrade(Message message, Player player) {
        player.lockAction.lock();
        try {
            int size = message.reader().readByte();
            if (size != 3) {
                player.addInfo(Player.INFO_RED, Language.ITEM_INVALID);
                return;
            }
            Item[] items = new Item[3];
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
                if (item.template.type == ItemType.TYPE_AVATAR || item.template.type == ItemType.TYPE_THU_CUOI) {
                    if (item.getExpiry() == -1) {
                        items[0] = item;
                    }
                } else if (item.template.id == ItemName.BUA_TAY_CHI_SO) {
                    items[2] = item;
                } else if (item.template.id == ItemName.NGOC_RONG_2_SAO) {
                    items[1] = item;
                }
            }
            if (items[0] == null || items[1] == null || items[2] == null) {
                player.addInfo(Player.INFO_RED, Language.ITEM_INVALID);
                return;
            }
            StringBuilder content = new StringBuilder();
            content.append(String.format("Xóa chỉ số %s", items[0].template.name)).append("\n");
            content.append(String.format("Cần sử dụng x1 %s", items[1].template.name)).append("\n");
            content.append(String.format("Cần sử dụng x10 %s", items[2].template.name)).append("\n");
            content.append("Tiêu tốn 100 Kim cương").append("\n");
            content.append("Hãy chọn chỉ số bạn muốn xóa bỏ (không thể khôi phục):");
            List<Command> commands = new ArrayList<>();
            commands.add(new Command(CommandName.CANCEL, "Từ chối", player));
            for (ItemOption option : items[0].options) {
                if (isCanRemove(option)) {
                    commands.add(new Command(CommandName.CONFIRM_UPGRADE, String.format("Xóa %s", option.toString()), player,
                            items[0].indexUI, items[1].indexUI, items[2].indexUI, option.template.id));
                }
            }
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
            int[] index = new int[]{(int) objects[0], (int) objects[1], (int) objects[2]};
            Item[] items = new Item[3];
            for (int i : index) {
                Item item = player.itemsBag[i];
                if (item == null) {
                    player.addInfo(Player.INFO_RED, Language.ITEM_INVALID);
                    return;
                }
                if (item.template.type == ItemType.TYPE_AVATAR || item.template.type == ItemType.TYPE_THU_CUOI) {
                    if (item.getExpiry() == -1) {
                        items[0] = item;
                    }
                } else if (item.template.id == ItemName.BUA_TAY_CHI_SO) {
                    items[2] = item;
                } else if (item.template.id == ItemName.NGOC_RONG_2_SAO) {
                    items[1] = item;
                }
            }
            if (items[0] == null || items[1] == null || items[2] == null) {
                player.addInfo(Player.INFO_RED, Language.ITEM_INVALID);
                return;
            }
            int optionID = (int) objects[3];
            ItemOption option = items[0].options.stream().filter(o -> o.template.id == optionID).findFirst().orElse(null);
            if (option == null) {
                return;
            }
            if (!isCanRemove(option)) {
                player.addInfo(Player.INFO_RED, String.format("Không thể xóa %s", option.toString()));
                return;
            }
            if (items[1].quantity < 1) {
                player.addInfo(Player.INFO_RED, String.format("Bạn không đủ %s", items[1].template.name));
                return;
            }
            if (items[2].quantity < 10) {
                player.addInfo(Player.INFO_RED, String.format("Bạn không đủ %s", items[2].template.name));
                return;
            }
            if (!player.isEnoughMoney(TypePrice.DIAMOND, 100)) {
                return;
            }
            player.downMoney(TypePrice.DIAMOND, 100);
            items[1].quantity -= 1;
            if (items[1].quantity <= 0) {
                player.itemsBag[items[1].indexUI] = null;
            }
            items[2].quantity -= 10;
            if (items[2].quantity <= 0) {
                player.itemsBag[items[2].indexUI] = null;
            }
            items[0].options.remove(option);
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
            if (item != null && (((item.template.type == ItemType.TYPE_AVATAR || item.template.type == ItemType.TYPE_THU_CUOI) && item.getExpiry() == -1)
                    || item.template.id == ItemName.NGOC_RONG_2_SAO || item.template.id == ItemName.BUA_TAY_CHI_SO)) {
                flags[i] = true;
            }
        }
        return flags;
    }

    public boolean isCanRemove(ItemOption option) {
        return (option.template.type == 0 || option.template.type == 3) && option.template.id != 67 && option.template.id != 68;
    }
}

