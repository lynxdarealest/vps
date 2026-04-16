package com.beemobi.rongthanonline.entity.player.json;

import com.beemobi.rongthanonline.achievement.Achievement;
import com.google.gson.annotations.SerializedName;

public class AchievementInfo {
    @SerializedName("id")
    public int id;

    @SerializedName("param")
    public int param;

    @SerializedName("is_received")
    public boolean isReceived;

    public AchievementInfo(Achievement info) {
        id = info.template.id;
        param = info.param;
        isReceived = info.isReceived;
    }
}
