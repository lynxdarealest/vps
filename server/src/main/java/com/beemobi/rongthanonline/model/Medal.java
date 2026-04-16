package com.beemobi.rongthanonline.model;

import com.beemobi.rongthanonline.data.MedalData;
import com.beemobi.rongthanonline.util.Utils;
import com.google.gson.reflect.TypeToken;

public class Medal {
    public int id;
    public int[] icons;
    public int dx;
    public int dy;
    public int delay;

    public Medal(MedalData data) {
        id = data.id;
        dx = data.dx;
        dy = data.dy;
        delay = data.delay;
        icons = Utils.gson.fromJson(data.icons, new TypeToken<int[]>() {
        }.getType());
    }
}
