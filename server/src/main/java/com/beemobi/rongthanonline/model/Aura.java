package com.beemobi.rongthanonline.model;

import com.beemobi.rongthanonline.data.AuraData;
import com.beemobi.rongthanonline.data.MedalData;
import com.beemobi.rongthanonline.util.Utils;
import com.google.gson.reflect.TypeToken;

public class Aura {
    public int id;
    public int[] icons;
    public int dx;
    public int dy;
    public int delay;

    public Aura(AuraData data) {
        id = data.id;
        dx = data.dx;
        dy = data.dy;
        delay = data.delay;
        icons = Utils.gson.fromJson(data.icons, new TypeToken<int[]>() {
        }.getType());
    }
}
