package com.beemobi.rongthanonline.entity.player.json;

import com.google.gson.annotations.SerializedName;

public class RechargeInfo {
    @SerializedName("diamond")
    public int diamond;

    @SerializedName("is_day")
    public boolean isDay;

    public RechargeInfo(int diamond, boolean isDay) {
        this.diamond = diamond;
        this.isDay = isDay;
    }
}
