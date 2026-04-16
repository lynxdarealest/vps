package com.beemobi.rongthanonline.service;

import com.beemobi.rongthanonline.entity.player.Player;
import com.beemobi.rongthanonline.entity.player.PlayerManager;
import com.beemobi.rongthanonline.map.expansion.manor.Manor;
import org.apache.log4j.Logger;

public class ManorService {
    private static final Logger logger = Logger.getLogger(ManorService.class);

    private final Manor manor;

    public ManorService(Manor manor) {
        this.manor = manor;
    }

    public void setTimeRemaining(long time) {
        manor.lock.lock();
        try {
            for (int id : manor.players) {
                Player player = PlayerManager.getInstance().findPlayerById(id);
                if (player != null) {
                    player.service.setTimeRemaining(time);
                }
            }
        } finally {
            manor.lock.unlock();
        }

    }

    public void addInfo(int type, String info) {
        manor.lock.lock();
        try {
            for (int id : manor.players) {
                Player player = PlayerManager.getInstance().findPlayerById(id);
                if (player != null) {
                    player.addInfo(type, info);
                }
            }
        } finally {
            manor.lock.unlock();
        }
    }
}
