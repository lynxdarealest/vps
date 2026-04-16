package com.beemobi.rongthanonline.map.expansion.festival;

import com.beemobi.rongthanonline.entity.player.Player;
import com.beemobi.rongthanonline.entity.player.PlayerManager;
import com.beemobi.rongthanonline.item.Item;
import com.beemobi.rongthanonline.item.ItemManager;
import com.beemobi.rongthanonline.item.ItemName;
import com.beemobi.rongthanonline.map.Map;
import com.beemobi.rongthanonline.map.MapManager;
import com.beemobi.rongthanonline.map.MapName;
import com.beemobi.rongthanonline.map.expansion.Expansion;
import com.beemobi.rongthanonline.map.expansion.ExpansionState;
import com.beemobi.rongthanonline.model.PointWeeklyType;
import com.beemobi.rongthanonline.server.Server;
import com.beemobi.rongthanonline.top.TopManager;
import com.beemobi.rongthanonline.top.TopType;
import com.beemobi.rongthanonline.top.expansion.festival.TopMartialArtsFestival;
import com.beemobi.rongthanonline.top.expansion.festival.TopMartialArtsFestivalInfo;
import com.beemobi.rongthanonline.util.Utils;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;

public class MartialArtsFestival extends Expansion implements Runnable {
    public static final int MINUTE_WAIT_REG = 10;
    public static final int LIMIT_PLAYER = 200;
    public static List<String> notes = new ArrayList<>();
    public static LinkedHashMap<Integer, ArrayList<String>> histories = new LinkedHashMap<>();
    public String name;
    public HashMap<Integer, Warrior> warriors;
    public ArrayList<Warrior> participants;
    public Lock lock;
    public long frees;
    public int hour;
    public int minLevel;
    public int maxLevel;
    public int round;
    public int masterId;
    public ArrayList<Warrior> tops;
    public long[] rewards;
    public LinkedHashMap<String, Long> donors;

    static {
        notes.add("Đại hội võ thuật gồm 4 giải đấu: Vệ binh, Chiến binh, Siêu chiến binh và mở rộng");
        notes.add("- Giải mở rộng được tổ chức vào 11h, 13h, 21h, 23h hàng ngày với lệ phí đăng ký là 10000 xu");
        notes.add("- Giải Vệ binh được tổ chức vào 5h và 15h hàng ngày với lệ phí đăng ký là 1000 xu");
        notes.add("- Giải Chiến binh được tổ chức vào 7h và 17h hàng ngày với lệ phí đăng ký là 10000 xu");
        notes.add("- Giải Siêu chiến binh được tổ chức vào 9h và 19h hàng ngày với lệ phí đăng ký là 100000 xu");
        notes.add("- Bạn cũng có thể tạo giải đấu riêng cho mình nhưng sẽ bị hủy nếu trùng với thời gian với giải đấu của Máy chủ");
        notes.add("- Tham gia giải đấu sẽ nhận được x10 Đá cấp 7 và 1 số phần quà ngẫu nhiên khác (đối với giải do Máy chủ tổ chức)");
        notes.add("- Thắng mỗi vòng, chiến binh sẽ nhận được phần thưởng bất kì, riêng vô địch giải đấu sẽ nhận được thêm Capsule Bạch Kim (đối với giải do Máy chủ tổ chức)");
        notes.add("- Bạn sẽ không nhận được phần thưởng nếu túi đồ không còn chỗ trống");
        notes.add("- Các chiến binh có thể tiến hành trao thưởng thêm cho các giải đấu với mức tối thiểu 10tr xu");
    }

    public MartialArtsFestival() {
        super(3600000);
        state = ExpansionState.WAIT_REG;
        lock = new ReentrantLock();
        warriors = new HashMap<>();
        participants = new ArrayList<>();
        donors = new LinkedHashMap<>();
        tops = new ArrayList<>();
        histories.clear();
    }

