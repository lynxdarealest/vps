package com.beemobi.rongthanonline.effect;

import com.beemobi.rongthanonline.entity.Entity;
import com.beemobi.rongthanonline.entity.player.Player;
import com.beemobi.rongthanonline.server.Server;
import org.apache.log4j.Logger;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Effect {
    private static final Logger logger = Logger.getLogger(Effect.class);
    public EffectTemplate template;
    public long time;
    public int param;
    public int paramOption;
    public long delay;
    public Entity entity;
    public long startTime;
    public long endTime;
    public long updateTime;
    public Lock lock = new ReentrantLock();

    public Effect() {

    }

    public Effect(Entity entity, int templateId, long time) {
        this(entity, templateId, time, 0, 0);
    }

    public Effect(Entity entity, int templateId, long time, long delay, int param) {
        this.entity = entity;
        template = EffectManager.getInstance().effectTemplates.get(templateId);
        long now = System.currentTimeMillis();
        startTime = now;
        endTime = now + time;
        this.time = time;
        this.delay = delay;
        this.param = param;
    }

    public Effect(Entity entity, int templateId, long time, long delay, int param, long updateTime) {
        this.entity = entity;
        template = EffectManager.getInstance().effectTemplates.get(templateId);
        long now = System.currentTimeMillis();
        startTime = now;
        endTime = now + time;
        this.time = time;
        this.delay = delay;
        this.param = param;
        this.updateTime = updateTime;
    }

    public Effect clone(Entity entity) {
        Effect effect = new Effect();
        effect.template = template;
        effect.entity = entity;
        effect.time = time;
        effect.delay = delay;
        effect.param = param;
        effect.startTime = startTime;
        effect.endTime = endTime;
        return effect;
    }

    public void update() {
        lock.lock();
        try {
            if (template == null) {
                time = 0;
                return;
            }
            long now = System.currentTimeMillis();
            if (time != 0) {
                if (template.isClearWhenDie && entity.isDead()) {
                    time = 0;
                } else {
                    time = endTime - now;
                }
            }
            try {
                switch (template.id) {
                    case EffectName.HOA_SOCOLA:
                    case EffectName.HOA_DA: {
                        if (time <= 0) {
                            Player player = (Player) entity;
                            player.service.setPart();
                            if (player.zone != null) {
                                player.zone.service.refreshPlayerPart(player);
                            }
                        }
                        return;
                    }

                    case EffectName.BIEN_KHI:
                    case EffectName.HOA_KHONG_LO: {
                        if (time <= 0) {
                            Player player = (Player) entity;
                            player.service.setPart();
                            player.service.setInfo();
                            if (player.zone != null) {
                                player.zone.service.refreshPlayerPart(player);
                                player.zone.service.refreshHp(player);
                            }
                        }
                        return;
                    }

                    case EffectName.KAIOKEN: {
                        if (time > 0) {
                            if (now - updateTime > delay) {
                                updateTime = now;
                                if (entity.hp * 100 / entity.maxHp <= paramOption + 1) {
                                    time = 0;
                                } else {
                                    entity.injure(null, entity.maxHp * paramOption / 100, false, false);
                                }
                            }
                        } else {
                            ((Player) entity).service.setInfo();
                        }
                        return;
                    }

                    case EffectName.TAI_TAO_NANG_LUONG: {
                        if (now - updateTime > delay) {
                            updateTime = now;
                            Player player = (Player) entity;
                            player.recovery(Player.RECOVERY_ALL, param, true);
                        }
                        return;
                    }

                    case EffectName.THUC_AN_0X:
                    case EffectName.THUC_AN_1X:
                    case EffectName.THUC_AN_2X:
                    case EffectName.THUC_AN_3X: {
                        if (now - updateTime > delay) {
                            updateTime = now;
                            if (entity.zone != null && entity.zone.map.isSurvival()) {
                                break;
                            }
                            Player player = (Player) entity;
                            player.recovery(Player.RECOVERY_ALL, param);
                        }
                        return;
                    }

                    case EffectName.TU_DONG_LUYEN_TAP: {
                        if (time <= 0) {
                            Player player = (Player) entity;
                            if (player.isAutoPlay) {
                                player.setIsAutoPlay(false);
                            }
                        }
                        return;
                    }

                    case EffectName.KEO_DAU_LAU_SUC_DANH:
                    case EffectName.KEO_DAU_LAU_NE_DON:
                    case EffectName.KEO_DAU_LAU_HP_KI:
                    case EffectName.TANG_PHAN_TRAM_HP_KI_DUA_NAU:
                    case EffectName.TANG_PHAN_TRAM_SUC_DANH_DUA_VANG:
                    case 20:
                    case 21:
                    case 29:
                    case 31:
                    case 32:
                    case 33:
                    case 34:
                    case 37:
                    case 38:
                    case 39:
                    case 40:
                    case 41:
                    case 43: {
                        // keo do + vang
                        if (time <= 0) {
                            Player player = (Player) entity;
                            player.service.setInfo();
                        }
                        break;
                    }
                    case 22: {
                        // mo ghsm
                        if (time <= 0) {
                            Player player = (Player) entity;
                            player.limitLevel++;
                            player.addInfo(Player.INFO_YELLOW, String.format("Giới hạn đã được nâng lên cấp %s", player.limitLevel));
                        }
                        break;
                    }
                    case 24:
                    case 25:
                    case 26:
                    case 27:
                    case 28: {
                        // up ki nang
                        /*if (time <= 0) {
                            Player player = (Player) entity;
                            int index = this.template.id - 24;
                            Skill skill = _me.skills.get(index);
                            if (skill != null) {
                                player.up(skill);
                                _me.service.addInfo(1, skill.getName() + " đã được cường hóa lên cấp " + skill.levelUpgrade);
                            }
                        }*/
                        break;
                    }

                    case EffectName.QUA_TRUNG_DE_TU: {
                        if (time <= 0 && !Server.getInstance().isInterServer()) {
                            Player player = (Player) entity;
                            if (player.disciple != null) {
                                player.deleteDisciple();
                            }
                            player.createDisciple(-1, param);
                        }
                        break;
                    }

                    case EffectName.HOP_THE_NAMEK:
                    case EffectName.HOP_THE_TRAI_DAT_VA_SAYAIN: {
                        if (time <= 0) {
                            Player player = (Player) entity;
                            if (player.isFusion()) {
                                player.fusion(0);
                                player.typeFusion = 0;
                                player.lastTimeFusion = System.currentTimeMillis();
                                player.addEffect(new Effect(player, EffectName.DELAY_HOP_THE, 600000));
                                player.service.setPart();
                                player.service.setInfo();
                            }
                        }
                        break;
                    }

                    case EffectName.THIEU_DOT: {
                        if (now - updateTime > delay) {
                            updateTime = now;
                            entity.injure(null, entity.maxHp * param / 100, false, false);
                        }
                        break;
                    }
                }
            } catch (Exception ex) {
                time = 0;
                logger.error("update", ex);
            }
        } finally {
            lock.unlock();
        }

    }
}
