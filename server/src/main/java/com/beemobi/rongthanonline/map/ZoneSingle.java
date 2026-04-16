package com.beemobi.rongthanonline.map;

import com.beemobi.rongthanonline.entity.player.Player;

public class ZoneSingle extends Zone {
    public ZoneSingle(Map map) {
        super(map);
    }

    @Override
    public void leave(Player player) {
        super.leave(player);
        if (player.isPlayer()) {
            map.close();
        }
    }
}
