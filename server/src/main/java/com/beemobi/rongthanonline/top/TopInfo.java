package com.beemobi.rongthanonline.top;

import lombok.Getter;

@Getter
public abstract class TopInfo {
    public int id;
    public String name;
    public int gender;
    public long score;
    public String info;
    public boolean isOnline;
    public long updateTime;

    public abstract void setObject(Object object);

    public abstract void clearObject(Object object);
}
