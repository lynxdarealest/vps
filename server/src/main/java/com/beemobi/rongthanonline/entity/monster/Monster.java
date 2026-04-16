package com.beemobi.rongthanonline.entity.monster;

import com.beemobi.rongthanonline.bot.disciple.Disciple;
import com.beemobi.rongthanonline.common.RandomCollection;
import com.beemobi.rongthanonline.effect.Effect;
import com.beemobi.rongthanonline.effect.EffectName;
import com.beemobi.rongthanonline.entity.Entity;
import com.beemobi.rongthanonline.entity.monster.pet.Pet;
import com.beemobi.rongthanonline.entity.player.Player;
import com.beemobi.rongthanonline.entity.player.PlayerManager;
import com.beemobi.rongthanonline.item.*;
import com.beemobi.rongthanonline.map.Zone;
import com.beemobi.rongthanonline.map.MapName;
import com.beemobi.rongthanonline.map.MapPlanet;
import com.beemobi.rongthanonline.map.expansion.barrack.Barrack;
import com.beemobi.rongthanonline.map.expansion.manor.Manor;
import com.beemobi.rongthanonline.model.Intrinsic;
import com.beemobi.rongthanonline.skill.Skill;
import com.beemobi.rongthanonline.task.Task;
import com.beemobi.rongthanonline.task.TaskClan;
import com.beemobi.rongthanonline.task.TaskClanType;
import com.beemobi.rongthanonline.task.TaskStepTemplate;
import com.beemobi.rongthanonline.util.Utils;

import java.util.*;

public class Monster extends Entity {

    public MonsterTemplate template;
    public MonsterStatus status;
    public MonsterLevelBoss levelBoss;
    public boolean isAutoRefresh;
    public int numRefresh;
    public long baseHp;
    public LinkedHashMap<Player, Long> enemies = new LinkedHashMap<>();
    public Player focus;
    public long delayRespawn;
    public long timeRespawn;

    public Monster() {
        super();
        status = MonsterStatus.LIVE;
        levelBoss = MonsterLevelBoss.NORMAL;
        options[OptionName.NE_DON] = 500;
        isAutoRefresh = true;
        Arrays.fill(lastUpdates, System.currentTimeMillis());
    }

    @Override
    public void update() {
        long now = System.currentTimeMillis();
        if (zone != null && !zone.map.isExpansion()
                && status == MonsterStatus.LIVE && levelBoss != MonsterLevelBoss.NORMAL
                && now - timeRespawn > 300000) {
            lock.lock();
            try {
                long hpInjure = hp;
                hp = -hpInjure;
                startDie(null);
                zone.service.monsterStartDie(this, hpInjure, false);
            } finally {
                lock.unlock();
            }
            return;
        }
        if (status == MonsterStatus.DIE) {
            if (isAutoRefresh && now - timeDie > delayRespawn) {
                respawn();
            }
            return;
        }
        updateEffect();
        updateEveryTime();
        if (template.id != 0 && now - lastTimeAttack > delayAttack() && isCanAttack()) {
            lastTimeAttack = now;
            updateAttack();
        }
    }

    public void updateAttack() {
        Player target = findTarget();
        if (target == null) {
            return;
        }
        if (target.isDodge(this, 0)) {
            zone.service.monsterAttack(this, target, 0);
            return;
        }
        long damage = this.damage;
        if (level > 15) {
            List<Player> players = zone.getPlayers(Zone.TYPE_PLAYER, Zone.TYPE_DISCIPLE);
            for (Player player : players) {
                if (player != null && player.zone == this.zone && !player.isDead() && Math.abs(x - player.x) < 500) {
                    if (enemies.containsKey(player)) {
                        damage += Math.max(damage / 5, 1);
                    } else {
                        damage += Math.max(damage / 10, 1);
                    }
                }
            }
        }
        if (levelBoss == MonsterLevelBoss.TINH_ANH) {
            damage *= 5;
        } else if (levelBoss == MonsterLevelBoss.THU_LINH) {
            damage *= 10;
        }
        damage = Utils.nextLong(damage - damage / 10, damage);
        damage = target.formatDamageInjure(this, damage, false);
        if (damage <= 0) {
            zone.service.monsterAttack(this, target, 0);
            return;
        }
        target.lock.lock();
        try {
            if (isDead() || target.isDead()) {
                return;
            }
            target.hp -= damage;
            zone.service.monsterAttack(this, target, damage);
            if (target.hp <= 0) {
                target.startDie(this);
            } else if (target.options[OptionName.PHAN_SAT_THUONG] > 0) {
                long dmg = Utils.statsOfPoint(damage, target.options[OptionName.PHAN_SAT_THUONG]);
                if (dmg >= hp) {
                    dmg = hp - 1;
                }
                if (dmg > 0) {
                    // phản sát thương
                    injure(target, dmg, false, false);
                }
            }
        } finally {
            target.lock.unlock();
        }
        if (target.isHaveEffect(EffectName.TAI_TAO_NANG_LUONG) && Math.abs(level - target.level) < 4) {
            Skill skill = target.skills.get(3);
            if (skill != null && skill.upgrade > 0) {
                target.upPointUpgradeSkill(skill, 1);
            }
        }
    }

