package com.beemobi.rongthanonline.entity.player.json;

import com.google.gson.annotations.SerializedName;

public class TrainingOfflineInfo {
    @SerializedName("master")
    public long master;

    @SerializedName("disciple")
    public long disciple;

    public TrainingOfflineInfo(long master, long disciple) {
        this.master = master;
        this.disciple = disciple;
    }
}
