package com.beemobi.rongthanonline.map.expansion.survival;

import com.beemobi.rongthanonline.entity.player.Player;

public class Gamer {
    public int playerId;
    public String name;
    public int gender;
    public long startTime;
    public long endTime;

    public Gamer(Player player) {
        playerId = player.id;
        name = player.name;
        gender = player.gender;
    }

    public Gamer(Player player, long now) {
        playerId = player.id;
        name = player.name;
        gender = player.gender;
        startTime = now;
    }
}
