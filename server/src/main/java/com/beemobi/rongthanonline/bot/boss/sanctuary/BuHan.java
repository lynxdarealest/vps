package com.beemobi.rongthanonline.bot.boss.sanctuary;

import com.beemobi.rongthanonline.entity.Entity;
import com.beemobi.rongthanonline.entity.player.Player;
import com.beemobi.rongthanonline.map.Map;
import com.beemobi.rongthanonline.map.MapManager;
import com.beemobi.rongthanonline.map.MapName;
import com.beemobi.rongthanonline.map.Zone;
import com.beemobi.rongthanonline.util.Utils;

import java.util.List;

public class BuHan extends Mabu {
    public BuBelly buBelly;

    public BuHan() {
        super(Mabu.BU.length - 2);
        buBelly = new BuBelly(this);
        Map map = new Map(MapManager.getInstance().mapTemplates.get(MapName.BUNG_BU));
        map.zones.get(0).enter(buBelly);
    }

    @Override
    public long formatDamageInjure(Entity attacker, long damage, boolean isCritical) {
        if (buBelly != null) {
            return 1;
        }
        return super.formatDamageInjure(attacker, damage, isCritical);
    }

    @Override
    public void updateEveryTenSeconds(long now) {
        super.updateEveryTenSeconds(now);
        try {
            if (buBelly != null) {
                Zone zone = buBelly.zone;
                if (zone != null) {
                    List<Player> playerList = this.zone.getPlayers(Zone.TYPE_PLAYER);
                    if (!playerList.isEmpty()) {
                        zone.enter(playerList.get(Utils.nextInt(playerList.size())));
                    }
                }
            }
        } catch (Exception ignored) {
        }
    }
}
