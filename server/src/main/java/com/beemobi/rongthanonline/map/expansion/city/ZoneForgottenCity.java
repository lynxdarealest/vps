package com.beemobi.rongthanonline.map.expansion.city;

import com.beemobi.rongthanonline.entity.player.Player;
import com.beemobi.rongthanonline.map.Zone;
import com.beemobi.rongthanonline.map.Map;
import com.beemobi.rongthanonline.map.MapName;
import com.beemobi.rongthanonline.model.MessageTime;

import java.util.List;

public class ZoneForgottenCity extends Zone {
    public ForgottenCity forgottenCity;

    public ZoneForgottenCity(Map map, ForgottenCity forgottenCity) {
        super(map);
        this.forgottenCity = forgottenCity;
    }

    @Override
    public void enter(Player player) {
        super.enter(player);
        setRandomTypeFlag(player);
        if (player.isPlayer()) {
            player.addMessageTime(MessageTime.FORGOT_CITY, forgottenCity.getCountDown());
        }
    }

    @Override
    public void close() {
        List<Player> playerList = getPlayers(Zone.TYPE_PLAYER);
        for (Player player : playerList) {
            try {
                player.teleport(MapName.NUI_PAOZU, false);
                player.addInfo(Player.INFO_YELLOW, "Trận chiến đã kết thúc");
            } catch (Exception ignored) {
            }
        }
        super.close();
    }
}
