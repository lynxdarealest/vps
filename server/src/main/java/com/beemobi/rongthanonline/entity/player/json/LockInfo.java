package com.beemobi.rongthanonline.entity.player.json;

import com.google.gson.annotations.SerializedName;

import java.sql.Timestamp;

public class LockInfo {
    @SerializedName("chat_time")
    public long chatTime;

    @SerializedName("chat_info")
    public String chatInfo;

    @SerializedName("login_time")
    public long loginTime;

    @SerializedName("login_info")
    public String loginInfo;

    @SerializedName("trade_time")
    public long tradeTime;

    @SerializedName("trade_info")
    public String tradeInfo;
}
