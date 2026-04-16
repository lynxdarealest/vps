package com.beemobi.rongthanonline.entity.player.json;

import com.google.gson.annotations.SerializedName;

public class PointInfo {
    @SerializedName("active")
    public int active;

    @SerializedName("pk")
    public int pk;

    @SerializedName("survival")
    public int survival;

    @SerializedName("capsule")
    public int capsule;

    @SerializedName("spaceship")
    public int spaceship;

    @SerializedName("flag_war")
    public int flagWar;

    public PointInfo(int active, int pk, int survival, int capsule, int spaceship, int flagWar) {
        this.active = active;
        this.pk = pk;
        this.survival = survival;
        this.capsule = capsule;
        this.spaceship = spaceship;
        this.flagWar = flagWar;
    }
}
