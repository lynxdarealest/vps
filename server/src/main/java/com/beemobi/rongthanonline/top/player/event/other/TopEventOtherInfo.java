package com.beemobi.rongthanonline.top.player.event.other;

import com.beemobi.rongthanonline.entity.player.Player;
import com.beemobi.rongthanonline.top.player.TopPlayerInfo;
import com.beemobi.rongthanonline.util.Utils;

public class TopEventOtherInfo extends TopPlayerInfo {

    public TopEventOtherInfo(Player player) {
        super(player);
    }

    @Override
    public long getScore() {
        if (player != null) {
            score = player.pointOtherEvent;
        }
        return score;
    }

    @Override
    public String getInfo() {
        if (player != null) {
            info = String.format("Điểm: %s", Utils.getMoneys(player.pointOtherEvent));
        }
        return info;
    }

    @Override
    public long getUpdateTime() {
        if (player != null) {
            updateTime = player.updateTimeEventOther;
        }
        return updateTime;
    }
}
