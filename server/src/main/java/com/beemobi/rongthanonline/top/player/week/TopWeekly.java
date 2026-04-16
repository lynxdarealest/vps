package com.beemobi.rongthanonline.top.player.week;

import com.beemobi.rongthanonline.top.TopInfo;
import com.beemobi.rongthanonline.top.TopType;
import com.beemobi.rongthanonline.top.player.TopPlayer;

import java.util.List;
import java.util.stream.Collectors;

public abstract class TopWeekly extends TopPlayer {

    public TopWeekly(TopType type, String name) {
        super(type, name);
        limit = 20;
    }

    @Override
    public List<TopInfo> query() {
        return elements.stream().filter(t -> {
            TopWeeklyInfo info = (TopWeeklyInfo) t;
            info.update();
            return info.score > 0 && info.updateTime > 0;
        }).collect(Collectors.toList());
    }

    public void clearWeek() {
        lock.writeLock().lock();
        try {
            elements.clear();
        } finally {
            lock.writeLock().unlock();
        }
    }
}

