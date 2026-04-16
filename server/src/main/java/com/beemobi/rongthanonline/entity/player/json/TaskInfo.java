package com.beemobi.rongthanonline.entity.player.json;

import com.google.gson.annotations.SerializedName;

public class TaskInfo {
    @SerializedName("id")
    public int id;

    @SerializedName("index")
    public int index;

    @SerializedName("param")
    public int param;

    public TaskInfo(int id, int index, int param) {
        this.id = id;
        this.index = index;
        this.param = param;
    }
}
