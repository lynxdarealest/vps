package com.beemobi.rongthanonline.map.expansion.island;

import com.beemobi.rongthanonline.entity.monster.Monster;
import com.beemobi.rongthanonline.entity.player.Player;
import com.beemobi.rongthanonline.map.*;
import com.beemobi.rongthanonline.map.expansion.Expansion;
import com.beemobi.rongthanonline.server.Server;
import com.beemobi.rongthanonline.util.Utils;

import java.util.ArrayList;
import java.util.List;

public class Island extends Expansion {
    private static final int[] MAP_IDS = {MapName.BANG_HOA_1, MapName.BANG_HOA_2, MapName.BANG_HOA_3, MapName.BANG_HOA_4,
            MapName.BANG_HOA_5, MapName.BANG_HOA_6, MapName.BANG_HOA_7, MapName.BANG_HOA_8,
            MapName.BANG_HOA_9, MapName.BANG_HOA_10, MapName.BANG_HOA_11, MapName.BANG_HOA_12,
            MapName.BANG_HOA_13, MapName.BANG_HOA_14, MapName.BANG_HOA_15};

    private static final long[] HP_MONSTER = {
            3000000, // 30
            6000000, // 35
            11000000, // 40
            16000000, // 45
            26000000, // 50
            36000000, // 55
            56000000, // 60
            76000000, // 65
            116000000, // 70
            156000000, // 75
            256000000, // 80
            356000000, // 85
            456000000, // 90
            556000000, // 95
            5560000000L}; // 100

    private static final long[] DAME_MONSTER = {
            5000, // 30
            10000, // 35
            20000, // 40
            50000, // 45
            100000, // 50
            200000, // 55
            500000, // 60
            1000000, // 65
            2000000, // 70
            5000000, // 75
            10000000, // 80
            20000000, // 85
            50000000, // 90
            100000000, // 95
            10000000000L};  // 100

    public Island() {
        super(-1);
        for (int i = 0; i < MAP_IDS.length; i++) {
            MapTemplate template = MapManager.getInstance().mapTemplates.get(MAP_IDS[i]);
            Map map = new Map(template, this);
            for (int j = 0; j < template.minZone; j++) {
                Zone zone = new ZoneIsland(map, this);
                for (Monster monster : zone.monsters) {
                    monster.hp = monster.maxHp = monster.baseHp = HP_MONSTER[i];
                    monster.level = 30 + 5 * i;
                    monster.damage = DAME_MONSTER[i];
                }
                map.zones.add(zone);
            }
            maps.add(map);
        }
        isRunning = true;
    }

    public void enter(Player player) {
        for (int i = maps.size() - 1; i >= 0; i--) {
            Map map = maps.get(i);
            Monster monster = map.zones.get(0).monsters.get(0);
            if (monster.isMonster() && player.level >= monster.level) {
                player.teleport(map, true);
                return;
            }
        }
        player.teleport(maps.get(0), true);
    }

    @Override
    public void close() {

    }

    @Override
    public void update() {

    }
}
