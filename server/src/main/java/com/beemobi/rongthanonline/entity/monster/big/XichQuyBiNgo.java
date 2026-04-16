package com.beemobi.rongthanonline.entity.monster.big;

import com.beemobi.rongthanonline.entity.Entity;
import com.beemobi.rongthanonline.item.ItemManager;
import com.beemobi.rongthanonline.item.ItemMap;
import com.beemobi.rongthanonline.item.ItemName;
import com.beemobi.rongthanonline.map.Zone;
import com.beemobi.rongthanonline.map.Map;
import com.beemobi.rongthanonline.map.MapManager;
import com.beemobi.rongthanonline.map.MapName;
import com.beemobi.rongthanonline.util.Utils;

import java.util.ArrayList;
import java.util.List;

public class XichQuyBiNgo extends BigMonster {

    public static final int[] MAPS = {MapName.BO_SONG_PU, MapName.THAC_NUOC_KEISE,
            MapName.RUNG_NAM_FUKA, MapName.DOI_HOANG_AKA, MapName.THUNG_LUNG_MARIA,
            MapName.DONG_BANG_MIKA, MapName.CAO_NGUYEN_TAKA, MapName.RUNG_GOZA, MapName.LANG_ARU};

    public XichQuyBiNgo() {
        super(73);
        hp = maxHp = baseHp = template.hp;
        percentDamage = 20;
        isHasPoint = false;
    }

    @Override
    public void startDie(Entity killer) {
        super.startDie(killer);
        Utils.setTimeout(() -> {
            Map map = MapManager.getInstance().maps.get(MAPS[Utils.nextInt(MAPS.length)]);
            if (map != null) {
                Zone zone = map.findOrRandomZone(-2);
                zone.enterBigMonster(new XichQuyBiNgo());
            }
        }, Utils.nextInt(300000, 600000));
    }

    @Override
    public long delayAttack() {
        return 2000;
    }

    @Override
    public long formatDamageInjure(Entity attacker, long damage, boolean isCritical) {
        return Math.min(damage, maxHp / 200);
    }

    @Override
    public void throwItemWhenLeaveMap(Entity killer) {
        if (killer == null || !killer.isPlayer()) {
            return;
        }
        List<ItemMap> itemMaps = new ArrayList<>();
        int length = Utils.nextInt(5, 10);
        for (int i = 0; i < length; i++) {
            itemMaps.add(ItemManager.getInstance().createItemMap(ItemName.XU_KHOA, Utils.nextInt(20000, 50000), -1));
        }
        length = Utils.nextInt(5, 10);
        for (int i = 0; i < length; i++) {
            itemMaps.add(ItemManager.getInstance().createItemMap(ItemName.BI_NGO, Utils.nextInt(5, 10), -1));
        }
        itemMaps.add(ItemManager.getInstance().createItemMap(ItemName.RUBY, 1, -1));
        zone.addItemMap(itemMaps, this.x);
    }
}
