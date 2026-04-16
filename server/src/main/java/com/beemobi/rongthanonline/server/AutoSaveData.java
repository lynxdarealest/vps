package com.beemobi.rongthanonline.server;

import com.beemobi.rongthanonline.clan.ClanManager;
import com.beemobi.rongthanonline.entity.player.PlayerManager;
import org.apache.log4j.Logger;

public class AutoSaveData implements Runnable {

    private static final Logger logger = Logger.getLogger(AutoSaveData.class);

    @Override
    public void run() {
        while (Server.getInstance().isRunning) {
            try {
                Thread.sleep(300000);
                Server.getInstance().saveData();
            } catch (InterruptedException ex) {
                logger.error("failed!", ex);
            }
        }
    }
}
