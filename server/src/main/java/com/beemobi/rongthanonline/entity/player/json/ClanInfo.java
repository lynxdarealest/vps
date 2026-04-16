package com.beemobi.rongthanonline.entity.player.json;

import com.google.gson.annotations.SerializedName;

public class ClanInfo {
    @SerializedName("id")
    public int id;

    @SerializedName("manor")
    public int manor;

    @SerializedName("time")
    public long time;

    public ClanInfo(int id, int manor, long time) {
        this.id = id;
        this.manor = manor;
        this.time = time;
    }
}
