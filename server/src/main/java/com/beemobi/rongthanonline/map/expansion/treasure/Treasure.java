package com.beemobi.rongthanonline.map.expansion.treasure;

import com.beemobi.rongthanonline.entity.monster.big.ThanThu;
import com.beemobi.rongthanonline.entity.player.Player;
import com.beemobi.rongthanonline.entity.player.PlayerManager;
import com.beemobi.rongthanonline.item.Item;
import com.beemobi.rongthanonline.item.ItemManager;
import com.beemobi.rongthanonline.item.ItemName;
import com.beemobi.rongthanonline.item.ItemOption;
import com.beemobi.rongthanonline.map.Map;
import com.beemobi.rongthanonline.map.MapManager;
import com.beemobi.rongthanonline.map.MapName;
import com.beemobi.rongthanonline.map.expansion.Expansion;
import com.beemobi.rongthanonline.map.expansion.ExpansionState;
import com.beemobi.rongthanonline.model.MessageTime;
import com.beemobi.rongthanonline.model.PointWeeklyType;
import com.beemobi.rongthanonline.server.Server;
import com.beemobi.rongthanonline.top.Top;
import com.beemobi.rongthanonline.top.TopInfo;
import com.beemobi.rongthanonline.top.TopManager;
import com.beemobi.rongthanonline.top.TopType;
import com.beemobi.rongthanonline.util.Utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.stream.Collectors;

public class Treasure extends Expansion implements Runnable {
    public static final int[] MAP_IDS = new int[]{MapName.HANG_PHIA_BAC, MapName.HANG_PHIA_NAM, MapName.HANG_TRUNG_TAM};
    public static final int MINUTE_WAIT_REG = 10;
    public static final int[] FLAGS = new int[]{2, 3};
    public static List<String> notes = new ArrayList<>();
    public HashMap<Integer, Pirate> pirates;
    public ReadWriteLock lockPirate;

    static {
        notes.add("- Động kho báu mở cửa vào 21h30 hàng ngày");
        notes.add("- Trận chiến diễn ra trong 30 phút");
        notes.add("- Các chiến binh sẽ được sắp xếp ngẫu nhiên vào phe Xanh hoặc Đỏ");
        notes.add("- Khi trận chiến bắt đầu, các chiến binh 2 phe hãy cố gắng hạ Thần thụ ở trung tâm Động kho báu");
        notes.add("- Phe nào hạ được Thần thụ, Thần thụ sẽ đổi màu cờ sang phe đó và mỗi 30 giây sẽ rớt ra rất nhiều vật phẩm quý giá");
        notes.add("- Chiến binh cùng màu cờ với Thần thụ, có tích lũy tạm thời ít nhất 5 điểm mới có thể nhặt vật phẩm");
        notes.add("- Chiến binh sẽ được +5 điểm khi hạ 1 chiến binh phe khác, và +1 điểm nếu tấn công lên Thần thụ khác cờ");
        notes.add("- Chiến binh sẽ bị mất điểm tạm thời nếu chết hoặc đổi khu vực");
        notes.add("- Chiến binh phải có ít nhất 1 Vé tham gia Động kho báu (có thể kiếm tại Bản doanh Red) mới có thể tham gia");
        notes.add("- Chiến binh phải đủ ít nhất 100 điểm năng động mới có thể tham gia");
        notes.add("- Chiến binh dưới level 30 cần phải đủ set trang bị từ cấp 4 trở lên mới có thể tham gia");
        notes.add("- Chiến binh từ level 30 đến level 39 cần phải đủ set trang bị từ cấp 6 trở lên mới có thể tham gia");
        notes.add("- Chiến binh từ level 40 đến level 49 cần phải đủ set trang bị từ cấp 8 trở lên mới có thể tham gia");
        notes.add("- Chiến binh từ level 50 cần phải đủ set trang bị từ cấp 10 trở lên mới có thể tham gia");
        notes.add("- Khi hết giờ, phe nào có tổng điểm tích lũy lớn hơn thì mỗi chiến binh phe đó có điểm tích lũy tối thiểu 100 sẽ nhận được 1 rương báu. Nếu tổng tích lũy 2 bên bằng nhau, phe Đỏ sẽ thắng");
        notes.add("- Ngoài ra top 5 chiến binh dẫn đầu về thành tích sẽ nhận được thêm 1 rương báu, riêng chiến binh top 1 sẽ nhận được Huy hiệu Vô địch");
    }

