package com.beemobi.rongthanonline.model;

import com.beemobi.rongthanonline.entity.player.Player;
import com.beemobi.rongthanonline.entity.player.json.EnemyInfo;

public class Enemy {
    public int playerId;
    public String name;
    public int gender;
    public long power;
    public transient boolean isOnline;

    public Enemy(EnemyInfo info) {
        playerId = info.playerId;
        name = info.name;
        gender = info.gender;
        power = info.power;
    }

    public Enemy(Player player) {
        playerId = player.id;
        name = player.name;
        gender = player.gender;
        power = player.power;
    }
}
