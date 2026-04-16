package com.beemobi.rongthanonline.top.clan;

import com.beemobi.rongthanonline.clan.Clan;
import com.beemobi.rongthanonline.clan.ClanManager;
import com.beemobi.rongthanonline.clan.ClanMember;
import com.beemobi.rongthanonline.entity.player.Player;
import com.beemobi.rongthanonline.repository.GameRepository;
import com.beemobi.rongthanonline.server.Server;
import com.beemobi.rongthanonline.top.Top;
import com.beemobi.rongthanonline.top.TopInfo;
import com.beemobi.rongthanonline.top.TopType;
import com.beemobi.rongthanonline.top.player.power.TopPowerInfo;
import org.apache.log4j.Logger;

import java.sql.Timestamp;
import java.util.List;

public class TopClan extends Top {
    private static final Logger logger = Logger.getLogger(TopClan.class);

    public TopClan() {
        super("Bang hội", TopType.TOP_CLAN);
        limit = 20;
    }

    @Override
    public void init() {
        List<Clan> clans = ClanManager.getInstance().clans.values().stream().toList();
        for (Clan clan : clans) {
            elements.add(new TopClanInfo(clan));
        }
    }

    @Override
    public void setObject(Object object) {
        lock.writeLock().lock();
        try {
            Clan clan = (Clan) object;
            TopInfo info = elements.stream().filter(i -> i.id == clan.id).findFirst().orElse(null);
            if (info != null) {
                info.setObject(clan);
                return;
            }
            elements.add(new TopClanInfo(clan));
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public void clearObject(Object object) {

    }
}
