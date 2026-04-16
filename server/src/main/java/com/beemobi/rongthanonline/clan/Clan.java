package com.beemobi.rongthanonline.clan;

import com.beemobi.rongthanonline.data.ClanData;
import com.beemobi.rongthanonline.entity.monster.MonsterManager;
import com.beemobi.rongthanonline.entity.monster.MonsterTemplate;
import com.beemobi.rongthanonline.entity.player.Player;
import com.beemobi.rongthanonline.entity.player.PlayerManager;
import com.beemobi.rongthanonline.map.Map;
import com.beemobi.rongthanonline.map.MapManager;
import com.beemobi.rongthanonline.map.MapName;
import com.beemobi.rongthanonline.map.expansion.manor.Manor;
import com.beemobi.rongthanonline.repository.GameRepository;
import com.beemobi.rongthanonline.service.ClanService;
import com.beemobi.rongthanonline.task.TaskClan;
import com.beemobi.rongthanonline.task.TaskClanType;
import com.beemobi.rongthanonline.top.Top;
import com.beemobi.rongthanonline.top.TopManager;
import com.beemobi.rongthanonline.top.TopType;
import com.beemobi.rongthanonline.util.Utils;
import org.apache.log4j.Logger;

import java.sql.Date;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.stream.Collectors;

public class Clan {
    private static final Logger logger = Logger.getLogger(Clan.class);
    public static final int MAX_LEVEL = 10;
    public static final long[] EXP_LEVEL = new long[]{5000, 10000, 20000, 40000, 80000, 120000, 160000, 200000, 240000, 280000, 320000};
    public static final long[] COIN_UPGRADE = new long[]{10000000, 500000000, 2000000000, 5000000000L, 10000000000L, 20000000000L, 30000000000L, 40000000000L, 50000000000L, 60000000000L, 70000000000L, 80000000000L, 90000000000L};
    public static List<String> notes;

    public int id;
    public String name;
    public String slogan;
    public String notification;
    public int level;
    public Timestamp createTime;
    public Timestamp resetTime;
    public Timestamp updateTime;
    public long exp;
    public long coin;
    public int leaderId;
    public ArrayList<ClanMember> members;
    public Map map;
    public ReadWriteLock lock = new ReentrantReadWriteLock();
    public ClanService service;
    public TaskClan taskClan;
    public long receiveTaskTime;
    public Manor manor;
    public int countManor;
    public int bonusSlot;
    public transient boolean isCharged;

    static {
        notes = new ArrayList<>();
        notes.add("Bang hội có thể nâng cấp khi điểm kinh nghiệm đạt tối đa");
        notes.add("Khi tham gia các hoạt động sau sẽ tăng điểm kinh nghiệm bang:");
        notes.add("- Làm nhiệm vụ bang: Khi hoàn thành nhiệm vụ bang nhanh và càng nhiều thành viên có mặt trong Lãnh địa tại thời điểm báo cáo hoàn thành thì điểm kinh nghiệm bang sẽ được tăng nhiều hơn. Tối đa 10 điểm với mỗi thành viên");
        notes.add("- Chiến thắng mỗi khu Ngọc rồng Namek sẽ tăng 100 điểm");
        notes.add("- Hạ Boss tại Thành phố Lãng quên vào 21h30 sẽ tăng 1 điểm với mỗi thành viên bang tham gia. Nhưng sẽ không được tăng điểm nếu có mặt các chiến binh thuộc bang hội khác hoặc không có bang hội (chỉ tính các chiến binh còn sống)");
    }

    public Clan(ClanData data) {
        id = data.id;
        name = data.name;
        slogan = data.slogan;
        notification = data.notification;
        level = data.level;
        exp = data.exp;
        leaderId = data.leaderId;
        bonusSlot = data.bonusSlot;
        coin = data.coin;
        createTime = data.createTime;
        resetTime = data.resetTime;
        countManor = data.countManor;
        updateTime = data.updateTime;
        members = new ArrayList<>();
        map = new Map(MapManager.getInstance().mapTemplates.get(MapName.LANH_DIA_BANG_HOI));
        service = new ClanService(this);
    }

    public int getMaxMember() {
        return 9 + level + bonusSlot;
    }

    public long getMaxExp() {
        return EXP_LEVEL[level - 1];
    }

    public long getTotalExp() {
        long total = exp;
        for (int i = 0; i < level; i++) {
            total += EXP_LEVEL[i];
        }
        return total;
    }

    public String getStrCreateTime() {
        Date date = new Date(createTime.getTime());
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss dd-MM-yyyy");
        return formatter.format(date);
    }

    public ClanMember findMemberByPlayerId(int playerId) {
        lock.readLock().lock();
        try {
            return members.stream().filter(m -> m.playerId == playerId).findFirst().orElse(null);
        } finally {
            lock.readLock().unlock();
        }
    }

