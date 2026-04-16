package com.beemobi.rongthanonline.model;

import com.beemobi.rongthanonline.data.PointWeeklyData;
import com.beemobi.rongthanonline.entity.player.Player;
import com.beemobi.rongthanonline.repository.GameRepository;

import java.sql.Timestamp;
import java.time.LocalDate;

public class PointWeekly {
    public long id;
    public LocalDate startWeek;
    public Player player;
    public long activePoint;
    public Timestamp activeTime;
    public long eventPoint;
    public Timestamp eventTime;
    public long eventOtherPoint;
    public Timestamp eventOtherTime;
    public long bossPoint;
    public Timestamp bossTime;
    public transient boolean isChanged;

    public PointWeekly(Player player, PointWeeklyData data) {
        id = data.id;
        this.player = player;
        startWeek = data.startWeek;
        activePoint = data.activePoint;
        activeTime = data.activeTime;
        eventPoint = data.eventPoint;
        eventTime = data.eventTime;
        bossPoint = data.bossPoint;
        bossTime = data.bossTime;
        eventOtherPoint = data.eventOtherPoint;
        eventOtherTime = data.eventOtherTime;
    }

    public PointWeekly(Player player, LocalDate startWeek) {
        this.id = -1;
        this.player = player;
        isChanged = true;
        this.startWeek = startWeek;
    }

    public void upPoint(PointWeeklyType type, long point) {
        isChanged = true;
        long now = System.currentTimeMillis();
        switch (type) {
            case ACTIVE:
                activePoint += point;
                activeTime = new Timestamp(now);
                break;

            case EVENT:
                eventPoint += point;
                eventTime = new Timestamp(now);
                break;

            case EVENT_OTHER:
                eventOtherPoint += point;
                eventOtherTime = new Timestamp(now);
                break;

            case BOSS:
                bossPoint += point;
                bossTime = new Timestamp(now);
                break;
        }
    }

    public void save() {
        if (!isChanged) {
            return;
        }
        isChanged = false;
        if (id == -1) {
            PointWeeklyData data = new PointWeeklyData(this);
            GameRepository.getInstance().pointWeekly.save(data);
            this.id = data.id;
        } else {
            GameRepository.getInstance().pointWeekly.saveData(this.id, activePoint, activeTime,
                    eventPoint, eventTime, bossPoint, bossTime, eventOtherPoint, eventOtherTime);
        }
    }
}
