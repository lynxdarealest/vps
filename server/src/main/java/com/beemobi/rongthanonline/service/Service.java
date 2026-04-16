package com.beemobi.rongthanonline.service;

import com.beemobi.rongthanonline.achievement.Achievement;
import com.beemobi.rongthanonline.bot.disciple.Disciple;
import com.beemobi.rongthanonline.bot.tournament.TournamentAthlete;
import com.beemobi.rongthanonline.clan.Clan;
import com.beemobi.rongthanonline.clan.ClanMember;
import com.beemobi.rongthanonline.command.Command;
import com.beemobi.rongthanonline.common.KeyValue;
import com.beemobi.rongthanonline.common.RandomCollection;
import com.beemobi.rongthanonline.dragon.Dragon;
import com.beemobi.rongthanonline.effect.EffectImage;
import com.beemobi.rongthanonline.effect.EffectManager;
import com.beemobi.rongthanonline.entity.monster.big.BigMonster;
import com.beemobi.rongthanonline.entity.monster.Monster;
import com.beemobi.rongthanonline.entity.monster.MonsterManager;
import com.beemobi.rongthanonline.entity.monster.MonsterTemplate;
import com.beemobi.rongthanonline.entity.monster.pet.Pet;
import com.beemobi.rongthanonline.entity.player.action.TradeAction;
import com.beemobi.rongthanonline.mission.IMission;
import com.beemobi.rongthanonline.map.Map;
import com.beemobi.rongthanonline.map.expansion.survival.ZoneSurvival;
import com.beemobi.rongthanonline.model.*;
import com.beemobi.rongthanonline.network.*;
import com.beemobi.rongthanonline.npc.Npc;
import com.beemobi.rongthanonline.npc.NpcManager;
import com.beemobi.rongthanonline.npc.NpcTemplate;
import com.beemobi.rongthanonline.entity.player.Player;
import com.beemobi.rongthanonline.entity.player.PlayerManager;
import com.beemobi.rongthanonline.item.*;
import com.beemobi.rongthanonline.map.MapManager;
import com.beemobi.rongthanonline.map.MapTemplate;
import com.beemobi.rongthanonline.map.Zone;
import com.beemobi.rongthanonline.effect.Effect;
import com.beemobi.rongthanonline.effect.EffectTemplate;
import com.beemobi.rongthanonline.model.input.TextField;
import com.beemobi.rongthanonline.model.waypoint.WayPoint;
import com.beemobi.rongthanonline.server.Server;
import com.beemobi.rongthanonline.skill.Skill;
import com.beemobi.rongthanonline.skill.SkillManager;
import com.beemobi.rongthanonline.skill.SkillOption;
import com.beemobi.rongthanonline.task.Task;
import com.beemobi.rongthanonline.task.TaskStepTemplate;
import com.beemobi.rongthanonline.task.TaskTemplate;
import com.beemobi.rongthanonline.team.Team;
import com.beemobi.rongthanonline.team.TeamMember;
import com.beemobi.rongthanonline.team.TeamStatus;
import com.beemobi.rongthanonline.util.Utils;
import org.apache.log4j.Logger;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class Service {
    private static final Logger logger = Logger.getLogger(Service.class);
    private Session session;
    private Player player;

    public Service(Session session) {
        this.session = session;
    }

    public Service(Player player) {
        this.player = player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public Message messageInfo(int command) throws IOException {
        Message ms = new Message(MessageName.PLAYER_INFO);
        ms.writer().writeByte(command);
        return ms;
    }

    public Message messageDisciple(int command) throws IOException {
        Message ms = new Message(MessageName.DISCIPLE_INFO);
        ms.writer().writeByte(command);
        return ms;
    }

    public Message messagePet(int command) throws IOException {
        Message ms = new Message(MessageName.PET_INFO);
        ms.writer().writeByte(command);
        return ms;
    }

    public void luckyBox(List<Item> items) {
        try {
            Message message = new Message(MessageName.TAB_LUCKY);
            message.writer().writeByte(1);
            message.writer().writeByte(items.size());
            for (Item itm : items) {
                itm.write(message);
            }
            session.sendMessage(message);
        } catch (Exception ex) {
            logger.error("luckyBox", ex);
        }
    }

    public void startDialogOk(String text) {
        try {
            Message msg = new Message(MessageName.DIALOG_OK);
            msg.writer().writeUTF(text);
            session.sendMessage(msg);
            msg.cleanup();
        } catch (Exception ex) {
            logger.error("startDialogOk", ex);
        }
    }

    public void startCreatePlayerScreen() {
        try {
            Message msg = new Message(MessageName.START_CREATE_PLAYER_SCREEN);
            session.sendMessage(msg);
            msg.cleanup();
        } catch (Exception ex) {
            logger.error("startCreatePlayerScreen", ex);
        }
    }

    public void versionSource() {
        try {
            Message msg = new Message(MessageName.VERSION_SOURCE);
            msg.writer().writeUTF(Server.VERSION);
            session.sendMessage(msg);
            msg.cleanup();
        } catch (Exception ex) {
            logger.error("versionSource", ex);
        }
    }

    public void sendVersionData() {
        try {
            Message message = new Message(MessageName.UPDATE_DATA);
            message.writer().writeByte(-1);
            message.writer().writeByte(Server.getInstance().versionIcon);
            for (int version : Server.getInstance().VERSION_DATA) {
                message.writer().writeByte(version);
            }
            session.sendMessage(message);
            message.cleanup();
        } catch (Exception ex) {
            logger.error("sendVersionData", ex);
        }
    }

    public void sendMonsterTemplates() {
        try {
            Message message = new Message(MessageName.UPDATE_DATA);
            message.writer().writeByte(4);
            message.writer().writeByte(Server.getInstance().VERSION_DATA[Server.MONSTER_TEMPLATE_VERSION]);
            message.writer().writeShort(Server.getInstance().monsterDartTemplates.size());
            for (Integer id : Server.getInstance().monsterDartTemplates.keySet()) {
                message.writer().writeShort(id);
                MonsterDartTemplate template = Server.getInstance().monsterDartTemplates.get(id);
                message.writer().writeBoolean(template.isMeteorite);
                message.writer().writeByte(template.light.icon.length);
                for (int icon : template.light.icon) {
                    message.writer().writeShort(icon);
                }
                message.writer().writeShort(template.light.dx);
                message.writer().writeShort(template.light.dy);
                message.writer().writeShort(template.light.delay);
                message.writer().writeByte(template.bullet.icon.length);
                for (int icon : template.bullet.icon) {
                    message.writer().writeShort(icon);
                }
                message.writer().writeShort(template.bullet.dx);
                message.writer().writeShort(template.bullet.dy);
                message.writer().writeShort(template.bullet.delay);
                message.writer().writeByte(template.explode.icon.length);
                for (int icon : template.explode.icon) {
                    message.writer().writeShort(icon);
                }
                message.writer().writeShort(template.explode.dx);
                message.writer().writeShort(template.explode.dy);
                message.writer().writeShort(template.explode.delay);
            }
            message.writer().writeShort(MonsterManager.getInstance().monsterTemplates.size());
            for (MonsterTemplate template : MonsterManager.getInstance().monsterTemplates.values()) {
                message.writer().writeShort(template.id);
                message.writer().writeUTF(template.name);
                message.writer().writeShort(template.rangeMove);
                message.writer().writeByte(template.speed);
                message.writer().writeByte(template.typeMove.ordinal());
                message.writer().writeByte(template.dartId);
                message.writer().writeByte(template.iconsMove.size());
                for (int iconId : template.iconsMove) {
                    message.writer().writeShort(iconId);
                }
                message.writer().writeShort(template.iconsInjure);
                message.writer().writeShort(template.iconsAttack);
                message.writer().writeShort(template.w);
                message.writer().writeShort(template.h);
                message.writer().writeByte(template.dx);
                message.writer().writeByte(template.dy);
            }
            session.sendMessage(message);
            message.cleanup();
        } catch (Exception ex) {
            logger.error("sendMonsterTemplates", ex);
        }
    }

    public void sendFrames() {
        try {
            Message message = new Message(MessageName.UPDATE_DATA);
            message.writer().writeByte(7);
            message.writer().writeByte(Server.getInstance().VERSION_DATA[Server.FRAME_VERSION]);
            message.writer().writeShort(Server.getInstance().frames.size());
            for (Integer id : Server.getInstance().frames.keySet()) {
                Frame frame = Server.getInstance().frames.get(id);
                message.writer().writeShort(id);
                message.writer().writeShort(frame.hpBar);
                message.writer().writeShort(frame.chat);
                message.writer().writeByte(frame.dead.length);
                for (int iconId : frame.dead) {
                    message.writer().writeShort(iconId);
                }
                message.writer().writeByte(frame.stand.length);
                for (int iconId : frame.stand) {
                    message.writer().writeShort(iconId);
                }
                message.writer().writeByte(frame.run.length);
                for (int iconId : frame.run) {
                    message.writer().writeShort(iconId);
                }
                message.writer().writeShort(frame.fly);
                message.writer().writeShort(frame.jump);
                message.writer().writeShort(frame.fall);
                message.writer().writeShort(frame.injure);
                message.writer().writeByte(frame.action.size());
                for (Integer action : frame.action.keySet()) {
                    message.writer().writeByte(action);
                    message.writer().writeShort(frame.action.get(action));
                }
                message.writer().writeShort(frame.dx);
                message.writer().writeShort(frame.dy);
                message.writer().writeShort(frame.width);
                message.writer().writeShort(frame.height);
            }
            session.sendMessage(message);
            message.cleanup();
        } catch (Exception ex) {
            logger.error("sendFrames", ex);
        }
    }

    public void sendItemTemplates() {
        try {
            Message message = new Message(MessageName.UPDATE_DATA);
            message.writer().writeByte(0);
            message.writer().writeByte(Server.getInstance().VERSION_DATA[Server.ITEM_TEMPLATE_VERSION]);
            message.writer().writeShort(ItemManager.getInstance().itemTemplates.size());
            for (ItemTemplate template : ItemManager.getInstance().itemTemplates.values()) {
                message.writer().writeShort(template.id);
                message.writer().writeUTF(template.name);
                message.writer().writeUTF(template.description);
                message.writer().writeByte(template.gender);
                message.writer().writeByte(template.type);
                message.writer().writeShort(template.iconId);
                message.writer().writeBoolean(template.isUpToUp);
                message.writer().writeShort(template.levelRequire);
                message.writer().writeBoolean(template.isMaster);
                message.writer().writeBoolean(template.isDisciple);
                message.writer().writeBoolean(template.isPet);
            }
            session.sendMessage(message);
            message.cleanup();
        } catch (Exception ex) {
            logger.error("sendItemTemplates", ex);
        }
    }

    public void sendItemOptionTemplates() {
        try {
            Message message = new Message(MessageName.UPDATE_DATA);
            message.writer().writeByte(1);
            message.writer().writeByte(Server.getInstance().VERSION_DATA[Server.ITEM_OPTION_VERSION]);
            message.writer().writeShort(ItemManager.getInstance().itemOptionTemplates.size());
            for (ItemOptionTemplate itemOptionTemplate : ItemManager.getInstance().itemOptionTemplates.values()) {
                message.writer().writeShort(itemOptionTemplate.id);
                message.writer().writeUTF(itemOptionTemplate.name);
                message.writer().writeByte(itemOptionTemplate.type);
            }
            session.sendMessage(message);
            message.cleanup();
        } catch (Exception ex) {
            logger.error("sendItemOptionTemplates", ex);
        }
    }

    public void sendLevel() {
        try {
            Message message = new Message(MessageName.UPDATE_DATA);
            message.writer().writeByte(6);
            message.writer().writeByte(Server.getInstance().VERSION_DATA[Server.LEVEL_VERSION]);
            message.writer().writeShort(Server.getInstance().levels.size());
            for (Level level : Server.getInstance().levels) {
                message.writer().writeShort(level.id);
                message.writer().writeUTF(level.name);
                message.writer().writeLong(level.power);
            }
            session.sendMessage(message);
            message.cleanup();
        } catch (Exception ex) {
            logger.error("sendLevel", ex);
        }
    }

    public void sendNpcTemplates() {
        try {
            Message message = new Message(MessageName.UPDATE_DATA);
            message.writer().writeByte(2);
            message.writer().writeByte(Server.getInstance().VERSION_DATA[Server.NPC_TEMPLATE_VERSION]);
            message.writer().writeShort(NpcManager.getInstance().npcTemplates.size());
            for (NpcTemplate template : NpcManager.getInstance().npcTemplates.values()) {
                message.writer().writeByte(template.id);
                message.writer().writeUTF(template.name);
                message.writer().writeByte(template.icons.size());
                for (int img : template.icons) {
                    message.writer().writeShort(img);
                }
                message.writer().writeShort(template.avatar);
                message.writer().writeByte(template.dx);
                message.writer().writeByte(template.dy);
                message.writer().writeShort(template.w);
                message.writer().writeShort(template.h);
            }
            session.sendMessage(message);
            message.cleanup();
        } catch (Exception ex) {
            logger.error("sendNpcTemplates", ex);
        }
    }

    public void sendEffectTemplates() {
        try {
            Message message = new Message(MessageName.UPDATE_DATA);
            message.writer().writeByte(3);
            message.writer().writeByte(Server.getInstance().VERSION_DATA[Server.EFFECT_TEMPLATE_VERSION]);
            message.writer().writeShort(EffectManager.getInstance().effectImages.size());
            for (EffectImage effectImage : EffectManager.getInstance().effectImages.values()) {
                message.writer().writeShort(effectImage.id);
                message.writer().writeShort(effectImage.dx);
                message.writer().writeShort(effectImage.dy);
                message.writer().writeShort(effectImage.delay);
                message.writer().writeByte(effectImage.icons.length);
                for (int icon : effectImage.icons) {
                    message.writer().writeShort(icon);
                }
            }
            message.writer().writeShort(EffectManager.getInstance().effectTemplates.size());
            for (EffectTemplate template : EffectManager.getInstance().effectTemplates.values()) {
                message.writer().writeShort(template.id);
                if (template.effectImage != null) {
                    message.writer().writeShort(template.effectImage.id);
                } else {
                    message.writer().writeShort(-1);
                }
                message.writer().writeShort(template.iconId);
                message.writer().writeBoolean(template.isClearWhenDie);
                message.writer().writeBoolean(template.isStun);
            }
            session.sendMessage(message);
            message.cleanup();
        } catch (Exception ex) {
            logger.error("sendEffectImage", ex);
        }
    }

    public void sendMedals() {
        try {
            Message message = new Message(MessageName.UPDATE_DATA);
            message.writer().writeByte(5);
            message.writer().writeByte(Server.getInstance().VERSION_DATA[Server.MEDAL_VERSION]);
            message.writer().writeByte(Server.getInstance().medals.size());
            for (Medal medal : Server.getInstance().medals) {
                message.writer().writeByte(medal.id);
                message.writer().writeShort(medal.dx);
                message.writer().writeShort(medal.dy);
                message.writer().writeShort(medal.delay);
                message.writer().writeByte(medal.icons.length);
                for (int img : medal.icons) {
                    message.writer().writeShort(img);
                }
            }
            session.sendMessage(message);
            message.cleanup();
        } catch (Exception ex) {
            logger.error("sendMedals", ex);
        }
    }

    public void sendAuras() {
        try {
            Message message = new Message(MessageName.UPDATE_DATA);
            message.writer().writeByte(11);
            message.writer().writeByte(Server.getInstance().VERSION_DATA[Server.AURA_VERSION]);
            message.writer().writeByte(Server.getInstance().auras.size());
            for (Aura aura : Server.getInstance().auras) {
                message.writer().writeByte(aura.id);
                message.writer().writeShort(aura.dx);
                message.writer().writeShort(aura.dy);
                message.writer().writeShort(aura.delay);
                message.writer().writeByte(aura.icons.length);
                for (int img : aura.icons) {
                    message.writer().writeShort(img);
                }
            }
            session.sendMessage(message);
            message.cleanup();
        } catch (Exception ex) {
            logger.error("sendAuras", ex);
        }
    }

    public void sendMounts() {
        try {
            Message message = new Message(MessageName.UPDATE_DATA);
            message.writer().writeByte(8);
            message.writer().writeByte(Server.getInstance().VERSION_DATA[Server.MOUNT_VERSION]);
            message.writer().writeByte(Server.getInstance().mounts.size());
            for (Mount mount : Server.getInstance().mounts) {
                message.writer().writeByte(mount.id);
                message.writer().writeShort(mount.dx);
                message.writer().writeShort(mount.dy);
                message.writer().writeShort(mount.delay);
                message.writer().writeByte(mount.icons.length);
                for (int img : mount.icons) {
                    message.writer().writeShort(img);
                }
                message.writer().writeByte(mount.layer);
            }
            session.sendMessage(message);
            message.cleanup();
        } catch (Exception ex) {
            logger.error("sendMounts", ex);
        }
    }

    public void sendBags() {
        try {
            Message message = new Message(MessageName.UPDATE_DATA);
            message.writer().writeByte(9);
            message.writer().writeByte(Server.getInstance().VERSION_DATA[Server.BAG_VERSION]);
            message.writer().writeByte(Server.getInstance().bags.size());
            for (Bag bag : Server.getInstance().bags) {
                message.writer().writeByte(bag.id);
                message.writer().writeShort(bag.dxFly);
                message.writer().writeShort(bag.dyFly);
                message.writer().writeShort(bag.delay);
                message.writer().writeByte(bag.icons.length);
                for (int img : bag.icons) {
                    message.writer().writeShort(img);
                }
            }
            for (Bag bag : Server.getInstance().bags) {
                message.writer().writeBoolean(bag.isFly);
            }
            session.sendMessage(message);
            message.cleanup();
        } catch (Exception ex) {
            logger.error("sendBags", ex);
        }
    }

    public void sendSkillPaint() {
        try {
            Message message = new Message(MessageName.UPDATE_DATA);
            message.writer().writeByte(10);
            message.writer().writeByte(Server.getInstance().VERSION_DATA[Server.SKILL_PAIN_VERSION]);
            message.writer().writeShort(Server.getInstance().playerDartTemplates.size());
            for (Integer id : Server.getInstance().playerDartTemplates.keySet()) {
                PlayerDartTemplate template = Server.getInstance().playerDartTemplates.get(id);
                message.writer().writeShort(id);
                message.writer().writeBoolean(template.isTarget);
                message.writer().writeBoolean(template.isLine);
                message.writer().writeByte(template.bullet.icon.length);
                for (int icon : template.bullet.icon) {
                    message.writer().writeShort(icon);
                }
                message.writer().writeShort(template.bullet.dx);
                message.writer().writeShort(template.bullet.dy);
                message.writer().writeShort(template.bullet.delay);
                message.writer().writeByte(template.explode.icon.length);
                for (int icon : template.explode.icon) {
                    message.writer().writeShort(icon);
                }
                message.writer().writeShort(template.explode.dx);
                message.writer().writeShort(template.explode.dy);
                message.writer().writeShort(template.explode.delay);
            }
            message.writer().writeShort(Server.getInstance().skillEffects.size());
            for (Integer id : Server.getInstance().skillEffects.keySet()) {
                SkillEffectInfo effect = Server.getInstance().skillEffects.get(id);
                message.writer().writeShort(id);
                message.writer().writeByte(effect.icons.length);
                for (int icon : effect.icons) {
                    message.writer().writeShort(icon);
                }
                message.writer().writeShort(effect.dx);
                message.writer().writeShort(effect.dy);
            }
            message.writer().writeShort(Server.getInstance().skillPaints.size());
            for (Integer id : Server.getInstance().skillPaints.keySet()) {
                SkillPaint paint = Server.getInstance().skillPaints.get(id);
                message.writer().writeShort(id);
                message.writer().writeShort(paint.dxFly);
                message.writer().writeShort(paint.dyFly);
                message.writer().writeByte(paint.info.length);
                for (SkillPaintInfo info : paint.info) {
                    message.writer().writeShort(info.soundId);
                    message.writer().writeShort(info.dartId);
                    message.writer().writeShort(info.timeOut);
                    message.writer().writeByte(info.action.length);
                    for (int action : info.action) {
                        message.writer().writeShort(action);
                    }
                    message.writer().writeByte(info.effects.size());
                    for (SkillEffect effect : info.effects) {
                        message.writer().writeShort(effect.id);
                        message.writer().writeByte(effect.loop);
                    }
                }
            }
            session.sendMessage(message);
            message.cleanup();
        } catch (Exception ex) {
            logger.error("sendSkillPaint", ex);
        }
    }

    public void setLogin() {
        try {
            Message msg = messageInfo(MessagePlayerInfoName.LOGIN);
            msg.writer().writeInt(player.id);
            msg.writer().writeUTF(player.name);
            msg.writer().writeByte(player.gender);
            msg.writer().writeLong(player.power);
            msg.writer().writeLong(player.potential);
            msg.writer().writeShort(player.level);
            msg.writer().writeShort(player.pointSkill);
            msg.writer().writeShort(player.head);
            msg.writer().writeShort(player.body);
            msg.writer().writeShort(player.mount);
            msg.writer().writeShort(player.bag);
            msg.writer().writeShort(player.medal);
            msg.writer().writeShort(player.aura);
            msg.writer().writeInt(player.baseDamage);
            msg.writer().writeInt(player.baseHp);
            msg.writer().writeInt(player.baseMp);
            msg.writer().writeInt(player.baseConstitution);
            for (byte i = 0; i < 4; i++) {
                msg.writer().writeLong(player.getPotentialToUp(i));
            }
            msg.writer().writeLong(player.maxHp);
            msg.writer().writeLong(player.maxMp);
            msg.writer().writeLong(player.hp);
            msg.writer().writeLong(player.mp);
            msg.writer().writeByte(player.speed);
            msg.writer().writeByte(player.pointPk);
            msg.writer().writeShort(player.pointActive);
            msg.writer().writeByte(player.countBarrack);
            msg.writer().writeUTF(Utils.pointToPercent(player.options[OptionName.NE_DON]));
            msg.writer().writeUTF(Utils.pointToPercent(player.options[OptionName.CHI_MANG]));
            msg.writer().writeUTF(Utils.pointToPercent(player.options[OptionName.GIAM_SAT_THUONG]));
            msg.writer().writeUTF(Utils.pointToPercent(player.options[OptionName.HUT_HP]));
            msg.writer().writeUTF(Utils.pointToPercent(player.options[OptionName.HUT_HP]));
            msg.writer().writeUTF(Utils.pointToPercent(player.options[OptionName.PHAN_SAT_THUONG]));
            msg.writer().writeLong(player.damage);
            msg.writer().writeLong(player.xu);
            msg.writer().writeLong(player.xuKhoa);
            msg.writer().writeInt(player.diamond);
            msg.writer().writeInt(player.ruby);
            msg.writer().writeByte(player.spaceship);
            msg.writer().writeByte(player.skills.size());
            for (Skill skill : player.skills) {
                msg.writer().writeByte(skill.template.id);
                msg.writer().writeByte(skill.template.name.length);
                for (String name : skill.template.name) {
                    msg.writer().writeUTF(name);
                }
                msg.writer().writeByte(skill.template.description.length);
                for (String description : skill.template.description) {
                    msg.writer().writeUTF(description);
                }
                msg.writer().writeByte(skill.template.type.ordinal());
                msg.writer().writeBoolean(skill.template.isProactive);
                msg.writer().writeByte(skill.template.iconId.length);
                for (int icon : skill.template.iconId) {
                    msg.writer().writeShort(icon);
                }
                msg.writer().writeByte(skill.template.dx.length);
                for (int[] dxs : skill.template.dx) {
                    msg.writer().writeByte(dxs.length);
                    for (int dx : dxs) {
                        msg.writer().writeShort(dx);
                    }
                }
                msg.writer().writeByte(skill.template.dy.length);
                for (int[] dys : skill.template.dy) {
                    msg.writer().writeByte(dys.length);
                    for (int dy : dys) {
                        msg.writer().writeShort(dy);
                    }
                }
                msg.writer().writeShort(skill.template.levelRequire[0]); // level học bình thường
                msg.writer().writeByte(skill.template.maxLevel[0]); // level bình thường
                msg.writer().writeByte(skill.template.maxLevel[1]); // level cường hóa
                msg.writer().writeByte(skill.template.pointUpgrade.length);
                for (int point : skill.template.pointUpgrade) {
                    msg.writer().writeInt(point);
                }
                msg.writer().writeByte(skill.template.coolDown.length);
                for (long[] coolDowns : skill.template.coolDown) {
                    msg.writer().writeByte(coolDowns.length);
                    for (long time : coolDowns) {
                        msg.writer().writeInt((int) time);
                    }
                }
                msg.writer().writeByte(skill.template.typeMana.ordinal());
                msg.writer().writeByte(skill.template.mana.length);
                for (int[] mana : skill.template.mana) {
                    msg.writer().writeByte(mana.length);
                    for (int mp : mana) {
                        msg.writer().writeInt(mp);
                    }
                }
                msg.writer().writeByte(skill.template.options.size());
                for (SkillOption option : skill.template.options) {
                    msg.writer().writeByte(option.template.id);
                    msg.writer().writeUTF(option.template.name);
                    msg.writer().writeByte(option.params.length);
                    for (int param : option.params) {
                        msg.writer().writeShort(param);
                    }
                    msg.writer().writeByte(option.upgrades.length);
                    for (int param : option.upgrades) {
                        msg.writer().writeShort(param);
                    }
                }
                msg.writer().writeByte(skill.level);
                msg.writer().writeByte(skill.upgrade);
                msg.writer().writeInt(skill.point);
                msg.writer().writeByte(skill.coolDownReduction);
                if (skill.level > 0 && skill.template.isProactive) {
                    msg.writer().writeLong(skill.timeCanUse);
                }
                RandomCollection<Integer> paints = skill.getListPaint();
                msg.writer().writeByte(paints.getMap().size());
                for (Double percent : paints.getMap().keySet()) {
                    msg.writer().writeUTF(percent.toString());
                    msg.writer().writeShort(paints.getMap().get(percent));
                }
            }
            if (session.version.equals("0.9.2")) {
                msg.writer().writeByte(player.keysSkill.length);
                for (Skill skill : player.keysSkill) {
                    if (skill != null) {
                        msg.writer().writeByte(skill.template.id);
                    } else {
                        msg.writer().writeByte(-1);
                    }
                }
            } else {
                int size = Math.min(6, player.keysSkill.length);
                msg.writer().writeByte(size);
                for (int i = 0; i < size; i++) {
                    Skill skill = player.keysSkill[i];
                    if (skill != null) {
                        msg.writer().writeByte(skill.template.id);
                    } else {
                        msg.writer().writeByte(-1);
                    }
                }
            }
            msg.writer().writeByte(player.mySkill.template.id);
            List<Effect> effects = player.getEffects();
            msg.writer().writeByte(effects.size());
            for (Effect effect : effects) {
                msg.writer().writeShort(effect.template.id);
                msg.writer().writeLong(effect.time);
            }
            player.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
            logger.error("setLogin", e);
        }
    }

    public void setMapInfo() {
        try {
            if (!player.isPlayer()) {
                return;
            }
            Zone zone = player.zone;
            MapTemplate template = zone.map.template;
            Message msg = new Message(MessageName.MAP_INFO);
            msg.writer().writeShort(template.id);
            if (!player.mapsLoad.contains(template.id)) {
                msg.writer().writeShort(template.iconId); // bgr id
                msg.writer().writeUTF(template.name);
                msg.writer().writeShort(template.row);
                msg.writer().writeShort(template.column);
                msg.writer().writeUTF(template.data);
                for (int img : template.imgsBgr) {
                    msg.writer().writeShort(img);
                }
                for (int i = 0; i < 4; i++) {
                    for (int j = 0; j < 3; j++) {
                        msg.writer().writeShort(template.colorsBgr[i][j]);
                    }
                }
            }
            msg.writer().writeByte(zone.id);
            msg.writer().writeShort(player.x);
            msg.writer().writeShort(player.y);
            msg.writer().writeByte(template.wayPoints.size());
            for (WayPoint wayPoint : template.wayPoints) {
                msg.writer().writeShort(wayPoint.x);
                msg.writer().writeShort(wayPoint.y);
                msg.writer().writeByte(wayPoint.type.ordinal());
                MapTemplate temp = MapManager.getInstance().mapTemplates.get(wayPoint.goMap);
                if (temp == null) {
                    msg.writer().writeUTF("Chưa mở");
                } else {
                    msg.writer().writeUTF(temp.name);
                }
            }
            List<Npc> npcList = zone.getNpcs();
            msg.writer().writeByte(npcList.size());
            for (Npc npc : npcList) {
                msg.writer().writeByte(npc.template.id);
                msg.writer().writeShort(npc.x);
                msg.writer().writeShort(npc.y);
            }

            msg.writer().writeByte(zone.getMonsters().size());
            for (Monster monster : zone.getMonsters()) {
                if (monster instanceof Pet) {
                    msg.writer().writeByte(2);
                } else if (monster instanceof BigMonster) {
                    msg.writer().writeByte(1);
                } else {
                    msg.writer().writeByte(0);
                }
                msg.writer().writeShort(monster.template.id);
                msg.writer().writeInt(monster.id);
                msg.writer().writeShort(monster.level);
                msg.writer().writeByte(monster.levelBoss.ordinal());
                msg.writer().writeShort(monster.x);
                msg.writer().writeShort(monster.y);
                msg.writer().writeLong(monster.maxHp);
                msg.writer().writeLong(monster.hp);
                msg.writer().writeByte(monster.status.ordinal());
            }
            List<ItemMap> itemMapList = zone.getItemMaps();
            msg.writer().writeShort(itemMapList.size());
            for (ItemMap itemMap : itemMapList) {
                msg.writer().writeInt(itemMap.id);
                msg.writer().writeShort(itemMap.template.id);
                msg.writer().writeShort(itemMap.x);
                msg.writer().writeShort(itemMap.y);
            }
            msg.writer().writeBoolean(Server.getInstance().dragon.isActive);
            player.sendMessage(msg);
            msg.cleanup();
            if (!player.mapsLoad.contains(zone.map.template.id)) {
                player.mapsLoad.add(zone.map.template.id);
            }
        } catch (Exception e) {
            logger.error("setMapInfo", e);
        }
    }

    public void setItemBag() {
        try {
            player.sortBag();
            Message msg = messageInfo(MessagePlayerInfoName.ITEM_BAG);
            msg.writer().writeByte(0);
            msg.writer().writeByte(player.itemsBag.length);
            for (Item item : player.itemsBag) {
                if (item != null) {
                    item.write(msg);
                } else {
                    msg.writer().writeShort(-1);
                }
            }
            player.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
            logger.error("setItemBag", e);
        }
    }

    public void refreshItemBag(int index) {
        try {
            if (index < 0 || index >= player.itemsBag.length) {
                return;
            }
            Item item = player.itemsBag[index];
            Message msg = messageInfo(MessagePlayerInfoName.ITEM_BAG);
            msg.writer().writeByte(1);
            msg.writer().writeByte(index);
            if (item == null) {
                msg.writer().writeShort(-1);
            } else {
                item.write(msg);
            }
            player.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
            logger.error("refreshItemBag", e);
        }
    }

    public void refreshQuantityItemBag(int index, int quantity) {
        try {
            Message msg = messageInfo(MessagePlayerInfoName.ITEM_BAG);
            msg.writer().writeByte(2);
            msg.writer().writeByte(index);
            msg.writer().writeInt(quantity);
            player.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
            logger.error("refreshQuantityItemBag", e);
        }
    }

    public void setItemBox() {
        try {
            player.sortBox();
            Message msg = messageInfo(MessagePlayerInfoName.ITEM_BOX);
            msg.writer().writeByte(0);
            msg.writer().writeByte(player.itemsBox.length);
            for (Item item : player.itemsBox) {
                if (item != null) {
                    item.write(msg);
                } else {
                    msg.writer().writeShort(-1);
                }
            }
            player.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
            logger.error("setItemBag", e);
        }
    }

    public void setItemBody() {
        try {
            Message msg = messageInfo(MessagePlayerInfoName.ITEM_BODY);
            msg.writer().writeByte(0);
            msg.writer().writeByte(player.itemsBody.length);
            for (Item item : player.itemsBody) {
                if (item != null) {
                    item.write(msg);
                } else {
                    msg.writer().writeShort(-1);
                }
            }
            player.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
            logger.error("setItemBody", e);
        }
    }

    public void setItemOther() {
        try {
            Message msg = messageInfo(MessagePlayerInfoName.ITEM_OTHER);
            msg.writer().writeByte(0);
            msg.writer().writeByte(player.itemsOther.length);
            for (Item item : player.itemsOther) {
                if (item != null) {
                    item.write(msg);
                } else {
                    msg.writer().writeShort(-1);
                }
            }
            player.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
            logger.error("setItemOther", e);
        }
    }

    public void refreshItemOther(int index) {
        try {
            if (index < 0 || index >= player.itemsOther.length) {
                return;
            }
            Item item = player.itemsOther[index];
            Message msg = messageInfo(MessagePlayerInfoName.ITEM_OTHER);
            msg.writer().writeByte(1);
            msg.writer().writeByte(index);
            if (item == null) {
                msg.writer().writeShort(-1);
            } else {
                item.write(msg);
            }
            player.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
            logger.error("refreshItemOther", e);
        }
    }

    public void setItemSurvival() {
        try {
            Message msg = messageInfo(MessagePlayerInfoName.ITEM_BODY);
            msg.writer().writeByte(0);
            msg.writer().writeByte(player.itemsSurvival.length);
            for (Item item : player.itemsSurvival) {
                if (item != null) {
                    item.write(msg);
                } else {
                    msg.writer().writeShort(-1);
                }
            }
            player.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
            logger.error("setItemSurvival", e);
        }
    }

    public void refreshItemBody(int index) {
        try {
            if (index < 0 || index >= player.itemsBody.length) {
                return;
            }
            Item item = player.itemsBody[index];
            Message msg = messageInfo(MessagePlayerInfoName.ITEM_BODY);
            msg.writer().writeByte(1);
            msg.writer().writeByte(index);
            if (item == null) {
                msg.writer().writeShort(-1);
            } else {
                item.write(msg);
            }
            player.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
            logger.error("refreshItemBody", e);
        }
    }

    public void setTaskMain() {
        try {
            Task task = player.taskMain;
            if (task == null) {
                return;
            }
            Message msg = messageInfo(MessagePlayerInfoName.TASK_MAIN);
            TaskTemplate template = task.template;
            msg.writer().writeByte(template.id);
            msg.writer().writeUTF(template.name);
            msg.writer().writeByte(task.index);
            msg.writer().writeInt(task.param);
            msg.writer().writeByte(template.steps.length);
            for (TaskStepTemplate step : template.steps) {
                msg.writer().writeUTF(step.name);
                msg.writer().writeInt(step.param);
                msg.writer().writeByte(step.npcId);
                msg.writer().writeUTF(step.description);
            }
            msg.writer().writeByte(template.items.size());
            for (Item item : template.items) {
                item.write(msg);
            }
            player.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
            logger.error("setTaskMain", e);
        }
    }

    public void nextTaskIndex() {
        try {
            Message msg = messageInfo(MessagePlayerInfoName.TASK_INDEX);
            player.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
            logger.error("nextTaskIndex", e);
        }
    }

    public void nextTaskParam() {
        try {
            Message msg = messageInfo(MessagePlayerInfoName.TASK_PARAM);
            player.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
            logger.error("nextTaskParam", e);
        }
    }

    /*
    0 - do
    1 - vang
     */
    public void addInfo(int type, String text) {
        try {
            Message msg = new Message(MessageName.ADD_INFO);
            msg.writer().writeByte(type);
            msg.writer().writeUTF(text);
            player.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
            logger.error("addInfo", e);
        }
    }

    public void setPart() {
        try {
            player.refreshPart();
            Message msg = messageInfo(MessagePlayerInfoName.ALL_PART);
            msg.writer().writeShort(player.head);
            msg.writer().writeShort(player.body);
            msg.writer().writeShort(player.mount);
            msg.writer().writeShort(player.bag);
            msg.writer().writeShort(player.medal);
            msg.writer().writeShort(player.aura);
            player.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
            logger.error("setAllPart", e);
        }
    }

    public void setInfo() {
        try {
            player.refreshInfo();
            Message msg = messageInfo(MessagePlayerInfoName.INFO);
            msg.writer().writeLong(player.maxHp);
            msg.writer().writeLong(player.maxMp);
            msg.writer().writeLong(player.hp);
            msg.writer().writeLong(player.mp);
            msg.writer().writeByte(player.speed);
            msg.writer().writeUTF(Utils.pointToPercent(player.options[OptionName.NE_DON]));
            msg.writer().writeUTF(Utils.pointToPercent(player.options[OptionName.CHI_MANG]));
            msg.writer().writeUTF(Utils.pointToPercent(player.options[OptionName.GIAM_SAT_THUONG]));
            msg.writer().writeUTF(Utils.pointToPercent(player.options[OptionName.HUT_HP]));
            msg.writer().writeUTF(Utils.pointToPercent(player.options[OptionName.HUT_MP]));
            msg.writer().writeUTF(Utils.pointToPercent(player.options[OptionName.PHAN_SAT_THUONG]));
            msg.writer().writeLong(player.damage);
            player.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
            logger.error("setInfo", e);
        }
        Pet pet = player.pet;
        if (pet != null) {
            pet.refreshInfo();
        }
    }

    public void setBaseInfo(int index) {
        try {
            Message msg = messageInfo(MessagePlayerInfoName.BASE_INFO);
            msg.writer().writeLong(player.potential);
            msg.writer().writeByte(index);
            if (index == 0) {
                msg.writer().writeInt(player.baseDamage);
            } else if (index == 1) {
                msg.writer().writeInt(player.baseHp);
            } else if (index == 2) {
                msg.writer().writeInt(player.baseMp);
            } else {
                msg.writer().writeInt(player.baseConstitution);
            }
            msg.writer().writeLong(player.getPotentialToUp(index));
            player.sendMessage(msg);
            msg.cleanup();
        } catch (Exception ex) {
            logger.error("setBaseInfo", ex);
        }
    }

    public void refreshSkill(int type, Skill skill) {
        try {
            Message msg = messageInfo(MessagePlayerInfoName.SKILL_INFO);
            msg.writer().writeByte(type);
            if (type == 0) {
                msg.writer().writeShort(player.pointSkill);
                msg.writer().writeByte(skill.template.id);
                msg.writer().writeByte(skill.level);
            } else if (type == 1) {
                msg.writer().writeShort(player.pointSkill);
            } else if (type == 2) {
                msg.writer().writeByte(skill.template.id);
                msg.writer().writeInt(skill.point);
            } else if (type == 3) {
                msg.writer().writeByte(skill.template.id);
                msg.writer().writeByte(skill.upgrade);
                msg.writer().writeInt(skill.point);
            }
            player.sendMessage(msg);
            msg.cleanup();
        } catch (Exception ex) {
            logger.error("refreshSkill", ex);
        }
    }

    public void addPlayer(Player _player) {
        try {
            Message msg = new Message(MessageName.ADD_PLAYER);
            msg.writer().writeInt(_player.id);
            if (_player.isDisciple()) {
                msg.writer().writeUTF("$" + _player.name);
            } else if (_player.isBoss()) {
                msg.writer().writeUTF("#" + _player.name);
            } else if (!_player.isPlayer() || !Server.getInstance().isInterServer()) {
                msg.writer().writeUTF(_player.name);
            } else {
                msg.writer().writeUTF(String.format("[%d] %s", _player.session.user.server, _player.name));
            }
            msg.writer().writeByte(_player.gender);
            msg.writer().writeShort(_player.head);
            msg.writer().writeShort(_player.body);
            msg.writer().writeShort(_player.mount);
            msg.writer().writeShort(_player.bag);
            msg.writer().writeShort(_player.medal);
            msg.writer().writeShort(_player.aura);
            msg.writer().writeShort(_player.x);
            msg.writer().writeShort(_player.y);
            msg.writer().writeLong(_player.maxHp);
            msg.writer().writeLong(_player.hp);
            msg.writer().writeByte(_player.typePk);
            msg.writer().writeByte(_player.typeFlag);
            msg.writer().writeShort(_player.level);
            msg.writer().writeByte(_player.spaceship);
            msg.writer().writeByte(_player.speed);
            if (_player.clan == null) {
                msg.writer().writeInt(-1);
            } else {
                msg.writer().writeInt(_player.clan.id);
                msg.writer().writeUTF(_player.clan.name);
            }
            msg.writer().writeByte(_player.upgrade);
            List<Effect> effects = _player.getEffects();
            msg.writer().writeByte(effects.size());
            for (Effect effect : effects) {
                if (!effect.template.isMe) {
                    msg.writer().writeShort(effect.template.id);
                    msg.writer().writeLong(effect.time);
                } else {
                    msg.writer().writeShort(-1);
                }
            }
            player.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
            logger.error("addPlayer", e);
        }
    }

    public void openZoneUI() {
        try {
            Message msg = new Message(MessageName.AREA_UI);
            msg.writer().writeByte(player.zone.map.zones.size());
            for (Zone zone : player.zone.map.zones) {
                int size = zone.getPlayers(Zone.TYPE_PLAYER).size();
                msg.writer().writeByte(zone.id);
                msg.writer().writeByte(zone.map.template.maxPlayer);
                msg.writer().writeByte(size);
                msg.writer().writeByte(zone.findAllTeam().size()); // nhom
                if (zone instanceof ZoneSurvival) {
                    msg.writer().writeBoolean(((ZoneSurvival) zone).isRedZone);
                } else {
                    msg.writer().writeBoolean(size > 10);
                }
            }
            player.sendMessage(msg);
            msg.cleanup();
        } catch (Exception ex) {
            logger.error("openZoneUI", ex);
        }
    }

    public void createMenu(int npcTemplateId, String chat, List<Command> commands) {
        try {
            Message msg = new Message(MessageName.OPEN_NPC);
            msg.writer().writeByte(npcTemplateId);
            msg.writer().writeUTF(chat);
            msg.writer().writeByte(commands.size());
            for (Command command : commands) {
                msg.writer().writeUTF(command.caption);
            }
            player.sendMessage(msg);
            msg.cleanup();
        } catch (Exception ex) {
            logger.error("createMenu", ex);
        }
    }

    public void startYesNo(String info, Command yes, Command no) {
        try {
            Message msg = new Message(MessageName.DIALOG_YES_NO);
            msg.writer().writeUTF(info);
            msg.writer().writeUTF(yes.caption);
            msg.writer().writeUTF(no.caption);
            player.sendMessage(msg);
            msg.cleanup();
        } catch (Exception ex) {
            logger.error("startYesNo", ex);
        }
    }

    public void openURL(String url) {
        try {
            Message msg = new Message(MessageName.OPEN_URL);
            msg.writer().writeUTF(url);
            player.sendMessage(msg);
            msg.cleanup();
        } catch (Exception ex) {
            logger.error("openURL", ex);
        }
    }

    public void setCoin() {
        try {
            Message msg = messageInfo(MessagePlayerInfoName.REFRESH_XU);
            msg.writer().writeLong(player.xu);
            player.sendMessage(msg);
            msg.cleanup();
        } catch (Exception ex) {
            logger.error("refreshXu", ex);
        }
    }

    public void setDiamond() {
        try {
            Message msg = messageInfo(MessagePlayerInfoName.REFRESH_DIAMOND);
            msg.writer().writeInt(player.diamond);
            player.sendMessage(msg);
            msg.cleanup();
        } catch (Exception ex) {
            logger.error("refreshDiamond", ex);
        }
    }

    public void setCoinLock() {
        try {
            Message msg = messageInfo(MessagePlayerInfoName.SET_COIN_LOCK);
            msg.writer().writeLong(player.xuKhoa);
            player.sendMessage(msg);
            msg.cleanup();
        } catch (Exception ex) {
            logger.error("setCoinLock", ex);
        }
    }

    public void setRuby() {
        try {
            Message msg = messageInfo(MessagePlayerInfoName.SET_RUBY);
            msg.writer().writeInt(player.ruby);
            player.sendMessage(msg);
            msg.cleanup();
        } catch (Exception ex) {
            logger.error("setRuby", ex);
        }
    }

    public void setCountBarrack() {
        try {
            Message msg = messageInfo(MessagePlayerInfoName.COUNT_BARRACK);
            msg.writer().writeByte(player.countBarrack);
            player.sendMessage(msg);
            msg.cleanup();
        } catch (Exception ex) {
            logger.error("setCountBarrack", ex);
        }
    }

    public void showTab(int type) {
        try {
            Message msg = messageInfo(MessagePlayerInfoName.SHOW_TAB_PANEL);
            msg.writer().writeByte(type);
            player.sendMessage(msg);
            msg.cleanup();
        } catch (Exception ex) {
            logger.error("showTab", ex);
        }
    }

    public void teamInfo() {
        Team team = player.getTeam();
        if (team == null) {
            return;
        }
        team.lock.readLock().lock();
        try {
            Message msg = messageInfo(MessagePlayerInfoName.TEAM_INFO);
            msg.writer().writeByte(0);
            msg.writer().writeByte(team.status.ordinal());
            msg.writer().writeByte(team.members.size());
            for (TeamMember member : team.members) {
                Player player = PlayerManager.getInstance().findPlayerById(member.playerId);
                if (player != null) {
                    member.zone = player.zone;
                    member.power = player.power;
                } else {
                    member.zone = null;
                }
                msg.writer().writeInt(member.playerId);
                msg.writer().writeUTF(member.name);
                msg.writer().writeLong(member.power);
                msg.writer().writeByte(member.gender);
                if (member.zone != null) {
                    msg.writer().writeUTF(member.zone.map.template.name + ": khu " + member.zone.id);
                } else {
                    msg.writer().writeUTF("Trạng thái: Offline");
                }
            }
            player.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
            logger.error("teamInfo", e);
        } finally {
            team.lock.readLock().unlock();
        }
    }

    public void addPlayerToTeam(TeamMember member) {
        try {
            Message msg = messageInfo(MessagePlayerInfoName.TEAM_INFO);
            msg.writer().writeByte(1);
            msg.writer().writeInt(member.playerId);
            msg.writer().writeUTF(member.name);
            msg.writer().writeLong(member.power);
            msg.writer().writeByte(member.gender);
            msg.writer().writeUTF(member.zone.map.template.name + ": khu " + member.zone.id);
            player.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
            logger.error("addPlayerToTeam", e);
        }
    }

    public void removePlayerFromTeam(int playerId) {
        try {
            Message msg = messageInfo(MessagePlayerInfoName.TEAM_INFO);
            msg.writer().writeByte(2);
            msg.writer().writeInt(playerId);
            player.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
            logger.error("removePlayerFromTeam", e);
        }
    }

    public void changeStatusTeam(TeamStatus status) {
        try {
            Message msg = messageInfo(MessagePlayerInfoName.TEAM_INFO);
            msg.writer().writeByte(3);
            msg.writer().writeByte(status.ordinal());
            player.sendMessage(msg);
            msg.cleanup();
        } catch (Exception ex) {
            logger.error("changeStatusTeam", ex);
        }
    }

    public void findTeam() {
        try {
            List<Team> teams = player.zone.findAllTeam();
            Message msg = messageInfo(MessagePlayerInfoName.TEAM_INFO);
            msg.writer().writeByte(6);
            msg.writer().writeByte(teams.size());
            for (Team team : teams) {
                team.lock.readLock().lock();
                try {
                    msg.writer().writeInt(team.id);
                    TeamMember member = team.members.get(0);
                    msg.writer().writeInt(member.playerId);
                    msg.writer().writeUTF(member.name);
                    msg.writer().writeByte(player.gender);
                    msg.writer().writeLong(player.power);
                    msg.writer().writeBoolean(PlayerManager.getInstance().findPlayerById(member.playerId) != null);
                    msg.writer().writeByte(team.members.size());
                } finally {
                    team.lock.readLock().unlock();
                }
            }
            player.sendMessage(msg);
            msg.cleanup();
        } catch (Exception ex) {
            logger.error("findTeam", ex);
        }
    }

    public void startTrade() {
        try {
            Message msg = new Message(MessageName.TRADE_ACTION);
            msg.writer().writeByte(1);
            msg.writer().writeInt(((TradeAction) player.tradeAction).targetId);
            player.sendMessage(msg);
            msg.cleanup();
        } catch (Exception ex) {
            logger.error("startTrade", ex);
        }
    }

    public void cancelTrade() {
        try {
            Message msg = new Message(MessageName.TRADE_ACTION);
            msg.writer().writeByte(2);
            player.sendMessage(msg);
            msg.cleanup();
        } catch (Exception ex) {
            logger.error("cancelTrade", ex);
        }
    }

    public void addItemTrade(int index, int quantity) {
        try {
            Message msg = new Message(MessageName.TRADE_ACTION);
            msg.writer().writeByte(3);
            msg.writer().writeByte(index);
            msg.writer().writeInt(quantity);
            player.sendMessage(msg);
            msg.cleanup();
        } catch (Exception ex) {
            logger.error("addItemTrade", ex);
        }
    }

    public void removeItemTrade(int index) {
        try {
            Message msg = new Message(MessageName.TRADE_ACTION);
            msg.writer().writeByte(4);
            msg.writer().writeByte(index);
            player.sendMessage(msg);
            msg.cleanup();
        } catch (Exception ex) {
            logger.error("removeItemTrade", ex);
        }
    }

    public void setCoinTrade() {
        try {
            Message msg = new Message(MessageName.TRADE_ACTION);
            msg.writer().writeByte(5);
            msg.writer().writeLong(((TradeAction) player.tradeAction).coin);
            player.sendMessage(msg);
            msg.cleanup();
        } catch (Exception ex) {
            logger.error("addXuTrade", ex);
        }
    }

    public void lockTrade() {
        try {
            Message msg = new Message(MessageName.TRADE_ACTION);
            msg.writer().writeByte(6);
            player.sendMessage(msg);
            msg.cleanup();
        } catch (Exception ex) {
            logger.error("lockTrade", ex);
        }
    }

    public void sendListItemTrade(Player trader) {
        try {
            Message msg = new Message(MessageName.TRADE_ACTION);
            msg.writer().writeByte(7);
            TradeAction action = (TradeAction) player.tradeAction;
            msg.writer().writeByte(action.items.size());
            for (int index : action.items.keySet()) {
                Item item = player.itemsBag[index];
                if (item != null) {
                    item.write(msg, action.items.get(index));
                }
            }
            msg.writer().writeLong(action.coin);
            trader.sendMessage(msg);
            msg.cleanup();
        } catch (Exception ex) {
            logger.error("sendListItemTrade", ex);
        }
    }

    public void confirmTrade() {
        try {
            Message msg = new Message(MessageName.TRADE_ACTION);
            msg.writer().writeByte(8);
            player.sendMessage(msg);
            msg.cleanup();
        } catch (Exception ex) {
            logger.error("confirmTrade", ex);
        }
    }

    public void completeTrade() {
        try {
            Message msg = new Message(MessageName.TRADE_ACTION);
            msg.writer().writeByte(9);
            player.sendMessage(msg);
            msg.cleanup();
        } catch (Exception ex) {
            logger.error("completeTrade", ex);
        }
    }

    public void setClanInfo(boolean isShowTab) {
        try {
            Message msg = new Message(MessageName.CLAN_INFO);
            msg.writer().writeByte(0);
            Clan clan = player.clan;
            if (clan == null) {
                msg.writer().writeInt(-1);
            } else {
                ClanMember me = clan.findMemberByPlayerId(player.id);
                if (me == null) {
                    player.setClan(null);
                    msg.writer().writeInt(-1);
                } else {
                    msg.writer().writeInt(clan.id);
                    msg.writer().writeUTF(clan.name);
                    msg.writer().writeUTF(clan.slogan);
                    msg.writer().writeLong(clan.coin);
                    msg.writer().writeUTF(clan.getNotification());
                    msg.writer().writeByte(clan.level);
                    msg.writer().writeLong(clan.exp);
                    msg.writer().writeShort(clan.getMaxMember());
                    msg.writer().writeLong(clan.getMaxExp());
                    msg.writer().writeUTF(clan.getStrCreateTime());
                    msg.writer().writeByte(me.role);
                    msg.writer().writeByte(clan.members.size());
                    for (ClanMember member : clan.members) {
                        msg.writer().writeInt(member.playerId);
                        Player p = PlayerManager.getInstance().findPlayerById(member.playerId);
                        if (p != null) {
                            member.setName(p.name);
                            member.setPower(p.power);
                            member.isOnline = true;
                        } else {
                            member.isOnline = false;
                        }
                        msg.writer().writeByte(member.role);
                        msg.writer().writeUTF(member.name);
                        msg.writer().writeByte(member.gender);
                        msg.writer().writeLong(member.power);
                        msg.writer().writeInt(member.point);
                        msg.writer().writeInt(member.pointDay);
                        msg.writer().writeBoolean(member.isOnline);
                        msg.writer().writeUTF(member.getStrJoinTime());
                    }
                }
            }
            msg.writer().writeBoolean(isShowTab);
            player.sendMessage(msg);
            msg.cleanup();
        } catch (Exception ex) {
            logger.error("setClanInfo", ex);
        }
    }

    public void setClanSlogan(String slogan) {
        try {
            Message msg = new Message(MessageName.CLAN_INFO);
            msg.writer().writeByte(3);
            msg.writer().writeUTF(slogan);
            player.sendMessage(msg);
            msg.cleanup();
        } catch (Exception ex) {
            logger.error("setClanSlogan", ex);
        }
    }

    public void setClanNotification(String notification) {
        try {
            Message msg = new Message(MessageName.CLAN_INFO);
            msg.writer().writeByte(4);
            msg.writer().writeUTF(notification);
            player.sendMessage(msg);
            msg.cleanup();
        } catch (Exception ex) {
            logger.error("setClanNotification", ex);
        }
    }

    public void setClanCoin(long coin) {
        try {
            Message msg = new Message(MessageName.CLAN_INFO);
            msg.writer().writeByte(1);
            msg.writer().writeLong(coin);
            player.sendMessage(msg);
            msg.cleanup();
        } catch (Exception ex) {
            logger.error("setClanCoin", ex);
        }
    }

    public void startClientInput(String name, TextField... fields) {
        try {
            Message msg = new Message(MessageName.CLIENT_INPUT);
            msg.writer().writeUTF(name);
            msg.writer().writeByte(fields.length);
            for (TextField textField : fields) {
                msg.writer().writeUTF(textField.name);
                msg.writer().writeByte(textField.type);
            }
            player.sendMessage(msg);
            msg.cleanup();
        } catch (Exception ex) {
            logger.error("startClientInput", ex);
        }
    }

    public void startDie() {
        try {
            Message msg = new Message(MessageName.ME_DIE);
            msg.writer().writeShort(player.x);
            msg.writer().writeShort(player.y);
            player.sendMessage(msg);
            msg.cleanup();
        } catch (Exception ex) {
            logger.error("startDie", ex);
        }
    }

    public void wakeUpFromDead() {
        try {
            Message msg = new Message(MessageName.WAKE_UP_FROM_DIE);
            msg.writer().writeInt(player.id);
            msg.writer().writeShort(player.x);
            msg.writer().writeShort(player.y);
            msg.writer().writeLong(player.hp);
            msg.writer().writeLong(player.mp);
            player.sendMessage(msg);
            msg.cleanup();
        } catch (Exception ex) {
            logger.error("startDie", ex);
        }
    }

    public void recoveryHp() {
        try {
            Message msg = messageInfo(MessagePlayerInfoName.RECOVERY_HP);
            msg.writer().writeLong(player.hp);
            player.sendMessage(msg);
            msg.cleanup();
        } catch (Exception ex) {
            logger.error("recoveryHp", ex);
        }
    }

    public void recoveryMp() {
        try {
            Message msg = messageInfo(MessagePlayerInfoName.RECOVERY_MP);
            msg.writer().writeLong(player.mp);
            player.sendMessage(msg);
            msg.cleanup();
        } catch (Exception ex) {
            logger.error("recoveryMp", ex);
        }
    }

    public void upPower(long exp) {
        try {
            Message msg = messageInfo(MessagePlayerInfoName.UP_POWER);
            msg.writer().writeLong(exp);
            player.sendMessage(msg);
            msg.cleanup();
        } catch (Exception ex) {
            logger.error("upPower", ex);
        }
    }

    public void setTypePk() {
        try {
            Message msg = messageInfo(MessagePlayerInfoName.SET_TYPE_PK);
            msg.writer().writeByte(player.typePk);
            player.sendMessage(msg);
            msg.cleanup();
        } catch (Exception ex) {
            logger.error("setTypePk", ex);
        }
        if (player.zone != null) {
            player.zone.service.setTypePk(player);
        }
    }

    public void setTypeFlag() {
        try {
            Message msg = messageInfo(MessagePlayerInfoName.SET_TYPE_FLAG);
            msg.writer().writeByte(player.typeFlag);
            player.sendMessage(msg);
            msg.cleanup();
        } catch (Exception ex) {
            logger.error("setTypeFlag", ex);
        }
        if (player.zone != null) {
            player.zone.service.setTypeFlag(player);
        }
    }

    public void stopCharge() {
        try {
            Message msg = messageInfo(MessagePlayerInfoName.STOP_WAIT_ULTIMATE);
            player.sendMessage(msg);
            msg.cleanup();
        } catch (Exception ex) {
            logger.error("stopCharge", ex);
        }
        if (player.zone != null) {
            player.zone.service.stopCharge(player);
        }
    }

    public void saveKeySkill(int templateId, int index) {
        try {
            Message msg = messageInfo(MessagePlayerInfoName.SAVE_KEY_SKILL);
            msg.writer().writeByte(templateId);
            msg.writer().writeByte(index);
            player.sendMessage(msg);
            msg.cleanup();
        } catch (Exception ex) {
            logger.error("saveKeySkill", ex);
        }
    }

    public void addEffect(Effect effect) {
        try {
            Message msg = messageInfo(MessagePlayerInfoName.ADD_EFFECT);
            msg.writer().writeShort(effect.template.id);
            msg.writer().writeLong(effect.time);
            player.sendMessage(msg);
            msg.cleanup();
        } catch (Exception ex) {
            logger.error("addEffect", ex);
        }
        if (player.zone != null && !effect.template.isMe) {
            player.zone.service.playerAddEffect(player, effect);
        }
    }

    public void removeEffect(Effect effect) {
        try {
            Message msg = messageInfo(MessagePlayerInfoName.REMOVE_EFFECT);
            msg.writer().writeShort(effect.template.id);
            player.sendMessage(msg);
            msg.cleanup();
        } catch (Exception ex) {
            logger.error("removeEffect", ex);
        }
    }

    public void removeEffect(int effectID) {
        try {
            Message msg = messageInfo(MessagePlayerInfoName.REMOVE_EFFECT);
            msg.writer().writeShort(effectID);
            player.sendMessage(msg);
            msg.cleanup();
        } catch (Exception ex) {
            logger.error("removeEffect", ex);
        }
    }

    public void setTimeEffect(Effect effect) {
        try {
            Message msg = messageInfo(MessagePlayerInfoName.SET_TIME_EFFECT);
            msg.writer().writeShort(effect.template.id);
            msg.writer().writeLong(effect.time);
            player.sendMessage(msg);
            msg.cleanup();
        } catch (Exception ex) {
            logger.error("setTimeEffect", ex);
        }
    }

    public void setTimeFlight(int second) {
        try {
            Message msg = new Message(MessageName.UPDATE_TIME_SOLO);
            msg.writer().writeInt(second);
            player.sendMessage(msg);
            msg.cleanup();
        } catch (Exception ex) {
            logger.error("setTimeSolo", ex);
        }
    }

    public void setTimeRemaining(long miliSecond) {
        try {
            Message msg = new Message(MessageName.UPDATE_TIME_REMAINING);
            msg.writer().writeLong(miliSecond); // milisecond
            player.sendMessage(msg);
            msg.cleanup();
        } catch (Exception ex) {
            logger.error("setTimeSolo", ex);
        }
    }

    public void pickItem(ItemMap itemMap) {
        try {
            Message msg = new Message(MessageName.ME_PICK_ITEM);
            msg.writer().writeInt(itemMap.id);
            msg.writer().writeInt(itemMap.quantity);
            player.sendMessage(msg);
            msg.cleanup();
        } catch (Exception ex) {
            logger.error("pickItem", ex);
        }
        if (player.zone != null) {
            player.zone.service.pickItem(player, itemMap);
        }
    }

    public void levelUp() {
        try {
            Message msg = messageInfo(MessagePlayerInfoName.LEVEL_UP);
            player.sendMessage(msg);
            msg.cleanup();
        } catch (Exception ex) {
            logger.error("levelUp", ex);
        }
        if (player.zone != null) {
            player.zone.service.levelUp(player);
        }
    }

    public void setPointPk() {
        try {
            Message msg = messageInfo(MessagePlayerInfoName.SET_POINT_PK);
            msg.writer().writeByte(player.pointPk);
            player.sendMessage(msg);
            msg.cleanup();
        } catch (Exception ex) {
            logger.error("setPointPk", ex);
        }
    }

    public void showListMapSpaceship() {
        try {
            Message msg = messageInfo(MessagePlayerInfoName.MAP_SPACESHIP);
            msg.writer().writeByte(player.mapSpaceships.size());
            for (KeyValue<Map, String> keyValue : player.mapSpaceships) {
                msg.writer().writeUTF(keyValue.value);
                msg.writer().writeUTF((String) keyValue.elements[0]);
                msg.writer().writeShort(keyValue.key.template.id);
            }
            player.sendMessage(msg);
            msg.cleanup();
        } catch (Exception ex) {
            logger.error("showListMapSpaceship", ex);
        }
    }

    public void setIsAutoPlay() {
        try {
            Message msg = messageInfo(MessagePlayerInfoName.IS_AUTO_PLAY);
            msg.writer().writeBoolean(player.isAutoPlay);
            player.sendMessage(msg);
            msg.cleanup();
        } catch (Exception ex) {
            logger.error("setIsCanAutoPlay", ex);
        }
    }

    public void resetPotential() {
        try {
            Message msg = messageInfo(MessagePlayerInfoName.RESET_BASE_INFO);
            msg.writer().writeLong(player.potential);
            msg.writer().writeInt(player.baseDamage);
            msg.writer().writeInt(player.baseHp);
            msg.writer().writeInt(player.baseMp);
            msg.writer().writeInt(player.baseConstitution);
            for (byte i = 0; i < 4; i++) {
                msg.writer().writeLong(player.getPotentialToUp(i));
            }
            player.sendMessage(msg);
            msg.cleanup();
        } catch (Exception ex) {
            logger.error("resetPotential", ex);
        }
    }

    public void setTaskDaily() {
        try {
            if (player.taskDaily == null) {
                return;
            }
            Message msg = messageInfo(MessagePlayerInfoName.TASK_DAILY);
            if (player.taskDaily != null) {
                msg.writer().writeByte(1);
                msg.writer().writeUTF(player.taskDaily.name);
                msg.writer().writeUTF(player.taskDaily.description);
                msg.writer().writeInt(player.taskDaily.maxParam);
                msg.writer().writeInt(player.taskDaily.param);
            } else {
                msg.writer().writeByte(0);
            }
            player.sendMessage(msg);
            msg.cleanup();
        } catch (Exception ex) {
            logger.error("setTaskDaily", ex);
        }
    }

    public void setTaskDailyParam() {
        try {
            Message msg = messageInfo(MessagePlayerInfoName.TASK_DAILY_PARAM);
            msg.writer().writeInt(player.taskDaily.param);
            player.sendMessage(msg);
            msg.cleanup();
        } catch (Exception ex) {
            logger.error("setTaskDailyParam", ex);
        }
    }

    public void setPointActive() {
        try {
            Message msg = messageInfo(MessagePlayerInfoName.SET_POINT_ACTIVE);
            msg.writer().writeShort(player.pointActive);
            player.sendMessage(msg);
            msg.cleanup();
        } catch (Exception ex) {
            logger.error("setPointActive", ex);
        }
    }

    public void viewInfoPlayer(Player view) {
        try {
            Message msg = new Message(MessageName.VIEW_PLAYER);
            writeInfoPlayer(msg, view);
            if (view.disciple != null) {
                writeInfoPlayer(msg, view.disciple);
            } else {
                msg.writer().writeInt(1);
            }
            Pet pet = view.pet;
            if (pet == null) {
                msg.writer().writeShort(-1);
            } else {
                msg.writer().writeShort(pet.template.id);
                msg.writer().writeByte(pet.options.size());
                for (ItemOption option : pet.options) {
                    msg.writer().writeShort(option.template.id);
                    msg.writer().writeInt(option.param);
                }
                msg.writer().writeByte(view.itemsPet.length);
                for (Item item : view.itemsPet) {
                    if (item != null) {
                        item.write(msg);
                    } else {
                        msg.writer().writeShort(-1);
                    }
                }
                msg.writer().writeShort(pet.getMaxStamina());
                msg.writer().writeShort(pet.stamina);
                msg.writer().writeLong(pet.maxHp);
                msg.writer().writeLong(pet.hp);
                msg.writer().writeLong(pet.damage);
                msg.writer().writeInt(pet.getExp());
                msg.writer().writeInt(pet.getMaxExp());
            }
            player.sendMessage(msg);
            msg.cleanup();
        } catch (Exception ex) {
            logger.error("viewInfoPlayer", ex);
        }
    }

    public void writeInfoPlayer(Message msg, Player view) {
        try {
            msg.writer().writeInt(view.id);
            msg.writer().writeUTF(view.name);
            msg.writer().writeByte(view.gender);
            msg.writer().writeLong(view.power);
            msg.writer().writeShort(view.level);
            msg.writer().writeShort(view.head);
            msg.writer().writeShort(view.body);
            msg.writer().writeLong(view.maxHp);
            msg.writer().writeLong(view.maxMp);
            msg.writer().writeLong(view.hp);
            msg.writer().writeLong(view.mp);
            msg.writer().writeByte(view.speed);
            msg.writer().writeByte(view.pointPk);
            msg.writer().writeShort(view.pointActive);
            msg.writer().writeByte(view.countBarrack);
            msg.writer().writeUTF(Utils.pointToPercent(view.options[OptionName.NE_DON]));
            msg.writer().writeUTF(Utils.pointToPercent(view.options[OptionName.CHI_MANG]));
            msg.writer().writeUTF(Utils.pointToPercent(view.options[OptionName.GIAM_SAT_THUONG]));
            msg.writer().writeUTF(Utils.pointToPercent(view.options[OptionName.HUT_HP]));
            msg.writer().writeUTF(Utils.pointToPercent(view.options[OptionName.HUT_MP]));
            msg.writer().writeUTF(Utils.pointToPercent(view.options[OptionName.PHAN_SAT_THUONG]));
            msg.writer().writeLong(view.damage);
            Item[] items;
            if (!view.isInSurvival()) {
                items = view.itemsBody;
            } else {
                items = view.itemsSurvival;
            }
            msg.writer().writeByte(items.length);
            for (Item item : items) {
                if (item != null) {
                    item.write(msg);
                } else {
                    msg.writer().writeShort(-1);
                }
            }
            msg.writer().writeByte(view.itemsOther.length);
            for (Item item : view.itemsOther) {
                if (item != null) {
                    item.write(msg);
                } else {
                    msg.writer().writeShort(-1);
                }
            }
            msg.writer().writeUTF(view.clan == null ? "Chưa có" : view.clan.name);
        } catch (Exception ex) {
            logger.error("writeInfoPlayer", ex);
        }
    }

    public void discipleInfo(int type) {
        try {
            Disciple disciple = player.disciple;
            if (type != MessageDiscipleInfoName.INFO && disciple == null) {
                return;
            }
            Message msg = messageDisciple(type);
            if (type == MessageDiscipleInfoName.INFO) {
                if (disciple == null) {
                    msg.writer().writeBoolean(false);
                } else {
                    msg.writer().writeBoolean(true);
                    msg.writer().writeInt(disciple.id);
                    msg.writer().writeUTF(disciple.name);
                    msg.writer().writeByte(disciple.gender);
                }
            } else if (type == MessageDiscipleInfoName.BASE_INFO) {
                msg.writer().writeInt(disciple.baseDamage);
                msg.writer().writeInt(disciple.baseHp);
                msg.writer().writeInt(disciple.baseMp);
                msg.writer().writeInt(disciple.baseConstitution);
                for (byte i = 0; i < 4; i++) {
                    msg.writer().writeLong(disciple.getPotentialToUp(i));
                }
            } else if (type == MessageDiscipleInfoName.SKILL_INFO) {
                List<Skill> skills = new ArrayList<>(disciple.skills);
                for (int i = 0; i < 4; i++) {
                    if (i < disciple.skills.size()) {
                        continue;
                    }
                    skills.add(SkillManager.getInstance().skillsDiscipleNotOpen.get(i - 1));
                }
                msg.writer().writeByte(skills.size());
                for (Skill skill : skills) {
                    msg.writer().writeByte(skill.template.id);
                    msg.writer().writeByte(skill.template.name.length);
                    for (String name : skill.template.name) {
                        msg.writer().writeUTF(name);
                    }
                    msg.writer().writeByte(skill.template.description.length);
                    for (String description : skill.template.description) {
                        msg.writer().writeUTF(description);
                    }
                    msg.writer().writeByte(skill.template.type.ordinal());
                    msg.writer().writeBoolean(skill.template.isProactive);
                    msg.writer().writeByte(skill.template.iconId.length);
                    for (int icon : skill.template.iconId) {
                        msg.writer().writeShort(icon);
                    }
                    msg.writer().writeByte(skill.template.dx.length);
                    for (int[] dxs : skill.template.dx) {
                        msg.writer().writeByte(dxs.length);
                        for (int dx : dxs) {
                            msg.writer().writeShort(dx);
                        }
                    }
                    msg.writer().writeByte(skill.template.dy.length);
                    for (int[] dys : skill.template.dy) {
                        msg.writer().writeByte(dys.length);
                        for (int dy : dys) {
                            msg.writer().writeShort(dy);
                        }
                    }
                    msg.writer().writeShort(skill.template.levelRequire[0]); // level học bình thường
                    msg.writer().writeByte(skill.template.maxLevel[0]); // level bình thường
                    msg.writer().writeByte(skill.template.maxLevel[1]); // level cường hóa
                    msg.writer().writeByte(skill.template.pointUpgrade.length);
                    for (int point : skill.template.pointUpgrade) {
                        msg.writer().writeInt(point);
                    }
                    msg.writer().writeByte(skill.template.coolDown.length);
                    for (long[] coolDowns : skill.template.coolDown) {
                        msg.writer().writeByte(coolDowns.length);
                        for (long time : coolDowns) {
                            msg.writer().writeInt((int) time);
                        }
                    }
                    msg.writer().writeByte(skill.template.typeMana.ordinal());
                    msg.writer().writeByte(skill.template.mana.length);
                    for (int[] mana : skill.template.mana) {
                        msg.writer().writeByte(mana.length);
                        for (int mp : mana) {
                            msg.writer().writeInt(mp);
                        }
                    }
                    msg.writer().writeByte(skill.template.options.size());
                    for (SkillOption option : skill.template.options) {
                        msg.writer().writeByte(option.template.id);
                        msg.writer().writeUTF(option.template.name);
                        msg.writer().writeByte(option.params.length);
                        for (int param : option.params) {
                            msg.writer().writeShort(param);
                        }
                        msg.writer().writeByte(option.upgrades.length);
                        for (int param : option.upgrades) {
                            msg.writer().writeShort(param);
                        }
                    }
                    msg.writer().writeByte(skill.level);
                    msg.writer().writeByte(skill.upgrade);
                    msg.writer().writeInt(skill.point);
                    msg.writer().writeByte(skill.coolDownReduction);
                    if (skill.level > 0 && skill.template.isProactive) {
                        msg.writer().writeLong(skill.timeCanUse);
                    }
                }
            } else if (type == MessageDiscipleInfoName.POWER_INFO) {
                msg.writer().writeLong(disciple.power);
                msg.writer().writeLong(disciple.potential);
                msg.writer().writeShort(disciple.level);
            } else if (type == MessageDiscipleInfoName.POINT) {
                msg.writer().writeLong(disciple.maxHp);
                msg.writer().writeLong(disciple.maxMp);
                msg.writer().writeLong(disciple.hp);
                msg.writer().writeLong(disciple.mp);
                msg.writer().writeByte(disciple.speed);
                msg.writer().writeUTF(Utils.pointToPercent(disciple.options[OptionName.NE_DON]));
                msg.writer().writeUTF(Utils.pointToPercent(disciple.options[OptionName.CHI_MANG]));
                msg.writer().writeUTF(Utils.pointToPercent(disciple.options[OptionName.GIAM_SAT_THUONG]));
                msg.writer().writeUTF(Utils.pointToPercent(disciple.options[OptionName.HUT_HP]));
                msg.writer().writeUTF(Utils.pointToPercent(disciple.options[OptionName.HUT_MP]));
                msg.writer().writeUTF(Utils.pointToPercent(disciple.options[OptionName.PHAN_SAT_THUONG]));
                msg.writer().writeLong(disciple.damage);
            } else if (type == MessageDiscipleInfoName.ITEM_BODY) {
                msg.writer().writeByte(disciple.itemsBody.length);
                for (Item item : disciple.itemsBody) {
                    if (item != null) {
                        item.write(msg);
                    } else {
                        msg.writer().writeShort(-1);
                    }
                }
            } else if (type == MessageDiscipleInfoName.ALL_PART) {
                msg.writer().writeShort(disciple.head);
                msg.writer().writeShort(disciple.body);
                msg.writer().writeShort(disciple.mount);
                msg.writer().writeShort(disciple.bag);
                msg.writer().writeShort(disciple.medal);
                msg.writer().writeShort(disciple.aura);
            } else if (type == MessageDiscipleInfoName.DISCIPLE_STATUS) {
                msg.writer().writeUTF(disciple.getStatusInfo());
            } else if (type == MessageDiscipleInfoName.NAME) {
                msg.writer().writeUTF(disciple.name);
            } else if (type == MessageDiscipleInfoName.STAMINA) {
                msg.writer().writeShort(disciple.stamina);
                msg.writer().writeShort(disciple.getMaxStamina());
            } else if (type == MessageDiscipleInfoName.ITEM_OTHER) {
                msg.writer().writeByte(disciple.itemsOther.length);
                for (Item item : disciple.itemsOther) {
                    if (item != null) {
                        item.write(msg);
                    } else {
                        msg.writer().writeShort(-1);
                    }
                }
            }
            player.sendMessage(msg);
            msg.cleanup();
        } catch (Exception ex) {
            logger.error("discipleInfo", ex);
        }
    }

    public void petInfo(int type) {
        try {
            Pet pet = player.pet;
            if (type != MessagePetInfoName.INFO && pet == null) {
                return;
            }
            Message msg = messagePet(type);
            if (type == MessagePetInfoName.INFO) {
                if (pet == null) {
                    msg.writer().writeBoolean(false);
                } else {
                    msg.writer().writeBoolean(true);
                    msg.writer().writeShort(pet.template.id);
                    msg.writer().writeInt(pet.id);
                    msg.writer().writeByte(pet.options.size());
                    for (ItemOption option : pet.options) {
                        msg.writer().writeShort(option.template.id);
                        msg.writer().writeInt(option.param);
                    }
                }
            } else if (type == MessagePetInfoName.ITEM_BODY) {
                msg.writer().writeByte(player.itemsPet.length);
                for (Item item : player.itemsPet) {
                    if (item != null) {
                        item.write(msg);
                    } else {
                        msg.writer().writeShort(-1);
                    }
                }
            } else if (type == MessagePetInfoName.STAMINA) {
                msg.writer().writeShort(pet.getMaxStamina());
                msg.writer().writeShort(pet.stamina);
            } else if (type == MessagePetInfoName.HP_DAMAGE) {
                msg.writer().writeLong(pet.maxHp);
                msg.writer().writeLong(pet.hp);
                msg.writer().writeLong(pet.damage);
            } else if (type == MessagePetInfoName.EXP) {
                msg.writer().writeInt(pet.getExp());
                msg.writer().writeInt(pet.getMaxExp());
            }
            player.sendMessage(msg);
            msg.cleanup();
        } catch (Exception ex) {
            logger.error("petInfo", ex);
        }
    }

    public void chatMe(Player player, String content) {
        try {
            Message msg = new Message(MessageName.CHAT_PUBLIC);
            msg.writer().writeInt(player.id);
            msg.writer().writeUTF(content);
            this.player.sendMessage(msg);
            msg.cleanup();
        } catch (Exception ex) {
            logger.error("chatMe", ex);
        }
    }

    public void fusion(Player player, int type) {
        try {
            Message msg = new Message(MessageName.FUSION);
            msg.writer().writeInt(player.id);
            msg.writer().writeByte(type);
            this.player.sendMessage(msg);
            msg.cleanup();
        } catch (Exception ex) {
            logger.error("fusion", ex);
        }
    }

    public void createSpin(RandomCollection<ItemRandom> items, int id, int quantity) {
        try {
            List<ItemRandom> randomList = new ArrayList<>(items.getMap().values());
            Collections.shuffle(randomList);
            for (int i = 0; i < randomList.size(); i++) {
                if (randomList.get(i).getId(player.gender) == id) {
                    Collections.swap(randomList, i, 6);
                    break;
                }
            }
            Message msg = new Message(MessageName.CREATE_SPIN);
            msg.writer().writeByte(randomList.size());
            for (ItemRandom itemRandom : randomList) {
                msg.writer().writeShort(itemRandom.getId(player.gender));
                if (itemRandom.getId(player.gender) == id) {
                    msg.writer().writeInt(quantity);
                } else {
                    msg.writer().writeInt(1);
                }
            }
            player.sendMessage(msg);
            msg.cleanup();
        } catch (Exception ex) {
            logger.error("createSpin", ex);
        }
    }

    public void showNotification(List<String> notifications) {
        try {
            if (notifications == null || notifications.isEmpty()) {
                return;
            }
            Message msg = new Message(MessageName.NOTIFICATION);
            msg.writer().writeByte(notifications.size());
            for (String notification : notifications) {
                if (notification.startsWith("-")) {
                    msg.writer().writeUTF(notification);
                } else {
                    msg.writer().writeUTF("- " + notification);
                }
            }
            player.sendMessage(msg);
            msg.cleanup();
        } catch (Exception ex) {
            logger.error("showNotification", ex);
        }
    }

    public void setNotification(List<String> notifications) {
        try {
            if (notifications == null || notifications.isEmpty()) {
                return;
            }
            Message msg = new Message(MessageName.NOTIFICATION);
            msg.writer().writeByte(notifications.size());
            for (String notification : notifications) {
                msg.writer().writeUTF(notification);
            }
            player.sendMessage(msg);
            msg.cleanup();
        } catch (Exception ex) {
            logger.error("setNotification", ex);
        }
    }

    public void summonDragon(Dragon dragon, boolean isActive) {
        try {
            Message msg = new Message(MessageName.SUMMON_DRAGON);
            if (isActive) {
                msg.writer().writeByte(0);
                msg.writer().writeShort(player.zone.map.template.id);
                msg.writer().writeShort(player.zone.id);
                msg.writer().writeShort(dragon.x);
                msg.writer().writeShort(dragon.y);
                msg.writer().writeByte(dragon.icons.length);
                for (int icon : dragon.icons) {
                    msg.writer().writeShort(icon);
                }
                msg.writer().writeByte(dragon.items.length);
                for (int item : dragon.items) {
                    msg.writer().writeShort(item);
                }
            } else {
                msg.writer().writeByte(1);
            }
            player.sendMessage(msg);
            msg.cleanup();
        } catch (Exception ex) {
            logger.error("summonDragon", ex);
        }
    }

    public void setFriend(int type, int playerId) {
        try {
            Message msg = messageInfo(MessagePlayerInfoName.FRIEND);
            msg.writer().writeByte(type);
            if (type == 0) {
                msg.writer().writeByte(player.friends.size());
                for (Friend friend : player.friends) {
                    Player p = PlayerManager.getInstance().findPlayerById(friend.playerId);
                    if (p != null) {
                        friend.name = p.name;
                        friend.power = p.power;
                        friend.isOnline = true;
                    } else {
                        friend.isOnline = false;
                    }
                    msg.writer().writeInt(friend.playerId);
                    msg.writer().writeUTF(friend.name);
                    msg.writer().writeByte(friend.gender);
                    msg.writer().writeLong(friend.power);
                    msg.writer().writeBoolean(friend.isOnline);
                }
            } else if (type == 2) {
                msg.writer().writeInt(playerId);
            }
            player.sendMessage(msg);
            msg.cleanup();
        } catch (Exception ex) {
            logger.error("summonDragon", ex);
        }
    }

    public void setEnemy(int type, int playerId) {
        try {
            Message msg = messageInfo(MessagePlayerInfoName.ENEMY);
            msg.writer().writeByte(type);
            if (type == 0) {
                msg.writer().writeByte(player.enemies.size());
                for (Enemy enemy : player.enemies) {
                    Player p = PlayerManager.getInstance().findPlayerById(enemy.playerId);
                    if (p != null) {
                        enemy.name = p.name;
                        enemy.power = p.power;
                        enemy.isOnline = true;
                    } else {
                        enemy.isOnline = false;
                    }
                    msg.writer().writeInt(enemy.playerId);
                    msg.writer().writeUTF(enemy.name);
                    msg.writer().writeByte(enemy.gender);
                    msg.writer().writeLong(enemy.power);
                    msg.writer().writeBoolean(enemy.isOnline);
                }
            } else if (type == 2) {
                msg.writer().writeInt(playerId);
            }
            player.sendMessage(msg);
            msg.cleanup();
        } catch (Exception ex) {
            logger.error("summonDragon", ex);
        }
    }

    public void recoverSkill() {
        try {
            Message msg = messageInfo(MessagePlayerInfoName.RECOVERY_SKILL);
            player.sendMessage(msg);
            msg.cleanup();
        } catch (Exception ex) {
            logger.error("recoverSkill", ex);
        }
    }

    public void showMemberMiniGame(List<MemberMiniGame> members) {
        try {
            Message msg = new Message(MessageName.ADMIN_MINI_GAME);
            msg.writer().writeShort(members.size());
            for (MemberMiniGame member : members) {
                msg.writer().writeInt(member.playerId);
                msg.writer().writeUTF(member.name);
                msg.writer().writeByte(member.gender);
                msg.writer().writeBoolean(member.isOnline);
                msg.writer().writeUTF(member.info);
            }
            player.sendMessage(msg);
            msg.cleanup();
        } catch (Exception ex) {
            logger.error("showMemberMiniGame", ex);
        }
    }

    public void showListTournamentAthlete(List<TournamentAthlete> members) {
        try {
            Message msg = new Message(MessageName.ADMIN_MINI_GAME);
            msg.writer().writeShort(members.size());
            for (TournamentAthlete member : members) {
                msg.writer().writeInt(member.playerId);
                msg.writer().writeUTF(member.name);
                msg.writer().writeByte(member.gender);
                msg.writer().writeBoolean(member.isOnline);
                msg.writer().writeUTF("SCĐ: " + Utils.formatNumber(member.pointPro));
            }
            player.sendMessage(msg);
            msg.cleanup();
        } catch (Exception ex) {
            logger.error("showListTournamentAthlete", ex);
        }
    }

    public void showListReward(long now) {
        try {
            Message msg = new Message(MessageName.REWARD);
            msg.writer().writeShort(player.rewards.size());
            for (Reward reward : player.rewards) {
                msg.writer().writeUTF(reward.info);
                msg.writer().writeUTF(Utils.formatTime(reward.getExpiry(now)));
                List<Item> items = reward.items;
                msg.writer().writeByte(items.size());
                for (Item item : items) {
                    item.write(msg);
                }
            }
            player.sendMessage(msg);
            msg.cleanup();
        } catch (Exception ex) {
            logger.error("showListReward", ex);
        }
    }

    public void setPointShop(int point) {
        try {
            Message msg = messageInfo(MessagePlayerInfoName.POINT_SHOP);
            msg.writer().writeInt(point);
            player.sendMessage(msg);
            msg.cleanup();
        } catch (Exception ex) {
            logger.error("setPointShop", ex);
        }
    }

    public void showAchievement() {
        try {
            Message msg = messageInfo(MessagePlayerInfoName.ACHIEVEMENT);
            msg.writer().writeByte(player.achievements.size());
            for (Achievement achievement : player.achievements.values()) {
                msg.writer().writeUTF(achievement.template.name);
                msg.writer().writeUTF(achievement.template.description);
                msg.writer().writeInt(achievement.template.ruby);
                msg.writer().writeInt(achievement.template.maxParam);
                if (achievement.template.id == 0 || achievement.template.id == 1) {
                    achievement.param = player.level;
                }
                if (achievement.template.id == 10) {
                    achievement.param = player.magicBean.level;
                }
                if (achievement.template.id == 8) {
                    achievement.param = player.onlineMinuteTotal;
                }
                msg.writer().writeInt(achievement.param);
                msg.writer().writeBoolean(achievement.isReceived);
            }
            player.sendMessage(msg);
            msg.cleanup();
        } catch (Exception ex) {
            logger.error("showAchievement", ex);
        }
    }

    public void showMission(int type, List<IMission> missions) {
        try {
            Message msg = new Message(MessageName.MISSION_ACTION);
            msg.writer().writeByte(type);
            msg.writer().writeByte(missions.size());
            for (IMission mission : missions) {
                msg.writer().writeUTF(mission.getName());
                msg.writer().writeUTF(mission.getDescription());
                msg.writer().writeByte(mission.getType());
                List<Item> items = mission.getItems();
                msg.writer().writeByte(items.size());
                for (Item item : items) {
                    item.write(msg);
                }
            }
            player.sendMessage(msg);
            msg.cleanup();
        } catch (Exception ex) {
            logger.error("showGift", ex);
        }
    }

    public void blockStrangers() {
        try {
            Message msg = new Message(MessageName.BLOCK_STRANGER);
            msg.writer().writeBoolean(player.isBlockStrangers);
            player.sendMessage(msg);
            msg.cleanup();
        } catch (Exception ex) {
            logger.error("blockStrangers", ex);
        }
    }

    public void setListItemLight(boolean[] flags) {
        try {
            Message msg = messageInfo(MessagePlayerInfoName.ITEM_BAG_IS_LIGHT);
            msg.writer().writeByte(flags.length);
            for (boolean flag : flags) {
                msg.writer().writeBoolean(flag);
            }
            player.sendMessage(msg);
            msg.cleanup();
        } catch (Exception ex) {
            logger.error("setListItemLight", ex);
        }
    }

    public void sendIcon(int iconId, byte[] bytes) {
        try {
            Message msg = new Message(MessageName.REQUEST_ICON);
            msg.writer().writeShort(iconId);
            msg.writer().writeInt(bytes.length);
            msg.writer().write(bytes);
            session.sendMessage(msg);
            msg.cleanup();
        } catch (Exception ex) {
            logger.error("sendIcon", ex);
        }
    }

    public void setHideMark() {
        try {
            Message msg = new Message(MessageName.HIDE_MARK);
            msg.writer().writeBoolean(player.isHideMark);
            player.sendMessage(msg);
            msg.cleanup();
        } catch (Exception ex) {
            logger.error("showMark", ex);
        }
    }

    public void setCoolDownReduction(int param) {
        try {
            Message msg = messageInfo(MessagePlayerInfoName.SET_COOL_DOWN_REDUCTION);
            msg.writer().writeByte(param);
            player.sendMessage(msg);
            msg.cleanup();
        } catch (Exception ex) {
            logger.error("setCoolDownReduction", ex);
        }
    }

    public void setAreaGoBack() {
        try {
            Message msg = new Message(MessageName.SET_AREA_GO_BACK);
            msg.writer().writeBoolean(player.zoneGoBack != null);
            player.sendMessage(msg);
            msg.cleanup();
        } catch (Exception ex) {
            logger.error("setAreaGoBack", ex);
        }
    }

    public void setTimeCanUseForSkill(Skill skill) {
        try {
            Message msg = messageInfo(MessagePlayerInfoName.SET_TIME_CAN_USE_FOR_SKILL);
            msg.writer().writeByte(skill.template.id);
            msg.writer().writeLong(skill.timeCanUse);
            player.sendMessage(msg);
            msg.cleanup();
        } catch (Exception ex) {
            logger.error("setTimeCanUseForSkill", ex);
        }
    }

    public void messageTime(int id, long time, String text) {
        try {
            Message msg = new Message(MessageName.MESSAGE_TIME);
            DataOutputStream ds = msg.writer();
            ds.writeShort(id);
            ds.writeLong(time);
            if (time != 0) {
                ds.writeUTF(text);
            }
            ds.flush();
            player.sendMessage(msg);
            msg.cleanup();
        } catch (Exception ex) {
            logger.error("messageTime", ex);
        }
    }

    public void setIntrinsic() {
        try {
            Message msg = new Message(MessageName.INTRINSIC);
            DataOutputStream ds = msg.writer();
            ds.writeByte(0);
            ds.writeByte(player.intrinsics.size());
            for (Intrinsic intrinsic : player.intrinsics.values()) {
                ds.writeByte(intrinsic.template.id);
                ds.writeShort(intrinsic.template.iconId);
                String[] info = intrinsic.getInfo();
                ds.writeUTF(info[0]);
                ds.writeUTF(info[1]);
                ds.writeShort(intrinsic.param);
            }
            ds.flush();
            player.sendMessage(msg);
            msg.cleanup();
        } catch (Exception ex) {
            logger.error("setIntrinsic", ex);
        }
    }

    public void updateIntrinsic(Intrinsic intrinsic) {
        try {
            Message msg = new Message(MessageName.INTRINSIC);
            DataOutputStream ds = msg.writer();
            ds.writeByte(1);
            ds.writeByte(intrinsic.template.id);
            ds.writeShort(intrinsic.param);
            ds.flush();
            player.sendMessage(msg);
            msg.cleanup();
        } catch (Exception ex) {
            logger.error("updateIntrinsic", ex);
        }
    }

    public void setCoolDownIntrinsic(Skill skill) {
        try {
            Message msg = messageInfo(MessagePlayerInfoName.SET_COOL_DOWN_INTRINSIC);
            msg.writer().writeByte(skill.template.id);
            msg.writer().writeShort(skill.coolDownIntrinsic);
            player.sendMessage(msg);
            msg.cleanup();
        } catch (Exception ex) {
            logger.error("setCoolDownIntrinsic", ex);
        }
    }

}
