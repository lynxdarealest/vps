package com.beemobi.rongthanonline.bot.disciple.json;

import com.google.gson.annotations.SerializedName;

public class DisciplePowerInfo {
    @SerializedName("power")
    public long power;

    @SerializedName("potential")
    public long potential;

    @SerializedName("level")
    public int level;

    @SerializedName("limit_level")
    public int limitLevel;

    @SerializedName("power_time")
    public long updatePowerTime;

    public DisciplePowerInfo(long power, long potential, int level, int limitLevel, long updatePowerTime) {
        this.power = power;
        this.potential = potential;
        this.level = level;
        this.limitLevel = limitLevel;
        this.updatePowerTime = updatePowerTime;
    }
}