    public MartialArtsFestival(String name, int minLevel, int maxLevel, int hour, long frees, int masterId) {
        super(3600000);
        state = ExpansionState.WAIT_REG;
        lock = new ReentrantLock();
        warriors = new HashMap<>();
        participants = new ArrayList<>();
        donors = new LinkedHashMap<>();
        tops = new ArrayList<>();
        histories.clear();
        this.name = name;
        this.minLevel = minLevel;
        this.maxLevel = maxLevel;
        this.hour = hour;
        this.frees = frees;
        maps.add(MapManager.getInstance().maps.get(MapName.DAI_HOI_VO_THUAT));
        this.masterId = masterId;
        rewards = new long[4];
        isRunning = true;
        new Thread(this).start();
        Server.getInstance().service.serverChat(String.format("Giải Đại hội võ thuật %s đã bắt đầu", name));
    }


    @Override
    public void close() {
        isRunning = false;
        MapManager.getInstance().martialArtsFestival = null;
    }

    @Override
    public void update() {
        if (isClose) {
            return;
        }
        long now = System.currentTimeMillis();
        if (state == ExpansionState.WAIT_REG) {
            long time = MINUTE_WAIT_REG * 60000L + startTime - now;
            if (time > 0) {
                if (now - updateTime > 60000) {
                    updateTime = now;
                    sendNotification(Player.INFO_YELLOW, String.format("Trận chiến của bạn sẽ diễn ra sau %s", Utils.formatTime(time)));
                }
                return;
            }
            if (warriors.size() < 4) {
                for (Warrior warrior : warriors.values()) {
                    Player player = PlayerManager.getInstance().findPlayerById(warrior.playerId);
                    if (player != null && player.zone != null && player.zone.map.template.id == MapName.DAI_HOI_VO_THUAT
                            && player.taskMain != null && player.taskMain.template.id == 11 && player.taskMain.index == 2) {
                        player.nextTaskParam();
                    }
                }
                sendNotification(Player.INFO_RED, "Giải đấu bị hủy do không đủ chiến binh đăng ký");
                close();
                return;
            }
            participants.addAll(warriors.values());
            for (int i = 0; i < participants.size(); i++) {
                Warrior warrior = participants.get(i);
                Player player = PlayerManager.getInstance().findPlayerById(warrior.playerId);
                if (player != null && player.zone != null && player.zone.map.template.id == MapName.DAI_HOI_VO_THUAT) {
                    if (!player.isBagFull()) {
                        player.addItem(ItemManager.getInstance().createItem(ItemName.DA_7, 10, true), true);
                    }
                    if (player.taskMain != null && player.taskMain.template.id == 11 && player.taskMain.index == 2) {
                        player.nextTaskParam();
                    }
                } else {
                    participants.remove(i);
                    i--;
                }
            }
            state = ExpansionState.RUN;
            int size = participants.size() / 2;
            Map map = maps.get(0);
            map.lock.writeLock().lock();
            try {
                while (map.zones.size() < size) {
                    map.zones.add(new Arena(map));
                }
            } finally {
                map.lock.writeLock().unlock();
            }
            updateTime = now - 110000;
            return;
        }
        if (now - updateTime <= 120000) {
            return;
        }
        updateTime = now;
        round++;
        if (participants.size() == 1) {
            reward();
            close();
            return;
        }
        startRound();
    }

