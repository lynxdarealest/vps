package com.beemobi.rongthanonline.effect;

import com.beemobi.rongthanonline.data.EffectImageData;
import com.beemobi.rongthanonline.util.Utils;
import com.google.gson.reflect.TypeToken;

public class EffectImage {
    public int id;
    public int[] icons;
    public int dx;
    public int dy;
    public int delay;

    public EffectImage(EffectImageData data) {
        id = data.id;
        dx = data.dx;
        dy = data.dy;
        delay = data.delay;
        icons = Utils.gson.fromJson(data.icons, new TypeToken<int[]>() {
        }.getType());
    }
}
