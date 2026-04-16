package com.beemobi.rongthanonline.entity.monster.big;

import com.beemobi.rongthanonline.entity.Entity;
import com.beemobi.rongthanonline.entity.monster.Monster;
import com.beemobi.rongthanonline.entity.monster.MonsterManager;
import com.beemobi.rongthanonline.entity.player.Player;
import com.beemobi.rongthanonline.map.Zone;
import com.beemobi.rongthanonline.model.PointWeeklyType;
import com.beemobi.rongthanonline.server.Server;
import com.beemobi.rongthanonline.util.Utils;
import org.apache.log4j.Logger;

import java.util.HashMap;
import java.util.List;

public abstract class BigMonster extends Monster {
    private static final Logger logger = Logger.getLogger(BigMonster.class);

    public int countAttack;
    public boolean isHasPoint;
    public int percentDamage;

    public BigMonster(int templateId) {
        super();
        isAutoRefresh = false;
        template = MonsterManager.getInstance().monsterTemplates.get(templateId);
        percentDamage = 30;
        isHasPoint = true;
    }

    @Override
    public boolean isBigMonster() {
        return true;
    }

    @Override
    public void startDie(Entity killer) {
        super.startDie(killer);
        Utils.setTimeout(() -> {
            zone.removeBigMonster(this);
            if (killer != null && killer.isPlayer()) {
                Player player = (Player) killer;
                sendNotificationWhenDead(player.name);
            }
            throwItemWhenLeaveMap(killer);
        }, 5000);
    }

    @Override
    public void injure(Entity attacker, long hpInjure, boolean isCritical, boolean isStrikeBack) {
        super.injure(attacker, hpInjure, isCritical, isStrikeBack);
        if (isDead() && isHasPoint) {
            for (Player player : enemies.keySet()) {
                if (!player.isLogout && player.zone == zone) {
                    long damage = enemies.get(player) / 1000000;
                    if (damage > 0) {
                        player.upPointWeekly(PointWeeklyType.BOSS, damage);
                    }
                }
            }
            if (attacker instanceof Player player && attacker.isPlayer()) {
                player.upPointWeekly(PointWeeklyType.BOSS, maxHp / 1000000);
            }
        }
    }

    @Override
    public void updateAttack() {
        countAttack++;
        HashMap<Integer, Long> targets = new HashMap<>();
        List<Player> playerList = zone.getPlayers(Zone.TYPE_PLAYER, Zone.TYPE_DISCIPLE);
        for (Player player : playerList) {
            player.lock.lock();
            try {
                if (player.zone == this.zone && !player.isDead()) {
                    long damage = player.formatDamageInjure(this, Utils.percentOf(player.maxHp, percentDamage), true);
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
            if (countAttack >= 6) {
                countAttack = 0;
            }
        }
    }

    public abstract void throwItemWhenLeaveMap(Entity killer);

    public void sendNotificationWhenAppear(Zone zone) {
        Server.getInstance().service.serverChat(String.format("BOSS %s vừa xuất hiện tại %s khu vực %d", template.name, zone.map.template.name, zone.id));
        logger.debug(String.format("%s: %s khu vực %d", template.name, zone.map.template.name, zone.id));
    }

    public void sendNotificationWhenDead(String name) {
        Server.getInstance().service.serverChat(String.format("%s: Đã tiêu diệt được %s mọi người đều ngưỡng mộ", name, template.name));
        logger.debug(String.format("%s: tiêu diệt %s", name, template.name));
    }
}