    public void startRound() {
        List<Warrior> participants = this.participants.stream().sorted(Comparator.comparing((Warrior w) -> w.level).reversed()).collect(Collectors.toList());
        if (round >= 3) {
            Collections.shuffle(participants);
        }
        this.participants.clear();
        int index = 0;
        Map map = maps.get(0);
        for (int i = 0; i < participants.size() - 1; i += 2) {
            Warrior warrior1 = participants.get(i);
            Warrior warrior2 = participants.get(i + 1);
            Player player1 = PlayerManager.getInstance().findPlayerById(warrior1.playerId);
            Player player2 = PlayerManager.getInstance().findPlayerById(warrior2.playerId);
            if (player1 == null || player1.zone == null || player1.zone.map.template.id != MapName.DAI_HOI_VO_THUAT) {
                tops.add(0, warrior1);
                if (player2 != null) {
                    if (player2.zone == null || player2.zone.map.template.id != MapName.DAI_HOI_VO_THUAT) {
                        player2.addInfo(Player.INFO_RED, "Bạn bị loại do đã rời Đại hội võ thuật");
                        addHistory(String.format("%s vs %s (cả 2 cùng bỏ cuộc)", warrior1.name, warrior2.name));
                    } else {
                        addParticipant(player2);
                        addHistory(String.format("%s vs %s (%s bỏ cuộc)", warrior1.name, warrior2.name, warrior1.name));
                    }
                }
                if (player1 != null) {
                    player1.addInfo(Player.INFO_RED, "Bạn bị loại do đã rời Đại hội võ thuật");
                }
            } else if (player2 == null || player2.zone == null || player2.zone.map.template.id != MapName.DAI_HOI_VO_THUAT) {
                tops.add(0, warrior2);
                addParticipant(player1);
                addHistory(String.format("%s vs %s (%s bỏ cuộc)", warrior1.name, warrior2.name, warrior2.name));
                if (player2 != null) {
                    player2.addInfo(Player.INFO_RED, "Bạn bị loại do đã rời Đại hội võ thuật");
                }
            } else {
                Arena arena = (Arena) map.zones.get(index++);
                Flight flight = new Flight(this, player1, player2, arena);
                flight.start();
            }
        }
        if (participants.size() % 2 == 1) {
            Warrior warrior = participants.get(participants.size() - 1);
            Player player = PlayerManager.getInstance().findPlayerById(warrior.playerId);
            if (player == null || player.zone == null || player.zone.map.template.id != MapName.DAI_HOI_VO_THUAT) {
                if (player != null) {
                    player.addInfo(Player.INFO_RED, "Bạn bị loại do đã rời Đại hội võ thuật");
                }
            } else {
                addParticipant(player);
                addHistory(String.format("%s vào thẳng vòng trong", warrior.name));
            }
        }
    }

    public void reward() {
        Warrior warrior = participants.get(0);
        tops.add(0, warrior);
        LocalDateTime localNow = LocalDateTime.now();
        TopMartialArtsFestival festival = new TopMartialArtsFestival(String.format("Giải %s\n[%dh %d/%d]", this.name, this.hour, localNow.getDayOfMonth(), localNow.getMonthValue()));
        int size = tops.size();
        for (int i = 0; i < size; i++) {
            Warrior w = tops.get(i);
            TopMartialArtsFestivalInfo topInfo = new TopMartialArtsFestivalInfo();
            topInfo.id = w.playerId;
            topInfo.name = w.name;
            topInfo.score = size - i;
            topInfo.gender = w.gender;
            topInfo.info = String.format("Lv%d: %s", w.level, Utils.formatNumber(w.power));
            festival.elements.add(topInfo);
            if (masterId == -1 && i < 3) {
                Player player = PlayerManager.getInstance().findPlayerById(w.playerId);
                if (player != null) {
                    if (player.taskMain != null && player.taskMain.template.id == 21 && player.taskMain.index == 3) {
                        player.nextTaskParam();
                    }
                    if (i == 0) {
                        player.upParamMissionDaily(7, 1);
                    }
                }
            }
        }
        TopManager.getInstance().topMartialArtsFestival.add(festival);
        Server.getInstance().service.serverChat(String.format("Chúc mừng %s vừa vô địch giải Đại hội võ thuật %s", warrior.name, this.name));
        if (masterId == -1) {
            Player player = PlayerManager.getInstance().findPlayerById(warrior.playerId);
            if (player != null) {
                player.upRuby(5);
                player.addInfo(Player.INFO_YELLOW, "Bạn nhận được 5 Ruby");
                player.upPointAchievement(2, 1);
            }
        }
        long[] rewards = new long[10];
        for (int i = 0; i < rewards.length; i++) {
            if (i >= this.rewards.length) {
                rewards[i] = this.rewards[this.rewards.length - 1];
            } else {
                rewards[i] = this.rewards[i];
            }
        }
        for (int i = 0; i < rewards.length; i++) {
            if (i >= tops.size()) {
                break;
            }
            if (rewards[i] > 0) {
                long reward = rewards[i] - rewards[i] / 20;
                if (reward > 0) {
                    Player player = PlayerManager.getInstance().findPlayerById(tops.get(i).playerId);
                    if (player != null) {
                        player.upXu(rewards[i]);
                        player.addInfo(Player.INFO_YELLOW, String.format("Bạn nhận được %s Xu từ giải đấu %s", Utils.formatNumber(rewards[i]), this.name));
                    }
                }
            }
        }
    }

