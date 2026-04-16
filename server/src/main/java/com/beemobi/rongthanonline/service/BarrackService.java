package com.beemobi.rongthanonline.service;

import com.beemobi.rongthanonline.entity.player.Player;
import com.beemobi.rongthanonline.entity.player.PlayerManager;
import com.beemobi.rongthanonline.map.MapName;
import com.beemobi.rongthanonline.map.expansion.barrack.Barrack;
import com.beemobi.rongthanonline.map.expansion.barrack.ZoneBarrack;
import com.beemobi.rongthanonline.model.MessageTime;
import org.apache.log4j.Logger;

public class BarrackService {
    private static final Logger logger = Logger.getLogger(BarrackService.class);

    private final Barrack barrack;

    public BarrackService(Barrack barrack) {
        this.barrack = barrack;
    }

    public void setTimeFlight(ZoneBarrack zone) {
        barrack.lock.lock();
        try {
            if (zone.timeStartBattle <= 0) {
                return;
            }
            long time = barrack.timeBattle * 1000L - (System.currentTimeMillis() - zone.timeStartBattle);
            for (int id : barrack.players) {
                Player player = PlayerManager.getInstance().findPlayerById(id);
                if (player != null) {
                    player.addMessageTime(MessageTime.WAR_BAN_DOANH_RED, time);
                }
            }
        } finally {
            barrack.lock.unlock();
        }
    }

    public void setTime() {
        barrack.lock.lock();
        try {
            long time = barrack.getCountDown();
            for (int id : barrack.players) {
                Player player = PlayerManager.getInstance().findPlayerById(id);
                if (player != null) {
                    player.addMessageTime(MessageTime.BAN_DOANH_RED, time);
                }
            }
        } finally {
            barrack.lock.unlock();
        }

    }

    public void addInfo(int type, String info) {
        barrack.lock.lock();
        try {
            for (int id : barrack.players) {
                Player player = PlayerManager.getInstance().findPlayerById(id);
                if (player != null) {
                    player.addInfo(type, info);
                }
            }
        } finally {
            barrack.lock.unlock();
        }
    }

    public void resetPosition() {
        barrack.lock.lock();
        try {
            for (int id : barrack.players) {
                Player player = PlayerManager.getInstance().findPlayerById(id);
                if (player != null && player.isInBarrack()) {
                    player.x = 90;
                    player.y = 504;
                    player.joinMap(barrack.findMap(MapName.TUONG_THANH_2), -1);
                }
            }
        } finally {
            barrack.lock.unlock();
        }
    }
}
