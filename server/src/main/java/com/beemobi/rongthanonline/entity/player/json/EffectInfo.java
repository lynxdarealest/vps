package com.beemobi.rongthanonline.entity.player.json;

import com.google.gson.annotations.SerializedName;

public class EffectInfo {
    @SerializedName("id")
    public int id;

    @SerializedName("delay")
    public long delay;

    @SerializedName("param")
    public int param;

    @SerializedName("time")
    public long time;

    @SerializedName("end_time")
    public long endTime;

    @SerializedName("update_time")
    public long updateTime;

    public EffectInfo(int id, long delay, int param, long time, long endTime, long updateTime) {
        this.id = id;
        this.delay = delay;
        this.param = param;
        this.time = time;
        this.endTime = endTime;
        this.updateTime = updateTime;
    }
}
