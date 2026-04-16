package com.beemobi.rongthanonline.map.expansion.spaceship;

import com.beemobi.rongthanonline.entity.player.Player;
import com.beemobi.rongthanonline.map.Map;
import com.beemobi.rongthanonline.map.MapManager;
import com.beemobi.rongthanonline.map.MapName;
import com.beemobi.rongthanonline.map.Zone;
import com.beemobi.rongthanonline.map.expansion.Expansion;
import com.beemobi.rongthanonline.server.Server;
import com.beemobi.rongthanonline.util.Utils;

import java.util.List;

public class Spaceship extends Expansion implements Runnable {
    public static int[] MAPS = {MapName.SPACESHIP_1, MapName.SPACESHIP_2, MapName.SPACESHIP_3, MapName.SPACESHIP_4};

    public Spaceship() {
        super(3600000);
        for (int i = 0; i < MAPS.length; i++) {
            Map map = new Map(MapManager.getInstance().mapTemplates.get(MAPS[i]), this);
            int count = i <= 1 ? 3 : 1;
            for (int j = 0; j < count; j++) {
                map.zones.add(new ZoneSpaceship(map, this, i));
            }
            maps.add(map);
        }
        isRunning = true;
        new Thread(this).start();
        Server.getInstance().service.serverChat("Cổng phi thuyền đã mở cửa, tham gia tại NPC Kaioshin (Đảo Kamê)");
    }

    public void enter(Player player) {
        List<Zone> zoneList = maps.get(0).zones;
        player.teleport(zoneList.get(Utils.nextInt(zoneList.size())), true);
    }

    public void next(Player player) {
        if (!(player.zone instanceof ZoneSpaceship)) {
            return;
        }
        if (player.zone.map.template.id == MapName.SPACESHIP_4) {
            return;
        }
        if (player.pointCurrSpaceship < 3) {
            player.addInfo(Player.INFO_RED, "Ngươi chưa tích đủ năng lượng");
            return;
        }
        List<Zone> zoneList = maps.get(maps.indexOf(player.zone.map) + 1).zones;
        player.teleport(zoneList.get(Utils.nextInt(zoneList.size())), true);
    }

    @Override
    public void close() {
        isRunning = false;
        for (Map map : maps) {
            map.close();
        }
        maps.clear();
        MapManager.getInstance().spaceship = null;
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
