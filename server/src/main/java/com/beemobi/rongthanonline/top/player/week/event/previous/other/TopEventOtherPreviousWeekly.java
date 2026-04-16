package com.beemobi.rongthanonline.top.player.week.event.previous.other;

import com.beemobi.rongthanonline.data.PointWeeklyData;
import com.beemobi.rongthanonline.data.RewardData;
import com.beemobi.rongthanonline.item.Item;
import com.beemobi.rongthanonline.mission.MissionManager;
import com.beemobi.rongthanonline.repository.GameRepository;
import com.beemobi.rongthanonline.server.Server;
import com.beemobi.rongthanonline.top.TopInfo;
import com.beemobi.rongthanonline.top.TopType;
import com.beemobi.rongthanonline.top.player.week.TopWeekly;
import com.beemobi.rongthanonline.util.Utils;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class TopEventOtherPreviousWeekly extends TopWeekly {

    public TopEventOtherPreviousWeekly() {
        super(TopType.TOP_EVENT_OTHER_PREVIOUS_WEEKLY, "Sự kiện");
        limit = 10;
    }

    @Override
    public void init() {
        lock.writeLock().lock();
        try {
            elements.clear();
            LocalDateTime now = LocalDateTime.now();
            LocalDate startOfPreviousWeek = Utils.getStartOfWeek(now.minusWeeks(1)).toLocalDate();
            List<PointWeeklyData> objects = GameRepository.getInstance().pointWeekly.findTopEventOther(Server.ID, startOfPreviousWeek, limit);
            for (PointWeeklyData object : objects) {
                TopEventOtherPreviousWeeklyInfo top = new TopEventOtherPreviousWeeklyInfo(null);
                top.id = object.playerId;
                top.name = object.name;
                top.gender = object.gender;
                top.score = object.eventOtherPoint;
                top.updateTime = object.eventOtherTime.getTime();
                top.info = String.format("Điểm: %s", Utils.getMoneys(top.score));
                elements.add(top);
            }
            reward(startOfPreviousWeek);
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public void setObject(Object object) {

    }

    public void reward(LocalDate week) {
        if (!GameRepository.getInstance().pointWeekly
                .findByServerAndPlayerIdAndStartWeekAndEventOtherPoint(Server.ID, -1, week, -1L).isEmpty()) {
           return;
        }
        PointWeeklyData data = new PointWeeklyData(week);
        data.eventOtherPoint = -1L;
        data.eventOtherTime = new Timestamp(System.currentTimeMillis());
        GameRepository.getInstance().pointWeekly.save(data);
        List<TopInfo> tops = getTops();
        int size = Math.min(tops.size(), 10);
        if (size == 0) {
            return;
        }
        List<RewardData> rewardDataList = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            TopInfo info = tops.get(i);
            List<Item> itemList;
            if (i == 0) {
                if (info.score < 2000) {
                    continue;
                }
                itemList = MissionManager.getInstance().getItems(2, 1);
            } else if (i == 1) {
                if (info.score < 2000) {
                    continue;
                }
                itemList = MissionManager.getInstance().getItems(2, 2);
            } else if (i == 2 || i == 3 || i == 4) {
                if (info.score < 1000) {
                    continue;
                }
                itemList = MissionManager.getInstance().getItems(2, 3);
            } else {
                if (info.score < 500) {
                    continue;
                }
                itemList = MissionManager.getInstance().getItems(2, 4);
            }
            rewardDataList.add(new RewardData(info.id, "Phần thưởng top " + (i + 1) + " " + this.name + " tuần", itemList));
        }
        if (!rewardDataList.isEmpty()) {
            GameRepository.getInstance().rewardData.saveAll(rewardDataList);
        }
    }

}
