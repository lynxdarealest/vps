package com.beemobi.rongthanonline.model;


import com.beemobi.rongthanonline.entity.player.Player;
import com.beemobi.rongthanonline.entity.player.json.FriendInfo;

public class Friend {
    public int playerId;
    public String name;
    public int gender;
    public long power;
    public transient boolean isOnline;

    public Friend(FriendInfo info) {
        playerId = info.playerId;
        name = info.name;
        gender = info.gender;
        power = info.power;
    }

    public Friend(Player player) {
        playerId = player.id;
        name = player.name;
        gender = player.gender;
        power = player.power;
    }
}
