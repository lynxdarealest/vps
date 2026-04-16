package com.beemobi.rongthanonline.top.player.week.event;

import com.beemobi.rongthanonline.entity.player.Player;
import com.beemobi.rongthanonline.model.PointWeekly;
import com.beemobi.rongthanonline.top.player.week.TopWeeklyInfo;
import com.beemobi.rongthanonline.util.Utils;

public class TopEventWeeklyInfo extends TopWeeklyInfo {

    public TopEventWeeklyInfo(Player player) {
        super(player);
    }

    @Override
    public void update() {
        super.update();
        if (player != null) {
            PointWeekly point = player.getPointWeekly();
            if (point != null) {
                score = point.eventPoint;
                if (point.eventTime != null) {
                    updateTime = point.eventTime.getTime();
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