    public Treasure() {
        super(1800000);
        Server.getInstance().service.serverChat("Động kho báu đã mở cửa, các chiến binh hãy báo danh tại NPC Quy Lão");
        for (int mapId : MAP_IDS) {
            Map map = new Map(MapManager.getInstance().mapTemplates.get(mapId), this);
            int countArea = 5;
            if (mapId == MapName.HANG_TRUNG_TAM) {
                countArea = 1;
            }
            for (int j = 0; j < countArea; j++) {
                map.zones.add(new ZoneTreasure(map, this));
            }
            maps.add(map);
        }
        pirates = new HashMap<>();
        lockPirate = new ReentrantReadWriteLock();
        state = ExpansionState.WAIT_REG;
        isRunning = true;
        new Thread(this).start();
        Top top = TopManager.getInstance().tops.get(TopType.TOP_TREASURE);
        if (top != null) {
            top.elements.clear();
        }
    }

    @Override
    public void close() {
        lockPirate.writeLock().lock();
        try {
            isRunning = false;
            for (Map map : maps) {
                map.close();
            }
            maps.clear();
            MapManager.getInstance().treasure = null;
        } finally {
            lockPirate.writeLock().unlock();
        }
    }

    @Override
    public void update() {
        long now = System.currentTimeMillis();
        if (isRunning && endTime < now) {
            reward();
            close();
            return;
        }
        if (state == ExpansionState.WAIT_REG) {
            long time = MINUTE_WAIT_REG * 60000L + startTime - now;
            if (time > 0) {
                if (now - updateTime > 60000) {
                    updateTime = now;
                    sendNotification(Player.INFO_YELLOW, String.format("Trận chiến của bạn sẽ diễn ra sau %s", Utils.formatTime(time)));
                }
                return;
            }
            if (pirates.size() < 12) {
                sendNotification(Player.INFO_RED, "Động kho báu đã đóng do không đủ chiến binh đăng ký");
                close();
                return;
            }
            updateTime = now;
            state = ExpansionState.RUN;
            maps.get(2).zones.get(0).enterBigMonster(new ThanThu(-1));
            return;
        }
        List<Pirate> pirateList = getPirates();
        long delay = 120000;
        for (Pirate pirate : pirateList) {
            if (pirate.total > 0 && now - pirate.updateTime > delay) {
                pirate.total = 0;
            }
        }
    }

    public void sendNotification(int type, String info) {
        lockPirate.readLock().lock();
        try {
            for (Pirate pirate : pirates.values()) {
                Player player = PlayerManager.getInstance().findPlayerById(pirate.playerId);
                if (player != null) {
                    player.addInfo(type, info);
                }
            }
        } finally {
            lockPirate.readLock().unlock();
        }
    }

    public void sendMessageTime(String flag) {
        lockPirate.readLock().lock();
        try {
            MessageTime messageTime = new MessageTime(MessageTime.FLAG_TREASURE, -1);
            String info = messageTime.text.replace("#", flag);
            for (Pirate pirate : pirates.values()) {
                Player player = PlayerManager.getInstance().findPlayerById(pirate.playerId);
                if (player != null) {
                    player.addMessageTime(MessageTime.FLAG_TREASURE, -1, info);
                }
            }
        } finally {
            lockPirate.readLock().unlock();
        }
    }

    public void reward() {
        if (isClose) {
            return;
        }
        isClose = true;
        long[] point = new long[2];
        for (Pirate pirate : pirates.values()) {
            if (pirate.flag == FLAGS[0]) {
                point[0] += pirate.total;
            } else if (pirate.flag == FLAGS[1]) {
                point[1] += pirate.total;
            }
        }
        int index = point[0] >= point[1] ? 0 : 1;
        Server.getInstance().service.serverChat(String.format("Chúc mừng phe %s đã chiến thắng Động kho báu", index == 0 ? "Đỏ" : "Xanh"));
        for (Pirate pirate : pirates.values()) {
            Player player = PlayerManager.getInstance().findPlayerById(pirate.playerId);
            if (player != null && player.isInTreasure()) {
                if (pirate.flag == FLAGS[index] && pirate.total >= 100) {
                    player.addItem(ItemManager.getInstance().createItem(ItemName.RUONG_KHO_BAU, 1, true), true);
                }
                player.upPointWeekly(PointWeeklyType.ACTIVE, 10);
            }
        }
        /*List<Pirate> pirateList = pirates.values().stream().filter(p -> p.flag == FLAGS[index]).toList();
        for (Pirate pirate : pirateList) {
            Player player = PlayerManager.getInstance().findPlayerById(pirate.playerId);
            if (player != null && player.isInTreasure() && pirate.total >= 100) {
                player.addItem(ItemManager.getInstance().createItem(ItemName.RUONG_KHO_BAU, 1, true), true);
            }
        }*/
        Top top = TopManager.getInstance().tops.get(TopType.TOP_TREASURE);
        if (top == null) {
            return;
        }
        List<TopInfo> tops = top.getTops();
        int size = Math.min(5, tops.size());
        for (int i = 0; i < size; i++) {
            TopInfo topInfo = tops.get(i);
            if (topInfo.score > 100) {
                Player player = PlayerManager.getInstance().findPlayerById(topInfo.id);
                if (player != null) {
                    if (i == 0) {
                        Item item = ItemManager.getInstance().createItem(ItemName.HUY_HIEU_VO_DICH, 1, true);
                        item.options.add(new ItemOption(174, 10));
                        item.setExpiry(2);
                        player.addItem(item, true);
                    }
                    player.addItem(ItemManager.getInstance().createItem(ItemName.RUONG_KHO_BAU, 1, true), true);
                }
            }
        }
    }

