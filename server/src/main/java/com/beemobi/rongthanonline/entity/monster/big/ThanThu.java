package com.beemobi.rongthanonline.entity.monster.big;

import com.beemobi.rongthanonline.entity.Entity;
import com.beemobi.rongthanonline.entity.monster.MonsterName;
import com.beemobi.rongthanonline.entity.player.Player;
import com.beemobi.rongthanonline.item.ItemManager;
import com.beemobi.rongthanonline.item.ItemMap;
import com.beemobi.rongthanonline.item.ItemName;
import com.beemobi.rongthanonline.map.Zone;
import com.beemobi.rongthanonline.map.MapManager;
import com.beemobi.rongthanonline.map.expansion.treasure.Pirate;
import com.beemobi.rongthanonline.map.expansion.treasure.Treasure;
import com.beemobi.rongthanonline.util.Utils;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ThanThu extends BigMonster {
    private static final Logger logger = Logger.getLogger(ThanThu.class);

    public final int flag;
    private long lastTimeThrowItem;

    public ThanThu(int flag) {
        super(flag == -1 ? MonsterName.THAN_THU : (flag == 0 ? MonsterName.THAN_THU_DO : MonsterName.THAN_THU_XANH));
        hp = maxHp = baseHp = template.hp;
        this.flag = flag;
        lastTimeThrowItem = System.currentTimeMillis();
        isHasPoint = false;
    }

    @Override
    public void updateAttack() {
        countAttack++;
        if (countAttack >= 8) {
            HashMap<Integer, Long> targets = new HashMap<>();
            List<Player> playerList = zone.getPlayers(Zone.TYPE_PLAYER, Zone.TYPE_DISCIPLE);
            for (Player player : playerList) {
                player.lock.lock();
                try {
                    if (player.zone == this.zone && !player.isDead()) {
                        long damage = player.formatDamageInjure(this, player.maxHp / 3, true);
                        if (damage > 0) {
                            player.hp -= damage;
                            if (player.hp <= 0) {
                                player.startDie(this);
                            }
                            targets.put(player.id, damage);
                        }
                    }
                } finally {
                    player.lock.unlock();
                }
            }
            if (!targets.isEmpty()) {
                zone.service.bigMonsterAttack(targets);
                countAttack = 0;
            }
            return;
        }
    }

    @Override
    public void startDie(Entity killer) {
        super.startDie(killer);
        Utils.setTimeout(() -> {
            Treasure treasure = MapManager.getInstance().treasure;
            if (treasure != null) {
                Zone zone = treasure.maps.get(2).zones.get(0);
                if (killer == null || !killer.isPlayer()) {
                    zone.enterBigMonster(new ThanThu(-1));
                } else {
                    Player player = (Player) killer;
                    int index = player.typeFlag == Treasure.FLAGS[0] ? 0 : 1;
                    zone.enterBigMonster(new ThanThu(index));
                    treasure.sendNotification(Player.INFO_YELLOW, String.format("Phe %s đã hạ được %s", index == 0 ? "Đỏ" : "Xanh", template.name));
                }
            }
        }, 10000);
    }

    @Override
    public void throwItemWhenLeaveMap(Entity killer) {
        if (flag == -1 && killer != null && killer.isPlayer()) {
            Player player = (Player) killer;
            addItemToMap(player.typeFlag == Treasure.FLAGS[0] ? 0 : 1);
        }
    }

    public void addItemToMap(int flag) {
        if (flag == -1) {
            return;
        }
        Treasure treasure = MapManager.getInstance().treasure;
        if (treasure == null) {
            return;
        }
        List<ItemMap> itemMaps = new ArrayList<>();
        int size = Utils.nextInt(7, 13);
        for (int i = 0; i < size; i++) {
            itemMaps.add(ItemManager.getInstance().createItemMap(ItemName.RUBY, Utils.nextInt(3, 7), -1));
        }
        size = Utils.nextInt(1, 2);
        for (int i = 0; i < size; i++) {
            itemMaps.add(ItemManager.getInstance().createItemMap(ItemName.DA_9, 1, -1));
        }
        size = Utils.nextInt(1, 2);
        for (int i = 0; i < size; i++) {
            itemMaps.add(ItemManager.getInstance().createItemMap(ItemName.DA_8, 1, -1));
        }
        size = Utils.nextInt(5, 10);
        for (int i = 0; i < size; i++) {
            itemMaps.add(ItemManager.getInstance().createItemMap(ItemName.DA_7, 1, -1));
        }
        size = Utils.nextInt(10, 15);
        for (int i = 0; i < size; i++) {
            itemMaps.add(ItemManager.getInstance().createItemMap(ItemName.XU_KHOA, Utils.nextInt(100000, 200000), -1));
        }
        List<Integer> players = new ArrayList<>();
        List<Pirate> pirates = treasure.findListPirateByFlag(flag);
        for (Pirate pirate : pirates) {
            players.add(pirate.playerId);
        }
        if (!itemMaps.isEmpty()) {
            for (ItemMap itemMap : itemMaps) {
                itemMap.players = new ArrayList<>(players);
            }
            zone.addItemMap(itemMaps, -1);
        }
    }

    @Override
    public void update() {
        super.update();
        if (flag != -1) {
            long now = System.currentTimeMillis();
            if (now - lastTimeThrowItem > 30000) {
                lastTimeThrowItem = now;
                addItemToMap(flag);
            }
        }
    }

    @Override
    public long formatDamageInjure(Entity attacker, long damage, boolean isCritical) {
        if (flag != -1 && attacker != null && attacker.isPlayer()) {
            Player player = (Player) attacker;
            if (player.typeFlag == Treasure.FLAGS[flag]) {
                return 0;
            }
            return 1;
        }
        return flag == -1 ? 1 : 0;
    }

    public void sendNotificationWhenAppear(Zone zone) {
    }

    public void sendNotificationWhenDead(String name) {

    }
}
