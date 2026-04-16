package com.beemobi.rongthanonline.top.player.week.active;

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

public class TopActiveWeekly extends TopWeekly {

    public TopActiveWeekly() {
        super(TopType.TOP_ACTIVE_WEEKLY, "Hoạt động");
    }

    @Override
    public void init() {
        lock.writeLock().lock();
        try {
            elements.clear();
            LocalDateTime now = LocalDateTime.now();
            LocalDate startOfWeek = Utils.getStartOfWeek(now).toLocalDate();
            List<PointWeeklyData> objects = GameRepository.getInstance().pointWeekly.findTopActive(Server.ID, startOfWeek, limit);
            for (PointWeeklyData object : objects) {
                TopActiveWeeklyInfo top = new TopActiveWeeklyInfo(null);
                top.id = object.playerId;
                top.name = object.name;
                top.gender = object.gender;
                top.score = object.activePoint;
                top.updateTime = object.activeTime.getTime();
                top.info = String.format("Điểm: %s", Utils.getMoneys(top.score));
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
            elements.add(new TopActiveWeeklyInfo(player));
        } finally {
            lock.writeLock().unlock();
        }
    }

}
