package com.beemobi.rongthanonline.map.expansion.barrack;

import com.beemobi.rongthanonline.entity.player.Player;
import com.beemobi.rongthanonline.entity.player.PlayerManager;
import com.beemobi.rongthanonline.map.Zone;
import com.beemobi.rongthanonline.map.Map;
import com.beemobi.rongthanonline.map.MapManager;
import com.beemobi.rongthanonline.map.MapName;
import com.beemobi.rongthanonline.map.expansion.Expansion;
import com.beemobi.rongthanonline.model.PointWeeklyType;
import com.beemobi.rongthanonline.service.BarrackService;
import com.beemobi.rongthanonline.util.Utils;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

public class Barrack extends Expansion {
    private static final Logger logger = Logger.getLogger(Barrack.class);
    public static List<String> notes = new ArrayList<>();
    public static final int[] MAP_IDS = {
            MapName.CONG_BAN_DOANH, MapName.SAN_TRUOC, MapName.SAN_SAU
            , MapName.THAP_CANH, MapName.SAN_TAP, MapName.PHONG_DIEU_KHIEN
            , MapName.NHA_KHO, MapName.TUONG_THANH_1, MapName.TUONG_THANH_2
            , MapName.PHONG_GIAM, MapName.PHONG_CHO_1, MapName.PHONG_CHO_2
            , MapName.PHONG_CHO_3, MapName.TANG_1, MapName.TANG_LAU};
    public int level;
    public int countPlayer;
    public int weekStart;
    public ArrayList<Integer> players;
    public int timeBattle;
    public BarrackService service;

    static {
        notes.add("- Mỗi chiến binh có thể tham gia miễn phí Bản doanh Red 1 lần trong ngày, tối đa 3 lần nếu sử dụng thêm vật phẩm phụ trợ");
        notes.add("- Mức sức mạnh và tiềm năng nhận được khi đánh quái trong Bản doanh sẽ cao hơn so với bên ngoài");
        notes.add("- Hạ quái và Boss trong Bản doanh sẽ có cơ hội nhận được các vật phẩm quý hiếm chỉ xuất hiện duy nhất tại đây");
        notes.add("- Chiến binh và các thành viên trong tổ đội có 60 phút để chinh phục Bản doanh");
        notes.add("- Chiến binh nên tạo tổ đội 6 người và ngang tài ngang sức để phá đảo Bản doanh 1 cách dễ dàng");
        notes.add("- Chiến binh phải đủ ít nhất 50 điểm năng động mới có thể tham gia Bản doanh Red");
        notes.add("- Chiến binh dưới level 20 cần phải đủ set trang bị từ cấp 2 trở lên mới có thể tham gia");
        notes.add("- Chiến binh từ level 20 đến level 29 cần phải đủ set trang bị từ cấp 3 trở lên mới có thể tham gia");
        notes.add("- Chiến binh từ level 30 đến level 39 cần phải đủ set trang bị từ cấp 5 trở lên mới có thể tham gia");
        notes.add("- Chiến binh từ level 40 cần phải đủ set trang bị từ cấp 8 trở lên mới có thể tham gia");
        notes.add("- Chiến binh tham gia hạ gục quái sẽ nhận 1 điểm, 2 điểm đối với quái tinh ranh, 5 điểm đối với quái đầu đàn, 10 điểm với BOSS");
        notes.add("- Chiến binh có thể dùng điểm để đổi phần thường trong cửa hàng. Điểm sẽ được bảo lưu và cộng dồn qua các ngày nếu chiến binh chưa sử dụng hết");
        notes.add("- Nếu chiến binh chưa chinh phục xong Bản doanh mà Máy chủ bảo trì thì sau khi bảo trì hoàn tất, bạn không thể tham gia lại");
    }

    public Barrack(int level, int countPlayer) {
        super(3600000);
        if (level < 10) {
            level = 10;
        }
        this.level = level;
        for (int mapId : MAP_IDS) {
            Map map = new Map(MapManager.getInstance().mapTemplates.get(mapId), this);
            map.zones.add(new ZoneBarrack(map, this));
            maps.add(map);
        }
        if (countPlayer < 1) {
            countPlayer = 1;
        }
        if (countPlayer > 6) {
            countPlayer = 6;
        }
        this.countPlayer = countPlayer;
        players = new ArrayList<>();
        weekStart = Utils.getWeekOfYearNow();
        service = new BarrackService(this);
        MapManager.getInstance().barracks.put(this.id, this);
    }

    public void addPlayer(Player player) {
        lock.lock();
        try {
            if (isClose) {
                return;
            }
            player.barrackId = id;
            if (!players.contains(player.id)) {
                players.add(player.id);
            }
        } finally {
            lock.unlock();
        }
    }

    public void upPoint(Zone zone, int point, int type) {
        long now = System.currentTimeMillis();
        for (int id : players) {
            Player player = zone.findPlayerById(id);
            if (player != null && !player.isDead()) {
                if (type == 0) {
                    // quai
                    if (now - player.lastTimeAttack < 30000) {
                        player.pointBarrack += point;
                    }
                } else if (type == 1) {
                    // bosss
                    player.pointBarrack += point;
                } else {
                    // win
                    player.pointBarrack += point;
                    player.upPointAchievement(24, 1);
                    player.upPointWeekly(PointWeeklyType.ACTIVE, 5);
                }
                if (player.pointBarrack < 0) {
                    player.pointBarrack = 0;
                }
            }
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
                        if (player.zone != null && player.zone.map.isBarrack()) {
                            player.teleport(MapName.NUI_PAOZU, false);
                            player.addInfo(Player.INFO_YELLOW, "Hết thời gian, cổng Bản doanh Red đã đóng lại");
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
        MapManager.getInstance().barracks.remove(id);
    }

    @Override
    public void update() {

    }
}
