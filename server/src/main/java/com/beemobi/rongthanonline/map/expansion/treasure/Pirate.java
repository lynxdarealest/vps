package com.beemobi.rongthanonline.map.expansion.treasure;

import com.beemobi.rongthanonline.entity.player.Player;
import com.beemobi.rongthanonline.top.Top;
import com.beemobi.rongthanonline.top.TopManager;
import com.beemobi.rongthanonline.top.TopType;

public class Pirate {
    public int playerId;
    public String name;
    public int gender;
    public int flag;
    public int level;
    public long timeChangeMap;
    public int total;
    public int point;
    public long updateTime;

    public Pirate(Player player) {
        playerId = player.id;
        name = player.name;
        level = player.level;
        gender = player.gender;
    }

    public void upPoint(int point) {
        updateTime = System.currentTimeMillis();
        this.point += point;
        total += point;
    }
}
