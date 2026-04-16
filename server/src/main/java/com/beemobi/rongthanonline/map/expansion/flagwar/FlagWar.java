package com.beemobi.rongthanonline.map.expansion.flagwar;

import com.beemobi.rongthanonline.entity.player.Player;
import com.beemobi.rongthanonline.map.Map;
import com.beemobi.rongthanonline.map.MapManager;
import com.beemobi.rongthanonline.map.MapName;
import com.beemobi.rongthanonline.map.expansion.Expansion;
import com.beemobi.rongthanonline.server.Server;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

public class FlagWar extends Expansion implements Runnable {
    private static final Logger logger = Logger.getLogger(FlagWar.class);
    public static List<String> notes;
    public List<Integer> vips = new ArrayList<>();

    static {
        notes = new ArrayList<>();
        notes.add("- Phó bản mở cửa vào 20h30 hàng ngày");
        notes.add("- Các chiến binh có 30 phút để tích lũy điểm cho bang hội:");
        notes.add("+> Hạ chiến binh khác +1 điểm");
        notes.add("+> Cắm cờ +100 điểm (nhặt cờ và đem đến cho NPC PU)");
        notes.add("Sau 30 phút thi đấu, top 3 bang hội có tích lũy điểm nhiều nhất sẽ nhận được:");
        notes.add("+> top 1: 500 điểm thưởng mỗi thàng viên tham gia");
        notes.add("+> top 2: 200 điểm thưởng mỗi thàng viên tham gia");
        notes.add("+> top 3: 100 điểm thưởng mỗi thàng viên tham gia");
        notes.add("- Điểm thưởng có thể sử dụng để đổi quà");
    }

    public FlagWar() {
        super(1800000);
        Server.getInstance().service.serverChat("Chế độ Cướp cờ đã mở cửa");
        Map map = new Map(MapManager.getInstance().mapTemplates.get(MapName.HANH_TINH_NGUC_TU), this);
        map.zones.add(new ZoneFlagWar(map, this));
        maps.add(map);
        isRunning = true;
        new Thread(this).start();
    }

    public void enter(Player player) {
        player.x = 325;
        player.y = 1584;
        maps.get(0).zones.get(0).enter(player);
    }

    @Override
    public void close() {
        isRunning = false;
        for (Map map : maps) {
            map.close();
        }
        maps.clear();
        MapManager.getInstance().flagWar = null;
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
