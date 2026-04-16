package com.beemobi.rongthanonline.top.player.disciple;

import com.beemobi.rongthanonline.bot.disciple.Disciple;
import com.beemobi.rongthanonline.entity.player.Player;
import com.beemobi.rongthanonline.repository.GameRepository;
import com.beemobi.rongthanonline.server.Server;
import com.beemobi.rongthanonline.top.Top;
import com.beemobi.rongthanonline.top.TopInfo;
import com.beemobi.rongthanonline.top.TopType;
import com.beemobi.rongthanonline.top.player.TopPlayer;
import com.beemobi.rongthanonline.top.player.power.TopPowerInfo;
import com.beemobi.rongthanonline.util.Utils;
import org.apache.log4j.Logger;

import java.util.List;

public class TopDisciple extends TopPlayer {
    private static final Logger logger = Logger.getLogger(TopDisciple.class);

    public TopDisciple() {
        super(TopType.POWER_DISCIPLE, "Đệ tử");
        limit = 50;
    }

    @Override
    public void init() {
        List<Object[]> objects = GameRepository.getInstance().discipleData.findTopPower(Server.ID, limit);
        for (Object[] object : objects) {
            TopDiscipleInfo top = new TopDiscipleInfo(null);
            top.id = (int) object[0];
            top.name = String.format("%s (%s)", object[1].toString(), object[5].toString());
            top.gender = (int) object[2];
            top.score = Long.parseLong(object[3].toString());
            top.updateTime = Long.parseLong(object[6].toString());
            top.info = String.format("Lv%s: %s", object[4].toString(), Utils.formatNumber(top.score));
            elements.add(top);
        }
    }

    @Override
    public void setObject(Object object) {
        lock.writeLock().lock();
        try {
            Disciple disciple = ((Player) object).disciple;
            if (disciple == null) {
                return;
            }
            TopInfo info = elements.stream().filter(i -> i.id == disciple.id).findFirst().orElse(null);
            if (info != null) {
                info.setObject(disciple);
                return;
            }
            elements.add(new TopDiscipleInfo(disciple));
        } finally {
            lock.writeLock().unlock();
        }
    }
}