    public HashMap<Integer, Warrior> getWarriors() {
        lock.lock();
        try {
            return warriors;
        } finally {
            lock.unlock();
        }
    }

    public void addParticipant(Player player) {
        lock.lock();
        try {
            Warrior warrior = participants.stream().filter(w -> w.playerId == player.id).findFirst().orElse(null);
            if (warrior != null) {
                return;
            }
            participants.add(new Warrior(player));
            player.addInfo(Player.INFO_YELLOW, String.format("Bạn được vào vòng %d", round + 1));
            if (masterId == -1) {
                player.upRuby(1);
                player.addInfo(Player.INFO_YELLOW, "Bạn nhận được 1 Ruby");
                player.addItem(ItemManager.getInstance().createItem(ItemName.MANH_GIAY, round, true), true);
                player.addItem(ItemManager.getInstance().createItem(ItemName.DA_7, round, true), true);
                if (player.taskMain != null && player.taskMain.template.id == 11 && player.taskMain.index == 2) {
                    player.nextTaskParam();
                }
                player.upPointWeekly(PointWeeklyType.ACTIVE, round);
            }
        } finally {
            lock.unlock();
        }
    }

    public void addWarrior(Player player) {
        lock.lock();
        try {
            if (warriors.containsKey(player.id)) {
                player.addInfo(Player.INFO_RED, "Bạn đã đăng ký rồi");
                return;
            }
            if (warriors.size() >= LIMIT_PLAYER) {
                player.addInfo(Player.INFO_RED, "Số lượng người tham gia đã đạt tối đa");
                return;
            }
            if (player.level < minLevel || player.level > maxLevel) {
                player.addInfo(Player.INFO_RED, "Cấp độ tham gia giải đấu không phù hợp");
                return;
            }
            if (state != ExpansionState.WAIT_REG) {
                player.addInfo(Player.INFO_RED, "Đã hết thời gian đăng ký");
                return;
            }
            if (frees > 0) {
                if (player.xuKhoa < frees) {
                    player.addInfo(Player.INFO_RED, "Bạn không đủ Xu khóa để đăng ký");
                    return;
                }
                player.upXuKhoa(-frees);
            }
            warriors.put(player.id, new Warrior(player));
            player.addInfo(Player.INFO_YELLOW, String.format("Đăng ký thành công giải %s", name));
        } finally {
            lock.unlock();
        }
    }

    public void addHistory(String history) {
        if (!histories.containsKey(round)) {
            histories.put(round, new ArrayList<>());
        }
        histories.get(round).add(history);
    }

    public void sendNotification(int type, String info) {
        lock.lock();
        try {
            for (Warrior warrior : warriors.values()) {
                Player player = PlayerManager.getInstance().findPlayerById(warrior.playerId);
                if (player != null) {
                    player.addInfo(type, info);
                }
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
            } catch (Exception ignored) {
            }
        }
    }
}
