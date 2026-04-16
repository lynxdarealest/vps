package com.beemobi.rongthanonline.bot.npc;

import com.beemobi.rongthanonline.bot.boss.Boss;
import com.beemobi.rongthanonline.entity.player.Player;
import com.beemobi.rongthanonline.service.Service;

public class Referee extends Player {

    public Referee() {
        super();
        id = Boss.autoIncrease++;
        this.name = "Trọng tài";
        hp = maxHp = mp = maxMp = baseHp = baseMp = 500;
        damage = baseDamage = 10;
        service = new Service(this);
        refreshPart();
    }

    @Override
    public boolean isBoss() {
        return false;
    }

    @Override
    public boolean isDisciple() {
        return false;
    }

    @Override
    public boolean isPlayer() {
        return false;
    }

    @Override
    public void refreshPart() {
        head = 329;
        body = 330;
    }

}
