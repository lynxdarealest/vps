package com.beemobi.rongthanonline.entity;

import com.beemobi.rongthanonline.bot.disciple.Disciple;
import com.beemobi.rongthanonline.effect.EffectName;
import com.beemobi.rongthanonline.entity.monster.pet.Pet;
import com.beemobi.rongthanonline.entity.player.Player;
import com.beemobi.rongthanonline.event.Event;
import com.beemobi.rongthanonline.item.ItemManager;
import com.beemobi.rongthanonline.item.ItemMap;
import com.beemobi.rongthanonline.map.Zone;
import com.beemobi.rongthanonline.effect.Effect;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public abstract class Entity {
    private static final Logger logger = Logger.getLogger(Entity.class);
    public static final byte UPDATE_ONE_SECONDS = 0;
    public static final byte UPDATE_THIRTY_SECONDS = 1;
    public static final byte UPDATE_ONE_MINUTES = 2;
    public static final byte UPDATE_FIVE_SECONDS = 3;
    public static final byte UPDATE_HALF_SECONDS = 4;
    public static final byte UPDATE_TEN_SECONDS = 5;

    public int id;
    public transient long maxHp;
    public transient long hp;
    public transient long maxMp;
    public transient long mp;
    public transient long damage;
    public transient Zone zone;
    public transient int x;
    public transient int y;
    public transient boolean isDie;
    public long timeDie;
    public int level;
    public transient long lastTimeAttack;
    public transient int[] options, optionZones;
    public long[] lastUpdates = new long[6];
    protected ArrayList<Effect> effects = new ArrayList<>();
    public ReadWriteLock lockEffects = new ReentrantReadWriteLock();
    public Lock lock = new ReentrantLock();

    public Entity() {
        options = new int[ItemManager.getInstance().itemOptionTemplates.size()];
        optionZones = new int[ItemManager.getInstance().itemOptionTemplates.size()];
    }

    public void updateEveryTime() {
        long now = System.currentTimeMillis();
        if (now - lastUpdates[UPDATE_HALF_SECONDS] >= 500) {// update 500ms
            lastUpdates[UPDATE_HALF_SECONDS] = now;
            updateEveryHalfSeconds(now);
        }
        if (now - lastUpdates[UPDATE_ONE_SECONDS] >= 1000) {// update 1s
            lastUpdates[UPDATE_ONE_SECONDS] = now;
            updateEveryOneSeconds(now);
        }
        if (now - lastUpdates[UPDATE_FIVE_SECONDS] >= 5000) {// update 5s
            lastUpdates[UPDATE_FIVE_SECONDS] = now;
            updateEveryFiveSeconds(now);
        }
        if (now - lastUpdates[UPDATE_THIRTY_SECONDS] >= 30000) {// update 30s
            lastUpdates[UPDATE_THIRTY_SECONDS] = now;
            updateEveryThirtySeconds(now);
        }
        if (now - lastUpdates[UPDATE_ONE_MINUTES] >= 60000) {// update 60s
            lastUpdates[UPDATE_ONE_MINUTES] = now;
            updateEveryOneMinutes(now);
        }
        if (now - lastUpdates[UPDATE_TEN_SECONDS] >= 10000) {// update 10s
            lastUpdates[UPDATE_TEN_SECONDS] = now;
            updateEveryTenSeconds(now);
        }
    }

    public abstract void updateEveryFiveSeconds(long now);

    public abstract void updateEveryThirtySeconds(long now);

    public abstract void updateEveryOneMinutes(long now);

    public abstract void updateEveryTenSeconds(long now);

    public abstract void updateEveryOneSeconds(long now);

    public abstract void updateEveryHalfSeconds(long now);

    public List<Effect> getEffects() {
        List<Effect> effects = new ArrayList<>();
        lockEffects.readLock().lock();
        try {
            for (Effect effect : this.effects) {
                if (effect.time > 0) {
                    effects.add(effect);
                }
            }
        } finally {
            lockEffects.readLock().unlock();
        }
        return effects;
    }

    public Effect findEffectByTemplateId(int id) {
        lockEffects.readLock().lock();
        try {
            return effects.stream().filter(e -> e.template.id == id && e.time > 0).findFirst().orElse(null);
        } finally {
            lockEffects.readLock().unlock();
        }
    }

    public Effect findEffectByTemplateId(List<Effect> effects, int id) {
        return effects.stream().filter(e -> e.template.id == id && e.time > 0).findFirst().orElse(null);
    }

    public boolean isHaveEffect(int... id) {
        lockEffects.readLock().lock();
        try {
            for (Effect effect : effects) {
                if (effect.time > 0) {
                    for (int i : id) {
                        if (i == effect.template.id) {
                            return true;
                        }
                    }
                }
            }
            return false;
        } finally {
            lockEffects.readLock().unlock();
        }
    }

    public boolean isHaveEffect(List<Effect> effects, int... id) {
        for (Effect effect : effects) {
            if (effect.time > 0) {
                for (int i : id) {
                    if (i == effect.template.id) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public int getEffectParam(int id) {
        lockEffects.readLock().lock();
        try {
            Effect effect = effects.stream().filter(e -> e.template.id == id && e.time > 0).findFirst().orElse(null);
            return effect != null ? effect.param : 0;
        } finally {
            lockEffects.readLock().unlock();
        }
    }

    public void addEffect(Effect effect) {
        lockEffects.writeLock().lock();
        try {
            effects.add(effect);
        } finally {
            lockEffects.writeLock().unlock();
        }
    }

    public void removeEffect(Effect effect) {
        lockEffects.writeLock().lock();
        try {
            effects.remove(effect);
        } finally {
            lockEffects.writeLock().unlock();
        }
    }

    public boolean removeEffectById(int id) {
        lockEffects.writeLock().lock();
        try {
            return effects.removeIf(e -> e.template.id == id);
        } finally {
            lockEffects.writeLock().unlock();
        }
    }

    public void clearAllEffect() {
        lockEffects.writeLock().lock();
        try {
            effects.clear();
        } finally {
            lockEffects.writeLock().unlock();
        }
    }

    public void updateEffect() {
        lockEffects.writeLock().lock();
        try {
            for (int i = 0; i < effects.size(); i++) {
                try {
                    Effect effect = effects.get(i);
                    if (effect.time <= 0) {
                        effects.remove(i);
                        i--;
                        if (isPlayer()) {
                            ((Player) this).service.removeEffect(effect);
                        }
                    } else {
                        effect.update();
                    }
                } catch (Exception e) {
                    logger.error("updateEffect", e);
                }
            }
        } finally {
            lockEffects.writeLock().unlock();
        }
    }

    public boolean isStun() {
        lockEffects.readLock().lock();
        try {
            for (Effect effect : effects) {
                if (effect.time >= 0) {
                    int id = effect.template.id;
                    if (id == EffectName.CHOANG_THAI_DUONG_HA_SAN
                            || id == EffectName.SOCOLA
                            || id == EffectName.CHOANG_MA_PHONG_BA
                            || id == EffectName.CHOANG_HUT_DAY
                            || id == EffectName.DONG_BANG
                            || id == EffectName.THOI_MIEN
                            || id == EffectName.HOA_DA) {
                        return true;
                    }
                }
            }
        } finally {
            lockEffects.readLock().unlock();
        }
        return false;
    }

    public boolean isDead() {
        return hp <= 0 || isDie;
    }

    public abstract void update();

    public abstract List<ItemMap> throwItem(Entity killer);

    public abstract void injure(Entity attacker, long hpInjure, boolean isCritical, boolean isStrikeBack);

    public abstract boolean isDodge(Entity attacker, int accurate);

    public void startDie(Entity killer) {
        timeDie = System.currentTimeMillis();
        try {
            if (zone != null && (isPlayer() || (killer != null && (isBoss() || isMonster())))) {
                List<ItemMap> itemMaps = throwItem(killer);
                if (itemMaps == null) {
                    itemMaps = new ArrayList<>();
                }
                if (Event.isEvent() && killer != null && (killer.isPlayer() || killer.isDisciple() || killer.isPet())) {
                    Player player;
                    if (killer.isDisciple()) {
                        player = ((Disciple) killer).master;
                    } else if (killer.isPet()) {
                        player = ((Pet) killer).master;
                    } else {
                        player = (Player) killer;
                    }
                    List<ItemMap> itemMapList = Event.event.throwItem(player, this);
                    if (itemMapList != null && !itemMapList.isEmpty()) {
                        itemMaps.addAll(itemMapList);
                    }
                }
                if (!itemMaps.isEmpty()) {
                    zone.addItemMap(itemMaps, this.x);
                    if (isMonster() && killer != null && (killer.isPlayer() || killer.isDisciple() || killer.isPet())) {
                        Player player;
                        if (killer.isDisciple()) {
                            player = ((Disciple) killer).master;
                        } else if (killer.isPet()) {
                            player = ((Pet) killer).master;
                        } else {
                            player = (Player) killer;
                        }
                        if ((player.isAutoPlay || player.isHaveEffect(EffectName.BUA_THU_HUT)) && !player.isInForgottenCity() && !player.isBagFull()) {
                            for (ItemMap itemMap : itemMaps) {
                                player.pickItem(itemMap);
                            }
                        }
                    }
                }
            }
        } catch (Exception ex) {
            logger.error("startDie", ex);
        }
    }

    public abstract boolean isPlayer();

    public abstract boolean isMonster();

    public abstract boolean isBoss();

    public abstract boolean isDisciple();

    public abstract boolean isPet();

    public boolean isEscort() {
        return false;
    }

    public abstract long formatDamageInjure(Entity attacker, long damage, boolean isCritical);
}
