package com.beemobi.rongthanonline.map.expansion.congress;

import com.beemobi.rongthanonline.entity.player.Player;
import com.beemobi.rongthanonline.map.Map;
import com.beemobi.rongthanonline.map.MapManager;
import com.beemobi.rongthanonline.map.MapName;
import com.beemobi.rongthanonline.map.expansion.Expansion;
import com.beemobi.rongthanonline.util.Utils;

import java.util.ArrayList;
import java.util.HashMap;

public class MartialCongress extends Expansion {

    public static int[] MAPS = {MapName.DAU_TRUONG};
    public static final int MAX_LEVEL = 15;

    public HashMap<Integer, Integer> levels = new HashMap<>();
    public HashMap<Integer, Integer> counts = new HashMap<>();
    public ArrayList<Integer> winners = new ArrayList<>();
    public ArrayList<Integer> rewards = new ArrayList<>();

    public MartialCongress() {
        super(-1);
        for (int mapId : MAPS) {
            Map map = new Map(MapManager.getInstance().mapTemplates.get(mapId), this);
            for (int i = 0; i < 10; i++) {
                map.zones.add(new ZoneMartialCongress(map, this));
            }
            maps.add(map);
        }
    }

    @Override
    public void close() {

    }

    @Override
    public void update() {

    }

    public void enter(Player player) {
        player.x = ZoneMartialCongress.POSITION_REFEREE[0][0] - Utils.nextInt(100, 200);
        player.y = ZoneMartialCongress.POSITION_REFEREE[0][1];
        player.joinMap(maps.get(0), -1);
    }

    public void addWinner(Player player) {
        lock.lock();
        try {
            winners.add(player.id);
        } finally {
            lock.unlock();
        }
    }

    public boolean isWinner(int playerID) {
        lock.lock();
        try {
            return winners.contains(playerID);
        } finally {
            lock.unlock();
        }
    }

    public void addReward(Player player) {
        lock.lock();
        try {
            rewards.add(player.id);
        } finally {
            lock.unlock();
        }
    }

    public boolean isReward(int playerID) {
        lock.lock();
        try {
            return rewards.contains(playerID);
        } finally {
            lock.unlock();
        }
    }

    public int getLevel(Player player) {
        lock.lock();
        try {
            return levels.getOrDefault(player.id, 0);
        } finally {
            lock.unlock();
        }
    }

    public void setLevel(Player player, int level) {
        lock.lock();
        try {
            levels.put(player.id, level);
        } finally {
            lock.unlock();
        }
    }

    public int getCount(Player player) {
        lock.lock();
        try {
            return counts.getOrDefault(player.id, 0);
        } finally {
            lock.unlock();
        }
    }

    public void setCount(Player player, int count) {
        lock.lock();
        try {
            counts.put(player.id, count);
        } finally {
            lock.unlock();
        }
    }

    public void reset() {
        lock.lock();
        try {
            winners.clear();
            levels.clear();
            rewards.clear();
            counts.clear();
        } finally {
            lock.unlock();
        }
    }

}
