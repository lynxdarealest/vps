package com.beemobi.rongthanonline.entity.player.json;

import com.google.gson.annotations.SerializedName;

public class EventInfo {
    @SerializedName("point")
    public int point;

    @SerializedName("point_other")
    public int pointOther;

    @SerializedName("index_reward")
    public int indexReward;

    @SerializedName("point_reward")
    public int pointReward;

    @SerializedName("update_time")
    public long updateTime;

    @SerializedName("other_time")
    public long updateOtherTime;


    public EventInfo(int point, int pointOther, int indexReward, int pointReward, long updateTime, long updateOtherTime) {
        this.point = point;
        this.pointOther = pointOther;
        this.indexReward = indexReward;
        this.pointReward = pointReward;
        this.updateTime = updateTime;
        this.updateOtherTime = updateOtherTime;
    }

    public EventInfo() {

    }
}
