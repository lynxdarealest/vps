package com.beemobi.rongthanonline.map;

import com.beemobi.rongthanonline.map.expansion.Expansion;
import com.beemobi.rongthanonline.map.expansion.barrack.Barrack;
import com.beemobi.rongthanonline.map.expansion.city.ForgottenCity;
import com.beemobi.rongthanonline.map.expansion.festival.Arena;
import com.beemobi.rongthanonline.map.expansion.flagwar.FlagWar;
import com.beemobi.rongthanonline.map.expansion.island.Island;
import com.beemobi.rongthanonline.map.expansion.manor.Manor;
import com.beemobi.rongthanonline.map.expansion.nrnm.DragonBallNamek;
import com.beemobi.rongthanonline.map.expansion.spaceship.Spaceship;
import com.beemobi.rongthanonline.map.expansion.survival.ZoneSurvival;
import com.beemobi.rongthanonline.map.expansion.survival.Survival;
import com.beemobi.rongthanonline.map.expansion.treasure.Treasure;
import com.beemobi.rongthanonline.model.waypoint.WayPoint;
import com.beemobi.rongthanonline.model.waypoint.WayPointType;
import com.beemobi.rongthanonline.util.Utils;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import static com.beemobi.rongthanonline.map.MapPlanet.NAMEK;

public class Map {
    public static final int MAP_SIZE = 72;
    private static final Logger logger = Logger.getLogger(Map.class);
    public MapTemplate template;
    public ArrayList<Zone> zones;
    public Expansion expansion;
    public ReadWriteLock lock = new ReentrantReadWriteLock();
    public int areaAutoIncrease;
    public boolean isFinish;

    public Map(MapTemplate template) {
        this.template = template;
        zones = new ArrayList<>();
        for (int i = 0; i < template.minZone; i++) {
            if (template.id == MapName.NHA_GO_HAN) {
                zones.add(new ZoneSingle(this));
            } else if (template.id == MapName.DAI_HOI_VO_THUAT) {
                zones.add(new Arena(this));
            } else {
                zones.add(new Zone(this));
            }
        }
    }

    public Map(MapTemplate template, Expansion expansion) {
        this.template = template;
        this.zones = new ArrayList<>();
        this.expansion = expansion;
    }

    public Zone findOrRandomZone(int id) {
        lock.readLock().lock();
        try {
            if (id == -1) {
                for (Zone zone : zones) {
                    if (zone.getPlayers(Zone.TYPE_PLAYER).size() < 8) {
                        return zone;
                    }
                }
                return zones.get(Utils.nextInt(zones.size()));
            }
            if (id == -2) {
                return zones.get(1 + Utils.nextInt(zones.size() - 1));
            }
            for (Zone zone : zones) {
                if (zone.id == id) {
                    return zone;
                }
            }
            return null;
        } finally {
            lock.readLock().unlock();
        }
    }

    public Zone findAreaInSurvival() {
        lock.readLock().lock();
        try {
            List<Zone> zoneList = new ArrayList<>();
            for (Zone zone : zones) {
                if (!((ZoneSurvival) zone).isRedZone) {
                    zoneList.add(zone);
                }
            }
            if (!zoneList.isEmpty()) {
                return zoneList.get(Utils.nextInt(zoneList.size()));
            }
            return zones.get(Utils.nextInt(zones.size()));
        } finally {
            lock.readLock().unlock();
        }
    }

    public WayPoint findWayPoint(int x, int y) {
        for (WayPoint wayPoint : template.wayPoints) {
            if (wayPoint.type == WayPointType.DAU_MAP && x >= wayPoint.x && x <= wayPoint.x + 50 && y >= wayPoint.y - 200 && y <= wayPoint.y) {
                return wayPoint;
            } else if (wayPoint.type == WayPointType.CUOI_MAP && x >= wayPoint.x - 50 && x <= wayPoint.x && y >= wayPoint.y - 200 && y <= wayPoint.y) {
                return wayPoint;
            } else if (wayPoint.type == WayPointType.GIUA_MAP && x >= wayPoint.x - 200 && x <= wayPoint.x + 200 && y >= wayPoint.y - 200 && y <= wayPoint.y) {
                return wayPoint;
            }
        }
        return null;
    }

    public boolean isWall(int px, int py) {
        int[] index = new int[]{px / MAP_SIZE, py / MAP_SIZE};
        if (index[0] < 0 || index[0] >= template.column || index[1] < 0 || index[1] >= template.row) {
            return true;
        }
        return template.datas[index[1]][index[0]] != 0;
    }

    public int getYSd(int xSd) {
        int num = 50;
        int i = 0;
        while (i < 150) {
            i++;
            num += 24;
            if (isWall(xSd, num)) {
                num -= num % 24;
                break;
            }
        }
        return num;
    }

    public void close() {
        lock.readLock().lock();
        try {
            for (Zone zone : zones) {
                zone.close();
            }
        } finally {
            lock.readLock().unlock();
        }
    }

    public boolean isBarrack() {
        return expansion instanceof Barrack;
    }

    public boolean isDragonBallNamek() {
        return expansion instanceof DragonBallNamek;
    }

    public boolean isFlagWar() {
        return expansion instanceof FlagWar;
    }

    public boolean isForgottenCity() {
        return expansion instanceof ForgottenCity;
    }

    public boolean isTreasure() {
        return expansion instanceof Treasure;
    }

    public boolean isIsland() {
        return expansion instanceof Island;
    }

    public boolean isSpaceship() {
        return expansion instanceof Spaceship;
    }

    public boolean isSurvival() {
        return expansion instanceof Survival;
    }

    public boolean isManor() {
        return expansion instanceof Manor;
    }

    public boolean isYardrat() {
        return template.planet == MapPlanet.YARDRAT;
    }

    public boolean isBill() {
        return template.planet == MapPlanet.BILL;
    }

    public boolean isCantChangeArea() {
        return template.id == MapName.LANG_CO_GIRA || template.id == MapName.BUNG_BU
                || template.id == MapName.THANH_DIA;
    }

    public Barrack getBarrack() {
        if (!isBarrack()) {
            return null;
        }
        return (Barrack) expansion;
    }

    public Treasure getTreasure() {
        if (!isTreasure()) {
            return null;
        }
        return (Treasure) expansion;
    }

    public Manor getManor() {
        if (!isManor()) {
            return null;
        }
        return (Manor) expansion;
    }

    public boolean isExpansion() {
        return expansion != null;
    }

    public ForgottenCity getForgottenCity() {
        if (!isForgottenCity()) {
            return null;
        }
        return (ForgottenCity) expansion;
    }

    public int getCountPlayerByLevelMark(int mark) {
        int count = 0;
        lock.readLock().lock();
        try {
            for (Zone zone : zones) {
                count += (int) zone.getPlayers(Zone.TYPE_PLAYER).stream().filter(player -> player.level / 10 == mark).count();
            }
        } finally {
            lock.readLock().unlock();
        }
        return count;
    }

    public String getPlanetName() {
        switch (template.planet) {
            case EARTH:
                return "Trái đất";
            case NAMEK:
                return "Namek";
            case COLD:
                return "Băng";
            case FIRE:
                return "Lửa";
            case YARDRAT:
                return "Yardrat";
            case SURVIVAL:
                return "Sinh tồn";
            case BILL:
                return "Bill";
            default:
                return "";
        }
    }
}
