package com.beemobi.rongthanonline.task;

public class TaskDaily {
    public String name;
    public int objectId;
    public int maxParam;
    public int param;
    public int type;
    public String description;

    @Override
    public String toString() {
        return name + " [" + param + "/" + maxParam + "]";
    }
}
