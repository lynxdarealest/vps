package com.beemobi.rongthanonline.model;

import com.beemobi.rongthanonline.command.Command;
import com.beemobi.rongthanonline.command.CommandName;
import com.beemobi.rongthanonline.common.Language;
import com.beemobi.rongthanonline.entity.player.Player;
import com.beemobi.rongthanonline.entity.player.json.MagicBeanInfo;
import com.beemobi.rongthanonline.item.Item;
import com.beemobi.rongthanonline.item.ItemManager;
import com.beemobi.rongthanonline.npc.NpcName;
import com.beemobi.rongthanonline.shop.TypePrice;
import com.beemobi.rongthanonline.util.Utils;

import java.util.ArrayList;
import java.util.List;

public class MagicBean {
    public final static long[] MINUTES = new long[]{0, 5, 500, 1000, 5000, 10080, 20160, 40320, 80640, 161280};
    public final static int[] COINS = new int[]{0, 50000, 100000, 600000, 9600000, 100800000, 201600000, 403200000, 806400000, 1612800000};

    public int level;
    public long updateTime;
    public boolean isUpdate;

    public static final int MAX_LEVEL = 10;

    public MagicBean(MagicBeanInfo magicBeanInfo) {
        level = magicBeanInfo.level;
        updateTime = magicBeanInfo.updateTime;
        isUpdate = magicBeanInfo.isUpdate;
        if (level < 1) {
            level = 1;
            updateTime = 0;
            isUpdate = false;
        }
    }

    public MagicBean() {
        level = 1;
        updateTime = 0;
        isUpdate = false;
    }

    public void harvest(Player player) {
        if (player.isBagFull()) {
            player.addInfo(Player.INFO_RED, Language.ME_BAG_FULL);
            return;
        }
        long now = System.currentTimeMillis();
        int count = (int) ((now - updateTime) / (level * 60000L));
        int max = 5 + (level - 1) * 2;
        if (count > max) {
            count = max;
        }
        if (count <= 0) {
            return;
        }
        updateTime = now;
        Item item = ItemManager.getInstance().createItem(57 + level, count, true);
        player.addItem(item);
        player.addInfo(Player.INFO_YELLOW, String.format("Bạn nhận được x%d %s", item.quantity, item.template.name));
    }

    public void upgradeNow(Player player) {
        if (!isUpdate) {
            return;
        }
        List<Command> commands = new ArrayList<>();
        commands.add(new Command(CommandName.CONFIRM_UPGRADE_MAGIC_BEAN_NOW, "Nâng cấp", player));
        commands.add(new Command(CommandName.CANCEL, "Không", player));
        player.createMenu(NpcName.ME, "Bạn có chắc chắn muốn nâng cấp nhanh Đậu thần không?", commands);
    }

    public void confirmUpgradeNow(Player player) {
        if (!isUpdate) {
            return;
        }
        long time = MagicBean.MINUTES[level] * 60000L + updateTime - System.currentTimeMillis();
        int diamond = (int) (time / 600000);
        if (diamond < 100) {
            diamond = 100;
        }
        if (!player.isEnoughMoney(TypePrice.RUBY, diamond)) {
            return;
        }
        player.downMoney(TypePrice.RUBY, diamond);
        isUpdate = false;
        level++;
        player.addInfo(Player.INFO_YELLOW, String.format("Đậu thần đã được nâng cấp lên cấp %d", level));
    }

    public void upgrade(Player player) {
        if (isUpdate) {
            return;
        }
        if (level >= MAX_LEVEL) {
            player.addInfo(Player.INFO_RED, "Đậu thần đã đạt cấp tối đa");
            return;
        }
        List<Command> commands = new ArrayList<>();
        commands.add(new Command(CommandName.CONFIRM_UPGRADE_MAGIC_BEAN, "Nâng cấp", player));
        commands.add(new Command(CommandName.CANCEL, "Không", player));
        player.createMenu(NpcName.ME, "Bạn có chắc chắn muốn nâng cấp Đậu thần không?", commands);
    }

    public void confirmUpgrade(Player player) {
        if (isUpdate) {
            return;
        }
        if (level >= MAX_LEVEL) {
            player.addInfo(Player.INFO_RED, "Đậu thần đã đạt cấp tối đa");
            return;
        }
        long coin = MagicBean.COINS[level];
        if (player.xu < coin) {
            player.addInfo(Player.INFO_RED, String.format("Bạn còn thiếu %s xu", Utils.getMoneys(coin - player.xu)));
            return;
        }
        player.upXu(-coin);
        updateTime = System.currentTimeMillis();
        isUpdate = true;
    }
}
