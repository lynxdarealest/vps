package com.beemobi.rongthanonline.top.player.week.event.other;

import com.beemobi.rongthanonline.entity.player.Player;
import com.beemobi.rongthanonline.model.PointWeekly;
import com.beemobi.rongthanonline.top.player.week.TopWeeklyInfo;
import com.beemobi.rongthanonline.util.Utils;

public class TopEventOtherWeeklyInfo extends TopWeeklyInfo {

    public TopEventOtherWeeklyInfo(Player player) {
        super(player);
    }

    @Override
    public void update() {
        super.update();
        if (player != null) {
            PointWeekly point = player.getPointWeekly();
            if (point != null) {
                score = point.eventOtherPoint;
                if (point.eventOtherTime != null) {
                    updateTime = point.eventOtherTime.getTime();
                } else {
                    updateTime = 0;
                }
            } else {
                score = 0;
                updateTime = 0;
            }
            info = String.format("Điểm: %s", Utils.getMoneys(score));
        }
    }

}