    public Player findTarget() {
        if (focus != null && !focus.isDead() && enemies.containsKey(focus) && focus.zone == this.zone
                && Utils.getDistance(x, y, focus.x, focus.y) < 900) {
            return focus;
        }
        List<Player> players = new ArrayList<>();
        for (Player player : enemies.keySet()) {
            if (player != null && !player.isDead() && player.zone == this.zone
                    && Utils.getDistance(x, y, player.x, player.y) < 900) {
                players.add(player);
            }
        }
        if (!players.isEmpty()) {
            return players.get(Utils.nextInt(players.size()));
        }
        if (template.id >= 6 && levelBoss != MonsterLevelBoss.TINH_ANH && levelBoss != MonsterLevelBoss.THU_LINH) {
            Player player = null;
            players = zone.getPlayers(Zone.TYPE_PLAYER, Zone.TYPE_DISCIPLE);
            int num = 300;
            for (Player p : players) {
                if (!p.isDead() && p.options[OptionName.VO_HINH] == 0 && Utils.getDistance(x, y, p.x, p.y) < num) {
                    num = Math.abs(x - p.x);
                    player = p;
                }
            }
            return player;
        }
        return null;
    }

    @Override
    public List<ItemMap> throwItem(Entity killer) {
        List<ItemMap> itemMaps = new ArrayList<>();
        if (isPet()) {
            return itemMaps;
        }
        Player player;
        if (killer.isDisciple()) {
            player = ((Disciple) killer).master;
        } else if (killer.isPet()) {
            player = ((Pet) killer).master;
        } else {
            player = (Player) killer;
        }
        Task task = player.taskMain;
        if (task != null) {
            TaskStepTemplate taskStepTemplate = task.template.steps[task.index];
            if (taskStepTemplate.monsterId == template.id && taskStepTemplate.itemId != -1 && Utils.nextInt(100) < Math.max(40 - player.level, 5)) {
                ItemMap itemMap = ItemManager.getInstance().createItemMap(taskStepTemplate.itemId, 1, player.id);
                itemMap.isTask = true;
                itemMaps.add(itemMap);
            }
        }
        Player pro = getPlayerAttackMost();
        int itemMapId = Math.abs(pro == null ? player.id : pro.id);
        if (zone.map.isManor()) {
            Manor manor = (Manor) zone.map.expansion;
            itemMaps.add(ItemManager.getInstance().createItemMap(ItemName.XU_KHOA, Utils.nextInt(1000 * manor.level, 2000 * manor.level), itemMapId));
            if (Utils.nextInt(100) < 20 + 10 * levelBoss.ordinal()) {
                int[][] items = new int[][]{
                        {
                                ItemName.DA_7,
                                ItemName.DA_8,
                                ItemName.DA_9
                        },
                        {
                                ItemName.DA_8,
                                ItemName.DA_9,
                                ItemName.DA_10
                        },
                        {
                                ItemName.DA_9,
                                ItemName.DA_10,
                                ItemName.DA_11
                        }
                };
                RandomCollection<Integer> r = new RandomCollection<>();
                r.add(1, 2);
                r.add(9, 1);
                r.add(90, 0);
                itemMaps.add(ItemManager.getInstance().createItemMap(items[r.next()][levelBoss.ordinal()], 1, itemMapId));
            }
            if (Utils.nextInt(100) < 10 + 10 * levelBoss.ordinal()) {
                RandomCollection<Integer> r = new RandomCollection<>();
                r.add(1, ItemName.BUA_BAO_VE_CAP_3);
                r.add(9, ItemName.BUA_BAO_VE_CAP_2);
                r.add(90, ItemName.BUA_BAO_VE_CAP_1);
                itemMaps.add(ItemManager.getInstance().createItemMap(r.next(), 1, itemMapId, 7));
            }
        } else {
            List<Effect> effectList = player.getEffects();
            if (Math.abs(level - player.level) <= 3 && (!zone.map.isBarrack() || numRefresh <= 0) && template.id != 0) {
                if (Utils.nextInt(100) < 5 + (player.isAutoPlay ? 10 : 0) + Math.max(40 - level, 0)) {
                    int xu = getXu();
                    int baseXu = xu;
                    if (player.options[OptionName.TI_LE_XU_DANH_QUAI] > 0) {
                        xu += (int) Utils.percentOf(baseXu, player.options[OptionName.TI_LE_XU_DANH_QUAI] / 100);
                    }
                    Intrinsic intrinsic = player.intrinsics.get(24);
                    if (intrinsic != null && intrinsic.param > 0) {
                        xu += (int) Utils.percentOf(baseXu, intrinsic.param);
                    }
                    if (xu > 0) {
                        itemMaps.add(ItemManager.getInstance().createItemMap(ItemName.XU_KHOA, xu, itemMapId));
                    }
                }
                if (Utils.nextInt(100) < Math.max(40 - 10 * (level / 10), 10)) {
                    itemMaps.add(ItemManager.getInstance().createItemMap(Math.min(56, 50 + level / 10), 1, itemMapId));
                }
                if (!zone.map.isBarrack() && levelBoss == MonsterLevelBoss.THU_LINH && Utils.nextInt(100) < 20) {
                    itemMaps.add(ItemManager.getInstance().createItemMap(ItemName.BI_KIP_KY_NANG, 1, itemMapId));
                }
                if (Utils.nextInt(100 + 50 * level) == 0) {
                    ItemMap itemMap;
                    if (level < 10) {
                        itemMap = ItemManager.getInstance().createItemMap(Utils.nextInt(0, 15), 1, itemMapId);
                    } else if (level < 20) {
                        itemMap = ItemManager.getInstance().createItemMap(Utils.nextInt(16, 31), 1, itemMapId);
                    } else {
                        itemMap = ItemManager.getInstance().createItemMap(Utils.nextInt(32, 47), 1, itemMapId);
                    }
                    itemMap.randomParam(-15, 15);
                    int percent = Utils.nextInt(100);
                    if (percent == 0) {
                        itemMap.options.add(new ItemOption(67, 3));
                    } else if (percent < 5) {
                        itemMap.options.add(new ItemOption(67, 2));
                    } else if (percent < 15) {
                        itemMap.options.add(new ItemOption(67, 1));
                    }
                    itemMaps.add(itemMap);
                }
            }
            boolean isRadarRT = player.isHaveEffect(effectList, EffectName.DO_MANH_TRANG_BI);
            if (zone.map.template.planet == MapPlanet.NAMEK || zone.map.template.id == 89 || zone.map.template.id == 90
                    || (zone.map.template.id >= 106 && zone.map.template.id <= 113) || zone.map.isBill()) {
                if (player.isHaveEffect(effectList, EffectName.MAY_DO_CAPSULE_DONG)) {
                    long now = System.currentTimeMillis();
                    if (now - PlayerManager.getInstance().timesReceiveCapsuleDong.getOrDefault(player.id, 0L) > 55000L && Utils.isPercent(10)) {
                        PlayerManager.getInstance().timesReceiveCapsuleDong.put(player.id, now);
                        itemMaps.add(ItemManager.getInstance().createItemMap(ItemName.CAPSULE_DONG, 1, itemMapId));
                    }
                }
                int percent = 0;
                if (zone.map.template.id == MapName.LANG_CO_GIRA) {
                    percent = isRadarRT ? 30 : 3;
                } else if (isRadarRT) {
                    percent = 20;
                }
                if (percent > 0 && Utils.nextInt(100) < percent) {
                    itemMaps.add(ItemManager.getInstance().createItemMap(Utils.nextInt(283,290), 1, itemMapId));
                }
                boolean isRadarRT2 = player.isHaveEffect(effectList, EffectName.DO_TIM_DA_NGU_SAC);
                if (isRadarRT2 && player.level >= 50 && (level >= 50 || template.id == 43) && Utils.nextInt(100) < 3) {
                    itemMaps.add(ItemManager.getInstance().createItemMap(ItemName.DA_NGU_SAC, 1, itemMapId));
                }
            }
            if (zone.map.isYardrat()) {
                if (Utils.nextInt(100) < 20) {
                    itemMaps.add(ItemManager.getInstance().createItemMap(ItemName.MANH_YARDRAT_THUONG, 1, itemMapId));
                }
                if (zone.map.template.id == 93 && player.isHaveEffect(effectList, EffectName.DO_MANH_YARDRAT) && Utils.nextInt(100) < 20) {
                    itemMaps.add(ItemManager.getInstance().createItemMap(ItemName.MANH_YARDRAT_DAC_BIET, 1, itemMapId));
                }
            }
            if (zone.map.isBill()) {
                if (Utils.isPercent(5)) {
                    itemMaps.add(ItemManager.getInstance().createItemMap(ItemName.MANH_VO_BONG_TAI, 1, itemMapId));
                }
            }
            if (zone.map.isIsland() && (this.level - player.level >= 0 || (player.level >= 85 && this.level >= 85)) && player.pointPlant == 0 && player.isHaveEffect(effectList, EffectName.DO_TINH_THACH) && Utils.isPercent(5)) {
                itemMaps.add(ItemManager.getInstance().createItemMap(ItemName.TINH_THACH, 1, itemMapId));
            }
            if (level / 10 == 3 && (player.isLevel3x() || (player.taskMain != null && player.taskMain.template.id == 12 && (player.taskMain.index == 1 || player.taskMain.index == 3)))) {
                int percent = isRadarRT ? 40 : 1;
                if (player.taskMain != null && player.taskMain.template.id == 12) {
                    percent += 10;
                }
                if (Utils.nextInt(100) < percent) {
                    itemMaps.add(ItemManager.getInstance().createItemMap(Utils.nextInt(133, 140), 1, itemMapId));
                }
            }
        }
        return itemMaps;
    }

