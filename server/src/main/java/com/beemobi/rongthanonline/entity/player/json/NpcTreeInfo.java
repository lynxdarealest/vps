package com.beemobi.rongthanonline.entity.player.json;

import com.google.gson.annotations.SerializedName;

public class NpcTreeInfo {
    @SerializedName("id")
    public int id;

    @SerializedName("time")
    public int time;

    public NpcTreeInfo(int id, int time) {
        this.id = id;
        this.time = time;
    }
}
