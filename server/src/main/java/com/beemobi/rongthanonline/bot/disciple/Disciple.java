package com.beemobi.rongthanonline.bot.disciple;

import com.beemobi.rongthanonline.bot.disciple.json.DiscipleBaseInfo;
import com.beemobi.rongthanonline.bot.disciple.json.DiscipleMapInfo;
import com.beemobi.rongthanonline.bot.disciple.json.DisciplePowerInfo;
import com.beemobi.rongthanonline.data.DiscipleData;
import com.beemobi.rongthanonline.effect.Effect;
import com.beemobi.rongthanonline.effect.EffectName;
import com.beemobi.rongthanonline.entity.Entity;
import com.beemobi.rongthanonline.entity.monster.Monster;
import com.beemobi.rongthanonline.entity.player.Player;
import com.beemobi.rongthanonline.entity.player.json.*;
import com.beemobi.rongthanonline.item.Item;
import com.beemobi.rongthanonline.item.ItemType;
import com.beemobi.rongthanonline.model.Level;
import com.beemobi.rongthanonline.network.MessageDiscipleInfoName;
import com.beemobi.rongthanonline.repository.GameRepository;
import com.beemobi.rongthanonline.server.Server;
import com.beemobi.rongthanonline.server.ServerRandom;
import com.beemobi.rongthanonline.service.Service;
import com.beemobi.rongthanonline.skill.Skill;
import com.beemobi.rongthanonline.skill.SkillManager;
import com.beemobi.rongthanonline.skill.SkillName;
import com.beemobi.rongthanonline.util.Utils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Disciple extends Player {
    private static final Logger logger = Logger.getLogger(Disciple.class);
    public final Player master;
    private final DiscipleData data;
    public DiscipleStatus status;
    public ArrayList<Player> enemies;
    public transient int stamina;
    public int typeDisciple;

    public Disciple(DiscipleData data, Player master) {
        this.data = data;
        this.master = master;
        service = new Service(this);
        enemies = new ArrayList<>();
        init();
    }

    @Override
    public boolean isDisciple() {
        return true;
    }

    @Override
    public boolean isPlayer() {
        return false;
    }

    public void chatForMatter(String content) {
        if (zone != null) {
            master.service.chatMe(this, content);
        }
    }

    public void move() {
        if (isDead() || zone == null) {
            return;
        }
        if (isLockMove()) {
            return;
        }
        if (status == DiscipleStatus.FOLLOW) {
            followMaster();
        }
    }

    public void followMaster() {
        if (zone == null || zone != master.zone) {
            return;
        }
        int x = master.x + (125 + Utils.nextInt(25)) * (Utils.nextInt(2) == 0 ? 1 : -1);
        int y = master.y;
        moveTo(x, y);
    }

    public void moveTo(int x, int y) {
        if (isDead() || master.isDead()) {
            return;
        }
        if (isLockMove()) {
            return;
        }
        this.x = x;
        this.y = y;
        if (zone != null) {
            zone.service.move(this);
        }
    }

    @Override
    public void update() {
        super.update();
        if (master.isDead() || isDead() || isStun() || zone == null) {
            return;
        }
        if (status == DiscipleStatus.ATTACK || status == DiscipleStatus.PROTECT) {
            mySkill = skills.get(0);
            int distance = 150;
            if (status == DiscipleStatus.ATTACK) {
                distance = 600;
            } else {
                for (Skill skill : skills) {
                    if (skill.template.id >= 24 && skill.template.id <= 26) {
                        distance = 400;
                        break;
                    }
                }
            }
            if (focus != null && (focus.isDead() || focus.zone != this.zone || Utils.getDistance(x, y, focus.x, focus.y) > distance)) {
                focus = null;
            }
            if (focus == null) {
                int dis = Utils.getDistance(x, y, master.x, master.y);
                if (dis > 200) {
                    followMaster();
                }
                focus = findEnemy(distance);
                if (focus == null) {
                    return;
                }
            }
            int dis = Utils.getDistance(x, y, master.x, master.y);
            if (dis > 300) {
                followMaster();
            }
            for (Skill skill : skills) {
                switch (skill.template.id) {
                    case SkillName.THAI_DUONG_HA_SAN: {
                        if (isCanUseSkill(skill)) {
                            chat("Thái dương hạ san");
                            useSkill(skill, null);
                            return;
                        }
                        break;
                    }

                    case SkillName.TAI_TAO_NANG_LUONG: {
                        if (isCanUseSkill(skill)) {
                            chat("Phục hồi năng lượng");
                            useSkill(skill, null);
                            return;
                        }
                        break;
                    }

                    default:
                        break;
                }
            }
            attack(focus);
        }
    }

    public boolean isCanUseSkill(Skill skill) {
        if (skill.level <= 0) {
            return false;
        }
        long manaUse = getManaUseSkill(skill);
        if (this.mp < manaUse) {
            return false;
        }
        return System.currentTimeMillis() >= skill.timeCanUse;
    }

    public Entity findEnemy(int distance) {
        Entity target = null;
        if (!enemies.isEmpty()) {
            int num = distance;
            for (Player enemy : enemies) {
                if (!enemy.isDead() && enemy.zone == this.zone && isCanAttackEntity(enemy)) {
                    int dis = Utils.getDistance(x, y, enemy.x, enemy.y);
                    if (dis < num) {
                        target = enemy;
                        num = dis;
                    }
                }
            }
            if (target != null) {
                return target;
            }
        }
        int num = distance;
        List<Monster> monsters = zone.getMonsters();
        for (Monster monster : monsters) {
            if (!monster.isDead() && monster.zone == this.zone && !monster.isPet()) {
                int dis = Utils.getDistance(x, y, monster.x, monster.y);
                if (dis < num) {
                    target = monster;
                    num = dis;
                }
            }
        }
        return target;
    }

    @Override
    public void updateEveryOneSeconds(long now) {
        if (master == null || master.zone == null) {
            if (zone != null) {
                zone.leave(this);
            }
            return;
        }
        if (isDead() && now - timeDie >= 30000) {
            followMaster();
            hp = maxHp;
            mp = maxMp;
            isDie = false;
            if (zone != null) {
                zone.leave(this);
            }
            if (status != DiscipleStatus.GO_HOME && master.zone != null) {
                master.zone.enter(this);
                chatForMatter("Sư phụ ơi con đây nè");
            }
        }
        super.updateEveryOneSeconds(now);
    }

    @Override
    public void updateEveryFiveSeconds(long now) {
        super.updateEveryFiveSeconds(now);
        if (status == DiscipleStatus.ATTACK || status == DiscipleStatus.PROTECT) {
            if (stamina <= 10 && master.isHaveEffect(EffectName.TU_DONG_LUYEN_TAP, EffectName.BUA_DE_TU)) {
                master.doUseBean();
            }
            if (stamina == 0) {
                chatForMatter("Sư phụ ơi cho con đậu thần");
            } else if (now - master.lastTimeAttack >= 600000 && !master.isHaveEffect(EffectName.BUA_DE_TU)) {
                chatForMatter("Sao sư phụ không đánh đi?");
            }
        }
    }

    @Override
    public long formatDamageInjure(Entity attacker, long damage, boolean isCritical) {
        if (attacker instanceof Monster && master.isHaveEffect(EffectName.BUA_DE_TU)) {
            damage /= 2;
        }
        return super.formatDamageInjure(attacker, damage, isCritical);
    }

    @Override
    public long getDamageWhenAttack(Entity focus, boolean isCritical) {
        long dmg = super.getDamageWhenAttack(focus, isCritical);
        if (master.isHaveEffect(EffectName.BUA_DE_TU)) {
            dmg *= 2;
        }
        return dmg;
    }

    @Override
    public void updateEveryThirtySeconds(long now) {
        super.updateEveryThirtySeconds(now);
        move();
    }

    public void attack(Entity entity) {
        if (entity == null || entity.isDead() || isStun() || stamina <= 0) {
            return;
        }
        long now = System.currentTimeMillis();
        if (now - master.lastTimeAttack >= 600000 && !master.isHaveEffect(EffectName.BUA_DE_TU)) {
            return;
        }
        int distance = Utils.getDistance(x, y, entity.x, entity.y);
        mySkill = skills.get(0);
        /*if (skills.size() >= 3 && distance > 150) {
            long manaUse = getManaUseSkill(skills.get(2));
            if (mp >= manaUse) {
                mySkill = skills.get(2);
            }
        }*/
        if (skills.size() >= 3) {
            long manaUse = getManaUseSkill(skills.get(2));
            if (mp >= manaUse) {
                mySkill = skills.get(2);
            }
        }
        if (now - lastTimeAttack < mySkill.getCoolDown()) {
            return;
        }
        long manaUse = getManaUseSkill();
        if (mp < manaUse) {
            return;
        }
        mp -= manaUse;
        lastTimeAttack = now;
        if (distance > mySkill.getDx() * 2) {
            moveTo(entity.x, entity.y);
        }
        stamina--;
        zone.service.useSkill(this);
        if (entity.isMonster()) {
            attackMonster((Monster) entity);
        } else if (isBoss() || isPlayer() || isDisciple()) {
            attackPlayer((Player) entity);
        }
    }

    public int getMaxStamina() {
        return 100 + level * 10;
    }

    @Override
    public void refreshPart() {
        int head;
        int body;
        int mount = -1;
        int medal = -1;
        int bag = -1;
        int aura = -1;
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
        if (typeDisciple == 1) {
            head = 325;
            body = 326;
        }
        List<Item> items = Arrays.stream(itemsBody).toList();
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
            body = item.template.bodyDisciple;
            if (item.template.headDisciple != -1) {
                head = item.template.headDisciple;
            }
        }

        item = items.get(ItemType.TYPE_THU_CUOI);
        if (item != null) {
            mount = item.template.mount;
        }

        if (!master.isHideMark) {
            item = items.get(ItemType.TYPE_AVATAR);
            if (item != null) {
                if (item.template.headDisciple != -1) {
                    head = item.template.headDisciple;
                }
                if (item.template.bodyDisciple != -1) {
                    body = item.template.bodyDisciple;
                }
            }
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
        List<Effect> effectList = getEffects();
        if (isHaveEffect(effectList, EffectName.HOA_DA)) {
            head = 319;
            body = 320;
        } else if (isHaveEffect(effectList, EffectName.HOA_SOCOLA)) {
            head = 327;
            body = 328;
        }
        this.head = head;
        this.body = body;
        this.mount = mount;
        this.medal = medal;
        this.bag = bag;
        this.aura = aura;
    }

    @Override
    public void addEnemy(Player player) {
        if (player == this || player == master) {
            return;
        }
        if (!enemies.contains(player)) {
            enemies.add(player);
            if (zone != null) {
                chat(String.format("Mi làm ta nổi giận rồi %s", player.name));
            }
        }
    }

    @Override
    public void upPower(long exp) {
        if (exp <= 0 || isLockExp()) {
            return;
        }
        ArrayList<Level> levels = Server.getInstance().levels;
        long maxPower = levels.get(level + 1).power;
        if (power >= maxPower) {
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
        if (level < 60) {
            master.upPower(Math.max(exp / 2, 1L));
        }
        master.service.discipleInfo(MessageDiscipleInfoName.POWER_INFO);
        master.service.discipleInfo(MessageDiscipleInfoName.STAMINA);
        if (level < Server.getInstance().levels.size() - 1 && power >= Server.getInstance().levels.get(level + 1).power) {
            level++;
            chatForMatter("Sư phụ ơi, con lên cấp rồi!!!");
        }
        if (level >= 10 && skills.size() == 1) {
            Skill skill = new Skill();
            skill.template = SkillManager.getInstance().skillTemplates.get(ServerRandom.SKILL_DISCIPLE_2.next());
            skill.level = 1;
            skill.upgrade = 0;
            skill.point = 0;
            skills.add(skill);
            master.service.discipleInfo(MessageDiscipleInfoName.SKILL_INFO);
        }
        if (level >= 20 && skills.size() == 2) {
            Skill skill = new Skill();
            skill.template = SkillManager.getInstance().skillTemplates.get(ServerRandom.SKILL_DISCIPLE_3.next());
            skill.level = 1;
            skill.upgrade = 0;
            skill.point = 0;
            skills.add(skill);
            master.service.discipleInfo(MessageDiscipleInfoName.SKILL_INFO);
        }
        if (level >= 30 && skills.size() == 3) {
            Skill skill = new Skill();
            skill.template = SkillManager.getInstance().skillTemplates.get(ServerRandom.SKILL_DISCIPLE_4.next());
            skill.level = 1;
            skill.upgrade = 0;
            skill.point = 0;
            skills.add(skill);
            master.service.discipleInfo(MessageDiscipleInfoName.SKILL_INFO);
        }
    }

    public String getStatusInfo() {
        if (status == DiscipleStatus.FOLLOW) {
            return "đi theo";
        } else if (status == DiscipleStatus.ATTACK) {
            return "tấn công";
        } else if (status == DiscipleStatus.PROTECT) {
            return "bảo vệ";
        } else {
            return "về nhà";
        }
    }

    @Override
    public void saveData(boolean isOffline) {
        if (Server.getInstance().isInterServer()) {
            return;
        }
        try {
            DiscipleBaseInfo baseInfo = new DiscipleBaseInfo(baseDamage, baseHp, baseMp, baseConstitution);
            DisciplePowerInfo powerInfo = new DisciplePowerInfo(power, potential, level, limitLevel, updatePowerTime);
            DiscipleMapInfo mapInfo = new DiscipleMapInfo(hp, mp, stamina);
            ArrayList<SkillInfo> skillsInfo = new ArrayList<>();
            for (Skill skill : skills) {
                if (skill.level > 0) {
                    skillsInfo.add(new SkillInfo(skill.template.id, skill.level, skill.upgrade, skill.point, skill.timeCanUse));
                }
            }
            Gson gson = new Gson();
            GameRepository.getInstance().discipleData.saveData(this.id, gson.toJson(baseInfo), gson.toJson(powerInfo),
                    Utils.getJsonArrayItem(this.itemsBody), Utils.getJsonArrayItem(this.itemsOther), gson.toJson(skillsInfo),
                    gson.toJson(mapInfo), status, typeDisciple);
        } catch (Exception ex) {
            logger.error("saveData", ex);
        }
    }

    public void init() {
        id = data.id;
        name = data.name;
        gender = data.gender;
        status = data.status;
        typeDisciple = data.type;
        Gson gson = new Gson();
        try {
            ListItemInfo items = gson.fromJson(data.itemsBody, new TypeToken<ListItemInfo>() {
            }.getType());
            itemsBody = new Item[Math.max(items.size, Player.DEFAULT_BODY)];
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
            itemsBody = new Item[Player.DEFAULT_BODY];
        }
        try {
            ListItemInfo items = gson.fromJson(data.itemsOther, new TypeToken<ListItemInfo>() {
            }.getType());
            itemsOther = new Item[Math.max(items.size, Player.DEFAULT_BODY)];
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
            itemsOther = new Item[Player.DEFAULT_BODY];
        }
        DiscipleBaseInfo baseInfo = gson.fromJson(data.baseInfo, new TypeToken<DiscipleBaseInfo>() {
        }.getType());
        baseDamage = baseInfo.damage;
        baseHp = baseInfo.hp;
        baseMp = baseInfo.mp;
        baseConstitution = baseInfo.constitution;
        DiscipleMapInfo mapInfo = gson.fromJson(data.mapInfo, new TypeToken<DiscipleMapInfo>() {
        }.getType());
        hp = Math.max(mapInfo.hp, 1L);
        mp = mapInfo.mp;
        stamina = mapInfo.stamina;
        DisciplePowerInfo powerInfo = gson.fromJson(data.powerInfo, new TypeToken<DisciplePowerInfo>() {
        }.getType());
        power = powerInfo.power;
        potential = powerInfo.potential;
        level = powerInfo.level;
        limitLevel = powerInfo.limitLevel;
        if (limitLevel > Server.MAX_LEVEL) {
            limitLevel = Server.MAX_LEVEL;
        }
        updatePowerTime = powerInfo.updatePowerTime;
        skills = new ArrayList<>();
        JSONArray skillsInfo = new JSONArray(data.skillsInfo);
        for (int i = 0; i < skillsInfo.length(); i++) {
            JSONObject object = skillsInfo.getJSONObject(i);
            Skill skill = new Skill();
            skill.template = SkillManager.getInstance().skillTemplates.get(object.getInt("id"));
            skill.level = object.getInt("level");
            skill.upgrade = object.getInt("upgrade");
            skill.timeCanUse = object.getLong("time_can_use");
            skill.point = object.getInt("point");
            skills.add(skill);
        }
        timeLogin = System.currentTimeMillis();
    }
}
