package com.beemobi.rongthanonline.lucky;

import com.beemobi.rongthanonline.command.Command;
import com.beemobi.rongthanonline.command.CommandName;
import com.beemobi.rongthanonline.common.Language;
import com.beemobi.rongthanonline.common.RandomCollection;
import com.beemobi.rongthanonline.entity.player.Player;
import com.beemobi.rongthanonline.entity.player.PlayerManager;
import com.beemobi.rongthanonline.network.Message;
import com.beemobi.rongthanonline.network.MessageName;
import com.beemobi.rongthanonline.npc.NpcName;
import com.beemobi.rongthanonline.server.Server;
import com.beemobi.rongthanonline.server.ServerMaintenance;
import com.beemobi.rongthanonline.util.Utils;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class LuckyCoin {
    private static final Logger logger = Logger.getLogger(LuckyCoin.class);

    public static final long MAX_COIN = 100000000L;
    public static final long MIN_COIN = 1000000L;

    public static final long DELAY = 120000;
    public boolean isRunning;
    public String name;
    public long total;
    public String winner;
    public ArrayList<LuckyPlayer> gamers;
    public int time;
    public long endTime;
    public int countPlayer;
    public ReadWriteLock lock = new ReentrantReadWriteLock();
    public Thread actionUpdate;
    public int status;
    public long updateTime;
    public int roundId;

    public LuckyCoin() {
        this.name = "Chọn ai đây";
        gamers = new ArrayList<>();
        winner = "Chưa có thông tin|0|0";
        endTime = System.currentTimeMillis() + DELAY;
        start();
    }

    public void update() {
        long now = System.currentTimeMillis();
        if (status == 0) {
            if (now >= endTime) {
                endTime = now + 3000L;
                status = 1;
                updateUI();
            }
            return;
        }
        if (status == 1) {
            if (now >= endTime) {
                endTime = now + 10000;
                if (countPlayer > 0) {
                    status = 2;
                } else {
                    status = 3;
                }
                updateUI();
            }
            return;
        }
        if (status == 2) {
            if (now >= endTime) {
                endTime = now + 10000L;
                status = 3;
                lock.readLock().lock();
                try {
                    RandomCollection<LuckyPlayer> random = new RandomCollection<>();
                    for (LuckyPlayer gamer : gamers) {
                        if (gamer != null && gamer.quantity > 0) {
                            random.add(gamer.quantity, gamer);
                        }
                    }
                    if (!random.isEmpty()) {
                        LuckyPlayer winner = random.next();
                        long coin = total - total / 20;
                        this.winner = String.format("%s|%s|%s", winner.name, Utils.getMoneys(coin), Utils.getMoneys(winner.quantity));
                        Player player = PlayerManager.getInstance().findPlayerById(winner.id);
                        if (player != null) {
                            player.upXu(coin);
                        }
                        Server.getInstance().service.serverChat(String.format("Chúc mừng %s vừa chiến thắng %s và nhận được %s xu", winner.name, this.name, Utils.getMoneys(coin)));
                    }
                } finally {
                    lock.readLock().unlock();
                }
                updateUI();
            }
            return;
        }
        if (status == 3) {
            if (now >= endTime) {
                lock.writeLock().lock();
                try {
                    total = 0;
                    countPlayer = 0;
                    roundId++;
                    gamers.clear();
                    status = 0;
                    endTime = now + DELAY;
                } finally {
                    lock.writeLock().unlock();
                }
                updateUI();
            }
            return;
        }
    }

    public void updateUI() {
        List<Player> playerList = PlayerManager.getInstance().getPlayers().stream().filter(p -> p.tabID == 33).toList();
        if (playerList.isEmpty()) {
            return;
        }
        if (status == 0 || status == 1) {
            for (Player player : playerList) {
                show(player, null);
            }
            return;
        }
        if (status == 2) {
            List<String> names = getListNameLuckyPlayer();
            for (Player player : playerList) {
                show(player, names);
            }
            return;
        }
        if (status == 3) {
            for (Player player : playerList) {
                show(player, null);
            }
            return;
        }
    }

    public boolean isCantJoin() {
        return status != 0 || endTime <= System.currentTimeMillis() + 1000;
    }

    public void join(Player player, long coin) {
        lock.writeLock().lock();
        try {
            if (ServerMaintenance.getInstance().isRunning) {
                player.addInfo(Player.INFO_RED, Language.CANCEL_ACTION_WHEN_SERVER_MAINTENANCE);
                return;
            }
            if (player.isTrading()) {
                player.addInfo(Player.INFO_RED, Language.CANCEL_ACTION_WHEN_TRADE);
                return;
            }
            if (isCantJoin()) {
                player.addInfo(Player.INFO_RED, "Đã hết thời gian tham gia");
                return;
            }
            LuckyPlayer gamer = gamers.stream().filter(p -> p.id == player.id).findFirst().orElse(null);
            if (gamer != null) {
                player.addInfo(Player.INFO_RED, "Bạn đã tham gia rồi, không thể tham gia thêm được nữa");
                return;
            }
            if (coin < MIN_COIN) {
                player.addInfo(Player.INFO_RED, String.format("Chỉ có thể tham gia tối thiểu %s xu", Utils.formatNumber(MIN_COIN)));
                return;
            }
            if (coin > MAX_COIN) {
                player.addInfo(Player.INFO_RED, String.format("Chỉ có thể tham gia tối đa %s xu", Utils.formatNumber(MAX_COIN)));
                return;
            }
            if (player.xu < coin) {
                player.addInfo(Player.INFO_RED, "Bạn không đủ xu");
                return;
            }
            player.upXu(-coin);
            gamers.add(new LuckyPlayer(player, coin));
            total += coin;
            countPlayer = gamers.size();
        } finally {
            lock.writeLock().unlock();
        }
        updateUI();
    }

    public void updateUI(Player player, List<String> names) {
        List<Player> playerList = PlayerManager.getInstance().getPlayers().stream()
                .filter(p -> p != player && p.tabID == 103).toList();
        if (!playerList.isEmpty()) {
            for (Player p : playerList) {
                show(p, names);
            }
        }
    }

    public void open(Player player) {
        try {
            Message message = new Message(MessageName.LUCKY_PICK_ME);
            message.writer().writeInt(-1);
            player.sendMessage(message);
        } catch (Exception ex) {
            logger.error("open", ex);
        }
    }

    public void show(Player player, List<String> names) {
        try {
            Message message = new Message(MessageName.LUCKY_PICK_ME);
            message.writer().writeInt(roundId);
            message.writer().writeLong(endTime);
            message.writer().writeByte(status);
            if (status == 0 || status == 1) {
                String[] array = winner.split("\\|");
                message.writer().writeUTF(array[0]);
                message.writer().writeUTF(array[1]);
                message.writer().writeUTF(array[2]);
                message.writer().writeLong(total);
                message.writer().writeShort(countPlayer);
                message.writer().writeLong(getQuantity(player.id));
            } else if (status == 2) {
                if (names == null) {
                    names = getListNameLuckyPlayer();
                }
                message.writer().writeShort(names.size());
                for (String name : names) {
                    message.writer().writeUTF(name);
                }
            } else if (status == 3) {
                if (countPlayer > 0) {
                    String[] array = winner.split("\\|");
                    message.writer().writeUTF(array[0]);
                } else {
                    message.writer().writeUTF("Không có người tham gia");
                }
            }
            player.sendMessage(message);
        } catch (Exception ex) {
            logger.error("show", ex);
        }
    }

    public void read(Message message, Player player) {
        try {
            int action = message.reader().readByte();
            switch (action) {
                case 0:
                    note(player);
                    break;

                case 1:
                    int coin = message.reader().readInt();
                    if (coin > 0) {
                        player.lockAction.lock();
                        try {
                            join(player, coin);
                        } finally {
                            player.lockAction.unlock();
                        }
                    }
                    break;
            }
        } catch (Exception ex) {
            logger.error("read", ex);
        }
    }

    public void note(Player player) {
        StringBuilder content = new StringBuilder();
        content.append("Luật chơi:").append("\n");
        content.append("- Mỗi vòng chơi sẽ diễn ra trong 120 giây").append("\n");
        content.append("- Bạn có thể tham gia tối thiểu 1tr xu và tối đa 100tr xu").append("\n");
        player.service.createMenu(NpcName.ME, content.toString(), List.of(new Command(CommandName.CANCEL, "OK", player)));
    }

    public List<String> getListNameLuckyPlayer() {
        List<String> names = new ArrayList<>();
        lock.readLock().lock();
        try {
            for (LuckyPlayer player : gamers) {
                names.add(player.name);
            }
        } finally {
            lock.readLock().unlock();
        }
        return names;
    }

    public long getQuantity(int id) {
        long coin = 0L;
        lock.readLock().lock();
        try {
            LuckyPlayer gamer = gamers.stream().filter(p -> p.id == id).findFirst().orElse(null);
            if (gamer != null) {
                coin = gamer.quantity;
            }
        } finally {
            lock.readLock().unlock();
        }
        return coin;
    }

    public void start() {
        isRunning = true;
        actionUpdate = new Thread(() -> {
            long delay = 1000;
            while (isRunning) {
                try {
                    long start = System.currentTimeMillis();
                    update();
                    long end = System.currentTimeMillis();
                    long time = end - start;
                    if (time > delay) {
                        continue;
                    }
                    try {
                        Thread.sleep(delay - time);
                    } catch (InterruptedException ignored) {
                    }
                } catch (Exception ex) {
                    logger.error(String.format("update %s error", this.getClass().getName()), ex);
                }
            }
        });
        actionUpdate.setName(String.format("thead update %s", this.name));
        actionUpdate.start();
    }
}
