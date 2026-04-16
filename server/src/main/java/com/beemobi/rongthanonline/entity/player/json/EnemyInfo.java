package com.beemobi.rongthanonline.entity.player.json;

import com.beemobi.rongthanonline.model.Enemy;
import com.google.gson.annotations.SerializedName;

public class EnemyInfo {
    @SerializedName("player_id")
    public int playerId;

    @SerializedName("name")
    public String name;

    @SerializedName("gender")
    public int gender;

    @SerializedName("power")
    public long power;

    public EnemyInfo(Enemy data) {
        playerId = data.playerId;
        name = data.name;
        gender = data.gender;
        power = data.power;
    }
}