    public ClanMember getLeader() {
        lock.readLock().lock();
        try {
            return members.stream().filter(m -> m.playerId == leaderId).findFirst().orElse(null);
        } finally {
            lock.readLock().unlock();
        }
    }

    public void upPointMember(int memberId, int point) {
        lock.readLock().lock();
        try {
            members.stream().filter(m -> m.playerId == memberId).findFirst().ifPresent(member -> member.upPoint(point));
        } finally {
            lock.readLock().unlock();
        }
    }

    public List<ClanMember> findMemberByRole(int role) {
        lock.readLock().lock();
        try {
            return members.stream().filter(m -> m.role == role).collect(Collectors.toList());
        } finally {
            lock.readLock().unlock();
        }
    }

    public List<ClanMember> getListMember() {
        lock.readLock().lock();
        try {
            return members.stream().toList();
        } finally {
            lock.readLock().unlock();
        }
    }

    public void addMember(ClanMember member) {
        lock.writeLock().lock();
        try {
            members.add(member);
        } finally {
            lock.writeLock().unlock();
        }
    }

    public void setSlogan(String slogan) {
        this.slogan = slogan;
        isCharged = true;
        service.setSlogan();
    }

    public void setNotification(String notification) {
        this.notification = notification;
        isCharged = true;
        service.setNotification();
    }

    public void setLeaderId(int leaderId) {
        this.leaderId = leaderId;
        isCharged = true;
    }

    public void upCoin(long coin) {
        this.coin += coin;
        isCharged = true;
        service.setCoin();
    }

    public long getCoinCreateTask() {
        return 1000000L + 2000000L * (level - 1);
    }

    public long getCoinUpgrade() {
        return COIN_UPGRADE[level - 1];
    }

    public void upgrade(Player player) {
        if (level >= MAX_LEVEL) {
            player.addInfo(Player.INFO_RED, "Bang hội đã đạt cấp tối đa");
            return;
        }
        ClanMember member = findMemberByPlayerId(player.id);
        if (member.role != 0 && member.role != 1) {
            player.addInfo(Player.INFO_RED, "Chức năng chỉ dành cho bang chủ hoặc bang phó");
            return;
        }
        if (exp < getMaxExp()) {
            player.addInfo(Player.INFO_RED, "Bang hội chưa đủ exp");
            return;
        }
        long coin = getCoinUpgrade();
        if (this.coin < coin) {
            player.addInfo(Player.INFO_RED, "Ngân sách của bang hội không đủ");
            return;
        }
        upCoin(-coin);
        level++;
        exp = 0;
        isCharged = true;
        service.addInfo(Player.INFO_YELLOW, String.format("Bang hội đã được nâng cấp lên cấp %d", level));
        player.service.setClanInfo(false);
    }

    public void createTask(Player player) {
        ClanMember member = findMemberByPlayerId(player.id);
        if (member.role != 0 && member.role != 1) {
            player.addInfo(Player.INFO_RED, "Chức năng chỉ dành cho bang chủ hoặc bang phó");
            return;
        }
        if (taskClan != null) {
            player.addInfo(Player.INFO_RED, "Bạn cần phải hoàn thành nhiệm vụ trước đó");
            return;
        }
        long require = getCoinCreateTask();
        if (coin < require) {
            player.addInfo(Player.INFO_RED, "Ngân sách của bang hội không đủ");
            return;
        }
        long now = System.currentTimeMillis();
        long time = 3600000 - now + receiveTaskTime;
        if (time > 0) {
            player.addInfo(Player.INFO_RED, String.format("Chỉ có thể nhận nhiệm vụ sau %s", Utils.formatTime(time)));
            return;
        }
        receiveTaskTime = now;
        upCoin(-require);
        TaskClan taskClan = new TaskClan();
        taskClan.startTime = now;
        taskClan.type = Utils.nextInt(3);
        switch (taskClan.type) {
            case TaskClanType.KILL_BOSS: {
                taskClan.name = "Cùng ít nhất 1 thành viên khác hạ boss";
                taskClan.maxParam = Utils.nextInt(4 * level, 6 * level);
                break;
            }
            case TaskClanType.KILL_MONSTER: {
                int num = 3;
                for (int i = MonsterManager.getInstance().monsterTemplates.size() - 1; i >= 0; i--) {
                    MonsterTemplate monsterTemplate = MonsterManager.getInstance().monsterTemplates.get(i);
                    if (monsterTemplate.level > 1 && monsterTemplate.level / 10 == level && monsterTemplate.id != 43) {
                        if (Utils.nextInt(100) < 50) {
                            num = 0;
                        } else {
                            num--;
                        }
                        if (num <= 0) {
                            taskClan.objectId = monsterTemplate.id;
                            taskClan.name = "Cùng ít nhất 1 thành viên khác tiêu diệt " + monsterTemplate.name;
                            break;
                        }
                    }
                }
                taskClan.maxParam = Utils.nextInt(500 + 100 * level, 700 + 200 * level);
                break;
            }
            case TaskClanType.KILL_SUPER_MONSTER: {
                int num = 3;
                for (int i = MonsterManager.getInstance().monsterTemplates.size() - 1; i >= 0; i--) {
                    MonsterTemplate monsterTemplate = MonsterManager.getInstance().monsterTemplates.get(i);
                    if (monsterTemplate.level > 1 && monsterTemplate.level / 10 == level && monsterTemplate.id != 43) {
                        if (Utils.nextInt(100) < 50) {
                            num = 0;
                        } else {
                            num--;
                        }
                        if (num <= 0) {
                            taskClan.objectId = monsterTemplate.id;
                            taskClan.name = "Cùng ít nhất 1 thành viên khác tiêu diệt Tinh Ranh hoặc Đầu đàn " + monsterTemplate.name;
                            break;
                        }
                    }
                }
                taskClan.maxParam = Utils.nextInt(20 + 10 * level, 30 + 20 * level);
                break;
            }
        }
        this.taskClan = taskClan;
        service.addInfo(Player.INFO_YELLOW, String.format("%s đã nhận nhiệm vụ %s cho bang hội", this.name, taskClan.toString()));
        service.setNotification();
    }

