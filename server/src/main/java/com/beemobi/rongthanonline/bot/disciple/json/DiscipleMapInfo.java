package com.beemobi.rongthanonline.bot.disciple.json;

import com.google.gson.annotations.SerializedName;

public class DiscipleMapInfo {

    @SerializedName("hp")
    public long hp;

    @SerializedName("mp")
    public long mp;

    @SerializedName("stamina")
    public int stamina;

    public DiscipleMapInfo(long hp, long mp, int stamina) {
        this.hp = hp;
        this.mp = mp;
        this.stamina = stamina;
    }
}
