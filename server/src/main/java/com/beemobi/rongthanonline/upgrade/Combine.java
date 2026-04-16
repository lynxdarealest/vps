package com.beemobi.rongthanonline.upgrade;

import com.beemobi.rongthanonline.command.Command;
import com.beemobi.rongthanonline.command.CommandName;
import com.beemobi.rongthanonline.common.Language;
import com.beemobi.rongthanonline.entity.player.Player;
import com.beemobi.rongthanonline.item.Item;
import com.beemobi.rongthanonline.item.ItemManager;
import com.beemobi.rongthanonline.item.ItemName;
import com.beemobi.rongthanonline.item.ItemType;
import com.beemobi.rongthanonline.network.Message;
import com.beemobi.rongthanonline.npc.NpcName;
import com.beemobi.rongthanonline.util.Utils;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

public class Combine extends Upgrade {
    private static final Logger logger = Logger.getLogger(Combine.class);

    public Combine(UpgradeType type, String name, String command) {
        super(type, name, command);
        notes.add("- Vào túi đồ chọn Ngọc Rồng cần làm phép");
        notes.add("- Sau đó chọn làm phép");
        notes.add("- x7 Ngọc rồng cùng loại sau làm phép sẽ được x1 Ngọc Rồng ít hơn 1 sao");
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
            if (size != 1) {
                player.addInfo(Player.INFO_RED, "Chỉ được chọn duy nhất 1 loại Ngọc rồng");
                return;
            }
            int index = message.reader().readByte();
            if (index < 0 || index >= player.itemsBag.length) {
                return;
            }
            Item item = player.itemsBag[index];
            if (item == null || item.template.type != ItemType.TYPE_DRAGON_BALL) {
                player.addInfo(Player.INFO_RED, Language.ITEM_INVALID);
                return;
            }
            if (item.quantity < 7) {
                player.addInfo(Player.INFO_RED, String.format("Cần ít nhất x7 %s", item.template.name));
                return;
            }
            if (item.template.id == ItemName.NGOC_RONG_1_SAO) {
                player.addInfo(Player.INFO_RED, String.format("Không thể làm phép với %s", item.template.name));
                return;
            }
            long coin = 10000L;
            List<Command> commands = new ArrayList<>();
            commands.add(new Command(CommandName.CONFIRM_UPGRADE, String.format("x1\n%s xu", Utils.formatNumber(coin)), player, index, 1));
            if (item.quantity >= 70) {
                commands.add(new Command(CommandName.CONFIRM_UPGRADE, String.format("x10\n%s xu", Utils.formatNumber(coin * 10)), player, index, 10));
            }
            if (item.quantity >= 700) {
                commands.add(new Command(CommandName.CONFIRM_UPGRADE, String.format("x100\n%s xu", Utils.formatNumber(coin * 100)), player, index, 100));
            }
            player.createMenu(NpcName.ME, String.format("Nhập %s", ItemManager.getInstance().itemTemplates.get(item.template.id - 1).name), commands);
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
            int index = (int) objects[0];
            if (index < 0 || index >= player.itemsBag.length) {
                return;
            }
            Item item = player.itemsBag[index];
            if (item == null || item.template.type != ItemType.TYPE_DRAGON_BALL) {
                player.addInfo(Player.INFO_RED, Language.ITEM_INVALID);
                return;
            }
            int quantity = (int) objects[1];
            if (item.quantity < 7 * quantity) {
                player.addInfo(Player.INFO_RED, String.format("Cần ít nhất x%d %s", 4 * quantity, item.template.name));
                return;
            }
            if (item.template.id == ItemName.NGOC_RONG_1_SAO) {
                player.addInfo(Player.INFO_RED, String.format("Không thể làm phép với %s", item.template.name));
                return;
            }
            long coin = 10000L * quantity;
            if (player.xu < coin) {
                player.addInfo(Player.INFO_RED, String.format("Bạn còn thiếu %s xu", Utils.getMoneys(coin - player.xu)));
                return;
            }
            player.removeQuantityItemBag(index, 7 * quantity);
            player.upXu(-coin);
            item = ItemManager.getInstance().createItem(item.template.id - 1, quantity, true);
            player.addItem(item);
            player.addInfo(Player.INFO_YELLOW, String.format("Bạn nhận được x%d %s", quantity, item.template.name));
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
            if (item != null && item.template.type == ItemType.TYPE_DRAGON_BALL) {
                flags[i] = true;
            }
        }
        return flags;
    }


}
