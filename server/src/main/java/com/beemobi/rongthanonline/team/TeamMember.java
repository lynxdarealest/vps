package com.beemobi.rongthanonline.team;

import com.beemobi.rongthanonline.entity.player.Player;
import com.beemobi.rongthanonline.map.Zone;

public class TeamMember {
    public int playerId;
    public String name;
    public transient Zone zone;
    public transient long power;
    public int gender;

    public TeamMember(Player player) {
        playerId = player.id;
        name = player.name;
        power = player.power;
        zone = player.zone;
        gender = player.gender;
    }
}
