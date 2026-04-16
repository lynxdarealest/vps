package com.beemobi.rongthanonline.map;

import com.beemobi.rongthanonline.bot.boss.Boss;
import com.beemobi.rongthanonline.bot.boss.TeamBoss;
import com.beemobi.rongthanonline.bot.disciple.Disciple;
import com.beemobi.rongthanonline.bot.disciple.DiscipleStatus;
import com.beemobi.rongthanonline.entity.monster.Monster;
import com.beemobi.rongthanonline.entity.monster.MonsterLevelBoss;
import com.beemobi.rongthanonline.entity.monster.MonsterTypeMove;
import com.beemobi.rongthanonline.entity.monster.big.BigMonster;
import com.beemobi.rongthanonline.entity.monster.pet.Pet;
import com.beemobi.rongthanonline.item.Item;
import com.beemobi.rongthanonline.npc.Npc;
import com.beemobi.rongthanonline.entity.player.Player;
import com.beemobi.rongthanonline.item.ItemMap;
import com.beemobi.rongthanonline.service.AreaService;
import com.beemobi.rongthanonline.team.Team;
import com.beemobi.rongthanonline.util.Utils;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.stream.Collectors;

public class Zone extends Thread {
    private static final Logger logger = Logger.getLogger(Zone.class);
    public static final int TYPE_ALL = 0;
    public static final int TYPE_PLAYER = 1;
    public static final int TYPE_DISCIPLE = 2;
    public static final int TYPE_BOSS = 3;
    public static final int TYPE_ESCORT = 4;
    public static int itemMapAutoIncrease;
    public static int monsterAutoIncrease;
    public ArrayList<Player> players;
    public ArrayList<Monster> monsters;
    public ArrayList<ItemMap> itemMaps;
    public ArrayList<Npc> npcs;
    public ReadWriteLock lockPlayer;
    public ReadWriteLock lockBoss;
    public ReadWriteLock lockMonster;
    public ReadWriteLock lockItemMap;
    public ReadWriteLock lockNpc;
    public Lock lockRandomTypeFlagForPlayer;
    public boolean isRunning;
    public Map map;
    public int id;
    public AreaService service;

    public Zone(Map map) {
        id = map.areaAutoIncrease++;
        this.map = map;
        players = new ArrayList<>();
        monsters = new ArrayList<>();
        npcs = new ArrayList<>();
        itemMaps = new ArrayList<>();
        lockPlayer = new ReentrantReadWriteLock();
        lockBoss = new ReentrantReadWriteLock();
        lockMonster = new ReentrantReadWriteLock();
        lockItemMap = new ReentrantReadWriteLock();
        lockNpc = new ReentrantReadWriteLock();
        lockRandomTypeFlagForPlayer = new ReentrantLock();
        for (Monster m : map.template.monsters) {
            Monster monster = new Monster();
            monster.id = monsterAutoIncrease++;
            monster.template = m.template;
            monster.hp = m.template.hp;
            monster.maxHp = m.template.hp;
            monster.damage = m.template.damage;
            monster.baseHp = m.template.hp;
            monster.status = m.status;
            monster.levelBoss = m.levelBoss;
            monster.level = m.template.level;
            monster.x = m.x;
            monster.y = m.y;
            monster.zone = this;
            monster.isAutoRefresh = true;
            monsters.add(monster);
        }
        for (Npc n : map.template.npcs) {
            Npc npc = new Npc();
            npc.x = n.x;
            npc.y = n.y;
            npc.template = n.template;
            npc.zone = this;
            npcs.add(npc);
        }
        service = new AreaService(this);
        isRunning = true;
        start();
    }

    @Override
    public void run() {
        long delay = 100;
        while (isRunning) {
            try {
                long start = System.currentTimeMillis();
                update();
                long time = System.currentTimeMillis() - start;
                if (time > delay) {
                    continue;
                }
                Thread.sleep(delay - time);
            } catch (Exception ex) {
                logger.debug("run", ex);
            }
        }
    }

