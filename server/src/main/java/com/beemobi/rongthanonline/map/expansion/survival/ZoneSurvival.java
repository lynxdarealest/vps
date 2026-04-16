package com.beemobi.rongthanonline.map.expansion.survival;

import com.beemobi.rongthanonline.bot.boss.other.BossSurvival;
import com.beemobi.rongthanonline.entity.player.Player;
import com.beemobi.rongthanonline.item.ItemManager;
import com.beemobi.rongthanonline.item.ItemMap;
import com.beemobi.rongthanonline.item.ItemName;
import com.beemobi.rongthanonline.map.Zone;
import com.beemobi.rongthanonline.map.Map;
import com.beemobi.rongthanonline.map.MapName;
import com.beemobi.rongthanonline.map.expansion.ExpansionState;
import com.beemobi.rongthanonline.model.MessageTime;
import com.beemobi.rongthanonline.model.PointWeeklyType;
import com.beemobi.rongthanonline.top.TopType;
import com.beemobi.rongthanonline.util.Utils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class ZoneSurvival extends Zone {
    public Survival survival;
    public boolean isRedZone;
    public long updateTime;
    public int count;
    public long timeAddPlayer;
    public ReadWriteLock lockTimeAddPlayer;

    public ZoneSurvival(Map map, Survival survival) {
        super(map);
        this.survival = survival;
        lockTimeAddPlayer = new ReentrantReadWriteLock();
    }

    @Override
    public void update() {
        super.update();
        long now = System.currentTimeMillis();
        if (isRedZone && now - updateTime > 1000) {
            updateTime = now;
            List<Player> playerList = getPlayers(TYPE_PLAYER);
            for (Player player : playerList) {
                if (player != null && player.zone == this && !player.isDead() && player.id != survival.masterId) {
                    long damage = player.maxHp * Math.max(survival.round / 2, 1) / 100;
                    player.injure(null, damage, false, false);
                    player.addInfo(Player.INFO_RED, "Khu vực đã đóng, hãy mau rời khỏi đây");
                }
            }
        }
    }

    @Override
    public void close() {
        List<Player> playerList = getPlayers(Zone.TYPE_PLAYER);
        for (Player player : playerList) {
            try {
                player.teleport(MapName.NUI_PAOZU, false);
                player.addInfo(Player.INFO_YELLOW, "Trận chiến đã kết thúc");
            } catch (Exception ignored) {
            }
        }
        super.close();
    }

    public void bornBot() {
        if (isRedZone || Utils.nextInt(100) < 50) {
            return;
        }
        enter(new BossSurvival(survival, survival.round));
    }

    public void createItemMap() {
        if (isRedZone) {
            return;
        }
        long now = System.currentTimeMillis();
        int size = Utils.nextInt(6, 10);
        List<ItemMap> itemMapList = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            int x = Utils.nextInt(300, map.template.width - 300);
            int count = Utils.nextInt(3, 6);
            for (int j = 0; j < count; j++) {
                int item;
                if (Utils.nextInt(100) < 20) {
                    item = ItemName.DAU_THAN_CAP_1;
                } else if (survival.round < 4) {
                    item = Utils.nextInt(176, 191);
                } else if (survival.round < 8) {
                    item = Utils.nextInt(192, 207);
                } else {
                    item = Utils.nextInt(208, 223);
                }
                ItemMap itemMap = ItemManager.getInstance().createItemMap(item, 1, -1);
                itemMap.x = x + Utils.nextInt(20, 30) * j;
                itemMap.y = map.getYSd(itemMap.x);
                itemMap.throwTime = now + 30000;
                itemMapList.add(itemMap);
            }
        }
        if (!itemMapList.isEmpty()) {
            addItemMap(itemMapList, -1);
        }
    }

    public void reward() {
        if (isRedZone || survival.masterId != -1) {
            return;
        }
        List<Player> playerList = getPlayers(Zone.TYPE_PLAYER);
        for (Player player : playerList) {
            player.addItem(ItemManager.getInstance().createItem(ItemName.DA_7, 1, true), true);
            player.upPointWeekly(PointWeeklyType.ACTIVE, count);
        }
    }

    @Override
    public void enter(Player player) {
        super.enter(player);
        if (player.isPlayer()) {
            if (survival.state == ExpansionState.WAIT_REG) {
                long now = System.currentTimeMillis();
                long time = Survival.MINUTE_WAIT_REG * 60000L + survival.startTime - now;
                if (time > 0) {
                    player.addMessageTime(MessageTime.SURVIVAL_TIME_WAIT, time);
                }
            } else {
                if (isRedZone) {
                    player.addMessageTime(MessageTime.SURVIVAL_ZONE_RED, -1);
                } else {
                    player.removeMessageTime(MessageTime.SURVIVAL_ZONE_RED);
                }
            }
        }
    }
}
