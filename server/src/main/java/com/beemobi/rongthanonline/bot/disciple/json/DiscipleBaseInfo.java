package com.beemobi.rongthanonline.bot.disciple.json;

import com.google.gson.annotations.SerializedName;

public class DiscipleBaseInfo {
    @SerializedName("damage")
    public int damage;

    @SerializedName("hp")
    public int hp;

    @SerializedName("mp")
    public int mp;

    @SerializedName("constitution")
    public int constitution;

    public DiscipleBaseInfo(int damage, int hp, int mp, int constitution) {
        this.damage = damage;
        this.hp = hp;
        this.mp = mp;
        this.constitution = constitution;
    }
}
