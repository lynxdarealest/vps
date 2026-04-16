package com.beemobi.rongthanonline.map.expansion.dark;

import com.beemobi.rongthanonline.entity.player.Player;
import com.beemobi.rongthanonline.map.Zone;
import com.beemobi.rongthanonline.map.Map;
import com.beemobi.rongthanonline.model.MessageTime;

public class ZoneDarkVillage extends Zone {
    public DarkVillage village;

    public ZoneDarkVillage(Map map, DarkVillage village) {
        super(map);
        this.village = village;
    }

    @Override
    public void enter(Player player) {
        super.enter(player);
        if (player.isPlayer()) {
            player.addMessageTime(MessageTime.DARK_VILLAGE, village.getCountDown());
        }
    }
}
