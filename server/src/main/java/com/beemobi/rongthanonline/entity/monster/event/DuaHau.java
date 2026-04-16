package com.beemobi.rongthanonline.entity.monster.event;

import com.beemobi.rongthanonline.entity.Entity;
import com.beemobi.rongthanonline.entity.monster.Monster;
import com.beemobi.rongthanonline.entity.monster.MonsterManager;
import com.beemobi.rongthanonline.entity.monster.MonsterName;
import com.beemobi.rongthanonline.item.ItemManager;
import com.beemobi.rongthanonline.item.ItemMap;
import com.beemobi.rongthanonline.item.ItemName;
import com.beemobi.rongthanonline.util.Utils;

import java.util.ArrayList;
import java.util.List;

public class DuaHau extends Monster {

    public DuaHau() {
        super();
        isAutoRefresh = false;
        template = MonsterManager.getInstance().monsterTemplates.get(MonsterName.DUA_HAU);
        hp = maxHp = baseHp = template.hp;
        damage = template.damage;
    }

    @Override
    public void startDie(Entity killer) {
        super.startDie(killer);
        Utils.setTimeout(() -> {
            zone.removeMonster(this);
        }, 5000);
    }

    @Override
    public List<ItemMap> throwItem(Entity killer) {
        List<ItemMap> itemMaps = new ArrayList<>();
        itemMaps.add(ItemManager.getInstance().createItemMap(ItemName.DUA_HAU, 1, killer.id));
        return itemMaps;
    }

    @Override
    public long formatDamageInjure(Entity attacker, long damage, boolean isCritical) {
        return Math.max(maxHp / 100, 1L);
    }
}
