package com.beemobi.rongthanonline.entity.player;

import com.beemobi.rongthanonline.item.Item;
import com.beemobi.rongthanonline.map.ZoneGoBack;
import com.beemobi.rongthanonline.network.Session;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.stream.Collectors;

public class PlayerManager {
    private static final Logger logger = Logger.getLogger(PlayerManager.class);
    private static PlayerManager instance;
    public final HashMap<Integer, Player> players = new HashMap<>();
    public final ConcurrentHashMap<Integer, ZoneGoBack> areaGoBacks = new ConcurrentHashMap<>();
    public HashMap<Integer, Long> timesReceiveCapsuleDong = new HashMap<>();
    public final ReadWriteLock lock = new ReentrantReadWriteLock();

    public static PlayerManager getInstance() {
        if (instance == null) {
            instance = new PlayerManager();
        }
        return instance;
    }

    public boolean addPlayer(Player player) {
        lock.writeLock().lock();
        try {
            if (!players.containsKey(player.id)) {
                players.put(player.id, player);
                return true;
            }
            return false;
        } finally {
            lock.writeLock().unlock();
        }
    }

    public void removePlayer(Player player) {
        lock.writeLock().lock();
        try {
            players.remove(player.id);
        } finally {
            lock.writeLock().unlock();
        }
    }

    public Player findPlayerById(int id) {
        lock.readLock().lock();
        try {
            return players.getOrDefault(id, null);
        } finally {
            lock.readLock().unlock();
        }
    }

    public Player findPlayerByName(String name) {
        lock.readLock().lock();
        try {
            return players.values().stream().filter(player -> player.name.equals(name)).findFirst().orElse(null);
        } finally {
            lock.readLock().unlock();
        }
    }

    public List<Player> getPlayers() {
        List<Player> playerList = new ArrayList<>();
        lock.readLock().lock();
        try {
            playerList.addAll(players.values());
        } finally {
            lock.readLock().unlock();
        }
        return playerList;
    }

    public List<Player> findListPlayerInIp(String ip) {
        lock.readLock().lock();
        try {
            return players.values().stream().filter(player -> player.session.ip.equals(ip)).collect(Collectors.toList());
        } finally {
            lock.readLock().unlock();
        }
    }

    public List<Player> findPlayerByUserId(int userId) {
        lock.readLock().lock();
        try {
            return players.values().stream().filter(player -> player.userId == userId).collect(Collectors.toList());
        } finally {
            lock.readLock().unlock();
        }
    }

    public boolean isOnline(int playerId) {
        lock.readLock().lock();
        try {
            return players.containsKey(playerId);
        } finally {
            lock.readLock().unlock();
        }
    }

    public void dialogMessage(String message) {
        lock.readLock().lock();
        try {
            for (Player player : players.values()) {
                try {
                    player.service.startDialogOk(message);
                } catch (Exception ex) {
                    logger.error("saveData", ex);
                }
            }
        } finally {
            lock.readLock().unlock();
        }
    }

    public void saveData() {
        lock.readLock().lock();
        try {
            for (Player player : players.values()) {
                try {
                    Session ss = player.session;
                    if (ss.isLoginCompleted && ss.socket != null && !ss.socket.isClosed() && ss.player != null) {
                        player.saveData(false);
                    }
                } catch (Exception ex) {
                    logger.error("saveData", ex);
                }
            }
        } finally {
            lock.readLock().unlock();
        }
    }

    public int getCountPlayer() {
        lock.readLock().lock();
        try {
            return players.size();
        } finally {
            lock.readLock().unlock();
        }
    }
}
