package com.beemobi.rongthanonline.entity.player.json;

import com.google.gson.annotations.SerializedName;

public class IntrinsicInfo {
    @SerializedName("id")
    public int id;

    @SerializedName("param")
    public int param;

    public IntrinsicInfo(int id, int param) {
        this.id = id;
        this.param = param;
    }
}
