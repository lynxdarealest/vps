package com.beemobi.rongthanonline.bot.boss.ginyu;

import com.beemobi.rongthanonline.bot.boss.Boss;
import com.beemobi.rongthanonline.bot.boss.TeamBoss;
import com.beemobi.rongthanonline.map.Zone;
import com.beemobi.rongthanonline.map.Map;
import com.beemobi.rongthanonline.map.MapManager;
import com.beemobi.rongthanonline.map.MapName;
import com.beemobi.rongthanonline.util.Utils;
import org.apache.log4j.Logger;

public class TeamGinyu extends TeamBoss {
    private static final Logger logger = Logger.getLogger(TeamGinyu.class);
    private static final int[] MAPS = {MapName.THI_TRAN_GURU, MapName.DAO_MAIMA, MapName.RUNG_GURU};
    private final So4 so4;
    private final So3 so3;
    private final So2 so2;
    private final So1 so1;
    private final Ginyu ginyu;

    public TeamGinyu() {
        so4 = new So4(this);
        so3 = new So3(this);
        so2 = new So2(this);
        so1 = new So1(this);
        ginyu = new Ginyu(this);
    }

    @Override
    public void next(Boss boss) {
        if (boss instanceof So4) {
            Utils.setTimeout(() -> {
                so3.setTypePk(3);
            }, boss.waitingTimeToLeave + 1000L);
        }
        if (boss instanceof So3) {
            Utils.setTimeout(() -> {
                so2.setTypePk(3);
            }, boss.waitingTimeToLeave + 1000L);
        }
        if (boss instanceof So2) {
            Utils.setTimeout(() -> {
                so1.setTypePk(3);
            }, boss.waitingTimeToLeave + 1000L);
        }
        if (boss instanceof So1) {
            Utils.setTimeout(() -> {
                ginyu.setTypePk(3);
            }, boss.waitingTimeToLeave + 1000L);
        }
        if (boss instanceof Ginyu) {
            end();
        }
    }

    @Override
    public void end() {
        Utils.setTimeout(this::born, ginyu.delayRespawn);
    }

    @Override
    public void born() {
        if (so4.isDead()) {
            so4.wakeUpFromDead();
        }
        if (so3.isDead()) {
            so3.wakeUpFromDead();
        }
        if (so2.isDead()) {
            so2.wakeUpFromDead();
        }
        if (so1.isDead()) {
            so1.wakeUpFromDead();
        }
        if (ginyu.isDead()) {
            ginyu.wakeUpFromDead();
        }
        so4.timeBorn = so3.timeBorn = so1.timeBorn = so2.timeBorn = ginyu.timeBorn = System.currentTimeMillis();
        Map map = MapManager.getInstance().maps.get(Utils.nextArray(MAPS));
        if (map != null) {
            so4.teleport(map, -2);
            so3.teleport(map, so4.zone.id);
            so2.teleport(map, so4.zone.id);
            so1.teleport(map, so4.zone.id);
            ginyu.teleport(map, so4.zone.id);
            so4.setTypePk(3);
        }
    }

    @Override
    public void update(Zone zone) {

    }
}