    public String getNotification() {
        if (taskClan == null) {
            return notification;
        }
        String info = taskClan.toString();
        if (info.length() > 60) {
            return String.format("%s\nNhiệm vụ: %s\n%s", notification, info.substring(0, 60), info.substring(60, taskClan.toString().length()));
        }
        return String.format("%s\nNhiệm vụ: %s", notification, info);
    }

    public void upExp(int exp, boolean isMultiPlayer) {
        if (level >= MAX_LEVEL) {
            return;
        }
        long total = 0;
        if (isMultiPlayer) {
            lock.readLock().lock();
            try {
                for (ClanMember member : members) {
                    Player player = PlayerManager.getInstance().findPlayerById(member.playerId);
                    if (player != null && player.zone != null && player.zone.map.template.id == MapName.LANH_DIA_BANG_HOI) {
                        this.exp += exp;
                        total += exp;
                        player.upRuby(1);
                        player.addInfo(Player.INFO_YELLOW, String.format("Bạn nhận được %d Ruby", 1));
                        member.upPoint(exp);
                    }
                }
            } finally {
                lock.readLock().unlock();
            }
        } else {
            this.exp += exp;
            total += exp;
        }
        isCharged = true;
        if (total > 0) {
            updateTime = new Timestamp(System.currentTimeMillis());
        }
    }

    public void upExpFlagWar(int exp) {
        long last = this.exp;
        List<ClanMember> memberList = getListMember();
        for (ClanMember member : memberList) {
            Player player = PlayerManager.getInstance().findPlayerById(member.playerId);
            if (player != null && player.zone != null && player.zone.map.template.id == MapName.HANH_TINH_NGUC_TU) {
                if (level < MAX_LEVEL) {
                    this.exp += exp;
                }
                member.upPoint(exp);
                player.pointFlagWar += exp;
            }
        }
        isCharged = true;
        if (last != this.exp) {
            updateTime = new Timestamp(System.currentTimeMillis());
        }
    }

    public void sort() {
        try {
            members.sort(new Comparator<ClanMember>() {
                @Override
                public int compare(ClanMember m1, ClanMember m2) {
                    return Integer.compare(m1.role, m2.role);
                }
            });
        } catch (Exception ex) {
            logger.error("sort", ex);
        }
    }

    public void update(long now) {
        int day = Utils.getCountDay(resetTime);
        if (day <= 0) {
            return;
        }
        resetTime = new Timestamp(now);
        setCountManor(1);
        lock.readLock().lock();
        try {
            for (ClanMember member : members) {
                member.setPointDay(0);
            }
        } finally {
            lock.readLock().unlock();
        }
        isCharged = true;
    }

    public void setCountManor(int count) {
        if (countManor != count) {
            countManor = count;
            isCharged = true;
        }
    }

    public void saveData() {
        lock.readLock().lock();
        try {
            if (isCharged) {
                GameRepository.getInstance().clanData.saveData(id, name, slogan, notification, level, leaderId, exp, coin, resetTime, updateTime, countManor);
                isCharged = false;
            }
            for (ClanMember member : members) {
                member.saveData();
            }
        } catch (Exception ex) {
            logger.error("saveData", ex);
        } finally {
            lock.readLock().unlock();
        }
    }
}
