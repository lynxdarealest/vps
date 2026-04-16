package com.beemobi.rongthanonline.history;

import com.beemobi.rongthanonline.data.history.HistoryLogData;
import com.beemobi.rongthanonline.entity.player.Player;
import com.beemobi.rongthanonline.repository.GameRepository;
import lombok.Getter;
import org.apache.log4j.Logger;

public class HistoryManager {
    private static final Logger logger = Logger.getLogger(HistoryManager.class);

    @Getter
    private static final HistoryManager instance = new HistoryManager();

    public void saveLogin(Player player) {
        try {
            GameRepository.getInstance().historyLog.save(new HistoryLogData(player, HistoryType.LOGIN));
        } catch (Exception ex) {
            logger.error("saveLogin", ex);
        }
    }

    public void saveLogout(Player player) {
        try {
            GameRepository.getInstance().historyLog.save(new HistoryLogData(player, HistoryType.LOGOUT));
        } catch (Exception ex) {
            logger.error("saveLogout", ex);
        }
    }
}
