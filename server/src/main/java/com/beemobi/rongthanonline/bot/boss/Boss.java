package com.beemobi.rongthanonline.bot.boss;

import com.beemobi.rongthanonline.effect.Effect;
import com.beemobi.rongthanonline.effect.EffectName;
import com.beemobi.rongthanonline.entity.Entity;
import com.beemobi.rongthanonline.entity.player.Player;
import com.beemobi.rongthanonline.entity.player.PlayerManager;
import com.beemobi.rongthanonline.item.Item;
import com.beemobi.rongthanonline.item.ItemManager;
import com.beemobi.rongthanonline.item.ItemTemplate;
import com.beemobi.rongthanonline.item.OptionName;
import com.beemobi.rongthanonline.map.Map;
import com.beemobi.rongthanonline.map.MapManager;
import com.beemobi.rongthanonline.map.Zone;
import com.beemobi.rongthanonline.map.expansion.barrack.Barrack;
import com.beemobi.rongthanonline.model.PointWeeklyType;
import com.beemobi.rongthanonline.server.Server;
import com.beemobi.rongthanonline.service.Service;
import com.beemobi.rongthanonline.skill.Skill;
import com.beemobi.rongthanonline.skill.SkillName;
import com.beemobi.rongthanonline.skill.SkillType;
import com.beemobi.rongthanonline.task.*;
import com.beemobi.rongthanonline.top.TopType;
import com.beemobi.rongthanonline.util.Utils;
import org.apache.log4j.Logger;

import java.util.*;
import java.util.stream.Collectors;

public abstract class Boss extends Player {
    private static final Logger logger = Logger.getLogger(Boss.class);
    public static int autoIncrease = 1000000000;
    public BossTemplate template;
    public String sayTheLastWordBeforeDie;
    public List<String> saysBeforeAttack = new ArrayList<>();
    public List<String> saysWhenAttack = new ArrayList<>();
    public int indexChat;
    public long waitingTimeToLeave;
    public long delayRespawn;
    public long timeBorn;
    public TeamBoss team;
    public int taskId;
    public int taskIndex;
    public HashMap<Integer, Long> enemies;
    public boolean isTask;
    public boolean isHasPoint;

    public Boss(int templateId, int level) {
        super();
        id = autoIncrease++;
        service = new Service(this);
        speed = 10;
        template = BossManager.getInstance().bossTemplates.get(templateId);
        name = template.name;
        itemsBody = new Item[Player.DEFAULT_BODY];
        if (level == -1) {
            this.level = template.level;
        } else {
            this.level = level;
        }
        if (template.head != -1) {
            head = template.head;
            body = template.body;
        } else {
            List<ItemTemplate> items = ItemManager.getInstance().itemTemplates.values().stream().filter(i -> i.type == 8 && i.body != -1 && i.head != -1).collect(Collectors.toList());
            ItemTemplate template = items.get(Utils.nextInt(items.size()));
            head = template.head;
            body = template.body;
        }
        waitingTimeToLeave = 5000;
        delayRespawn = Utils.nextInt(900000, 1200000);
        hp = maxHp = template.hp;
        mp = maxMp = 100;
        timeBorn = System.currentTimeMillis();
        skills = new ArrayList<>();
        damage = template.damage;
        options[OptionName.NE_DON] = 2000;
        options[OptionName.GIAM_SAT_THUONG] = 80 * this.level;
        taskId = -1;
        taskIndex = -1;
        enemies = new HashMap<>();
        setSkill();
        setTask();
        isHasPoint = true;
    }

    public void setTask() {
        if (template == null) {
            return;
        }
        for (TaskTemplate taskTemplate : TaskManager.getInstance().taskTemplates.values()) {
            for (TaskStepTemplate taskStepTemplate : taskTemplate.steps) {
                if (taskStepTemplate.bossId == template.id) {
                    isTask = true;
                    return;
                }
            }
        }
    }

    public abstract void setSkill();

    public void sendNotificationWhenAppear(Zone zone) {
        Server.getInstance().service.serverChat(String.format("BOSS %s vừa xuất hiện tại %s khu vực %d", template.name, zone.map.template.name, zone.id));
        logger.debug(String.format("%s: %s khu vực %d", template.name, zone.map.template.name, zone.id));
    }

    public void sendNotificationWhenDead(String name) {
        Server.getInstance().service.serverChat(String.format("%s: Đã tiêu diệt được %s mọi người đều ngưỡng mộ", name, this.name));
        logger.debug(String.format("%s: tiêu diệt %s", name, this.name));
    }

