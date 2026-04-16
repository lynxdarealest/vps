package com.beemobi.rongthanonline.task;

public class TaskClan {
    public String name;
    public int maxParam;
    public int param;
    public int type;
    public int objectId;
    public long startTime;

    @Override
    public String toString() {
        return name + " [" + param + "/" + maxParam + "]";
    }
}
