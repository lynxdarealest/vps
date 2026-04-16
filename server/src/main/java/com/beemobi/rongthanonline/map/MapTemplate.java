package com.beemobi.rongthanonline.map;

import com.beemobi.rongthanonline.data.MapTemplateData;
import com.beemobi.rongthanonline.entity.monster.Monster;
import com.beemobi.rongthanonline.npc.Npc;
import com.beemobi.rongthanonline.model.waypoint.WayPoint;
import com.beemobi.rongthanonline.util.Utils;

import java.util.ArrayList;

public class MapTemplate {
    public int id;

    public ArrayList<WayPoint> wayPoints;

    public ArrayList<Npc> npcs;

    public ArrayList<Monster> monsters;

    public MapType type;

    public String name;

    public MapPlanet planet;

    public int maxZone;

    public int minZone;

    public int maxPlayer;

    public int row;

    public int column;

    public String data;

    public int[] imgsBgr;

    public int[][] colorsBgr;

    public int[][] datas;

    public int width; // column * size

    public int height;

    public int iconId;

    public MapTemplate(MapTemplateData data) {
        id = data.id;
        name = data.name;
        maxZone = data.maxZone;
        minZone = data.minZone;
        row = data.row;
        column = data.column;
        this.data = data.data;
        maxPlayer = data.maxPlayer;
        width = data.column * Map.MAP_SIZE;
        height = data.row * Map.MAP_SIZE;
        type = data.type;
        planet = data.planet;
        datas = new int[data.row][data.column];
        iconId = data.iconId;
        String value = data.data;
        for (int i = 0; i < data.row; i++) {
            for (int j = 0; j < data.column; j++) {
                datas[i][j] = Integer.parseInt(value.substring(0, 1));
                value = value.substring(1);
            }
        }
        imgsBgr = Utils.gson.fromJson(data.imagesBgr, int[].class);
        colorsBgr = Utils.gson.fromJson(data.colorsBgr, int[][].class);
    }


}
