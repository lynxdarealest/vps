package com.beemobi.rongthanonline.entity.player.json;

import com.beemobi.rongthanonline.task.TaskDaily;
import com.google.gson.annotations.SerializedName;

public class TaskDailyInfo {
    @SerializedName("count")
    public int count;

    @SerializedName("completed")
    public int countCompleted;

    @SerializedName("task")
    public TaskDaily taskDaily;

    @SerializedName("create_time")
    public long createTime;
}
