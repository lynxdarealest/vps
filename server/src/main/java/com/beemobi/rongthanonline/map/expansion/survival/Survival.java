package com.beemobi.rongthanonline.map.expansion.survival;

import com.beemobi.rongthanonline.entity.player.Player;
import com.beemobi.rongthanonline.entity.player.PlayerManager;
import com.beemobi.rongthanonline.item.Item;
import com.beemobi.rongthanonline.item.ItemManager;
import com.beemobi.rongthanonline.item.ItemMap;
import com.beemobi.rongthanonline.item.ItemName;
import com.beemobi.rongthanonline.map.*;
import com.beemobi.rongthanonline.map.expansion.Expansion;
import com.beemobi.rongthanonline.map.expansion.ExpansionState;
import com.beemobi.rongthanonline.server.Server;
import com.beemobi.rongthanonline.top.*;
import com.beemobi.rongthanonline.top.expansion.survival.TopSurvival;
import com.beemobi.rongthanonline.top.expansion.survival.TopSurvivalInfo;
import com.beemobi.rongthanonline.util.Utils;
import org.apache.log4j.Logger;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

public class Survival extends Expansion implements Runnable {
    private static final Logger logger = Logger.getLogger(Survival.class);
    public static final int[] MAP_IDS = {MapName.CUA_KHONG_GIAN, MapName.CUA_1, MapName.CUA_2, MapName.CUA_3};
    public static final int MINUTE_WAIT_REG = 10;
    public static final int[] REWARD_DIAMONDS = new int[]{100, 50, 20, 10};
    public static final int[][] REWARD_ITEMS = new int[][]{{ItemName.DA_10, 10}, {ItemName.DA_10, 5}, {ItemName.DA_10, 2}, {ItemName.DA_10, 1}};
    public static List<String> notes = new ArrayList<>();
    public ArrayList<Gamer> gamers;
    public ArrayList<Gamer> tops;
    public int masterId;
    public int round;
    public int hour;
    public String name;
    public long[] rewards;
    public LinkedHashMap<String, Long> donors;

    static {
        notes.add("- Chiến trường sinh tồn mở cửa vào 12h và 20h hàng ngày");
        notes.add("- Bạn cũng có thể tạo trận chiến riêng cho mình nhưng sẽ bị hủy nếu trùng với thời gian với trận chiến của Máy chủ");
        notes.add("- Các chiến binh tham gia sẽ nhận được x10 Đá cấp 7");
        notes.add("- Sau mỗi phút sống sót, chiến binh sẽ nhận được thêm x1 đá cấp 7");
        notes.add("- Phần thưởng cho top 1 là 100 kim cương và x10 Đá cấp 10");
        notes.add("- Phần thưởng cho top 2 là 50 kim cương và x5 Đá cấp 10");
        notes.add("- Phần thưởng cho top 3 là 20 kim cương và x2 Đá cấp 10");
        notes.add("- Phần thưởng cho top 4-10 là 10 kim cương và x1 Đá cấp 10");
        notes.add("- Trận chiến do người chơi tổ chức sẽ không thể nhận phần thưởng của Máy chủ");
        notes.add("- Bạn sẽ không nhận được phần thưởng nếu túi đồ không còn chỗ trống");
        notes.add("- Các chiến binh có thể tiến hành trao thưởng thêm cho các trận chiến với mức tối thiểu 10tr xu");
    }

    public Survival(String name, int hour, int masterId) {
        super(3600000);
        for (int mapId : MAP_IDS) {
            Map map = new Map(MapManager.getInstance().mapTemplates.get(mapId), this);
            for (int i = 0; i < map.template.minZone; i++) {
                map.zones.add(new ZoneSurvival(map, this));
            }
            maps.add(map);
        }
        state = ExpansionState.WAIT_REG;
        gamers = new ArrayList<>();
        tops = new ArrayList<>();
        this.hour = hour;
        this.name = name;
        this.masterId = masterId;
        rewards = new long[4];
        donors = new LinkedHashMap<>();
        isRunning = true;
        new Thread(this).start();
        if (masterId == -1) {
            Server.getInstance().service.serverChat("Chiến trường sinh tồn đã mở cửa");
        } else {
            Server.getInstance().service.serverChat(String.format("%s đã mở cửa trận chiến Chiến trường sinh tồn", this.name));
        }
    }

    @Override
    public void close() {
        isRunning = false;
        for (Map map : maps) {
            map.close();
        }
        maps.clear();
        MapManager.getInstance().survival = null;
    }

