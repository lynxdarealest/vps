package com.beemobi.rongthanonline.entity.player.json;

import com.google.gson.annotations.SerializedName;

public class MissionWeekInfo {
    @SerializedName("id")
    public int id;

    @SerializedName("type")
    public int type;

    public MissionWeekInfo(int id, int type) {
        this.id = id;
        this.type = type;
    }
}
