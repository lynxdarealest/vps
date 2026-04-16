package com.beemobi.rongthanonline.upgrade;

import com.beemobi.rongthanonline.command.Command;
import com.beemobi.rongthanonline.command.CommandName;
import com.beemobi.rongthanonline.common.Language;
import com.beemobi.rongthanonline.item.ItemName;
import com.beemobi.rongthanonline.npc.NpcName;
import com.beemobi.rongthanonline.entity.player.Player;
import com.beemobi.rongthanonline.item.Item;
import com.beemobi.rongthanonline.item.ItemManager;
import com.beemobi.rongthanonline.item.ItemType;
import com.beemobi.rongthanonline.network.Message;
import com.beemobi.rongthanonline.shop.TypePrice;
import com.beemobi.rongthanonline.util.Utils;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

public class UpgradeStone extends Upgrade {
    private static final Logger logger = Logger.getLogger(UpgradeStone.class);
    private static final int[] XU_UPGRADE_STONE = new int[]{500, 1000, 2000, 4000, 10000, 40000, 100000, 250000, 600000, 1000000, 2000000};

    public UpgradeStone(UpgradeType type, String name, String command) {
        super(type, name, command);
        notes.add("- Vào túi đồ chọn Đá cường hóa cần luyện");
        notes.add("- Sau đó chọn Luyện đá");
        notes.add("- Lưu ý: nếu không đủ Xu khóa sẽ trừ Xu");
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
                player.addInfo(Player.INFO_RED, "Chỉ được chọn duy nhất 1 loại đá");
                return;
            }
            int index = message.reader().readByte();
            if (index < 0 || index >= player.itemsBag.length) {
                return;
            }
            Item item = player.itemsBag[index];
            if (item == null || item.template.type != ItemType.TYPE_STONE) {
                player.addInfo(Player.INFO_RED, Language.ITEM_INVALID);
                return;
            }
            if (item.quantity < 4) {
                player.addInfo(Player.INFO_RED, String.format("Cần ít nhất x4 %s", item.template.name));
                return;
            }
            int stone = item.nextStoneId();
            if (stone <= 50 || stone >= 162) {
                player.addInfo(Player.INFO_RED, "Đá cường hóa đã đạt cấp tối đa");
                return;
            }
            long coin = (stone <= 57 ? XU_UPGRADE_STONE[stone - 51] : XU_UPGRADE_STONE[stone - 151]);
            List<Command> commands = new ArrayList<>();
            commands.add(new Command(CommandName.CONFIRM_UPGRADE, String.format("x1\n%s Xu khóa", Utils.formatNumber(coin)), player, index, 1));
            if (item.quantity >= 40) {
                commands.add(new Command(CommandName.CONFIRM_UPGRADE, String.format("x10\n%s Xu khóa", Utils.formatNumber(coin * 10)), player, index, 10));
            }
            if (item.quantity >= 400) {
                commands.add(new Command(CommandName.CONFIRM_UPGRADE, String.format("x100\n%s Xu khóa", Utils.formatNumber(coin * 100)), player, index, 100));
            }
            if (item.quantity >= 4000) {
                commands.add(new Command(CommandName.CONFIRM_UPGRADE, String.format("x1000\n%s Xu khóa", Utils.formatNumber(coin * 1000)), player, index, 1000));
            }
            if (item.quantity >= 40000) {
                commands.add(new Command(CommandName.CONFIRM_UPGRADE, String.format("x10000\n%s Xu khóa", Utils.formatNumber(coin * 10000L)), player, index, 10000));
            }
            player.createMenu(NpcName.ME, "", commands);
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
            if (item == null || item.template.type != ItemType.TYPE_STONE) {
                player.addInfo(Player.INFO_RED, Language.ITEM_INVALID);
                return;
            }
            int quantity = (int) objects[1];
            if (item.quantity < 4 * quantity) {
                player.addInfo(Player.INFO_RED, String.format("Cần ít nhất x%d %s", 4 * quantity, item.template.name));
                return;
            }
            int stone = item.nextStoneId();
            if (stone <= 50 || stone >= 162) {
                player.addInfo(Player.INFO_RED, "Đá cường hóa đã đạt cấp tối đa");
                return;
            }
            long coin = (long) (stone <= 57 ? XU_UPGRADE_STONE[stone - 51] : XU_UPGRADE_STONE[stone - 151]) * quantity;
            if (!player.isEnoughMoney(TypePrice.COIN_LOCK, coin)) {
                return;
            }
            player.removeQuantityItemBag(index, 4 * quantity);
            player.downMoney(TypePrice.COIN_LOCK, coin);
            item = ItemManager.getInstance().createItem(stone, quantity, true);
            player.addItem(item, true);
            if (item.template.id >= ItemName.DA_4 && player.taskMain != null && player.taskMain.template.id == 12 && player.taskMain.index == 4) {
                player.nextTaskParam();
            }
            if (item.template.id == ItemName.DA_12) {
                player.upPointAchievement(15, 1);
            }
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
            if (item != null && item.template.type == ItemType.TYPE_STONE && item.quantity >= 4) {
                flags[i] = true;
            }
        }
        return flags;
    }
}
