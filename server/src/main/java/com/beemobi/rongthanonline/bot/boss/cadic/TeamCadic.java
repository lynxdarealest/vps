package com.beemobi.rongthanonline.bot.boss.cadic;

import com.beemobi.rongthanonline.bot.boss.Boss;
import com.beemobi.rongthanonline.bot.boss.TeamBoss;
import com.beemobi.rongthanonline.map.Zone;
import com.beemobi.rongthanonline.map.Map;
import com.beemobi.rongthanonline.map.MapManager;
import com.beemobi.rongthanonline.map.MapName;
import com.beemobi.rongthanonline.util.Utils;
import org.apache.log4j.Logger;

public class TeamCadic extends TeamBoss {
    private static final Logger logger = Logger.getLogger(TeamCadic.class);
    private final Cadic cadic;
    private final Napan napan;

    public TeamCadic() {
        cadic = new Cadic(this);
        napan = new Napan(this);
    }

    @Override
    public void next(Boss boss) {
        if (boss instanceof Napan) {
            Utils.setTimeout(() -> {
                cadic.setTypePk(3);
            }, boss.waitingTimeToLeave + 1000L);
        }
        if (boss instanceof Cadic) {
            end();
        }
    }

    @Override
    public void end() {
        Utils.setTimeout(this::born, cadic.delayRespawn);
    }

    @Override
    public void born() {
        if (napan.isDead()) {
            napan.wakeUpFromDead();
        }
        if (cadic.isDead()) {
            cadic.wakeUpFromDead();
        }
        cadic.timeBorn = napan.timeBorn = System.currentTimeMillis();
        Map map = MapManager.getInstance().maps.get(MapName.PHIA_TAY_SARA);
        if (map != null) {
            napan.teleport(map, -2);
            cadic.teleport(map, napan.zone.id);
            napan.setTypePk(3);
        }
    }

    @Override
    public void update(Zone zone) {

    }
}