    public int getXu() {
        int per = Math.max(level / 10, 1);
        int xu = level * per * per * 10 + 100;
        if (xu > 20000) {
            xu = 20000;
        }
        if (xu < 100) {
            xu = 100;
        }
        Barrack barrack = zone.map.getBarrack();
        if (barrack != null) {
            if (numRefresh > 0) {
                return 1;
            }
            xu = xu * 2 * barrack.countPlayer / 6;
        }
        return Utils.nextInt(xu - xu / 10, xu);
    }

    public Player getPlayerAttackMost() {
        if (enemies.isEmpty()) {
            return null;
        }
        List<Map.Entry<Player, Long>> list
                = new ArrayList<>(enemies.entrySet());
        list.sort((entry1, entry2) -> {
            return entry2.getValue().compareTo(entry1.getValue());
        });
        return list.get(0).getKey();
    }

    @Override
    public void injure(Entity attacker, long hpInjure, boolean isCritical, boolean isStrikeBack) {
        if (attacker == null) {
            return;
        }
        lock.lock();
        try {
            if (isDead()) {
                return;
            }
            hp -= hpInjure;
            if (hp <= 0) {
                startDie(attacker);
                zone.service.monsterStartDie(this, hpInjure, isCritical);
            } else {
                zone.service.monsterInjure(this, hpInjure, isCritical);
            }
        } finally {
            lock.unlock();
        }
        if (!(attacker instanceof Player player)) {
            return;
        }
        addEnemy(player, hpInjure);
        if (!isDead() && isStrikeBack && hpInjure > 0 && options[OptionName.PHAN_SAT_THUONG] > 0) {
            long damage = Utils.statsOfPoint(hpInjure, options[OptionName.PHAN_SAT_THUONG]);
            if (damage > 0) {
                // phản sát thương
                player.injure(this, damage, false, false);
            }
        }
        if (isDead() && attacker.isPlayer()) {
            if (isTaskKill(player)) {
               /* if (player.taskMain.template.id == 8) {
                    Task taskMain = player.taskMain;
                    if (taskMain.index == 1 || taskMain.index == 3) {
                        player.nextTaskParam();
                    } else if ((taskMain.index == 2 && levelBoss == MonsterLevelBoss.TINH_ANH) || (taskMain.index == 4 && levelBoss == MonsterLevelBoss.THU_LINH)) {
                        player.nextTaskParam();
                        Team team = player.getTeam();
                        if (team != null) {
                            team.lock.readLock().lock();
                            try {
                                for (TeamMember member : team.members) {
                                    if (member.playerId == player.id) {
                                        continue;
                                    }
                                    Player p = zone.findPlayerById(member.playerId);
                                    if (p != player && !p.isDead() && p.taskMain.template.id == 8
                                            && ((p.taskMain.index == 2 && levelBoss == MonsterLevelBoss.TINH_ANH) || (p.taskMain.index == 4 && levelBoss == MonsterLevelBoss.THU_LINH))
                                            && p.zone == zone && enemies.containsKey(p)) {
                                        p.nextTaskParam();
                                    }
                                }
                            } finally {
                                team.lock.readLock().unlock();
                            }
                        }
                    }
                } else */if (player.taskMain.template.id == 25 || player.taskMain.template.id == 26) {
                    if (zone.findAllPlayerSameClan(player).size() > 2) {
                        player.nextTaskParam();
                    }
                } else {
                    player.nextTaskParam();
                }
            }
            if (player.taskDaily != null) {
                if (player.taskDaily.type == 0 && player.taskDaily.objectId == template.id) {
                    player.upTaskDailyParam(1);
                }
                if (player.taskDaily.type == 3 && player.taskDaily.objectId == template.id
                        && (levelBoss == MonsterLevelBoss.THU_LINH || levelBoss == MonsterLevelBoss.TINH_ANH)) {
                    player.upTaskDailyParam(1);
                }
            }
            if (player.clan != null) {
                TaskClan taskClan = player.clan.taskClan;
                if (taskClan != null && taskClan.objectId == this.template.id
                        && (taskClan.type == TaskClanType.KILL_MONSTER || (taskClan.type == TaskClanType.KILL_SUPER_MONSTER && (levelBoss == MonsterLevelBoss.THU_LINH || levelBoss == MonsterLevelBoss.TINH_ANH)))) {
                    List<Player> playerList = player.zone.findAllPlayerSameClan(player);
                    boolean flag = false;
                    for (Player p : playerList) {
                        if (p.id != player.id && p.zone == zone && !p.isDead()) {
                            flag = true;
                            break;
                        }
                    }
                    if (flag) {
                        taskClan.param++;
                        if (taskClan.param % 10 == 0) {
                            player.clan.service.addInfo(Player.INFO_YELLOW, String.format("Nhiệm vụ hoàn thành %d/%d", taskClan.param, taskClan.maxParam));
                        }
                    }
                }
            }
            if (player.zone != null) {
                Barrack barrack = player.zone.map.getBarrack();
                if (barrack != null) {
                    barrack.upPoint(player.zone, (1 + 2 * levelBoss.ordinal()) * (numRefresh > 0 ? -1 : 1), 0);
                }
            }
            if (Math.abs(level - player.level) < 4) {
                if (player.mySkill == player.skills.get(0) || player.mySkill == player.skills.get(2)) {
                    player.upPointUpgradeSkill(player.mySkill, 1 + levelBoss.ordinal());
                }
                if (levelBoss == MonsterLevelBoss.TINH_ANH) {
                    player.upPointAchievement(11, 1);
                }
                if (levelBoss == MonsterLevelBoss.THU_LINH) {
                    player.upPointAchievement(12, 1);
                }
            }
            if (template.id == 0) {
                player.upPointAchievement(17, 1);
            }
        }
    }

