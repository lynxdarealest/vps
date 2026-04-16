package com.beemobi.rongthanonline.bot.boss;

import com.beemobi.rongthanonline.data.BossTemplateData;

public class BossTemplate {
    public int id;

    public String name;

    public long hp;

    public int head;

    public int body;

    public int level;

    public long damage;

    public boolean isAutoRespawn;

    public BossTemplate(BossTemplateData data) {
        id = data.id;
        name = data.name;
        hp = data.hp;
        head = data.head;
        body = data.body;
        level = data.level;
        damage = data.damage;
        isAutoRespawn = data.isAutoRespawn;
    }
}
