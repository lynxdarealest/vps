package com.beemobi.rongthanonline.entity.player.json;

import com.google.gson.annotations.SerializedName;

public class MapInfo {
    @SerializedName("map_id")
    public int mapId;

    @SerializedName("x")
    public int x;

    @SerializedName("y")
    public int y;

    @SerializedName("hp")
    public long hp;

    @SerializedName("mp")
    public long mp;

    public MapInfo(int mapId, int x, int y, long hp, long mp) {
        this.mapId = mapId;
        this.x = x;
        this.y = y;
        this.hp = hp;
        this.mp = mp;
    }
}
