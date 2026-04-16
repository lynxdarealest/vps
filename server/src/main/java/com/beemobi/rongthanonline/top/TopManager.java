package com.beemobi.rongthanonline.top;

import com.beemobi.rongthanonline.clan.Clan;
import com.beemobi.rongthanonline.entity.player.Player;
import com.beemobi.rongthanonline.event.Event;
import com.beemobi.rongthanonline.server.Server;
import com.beemobi.rongthanonline.top.clan.TopClan;
import com.beemobi.rongthanonline.top.player.TopPlayer;
import com.beemobi.rongthanonline.top.player.disciple.TopDisciple;
import com.beemobi.rongthanonline.top.player.event.TopEvent;
import com.beemobi.rongthanonline.top.player.event.other.TopEventOther;
import com.beemobi.rongthanonline.top.player.power.TopPower;
import com.beemobi.rongthanonline.top.player.pro.TopPro;
import com.beemobi.rongthanonline.top.player.week.TopWeekly;
import com.beemobi.rongthanonline.top.player.week.active.TopActiveWeekly;
import com.beemobi.rongthanonline.top.player.week.active.previous.TopActivePreviousWeekly;
import com.beemobi.rongthanonline.top.player.week.boss.TopBossWeekly;
import com.beemobi.rongthanonline.top.player.week.boss.previous.TopBossPreviousWeekly;
import com.beemobi.rongthanonline.top.player.week.event.TopEventWeekly;
import com.beemobi.rongthanonline.top.player.week.event.other.TopEventOtherWeekly;
import com.beemobi.rongthanonline.top.player.week.event.previous.TopEventPreviousWeekly;
import com.beemobi.rongthanonline.top.player.week.event.previous.other.TopEventOtherPreviousWeekly;
import org.apache.log4j.Logger;

import java.time.*;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class TopManager {
    private static final Logger logger = Logger.getLogger(TopManager.class);
    private static TopManager instance;
    public HashMap<TopType, Top> tops;
    public ArrayList<Top> topMartialArtsFestival;
    public ArrayList<Top> topSurvival;

    public static TopManager getInstance() {
        if (instance == null) {
            instance = new TopManager();
        }
        return instance;
    }

    public void addTop(Top top) {
        tops.put(top.type, top);
    }

    public void showTop(Player player, TopType type) {
        Top top = tops.get(type);
        if (top != null) {
            top.show(player);
        }
    }

    public void setObject(Object object) {
        if (object instanceof Player) {
            for (Top top : tops.values()) {
                if (top instanceof TopPlayer) {
                    top.setObject(object);
                }
            }
            return;
        }
        if (object instanceof Clan) {
            for (Top top : tops.values()) {
                if (top.type == TopType.TOP_CLAN) {
                    top.setObject(object);
                }
            }
        }
    }

    public void clearObject(Object object) {
        if (object instanceof Player) {
            for (Top top : tops.values()) {
                if (top.type == TopType.POWER_MASTER || top.type == TopType.TOP_EVENT || top.type == TopType.TOP_PRO) {
                    top.clearObject(object);
                }
            }
        }
    }

    public void init() {
        topMartialArtsFestival = new ArrayList<>();
        topSurvival = new ArrayList<>();
        tops = new HashMap<>();
        addTop(new TopPower());
        addTop(new TopDisciple());
        addTop(new TopClan());
        addTop(new TopPro());
        addTop(new TopBossWeekly());
        addTop(new TopActiveWeekly());
        addTop(new TopActivePreviousWeekly());
        addTop(new TopBossPreviousWeekly());
        if (Event.isEvent()) {
            addTop(new TopEvent());
            //addTop(new TopEventOther());
            addTop(new TopEventWeekly());
            //addTop(new TopEventOtherWeekly());
            addTop(new TopEventPreviousWeekly());
            //addTop(new TopEventOtherPreviousWeekly());
        }
        for (Top top : tops.values()) {
            top.init();
        }
        if (!Server.getInstance().isInterServer()) {
            resetEveryMonday();
        }
    }

    public void resetEveryMonday() {
        LocalDateTime localNow = LocalDateTime.now();
        ZoneId currentZone = ZoneId.of("Asia/Ho_Chi_Minh");
        ZonedDateTime zonedNow = ZonedDateTime.of(localNow, currentZone);
        ZonedDateTime zonedNext = zonedNow.with(LocalTime.of(0, 10, 0))
                .with(TemporalAdjusters.next(DayOfWeek.MONDAY));
        if (zonedNow.compareTo(zonedNext) > 0) {
            zonedNext = zonedNext.plusWeeks(1);
        }
        Duration duration = Duration.between(zonedNow, zonedNext);
        long delay = duration.getSeconds();
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
        scheduler.scheduleAtFixedRate(() -> {
            try {
                for (Top top : tops.values()) {
                    if (top.type == TopType.TOP_ACTIVE_PREVIOUS_WEEKLY || top.type == TopType.TOP_BOSS_PREVIOUS_WEEKLY
                            || top.type == TopType.TOP_EVENT_PREVIOUS_WEEKLY
                            || top.type == TopType.TOP_EVENT_OTHER_PREVIOUS_WEEKLY) {
                        top.init();
                    } else if (top instanceof TopWeekly) {
                        ((TopWeekly) top).clearWeek();
                    }
                }
            } catch (Exception ex) {
                logger.error("resetEveryMonday", ex);
            }
        }, delay, 60L * 60 * 24 * 7, TimeUnit.SECONDS);
    }
}
