package com.beemobi.rongthanonline.model;

import com.beemobi.rongthanonline.data.LevelData;

public class Level {
    public int id;
    public String name;
    public long power;

    public Level(LevelData data) {
        id = data.id;
        name = data.name;
        power = data.power;
    }
}