    public void update() {
        List<Player> playerList = getPlayers(Zone.TYPE_ALL);
        List<TeamBoss> teamBossList = new ArrayList<>();
        for (Player player : playerList) {
            try {
                if (player != null && player.zone == this) {
                    player.update();
                    if (player.isBoss()) {
                        Boss boss = (Boss) player;
                        if (boss.team != null && !teamBossList.contains(boss.team)) {
                            teamBossList.add(boss.team);
                        }
                    }
                }
            } catch (Exception e) {
                logger.error("update player in area", e);
            }
        }
        if (!teamBossList.isEmpty()) {
            for (TeamBoss teamBoss : teamBossList) {
                try {
                    teamBoss.update(this);
                } catch (Exception ex) {
                    logger.error("update team boss in area", ex);
                }
            }
        }
        List<Monster> monsterList = getMonsters();
        for (Monster monster : monsterList) {
            try {
                if (monster != null) {
                    monster.update();
                }
            } catch (Exception e) {
                logger.error("update monster in area", e);
            }
        }
        List<ItemMap> itemMapList = getItemMaps();
        List<ItemMap> itemMapRemoveList = new ArrayList<>();
        long now = System.currentTimeMillis();
        for (ItemMap itemMap : itemMapList) {
            if (now - itemMap.throwTime > 30000 || itemMap.isPickedUp) {
                itemMapRemoveList.add(itemMap);
            } else {
                itemMap.update();
            }
        }
        for (ItemMap itemMap : itemMapRemoveList) {
            removeItemMap(itemMap, !itemMap.isPickedUp);
        }
    }

    public void enter(Player player) {
        if (player.isDisciple() && (map.isSurvival() || map.template.id == MapName.DAI_HOI_VO_THUAT || map.template.id == MapName.DAU_TRUONG
                || map.template.id == MapName.LANH_DIA_BANG_HOI || map.isTreasure())) {
            if (player.zone != null) {
                player.zone.leave(player);
            }
            return;
        }
        try {
            if (player.zone != null) {
                player.zone.leave(player);
            }
            if (player.disciple != null) {
                Disciple disciple = player.disciple;
                if (!disciple.isDead() && disciple.status != DiscipleStatus.GO_HOME && !player.isFusion()) {
                    disciple.followMaster();
                    enter(disciple);
                }
            }
            player.zone = this;
            player.mapId = map.template.id;
            if ((player.isPlayer() || player.isDisciple()) && player.planet != map.template.planet) {
                if (player.isPlayer()) {
                    if (map.template.planet == MapPlanet.SURVIVAL) {
                        if (player.disciple != null && player.isFusion()) {
                            player.fusion(0);
                            player.typeFusion = 0;
                            player.lastTimeFusion = System.currentTimeMillis();
                        }
                        player.itemsSurvival = new Item[player.itemsBody.length];
                        player.service.setItemSurvival();
                        player.service.setPart();
                    } else if (player.planet == MapPlanet.SURVIVAL) {
                        player.service.setItemBody();
                        player.service.setPart();
                    }
                }
                player.planet = map.template.planet;
                int pointPlant = player.pointPlant;
                player.service.setInfo();
                if (player.isPlayer()) {
                    if (player.pointPlant > 0) {
                        if (map.template.planet == MapPlanet.NAMEK) {
                            player.addInfo(Player.INFO_YELLOW, "Bạn đã đến hành tinh Namek");
                        } else if (map.template.planet == MapPlanet.FIRE) {
                            player.addInfo(Player.INFO_YELLOW, "Bạn đã đến khu vực Lửa");
                        } else if (map.template.planet == MapPlanet.COLD) {
                            player.addInfo(Player.INFO_YELLOW, "Bạn đã đến khu vực Băng");
                        }
                        player.addInfo(Player.INFO_RED, String.format("Sức chiến đấu của bạn bị giảm %d%%", 15 * player.pointPlant));
                    } else if (pointPlant != player.pointPlant) {
                        player.addInfo(Player.INFO_YELLOW, "Sức chiến đấu của bạn đã trở lại bình thường");
                    }
                }
            }
            player.service.setMapInfo();
        } finally {
            addPlayer(player);
        }
        player.isHide = false;
        if (map.template.id == MapName.DAI_HOI_VO_THUAT && player.isAdminMartialArtsFestival()) {
            player.isHide = true;
        } else if (map.isSurvival() && player.isAdminSurvival()) {
            player.isHide = true;
        }
        if (!player.isHide) {
            service.addPlayer(player);
        }
        if (map.template.id == 63) {
            setRandomTypeFlag(player);
        }
        if (player.pet != null) {
            Pet pet = player.pet;
            if (!pet.isDead() && pet.stamina > 0) {
                pet.followMaster();
                enter(pet);
            }
        }
    }

