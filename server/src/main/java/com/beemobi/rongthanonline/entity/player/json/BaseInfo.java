package com.beemobi.rongthanonline.entity.player.json;

import com.google.gson.annotations.SerializedName;

public class BaseInfo {
    @SerializedName("damage")
    public int damage;

    @SerializedName("hp")
    public int hp;

    @SerializedName("mp")
    public int mp;

    @SerializedName("constitution")
    public int constitution;

    public BaseInfo(int damage, int hp, int mp, int constitution) {
        this.damage = damage;
        this.hp = hp;
        this.mp = mp;
        this.constitution = constitution;
    }


}
