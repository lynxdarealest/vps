package com.beemobi.rongthanonline.top.player.disciple;

import com.beemobi.rongthanonline.entity.player.Player;
import com.beemobi.rongthanonline.top.player.TopPlayerInfo;
import com.beemobi.rongthanonline.util.Utils;

public class TopDiscipleInfo extends TopPlayerInfo {

    public TopDiscipleInfo(Player player) {
        super(player);
    }

    @Override
    public long getScore() {
        if (player != null) {
            score = player.power;
        }
        return score;
    }

    @Override
    public String getInfo() {
        if (player != null) {
            info = String.format("Lv%s: %s", player.level, Utils.formatNumber(player.power));
        }
        return info;
    }

    @Override
    public long getUpdateTime() {
        if (player != null) {
            updateTime = player.updatePowerTime;
        }
        return updateTime;
    }
}
