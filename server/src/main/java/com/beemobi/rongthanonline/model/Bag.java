package com.beemobi.rongthanonline.model;

import com.beemobi.rongthanonline.data.BagData;
import com.beemobi.rongthanonline.util.Utils;
import com.google.gson.reflect.TypeToken;

public class Bag {
    public int id;
    public int[] icons;
    public int dxFly;
    public int dyFly;
    public int delay;
    public boolean isFly;

    public Bag(BagData data) {
        id = data.id;
        dxFly = data.dxFly;
        dyFly = data.dyFly;
        delay = data.delay;
        isFly = data.isFly;
        icons = Utils.gson.fromJson(data.icons, new TypeToken<int[]>() {
        }.getType());
    }
}
