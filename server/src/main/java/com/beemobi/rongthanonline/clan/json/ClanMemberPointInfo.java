package com.beemobi.rongthanonline.clan.json;

import com.google.gson.annotations.SerializedName;

public class ClanMemberPointInfo {
    @SerializedName("total")
    public int total;

    @SerializedName("day")
    public int day;

    public ClanMemberPointInfo(int total, int day) {
        this.total = total;
        this.day = day;
    }
}
