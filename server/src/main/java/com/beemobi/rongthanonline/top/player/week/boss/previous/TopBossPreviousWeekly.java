package com.beemobi.rongthanonline.top.player.week.boss.previous;

import com.beemobi.rongthanonline.data.PointWeeklyData;
import com.beemobi.rongthanonline.data.RewardData;
import com.beemobi.rongthanonline.entity.player.Player;
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

public class TopBossPreviousWeekly extends TopWeekly {

    public TopBossPreviousWeekly() {
        super(TopType.TOP_BOSS_PREVIOUS_WEEKLY, "Săn boss");
        limit = 10;
    }

    @Override
    public void init() {
        lock.writeLock().lock();
        try {
            elements.clear();
            LocalDateTime now = LocalDateTime.now();
            LocalDate startOfPreviousWeek = Utils.getStartOfWeek(now.minusWeeks(1)).toLocalDate();
            List<PointWeeklyData> objects = GameRepository.getInstance().pointWeekly.findTopBoss(Server.ID, startOfPreviousWeek, limit);
            for (PointWeeklyData object : objects) {
                TopBossPreviousWeeklyInfo top = new TopBossPreviousWeeklyInfo(null);
                top.id = object.playerId;
                top.name = object.name;
                top.gender = object.gender;
                top.score = object.bossPoint;
                top.updateTime = object.bossTime.getTime();
                top.info = String.format("Điểm: %s", Utils.formatNumber(top.score));
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
                .findByServerAndPlayerIdAndStartWeekAndBossPoint(Server.ID, -1, week, -1L).isEmpty()) {
            return;
        }
        PointWeeklyData data = new PointWeeklyData(week);
        data.bossPoint = -1L;
        data.bossTime = new Timestamp(System.currentTimeMillis());
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
                itemList = MissionManager.getInstance().getItems(0, 1);
            } else if (i == 1) {
                itemList = MissionManager.getInstance().getItems(0, 2);
            } else if (i == 2 || i == 3 || i == 4) {
                itemList = MissionManager.getInstance().getItems(0, 3);
            } else {
                itemList = MissionManager.getInstance().getItems(0, 4);
            }
            rewardDataList.add(new RewardData(info.id, "Phần thưởng top " + (i + 1) + " " + this.name + " tuần", itemList));
        }
        if (!rewardDataList.isEmpty()) {
            GameRepository.getInstance().rewardData.saveAll(rewardDataList);
        }
    }
}
