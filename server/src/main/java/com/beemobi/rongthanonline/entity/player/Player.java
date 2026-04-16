package com.beemobi.rongthanonline.entity.player;

import com.beemobi.rongthanonline.achievement.Achievement;
import com.beemobi.rongthanonline.achievement.AchievementManager;
import com.beemobi.rongthanonline.bot.boss.Boss;
import com.beemobi.rongthanonline.bot.disciple.Disciple;
import com.beemobi.rongthanonline.bot.disciple.DiscipleStatus;
import com.beemobi.rongthanonline.bot.escort.Escort;
import com.beemobi.rongthanonline.clan.Clan;
import com.beemobi.rongthanonline.clan.ClanManager;
import com.beemobi.rongthanonline.clan.ClanMember;
import com.beemobi.rongthanonline.command.CommandName;
import com.beemobi.rongthanonline.common.KeyValue;
import com.beemobi.rongthanonline.common.Language;
import com.beemobi.rongthanonline.data.*;
import com.beemobi.rongthanonline.data.history.HistoryBuyItemData;
import com.beemobi.rongthanonline.data.history.HistoryGiveDiamondData;
import com.beemobi.rongthanonline.dragon.Dragon;
import com.beemobi.rongthanonline.effect.Effect;
import com.beemobi.rongthanonline.effect.EffectName;
import com.beemobi.rongthanonline.entity.Entity;
import com.beemobi.rongthanonline.command.Command;
import com.beemobi.rongthanonline.entity.monster.*;
import com.beemobi.rongthanonline.entity.monster.big.BigMonster;
import com.beemobi.rongthanonline.entity.monster.pet.Pet;
import com.beemobi.rongthanonline.entity.player.action.*;
import com.beemobi.rongthanonline.entity.player.json.*;
import com.beemobi.rongthanonline.event.Event;
import com.beemobi.rongthanonline.gift.GiftCode;
import com.beemobi.rongthanonline.gift.GiftCodeManager;
import com.beemobi.rongthanonline.history.*;
import com.beemobi.rongthanonline.lucky.LuckyManager;
import com.beemobi.rongthanonline.map.expansion.ExpansionState;
import com.beemobi.rongthanonline.map.expansion.congress.ZoneMartialCongress;
import com.beemobi.rongthanonline.map.expansion.flagwar.ZoneFlagWar;
import com.beemobi.rongthanonline.map.expansion.island.ZoneIsland;
import com.beemobi.rongthanonline.map.expansion.spaceship.Spaceship;
import com.beemobi.rongthanonline.map.expansion.spaceship.ZoneSpaceship;
import com.beemobi.rongthanonline.map.expansion.survival.ZoneSurvival;
import com.beemobi.rongthanonline.map.expansion.tournament.ZoneTournament;
import com.beemobi.rongthanonline.map.expansion.treasure.Pirate;
import com.beemobi.rongthanonline.map.expansion.treasure.Treasure;
import com.beemobi.rongthanonline.mission.*;
import com.beemobi.rongthanonline.map.*;
import com.beemobi.rongthanonline.map.Map;
import com.beemobi.rongthanonline.map.expansion.barrack.Barrack;
import com.beemobi.rongthanonline.map.expansion.festival.Arena;
import com.beemobi.rongthanonline.map.expansion.festival.Flight;
import com.beemobi.rongthanonline.map.expansion.festival.MartialArtsFestival;
import com.beemobi.rongthanonline.map.expansion.festival.Warrior;
import com.beemobi.rongthanonline.map.expansion.manor.Manor;
import com.beemobi.rongthanonline.map.expansion.manor.ManorType;
import com.beemobi.rongthanonline.map.expansion.nrnm.ZoneDragonBallNamek;
import com.beemobi.rongthanonline.map.expansion.survival.Gamer;
import com.beemobi.rongthanonline.map.expansion.survival.Survival;
import com.beemobi.rongthanonline.model.*;
import com.beemobi.rongthanonline.network.*;
import com.beemobi.rongthanonline.npc.*;
import com.beemobi.rongthanonline.item.*;
import com.beemobi.rongthanonline.model.input.ClientInputType;
import com.beemobi.rongthanonline.model.input.TextField;
import com.beemobi.rongthanonline.model.waypoint.WayPoint;
import com.beemobi.rongthanonline.model.waypoint.WayPointType;
import com.beemobi.rongthanonline.repository.GameRepository;
import com.beemobi.rongthanonline.server.Server;
import com.beemobi.rongthanonline.server.ServerRandom;
import com.beemobi.rongthanonline.service.Service;
import com.beemobi.rongthanonline.shop.Shop;
import com.beemobi.rongthanonline.shop.ShopManager;

import com.beemobi.rongthanonline.shop.ShopType;
import com.beemobi.rongthanonline.shop.TypePrice;
import com.beemobi.rongthanonline.skill.*;
import com.beemobi.rongthanonline.task.Task;
import com.beemobi.rongthanonline.task.TaskDaily;
import com.beemobi.rongthanonline.task.TaskDailyType;
import com.beemobi.rongthanonline.task.TaskManager;
import com.beemobi.rongthanonline.team.Team;
import com.beemobi.rongthanonline.team.TeamManager;
import com.beemobi.rongthanonline.team.TeamMember;
import com.beemobi.rongthanonline.top.TopManager;
import com.beemobi.rongthanonline.upgrade.Crystallize;
import com.beemobi.rongthanonline.upgrade.UpgradeItem;
import com.beemobi.rongthanonline.upgrade.UpgradeType;
import com.beemobi.rongthanonline.util.Utils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Player extends Entity {
    public static final int DEFAULT_BAG = 36;
    public static final int DEFAULT_BOX = 36;
    public static final int DEFAULT_BODY = 20;
    public static final int RANGE_HP = 1;
    public static final int RANGE_MP = 1;
    public static final int RANGE_DAMAGE = 1;
    public static final int RANGE_CONSTITUTION = 50000;
    public static final int INFO_RED = 0;
    public static final int INFO_YELLOW = 1;
    public static final int RECOVERY_ALL = 0;
    public static final int RECOVERY_HP = 1;
    public static final int RECOVERY_MP = 2;
    public static final int TYPE_PK_NORMAL = 0;
    public static final int TYPE_PK_SOLO = 1;
    public static final int TYPE_PK_DO_SAT = 2;
    public static final int TYPE_PK_BOSS = 3;
    public static final int TYPE_FLAG_NORMAL = 0;
    public static final int TYPE_FLAG_BLACK = 1;
    private static final Logger logger = Logger.getLogger(Player.class);
    private static final Pattern MAP_GROUP_TRAILING_NUMBER_PATTERN = Pattern.compile("^(.*?)(\\d+)$");
    public PlayerData data;
    public Service service;
    public Session session;
    public String name, pin;
    public int userId, server, gender, diamond, ruby;
    public long xu, xuKhoa;
    public int baseHp, baseMp, baseDamage, baseConstitution;

    public transient int speed, typePk, typeFlag, upgrade;
    public transient int body, head;
    public transient int mount = -1;
    public transient int medal = -1;
    public transient int aura = -1;
    public transient int bag = -1;
    public transient int bagLoot = -1;
    public transient int tabID = -1;
    public transient boolean isHide;
    public int teamId = -1;
    public int barrackId = -1;
    public int testPlayerId = -1;
    public boolean isFirstMap, isBlockStrangers, isFusion, isAutoPlay, isCanUseSkillCharge, isCharge, isLogout,
            isRechargedInDay, isHideMark, isTrainingOfflineForMaster, isTrainingOfflineForDisciple, isProtect, isFirstDodge;
    public int pointPlant, pointSurvival, pointCapsule, pointEvent, indexRewardEvent, pointOtherEvent, pointRewardEvent,
            pointPk, pointActive, countBarrack, numBuyBarrack, pointBarrack, spaceship, percentVietThuongSau, pointSpaceship,
            pointCurrSpaceship, typeLuckyBox;

    public int tickMoveMod, tickPickMod, onlineMinuteTotal, typeFusion, onlineMinuteDay;
    public long power, potential, expTraining, timeLogin, timeVetThuongSau, updatePowerTime;
    public int limitLevel, pointSkill, diamondRecharge, pointFlagWar;

    // LOCK
    public Lock lockAction = new ReentrantLock();
    public ReadWriteLock lockMessageTime = new ReentrantReadWriteLock();
    public long lastTimeChatPublic, lastTimeChatPlayer, lastTimeChatGlobal, lastTimeChangePk, lastTimeChangeArea, lastTimePickItem, lastTimeUseBean,
            lastTimeFusion, lastTimeInputGiftCode, updateTimeEvent, lastTimeTeleport, updateTimeEventOther;
    public long[] timeUpdateEffectOptionItem = new long[8];

    // MAP
    public transient int preX, preY, mapId;
    public Lock lockMove;
    public transient long lastTimeMove, pointPro;

    // CLAN
    public Clan clan;
    public int clanId = -1;
    public long clanTime;
    public int countManor;

    // TASK
    public Task taskMain;
    public TaskDaily taskDaily;
    public long createTaskDailyTime;
    public int countCompleteTaskDaily, countTaskDaily;
    public ArrayList<IMission> missionWeeks;
    public ArrayList<IMission> missionDailies;
    public ArrayList<IMission> missionRecharges;
    public ArrayList<IMission> missionEvents;

    public List<Skill> skills;
    public Skill[] keysSkill;
    public Skill mySkill;
    public Lock lockAttack = new ReentrantLock();
    public int skillUseId = -1;
    public ArrayList<Integer> mapsLoad;
    public ArrayList<KeyValue<Map, String>> mapSpaceships;
    public transient boolean mapSpaceshipNeedCapsule;
    public int lastMapSpaceshipId;
    public NpcController npcController;
    public NpcTaskController npcTaskController;
    public ArrayList<Command> commands = new ArrayList<>();
    public LinkedHashMap<String, List<ItemShop>> itemsShop;
    public Item[] itemsBody, itemsBag, itemsBox, itemsSurvival, itemsPet, itemsOther;
    public UpgradeType upgradeType;
    public Entity focus;
    public ZoneGoBack zoneGoBack;
    public Disciple disciple;
    public Pet pet;
    public Escort escort;
    public MagicBean magicBean;
    public ArrayList<Friend> friends;
    public ArrayList<Enemy> enemies;
    public ArrayList<Reward> rewards;
    public ArrayList<MessageTime> messageTimes;
    public ArrayList<NpcTree> npcTrees;
    public HashMap<Integer, Achievement> achievements;
    public MapPlanet planet = MapPlanet.EARTH;
    public Timestamp resetTime, logoutTime;
    public Action friendAction, enemyAction, teamAction, consignmentAction, clanAction, missionAction, tradeAction, weeklyAction;
    public LockInfo lockInfo;
    public ClientInputType clientInputType;
    public TreeMap<Integer, Intrinsic> intrinsics;
    public IntrinsicAttack intrinsicAttack;

    public Player() {
        super();
        Arrays.fill(lastUpdates, System.currentTimeMillis());
    }

    public Player(PlayerData data) {
        super();
        npcController = new NpcController(this);
        npcTaskController = new NpcTaskController(this);
        this.data = data;
        mapsLoad = new ArrayList<>();
        mapSpaceships = new ArrayList<>();
        mapSpaceshipNeedCapsule = false;
        lockMove = new ReentrantLock();
        lastMapSpaceshipId = -1;
        long now = System.currentTimeMillis();
        lastTimeChangePk = now;
        lastTimeChatGlobal = now;
        lastTimeChatPublic = now;
        lastTimeChangeArea = now;
        lastTimeAttack = now;
        lastTimeMove = now;
        friendAction = new FriendAction(this);
        enemyAction = new EnemyAction(this);
        teamAction = new TeamAction(this);
        consignmentAction = new ConsignmentAction(this);
        clanAction = new ClanAction(this);
        missionAction = new MissionAction(this);
        tradeAction = new TradeAction(this);
        weeklyAction = new WeeklyAction(this);
        messageTimes = new ArrayList<>();
        init();
    }

    @Override
    public void update() {
        updateEffect();
        updateEveryTime();
    }

    @Override
    public void updateEveryHalfSeconds(long now) {
        try {

        } catch (Exception e) {
            logger.error("updateEveryHalfSeconds", e);
        }
    }

    public void updateResetEveryDay(long now) {
        if (resetTime == null) {
            resetTime = new Timestamp(now);
        }
        int day = Utils.getCountDay(resetTime);
        if (day <= 0) {
            return;
        }
        try {
            if (day > 1 || !isRechargedInDay) {
                resetMissionWeek();
            } else {
                boolean isReset = false;
                for (IMission mission : missionWeeks) {
                    if (mission.getName().equals("Ngày 7") && mission.getType() != MissionType.CHUA_HOAN_THANH) {
                        isReset = true;
                        break;
                    }
                }
                if (isReset) {
                    resetMissionWeek();
                }
            }
            if (day >= 7 || Utils.getIndexDayOfWeek(resetTime.getTime()) >= Utils.getIndexDayOfWeek(now)) {
                ((WeeklyAction) weeklyAction).reset();
            }
            isRechargedInDay = false;
            resetMissionDaily();
            onlineMinuteDay = 0;
            if (pointActive > 0) {
                pointActive -= 2;
                if (pointActive < 0) {
                    pointActive = 0;
                }
                service.setPointActive();
            }
            pointCapsule = 0;
            countManor = 1;
            countTaskDaily = 30;
            countCompleteTaskDaily = 0;
            numBuyBarrack = 0;
            if (countBarrack != 1) {
                countBarrack = 1;
                service.setCountBarrack();
            }
            if (pointPk > 0) {
                pointPk--;
                service.setPointPk();
            }
            boolean isUpdateInfo = false;
            if (isRemoveItemOutOfNewDay(itemsBag, day)) {
                service.setItemBag();
            }
            if (isRemoveItemOutOfNewDay(itemsBox, day)) {
                service.setItemBox();
            }
            if (isRemoveItemOutOfNewDay(itemsBody, day)) {
                service.setItemBody();
                isUpdateInfo = true;
            }
            if (isRemoveItemOutOfNewDay(itemsPet, day) && pet != null) {
                isUpdateInfo = true;
                service.petInfo(MessagePetInfoName.ITEM_BODY);
            }
            if (disciple != null) {
                if (isRemoveItemOutOfNewDay(disciple.itemsBody, day)) {
                    isUpdateInfo = true;
                    service.discipleInfo(MessageDiscipleInfoName.ITEM_BODY);
                    disciple.refreshInfo();
                    service.discipleInfo(MessageDiscipleInfoName.POINT);
                    disciple.refreshPart();
                    service.discipleInfo(MessageDiscipleInfoName.ALL_PART);
                    if (disciple.zone != null) {
                        disciple.zone.service.refreshHp(disciple);
                        disciple.zone.service.refreshPlayerPart(disciple);
                    }
                }
            }
            if (isUpdateInfo) {
                service.setInfo();
            }
        } finally {
            resetTime = new Timestamp(now);
        }
    }

    private boolean isRemoveItemOutOfNewDay(Item[] items, int day) {
        boolean isModify = false;
        for (int i = 0; i < items.length; i++) {
            Item item = items[i];
            if (item != null) {
                for (ItemOption itemOption : item.options) {
                    if (itemOption.template.id == 50) {
                        itemOption.param -= day;
                        if (itemOption.param < 1) {
                            items[i] = null;
                        }
                        isModify = true;
                        break;
                    }
                }
            }
        }
        return isModify;
    }

    @Override
    public void updateEveryOneSeconds(long now) {
        try {
            if (!isPlayer() || zone == null) {
                return;
            }
            updateResetEveryDay(now);
            if (!isDead()) {
                boolean isUpdate = false;
                if (zone.map.isWall(x, y - 5)) {
                    int ySd = zone.map.getYSd(x);
                    if (ySd <= 10) {
                        x = 200;
                        y = zone.map.getYSd(x);
                    } else {
                        y = ySd;
                    }
                    isUpdate = true;
                }
                if (zone instanceof ArenaCustom arenaCustom && this.y <= ArenaCustom.POSITION[0][1]) {
                    if (this.id != arenaCustom.warriors[0].id && this.id != arenaCustom.warriors[1].id) {
                        isUpdate = true;
                        this.x = ArenaCustom.POSITION_REFEREE[0][0];
                        this.y = ArenaCustom.POSITION_REFEREE[0][1];
                        addInfo(INFO_RED, "Tài khoản của bạn sẽ bị khóa nếu còn tiếp tục vi phạm");
                    }
                }
                if (zone instanceof ZoneMartialCongress zoneMartialCongress && this.y <= ArenaCustom.POSITION[0][1]) {
                    if (this.id != zoneMartialCongress.warriorId) {
                        isUpdate = true;
                        this.x = ArenaCustom.POSITION_REFEREE[0][0];
                        this.y = ArenaCustom.POSITION_REFEREE[0][1];
                        addInfo(INFO_RED, "Tài khoản của bạn sẽ bị khóa nếu còn tiếp tục vi phạm");
                    }
                }
                if (zone instanceof ZoneTournament zoneTournament && this.y <= ArenaCustom.POSITION[0][1]) {
                    if (this.id != zoneTournament.warriorId) {
                        isUpdate = true;
                        this.x = ArenaCustom.POSITION_REFEREE[0][0];
                        this.y = ArenaCustom.POSITION_REFEREE[0][1];
                        addInfo(INFO_RED, "Tài khoản của bạn sẽ bị khóa nếu còn tiếp tục vi phạm");
                    }
                }
                if (isUpdate) {
                    zone.service.setPosition(this);
                }
            }
            if (!isInSurvival() && !isDead()) {
                boolean[] isUpdates = new boolean[timeUpdateEffectOptionItem.length];
                for (int i = 0; i < timeUpdateEffectOptionItem.length; i++) {
                    if (now - timeUpdateEffectOptionItem[i] > 30000) {
                        timeUpdateEffectOptionItem[i] = now;
                        if (options[86 + i] > 0) {
                            addEffect(new Effect(this, 37 + i, 5000, 0, options[86 + i]));
                            isUpdates[i] = true;
                        }
                    }
                }
                long last_hp = maxHp;
                for (boolean isUpdate : isUpdates) {
                    if (isUpdate) {
                        service.setInfo();
                        break;
                    }
                }
                if (isUpdates[ItemType.TYPE_QUAN] && last_hp < maxHp) {
                    long dis = maxHp - last_hp;
                    if (hp + dis <= maxHp) {
                        hp += dis;
                    } else {
                        hp = maxHp;
                    }
                    service.recoveryHp();
                }
            }
            if (zone.map.template.id == MapName.DAI_HOI_VO_THUAT && this.y <= 792 && (MapManager.getInstance().martialArtsFestival == null || MapManager.getInstance().martialArtsFestival.masterId != this.id)) {
                Arena arena = (Arena) zone;
                if (arena.flight == null || (arena.flight.player1.id != this.id && arena.flight.player2.id != this.id)) {
                    this.x = Flight.POSITION_WAIT[0];
                    this.y = Flight.POSITION_WAIT[1];
                    zone.service.setPosition(this);
                }
            }
            List<Player> playerList = zone.getPlayers(Zone.TYPE_PLAYER, Zone.TYPE_DISCIPLE);
            int[] options = new int[this.options.length];
            if (!playerList.isEmpty()) {
                for (Player player : playerList) {
                    if (player == this || player.isDead()) {
                        continue;
                    }
                    int d = Utils.getDistance(this.x, this.y, player.x, player.y);
                    if (d < 500) {
                        if (this.options[106] == 0 && options[106] == 0 && player.options[106] > 0) {
                            options[106] = player.options[106];
                        }
                        if (this.options[170] == 0 && options[170] == 0 && player.options[170] > 0) {
                            options[170] = Math.min(player.options[170], 50);
                        }
                        if (this.options[191] == 0 && options[191] == 0 && player.options[191] > 0) {
                            options[191] = player.options[191];
                        }
                    }
                }
            }
            boolean isUpdate = false;
            for (int i = 0; i < options.length; i++) {
                if (options[i] != optionZones[i]) {
                    optionZones[i] = options[i];
                    isUpdate = true;
                }
            }
            if (isUpdate) {
                service.setInfo();
                zone.service.refreshHp(this);
            }
        } catch (Exception e) {
            logger.error("updateEveryOneSeconds", e);
        }
    }

    @Override
    public void updateEveryFiveSeconds(long now) {
        try {
            if (isPlayer() && isDead() && !isInBarrack() && now - timeDie > (isInTreasure() ? 5000 : 15000)) {
                returnTownFromDead();
            }
            if (options[107] > 0 && zone != null) {
                long hpRecovery = 0;
                long mpRecovery = 0;
                if (zone.map.template.id != MapName.DAI_HOI_VO_THUAT) {
                    List<Player> playerList = zone.getPlayers(Zone.TYPE_PLAYER, Zone.TYPE_DISCIPLE);
                    if (!playerList.isEmpty()) {
                        for (Player player : playerList) {
                            if (player == this || player.isDead()) {
                                continue;
                            }
                            int d = Utils.getDistance(this.x, this.y, player.x, player.y);
                            if (d < 500) {
                                if (player.hp > 1) {
                                    long hp = player.maxHp * options[107] / 100;
                                    if (hp >= player.hp) {
                                        hp = player.hp - 1;
                                    }
                                    if (hp > 0) {
                                        player.recovery(RECOVERY_HP, -hp);
                                        hpRecovery += hp;
                                    }
                                }
                                if (player.mp > 1) {
                                    long mp = player.maxMp * options[107] / 100;
                                    if (mp >= player.mp) {
                                        mp = player.mp - 1;
                                    }
                                    if (mp > 0) {
                                        player.recovery(RECOVERY_MP, -mp);
                                        mpRecovery += mp;
                                    }
                                }
                            }
                        }
                    }
                }
                if (!zone.map.isExpansion()) {
                    List<Monster> monsterList = zone.getMonsters();
                    for (Monster monster : monsterList) {
                        int d = Utils.getDistance(this.x, this.y, monster.x, monster.y);
                        if (d < 500) {
                            monster.lock.lock();
                            try {
                                if (!monster.isBigMonster() && !monster.isDead() && monster.levelBoss == MonsterLevelBoss.NORMAL && monster.hp > 1) {
                                    long hp = monster.maxHp * options[107] / 100;
                                    if (hp >= monster.hp) {
                                        hp = monster.hp - 1;
                                    }
                                    if (hp > 0) {
                                        monster.hp -= hp;
                                        zone.service.monsterInjure(monster, hp, false);
                                        hpRecovery += hp;
                                    }
                                }
                            } finally {
                                monster.lock.unlock();
                            }
                        }
                    }
                }
                if (hpRecovery > 0) {
                    recovery(RECOVERY_HP, hpRecovery);
                }
                if (mpRecovery > 0) {
                    recovery(RECOVERY_MP, mpRecovery);
                }
            }
        } catch (Exception e) {
            logger.error("updateEveryFiveSeconds", e);
        }
    }

    @Override
    public void updateEveryThirtySeconds(long now) {
        try {
            if (!isDead()) {
                if (options[OptionName.HOI_HP_30_GIAY] > 0) {
                    recovery(RECOVERY_HP, options[OptionName.HOI_HP_30_GIAY], true);
                }
                if (options[OptionName.HOI_MP_30_GIAY] > 0) {
                    recovery(RECOVERY_MP, options[OptionName.HOI_MP_30_GIAY], true);
                }
                if ((isPlayer() || isDisciple()) && zone != null) {
                    Manor manor = zone.map.getManor();
                    if (manor != null && Utils.nextInt(2) == 0) {
                        if (manor.type == ManorType.MANOR_COLD) {
                            addEffect(new Effect(this, EffectName.DONG_BANG, 5000));
                        } else if (manor.type == ManorType.MANOR_FIRE) {
                            addEffect(new Effect(this, EffectName.THIEU_DOT, 10000, 1000, 5));
                        }
                    }
                }
                if ((this.options[136] > 0 || this.options[137] > 0) && zone != null) {
                    List<Player> playerList = zone.getPlayers(Zone.TYPE_PLAYER, Zone.TYPE_DISCIPLE);
                    if (playerList.size() > 1) {
                        if (this.options[136] > 0) {
                            chat("Phẹtttttt");
                        }
                        if (this.options[137] > 0) {
                            chat("Úm ba laaaaa");
                        }
                        for (Player player : playerList) {
                            if (player.isDead() || player == this || (this.options[136] > 0 && (player.options[136] > 0 || player.options[137] > 0)) || (this.options[137] > 0 && player.options[137] > 0)) {
                                continue;
                            }
                            if (Utils.getDistance(this.x, this.y, player.x, player.y) > 900) {
                                continue;
                            }
                            if (this.options[137] > 0) {
                                player.addEffect(new Effect(player, EffectName.HOA_SOCOLA, Math.min(this.options[137] * 1000L, 5000)));
                            }
                            if (this.options[136] > 0) {
                                player.addEffect(new Effect(player, EffectName.HOA_DA, Math.min(this.options[136] * 1000L, 5000)));
                            }
                        }
                    }
                }
                if (options[171] > 0) {
                    isFirstDodge = true;
                }
            }
        } catch (Exception e) {
            logger.error("updateEveryThirtySeconds", e);
        }
    }

    @Override
    public void updateEveryOneMinutes(long now) {
        try {
            if (isPlayer() && !isInSurvival()) {
                if (now - lastTimeAttack < 5000) {
                    Item item = itemsBody[ItemType.TYPE_VONG_KIM_HAM];
                    if (item != null) {
                        int max = 10;
                        if (item.template.id == ItemName.VONG_KIM_HAM_VANG) {
                            max = 100;
                        } else if (item.template.id == ItemName.VONG_KIM_HAM_BACH_KIM) {
                            max = 1000;
                        }
                        for (ItemOption option : item.options) {
                            if (option.template.id == 24) {
                                if (option.param < max) {
                                    option.param++;
                                    service.refreshItemBody(item.indexUI);
                                }
                                break;
                            }
                        }
                    }
                }
                boolean isRefresh = false;
                for (int i = 0; i < itemsBag.length; i++) {
                    Item item = itemsBag[i];
                    if (item != null && item.template.type == ItemType.TYPE_VONG_KIM_HAM) {
                        for (ItemOption option : item.options) {
                            if (option.template.id == 24 && option.param > 0) {
                                option.param--;
                                if (option.param == 0) {
                                    isRefresh = true;
                                }
                                service.refreshItemBag(i);
                                break;
                            }
                        }
                    }
                }
                if (isRefresh) {
                    service.setInfo();
                }
            }
            if (isPlayer()) {
                onlineMinuteTotal++;
                onlineMinuteDay++;
                for (int i = 0; i < npcTrees.size(); i++) {
                    NpcTree npcTree = npcTrees.get(i);
                    if (npcTree.time > 0) {
                        npcTree.time--;
                        if (npcTree.time == 0) {
                            NpcTree newTree = new NpcTree(this, npcTree.template.id + 1, 0);
                            newTree.x = npcTree.x;
                            newTree.y = npcTree.y;
                            npcTrees.set(i, newTree);
                            if (zone != null && zone.map.template.id == MapName.NHA_GO_HAN) {
                                zone.leave(npcTree);
                                zone.enter(newTree);
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            logger.error("updateEveryOneMinutes", e);
        }
    }

    @Override
    public void updateEveryTenSeconds(long now) {
        try {
            if (isPlayer() || isDisciple()) {
                if (zone != null && options[85] == 0) {
                    List<Player> players = zone.getPlayers(Zone.TYPE_PLAYER, Zone.TYPE_DISCIPLE);
                    if (players.size() > 1) {
                        for (Player player : players) {
                            if (player != this && Math.abs(x - player.x) < 500 && player.options[85] > 0) {
                                zone.service.chatPublic(this, "Wowwww, Nàng đẹp quá <3");
                                break;
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            logger.error("updateEveryTenSeconds", e);
        }
    }

    public void addMessageTime(int id, long time) {
        lockMessageTime.writeLock().lock();
        try {
            MessageTime messageTime = messageTimes.stream().filter(m -> m.id == id).findFirst().orElse(null);
            if (messageTime == null) {
                messageTime = new MessageTime(id, time);
                messageTimes.add(messageTime);
            } else {
                messageTime.endTime = System.currentTimeMillis() + time;
            }
            service.messageTime(messageTime.id, messageTime.getCountDown(), messageTime.text);
        } finally {
            lockMessageTime.writeLock().unlock();
        }
    }

    public void addMessageTime(int id, long time, String text) {
        lockMessageTime.writeLock().lock();
        try {
            MessageTime messageTime = messageTimes.stream().filter(m -> m.id == id).findFirst().orElse(null);
            if (messageTime == null) {
                messageTime = new MessageTime(id, time);
                messageTimes.add(messageTime);
            } else {
                messageTime.endTime = System.currentTimeMillis() + time;
            }
            messageTime.text = text;
            service.messageTime(messageTime.id, messageTime.getCountDown(), messageTime.text);
        } finally {
            lockMessageTime.writeLock().unlock();
        }
    }

    public void removeMessageTime(int id) {
        lockMessageTime.writeLock().lock();
        try {
            if (messageTimes.removeIf(m -> m.id == id)) {
                service.messageTime(id, 0, null);
            }
        } finally {
            lockMessageTime.writeLock().unlock();
        }
    }

    public void updateMessageTime() {
        if (messageTimes == null) {
            return;
        }
        lockMessageTime.writeLock().lock();
        try {
            long now = System.currentTimeMillis();
            for (int i = 0; i < messageTimes.size(); i++) {
                MessageTime messageTime = messageTimes.get(i);
                if (messageTime.endTime < now) {
                    messageTimes.remove(messageTime);
                    i--;
                    service.messageTime(messageTime.id, 0, null);
                }
            }
        } finally {
            lockMessageTime.writeLock().unlock();
        }
    }

    public void openNpc(Message message) {
        try {
            if (isDead() || zone == null) {
                return;
            }
            if (isTrading()) {
                addInfo(INFO_RED, Language.CANCEL_ACTION_WHEN_TRADE);
                return;
            }
            if (isStun()) {
                addInfo(INFO_RED, Language.CANT_ACTION);
                return;
            }
            int npcId = message.reader().readByte();
            if (npcId == 3) {
                service.openZoneUI();
                return;
            }
            if (npcId == -1) {
                service.showNotification(Server.getInstance().notes);
                return;
            }
            if (npcId == -2) {
                openMapByCurrentPlanet();
                return;
            }
            Npc npc = zone.getNpcs().stream().filter(n -> n.template.id == npcId && Math.abs(this.x - n.x) <= 300).findFirst().orElse(null);
            if (npcId == NpcName.ME || npcId == NpcName.MA_BAO_VE || npc != null) {
                npcController.openMenu(npcId, npc);
            }
        } catch (Exception ex) {
            logger.error("openNpc", ex);
        }
    }

    private void openMapByCurrentPlanet() {
        if (!hasCapsuleForMapTeleport()) {
            addInfo(INFO_RED, "Bạn cần Capsule tàu bay để mở bản đồ");
            return;
        }
        MapPlanet currentPlanet = zone.map.template.planet;
        ArrayList<KeyValue<Map, String>> mapList = new ArrayList<>();
        if (lastMapSpaceshipId != -1) {
            Map oldMap = MapManager.getInstance().maps.get(lastMapSpaceshipId);
            if (oldMap != null && oldMap.template.planet == currentPlanet && oldMap.template.id != zone.map.template.id && isCanJoinMap(oldMap)) {
                mapList.add(new KeyValue<>(oldMap, "Chỗ cũ: " + oldMap.template.name, oldMap.getPlanetName()));
            }
        }
        ArrayList<Map> maps = new ArrayList<>(MapManager.getInstance().maps.values());
        maps.sort(Comparator.comparingInt(m -> m.template.id));
        for (Map map : maps) {
            if (map.template.id == zone.map.template.id || map.template.id == lastMapSpaceshipId) {
                continue;
            }
            if (map.template.planet != currentPlanet || !isCanJoinMap(map)) {
                continue;
            }
            mapList.add(new KeyValue<>(map, map.template.name, map.getPlanetName()));
        }
        showListMapSpaceship(mapList, true, true, false);
    }

    private boolean hasCapsuleForMapTeleport() {
        return getQuantityItemInBag(ItemName.CAPSULE_TAU_BAY_DAC_BIET) > 0 || getQuantityItemInBag(ItemName.CAPSULE_TAU_BAY_THUONG) > 0;
    }

    private boolean consumeCapsuleForMapTeleport() {
        if (getQuantityItemInBag(ItemName.CAPSULE_TAU_BAY_DAC_BIET) > 0) {
            return true;
        }
        if (getQuantityItemInBag(ItemName.CAPSULE_TAU_BAY_THUONG) > 0) {
            removeQuantityItemBagById(ItemName.CAPSULE_TAU_BAY_THUONG, 1);
            return true;
        }
        addInfo(INFO_RED, "Bạn cần Capsule tàu bay để bay đến map này");
        return false;
    }

    public void confirmMenu(Message message) {
        try {
            if (isDead() || zone == null) {
                return;
            }
            if (isTrading()) {
                addInfo(INFO_RED, Language.CANCEL_ACTION_WHEN_TRADE);
                return;
            }
            int npcId = message.reader().readByte();
            int select = message.reader().readByte();
            npcController.confirmMenu(npcId, select);
        } catch (Exception ex) {
            logger.error("openNpc", ex);
        }
    }

    public void setService(Service service) {
        this.service = service;
    }

    public void setSession(Session session) {
        this.session = session;
    }

    public void join() {
        if (!isPlayer()) {
            return;
        }
        long now = System.currentTimeMillis();
        if (!Server.getInstance().isInterServer()) {
            GameRepository.getInstance().playerData.setOnline(this.id, true, new Timestamp(now));
        }
        Arrays.fill(lastUpdates, now);
        PlayerManager.getInstance().timesReceiveCapsuleDong.put(id, now + 60000);
        loadDisciple();
        if (disciple != null) {
            if (itemsBody[ItemType.TYPE_BONG_TAI] != null) {
                isFusion = true;
                typeFusion = 1;
                if (itemsBody[ItemType.TYPE_BONG_TAI].getUpgrade() > 0) {
                    typeFusion = 2;
                }
            } else if (isHaveEffect(EffectName.HOP_THE_TRAI_DAT_VA_SAYAIN, EffectName.HOP_THE_NAMEK)) {
                isFusion = true;
                typeFusion = 0;
            }
        }
        refreshPart();
        refreshInfo();
        service.setItemBag();
        service.setItemBox();
        service.setItemBody();
        service.setItemOther();
        service.setTaskMain();
        service.setIntrinsic();
        if (taskDaily != null) {
            service.setTaskDaily();
        }
        service.setLogin();
        for (Skill skill : skills) {
            if (skill.coolDownIntrinsic > 0) {
                service.setCoolDownIntrinsic(skill);
            }
        }
        Map map = null;
        if (hp > 0) {
            map = MapManager.getInstance().maps.get(mapId);
        } else {
            hp = 1;
        }
        if (itemsBody[15] != null) {
            pet = new Pet(this, itemsBody[15]);
        }
        if (map == null) {
            teleport(MapName.NUI_PAOZU, false);
        } else {
            joinMap(map, -1);
        }
        if (clan != null) {
            service.setClanInfo(false);
        }
        if (getTeam() != null) {
            service.teamInfo();
        }
        discipleJoin();
        petJoin();
        timeLogin = now;
        try {
            Item item = itemsBody[ItemType.TYPE_MEDAL];
            if (item != null) {
                if (item.template.id == ItemName.HUY_HIEU_TRUM_SERVER) {
                    Server.getInstance().service.serverChat(String.format("%s (Trùm Server) vừa online", this.name));
                }
                if (item.template.id == ItemName.HUY_HIEU_TRUM_SINH_TON) {
                    Server.getInstance().service.serverChat(String.format("%s (Trùm Sinh tồn) vừa online", this.name));
                }
                if (item.template.id == ItemName.HUY_HIEU_DAI_GIA) {
                    Server.getInstance().service.serverChat(String.format("%s (Đại gia Rồng Thần) vừa online", this.name));
                }
            }
        } catch (Exception ignored) {
        }
        if (!isAdmin()) {
            TopManager.getInstance().setObject(this);
        }
    }

    public void discipleJoin() {
        service.discipleInfo(MessageDiscipleInfoName.INFO);
        if (disciple != null) {
            service.discipleInfo(MessageDiscipleInfoName.BASE_INFO);
            service.discipleInfo(MessageDiscipleInfoName.SKILL_INFO);
            service.discipleInfo(MessageDiscipleInfoName.POWER_INFO);
            service.discipleInfo(MessageDiscipleInfoName.POINT);
            service.discipleInfo(MessageDiscipleInfoName.ITEM_BODY);
            service.discipleInfo(MessageDiscipleInfoName.ALL_PART);
            service.discipleInfo(MessageDiscipleInfoName.DISCIPLE_STATUS);
            service.discipleInfo(MessageDiscipleInfoName.STAMINA);
            service.discipleInfo(MessageDiscipleInfoName.ITEM_OTHER);
        }
    }

    public void petJoin() {
        service.petInfo(MessagePetInfoName.INFO);
        service.petInfo(MessagePetInfoName.ITEM_BODY);
        service.petInfo(MessagePetInfoName.STAMINA);
        service.petInfo(MessagePetInfoName.HP_DAMAGE);
        service.petInfo(MessagePetInfoName.EXP);
    }

    public void loadDisciple() {
        Optional<DiscipleData> discipleData = GameRepository.getInstance().discipleData.findById(-this.id);
        if (!discipleData.isPresent()) {
            disciple = null;
            return;
        }
        disciple = new Disciple(discipleData.get(), this);
        disciple.refreshPart();
        disciple.refreshInfo();
        disciple.hp = Math.max(1, disciple.hp);
    }

    public void joinMap(Map map, int areaId) {
        if (this.zone != null) {
            this.zone.leave(this);
        }
        Zone zone = map.findOrRandomZone(areaId);
        if (zone == null) {
            zone = map.findOrRandomZone(-1);
        }
        if (zone != null) {
            zone.enter(this);
        }
    }

    public void finishLoadMap() {
        List<Player> players = zone.getPlayers(Zone.TYPE_ALL);
        for (Player player : players) {
            if (player != this && !player.isHide) {
                service.addPlayer(player);
            }
        }
        Dragon dragon = Server.getInstance().dragon;
        if (dragon != null && dragon.isActive && dragon.zone == this.zone) {
            service.summonDragon(dragon, true);
        }
        dragon = Server.getInstance().dragonTet2024;
        if (dragon != null && dragon.isActive && dragon.zone == this.zone) {
            service.summonDragon(dragon, true);
        }
        if (this.typePk == 2 && zone != null && !zone.map.isSurvival()) {
            for (Player player : players) {
                if (player != this && player.typePk == 2 && player.isPlayer()) {
                    setTypePk(0);
                    break;
                }
            }
        }
        if (!isFirstMap && !Server.getInstance().isInterServer()) {
            isFirstMap = true;
            ((WeeklyAction) weeklyAction).init();
            List<String> notifications = new ArrayList<>(Server.getInstance().notifications);
            initReward();
            if (!rewards.isEmpty()) {
                notifications.add(String.format("Bạn có %d quà, hãy mở hộp quà để nhận", rewards.size()));
            }
            notifications.addAll(initOrder());
            if (zoneGoBack != null) {
                service.setAreaGoBack();
            }
            if (data.logoutTime != null) {
                try {
                    TrainingOfflineInfo info = Utils.gson.fromJson(data.trainingOffline, new TypeToken<TrainingOfflineInfo>() {
                    }.getType());
                    if (info.master > 0) {
                        long now = System.currentTimeMillis();
                        long time = (now - data.logoutTime.getTime()) / 1000;
                        if (time > 86400) {
                            time = 86400;
                        }
                        long exp = Math.max(1L, info.master * time);
                        upPower(exp);
                        notifications.add(String.format("Bạn đã tăng %s sức mạnh tiềm năng trong thời gian luyện tập %s phút", Utils.formatNumber(exp), Utils.getMoneys(time / 60)));
                    }
                    if (info.disciple > 0 && disciple != null) {
                        long now = System.currentTimeMillis();
                        long time = (now - data.logoutTime.getTime()) / 1000;
                        if (time > 86400) {
                            time = 86400;
                        }
                        long exp = Math.max(1L, info.disciple * time);
                        disciple.upPower(exp);
                        notifications.add(String.format("Đệ tử đã tăng %s sức mạnh tiềm năng trong thời gian luyện tập %s phút", Utils.formatNumber(exp), Utils.getMoneys(time / 60)));
                    }
                } catch (Exception ex) {
                    logger.error("training offline", ex);
                }
            }
            if (!notifications.isEmpty()) {
                service.showNotification(notifications);
            }
        }
        if (zoneGoBack != null && this.zone != null && isAutoPlay) {
            if (zoneGoBack.zone != null && zoneGoBack.zone.isRunning) {
                if (this.zone != zoneGoBack.zone) {
                    Utils.setTimeout(() -> {
                        if (zoneGoBack != null) {
                            Zone zone = zoneGoBack.zone;
                            if (zone != null && zone.isRunning) {
                                if (zone != this.zone) {
                                    this.x = zoneGoBack.x;
                                    this.y = zoneGoBack.y;
                                    zone.enter(this);
                                }
                            } else {
                                zoneGoBack = null;
                            }
                        }
                    }, 5000);
                }
            } else {
                zoneGoBack = null;
            }
        }
    }

    public void setGoBack() {
        if (this.zone == null) {
            return;
        }
        if (zoneGoBack != null) {
            setAreaGoBack(null);
        } else {
            if (zone.map.isExpansion() || zone.map.template.id == MapName.DAO_HOA_1 || zone.map.template.id == MapName.DAO_HOA_2) {
                addInfo(INFO_RED, String.format("Không thể lưu vị trí tại %s", zone.map.template.name));
                return;
            }
            if (!isHaveEffect(EffectName.TU_DONG_LUYEN_TAP)) {
                addInfo(INFO_RED, "Yêu cầu sử dụng Tự động luyện tập");
                return;
            }
            setAreaGoBack(new ZoneGoBack(this.zone, this.x, this.y));
            addInfo(INFO_YELLOW, String.format("Đã lưu %s khu vực %d", zone.map.template.name, zone.id));
        }
    }

    public void setAreaGoBack(ZoneGoBack zoneGoBack) {
        this.zoneGoBack = zoneGoBack;
        service.setAreaGoBack();
    }

    public void completeMissionWeek() {
        if (isRechargedInDay) {
            return;
        }
        isRechargedInDay = true;
        for (int i = 0; i < missionWeeks.size() - 1; i++) {
            IMission mission = missionWeeks.get(i);
            if (mission.getType() == MissionType.CHUA_HOAN_THANH) {
                mission.setType(MissionType.CHUA_NHAN_THUONG);
                if (i == missionWeeks.size() - 2) {
                    missionWeeks.get(missionWeeks.size() - 1).setType(MissionType.CHUA_NHAN_THUONG);
                }
                return;
            }
        }
    }

    public void resetMissionWeek() {
        missionWeeks.clear();
        for (MissionWeekTemplate template : MissionManager.getInstance().missionWeekTemplates.values()) {
            missionWeeks.add(new MissionWeek(template));
        }
    }

    public void upParamMissionDaily(int id, int point) {
        if (id < 0 || id >= missionDailies.size()) {
            return;
        }
        IMission mission = missionDailies.stream().filter(m -> m.getTemplateId() == id).findFirst().orElse(null);
        if (mission != null) {
            MissionDaily daily = (MissionDaily) mission;
            daily.param += point;
            if (daily.param >= daily.template.param && daily.type == MissionType.CHUA_HOAN_THANH) {
                daily.setType(MissionType.CHUA_NHAN_THUONG);
            }
        }
    }

    public void resetMissionDaily() {
        missionDailies.clear();
        for (MissionDailyTemplate template : MissionManager.getInstance().missionDailyTemplates.values()) {
            missionDailies.add(new MissionDaily(template));
        }
    }

    public void resetMissionRecharge() {
        missionRecharges.clear();
        for (MissionRechargeTemplate template : MissionManager.getInstance().missionRechargeTemplates.values()) {
            missionRecharges.add(new MissionRecharge(template));
        }
    }

    public void resetMissionEvent() {
        missionEvents.clear();
        for (MissionEventTemplate template : MissionManager.getInstance().missionEventTemplates.values()) {
            missionEvents.add(new MissionEvent(template));
        }
    }

    public void upParamMissionRecharge(int point) {
        for (IMission mission : missionRecharges) {
            MissionRecharge recharge = (MissionRecharge) mission;
            recharge.param += point;
            if (recharge.param >= recharge.template.param && recharge.type == MissionType.CHUA_HOAN_THANH) {
                recharge.setType(MissionType.CHUA_NHAN_THUONG);
            }
        }
    }

    public void upParamMissionEvent(int point) {
        for (IMission mission : missionEvents) {
            MissionEvent recharge = (MissionEvent) mission;
            recharge.param += point;
            if (recharge.param >= recharge.template.param && recharge.type == MissionType.CHUA_HOAN_THANH) {
                recharge.setType(MissionType.CHUA_NHAN_THUONG);
            }
        }
    }

    public boolean isFusion() {
        return isFusion || isHaveEffect(EffectName.HOP_THE_TRAI_DAT_VA_SAYAIN, EffectName.HOP_THE_NAMEK);
    }

    public boolean isFusion(List<Effect> effects) {
        return isFusion || isHaveEffect(effects, EffectName.HOP_THE_TRAI_DAT_VA_SAYAIN, EffectName.HOP_THE_NAMEK);
    }

    public boolean isLockMove() {
        return isDead() || isStun();
    }

    public void move(Message message) {
        lockMove.lock();
        try {
            if (isLockMove()) {
                return;
            }
            long now = System.currentTimeMillis();
            preX = x;
            preY = y;
            x = message.reader().readShort();
            y = message.reader().readShort();
            long time = now - lastTimeMove;
            lastTimeMove = now;
           /* Effect effect = findEffectByTemplateId(EffectTemplateName.TU_DONG_LUYEN_TAP);
            if (effect == null) {
                int distance = Math.abs(preX - x);
                long maxDistance = time + speed;
                if (distance > maxDistance) {
                    tickMoveMod++;
                    if (tickMoveMod > 20) {
                        session.disconnect();
                    }
                } else if (tickMoveMod > 0) {
                    tickMoveMod--;
                }

                *//*if (distance > 1500) {
                    tickMoveMod++;
                    if (tickMoveMod >= 100) {
                        Server.getInstance().service.lockUser(this.name, 10, "Dịch map", 7071);
                        return;
                    }
                }*//*
            }*/
            if (disciple != null && disciple.status == DiscipleStatus.FOLLOW) {
                disciple.move();
            }
            if (pet != null && !pet.isDead()) {
                pet.move();
            }
            if (escort != null && escort.follower == this) {
                int distance = Utils.getDistance(this, escort);
                if (distance > Escort.MAX_DISTANCE || escort.zone != this.zone) {
                    escort.stopFollow();
                }
            }
            zone.service.move(this);
            if ((x < 10 || x > zone.map.template.width - 10 || y < 0 || y > zone.map.template.height - 10) && zone.map.findWayPoint(x, y) == null) {
                x = preX;
                y = preY;
                zone.service.setPosition(this);
            }
        } catch (Exception e) {
            logger.error("move", e);
        } finally {
            lockMove.unlock();
        }
    }

    public boolean isCanJoinBarrack() {
        if (level < 10) {
            return false;
        }
        if (pointActive < 50) {
            return false;
        }
        if (countBarrack < 1) {
            return false;
        }
        int max = 8;
        if (level < 20) {
            max = 2;
        } else if (level <= 29) {
            max = 3;
        } else if (level <= 39) {
            max = 5;
        }
        for (int i = 0; i < 8; i++) {
            Item item = itemsBody[i];
            if (item == null || item.getUpgrade() < max) {
                return false;
            }
        }
        return true;
    }

    public void openBarrack() {
        if (!isCanJoinBarrack() || MapManager.getInstance().isContainPlayerInBarrack(this.id)) {
            addInfo(INFO_RED, "Bạn không đủ điều kiện để tham gia Bản doanh Red");
            return;
        }
        Team team = getTeam();
        if (team == null) {
            Barrack barrack = new Barrack(level, 1);
            countBarrack = 0;
            service.setCountBarrack();
            joinBarrack(barrack);
            return;
        }
        if (team.leaderId != this.id) {
            addInfo(INFO_RED, "Chức năng dành cho đội trưởng");
            return;
        }
        Zone zone = this.zone;
        int size_level = this.level / 10;
        int level = this.level;
        team.lock.readLock().lock();
        try {
            for (TeamMember member : team.members) {
                if (member.playerId != this.id) {
                    Player player = PlayerManager.getInstance().findPlayerById(member.playerId);
                    if (player == null) {
                        addInfo(INFO_RED, String.format("%s hiện đang offline", member.name));
                        return;
                    }
                    if (!player.isCanJoinBarrack() || MapManager.getInstance().isContainPlayerInBarrack(player.id)) {
                        addInfo(INFO_RED, String.format("%s không đủ điều kiện để tham gia Bản doanh Red", member.name));
                        return;
                    }
                    if (player.level / 10 != size_level) {
                        addInfo(INFO_RED, String.format("%s không cùng mốc cấp độ", member.name));
                        return;
                    }
                    if (player.level > level) {
                        level = player.level;
                    }
                }
            }
            Barrack barrack = new Barrack(level, team.members.size());
            for (TeamMember member : team.members) {
                Player player = PlayerManager.getInstance().findPlayerById(member.playerId);
                if (player != null) {
                    barrack.addPlayer(player);
                    player.countBarrack = 0;
                    player.service.setCountBarrack();
                    player.addInfo(INFO_YELLOW, String.format("%s đã mở cửa Bản doanh Red", this.name));
                    if (player.zone == zone) {
                        player.joinBarrack(barrack);
                    }
                }
            }
        } finally {
            team.lock.readLock().unlock();
        }
    }

    public void joinBarrack(Barrack barrack) {
        if (barrack.isClose) {
            addInfo(INFO_RED, "Cổng Bản doanh đã đóng");
            return;
        }
        barrack.addPlayer(this);
        Map map = barrack.findMap(MapName.CONG_BAN_DOANH);
        x = 90;
        y = 648;
        joinMap(map, -1);
        /*long time = barrack.endTime - System.currentTimeMillis();
        if (time > 0) {
            service.setTimeRemaining(time);
        }*/
    }

    public void requestInfoPlayer(Message message) {
        try {
            viewInfoPlayer(message.reader().readInt());
        } catch (Exception ex) {
            logger.error("requestInfoPlayer", ex);
        }
    }

    public void blockStrangers() {
        isBlockStrangers = !isBlockStrangers;
        service.blockStrangers();
    }

    public void setHideMark() {
        isHideMark = !isHideMark;
        service.setHideMark();
        service.setPart();
        if (zone != null) {
            zone.service.refreshPlayerPart(this);
        }
    }

    public boolean isCanSendAction(Player focus) {
        if (focus == null) {
            return false;
        }
        if (!focus.isBlockStrangers) {
            return true;
        }
        for (Friend friend : focus.friends) {
            if (friend.playerId == this.id) {
                return true;
            }
        }
        return false;
    }

    public void viewInfoPlayer(int playerId) {
        Player player = PlayerManager.getInstance().findPlayerById(Math.abs(playerId));
        if (player == null) {
            addInfo(INFO_RED, "Hiện tại người chơi không online");
            return;
        }
        service.viewInfoPlayer(player);
        if (playerId < 0) {
            player.addInfo(Player.INFO_YELLOW, String.format("%s đang xem thông tin đệ tử của bạn", name));
        } else {
            player.addInfo(Player.INFO_YELLOW, String.format("%s đang xem thông tin của bạn", name));
        }
    }

    public void requestChangeMap() {
        lock.lock();
        try {
            if (isDead()) {
                return;
            }
            String notification = "Bạn chưa thể đến khu vực này";
            WayPoint wayPoint = zone.map.findWayPoint(x, y);
            if (wayPoint == null) {
                addInfo(INFO_RED, notification);
                x = preX;
                y = preY;
                zone.service.setPosition(this);
                return;
            }
            Map map = MapManager.getInstance().maps.get(wayPoint.goMap);
            if (MapManager.getInstance().isMapBarrack(wayPoint.goMap) && isInBarrack()) {
                Barrack barrack = getBarrack();
                map = barrack.findMap(wayPoint.goMap);
                if (!zone.map.isFinish && zone.map.template.id < map.template.id) {
                    map = null;
                }
                if (map != null && map.template.id == MapName.TUONG_THANH_1 && (!barrack.findMap(MapName.PHONG_DIEU_KHIEN).isFinish || !barrack.findMap(MapName.NHA_KHO).isFinish)) {
                    map = null;
                }
                if (map == null) {
                    notification = "Khu vực chưa được mở";
                }
            }
            if (clan != null && clan.manor != null && MapManager.getInstance().isMapManor(wayPoint.goMap) && isInManor()) {
                Manor manor = clan.manor;
                map = manor.findMap(wayPoint.goMap);
                if (!zone.map.isFinish && zone.map.template.id < map.template.id) {
                    map = null;
                }
                if (map != null && map.template.id == MapName.CUA_LUA_5 && (!manor.findMap(MapName.CUA_LUA_3).isFinish || !manor.findMap(MapName.CUA_LUA_4).isFinish)) {
                    map = null;
                }
                if (map != null && map.template.id == MapName.CUA_BANG_5 && (!manor.findMap(MapName.CUA_BANG_3).isFinish || !manor.findMap(MapName.CUA_BANG_4).isFinish)) {
                    map = null;
                }
                if (map == null) {
                    notification = "Khu vực chưa được mở";
                }
            }
            if (isInSurvival() && MapManager.getInstance().survival != null) {
                map = MapManager.getInstance().survival.maps.stream().filter(m -> m.template.id == wayPoint.goMap).findFirst().orElse(null);
                if (map != null && map.zones.stream().allMatch(area -> ((ZoneSurvival) area).isRedZone)) {
                    map = null;
                    notification = "Khu vực đã đóng";
                }
            }
            if (isInTreasure() && MapManager.getInstance().treasure != null) {
                Treasure treasure = MapManager.getInstance().treasure;
                if (treasure.state == ExpansionState.WAIT_REG) {
                    map = null;
                    notification = "Trận chiến chưa bắt đầu";
                } else {
                    map = treasure.maps.stream().filter(m -> m.template.id == wayPoint.goMap).findFirst().orElse(null);
                    if (map != null) {
                        Pirate pirate = treasure.findPirateByPlayerId(this.id);
                        if (pirate == null) {
                            map = null;
                        } else {
                            pirate.point = 0;
                            long now = System.currentTimeMillis();
                            long time = Math.max(pirate.timeChangeMap, timeDie) + 10000 - now;
                            if (time > 0) {
                                map = null;
                                notification = String.format("Vui lòng chờ %s", Utils.formatTime(time));
                            } else {
                                pirate.timeChangeMap = now;
                                if (map == treasure.maps.get(2)) {
                                    long count = map.zones.get(0).getPlayers(Zone.TYPE_PLAYER).stream().filter(p -> p.typeFlag == pirate.flag).count();
                                    if (count > map.template.maxPlayer / 2) {
                                        map = null;
                                        notification = "Khu vực đã đầy";
                                        pirate.timeChangeMap = now - 5000;
                                    }
                                } else {
                                    int indexFlag = -1;
                                    if (map == treasure.maps.get(0)) {
                                        indexFlag = 0;
                                    } else if (map == treasure.maps.get(1)) {
                                        indexFlag = 1;
                                    }
                                    if (indexFlag != -1 && typeFlag != Treasure.FLAGS[indexFlag]) {
                                        map = null;
                                        notification = "Bạn không thể vào khu vực riêng của " + (indexFlag == 0 ? "phe Đỏ" : "phe Xanh");
                                    }
                                }
                            }

                        }
                    }
                }
            }
            if (isInIsland() && MapManager.getInstance().island != null) {
                map = MapManager.getInstance().island.maps.stream().filter(m -> m.template.id == wayPoint.goMap).findFirst().orElse(null);
            }
            if (map != null && !isCanJoinMap(map)) {
                map = null;
            }
            if (map == null) {
                if (wayPoint.type == WayPointType.CUOI_MAP || wayPoint.type == WayPointType.DAU_MAP) {
                    x = wayPoint.x + (90 * (wayPoint.type == WayPointType.CUOI_MAP ? (-1) : 1));
                    y = wayPoint.y;
                    zone.service.setPosition(this);
                } else if (wayPoint.type == WayPointType.GIUA_MAP) {
                    x = wayPoint.x;
                    y = wayPoint.y;
                    zone.service.setPosition(this);
                }
                addInfo(INFO_RED, notification);
                return;
            }
            preX = x = wayPoint.goX;
            preY = y = wayPoint.goY;
            Zone lastZone = this.zone;
            joinMap(map, -1);
            if (escort != null && escort.follower == this) {
                int distance = Utils.getDistance(escort.x, escort.y, wayPoint.x, wayPoint.y);
                if (distance > Escort.MAX_DISTANCE || escort.zone != lastZone) {
                    escort.stopFollow();
                } else {
                    escort.x = this.x;
                    escort.y = this.y;
                    zone.enter(escort);
                    if (escort.isEnd()) {
                        escort.end();
                    }
                }
            }
        } finally {
            lock.unlock();
        }
    }

    public void requestChangeZone(Message message) {
        lock.lock();
        try {
            if (isDead() || isStun() || isInSpaceship()) {
                addInfo(INFO_RED, Language.CANT_ACTION);
                return;
            }
            int areaId = message.reader().readByte();
            Map map = this.zone.map;
            if (areaId < 0 || areaId >= map.zones.size() || areaId == this.zone.id) {
                return;
            }
            long now = System.currentTimeMillis();
            if (!isAdmin()) {
                if (map.isCantChangeArea()) {
                    addInfo(INFO_RED, Language.CANT_ACTION);
                    return;
                }
                long delay = 10000L;
                if ((map.template.id == MapName.DAI_HOI_VO_THUAT && isAdminMartialArtsFestival()) || (map.isSurvival() && isAdminSurvival())) {
                    delay = 2000L;
                }
                long time = delay + lastTimeChangeArea - now;
                if (time > 0) {
                    addInfo(INFO_RED, String.format("Bạn chỉ có thể đổi khu sau %s", Utils.formatTime(time)));
                    return;
                }
            }
            lastTimeChangeArea = now;
            Zone zone = map.zones.get(areaId);
            if (!isAdmin()) {
                List<Player> players = zone.getPlayers(Zone.TYPE_PLAYER);
                if (players.size() >= map.template.maxPlayer) {
                    addInfo(INFO_RED, "Khu vực đã đầy, vui lòng thử lại sau");
                    return;
                }
                if (taskMain != null) {
                    List<Player> bossList = zone.getPlayers(Zone.TYPE_BOSS);
                    if (!bossList.isEmpty()) {
                        boolean flag = Server.getInstance().isHourSupportTask();
                        for (Player player : bossList) {
                            Boss boss = (Boss) player;
                            if ((boss.taskId != -1 && (taskMain.template.id != boss.taskId || (boss.taskIndex != -1 && taskMain.index != boss.taskIndex)))) {
                                addInfo(INFO_RED, "Bạn không thể vào khu Boss nhiệm vụ, vui lòng chọn khu khác");
                                return;
                            }
                            if (flag && (!boss.isTask || taskMain.template.steps[taskMain.index].bossId == boss.template.id)) {
                                flag = false;
                            }
                        }
                        if (flag) {
                            addInfo(INFO_RED, "Đang trong khung giờ hỗ trợ nhiệm vụ, bạn không thể vào khu vực này");
                            return;
                        }
                    }
                }
            }
            if (isInSurvival()) {
                ZoneSurvival areaSurvival = (ZoneSurvival) zone;
                if (areaSurvival.isRedZone) {
                    addInfo(INFO_RED, "Khu vực đã đóng, vui lòng chọn khu vực khác");
                    return;
                }
                areaSurvival.lockTimeAddPlayer.writeLock().lock();
                try {
                    long time = areaSurvival.timeAddPlayer + 1000 - now;
                    if (time > 0) {
                        lastTimeChangeArea = now - 9000;
                        addInfo(INFO_RED, String.format("Vui lòng thử lại sau %s", Utils.formatTime(time)));
                        return;
                    }
                    areaSurvival.timeAddPlayer = now;
                } finally {
                    areaSurvival.lockTimeAddPlayer.writeLock().unlock();
                }
            }
            zone.enter(this);
            if (escort != null && escort.follower == this && escort.zone != null && escort.zone.map.template.id == zone.map.template.id) {
                zone.enter(escort);
            }
        } catch (Exception ex) {
            logger.error("requestChangeZone", ex);
        } finally {
            lock.unlock();
        }
    }

    public void clearMap() {
        if (typePk == 1) {
            Player player = zone.findPlayerById(testPlayerId);
            if (player != null) {
                player.clearPk();
                player.resultSolo(2);
            }
            clearPk();
            resultSolo(3);
        }
        if (bagLoot != -1) {
            bagLoot = -1;
            bag = -1;
            if (zone != null) {
                zone.service.setBag(this);
            }
        }
        tabID = -1;
        zone = null;
        if (commands != null) {
            commands.clear();
        }
        intrinsicAttack = null;
    }

    @Override
    public List<ItemMap> throwItem(Entity killer) {
        List<ItemMap> itemMaps = new ArrayList<>();
        if (isPlayer() && isInSurvival() && itemsSurvival != null) {
            for (Item item : itemsSurvival) {
                if (item != null) {
                    itemMaps.add(item.cloneItemMap());
                }
            }
        }
        if (isBoss() && killer != null) {
            int xu = level * 1000;
            if (xu < 10000) {
                xu = 10000;
            }
            itemMaps.add(ItemManager.getInstance().createItemMap(ItemName.XU_KHOA, Utils.nextInt(x - xu / 10, xu), killer.id));
        }
        return itemMaps;
    }

    @Override
    public void addEffect(Effect effect) {
        if (effect.template.id == EffectName.CHOANG_THAI_DUONG_HA_SAN || effect.template.id == EffectName.SOCOLA || effect.template.id == EffectName.DONG_BANG || effect.template.id == EffectName.DICH_CHUYEN_TUC_THOI || effect.template.id == EffectName.HOA_SOCOLA || effect.template.id == EffectName.HOA_DA) {
            List<Effect> effectList = getEffects();
            if (isHaveEffect(effectList, EffectName.HOA_KHONG_LO, EffectName.THOI_MIEN, EffectName.HOA_DA)) {
                return;
            }
            if (effect.template.id == EffectName.CHOANG_THAI_DUONG_HA_SAN && isHaveEffect(effectList, EffectName.KHANG_THAI_DUONG_HA_SAN_KINH_RAM) && !zone.map.isSurvival()) {
                effect.time /= 2;
            }
            if (options[OptionName.GIAM_HIEU_UNG_KHONG_CHE] > 0) {
                effect.time -= effect.time * options[OptionName.GIAM_HIEU_UNG_KHONG_CHE] / 100;
                effect.endTime = System.currentTimeMillis() + effect.time;
            }
        }
        super.addEffect(effect);
        service.addEffect(effect);
        if (effect.template.isRefreshPart) {
            service.setPart();
            if (zone != null) {
                zone.service.refreshPlayerPart(this);
            }
        }
    }

    public void addTimeEffect(Effect effect) {
        lockEffects.writeLock().lock();
        try {
            Effect eff = effects.stream().filter(e -> e.template.id == effect.template.id).findFirst().orElse(null);
            if (eff != null) {
                eff.lock.lock();
                try {
                    if (eff.time + effect.time > 86400000) {
                        effect.time = 86400000 - eff.time;
                    }
                    eff.time += effect.time;
                    eff.endTime += effect.time;
                    service.setTimeEffect(eff);
                } finally {
                    eff.lock.unlock();
                }
            } else {
                effects.add(effect);
                service.addEffect(effect);
            }
        } finally {
            lockEffects.writeLock().unlock();
        }
    }

    public void addTimeEffect(int id, long time) {
        lockEffects.writeLock().lock();
        try {
            Effect eff = effects.stream().filter(e -> e.template.id == id).findFirst().orElse(null);
            if (eff != null) {
                eff.lock.lock();
                try {
                    if (eff.template.id != EffectName.QUA_TRUNG_DE_TU && eff.time + time > 86400000) {
                        time = 86400000 - eff.time;
                    }
                    eff.time += time;
                    eff.endTime += time;
                    service.setTimeEffect(eff);
                } finally {
                    eff.lock.unlock();
                }
            }
        } finally {
            lockEffects.writeLock().unlock();
        }
    }

    @Override
    public void injure(Entity attacker, long hpInjure, boolean isCritical, boolean isStrikeBack) {
        boolean isHapThu = false;
        lock.lock();
        try {
            if (isDead()) {
                return;
            }
            long dmg = hpInjure;
            if (options[117] > 0 && attacker != null && attacker.isPlayer()) {
                Player player = (Player) attacker;
                if (player.mySkill != null && player.mySkill.isCanHapThu()) {
                    dmg -= dmg * options[117] / 100;
                    if (dmg < 0) {
                        dmg = 0;
                    }
                    isHapThu = true;
                }
            }
            hp -= dmg;
            if (zone != null) {
                zone.service.playerInjure(this, dmg, isCritical);
            }
            if (hp <= 0) {
                startDie(attacker);
            }
        } finally {
            lock.unlock();
        }
        if (attacker != null && isStrikeBack && options[OptionName.PHAN_SAT_THUONG] > 0 && hpInjure > 0 && !attacker.isBoss() && !(attacker instanceof BigMonster)) {
            long damage = Utils.statsOfPoint(hpInjure, options[OptionName.PHAN_SAT_THUONG]);
            if (attacker.options[153] > 0) {
                damage -= Utils.percentOf(damage, attacker.options[153]);
            }
            if (damage > 0) {
                // phản sát thương
                attacker.injure(this, damage, false, false);
            }
        }
        if (attacker != null && !isInSurvival()) {
            if (disciple != null && (attacker.isPlayer() || attacker.isDisciple() || attacker.isBoss())) {
                disciple.addEnemy((Player) attacker);
            }
            if (isDead() && isPlayer() && attacker.isPlayer()) {
                addEnemy((Player) attacker);
            }
            if (attacker.isBoss() && isHaveEffect(EffectName.TAI_TAO_NANG_LUONG)) {
                Skill skill = skills.get(3);
                if (skill != null && skill.upgrade > 0) {
                    upPointUpgradeSkill(skill, 5);
                }
            }
        }
        if (isHapThu && !isDead() && options[117] > 0) {
            long recovery = options[117] * hpInjure / 100;
            if (recovery > 0) {
                recovery(RECOVERY_ALL, recovery);
            }
        }
    }

    @Override
    public void startDie(Entity killer) {
        Arrays.fill(timeUpdateEffectOptionItem, timeDie + Utils.nextInt(2000, 3000));
        hp = 0;
        isDie = true;
        y = zone.map.getYSd(x);
        service.startDie();
        zone.service.playerDie(this);
        super.startDie(killer);
        if (isPlayer()) {
            if (typePk == 1) {
                Player playerSolo = zone.findPlayerById(testPlayerId);
                if (playerSolo != null) {
                    playerSolo.clearPk();
                    playerSolo.resultSolo(0);
                    if (playerSolo.taskDaily != null && playerSolo.taskDaily.type == TaskDailyType.KILL_PLAYER) {
                        playerSolo.upTaskDailyParam(1);
                    }
                    if (playerSolo.taskMain != null && playerSolo.taskMain.template.id == 11 && playerSolo.taskMain.index == 1) {
                        playerSolo.nextTaskParam();
                    }
                    EnemyAction action = (EnemyAction) playerSolo.enemyAction;
                    if (action.targetId == this.id) {
                        playerSolo.removeEnemy(this.id);
                    }
                }
                clearPk();
                resultSolo(1);
            }
            if (zoneGoBack != null) {
                Utils.setTimeout(() -> {
                    if (zoneGoBack != null && isDead()) {
                        returnTownFromDead();
                    }
                }, 2000);
            }
            if (escort != null) {
                escort.stopFollow();
            }
            if (zone instanceof ZoneSpaceship && killer != null && killer.isPlayer() && killer.zone == zone) {
                Player player = (Player) killer;
                player.pointCurrSpaceship++;
                player.pointSpaceship++;
                ((ZoneSpaceship) zone).setEnergy(player);
            }
        }
        if (isPlayer() || isDisciple()) {
            if (killer != null && killer.isPlayer() && !isInSurvival()) {
                Player player = (Player) killer;
                if (player.typePk == 2) {
                    player.pointPk++;
                    player.service.setPointPk();
                    player.addInfo(INFO_RED, String.format("Điểm hiếu chiến của bạn là %d/5", player.pointPk));
                    player.addInfo(INFO_RED, "Bạn sẽ bị chặn các tính năng của trò chơi nếu hiếu chiến đạt tối đa");
                    if (!player.isDead() && player.isInIsland()) {
                        player.teleport(MapName.NUI_PAOZU, true);
                    }
                }
            }
        }
        if (isInDragonBallNamek()) {
            ZoneDragonBallNamek areaDragonBallNamek = (ZoneDragonBallNamek) this.zone;
            if (areaDragonBallNamek.holder != null && areaDragonBallNamek.holder.id == this.id) {
                areaDragonBallNamek.setHolder(null);
                bag = -1;
                zone.service.setBag(this);
            }
        }
        if (zone instanceof ZoneFlagWar zoneFlagWar) {
            if (zoneFlagWar.holder != null && zoneFlagWar.holder.id == this.id) {
                zoneFlagWar.setHolder(null);
                bag = -1;
                zone.service.setBag(this);
            }
            if (killer instanceof Pet miniPet) {
                zoneFlagWar.upPoint(miniPet.master, 1);
            }
            if (killer instanceof Player player) {
                zoneFlagWar.upPoint(player, 1);
            }
        }
        if (isInTreasure() && killer != null && killer.isPlayer()) {
            Treasure treasure = MapManager.getInstance().treasure;
            if (treasure != null) {
                Pirate pirate = treasure.findPirateByPlayerId(killer.id);
                if (pirate != null) {
                    pirate.upPoint(5);
                }
            }
        }
    }

    @Override
    public boolean isDodge(Entity attacker, int accurate) {
        int dodge = options[OptionName.NE_DON] + accurate + getEffectParam(EffectName.NE_DON_TU_TRI_THUONG);
        if (dodge < 0) {
            return false;
        }
        dodge = dodge * 100 / (Math.abs(dodge) + 10000);
        if (attacker != null) {
            dodge -= attacker.options[184];
        }
        return Utils.isPercent(dodge);
    }


    @Override
    public boolean isPlayer() {
        return true;
    }

    @Override
    public boolean isMonster() {
        return false;
    }

    @Override
    public boolean isBoss() {
        return false;
    }

    @Override
    public boolean isDisciple() {
        return false;
    }

    @Override
    public boolean isPet() {
        return false;
    }

    public void attack(Message message) {
        lockAttack.lock();
        try {
            if (mySkill == null || zone == null || mySkill.template.id != skillUseId || zone != mySkill.zone) {
                return;
            }
            byte type = message.reader().readByte();
            if (type != -1) {
                if (focus == null) {
                    return;
                }
                int focusId = message.reader().readInt();
                if (focus.id != focusId) {
                    return;
                }
                if (type == 0 && !(focus instanceof Player)) {
                    return;
                }
                if (type == 1 && !(focus instanceof Monster)) {
                    return;
                }
            }
            lastTimeAttack = System.currentTimeMillis();
            switch (mySkill.template.id) {
                case SkillName.KARAK:
                case SkillName.KARAP:
                case SkillName.KARAV:
                case SkillName.KAME:
                case SkillName.MASENDAN:
                case SkillName.SOKIDAN:
                case SkillName.SUPER_KAME:
                case SkillName.MA_PHONG_BA:
                case SkillName.BIGBANG_FLASH: {
                    if (type == 0) {
                        attackPlayer((Player) focus);
                        if (options[167] > 0 && Utils.isPercent(options[167])) {
                            attackPlayer((Player) focus);
                        }
                    } else {
                        attackMonster((Monster) focus);
                        if (options[167] > 0 && Utils.isPercent(options[167])) {
                            attackMonster((Monster) focus);
                        }
                    }
                    if (pet != null && pet.stamina > 0) {
                        pet.attack(focus);
                    }
                    if (mySkill.template.id == SkillName.KAME || mySkill.template.id == SkillName.MASENDAN || mySkill.template.id == SkillName.SOKIDAN) {
                        upPointAchievement(19, 1);
                    }
                    break;
                }

                case SkillName.DICH_CHUYEN_TUC_THOI: {
                    focus.addEffect(new Effect(focus, EffectName.DICH_CHUYEN_TUC_THOI, mySkill.getParam(10)));
                    long dame = damage * 2 + damage * mySkill.level / 5;
                    dame = focus.formatDamageInjure(this, dame, false);
                    if (focus instanceof Monster && damage >= focus.hp && focus.hp == focus.maxHp) {
                        dame = focus.hp - 1;
                    }
                    focus.injure(this, dame, false, true);
                    if (isPlayer()) {
                        Intrinsic intrinsic = intrinsics.get(15);
                        if (intrinsic != null && intrinsic.param > 0) {
                            intrinsicAttack = new IntrinsicAttack(focus, intrinsic.param);
                        }
                    }
                    break;
                }

                case SkillName.THOI_MIEN: {
                    long time = mySkill.getParam(30);
                    focus.addEffect(new Effect(focus, EffectName.THOI_MIEN, time));
                    focus.addEffect(new Effect(focus, EffectName.BI_GIAM_DAMAGE_TU_SOCOLA, time + 5000, 0, mySkill.getParam(12)));
                    if (isPlayer()) {
                        Intrinsic intrinsic = intrinsics.get(15);
                        if (intrinsic != null && intrinsic.param > 0) {
                            intrinsicAttack = new IntrinsicAttack(focus, intrinsic.param);
                        }
                    }
                    break;
                }

                case SkillName.TRI_THUONG: {
                    if (type != 0) {
                        break;
                    }
                    Player target = (Player) this.focus;
                    if (!isCanAttackEntity(target)) {
                        break;
                    }
                    int[] param = new int[]{mySkill.getParam(13), mySkill.getParam(14)};
                    recovery(RECOVERY_HP, param[0], true);
                    addEffect(new Effect(this, EffectName.NE_DON_TU_TRI_THUONG, 5000, 0, param[1]));
                    List<Player> players = zone.getPlayers(Zone.TYPE_PLAYER, Zone.TYPE_DISCIPLE);
                    int point = 0;
                    for (Player player : players) {
                        if (player != this && player.zone == this.zone && player.typePk != 1) {
                            if (player.isDead()) {
                                if (!isInSurvival()) {
                                    player.lock.lock();
                                    try {
                                        if (player.isDead()) {
                                            player.hp = player.maxHp;
                                            player.mp = player.maxMp;
                                            player.isDie = false;
                                            player.service.wakeUpFromDead();
                                            zone.service.playerWakeUpFromDead(player);
                                            point++;
                                        }
                                    } finally {
                                        player.lock.unlock();
                                    }
                                }
                            } else {
                                player.recovery(RECOVERY_ALL, param[0], true);
                            }
                            if (player == target || (player.teamId != -1 && player.teamId == this.teamId)) {
                                player.addEffect(new Effect(player, EffectName.NE_DON_TU_TRI_THUONG, 5000, 0, param[1]));
                                point++;
                            }
                        }
                    }
                    if (point > 0) {
                        upPointUpgradeSkill(mySkill, point);
                    }
                    break;
                }

                case SkillName.TU_PHAT_NO: {
                    if (!isCanUseSkillCharge) {
                        break;
                    }
                    long damage = getDamageWhenAttack(null, false);
                    List<Monster> monsters = zone.getMonsters();
                    for (Monster monster : monsters) {
                        if (!monster.isDead() && Utils.getDistance(x, y, monster.x, monster.y) < mySkill.getDx() * 2) {
                            long dmg = monster.formatDamageInjure(this, damage, false);
                            monster.injure(this, dmg, false, false);
                        }
                    }
                    List<Player> players = zone.getPlayers(Zone.TYPE_ALL);
                    for (Player player : players) {
                        if (player != this && player.zone == this.zone && isCanAttackEntity(player) && Utils.getDistance(x, y, player.x, player.y) < mySkill.getDx() * 2) {
                            long dmg = player.formatDamageInjure(this, damage, false);
                            player.injure(this, dmg, false, false);
                        }
                    }
                    startDie(null);
                    break;
                }

                case SkillName.LAZE: {
                    if (!isCanUseSkillCharge) {
                        break;
                    }
                    Entity entity = this.focus;
                    if (!isCanAttackEntity(entity)) {
                        break;
                    }
                    long damage = getDamageWhenAttack(entity, false);
                    if (entity instanceof Player) {
                        Player player = (Player) entity;
                        if (player.isDodge(this, -options[OptionName.CHINH_XAC])) {
                            player.injure(this, 0, false, false);
                            break;
                        }
                    }
                    damage = entity.formatDamageInjure(this, damage, false);
                    entity.injure(this, damage, false, true);
                    addEffect(new Effect(this, EffectName.TANG_PHAN_TRAM_SUC_DANH_TU_LAZE, 5000, 0, mySkill.getParam(22)));
                    break;
                }

                case SkillName.QUA_CAU_GENKI: {
                    if (!isCanUseSkillCharge) {
                        break;
                    }
                    Entity entity = this.focus;
                    if (!isCanAttackEntity(entity)) {
                        break;
                    }
                    if (entity instanceof Player) {
                        Player player = (Player) entity;
                        if (player.isDodge(this, -options[OptionName.CHINH_XAC])) {
                            player.injure(this, 0, false, false);
                            break;
                        }
                    }
                    boolean isCritical = false;
                    long damage = getDamageWhenAttack(entity, isCritical);
                    damage = entity.formatDamageInjure(this, damage, isCritical);
                    entity.injure(this, damage, isCritical, true);
                    List<Monster> monsters = zone.getMonsters();
                    for (Monster monster : monsters) {
                        if (!monster.isDead() && Utils.getDistance(x, y, monster.x, monster.y) < mySkill.getDx() * 2) {
                            long dmg = monster.formatDamageInjure(this, damage, isCritical);
                            monster.injure(this, dmg, isCritical, false);
                        }
                    }
                    break;
                }
            }
        } catch (Exception ex) {
            logger.error("attack", ex);
        } finally {
            skillUseId = -1;
            isCanUseSkillCharge = false;
            lockAttack.unlock();
        }
    }

    public void useSkill(Skill skill, Entity focus) {
        lockAttack.lock();
        try {
            if (isStun() || isDead() || zone == null || isCharge) {
                return;
            }
            if (pointPk >= 5) {
                addInfo(INFO_RED, "Điểm hiếu chiến quá cao");
                return;
            }
            mySkill = skill;
            if (skill.template.optionRequire != -1 && skill.upgrade < 5 && options[skill.template.optionRequire] == 0) {
                addInfo(INFO_RED, "Cần phải sử dụng trang bị hỗ trợ sử dụng kỹ năng này");
                return;
            }
            long now = System.currentTimeMillis();
            if (now < mySkill.timeCanUse) {
                return;
            }
            long manaUse = getManaUseSkill();
            if (mp < manaUse) {
                addInfo(INFO_RED, "Không đủ KI để sử dụng");
                return;
            }
            mySkill.timeCanUse = now + mySkill.getCoolDown();
            mp -= manaUse;
            skill.zone = this.zone;
            skillUseId = skill.template.id;
            this.focus = focus;
            zone.service.useSkill(this);
            if (pet != null && pet.stamina > 0 && focus != null && (skill.template.id == SkillName.KARAK || skill.template.id == SkillName.KARAP || skill.template.id == SkillName.KARAV || skill.template.id == SkillName.KAME || skill.template.id == SkillName.MASENDAN || skill.template.id == SkillName.SOKIDAN)) {
                zone.service.monsterAttack(pet, focus, -1);
            }
            switch (skill.template.id) {

                case SkillName.HUT_DAY: {
                    if (focus instanceof Player player) {
                        int dis = Utils.getDistance(this, focus);
                        if (dis > 500) {
                            focus.x = this.x;
                            focus.y = this.y;
                        } else {
                            if (focus.x < this.x) {
                                if (focus.x > 590) {
                                    focus.x -= 500;
                                } else {
                                    focus.x = 90;
                                }
                            } else {
                                int maxTX = zone.map.template.width - 590;
                                if (focus.x < maxTX) {
                                    focus.x += 500;
                                } else {
                                    focus.x = maxTX + 500;
                                }
                            }
                        }
                        zone.service.setPosition(player);
                        focus.addEffect(new Effect(this, EffectName.CHOANG_HUT_DAY, skill.getParam(10)));
                    }
                    break;
                }

                case SkillName.KHIEN_NANG_LUONG: {
                    zone.service.useSkill(this);
                    int param = skill.getParam(28);
                    Utils.setTimeout(() -> {
                        if (zone != null && zone == skill.zone && !isDead()) {
                            addEffect(new Effect(this, EffectName.KHIEN_NANG_LUONG, param));
                        }
                    }, 50);
                    break;
                }

                case SkillName.DICH_CHUYEN_TUC_THOI: {
                    if (focus != null) {
                        x = focus.x;
                        y = focus.y;
                        zone.service.setPosition(this);
                    }
                    break;
                }

                case SkillName.KAIOKEN: {
                    int[] param = {skill.getParam(37), skill.getParam(36), skill.getParam(35)};
                    Utils.setTimeout(() -> {
                        if (zone != null && zone == skill.zone && !isDead()) {
                            Effect effect = new Effect(this, EffectName.KAIOKEN, param[0], 1000, param[1]);
                            effect.paramOption = param[2];
                            addEffect(effect);
                            service.setInfo();
                            zone.service.refreshHp(this);
                        }
                    }, 50);
                    break;
                }

                case SkillName.HOA_KHONG_LO: {
                    int param = skill.getParam(8);
                    Utils.setTimeout(() -> {
                        if (zone != null && zone == skill.zone && !isDead()) {
                            addEffect(new Effect(this, EffectName.HOA_KHONG_LO, param * 1000L));
                            service.setPart();
                            service.setInfo();
                            zone.service.refreshPlayerPart(this);
                            zone.service.refreshHp(this);
                        }
                    }, 1000);
                    break;
                }

                case SkillName.BIEN_KHI: {
                    int param = skill.getParam(8);
                    int perHp = skill.getParam(33);
                    Utils.setTimeout(() -> {
                        if (zone != null && zone == skill.zone && !isDead()) {
                            Effect effect = new Effect(this, EffectName.BIEN_KHI, param * 1000L, 0, perHp);
                            effect.paramOption = skill.upgrade >= 5 ? 1 : 0;
                            addEffect(effect);
                            service.setPart();
                            service.setInfo();
                            zone.service.refreshPlayerPart(this);
                            zone.service.refreshHp(this);
                        }
                    }, 1000);
                    break;
                }

                case SkillName.TAI_TAO_NANG_LUONG: {
                    zone.service.useSkill(this);
                    int param = skill.getParam(7);
                    Utils.setTimeout(() -> {
                        if (zone != null && zone == skill.zone && !isDead()) {
                            addEffect(new Effect(this, EffectName.TAI_TAO_NANG_LUONG, 5100, 1000, param));
                        }
                    }, 50);
                    break;
                }

                case SkillName.THAI_DUONG_HA_SAN: {
                    int param = skill.getParam(10);
                    int dx = skill.getDx();
                    Utils.setTimeout(() -> {
                        if (zone != null && zone == skill.zone && !isDead()) {
                            int point = 0;
                            List<Monster> monsters = zone.getMonsters();
                            for (Monster monster : monsters) {
                                if (!monster.isDead() && Utils.getDistance(x, y, monster.x, monster.y) < dx * 2) {
                                    monster.addEffect(new Effect(monster, EffectName.CHOANG_THAI_DUONG_HA_SAN, param));
                                    if (Math.abs(monster.level - level) < 4) {
                                        point++;
                                    }
                                }
                            }
                            boolean isDhvt = zone.map.template.id == MapName.DAI_HOI_VO_THUAT;
                            List<Player> players = zone.getPlayers(Zone.TYPE_ALL);
                            for (Player player : players) {
                                if (player != this && player.zone == this.zone && isCanAttackEntity(player) && Utils.getDistance(x, y, player.x, player.y) < dx * 2) {
                                    player.addEffect(new Effect(player, EffectName.CHOANG_THAI_DUONG_HA_SAN, param));
                                    if (player.isBoss()) {
                                        point += 3;
                                    } else if (isDhvt && player.isPlayer()) {
                                        point++;
                                    }
                                }
                            }
                            if (point > 0 && isPlayer()) {
                                upPointUpgradeSkill(skill, point);
                            }
                        }
                    }, 150);
                    break;
                }

                case SkillName.TU_PHAT_NO:
                case SkillName.LAZE:
                case SkillName.QUA_CAU_GENKI: {
                    isCharge = true;
                    Utils.setTimeout(() -> {
                        if (isCharge) {
                            isCharge = false;
                            isCanUseSkillCharge = true;
                            service.stopCharge();
                        }
                    }, Utils.nextInt(2000, 3000));
                    break;
                }
            }
            if (options[118] > 0) {
                long mpRecovery = manaUse * options[118] / 100;
                if (mpRecovery > 0) {
                    recovery(RECOVERY_MP, mpRecovery);
                }
            }
        } finally {
            lockAttack.unlock();
        }
    }

    public void useSkill(Message message) {
        try {
            int skillId = message.reader().readByte();
            Skill skill = getSkill(skillId);
            if (skill == null || skill.level <= 0 || !skill.template.isProactive) {
                return;
            }
            Entity focus = null;
            if (skill.template.type == SkillType.NEED_FOCUS) {
                if (message.reader().available() <= 0) {
                    return;
                }
                int type = message.reader().readByte();
                if (type == 1) {
                    focus = zone.findMonsterById(message.reader().readInt());
                    if (focus == null || focus.isDead()) {
                        return;
                    }
                } else if (type == 0) {
                    focus = zone.findPlayerById(message.reader().readInt());
                    if (focus == null || (focus.isDead() && skill.template.id != SkillName.TRI_THUONG) || !isCanAttackEntityWithSkill(focus, skill)) {
                        return;
                    }
                }
            }
            useSkill(skill, focus);
        } catch (Exception ex) {
            logger.error("useSkill", ex);
        }
    }

    public void selectSkill(Message message) {
        try {
            if (isDead() || isCharge) {
                return;
            }
            Skill skill = getSkill(message.reader().readByte());
            if (skill != null) {
                mySkill = skill;
            }
        } catch (Exception ex) {
            logger.error("selectSkill", ex);
        }
    }

    public void attackPlayer(Player player) {
        if (!isCanAttackEntity(player)) {
            return;
        }
        player.options[OptionName.VO_HINH] = 0;
        focus = player;
        zone.service.useSkill(this);
        if (player.isDodge(this, -options[OptionName.CHINH_XAC])) {
            player.injure(this, 0, false, false);
            return;
        }
        boolean isCritical = isCritical(player, 0);
        long damage = getDamageWhenAttack(player, isCritical);
        if (intrinsicAttack != null && intrinsicAttack.entity == player) {
            if (player.isBoss()) {
                damage += Utils.percentOf(damage, intrinsicAttack.percent);
            }
            intrinsicAttack = null;
        }
        damage = player.formatDamageInjure(this, damage, isCritical);
        if (damage <= 0) {
            player.injure(this, 0, false, false);
            return;
        }
        boolean isHakai = (player.isPlayer() || player.isDisciple()) && Utils.isPercent(options[192]);
        if (isHakai) {
            damage += player.maxHp / 3;
        }
        player.injure(this, damage, isCritical, true);
        sucking(damage, false);
        if (options[166] > 0 && Utils.isPercent(options[166])) {
            if (player.removeEffectById(EffectName.KHIEN_NANG_LUONG)) {
                service.removeEffect(EffectName.KHIEN_NANG_LUONG);
            }
        }
        if (options[164] > 0) {
            player.timeVetThuongSau = System.currentTimeMillis();
            player.percentVietThuongSau = options[164];
        }
        if (isHakai) {
            chat("Hakai " + player.name);
        }
    }

    public void attackMonster(Monster monster) {
        if (monster == null || monster.isDead() || monster == pet || monster instanceof Pet) {
            return;
        }
        if (isPlayer() && Math.abs(this.x - monster.x) > mySkill.getDx() * 3) {
            return;
        }
        focus = monster;
        monster.focus = this;
        if (monster.isDodge(this, -options[OptionName.CHINH_XAC])) {
            monster.injure(this, 0, false, true);
            return;
        }
        boolean isCritical = isCritical(monster, 0);
        long damage;
        if (monster.template.id == 0 || monster.template.id == 34) {
            damage = 1;
        } else {
            damage = getDamageWhenAttack(monster, isCritical);
            if (intrinsicAttack != null && intrinsicAttack.entity == monster) {
                damage += Utils.percentOf(damage, intrinsicAttack.percent);
                intrinsicAttack = null;
            }
            damage = monster.formatDamageInjure(this, damage, isCritical);
        }
        if (monster.template.id == 106 || monster.template.id == 107) {
            monster.injure(this, monster.maxHp / 10, isCritical, true);
        } else {
            if (damage >= monster.hp && monster.hp == monster.maxHp) {
                damage = monster.hp - 1;
            } else if (damage > monster.hp) {
                damage = monster.hp;
            }
            monster.injure(this, damage, isCritical, true);
        }
        sucking(damage, true);
        if (monster.template.id != 0 && monster.template.id != 34 && (monster.template.id == 43 || monster.template.id == 106 || monster.template.id == 107 || Math.abs(level - monster.level) < 10) && !isLockExp()) {
            long exp = Math.max(damage / 2, 1L);
            Barrack barrack = zone.map.getBarrack();
            if (barrack != null) {
                if (monster.numRefresh > 0) {
                    exp = 1;
                } else {
                    List<Player> players = zone.getPlayers(Zone.TYPE_BOSS);
                    if (!players.isEmpty()) {
                        exp = 1;
                    }
                }
                if (exp != 1) {
                    if (barrack.level < 40) {
                        exp *= 3;
                    } else if (barrack.level < 50) {
                        exp *= 2;
                    } else {
                        exp += exp / 2;
                    }
                }
            }
            if (monster.template.id != MonsterName.HEO_RUNG && !zone.map.isManor()) {
                if (monster.levelBoss == MonsterLevelBoss.THU_LINH) {
                    exp *= 2;
                } else if (monster.levelBoss == MonsterLevelBoss.TINH_ANH) {
                    exp += exp / 2;
                }
            }
            if (zone.map.template.id == MapName.LANG_CO_GIRA) {
                exp += exp / 2;
            } else if (zone.map.isManor()) {
                exp += exp / 5;
            } else if ((zone.map.isIsland() && level < 90)
                    || zone.map.template.id == MapName.DAO_HOA_1
                    || zone.map.template.id == MapName.DAO_HOA_2) {
                exp += exp / 3;
            }
            if (zone.map.template.id == MapName.BANG_HOA_15) {
                exp /= 4;
            }
            List<Effect> effectList = getEffects();
            int multi = Server.getInstance().getMultiExp();
            if (isPlayer()) {
                if (isHaveEffect(effectList, EffectName.BUA_TRI_TUE)) {
                    multi += 100;
                }
                if (level >= 40 && isHaveEffect(effectList, EffectName.BUA_SIEU_TRI_TUE)) {
                    multi += 200;
                }
                if (level >= 60 && isHaveEffect(effectList, EffectName.BUA_SIEU_CAP_TRI_TUE)) {
                    multi += 300;
                }
                if (level < 10) {
                    multi += 100;
                } else if (level < 20) {
                    multi += 50;
                }
            }
            if (typePk == 2) {
                multi += 20;
            } else if (typeFlag == 1) {
                multi += 15;
            } else if (typeFlag > 1) {
                multi += 10;
            }
            if (isDisciple()) {
                Player master = ((Disciple) this).master;
                List<Effect> masterEffects = master.getEffects();
                if (master.isHaveEffect(masterEffects, EffectName.TANG_TNSM_DUA_XANH)) {
                    multi += 100;
                }
                if (master.isHaveEffect(masterEffects, EffectName.KEO_DAU_LAU_TNSM)) {
                    multi += 200;
                }
                if (level >= 40 && master.isHaveEffect(masterEffects, EffectName.BUA_SIEU_DE_TU)) {
                    multi += 200;
                }
                if (level >= 60 && master.isHaveEffect(masterEffects, EffectName.BUA_SIEU_CAP_DE_TU)) {
                    multi += 300;
                }
                if (master.isHaveEffect(masterEffects, EffectName.TANG_TNSN_DE_TU_TU_NR_NGUYEN_DAN)) {
                    exp *= 2;
                }
            } else {
                if (isHaveEffect(effectList, EffectName.TANG_TNSM_DUA_XANH)) {
                    multi += 100;
                }
                if (isHaveEffect(effectList, EffectName.KEO_DAU_LAU_TNSM)) {
                    multi += 200;
                }
                if (isHaveEffect(effectList, EffectName.TANG_TNSM_TU_NR_NGUYEN_DAN)) {
                    multi += 100;
                }
            }
            if (multi > 0) {
                exp += exp * multi / 100;
            }
            if (options[OptionName.TNSM] > 0) {
                exp += Utils.percentOf(exp, options[OptionName.TNSM] / 100);
            }
            if (options[146] > 0) {
                exp += Utils.percentOf(exp, options[146]);
            }
            if (options[168] > 0) {
                exp += Utils.percentOf(exp, options[168]);
            }
            if (isPlayer()) {
                Intrinsic intrinsic = intrinsics.get(25);
                if (intrinsic != null && intrinsic.param > 0) {
                    exp += Utils.percentOf(exp, intrinsic.param);
                }
            }
            if (monster.template.id != 43 && monster.template.id != 106 && monster.template.id != 107) {
                if (monster.level > level) {
                    int percent = 2 * Math.abs(level - monster.level);
                    exp += exp * percent / 100;
                } else if (monster.level < level) {
                    int percent = 10 * Math.abs(level - monster.level);
                    exp -= exp * percent / 100;
                }
            }
            if (this.level >= 60) {
                for (int i = 60; i <= this.level; i++) {
                    exp -= exp * (10L + 2L * (i - 60) / 10) / 100;
                }
            }
            if (exp > 0) {
                upPower(exp);
                expTraining += exp;
                if (isPlayer() && !(zone instanceof ZoneIsland)) {
                    List<Player> players = zone.findAllPlayerSameTeam(this);
                    if (players.size() > 1) {
                        for (Player player : players) {
                            if (player != this && !player.isDead()) {
                                int dis = Math.abs(this.level - player.level);
                                if (dis < 10) {
                                    long xp = exp - exp * (40L - dis * 4L) / 100;
                                    if (xp > 0) {
                                        player.upPower(xp);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        if (damage > 0 && isInTreasure()) {
            Treasure treasure = MapManager.getInstance().treasure;
            if (treasure != null) {
                Pirate pirate = treasure.findPirateByPlayerId(this.id);
                if (pirate != null) {
                    pirate.upPoint(1);
                }
            }
        }
    }

    public void sucking(long damage, boolean isMonster) {
        if (isDead()) {
            return;
        }
        if (damage <= 0) {
            return;
        }
        int blood = options[OptionName.HUT_HP] + (isMonster ? options[OptionName.HUT_HP_QUAI] : 0);
        if (blood > 0) {
            long hpNew = this.hp + Utils.statsOfPoint(damage, blood);
            if (hpNew > maxHp) {
                hpNew = maxHp;
            }
            if (this.hp != hpNew) {
                this.hp = hpNew;
                service.recoveryHp();
                if (zone != null) {
                    zone.service.refreshHp(this);
                }
            }
        }
        int mana = options[OptionName.HUT_MP] + (isMonster ? options[OptionName.HUT_MP_QUAI] : 0);
        if (mana > 0) {
            long mpNew = this.mp + Utils.statsOfPoint(damage, mana);
            if (mpNew > maxMp) {
                mpNew = maxMp;
            }
            if (this.mp != mpNew) {
                this.mp = mpNew;
                service.recoveryMp();
            }
        }
    }

    public boolean isCanAttackEntity(Entity entity) {
        return isCanAttackEntityWithSkill(entity, mySkill);
    }

    public boolean isCanAttackEntityWithSkill(Entity entity, Skill skill) {
        if (entity == null || zone == null || skill == null || zone != entity.zone || this == entity) {
            return false;
        }
        if (entity.isEscort()) {
            return false;
        }
        if (entity.isDead()) {
            if ((entity.isPlayer() || entity.isDisciple()) && isPlayer() && skill.template.id == SkillName.TRI_THUONG && !zone.map.isSurvival()) {
                return true;
            }
            return false;
        }
        if (entity.isMonster()) {
            return !entity.isDead();
        }
        if (entity.isBoss()) {
            Boss boss = ((Boss) entity);
            return boss.typePk == 3 || (boss.typePk == 1 && boss.testPlayerId == this.id);
        }
        if (isDisciple() && entity.isPlayer() && entity.id == Math.abs(this.id)) {
            return false;
        }
        if (!(entity instanceof Player)) {
            return false;
        }
        Player player = (Player) entity;
        if (isBoss()) {
            return !player.isHide && player.options[OptionName.VO_HINH] == 0 && ((testPlayerId == -1 && typePk == 3) || (testPlayerId == player.id) && typePk == 1);
        }
        if (skill.template.id == SkillName.TRI_THUONG) {
            return true;
        }
        if (zone.map.isSurvival() && (isAdminSurvival() || player.isAdminSurvival())) {
            return false;
        }
        if (player.options[OptionName.VO_HINH] > 0 && player.typePk == 0 && player.typeFlag == 0) {
            return false;
        }
        if (player.typePk == 3) {
            return true;
        }
        if (typePk == 1 && player.typePk == 1 && testPlayerId == player.id) {
            return true;
        }
        int mapId = zone.map.template.id;
        if (mapId != 0 && mapId != MapName.DAI_HOI_VO_THUAT && mapId != MapName.DAU_TRUONG) {
            if (player.typePk == 2 || typePk == 2) {
                return true;
            }
            return typeFlag > 0 && player.typeFlag > 0 && (typeFlag != player.typeFlag || player.typeFlag == 1);
        }
        return false;
    }

    public long getManaUseSkill() {
        if (mySkill.template.typeMana != SkillTypeMana.PERCENT_MANA) {
            return mySkill.getManaUse();
        }
        return mySkill.getManaUse() * maxMp / 100;
    }

    public long getManaUseSkill(Skill skill) {
        if (skill.template.typeMana != SkillTypeMana.PERCENT_MANA) {
            return skill.getManaUse();
        }
        return skill.getManaUse() * maxMp / 100;
    }

    public void tabAction(Message message) {
        try {
            boolean isOpen = message.reader().readBoolean();
            if (isOpen) {
                tabID = message.reader().readByte();
                if (tabID == 33) {
                    LuckyManager.getInstance().luckyCoin.show(this, null);
                }
            } else {
                tabID = -1;
            }
        } catch (Exception ex) {
            logger.error("tabAction", ex);
        }
    }

    public void changePk(Message message) {
        lock.lock();
        try {
            if (level < 5) {
                addInfo(INFO_RED, "Yêu cầu trình độ cấp 5 trở lên");
                return;
            }
            if (isDead()) {
                addInfo(INFO_RED, Language.CANCEL_ACTION_WHEN_DIE);
                return;
            }
            if (!isAdmin()) {
                long now = System.currentTimeMillis();
                long time = 20000L + lastTimeChangePk - now;
                if (time > 0) {
                    addInfo(INFO_RED, String.format("Chỉ có thể thay đổi sau %s", Utils.formatTime(time)));
                    return;
                }
                lastTimeChangePk = now;
            }
            if (zone == null || isInSurvival() || isInDragonBallNamek() || isInForgottenCity() || isInTreasure() || zone.map.template.id == MapName.DAI_HOI_VO_THUAT
                    || zone.map.template.id == MapName.LANG_CO_GIRA || isInSpaceship()) {
                addInfo(INFO_RED, Language.CANT_ACTION);
                return;
            }
            int type = message.reader().readByte();
            int index = message.reader().readByte();
            if (type == 0) {
                if (index == -1) {
                    if (typePk == 2) {
                        setTypePk(0);
                    }
                    if (typeFlag > 0) {
                        setTypeFlag(0);
                    }
                } else if (index == 0) {
                    List<Player> players = zone.getPlayers(Zone.TYPE_PLAYER, Zone.TYPE_DISCIPLE);
                    for (Player player : players) {
                        if (player != this && player.typePk == 2) {
                            addInfo(INFO_RED, "Trong khu đã có chiến binh bật đồ sát");
                            return;
                        }
                    }
                    if (typeFlag > 0) {
                        setTypeFlag(0);
                    }
                    setTypePk(2);
                }
            } else if (type == 1) {
                if (index >= 1 && index <= 8) {
                    if (typePk == 2) {
                        setTypePk(0);
                    }
                    setTypeFlag(index);
                }
            }
        } catch (Exception ex) {
            logger.error("changePk", ex);
        } finally {
            lock.unlock();
        }
    }

    public void setTypePk(int pk) {
        typePk = pk;
        service.setTypePk();
        if (isPlayer() && pk != 1) {
            if (disciple != null) {
                disciple.setTypePk(pk);
            }
        }
    }

    public void setTypeFlag(int flag) {
        typeFlag = flag;
        service.setTypeFlag();
        if (isPlayer()) {
            if (disciple != null) {
                disciple.setTypeFlag(flag);
            }
        }
    }

    @Override
    public long formatDamageInjure(Entity attacker, long damage, boolean isCritical) {
        if (isFirstDodge) {
            isFirstDodge = false;
            return 0;
        }
        if (attacker.isPlayer() && isPlayer() && attacker instanceof Player player && !(zone instanceof ZoneSurvival)) {
            Skill skill = player.mySkill;
            if (skill != null) {
                damage /= skill.isSkillUltimate() ? 3 : 10;
            } else {
                damage /= 10;
            }
        }
        List<Effect> effects = getEffects();
        int reduce = options[OptionName.GIAM_SAT_THUONG];
        if (attacker.isMonster()) {
            reduce += options[OptionName.GIAM_SAT_THUONG_TU_QUAI];
            if (isHaveEffect(effects, EffectName.BUA_CUNG_CAP)) {
                damage /= 2;
            }
        }
        if (planet != MapPlanet.SURVIVAL) {
            Effect effect = findEffectByTemplateId(effects, EffectName.TANG_PHAN_TRAM_GIAM_SAT_THUONG_TU_KEO_LUC);
            if (effect != null) {
                damage -= damage * effect.param / 100;
            }
        }
        if (isInManor()) {
            Effect effect = findEffectByTemplateId(effects, EffectName.PHU_HO_SUC_DANH_VA_GIAM_SAT_THUONG_LANH_DIA);
            if (effect != null) {
                damage /= effect.param;
            }
        }
        if (reduce > 0) {
            damage -= Utils.statsOfPoint(damage, reduce);
        }
        if (isCritical && options[OptionName.GIAM_SAT_THUONG_CHI_MANG] > 0) {
            damage -= Utils.statsOfPoint(damage, options[OptionName.GIAM_SAT_THUONG_CHI_MANG]);
        }
        if (attacker.isPlayer()) {
            Player player = (Player) attacker;
            if (options[121] > 0 && player.mySkill.isSkillUltimate()) {
                damage -= Utils.percentOf(damage, options[121]);
            }
            if (options[132] > 0 && player.gender == 0) {
                damage -= Utils.percentOf(damage, options[132]);
            }
        }
        Effect effect = findEffectByTemplateId(effects, EffectName.KHIEN_NANG_LUONG);
        if (effect != null) {
            if (isPlayer() && damage > maxHp) {
                removeEffect(effect);
                service.removeEffect(effect);
                addInfo(INFO_RED, "Khiên năng lượng đã bị phá vỡ");
            }
            return 1;
        }
        if (!isBoss() && zone instanceof ZoneSpaceship) {
            return maxHp / 20;
        }
        if (zone instanceof ZoneFlagWar zoneFlagWar && zoneFlagWar.flagWar.vips.contains(this.id)) {
            if (damage < maxHp / 20) {
                return maxHp / 20;
            }
            if (damage > maxHp / 10) {
                return maxHp / 10;
            }
        }
        return damage;
    }

    public long getDamageWhenAttack(Entity focus, boolean isCritical) {
        if (mySkill == null) {
            return 1;
        }
        if (planet == MapPlanet.SURVIVAL) {
            return damage * (isCritical ? 3 : 1);
        }
        if (zone != null && mySkill.isSkillUltimate()) {
            if (zone.map.template.id == MapName.DAI_HOI_VO_THUAT) {
                return 1;
            }
            if (zone.map.isManor()) {
                return 0;
            }
        }
        List<Effect> effectList = getEffects();
        if (isHaveEffect(effectList, EffectName.HOA_SOCOLA)) {
            return 1;
        }
        long maxDamage = damage * mySkill.getParam(0) / 100;
        switch (mySkill.template.id) {
            case SkillName.KARAK:
            case SkillName.KARAP:
            case SkillName.KARAV:
            case SkillName.KAME:
            case SkillName.MASENDAN:
            case SkillName.SOKIDAN:
            case SkillName.SUPER_KAME:
            case SkillName.MA_PHONG_BA:
            case SkillName.BIGBANG_FLASH:
                maxDamage += maxMp * mySkill.getParam(29) / 100;
                break;

            case SkillName.LAZE:
                maxDamage = damage * mySkill.getParam(21) / 100 + getManaUseSkill() * mySkill.getParam(20) / 100;
                if (options[144] > 0) {
                    maxDamage += Utils.percentOf(maxDamage, options[144]);
                }
                break;

            case SkillName.TU_PHAT_NO:
                maxDamage = hp * (mySkill.getParam(15) + (long) mySkill.getParam(18) * level) / 100;
                if (options[145] > 0) {
                    maxDamage += Utils.percentOf(maxDamage, options[145]);
                }
                break;

            case SkillName.QUA_CAU_GENKI: {
                maxDamage = damage * mySkill.getParam(23) / 100;
                int num_player = zone.players.size();
                int param = mySkill.getParam(24);
                if (num_player > 0) {
                    for (int i = 0; i < num_player; i++) {
                        maxDamage += maxDamage * (param + level / 10) / 100;
                    }
                }
                if (options[143] > 0) {
                    maxDamage += Utils.percentOf(maxDamage, options[143]);
                }
                break;
            }
        }
        if (options[128] > 0 && (mySkill.template.id == SkillName.KAME || mySkill.template.id == SkillName.SOKIDAN || mySkill.template.id == SkillName.MASENDAN || mySkill.template.id == SkillName.KAME_DISCIPLE || mySkill.template.id == SkillName.SOKIDAN_DISCIPLE || mySkill.template.id == SkillName.MASENDAN_DISCIPLE || mySkill.template.id == SkillName.SUPER_KAME || mySkill.template.id == SkillName.MA_PHONG_BA || mySkill.template.id == SkillName.BIGBANG_FLASH)) {
            maxDamage += Utils.percentOf(maxDamage, options[128]);
        }
        if (isPlayer()) {
            if (mySkill.template.id != SkillName.THOI_MIEN && mySkill.template.id != SkillName.DICH_CHUYEN_TUC_THOI) {
                for (Intrinsic intrinsic : intrinsics.values()) {
                    if (intrinsic.template.skillTemplateId == mySkill.template.id && !intrinsic.template.isCoolDown && intrinsic.param > 0) {
                        if (intrinsic.template.isUpgrade) {
                            if (mySkill.upgrade <= 0) {
                                break;
                            }
                            maxDamage += Utils.percentOf(maxDamage, intrinsic.param);
                            break;
                        }
                        maxDamage += Utils.percentOf(maxDamage, intrinsic.param);
                        break;
                    }
                }
            }
            if (focus instanceof Monster) {
                Intrinsic intrinsic = intrinsics.get(26);
                if (intrinsic != null && intrinsic.param > 0) {
                    maxDamage += Utils.percentOf(maxDamage, intrinsic.param);
                }
            } else if (focus instanceof Boss) {
                Intrinsic intrinsic = intrinsics.get(27);
                if (intrinsic != null && intrinsic.param > 0) {
                    maxDamage += Utils.percentOf(maxDamage, intrinsic.param);
                }
            }
        }
        boolean[] flags = new boolean[2];
        if (options[147] > 0) {
            Effect effect = findEffectByTemplateId(effectList, EffectName.BIEN_KHI);
            if (effect != null) {
                flags[0] = true;
                maxDamage += Utils.percentOf(maxDamage, options[147]);
            }
        }
        if (options[148] > 0) {
            Effect effect = findEffectByTemplateId(effectList, EffectName.KAIOKEN);
            if (effect != null) {
                maxDamage += Utils.percentOf(maxDamage, options[148]);
            }
        }
        if (options[149] > 0) {
            Effect effect = findEffectByTemplateId(effectList, EffectName.HOA_KHONG_LO);
            if (effect != null) {
                flags[1] = true;
                maxDamage += Utils.percentOf(maxDamage, options[149]);
            }
        }
        if (flags[0]) {
            Skill skill = getSkill(SkillName.BIEN_KHI);
            if (skill != null) {
                maxDamage += Utils.percentOf(maxDamage, skill.getParam(41));
            }
        }
        if (flags[1]) {
            Skill skill = getSkill(SkillName.HOA_KHONG_LO);
            if (skill != null) {
                maxDamage += Utils.percentOf(maxDamage, skill.getParam(40));
            }
        }
        if (maxDamage < 1) {
            return 1;
        }
        long damage = Utils.nextLong(maxDamage - maxDamage / 10, maxDamage);
        if (isCritical) {
            int total = 50 + options[OptionName.TAN_CONG_CHI_MANG] / 200 + options[OptionName.TAN_CONG_CHI_MANG_PERCENT];
            if (focus != null && focus.isPlayer()) {
                total -= focus.options[OptionName.GIAM_SAT_THUONG_CHI_MANG] / 200 - focus.options[186];
                total /= 2;
            }
            if (total > 0) {
                damage += Utils.percentOf(damage, total);
            }
        }
        Effect effect = findEffectByTemplateId(effectList, EffectName.BI_GIAM_DAMAGE_TU_SOCOLA);
        if (effect != null) {
            damage -= damage * effect.param / 100;
        }
        effect = findEffectByTemplateId(effectList, EffectName.TANG_PHAN_TRAM_SUC_DANH_TU_LAZE);
        if (effect != null) {
            damage += damage * effect.param / 100;
        }
        if (focus instanceof Player player) {
            int point = options[OptionName.XUYEN_GIAP] - player.options[OptionName.GIAM_SAT_THUONG];
            point = point * 100 / (Math.abs(point) + 10000) + options[183] - player.options[185];
            damage += Utils.statsOfPoint(damage, point);
            if (player.isPlayer()) {
                if (options[OptionName.TANG_SAT_THUONG_LEN_TOC_SAIYAN] > 0 && player.gender == 2) {
                    damage += Utils.percentOf(damage, options[OptionName.TANG_SAT_THUONG_LEN_TOC_SAIYAN]);
                }
                if (options[OptionName.TANG_SAT_THUONG_LEN_TOC_TRAI_DAT] > 0 && player.gender == 0) {
                    damage += Utils.percentOf(damage, options[OptionName.TANG_SAT_THUONG_LEN_TOC_TRAI_DAT]);
                }
                if (options[OptionName.TANG_SAT_THUONG_LEN_TOC_NAMEK] > 0 && player.gender == 1) {
                    damage += Utils.percentOf(damage, options[OptionName.TANG_SAT_THUONG_LEN_TOC_NAMEK]);
                }
                if (options[133] > 0 && player.gender == 0) {
                    damage += Utils.percentOf(damage, options[133]);
                }
            }
            if (isPlayer()) {
                if (player.options[OptionName.GIAM_SAT_THUONG_TU_SAYAIN] > 0 && this.gender == 2) {
                    damage -= Utils.percentOf(damage, player.options[OptionName.GIAM_SAT_THUONG_TU_SAYAIN]);
                }
                if (player.options[OptionName.GIAM_SAT_THUONG_TU_TRAI_DAT] > 0 && this.gender == 0) {
                    damage += Utils.percentOf(damage, player.options[OptionName.GIAM_SAT_THUONG_TU_TRAI_DAT]);
                }
                if (player.options[OptionName.GIAM_SAT_THUONG_TU_NAMEK] > 0 && this.gender == 1) {
                    damage += Utils.percentOf(damage, player.options[OptionName.GIAM_SAT_THUONG_TU_NAMEK]);
                }
            }
        }
        if (focus instanceof Monster && !(focus instanceof BigMonster)) {
            int per = options[OptionName.TAN_CONG_QUAI] + (isHaveEffect(effectList, EffectName.BUA_SUC_MANH) ? 50 : 0);
            if (per > 0) {
                damage += Utils.percentOf(damage, per);
            }
        }
        if (zone != null && options[85] == 0) {
            List<Player> players = zone.getPlayers(Zone.TYPE_PLAYER, Zone.TYPE_DISCIPLE);
            if (players.size() > 1) {
                int param = 0;
                for (Player player : players) {
                    if (player != this && Math.abs(x - player.x) < 500 && player.options[85] > 0) {
                        if (param < player.options[85]) {
                            param = player.options[85];
                        }
                    }
                }
                if (param > 0) {
                    damage += damage * param / 100;
                }
            }
        }
        if (isPlayer() && Server.getInstance().maxDamage < damage && !(focus instanceof Monster)) {
            Server.getInstance().maxDamage = damage;
            Server.getInstance().service.serverChat(String.format("%s đã đánh 1 %s với sát thương %s, mọi người đều ngưỡng mộ", this.name, mySkill.getName(), Utils.getMoneys(damage)));
        }
        return damage;
    }

    public boolean isCritical(Entity entity, int bonus) {
        if (isHaveEffect(EffectName.HOA_KHONG_LO, EffectName.BIEN_KHI)) {
            return true;
        }
        if (entity != null && entity.isHaveEffect(EffectName.PHA_GIAP_LUOI_CUA_ANH_SANG)) {
            return true;
        }
        int critical = options[OptionName.CHI_MANG] + bonus;
        return Utils.nextInt(10000) < critical * 10000 / (critical + 10000);
    }

    public void wakeUpFromDead() {
        lock.lock();
        try {
            if (!this.isDead()) {
                return;
            }
            if (isPlayer()) {
                if (zone == null || zone.map.template.id == MapName.DAI_HOI_VO_THUAT
                        || isInSurvival() || isInTreasure() || isInIsland()) {
                    addInfo(INFO_RED, Language.CANT_ACTION);
                    return;
                }
                if (!isEnoughMoney(TypePrice.RUBY, 1)) {
                    return;
                }
                downMoney(TypePrice.RUBY, 1);
                if (zone.map.isBarrack()) {
                    zone.upNumRefreshMonster(1);
                }
                upPointAchievement(4, 1);
            }
            hp = maxHp;
            mp = maxMp;
            isDie = false;
            if (zone instanceof ZoneFlagWar) {
                this.x = Utils.nextInt(200, zone.map.template.width - 200);
                this.y = zone.map.getYSd(this.x);
            }
            service.wakeUpFromDead();
            if (zone != null) {
                zone.service.playerWakeUpFromDead(this);
            }
        } finally {
            lock.unlock();
        }
    }

    public void returnTownFromDead() {
        lock.lock();
        try {
            if (!this.isDead()) {
                return;
            }
            hp = maxHp;
            mp = maxMp;
            isDie = false;
            if (isInTreasure()) {
                Treasure treasure = MapManager.getInstance().treasure;
                if (treasure != null) {
                    Map map = typeFlag == Treasure.FLAGS[0] ? treasure.maps.get(0) : treasure.maps.get(1);
                    teleport(map, false);
                    service.setInfo();
                    return;
                }
            }
            if (isInSpaceship()) {
                Spaceship spaceship = MapManager.getInstance().spaceship;
                if (spaceship != null) {
                    spaceship.enter(this);
                    return;
                }
            }
            teleport(MapName.NUI_PAOZU, false);
            service.setInfo();
        } finally {
            lock.unlock();
        }
    }

    public void teleport(int mapId, boolean isNow) {
        teleport(MapManager.getInstance().maps.get(mapId), isNow);
    }

    public void teleport(Map map, boolean isNow) {
        if (map == null) {
            return;
        }
        if (spaceship > 0) {
            recovery(RECOVERY_ALL, 100, true);
        }
        if (zone != null) {
            if (!isNow) {
                zone.service.addTeleport(this);
            }
            x = x * map.template.width / zone.map.template.width;
            y = 10;
            zone.leave(this);
        } else {
            x = map.template.width / 2;
            y = 10;
        }
        joinMap(map, -1);
        y = map.getYSd(x);
    }

    public void teleport(Zone zone, boolean isNow) {
        if (zone == null) {
            return;
        }
        if (spaceship > 0) {
            recovery(RECOVERY_ALL, 100, true);
        }
        if (this.zone != null) {
            if (!isNow) {
                this.zone.service.addTeleport(this);
            }
            x = x * zone.map.template.width / this.zone.map.template.width;
            y = 10;
            this.zone.leave(this);
        } else {
            x = zone.map.template.width / 2;
            y = 10;
        }
        zone.enter(this);
        y = zone.map.getYSd(x);
    }

    public Skill getSkill(int templateId) {
        for (Skill skill : skills) {
            if (skill.template.id == templateId) {
                return skill;
            }
        }
        return null;
    }

    public void teleportToPlayer(Message message) {
        lock.lock();
        try {
            if (this.options[114] == 0) {
                addInfo(INFO_RED, "Cần phải sử dụng trang bị hỗ trợ dịch chuyển tức thời");
                return;
            }
            long now = System.currentTimeMillis();
            if (now - lastTimeTeleport < 5000) {
                addInfo(INFO_RED, "Thao tác quá nhanh");
                return;
            }
            int playerId = message.reader().readInt();
            if (playerId == this.id) {
                return;
            }
            Player player = PlayerManager.getInstance().findPlayerById(playerId);
            if (player == null) {
                addInfo(INFO_RED, "Hiện tại người chơi này không online");
                return;
            }
            if (player.zone == null || player.zone == zone || player.zone.map.isExpansion()
                    || player.zone.map.template.id == MapName.DAI_HOI_VO_THUAT
                    || player.zone.map.template.id == MapName.DAU_TRUONG
                    || player.zone.map.template.id == MapName.LANH_DIA_BANG_HOI
                    || player.zone.map.template.id == MapName.NHA_GO_HAN
                    || !isCanJoinMap(player.zone.map)) {
                addInfo(INFO_RED, "Không thể đến khu vực này");
                return;
            }
            if (player.options[165] > 0) {
                addInfo(INFO_RED, "Người chơi đã ẩn KI, không thể tìm thấy vị trí");
                return;
            }
            if (!player.zone.getPlayers(Zone.TYPE_BOSS).isEmpty()) {
                player.addInfo(Player.INFO_RED, Language.CANT_ACTION);
                return;
            }
            lastTimeTeleport = now;
            if (zone != null) {
                zone.leave(this);
            }
            if (player.x <= 200) {
                x = player.x + 50;
            } else if (player.x >= player.zone.map.template.width - 200) {
                x = player.x - 50;
            } else {
                x = player.x + 50 * (Utils.nextInt(2) == 0 ? 1 : -1);
            }
            y = player.y;
            player.zone.enter(this);
        } catch (Exception ex) {
            logger.error("teleportToPlayer", ex);
        } finally {
            lock.unlock();
        }
    }

    public long getPotentialToUp(int index, int point) {
        long potential = 0L;
        int range;
        int point_default;
        if (index == 0) {
            range = RANGE_DAMAGE;
            point_default = baseDamage - 9;
        } else if (index == 1) {
            range = RANGE_HP;
            point_default = baseHp - 4;
        } else if (index == 2) {
            range = RANGE_MP;
            point_default = baseMp - 4;
        } else {
            range = RANGE_CONSTITUTION;
            point_default = baseConstitution - 4;
        }
        for (short i = 1; i <= point; i++) {
            potential += 10 + (long) range * point_default * (point_default - 1) / 2;
            point_default++;
        }
        return potential;
    }

    public long getPotentialToUp(int index) {
        int range;
        int point;
        if (index == 0) {
            range = RANGE_DAMAGE;
            point = baseDamage - 9;
        } else if (index == 1) {
            range = RANGE_HP;
            point = baseHp - 4;
        } else if (index == 2) {
            range = RANGE_MP;
            point = baseMp - 4;
        } else {
            range = RANGE_CONSTITUTION;
            point = baseConstitution - 4;
        }
        return 10 + (long) range * point * (point - 1) / 2;
    }

    public void addItemOption(HashMap<Integer, ArrayList<Integer>> itemOptions, int id, int param) {
        if (!itemOptions.containsKey(id)) {
            itemOptions.put(id, new ArrayList<>());
        }
        itemOptions.get(id).add(param);
    }

    public void refreshInfo() {
        int[] options = new int[ItemManager.getInstance().itemOptionTemplates.size()];
        HashMap<Integer, ArrayList<Integer>> itemOptions = new HashMap<>();
        Item[] items = itemsBody;
        boolean isInSurvival = isInSurvival();
        if (isInSurvival) {
            items = itemsSurvival;
        }
        int upgrade = UpgradeItem.MAX_UPGRADE;
        for (int i = 0; i < items.length; i++) {
            Item item = items[i];
            if (item == null) {
                if (i < 8) {
                    upgrade = -1;
                }
                continue;
            }
            if (item.template.type == ItemType.TYPE_PET && (pet == null || pet.stamina <= 0)) {
                continue;
            }
            int current_upgrade = item.getUpgrade();
            for (ItemOption option : item.getOptionDisplays()) {
                if (option.template.type == 1) {
                    String[] array = option.template.name.split(" ");
                    int point = Integer.parseInt(array[0].replaceAll("[()+]", ""));
                    if (current_upgrade >= point) {
                        addItemOption(itemOptions, option.template.id, option.param);
                        if (point == 14) {
                            if (current_upgrade >= 18) {
                                addItemOption(itemOptions, option.template.id, option.param);
                            }
                            if (current_upgrade >= 19) {
                                addItemOption(itemOptions, option.template.id, option.param);
                            }
                        }
                        if (point == 12 && current_upgrade >= 17) {
                            addItemOption(itemOptions, option.template.id, option.param);
                        }
                    }
                } else {
                    addItemOption(itemOptions, option.template.id, option.param);
                }
            }
            if (i < 8) {
                upgrade = Math.min(upgrade, current_upgrade);
            }
        }
        int pointOther = -1;
        if (!isInSurvival) {
            if (pet != null && pet.stamina > 0 && !pet.isDead()) {
                for (Item item : itemsPet) {
                    if (item == null) {
                        continue;
                    }
                    for (ItemOption option : item.options) {
                        addItemOption(itemOptions, option.template.id, option.param);
                    }
                }
            }
            if (isPlayer() || isDisciple()) {
                pointOther = UpgradeItem.MAX_UPGRADE;
                ItemOption itemOption = null;
                int[] upgrades = new int[]{-1, -1, -1, -1, -1, -1, -1, -1};
                for (int i = 0; i < 8; i++) {
                    Item item = itemsOther[i];
                    if (item == null) {
                        pointOther = -1;
                        break;
                    }
                    for (ItemOption option : item.options) {
                        if (option.template.type == 7) {
                            if (itemOption == null) {
                                itemOption = option;
                            } else if (itemOption.template.id != option.template.id) {
                                pointOther = -1;
                                break;
                            }
                            break;
                        }
                    }
                    if (pointOther == -1) {
                        break;
                    }
                    upgrades[i] = item.getUpgrade();
                    pointOther = Math.min(pointOther, upgrades[i]);
                }
                if (pointOther >= 0) {
                    for (int i = 0; i < 8; i++) {
                        Item item = itemsOther[i];
                        for (ItemOption option : item.options) {
                            switch (option.template.type) {
                                case 1: {
                                    String[] array = option.template.name.split(" ");
                                    int up = Integer.parseInt(array[0].replaceAll("[()+]", ""));
                                    if (upgrades[i] >= up) {
                                        addItemOption(itemOptions, option.template.id, option.param);
                                        if (up == 12 && upgrades[i] >= 17) {
                                            addItemOption(itemOptions, option.template.id, option.param);
                                        }
                                        if (up == 14) {
                                            if (upgrades[i] >= 18) {
                                                addItemOption(itemOptions, option.template.id, option.param);
                                            }
                                            if (upgrades[i] >= 19) {
                                                addItemOption(itemOptions, option.template.id, option.param);
                                            }
                                        }
                                    }
                                    break;
                                }

                                case 5: {
                                    if (pointOther >= 4) {
                                        String[] array = option.template.name.split("\\)");
                                        int up = Integer.parseInt(array[0].split("\\+")[1]);
                                        if (pointOther >= up && up < 14) {
                                            addItemOption(itemOptions, option.template.id, option.param);
                                        }
                                    }
                                    break;
                                }

                                default:
                                    addItemOption(itemOptions, option.template.id, option.param);
                                    break;

                            }
                        }
                    }
                }
            }
        }
        for (int id : itemOptions.keySet()) {
            ArrayList<Integer> params = itemOptions.get(id);
            for (int param : params) {
                options[id] += param;
            }
        }
        //long base_point = Math.max(20L + (level - 20) / 10 * 20L, 20L);
        long base_point = 20L;
        long max_hp = options[3];
        long max_mp = options[4];
        long max_damage = options[0] + options[9];
        int bonus_speed = options[37] + options[7];
        if (isInSurvival) {
            max_hp += 10000;
            max_mp += 10000000;
            max_damage += 100;
        } else {
            options[OptionName.CHI_MANG] += (int) Utils.percentOf(baseConstitution, 100 + options[142]) * 10;
            options[OptionName.NE_DON] += (int) Utils.percentOf(baseConstitution, 100 + options[142]) * 10;
            if (isPlayer()) {
                Intrinsic intrinsic = intrinsics.get(31);
                if (intrinsic != null && intrinsic.param > 0) {
                    options[140] += intrinsic.param;
                }
                intrinsic = intrinsics.get(32);
                if (intrinsic != null && intrinsic.param > 0) {
                    options[141] += intrinsic.param;
                }
            }
            max_hp += Utils.percentOf(baseHp, 100 + options[140]) * base_point;
            max_mp += Utils.percentOf(baseMp, 100 + options[141]) * base_point;
            //max_damage += Utils.percentOf(baseDamage, 100 + options[139]) * (Math.max(2L, level / 10)) / 2;
            max_damage += Utils.percentOf(baseDamage, 100 + options[139]);
        }
        options[OptionName.CHI_MANG] += options[14] * (upgrade >= 17 ? 2 : 1) + options[39];
        options[OptionName.GIAM_SAT_THUONG] += options[16] * (upgrade >= 17 ? 2 : 1) + options[40] * (upgrade >= 17 ? 2 : 1) + options[8];
        options[OptionName.NE_DON] += options[15] * (upgrade >= 17 ? 2 : 1) + options[44];
        options[OptionName.TAN_CONG_CHI_MANG_PERCENT] += options[13] * (upgrade >= 17 ? 2 : 1);
        options[OptionName.CHINH_XAC] += options[38];

        // kích 8 món
        if (upgrade >= 0) {
            max_hp += options[46];
            max_hp += options[46];
            max_damage += options[45];
            options[OptionName.CHINH_XAC] += options[17];
            options[OptionName.GIAM_SAT_THUONG_CHI_MANG] += options[18];
            options[OptionName.NE_DON] += options[47];
        }

        // tăng chỉ số theo cấp độ item
        int total = options[11] + options[41] + options[43];
        if (total > 0) {
            max_hp += Utils.percentOf(max_hp, total);
        }
        total = options[11] + options[41] + options[43] + options[12];
        if (total > 0) {
            max_mp += Utils.percentOf(max_mp, total);
        }
        total = options[10] + options[42];
        if (total > 0) {
            max_damage += Utils.percentOf(max_damage, total);
        }

        // tăng chỉ số theo set mốc cấp độ
        if (upgrade >= 4) {
            options[OptionName.CHINH_XAC] += options[94];
        }
        if (upgrade >= 8) {
            options[OptionName.GIAM_SAT_THUONG_TU_QUAI] += options[95];
        }
        if (upgrade >= 12) {
            options[OptionName.XUYEN_GIAP] += options[86];
        }
        long base_max_hp = max_hp;
        long base_max_mp = max_mp;
        long base_max_damage = max_damage;
        if (upgrade >= 16) {
            int param = options[97] + options[98] * (upgrade - 15);
            max_damage += Utils.percentOf(base_max_damage, param);
            max_hp += Utils.percentOf(base_max_hp, param);
            max_mp += Utils.percentOf(base_max_mp, param);
        } else if (upgrade >= 14) {
            max_damage += Utils.percentOf(base_max_damage, options[97]);
            max_hp += Utils.percentOf(base_max_hp, options[97]);
            max_mp += Utils.percentOf(base_max_mp, options[97]);
        }
        if (pointOther >= 14) {
            for (int i = 0; i < 8; i++) {
                Item item = itemsOther[i];
                if (item != null) {
                    for (ItemOption option : item.options) {
                        if (option.template.type == 5) {
                            String[] array = option.template.name.split("\\)");
                            int up = Integer.parseInt(array[0].split("\\+")[1]);
                            if (up >= 14) {
                                long param = option.param;
                                if (up == 16) {
                                    if (pointOther >= 17) {
                                        param += option.param;
                                    }
                                    if (pointOther >= 18) {
                                        param += option.param;
                                    }
                                    if (pointOther >= 19) {
                                        param += option.param;
                                    }
                                }
                                max_damage += Utils.percentOf(base_max_damage, param);
                                max_hp += Utils.percentOf(base_max_hp, param);
                                max_mp += Utils.percentOf(base_max_mp, param);
                            }
                        }
                    }
                }
            }
        }
        /*base_max_hp = max_hp;
        base_max_mp = max_mp;
        base_max_damage = max_damage;*/
        // tăng chỉ số theo option type 0
        total = options[25] + options[85] + level / 5 * options[33];
        if (total > 0) {
            max_damage += Utils.percentOf(base_max_damage, total);
        }
        total = options[31] + level / 5 * options[34];
        if (total > 0) {
            max_hp += Utils.percentOf(base_max_hp, total);
        }
        total = options[32] + level / 5 * options[35];
        if (total > 0) {
            max_mp += Utils.percentOf(base_max_mp, total);
        }
        if (options[78] > 0) {
            options[OptionName.CHI_MANG] += level / 5 * options[78];
        }
        if (options[111] > 0) {
            options[OptionName.NE_DON] += level / 5 * options[111];
        }
        // tăng chỉ số theo ép sao
        base_max_hp = max_hp;
        base_max_mp = max_mp;
        base_max_damage = max_damage;
        if (options[53] > 0) {
            max_hp += Utils.percentOf(base_max_hp, options[53] / 100);
        }
        if (options[54] > 0) {
            max_mp += Utils.percentOf(base_max_mp, options[54] / 100);
        }
        options[OptionName.GIAM_SAT_THUONG] += options[55];
        if (options[56] > 0) {
            max_damage += Utils.percentOf(base_max_damage, options[56] / 100);
        }
        options[OptionName.CHI_MANG] += options[57];
        options[OptionName.TAN_CONG_CHI_MANG] += options[58];
        options[OptionName.TNSM] += options[59];
        options[OptionName.NE_DON] += options[60];
        options[OptionName.TI_LE_XU_DANH_QUAI] += options[61];
        options[OptionName.CHINH_XAC] += options[62];
        options[OptionName.XUYEN_GIAP] += options[63];
        options[OptionName.HUT_HP] += options[64];
        options[OptionName.PHAN_SAT_THUONG] += options[65];
        options[OptionName.HUT_MP] += options[66];

        if (!isInSurvival) {
            for (Skill skill : skills) {
                if (!skill.template.isProactive && skill.level > 0) {
                    for (SkillOption option : skill.template.options) {
                        switch (option.template.id) {
                            case 1:
                                max_damage += base_max_damage * option.getParam(skill.level, skill.upgrade) / 100;
                                break;
                            case 2:
                                max_hp += base_max_hp * option.getParam(skill.level, skill.upgrade) / 100;
                                break;
                            case 3:
                                max_mp += base_max_mp * option.getParam(skill.level, skill.upgrade) / 100;
                                break;
                            case 4:
                                options[OptionName.TANG_SAT_THUONG_LEN_TOC_TRAI_DAT] += option.getParam(skill.level, skill.upgrade);
                                break;
                            case 5:
                                options[OptionName.TANG_SAT_THUONG_LEN_TOC_NAMEK] += option.getParam(skill.level, skill.upgrade);
                                break;
                            case 6:
                                options[OptionName.TANG_SAT_THUONG_LEN_TOC_SAIYAN] += option.getParam(skill.level, skill.upgrade);
                                break;
                            case 39:
                                options[OptionName.TNSM] += option.getParam(skill.level, skill.upgrade) * 1000;
                                break;
                        }
                    }
                }
            }
        }
        if (isPlayer()) {
            Intrinsic intrinsic = intrinsics.get(37);
            if (intrinsic != null && intrinsic.param > 0) {
                options[OptionName.TANG_SAT_THUONG_LEN_TOC_TRAI_DAT] += intrinsic.param;
            }
            intrinsic = intrinsics.get(38);
            if (intrinsic != null && intrinsic.param > 0) {
                options[OptionName.TANG_SAT_THUONG_LEN_TOC_NAMEK] += intrinsic.param;
            }
            intrinsic = intrinsics.get(39);
            if (intrinsic != null && intrinsic.param > 0) {
                options[OptionName.TANG_SAT_THUONG_LEN_TOC_SAIYAN] += intrinsic.param;
            }
            intrinsic = intrinsics.get(40);
            if (intrinsic != null && intrinsic.param > 0) {
                options[OptionName.GIAM_SAT_THUONG_TU_TRAI_DAT] += intrinsic.param;
            }
            intrinsic = intrinsics.get(41);
            if (intrinsic != null && intrinsic.param > 0) {
                options[OptionName.GIAM_SAT_THUONG_TU_NAMEK] += intrinsic.param;
            }
            intrinsic = intrinsics.get(42);
            if (intrinsic != null && intrinsic.param > 0) {
                options[OptionName.GIAM_SAT_THUONG_TU_SAYAIN] += intrinsic.param;
            }
            intrinsic = intrinsics.get(33);
            if (intrinsic != null && intrinsic.param > 0) {
                options[185] += intrinsic.param;
            }
            intrinsic = intrinsics.get(34);
            if (intrinsic != null && intrinsic.param > 0) {
                options[186] += intrinsic.param;
            }
            intrinsic = intrinsics.get(35);
            if (intrinsic != null && intrinsic.param > 0) {
                options[183] += intrinsic.param;
            }
            intrinsic = intrinsics.get(36);
            if (intrinsic != null && intrinsic.param > 0) {
                options[184] += intrinsic.param;
            }
        }
        // hiệu ứng
        List<Effect> effectList = getEffects();
        for (Effect effect : effectList) {
            if (effect.time <= 0) {
                continue;
            }
            switch (effect.template.id) {
                case 37:
                    options[OptionName.GIAM_SAT_THUONG] += effect.param * 100;
                    break;

                case EffectName.TANG_PHAN_TRAM_HP_TU_KEO_DO:
                    if (!isInSurvival) {
                        max_hp += Utils.percentOf(max_hp, effect.param);
                    }
                    break;

                case EffectName.BIEN_KHI:
                    max_hp += Utils.percentOf(max_hp, effect.param);
                    break;

                case 38:
                    max_hp += Utils.percentOf(max_hp, effect.param);
                    max_mp += Utils.percentOf(max_mp, effect.param);
                    break;

                case EffectName.TANG_PHAN_TRAM_SUC_DANH_TU_PHAN_THUONG_NRNM_1_SAO:
                case EffectName.TANG_PHAN_TRAM_SUC_DANH_TU_KEO_VANG:
                case EffectName.TANG_PHAN_TRAM_SUC_DANH_DUA_VANG:
                case EffectName.KEO_DAU_LAU_SUC_DANH:
                    if (!isInSurvival) {
                        max_damage += Utils.percentOf(max_damage, effect.param);
                    }
                    break;

                case EffectName.KAIOKEN:
                case EffectName.BANH_BO:
                case 39:
                    max_damage += Utils.percentOf(max_damage, effect.param);
                    break;

                case 40:
                    options[OptionName.NE_DON] += effect.param * 100;
                    break;

                case 41:
                    options[OptionName.CHINH_XAC] += effect.param * 100;
                    break;

                case 42:
                    options[OptionName.HOI_HP_30_GIAY] += effect.param;
                    break;

                case 43:
                    options[OptionName.CHI_MANG] += effect.param * 100;
                    break;

                case 44:
                    options[OptionName.HOI_MP_30_GIAY] += effect.param;
                    break;

                case EffectName.TANG_PHAN_TRAM_KI_TU_KEO_XANH:
                    if (!isInSurvival) {
                        max_mp += Utils.percentOf(max_mp, effect.param);
                    }
                    break;

                case EffectName.BANH_SAU:
                case EffectName.TANG_PHAN_TRAM_HP_KI_TU_PHAN_THUONG_NRNM_2_SAO:
                case EffectName.TANG_PHAN_TRAM_HP_KI_DUA_NAU:
                case EffectName.KEO_DAU_LAU_HP_KI:
                    if (!isInSurvival) {
                        max_hp += Utils.percentOf(max_hp, effect.param);
                        max_mp += Utils.percentOf(max_mp, effect.param);
                    }
                    break;

                case EffectName.KEO_DAU_LAU_NE_DON:
                    if (!isInSurvival) {
                        options[OptionName.NE_DON] += effect.param;
                    }
                    break;

                case EffectName.TANG_SUC_DANH_TU_NR_NGUYEN_DAN:
                    if (!isInSurvival) {
                        max_damage += Utils.percentOf(max_damage, 30);
                    }
                    break;

                case EffectName.TANG_HP_KI_TU_NR_NGUYEN_DAN:
                    if (!isInSurvival) {
                        max_hp += Utils.percentOf(max_hp, 50);
                        max_mp += Utils.percentOf(max_mp, 50);
                    }
                    break;

                case EffectName.PHU_HO_HP:
                    if (zone instanceof ZoneDragonBallNamek) {
                        max_hp *= effect.param;
                    }
                    break;

                case EffectName.PHU_HO_KI:
                    if (zone instanceof ZoneDragonBallNamek) {
                        max_mp *= effect.param;
                    }
                    break;

                case EffectName.PHU_HO_SUC_DANH:
                    if (zone instanceof ZoneDragonBallNamek) {
                        max_damage *= effect.param;
                    }
                    break;

                case EffectName.PHU_HO_SUC_DANH_VA_GIAM_SAT_THUONG_LANH_DIA:
                    if (zone.map.isManor()) {
                        max_damage *= effect.param;
                    }
                    break;

                case EffectName.PHU_HO_MABU:
                    if (zone instanceof ZoneSpaceship) {
                        max_hp *= 10;
                        max_mp *= 10;
                        max_damage *= 10;
                    }
                    break;
            }
        }
        if (options[195] > 0 && isHaveEffect(effects, EffectName.BIEN_KHI, EffectName.KAIOKEN, EffectName.HOA_KHONG_LO)) {
            max_damage += Utils.percentOf(max_damage, options[195]);
            max_hp += Utils.percentOf(max_hp, options[195]);
            max_mp += Utils.percentOf(max_mp, options[195]);
        }
        if (isFusion() && disciple != null && planet != MapPlanet.SURVIVAL) {
            max_hp += disciple.maxHp;
            max_mp += disciple.maxMp;
            max_damage += disciple.damage;
            if (disciple.typeDisciple == 1) {
                max_damage += Utils.percentOf(max_damage, 15);
                max_hp += Utils.percentOf(max_hp, 15);
                max_mp += Utils.percentOf(max_mp, 15);
            }
        }
        if (options[100] > 0) {
            max_mp -= max_mp * options[100] / 100;
            max_hp -= max_hp * options[100] / 100;
            max_damage -= max_damage * options[100] / 100;
        } else if (isPlayer() && !isInSurvival) {
            Item[] bags = itemsBag;
            for (Item item : bags) {
                if (item != null && item.template.type == ItemType.TYPE_VONG_KIM_HAM) {
                    int time = 0;
                    int param = 0;
                    for (ItemOption option : item.options) {
                        if (option.template.id == 24) {
                            time = option.param;
                        } else if (option.template.id == 101) {
                            param = option.param;
                        }
                    }
                    if (time > 0 && param > 0) {
                        max_mp += Utils.percentOf(max_mp, param);
                        max_hp += Utils.percentOf(max_hp, param);
                        max_damage += Utils.percentOf(max_damage, param);
                    }
                    break;
                }
            }
        }
        if (options[110] > 0 && (planet == MapPlanet.COLD || planet == MapPlanet.FIRE)) {
            max_damage += Utils.percentOf(max_damage, options[110]);
        }
        ArrayList<Integer> optionList = itemOptions.get(160);
        if (optionList != null && optionList.size() >= 4) {
            options[109] += options[160];
        }
        optionList = itemOptions.get(169);
        if (optionList != null && optionList.size() >= 4) {
            options[109] += options[169];
        }
        optionList = itemOptions.get(179);
        if (optionList != null && optionList.size() >= 3) {
            options[109] += options[179];
        }
        optionList = itemOptions.get(193);
        if (optionList != null && optionList.size() >= 3) {
            options[109] += options[193];
        }
        optionList = itemOptions.get(194);
        if (optionList != null && optionList.size() >= 3) {
            options[109] += options[194];
        }
        if (options[109] > 0) {
            max_mp += Utils.percentOf(max_mp, options[109]);
            max_hp += Utils.percentOf(max_hp, options[109]);
            max_damage += Utils.percentOf(max_damage, options[109]);
        }
        int point = optionZones[106] + optionZones[191];
        if (point > 0) {
            max_mp -= Utils.percentOf(max_mp, point);
            max_hp -= Utils.percentOf(max_hp, point);
            max_damage -= Utils.percentOf(max_damage, point);
        }
        int point_plant = 0;
        if (zone != null && zone.map.template.planet != MapPlanet.EARTH) {
            if (zone.map.template.planet == MapPlanet.NAMEK) {
                if (options[79] == 0) {
                    point_plant = gender == 1 ? 1 : 2;
                }
            } else if (zone.map.template.planet == MapPlanet.COLD) {
                if (options[72] == 0) {
                    point_plant = 3;
                }
            } else if (zone.map.template.planet == MapPlanet.FIRE) {
                if (options[73] == 0) {
                    point_plant = 3;
                }
            }
        }
        if (point_plant > 0) {
            max_hp -= Utils.percentOf(max_hp, point_plant * 15);
            max_mp -= Utils.percentOf(max_mp, point_plant * 15);
            max_damage -= Utils.percentOf(max_damage, point_plant * 15);
        }
        this.pointPlant = point_plant;
        this.upgrade = upgrade;
        this.speed = 12 + bonus_speed / 100;
        if (optionZones[170] > 0) {
            this.speed -= this.speed * optionZones[170] / 100;
        }
        this.maxHp = max_hp;
        if (this.hp > this.maxHp) {
            this.hp = this.maxHp;
        }
        this.maxMp = max_mp;
        if (this.mp > this.maxMp) {
            this.mp = this.maxMp;
        }
        this.damage = max_damage;
        if (this.options[112] != options[112]) {
            for (Skill skill : skills) {
                skill.coolDownReduction = options[112];
            }
            service.setCoolDownReduction(options[112]);
        }
        this.options = options;
        long pointPro = baseHp + baseConstitution + baseMp + baseDamage;
        pointPro += getTotalPoint(itemsBody);
        pointPro += getTotalPoint(itemsOther);
        pointPro += getTotalPoint(itemsPet);
        if (disciple != null) {
            pointPro += getTotalPoint(disciple.itemsBody);
            pointPro += getTotalPoint(disciple.itemsOther);
        }
        this.pointPro = pointPro;
    }

    public long getTotalPoint(Item[] items) {
        if (items == null) {
            return 0;
        }
        long point = 0;
        for (Item item : items) {
            if (item != null) {
                point += item.getTotalOption();
            }
        }
        return point;
    }

    public void refreshPart() {
        int head;
        int body;
        int mount = -1;
        int medal = -1;
        int aura = -1;
        int bag = -1;
        switch (gender) {
            case 0:
                head = 5;
                body = 6;
                break;
            case 1:
                head = 3;
                body = 7;
                break;
            default:
                head = 4;
                body = 8;
                break;
        }
        List<Item> items;
        if (isInSurvival()) {
            items = Arrays.stream(itemsSurvival).collect(Collectors.toList());
        } else {
            items = Arrays.stream(itemsBody).collect(Collectors.toList());
        }
        List<Effect> effectList = getEffects();
        if (isHaveEffect(effectList, EffectName.HOA_DA)) {
            head = 319;
            body = 320;
        } else if (isHaveEffect(effectList, EffectName.HOA_SOCOLA)) {
            head = 327;
            body = 328;
        } else if (isHaveEffect(effectList, EffectName.BIEN_KHI)) {
            head = 133;
            body = 134;
            Effect effect = effectList.stream().filter(e -> e.template.id == EffectName.BIEN_KHI).findFirst().orElse(null);
            if (effect != null && effect.paramOption == 1) {
                head = 237;
                body = 238;
            }
        } else if (isHaveEffect(effectList, EffectName.HOA_KHONG_LO)) {
            head = 131;
            body = 132;
        } else if (isFusion(effectList) && !isHideMark) {
            if (gender == 1) {
                if (typeFusion == 2) {
                    head = 389;
                    body = 390;
                } else {
                    head = 127;
                    body = 128;
                }
            } else {
                if (typeFusion == 0) {
                    head = 129;
                    body = 130;
                } else if (typeFusion == 1) {
                    head = 125;
                    body = 126;
                } else if (typeFusion == 2) {
                    head = 391;
                    body = 392;
                }
            }
        } else {
            Item item = items.get(ItemType.TYPE_AO);
            boolean isFull = true;
            for (int i = 0; i < 8; i++) {
                if (itemsOther[i] == null) {
                    isFull = false;
                    break;
                }
            }
            if (isFull) {
                if (itemsOther[ItemType.TYPE_AO] != null) {
                    item = itemsOther[ItemType.TYPE_AO];
                }
            }
            if (item != null) {
                body = item.template.body;
                if (item.template.head != -1) {
                    head = item.template.head;
                }
            }
            if (!isHideMark || isFusion(effectList)) {
                item = items.get(ItemType.TYPE_AVATAR);
                if (item != null) {
                    if (item.template.head != -1) {
                        head = item.template.head;
                    }
                    if (item.template.body != -1) {
                        body = item.template.body;
                    }
                }
            }
        }
        Item item = items.get(ItemType.TYPE_THU_CUOI);
        if (item != null) {
            mount = item.template.mount;
        }
        item = items.get(ItemType.TYPE_MEDAL);
        if (item != null) {
            medal = item.template.medal;
        }
        item = items.get(14); // item bag
        if (item != null) {
            bag = item.template.bag;
        }
        item = items.get(16); // aura
        if (item != null) {
            aura = item.template.aura;
        }
        if (bagLoot != -1) {
            bag = bagLoot;
        }
        if (zone instanceof ZoneFlagWar) {
            aura = -1;
            mount = -1;
            medal = -1;
        }
        this.head = head;
        this.body = body;
        this.mount = mount;
        this.medal = medal;
        this.bag = bag;
        this.aura = aura;
    }

    public void sortBag() {
        sortArrayItem(itemsBag);
    }

    public void sortBox() {
        sortArrayItem(itemsBox);
    }

    public void upBasePoint(Message message) {
        try {
            if (isDead()) {
                return;
            }
            if (taskMain != null && (taskMain.template.id < 1 || (taskMain.template.id == 1 && taskMain.index <= 1))) {
                addInfo(INFO_RED, "Bạn cần phải làm nhiệm vụ trước");
                return;
            }
            int index = message.reader().readByte();
            int point = message.reader().readShort();
            if (index < 0 || index > 3 || point <= 0) {
                return;
            }
            long potential = getPotentialToUp(index, point);
            if (this.potential < potential) {
                addInfo(INFO_RED, String.format("Cần ít nhất %s tiềm năng", Utils.getMoneys(potential)));
                return;
            }
            switch (index) {
                case 0:
                    if (baseDamage + point > baseMp + 1000 || baseDamage + point > baseHp + 1000) {
                        addInfo(INFO_RED, Language.CANT_UP_DAMAGE);
                        return;
                    }
                    baseDamage += point;
                    break;

                case 1:
                    if (baseHp + point > baseMp + 1000 || baseHp + point > baseDamage + 1000) {
                        addInfo(INFO_RED, Language.CANT_UP_HP);
                        return;
                    }
                    baseHp += point;
                    break;

                case 2:
                    if (baseMp + point > baseHp + 1000 || baseMp + point > baseDamage + 1000) {
                        addInfo(INFO_RED, Language.CANT_UP_MP);
                        return;
                    }
                    baseMp += point;
                    break;

                case 3:
                    baseConstitution += point;
                    break;
            }
            this.potential -= potential;
            service.setInfo();
            service.setBaseInfo(index);
            if (taskMain != null && taskMain.template.id == 1 && taskMain.index == 2) {
                nextTaskIndex();
                int itemId = 0;
                if (gender == 1) {
                    itemId = 4;
                } else if (gender == 2) {
                    itemId = 8;
                }
                Item reward = ItemManager.getInstance().createItem(itemId, true);
                reward.randomParam(-15, 15);
                addItem(reward);
                addInfo(INFO_YELLOW, String.format("Bạn nhận được %s", reward.template.name));
            }
        } catch (Exception ex) {
            logger.error("upBasePoint", ex);
        }
    }

    public void discipleInfo(Message message) {
        if (disciple == null) {
            return;
        }
        try {
            int type = message.reader().readByte();
            if (type == MessageDiscipleInfoName.POWER_INFO) {
                upBasePointForDisciple(message.reader().readByte(), message.reader().readShort());
            } else if (type == MessageDiscipleInfoName.DISCIPLE_STATUS) {
                List<Command> commandList = new ArrayList<>();
                String[] status = {"Đi theo", "Bảo vệ", "Tấn công", "Về nhà", "Hợp thể"};
                for (int i = 0; i < status.length; i++) {
                    commandList.add(new Command(CommandName.CHANGE_STATUS_DISCIPLE, status[i], this, i));
                }
                createMenu(NpcName.ME, "", commandList);
            }
        } catch (Exception ex) {
            logger.error("discipleInfo", ex);
        }
    }

    public void upBasePointForDisciple(int index, int point) {
        try {
            if (disciple.isDead()) {
                addInfo(INFO_RED, "Không thể thực hiện khi đệ tử đang kiệt sức");
                return;
            }
            if (index < 0 || index > 3 || point <= 0) {
                return;
            }
            long potential = disciple.getPotentialToUp(index, point);
            if (disciple.potential < potential) {
                addInfo(INFO_RED, String.format("Cần ít nhất %s tiềm năng", Utils.getMoneys(potential)));
                return;
            }
            switch (index) {
                case 0:
                    if (disciple.baseDamage + point > disciple.baseMp + 1000 || disciple.baseDamage + point > disciple.baseHp + 1000) {
                        addInfo(INFO_RED, Language.CANT_UP_DAMAGE);
                        return;
                    }
                    disciple.baseDamage += point;
                    break;

                case 1:
                    if (disciple.baseHp + point > disciple.baseMp + 1000 || disciple.baseHp + point > disciple.baseDamage + 1000) {
                        addInfo(INFO_RED, Language.CANT_UP_HP);
                        return;
                    }
                    disciple.baseHp += point;
                    break;

                case 2:
                    if (disciple.baseMp + point > disciple.baseHp + 1000 || disciple.baseMp + point > disciple.baseDamage + 1000) {
                        addInfo(INFO_RED, Language.CANT_UP_MP);
                        return;
                    }
                    disciple.baseMp += point;
                    break;

                case 3:
                    disciple.baseConstitution += point;
                    break;
            }
            disciple.potential -= potential;
            service.discipleInfo(MessageDiscipleInfoName.POWER_INFO);
            service.discipleInfo(MessageDiscipleInfoName.BASE_INFO);
            disciple.refreshInfo();
            service.discipleInfo(MessageDiscipleInfoName.POINT);
        } catch (Exception ex) {
            logger.error("upBasePointForDisciple", ex);
        }
    }

    public void upSkillPoint(Message message) {
        try {
            if (isDead() || pointSkill < 0) {
                return;
            }
            if (taskMain != null && (taskMain.template.id < 1 || (taskMain.template.id == 1 && taskMain.index == 0))) {
                addInfo(INFO_RED, "Bạn cần phải làm nhiệm vụ trước");
                return;
            }
            int skillTemplateId = message.reader().readByte();
            Skill skill = getSkill(skillTemplateId);
            if (skill == null) {
                return;
            }
            if (level < skill.template.levelRequire[0]) {
                addInfo(INFO_RED, Language.LEVEL_NOT_ENOUGH);
                return;
            }
            if (skill.level >= skill.template.maxLevel[0]) {
                return;
            }
            if (skill.level == 0) {
                if (skill.template.id == 33 || skill.template.id == 34 || skill.template.id == 35 || skill.template.id == 36) {
                    addInfo(INFO_RED, String.format("Cần sử dụng %s để học kỹ năng này", skill.getName().replace("Chiêu", "Sách")));
                    return;
                }
                if (skill.template.optionRequire == -1 && skill.template.levelRequire[0] >= 30) {
                    addInfo(INFO_RED, String.format("Cần sử dụng %s để học kỹ năng này", skill.getName().replace("Chiêu", "Sách")));
                    return;
                }
            }
            if (skill.template.optionRequire != -1 && this.options[skill.template.optionRequire] == 0) {
                addInfo(INFO_RED, "Cần sử dụng trang bị hỗ trợ sử dụng kỹ năng này");
                return;
            }
            int[] require = skill.getQuantityItem();
            if (require[0] > 0) {
                int quantity = getQuantityItemInBag(ItemName.BI_KIP_KY_NANG);
                if (quantity < require[0]) {
                    addInfo(INFO_RED, String.format("Bạn còn thiếu %d Bí kíp kỹ năng", require[0] - quantity));
                    return;
                }
                quantity = getQuantityItemInBag(ItemName.LOI_KI_NANG);
                if (require[1] > 0 && quantity < require[1]) {
                    addInfo(INFO_RED, String.format("Bạn còn thiếu %d Lõi kỹ năng", require[1] - quantity));
                    return;
                }
                removeQuantityItemBagById(ItemName.BI_KIP_KY_NANG, require[0]);
                if (require[1] > 0) {
                    removeQuantityItemBagById(ItemName.LOI_KI_NANG, require[1]);
                }
            }
            skill.level++;
            pointSkill--;
            service.refreshSkill(0, skill);
            if (!skill.template.isProactive) {
                service.setInfo();
            }
            if (taskMain != null && taskMain.template.id == 1 && taskMain.index == 1) {
                nextTaskIndex();
            }
        } catch (Exception ex) {
            logger.error("upSkillPoint", ex);
        }
    }

    public void upPointUpgradeSkill(Skill skill, int point) {
        try {
            if (!skill.template.isProactive) {
                skill.point += point;
                service.refreshSkill(2, skill);
                return;
            }
            if (skill.point >= skill.getPointUpgrade() || skill.upgrade == 0 || skill.upgrade >= skill.template.maxLevel[1]) {
                return;
            }
            if (isHaveEffect(EffectName.MO_GIOI_HAN_SUC_MANH)) {
                return;
            }
            skill.point += point;
            service.refreshSkill(2, skill);
        } catch (Exception ex) {
            logger.error("upPointUpgradeSkill", ex);
        }
    }

    public void saveKeySkill(Message message) {
        try {
            int templateId = message.reader().readByte();
            int index = message.reader().readByte();
            if (index <= 0 || index >= keysSkill.length) {
                return;
            }
            Skill skill = getSkill(templateId);
            if (skill == null) {
                return;
            }
            if (!skill.template.isProactive || skill.level <= 0) {
                return;
            }
            keysSkill[index] = skill;
            service.saveKeySkill(templateId, index);
        } catch (Exception ex) {
            logger.error("saveKeySkill", ex);
        }
    }

    public void intrinsic(Message message) {
        try {
            if (level < 20) {
                addInfo(INFO_RED, "Yêu cầu cấp độ từ 20 trở lên");
                return;
            }
            int templateId = message.reader().readByte();
            Intrinsic intrinsic = intrinsics.get(templateId);
            if (intrinsic == null) {
                return;
            }
            List<Command> commandList = new ArrayList<>();
            commandList.add(new Command(CommandName.CANCEL, "Đóng", this));
            commandList.add(new Command(CommandName.MO_NOI_TAI, String.format("%s\n%s xu", intrinsic.param == 0 ? "Mở" : "Thay đổi", Utils.formatNumber(intrinsic.template.priceCoin)), this, templateId, 0));
            commandList.add(new Command(CommandName.MO_NOI_TAI, String.format("%s\n%s KC", intrinsic.param == 0 ? "Mở" : "Thay đổi", Utils.formatNumber(intrinsic.template.priceDiamond)), this, templateId, 1));
            StringBuilder content = new StringBuilder();
            content.append(intrinsic.template.name.replace("#", intrinsic.param + "")).append("\n");
            if (intrinsic.param == 0) {
                content.append(String.format("Tối thiểu %d%%, tối đa %d%%", intrinsic.template.min, intrinsic.template.max)).append("\n");
                content.append("Bạn có muốn mở bản năng này không?");
            } else {
                content.append("Bạn có muốn thay đổi bản năng này không?");
            }
            createMenu(NpcName.ME, content.toString(), commandList);
        } catch (Exception ex) {
            logger.error("saveKeySkill", ex);
        }
    }

    public void buyItem(Message message) {
        lockAction.lock();
        try {
            if (isDead()) {
                return;
            }
            if (Server.getInstance().isInterServer()) {
                addInfo(INFO_RED, Language.CANCEL_ACTION_WHEN_SERVER_IS_INTER_SERVER);
                return;
            }
            if (isTrading()) {
                addInfo(INFO_RED, Language.CANCEL_ACTION_WHEN_TRADE);
                return;
            }
            if (isProtect) {
                addInfo(Player.INFO_RED, Language.TAI_KHOAN_DANG_DUOC_BAO_VE);
                return;
            }
            if (isBagFull()) {
                addInfo(INFO_RED, Language.ME_BAG_FULL);
                return;
            }
            if (itemsShop == null || itemsShop.isEmpty()) {
                return;
            }
            int tabIndex = message.reader().readByte();
            if (tabIndex < 0 || tabIndex >= itemsShop.size()) {
                return;
            }
            int itemShopId = message.reader().readInt();
            int quantity = message.reader().readInt();
            if (quantity < 1 || quantity > 999) {
                return;
            }
            ItemShop itemShop = new ArrayList<>(itemsShop.values()).get(tabIndex).stream().filter(i -> i.id == itemShopId).findFirst().orElse(null);
            if (itemShop == null) {
                return;
            }
            if (itemShop.template.id != 614 && itemShop.template.id != 615) {
                if (level < itemShop.template.levelRequire && !itemShop.isRepurchase) {
                    addInfo(INFO_RED, "Cấp độ chưa đạt yêu cầu");
                    return;
                }
            } else {
                if (disciple == null) {
                    addInfo(INFO_RED, "Bạn chưa có đệ tử");
                    return;
                }
                if (disciple.level < itemShop.template.levelRequire) {
                    addInfo(INFO_RED, "Cấp độ đệ tử chưa đạt yêu cầu");
                    return;
                }
            }
            if ((!itemShop.template.isUpToUp || itemShop.isRepurchase || itemShop.isSaleCard) && quantity > 1) {
                return;
            }
            long price = quantity * (long) itemShop.price;
            if (!isEnoughMoney(itemShop.typePrice, price)) {
                return;
            }
            if (itemShop.typePrice == TypePrice.DIAMOND) {
                ItemOption option = itemShop.getOption(74);
                if (option != null) {
                    int q = getQuantityItemInBag(ItemName.THE_KIM_CUONG);
                    int require = option.param * quantity;
                    if (q < require) {
                        addInfo(INFO_RED, String.format("Bạn còn thiếu %d thẻ kim cương", require - q));
                        return;
                    }
                }
            }
            if (itemShop.template.id == ItemName.CHIEN_THUYEN_TENIS) {
                if (spaceship == 1) {
                    addInfo(INFO_RED, "Chiến thuyền Tenis đã đạt cấp tối đa");
                    return;
                }
                spaceship = 1;
                buyItem(itemShop, price);
                addInfo(INFO_YELLOW, "Đã nâng cấp Chiến thuyền Tenis");
                return;
            }
            if (itemShop.isRepurchase) {
                /*Item item = itemShop.cloneItem();
                if (addItem(item)) {
                    buyItem(itemShop);
                    if (item.quantity > 1) {
                        addInfo(INFO_YELLOW, String.format("Bạn nhận được x%d %s", item.quantity, item.template.name));
                    } else {
                        addInfo(INFO_YELLOW, String.format("Bạn nhận được %s", item.template.name));
                    }
                    itemsShop.clear();
                    Shop shop = ShopManager.getInstance().shops.get(ShopType.SHOP_REPURCHASE);
                    if (shop != null) {
                        shop.show(this);
                    }
                }*/
                return;
            }
            if (itemShop.template.id == ItemName.VE_BAN_DOANH) {
                if (countBarrack > 0) {
                    addInfo(INFO_RED, "Bạn vẫn còn lượt tham gia Bản doanh Red");
                    return;
                }
                if (numBuyBarrack > 1) {
                    addInfo(INFO_RED, "Bạn đã hết lượt mua trong ngày");
                    return;
                }
                if (itemShop.typePrice.ordinal() == numBuyBarrack) {
                    countBarrack++;
                    numBuyBarrack++;
                    buyItem(itemShop, price);
                    service.setCountBarrack();
                    addInfo(INFO_YELLOW, String.format("Số lần tham gia Bản doanh Red của bạn là %d", countBarrack));
                }
                return;
            }
            if (itemShop.template.id == ItemName.BUA_MO_RONG_TUI) {
                if (itemsBag.length >= 121) {
                    addInfo(INFO_RED, "Túi đồ đã được mở rộng tối đa");
                    return;
                }
                buyItem(itemShop, price);
                expandBag(6);
                return;
            }
            if (itemShop.template.id == ItemName.BUA_MO_RONG_RUONG) {
                if (itemsBox.length >= 121) {
                    addInfo(INFO_RED, "Rương đồ đã được mở rộng tối đa");
                    return;
                }
                buyItem(itemShop, price);
                expandBox(6);
                return;
            }
            if (itemShop.template.type == ItemType.TYPE_AMULET) {
                buyItem(itemShop, price);
                long time = itemShop.getParam(23) * 60000L;
                int effectId = itemShop.getEffectTemplateId();
                Effect effect = findEffectByTemplateId(effectId);
                if (effect == null) {
                    effect = new Effect(this, effectId, time);
                    addEffect(effect);
                } else {
                    effect.endTime += time;
                    effect.time += time;
                }
                addInfo(INFO_YELLOW, String.format("Thời gian %s được gia hạn lên %s", itemShop.template.name, Utils.formatTime(effect.time)));
                Shop shop = ShopManager.getInstance().shops.get(ShopType.SHOP_AMULET_TIME);
                if (shop != null) {
                    shop.show(this);
                }
                return;
            }
            if (itemShop.template.id == ItemName.HUY_HIEU_BANG_HOI_CAP_6) {
                if (clan == null) {
                    addInfo(INFO_RED, "Hiện tại chưa có bang hội");
                    return;
                }
                if (clan.level < 6) {
                    addInfo(INFO_RED, "Cấp độ bang hội chưa đạt yêu cầu");
                    return;
                }
                if (itemShop.options.stream().anyMatch(o -> o.template.id == 190)) {
                    Item item = ItemManager.getInstance().createItem(itemShop.template.id, 1, false);
                    item.createOptionSpec(5, 5);
                    item.options.add(new ItemOption(50, 7));
                    if (addItem(item)) {
                        buyItem(itemShop, price);
                        addInfo(INFO_YELLOW, String.format("Bạn nhận được %s", item.template.name));
                    }
                    return;
                }
            }
            if (itemShop.template.type == ItemType.TYPE_CLAN) {
                if (clan == null) {
                    addInfo(INFO_RED, "Hiện tại chưa có bang hội");
                    return;
                }
                int[] level;
                if (itemShop.template.id == ItemName.LENH_BAI_CAP_2) {
                    level = new int[]{2, 2};
                } else if (itemShop.template.id == ItemName.LENH_BAI_CAP_3) {
                    level = new int[]{3, 3};
                } else if (itemShop.template.id == ItemName.LENH_BAI_CAP_4) {
                    level = new int[]{4, 4};
                } else if (itemShop.template.id == ItemName.LENH_BAI_CAP_5) {
                    level = new int[]{5, 5};
                } else if (itemShop.template.id == ItemName.LENH_BAI_CAP_6) {
                    level = new int[]{5, 6};
                } else if (itemShop.template.id == ItemName.LENH_BAI_CAP_7) {
                    level = new int[]{5, 7};
                } else if (itemShop.template.id == ItemName.LENH_BAI_CAP_8) {
                    level = new int[]{5, 8};
                } else if (itemShop.template.id == ItemName.LENH_BAI_CAP_9) {
                    level = new int[]{5, 9};
                } else if (itemShop.template.id == ItemName.LENH_BAI_CAP_10) {
                    level = new int[]{5, 10};
                } else {
                    level = new int[]{1, 1};
                }
                if (level[1] > clan.level) {
                    addInfo(INFO_RED, "Cấp độ bang hội chưa đạt yêu cầu");
                    return;
                }
                Item item = ItemManager.getInstance().createItem(itemShop.template.id, 1, false);
                item.createOptionSpec(level[0], level[1]);
                item.options.add(new ItemOption(50, 7));
                if (addItem(item)) {
                    buyItem(itemShop, price);
                    addInfo(INFO_YELLOW, String.format("Bạn nhận được %s", item.template.name));
                }
                return;
            }
            if (itemShop.template.id == ItemName.LENH_BAI_BANG_HOI) {
                if (clan == null) {
                    addInfo(INFO_RED, "Hiện tại chưa có bang hội");
                    return;
                }
                if (clan.leaderId != this.id) {
                    addInfo(INFO_RED, "Tính năng dành cho bang chủ");
                    return;
                }
                int slot = clan.bonusSlot;
                if (slot >= clan.level) {
                    addInfo(INFO_RED, "Không thể sử dụng thêm");
                    return;
                }
                GameRepository.getInstance().clanData.setBonusSlot(clan.id, slot + 1);
                clan.bonusSlot = slot + 1;
                buyItem(itemShop, price);
                addInfo(INFO_YELLOW, "Giới hạn thành viên bang hiện tại là " + clan.getMaxMember());
                return;
            }
            if (itemShop.template.id == ItemName.KHUYEN_TAI) {
                Item item = ItemManager.getInstance().createItem(itemShop.template.id, 1, false);
                item.createOptionSpec(3, 2);
                item.options.add(new ItemOption(50, 7));
                if (addItem(item)) {
                    buyItem(itemShop, price);
                    addInfo(INFO_YELLOW, String.format("Bạn nhận được %s", item.template.name));
                }
                return;
            }
            if (!itemShop.isItemBody() || itemShop.typePrice == TypePrice.POINT_EVENT) {
                Item item = itemShop.cloneItem();
                item.quantity = quantity * itemShop.quantity;
                for (ItemOption option : item.options) {
                    if (option.template.id == 24) {
                        option.param *= quantity;
                        item.quantity = 1;
                        break;
                    }
                }
                ItemOption option = itemShop.getOption(74);
                if (option != null) {
                    item.removeOption(74); // thẻ kim cương
                }
                if (itemShop.typePrice == TypePrice.POINT_EVENT && item.template.id == ItemName.SAO_PHA_LE_DANH_BONG) {
                    item.options.clear();
                    item.options.add(item.createOptionCrystal(false, true));
                    item.setExpiry(7);
                }
                if (addItem(item)) {
                    buyItem(itemShop, price);
                    if (option != null) {
                        removeQuantityItemBagById(ItemName.THE_KIM_CUONG, option.param * quantity);
                    }
                    if (item.quantity > 1) {
                        addInfo(INFO_YELLOW, String.format("Bạn nhận được %d %s", item.quantity, item.template.name));
                    } else {
                        addInfo(INFO_YELLOW, String.format("Bạn nhận được %s", item.template.name));
                    }
                }
                return;
            }
            if (itemShop.isSaleCard) {
                if (getQuantityItemInBag(ItemName.THE_GIAM_GIA) == 0) {
                    addInfo(INFO_RED, "Không tìm thấy Thẻ giảm giá");
                    return;
                } else {
                    removeQuantityItemBagById(ItemName.THE_GIAM_GIA, 1);
                }
            }
            Item item = itemShop.cloneParam(-15, 15);
            item.quantity = quantity * itemShop.quantity;
            if (item.template.type == ItemType.TYPE_VONG_KIM_HAM) {
                item.options.add(new ItemOption(24, 0));
            }
            if (addItem(item)) {
                buyItem(itemShop, price);
                addInfo(INFO_YELLOW, String.format("Bạn nhận được %s", item.template.name));
                upPointAchievement(5, 1);
            }
        } catch (Exception ex) {
            logger.error("buyItem", ex);
        } finally {
            lockAction.unlock();
        }
    }

    public void expandBag(int num) {
        int size = itemsBag.length + num;
        Item[] items = new Item[size];
        System.arraycopy(itemsBag, 0, items, 0, itemsBag.length);
        itemsBag = items;
        service.setItemBag();
        addInfo(INFO_YELLOW, String.format("Túi đồ đã được mở rộng lên %d ô", itemsBag.length));
    }

    public void expandBox(int num) {
        int size = itemsBox.length + num;
        Item[] items = new Item[size];
        System.arraycopy(itemsBox, 0, items, 0, itemsBox.length);
        itemsBox = items;
        service.setItemBag();
        addInfo(INFO_YELLOW, String.format("Rương đồ đã được mở rộng lên %d ô", itemsBox.length));
    }

    public void buyItem(ItemShop itemShop, long price) {
        long[] coins = {0, 0};
        if (itemShop.typePrice == TypePrice.COIN) {
            coins[0] = this.xu;
            upXu(-price);
            coins[1] = this.xu;
        } else if (itemShop.typePrice == TypePrice.COIN_LOCK) {
            coins[0] = this.xuKhoa;
            downMoney(TypePrice.COIN_LOCK, price);
            coins[1] = this.xuKhoa;
        } else if (itemShop.typePrice == TypePrice.DIAMOND) {
            coins[0] = this.diamond;
            upDiamond(-(int) price);
            coins[1] = this.diamond;
            if (Event.isEvent()) {
                upParamMissionEvent((int) price);
            }
        } else if (itemShop.typePrice == TypePrice.RUBY) {
            coins[0] = this.ruby;
            downMoney(TypePrice.RUBY, price);
            coins[1] = this.ruby;
            if (Event.isEvent() && coins[0] < price) {
                upParamMissionEvent((int) (price - coins[0]));
            }
        } else if (itemShop.typePrice == TypePrice.POINT_BARRACK) {
            coins[0] = this.pointBarrack;
            pointBarrack -= (int) price;
            service.setPointShop(pointBarrack);
            coins[1] = this.pointBarrack;
        } else if (itemShop.typePrice == TypePrice.POINT_ACTIVE) {
            coins[0] = this.pointActive;
            pointActive -= (int) price;
            service.setPointShop(pointActive);
            service.setPointActive();
            coins[1] = this.pointActive;
        } else if (itemShop.typePrice == TypePrice.POINT_EVENT) {
            coins[0] = this.pointRewardEvent;
            pointRewardEvent -= (int) price;
            service.setPointShop(pointRewardEvent);
            coins[1] = this.pointRewardEvent;
        } else if (itemShop.typePrice == TypePrice.POINT_SPACESHIP) {
            coins[0] = this.pointSpaceship;
            pointSpaceship -= (int) price;
            service.setPointShop(pointSpaceship);
            coins[1] = this.pointSpaceship;
        } else if (itemShop.typePrice == TypePrice.POINT_FLAG_WAR) {
            coins[0] = this.pointFlagWar;
            pointFlagWar -= (int) price;
            service.setPointShop(pointFlagWar);
            coins[1] = this.pointFlagWar;
        }
        HistoryBuyItemData history = new HistoryBuyItemData(this, itemShop, coins);
        history.save();
    }

    public void addItem(Item item, boolean isNotification) {
        if (addItem(item) && isNotification) {
            if (item.quantity > 1) {
                addInfo(INFO_YELLOW, String.format("Bạn nhận được %s %s", Utils.getMoneys(item.quantity), item.template.name));
            } else {
                addInfo(INFO_YELLOW, String.format("Bạn nhận được %s", item.template.name));
            }
        }
    }

    public boolean addItem(Item item) {
        if (item.template.type == ItemType.TYPE_XU) {
            upXu(item.quantity);
            return true;
        }
        if (item.template.type == ItemType.TYPE_DIAMOND) {
            upDiamond(item.quantity);
            return true;
        }
        if (item.template.id == ItemName.XU_KHOA) {
            upXuKhoa(item.quantity);
            return true;
        }
        if (item.template.id == ItemName.RUBY) {
            upRuby(item.quantity);
            return true;
        }
        if (item.template.type == ItemName.EXP) {
            upPower(item.quantity);
            return true;
        }
        if (item.template.type == ItemType.TYPE_AMULET) {
            long time = item.getParam(23) * 60000L;
            int effectId = item.getEffectTemplateId();
            Effect effect = findEffectByTemplateId(effectId);
            if (effect == null) {
                effect = new Effect(this, effectId, time);
                effects.add(effect);

            } else {
                effect.endTime += time;
                effect.time += time;
            }
            return true;
        }
        if (item.template.isUpToUp) {
            int max_quantity = item.getMaxQuantity();
            for (int i = 0; i < itemsBag.length; i++) {
                Item value = itemsBag[i];
                if (value == null || value.template.id != item.template.id || value.isLock != item.isLock) {
                    continue;
                }

                boolean flag = false;
                // check all option up to up có bị full không
                for (ItemOption option_i : value.options) {
                    int optionId = option_i.template.id;
                    if (optionId == 24) {
                        for (ItemOption option : item.options) {
                            if (option.template.id == optionId) {
                                flag = true;
                                if (option_i.param + option.param * item.quantity > max_quantity) {
                                    return false;
                                } else {
                                    break;
                                }
                            }
                        }
                    }
                }

                if (flag) {
                    // không full sẽ up option
                    for (ItemOption option_i : value.options) {
                        int optionId = option_i.template.id;
                        if (optionId == 24) {
                            for (ItemOption option : item.options) {
                                if (option.template.id == optionId) {
                                    option_i.param += option.param * item.quantity;
                                    break;
                                }
                            }
                        }
                    }

                    // up option sẽ mặc định return
                    service.refreshItemBag(i);
                    return true;
                }

                // up quantity
                int count = 0;
                int day = -1;
                if (value.options.size() == item.options.size()) {
                    for (ItemOption option_i : value.options) {
                        for (ItemOption option : item.options) {
                            if (option.template.id == option_i.template.id) {
                                if (option.template.id == 50) {
                                    count++;
                                    day = Math.min(option.param, option_i.param);
                                } else if (option.param == option_i.param) {
                                    count++;
                                }
                            }
                        }
                    }
                } else {
                    count = -1;
                }
                if (count == value.options.size() && value.quantity + item.quantity <= max_quantity) {
                    value.quantity += item.quantity;
                    if (day != -1) {
                        value.setExpiry(day);
                    }
                    service.refreshQuantityItemBag(i, value.quantity);
                    return true;
                }
            }
        }
        for (int i = 0; i < itemsBag.length; i++) {
            if (itemsBag[i] == null) {
                itemsBag[i] = item;
                item.indexUI = i;
                service.refreshItemBag(i);
                return true;
            }
        }
        return false;
    }

    public void upXu(long xu) {
        if (xu == 0) {
            return;
        }
        this.xu += xu;
        if (this.xu < 0) {
            this.xu = 0;
        }
        service.setCoin();
    }

    public void upXuKhoa(long xu) {
        if (xu == 0) {
            return;
        }
        this.xuKhoa += xu;
        if (this.xuKhoa < 0) {
            this.xuKhoa = 0;
        }
        service.setCoinLock();
    }

    public void upDiamond(int diamond) {
        if (diamond == 0) {
            return;
        }
        this.diamond += diamond;
        if (this.diamond < 0) {
            this.diamond = 0;
        }
        service.setDiamond();
    }

    public void upRuby(int ruby) {
        if (ruby == 0) {
            return;
        }
        this.ruby += ruby;
        if (this.ruby < 0) {
            this.ruby = 0;
        }
        service.setRuby();
    }

    public boolean isEnoughMoney(TypePrice typePrice, long price) {
        switch (typePrice) {

            case COIN:
                if (xu < price) {
                    addInfo(INFO_RED, "Bạn còn thiếu " + Utils.getMoneys(price - xu) + " Xu");
                    return false;
                }
                return true;

            case DIAMOND:
                if (diamond < price) {
                    addInfo(INFO_RED, "Bạn còn thiếu " + Utils.getMoneys(price - diamond) + " Kim cương");
                    return false;
                }
                return true;

            case COIN_LOCK: {
                long total = xu + xuKhoa;
                if (total < price) {
                    addInfo(INFO_RED, "Bạn còn thiếu " + Utils.getMoneys(price - total) + " Xu khóa và Xu");
                    return false;
                }
                return true;
            }

            case RUBY: {
                long total = diamond + ruby;
                if (total < price) {
                    addInfo(INFO_RED, "Bạn còn thiếu " + Utils.getMoneys(price - total) + " Ruby và Kim cương");
                    return false;
                }
                return true;
            }

            case POINT_EVENT:
                if (pointRewardEvent < price) {
                    addInfo(INFO_RED, "Bạn còn thiếu " + Utils.getMoneys(price - pointRewardEvent) + " Điểm");
                    return false;
                }
                return true;

            case POINT_ACTIVE:
                if (pointActive < price) {
                    addInfo(INFO_RED, "Bạn còn thiếu " + Utils.getMoneys(price - pointActive) + " Điểm");
                    return false;
                }
                return true;

            case POINT_SPACESHIP:
                if (pointSpaceship < price) {
                    addInfo(INFO_RED, "Bạn còn thiếu " + Utils.getMoneys(price - pointSpaceship) + " Điểm");
                    return false;
                }
                return true;

            case POINT_BARRACK:
                if (pointBarrack < price) {
                    addInfo(INFO_RED, "Bạn còn thiếu " + Utils.getMoneys(price - pointBarrack) + " Điểm");
                    return false;
                }
                return true;

            case POINT_FLAG_WAR:
                if (pointFlagWar < price) {
                    addInfo(INFO_RED, "Bạn còn thiếu " + Utils.getMoneys(price - pointFlagWar) + " Điểm");
                    return false;
                }
                return true;
        }
        return false;
    }

    public void downMoney(TypePrice typePrice, long price) {
        switch (typePrice) {
            case COIN:
                upXu(-price);
                break;

            case DIAMOND:
                upDiamond(-(int) price);
                break;

            case COIN_LOCK: {
                if (price <= xuKhoa) {
                    upXuKhoa(-price);
                } else {
                    upXu(-(price - xuKhoa));
                    upXuKhoa(-xuKhoa);
                }
                break;
            }

            case RUBY: {
                if (price <= ruby) {
                    upRuby(-(int) price);
                } else {
                    upDiamond(-(int) (price - ruby));
                    upRuby(-ruby);
                }
                break;
            }
        }
    }

    public void upPower(long exp) {
        if (exp <= 0 || isLockExp()) {
            if (taskMain != null) {
                if (taskMain.template.id == 3 && taskMain.index == 1 && level >= 5) {
                    nextTaskIndex();
                    addInfo(INFO_YELLOW, "Hãy mau đi tiêu diệt lũ Giran");
                    return;
                }
                if (taskMain.template.id == 5 && taskMain.index == 1 && level >= 7) {
                    nextTaskIndex();
                    addInfo(INFO_YELLOW, "Hãy mau đi tiêu diệt Giran bố để lấy lại Ngọc rồng 7 sao");
                }
                if (taskMain.template.id == 10 && taskMain.index == 1 && level >= 20) {
                    nextTaskIndex();
                    addInfo(INFO_YELLOW, "Hãy mau đi hạ Yamcha");
                }
            }
            return;
        }
        ArrayList<Level> levels = Server.getInstance().levels;
        long maxPower = levels.get(level + 1).power;
        if (power >= maxPower) {
            if (level < levels.size() - 1) {
                levelUp();
            }
            return;
        }
        if (power + exp > maxPower) {
            exp = maxPower - power;
            if (exp <= 0) {
                return;
            }
        }
        power += exp;
        potential += exp;
        updatePowerTime = System.currentTimeMillis();
        service.upPower(exp);
        if (level < levels.size() - 1 && power >= maxPower) {
            levelUp();
        }
        if (taskMain != null) {
            if (taskMain.template.id == 3 && taskMain.index == 1 && level >= 5) {
                nextTaskIndex();
                addInfo(INFO_YELLOW, "Hãy mau đi tiêu diệt lũ Giran");
                return;
            }
            if (taskMain.template.id == 5 && taskMain.index == 1 && level >= 7) {
                nextTaskIndex();
                addInfo(INFO_YELLOW, "Hãy mau đi tiêu diệt Giran bố để lấy lại Ngọc rồng 7 sao");
            }
            if (taskMain.template.id == 10 && taskMain.index == 1 && level >= 20) {
                nextTaskIndex();
                addInfo(INFO_YELLOW, "Hãy mau đi hạ Yamcha");
            }
        }
    }

    public void levelUp() {
        level++;
        pointSkill++;
        service.levelUp();
    }

    public boolean isLockExp() {
        return level >= limitLevel || isHaveEffect(EffectName.BUA_NGUNG_DONG);
    }

    public void createMenu(int npcTemplateId, String chat, List<Command> commands) {
        this.commands.clear();
        this.commands.addAll(commands);
        service.createMenu(npcTemplateId, chat, commands);
    }

    public void startYesNo(String info, Command yes, Command no) {
        commands.clear();
        commands.add(yes);
        commands.add(no);
        service.startYesNo(info, yes, no);
    }

    public void viewTop() {
        List<Command> commands = new ArrayList<>();
        commands.add(new Command(CommandName.SHOW_TOP_PRO, "Sức chiến đấu", this));
        commands.add(new Command(CommandName.SHOW_TOP_POWER, "Sức mạnh", this));
        commands.add(new Command(CommandName.SHOW_TOP_DISCIPLE, "Đệ tử", this));
        if (Event.isEvent()) {
            commands.add(new Command(CommandName.SHOW_EVENT, "TOP\nSự kiện", this, 1, -1));
        }
        commands.add(new Command(CommandName.SHOW_TOP_CLAN, "Bang hội", this));
        commands.add(new Command(CommandName.SHOW_TOP_WEEK, "Đua top\ntuần", this));
        createMenu(NpcName.ME, "", commands);
    }

    public void viewLucky() {
        List<Command> commands = new ArrayList<>();
        commands.add(new Command(CommandName.O_MAY_MAN, "Ô may mắn\n(Kim cương)", this, 0));
        commands.add(new Command(CommandName.O_MAY_MAN, "Ô may mắn\n(Thẻ vận may)", this, 1));
        commands.add(new Command(CommandName.CHON_AI_DAY, "Chọn ai đây", this));
        createMenu(NpcName.ME, "", commands);
    }

    public void itemAction(Message message) {
        lockAction.lock();
        try {
            if (isDead()) {
                return;
            }
            if (isTrading()) {
                addInfo(INFO_RED, Language.CANCEL_ACTION_WHEN_TRADE);
                return;
            }
            if (zone == null || isInSurvival()) {
                addInfo(INFO_RED, Language.CANT_ACTION);
                return;
            }
            if (isProtect) {
                addInfo(Player.INFO_RED, Language.TAI_KHOAN_DANG_DUOC_BAO_VE);
                return;
            }
            int type = message.reader().readByte();
            int index = message.reader().readByte();
            switch (type) {
                case ItemActionName.USE_ITEM:
                    useItem(index);
                    break;

                case ItemActionName.BODY_TO_BAG:
                    itemBodyToBag(index);
                    break;

                case ItemActionName.THROW_ITEM:
                    itemBagToArea(index);
                    break;

                case ItemActionName.BAG_TO_BOX:
                    itemBagToBox(index);
                    break;

                case ItemActionName.BOX_TO_BAG:
                    itemBoxToBag(index);
                    break;

                case ItemActionName.SELL_ITEM:
                    sellItem(index);
                    break;

                case ItemActionName.BAG_TO_DISCIPLE:
                    itemBagToDisciple(index);
                    break;

                case ItemActionName.DISCIPLE_TO_BAG:
                    itemDiscipleToBag(index);
                    break;

                case ItemActionName.BAG_TO_PET:
                    itemBagToPet(index);
                    break;

                case ItemActionName.PET_TO_BAG:
                    itemPetToBag(index);
                    break;

                case ItemActionName.OTHER_TO_BAG:
                    itemOtherToBag(index);
                    break;

                case ItemActionName.OTHER_DISCIPLE_TO_BAG:
                    itemOtherDiscipleToBag(index);
                    break;
            }
        } catch (Exception ex) {
            logger.error("itemAction", ex);
        } finally {
            lockAction.unlock();
        }
    }

    public void itemBagToPet(int index) {
        if (index < 0 || index >= itemsBag.length) {
            return;
        }
        if (pet == null) {
            addInfo(INFO_RED, "Bạn chưa có thú nuôi");
            return;
        }
        Item item = itemsBag[index];
        if (item == null) {
            return;
        }
        if (!item.template.isPet) {
            addInfo(INFO_RED, "Thú nuôi không thể sử dụng vật phẩm này");
            return;
        }
        int type = item.getIndexPet();
        if (type == -1) {
            addInfo(INFO_RED, Language.ME_CANT_USE_EQUIP);
            return;
        }
        Item itemBody = itemsPet[type];
        itemsPet[type] = item.cloneItem();
        if (!itemsPet[type].isLock) {
            itemsPet[type].createOptionEquipPet();
            itemsPet[type].isLock = true;
        }
        itemsPet[type].indexUI = type;
        if (itemBody != null) {
            itemsBag[index] = itemBody.cloneItem();
            itemsBag[index].indexUI = index;
            service.refreshItemBag(index);
        } else {
            itemsBag[index] = null;
            service.setItemBag();
        }
        service.petInfo(MessagePetInfoName.ITEM_BODY);
        service.setInfo();
        if (zone != null) {
            zone.service.refreshHp(this);
        }
    }

    public void itemPetToBag(int index) {
        if (pet == null) {
            return;
        }
        if (index < 0 || index >= itemsPet.length) {
            return;
        }
        Item item = itemsPet[index];
        if (item == null) {
            return;
        }
        int indexEmpty = getIndexItemBagEmpty();
        if (indexEmpty == -1) {
            addInfo(INFO_RED, Language.ME_BAG_FULL);
            return;
        }
        itemsBag[indexEmpty] = item.cloneItem();
        itemsBag[indexEmpty].indexUI = indexEmpty;
        itemsPet[index] = null;
        service.petInfo(MessagePetInfoName.ITEM_BODY);
        service.refreshItemBag(indexEmpty);
        service.setInfo();
        if (zone != null) {
            zone.service.refreshHp(this);
        }
    }

    public void itemBagToDisciple(int index) {
        if (index < 0 || index >= itemsBag.length) {
            return;
        }
        if (disciple == null) {
            return;
        }
        if (isFusion()) {
            addInfo(INFO_RED, Language.CANCEL_ACTION_WHEN_FUSION);
            return;
        }
        Item item = itemsBag[index];
        if (item == null) {
            return;
        }
        if (!item.template.isDisciple) {
            addInfo(INFO_RED, "Đệ tử không thể sử dụng vật phẩm này");
            return;
        }
        if (!item.isItemBody()) {
            return;
        }
        if (item.template.gender != -1 && item.template.gender != disciple.gender) {
            addInfo(INFO_RED, Language.ME_CANT_USE_EQUIP);
            return;
        }
        if (item.template.levelRequire > disciple.level) {
            addInfo(INFO_RED, Language.ME_CANT_USE_EQUIP);
            return;
        }
        int type = item.getIndexBody();
        if (item.isItemRider()) {
            if (item.options.isEmpty()) {
                addInfo(INFO_RED, "Trang bị chưa được nâng cấp");
                return;
            }
            Item itemBody = disciple.itemsOther[type];
            disciple.itemsOther[type] = item.cloneItem();
            disciple.itemsOther[type].indexUI = type;
            disciple.itemsOther[type].isLock = true;
            if (itemBody != null) {
                itemsBag[index] = itemBody.cloneItem();
                itemsBag[index].indexUI = index;
                service.refreshItemBag(index);
            } else {
                itemsBag[index] = null;
                service.setItemBag();
            }
            service.discipleInfo(MessageDiscipleInfoName.ITEM_OTHER);
            disciple.refreshPart();
            service.discipleInfo(MessageDiscipleInfoName.ALL_PART);
            disciple.refreshInfo();
            service.discipleInfo(MessageDiscipleInfoName.POINT);
            if (disciple.zone != null) {
                disciple.zone.service.refreshHp(disciple);
                disciple.zone.service.refreshPlayerPart(disciple);
            }
            return;
        }
        Item itemBody = disciple.itemsBody[type];
        disciple.itemsBody[type] = item.cloneItem();
        disciple.itemsBody[type].indexUI = type;
        if (itemBody != null) {
            itemsBag[index] = itemBody.cloneItem();
            itemsBag[index].indexUI = index;
            service.refreshItemBag(index);
        } else {
            itemsBag[index] = null;
            service.setItemBag();
        }
        service.discipleInfo(MessageDiscipleInfoName.ITEM_BODY);
        if (item.isSkin()) {
            disciple.refreshPart();
            service.discipleInfo(MessageDiscipleInfoName.ALL_PART);
        }
        disciple.refreshInfo();
        service.discipleInfo(MessageDiscipleInfoName.POINT);
        if (disciple.zone != null) {
            disciple.zone.service.refreshHp(disciple);
            if (item.isSkin()) {
                disciple.zone.service.refreshPlayerPart(disciple);
            }
        }
    }

    public void itemDiscipleToBag(int index) {
        if (disciple == null) {
            return;
        }
        if (index < 0 || index >= disciple.itemsBody.length) {
            return;
        }
        if (isFusion()) {
            addInfo(INFO_RED, Language.CANCEL_ACTION_WHEN_FUSION);
            return;
        }
        Item item = disciple.itemsBody[index];
        if (item == null) {
            return;
        }
        int indexEmpty = getIndexItemBagEmpty();
        if (indexEmpty == -1) {
            addInfo(INFO_RED, Language.ME_BAG_FULL);
            return;
        }
        itemsBag[indexEmpty] = item.cloneItem();
        itemsBag[indexEmpty].indexUI = indexEmpty;
        disciple.itemsBody[index] = null;
        service.discipleInfo(MessageDiscipleInfoName.ITEM_BODY);
        service.refreshItemBag(indexEmpty);
        disciple.refreshInfo();
        service.discipleInfo(MessageDiscipleInfoName.POINT);
        if (item.isSkin()) {
            disciple.refreshPart();
            service.discipleInfo(MessageDiscipleInfoName.ALL_PART);
        }
        if (disciple.zone != null) {
            disciple.zone.service.refreshHp(disciple);
            if (item.isSkin()) {
                disciple.zone.service.refreshPlayerPart(disciple);
            }
        }
    }

    public void itemOtherDiscipleToBag(int index) {
        if (disciple == null) {
            return;
        }
        if (index < 0 || index >= disciple.itemsOther.length) {
            return;
        }
        if (isFusion()) {
            addInfo(INFO_RED, Language.CANCEL_ACTION_WHEN_FUSION);
            return;
        }
        Item item = disciple.itemsOther[index];
        if (item == null) {
            return;
        }
        int indexEmpty = getIndexItemBagEmpty();
        if (indexEmpty == -1) {
            addInfo(INFO_RED, Language.ME_BAG_FULL);
            return;
        }
        itemsBag[indexEmpty] = item.cloneItem();
        itemsBag[indexEmpty].indexUI = indexEmpty;
        disciple.itemsOther[index] = null;
        service.discipleInfo(MessageDiscipleInfoName.ITEM_OTHER);
        service.refreshItemBag(indexEmpty);
        disciple.refreshInfo();
        service.discipleInfo(MessageDiscipleInfoName.POINT);
        disciple.refreshPart();
        service.discipleInfo(MessageDiscipleInfoName.ALL_PART);
        if (disciple.zone != null) {
            disciple.zone.service.refreshHp(disciple);
            disciple.zone.service.refreshPlayerPart(disciple);
        }
    }

    public void sellItem(int index) {
        if (index < 0 || index >= itemsBag.length) {
            return;
        }
        Item item = itemsBag[index];
        if (item == null) {
            return;
        }
        if (item.template.type == ItemType.TYPE_TASK) {
            addInfo(INFO_RED, "Bạn không thể bán vật phẩm nhiệm vụ");
            return;
        }
        if (item.template.id == ItemName.TDLT) {
            addInfo(INFO_RED, "Không thể bán vật phẩm này");
            return;
        }
        String content;
        //long price = Math.max(ShopManager.getInstance().prices.getOrDefault(item.template.id, 50) / 10 * (long) item.quantity, 5L);
        long price = Math.max(5L * item.quantity, 5L);
        if (item.quantity > 1) {
            content = String.format("Bạn có chắc chắn muốn bán x%d %s với giá %s xu không? Sau khi bán không thể hoàn tác", item.quantity, item.template.name, Utils.getMoneys(price));
        } else {
            content = String.format("Bạn có chắc chắn muốn bán %s với giá %s xu không? Sau khi bán không thể hoàn tác", item.template.name, Utils.getMoneys(price));
        }
        startYesNo(content, new Command(CommandName.CONFIRM_SELL_ITEM, Language.YES, this, index), new Command(CommandName.CANCEL, Language.NO, this));
    }

    public void confirmSellItem(int index) {
        lockAction.lock();
        try {
            if (isTrading()) {
                addInfo(INFO_RED, Language.CANCEL_ACTION_WHEN_TRADE);
                return;
            }
            if (isProtect) {
                addInfo(Player.INFO_RED, Language.TAI_KHOAN_DANG_DUOC_BAO_VE);
                return;
            }
            if (index < 0 || index >= itemsBag.length) {
                return;
            }
            Item item = itemsBag[index];
            if (item == null) {
                return;
            }
            if (item.template.type == ItemType.TYPE_TASK) {
                addInfo(INFO_RED, "Bạn không thể bán vật phẩm nhiệm vụ");
                return;
            }
            if (item.template.id == ItemName.TDLT) {
                addInfo(INFO_RED, "Không thể bán vật phẩm này");
                return;
            }
            //long coin = Math.max(ShopManager.getInstance().prices.getOrDefault(item.template.id, 50) / 10 * (long) item.quantity, 5L);
            long coin = Math.max(5L * item.quantity, 5L);
            removeItemBag(index);
            upXuKhoa(coin);
            addInfo(INFO_YELLOW, String.format("Bạn nhận được %s Xu khóa", Utils.getMoneys(coin)));
        } finally {
            lockAction.unlock();
        }
    }

    public int getCountItemBagEmpty() {
        int num = 0;
        for (Item item : itemsBag) {
            if (item == null) {
                num++;
            }
        }
        return num;
    }

    public int getIndexItemBagEmpty() {
        for (byte i = 0; i < itemsBag.length; i++) {
            if (itemsBag[i] == null) {
                return i;
            }
        }
        return -1;
    }

    public int getIndexItemBoxEmpty() {
        for (byte i = 0; i < itemsBox.length; i++) {
            if (itemsBox[i] == null) {
                return i;
            }
        }
        return -1;
    }

    public int getQuantityItemInBag(int id) {
        int quantity = 0;
        for (Item item : itemsBag) {
            if (item != null && item.template.id == id) {
                quantity += item.quantity;
            }
        }
        return quantity;
    }

    public void removeQuantityItemBagById(int id, int quantity) {
        int num = quantity;
        for (int i = 0; i < itemsBag.length; i++) {
            Item item = itemsBag[i];
            if (item != null && item.template.id == id) {
                if (item.quantity > num) {
                    item.quantity -= num;
                    if (num == quantity) {
                        service.refreshQuantityItemBag(i, item.quantity);
                        return;
                    }
                } else {
                    num -= item.quantity;
                    item.quantity = 0;
                }
                if (num <= 0) {
                    break;
                }
            }
        }
        service.setItemBag();
    }

    public void removeQuantityItemBag(int index, int quantity) {
        if (index < 0 || index >= itemsBag.length) {
            return;
        }
        Item item = itemsBag[index];
        if (item == null) {
            return;
        }
        item.quantity -= quantity;
        if (item.quantity <= 0) {
            itemsBag[index] = null;
            service.setItemBag();
        } else {
            service.refreshQuantityItemBag(index, item.quantity);
        }
    }

    public void removeQuantityItemBag(Item item, int quantity) {
        if (item == null) {
            return;
        }
        item.quantity -= quantity;
        if (item.quantity <= 0) {
            itemsBag[item.indexUI] = null;
            service.setItemBag();
        } else {
            service.refreshQuantityItemBag(item.indexUI, item.quantity);
        }
    }

    public void removeItemBag(int index) {
        if (index < 0 || index >= itemsBag.length) {
            return;
        }
        itemsBag[index] = null;
        service.setItemBag();
    }

    public boolean isBagFull() {
        for (Item item : itemsBag) {
            if (item == null) {
                return false;
            }
        }
        return true;
    }

    private void itemBagToBox(int index) {
        if (index < 0 || index >= itemsBag.length) {
            return;
        }
        Item item = itemsBag[index];
        if (item == null) {
            return;
        }
        if (item.isExpiry()) {
            addInfo(INFO_RED, Language.CANT_PUT_ITEM_EXPIRY_FROM_BAG_TO_BOX);
            return;
        }
        if (item.template.type == ItemType.TYPE_VONG_KIM_HAM) {
            addInfo(INFO_RED, "Bạn không thể cất vật phẩm này vào rương");
            return;
        }
        int indexEmpty = getIndexItemBoxEmpty();
        if (indexEmpty == -1) {
            addInfo(INFO_RED, Language.ME_BOX_FULL);
            return;
        }
        itemsBox[indexEmpty] = item.cloneItem();
        itemsBox[indexEmpty].indexUI = indexEmpty;
        itemsBag[index] = null;
        service.setItemBox();
        service.setItemBag();
    }

    private void itemBoxToBag(int index) {
        if (index < 0 || index >= itemsBox.length) {
            return;
        }
        Item item = itemsBox[index];
        if (item == null) {
            return;
        }
        int indexEmpty = getIndexItemBagEmpty();
        if (indexEmpty == -1) {
            addInfo(INFO_RED, Language.ME_BAG_FULL);
            return;
        }
        itemsBag[indexEmpty] = item.cloneItem();
        itemsBag[indexEmpty].indexUI = indexEmpty;
        itemsBox[index] = null;
        service.setItemBox();
        service.setItemBag();
    }

    private void itemBagToArea(int index) {
        if (index < 0 || index >= itemsBag.length) {
            return;
        }
        Item item = itemsBag[index];
        if (item == null) {
            return;
        }
        addInfo(INFO_RED, String.format(Language.CANT_THROW_ITEM_TO_AREA, item.template.name));
    }

    private void itemBodyToBag(int index) {
        if (index < 0 || index >= itemsBody.length) {
            return;
        }
        Item item = itemsBody[index];
        if (item == null) {
            return;
        }
        int indexEmpty = getIndexItemBagEmpty();
        if (indexEmpty == -1) {
            addInfo(INFO_RED, Language.ME_BAG_FULL);
            return;
        }
        if (item.template.type == ItemType.TYPE_PET) {
            if (Arrays.stream(itemsPet).anyMatch(i -> i != null)) {
                addInfo(INFO_RED, "Bạn chưa tháo hết trang bị của pet");
                return;
            }
        }
        itemsBag[indexEmpty] = item.cloneItem();
        itemsBag[indexEmpty].indexUI = indexEmpty;
        itemsBody[index] = null;
        if (item.template.type == ItemType.TYPE_BONG_TAI && disciple != null && isFusion()) {
            fusion(0);
            typeFusion = 0;
            lastTimeFusion = System.currentTimeMillis();
        }
        service.refreshItemBag(indexEmpty);
        service.refreshItemBody(index);
        service.setInfo();
        service.setPart();
        if (zone != null) {
            zone.service.refreshHp(this);
            zone.service.refreshPlayerPart(this);
        }
        if (index == 15) {
            if (pet != null && zone != null) {
                zone.leave(pet);
            }
            pet = null;
            service.petInfo(MessagePetInfoName.INFO);
            service.showTab(-1);
        }
    }

    public void doUseBean() {
        if (itemsBag == null) {
            return;
        }
        for (int i = 0; i < itemsBag.length; i++) {
            if (itemsBag[i] != null && itemsBag[i].template.type == ItemType.TYPE_BEAN) {
                useItem(i);
                return;
            }
        }
    }

    private void useItem(int index) {
        if (index < 0 || index >= itemsBag.length) {
            return;
        }
        Item item = itemsBag[index];
        if (item == null) {
            return;
        }
        if (!item.template.isMaster) {
            addInfo(INFO_RED, Language.ME_CANT_USE_EQUIP);
            return;
        }
        if (item.isItemRider()) {
            itemBagToOther(item);
            return;
        }
        if (item.isItemBody()) {
            itemBagToBody(item);
            return;
        }
        if (item.template.levelRequire > level) {
            addInfo(INFO_RED, Language.LEVEL_NOT_ENOUGH);
            return;
        }
        long now = System.currentTimeMillis();
        switch (item.template.type) {
            case ItemType.TYPE_STONE: {
                addInfo(INFO_YELLOW, "Hãy đến gặp NPC Ô Long để nâng cấp trang bị");
                return;
            }

            case ItemType.TYPE_BEAN: {
                if (now - lastTimeUseBean < 9000) {
                    return;
                }
                lastTimeUseBean = now;
                removeQuantityItemBag(index, 1);
                long hp = item.getParam(22);
                int param = item.getParam(177);
                if (param > 0) {
                    hp = Math.max(hp, Utils.percentOf(Math.max(this.maxHp, this.maxMp), param));
                }
                recovery(RECOVERY_ALL, hp);
                if (disciple != null && !isFusion() && !disciple.isDead() && disciple.zone == this.zone && disciple.zone != null) {
                    disciple.recovery(RECOVERY_ALL, hp);
                    int level = item.template.id - 58;
                    int maxStamina = disciple.getMaxStamina();
                    int recovery = Math.max(maxStamina * Math.max(level * 5 + 40 - disciple.level, 5) / 100, 1);
                    if (disciple.stamina + recovery > maxStamina) {
                        disciple.stamina = maxStamina;
                    } else {
                        disciple.stamina += recovery;
                    }
                    service.discipleInfo(MessageDiscipleInfoName.STAMINA);
                    disciple.chat("Cám ơn sư phụ");
                }
                return;
            }

            case ItemType.TYPE_THUC_AN: {
                removeQuantityItemBag(index, 1);
                int effectId = item.getEffectTemplateId();
                if (effectId != -1) {
                    Effect effect = new Effect(this, effectId, 1800000L, 1000L, item.getParam(20) + level * item.getParam(21));
                    addTimeEffect(effect);
                    if (taskMain != null && taskMain.template.id == 2 && taskMain.index == 1) {
                        nextTaskIndex();
                        addInfo(INFO_YELLOW, "Hãy mau đi tiêu diệt lũ Hổ nanh kiếm");
                    }
                }
                return;
            }

            case ItemType.TYPE_EVENT: {
                if (Server.getInstance().isInterServer()) {
                    addInfo(INFO_RED, Language.CANCEL_ACTION_WHEN_SERVER_IS_INTER_SERVER);
                    return;
                }
                if (!Event.isEvent()) {
                    addInfo(INFO_RED, "Sự kiện đã kết thúc");
                    return;
                }
                switch (item.template.id) {
                    case ItemName.HOP_QUA_DO: {
                        if (!Event.isTeacherDay2024()) {
                            addInfo(INFO_RED, "Sự kiện của vật phẩm này đã kết thúc");
                            return;
                        }
                        Event.event.useItem(this, item);
                        return;
                    }

                    case ItemName.CAPSULE_HALOWEEN: {
                        if (!Event.isHaloween2024()) {
                            addInfo(INFO_RED, "Sự kiện Haloween 2024 đã kết thúc");
                            return;
                        }
                        Event.event.useItem(this, item);
                        return;
                    }

                    case ItemName.GUONG_MA_THUAT_PHONG_AN: {
                        if (!Event.isCoHon2024()) {
                            addInfo(INFO_RED, "Sự kiện Cô Hồn 2024 đã kết thúc");
                            return;
                        }
                        Event.event.useItem(this, item);
                        return;
                    }

                    case ItemName.GIO_VAT_TU: {
                        if (!Event.isTuuTruong2024()) {
                            addInfo(INFO_RED, "Sự kiện Tựu trường 2024 đã kết thúc");
                            return;
                        }
                        Event.event.useItem(this, item);
                        return;
                    }

                    case ItemName.CAPSULE_HE_2024:
                    case ItemName.BO_CAN_CAU_DAC_BIET:
                    case ItemName.BO_CAN_CAU_THUONG:
                    case ItemName.SAO_BIEN:
                    case ItemName.VO_SO:
                    case ItemName.CUA_BIEN: {
                        if (!Event.isHe2024()) {
                            addInfo(INFO_RED, "Sự kiện Hè 2024 đã kết thúc");
                            return;
                        }
                        Event.event.useItem(this, item);
                        return;
                    }

                    case ItemName.CAPSULE_HUNG_VUONG:
                    case ItemName.DUA_HAU:
                    case ItemName.LE_VAT_DAC_BIET:
                    case ItemName.LE_VAT_THUONG: {
                        if (!Event.isHungVuong()) {
                            addInfo(INFO_RED, "Sự kiện Giỗ tổ Hùng Vương đã kết thúc");
                            return;
                        }
                        Event.event.useItem(this, item);
                        return;
                    }

                    case ItemName.CAPSULE_LADY_GIRL:
                    case ItemName.GIO_HOA_DO:
                    case ItemName.GIO_HOA_TIM: {
                        if (!Event.isWomanDay()) {
                            addInfo(INFO_RED, "Sự kiện Quốc tế Phụ nữ 8/3/2024 đã kết thúc");
                            return;
                        }
                        Event.event.useItem(this, item);
                        return;
                    }

                    case ItemName.CAPSULE_TET_2024:
                    case ItemName.LI_XI_2024:
                    case ItemName.BANH_TET_2024:
                    case ItemName.BANH_CHUNG_2024: {
                        if (!Event.isTet()) {
                            addInfo(INFO_RED, "Sự kiện Tết Nguyên đán 2024 đã kết thúc");
                            return;
                        }
                        Event.event.useItem(this, item);
                        return;
                    }

                    case ItemName.HOP_QUA_NOEL_DAC_BIET:
                    case ItemName.HOP_QUA_NOEL_THUONG:
                    case ItemName.CAPSULE_NOEL: {
                        if (!Event.isNoel()) {
                            addInfo(INFO_RED, "Sự kiện Noel 2023 đã kết thúc");
                            return;
                        }
                        Event.event.useItem(this, item);
                        return;
                    }

                    case ItemName.CAPSULE_TRUNG_THU:
                    case ItemName.BANH_TRUNG_THU_THUONG_SU_KIEN_TRUNG_THU_2023:
                    case ItemName.BANH_TRUNG_THU_DAC_BIET_SU_KIEN_TRUNG_THU_2023:
                    case ItemName.MANH_THO_NGOC_SU_KIEN_TRUNG_THU_2023:
                    case ItemName.DEN_TROI_SU_KIEN_TRUNG_THU_2023: {
                        if (!Event.isTrungThu2024()) {
                            addInfo(INFO_RED, "Sự kiện Trung thu 2024 đã kết thúc");
                        }
                        Event.event.useItem(this, item);
                        return;
                    }

                   /* case ItemName.HOP_QUA_DO:
                    case ItemName.HOP_QUA_XANH:
                    case ItemName.GIO_VAT_TU:
                    case ItemName.CAPSULE_TEACHER: {
                        if (!Event.isTeacherDay()) {
                            addInfo(INFO_RED, "Sự kiện của vật phẩm này đã kết thúc");
                            return;
                        }
                        Event.event.useItem(this, item);
                        return;
                    }

                    case ItemName.CAPSULE_BAC_HALLOWEEN_2023:
                    case ItemName.CAPSULE_BI_NGO_HALLOWEEN_2023: {
                        if (!Event.isHalloween()) {
                            addInfo(INFO_RED, "Sự kiện Halloween 2023 đã kết thúc");
                            return;
                        }
                        Event.event.useItem(this, item);
                        return;
                    }

                    case ItemName.BANH_TRUNG_THU_THUONG_SU_KIEN_TRUNG_THU_2023:
                    case ItemName.BANH_TRUNG_THU_DAC_BIET_SU_KIEN_TRUNG_THU_2023:
                    case ItemName.DEN_TROI_SU_KIEN_TRUNG_THU_2023: {
                        if (!Event.isTrungThu()) {
                            addInfo(INFO_RED, "Sự kiện Trung thu 2023 đã kết thúc");
                        }
                        Event.event.useItem(this, item);
                        return;
                    }

                    case ItemName.TUI_MAY_MAN: {
                        if (!Event.isNationalDay()) {
                            addInfo(INFO_RED, "Sự kiện Quốc khánh 2023 đã kết thúc");
                        }
                        Event.event.useItem(this, item);
                        return;
                    }

                    case ItemName.NGOC_TRAI_THUONG:
                    case ItemName.NGOC_TRAI_DAC_BIET: {
                        if (!Event.isHungVuong()) {
                            addInfo(INFO_RED, "Sự kiện Giỗ tổ Hùng Vương 2023 đã kết thúc");
                        }
                        Event.event.useItem(this, item);
                        return;
                    }*/

                    default: {
                        addInfo(INFO_YELLOW, "Hãy tìm NPC sự kiện tại Núi Paozu để sử dụng");
                        return;
                    }
                }
            }

            case ItemType.TYPE_OTHER: {
                switch (item.template.id) {

                    case ItemName.CAPSULE_HIEP_SI: {
                        if (isBagFull()) {
                            addInfo(INFO_RED, Language.ME_BAG_FULL);
                            return;
                        }
                        removeQuantityItemBag(index, 1);
                        Item newItem = ItemManager.getInstance().createItem(Utils.nextInt(522, 545), 1, true);
                        newItem.isLock = true;
                        addItem(newItem, true);
                        return;
                    }

                    case ItemName.TUI_TRANG_BI_PET: {
                        List<Command> commandList = new ArrayList<>();
                        HashMap<Integer, Integer[]> params = new HashMap<>();
                        params.put(0, new Integer[]{300, 1000});
                        params.put(1, new Integer[]{300, 1000});
                        params.put(2, new Integer[]{1000, 2000});
                        params.put(3, new Integer[]{300, 1000});
                        params.put(4, new Integer[]{300, 1000});
                        params.put(5, new Integer[]{300, 1000});
                        params.put(6, new Integer[]{300, 1000});
                        params.put(25, new Integer[]{3, 10});
                        params.put(26, new Integer[]{500, 1000});
                        params.put(27, new Integer[]{1000, 2000});
                        params.put(28, new Integer[]{500, 1000});
                        params.put(29, new Integer[]{500, 1000});
                        params.put(30, new Integer[]{500, 1000});
                        params.put(31, new Integer[]{3, 10});
                        params.put(32, new Integer[]{3, 10});
                        params.put(99, new Integer[]{1000, 2000});
                        int upgrade = item.getUpgrade();
                        for (Integer optionID : params.keySet()) {
                            int param = params.get(optionID)[1];
                            for (int i = 0; i < upgrade; i++) {
                                param = param * 11 / 10;
                            }
                            commandList.add(new Command(CommandName.TUI_TRANG_BI_PET,
                                    ItemManager.getInstance().itemOptionTemplates.get(optionID).name.replace("#", String.valueOf(param)),
                                    this, optionID, item));
                        }
                        createMenu(NpcName.ME, "Chọn 1 chỉ số (+" + upgrade + ")", commandList);
                        return;
                    }

                    case ItemName.MANH_GIAY_CHIEU_HUT_DAY: {
                        List<Command> commandList = new ArrayList<>();
                        commandList.add(new Command(CommandName.DOI_SACH_HUT_DAY, "OK", this));
                        commandList.add(new Command(CommandName.CANCEL, "Hủy", this));
                        StringBuilder content = new StringBuilder();
                        content.append("Đổi sách Chiêu Sức mạnh tuyệt đối sẽ tốn 50 mảnh giấy và 100 kim cương.").append("\n");
                        content.append("Bạn có muốn đổi không?").append("\n");
                        createMenu(NpcName.ME, content.toString(), commandList);
                        return;
                    }

                    case ItemName.RUONG_BAT_BAO_SO_CAP: {
                        if (getCountItemBagEmpty() < 8) {
                            addInfo(INFO_RED, "Cần ít nhất 8 ô trống trong túi đồ");
                            return;
                        }
                        removeQuantityItemBag(index, 1);
                        List<Item> items = new ArrayList<>();
                        for (int i = 0; i < 16; i++) {
                            Item newItem = ItemManager.getInstance().createItem(i, true);
                            if (newItem.template.gender == -1 || newItem.template.gender == this.gender) {
                                newItem.randomParam(-15, 15);
                                newItem.isLock = true;
                                int num = 0;
                                while (num < 4) {
                                    num++;
                                    newItem.nextUpgrade();
                                }
                                items.add(newItem);
                            }
                        }
                        for (Item newItem : items) {
                            addItem(newItem.cloneItem(), true);
                        }
                        return;
                    }

                    case ItemName.RUONG_BAT_BAO_CAP_1: {
                        if (getCountItemBagEmpty() < 8) {
                            addInfo(INFO_RED, "Cần ít nhất 8 ô trống trong túi đồ");
                            return;
                        }
                        removeQuantityItemBag(index, 1);
                        List<Item> items = new ArrayList<>();
                        for (int i = 16; i < 32; i++) {
                            Item newItem = ItemManager.getInstance().createItem(i, true);
                            if (newItem.template.gender == -1 || newItem.template.gender == this.gender) {
                                newItem.randomParam(-15, 15);
                                newItem.isLock = true;
                                int num = 0;
                                while (num < 12) {
                                    num++;
                                    newItem.nextUpgrade();
                                }
                                items.add(newItem);
                            }
                        }
                        for (Item newItem : items) {
                            addItem(newItem.cloneItem(), true);
                        }
                        return;
                    }

                    case ItemName.RUONG_BAT_BAO_CAP_2: {
                        if (getCountItemBagEmpty() < 8) {
                            addInfo(INFO_RED, "Cần ít nhất 8 ô trống trong túi đồ");
                            return;
                        }
                        removeQuantityItemBag(index, 1);
                        List<Item> items = new ArrayList<>();
                        for (int i = 32; i < 48; i++) {
                            Item newItem = ItemManager.getInstance().createItem(i, true);
                            if (newItem.template.gender == -1 || newItem.template.gender == this.gender) {
                                newItem.randomParam(-15, 15);
                                newItem.isLock = true;
                                int num = 0;
                                while (num < 12) {
                                    num++;
                                    newItem.nextUpgrade();
                                }
                                items.add(newItem);
                            }
                        }
                        for (Item newItem : items) {
                            addItem(newItem.cloneItem(), true);
                        }
                        return;
                    }

                    case ItemName.RUONG_BAT_BAO_CAP_3: {
                        if (getCountItemBagEmpty() < 8) {
                            addInfo(INFO_RED, "Cần ít nhất 8 ô trống trong túi đồ");
                            return;
                        }
                        removeQuantityItemBag(index, 1);
                        List<Item> items = new ArrayList<>();
                        for (int i = 116; i < 132; i++) {
                            Item newItem = ItemManager.getInstance().createItem(i, true);
                            if (newItem.template.gender == -1 || newItem.template.gender == this.gender) {
                                newItem.randomParam(-15, 15);
                                newItem.isLock = true;
                                int num = 0;
                                while (num < 12) {
                                    num++;
                                    newItem.nextUpgrade();
                                }
                                items.add(newItem);
                            }
                        }
                        for (Item newItem : items) {
                            addItem(newItem.cloneItem(), true);
                        }
                        return;
                    }

                    case ItemName.RUONG_BAT_BAO_CAP_4: {
                        if (getCountItemBagEmpty() < 8) {
                            addInfo(INFO_RED, "Cần ít nhất 8 ô trống trong túi đồ");
                            return;
                        }
                        removeQuantityItemBag(index, 1);
                        List<Item> items = new ArrayList<>();
                        for (int i = 292; i < 308; i++) {
                            Item newItem = ItemManager.getInstance().createItem(i, true);
                            if (newItem.template.gender == -1 || newItem.template.gender == this.gender) {
                                newItem.randomParam(-15, 15);
                                newItem.isLock = true;
                                int num = 0;
                                while (num < 12) {
                                    num++;
                                    newItem.nextUpgrade();
                                }
                                items.add(newItem);
                            }
                        }
                        for (Item newItem : items) {
                            addItem(newItem.cloneItem(), true);
                        }
                        return;
                    }

                    case ItemName.RUONG_BAT_BAO_CAP_5: {
                        if (getCountItemBagEmpty() < 8) {
                            addInfo(INFO_RED, "Cần ít nhất 8 ô trống trong túi đồ");
                            return;
                        }
                        removeQuantityItemBag(index, 1);
                        List<Item> items = new ArrayList<>();
                        for (int i = 522; i < 546; i++) {
                            Item newItem = ItemManager.getInstance().createItem(i, false);
                            if (newItem.template.gender == -1 || newItem.template.gender == this.gender) {
                                newItem.isLock = true;
                                items.add(newItem);
                            }
                        }
                        for (Item newItem : items) {
                            addItem(newItem.cloneItem(), true);
                        }
                        return;
                    }

                    case ItemName.RUONG_BAT_BAO_CAP_6: {
                        if (getCountItemBagEmpty() < 8) {
                            addInfo(INFO_RED, "Cần ít nhất 8 ô trống trong túi đồ");
                            return;
                        }
                        removeQuantityItemBag(index, 1);
                        List<Item> items = new ArrayList<>();
                        for (int i = 292; i < 308; i++) {
                            Item newItem = ItemManager.getInstance().createItem(i, true);
                            if (newItem.template.gender == -1 || newItem.template.gender == this.gender) {
                                newItem.randomParam(-15, 15);
                                newItem.isLock = true;
                                int num = 0;
                                while (num < 14) {
                                    num++;
                                    newItem.nextUpgrade();
                                }
                                items.add(newItem);
                            }
                        }
                        for (Item newItem : items) {
                            addItem(newItem.cloneItem(), true);
                        }
                        return;
                    }

                    case ItemName.TRUNG_MABU: {
                        List<Command> commandList = new ArrayList<>();
                        commandList.add(new Command(CommandName.CANCEL, "Từ chối", this));
                        commandList.add(new Command(CommandName.DOI_DE_MABU, "OK", this));
                        StringBuilder content = new StringBuilder();
                        content.append("Sẽ mất 7 ngày để trứng nở ra đệ tử Mabư (cấp 30)").append("\n");
                        content.append("Hoàn thành nhiệm vụ hàng ngày để giúp trứng nở nhanh hơn").append("\n");
                        content.append("Bạn cũng có thể mua bùa thời gian tại NPC Bà hạt mít để trứng nở ngay lập tức.").append("\n");
                        content.append("Hợp thể với đệ tử Mabư sẽ giúp tăng 15% sức chiến đấu").append("\n");
                        if (disciple != null) {
                            content.append("Đệ tử cũ và toàn bộ trang bị đang sử dụng trên người đệ tử cũ sẽ bị xóa bỏ").append("\n");
                        }
                        createMenu(NpcName.ME, content.toString(), commandList);
                        return;
                    }

                    case ItemName.CAPSULE_CELL: {
                        int upgrade = item.getUpgrade();
                        if (upgrade < 1 || upgrade > 16) {
                            addInfo(INFO_RED, "Capsule đã bị hỏng");
                            return;
                        }
                        List<Item> itemList = new ArrayList<>();
                        if (upgrade <= 4) {
                            int[] stones = {ItemName.DA_2, ItemName.DA_2, ItemName.DA_3, ItemName.DA_4};
                            itemList.add(ItemManager.getInstance().createItem(stones[upgrade - 1], Utils.nextInt(30, 50), true));
                            itemList.add(ItemManager.getInstance().createItem(ItemName.DAU_THAN_CAP_4, Utils.nextInt(30, 50), true));
                            itemList.add(ItemManager.getInstance().createItem(ItemName.NGOC_RONG_6_SAO, 1, true));
                        } else if (upgrade <= 8) {
                            int[] stones = {ItemName.DA_5, ItemName.DA_5, ItemName.DA_6, ItemName.DA_6};
                            itemList.add(ItemManager.getInstance().createItem(stones[upgrade - 5], Utils.nextInt(30, 50), true));
                            itemList.add(ItemManager.getInstance().createItem(ItemName.DAU_THAN_CAP_8, Utils.nextInt(30, 50), true));
                            itemList.add(ItemManager.getInstance().createItem(ItemName.NGOC_RONG_5_SAO, 1, true));
                            if (Utils.isPercent(10 * (upgrade - 4))) {
                                itemList.add(ItemManager.getInstance().createItem(ItemName.BUA_BAO_VE_CAP_1, 1, true));
                            }
                        } else if (upgrade <= 12) {
                            int[] stones = {ItemName.DA_7, ItemName.DA_7, ItemName.DA_8, ItemName.DA_8};
                            itemList.add(ItemManager.getInstance().createItem(stones[upgrade - 9], Utils.nextInt(10, 20), true));
                            itemList.add(ItemManager.getInstance().createItem(ItemName.DAU_THAN_CAP_9, Utils.nextInt(30, 50), true));
                            itemList.add(ItemManager.getInstance().createItem(ItemName.NGOC_RONG_4_SAO, 1, true));
                            if (Utils.isPercent(10 * (upgrade - 8))) {
                                itemList.add(ItemManager.getInstance().createItem(ItemName.BUA_BAO_VE_CAP_2, 1, true));
                            }
                        } else {
                            int[] stones = {ItemName.DA_9, ItemName.DA_9, ItemName.DA_10, ItemName.DA_11};
                            itemList.add(ItemManager.getInstance().createItem(stones[upgrade - 13], Utils.nextInt(10, 20), true));
                            itemList.add(ItemManager.getInstance().createItem(ItemName.DAU_THAN_CAP_10, Utils.nextInt(30, 50), true));
                            itemList.add(ItemManager.getInstance().createItem(ItemName.NGOC_RONG_3_SAO, 1, true));
                            if (Utils.isPercent(10 * (upgrade - 12))) {
                                itemList.add(ItemManager.getInstance().createItem(ItemName.BUA_BAO_VE_CAP_3, 1, true));
                            }
                        }
                        itemList.add(ItemManager.getInstance().createItem(ItemName.BI_KIP_KY_NANG, Utils.nextInt(2 * upgrade, 3 * upgrade), true));
                        itemList.add(ItemManager.getInstance().createItem(ItemName.CAPSULE_TAU_BAY_THUONG, Utils.nextInt(2 * upgrade, 3 * upgrade), true));
                        itemList.add(ItemManager.getInstance().createItem(ItemName.SAO_PHA_LE, Utils.nextInt(2 * upgrade, 3 * upgrade), true));
                        if (upgrade > 8) {
                            itemList.add(ItemManager.getInstance().createItem(ItemName.LOI_KI_NANG, Utils.nextInt(2 * upgrade, 3 * upgrade), true));
                        } else if (upgrade >= 4) {
                            itemList.add(ItemManager.getInstance().createItem(ItemName.LOI_KI_NANG, upgrade, true));
                        }
                        itemList.add(ItemManager.getInstance().createItem(ItemName.RUBY, upgrade, true));
                        itemList.add(ItemManager.getInstance().createItem(ItemName.XU, Utils.nextInt(90000 * upgrade * upgrade, 100000 * upgrade * upgrade), true));
                        if (getCountItemBagEmpty() < itemList.size()) {
                            addInfo(INFO_RED, String.format("Cần ít nhất %d ô trống trong túi đồ", itemList.size()));
                            return;
                        }
                        removeQuantityItemBag(index, 1);
                        for (Item newItem : itemList) {
                            addItem(newItem, true);
                        }
                        return;
                    }

                    case ItemName.CAPSULE_HUYEN_BI: {
                        if (isBagFull()) {
                            addInfo(INFO_RED, Language.ME_BAG_FULL);
                            return;
                        }
                        removeQuantityItemBag(index, 1);
                        int upgrade = item.getUpgrade();
                        int star = item.getParam(67);
                        Item newItem = ItemManager.getInstance().createItem(Utils.nextInt(337, 352), 1, true);
                        newItem.randomParam(15, 20);
                        while (upgrade > 0) {
                            upgrade--;
                            newItem.nextUpgrade();
                        }
                        if (star > 0) {
                            newItem.options.add(new ItemOption(67, star));
                            newItem.options.add(new ItemOption(68, 0));
                        }
                        addItem(newItem, true);
                        return;
                    }

                    case ItemName.MAM_HAC_LONG:
                    case ItemName.MAM_THIEN_MOC:
                    case ItemName.MAM_CAY_THONG:
                    case ItemName.MAM_DAO:
                    case ItemName.MAM_NAM_HAC_HOA:
                    case ItemName.MAM_DUA_EXP:
                    case ItemName.HOA_CHUOI: {
                        if (zone.map.template.id != MapName.NHA_GO_HAN) {
                            addInfo(INFO_RED, "Chỉ có thể trồng cây tại vườn trong nhà Gohan");
                            return;
                        }
                        if (npcTrees.size() >= 4) {
                            addInfo(INFO_RED, "Số lượng cây đã đạt tối đa");
                            return;
                        }
                        int npcID = 35;
                        if (item.template.id == ItemName.MAM_HAC_LONG) {
                            npcID = 37;
                        }
                        if (item.template.id == ItemName.MAM_CAY_THONG) {
                            npcID = 39;
                        }
                        if (item.template.id == ItemName.MAM_DAO) {
                            npcID = 43;
                        }
                        if (item.template.id == ItemName.MAM_NAM_HAC_HOA) {
                            npcID = 45;
                        }
                        if (item.template.id == ItemName.HOA_CHUOI) {
                            npcID = 50;
                        }
                        if (item.template.id == ItemName.MAM_DUA_EXP) {
                            npcID = 52;
                        }
                        final int findId = npcID;
                        NpcTree npcTree = npcTrees.stream().filter(n -> n.template.id == findId).findFirst().orElse(null);
                        if (npcTree != null) {
                            addInfo(INFO_RED, "Không thể trồng thêm");
                            return;
                        }
                        removeQuantityItemBag(index, 1);
                        npcTree = new NpcTree(this, npcID, 60);
                        npcTree.x = npcTrees.size() * 200 + 1425;
                        npcTree.y = 846;
                        npcTrees.add(npcTree);
                        zone.enter(npcTree);
                        return;
                    }

                    case ItemName.QUA_CHUOI:
                    case ItemName.TRAI_DUA_EXP:
                    case ItemName.THIEN_MOC_QUA:
                    case ItemName.QUA_THONG_EXP:
                    case ItemName.HAC_LONG_QUA:
                    case ItemName.QUA_DAO_EXP:
                    case ItemName.QUA_NAM_HAC_HOA: {
                        int exp = item.getParam(129);
                        if (exp <= 0) {
                            addInfo(INFO_RED, "Vật phẩm không có exp");
                            return;
                        }
                        if (itemsBody[15] == null || pet == null) {
                            addInfo(INFO_RED, "Bạn chưa có thú nuôi");
                            return;
                        }
                        if (pet.isDead() || pet.stamina <= 0) {
                            addInfo(INFO_RED, "Bạn chưa triệu hồi Pet");
                            return;
                        }
                        removeQuantityItemBag(index, 1);
                        Item itemPet = itemsBody[15];
                        ItemOption option = itemPet.getOption(131);
                        if (option == null) {
                            itemPet.options.add(new ItemOption(131, exp));
                        } else {
                            option.param += exp;
                            ItemOption upgrade = itemPet.getOption(19);
                            if (upgrade == null || upgrade.param < Pet.MAX_LEVEL) {
                                int maxExp = 500 + 50 * (upgrade == null ? 0 : upgrade.param);
                                if (option.param >= maxExp) {
                                    option.param -= maxExp;
                                    itemPet.nextUpgradePet();
                                    pet.setItem(itemPet);
                                    service.setInfo();
                                    service.petInfo(MessagePetInfoName.INFO);
                                    service.petInfo(MessagePetInfoName.ITEM_BODY);
                                }
                            } else {
                                addInfo(INFO_RED, "Pet đã đạt cấp tối đa");
                            }
                        }
                        pet.setItemOption(131, option == null ? exp : option.param);
                        service.refreshItemBody(itemPet.indexUI);
                        service.petInfo(MessagePetInfoName.EXP);
                        return;
                    }

                    case ItemName.THE_TRIEU_HOI: {
                        if (itemsBody[15] == null || pet == null) {
                            addInfo(INFO_RED, "Bạn chưa có thú nuôi");
                            return;
                        }
                        removeQuantityItemBag(index, 1);
                        int stamina = pet.stamina;
                        pet.stamina = pet.getMaxStamina();
                        service.petInfo(MessagePetInfoName.STAMINA);
                        if (zone != null) {
                            zone.enter(pet);
                            pet.followMaster();
                        }
                        if (stamina <= 0) {
                            service.setInfo();
                        }
                        return;
                    }

                    case ItemName.TRUNG_HAN_LONG: {
                        if (isBagFull()) {
                            addInfo(INFO_RED, Language.ME_BAG_FULL);
                            return;
                        }
                        removeQuantityItemBag(index, 1);
                        Item newItem = ItemManager.getInstance().createItem(ItemName.PET_HAN_LONG, 1, false);
                        newItem.createOptionPet();
                        addItem(newItem, true);
                        return;
                    }

                    case ItemName.TRUNG_HOA_LONG: {
                        if (isBagFull()) {
                            addInfo(INFO_RED, Language.ME_BAG_FULL);
                            return;
                        }
                        removeQuantityItemBag(index, 1);
                        Item newItem = ItemManager.getInstance().createItem(ItemName.PET_HOA_LONG, 1, false);
                        newItem.createOptionPet();
                        addItem(newItem, true);
                        return;
                    }

                    case ItemName.TRUNG_HAC_LONG: {
                        if (isBagFull()) {
                            addInfo(INFO_RED, Language.ME_BAG_FULL);
                            return;
                        }
                        removeQuantityItemBag(index, 1);
                        Item newItem = ItemManager.getInstance().createItem(ItemName.PET_HAC_LONG, 1, false);
                        newItem.createOptionPet();
                        addItem(newItem, true);
                        return;
                    }

                    case ItemName.MANH_YARDRAT_THUONG:
                    case ItemName.MANH_YARDRAT_DAC_BIET: {
                        if (isBagFull()) {
                            addInfo(INFO_RED, Language.ME_BAG_FULL);
                            return;
                        }
                        if (item.quantity < 10000) {
                            addInfo(INFO_RED, "Cần thu thập 10000 mảnh để đổi cải trang");
                            return;
                        }
                        removeQuantityItemBag(index, 10000);
                        int itemId = item.template.id == ItemName.MANH_YARDRAT_THUONG ? ItemName.CAI_TRANG_YARDRAT_THUONG : ItemName.CAI_TRANG_YARDRAT_DAC_BIET;
                        Item itm = ItemManager.getInstance().createItem(itemId, 1, true, true);
                        addItem(itm);
                        addInfo(INFO_YELLOW, String.format("Bạn nhận được %s", itm.template.name));
                        return;
                    }

                    case ItemName.SACH_CHIEU_HUT_DAY: {
                        Skill skill = getSkill(36);
                        if (skill == null || skill.level > 0) {
                            addInfo(INFO_RED, "Không thể thực hiện");
                            return;
                        }
                        if (pointSkill < 1) {
                            addInfo(INFO_RED, "Bạn không đủ điểm kỹ năng");
                            return;
                        }
                        removeQuantityItemBag(index, 1);
                        skill.level = 1;
                        pointSkill--;
                        service.refreshSkill(0, skill);
                        addInfo(INFO_YELLOW, String.format("Học thành công %s", skill.getName()));
                        return;
                    }

                    case ItemName.SACH_THOI_MIEN: {
                        Skill skill = getSkill(30);
                        if (skill == null || skill.level > 0) {
                            addInfo(INFO_RED, "Không thể thực hiện");
                            return;
                        }
                        if (pointSkill < 1) {
                            addInfo(INFO_RED, "Bạn không đủ điểm kỹ năng");
                            return;
                        }
                        removeQuantityItemBag(index, 1);
                        skill.level = 1;
                        pointSkill--;
                        service.refreshSkill(0, skill);
                        addInfo(INFO_YELLOW, String.format("Học thành công %s", skill.getName()));
                        return;
                    }

                    case ItemName.SACH_SUPER_KAMEJOKO: {
                        Skill skill = getSkill(33);
                        if (skill == null || skill.level > 0) {
                            addInfo(INFO_RED, "Không thể thực hiện");
                            return;
                        }
                        if (pointSkill < 1) {
                            addInfo(INFO_RED, "Bạn không đủ điểm kỹ năng");
                            return;
                        }
                        removeQuantityItemBag(index, 1);
                        skill.level = 1;
                        pointSkill--;
                        service.refreshSkill(0, skill);
                        addInfo(INFO_YELLOW, String.format("Học thành công %s", skill.getName()));
                        return;
                    }

                    case ItemName.SACH_MA_PHONG_BA: {
                        Skill skill = getSkill(34);
                        if (skill == null || skill.level > 0) {
                            addInfo(INFO_RED, "Không thể thực hiện");
                            return;
                        }
                        if (pointSkill < 1) {
                            addInfo(INFO_RED, "Bạn không đủ điểm kỹ năng");
                            return;
                        }
                        removeQuantityItemBag(index, 1);
                        skill.level = 1;
                        pointSkill--;
                        service.refreshSkill(0, skill);
                        addInfo(INFO_YELLOW, String.format("Học thành công %s", skill.getName()));
                        return;
                    }

                    case ItemName.SACH_BIGBANG_FALSH: {
                        Skill skill = getSkill(35);
                        if (skill == null || skill.level > 0) {
                            addInfo(INFO_RED, "Không thể thực hiện");
                            return;
                        }
                        if (pointSkill < 1) {
                            addInfo(INFO_RED, "Bạn không đủ điểm kỹ năng");
                            return;
                        }
                        removeQuantityItemBag(index, 1);
                        skill.level = 1;
                        pointSkill--;
                        service.refreshSkill(0, skill);
                        addInfo(INFO_YELLOW, String.format("Học thành công %s", skill.getName()));
                        return;
                    }

                    case ItemName.KEO_DAU_LAU_HALLOWEEN_2023:
                    case ItemName.DUA_XANH: {
                        removeQuantityItemBag(index, 1);
                        int effectId = item.getEffectTemplateId();
                        if (effectId != -1) {
                            addTimeEffect(new Effect(this, effectId, 1800000L, 0, item.getParam(103)));
                        }
                        return;
                    }

                    case ItemName.KEO_CAM_LAU_HALLOWEEN_2023:
                    case ItemName.DUA_NAU: {
                        removeQuantityItemBag(index, 1);
                        int effectId = item.getEffectTemplateId();
                        if (effectId != -1) {
                            addTimeEffect(new Effect(this, effectId, 1800000L, 0, item.getParam(104)));
                        }
                        service.setInfo();
                        return;
                    }

                    case ItemName.KEO_MAT_XANH_HALLOWEEN_2023:
                    case ItemName.DUA_VANG: {
                        removeQuantityItemBag(index, 1);
                        int effectId = item.getEffectTemplateId();
                        if (effectId != -1) {
                            addTimeEffect(new Effect(this, effectId, 1800000L, 0, item.getParam(102)));
                        }
                        service.setInfo();
                        return;
                    }

                    case ItemName.KEO_THAN_CHET_HALLOWEEN_2023: {
                        removeQuantityItemBag(index, 1);
                        int effectId = item.getEffectTemplateId();
                        if (effectId != -1) {
                            addTimeEffect(new Effect(this, effectId, 1800000L, 0, item.getParam(119)));
                        }
                        service.setInfo();
                        return;
                    }

                    case ItemName.NANG_CAP_CHIEU_1_DE_TU: {
                        if (disciple == null) {
                            addInfo(INFO_RED, "Bạn chưa có đệ tử");
                            return;
                        }
                        Skill skill = disciple.skills.get(0);
                        if (skill.level >= skill.template.maxLevel[0]) {
                            addInfo(INFO_RED, "Kĩ năng đã đạt cấp tối đa");
                            return;
                        }
                        removeQuantityItemBag(index, 1);
                        skill.level++;
                        service.discipleInfo(MessageDiscipleInfoName.SKILL_INFO);
                        return;
                    }

                    case ItemName.NANG_CAP_CHIEU_2_DE_TU: {
                        if (disciple == null) {
                            addInfo(INFO_RED, "Bạn chưa có đệ tử");
                            return;
                        }
                        if (disciple.skills.size() >= 2) {
                            Skill skill = disciple.skills.get(1);
                            if (skill.level >= skill.template.maxLevel[0]) {
                                addInfo(INFO_RED, "Kĩ năng đã đạt cấp tối đa");
                                return;
                            }
                            removeQuantityItemBag(index, 1);
                            skill.level++;
                            service.discipleInfo(MessageDiscipleInfoName.SKILL_INFO);
                        } else {
                            addInfo(INFO_RED, "Kĩ năng chưa được mở");
                            return;
                        }
                        return;
                    }

                    case ItemName.NANG_CAP_CHIEU_3_DE_TU: {
                        if (disciple == null) {
                            addInfo(INFO_RED, "Bạn chưa có đệ tử");
                            return;
                        }
                        if (disciple.skills.size() >= 3) {
                            Skill skill = disciple.skills.get(2);
                            if (skill.level >= skill.template.maxLevel[0]) {
                                addInfo(INFO_RED, "Kĩ năng đã đạt cấp tối đa");
                                return;
                            }
                            removeQuantityItemBag(index, 1);
                            skill.level++;
                            service.discipleInfo(MessageDiscipleInfoName.SKILL_INFO);
                        } else {
                            addInfo(INFO_RED, "Kĩ năng chưa được mở");
                            return;
                        }
                        return;
                    }

                    case ItemName.NANG_CAP_CHIEU_4_DE_TU: {
                        if (disciple == null) {
                            addInfo(INFO_RED, "Bạn chưa có đệ tử");
                            return;
                        }
                        if (disciple.skills.size() >= 4) {
                            Skill skill = disciple.skills.get(3);
                            if (skill.level >= skill.template.maxLevel[0]) {
                                addInfo(INFO_RED, "Kĩ năng đã đạt cấp tối đa");
                                return;
                            }
                            removeQuantityItemBag(index, 1);
                            skill.level++;
                            service.discipleInfo(MessageDiscipleInfoName.SKILL_INFO);
                        } else {
                            addInfo(INFO_RED, "Kĩ năng chưa được mở");
                            return;
                        }
                        return;
                    }

                    case ItemName.NANG_CAP_CHIEU_5_DE_TU: {
                        if (disciple == null) {
                            addInfo(INFO_RED, "Bạn chưa có đệ tử");
                            return;
                        }
                        if (disciple.skills.size() >= 5) {
                            Skill skill = disciple.skills.get(4);
                            if (skill.level >= skill.template.maxLevel[0]) {
                                addInfo(INFO_RED, "Kĩ năng đã đạt cấp tối đa");
                                return;
                            }
                            removeQuantityItemBag(index, 1);
                            skill.level++;
                            service.discipleInfo(MessageDiscipleInfoName.SKILL_INFO);
                        } else {
                            addInfo(INFO_RED, "Kĩ năng chưa được mở");
                            return;
                        }
                        return;
                    }

                    case ItemName.QUA_TRUNG: {
                        if (disciple != null) {
                            addInfo(INFO_RED, "Bạn đã có đệ tử");
                            return;
                        }
                        int tempId = item.getEffectTemplateId();
                        Effect effect = findEffectByTemplateId(tempId);
                        if (effect != null) {
                            addInfo(INFO_RED, Language.CANT_ACTION);
                            return;
                        }
                        removeQuantityItemBag(index, 1);
                        addEffect(new Effect(this, tempId, 1000L * 60 * 60 * 24 * 7));
                        List<Command> commandList = new ArrayList<>();
                        commandList.add(new Command(CommandName.CANCEL, "OK", this));
                        StringBuilder content = new StringBuilder();
                        content.append("Sẽ mất 7 ngày để trứng nở").append("\n");
                        content.append("Hoàn thành nhiệm vụ hàng ngày để giúp trứng nở nhanh hơn").append("\n");
                        content.append("Bạn cũng có thể mua bùa thời gian tại NPC Bà hạt mít để trứng nở ngay lập tức.").append("\n");
                        createMenu(NpcName.ME, content.toString(), commandList);
                        return;
                    }

                    case ItemName.BUA_THOI_GIAN: {
                        if (disciple != null) {
                            addInfo(INFO_RED, "Bạn đã có đệ tử");
                            return;
                        }
                        Effect effect = findEffectByTemplateId(EffectName.QUA_TRUNG_DE_TU);
                        if (effect == null) {
                            addInfo(INFO_RED, Language.CANT_ACTION);
                            return;
                        }
                        removeQuantityItemBag(index, 1);
                        addTimeEffect(EffectName.QUA_TRUNG_DE_TU, -(effect.time - 10000L));
                        return;
                    }

                    case ItemName.BUA_HOAN_LUONG: {
                        removeQuantityItemBag(index, 1);
                        pointPk = 0;
                        service.setPointPk();
                        addInfo(INFO_YELLOW, String.format("Điểm hiếu chiến của bạn là %d", pointPk));
                        return;
                    }

                    case ItemName.CAPSULE_TAU_BAY_THUONG:
                    case ItemName.CAPSULE_TAU_BAY_DAC_BIET: {
                        if (isStun()) {
                            addInfo(INFO_RED, Language.CANT_ACTION);
                            return;
                        }
                        if (item.template.id == ItemName.CAPSULE_TAU_BAY_THUONG) {
                            removeQuantityItemBag(index, 1);
                        }
                        MapPlanet currentPlanet = zone.map.template.planet;
                        ArrayList<KeyValue<Map, String>> mapList = new ArrayList<>();
                        if (lastMapSpaceshipId != -1) {
                            Map map = MapManager.getInstance().maps.get(lastMapSpaceshipId);
                            if (map != null && map.template.planet == currentPlanet && map.template.id != zone.map.template.id && isCanJoinMap(map)) {
                                mapList.add(new KeyValue<>(map, "Chỗ cũ: " + map.template.name, map.getPlanetName()));
                            } else {
                                lastMapSpaceshipId = -1;
                            }
                        }
                        for (Map map : MapManager.getInstance().mapSpaceships) {
                            if (map.template.planet == currentPlanet && map.template.id != lastMapSpaceshipId && map.template.id != zone.map.template.id && isCanJoinMap(map)) {
                                mapList.add(new KeyValue<>(map, map.template.name, map.getPlanetName()));
                            }
                        }
                        if (clan != null) {
                            mapList.add(new KeyValue<>(clan.map, clan.map.template.name, "Bang hội " + clan.name));
                        }
                        showListMapSpaceship(mapList, false, true, false);
                        return;
                    }

                    case ItemName.CAPSULE_THOI_KHONG: {
                        MapPlanet currentPlanet = zone.map.template.planet;
                        ArrayList<KeyValue<Map, String>> mapList = new ArrayList<>();
                        if (lastMapSpaceshipId != -1) {
                            Map map = MapManager.getInstance().maps.get(lastMapSpaceshipId);
                            if (map != null && map.template.planet == currentPlanet && map.template.id != zone.map.template.id && isCanJoinMap(map)) {
                                mapList.add(new KeyValue<>(map, "Chỗ cũ: " + map.template.name, map.getPlanetName()));
                            } else {
                                lastMapSpaceshipId = -1;
                            }
                        }
                        for (Map map : MapManager.getInstance().maps.values()) {
                            if (map.template.planet == currentPlanet && map.template.id != lastMapSpaceshipId && map.template.id != zone.map.template.id && isCanJoinMap(map)) {
                                mapList.add(new KeyValue<>(map, map.template.name, map.getPlanetName()));
                            }
                        }
                        if (clan != null) {
                            mapList.add(new KeyValue<>(clan.map, clan.map.template.name, "Bang hội " + clan.name));
                        }
                        showListMapSpaceship(mapList, false, true, false);
                        return;
                    }

                    case ItemName.TDLT: {
                        if (isInTreasure()) {
                            addInfo(INFO_YELLOW, Language.CANT_ACTION);
                            return;
                        }
                        ItemOption itemOption = item.getOption(24);
                        if (itemOption == null) {
                            return;
                        }
                        Effect effect = findEffectByTemplateId(EffectName.TU_DONG_LUYEN_TAP);
                        if (effect != null) {
                            long time = itemOption.param + effect.time / 60000;
                            if (time > item.getMaxQuantity()) {
                                time = item.getMaxQuantity();
                            }
                            itemOption.param = (int) time;
                            removeEffect(effect);
                            service.removeEffect(effect);
                            service.refreshItemBag(index);
                            setIsAutoPlay(false);
                        } else {
                            if (itemOption.param <= 0) {
                                return;
                            }
                            addEffect(new Effect(this, EffectName.TU_DONG_LUYEN_TAP, itemOption.param * 60000L));
                            itemOption.param = 0;
                            service.refreshItemBag(index);
                            setIsAutoPlay(true);
                        }
                        return;
                    }

                    case ItemName.BUA_TAY_TIEM_NANG: {
                        removeQuantityItemBag(index, 1);
                        baseDamage = 10;
                        baseHp = 5;
                        baseMp = 5;
                        baseConstitution = 5;
                        potential = power;
                        service.resetPotential();
                        service.setInfo();
                        return;
                    }

                    case ItemName.BUA_TAY_TNSM_DE_TU: {
                        if (disciple == null) {
                            addInfo(INFO_RED, "Bạn chưa có đệ tử");
                            return;
                        }
                        if (isFusion()) {
                            addInfo(INFO_RED, Language.CANCEL_ACTION_WHEN_FUSION);
                            return;
                        }
                        if (disciple.isDead()) {
                            addInfo(INFO_RED, "Không thể thực hiện khi đệ tử đang kiệt sức");
                            return;
                        }
                        removeQuantityItemBag(index, 1);
                        disciple.baseDamage = 10;
                        disciple.baseHp = 5;
                        disciple.baseMp = 5;
                        disciple.baseConstitution = 5;
                        disciple.potential = disciple.power;
                        service.discipleInfo(MessageDiscipleInfoName.POWER_INFO);
                        service.discipleInfo(MessageDiscipleInfoName.BASE_INFO);
                        disciple.refreshInfo();
                        service.discipleInfo(MessageDiscipleInfoName.POINT);
                        return;
                    }

                    case ItemName.THE_KIM_CUONG: {
                        if (Server.getInstance().isInterServer()) {
                            addInfo(INFO_RED, Language.CANCEL_ACTION_WHEN_SERVER_IS_INTER_SERVER);
                            return;
                        }
                        startClientInput(ClientInputType.INPUT_TANG_KIM_CUONG, "Tặng kim cương", new TextField("Tên nhân vật", TextField.TYPE_NORMAL), new TextField("Kim cương", TextField.TYPE_NUMBER));
                        return;
                    }

                    case ItemName.CAPSULE_BAC: {
                        if (isBagFull()) {
                            addInfo(INFO_RED, Language.ME_BAG_FULL);
                            return;
                        }
                        removeQuantityItemBag(index, 1);
                        ItemRandom itemRandom = ServerRandom.CAPSULE_BAC.next();
                        int itemId = itemRandom.getId(gender);
                        int quantity = itemRandom.nextQuantity();
                        if (itemId == ItemName.XU) {
                            upXu(quantity);
                            if (quantity >= 1000000) {
                                Server.getInstance().service.serverChat(String.format("Chúc mừng %s mở %s nhận được %s xu", this.name, item.template.name, Utils.getMoneys(quantity)));
                            }
                        } else {
                            Item im = ItemManager.getInstance().createItem(itemId, quantity, true);
                            if (im.isItemBody()) {
                                im.randomParam(-15, 15);
                            }
                            addItem(im);
                        }
                        service.createSpin(ServerRandom.CAPSULE_BAC, itemId, quantity);
                        return;
                    }

                    case ItemName.CAPSULE_VANG: {
                        if (isBagFull()) {
                            addInfo(INFO_RED, Language.ME_BAG_FULL);
                            return;
                        }
                        removeQuantityItemBag(index, 1);
                        ItemRandom itemRandom = ServerRandom.CAPSULE_VANG.next();
                        int itemId = itemRandom.getId(gender);
                        int quantity = itemRandom.nextQuantity();
                        if (itemId == ItemName.XU) {
                            upXu(quantity);
                            if (quantity >= 1000000) {
                                Server.getInstance().service.serverChat(String.format("Chúc mừng %s mở %s nhận được %s xu", this.name, item.template.name, Utils.getMoneys(quantity)));
                            }
                        } else {
                            Item im = ItemManager.getInstance().createItem(itemId, quantity, true);
                            if (im.isItemBody()) {
                                im.randomParam(-15, 15);
                            }
                            addItem(im);
                        }
                        service.createSpin(ServerRandom.CAPSULE_VANG, itemId, quantity);
                        return;
                    }

                    case ItemName.CAPSULE_BACH_KIM: {
                        if (isBagFull()) {
                            addInfo(INFO_RED, Language.ME_BAG_FULL);
                            return;
                        }
                        removeQuantityItemBag(index, 1);
                        if (pointCapsule == 0) {
                            pointCapsule = Utils.nextInt(80, 120);
                        }
                        if (pointCapsule > 0) {
                            pointCapsule--;
                            if (pointCapsule == 0) {
                                pointCapsule = Utils.nextInt(80, 120);
                                Item im = ItemManager.getInstance().createItem(Utils.nextInt(522, 545), 1, false);
                                addItem(im);
                                addInfo(INFO_YELLOW, String.format("Bạn nhận được %s", im.template.name));
                                Server.getInstance().service.serverChat(String.format("Chúc mừng %s mở %s nhận được %s", this.name, item.template.name, im.template.name));
                                return;
                            }
                        }
                        ItemRandom itemRandom = ServerRandom.CAPSULE_BACH_KIM.next();
                        int itemId = itemRandom.getId(gender);
                        int quantity = itemRandom.nextQuantity();
                        if (itemId == ItemName.XU) {
                            upXu(quantity);
                            if (quantity >= 20000000) {
                                Server.getInstance().service.serverChat(String.format("Chúc mừng %s mở %s nhận được %s xu", this.name, item.template.name, Utils.getMoneys(quantity)));
                            }
                        } else if (itemId == ItemName.SAO_PHA_LE_DANH_BONG) {
                            Item im = ItemManager.getInstance().createItem(itemId, quantity, false);
                            im.options.add(im.createOptionCrystal(true, true));
                            im.setExpiry(7);
                            addItem(im);
                        } else {
                            Item im = ItemManager.getInstance().createItem(itemId, quantity, true);
                            if (im.isItemBody()) {
                                im.randomParam(-15, 15);
                            }
                            if (addItem(im)) {
                                if (im.template.isAvatarLegendary() || im.isItemRider()) {
                                    Server.getInstance().service.serverChat(String.format("Chúc mừng %s mở %s nhận được %s", this.name, item.template.name, im.template.name));
                                }
                            }
                        }
                        service.createSpin(ServerRandom.CAPSULE_BACH_KIM, itemId, quantity);
                        return;
                    }

                    case ItemName.CAPSULE_KI_BI: {
                        if (isBagFull()) {
                            addInfo(INFO_RED, Language.ME_BAG_FULL);
                            return;
                        }
                        removeQuantityItemBag(index, 1);
                        int per = Utils.nextInt(100);
                        if (per < 5) {
                            int max = Math.max(level * 1000, 1000);
                            int xu = Utils.nextInt(max - max / 10, max);
                            if (Utils.nextInt(100) == 0) {
                                xu = Utils.nextInt(1000000, 1100000);
                                Server.getInstance().service.serverChat(String.format("Chúc mừng %s mở capsule kỳ bí nhận được %s Xu khóa", this.name, Utils.getMoneys(xu)));
                            }
                            upXuKhoa(xu);
                            addInfo(INFO_YELLOW, String.format("Bạn nhận được %s Xu khóa", Utils.getMoneys(xu)));
                        } else if (per < 80) {
                            Item spl = ItemManager.getInstance().createItem(ItemName.SAO_PHA_LE, 1, true);
                            spl.setExpiry(3);
                            addItem(spl);
                            addInfo(INFO_YELLOW, String.format("Bạn nhận được x%d %s", spl.quantity, spl.template.name));
                        } else {
                            int stone = Math.min(52 + level / 10, 55);
                            Item st = ItemManager.getInstance().createItem(Utils.nextInt(stone - 1, stone), 1, true);
                            st.setExpiry(3);
                            addItem(st);
                            addInfo(INFO_YELLOW, String.format("Bạn nhận được x%d %s", st.quantity, st.template.name));
                        }
                        return;
                    }

                    case ItemName.THE_TIEN_ICH: {
                        List<Command> commandList = new ArrayList<>();
                        commandList.add(new Command(CommandName.SHOW_SHOP_BUNMA, "Cửa hàng Bunma", this));
                        commandList.add(new Command(CommandName.SHOW_SHOP_BA_HAT_MIT, "Cửa hàng\nBà hạt mít", this));
                        commandList.add(new Command(CommandName.UPGRADE_ITEM, "Cường hóa", this));
                        commandList.add(new Command(CommandName.UPGRADE_STONE, "Luyện đá", this));
                        commandList.add(new Command(CommandName.CRYSTALLIZE, "Pha lê hóa", this));
                        commandList.add(new Command(CommandName.ENCHANT_ITEM, "Ép Pha lê", this));
                        commandList.add(new Command(CommandName.SHOW_TOP, "Bảng xếp hạng", this));
                        createMenu(NpcName.ME, "Thẻ tiện ích", commandList);
                        return;
                    }

                    case ItemName.MAY_DO_TINH_THACH:
                    case ItemName.MAY_DO_MANH_YARDRAT:
                    case ItemName.KINH_RAM:
                    case ItemName.RADAR_RT:
                    case ItemName.RADAR_RT2:
                    case ItemName.MAY_DO_CAPSULE_DONG: {
                        removeQuantityItemBag(index, 1);
                        int effectId = item.getEffectTemplateId();
                        if (effectId != -1) {
                            addTimeEffect(new Effect(this, effectId, item.getParam(23) * 60000L));
                        }
                        return;
                    }

                    case ItemName.MANH_GIAY: {
                        if (item.quantity < 300) {
                            addInfo(INFO_RED, "Cần ít nhất 300 mảnh giấy để đổi Sách kỹ năng");
                            return;
                        }
                        if (isBagFull()) {
                            addInfo(INFO_RED, Language.ME_BAG_FULL);
                            return;
                        }
                        removeQuantityItemBag(index, 300);
                        addItem(ItemManager.getInstance().createItem(ItemName.SACH_KY_NANG, 1, true));
                        return;
                    }

                    case ItemName.SACH_KY_NANG: {
                        if (pointSkill > 100) {
                            addInfo(INFO_RED, "Điểm kỹ năng quá cao, không thể sử dụng thêm");
                            return;
                        }
                        removeQuantityItemBag(index, 1);
                        pointSkill++;
                        service.refreshSkill(1, null);
                        addInfo(INFO_YELLOW, String.format("Điểm kỹ năng của bạn là %d", pointSkill));
                        return;
                    }

                    case ItemName.BANH_SAU: {
                        removeQuantityItemBag(index, 1);
                        addTimeEffect(new Effect(this, EffectName.BANH_SAU, 300000L, 0, 20));
                        service.setInfo();
                        return;
                    }

                    case ItemName.BANH_BO: {
                        removeQuantityItemBag(index, 1);
                        addTimeEffect(new Effect(this, EffectName.BANH_BO, 300000L, 0, 10));
                        service.setInfo();
                        return;
                    }

                    case ItemName.KEO_DO: {
                        removeQuantityItemBag(index, 1);
                        int effectId = item.getEffectTemplateId();
                        if (effectId != -1) {
                            addTimeEffect(new Effect(this, effectId, 300000L, 0, item.getParam(76)));
                        }
                        service.setInfo();
                        return;
                    }

                    case ItemName.KEO_VANG: {
                        removeQuantityItemBag(index, 1);
                        int effectId = item.getEffectTemplateId();
                        if (effectId != -1) {
                            addTimeEffect(new Effect(this, effectId, 300000L, 0, item.getParam(77)));
                        }
                        service.setInfo();
                        return;
                    }

                    case ItemName.KEO_LUC: {
                        removeQuantityItemBag(index, 1);
                        int effectId = item.getEffectTemplateId();
                        if (effectId != -1) {
                            addTimeEffect(new Effect(this, effectId, 300000L, 0, item.getParam(82)));
                        }
                        service.setInfo();
                        return;
                    }

                    case ItemName.KEO_XANH: {
                        removeQuantityItemBag(index, 1);
                        int effectId = item.getEffectTemplateId();
                        if (effectId != -1) {
                            addTimeEffect(new Effect(this, effectId, 300000L, 0, item.getParam(83)));
                        }
                        service.setInfo();
                        return;
                    }

                    case ItemName.CAPSULE_DONG: {
                        if (isBagFull()) {
                            addInfo(INFO_RED, Language.ME_BAG_FULL);
                            return;
                        }
                        removeQuantityItemBag(index, 1);
                        ItemRandom itemRandom = ServerRandom.CAPSULE_DONG.next();
                        int itemId = itemRandom.getId();
                        if (itemId == ItemName.XU) {
                            int xu = itemRandom.nextQuantity();
                            upXu(xu);
                            addInfo(INFO_YELLOW, String.format("Bạn nhận được %s xu", Utils.getMoneys(xu)));
                            if (xu >= 8000000) {
                                Server.getInstance().service.serverChat(String.format("Chúc mừng %s mở capsule đồng nhận được %s xu", this.name, Utils.getMoneys(xu)));
                            }
                        } else {
                            Item im = ItemManager.getInstance().createItem(itemId, true);
                            addItem(im);
                            addInfo(INFO_YELLOW, String.format("Bạn nhận được %s", im.template.name));
                        }
                        return;
                    }

                    case ItemName.DOI_TEN_DE_TU: {
                        if (Server.getInstance().isInterServer()) {
                            addInfo(INFO_RED, Language.CANCEL_ACTION_WHEN_SERVER_IS_INTER_SERVER);
                            return;
                        }
                        startClientInput(ClientInputType.INPUT_SET_DISCIPLE_NAME, "Đặt tên đệ tử", new TextField("Tên (5-10 kí tự)", TextField.TYPE_NORMAL));
                        return;
                    }

                    case ItemName.THE_DOI_TEN_DE_TU_DAC_BIET: {
                        if (Server.getInstance().isInterServer()) {
                            addInfo(INFO_RED, Language.CANCEL_ACTION_WHEN_SERVER_IS_INTER_SERVER);
                            return;
                        }
                        startClientInput(ClientInputType.INPUT_SET_DISCIPLE_NAME_DAC_BIET, "Đặt tên đệ tử", new TextField("Tên (5-15 kí tự, có thể viết hoa, có dấu, chứa dấu cách)", TextField.TYPE_NORMAL));
                        return;
                    }

                    case ItemName.THE_DOI_DE_TU: {
                        if (Server.getInstance().isInterServer()) {
                            addInfo(INFO_RED, Language.CANCEL_ACTION_WHEN_SERVER_IS_INTER_SERVER);
                            return;
                        }
                        if (disciple != null) {
                            if (isFusion()) {
                                addInfo(INFO_RED, Language.CANCEL_ACTION_WHEN_FUSION);
                                return;
                            }
                            if (disciple.isDead()) {
                                addInfo(INFO_RED, "Không thể thực hiện khi Đệ tử đang kiệt sức");
                                return;
                            }
                            for (Item i : disciple.itemsBody) {
                                if (i != null) {
                                    addInfo(INFO_RED, "Không thể thực hiện khi chưa tháo hết trang bị trên người Đệ tử");
                                    return;
                                }
                            }
                            removeQuantityItemBag(index, 1);
                            int gender = disciple.gender + 1;
                            if (gender > 2) {
                                gender = 0;
                            }
                            deleteDisciple();
                            createDisciple(gender, 0);
                        }
                        return;
                    }

                    case ItemName.THE_DOI_TEN_DAC_BIET: {
                        if (Server.getInstance().isInterServer()) {
                            addInfo(INFO_RED, Language.CANCEL_ACTION_WHEN_SERVER_IS_INTER_SERVER);
                            return;
                        }
                        startClientInput(ClientInputType.INPUT_DOI_TEN_DAC_BIET, "Đổi tên nhân vật", new TextField("Tên (5-15 kí tự, có thể viết hoa, có dấu, chứa dấu cách)", TextField.TYPE_NORMAL));
                        return;
                    }

                    case ItemName.THE_DOI_TEN: {
                        if (Server.getInstance().isInterServer()) {
                            addInfo(INFO_RED, Language.CANCEL_ACTION_WHEN_SERVER_IS_INTER_SERVER);
                            return;
                        }
                        startClientInput(ClientInputType.INPUT_DOI_TEN, "Đổi tên nhân vật", new TextField("Tên (5-10 kí tự)", TextField.TYPE_NORMAL));
                        return;
                    }

                    case ItemName.RUONG_KHO_BAU: {
                        if (isBagFull()) {
                            addInfo(INFO_RED, Language.ME_BAG_FULL);
                            return;
                        }
                        removeQuantityItemBag(index, 1);
                        ItemRandom itemRandom = ServerRandom.RUONG_KHO_BAU.next();
                        if (itemRandom.getId() == ItemName.RUBY) {
                            int quantity = itemRandom.nextQuantity();
                            upRuby(quantity);
                            addInfo(INFO_YELLOW, String.format("Bạn nhận được %d Ruby", quantity));
                        } else {
                            addInfo(INFO_RED, "Rương trống rỗng");
                        }
                        return;
                    }

                    case ItemName.THE_DOI_KY_NANG_DE_TU: {
                        Command yes = new Command(CommandName.CONFIRM_CHANGE_ALL_SKILL_DISCIPLE, "Có", this);
                        Command no = new Command(CommandName.CANCEL, "Không", this);
                        startYesNo("Bạn có chắc chắc muốn đổi toàn bộ kỹ năng của đệ tử không?", yes, no);
                        return;
                    }

                }
                return;
            }
        }
    }

    public void setIsAutoPlay(boolean isAutoPlay) {
        this.isAutoPlay = isAutoPlay;
        service.setIsAutoPlay();
        if (!isAutoPlay && zoneGoBack != null) {
            setAreaGoBack(null);
        }
    }

    public void showListMapSpaceship(ArrayList<KeyValue<Map, String>> maps) {
        showListMapSpaceship(maps, false);
    }

    public void showListMapSpaceship(ArrayList<KeyValue<Map, String>> maps, boolean needCapsuleWhenSelect) {
        showListMapSpaceship(maps, needCapsuleWhenSelect, false, false);
    }

    public void showListMapSpaceshipCapsule(ArrayList<KeyValue<Map, String>> maps) {
        showListMapSpaceshipCapsule(maps, false);
    }

    public void showListMapSpaceshipCapsule(ArrayList<KeyValue<Map, String>> maps, boolean needCapsuleWhenSelect) {
        showListMapSpaceship(maps, needCapsuleWhenSelect, true, true);
    }

    private void showListMapSpaceship(ArrayList<KeyValue<Map, String>> maps, boolean needCapsuleWhenSelect, boolean currentPlanetOnly, boolean groupByArea) {
        if (zone == null || isDead()) {
            return;
        }
        mapSpaceships.clear();
        MapPlanet currentPlanet = zone.map.template.planet;
        for (KeyValue<Map, String> destination : maps) {
            if (destination == null || destination.key == null) {
                continue;
            }
            if (currentPlanetOnly && destination.key.template.planet != currentPlanet) {
                continue;
            }
            mapSpaceships.add(destination);
        }
        if (groupByArea) {
            mapSpaceships = groupMapDestinationsByArea(mapSpaceships);
        }
        mapSpaceshipNeedCapsule = needCapsuleWhenSelect;
        if (mapSpaceships.isEmpty()) {
            mapSpaceshipNeedCapsule = false;
            addInfo(INFO_YELLOW, "Hiện tại không có điểm đến nào");
        }
        service.showListMapSpaceship();
    }

    private ArrayList<KeyValue<Map, String>> groupMapDestinationsByArea(ArrayList<KeyValue<Map, String>> maps) {
        ArrayList<KeyValue<Map, String>> groupedDestinations = new ArrayList<>();
        LinkedHashMap<String, MapAreaGroup> groups = new LinkedHashMap<>();
        for (KeyValue<Map, String> destination : maps) {
            String mapName = destination.value == null ? "" : destination.value.trim();
            if (mapName.isEmpty() || mapName.startsWith("Chỗ cũ:")) {
                groupedDestinations.add(destination);
                continue;
            }
            java.util.regex.Matcher matcher = MAP_GROUP_TRAILING_NUMBER_PATTERN.matcher(mapName);
            if (!matcher.matches()) {
                groupedDestinations.add(destination);
                continue;
            }
            String baseName = matcher.group(1) == null ? "" : matcher.group(1).trim();
            if (baseName.isEmpty()) {
                groupedDestinations.add(destination);
                continue;
            }
            int order;
            try {
                order = Integer.parseInt(matcher.group(2));
            } catch (Exception e) {
                groupedDestinations.add(destination);
                continue;
            }
            String groupKey = baseName.toLowerCase();
            MapAreaGroup group = groups.get(groupKey);
            if (group == null) {
                group = new MapAreaGroup(baseName, destination, order);
                groups.put(groupKey, group);
            } else {
                group.addDestination(destination, order);
            }
        }
        for (MapAreaGroup group : groups.values()) {
            if (group.count >= 2) {
                String info = getGroupedDestinationInfo(group);
                groupedDestinations.add(new KeyValue<>(group.defaultDestination.key, group.baseName, info));
            } else {
                groupedDestinations.add(group.defaultDestination);
            }
        }
        return groupedDestinations;
    }

    private String getGroupedDestinationInfo(MapAreaGroup group) {
        String originInfo = "";
        if (group.defaultDestination.elements != null && group.defaultDestination.elements.length > 0 && group.defaultDestination.elements[0] != null) {
            originInfo = String.valueOf(group.defaultDestination.elements[0]);
        }
        String range = group.minOrder == group.maxOrder ? String.valueOf(group.minOrder) : (group.minOrder + "-" + group.maxOrder);
        String defaultMap = group.defaultDestination.value == null ? "" : group.defaultDestination.value;
        if (originInfo == null || originInfo.isEmpty()) {
            return "Khu " + range + " | Mặc định: " + defaultMap;
        }
        return originInfo + " | Khu " + range + " | Mặc định: " + defaultMap;
    }

    private static class MapAreaGroup {
        private final String baseName;
        private KeyValue<Map, String> defaultDestination;
        private int minOrder;
        private int maxOrder;
        private int count;

        private MapAreaGroup(String baseName, KeyValue<Map, String> destination, int order) {
            this.baseName = baseName;
            this.defaultDestination = destination;
            this.minOrder = order;
            this.maxOrder = order;
            this.count = 1;
        }

        private void addDestination(KeyValue<Map, String> destination, int order) {
            if (order < minOrder) {
                minOrder = order;
                defaultDestination = destination;
            }
            if (order > maxOrder) {
                maxOrder = order;
            }
            count++;
        }
    }

    public void selectMapSpaceship(Message message) {
        try {
            if (isDead()) {
                return;
            }
            if (isStun()) {
                addInfo(INFO_RED, Language.CANT_ACTION);
                return;
            }
            if (isTrading()) {
                addInfo(INFO_RED, Language.CANCEL_ACTION_WHEN_TRADE);
                return;
            }
            int select = message.reader().readByte();
            if (select < 0 || select >= mapSpaceships.size()) {
                return;
            }
            KeyValue<Map, String> keyValue = mapSpaceships.get(select);
            Map map = keyValue.key;
            if (map == null || map.template.planet != zone.map.template.planet) {
                addInfo(INFO_RED, "Chỉ có thể chọn map cùng hành tinh hiện tại");
                mapSpaceships.clear();
                mapSpaceshipNeedCapsule = false;
                return;
            }
            if (map.template.id == MapName.DAU_TRUONG) {
                ArenaCustom zone = (ArenaCustom) keyValue.elements[1];
                if (zone.status == ArenaCustom.STATUS_CLOSE) {
                    addInfo(INFO_RED, "Trận chiến đã kết thúc");
                    return;
                }
                this.x = ArenaCustom.POSITION_REFEREE[0][0];
                this.y = ArenaCustom.POSITION_REFEREE[0][1];
                zone.enter(this);
                mapSpaceshipNeedCapsule = false;
                return;
            }
            if (map.template.id == MapName.LANH_DIA_BANG_HOI && (clan == null || clan.map != map)) {
                addInfo(INFO_RED, Language.CANT_ACTION);
                return;
            }
            if (mapSpaceshipNeedCapsule && !consumeCapsuleForMapTeleport()) {
                mapSpaceships.clear();
                mapSpaceshipNeedCapsule = false;
                return;
            }
            if (map.template.id == lastMapSpaceshipId) {
                lastMapSpaceshipId = -1;
            } else if (zone.map.template.id != MapName.LANH_DIA_BANG_HOI && zone.map.template.planet != MapPlanet.COLD && zone.map.template.planet != MapPlanet.FIRE && zone.map.template.id != MapName.DAU_TRUONG) {
                lastMapSpaceshipId = zone.map.template.id;
            }
            teleport(map, map.expansion != null);
            mapSpaceships.clear();
            mapSpaceshipNeedCapsule = false;
        } catch (Exception ex) {
            logger.error("selectMapSpaceship", ex);
        }
    }

    public boolean isCanJoinMap(Map map) {
        if (map == null) {
            return false;
        }
        if (isAdmin()) {
            return true;
        }
        if (map.isIsland()) {
            return pointPk == 0;
        }
        if (map.template.id == MapName.DAO_HOA_1 || map.template.id == MapName.DAO_HOA_2) {
            return true;
        }
        int mapId = map.template.id;
        if (MapManager.getInstance().isMapBarrack(mapId)) {
            return true;
        }
        if (map.isDragonBallNamek()) {
            return true;
        }
        if (map.isSurvival()) {
            return true;
        }
        if (map.isManor()) {
            return true;
        }
        if (map.isTreasure()) {
            return true;
        }
        if (taskMain == null) {
            return false;
        }
        if (mapId == 25 && taskMain.template.id < 23) {
            return false;
        }
        if (taskMain.template.id < 2 && mapId != 0 && mapId != 9) {
            return false;
        }
        if (taskMain.template.id == 2) {
            if (taskMain.index < 2 && mapId != 0 && mapId != 9) {
                return false;
            }
            if (taskMain.index >= 2 && mapId != 0 && mapId != 9 && mapId != 1 && mapId != 3) {
                return false;
            }
        }
        if (taskMain.template.id == 3 && mapId >= 5 && mapId != 9) {
            return false;
        }
        if (taskMain.template.id == 4 && mapId >= 5 && mapId != 9 && mapId != 20 && mapId != 21) {
            return false;
        }
        if (taskMain.template.id == 5 && mapId >= 7 && mapId != 9 && mapId != 20 && mapId != 21) {
            return false;
        }
        if (taskMain.template.id == 6 && mapId > 9 && mapId != 19 && mapId != 20 && mapId != 21) {
            return false;
        }
        if (taskMain.template.id == 7 && mapId > 9 && mapId != 19 && mapId != 20 && mapId != 21) {
            return false;
        }
        if (taskMain.template.id == 8 && mapId > 9 && mapId != 19 && mapId != 20 && mapId != 21) {
            return false;
        }
        if (taskMain.template.id == 9 && mapId >= 15 && mapId != 19 && mapId != 17 && mapId != 18 && mapId != 20 && mapId != 21 && mapId != 22) {
            return false;
        }
        if (taskMain.template.id < 11 && mapId >= 24) {
            return false;
        }
        if (taskMain.template.id < 13 && mapId >= 27) {
            return false;
        }
        if (taskMain.template.id < 16 && mapId >= 59) {
            return false;
        }
        if (taskMain.template.id < 19 && mapId >= 62) {
            return false;
        }
        return true;
    }

    public boolean isCanUseFusion() {
        return zone != null && zone.map.template.id != MapName.DAI_HOI_VO_THUAT && zone.map.template.id != MapName.DAU_TRUONG && planet != MapPlanet.SURVIVAL && !isInTreasure();
    }

    public void showReward() {
        long now = System.currentTimeMillis();
        rewards.removeIf(r -> r.getExpiry(now) <= 0);
        service.showListReward(now);
    }

    public void reward(Message message) {
        if (Server.getInstance().isInterServer()) {
            addInfo(INFO_RED, Language.CANCEL_ACTION_WHEN_SERVER_IS_INTER_SERVER);
            return;
        }
        lockAction.lock();
        try {
            if (rewards.isEmpty()) {
                return;
            }
            int index = message.reader().readByte();
            if (index < 0 || index >= rewards.size()) {
                return;
            }
            long now = System.currentTimeMillis();
            Reward reward = rewards.get(index);
            if (reward.getExpiry(now) <= 0) {
                addInfo(INFO_RED, "Quà tặng đã hết hạn");
                return;
            }
            List<Item> items = reward.items;
            if (getCountItemBagEmpty() < items.size()) {
                addInfo(INFO_RED, String.format("Cần ít nhất %d ô trống trong túi đồ", items.size()));
                return;
            }
            GameRepository.getInstance().rewardData.setRewardTime(reward.id, new Timestamp(now));
            rewards.remove(reward);
            for (Item item : items) {
                addItem(item.cloneItem(), true);
            }
            service.showListReward(now);
        } catch (Exception ex) {
            logger.error("reward", ex);
        } finally {
            lockAction.unlock();
        }
    }

    private void itemOtherToBag(int index) {
        if (index < 0 || index >= itemsOther.length) {
            return;
        }
        Item item = itemsOther[index];
        if (item == null) {
            return;
        }
        int indexEmpty = getIndexItemBagEmpty();
        if (indexEmpty == -1) {
            addInfo(INFO_RED, Language.ME_BAG_FULL);
            return;
        }
        itemsBag[indexEmpty] = item.cloneItem();
        itemsBag[indexEmpty].indexUI = indexEmpty;
        itemsOther[index] = null;
        service.refreshItemBag(indexEmpty);
        service.refreshItemOther(index);
        service.setInfo();
        service.setPart();
        if (zone != null) {
            zone.service.refreshHp(this);
            zone.service.refreshPlayerPart(this);
        }
    }

    private void itemBagToOther(Item item) {
        if (item.template.gender != -1 && item.template.gender != gender) {
            addInfo(INFO_RED, Language.ME_CANT_USE_EQUIP);
            return;
        }
        if (item.template.levelRequire > level) {
            addInfo(INFO_RED, Language.LEVEL_NOT_ENOUGH);
            return;
        }
        if (item.options.isEmpty()) {
            addInfo(INFO_RED, "Trang bị chưa được nâng cấp");
            return;
        }
        int index = item.indexUI;
        int type = item.template.type;
        Item itemBody = itemsOther[type];
        itemsOther[type] = item.cloneItem();
        itemsOther[type].indexUI = type;
        itemsOther[type].isLock = true;
        if (itemBody != null) {
            itemsBag[index] = itemBody.cloneItem();
            itemsBag[index].indexUI = index;
            service.refreshItemBag(index);
        } else {
            itemsBag[index] = null;
            service.setItemBag();
        }
        service.refreshItemOther(type);
        service.setInfo();
        service.setPart();
        if (zone != null) {
            zone.service.refreshHp(this);
            zone.service.refreshPlayerPart(this);
        }
    }

    private void itemBagToBody(Item item) {
        if (item.template.gender != -1 && item.template.gender != gender) {
            addInfo(INFO_RED, Language.ME_CANT_USE_EQUIP);
            return;
        }
        if (item.template.levelRequire > level) {
            addInfo(INFO_RED, Language.LEVEL_NOT_ENOUGH);
            return;
        }
        if (item.template.type == ItemType.TYPE_BONG_TAI) {
            if (disciple == null || disciple.isDead() || isFusion() || !isCanUseFusion() || isHaveEffect(EffectName.HOP_THE_NAMEK, EffectName.HOP_THE_TRAI_DAT_VA_SAYAIN) || (disciple.itemsBody[ItemType.TYPE_BONG_TAI] != null && disciple.itemsBody[ItemType.TYPE_BONG_TAI].template.id == ItemName.BONG_TAI_PORATA)) {
                addInfo(INFO_RED, Language.CANT_ACTION);
                return;
            }
            long now = System.currentTimeMillis();
            long time = 10000L + lastTimeFusion - now;
            if (time > 0) {
                addInfo(INFO_RED, String.format("Vui lòng thử lại sau %s", Utils.formatTime(time)));
                return;
            }
            Effect effect = findEffectByTemplateId(EffectName.DELAY_HOP_THE);
            if (effect != null && effect.time > 0) {
                addInfo(INFO_RED, String.format("Vui lòng thử lại sau %s", Utils.formatTime(effect.time)));
                return;
            }
            lastTimeFusion = now;
        }
        if (item.template.id == ItemName.HUY_HIEU_BANG_HOI_CAP_6 && item.options.stream().anyMatch(o -> o.template.id == 189)) {
            Item itemBody = itemsBody[ItemType.TYPE_MEDAL];
            if (itemBody == null || itemBody.getExpiry() != -1) {
                addInfo(INFO_RED, "Bạn chưa trang bị Huy hiệu không có hạn sử dụng");
                return;
            }
            ItemOption option = item.getOption(189);
            if (option == null) {
                return;
            }
            item.options.remove(option);
            for (ItemOption itemOption : itemBody.options) {
                if (itemOption.template.id == 19 || itemOption.template.id == 67 || itemOption.template.id == 68 || itemOption.template.type == 4 || itemOption.template.id == 176) {
                    item.options.add(new ItemOption(itemOption.template.id, itemOption.param));
                } else {
                    item.options.add(new ItemOption(itemOption.template.id, itemOption.param * (100 + Utils.nextInt(option.param * 2 / 3, option.param)) / 100));
                }
            }
        }
        int index = item.indexUI;
        int type = item.getIndexBody();
        Item itemBody = itemsBody[type];
        itemsBody[type] = item.cloneItem();
        itemsBody[type].indexUI = type;
        if (itemBody != null) {
            itemsBag[index] = itemBody.cloneItem();
            itemsBag[index].indexUI = index;
            service.refreshItemBag(index);
        } else {
            itemsBag[index] = null;
            service.setItemBag();
        }
        service.refreshItemBody(type);
        if (item.template.type == ItemType.TYPE_BONG_TAI) {
            fusion(1);
            typeFusion = item.getUpgrade() > 0 ? 2 : 1;
        }
        if (type == 15) {
            if (zone != null && pet != null) {
                zone.leave(pet);
                service.showTab(-1);
            }
            pet = new Pet(this, itemsBody[type]);
            petJoin();
        }
        service.setInfo();
        service.setPart();
        if (zone != null) {
            zone.service.refreshHp(this);
            zone.service.refreshPlayerPart(this);
        }
        if (taskMain != null && taskMain.template.id == 1 && taskMain.index == 3) {
            nextTaskIndex();
        }
        if (item.template.type == ItemType.TYPE_BONG_TAI) {
            recovery(RECOVERY_ALL, 100, true);
        }
        List<Integer> optionIds = new ArrayList<>();
        for (ItemOption option : item.options) {
            optionIds.add(option.template.id);
        }
        if (!optionIds.isEmpty()) {
            for (Skill skill : skills) {
                if (skill.template.optionRequire != -1 && skill.upgrade < 5 && optionIds.contains(skill.template.optionRequire)) {
                    skill.timeCanUse = System.currentTimeMillis() + skill.getCoolDown();
                    service.setTimeCanUseForSkill(skill);
                }
            }
        }
    }

    public void fusion(int type) {
        if (disciple == null) {
            return;
        }
        // 1 -> hợp thể
        // - -> tách hợp thể
        if (type == 1) {
            if (!isFusion) {
                if (disciple.zone != null) {
                    disciple.zone.leave(disciple);
                }
                disciple.status = DiscipleStatus.GO_HOME;
                service.discipleInfo(MessageDiscipleInfoName.DISCIPLE_STATUS);
                isFusion = true;
            }
        } else if (type == 0) {
            if (isFusion) {
                Utils.setTimeout(() -> {
                    try {
                        disciple.status = DiscipleStatus.PROTECT;
                        if (zone != null) {
                            disciple.followMaster();
                            zone.enter(disciple);
                        }
                        service.discipleInfo(MessageDiscipleInfoName.DISCIPLE_STATUS);
                    } catch (Exception ignored) {
                    }
                }, 1000);
                isFusion = false;
            }
        }
        service.fusion(this, type);
        if (zone != null) {
            zone.service.fusion(this, type);
        }
    }

    public void sortArrayItem(Item[] items) {
        // sort quantity
        for (int i = 0; i < items.length - 1; i++) {
            if (items[i] == null) {
                continue;
            }
            if (items[i].quantity <= 0) {
                items[i] = null;
                continue;
            }
            if (!items[i].template.isUpToUp) {
                continue;
            }
            int max_quantity = items[i].getMaxQuantity();
            for (int j = i + 1; j < items.length; j++) {
                if (items[j] == null) {
                    continue;
                }
                if (items[j].quantity <= 0) {
                    items[j] = null;
                    continue;
                }
                if (items[j].template.id != items[i].template.id || items[j].isLock != items[i].isLock) {
                    continue;
                }
                // sort option
                boolean isDelete = false;
                for (ItemOption option_i : items[i].options) {
                    int optionId = option_i.template.id;
                    if (optionId == 24) {
                        for (int k = 0; k < items[j].options.size(); k++) {
                            ItemOption option_j = items[j].options.get(k);
                            if (option_j.template.id == optionId) {
                                if (option_i.param + option_j.param > max_quantity) {
                                    option_j.param = option_i.param + option_j.param - max_quantity;
                                    option_i.param = max_quantity;
                                } else {
                                    option_i.param += option_j.param;
                                    option_j.param = 0;
                                }
                                isDelete = true;
                                break;
                            }
                        }
                    }
                }
                if (isDelete) {
                    /*for (int k = 0; k < items[j].options.size(); k++) {
                        ItemOption option_j = items[j].options.get(k);
                        int optionId = option_j.optionTemplate.id;
                        if ((optionId == 1 || optionId == 31 || optionId == 11 || optionId == 12 || optionId == 13) && option_j.param > 0) {
                            isDelete = false;
                            break;
                        }
                    }
                    if (isDelete) {
                        items[j] = null;
                    }*/
                    items[j] = null; // mac dinh xoa
                } else {
                    // sort quantity
                    // check option -> nếu giống nhau tất cả các option thì có thể up to up
                    int count = 0;
                    int day = -1;
                    if (items[i].options.size() == items[j].options.size()) {
                        for (ItemOption option_i : items[i].options) {
                            for (ItemOption option_j : items[j].options) {
                                if (option_i.template.id == option_j.template.id) {
                                    if (option_i.template.id == 50) {
                                        count++;
                                        day = Math.min(option_i.param, option_j.param);
                                    } else if (option_i.param == option_j.param) {
                                        count++;
                                    }
                                }
                            }
                        }
                    } else {
                        count = -1;
                    }
                    if (count == items[i].options.size()) {
                        if (items[i].quantity + items[j].quantity > max_quantity) {
                            items[j].quantity = items[i].quantity + items[j].quantity - max_quantity;
                            items[i].quantity = max_quantity;
                        } else {
                            items[i].quantity += items[j].quantity;
                            items[j] = null;
                        }
                        if (day != -1) {
                            items[i].setExpiry(day);
                            /*if (items[j] != null) {
                                items[j].setExpiry(day);
                            }*/
                        }
                    }
                }
            }
        }

        // sort null
        for (int i = 0; i < items.length - 1; i++) {
            if (items[i] == null) {
                for (int j = i + 1; j < items.length; j++) {
                    if (items[j] != null) {
                        Item item = items[i];
                        items[i] = items[j];
                        items[i].indexUI = i;
                        items[j] = item;
                        break;
                    }
                }
            }
        }
    }

    public void addInfo(int type, String info) {
        service.addInfo(type, info);
    }

    public void chatPublic(Message message) {
        try {
            if (zone == null) {
                return;
            }
            long now = System.currentTimeMillis();
            if (lockInfo != null && now < lockInfo.chatTime) {
                service.startDialogOk(String.format("Tài khoản của bạn bị khóa chat đến %s", Utils.formatTime(lockInfo.chatTime)));
                return;
            }
            if (!isAdmin() && now - lastTimeChatPublic < 5000) {
                return;
            }
            lastTimeChatPublic = now;
            String content = message.reader().readUTF().toLowerCase().trim();
            if (content.isEmpty()) {
                return;
            }
            if (content.length() > 200) {
                content = content.substring(0, 200);
            }
            for (String txt : Language.text_toxic) {
                if (content.contains(txt)) {
                    content = content.replace(txt, "*");
                }
            }
            if (isAdmin()) {
                if (content.startsWith("map ")) {
                    try {
                        int mapId = Integer.parseInt(content.substring(4));
                        Map map = MapManager.getInstance().maps.get(mapId);
                        if (MapManager.getInstance().isMapBarrack(mapId) && getBarrack() != null) {
                            map = getBarrack().findMap(mapId);
                        }
                        if (map != null) {
                            x = 90;
                            joinMap(map, -1);
                        }
                        return;
                    } catch (Exception ignored) {
                    }
                } else if (content.startsWith("item ")) {
                    try {
                        int itemId = Integer.parseInt(content.substring(5));
                        Item item = ItemManager.getInstance().createItem(itemId, true);
                        if (item.template.isUpToUp) {
                            item.quantity = 1000000;
                        }
                        if (item.isItemBody()) {
                            item.randomParam(5, 12);
                        }
                        addItem(item);
                        addInfo(INFO_YELLOW, String.format("Bạn nhận được x%d %s", item.quantity, item.template.name));
                        return;
                    } catch (Exception ignored) {
                    }
                } else if (content.startsWith("e ")) {
                    try {
                        int effect = Integer.parseInt(content.substring(2));
                        addEffect(new Effect(this, effect, 15000));
                        return;
                    } catch (Exception ignored) {
                    }
                } else if (content.startsWith("lock ")) {
                    try {
                        int id = Integer.parseInt(content.split(" ")[1]);
                        GameRepository.getInstance().playerData.setLockNow(id, true);
                        Player player = PlayerManager.getInstance().findPlayerById(id);
                        if (player != null) {
                            player.session.disconnect();
                        }
                        service.startDialogOk("Khóa thành công " + id);
                        return;
                    } catch (Exception ignored) {
                    }
                } else if (content.startsWith("unlock ")) {
                    try {
                        int id = Integer.parseInt(content.split(" ")[1]);
                        GameRepository.getInstance().playerData.setLockNow(id, false);
                        service.startDialogOk("Mở khóa thành công " + id);
                        return;
                    } catch (Exception ignored) {
                    }
                }
            }
            if (disciple != null && !isFusion()) {
                if (content.equals("di theo") || content.equals("follow")) {
                    setDiscipleStatus(DiscipleStatus.FOLLOW);
                    return;
                }
                if (content.equals("bao ve") || content.equals("protect")) {
                    setDiscipleStatus(DiscipleStatus.PROTECT);
                    return;
                }
                if (content.equals("tan cong") || content.equals("attack")) {
                    setDiscipleStatus(DiscipleStatus.ATTACK);
                    return;
                }
                if (content.equals("ve nha") || content.equals("go home")) {
                    setDiscipleStatus(DiscipleStatus.GO_HOME);
                    return;
                }
            }
            if (((Event.isNoel() && content.equals("noel"))
                    || (Event.isTet() && content.equals("tet"))
                    || (Event.isTuuTruong2024() && content.equals("mh"))
                    || (Event.isHungVuong() && content.equals("hv")))
                    && escort == null && !isDead()) {
                List<Player> playerList = zone.getPlayers(Zone.TYPE_ESCORT);
                if (!playerList.isEmpty()) {
                    Player player = playerList.get(0);
                    int distance = Utils.getDistance(this, player);
                    for (int i = 1; i < playerList.size(); i++) {
                        Player p = playerList.get(i);
                        int dis = Utils.getDistance(this, p);
                        if (distance > dis) {
                            distance = dis;
                            player = p;
                        }
                    }
                    ((Escort) player).setFollower(this);
                }
            }
            zone.service.chatPublic(this, content);
        } catch (Exception ex) {
            logger.error("chatPublic", ex);
        }
    }

    public void chat(String content) {
        if (zone != null) {
            zone.service.chatPublic(this, content);
        }
    }

    public void chatPlayer(Message message) {
        try {
            long now = System.currentTimeMillis();
            if (lockInfo != null && now < lockInfo.chatTime) {
                service.startDialogOk(String.format("Tài khoản của bạn bị khóa chat đến %s", Utils.formatTime(lockInfo.chatTime)));
                return;
            }
            if (now - lastTimeChatPlayer < 1000) {
                return;
            }
            lastTimeChatPlayer = now;
            int playerId = message.reader().readInt();
            if (this.id == playerId) {
                return;
            }
            Player player = PlayerManager.getInstance().findPlayerById(playerId);
            if (player == null) {
                addInfo(INFO_RED, "Hiện tại người chơi không online");
                return;
            }
            String content = message.reader().readUTF().toLowerCase();
            if (content.isEmpty()) {
                return;
            }
            if (content.length() > 200) {
                content = content.substring(0, 200);
            }
            for (String txt : Language.text_toxic) {
                if (content.contains(txt)) {
                    content = content.replace(txt, "*");
                }
            }
            Message msg = new Message(MessageName.CHAT_PLAYER);
            msg.writer().writeInt(id);
            msg.writer().writeUTF(name);
            msg.writer().writeShort(head);
            msg.writer().writeUTF(content);
            player.sendMessage(msg);
            msg.cleanup();
        } catch (Exception ex) {
            logger.error("chatPlayer", ex);
        }
    }

    public void setDiscipleStatus(DiscipleStatus status) {
        if (disciple == null) {
            return;
        }
        if (isFusion()) {
            addInfo(INFO_RED, Language.CANT_ACTION);
            return;
        }
        disciple.status = status;
        switch (status.ordinal()) {
            case 0:
                disciple.chat("Ok con theo sư phụ");
                break;

            case 1:
                disciple.chat("Ok con sẽ bảo vệ sư phụ");
                break;

            case 2:
                disciple.chat("Ok sư phụ để con lo cho");
                break;

            case 3:
                if (disciple.zone != null) {
                    disciple.chat("Ok con về, bibi sư phụ");
                    if (!disciple.isDead()) {
                        disciple.clearAllEffect();
                        Utils.setTimeout(() -> {
                            if (disciple.zone != null) {
                                disciple.zone.leave(disciple);
                            }
                        }, 2000);
                    }
                }
                break;

            case 4: {
                if (!isCanUseFusion()) {
                    addInfo(INFO_RED, Language.CANT_ACTION);
                    return;
                }
                long now = System.currentTimeMillis();
                long time = 10000L + lastTimeFusion - now;
                if (time > 0) {
                    addInfo(INFO_RED, String.format("Vui lòng thử lại sau %s", Utils.formatTime(time)));
                    return;
                }
                Effect effect = findEffectByTemplateId(EffectName.DELAY_HOP_THE);
                if (effect != null && effect.time > 0) {
                    addInfo(INFO_RED, String.format("Vui lòng thử lại sau %s", Utils.formatTime(effect.time)));
                    return;
                }
                lastTimeFusion = now;
                if (zone == null || isInSurvival() || zone.map.template.id == MapName.DAI_HOI_VO_THUAT || isInTreasure()) {
                    addInfo(INFO_RED, Language.CANT_ACTION);
                    return;
                }
                addEffect(new Effect(this, gender == 1 ? EffectName.HOP_THE_NAMEK : EffectName.HOP_THE_TRAI_DAT_VA_SAYAIN, Math.max(180000L, 600000L - 60000L * (level / 5))));
                fusion(1);
                typeFusion = 0;
                service.setInfo();
                service.setPart();
                if (zone != null) {
                    zone.service.refreshHp(this);
                    zone.service.refreshPlayerPart(this);
                }
                recovery(RECOVERY_ALL, 100, true);
                break;
            }
        }
    }

    public void pickItem(Message message) {
        try {
            if (taskMain != null && taskMain.template.id < 2) {
                addInfo(INFO_RED, "Bạn cần phải làm nhiệm vụ trước");
                return;
            }
            long now = System.currentTimeMillis();
            if (now - lastTimePickItem < 500) {
                return;
            }
            lastTimePickItem = now;
            int itemMapId = message.reader().readInt();
            if (itemMapId < 0 || isTrading() || isDead()) {
                return;
            }
            ItemMap itemMap = zone.findItemMapById(itemMapId);
            if (itemMap == null) {
                return;
            }
            Effect effect = findEffectByTemplateId(EffectName.TU_DONG_LUYEN_TAP);
            if (effect == null) {
                int distance = Math.abs(this.x - itemMap.x);
                if (distance > 1000) {
                    tickPickMod++;
                    if (tickPickMod >= 10) {
                        Server.getInstance().service.lockPlayer(this.name, 10, "Nhặt vật phẩm từ khoảng cách xa", true, 0);
                        return;
                    }
                    return;
                }
            }
            pickItem(itemMap);
        } catch (Exception ex) {
            logger.error("pickItem", ex);
        }
    }

    public void pickItem(ItemMap itemMap) {
        if (itemMap == null) {
            return;
        }
        itemMap.lock.lock();
        try {
            if (itemMap.isPickedUp || isTrading() || isDead()) {
                return;
            }
            if (isBagFull()) {
                addInfo(INFO_RED, Language.ME_BAG_FULL);
                return;
            }
            if ((itemMap.template.type == ItemType.TYPE_TASK || itemMap.isTask) && (taskMain.template.steps[taskMain.index].itemId != itemMap.template.id || taskMain.template.steps[taskMain.index].itemId == -1)) {
                addInfo(INFO_RED, "Không thể nhặt vật phẩm này!");
                return;
            }
            if (itemMap.playerId != -1 && itemMap.playerId != this.id) {
                addInfo(INFO_RED, "Không thể nhặt vật phẩm của người khác");
                return;
            }
            if (itemMap.players != null && !itemMap.players.contains(this.id)) {
                addInfo(INFO_RED, "Bạn không đủ điều kiện để nhặt vật phẩm này");
                return;
            }
            if (itemMap.lockTime > 0) {
                long time = itemMap.lockTime - System.currentTimeMillis();
                if (time > 0) {
                    addInfo(INFO_RED, String.format("Chỉ có thể nhặt sau %s", Utils.formatTime(time)));
                    return;
                }
            }
            if (isInDragonBallNamek() && itemMap.template.type == ItemType.TYPE_DRAGON_BALL_NAMEK) {
                if (itemMap.template.id == ItemName.NGOC_RONG_NAMEK_STONE) {
                    addInfo(INFO_RED, "Chỉ là hòn đá, vác chi cho nặng");
                    return;
                }
                ZoneDragonBallNamek areaDragonBallNamek = (ZoneDragonBallNamek) this.zone;
                if (areaDragonBallNamek.isReward) {
                    addInfo(INFO_RED, "Trận chiến trong khu vực này đã kết thúc");
                    return;
                }
                itemMap.isPickedUp = true;
                areaDragonBallNamek.setHolder(this);
                this.bag = this.bagLoot = itemMap.template.bag;
                zone.service.setBag(this);
                service.pickItem(itemMap);
                return;
            }
            if (itemMap.template.type == ItemType.TYPE_DRAGON_BALL_NAMEK && zone instanceof ZoneFlagWar zoneFlagWar) {
                itemMap.isPickedUp = true;
                zoneFlagWar.setHolder(this);
                this.bag = this.bagLoot = itemMap.template.bag;
                zone.service.setBag(this);
                service.pickItem(itemMap);
                return;
            }
            if (isInTreasure()) {
                Treasure treasure = MapManager.getInstance().treasure;
                if (treasure == null) {
                    addInfo(INFO_RED, "Trận chiến đã kết thúc");
                    return;
                }
                Pirate pirate = treasure.findPirateByPlayerId(this.id);
                if (pirate == null) {
                    addInfo(INFO_RED, "Trận chiến đã kết thúc");
                    return;
                }
                if (pirate.point < 5) {
                    addInfo(INFO_RED, "Yêu cầu điểm tích lũy tối thiểu là 5 để nhặt vật phẩm");
                    return;
                }
            }
            if (isInSurvival() || planet == MapPlanet.SURVIVAL) {
                if (itemMap.template.id == ItemName.CAPSULE_VIEN_TRO_SURVIVAL) {
                    itemMap.isPickedUp = true;
                    service.pickItem(itemMap);
                    List<ItemTemplate> templates = new ArrayList<>();
                    for (int i = 208; i < 224; i++) {
                        ItemTemplate template = ItemManager.getInstance().itemTemplates.get(i);
                        if (template.gender == -1 || template.gender == gender) {
                            templates.add(template);
                        }
                    }
                    ItemTemplate template = templates.get(Utils.nextInt(templates.size()));
                    if (itemsSurvival[template.type] == null || itemsSurvival[template.type].template.id != template.id) {
                        int upgrade = itemsSurvival[template.type] != null ? (itemsSurvival[template.type].getUpgrade() + 1) : 0;
                        Item item = ItemManager.getInstance().createItem(template.id, 1, true);
                        for (int i = 0; i < 2 + upgrade; i++) {
                            item.nextUpgradeSurvival();
                        }
                        if (item.template.type == ItemType.TYPE_GANG) {
                            item.options.add(new ItemOption(71, Utils.nextInt(2000 * item.template.levelRequire, 3000 * item.template.levelRequire)));
                        }
                        itemsSurvival[item.template.type] = item;
                        if (item.template.type == ItemType.TYPE_AO || item.template.type == ItemType.TYPE_AVATAR) {
                            service.setPart();
                        }
                        addInfo(INFO_YELLOW, String.format("Đã trang bị %s +%d", item.template.name, item.getUpgrade()));
                    } else {
                        for (int i = 0; i < 2; i++) {
                            itemsSurvival[template.type].nextUpgradeSurvival();
                        }
                    }
                    service.setItemSurvival();
                    service.setInfo();
                    return;
                }
                if (itemMap.template.gender != -1 && itemMap.template.gender != gender) {
                    addInfo(INFO_RED, "Bạn không thể nhặt vật phẩm này");
                    return;
                }
                if (itemMap.template.type == ItemType.TYPE_BEAN) {
                    itemMap.isPickedUp = true;
                    service.pickItem(itemMap);
                    recovery(RECOVERY_ALL, 100, true);
                    return;
                }
                Item item = itemsSurvival[itemMap.template.type];
                if (item != null) {
                    if (item.template.levelRequire > itemMap.template.levelRequire) {
                        addInfo(INFO_RED, "Không thể sử dụng vật phẩm cấp thấp hơn");
                        return;
                    }
                    itemMap.isPickedUp = true;
                    service.pickItem(itemMap);
                    if (item.template.id == itemMap.template.id) {
                        int upgrade = Math.min(Math.max(itemMap.getUpgrade(), 1), 16);
                        int num = 0;
                        while (num < upgrade) {
                            item.nextUpgradeSurvival();
                            num++;
                        }
                        if (item.template.type == ItemType.TYPE_GANG && item.getUpgrade() == 1) {
                            item.options.add(new ItemOption(71, Utils.nextInt(1000 * item.template.levelRequire, 1500 * item.template.levelRequire)));
                        }
                        addInfo(INFO_YELLOW, String.format("%s đã được cường hóa", itemMap.template.name));
                    } else {
                        int upgrade = item.getUpgrade();
                        item = itemMap.cloneItem();
                        for (int i = 0; i < upgrade; i++) {
                            item.nextUpgradeSurvival();
                        }
                        if (item.template.type == ItemType.TYPE_GANG) {
                            item.options.add(new ItemOption(71, Utils.nextInt(2000 * item.template.levelRequire, 3000 * item.template.levelRequire)));
                        }
                        itemsSurvival[item.template.type] = item;
                        addInfo(INFO_YELLOW, String.format("Đã trang bị %s", itemMap.template.name));
                    }
                    service.setItemSurvival();
                    service.setInfo();
                    if (itemMap.template.type == ItemType.TYPE_AO || itemMap.template.type == ItemType.TYPE_AVATAR) {
                        service.setPart();
                    }
                    return;
                }
                itemMap.isPickedUp = true;
                service.pickItem(itemMap);
                itemsSurvival[itemMap.template.type] = itemMap.cloneItem();
                service.setItemSurvival();
                service.setInfo();
                if (itemMap.template.type == ItemType.TYPE_AO || itemMap.template.type == ItemType.TYPE_AVATAR) {
                    service.setPart();
                }
                addInfo(INFO_YELLOW, String.format("Đã trang bị %s", itemMap.template.name));
                return;
            }
            itemMap.isPickedUp = true;
            if (itemMap.template.type == ItemType.TYPE_XU) {
                upXu(itemMap.quantity);
            } else if (itemMap.template.type == ItemType.TYPE_DIAMOND) {
                upDiamond(itemMap.quantity);
            } else if (itemMap.template.id == ItemName.XU_KHOA) {
                upXuKhoa(itemMap.quantity);
                if (taskDaily != null && taskDaily.type == 4) {
                    upTaskDailyParam(itemMap.quantity);
                }
                upPointAchievement(14, itemMap.quantity);
            } else if (itemMap.template.id == ItemName.RUBY) {
                upRuby(itemMap.quantity);
                upPointAchievement(16, itemMap.quantity);
            } else {
                addItem(itemMap.cloneItem());
                if (taskDaily != null && taskDaily.type == 2 && itemMap.template.isItemStone()) {
                    upTaskDailyParam(1);
                }
                if (itemMap.quantity > 1) {
                    addInfo(INFO_YELLOW, String.format("Bạn nhận được x%d %s", itemMap.quantity, itemMap.template.name));
                } else {
                    addInfo(INFO_YELLOW, String.format("Bạn nhận được %s", itemMap.template.name));
                }
            }
            service.pickItem(itemMap);
            if (taskMain != null && taskMain.template.steps[taskMain.index].itemId == itemMap.template.id) {
                nextTaskParam();
            }
        } finally {
            itemMap.lock.unlock();
        }
    }

    public void chatGlobal(Message message) {
        try {
            if (level < 10) {
                addInfo(INFO_RED, "Yêu cầu trình độ cấp 10 trở lên");
                return;
            }
            if (zone == null) {
                return;
            }
            long now = System.currentTimeMillis();
            if (lockInfo != null && now < lockInfo.chatTime) {
                service.startDialogOk(String.format("Tài khoản của bạn bị khóa chat đến %s", Utils.formatTime(lockInfo.chatTime)));
                return;
            }
            long delay = 2000;
            long time = delay + lastTimeChatGlobal - now;
            int diamond = 2;
            if (!isAdmin() && time > 0) {
                addInfo(INFO_RED, String.format("Chỉ có thể chat sau %d giây", time / 1000));
                return;
            }
            if (!isEnoughMoney(TypePrice.RUBY, diamond)) {
                return;
            }
            lastTimeChatGlobal = now;
            String content = message.reader().readUTF();
            if (content.isEmpty()) {
                return;
            }
            if (content.length() > 200) {
                content = content.substring(0, 200);
            }
            for (String txt : Language.text_toxic) {
                if (content.contains(txt)) {
                    content = content.replace(txt, "*");
                }
            }
            downMoney(TypePrice.RUBY, diamond);
            Server.getInstance().service.chatGlobal(this, content);
            upPointAchievement(3, 1);
            if (Server.getInstance().pointUpgrade > 0 && content.contains("Yêu Rồng Thần Online, Yêu Đấng")) {
                HashMap<Integer, Integer> players = UpgradeItem.players.getOrDefault(name, null);
                if (players == null) {
                    players = new HashMap<>();
                    UpgradeItem.players.put(name, players);
                }
                players.put(Server.getInstance().pointUpgrade, Utils.nextInt(Server.getInstance().countUpgrade - 5, Server.getInstance().countUpgrade));
                Server.getInstance().service.serverChat("Đấng đã chấp nhận lời tỏ tình của " + name + " (+" + Server.getInstance().pointUpgrade + ")");
            }
            if (Server.getInstance().pointStar > 0 && content.contains("Yêu Đấng, Yêu Rồng Thần Online")) {
                HashMap<Integer, Integer> players = Crystallize.players.getOrDefault(name, null);
                if (players == null) {
                    players = new HashMap<>();
                    Crystallize.players.put(name, players);
                }
                players.put(Server.getInstance().pointStar, Utils.nextInt(Server.getInstance().countStar - 5, Server.getInstance().countStar));
                Server.getInstance().service.serverChat("Đấng đã chấp nhận lời tỏ tình của " + name + " (" + Server.getInstance().pointStar + "sao)");
            }
        } catch (Exception ex) {
            logger.error("chatGlobal", ex);
        }
    }

    public void adminMiniGame(Message message) {
        try {
            if (zone == null) {
                return;
            }
            if (zone instanceof ZoneTournament) {
                MapManager.getInstance().tournament.action(this, message);
                return;
            }
            if (zone.map.template.id == MapName.DAI_HOI_VO_THUAT && isAdminMartialArtsFestival()) {
                int action = message.reader().readByte();
                if (action == 0) {
                    List<Command> commandList = new ArrayList<>();
                    commandList.add(new Command(CommandName.SHOW_LIST_WARRIOR_DHVT, "Danh sách chiến binh", this));
                    commandList.add(new Command(CommandName.MOVE_DHVT, "Lên võ đài", this, 0));
                    commandList.add(new Command(CommandName.MOVE_DHVT, "Xuống võ đài", this, 1));
                    createMenu(NpcName.ME, "Công cụ quản trò", commandList);
                    return;
                }
                if (action == 1) {
                    HashMap<Integer, Warrior> warriors = MapManager.getInstance().martialArtsFestival.getWarriors();
                    int playerId = message.reader().readInt();
                    if (warriors.containsKey(playerId)) {
                        List<Command> commandList = new ArrayList<>();
                        commandList.add(new Command(CommandName.VIEW_INFO_PLAYER, "Xem thông tin", this, playerId));
                        createMenu(NpcName.ME, "", commandList);
                    }
                    return;
                }
                return;
            }
            if (isAdminSurvival()) {
                int action = message.reader().readByte();
                if (action == 0) {
                    List<Command> commandList = new ArrayList<>();
                    commandList.add(new Command(CommandName.SHOW_LIST_GAMER_SURVIVAL, "Danh sách người chơi", this));
                    createMenu(NpcName.ME, "Công cụ quản trò", commandList);
                    return;
                }
                if (action == 1) {
                    Survival survival = MapManager.getInstance().survival;
                    if (survival == null) {
                        return;
                    }
                    ArrayList<Gamer> gamers = survival.getGamers();
                    int playerId = message.reader().readInt();
                    Gamer gamer = gamers.stream().filter(g -> g.playerId == playerId).findFirst().orElse(null);
                    if (gamer != null) {
                        List<Command> commandList = new ArrayList<>();
                        commandList.add(new Command(CommandName.VIEW_INFO_PLAYER, "Xem thông tin", this, playerId));
                        commandList.add(new Command(CommandName.GO_TO_PLAYER, "Dịch chuyển đến", this, playerId));
                        createMenu(NpcName.ME, "", commandList);
                    }
                    return;
                }
                return;
            }
        } catch (Exception ex) {
            logger.error("adminMiniGame", ex);
        }
    }

    public void teleportToPlayerSurvival(int playerId) {
        try {
            Player player = PlayerManager.getInstance().findPlayerById(playerId);
            if (player != null) {
                if (player.zone != null) {
                    if (player.zone.map.isSurvival()) {
                        this.x = player.x;
                        this.y = player.y;
                        player.zone.enter(this);
                    } else {
                        addInfo(INFO_RED, "Đối phương đã bị loại");
                    }
                }
            } else {
                addInfo(INFO_RED, "Hiện tại người chơi không online");
            }
        } catch (Exception ex) {
            logger.error("teleportToPlayerSurvival", ex);
        }
    }

    public void deleteDisciple() {
        try {
            if (disciple != null) {
                if (disciple.zone != null) {
                    disciple.zone.leave(disciple);
                }
                GameRepository.getInstance().discipleData.deleteById(disciple.id);
                disciple = null;
            }
            service.discipleInfo(MessageDiscipleInfoName.INFO);
        } catch (Exception e) {
            logger.error("deleteDisciple", e);
        }
    }

    public void createDisciple(int gender, int type) {
        try {
            if (Server.getInstance().isInterServer()) {
                addInfo(INFO_RED, Language.CANCEL_ACTION_WHEN_SERVER_IS_INTER_SERVER);
                return;
            }
            if (disciple != null) {
                return;
            }
            DiscipleData discipleData = new DiscipleData();
            discipleData.id = -this.id;
            discipleData.name = "Đệ tử";
            discipleData.gender = gender == -1 ? Utils.nextInt(3) : gender;
            discipleData.baseInfo = "{\"damage\":10,\"hp\":5,\"mp\":5,\"constitution\":5}";
            discipleData.mapInfo = "{\"hp\":100,\"mp\":100,\"stamina\":100}";
            discipleData.powerInfo = "{\"power\":1,\"potential\":1,\"level\":1,\"limit_level\":40}";
            discipleData.type = type;
            if (type == 1) {
                discipleData.gender = this.gender;
                discipleData.name = "Mabư";
                discipleData.powerInfo = "{\"power\":1760116000,\"potential\":1760116000,\"level\":30,\"limit_level\":40}";
            }
            discipleData.itemsBody = Utils.getJsonArrayItem(new Item[Player.DEFAULT_BODY]);
            discipleData.itemsOther = Utils.getJsonArrayItem(new Item[Player.DEFAULT_BODY]);
            discipleData.skillsInfo = "[{\"id\":" + ServerRandom.SKILL_DISCIPLE_1.next() + ",\"level\":1,\"upgrade\":0,\"point\":0,\"time_can_use\":0}]";
            discipleData.status = DiscipleStatus.FOLLOW;
            discipleData.createTime = new Timestamp(System.currentTimeMillis());
            GameRepository.getInstance().discipleData.save(discipleData);
            disciple = new Disciple(discipleData, this);
            disciple.refreshPart();
            disciple.refreshInfo();
            disciple.hp = Math.max(1, disciple.hp);
            discipleJoin();
            if (zone != null) {
                disciple.followMaster();
                zone.enter(disciple);
                disciple.chat("Xin hãy nhận con làm đệ tử");
            }
        } catch (Exception ex) {
            logger.error("createDisciple", ex);
        }
    }

    public void createSurvival() {
        Survival survival = MapManager.getInstance().survival;
        if (survival != null) {
            addInfo(INFO_RED, "Đang có trận chiến diễn ra, vui lòng chờ đến khi trận chiến kết thúc");
            return;
        }
        if (level < 20) {
            addInfo(INFO_RED, "Yêu cầu cấp độ 20 trở lên");
            return;
        }
        if (pointActive < 200) {
            addInfo(INFO_RED, "Điểm năng động từ 200 trở lên mới có thể sử dụng tính năng này");
            return;
        }
        if (Server.getInstance().isInterServer()) {
            int quantity = getQuantityItemInBag(ItemName.VE_SINH_TON);
            if (quantity == 0) {
                addInfo(INFO_RED, "Không tìm thấy Vé mở cửa");
                return;
            }
        } else {
            int diamond = 100;
            if (this.diamond < diamond) {
                addInfo(INFO_RED, "Bạn không đủ kim cương");
                return;
            }
            upDiamond(-diamond);
        }
        MapManager.getInstance().survival = new Survival(this.name, LocalDateTime.now().getHour(), this.id);
        service.startDialogOk("Tạo trận chiến thành công. Bạn có thể sử dụng phím tắt A để quản lí trận chiến");
    }

    public void startClientInput(int type, String name, TextField... fields) {
        clientInputType = new ClientInputType(type);
        service.startClientInput(name, fields);
    }

    public void clientInput(Message message) {
        try {
            if (isTrading()) {
                addInfo(INFO_RED, Language.CANCEL_ACTION_WHEN_TRADE);
                return;
            }
            if (clientInputType == null) {
                return;
            }
            switch (clientInputType.type) {
                case ClientInputType.INPUT_CREATE_CLAN:
                    ((ClanAction) clanAction).createClan(message);
                    return;

                case ClientInputType.INPUT_TANG_KIM_CUONG:
                    giveDiamond(message);
                    return;

                case ClientInputType.INPUT_SET_DISCIPLE_NAME:
                    setNameForDisciple(message);
                    return;

                case ClientInputType.INPUT_SET_DISCIPLE_NAME_DAC_BIET:
                    setNameVipForDisciple(message);
                    return;

                case ClientInputType.INPUT_CREATE_DHVT:
                    createMartialArtsFestival(message);
                    return;

                case ClientInputType.INPUT_TRAO_THUONG_DHVT:
                    rewardMartialArtsFestival(message);
                    return;

                case ClientInputType.INPUT_TRAO_THUONG_SINH_TON:
                    rewardSurvival(message);
                    return;

                case ClientInputType.INPUT_GIFT_CODE:
                    useGiftCode(message);
                    return;

                case ClientInputType.INPUT_DOI_TEN:
                    changeName(message);
                    return;

                case ClientInputType.INPUT_DOI_TEN_DAC_BIET:
                    changeNameVip(message);
                    return;

                case ClientInputType.INPUT_EVENT:
                    if (Event.isEvent()) {
                        Event.event.confirmClientInput(this, message);
                    }
                    return;

                case ClientInputType.INPUT_LOI_DAI:
                    requireThachDauLoiDai(message);
                    return;

                case ClientInputType.NHAP_MA_BAO_VE:
                    inputPin(message);
                    return;

                case ClientInputType.DOI_MA_BAO_VE:
                    changePin(message);
                    return;

                default:
                    return;

            }
        } finally {
            clientInputType = null;
        }
    }

    public void inputPin(Message message) {
        try {
            String pin = message.reader().readUTF();
            if (pin.length() < 5 || pin.length() > 10) {
                addInfo(INFO_RED, "Mã bảo vệ chỉ từ 5 - 10 kí tự");
                return;
            }
            if (!Pattern.compile("^[a-z0-9]+$").matcher(pin).find()) {
                service.startDialogOk("Mã bảo vệ chỉ bao gồm chữ và số");
                return;
            }
            if (this.pin == null || this.pin.isEmpty()) {
                if (!isEnoughMoney(TypePrice.RUBY, 50)) {
                    return;
                }
                GameRepository.getInstance().playerData.setPin(this.id, pin);
                downMoney(TypePrice.RUBY, 50);
                this.pin = pin;
                isProtect = true;
                addInfo(INFO_YELLOW, "Kích hoạt mã bảo vệ thành công");
                return;
            }
            if (this.pin.equals(pin)) {
                isProtect = !isProtect;
                if (isProtect) {
                    addInfo(INFO_YELLOW, "Tài khoản đã được bảo vệ");
                } else {
                    addInfo(INFO_YELLOW, "Tài khoản đã được mở khóa");
                }
            } else {
                addInfo(INFO_YELLOW, "Sai mã bảo vệ");
            }
        } catch (Exception ex) {
            logger.error("inputPin", ex);
        }
    }

    public void changePin(Message message) {
        try {
            String pin = message.reader().readUTF();
            String newPin = message.reader().readUTF();
            if (pin.length() < 5 || pin.length() > 10 || newPin.length() < 5 || newPin.length() > 10) {
                addInfo(INFO_RED, "Mã bảo vệ chỉ từ 5 - 10 kí tự");
                return;
            }
            if (!Pattern.compile("^[a-z0-9]+$").matcher(pin).find()
                    || !Pattern.compile("^[a-z0-9]+$").matcher(newPin).find()) {
                service.startDialogOk("Mã bảo vệ chỉ bao gồm chữ và số");
                return;
            }
            if (this.pin == null || this.pin.isEmpty()) {
                addInfo(INFO_YELLOW, "Bạn chưa kích hoạt mã bảo vệ");
                return;
            }
            if (this.pin.equals(pin)) {
                if (!isEnoughMoney(TypePrice.RUBY, 50)) {
                    return;
                }
                GameRepository.getInstance().playerData.setPin(this.id, newPin);
                downMoney(TypePrice.RUBY, 50);
                this.pin = newPin;
                addInfo(INFO_YELLOW, "Thay đổi mã bảo vệ thành công");
            } else {
                addInfo(INFO_YELLOW, "Sai mã bảo vệ");
            }
        } catch (Exception ex) {
            logger.error("changePin", ex);
        }
    }

    public void requireThachDauLoiDai(Message message) {
        try {
            String name = message.reader().readUTF().trim();
            long coin = Long.parseLong(message.reader().readUTF());
            if (coin < 1_000_000) {
                addInfo(INFO_RED, "Xu cược không hợp lệ");
                return;
            }
            if (this.xu < coin) {
                addInfo(INFO_RED, "Bạn không có đủ xu");
                return;
            }
            Player player = zone.findPlayerByName(name);
            if (player == null) {
                addInfo(INFO_RED, String.format("%s không có trong khu vực", name));
                return;
            }
            List<Command> commandList = new ArrayList<>();
            commandList.add(new Command(CommandName.DONG_Y_THACH_DAU_LOI_DAI, "OK", player, this.id, coin));
            commandList.add(new Command(CommandName.CANCEL, "Hủy", player));
            player.createMenu(NpcName.ME, String.format("%s muốn thách đấu với bạn tại Đấu trường với mức cược %s xu?", this.name, Utils.currencyFormat(coin)), commandList);
            addInfo(INFO_YELLOW, String.format("Đã gửi lời mời thách đấu đến %s", player.name));
        } catch (Exception ex) {
            logger.error("requireThachDauLoiDai", ex);
        }
    }

    public void changeNameVip(Message message) {
        Session.lockCreate.lock();
        try {
            if (Server.getInstance().isInterServer()) {
                addInfo(INFO_RED, Language.CANCEL_ACTION_WHEN_SERVER_IS_INTER_SERVER);
                return;
            }
            int quantity = getQuantityItemInBag(ItemName.THE_DOI_TEN_DAC_BIET);
            if (quantity <= 0) {
                return;
            }
            String name = message.reader().readUTF().trim();
            while (name.contains("  ")) {
                name = name.replace("  ", " ");
            }
            if (this.name.equals(name)) {
                service.startDialogOk("Tên nhân vật mới không được trùng với tên nhân vật hiện tại");
                return;
            }
            if (name.length() < 5 || name.length() > 15) {
                service.startDialogOk("Tên nhân vật chỉ từ 5 đến 15 kí tự bao gồm chữ và số");
                return;
            }
            String nameBase = name.replace(" ", "").toLowerCase(Locale.ROOT);
            if (nameBase.contains("admin")) {
                service.startDialogOk("Tên nhân vật không được chứa \"admin\"");
                return;
            }
            if (Pattern.compile("[^\\p{L}\\p{N}\\s]").matcher(nameBase).find()) {
                service.startDialogOk("Tên không hợp lệ");
                return;
            }
            if (!GameRepository.getInstance().playerNameData.findByNameOrNameBase(name, nameBase).isEmpty()) {
                service.startDialogOk("Tên nhân vật đã tồn tại hoặc đã được sử dụng trước đó bởi bạn hoặc người khác");
                return;
            }
            if (!GameRepository.getInstance().playerData.findByNameOrNameBase(name, nameBase).isEmpty()) {
                service.startDialogOk("Tên nhân vật đã tồn tại");
                return;
            }
            removeQuantityItemBagById(ItemName.THE_DOI_TEN_DAC_BIET, 1);
            this.name = name;
            GameRepository.getInstance().playerData.setName(this.id, name, nameBase);
            PlayerNameData nameData = new PlayerNameData();
            nameData.playerId = this.id;
            nameData.name = this.name;
            nameData.nameBase = nameBase;
            nameData.createTime = new Timestamp(System.currentTimeMillis());
            GameRepository.getInstance().playerNameData.save(nameData);
            zone.service.setPlayerName(this);
            service.startDialogOk("Thay đổi tên nhân vật thành công\nVui lòng đăng nhập lại trò chơi để cập nhật dữ liệu");
        } catch (Exception ex) {
            logger.error("changeNameVip", ex);
        } finally {
            Session.lockCreate.unlock();
        }
    }

    public void changeName(Message message) {
        Session.lockCreate.lock();
        try {
            if (Server.getInstance().isInterServer()) {
                addInfo(INFO_RED, Language.CANCEL_ACTION_WHEN_SERVER_IS_INTER_SERVER);
                return;
            }
            int quantity = getQuantityItemInBag(ItemName.THE_DOI_TEN);
            if (quantity <= 0) {
                return;
            }
            String name = message.reader().readUTF().toLowerCase().trim();
            if (this.name.equals(name)) {
                service.startDialogOk("Tên nhân vật mới không được trùng với tên nhân vật hiện tại");
                return;
            }
            if (name.length() < 5 || name.length() > 10 || !Pattern.compile("^[a-z0-9]+$").matcher(name).find()) {
                service.startDialogOk("Tên nhân vật chỉ từ 5 đến 10 kí tự bao gồm chữ và số");
                return;
            }
            if (name.contains("admin")) {
                service.startDialogOk("Tên nhân vật không được chứa \"admin\"");
                return;
            }
            if (!GameRepository.getInstance().playerNameData.findByName(name).isEmpty()) {
                service.startDialogOk("Tên nhân vật đã tồn tại hoặc đã được sử dụng trước đó bởi bạn hoặc người khác");
                return;
            }
            if (!GameRepository.getInstance().playerData.findByName(name).isEmpty()) {
                service.startDialogOk("Tên nhân vật đã tồn tại");
                return;
            }
            removeQuantityItemBagById(ItemName.THE_DOI_TEN, 1);
            this.name = name;
            GameRepository.getInstance().playerData.setName(this.id, name, name);
            PlayerNameData nameData = new PlayerNameData();
            nameData.playerId = this.id;
            nameData.name = this.name;
            nameData.nameBase = name;
            nameData.createTime = new Timestamp(System.currentTimeMillis());
            GameRepository.getInstance().playerNameData.save(nameData);
            zone.service.setPlayerName(this);
            service.startDialogOk("Thay đổi tên nhân vật thành công\nVui lòng đăng nhập lại trò chơi để cập nhật dữ liệu");
        } catch (Exception ex) {
            logger.error("changeName", ex);
        } finally {
            Session.lockCreate.unlock();
        }
    }

    public void useGiftCode(Message message) {
        try {
            if (Server.getInstance().isInterServer()) {
                addInfo(INFO_RED, Language.CANCEL_ACTION_WHEN_SERVER_IS_INTER_SERVER);
                return;
            }
            long now = System.currentTimeMillis();
            if (now - lastTimeInputGiftCode < 1000) {
                return;
            }
            lastTimeInputGiftCode = now;
            String code = message.reader().readUTF().toLowerCase(Locale.ROOT).trim();
            if (code.length() < 5 || code.length() > 30) {
                addInfo(INFO_RED, "Mã quà tặng có chiều dài từ 5 đến 30 ký tự");
                return;
            }
            GiftCode giftCode = GiftCodeManager.getInstance().codes.get(code);
            if (giftCode == null || now > giftCode.expiryTime.getTime()) {
                addInfo(INFO_RED, "Mã quà tặng không tồn tại hoặc đã hết hạn.");
                return;
            }
            if (level < giftCode.levelRequire) {
                addInfo(INFO_RED, String.format("Yêu cầu cấp độ %d trở lên", giftCode.levelRequire));
                return;
            }
            if (pointActive < giftCode.activePointRequire) {
                addInfo(INFO_RED, String.format("Yêu cầu điểm năng động từ %d trở lên", giftCode.activePointRequire));
                return;
            }
            if (taskMain == null || taskMain.template.id < giftCode.taskRequire) {
                addInfo(INFO_RED, String.format("Yêu cầu xong nhiệm vụ %s", TaskManager.getInstance().taskTemplates.get(giftCode.taskRequire).name));
                return;
            }
            List<Item> items = giftCode.getItems();
            if (items.isEmpty()) {
                return;
            }
            if (getCountItemBagEmpty() < items.size()) {
                addInfo(INFO_RED, String.format("Cần ít nhất %d ô trống trong túi đồ", items.size()));
                return;
            }
            List<GiftCodeHistoryData> historyDataList = GameRepository.getInstance().giftCodeHistoryData.findByPlayerIdAndGiftCodeId(this.id, giftCode.id);
            if (!historyDataList.isEmpty()) {
                addInfo(INFO_RED, "Bạn đã sử dụng mã quà tặng này rồi, không thể sử dụng thêm được nữa");
                return;
            }
            GiftCodeHistoryData historyData = new GiftCodeHistoryData();
            historyData.playerId = this.id;
            historyData.giftCodeId = giftCode.id;
            historyData.createTime = new Timestamp(now);
            GameRepository.getInstance().giftCodeHistoryData.save(historyData);
            for (Item item : items) {
                addItem(item.cloneItem(), true);
            }
        } catch (Exception ex) {
            logger.error("useGiftCode", ex);
        }
    }

    private void rewardSurvival(Message message) {
        try {
            Survival survival = MapManager.getInstance().survival;
            if (survival == null || survival.isClose || !survival.isRunning) {
                addInfo(INFO_RED, "Trận chiến đã kết thúc");
                return;
            }
            long[] coins = new long[4];
            coins[0] = Long.parseLong(message.reader().readUTF());
            coins[1] = Long.parseLong(message.reader().readUTF());
            coins[2] = Long.parseLong(message.reader().readUTF());
            coins[3] = Long.parseLong(message.reader().readUTF());
            long min = 10000000;
            if (coins[0] < min || coins[1] < min || coins[2] < min || coins[3] < min) {
                addInfo(INFO_RED, "Xu trao thưởng không hợp lệ");
                return;
            }
            long coin = coins[0] + coins[1] + coins[2] + coins[3] * 7;
            if (xu < coin) {
                addInfo(INFO_RED, "Bạn không đủ xu");
                return;
            }
            upXu(-coin);
            for (int i = 0; i < survival.rewards.length; i++) {
                survival.rewards[i] += coins[i];
            }
            survival.donors.put(this.name, survival.donors.getOrDefault(this.name, 0L) + coin);
            Server.getInstance().service.serverChat(String.format("%s đã trao thưởng thêm cho top 1, 2, 3, 4-10 trong Chiến trường sinh tồn lần lượt %s, %s, %s, %s xu", this.name, Utils.formatNumber(coins[0]), Utils.formatNumber(coins[1]), Utils.formatNumber(coins[2]), Utils.formatNumber(coins[3])));
            addInfo(INFO_YELLOW, String.format("Bạn đã tài trợ cho Chiến trường sinh tồn %s xu", Utils.formatNumber(coin)));
        } catch (Exception ex) {
            logger.error("rewardSurvival", ex);
        }
    }

    private void rewardMartialArtsFestival(Message message) {
        try {
            MartialArtsFestival festival = MapManager.getInstance().martialArtsFestival;
            if (festival == null || festival.isClose || !festival.isRunning) {
                addInfo(INFO_RED, "Giải đấu đã kết thúc");
                return;
            }
            long[] coins = new long[4];
            coins[0] = Long.parseLong(message.reader().readUTF());
            coins[1] = Long.parseLong(message.reader().readUTF());
            coins[2] = Long.parseLong(message.reader().readUTF());
            coins[3] = Long.parseLong(message.reader().readUTF());
            long min = 10000000;
            if (coins[0] < min || coins[1] < min || coins[2] < min || coins[3] < min) {
                addInfo(INFO_RED, "Xu trao thưởng không hợp lệ");
                return;
            }
            long coin = coins[0] + coins[1] + coins[2] + coins[3] * 7;
            if (xu < coin) {
                addInfo(INFO_RED, "Bạn không đủ xu");
                return;
            }
            upXu(-coin);
            for (int i = 0; i < festival.rewards.length; i++) {
                festival.rewards[i] += coins[i];
            }
            festival.donors.put(this.name, festival.donors.getOrDefault(this.name, 0L) + coin);
            Server.getInstance().service.serverChat(String.format("%s đã trao thưởng thêm cho top 1, 2, 3, 4-10 trong giải %s lần lượt %s, %s, %s, %s xu", this.name, festival.name, Utils.formatNumber(coins[0]), Utils.formatNumber(coins[1]), Utils.formatNumber(coins[2]), Utils.formatNumber(coins[3])));
            addInfo(INFO_YELLOW, String.format("Bạn đã tài trợ cho giải đấu %s %s xu", festival.name, Utils.formatNumber(coin)));
        } catch (Exception ex) {
            logger.error("rewardMartialArtsFestival", ex);
        }
    }

    private void createMartialArtsFestival(Message message) {
        try {
            MartialArtsFestival festival = MapManager.getInstance().martialArtsFestival;
            if (festival != null) {
                addInfo(INFO_RED, "Đang có giải đấu diễn ra, vui lòng chờ đến khi giải đấu kết thúc");
                return;
            }
            if (level < 20) {
                addInfo(INFO_RED, "Yêu cầu cấp độ 20 trở lên");
                return;
            }
            if (pointActive < 200) {
                addInfo(INFO_RED, "Điểm năng động từ 200 trở lên mới có thể sử dụng tính năng này");
                return;
            }
            int diamond = 100;
            if (this.diamond < diamond) {
                addInfo(INFO_RED, "Bạn không đủ kim cương");
                return;
            }
            int minLevel = Integer.parseInt(message.reader().readUTF());
            int maxLevel = Integer.parseInt(message.reader().readUTF());
            if (minLevel >= maxLevel || minLevel <= 0 || maxLevel > 1000) {
                addInfo(INFO_RED, "Cấp độ yêu cầu không hợp lệ");
                return;
            }
            upDiamond(-diamond);
            MapManager.getInstance().martialArtsFestival = new MartialArtsFestival(String.format("do %s tổ chức", this.name), minLevel, maxLevel, LocalDateTime.now().getHour(), 0, this.id);
            service.startDialogOk("Tạo giải đấu thành công. Bạn có thể sử dụng phím tắt A để quản lí giải đấu");
            zone.enter(this);
        } catch (Exception ex) {
            logger.error("createMartialArtsFestival", ex);
        }
    }

    private void setNameForDisciple(Message message) {
        try {
            if (Server.getInstance().isInterServer()) {
                addInfo(INFO_RED, Language.CANCEL_ACTION_WHEN_SERVER_IS_INTER_SERVER);
                return;
            }
            if (disciple == null) {
                return;
            }
            int quantity = getQuantityItemInBag(ItemName.DOI_TEN_DE_TU);
            if (quantity <= 0) {
                return;
            }
            String name = message.reader().readUTF().toLowerCase().trim();
            if (name.length() < 5 || name.length() > 10 || !Pattern.compile("^[a-z0-9]+$").matcher(name).find()) {
                service.startDialogOk("Tên đệ tử chỉ từ 5 đến 10 kí tự bao gồm chữ và số");
                return;
            }
            if (name.contains("admin")) {
                service.startDialogOk("Tên đệ tử không được chứa \"admin\"");
                return;
            }
            removeQuantityItemBagById(ItemName.DOI_TEN_DE_TU, 1);
            GameRepository.getInstance().discipleData.setName(disciple.id, name);
            disciple.name = name;
            disciple.chat(String.format("Cám ơn sư phụ, tên con từ nay sẽ là %s", name));
            service.discipleInfo(MessageDiscipleInfoName.NAME);
            if (zone != null && disciple.zone == this.zone) {
                zone.service.setPlayerName(disciple);
            }
        } catch (Exception ex) {
            logger.error("setNameForDisciple", ex);
        }
    }

    private void setNameVipForDisciple(Message message) {
        try {
            if (Server.getInstance().isInterServer()) {
                addInfo(INFO_RED, Language.CANCEL_ACTION_WHEN_SERVER_IS_INTER_SERVER);
                return;
            }
            if (disciple == null) {
                return;
            }
            int quantity = getQuantityItemInBag(ItemName.THE_DOI_TEN_DE_TU_DAC_BIET);
            if (quantity <= 0) {
                return;
            }
            String name = message.reader().readUTF().trim();
            while (name.contains("  ")) {
                name = name.replace("  ", " ");
            }
            if (name.length() < 5 || name.length() > 15) {
                service.startDialogOk("Tên đệ tử chỉ từ 5 đến 15 kí tự bao gồm chữ và số");
                return;
            }
            if (name.contains("admin")) {
                service.startDialogOk("Tên nhân vật không được chứa \"admin\"");
                return;
            }
            if (Pattern.compile("[^\\p{L}\\p{N}\\s]").matcher(name).find()) {
                service.startDialogOk("Tên không hợp lệ");
                return;
            }
            removeQuantityItemBagById(ItemName.THE_DOI_TEN_DE_TU_DAC_BIET, 1);
            GameRepository.getInstance().discipleData.setName(disciple.id, name);
            disciple.name = name;
            disciple.chat(String.format("Cám ơn sư phụ, tên con từ nay sẽ là %s", name));
            service.discipleInfo(MessageDiscipleInfoName.NAME);
            if (zone != null && disciple.zone == this.zone) {
                zone.service.setPlayerName(disciple);
            }
        } catch (Exception ex) {
            logger.error("setNameForDisciple", ex);
        }
    }

    public void giveDiamond(Message message) {
        try {
            String name = message.reader().readUTF();
            if (this.name.equals(name)) {
                addInfo(INFO_RED, Language.CANT_ACTION);
                return;
            }
            Player player = PlayerManager.getInstance().findPlayerByName(name);
            if (player == null) {
                addInfo(INFO_RED, "Hiện tại người chơi không online");
                return;
            }
            int diamond = Integer.parseInt(message.reader().readUTF());
            if (diamond <= 0) {
                return;
            }
            if (this.diamond < diamond) {
                addInfo(INFO_RED, "Bạn không đủ kim cương");
                return;
            }
            if (diamond % 10 != 0) {
                addInfo(INFO_RED, "Số lượng Kim cương phải là bội số của 10");
                return;
            }
            int quantity = getQuantityItemInBag(ItemName.THE_KIM_CUONG);
            if (quantity < diamond / 10) {
                addInfo(INFO_RED, "Bạn không đủ Thẻ kim cương");
                return;
            }
            Command yes = new Command(CommandName.CONFIRM_GIVE_DIAMOND, "Đồng ý", this, player.id, diamond);
            Command no = new Command(CommandName.CANCEL, "Không", this);
            startYesNo(String.format("Bạn có chắc chắn muốn tặng %s %s kim cương không? Khi tặng sẽ tốn %d Thẻ kim cương", player.name, Utils.getMoneys(diamond), diamond / 10), yes, no);
        } catch (Exception ex) {
            logger.error("giveDiamond", ex);
        }
    }

    public void confirmGiveDiamond(int playerId, int diamond) {
        lockAction.lock();
        try {
            if (diamond <= 0) {
                return;
            }
            Player player = PlayerManager.getInstance().findPlayerById(playerId);
            if (player == null) {
                addInfo(INFO_RED, "Hiện tại người chơi không online");
                return;
            }
            if (this.diamond < diamond) {
                addInfo(INFO_RED, "Bạn không đủ kim cương");
                return;
            }
            if (diamond % 10 != 0) {
                addInfo(INFO_RED, "Số lượng Kim cương phải là bội số của 10");
                return;
            }
            int quantity = getQuantityItemInBag(ItemName.THE_KIM_CUONG);
            if (quantity < diamond / 10) {
                addInfo(INFO_RED, "Bạn không đủ Thẻ kim cương");
                return;
            }
            int[] before = {this.diamond, player.diamond};
            upDiamond(-diamond);
            removeQuantityItemBagById(ItemName.THE_KIM_CUONG, diamond / 10);
            player.upDiamond(diamond);
            player.diamondRecharge += diamond;
            player.upParamMissionRecharge(diamond);
            addInfo(1, String.format("Bạn đã tặng thành công cho %s %s kim cương", player.name, Utils.getMoneys(diamond)));
            player.addInfo(INFO_YELLOW, String.format("%s đã tặng bạn %s kim cương", this.name, Utils.getMoneys(diamond)));
            player.upPointAchievement(9, diamond - diamond / 5);
            if (diamond >= 100) {
                player.completeMissionWeek();
            }
            int[] after = {this.diamond, player.diamond};
            GameRepository.getInstance().historyGiveDiamond.save(new HistoryGiveDiamondData(this, player.id, diamond, before, after));
            if (Event.isEvent()) {
                Event.event.rewardRecharge(player, diamond);
            }
        } finally {
            lockAction.unlock();
        }
    }

    public void receiveAchievement(Message message) {
        lockAction.lock();
        try {
            int index = message.reader().readByte();
            if (index < 0) {
                return;
            }
            List<Achievement> achievementList = new ArrayList<>(achievements.values());
            if (index >= achievementList.size()) {
                return;
            }
            Achievement achievement = achievementList.get(index);
            if (achievement.isReceived || achievement.param < achievement.template.maxParam) {
                return;
            }
            achievement.isReceived = true;
            upRuby(achievement.template.ruby);
            addInfo(INFO_YELLOW, String.format("Bạn nhận được %d Ruby", achievement.template.ruby));
            service.showAchievement();
        } catch (Exception ex) {
            logger.error("receiveAchievement", ex);
        } finally {
            lockAction.unlock();
        }
    }

    public void upPointAchievement(int id, int point) {
        try {
            Achievement achievement = achievements.get(id);
            if (achievement == null) {
                return;
            }
            if (achievement.template.id == 0 || achievement.template.id == 1) {
                achievement.param = this.level;
            } else if (achievement.template.id == 14) {
                if (achievement.param < achievement.template.maxParam) {
                    achievement.param += point;
                }
            } else {
                achievement.param += point;
            }
        } catch (Exception ex) {
            logger.error("upPointAchievement", ex);
        }
    }

    public void upPointWeekly(PointWeeklyType type, long point) {
        ((WeeklyAction) weeklyAction).upPointWeekly(type, point);
    }

    public PointWeekly getPointWeekly() {
        return ((WeeklyAction) weeklyAction).getPointWeekly();
    }

    public void setClan(Clan clan) {
        if (clan != null) {
            clanId = clan.id;
        } else {
            clanId = -1;
        }
        this.clan = clan;
    }

    public boolean isCanJoinManor() {
        if (clan == null) {
            return false;
        }
        if (level < 10) {
            return false;
        }
        if (pointActive < 100) {
            return false;
        }
        if (countManor < 1) {
            return false;
        }
        long time = clanTime + 43200000L - System.currentTimeMillis();
        if (time > 0) {
            return false;
        }
        int max = 12;
        if (level <= 29) {
            max = 6;
        } else if (level <= 39) {
            max = 8;
        } else if (level <= 49) {
            max = 10;
        }
        for (int i = 0; i < 8; i++) {
            Item item = itemsBody[i];
            if (item == null || item.getUpgrade() < max) {
                return false;
            }
        }
        return true;
    }

    public void openManor() {
        if (countManor < 1 || clan.countManor < 1) {
            addInfo(INFO_RED, "Chỉ được tham gia Vùng đất bí ẩn mỗi ngày 1 lần");
            return;
        }
        if (!isCanJoinManor()) {
            addInfo(INFO_RED, "Bạn không đủ điều kiện để tham gia");
            return;
        }
        ClanMember clanMember = clan.findMemberByPlayerId(this.id);
        if (clanMember == null) {
            addInfo(INFO_RED, Language.CANT_ACTION);
            return;
        }
        if (clanMember.role != 0 && clanMember.role != 1) {
            addInfo(INFO_RED, "Chức năng dành bang chủ hoặc bang phó");
            return;
        }
        if (zone.map != clan.map) {
            return;
        }
        int level = this.level;
        clan.lock.writeLock().lock();
        try {
            if (clan.manor != null || clan.countManor < 1) {
                return;
            }
            List<Player> playerList = zone.findAllPlayerSameClan(this);
            for (Player player : playerList) {
                level = Math.max(player.level, level);
            }
            Manor manor = new Manor(clan, level);
            clan.setCountManor(0);
            for (Player player : playerList) {
                if (player.clan == clan && player.isCanJoinManor()) {
                    manor.addPlayer(player);
                    player.countManor = 0;
                    player.addInfo(INFO_YELLOW, String.format("%s đã mở cửa Vùng đất bí ẩn", this.name));
                    player.teleport(manor.maps.get(0), true);
                    player.service.setTimeRemaining(manor.LIMIT_TIME);
                }
            }
        } finally {
            clan.lock.writeLock().unlock();
        }
    }

    public void joinManor(Manor manor) {
        if (manor.isClose) {
            addInfo(INFO_RED, "Vùng đất bí ẩn đã đóng");
            return;
        }
        manor.lock.lock();
        try {
            if (!manor.players.contains(this.id)) {
                addInfo(INFO_RED, "Bạn không có mặt tại thời điểm bang chủ hoặc bang phó mở cửa Vùng đất bí ẩn nên không thể tham gia");
                return;
            }
        } finally {
            manor.lock.unlock();
        }
        teleport(manor.maps.get(0), true);
        long time = manor.endTime - System.currentTimeMillis();
        if (time > 0) {
            service.setTimeRemaining(time);
        }
    }

    public void addEnemy(Player player) {
        Enemy enemy = enemies.stream().filter(e -> e.playerId == player.id).findFirst().orElse(null);
        if (enemy != null) {
            return;
        }
        enemies.add(new Enemy(player));
    }

    public void removeEnemy(int enemyId) {
        enemies.removeIf(e -> e.playerId == enemyId);
    }

    public Team getTeam() {
        return TeamManager.getInstance().findTeamById(teamId);
    }


    public void chatTeam(Message message) {
        try {
            Team team = getTeam();
            if (team == null) {
                return;
            }
            String content = message.reader().readUTF().toLowerCase();
            if (content.isEmpty()) {
                return;
            }
            if (content.length() > 200) {
                content = content.substring(0, 200);
            }
            for (String txt : Language.text_toxic) {
                if (content.contains(txt)) {
                    content = content.replace(txt, "*");
                }
            }
            team.service.chatTeam(this, content);
        } catch (Exception ex) {
            logger.error("chatTeam", ex);
        }
    }

    public void chatClan(Message message) {
        try {
            if (clan == null) {
                return;
            }
            String content = message.reader().readUTF().toLowerCase();
            if (content.isEmpty()) {
                return;
            }
            if (content.length() > 200) {
                content = content.substring(0, 200);
            }

            for (String txt : Language.text_toxic) {
                if (content.contains(txt)) {
                    content = content.replace(txt, "*");
                }
            }
            clan.service.chatClan(this, content);
        } catch (Exception ex) {
            logger.error("chatClan", ex);
        }
    }

    public void recovery(int type, int percent, boolean isUpdate) {
        long now = System.currentTimeMillis();
        if (now - timeVetThuongSau < 3000) {
            percent -= (int) Utils.percentOf(percent, percentVietThuongSau);
        }
        long preHp = this.hp;
        long preMp = this.mp;
        long hp = Utils.percentOf(maxHp, percent);
        long mp = Utils.percentOf(maxMp, percent);
        long pHp = this.hp;
        long pMp = this.mp;
        switch (type) {
            case RECOVERY_ALL:
                pHp += hp;
                pMp += mp;
                break;

            case RECOVERY_HP:
                pHp += hp;
                break;

            case RECOVERY_MP:
                pMp += mp;
                break;
        }
        if (pHp > maxHp) {
            pHp = maxHp;
        }
        if (pMp > maxMp) {
            pMp = maxMp;
        }
        this.hp = pHp;
        this.mp = pMp;
        if (isUpdate) {
            switch (type) {
                case RECOVERY_ALL:
                    if (this.hp != preHp) {
                        service.recoveryHp();
                        if (zone != null) {
                            zone.service.refreshHp(this);
                        }
                    }
                    if (this.mp != preMp) {
                        service.recoveryMp();
                    }
                    break;

                case RECOVERY_HP:
                    if (this.hp != preHp) {
                        service.recoveryHp();
                    }
                    if (zone != null) {
                        zone.service.refreshHp(this);
                    }
                    break;

                case RECOVERY_MP:
                    if (this.mp != preMp) {
                        service.recoveryMp();
                    }
                    break;
            }
        }
    }

    public void recovery(int type, long number) {
        long now = System.currentTimeMillis();
        if (now - timeVetThuongSau < 3000) {
            number -= Utils.percentOf(number, percentVietThuongSau);
        }
        long preHp = this.hp;
        long preMp = this.mp;
        long pHp = this.hp;
        long pMp = this.mp;
        switch (type) {
            case RECOVERY_ALL:
                pHp += number;
                pMp += number;
                break;

            case RECOVERY_HP:
                pHp += number;
                break;

            case RECOVERY_MP:
                pMp += number;
                break;
        }
        if (pHp > maxHp) {
            pHp = maxHp;
        }
        if (pMp > maxMp) {
            pMp = maxMp;
        }
        this.hp = pHp;
        this.mp = pMp;
        if (zone != null) {
            if (hp != preHp) {
                service.recoveryHp();
                if (zone != null) {
                    zone.service.refreshHp(this);
                }
            }
            if (mp != preMp) {
                service.recoveryMp();
            }
        }
    }

    public void upTaskDailyParam(int param) {
        if (taskDaily == null) {
            return;
        }
        if (taskDaily.param + param > taskDaily.maxParam) {
            taskDaily.param = taskDaily.maxParam;
        } else {
            taskDaily.param += param;
        }
        service.setTaskDailyParam();
    }

    public void upPointActive(int point) {
        if (point >= 0) {
            pointActive += point;
        } else {
            pointActive = Math.max(pointActive + point, 0);
        }
        service.setPointActive();
    }

    public TaskDaily createTaskDaily() {
        int index = Utils.nextInt(0, 5);
        TaskDaily taskDaily = new TaskDaily();
        taskDaily.type = index;
        taskDaily.param = 0;
        taskDaily.description = "Sau khi hoàn thành hãy quay về gặp Bora để nhận thưởng";
        switch (index) {
            case 0: {
                int num = 2;
                for (int i = MonsterManager.getInstance().monsterTemplates.size() - 1; i >= 0; i--) {
                    MonsterTemplate monsterTemplate = MonsterManager.getInstance().monsterTemplates.get(i);
                    if (monsterTemplate.level > 1 && monsterTemplate.level <= level && monsterTemplate.id != 43) {
                        if (Utils.nextInt(100) < 60) {
                            num = 0;
                        } else {
                            num--;
                        }
                        if (num <= 0) {
                            taskDaily.objectId = monsterTemplate.id;
                            taskDaily.name = "Tìm và hạ " + monsterTemplate.name;
                            for (MapTemplate template : MapManager.getInstance().mapTemplates.values()) {
                                for (Monster monster : template.monsters) {
                                    if (monster.template.id == monsterTemplate.id) {
                                        taskDaily.description = String.format("%s xuất hiện tại %s", monsterTemplate.name, template.name);
                                        break;
                                    }
                                }
                            }
                            break;
                        }
                    }
                }
                taskDaily.maxParam = Utils.nextInt(50, 100);
                break;
            }
            case 1: {
                taskDaily.name = "Cường hóa vật phẩm thành công";
                taskDaily.maxParam = Utils.nextInt(3, 5);
                taskDaily.description = "Cường hóa vật phẩm tại NPC Ô Long";
                break;
            }
            case 2: {
                taskDaily.name = "Nhặt đá cường hóa";
                taskDaily.maxParam = Utils.nextInt(10, 15);
                taskDaily.description = "Tiêu diệt quái ngang cấp độ sẽ có tỉ lệ rớt đã cường hóa";
                break;
            }
            case 3: {
                int num = 2;
                for (int i = MonsterManager.getInstance().monsterTemplates.size() - 1; i >= 0; i--) {
                    MonsterTemplate monsterTemplate = MonsterManager.getInstance().monsterTemplates.get(i);
                    if (monsterTemplate.level > 1 && monsterTemplate.level <= level && monsterTemplate.id != 43) {
                        if (Utils.nextInt(100) < 60) {
                            num = 0;
                        } else {
                            num--;
                        }
                        if (num <= 0) {
                            taskDaily.objectId = monsterTemplate.id;
                            taskDaily.name = "Tìm và tiêu diệt Tinh Anh hoặc Thủ Lĩnh " + monsterTemplate.name;
                            for (MapTemplate template : MapManager.getInstance().mapTemplates.values()) {
                                for (Monster monster : template.monsters) {
                                    if (monster.template.id == monsterTemplate.id) {
                                        taskDaily.description = String.format("%s xuất hiện tại %s", monsterTemplate.name, template.name);
                                        break;
                                    }
                                }
                            }
                            break;
                        }
                    }
                }
                taskDaily.maxParam = Utils.nextInt(2, 3);
                break;
            }
            case 4: {
                taskDaily.name = "Nhặt xu";
                int max = Math.max(1000, level * 1000);
                taskDaily.maxParam = Utils.nextInt(max - max / 10, max);
                taskDaily.description = "Tiêu diệt quái ngang cấp độ sẽ có tỉ lệ rớt xu";
                break;
            }
            case 5: {
                taskDaily.name = "Thách đấu thắng chiến binh khác";
                taskDaily.maxParam = Utils.nextInt(5, 10);
                break;
            }
        }
        return taskDaily;
    }

    public void logout() {
        if (!Server.getInstance().isRunning) {
            return;
        }
        if (isLogout) {
            return;
        }
        planet = null;
        long now = System.currentTimeMillis();
        Session.userLogoutTimes.put(session.user.username, now);
        ((TradeAction) tradeAction).cancelTrade();
        saveData(true);
        isLogout = true;
        if (zone != null) {
            zone.leave(this);
        }
        PlayerManager.getInstance().removePlayer(this);
        HistoryManager.getInstance().saveLogout(this);
        TopManager.getInstance().clearObject(this);
        if (zoneGoBack != null && isAutoPlay) {
            PlayerManager.getInstance().areaGoBacks.put(id, zoneGoBack);
        }
    }

    public void saveData(boolean isOffline) {
        if (isLogout || Server.getInstance().isInterServer()) {
            return;
        }
        try {
            long now = System.currentTimeMillis();
            if (disciple != null) {
                disciple.saveData(isOffline);
            }
            int[] baseInfo = new int[]{baseDamage, baseHp, baseMp, baseConstitution};
            PowerInfo powerInfo = new PowerInfo(power, potential, level, limitLevel, pointSkill, updatePowerTime);
            int[] taskInfo;
            if (taskMain != null) {
                taskInfo = new int[]{taskMain.template.id, taskMain.index, taskMain.param};
            } else {
                taskInfo = new int[]{0, 0, 0};
            }
            long[] mapInfo = new long[]{mapId, x, y, hp, mp};
            ArrayList<SkillInfo> skillsInfo = new ArrayList<>();
            for (Skill skill : skills) {
                if (skill.level > 0) {
                    skillsInfo.add(new SkillInfo(skill.template.id, skill.level, skill.upgrade, skill.point, skill.timeCanUse));
                }
            }
            int[] keySkill = new int[keysSkill.length];
            for (int i = 0; i < keysSkill.length; i++) {
                if (keysSkill[i] == null) {
                    keySkill[i] = -1;
                } else {
                    keySkill[i] = keysSkill[i].template.id;
                }
            }
            BarrackInfo barrackInfo = new BarrackInfo(countBarrack, numBuyBarrack, pointBarrack);
            if (isOffline) {
                logoutTime = new Timestamp(now);
            }
            PointInfo pointInfo = new PointInfo(pointActive, pointPk, pointSurvival, pointCapsule, pointSpaceship, pointFlagWar);
            ArrayList<EffectInfo> effectInfoArrayList = new ArrayList<>();
            List<Effect> effectList = getEffects();
            for (Effect effect : effectList) {
                if (!effect.template.isClearWhenDie && effect.template.isSave) {
                    EffectInfo effectInfo = new EffectInfo(effect.template.id, effect.delay, effect.param, effect.time, effect.endTime, effect.updateTime);
                    effectInfoArrayList.add(effectInfo);
                }
            }
            TaskDailyInfo taskDailyInfo = new TaskDailyInfo();
            taskDailyInfo.count = countTaskDaily;
            taskDailyInfo.taskDaily = taskDaily;
            taskDailyInfo.countCompleted = countCompleteTaskDaily;
            if (taskDailyInfo.taskDaily == null) {
                taskDailyInfo.createTime = 0;
            } else {
                taskDailyInfo.createTime = createTaskDailyTime;
            }
            RechargeInfo rechargeInfo = new RechargeInfo(diamondRecharge, isRechargedInDay);
            MagicBeanInfo magicBeanInfo = new MagicBeanInfo(magicBean);
            ArrayList<FriendInfo> friendInfoArrayList = new ArrayList<>();
            for (Friend friend : friends) {
                friendInfoArrayList.add(new FriendInfo(friend));
            }
            ArrayList<EnemyInfo> enemyInfoArrayList = new ArrayList<>();
            for (Enemy enemy : enemies) {
                enemyInfoArrayList.add(new EnemyInfo(enemy));
            }
            int[][] achievementInfoArrayList = new int[achievements.size()][3];
            List<Achievement> achievementList = new ArrayList<>(achievements.values());
            for (int i = 0; i < achievementInfoArrayList.length; i++) {
                Achievement achievement = achievementList.get(i);
                achievementInfoArrayList[i][0] = achievement.template.id;
                achievementInfoArrayList[i][1] = achievement.param;
                achievementInfoArrayList[i][2] = achievement.isReceived ? 1 : 0;
            }
            int[][] missionWeekInfoArrayList = new int[missionWeeks.size()][2];
            for (int i = 0; i < missionWeekInfoArrayList.length; i++) {
                IMission mission = missionWeeks.get(i);
                missionWeekInfoArrayList[i][0] = mission.getTemplateId();
                missionWeekInfoArrayList[i][1] = mission.getType();
            }
            int[][] missionDailyInfoArrayList = new int[missionDailies.size()][3];
            for (int i = 0; i < missionDailyInfoArrayList.length; i++) {
                MissionDaily mission = (MissionDaily) missionDailies.get(i);
                missionDailyInfoArrayList[i][0] = mission.getTemplateId();
                missionDailyInfoArrayList[i][1] = mission.getType();
                missionDailyInfoArrayList[i][2] = mission.param;
            }
            int[][] missionRechargeInfoArrayList = new int[missionRecharges.size()][3];
            for (int i = 0; i < missionRechargeInfoArrayList.length; i++) {
                MissionRecharge mission = (MissionRecharge) missionRecharges.get(i);
                missionRechargeInfoArrayList[i][0] = mission.getTemplateId();
                missionRechargeInfoArrayList[i][1] = mission.getType();
                missionRechargeInfoArrayList[i][2] = mission.param;
            }
            int[][] missionEventInfoArrayList = new int[missionEvents.size()][3];
            for (int i = 0; i < missionEventInfoArrayList.length; i++) {
                MissionEvent mission = (MissionEvent) missionEvents.get(i);
                missionEventInfoArrayList[i][0] = mission.getTemplateId();
                missionEventInfoArrayList[i][1] = mission.getType();
                missionEventInfoArrayList[i][2] = mission.param;
            }
            int[][] npcTreeInfoArrayList = new int[npcTrees.size()][2];
            for (int i = 0; i < npcTreeInfoArrayList.length; i++) {
                NpcTree npcTree = npcTrees.get(i);
                npcTreeInfoArrayList[i][0] = npcTree.template.id;
                npcTreeInfoArrayList[i][1] = npcTree.time;
            }
            ArrayList<IntrinsicInfo> intrinsicInfoArrayList = new ArrayList<>();
            for (Intrinsic intrinsic : intrinsics.values()) {
                if (intrinsic.param > 0) {
                    intrinsicInfoArrayList.add(new IntrinsicInfo(intrinsic.template.id, intrinsic.param));
                }
            }
            EventInfo eventInfo = new EventInfo(pointEvent, pointOtherEvent, indexRewardEvent, pointRewardEvent, updateTimeEvent, updateTimeEventOther);
            long[] clanInfo = new long[]{clanId, countManor, clanTime};
            OnlineInfo onlineInfo = new OnlineInfo(onlineMinuteTotal, onlineMinuteDay);
            TrainingOfflineInfo trainingOfflineInfo = new TrainingOfflineInfo(0L, 0L);
            if (isTrainingOfflineForMaster) {
                long time = (now - timeLogin) / 1000;
                if (time > 600) {
                    trainingOfflineInfo.master = expTraining / time / 2;
                }
            }
            if (disciple != null && isTrainingOfflineForDisciple) {
                long time = (now - disciple.timeLogin) / 1000;
                if (time > 600) {
                    trainingOfflineInfo.disciple = disciple.expTraining / time / 3;
                }
            }
            Gson gson = new Gson();
            GameRepository.getInstance().playerData.saveData(this.id, gson.toJson(baseInfo), gson.toJson(powerInfo),
                    this.diamond, this.xu, this.ruby, this.xuKhoa,
                    gson.toJson(clanInfo), Utils.getJsonArrayItem(this.itemsBag), Utils.getJsonArrayItem(this.itemsBox), Utils.getJsonArrayItem(this.itemsBody),
                    Utils.getJsonArrayItem(this.itemsPet), Utils.getJsonArrayItem(this.itemsOther), gson.toJson(taskInfo), gson.toJson(skillsInfo),
                    gson.toJson(keySkill), gson.toJson(mapInfo), gson.toJson(barrackInfo), resetTime, logoutTime,
                    !isOffline, gson.toJson(pointInfo), gson.toJson(effectInfoArrayList), spaceship, gson.toJson(taskDailyInfo),
                    gson.toJson(rechargeInfo), gson.toJson(magicBeanInfo), gson.toJson(npcTreeInfoArrayList),
                    gson.toJson(friendInfoArrayList), gson.toJson(enemyInfoArrayList), gson.toJson(achievementInfoArrayList),
                    gson.toJson(missionWeekInfoArrayList), gson.toJson(missionDailyInfoArrayList),
                    gson.toJson(missionRechargeInfoArrayList), gson.toJson(missionEventInfoArrayList), gson.toJson(eventInfo),
                    gson.toJson(onlineInfo), gson.toJson(intrinsicInfoArrayList), this.pointPro, gson.toJson(trainingOfflineInfo));
        } catch (Exception ex) {
            logger.error("saveData", ex);
        }
        try {
            ((WeeklyAction) weeklyAction).save();
        } catch (Exception ex) {
            logger.error("saveData", ex);
        }
    }

    public void init() {
        id = data.id;
        name = data.name;
        userId = data.userId;
        server = data.server;
        gender = data.gender;
        pin = data.pin;
        if (pin != null && !pin.isEmpty() && !Server.getInstance().isInterServer()) {
            isProtect = true;
        }
        int[] baseInfo = Utils.gson.fromJson(data.baseInfo, new TypeToken<int[]>() {
        }.getType());
        baseDamage = baseInfo[0];
        baseHp = baseInfo[1];
        baseMp = baseInfo[2];
        baseConstitution = baseInfo[3];
        PowerInfo powerInfo = Utils.gson.fromJson(data.powerInfo, new TypeToken<PowerInfo>() {
        }.getType());
        power = powerInfo.power;
        potential = powerInfo.potential;
        level = powerInfo.level;
        limitLevel = powerInfo.limitLevel;
        if (limitLevel > Server.MAX_LEVEL) {
            limitLevel = Server.MAX_LEVEL;
        }
        if (power > Server.getInstance().levels.get(level + 1).power) {
            power = Server.getInstance().levels.get(level + 1).power;
        }
        pointSkill = powerInfo.pointSkill;
        updatePowerTime = powerInfo.updatePowerTime;
        diamond = data.diamond;
        ruby = data.ruby;
        xuKhoa = data.xuKhoa;
        xu = data.xu;
        long timeNow = System.currentTimeMillis();
        Timestamp now = new Timestamp(timeNow);
        Arrays.fill(timeUpdateEffectOptionItem, timeNow);
        if (data.resetTime == null) {
            data.resetTime = now;
        }
        resetTime = data.resetTime;
        try {
            ListItemInfo items = Utils.gson.fromJson(data.itemsBag, new TypeToken<ListItemInfo>() {
            }.getType());
            itemsBag = new Item[Math.max(items.size, DEFAULT_BAG)];
            for (int i = 0; i < items.items.size(); i++) {
                ItemInfo info = items.items.get(i);
                Item item = new Item(info);
                item.indexUI = i;
                itemsBag[i] = item;
                if (item.quantity < 1) {
                    itemsBag[i] = null;
                }
            }
        } catch (Exception ex) {
            itemsBag = new Item[DEFAULT_BAG];
        }
        try {
            ListItemInfo items = Utils.gson.fromJson(data.itemsBox, new TypeToken<ListItemInfo>() {
            }.getType());
            itemsBox = new Item[Math.max(items.size, DEFAULT_BOX)];
            for (int i = 0; i < items.items.size(); i++) {
                ItemInfo info = items.items.get(i);
                Item item = new Item(info);
                item.indexUI = i;
                itemsBox[i] = item;
                if (item.quantity < 1) {
                    itemsBox[i] = null;
                }
            }
        } catch (Exception ex) {
            itemsBox = new Item[DEFAULT_BOX];
        }
        try {
            ListItemInfo items = Utils.gson.fromJson(data.itemsBody, new TypeToken<ListItemInfo>() {
            }.getType());
            itemsBody = new Item[Math.max(items.size, DEFAULT_BODY)];
            for (int i = 0; i < items.items.size(); i++) {
                ItemInfo info = items.items.get(i);
                Item item = new Item(info);
                item.indexUI = item.getIndexBody();
                itemsBody[item.indexUI] = item;
                if (item.quantity < 1) {
                    itemsBody[item.indexUI] = null;
                }
            }
        } catch (Exception ex) {
            itemsBody = new Item[DEFAULT_BODY];
        }
        try {
            ListItemInfo items = Utils.gson.fromJson(data.itemsOther, new TypeToken<ListItemInfo>() {
            }.getType());
            itemsOther = new Item[Math.max(items.size, DEFAULT_BODY)];
            for (int i = 0; i < items.items.size(); i++) {
                ItemInfo info = items.items.get(i);
                Item item = new Item(info);
                item.indexUI = item.getIndexBody();
                itemsOther[item.indexUI] = item;
                if (item.quantity < 1) {
                    itemsOther[item.indexUI] = null;
                }
            }
        } catch (Exception ex) {
            itemsOther = new Item[DEFAULT_BODY];
        }
        try {
            ListItemInfo items = Utils.gson.fromJson(data.itemsPet, new TypeToken<ListItemInfo>() {
            }.getType());
            itemsPet = new Item[Math.max(items.size, DEFAULT_BODY)];
            for (int i = 0; i < items.items.size(); i++) {
                ItemInfo info = items.items.get(i);
                Item item = new Item(info);
                item.indexUI = item.getIndexPet();
                itemsPet[item.indexUI] = item;
                if (item.quantity < 1) {
                    itemsPet[item.indexUI] = null;
                }
            }
        } catch (Exception ex) {
            itemsPet = new Item[DEFAULT_BODY];
        }
        taskMain = new Task();
        int[] taskInfo = Utils.gson.fromJson(data.taskInfo, new TypeToken<int[]>() {
        }.getType());
        taskMain.template = TaskManager.getInstance().taskTemplates.get(taskInfo[0]);
        taskMain.index = taskInfo[1];
        taskMain.param = taskInfo[2];
        if (taskMain.index >= taskMain.template.steps.length) {
            taskMain.index = taskMain.template.steps.length - 1;
        }
        skills = SkillManager.getInstance().createSkillsByGender(data.gender);
        JSONArray skillsInfo = new JSONArray(data.skillsInfo);
        for (int i = 0; i < skillsInfo.length(); i++) {
            JSONObject object = skillsInfo.getJSONObject(i);
            Skill skill = getSkill(object.getInt("id"));
            if (skill != null) {
                skill.level = object.getInt("level");
                skill.upgrade = object.getInt("upgrade");
                skill.timeCanUse = object.getLong("time_can_use");
                skill.point = object.getInt("point");
            }
        }
        if (isAdmin()) {
            skills.clear();
            for (SkillTemplate template : SkillManager.getInstance().skillTemplates.values()) {
                if (template.id < 100) {
                    Skill skill = new Skill();
                    skill.template = template;
                    skill.level = 7;
                    skill.point = 0;
                    skill.upgrade = 7;
                    skills.add(skill);
                }
            }
        }
        skills.sort(new Comparator<Skill>() {
            @Override
            public int compare(Skill s1, Skill s2) {
                return Integer.compare(s1.template.id, s2.template.id);
            }
        });
        JSONArray keys = new JSONArray(data.keysSkill);
        keysSkill = new Skill[10];
        for (int i = 0; i < keysSkill.length; i++) {
            if (i < keys.length()) {
                int id = keys.getInt(i);
                if (id != -1) {
                    keysSkill[i] = getSkill(id);
                } else {
                    keysSkill[i] = null;
                }
            } else {
                keysSkill[i] = null;
            }
        }
        for (Skill skill : keysSkill) {
            if (skill != null) {
                mySkill = skill;
                break;
            }
        }
        if (mySkill == null) {
            keysSkill[0] = skills.get(0);
            mySkill = keysSkill[0];
        }
        List<IntrinsicTemplate> intrinsicTemplateList = SkillManager.getInstance().getIntrinsicTemplates(data.gender);
        ArrayList<IntrinsicInfo> intrinsicInfoArrayList = Utils.gson.fromJson(data.intrinsic, new TypeToken<ArrayList<IntrinsicInfo>>() {
        }.getType());
        HashMap<Integer, Integer> intrinsicInfoHashMap = new HashMap<>();
        for (IntrinsicInfo info : intrinsicInfoArrayList) {
            intrinsicInfoHashMap.put(info.id, info.param);
        }
        intrinsics = new TreeMap<>();
        for (IntrinsicTemplate template : intrinsicTemplateList) {
            Intrinsic intrinsic = new Intrinsic(template, intrinsicInfoHashMap.getOrDefault(template.id, 0));
            intrinsics.put(template.id, intrinsic);
            if (intrinsic.param > 0 && intrinsic.template.isCoolDown) {
                skills.stream().filter(s -> s.template.id == intrinsic.template.skillTemplateId).findFirst().ifPresent(skill -> skill.coolDownIntrinsic = intrinsic.param);
            }
        }
        long[] mapInfo = Utils.gson.fromJson(data.mapInfo, new TypeToken<long[]>() {
        }.getType());
        mapId = (int) mapInfo[0];
        x = (int) mapInfo[1];
        y = (int) mapInfo[2];
        hp = mapInfo[3];
        mp = mapInfo[4];
        JSONObject barrackInfo = new JSONObject(data.barrackInfo);
        countBarrack = barrackInfo.getInt("count");
        numBuyBarrack = barrackInfo.getInt("num_buy");
        pointBarrack = barrackInfo.getInt("point");
        Team team = TeamManager.getInstance().findTeamByPlayerId(data.id);
        if (team != null) {
            teamId = team.id;
        } else {
            teamId = -1;
        }
        MapManager.getInstance().barracks.values().stream().filter(b -> b.players.contains(this.id)).findFirst().ifPresent(barrack -> this.barrackId = barrack.id);
        long[] clanInfo = Utils.gson.fromJson(data.clanInfo, new TypeToken<long[]>() {
        }.getType());
        clanId = (int) clanInfo[0];
        countManor = (int) clanInfo[1];
        clanTime = clanInfo[2];
        if (clanId != -1) {
            Clan clan = ClanManager.getInstance().findClanById(clanId);
            if (clan != null) {
                ClanMember member = clan.findMemberByPlayerId(data.id);
                if (member == null) {
                    clanId = -1;
                } else {
                    this.clan = clan;
                }
            } else {
                clanId = -1;
            }
        }
        logoutTime = data.logoutTime;
        PointInfo pointInfo = Utils.gson.fromJson(data.point, new TypeToken<PointInfo>() {
        }.getType());
        pointActive = pointInfo.active;
        pointPk = pointInfo.pk;
        pointSurvival = pointInfo.survival;
        pointCapsule = pointInfo.capsule;
        pointSpaceship = pointInfo.spaceship;
        pointFlagWar = pointInfo.flagWar;
        EventInfo eventInfo = Utils.gson.fromJson(data.event, new TypeToken<EventInfo>() {
        }.getType());
        pointEvent = eventInfo.point;
        indexRewardEvent = eventInfo.indexReward;
        updateTimeEvent = eventInfo.updateTime;
        updateTimeEventOther = eventInfo.updateOtherTime;
        pointRewardEvent = eventInfo.pointReward;
        pointOtherEvent = eventInfo.pointOther;
        ArrayList<EffectInfo> effectInfoArrayList = Utils.gson.fromJson(data.effect, new TypeToken<ArrayList<EffectInfo>>() {
        }.getType());
        for (EffectInfo effectInfo : effectInfoArrayList) {
            if (effectInfo.id == EffectName.TU_DONG_LUYEN_TAP) {
                isAutoPlay = true;
            }
            Effect effect = new Effect(this, effectInfo.id, effectInfo.time, effectInfo.delay, effectInfo.param, effectInfo.updateTime);
            if (effect.template.isActiveWhenOnline) {
                effect.endTime = timeNow + effect.time;
            } else {
                effect.endTime = effectInfo.endTime;
            }
            this.effects.add(effect);
        }
        spaceship = data.spaceship;
        try {
            TaskDailyInfo taskDailyInfo = Utils.gson.fromJson(data.taskDaily, new TypeToken<TaskDailyInfo>() {
            }.getType());
            countTaskDaily = taskDailyInfo.count;
            taskDaily = taskDailyInfo.taskDaily;
            countCompleteTaskDaily = taskDailyInfo.countCompleted;
            createTaskDailyTime = taskDailyInfo.createTime;
            if (taskDaily != null && taskDaily.objectId <= 0 && (taskDaily.type == 0 || taskDaily.type == 3)) {
                taskDaily = null;
            }
        } catch (Exception ex) {
            taskDaily = null;
        }
        RechargeInfo rechargeInfo = Utils.gson.fromJson(data.recharge, new TypeToken<RechargeInfo>() {
        }.getType());
        diamondRecharge = rechargeInfo.diamond;
        isRechargedInDay = rechargeInfo.isDay;
        MagicBeanInfo magicBeanInfo = Utils.gson.fromJson(data.magicBean, new TypeToken<MagicBeanInfo>() {
        }.getType());
        magicBean = new MagicBean(magicBeanInfo);
        ArrayList<FriendInfo> friendInfoList = Utils.gson.fromJson(data.friend, new TypeToken<ArrayList<FriendInfo>>() {
        }.getType());
        friends = new ArrayList<>();
        for (FriendInfo info : friendInfoList) {
            friends.add(new Friend(info));
        }
        ArrayList<EnemyInfo> enemyInfoList = Utils.gson.fromJson(data.enemy, new TypeToken<ArrayList<EnemyInfo>>() {
        }.getType());
        enemies = new ArrayList<>();
        for (EnemyInfo info : enemyInfoList) {
            enemies.add(new Enemy(info));
        }
        int[][] npcTreeInfoList = Utils.gson.fromJson(data.npcTree, new TypeToken<int[][]>() {
        }.getType());
        npcTrees = new ArrayList<>();
        for (int[] info : npcTreeInfoList) {
            npcTrees.add(new NpcTree(this, info));
        }
        int[][] achievementInfoArrayList = Utils.gson.fromJson(data.achievement, new TypeToken<int[][]>() {
        }.getType());
        achievements = new HashMap<>();
        for (int[] info : achievementInfoArrayList) {
            achievements.put(info[0], new Achievement(info));
        }
        if (achievements.size() < AchievementManager.getInstance().achievements.size()) {
            for (Integer id : AchievementManager.getInstance().achievements.keySet()) {
                if (!achievements.containsKey(id)) {
                    achievements.put(id, new Achievement(id));
                }
            }
        }
        for (Achievement achievement : achievements.values()) {
            if (achievement.template.id == 0 || achievement.template.id == 1) {
                achievement.param = level;
            }
            if (achievement.template.id == 10 && magicBean != null) {
                achievement.param = magicBean.level;
            }
            if (achievement.template.id == 8) {
                achievement.param = onlineMinuteTotal;
            }
        }
        int[][] missionWeekInfoArrayList = Utils.gson.fromJson(data.missionWeek, new TypeToken<int[][]>() {
        }.getType());
        missionWeeks = new ArrayList<>();
        for (int[] info : missionWeekInfoArrayList) {
            missionWeeks.add(new MissionWeek(info));
        }
        if (missionWeeks.size() != MissionManager.getInstance().missionWeekTemplates.size()) {
            resetMissionWeek();
        }
        int[][] missionDailyInfoArrayList = Utils.gson.fromJson(data.missionDaily, new TypeToken<int[][]>() {
        }.getType());
        missionDailies = new ArrayList<>();
        for (int[] info : missionDailyInfoArrayList) {
            missionDailies.add(new MissionDaily(info));
        }
        if (missionDailies.size() != MissionManager.getInstance().missionDailyTemplates.size()) {
            resetMissionDaily();
        }
        int[][] missionRechargeInfoArrayList = Utils.gson.fromJson(data.missionRecharge, new TypeToken<int[][]>() {
        }.getType());
        missionRecharges = new ArrayList<>();
        for (int[] info : missionRechargeInfoArrayList) {
            missionRecharges.add(new MissionRecharge(info));
        }
        if (missionRecharges.size() != MissionManager.getInstance().missionRechargeTemplates.size()) {
            resetMissionRecharge();
        }
        int[][] missionEventInfoArrayList = Utils.gson.fromJson(data.missionEvent, new TypeToken<int[][]>() {
        }.getType());
        missionEvents = new ArrayList<>();
        for (int[] info : missionEventInfoArrayList) {
            missionEvents.add(new MissionEvent(info));
        }
        if (missionEvents.size() != MissionManager.getInstance().missionEventTemplates.size()) {
            resetMissionEvent();
        }
        OnlineInfo onlineInfo = Utils.gson.fromJson(data.onlineInfo, new TypeToken<OnlineInfo>() {
        }.getType());
        onlineMinuteTotal = onlineInfo.total;
        onlineMinuteDay = onlineInfo.day;
        rewards = new ArrayList<>();
        zoneGoBack = PlayerManager.getInstance().areaGoBacks.getOrDefault(data.id, null);
        if (zoneGoBack != null && !isAutoPlay) {
            PlayerManager.getInstance().areaGoBacks.remove(data.id);
            zoneGoBack = null;
        }
    }

    public List<String> initOrder() {
        List<String> notification = new ArrayList<>();
        try {
            int diamond = 0;
            int count = 0;
            long coin = 0;
            List<OrderData> orders = GameRepository.getInstance().orderData.findByUserIdAndStatusAndServer(userId, 0, server);
            if (!orders.isEmpty()) {
                if (isBagFull()) {
                    notification.add("Túi đồ không đủ ô trống, hãy để trống ít nhất 1 ô trong túi đồ và đăng nhập lại để có thể nhận kim cương nạp thẻ");
                    return notification;
                }
                for (OrderData order : orders) {
                    if (order.type == 4) {
                        coin += order.coin;
                    } else {
                        if (order.type != 3) {
                            count += order.diamond / 10;
                        }
                        diamond += order.diamond;
                    }
                    order.status = 1;
                    order.updateTime = new Timestamp(System.currentTimeMillis());
                }
                GameRepository.getInstance().orderData.saveAll(orders);
            }
            if (diamond > 0) {
                upDiamond(diamond);
                diamondRecharge += diamond;
                if (count > 0) {
                    addItem(ItemManager.getInstance().createItem(ItemName.THE_KIM_CUONG, count, true));
                }
                upPointAchievement(9, diamond);
                if (diamond >= 50) {
                    completeMissionWeek();
                }
                upParamMissionRecharge(diamond);
                notification.add(String.format("Nạp thẻ thành công, bạn nhận được %d và x%d Thẻ kim cương.", diamond, count));
                if (Event.isEvent()) {
                    Event.event.rewardRecharge(this, diamond);
                }
            }
            if (coin > 0) {
                xu += coin;
                notification.add(String.format("Nạp thẻ thành công, bạn nhận được %s xu", Utils.getMoneys(xu)));
            }
        } catch (Exception ex) {
            logger.error("initOrder", ex);
        }
        return notification;
    }

    public void initReward() {
        try {
            List<RewardData> rewardDataList = GameRepository.getInstance().rewardData.findByPlayerId(this.id);
            if (rewardDataList.isEmpty()) {
                return;
            }
            long now = System.currentTimeMillis();
            for (RewardData data : rewardDataList) {
                if (data.expiryTime.getTime() > now) {
                    rewards.add(new Reward(data));
                }
            }
        } catch (Exception ex) {
            logger.error("initReward", ex);
        }
    }

    public boolean isAdmin() {
        return this.id < 5;
    }

    public boolean isLevel3x() {
        return level / 10 == 3;
    }

    public boolean isLevel4x() {
        return level / 10 == 4;
    }

    public boolean isLevel1x() {
        return level / 10 == 1;
    }

    public boolean isLevel2x() {
        return level / 10 == 2;
    }

    public boolean isTrading() {
        return tradeAction != null && ((TradeAction) tradeAction).isTrading;
    }

    public void sendInviteSolo(Message message) {
        try {
            if (testPlayerId != -1) {
                addInfo(INFO_RED, "Bạn đang thách đấu người khác");
                return;
            }
            if (level < 5) {
                addInfo(INFO_RED, "Yêu cầu trình độ cấp 5 trở lên");
                return;
            }
            if (isTrading()) {
                addInfo(INFO_RED, Language.CANCEL_ACTION_WHEN_TRADE);
                return;
            }
            if (zone == null || isInSurvival() || zone.map.template.id == MapName.DAI_HOI_VO_THUAT
                    || zone.map.template.id == MapName.HANH_TINH_NGUC_TU
                    || zone.map.template.id == MapName.DAU_TRUONG || isInTreasure() || isInSpaceship()) {
                addInfo(INFO_RED, Language.CANT_ACTION);
                return;
            }
            if (isDead()) {
                addInfo(INFO_RED, "Không thể thực hiện khi kiệt sức");
                return;
            }
            int playerId = message.reader().readInt();
            Player player = zone.findPlayerById(playerId);
            if (player == null) {
                addInfo(INFO_RED, "Người chơi không có trong khu vực");
                return;
            }
            if (player.isDead()) {
                addInfo(INFO_RED, "Nhân vật này đã kiệt sức");
                return;
            }
            if (player.testPlayerId != -1) {
                addInfo(INFO_RED, "Người này đang thách đấu người khác");
                return;
            }
            if (!isCanSendAction(player)) {
                addInfo(INFO_RED, "Người chơi đang chặn người lạ, bạn cần phải kết bạn trước");
                return;
            }
            Command yes = new Command(CommandName.CONFIRM_PK, "Đồng ý", player, this.id);
            Command no = new Command(CommandName.CANCEL, "Không", player);
            player.startYesNo(String.format("%s (lv%d %s sức mạnh) muốn thách đấu với bạn?", this.name, this.level, Utils.getMoneys(this.power)), yes, no);
        } catch (Exception ex) {
            logger.error("sendInviteSolo", ex);
        }
    }

    public void confirmInviteSolo(int playerId) {
        if (testPlayerId != -1) {
            addInfo(INFO_RED, "Bạn đang thách đấu người khác");
            return;
        }
        if (level < 5) {
            addInfo(INFO_RED, "Yêu cầu trình độ cấp 5 trở lên");
            return;
        }
        if (isTrading()) {
            addInfo(INFO_RED, Language.CANCEL_ACTION_WHEN_TRADE);
            return;
        }
        if (zone == null || isInSurvival() || zone.map.template.id == MapName.DAI_HOI_VO_THUAT
                || zone.map.template.id == MapName.HANH_TINH_NGUC_TU
                || zone.map.template.id == MapName.DAU_TRUONG || isInTreasure() || isInSpaceship()) {
            addInfo(INFO_RED, Language.CANT_ACTION);
            return;
        }
        if (isDead()) {
            addInfo(INFO_RED, "Không thể thực hiện khi kiệt sức");
            return;
        }
        Player player = zone.findPlayerById(playerId);
        if (player == null) {
            addInfo(INFO_RED, "Người chơi không có trong khu vực");
            return;
        }
        if (player.isDead()) {
            addInfo(INFO_RED, "Nhân vật này đã kiệt sức");
            return;
        }
        if (player.testPlayerId != -1) {
            addInfo(INFO_RED, "Người này đang thách đấu người khác");
            return;
        }
        player.testPlayerId = this.id;
        testPlayerId = playerId;
        setTypePk(1);
        player.setTypePk(1);
        zone.service.playerFlight(this, player);
    }

    public void resultSolo(int type) {
        if (type == 0) {
            addInfo(INFO_YELLOW, "Đối thủ đã kiệt sức, bạn thắng");
        }
        if (type == 1) {
            addInfo(INFO_RED, "Bạn đã thua vì kiệt sức");
        }
        if (type == 2) {
            addInfo(INFO_YELLOW, "Đối thủ đã bỏ chạy, bạn thắng");
        }
        if (type == 3) {
            addInfo(INFO_RED, "Bạn đã thua vì bỏ chạy");
        }
    }

    public void clearPk() {
        testPlayerId = -1;
        setTypePk(0);
    }

    public boolean isInBarrack() {
        return zone != null && zone.map.isBarrack();
    }

    public boolean isInManor() {
        return zone != null && zone.map.isManor();
    }

    public boolean isInDragonBallNamek() {
        return zone != null && zone.map.isDragonBallNamek();
    }

    public boolean isInFlagWar() {
        return zone != null && zone.map.isFlagWar();
    }

    public boolean isInForgottenCity() {
        return zone != null && zone.map.isForgottenCity();
    }

    public boolean isInTreasure() {
        return zone != null && zone.map.isTreasure();
    }

    public boolean isInIsland() {
        return zone != null && zone.map.isIsland();
    }

    public boolean isInSpaceship() {
        return zone != null && zone.map.isSpaceship();
    }

    public boolean isAdminMartialArtsFestival() {
        return MapManager.getInstance().martialArtsFestival != null && MapManager.getInstance().martialArtsFestival.masterId == this.id;
    }

    public boolean isAdminSurvival() {
        return MapManager.getInstance().survival != null && MapManager.getInstance().survival.masterId == this.id;
    }

    public boolean isInSurvival() {
        return zone != null && zone.map.isSurvival();
    }

    public void recoverSkill() {
        for (Skill skill : skills) {
            if (skill.level > 0) {
                skill.timeCanUse = 0;
            }
        }
        service.recoverSkill();
    }

    public Barrack getBarrack() {
        return MapManager.getInstance().barracks.get(barrackId);
    }

    public void clearItemTask() {
        lockAction.lock();
        try {
            boolean flag = false;
            for (int i = 0; i < itemsBag.length; i++) {
                if (itemsBag[i] != null && itemsBag[i].template.type == ItemType.TYPE_TASK) {
                    itemsBag[i] = null;
                    flag = true;
                }
            }
            if (flag) {
                service.setItemBag();
            }
        } finally {
            lockAction.unlock();
        }
    }

    public void nextTask() {
        clearItemTask();
        lock.lock();
        try {
            if (taskMain == null) {
                return;
            }
            int index = taskMain.template.id;
            taskMain.param = 0;
            taskMain.index = 0;
            taskMain.template = TaskManager.getInstance().taskTemplates.get(index + 1);
            service.setTaskMain();
        } finally {
            lock.unlock();
        }
    }

    public void nextTaskIndex() {
        if (taskMain == null) {
            return;
        }
        taskMain.param = 0;
        taskMain.index++;
        service.nextTaskIndex();
    }

    public void nextTaskParam() {
        if (taskMain == null) {
            return;
        }
        taskMain.param++;
        if (taskMain.param >= taskMain.template.steps[taskMain.index].param) {
            nextTaskIndex();
        } else {
            service.nextTaskParam();
        }
    }

    public void sendMessage(Message message) {
        if (session == null || !isPlayer()) {
            return;
        }
        session.sendMessage(message);
    }

    public String getIpAddress() {
        return session.ip;
    }
}
