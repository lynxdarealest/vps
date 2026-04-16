package com.beemobi.rongthanonline.entity.player.json;

import com.google.gson.annotations.SerializedName;

public class ItemOptionInfo {
    @SerializedName("id")
    public int id;

    @SerializedName("param")
    public int param;

    public ItemOptionInfo(int id, int param) {
        this.id = id;
        this.param = param;
    }
}
