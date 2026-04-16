package com.beemobi.rongthanonline.map.expansion.island;

import com.beemobi.rongthanonline.map.Zone;
import com.beemobi.rongthanonline.map.Map;

public class ZoneIsland extends Zone {
    public Island island;

    public ZoneIsland(Map map, Island island) {
        super(map);
        this.island = island;
    }
}
