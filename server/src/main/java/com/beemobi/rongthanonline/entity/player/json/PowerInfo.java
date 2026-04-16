package com.beemobi.rongthanonline.entity.player.json;

import com.google.gson.annotations.SerializedName;

public class PowerInfo {
    @SerializedName("power")
    public long power;

    @SerializedName("potential")
    public long potential;

    @SerializedName("level")
    public int level;

    @SerializedName("limit_level")
    public int limitLevel;

    @SerializedName("point_skill")
    public int pointSkill;

    @SerializedName("power_time")
    public long updatePowerTime;

    public PowerInfo(long power, long potential, int level, int limitLevel, int pointSkill, long updatePowerTime) {
        this.power = power;
        this.potential = potential;
        this.level = level;
        this.limitLevel = limitLevel;
        this.pointSkill = pointSkill;
        this.updatePowerTime = updatePowerTime;
    }
}
