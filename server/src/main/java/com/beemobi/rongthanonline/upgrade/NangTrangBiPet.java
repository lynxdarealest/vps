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
import com.beemobi.rongthanonline.shop.TypePrice;
import com.beemobi.rongthanonline.util.Utils;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class NangTrangBiPet extends Upgrade {
    private static final Logger logger = Logger.getLogger(NangTrangBiPet.class);

    public NangTrangBiPet(UpgradeType type, String name, String command) {
        super(type, name, command);
        notes.add("- Vào túi đồ chọn Trang bị Pet");
        notes.add("- Chọn Trấn Long Thạch");
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
            Item[] items = new Item[size];
            ArrayList<Integer> duplicates = new ArrayList<>();
            for (int i = 0; i < size; i++) {
                int index = message.reader().readByte();
                if (index < 0 || index >= player.itemsBag.length) {
                    return;
                }
                Item item = player.itemsBag[index];
                if (item == null) {
                    player.addInfo(Player.INFO_RED, "Vật phẩm không hợp lệ");
                    return;
                }
                if (duplicates.contains(index)) {
                    player.addInfo(Player.INFO_RED, "Vật phẩm không hợp lệ");
                    return;
                }
                duplicates.add(index);
                if (item.template.isPet) {
                    items[0] = item;
                } else if (item.template.id == ItemName.TRAN_LONG_THACH) {
                    items[1] = item;
                }
            }
            if (Arrays.stream(items).anyMatch(Objects::isNull)) {
                player.addInfo(Player.INFO_RED, "Vật phẩm không hợp lệ");
                return;
            }
            int upgrade = items[0].getUpgrade();
            if (upgrade >= 19) {
                player.addInfo(Player.INFO_RED, "Vật phẩm đã đạt cấp tối đa");
                return;
            }
            StringBuilder content = new StringBuilder();
            content.append("Nâng cấp " + items[0].template.name + " +" + (upgrade + 1)).append("\n");
            content.append("Cần sử dụng " + ((upgrade + 1) * 100) + " " + items[1].template.name).append("\n");
            content.append("Tỉ lệ thành công 100%").append("\n");
            List<Command> commands = new ArrayList<>();
            long coin = (upgrade + 1) * 500_000_000L;
            commands.add(new Command(CommandName.CONFIRM_UPGRADE, "Làm phép\n" + Utils.formatNumber(coin) + " xu", player, 0, items[0].indexUI, items[1].indexUI));
            commands.add(new Command(CommandName.CONFIRM_UPGRADE, "Làm phép\n" + Utils.formatNumber(coin * 2) + " xu khóa", player, 1, items[0].indexUI, items[1].indexUI));
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
            Item[] items = new Item[2];
            ArrayList<Integer> duplicates = new ArrayList<>();
            int[] indexes = new int[]{(int) objects[1], (int) objects[2]};
            for (int i = 0; i < indexes.length; i++) {
                int index = indexes[i];
                Item item = player.itemsBag[index];
                if (item == null) {
                    player.addInfo(Player.INFO_RED, "Vật phẩm không hợp lệ");
                    return;
                }
                if (duplicates.contains(index)) {
                    player.addInfo(Player.INFO_RED, "Vật phẩm không hợp lệ");
                    return;
                }
                duplicates.add(index);
                if (item.template.isPet) {
                    items[0] = item;
                } else if (item.template.id == ItemName.TRAN_LONG_THACH) {
                    items[1] = item;
                }
            }
            if (Arrays.stream(items).anyMatch(Objects::isNull)) {
                player.addInfo(Player.INFO_RED, "Vật phẩm không hợp lệ");
                return;
            }
            int upgrade = items[0].getUpgrade();
            if (upgrade >= 19) {
                player.addInfo(Player.INFO_RED, "Vật phẩm đã đạt cấp tối đa");
                return;
            }
            int quantity = (upgrade + 1) * 100;
            if (items[1].quantity < quantity) {
                player.addInfo(Player.INFO_RED, "Số lượng " + items[1].template.name + " không đủ");
                return;
            }
            long coin = (upgrade + 1) * 500_000_000L;
            if ((int) objects[0] == 0) {
                if (!player.isEnoughMoney(TypePrice.COIN, coin)) {
                    return;
                }
                player.downMoney(TypePrice.COIN, coin);
            } else {
                coin *= 2;
                if (!player.isEnoughMoney(TypePrice.COIN_LOCK, coin)) {
                    return;
                }
                player.downMoney(TypePrice.COIN_LOCK, coin);
            }
            items[1].quantity -= quantity;
            if (items[1].quantity < 1) {
                player.itemsBag[items[1].indexUI] = null;
            }
            for (ItemOption option : items[0].options) {
                int id = option.template.id;
                if (id == 19) {
                    option.param++;
                }
                if (id == 0 || id == 1 || id == 2 || id == 3 || id == 4 || id == 5 || id == 6 || id == 25 || id == 26 || id == 27 || id == 28 || id == 29 || id == 30 || id == 31 || id == 32 || id == 99) {
                    option.param = option.param * 11 / 10;
                }
            }
            if (upgrade == 0) {
                items[0].options.add(new ItemOption(19, 1));
            }
            player.addInfo(Player.INFO_YELLOW, "Làm phép thành công");
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
            if (item != null && (item.template.id == ItemName.TRAN_LONG_THACH || item.template.isPet)) {
                flags[i] = true;
            }
        }
        return flags;
    }
}
