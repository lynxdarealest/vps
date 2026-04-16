package com.beemobi.rongthanonline.clan;

import com.beemobi.rongthanonline.data.ClanData;
import com.beemobi.rongthanonline.data.ClanMemberData;
import com.beemobi.rongthanonline.repository.GameRepository;
import com.beemobi.rongthanonline.server.Server;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class ClanManager implements Runnable {
    private static final Logger logger = Logger.getLogger(ClanManager.class);
    private static ClanManager instance;
    public HashMap<Integer, Clan> clans;
    public ReadWriteLock lock = new ReentrantReadWriteLock();
    public boolean isRunning;

    public static ClanManager getInstance() {
        if (instance == null) {
            instance = new ClanManager();
        }
        return instance;
    }

    public void init() {
        clans = new HashMap<>();
        List<ClanData> clanDataList = GameRepository.getInstance().clanData.findAll();
        for (ClanData data : clanDataList) {
            clans.put(data.id, new Clan(data));
        }
        ArrayList<ClanMemberData> removes = new ArrayList<>();
        List<ClanMemberData> members = GameRepository.getInstance().clanMemberData.findByServer(Server.ID);
        for (ClanMemberData data : members) {
            ClanMember member = new ClanMember(data);
            if (clans.containsKey(member.clanId)) {
                clans.get(member.clanId).members.add(member);
            } else {
                removes.add(data);
            }
        }
        GameRepository.getInstance().clanMemberData.deleteAll(removes);
        for (Clan clan : clans.values()) {
            clan.sort();
        }
        isRunning = true;
        new Thread(this).start();
    }

    public Clan findClanById(int clanId) {
        lock.readLock().lock();
        try {
            return clans.get(clanId);
        } finally {
            lock.readLock().unlock();
        }
    }

    public Clan findClanByName(String name) {
        lock.readLock().lock();
        try {
            return clans.values().stream().filter(clan -> clan.name.equals(name)).findFirst().orElse(null);
        } finally {
            lock.readLock().unlock();
        }
    }

    public void saveData() {
        lock.readLock().lock();
        try {
            for (Clan clan : clans.values()) {
                clan.saveData();
            }
        } finally {
            lock.readLock().unlock();
        }
    }

    public void addClan(Clan clan) {
        lock.writeLock().lock();
        try {
            clans.put(clan.id, clan);
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public void run() {
        while (isRunning) {
            long delay = 1000;
            try {
                long l1 = System.currentTimeMillis();
                update(l1);
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

    public void update(long now) {
        lock.readLock().lock();
        try {
            for (Clan clan : clans.values()) {
                clan.update(now);
            }
        } finally {
            lock.readLock().unlock();
        }
    }

    public void close() {
        isRunning = false;
    }
}