    public boolean isTaskKill(Player player) {
        Task task = player.taskMain;
        if (task == null) {
            return false;
        }
        TaskStepTemplate taskStepTemplate = task.template.steps[task.index];
        return taskStepTemplate.monsterId == template.id && taskStepTemplate.itemId == -1;
    }

    @Override
    public boolean isDodge(Entity attacker, int accurate) {
        int dodge = options[OptionName.NE_DON] + accurate;
        if (dodge < 0) {
            return false;
        }
        return Utils.nextInt(10000) < dodge * 10000 / (10000 + dodge);
    }

    @Override
    public void startDie(Entity killer) {
        delayRespawn = getDelayRespawn();
        status = MonsterStatus.DIE;
        clearAllEffect();
        super.startDie(killer);
    }

    @Override
    public void updateEveryFiveSeconds(long now) {

    }

    @Override
    public void updateEveryThirtySeconds(long now) {

    }

    @Override
    public void updateEveryOneMinutes(long now) {

    }

    @Override
    public void updateEveryTenSeconds(long now) {

    }

    @Override
    public void updateEveryOneSeconds(long now) {

    }

    @Override
    public void updateEveryHalfSeconds(long now) {

    }

    @Override
    public void addEffect(Effect effect) {
        super.addEffect(effect);
        zone.service.monsterAddEffect(this, effect);
    }

