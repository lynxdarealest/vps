package com.beemobi.rongthanonline.top.player.pro;

import com.beemobi.rongthanonline.entity.player.Player;
import com.beemobi.rongthanonline.repository.GameRepository;
import com.beemobi.rongthanonline.server.Server;
import com.beemobi.rongthanonline.top.TopInfo;
import com.beemobi.rongthanonline.top.TopType;
import com.beemobi.rongthanonline.top.player.TopPlayer;
import com.beemobi.rongthanonline.util.Utils;
import org.apache.log4j.Logger;

import java.util.List;

public class TopPro extends TopPlayer {
    private static final Logger logger = Logger.getLogger(TopPro.class);

    public TopPro() {
        super(TopType.TOP_PRO, "Sức chiến đấu");
        limit = 50;
    }

    @Override
    public void init() {
        List<Object[]> objects = GameRepository.getInstance().playerData.findTopPro(Server.ID, limit);
        for (Object[] object : objects) {
            TopProInfo top = new TopProInfo(null);
            top.id = (int) object[0];
            top.name = (String) object[1];
            top.gender = (int) object[2];
            top.score = Long.parseLong(object[3].toString());
            top.info = String.format("SCĐ: %s", Utils.formatNumber(top.score));
            elements.add(top);
        }
    }

    @Override
    public void setObject(Object object) {
        lock.writeLock().lock();
        try {
            Player player = (Player) object;
            TopInfo info = elements.stream().filter(i -> i.id == player.id).findFirst().orElse(null);
            if (info != null) {
                info.setObject(player);
                return;
            }
            elements.add(new TopProInfo(player));
        } finally {
            lock.writeLock().unlock();
        }
    }
}