    public void setRandomTypeFlag(Player player) {
        lockRandomTypeFlagForPlayer.lock();
        try {
            if (!player.isPlayer()) {
                return;
            }
            if (player.typePk != 0) {
                player.setTypePk(0);
            }
            List<Player> playerList = getPlayers(TYPE_PLAYER);
            if (player.clan != null) {
                for (Player p : playerList) {
                    if (p != player) {
                        if (player.clan == p.clan && p.typeFlag > 0) {
                            player.setTypeFlag(p.typeFlag);
                            return;
                        }
                    }
                }
            }
            List<Integer> flags = new ArrayList<>();
            for (int i = 2; i <= 8; i++) {
                flags.add(i);
            }
            for (Player p : playerList) {
                if (p != player && flags.contains(p.typeFlag)) {
                    flags.remove(Integer.valueOf(p.typeFlag));
                }
            }
            if (!flags.isEmpty()) {
                player.setTypeFlag(flags.get(Utils.nextInt(flags.size())));
                return;
            }
            player.setTypeFlag(2 + Utils.nextInt(7));
        } finally {
            lockRandomTypeFlagForPlayer.unlock();
        }
    }

    public int getTypeFlagFree(List<Player> playerList) {
        lockRandomTypeFlagForPlayer.lock();
        try {
            List<Integer> flags = new ArrayList<>();
            for (int i = 2; i <= 8; i++) {
                flags.add(i);
            }
            for (Player p : playerList) {
                if (flags.contains(p.typeFlag)) {
                    flags.remove(Integer.valueOf(p.typeFlag));
                }
            }
            if (!flags.isEmpty()) {
                return flags.get(Utils.nextInt(flags.size()));
            }
            return 2 + Utils.nextInt(7);
        } finally {
            lockRandomTypeFlagForPlayer.unlock();
        }
    }

    public void leave(Player player) {
        try {
            if (player.isPlayer()) {
                Disciple disciple = player.disciple;
                if (disciple != null && disciple.zone != null && !disciple.isDead()) {
                    disciple.zone.leave(disciple);
                }
                Pet pet = player.pet;
                if (pet != null && pet.zone != null) {
                    pet.zone.leave(pet);
                }
            }
        } finally {
            removePlayer(player);
        }
        player.clearMap();
        service.removePlayer(player);
    }

    private void addPlayer(Player player) {
        lockPlayer.writeLock().lock();
        try {
            players.add(player);
        } finally {
            lockPlayer.writeLock().unlock();
        }
    }

    private void removePlayer(Player player) {
        lockPlayer.writeLock().lock();
        try {
            players.remove(player);
        } finally {
            lockPlayer.writeLock().unlock();
        }
    }

    public Player findPlayerById(int id) {
        lockPlayer.readLock().lock();
        try {
            return players.stream().filter(p -> p.id == id).findFirst().orElse(null);
        } finally {
            lockPlayer.readLock().unlock();
        }
    }

    public Player findPlayerByName(String name) {
        lockPlayer.readLock().lock();
        try {
            return players.stream().filter(p -> p.name.equals(name)).findFirst().orElse(null);
        } finally {
            lockPlayer.readLock().unlock();
        }
    }

    public List<Player> getPlayers(int... args) {
        List<Player> playerList = new ArrayList<>();
        List<Integer> finds = Arrays.stream(args).boxed().collect(Collectors.toList());
        lockPlayer.readLock().lock();
        try {
            for (Player player : players) {
                if (player.isLogout) {
                    continue;
                }
                if (finds.contains(TYPE_ALL)
                        || (finds.contains(TYPE_PLAYER) && player.isPlayer())
                        || (finds.contains(TYPE_DISCIPLE) && player.isDisciple())
                        || (finds.contains(TYPE_BOSS) && player.isBoss())
                        || (finds.contains(TYPE_ESCORT) && player.isEscort())) {
                    playerList.add(player);
                }
            }
        } finally {
            lockPlayer.readLock().unlock();
        }
        return playerList;
    }

