package com.beemobi.rongthanonline.entity.player.json;

import com.google.gson.annotations.SerializedName;


public class OnlineInfo {
    @SerializedName("total")
    public int total;

    @SerializedName("day")
    public int day;

    public OnlineInfo(int total, int day) {
        this.total = total;
        this.day = day;
    }
}
