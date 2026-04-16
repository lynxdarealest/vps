package com.beemobi.rongthanonline.skill;

import com.beemobi.rongthanonline.entity.Entity;

public class IntrinsicAttack {
    public Entity entity;
    public int percent;

    public IntrinsicAttack(Entity entity, int percent) {
        this.entity = entity;
        this.percent = percent;
    }
}
