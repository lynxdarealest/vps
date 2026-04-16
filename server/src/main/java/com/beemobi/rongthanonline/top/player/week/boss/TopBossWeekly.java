package com.beemobi.rongthanonline.top.player.week.boss;

import com.beemobi.rongthanonline.data.PointWeeklyData;
import com.beemobi.rongthanonline.entity.player.Player;
import com.beemobi.rongthanonline.repository.GameRepository;
import com.beemobi.rongthanonline.server.Server;
import com.beemobi.rongthanonline.top.TopInfo;
import com.beemobi.rongthanonline.top.TopType;
import com.beemobi.rongthanonline.top.player.week.TopWeekly;
import com.beemobi.rongthanonline.util.Utils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class TopBossWeekly extends TopWeekly {

    public TopBossWeekly() {
        super(TopType.TOP_BOSS_WEEKLY, "Săn boss");
    }

    @Override
    public void init() {
        lock.writeLock().lock();
        try {
            elements.clear();
            LocalDateTime now = LocalDateTime.now();
            LocalDate startOfWeek = Utils.getStartOfWeek(now).toLocalDate();
            List<PointWeeklyData> objects = GameRepository.getInstance().pointWeekly.findTopBoss(Server.ID, startOfWeek, limit);
            for (PointWeeklyData object : objects) {
                TopBossWeeklyInfo top = new TopBossWeeklyInfo(null);
                top.id = object.playerId;
                top.name = object.name;
                top.gender = object.gender;
                top.score = object.bossPoint;
                top.updateTime = object.bossTime.getTime();
                top.info = String.format("Điểm: %s", Utils.formatNumber(top.score));
                elements.add(top);
            }
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public void setObject(Object object) {
        lock.writeLock().lock();
        try {
            Player player = (Player) object;
            TopInfo info = elements.stream().filter(i -> i.id == player.id).findFirst().orElse(null);
            if (info != null) {
                info.setObject(player);
                return;
            }
            elements.add(new TopBossWeeklyInfo(player));
        } finally {
            lock.writeLock().unlock();
        }
    }

}
