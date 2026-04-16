package com.beemobi.rongthanonline.map.expansion.sanctuary;

import com.beemobi.rongthanonline.bot.boss.sanctuary.Mabu;
import com.beemobi.rongthanonline.entity.player.Player;
import com.beemobi.rongthanonline.map.Map;
import com.beemobi.rongthanonline.map.MapName;
import com.beemobi.rongthanonline.map.Zone;
import com.beemobi.rongthanonline.model.MessageTime;

import java.util.List;

public class ZoneSanctuary extends Zone {
    public Sanctuary sanctuary;

    public ZoneSanctuary(Map map, Sanctuary sanctuary) {
        super(map);
        this.sanctuary = sanctuary;
        enter(new Mabu(0));
    }

    @Override
    public void enter(Player player) {
        super.enter(player);
        if (player.isPlayer()) {
            player.addMessageTime(MessageTime.SANCTUARY, sanctuary.getCountDown());
        }
    }

    @Override
    public void close() {
        List<Player> playerList = getPlayers(Zone.TYPE_PLAYER);
        for (Player player : playerList) {
            try {
                player.teleport(MapName.DAO_KAME, false);
                player.addInfo(Player.INFO_YELLOW, "Trận chiến đã kết thúc");
            } catch (Exception ignored) {
            }
        }
        super.close();
    }
}
