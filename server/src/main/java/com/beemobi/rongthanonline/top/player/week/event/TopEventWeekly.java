package com.beemobi.rongthanonline.top.player.week.event;

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

public class TopEventWeekly extends TopWeekly {

    public TopEventWeekly() {
        super(TopType.TOP_EVENT_WEEKLY, "Sự kiện");
    }

    @Override
    public void init() {
        lock.writeLock().lock();
        try {
            elements.clear();
            LocalDateTime now = LocalDateTime.now();
            LocalDate startOfWeek = Utils.getStartOfWeek(now).toLocalDate();
            List<PointWeeklyData> objects = GameRepository.getInstance().pointWeekly.findTopEvent(Server.ID, startOfWeek, limit);
            for (PointWeeklyData object : objects) {
                TopEventWeeklyInfo top = new TopEventWeeklyInfo(null);
                top.id = object.playerId;
                top.name = object.name;
                top.gender = object.gender;
                top.score = object.eventPoint;
                top.updateTime = object.eventTime.getTime();
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
            elements.add(new TopEventWeeklyInfo(player));
        } finally {
            lock.writeLock().unlock();
        }
    }

}
