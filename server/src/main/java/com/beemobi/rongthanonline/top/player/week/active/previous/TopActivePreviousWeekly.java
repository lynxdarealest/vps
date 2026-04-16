package com.beemobi.rongthanonline.top.player.week.active.previous;

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

public class TopActivePreviousWeekly extends TopWeekly {

    public TopActivePreviousWeekly() {
        super(TopType.TOP_ACTIVE_PREVIOUS_WEEKLY, "Hoạt động");
        limit = 10;
    }

    @Override
    public void init() {
        lock.writeLock().lock();
        try {
            elements.clear();
            LocalDateTime now = LocalDateTime.now();
            LocalDate startOfPreviousWeek = Utils.getStartOfWeek(now.minusWeeks(1)).toLocalDate();
            List<PointWeeklyData> objects = GameRepository.getInstance().pointWeekly.findTopActive(Server.ID, startOfPreviousWeek, limit);
            for (PointWeeklyData object : objects) {
                TopActivePreviousWeeklyInfo top = new TopActivePreviousWeeklyInfo(null);
                top.id = object.playerId;
                top.name = object.name;
                top.gender = object.gender;
                top.score = object.activePoint;
                top.updateTime = object.activeTime.getTime();
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
                .findByServerAndPlayerIdAndStartWeekAndActivePoint(Server.ID, -1, week, -1L).isEmpty()) {
            return;
        }
        PointWeeklyData data = new PointWeeklyData(week);
        data.activePoint = -1L;
        data.activeTime = new Timestamp(System.currentTimeMillis());
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
                itemList = MissionManager.getInstance().getItems(1, 1);
            } else if (i == 1) {
                itemList = MissionManager.getInstance().getItems(1, 2);
            } else if (i == 2 || i == 3 || i == 4) {
                itemList = MissionManager.getInstance().getItems(1, 3);
            } else {
                itemList = MissionManager.getInstance().getItems(1, 4);
            }
            rewardDataList.add(new RewardData(info.id, "Phần thưởng top " + (i + 1) + " " + this.name + " tuần", itemList));
        }
        if (!rewardDataList.isEmpty()) {
            GameRepository.getInstance().rewardData.saveAll(rewardDataList);
        }
    }
}