    @Override
    public void update() {
        long now = System.currentTimeMillis();
        if (isRunning && endTime < now) {
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
            gamers.clear();
            Map map = maps.get(0);
            for (Zone zone : map.zones) {
                List<Player> players = zone.getPlayers(Zone.TYPE_PLAYER);
                for (Player player : players) {
                    if (player != null && player.zone == zone && player.id != masterId) {
                        lock.lock();
                        try {
                            gamers.add(new Gamer(player, now));
                        } finally {
                            lock.unlock();
                        }
                        player.recovery(Player.RECOVERY_ALL, 100, true);
                        Map m = maps.get(Utils.nextInt(1, 3));
                        Zone a = m.zones.get(Utils.nextInt(m.zones.size()));
                        player.teleport(a, true);
                        player.setTypePk(2);
                        if (masterId == -1 && !player.isBagFull()) {
                            player.addItem(ItemManager.getInstance().createItem(ItemName.DA_7, 10, true), true);
                        }
                    }
                }
            }
            if (gamers.isEmpty()) {
                close();
                return;
            }
            for (int i = 1; i < maps.size(); i++) {
                for (Zone zone : maps.get(i).zones) {
                    ZoneSurvival areaSurvival = (ZoneSurvival) zone;
                    areaSurvival.createItemMap();
                }
            }
            updateTime = now;
            state = ExpansionState.RUN;
            return;
        }
        for (int i = 0; i < gamers.size(); i++) {
            Gamer gamer = gamers.get(i);
            if (gamers.size() == 1) {
                gamer.endTime = now;
                tops.add(0, gamer);
                reward();
                close();
                return;
            }
            Player player = PlayerManager.getInstance().findPlayerById(gamer.playerId);
            if (player == null || player.planet != MapPlanet.SURVIVAL) {
                lock.lock();
                try {
                    gamers.remove(i);
                } finally {
                    lock.unlock();
                }
                gamer.endTime = now;
                tops.add(0, gamer);
                i--;
            }
        }
        if (now - updateTime < 60000) {
            return;
        }
        updateTime = now;
        round++;
        List<ZoneSurvival> areaList = new ArrayList<>();
        for (int i = 1; i < maps.size(); i++) {
            Map map = maps.get(i);
            for (Zone zone : map.zones) {
                ZoneSurvival areaSurvival = (ZoneSurvival) zone;
                if (!areaSurvival.isRedZone) {
                    areaList.add(areaSurvival);
                }
            }
        }
        int num = 3;
        while (num > 0 && !areaList.isEmpty()) {
            num--;
            ZoneSurvival area = areaList.get(Utils.nextInt(areaList.size()));
            area.isRedZone = true;
            areaList.remove(area);
        }
        if (areaList.isEmpty()) {
            if (round % 2 == 0) {
                Map map = maps.get(Utils.nextInt(1, 3));
                for (Gamer gamer : gamers) {
                    Player player = PlayerManager.getInstance().findPlayerById(gamer.playerId);
                    if (player != null) {
                        player.teleport(map.zones.get(0), true);
                    }
                }
            }
            return;
        }
        for (ZoneSurvival area : areaList) {
            area.count++;
            area.reward();
            area.bornBot();
            area.createItemMap();
        }
        if (round % 2 == 0 && !areaList.isEmpty()) {
            for (int i = 0; i < 5; i++) {
                Zone zone = areaList.get(Utils.nextInt(areaList.size()));
                ItemMap itemMap = ItemManager.getInstance().createItemMap(ItemName.CAPSULE_VIEN_TRO_SURVIVAL, 1, -1);
                itemMap.x = zone.map.template.width / 2;
                itemMap.y = zone.map.getYSd(itemMap.x);
                itemMap.throwTime = now + 120000;
                itemMap.lockTime = now + 20000;
                zone.addItemMap(itemMap);
                sendServerChat(String.format("%s đã xuất hiện tại %s khu vực %d", itemMap.template.name, zone.map.template.name, zone.id));
            }
        }
        if (round % 3 == 2) {
            sendNotification(Player.INFO_YELLOW, "Chiến trường sẽ gom lại sau 60 giây");
        } else if (round % 3 == 0) {
            Map map = maps.get(Utils.nextInt(1, 3));
            areaList.removeIf(area -> area.map != map);
            if (!areaList.isEmpty()) {
                for (Gamer gamer : gamers) {
                    Player player = PlayerManager.getInstance().findPlayerById(gamer.playerId);
                    if (player != null) {
                        player.teleport(areaList.get(Utils.nextInt(areaList.size())), true);
                    }
                }
            }
        }
    }

