package com.beemobi.rongthanonline.entity.player.action;

import com.beemobi.rongthanonline.data.PointWeeklyData;
import com.beemobi.rongthanonline.entity.player.Player;
import com.beemobi.rongthanonline.model.PointWeekly;
import com.beemobi.rongthanonline.model.PointWeeklyType;
import com.beemobi.rongthanonline.network.Message;
import com.beemobi.rongthanonline.repository.GameRepository;
import com.beemobi.rongthanonline.util.Utils;
import org.apache.log4j.Logger;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class WeeklyAction extends Action {
    private static final Logger logger = Logger.getLogger(WeeklyAction.class);

    private final ReadWriteLock lock = new ReentrantReadWriteLock();
    public PointWeekly pointWeekly;

    public WeeklyAction(Player player) {
        super(player);
    }

    @Override
    public void action(Message message) {

    }

    public void init() {
        Optional<PointWeeklyData> weeklyData = GameRepository.getInstance().pointWeekly
                .findByPlayerIdAndStartWeek(player.id, Utils.getStartOfWeek(LocalDateTime.now()).toLocalDate());
        weeklyData.ifPresent(pointWeeklyData -> pointWeekly = new PointWeekly(player, pointWeeklyData));
    }

    public PointWeekly getPointWeekly() {
        lock.readLock().lock();
        try {
            return pointWeekly;
        } finally {
            lock.readLock().unlock();
        }
    }

    public void upPointWeekly(PointWeeklyType type, long point) {
        lock.writeLock().lock();
        try {
            LocalDate startWeekly = Utils.getStartOfWeek(LocalDateTime.now()).toLocalDate();
            boolean isCreate = false;
            if (pointWeekly == null) {
                isCreate = true;
            } else if (startWeekly.isAfter(pointWeekly.startWeek)) {
                pointWeekly.save();
                isCreate = true;
            }
            if (isCreate) {
                pointWeekly = new PointWeekly(player, startWeekly);
            }
            pointWeekly.upPoint(type, point);
            if (type == PointWeeklyType.ACTIVE) {
                player.addInfo(Player.INFO_YELLOW, "Bạn nhận được " + point + " điểm hoạt động");
            } else if (type == PointWeeklyType.BOSS) {
                player.addInfo(Player.INFO_YELLOW, "Bạn nhận được " + point + " điểm săn boss");
            }
        } finally {
            lock.writeLock().unlock();
        }
    }

    public void reset() {
        lock.writeLock().lock();
        try {
            LocalDate startWeekly = Utils.getStartOfWeek(LocalDateTime.now()).toLocalDate();
            if (pointWeekly != null && startWeekly.isAfter(pointWeekly.startWeek)) {
                pointWeekly.save();
                pointWeekly = null;
            }
        } finally {
            lock.writeLock().unlock();
        }
    }

    public void save() {
        lock.readLock().lock();
        try {
            if (pointWeekly != null) {
                pointWeekly.save();
            }
        } finally {
            lock.readLock().unlock();
        }
    }

}

