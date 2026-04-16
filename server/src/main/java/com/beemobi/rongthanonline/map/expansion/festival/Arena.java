package com.beemobi.rongthanonline.map.expansion.festival;

import com.beemobi.rongthanonline.map.Zone;
import com.beemobi.rongthanonline.map.Map;

public class Arena extends Zone {
    private MartialArtsFestival martialArtsFestival;
    public Flight flight;

    public Arena(Map map) {
        super(map);
    }

    @Override
    public void update() {
        super.update();
        if (flight != null) {
            flight.update();
        }
    }
}
