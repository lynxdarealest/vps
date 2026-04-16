package com.beemobi.rongthanonline.network;

import com.beemobi.rongthanonline.entity.player.Player;
import com.beemobi.rongthanonline.entity.player.PlayerManager;
import org.apache.log4j.Logger;

import java.util.HashMap;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.stream.Collectors;

public class SessionManager {
    private static final Logger logger = Logger.getLogger(SessionManager.class);
    private static SessionManager instance;
    public final HashMap<Integer, Session> sessions = new HashMap<>();
    public final ReadWriteLock lock = new ReentrantReadWriteLock();
    public HashMap<String, Long> userLogins = new HashMap<>();
    public Lock lockUserLogin = new ReentrantLock();

    public static SessionManager getInstance() {
        if (instance == null) {
            instance = new SessionManager();
        }
        return instance;
    }

    public void addUserLogout(String username) {
        lockUserLogin.lock();
        try {
            userLogins.put(username, System.currentTimeMillis());
        } finally {
            lockUserLogin.unlock();
        }
    }

    public long getTimeUserLogin(String username) {
        lockUserLogin.lock();
        try {
            long time = userLogins.getOrDefault(username, 0L);
            userLogins.put(username, System.currentTimeMillis());
            return time;
        } finally {
            lockUserLogin.unlock();
        }
    }

    public void addSession(Session session) {
        lock.writeLock().lock();
        try {
            if (!sessions.containsKey(session.id)) {
                sessions.put(session.id, session);
            }
        } finally {
            lock.writeLock().unlock();
        }
    }

    public void removeSession(Session session) {
        lock.writeLock().lock();
        try {
            sessions.remove(session.id);
        } finally {
            lock.writeLock().unlock();
        }
    }

    public List<Session> findListSessionWaitLogin(int playerId) {
        lock.readLock().lock();
        try {
            return sessions.values().stream().filter(s -> {
                return s.player == null && !s.isLoginCompleted && s.waitId == playerId;
            }).collect(Collectors.toList());
        } finally {
            lock.readLock().unlock();
        }
    }

}
