package com.beemobi.rongthanonline.entity.monster;

import com.beemobi.rongthanonline.data.MonsterTemplateData;
import com.beemobi.rongthanonline.util.Utils;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;

public class MonsterTemplate {
    public int id;
    public int rangeMove;
    public int speed;
    public MonsterTypeMove typeMove;
    public long hp;
    public String name;
    public int level;
    public int damage;
    public ArrayList<Integer> iconsMove;
    public int iconsAttack;
    public int iconsInjure;
    public int dartId;
    public int w;
    public int h;
    public int dx;
    public int dy;

    public MonsterTemplate(MonsterTemplateData data) {
        id = data.id;
        name = data.name;
        speed = data.speed;
        typeMove = data.typeMove;
        rangeMove = data.rangeMove;
        hp = data.hp;
        level = data.level;
        damage = data.damage;
        dartId = data.dartId;
        iconsMove = Utils.gson.fromJson(data.iconsMove, new TypeToken<ArrayList<Integer>>() {
        }.getType());
        iconsInjure = data.iconsInjure;
        iconsAttack = data.iconsAttack;
        w = data.w;
        h = data.h;
        dx = data.dx;
        dy = data.dy;
    }
}