    public List<Team> findAllTeam() {
        List<Team> teams = new ArrayList<>();
        lockPlayer.readLock().lock();
        try {
            for (Player player : players) {
                Team team = player.getTeam();
                if (player.isPlayer() && team != null && !teams.contains(team)) {
                    teams.add(team);
                }
            }
        } finally {
            lockPlayer.readLock().unlock();
        }
        return teams;
    }

    public List<Player> findAllPlayerSameTeam(Player player) {
        List<Player> teams = new ArrayList<>();
        lockPlayer.readLock().lock();
        try {
            for (Player p : players) {
                if (p.isPlayer() && p.teamId != -1 && p.teamId == player.teamId) {
                    teams.add(p);
                }
            }
        } finally {
            lockPlayer.readLock().unlock();
        }
        return teams;
    }

    public List<Player> findAllPlayerSameClan(Player player) {
        List<Player> members = new ArrayList<>();
        lockPlayer.readLock().lock();
        try {
            for (Player p : players) {
                if (p.isPlayer() && p.clanId != -1 && p.clan == player.clan) {
                    members.add(p);
                }
            }
        } finally {
            lockPlayer.readLock().unlock();
        }
        return members;
    }

    public void removeNpc(Npc npc) {
        lockNpc.writeLock().lock();
        try {
            npcs.remove(npc);
        } finally {
            lockNpc.writeLock().unlock();
        }
    }

    public void addNpc(Npc npc) {
        lockNpc.writeLock().lock();
        try {
            npcs.add(npc);
        } finally {
            lockNpc.writeLock().unlock();
        }
    }

    public void enter(Npc npc) {
        try {
            if (npc.zone != null) {
                npc.zone.leave(npc);
            }
            npc.zone = this;
        } finally {
            addNpc(npc);
        }
        service.addNpc(npc);
    }

    public void leave(Npc npc) {
        removeNpc(npc);
        service.removeNpc(npc);
    }

    public List<Npc> getNpcs() {
        lockNpc.readLock().lock();
        try {
            return npcs;
        } finally {
            lockNpc.readLock().unlock();
        }
    }

    public void npcChat(Npc npc, String content) {
        service.npcChat(npc, content);
    }

    public Npc findNpcById(int id) {
        lockNpc.readLock().lock();
        try {
            return npcs.stream().filter(n -> n.template.id == id).findFirst().orElse(null);
        } finally {
            lockNpc.readLock().unlock();
        }
    }

    public List<Monster> getMonsters() {
        lockMonster.readLock().lock();
        try {
            return monsters.stream().filter(m -> !m.isPet()).collect(Collectors.toList());
        } finally {
            lockMonster.readLock().unlock();
        }
    }

    public List<Monster> findMonsterDies() {
        lockMonster.readLock().lock();
        try {
            return monsters.stream().filter(Monster::isDead).collect(Collectors.toList());
        } finally {
            lockMonster.readLock().unlock();
        }
    }

    public List<Monster> findMonsterLives() {
        lockMonster.readLock().lock();
        try {
            return monsters.stream().filter(m -> !m.isDead()).collect(Collectors.toList());
        } finally {
            lockMonster.readLock().unlock();
        }
    }

    public Monster findMonsterById(int id) {
        lockMonster.readLock().lock();
        try {
            return monsters.stream().filter(m -> m.id == id).findFirst().orElse(null);
        } finally {
            lockMonster.readLock().unlock();
        }
    }

    public Monster findAndRandomMonster(MonsterLevelBoss levelBoss) {
        lockMonster.readLock().lock();
        try {
            List<Monster> monsters = this.monsters.stream().filter(m -> m.levelBoss == levelBoss).collect(Collectors.toList());
            if (monsters.isEmpty()) {
                return null;
            }
            return monsters.get(Utils.nextInt(monsters.size()));
        } finally {
            lockMonster.readLock().unlock();
        }
    }

    public void upNumRefreshMonster(int count) {
        lockMonster.writeLock().lock();
        try {
            for (Monster monster : monsters) {
                monster.numRefresh += count;
            }
        } finally {
            lockMonster.writeLock().unlock();
        }
    }

