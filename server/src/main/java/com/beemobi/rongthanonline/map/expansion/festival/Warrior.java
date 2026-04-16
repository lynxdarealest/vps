package com.beemobi.rongthanonline.map.expansion.festival;

import com.beemobi.rongthanonline.entity.player.Player;

public class Warrior {
    public int playerId;
    public String name;
    public int gender;
    public long power;
    public int level;

    public Warrior(Player player) {
        playerId = player.id;
        name = player.name;
        gender = player.gender;
        level = player.level;
        power = player.power;
    }
}
