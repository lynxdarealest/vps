package com.beemobi.rongthanonline.top.player.week;

import com.beemobi.rongthanonline.entity.player.Player;
import com.beemobi.rongthanonline.top.player.TopPlayerInfo;

public abstract class TopWeeklyInfo extends TopPlayerInfo {

    public TopWeeklyInfo(Player player) {
        super(player);
    }

    public void update() {
        if (player != null) {
            this.name = player.name;
        }
    }
}