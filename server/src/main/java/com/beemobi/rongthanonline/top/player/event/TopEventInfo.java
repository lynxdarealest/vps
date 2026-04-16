package com.beemobi.rongthanonline.top.player.event;

import com.beemobi.rongthanonline.entity.player.Player;
import com.beemobi.rongthanonline.top.player.TopPlayerInfo;
import com.beemobi.rongthanonline.util.Utils;

public class TopEventInfo extends TopPlayerInfo {

    public TopEventInfo(Player player) {
        super(player);
    }

    @Override
    public long getScore() {
        if (player != null) {
            score = player.pointEvent;
        }
        return score;
    }

    @Override
    public String getInfo() {
        if (player != null) {
            info = String.format("Điểm: %s", Utils.getMoneys(player.pointEvent));
        }
        return info;
    }

    @Override
    public long getUpdateTime() {
        if (player != null) {
            updateTime = player.updateTimeEvent;
        }
        return updateTime;
    }
}
