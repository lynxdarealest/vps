package com.beemobi.rongthanonline.map.expansion.treasure;

import com.beemobi.rongthanonline.entity.player.Player;
import com.beemobi.rongthanonline.map.Zone;
import com.beemobi.rongthanonline.map.Map;
import com.beemobi.rongthanonline.map.MapName;
import com.beemobi.rongthanonline.map.expansion.ExpansionState;
import com.beemobi.rongthanonline.map.expansion.survival.Survival;
import com.beemobi.rongthanonline.model.MessageTime;

import java.util.List;

public class ZoneTreasure extends Zone {
    public Treasure treasure;

    public ZoneTreasure(Map map, Treasure treasure) {
        super(map);
        this.treasure = treasure;
    }

    @Override
    public void close() {
        List<Player> playerList = getPlayers(Zone.TYPE_PLAYER);
        for (Player player : playerList) {
            try {
                player.teleport(MapName.NUI_PAOZU, false);
                player.addInfo(Player.INFO_YELLOW, "Trận chiến đã kết thúc");
                player.removeMessageTime(MessageTime.FLAG_TREASURE);
            } catch (Exception ignored) {
            }
        }
        super.close();
    }

    @Override
    public void enter(Player player) {
        super.enter(player);
        if (player.isPlayer()) {
            if (treasure.state == ExpansionState.WAIT_REG) {
                long now = System.currentTimeMillis();
                long time = Survival.MINUTE_WAIT_REG * 60000L + treasure.startTime - now;
                if (time > 0) {
                    player.addMessageTime(MessageTime.TREASURE_TIME_WAIT, time);
                }
            }
        }
    }
}