    public void addMonster(Monster monster) {
        lockMonster.writeLock().lock();
        try {
            monsters.add(monster);
        } finally {
            lockMonster.writeLock().unlock();
        }
    }

    public void removeMonster(Monster monster) {
        lockMonster.writeLock().lock();
        try {
            monsters.remove(monster);
        } finally {
            lockMonster.writeLock().unlock();
        }
        service.removeMonster(monster);
    }

    public void leave(Monster monster) {
        removeMonster(monster);
        service.removeMonster(monster);
    }

    public void enter(Monster monster) {
        try {
            if (monster.zone != null) {
                monster.zone.removeMonster(monster);
            }
            monster.zone = this;
            if (monster.id == 0) {
                monster.id = monsterAutoIncrease++;
            }
        } finally {
            addMonster(monster);
        }
        service.addMonster(monster);
    }

    public void removeBigMonster(BigMonster monster) {
        lockMonster.writeLock().lock();
        try {
            monsters.remove(monster);
        } finally {
            lockMonster.writeLock().unlock();
        }
        service.removeBigMonster(monster);
    }

    public void enterBigMonster(BigMonster monster) {
        try {
            if (monster.zone != null) {
                monster.zone.removeBigMonster(monster);
            }
            monster.zone = this;
            monster.id = monsterAutoIncrease++;
            monster.x = map.template.width / 2;
            monster.y = map.getYSd(monster.x);
            if (monster.template.typeMove == MonsterTypeMove.FLY) {
                monster.y -= 300;
            }
            monster.sendNotificationWhenAppear(this);
        } finally {
            addMonster(monster);
        }
        service.addBigMonster(monster);
    }

    public List<ItemMap> getItemMaps() {
        lockItemMap.readLock().lock();
        try {
            return itemMaps;
        } finally {
            lockItemMap.readLock().unlock();
        }
    }

    public void addItemMap(ItemMap itemMap) {
        lockItemMap.writeLock().lock();
        try {
            itemMap.id = itemMapAutoIncrease++;
            this.itemMaps.add(itemMap);
        } finally {
            lockItemMap.writeLock().unlock();
        }
        service.addItemMap(itemMap);
    }

    public void addItemMap(List<ItemMap> itemMaps, int x) {
        lockItemMap.writeLock().lock();
        try {
            Collections.shuffle(itemMaps);
            for (int i = 0; i < itemMaps.size(); i++) {
                ItemMap itemMap = itemMaps.get(i);
                itemMap.id = itemMapAutoIncrease++;
                if (x != -1) {
                    if (i == 0) {
                        itemMap.x = x;
                    } else {
                        itemMap.x = x + ((i - 1) / 2 + 1) * Utils.nextInt(20, 40) * (i % 2 == 0 ? 1 : (-1));
                    }
                } else if (itemMap.x == -1) {
                    itemMap.x = Utils.nextInt(300, map.template.width - 300);
                }
                if (itemMap.playerId < -1) {
                    itemMap.playerId = Math.abs(itemMap.playerId);
                }
                itemMap.y = map.getYSd(itemMap.x);
            }
            this.itemMaps.addAll(itemMaps);
        } finally {
            lockItemMap.writeLock().unlock();
        }
        for (ItemMap itemMap : itemMaps) {
            service.addItemMap(itemMap);
        }
    }

    public void removeItemMap(ItemMap itemMap, boolean isUpdate) {
        lockItemMap.writeLock().lock();
        try {
            itemMaps.remove(itemMap);
        } finally {
            lockItemMap.writeLock().unlock();
        }
        if (isUpdate) {
            service.removeItemMap(itemMap);
        }
    }

    public ItemMap findItemMapById(int id) {
        lockItemMap.readLock().lock();
        try {
            return itemMaps.stream().filter(itemMap -> itemMap.id == id).findFirst().orElse(null);
        } finally {
            lockItemMap.readLock().unlock();
        }
    }

    public ItemMap findItemMapByTemplateId(int id) {
        lockItemMap.readLock().lock();
        try {
            return itemMaps.stream().filter(itemMap -> itemMap.template.id == id).findFirst().orElse(null);
        } finally {
            lockItemMap.readLock().unlock();
        }
    }

    public void close() {
        isRunning = false;
    }
}
