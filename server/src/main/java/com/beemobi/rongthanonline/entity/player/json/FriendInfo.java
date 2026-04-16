package com.beemobi.rongthanonline.entity.player.json;

import com.beemobi.rongthanonline.model.Friend;
import com.google.gson.annotations.SerializedName;

public class FriendInfo {
    @SerializedName("player_id")
    public int playerId;

    @SerializedName("name")
    public String name;

    @SerializedName("gender")
    public int gender;

    @SerializedName("power")
    public long power;

    public FriendInfo(Friend data) {
        playerId = data.playerId;
        name = data.name;
        gender = data.gender;
        power = data.power;
    }
}