    @Override
    public boolean isPlayer() {
        return false;
    }

    @Override
    public boolean isMonster() {
        return true;
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

    public boolean isBigMonster() {
        return false;
    }

    public long getDelayRespawn() {
        long delay = 10000;
        int count = zone.getPlayers(Zone.TYPE_PLAYER).size();
        for (int i = 0; i < count; i++) {
            delay -= 1000;
        }
        return Math.max(delay, 5000);
    }

    public long delayAttack() {
        return Math.max(2000 - enemies.size() * 400L, 500);
    }

    public void respawn() {
        numRefresh++;
        status = MonsterStatus.LIVE;
        clearAllEffect();
        enemies.clear();
        levelBoss = MonsterLevelBoss.NORMAL;
        if (level > 5) {
            boolean flag = zone.getMonsters().stream().filter(m -> m.levelBoss == MonsterLevelBoss.THU_LINH || m.levelBoss == MonsterLevelBoss.TINH_ANH).findFirst().orElse(null) != null;
            if (!flag) {
                int r = Utils.nextInt(100);
                int perBoss = 0;
                if (template.id == MonsterName.HEO_RUNG) {
                    perBoss = 10;
                }
                if (r == perBoss) {
                    levelBoss = MonsterLevelBoss.THU_LINH;
                } else if (r < perBoss + 5) {
                    levelBoss = MonsterLevelBoss.TINH_ANH;
                }
            }
        }
        hp = maxHp = getMaxHp();
        isDie = false;
        timeRespawn = System.currentTimeMillis();
        zone.service.monsterRespawn(this);
    }

    public void addEnemy(Player player, long damage) {
        enemies.put(player, enemies.getOrDefault(player, 0L) + damage);
    }

    @Override
    public boolean isDead() {
        return super.isDead() || status == MonsterStatus.DIE;
    }

    public boolean isCanAttack() {
        return !isStun();
    }

    public long getMaxHp() {
        if (zone.map.isManor() || zone.map.isBarrack()) {
            if (levelBoss == MonsterLevelBoss.THU_LINH) {
                return baseHp * 10L;
            }
            if (levelBoss == MonsterLevelBoss.TINH_ANH) {
                return baseHp * 3L;
            }
            return baseHp;
        }
        if (levelBoss == MonsterLevelBoss.THU_LINH) {
            return baseHp * 30L;
        }
        if (levelBoss == MonsterLevelBoss.TINH_ANH) {
            return baseHp * 5L;
        }
        return baseHp;
    }

    @Override
    public long formatDamageInjure(Entity attacker, long damage, boolean isCritical) {
        if (attacker == null) {
            return damage;
        }
        /*if (attacker.isPlayer() || attacker.isDisciple()) {
            Player player = (Player) attacker;
            int armor = Math.max(options[OptionName.GIAM_SAT_THUONG] - player.options[OptionName.XUYEN_GIAP], 0);
            damage -= damage * armor / (armor + 10000);
        }*/
        if (zone.map.isYardrat()) {
            return Math.min(damage, maxHp / 100);
        }
        return damage;
    }

    public void upLevelBoss(MonsterLevelBoss level, boolean isAutoRefresh) {
        levelBoss = level;
        maxHp = getMaxHp();
        hp = maxHp;
        this.isAutoRefresh = isAutoRefresh;
    }
}