    public void reward() {
        Gamer gamer = gamers.get(0);
        Server.getInstance().service.serverChat(String.format("Chúc mừng %s vừa đạt top 1 Chiến trường sinh tồn", gamer.name));
        if (!Server.getInstance().isInterServer()) {
            int size = Math.min(tops.size(), 10);
            long[] rewards = new long[size];
            for (int i = 0; i < rewards.length; i++) {
                if (i >= this.rewards.length) {
                    rewards[i] = this.rewards[this.rewards.length - 1];
                } else {
                    rewards[i] = this.rewards[i];
                }
            }
            for (int i = 0; i < size; i++) {
                Player player = PlayerManager.getInstance().findPlayerById(tops.get(i).playerId);
                if (player != null) {
                    if (masterId == -1) {
                        int index = i;
                        if (index >= REWARD_DIAMONDS.length) {
                            index = REWARD_DIAMONDS.length - 1;
                        }
                        player.upRuby(REWARD_DIAMONDS[index]);
                        Item item = ItemManager.getInstance().createItem(REWARD_ITEMS[index][0], REWARD_ITEMS[index][1], true);
                        player.addItem(item);
                        player.addInfo(Player.INFO_YELLOW, String.format("Bạn đạt được top %d Chiến trường sinh tồn nhận được %d Ruby và x%d %s", (i + 1), REWARD_DIAMONDS[index], item.quantity, item.template.name));
                        player.upPointAchievement(22, 1);
                        if (player.taskMain != null && player.taskMain.template.id == 21 && player.taskMain.index == 2) {
                            player.nextTaskParam();
                        }
                        if (i == 0) {
                            player.upParamMissionDaily(6, 1);
                        }
                    }
                    if (rewards[i] > 0) {
                        rewards[i] -= rewards[i] / 20;
                        if (rewards[i] > 0) {
                            player.upXu(rewards[i]);
                            if (donors.size() > 1) {
                                player.addInfo(Player.INFO_YELLOW, String.format("Bạn nhận được %s xu từ các nhà tài trợ của Chiến trường sinh tồn", Utils.formatNumber(rewards[i])));
                            } else {
                                player.addInfo(Player.INFO_YELLOW, String.format("Bạn nhận được %s xu từ nhà tài trợ của Chiến trường sinh tồn", Utils.formatNumber(rewards[i])));
                            }
                        }
                    }
                }
            }
        } else if (masterId == -1) {
           /* int size = Math.min(tops.size(), 10);
            List<RewardData> rewardDataList = new ArrayList<>();
            for (int i = 0; i < size; i++) {
                Player player = PlayerManager.getInstance().findPlayerById(tops.get(i).playerId);
                if (player != null) {
                    String info = String.format("Phần thưởng top %d Chiến trường Sinh tồn Liên máy chủ", i + 1);
                    if (i == 0) {
                        rewardDataList.add(new RewardData(player, 189, "ADMIN", info));
                    }
                    rewardDataList.add(new RewardData(player, 190 + i, "ADMIN", info));
                }
            }
            if (!rewardDataList.isEmpty()) {
                GameRepository.getInstance().rewardData.saveAll(rewardDataList);
            }*/
        }
        LocalDateTime localNow = LocalDateTime.now();
        TopSurvival survival = new TopSurvival(String.format("Giải %s\n[%dh %d/%d]", this.name, this.hour, localNow.getDayOfMonth(), localNow.getMonthValue()));
        int score = tops.size();
        for (Gamer g : tops) {
            TopSurvivalInfo topInfo = new TopSurvivalInfo();
            topInfo.id = g.playerId;
            topInfo.name = g.name;
            topInfo.score = score--;
            topInfo.gender = g.gender;
            topInfo.info = String.format("Sinh tồn: %d giây", (g.endTime - g.startTime) / 1000);
            survival.elements.add(topInfo);
        }
        TopManager.getInstance().topSurvival.add(survival);
    }

    public void sendNotification(int type, String info) {
        lock.lock();
        try {
            for (Gamer gamer : gamers) {
                Player player = PlayerManager.getInstance().findPlayerById(gamer.playerId);
                if (player != null) {
                    player.addInfo(type, info);
                }
            }
        } finally {
            lock.unlock();
        }
    }

    public void sendServerChat(String content) {
        lock.lock();
        try {
            for (Gamer gamer : gamers) {
                Player player = PlayerManager.getInstance().findPlayerById(gamer.playerId);
                if (player != null) {
                    Server.getInstance().service.serverChat(player, content);
                }
            }
        } finally {
            lock.unlock();
        }
    }

    public ArrayList<Gamer> getGamers() {
        lock.lock();
        try {
            return gamers;
        } finally {
            lock.unlock();
        }
    }

    public void join(Player player) {
        lock.lock();
        try {
            if (player.id == masterId) {
                return;
            }
            Gamer gamer = gamers.stream().filter(g -> g.playerId == player.id).findFirst().orElse(null);
            if (gamer == null) {
                gamers.add(new Gamer(player, -1));
            }
        } finally {
            lock.unlock();
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
            } catch (Exception e) {
                close();
            }
        }
    }
}
