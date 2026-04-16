package com.beemobi.rongthanonline.entity.monster.event;

import com.beemobi.rongthanonline.entity.Entity;
import com.beemobi.rongthanonline.entity.monster.Monster;
import com.beemobi.rongthanonline.entity.monster.MonsterManager;
import com.beemobi.rongthanonline.item.ItemManager;
import com.beemobi.rongthanonline.item.ItemMap;
import com.beemobi.rongthanonline.item.ItemName;
import com.beemobi.rongthanonline.util.Utils;

import java.util.ArrayList;
import java.util.List;

public class ThoNgoc extends Monster {

    public ThoNgoc() {
        super();
        isAutoRefresh = false;
        template = MonsterManager.getInstance().monsterTemplates.get(66);
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
        if (Utils.isPercent(50)) {
            itemMaps.add(ItemManager.getInstance().createItemMap(ItemName.MANH_THO_NGOC_SU_KIEN_TRUNG_THU_2023, 1, killer.id));
        }
        if (Utils.nextInt(100) < 5) {
            itemMaps.add(ItemManager.getInstance().createItemMap(ItemName.RUBY, 1, -1));
        }
        itemMaps.add(ItemManager.getInstance().createItemMap(ItemName.XU_KHOA, 10000, -1));
        return itemMaps;
    }

    @Override
    public long formatDamageInjure(Entity attacker, long damage, boolean isCritical) {
        return Math.max(maxHp / 100, 1L);
    }

    @Override
    public void injure(Entity attacker, long hpInjure, boolean isCritical, boolean isStrikeBack) {
        super.injure(attacker, hpInjure, isCritical, isStrikeBack);
        if (!isDead() && attacker != null && !attacker.isDead()) {
            attacker.injure(this, attacker.maxHp / 20, false, false);
        }
    }
}