    public void addPlayer(Player player) {
        lockPirate.writeLock().lock();
        try {
            if (isClose) {
                return;
            }
            Pirate pirate = pirates.get(player.id);
            if (pirate == null) {
                if (!Server.getInstance().isInterServer()) {
                    int quantity = player.getQuantityItemInBag(ItemName.VE_THAM_GIA_DAO_KHO_BAU);
                    if (quantity < 1) {
                        player.addInfo(Player.INFO_RED, "Bạn chưa có vé tham gia Động kho báu");
                        return;
                    }
                    player.removeQuantityItemBagById(ItemName.VE_THAM_GIA_DAO_KHO_BAU, 1);
                }
                pirate = new Pirate(player);
                if (Server.getInstance().isInterServer()) {
                    pirate.flag = player.server == 1 ? FLAGS[0] : FLAGS[1];
                } else {
                    int mark = player.level / 10;
                    long[] count = new long[2];
                    count[0] = pirates.values().stream().filter(p -> p.level / 10 == mark && p.flag == FLAGS[0]).count();
                    count[1] = pirates.values().stream().filter(p -> p.level / 10 == mark && p.flag == FLAGS[1]).count();
                    if (count[0] == count[1]) {
                        pirate.flag = FLAGS[Utils.nextInt(0, 1)];
                    } else {
                        pirate.flag = count[0] < count[1] ? FLAGS[0] : FLAGS[1];
                    }
                }
                pirates.put(player.id, pirate);
            }
            if (player.typePk > 0) {
                player.setTypePk(0);
            }
            player.setTypeFlag(pirate.flag);
            if (pirate.flag == FLAGS[0]) {
                player.teleport(maps.get(0), true);
            } else {
                player.teleport(maps.get(1), true);
            }
        } finally {
            lockPirate.writeLock().unlock();
        }
    }

    public long getTotalPirate(int index) {
        lockPirate.readLock().lock();
        try {
            if (index == -1) {
                return pirates.size();
            } else {
                return pirates.values().stream().filter(p -> p.flag == FLAGS[index]).count();
            }
        } finally {
            lockPirate.readLock().unlock();
        }
    }

    public long getTotalPoint(int index) {
        lockPirate.readLock().lock();
        try {
            int point = 0;
            if (index == -1) {
                for (Pirate pirate : pirates.values()) {
                    point += pirate.total;
                }
            } else {
                for (Pirate pirate : pirates.values()) {
                    if (pirate.flag == FLAGS[index]) {
                        point += pirate.total;
                    }
                }
            }
            return point;
        } finally {
            lockPirate.readLock().unlock();
        }
    }

    public long getTotalPointPirate(int playerId) {
        lockPirate.readLock().lock();
        try {
            Pirate pirate = pirates.get(playerId);
            if (pirate == null) {
                return 0;
            }
            return pirate.total;
        } finally {
            lockPirate.readLock().unlock();
        }
    }

    public long getPointPirate(int playerId) {
        lockPirate.readLock().lock();
        try {
            Pirate pirate = pirates.get(playerId);
            if (pirate == null) {
                return 0;
            }
            return pirate.point;
        } finally {
            lockPirate.readLock().unlock();
        }
    }

    public Pirate findPirateByPlayerId(int playerId) {
        lockPirate.readLock().lock();
        try {
            return pirates.get(playerId);
        } finally {
            lockPirate.readLock().unlock();
        }
    }

    public List<Pirate> findListPirateByFlag(int index) {
        lockPirate.readLock().lock();
        try {
            return pirates.values().stream().filter(p -> p.flag == FLAGS[index]).collect(Collectors.toList());
        } finally {
            lockPirate.readLock().unlock();
        }
    }

    public List<Pirate> getPirates() {
        lockPirate.readLock().lock();
        try {
            return new ArrayList<>(pirates.values());
        } finally {
            lockPirate.readLock().unlock();
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
