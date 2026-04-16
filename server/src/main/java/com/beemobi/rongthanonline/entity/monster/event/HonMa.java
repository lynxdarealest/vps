package com.beemobi.rongthanonline.entity.monster.event;

import com.beemobi.rongthanonline.bot.disciple.Disciple;
import com.beemobi.rongthanonline.entity.Entity;
import com.beemobi.rongthanonline.entity.monster.Monster;
import com.beemobi.rongthanonline.entity.monster.MonsterManager;
import com.beemobi.rongthanonline.entity.monster.pet.Pet;
import com.beemobi.rongthanonline.entity.player.Player;
import com.beemobi.rongthanonline.item.Item;
import com.beemobi.rongthanonline.item.ItemManager;
import com.beemobi.rongthanonline.item.ItemMap;
import com.beemobi.rongthanonline.item.ItemName;
import com.beemobi.rongthanonline.map.Map;
import com.beemobi.rongthanonline.map.MapManager;
import com.beemobi.rongthanonline.map.MapName;
import com.beemobi.rongthanonline.map.Zone;
import com.beemobi.rongthanonline.model.PointWeeklyType;
import com.beemobi.rongthanonline.util.Utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class HonMa extends Monster {

    public HonMa() {
        super();
        isAutoRefresh = false;
        template = MonsterManager.getInstance().monsterTemplates.get(113);
        hp = maxHp = baseHp = template.hp;
        damage = template.damage;
    }

    @Override
    public void startDie(Entity killer) {
        super.startDie(killer);
        Utils.setTimeout(() -> {
            zone.removeMonster(this);
        }, 5000);
        Utils.setTimeout(this::enter, 60000);
    }

    public void enter() {
        if (isDead()) {
            respawn();
        }
        int[] MAPS = {MapName.BO_SONG_PU, MapName.THAC_NUOC_KEISE,
                MapName.RUNG_NAM_FUKA, MapName.DOI_HOANG_AKA, MapName.THUNG_LUNG_MARIA,
                MapName.DONG_BANG_MIKA, MapName.CAO_NGUYEN_TAKA, MapName.RUNG_GOZA, MapName.LANG_ARU};
        List<Zone> zoneList = new ArrayList<>();
        for (int mapId : MAPS) {
            Map map = MapManager.getInstance().maps.get(mapId);
            if (map != null) {
                zoneList.addAll(map.zones.stream().filter(area -> area.id > 0 && area.id < 4 && area.isRunning).toList());
            }
        }
        if (!zoneList.isEmpty()) {
            Zone zone = zoneList.get(Utils.nextInt(zoneList.size()));
            this.x = Utils.nextInt(500, zone.map.template.width - 500);
            this.y = zone.map.getYSd(this.x) - 50;
            zone.enter(this);
        }
    }

    @Override
    public List<ItemMap> throwItem(Entity killer) {
        List<ItemMap> itemMaps = new ArrayList<>();
        if (killer instanceof Player player && killer.isPlayer()) {
            Arrays.stream(player.itemsBag).filter(i -> i != null && i.template.id == ItemName.GUONG_MA_THUAT)
                    .findFirst()
                    .ifPresent(item -> itemMaps.add(ItemManager.getInstance().createItemMap(ItemName.XU, Utils.nextInt(100000, 200000), killer.id)));
        }
        itemMaps.add(ItemManager.getInstance().createItemMap(ItemName.XU_KHOA, Utils.nextInt(10000, 20000), killer.id));
        return itemMaps;
    }

    @Override
    public long formatDamageInjure(Entity attacker, long damage, boolean isCritical) {
        return Math.max(maxHp / 100, Utils.nextInt(1, 10));
    }

    @Override
    public void injure(Entity attacker, long hpInjure, boolean isCritical, boolean isStrikeBack) {
        super.injure(attacker, hpInjure, isCritical, isStrikeBack);
        if (!isDead() && attacker != null && !attacker.isDead()) {
            attacker.injure(this, attacker.maxHp / 50, false, false);
        }
        if (isDead() && attacker != null) {
            Player player = null;
            if (attacker.isPlayer()) {
                player = (Player) attacker;
            } else if (attacker.isDisciple()) {
                player = ((Disciple) attacker).master;
            } else if (attacker.isPet()) {
                player = ((Pet) attacker).master;
            }
            if (player == null) {
                return;
            }
            player.lockAction.lock();
            try {
                if (player.isBagFull()) {
                    return;
                }
                Item item = Arrays.stream(player.itemsBag).filter(i -> i != null && i.template.id == ItemName.GUONG_MA_THUAT).findFirst().orElse(null);
                if (item != null) {
                    int index = item.indexUI;
                    item.quantity--;
                    if (item.quantity < 1) {
                        player.itemsBag[index] = null;
                    }
                    player.service.refreshItemBag(index);
                    Item newItem = ItemManager.getInstance().createItem(ItemName.GUONG_MA_THUAT_PHONG_AN, 1, true);
                    if (item.isLock) {
                        newItem.isLock = true;
                    }
                    player.addItem(newItem, true);
                    long damage = enemies.getOrDefault(player, 0L);
                    if (damage > 0) {
                        int point = (int) Math.max(maxHp, damage);
                        player.pointEvent += point;
                        player.updateTimeEvent = System.currentTimeMillis();
                        player.upPointWeekly(PointWeeklyType.EVENT, point);
                    }
                    player.pointRewardEvent += 3;
                }
            } finally {
                player.lockAction.unlock();
            }
        }
    }
}
