package com.beemobi.rongthanonline.map.expansion.sanctuary;

import com.beemobi.rongthanonline.entity.monster.big.Hirudegarn;
import com.beemobi.rongthanonline.entity.monster.big.SuperHirudegarn;
import com.beemobi.rongthanonline.entity.player.Player;
import com.beemobi.rongthanonline.map.Map;
import com.beemobi.rongthanonline.map.MapManager;
import com.beemobi.rongthanonline.map.MapName;
import com.beemobi.rongthanonline.map.Zone;
import com.beemobi.rongthanonline.map.expansion.Expansion;
import com.beemobi.rongthanonline.server.Server;
import com.beemobi.rongthanonline.util.Utils;

import java.util.List;

public class Sanctuary extends Expansion implements Runnable {
    public static int[] MAPS = {MapName.THANH_DIA, MapName.BUNG_BU};

    public Sanctuary() {
        super(3600000);
        Map map = new Map(MapManager.getInstance().mapTemplates.get(MapName.THANH_DIA), this);
        for (int i = 0; i < 9; i++) {
            map.zones.add(new ZoneSanctuary(map, this));
        }
        maps.add(map);
        isRunning = true;
        new Thread(this).start();
        Server.getInstance().service.serverChat("Thánh địa Kaio đã mở cửa, tham gia tại NPC Kaioshin (Đảo Kamê)");
    }

    public void enter(Player player) {
        List<Zone> zoneList = maps.get(0).zones;
        player.teleport(zoneList.get(Utils.nextInt(zoneList.size())), true);
    }

    @Override
    public void close() {
        isRunning = false;
        for (Map map : maps) {
            map.close();
        }
        maps.clear();
        MapManager.getInstance().sanctuary = null;
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
