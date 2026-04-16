package com.beemobi.rongthanonline.npc;

import com.beemobi.rongthanonline.data.NpcTemplateData;
import com.beemobi.rongthanonline.util.Utils;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;

public class NpcTemplate {
    public int id;

    public String name;

    public ArrayList<Integer> icons;

    public int avatar;
    public int dx;
    public int dy;
    public int w;
    public int h;

    public NpcTemplate(NpcTemplateData data) {
        id = data.id;
        name = data.name;
        avatar = data.avatar;
        icons = Utils.gson.fromJson(data.icons, new TypeToken<ArrayList<Integer>>() {
        }.getType());
        w = data.w;
        h = data.h;
        dx = data.dx;
        dy = data.dy;
    }
}
