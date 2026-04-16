package com.beemobi.rongthanonline.top;

import com.beemobi.rongthanonline.entity.player.Player;
import com.beemobi.rongthanonline.entity.player.PlayerManager;
import com.beemobi.rongthanonline.network.Message;
import com.beemobi.rongthanonline.network.MessageName;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public abstract class Top {
    private static final Logger logger = Logger.getLogger(Top.class);
    public String name;
    public TopType type;
    public ArrayList<TopInfo> elements;
    public ReadWriteLock lock;
    public int limit;

    public Top(String name, TopType type) {
        this.name = name;
        this.type = type;
        elements = new ArrayList<>();
        lock = new ReentrantReadWriteLock();
        limit = 100;
    }

    public abstract void init();

    public abstract void setObject(Object object);

    public abstract void clearObject(Object object);

    public void show(Player player) {
        try {
            List<TopInfo> tops = getTops();
            if (tops == null || tops.isEmpty()) {
                player.addInfo(Player.INFO_RED, "Bảng xếp hạng trống");
                return;
            }
            int size = Math.min(limit, tops.size());
            Message msg = new Message(MessageName.TOP);
            msg.writer().writeByte(size);
            for (int i = 0; i < size; i++) {
                TopInfo top = tops.get(i);
                msg.writer().writeInt(top.getId());
                msg.writer().writeUTF(top.getName());
                msg.writer().writeByte(top.getGender());
                msg.writer().writeUTF(top.getInfo());
                //msg.writer().writeBoolean(top.isOnline());
                msg.writer().writeBoolean(PlayerManager.getInstance().isOnline(top.getId()));
            }
            player.sendMessage(msg);
            msg.cleanup();
        } catch (IOException ex) {
            logger.error("show", ex);
        }
    }

    public List<TopInfo> query() {
        return elements;
    }

    public List<TopInfo> getTops() {
        lock.readLock().lock();
        try {
            List<TopInfo> elements = query();
            elements.sort(new Comparator<TopInfo>() {
                @Override
                public int compare(TopInfo top2, TopInfo top1) {
                    long[] score = new long[]{top1.getScore(), top2.getScore()};
                    if (score[0] < score[1]) {
                        return -1;
                    } else if (score[0] > score[1]) {
                        return 1;
                    } else {
                        return Long.compare(top2.getUpdateTime(), top1.getUpdateTime());
                    }
                }
            });
            return elements;
        } finally {
            lock.readLock().unlock();
        }
    }

    public TopInfo findTopInfoByPlayerId(int playerId) {
        lock.readLock().lock();
        try {
            return elements.stream().filter(t -> t.id == playerId).findFirst().orElse(null);
        } finally {
            lock.readLock().unlock();
        }
    }
}
