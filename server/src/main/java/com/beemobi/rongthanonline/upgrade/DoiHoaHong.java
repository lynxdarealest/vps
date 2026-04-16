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
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

public class DoiHoaHong extends Upgrade {
    private static final Logger logger = Logger.getLogger(DoiHoaHong.class);

    public static final int[] QUANTITY = {10, 30, 100, 200, 500, 1500, 7000, 20000, 50000, 100000};

    public DoiHoaHong(UpgradeType type, String name, String command) {
        super(type, name, command);
        notes.add("- Vào túi đồ chọn Trang bị 4x 1 sao trở lên");
        notes.add("- Sau đó chọn Làm phép");
        notes.add("* Lưu ý: số hoa hồng nhận được sẽ tỉ lệ với số sao trang bị");
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
            if (size < 0 || size > 30) {
                player.addInfo(Player.INFO_RED, Language.ITEM_INVALID);
                return;
            }
            ArrayList<Integer> values = new ArrayList<>();
            Item[] items = new Item[size];
            for (int i = 0; i < items.length; i++) {
                int index = message.reader().readByte();
                if (index < 0 || index >= player.itemsBag.length) {
                    return;
                }
                if (values.contains(index)) {
                    return;
                }
                values.add(index);
                Item item = player.itemsBag[index];
                if (item == null || item.template.type >= 8 || item.template.levelRequire / 10 != 4 || item.getMaxStar() == 0) {
                    player.addInfo(Player.INFO_RED, Language.ITEM_INVALID);
                    return;
                }
                items[i] = item;
            }
            int num = 0;
            for (Item item : items) {
                num += QUANTITY[item.getMaxStar() - 1] * 3;
            }
            List<Command> commands = new ArrayList<>();
            commands.add(new Command(CommandName.CONFIRM_UPGRADE, "Làm phép", player, values));
            commands.add(new Command(CommandName.CANCEL, "Từ chối", player));
            player.createMenu(NpcName.ME, "Sau khi làm phép sẽ nhận được " + num + " hoa hồng", commands);
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
            Item[] items = new Item[values.size()];
            int num = 0;
            for (int i = 0; i < values.size(); i++) {
                int index = values.get(i);
                if (index < 0 || index >= player.itemsBag.length) {
                    return;
                }
                Item item = player.itemsBag[index];
                if (item == null || item.template.type >= 8 || item.template.levelRequire / 10 != 4) {
                    player.addInfo(Player.INFO_RED, Language.ITEM_INVALID);
                    return;
                }
                int star = item.getMaxStar();
                if (star == 0) {
                    player.addInfo(Player.INFO_RED, Language.ITEM_INVALID);
                    return;
                }
                items[i] = item;
                num += QUANTITY[star - 1] * 3;
            }
            for (Item item : items) {
                player.itemsBag[item.indexUI] = null;
            }
            Item newItem = ItemManager.getInstance().createItem(ItemName.HOA_HONG_DO, num, true);
            player.addItem(newItem, true);
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
            if (item != null && item.template.type < 8 && item.template.levelRequire / 10 == 4 && item.getMaxStar() > 0) {
                flags[i] = true;
            }
        }
        return flags;
    }
}
