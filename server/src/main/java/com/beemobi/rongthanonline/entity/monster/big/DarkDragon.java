package com.beemobi.rongthanonline.entity.monster.big;

import com.beemobi.rongthanonline.effect.Effect;
import com.beemobi.rongthanonline.effect.EffectName;
import com.beemobi.rongthanonline.entity.Entity;
import com.beemobi.rongthanonline.entity.monster.Monster;
import com.beemobi.rongthanonline.entity.player.Player;
import com.beemobi.rongthanonline.item.ItemManager;
import com.beemobi.rongthanonline.item.ItemMap;
import com.beemobi.rongthanonline.item.ItemName;
import com.beemobi.rongthanonline.item.OptionName;
import com.beemobi.rongthanonline.map.Zone;
import com.beemobi.rongthanonline.map.MapManager;
import com.beemobi.rongthanonline.map.expansion.dark.DarkVillage;
import com.beemobi.rongthanonline.util.Utils;
import org.apache.log4j.Logger;

import java.util.*;

public class DarkDragon extends BigMonster {
    private static final Logger logger = Logger.getLogger(DarkDragon.class);
    public final int star;

    public boolean isVip;

    public long lastTimeUpdate;

    public DarkDragon(int star, boolean isVip) {
        super(81 - star);
        this.star = star;
        if (isVip) {
            hp = maxHp = baseHp = 100_000_000_000L * (8 - star);
            percentDamage = 20;
        } else {
            hp = maxHp = baseHp = 100_000_000L * (8 - star);
            percentDamage = 5;
        }
        options[OptionName.GIAM_SAT_THUONG] = 1000 * star;
        this.isVip = isVip;
        switch (star) {
            case 2:
                options[OptionName.NE_DON] = 8000;
                break;
            case 7:
                options[OptionName.PHAN_SAT_THUONG] = 90000;
                break;
        }
    }

    @Override
    public void startDie(Entity killer) {
        Zone zone = this.zone;
        super.startDie(killer);
        if (star > 1) {
            Utils.setTimeout(() -> {
                DarkVillage village = MapManager.getInstance().village;
                if (village != null) {
                    zone.enterBigMonster(new DarkDragon(star - 1, isVip));
                }
            }, 10000);
        }
    }

    @Override
    public void update() {
        super.update();
        if (star == 3 || star == 4) {
            long now = System.currentTimeMillis();
            if (now - lastTimeUpdate > 10000) {
                lastTimeUpdate = now;
                List<Player> playerList = zone.getPlayers(Zone.TYPE_PLAYER);
                if (!playerList.isEmpty()) {
                    Collections.shuffle(playerList);
                    for (int i = 0; i < playerList.size(); i++) {
                        if (i >= 5) {
                            break;
                        }
                        Player player = playerList.get(i);
                        if (star == 3) {
                            player.addEffect(new Effect(player, EffectName.DONG_BANG, 5000));
                        } else {
                            player.addEffect(new Effect(player, EffectName.THIEU_DOT, 10000, 1000, 10));
                        }
                    }
                }
            }
        }
    }

    @Override
    public void throwItemWhenLeaveMap(Entity killer) {
        if (killer == null) {
            return;
        }
        List<ItemMap> itemMaps = new ArrayList<>();
        int length = Utils.nextInt(5, 10);
        for (int i = 0; i < length; i++) {
            int quantity = isVip ? Utils.nextInt(1000000, 1500000) : Utils.nextInt(100000, 200000);
            itemMaps.add(ItemManager.getInstance().createItemMap(ItemName.XU, quantity, -1));
        }
        if (!isVip) {
            length = Utils.nextInt(5, 10);
            for (int i = 0; i < length; i++) {
                itemMaps.add(ItemManager.getInstance().createItemMap(ItemName.THE_XU, 1, -1));
            }
            length = Utils.nextInt(5, 10);
            for (int i = 0; i < length; i++) {
                itemMaps.add(ItemManager.getInstance().createItemMap(ItemName.DA_9, 1, -1));
            }
        } else {
            length = Utils.nextInt(5, 10);
            for (int i = 0; i < length; i++) {
                itemMaps.add(ItemManager.getInstance().createItemMap(ItemName.DA_10, 1, -1));
            }
        }
        if (Utils.isPercent(isVip ? (10 * (8 - star)) : 10)) {
            int[] items = {ItemName.TRUNG_HAC_LONG, ItemName.TRUNG_HAN_LONG, ItemName.TRUNG_HOA_LONG};
            itemMaps.add(ItemManager.getInstance().createItemMap(items[Utils.nextInt(items.length)], 1, killer.id));
        }
        if (!itemMaps.isEmpty()) {
            this.zone.addItemMap(itemMaps, this.x);
        }
    }

    @Override
    public long formatDamageInjure(Entity attacker, long damage, boolean isCritical) {
        if (star == 5 || star == 6) {
            if (!(attacker instanceof Player)) {
                return 1;
            }
            Player player = (Player) attacker;
            if (star == 5 && player.mySkill != null && player.mySkill.isCanHapThu()) {
                return 0;
            }
            if (star == 6 && player.isHaveEffect(EffectName.BIEN_KHI, EffectName.HOA_KHONG_LO)) {
                return 0;
            }
        }
        damage = super.formatDamageInjure(attacker, damage, isCritical);
        if (!isVip || attacker instanceof Monster) {
            return Math.min(Math.max(hp / 100, 1), damage);
        }
        return damage;
    }

    @Override
    public long delayAttack() {
        return Math.max(2000 - enemies.size() * 400L, 1000);
    }

}
