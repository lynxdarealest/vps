package com.beemobi.rongthanonline.map.expansion.dark;

import com.beemobi.rongthanonline.entity.monster.big.DarkDragon;
import com.beemobi.rongthanonline.entity.player.Player;
import com.beemobi.rongthanonline.map.Zone;
import com.beemobi.rongthanonline.map.Map;
import com.beemobi.rongthanonline.map.MapManager;
import com.beemobi.rongthanonline.map.MapName;
import com.beemobi.rongthanonline.map.expansion.Expansion;
import com.beemobi.rongthanonline.server.Server;
import com.beemobi.rongthanonline.util.Utils;

import java.util.List;
import java.util.stream.Collectors;

public class DarkVillage extends Expansion implements Runnable {

    public DarkVillage() {
        super(3600000);
        Map map = new Map(MapManager.getInstance().mapTemplates.get(MapName.LANG_CO_GIRA), this);
        for (int i = 0; i < map.template.minZone; i++) {
            Zone zone = new ZoneDarkVillage(map, this);
            DarkDragon darkDragon = new DarkDragon(7, i <= 4);
            zone.enterBigMonster(darkDragon);
            map.zones.add(zone);
        }
        maps.add(map);
        isRunning = true;
        new Thread(this).start();
        Server.getInstance().service.serverChat("Lối vào làng cổ Gira đã xuất hiện, hãy đến gặp NPC Trưởng lão để tham gia");
    }

    public void enter(Player player) {
        lock.lock();
        try {
            Map map = maps.get(0);
            List<Zone> zoneList;
            if (player.level < 55) {
                zoneList = map.zones.stream().filter(a -> a.id > 4).collect(Collectors.toList());
            } else {
                zoneList = map.zones.stream().filter(a -> a.id <= 4).collect(Collectors.toList());
            }
            player.teleport(zoneList.get(Utils.nextInt(zoneList.size())), true);
        } finally {
            lock.unlock();
        }
    }

    @Override
    public void close() {
        isRunning = false;
        for (Map map : maps) {
            map.close();
        }
        maps.clear();
        MapManager.getInstance().village = null;
    }

    @Override
    public void update() {
        if (isRunning && endTime < System.currentTimeMillis()) {
            close();
        }
    }

    @Override
    public void run() {
        while (isRunning) {
            long delay = 1000;
            try {
                long start = System.currentTimeMillis();
                update();
                long end = System.currentTimeMillis();
                long time = end - start;
                if (time > delay) {
                    continue;
                }
                Thread.sleep(delay - time);
            } catch (Exception ignored) {
            }
        }
    }
}
