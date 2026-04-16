package com.beemobi.rongthanonline.model.waypoint;

import com.beemobi.rongthanonline.data.WayPointData;

public class WayPoint {
    public int id;
    public int mapId;
    public int x;
    public int y;
    public WayPointType type;
    public int goMap;
    public int goX;
    public int goY;

    public WayPoint(WayPointData data) {
        id = data.id;
        mapId = data.mapId;
        x = data.x;
        y = data.y;
        type = data.type;
        goMap = data.goMap;
        goX = data.goX;
        goY = data.goY;
    }
}
