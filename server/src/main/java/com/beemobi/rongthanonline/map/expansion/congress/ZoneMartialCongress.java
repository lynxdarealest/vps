package com.beemobi.rongthanonline.map.expansion.congress;

import com.beemobi.rongthanonline.bot.boss.Boss;
import com.beemobi.rongthanonline.bot.boss.congress.CellCongress;
import com.beemobi.rongthanonline.bot.npc.Referee;
import com.beemobi.rongthanonline.entity.player.Player;
import com.beemobi.rongthanonline.entity.player.PlayerManager;
import com.beemobi.rongthanonline.map.Map;
import com.beemobi.rongthanonline.map.Zone;
import com.beemobi.rongthanonline.model.MessageTime;

import java.util.ArrayList;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class ZoneMartialCongress extends Zone {

    public static final int[][] POSITION_REFEREE = {{1450, 1296}, {1450, 1008}};
    public static final int[][] POSITION = {{1250, 1008}, {1650, 1008}};
    public MartialCongress martial;
    public Referee referee;
    public int warriorId = -1;
    public ArrayList<Integer> waiters = new ArrayList<>();
    public int status;
    public Boss boss;
    public long last;
    public int time;
    public ReadWriteLock lockWait = new ReentrantReadWriteLock();

    public ZoneMartialCongress(Map map, MartialCongress martial) {
        super(map);
        this.martial = martial;
        referee = new Referee();
        referee.x = POSITION_REFEREE[0][0];
        referee.y = POSITION_REFEREE[0][1];
        players.add(referee);
        referee.zone = this;
    }

    @Override
    public void update() {
        super.update();
        if (warriorId == -1) {
            if (!waiters.isEmpty()) {
                warriorId = waiters.get(0);
                waiters.remove(0);
                if (martial.isWinner(warriorId)) {
                    warriorId = -1;
                }
            }
            status = 0;
            if (boss != null) {
                leave(boss);
            }
            boss = null;
            return;
        }
        Player player = findPlayerById(warriorId);
        if (player != null && !player.isPlayer()) {
            warriorId = -1;
            return;
        }
        if (player == null) {
            try {
                setPosition(referee, POSITION_REFEREE[0][0], POSITION_REFEREE[0][1]);
                if (status == 2) {
                    player = PlayerManager.getInstance().findPlayerById(warriorId);
                    if (player != null) {
                        player.addInfo(Player.INFO_RED, "Bạn đã thua vì bỏ chạy");
                        endFlight(player, false);
                    }
                }
            } finally {
                warriorId = -1;
            }
            return;
        }

        // bắt đầu trận chiến -> check đánh đến boss nào và gọi boss ra
        if (status == 0) {
            boss = createBoss(player);
            boss.x = POSITION[1][0];
            boss.y = POSITION[1][1];
            enter(boss);
            resetPosition(player, boss);
            time = 15;
            last = System.currentTimeMillis();
            status = 1;
            setPosition(referee, POSITION_REFEREE[1][0], POSITION_REFEREE[1][1]);
            return;
        }

        // chờ cho trọng tài đếm giờ
        if (status == 1) {
            long now = System.currentTimeMillis();
            if (now - last >= 1000) {
                last = now;
                time--;
                if (time == 12) {
                    /*player.addItem(new ItemTime(ItemTimeName.DICH_CHUYEN_TUC_THOI, 15000));
                    boss.addItemTime(new ItemTime(ItemTimeName.DICH_CHUYEN_TUC_THOI, 15000));*/
                    service.chatPublic(referee, String.format("Trận đấu giữa %s và %s sắp diễn ra", player.name, boss.name));
                } else if (time == 8) {
                    service.chatPublic(referee, "Xin quý vị khán giả cho 1 tràng pháo tay cổ vũ cho 2 đấu thủ nào");
                } else if (time == 4) {
                    service.chatPublic(referee, "Mọi người hãy ổn định chỗ ngồi, trận đấu sẽ bắt đầu sau 3 giây nữa");
                } else if (time == 1) {
                    service.chatPublic(referee, "Trận đấu bắt đầu");
                } else if (time == 0) {
                    startFight(player, boss);
                    status = 2;
                    time = 181;
                    player.addMessageTime(MessageTime.TIME_COMBAT_ARENA_CELL, time * 1000L);
                }
            }
            return;
        }

        // trận chiến
        if (status == 2) {
            long now = System.currentTimeMillis();
            if (now - last >= 1000) {
                last = now;
                time--;
                if (time <= 0) {
                    player.addInfo(Player.INFO_RED, "Hết thời gian thi đấu, bạn đã thua");
                    endFlight(player, false);
                    return;
                }
            }
            if (player.isDead()) {
                player.addInfo(Player.INFO_RED, "Bạn đã thua vì kiệt sức");
                endFlight(player, false);
            } else if (player.y > POSITION[0][1]) {
                player.addInfo(Player.INFO_RED, "Bạn đã thua vì rớt đài");
                endFlight(player, false);
            } else if (boss.isDead()) {
                endFlight(player, true);
            }
        }
    }

    public void endFlight(Player player, boolean isWin) {
        if (player.isDead()) {
            player.hp = player.maxHp;
            player.mp = player.maxMp;
            player.isDie = false;
            player.service.wakeUpFromDead();
            service.playerWakeUpFromDead(player);
        }
        warriorId = -1;
        if (!isWin) {
            setPosition(player, POSITION_REFEREE[0][0] - 100, POSITION_REFEREE[0][1]);
            referee.chat(String.format("%s đã thua đối thủ %s", player.name, boss.name));
        } else {
            int level = martial.getLevel(player);
            if (level < MartialCongress.MAX_LEVEL) {
                referee.chat(String.format("Chúc mừng %s đã chiến thắng đối thủ %s", player.name, boss.name));
                addWaiter(player, 0);
            } else {
                referee.chat(String.format("Chúc mừng %s đã vô địch giải đấu", player.name));
                martial.addWinner(player);
                setPosition(player, POSITION_REFEREE[0][0] - 100, POSITION_REFEREE[0][1]);
            }
            martial.setLevel(player, level + 1);
        }
        boss.clearPk();
        player.clearPk();
        player.removeMessageTime(MessageTime.TIME_COMBAT_ARENA_CELL);
        leave(boss);
    }

    public void addWaiter(Player player, int index) {
        lockWait.writeLock().lock();
        try {
            if (index != -1) {
                waiters.removeIf(p -> p == player.id);
                waiters.add(index, player.id);
                return;
            }
            if (warriorId == player.id || waiters.contains(player.id)) {
                player.addInfo(Player.INFO_RED, "Không thể đăng ký thêm");
                return;
            }
            if (martial.isWinner(player.id)) {
                player.addInfo(Player.INFO_RED, "Bạn đã vô địch giải đấu, không thể đăng ký thêm");
                return;
            }
            if (martial.isReward(player.id)) {
                player.addInfo(Player.INFO_RED, "Bạn đã nhận thưởng, không thể đăng ký thêm");
                return;
            }
            int count = martial.getCount(player);
            long gold = count * 500_000L + 500_000L;
            if (player.xu < gold) {
                player.addInfo(Player.INFO_RED, "Bạn không đủ xu để đăng ký thi đấu");
                return;
            }
            player.upXu(-gold);
            martial.setCount(player, count + 1);
            waiters.add(player.id);
            player.addInfo(Player.INFO_YELLOW, String.format("Số thứ tự của bạn là %s", waiters.size()));
        } finally {
            lockWait.writeLock().unlock();
        }
    }

    public void startFight(Player player, Boss boss) {
        setPosition(referee, POSITION_REFEREE[0][0], POSITION_REFEREE[0][1]);
        player.recovery(Player.RECOVERY_ALL, 100, true);
        boss.recovery(Player.RECOVERY_ALL, 100, true);
        resetPosition(player, boss);
        player.testPlayerId = boss.id;
        boss.testPlayerId = player.id;
        player.setTypePk(1);
        boss.setTypePk(1);
        service.playerFlight(player, boss);
    }

    public void resetPosition(Player player, Boss boss) {
        setPosition(player, POSITION[0][0], POSITION[0][1]);
        setPosition(boss, POSITION[1][0], POSITION[1][1]);
    }

    public void setPosition(Player player, int x, int y) {
        player.x = x;
        player.y = y;
        service.setPosition(player);
    }

    public Boss createBoss(Player player) {
        return new CellCongress(martial.getLevel(player));
    }


}
