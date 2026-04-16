package com.beemobi.rongthanonline.entity.player.json;

import com.google.gson.annotations.SerializedName;

public class SkillInfo {
    @SerializedName("id")
    public int id;

    @SerializedName("level")
    public int level;

    @SerializedName("upgrade")
    public int upgrade;

    @SerializedName("point")
    public int point;

    @SerializedName("time_can_use")
    public long timeCanUse;

    public SkillInfo(int id, int level, int upgrade, int point, long timeCanUse) {
        this.id = id;
        this.level = level;
        this.upgrade = upgrade;
        this.point = point;
        this.timeCanUse = timeCanUse;
    }
}
