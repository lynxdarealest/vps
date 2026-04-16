package com.beemobi.rongthanonline.entity.player.json;

import com.google.gson.annotations.SerializedName;

public class BarrackInfo {
    @SerializedName("count")
    public int count;

    @SerializedName("num_buy")
    public int numBuy;

    @SerializedName("point")
    public int point;

    public BarrackInfo(int count, int numBuy, int point) {
        this.count = count;
        this.numBuy = numBuy;
        this.point = point;
    }
}
