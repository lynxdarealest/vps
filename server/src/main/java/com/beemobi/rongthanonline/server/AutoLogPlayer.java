package com.beemobi.rongthanonline.server;

import com.beemobi.rongthanonline.entity.player.PlayerManager;
import org.apache.log4j.Logger;

public class AutoLogPlayer implements Runnable {

    private static final Logger logger = Logger.getLogger(AutoLogPlayer.class);

    @Override
    public void run() {
        Server server = Server.getInstance();
        int count = 0;
        while (server.isRunning) {
            try {
                int num = PlayerManager.getInstance().getCountPlayer();
                if (count != num) {
                    count = num;
                    logger.debug(String.format("Count Player: %d", count));
                }
                Thread.sleep(1000);
            } catch (InterruptedException ex) {
                logger.error("failed!", ex);
            }
        }
    }
}
