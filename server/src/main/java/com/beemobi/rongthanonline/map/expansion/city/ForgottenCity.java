package com.beemobi.rongthanonline.map.expansion.city;

import com.beemobi.rongthanonline.entity.monster.big.Hirudegarn;
import com.beemobi.rongthanonline.entity.monster.big.SuperHirudegarn;
import com.beemobi.rongthanonline.map.Zone;
import com.beemobi.rongthanonline.map.Map;
import com.beemobi.rongthanonline.map.MapManager;
import com.beemobi.rongthanonline.map.MapName;
import com.beemobi.rongthanonline.map.expansion.Expansion;
import com.beemobi.rongthanonline.server.Server;

public class ForgottenCity extends Expansion implements Runnable {

    public ForgottenCity() {
        super(3600000);
        Map map = new Map(MapManager.getInstance().mapTemplates.get(MapName.THANH_PHO_LANG_QUEN), this);
        for (int i = 0; i < map.template.minZone; i++) {
            map.zones.add(new ZoneForgottenCity(map, this));
        }
        Hirudegarn.count = 0;
        SuperHirudegarn.count = 0;
        Zone zone = map.zones.get(0);
        zone.enterBigMonster(new Hirudegarn());
        maps.add(map);
        isRunning = true;
        new Thread(this).start();
        Server.getInstance().service.serverChat("Thành phố lãng quên đã mở cửa");
    }

    @Override
    public void close() {
        isRunning = false;
        for (Map map : maps) {
            map.close();
        }
        maps.clear();
        MapManager.getInstance().forgottenCity = null;
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
                long l1 = System.currentTimeMillis();
                update();
                long l2 = System.currentTimeMillis();
                long l3 = l2 - l1;
                if (l3 > delay) {
                    continue;
                }
                Thread.sleep(delay - l3);
            } catch (Exception ignored) {
            }
        }
    }
}
