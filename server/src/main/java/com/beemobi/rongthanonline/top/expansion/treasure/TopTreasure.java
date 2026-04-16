package com.beemobi.rongthanonline.top.expansion.treasure;

import com.beemobi.rongthanonline.entity.player.Player;
import com.beemobi.rongthanonline.map.expansion.treasure.Pirate;
import com.beemobi.rongthanonline.top.Top;
import com.beemobi.rongthanonline.top.TopInfo;
import com.beemobi.rongthanonline.top.TopType;
import com.beemobi.rongthanonline.top.player.power.TopPowerInfo;
import org.apache.log4j.Logger;

public class TopTreasure extends Top {
    private static final Logger logger = Logger.getLogger(TopTreasure.class);

    public TopTreasure() {
        super("Động kho báu", TopType.TOP_TREASURE);
        limit = 10;
    }

    @Override
    public void init() {

    }

    @Override
    public void setObject(Object object) {
        lock.writeLock().lock();
        try {
            Pirate pirate = (Pirate) object;
            TopInfo info = elements.stream().filter(i -> i.id == pirate.playerId).findFirst().orElse(null);
            if (info != null) {
                info.setObject(pirate);
                return;
            }
            elements.add(new TopTreasureInfo(pirate));
        } finally {
            lock.writeLock().unlock();
        }
        /*Pirate pirate = (Pirate) object;
        TopInfo top = findTopInfoByPlayerId(pirate.playerId);
        long now = System.currentTimeMillis();
        if (top != null) {
            top.score = pirate.total;
            top.updateTime = now;
            top.info = String.format("Thành tích: %d điểm", top.score);
            return;
        }
        lock.writeLock().lock();
        try {
            top = new TopInfo();
            top.id = pirate.playerId;
            top.name = String.format("%s (%s)", pirate.name, pirate.flag == Treasure.FLAGS[0] ? "đỏ" : "xanh");
            top.gender = pirate.gender;
            top.score = pirate.total;
            top.updateTime = now;
            top.info = String.format("Thành tích: %d điểm", top.score);
            elements.add(top);
        } finally {
            lock.writeLock().unlock();
        }*/
    }

    @Override
    public void clearObject(Object object) {

    }
}
