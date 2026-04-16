package com.beemobi.rongthanonline.map;

import com.beemobi.rongthanonline.data.*;
import com.beemobi.rongthanonline.entity.monster.Monster;
import com.beemobi.rongthanonline.entity.monster.MonsterManager;
import com.beemobi.rongthanonline.map.expansion.barrack.Barrack;
import com.beemobi.rongthanonline.map.expansion.city.ForgottenCity;
import com.beemobi.rongthanonline.map.expansion.congress.MartialCongress;
import com.beemobi.rongthanonline.map.expansion.dark.DarkVillage;
import com.beemobi.rongthanonline.map.expansion.festival.MartialArtsFestival;
import com.beemobi.rongthanonline.map.expansion.flagwar.FlagWar;
import com.beemobi.rongthanonline.map.expansion.island.Island;
import com.beemobi.rongthanonline.map.expansion.nrnm.DragonBallNamek;
import com.beemobi.rongthanonline.map.expansion.tournament.Tournament;
import com.beemobi.rongthanonline.map.expansion.sanctuary.Sanctuary;
import com.beemobi.rongthanonline.map.expansion.spaceship.Spaceship;
import com.beemobi.rongthanonline.map.expansion.survival.Survival;
import com.beemobi.rongthanonline.map.expansion.treasure.Treasure;
import com.beemobi.rongthanonline.npc.Npc;
import com.beemobi.rongthanonline.npc.NpcManager;
import com.beemobi.rongthanonline.model.waypoint.WayPoint;
import com.beemobi.rongthanonline.repository.GameRepository;
import com.beemobi.rongthanonline.server.Server;
import com.beemobi.rongthanonline.util.Utils;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MapManager {
    private static final Logger logger = Logger.getLogger(MapManager.class);
    private static MapManager instance;
    public HashMap<Integer, Map> maps;
    public HashMap<Integer, MapTemplate> mapTemplates;
    public HashMap<Integer, ArrayList<Npc>> map_npc;
    public HashMap<Integer, ArrayList<Monster>> map_monster;
    public HashMap<Integer, ArrayList<WayPoint>> map_waypoint;
    public ArrayList<Map> mapSpaceships;
    public HashMap<Integer, Barrack> barracks;
    public MartialArtsFestival martialArtsFestival;
    public DragonBallNamek dragonBallNamek;
    public FlagWar flagWar;
    public ForgottenCity forgottenCity;
    public DarkVillage village;
    public Treasure treasure;
    public Survival survival;
    public Island island;
    public MartialCongress martialCongress;
    public Tournament tournament;
    public Sanctuary sanctuary;
    public Spaceship spaceship;
    public List<ArenaCustom> arenaCustoms;

    public MapManager() {
        barracks = new HashMap<>();
        arenaCustoms = new ArrayList<>();
    }

    public static MapManager getInstance() {
        if (instance == null) {
            instance = new MapManager();
        }
        return instance;
    }

    public boolean isMapBarrack(int id) {
        return id >= 28 && id <= 42;
    }

    public boolean isMapManor(int id) {
        return id >= 73 && id <= 84;
    }

    public boolean isMapIsland(int id) {
        return id >= 94 && id <= 105;
    }

    public void init() {
        initMapMob();
        initMapNpc();
        initMapWaypoint();
        initMapTemplate();
        maps = new HashMap<>();
        for (MapTemplate template : mapTemplates.values()) {
            if (template.type == MapType.ONLINE) {
                maps.put(template.id, new Map(template));
            }
        }
        initSpaceship();
    }

    public boolean isContainPlayerInBarrack(int playerId) {
        for (Barrack barrack : barracks.values()) {
            if (barrack.players.contains(playerId)) {
                return true;
            }
        }
        return false;
    }

    public void openExpansion() {
        openMartialArtsFestival();
        island = new Island();
        martialCongress = new MartialCongress();
        tournament = new Tournament();
        long day = 86400;
        if (Server.getInstance().isInterServer()) {
            Utils.setScheduled(() -> {
                try {
                    if (survival != null) {
                        survival.close();
                    }
                    survival = new Survival("Máy chủ", 20, -1);
                } catch (Exception ignored) {
                }
            }, day, 19, 0);
            Utils.setScheduled(() -> {
                try {
                    if (treasure != null) {
                        treasure.close();
                    }
                    treasure = new Treasure();
                } catch (Exception ignored) {
                }
            }, day, 22, 30);
            return;
        }
        Utils.setScheduled(() -> {
            try {
                if (flagWar != null) {
                    flagWar.close();
                }
                flagWar = new FlagWar();
            } catch (Exception ignored) {
            }
        }, day, 20, 30);
        Utils.setScheduled(() -> {
            try {
                if (spaceship != null) {
                    spaceship.close();
                }
                spaceship = new Spaceship();
            } catch (Exception ignored) {
            }
        }, day, 12, 30);
        Utils.setScheduled(() -> {
            try {
                if (sanctuary != null) {
                    sanctuary.close();
                }
                sanctuary = new Sanctuary();
            } catch (Exception ignored) {
            }
        }, day, 14, 0);
        Utils.setScheduled(() -> {
            try {
                if (dragonBallNamek != null) {
                    dragonBallNamek.close();
                }
                dragonBallNamek = new DragonBallNamek();
            } catch (Exception ignored) {
            }
        }, day, 21, 0);
        Utils.setScheduled(() -> {
            try {
                if (forgottenCity != null) {
                    forgottenCity.close();
                }
                forgottenCity = new ForgottenCity();
            } catch (Exception ignored) {
            }
        }, day, 22, 0);
        Utils.setScheduled(() -> {
            try {
                if (survival != null) {
                    survival.close();
                }
                survival = new Survival("Máy chủ", 20, -1);
            } catch (Exception ignored) {
            }
        }, day, 20, 0);
        Utils.setScheduled(() -> {
            try {
                if (survival != null) {
                    survival.close();
                }
                survival = new Survival("Máy chủ", 12, -1);
            } catch (Exception ignored) {
            }
        }, day, 12, 0);
        Utils.setScheduled(() -> {
            try {
                if (treasure != null) {
                    treasure.close();
                }
                treasure = new Treasure();
            } catch (Exception ignored) {
            }
        }, day, 21, 30);
        Utils.setScheduled(() -> {
            try {
                if (village != null) {
                    village.close();
                }
                village = new DarkVillage();
            } catch (Exception ignored) {
            }
        }, day, 23, 0);
        Utils.setScheduled(() -> {
            try {
                if (village != null) {
                    village.close();
                }
                village = new DarkVillage();
            } catch (Exception ignored) {
            }
        }, day, 13, 0);
        Utils.setScheduled(() -> {
            try {
                if (martialCongress != null) {
                    martialCongress.reset();
                }
            } catch (Exception ignored) {
            }
        }, day, 0, 0);
    }

    public void openMartialArtsFestival() {
        openMartialArtsFestival("Vệ binh", 10, 19, 1000L, 5, 0);
        openMartialArtsFestival("Vệ binh", 10, 19, 1000L, 15, 0);
        openMartialArtsFestival("Chiến binh", 20, 29, 10000L, 7, 0);
        openMartialArtsFestival("Chiến binh", 20, 29, 10000L, 17, 0);
        openMartialArtsFestival("Siêu chiến binh", 30, 39, 100000L, 9, 0);
        openMartialArtsFestival("Siêu chiến binh", 30, 39, 100000L, 19, 0);
        openMartialArtsFestival("Mở rộng", 10, 100, 10000L, 11, 0);
        openMartialArtsFestival("Mở rộng", 10, 100, 10000L, 13, 0);
        openMartialArtsFestival("Mở rộng", 10, 100, 10000L, 21, 0);
        openMartialArtsFestival("Mở rộng", 10, 100, 10000L, 23, 0);
    }

    public void openMartialArtsFestival(String name, int minLevel, int maxLevel, long frees, int hour, int minute) {
        Utils.setScheduled(() -> {
            try {
                if (martialArtsFestival != null) {
                    martialArtsFestival.close();
                }
                martialArtsFestival = new MartialArtsFestival(name, minLevel, maxLevel, hour, frees, -1);
            } catch (Exception ignored) {
            }
        }, 86400, hour, minute);
    }

    public void close() {
        if (martialArtsFestival != null) {
            martialArtsFestival.close();
        }
        if (survival != null) {
            survival.close();
        }
        if (forgottenCity != null) {
            forgottenCity.close();
        }
        if (dragonBallNamek != null) {
            dragonBallNamek.close();
        }
        for (Map map : maps.values()) {
            try {
                map.close();
            } catch (Exception e) {
                logger.error("close", e);
            }
        }
    }

    public void initSpaceship() {
        mapSpaceships = new ArrayList<>();
        for (Map map : maps.values()) {
            if (map.template.id == 0 || map.template.id == 20 || map.template.id == 17
                    || map.template.id == 19 || map.template.id == 12 || map.template.id == 49) {
                mapSpaceships.add(map);
            }
        }
    }

    public void initMapTemplate() {
        mapTemplates = new HashMap<>();
        List<MapTemplateData> mapTemplateDataList = GameRepository.getInstance().mapTemplateData.findAll();
        for (MapTemplateData data : mapTemplateDataList) {
            MapTemplate template = new MapTemplate(data);
            template.npcs = map_npc.getOrDefault(template.id, new ArrayList<>());
            template.wayPoints = map_waypoint.getOrDefault(template.id, new ArrayList<>());
            template.monsters = map_monster.getOrDefault(template.id, new ArrayList<>());
            mapTemplates.put(template.id, template);
        }
    }

    public void initMapWaypoint() {
        map_waypoint = new HashMap<>();
        List<WayPointData> wayPointDataList = GameRepository.getInstance().wayPointData.findAll();
        for (WayPointData data : wayPointDataList) {
            int mapId = data.mapId;
            if (!map_waypoint.containsKey(mapId)) {
                map_waypoint.put(mapId, new ArrayList<>());
            }
            map_waypoint.get(mapId).add(new WayPoint(data));
        }
    }

    public void initMapMob() {
        map_monster = new HashMap<>();
        List<MapMonsterData> mapMonsterDataList = GameRepository.getInstance().mapMonsterData.findAll();
        for (MapMonsterData data : mapMonsterDataList) {
            Monster monster = new Monster();
            monster.template = MonsterManager.getInstance().monsterTemplates.get(data.monsterId);
            monster.x = data.x;
            monster.y = data.y;
            monster.hp = monster.maxHp = monster.baseHp = monster.template.hp;
            int mapId = data.mapId;
            if (!map_monster.containsKey(mapId)) {
                map_monster.put(mapId, new ArrayList<>());
            }
            map_monster.get(mapId).add(monster);
        }
    }

    public void initMapNpc() {
        map_npc = new HashMap<>();
        List<MapNpcData> mapNpcDataList = GameRepository.getInstance().mapNpcData.findAll();
        for (MapNpcData data : mapNpcDataList) {
            Npc npc = new Npc();
            npc.template = NpcManager.getInstance().npcTemplates.get(data.npcId);
            npc.x = data.x;
            npc.y = data.y;
            int mapId = data.mapId;
            if (!map_npc.containsKey(mapId)) {
                map_npc.put(mapId, new ArrayList<>());
            }
            map_npc.get(mapId).add(npc);
        }
    }

}
