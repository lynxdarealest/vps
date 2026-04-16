package com.beemobi.rongthanonline.top.player.pro;

import com.beemobi.rongthanonline.entity.player.Player;
import com.beemobi.rongthanonline.top.player.TopPlayerInfo;
import com.beemobi.rongthanonline.util.Utils;

public class TopProInfo extends TopPlayerInfo {

    public TopProInfo(Player player) {
        super(player);
    }

    @Override
    public long getScore() {
        if (player != null) {
            score = player.pointPro;
        }
        return score;
    }

    @Override
    public String getInfo() {
        if (player != null) {
            info = String.format("SCĐ: %s", Utils.formatNumber(player.pointPro));
        }
        return info;
    }
}
