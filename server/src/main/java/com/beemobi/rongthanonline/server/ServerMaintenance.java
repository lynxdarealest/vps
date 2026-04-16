package com.beemobi.rongthanonline.server;

import com.beemobi.rongthanonline.clan.ClanManager;
import com.beemobi.rongthanonline.entity.player.PlayerManager;
import com.beemobi.rongthanonline.map.MapManager;
import com.beemobi.rongthanonline.util.Utils;
import org.apache.log4j.Logger;

public class ServerMaintenance implements Runnable {
    private static final Logger logger = Logger.getLogger(ServerMaintenance.class);
    private static ServerMaintenance instance;
    public String message;
    public int seconds;
    public boolean isRunning;

    public static ServerMaintenance getInstance() {
        if (instance == null) {
            instance = new ServerMaintenance();
        }
        return instance;
    }

    public void start(String message, int seconds) {
        if (isRunning) {
            return;
        }
        isRunning = true;
        this.message = message;
        this.seconds = seconds;
        new Thread(this).start();
    }

    @Override
    public void run() {
        try {
            Server server = Server.getInstance();
            logger.debug(String.format("Máy chủ bảo trì sau %s", Utils.getTimeAgo(seconds)));
            String text = "Máy chủ bảo trì sau %s, vui lòng thoát trò chơi để đảm bảo không bị mất dữ liệu.\n%s";
            server.service.serverChat(String.format(text, Utils.getTimeAgo(seconds), this.message));
            while (seconds > 0) {
                if (seconds == 60) {
                    PlayerManager.getInstance().dialogMessage(String.format(text, Utils.getTimeAgo(seconds), this.message));
                    logger.debug(String.format("Máy chủ bảo trì sau %s", Utils.getTimeAgo(seconds)));
                }
                seconds--;
                try {
                    Thread.sleep(1000L);
                } catch (Exception ex) {
                    logger.error("failed!", ex);
                }
            }
            server.saveData();
            server.isMaintained = true;
            server.stop();
            System.exit(1);
        } catch (Exception ex) {
            logger.error("run", ex);
        }
    }
}