    @Override
    public void startDie(Entity killer) {
        if (killer != null && killer.isPlayer()) {
            sendNotificationWhenDead(((Player) killer).name);
        }
        if (sayTheLastWordBeforeDie != null) {
            chat(sayTheLastWordBeforeDie);
        }
        typePk = 0;
        clearAllEffect();
        super.startDie(killer);
        if (waitingTimeToLeave >= 0) {
            Utils.setTimeout(() -> {
                if (zone != null) {
                    zone.leave(this);
                }
            }, waitingTimeToLeave);
        }
        if (template.isAutoRespawn) {
            Utils.setTimeout(this::respawn, delayRespawn);
        }
    }

    @Override
    public void injure(Entity attacker, long hpInjure, boolean isCritical, boolean isStrikeBack) {
        if (attacker != null) {
            enemies.put(attacker.id, enemies.getOrDefault(attacker.id, 0L) + hpInjure);
        }
        super.injure(attacker, hpInjure, isCritical, isStrikeBack);
        if (isDead() && attacker != null && !attacker.isPet()) {
            for (Integer playerId : enemies.keySet()) {
                if (playerId == attacker.id && enemies.get(playerId) >= maxHp / 10) {
                    Player player = PlayerManager.getInstance().findPlayerById(playerId);
                    if (player != null && player.zone != null && player.zone == zone && player.taskMain != null &&
                            player.taskMain.template.steps[player.taskMain.index].bossId == template.id) {
                        player.nextTaskParam();
                    }
                }
            }
            Player player = (Player) attacker;
            if (player.taskMain != null) {
                Task taskMain = player.taskMain;
                if (taskMain.template.steps[taskMain.index].bossId == template.id) {
                    player.nextTaskParam();
                }
            }
            if (player.mySkill.isSkillUltimate()) {
                player.upPointUpgradeSkill(player.mySkill, 1);
            }
            if (player.zone != null) {
                Barrack barrack = player.zone.map.getBarrack();
                if (barrack != null) {
                    barrack.upPoint(player.zone, 10, 1);
                }
                if (player.clan != null && player.clan.taskClan != null
                        && player.clan.taskClan.type == TaskClanType.KILL_BOSS && player.zone.findAllPlayerSameClan(player).size() >= 2) {
                    player.clan.taskClan.param++;
                    player.clan.service.addInfo(Player.INFO_YELLOW, String.format("Nhiệm vụ hoàn thành %d/%d", player.clan.taskClan.param, player.clan.taskClan.maxParam));
                }
            }
            if (player.isPlayer()) {
                player.upPointAchievement(13, 1);
            }
        }
        if (isDead() && isHasPoint) {
            for (Integer id : enemies.keySet()) {
                long damage = enemies.get(id) / 1000000;
                if (damage > 0) {
                    Player player = PlayerManager.getInstance().findPlayerById(id);
                    if (player != null) {
                        player.upPointWeekly(PointWeeklyType.BOSS, damage);
                    }
                }
            }
            if (attacker instanceof Player player && attacker.isPlayer()) {
                player.upPointWeekly(PointWeeklyType.BOSS, maxHp / 1000000);
            }
            enemies.clear();
        }
    }

    public void respawn() {
        if (isDead()) {
            wakeUpFromDead();
        }
        recovery(RECOVERY_ALL, 100, false);
        enemies.clear();
        timeBorn = System.currentTimeMillis();
        joinClient();
    }

    public void setLocation(Zone zone) {
        setLocation(zone.map, zone.id);
    }

    public void setLocation(int mapId, int areaId) {
        Map map = MapManager.getInstance().maps.get(mapId);
        if (map != null) {
            setLocation(map, areaId);
        }
    }

    public void setLocation(Map map, int areaId) {
        x = 600 + Utils.nextInt(1000);
        y = map.getYSd(x);
        joinMap(map, areaId);
        sendNotificationWhenAppear(zone);
        timeBorn = System.currentTimeMillis();
    }

    @Override
    public boolean isPlayer() {
        return false;
    }

    @Override
    public boolean isBoss() {
        return true;
    }

    public void joinClient() {
        enemies.clear();
        timeBorn = System.currentTimeMillis();
    }

    @Override
    public void update() {
        updateEffect();
        if (isDead()) {
            return;
        }
        long now = System.currentTimeMillis();
        if (now - timeBorn > 3600000) {
            chat("Chế độ tự hủy, bùm bùm bùm!!!");
            injure(null, maxHp, false, false);
            return;
        }
        super.updateEveryTime();
    }

