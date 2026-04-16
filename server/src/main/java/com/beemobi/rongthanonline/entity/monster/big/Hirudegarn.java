package com.beemobi.rongthanonline.entity.monster.big;

import com.beemobi.rongthanonline.entity.Entity;
import com.beemobi.rongthanonline.entity.player.Player;
import com.beemobi.rongthanonline.item.ItemManager;
import com.beemobi.rongthanonline.item.ItemMap;
import com.beemobi.rongthanonline.item.ItemName;
import com.beemobi.rongthanonline.map.Zone;
import com.beemobi.rongthanonline.map.MapManager;
import com.beemobi.rongthanonline.map.expansion.city.ForgottenCity;
import com.beemobi.rongthanonline.util.Utils;

import java.util.*;

public class Hirudegarn extends BigMonster {
    public static int count;

    public Hirudegarn() {
        super(35);
        count++;
        level = count;
        baseHp = template.hp;
        for (int i = 0; i < level; i++) {
            baseHp += baseHp / 10;
        }
        hp = maxHp = baseHp;
    }

    @Override
    public long formatDamageInjure(Entity attacker, long damage, boolean isCritical) {
        long dmg = super.formatDamageInjure(attacker, damage, isCritical);
        return Math.min(dmg, maxHp / 100);
    }

    @Override
    public void startDie(Entity killer) {
        super.startDie(killer);
        Utils.setTimeout(() -> {
            ForgottenCity forgottenCity = MapManager.getInstance().forgottenCity;
            if (forgottenCity != null) {
                Zone zone = forgottenCity.maps.get(0).findOrRandomZone(-2);
                zone.enterBigMonster(new SuperHirudegarn());
            }
        }, 15000);
        if (killer != null && killer.isPlayer()) {
            Player player = (Player) killer;
            if (player.clan != null) {
                List<Player> playerList = zone.getPlayers(Zone.TYPE_PLAYER);
                boolean flag = false;
                for (Player p : playerList) {
                    if (!p.isDead() && (p.clan == null || p.clan != player.clan)) {
                        flag = true;
                        break;
                    }
                }
                if (!flag) {
                    int point = playerList.size();
                    player.clan.upExp(point, false);
                    player.clan.upPointMember(player.id, point);
                }
            }
        }
    }

    @Override
    public void throwItemWhenLeaveMap(Entity killer) {
        if (killer == null || !killer.isPlayer()) {
            return;
        }
        List<Map.Entry<Player, Long>> list
                = new ArrayList<>(enemies.entrySet());
        list.sort(new Comparator<Map.Entry<Player, Long>>() {
            // Comparing two entries by value
            @Override
            public int compare(
                    Map.Entry<Player, Long> entry1,
                    Map.Entry<Player, Long> entry2) {
                // Subtracting the entries
                return entry2.getValue().compareTo(entry1.getValue());
            }
        });
        long min = maxHp / 100;
        ArrayList<Integer> players = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            Player player = list.get(i).getKey();
            if (i < 3) {
                if (player.zone == this.zone) {
                    player.addItem(ItemManager.getInstance().createItem(ItemName.LOI_KI_NANG, 6 - i * 2, true), true);
                }
            }
            if (list.get(i).getValue() >= min) {
                players.add(player.id);
            }
        }
        List<ItemMap> itemMaps = new ArrayList<>();
        Player player = (Player) killer;
        player.addItem(ItemManager.getInstance().createItem(ItemName.LOI_KI_NANG, 5, true), true);
        int length = Utils.nextInt(5, 10);
        for (int i = 0; i < length; i++) {
            itemMaps.add(ItemManager.getInstance().createItemMap(ItemName.XU, Utils.nextInt(100000, 200000), -1));
        }
        length = Utils.nextInt(3, 5);
        for (int i = 0; i < length; i++) {
            itemMaps.add(ItemManager.getInstance().createItemMap(ItemName.LOI_KI_NANG, 1, -1));
        }
        if (!itemMaps.isEmpty()) {
            for (ItemMap itemMap : itemMaps) {
                itemMap.players = new ArrayList<>(players);
            }
            this.zone.addItemMap(itemMaps, this.x);
        }
    }

}
