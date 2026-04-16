package com.beemobi.rongthanonline.bot.boss.sanctuary;

import com.beemobi.rongthanonline.entity.Entity;
import com.beemobi.rongthanonline.entity.player.Player;
import com.beemobi.rongthanonline.map.MapName;
import com.beemobi.rongthanonline.map.Zone;
import com.beemobi.rongthanonline.util.Utils;

import java.util.List;

public class BuBelly extends Mabu {

    public BuHan buHan;

    public BuBelly(BuHan buHan) {
        super(2);
        this.buHan = buHan;
    }

    @Override
    public void startDie(Entity killer) {
        Zone zone = this.zone;
        super.startDie(killer);
        Utils.setTimeout(() -> {
            try {
                List<Player> playerList = zone.getPlayers(Zone.TYPE_PLAYER);
                if (!playerList.isEmpty()) {
                    Zone z = buHan.zone;
                    if (z != null && z.isRunning) {
                        for (Player player : playerList) {
                            try {
                                z.enter(player);
                            } catch (Exception ignored) {
                            }
                        }
                    } else {
                        for (Player player : playerList) {
                            try {
                                player.teleport(MapName.DAO_KAME, false);
                                player.addInfo(Player.INFO_YELLOW, "Trận chiến đã kết thúc");
                            } catch (Exception ignored) {
                            }
                        }
                    }
                }
            } catch (Exception ignored) {
            }
            buHan.buBelly = null;
            zone.close();
        }, waitingTimeToLeave + 2000);
    }
}