    @Override
    public void updateEveryThirtySeconds(long now) {
        super.updateEveryThirtySeconds(now);
        recovery(RECOVERY_HP, 5, true);
        if (typePk == 3) {
            Skill skill = getSkill(SkillName.KHIEN_NANG_LUONG);
            if (skill != null) {
                addEffect(new Effect(this, EffectName.KHIEN_NANG_LUONG, skill.getParam(28)));
                chat("Vòng bảo vệ kích hoạt!!!");
            }
        }
    }

    @Override
    public void updateEveryOneMinutes(long now) {
        super.updateEveryOneMinutes(now);
        if (typePk == 3) {
            Skill skill = getSkill(SkillName.TAI_TAO_NANG_LUONG);
            if (skill != null) {
                mySkill = skill;
                zone.service.useSkill(this);
                addEffect(new Effect(this, EffectName.TAI_TAO_NANG_LUONG, 5100, 1000, mySkill.getParam(7)));
            }
        }
    }

    public boolean isAttack() {
        return !isDead();
    }

    @Override
    public void updateEveryOneSeconds(long now) {
        super.updateEveryOneSeconds(now);
        if (!isAttack()) {
            return;
        }
        if (typePk != 3 && typePk != 1) {
            if (team == null) {
                List<Player> players = zone.getPlayers(Zone.TYPE_PLAYER);
                if (!players.isEmpty()) {
                    if (now - lastTimeChatPublic > 3000) {
                        lastTimeChatPublic = now;
                        if (indexChat >= saysBeforeAttack.size()) {
                            setTypePk(3);
                        } else {
                            chat(saysBeforeAttack.get(indexChat));
                            indexChat++;
                        }
                    }
                }
            }
            return;
        }
        if (isStun()) {
            return;
        }
        mySkill = getSkillAttack();
        if (mySkill == null) {
            return;
        }
        List<Player> players = zone.getPlayers(Zone.TYPE_PLAYER, Zone.TYPE_DISCIPLE).stream().filter(p -> !p.isDead() && p.options[OptionName.VO_HINH] == 0 && isCanAttackEntity(p)).collect(Collectors.toList());
        if (players.isEmpty()) {
            return;
        }
        if (!saysWhenAttack.isEmpty() && now - lastTimeChatPublic > 3000) {
            lastTimeChatPublic = now;
            chat(saysWhenAttack.get(Utils.nextInt(saysWhenAttack.size())));
        }
        focus = players.get(Utils.nextInt(players.size()));
        int dx = mySkill.getDx();
        int x = focus.x + Utils.nextInt(dx / 3, dx - dx / 4) * (Utils.nextInt(2) % 2 == 0 ? 1 : (-1));
        int y = focus.y - Utils.nextInt(100);
        if (x < 90) {
            x = 90;
        }
        if (x > this.zone.map.template.width - 90) {
            x = this.zone.map.template.width - 90;
        }
        if (y < 350) {
            y = 350;
        }
        if (y > zone.map.template.height - 90) {
            y = zone.map.template.height - 90;
        }
        this.x = x;
        this.y = y;
        zone.service.move(this);
        Skill skill = getSkill(SkillName.THAI_DUONG_HA_SAN);
        if (skill != null && now > skill.timeCanUse) {
            useSkill(skill, null);
            chat("Thái dương hạ san");
            return;
        }
        zone.service.useSkill(this);
        attackPlayer((Player) focus);
    }

    @Override
    public long getManaUseSkill() {
        return 0L;
    }

    @Override
    public long getManaUseSkill(Skill skill) {
        return 0L;
    }

    public Skill getSkillAttack() {
        List<Skill> skillList = new ArrayList<>();
        for (Skill skill : skills) {
            if (skill.template.type == SkillType.NEED_FOCUS) {
                skillList.add(skill);
            }
        }
        if (skillList.isEmpty()) {
            return null;
        }
        return skillList.get(Utils.nextInt(skillList.size()));
    }

    public void teleport(Map map, int areaId) {
        recovery(RECOVERY_ALL, 100, false);
        if (zone != null) {
            zone.leave(this);
        }
        x = 600 + Utils.nextInt(1000);
        y = 10;
        joinMap(map, areaId);
        y = map.getYSd(x);
        sendNotificationWhenAppear(zone);
    }

    public boolean isNotThrowItem() {
        return isTask && Server.getInstance().isHourSupportTask();
    }
}
