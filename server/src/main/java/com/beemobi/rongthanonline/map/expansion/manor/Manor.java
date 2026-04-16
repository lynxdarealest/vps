package com.beemobi.rongthanonline.map.expansion.manor;

import com.beemobi.rongthanonline.clan.Clan;
import com.beemobi.rongthanonline.entity.player.Player;
import com.beemobi.rongthanonline.entity.player.PlayerManager;
import com.beemobi.rongthanonline.map.Map;
import com.beemobi.rongthanonline.map.MapManager;
import com.beemobi.rongthanonline.map.MapName;
import com.beemobi.rongthanonline.map.expansion.Expansion;
import com.beemobi.rongthanonline.service.ManorService;
import com.beemobi.rongthanonline.util.Utils;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

public class Manor extends Expansion {
    private static final Logger logger = Logger.getLogger(Manor.class);
    public static List<String> notes = new ArrayList<>();
    public static final int[][] MAP_IDS = {
            {
                    MapName.CUA_LUA_1,
                    MapName.CUA_LUA_2,
                    MapName.CUA_LUA_3,
                    MapName.CUA_LUA_4,
                    MapName.CUA_LUA_5,
                    MapName.CUA_LUA_6
            },
            {
                    MapName.CUA_BANG_1,
                    MapName.CUA_BANG_2,
                    MapName.CUA_BANG_3,
                    MapName.CUA_BANG_4,
                    MapName.CUA_BANG_5,
                    MapName.CUA_BANG_6
            }
    };
    public int level;
    public ManorType type;
    public ManorService service;
    public Clan clan;
    public ArrayList<Integer> players;

    static {
        notes.add("- Mỗi bang hội chỉ có thể mở cửa Vùng đất bí ẩn mỗi ngày 1 lần");
        notes.add("- Mỗi chiến binh chỉ có thể tham gia Vùng đất bí ẩn mỗi ngày 1 lần");
        notes.add("- Mức sức mạnh và tiềm năng nhận được khi đánh quái trong Vùng đất bí ẩn sẽ cao hơn so với bên ngoài");
        notes.add("- Hạ quái và Boss sẽ có cơ hội nhận được các vật phẩm quý hiếm chỉ xuất hiện duy nhất tại đây");
        notes.add("- Bang hội có 60 phút để chinh phục Vùng đất bí ẩn");
        notes.add("- Chiến binh phải đủ ít nhất 100 điểm năng động mới có thể tham gia");
        notes.add("- Chiến binh tham gia bang hội từ 12 tiếng trở lên mới có thể tham gia");
        notes.add("- Chiến binh dưới level 30 cần phải đủ set trang bị từ cấp 6 trở lên mới có thể tham gia");
        notes.add("- Chiến binh từ level 30 đến level 39 cần phải đủ set trang bị từ cấp 8 trở lên mới có thể tham gia");
        notes.add("- Chiến binh từ level 40 đến level 49 cần phải đủ set trang bị từ cấp 10 trở lên mới có thể tham gia");
        notes.add("- Chiến binh từ level 50 cần phải đủ set trang bị từ cấp 12 trở lên mới có thể tham gia");
        notes.add("- Nếu bang hội chưa chinh phục xong Vùng đất bí ẩn mà Máy chủ bảo trì thì sau khi bảo trì hoàn tất, bang hội không thể tham gia lại");
    }

    public Manor(Clan clan, int level) {
        super(3600000);
        this.clan = clan;
        this.level = level;
        service = new ManorService(this);
        int index = Utils.nextInt(2);
        if (index == 0) {
            type = ManorType.MANOR_FIRE;
        } else {
            type = ManorType.MANOR_COLD;
        }
        players = new ArrayList<>();
        for (int mapId : MAP_IDS[index]) {
            Map map = new Map(MapManager.getInstance().mapTemplates.get(mapId), this);
            map.zones.add(new ZoneManor(map, this));
            maps.add(map);
        }
        clan.manor = this;
    }

    public void addPlayer(Player player) {
        lock.lock();
        try {
            if (isClose) {
                return;
            }
            if (!players.contains(player.id)) {
                players.add(player.id);
            }
        } finally {
            lock.unlock();
        }
    }

    public Map findMap(int id) {
        return this.maps.stream().filter(m -> m.template.id == id).findFirst().orElse(null);
    }

    @Override
    public void close() {
        lock.lock();
        try {
            if (isClose) {
                return;
            }
            isClose = true;
            while (!players.isEmpty()) {
                try {
                    Player player = PlayerManager.getInstance().findPlayerById(players.get(0));
                    players.remove(0);
                    if (player != null) {
                        if (player.zone != null && player.zone.map.isManor()) {
                            player.teleport(MapName.NUI_PAOZU, false);
                            player.addInfo(Player.INFO_YELLOW, "Hết thời gian, phó bản đã đóng lại");
                        }
                    }
                } catch (Exception ex) {
                    logger.error("close", ex);
                }
            }
            for (Map map : this.maps) {
                map.close();
            }
        } finally {
            lock.unlock();
        }
        clan.manor = null;
    }

    @Override
    public void update() {

    }
}
