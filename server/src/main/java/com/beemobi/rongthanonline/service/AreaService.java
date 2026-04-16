package com.beemobi.rongthanonline.service;

import com.beemobi.rongthanonline.dragon.Dragon;
import com.beemobi.rongthanonline.effect.Effect;
import com.beemobi.rongthanonline.entity.Entity;
import com.beemobi.rongthanonline.entity.monster.Monster;
import com.beemobi.rongthanonline.entity.monster.big.BigMonster;
import com.beemobi.rongthanonline.entity.monster.pet.Pet;
import com.beemobi.rongthanonline.entity.player.Player;
import com.beemobi.rongthanonline.item.ItemMap;
import com.beemobi.rongthanonline.map.Zone;
import com.beemobi.rongthanonline.network.Message;
import com.beemobi.rongthanonline.network.MessageName;
import com.beemobi.rongthanonline.npc.Npc;
import org.apache.log4j.Logger;

import java.util.HashMap;

public class AreaService {
    private static final Logger logger = Logger.getLogger(AreaService.class);

    private final Zone zone;

    public AreaService(Zone zone) {
        this.zone = zone;
    }

    public void refreshPlayerPart(Player player) {
        zone.lockPlayer.readLock().lock();
        try {
            for (Player p : zone.players) {
                if (p != player && p.isPlayer()) {
                    try {
                        Message msg = new Message(MessageName.AREA_REFRESH_PART_PLAYER);
                        msg.writer().writeInt(player.id);
                        msg.writer().writeShort(player.head);
                        msg.writer().writeShort(player.body);
                        msg.writer().writeShort(player.mount);
                        msg.writer().writeShort(player.bag);
                        msg.writer().writeShort(player.medal);
                        msg.writer().writeShort(player.aura);
                        p.sendMessage(msg);
                        msg.cleanup();
                    } catch (Exception ex) {
                        logger.error("refreshPlayerPart", ex);
                    }
                }
            }
        } finally {
            zone.lockPlayer.readLock().unlock();
        }
    }

    public void refreshHp(Player player) {
        zone.lockPlayer.readLock().lock();
        try {
            for (Player p : zone.players) {
                if (p != player && p.isPlayer()) {
                    try {
                        Message msg = new Message(MessageName.AREA_REFRESH_HP_PLAYER);
                        msg.writer().writeInt(player.id);
                        msg.writer().writeLong(player.maxHp);
                        msg.writer().writeLong(player.hp);
                        p.sendMessage(msg);
                        msg.cleanup();
                    } catch (Exception ex) {
                        logger.error("refreshHp", ex);
                    }
                }
            }
        } finally {
            zone.lockPlayer.readLock().unlock();
        }
    }

    public void addPlayer(Player player) {
        zone.lockPlayer.readLock().lock();
        try {
            for (Player p : zone.players) {
                if (p != player && p.isPlayer()) {
                    p.service.addPlayer(player);
                }
            }
        } finally {
            zone.lockPlayer.readLock().unlock();
        }
    }

    public void removePlayer(Player player) {
        zone.lockPlayer.readLock().lock();
        try {
            for (Player p : zone.players) {
                if (p.isPlayer()) {
                    try {
                        Message msg = new Message(MessageName.REMOVE_PLAYER);
                        msg.writer().writeInt(player.id);
                        p.sendMessage(msg);
                        msg.cleanup();
                    } catch (Exception ex) {
                        logger.error("removePlayer", ex);
                    }
                }
            }
        } finally {
            zone.lockPlayer.readLock().unlock();
        }
    }

    public void move(Player player) {
        zone.lockPlayer.readLock().lock();
        try {
            for (Player p : zone.players) {
                if (p != player && p.isPlayer()) {
                    try {
                        Message msg = new Message(MessageName.PLAYER_MOVE);
                        msg.writer().writeInt(player.id);
                        msg.writer().writeShort(player.x);
                        msg.writer().writeShort(player.y);
                        p.sendMessage(msg);
                        msg.cleanup();
                    } catch (Exception e) {
                        logger.error("move", e);
                    }
                }
            }
        } finally {
            zone.lockPlayer.readLock().unlock();
        }
    }

    public void setPosition(Player player) {
        zone.lockPlayer.readLock().lock();
        try {
            for (Player p : zone.players) {
                if (p.isPlayer()) {
                    try {
                        Message msg = new Message(MessageName.SET_POSITION_PLAYER);
                        msg.writer().writeInt(player.id);
                        msg.writer().writeShort(player.x);
                        msg.writer().writeShort(player.y);
                        p.sendMessage(msg);
                        msg.cleanup();
                    } catch (Exception ex) {
                        logger.error("resetPosition", ex);
                    }
                }
            }
        } finally {
            zone.lockPlayer.readLock().unlock();
        }
    }

    public void chatPublic(Player player, String content) {
        zone.lockPlayer.readLock().lock();
        try {
            for (Player p : zone.players) {
                if (p.isPlayer()) {
                    try {
                        Message msg = new Message(MessageName.CHAT_PUBLIC);
                        msg.writer().writeInt(player.id);
                        msg.writer().writeUTF(content);
                        p.sendMessage(msg);
                        msg.cleanup();
                    } catch (Exception ex) {
                        logger.error("chatPublic", ex);
                    }
                }
            }
        } finally {
            zone.lockPlayer.readLock().unlock();
        }
    }

    public void updateClan(Player player) {
        zone.lockPlayer.readLock().lock();
        try {
            for (Player p : zone.players) {
                if (p != player && p.isPlayer()) {
                    try {
                        Message msg = new Message(MessageName.UPDATE_CLAN_IN_AREA);
                        msg.writer().writeInt(player.id);
                        if (player.clan == null) {
                            msg.writer().writeInt(-1);
                        } else {
                            msg.writer().writeInt(player.clan.id);
                            msg.writer().writeUTF(player.clan.name);
                        }
                        p.sendMessage(msg);
                        msg.cleanup();
                    } catch (Exception ex) {
                        logger.error("updateClan", ex);
                    }
                }
            }
        } finally {
            zone.lockPlayer.readLock().unlock();
        }
    }

    public void monsterAttack(Monster monster, Entity focus, long damage) {
        zone.lockPlayer.readLock().lock();
        try {
            for (Player p : zone.players) {
                if (p.isPlayer()) {
                    try {
                        Message msg = new Message(MessageName.MONSTER_ATTACK);
                        msg.writer().writeInt(monster.id);
                        msg.writer().writeByte(focus instanceof Player ? 0 : 1);
                        msg.writer().writeInt(focus.id);
                        msg.writer().writeLong(damage);
                        p.sendMessage(msg);
                        msg.cleanup();
                    } catch (Exception ex) {
                        logger.error("monsterAttack", ex);
                    }
                }
            }
        } finally {
            zone.lockPlayer.readLock().unlock();
        }
    }

    public void playerDie(Player player) {
        zone.lockPlayer.readLock().lock();
        try {
            for (Player p : zone.players) {
                if (p != player && p.isPlayer()) {
                    try {
                        Message msg = new Message(MessageName.PLAYER_DIE);
                        msg.writer().writeInt(player.id);
                        msg.writer().writeShort(player.x);
                        msg.writer().writeShort(player.y);
                        p.sendMessage(msg);
                        msg.cleanup();
                    } catch (Exception ex) {
                        logger.error("playerDie", ex);
                    }
                }
            }
        } finally {
            zone.lockPlayer.readLock().unlock();
        }
    }

    public void playerWakeUpFromDead(Player player) {
        zone.lockPlayer.readLock().lock();
        try {
            for (Player p : zone.players) {
                if (p != player && p.isPlayer()) {
                    try {
                        Message msg = new Message(MessageName.WAKE_UP_FROM_DIE);
                        msg.writer().writeInt(player.id);
                        msg.writer().writeShort(player.x);
                        msg.writer().writeShort(player.y);
                        msg.writer().writeLong(player.hp);
                        msg.writer().writeLong(player.mp);
                        p.sendMessage(msg);
                        msg.cleanup();
                    } catch (Exception ex) {
                        logger.error("playerWakeUpFromDead", ex);
                    }
                }
            }
        } finally {
            zone.lockPlayer.readLock().unlock();
        }
    }

    public void addTeleport(Player player) {
        zone.lockPlayer.readLock().lock();
        try {
            for (Player p : zone.players) {
                if (p.isPlayer()) {
                    try {
                        Message msg = new Message(MessageName.ADD_TELEPORT);
                        msg.writer().writeInt(player.id);
                        p.sendMessage(msg);
                        msg.cleanup();
                    } catch (Exception ex) {
                        logger.error("addTeleport", ex);
                    }
                }
            }
        } finally {
            zone.lockPlayer.readLock().unlock();
        }
    }

    public void useSkill(Player player) {
        zone.lockPlayer.readLock().lock();
        try {
            for (Player p : zone.players) {
                if (p != player && p.isPlayer()) {
                    try {
                        Message msg = new Message(MessageName.PLAYER_USE_SKILL_IN_AREA);
                        msg.writer().writeInt(player.id);
                        if (player.focus == null) {
                            msg.writer().writeByte(-1);
                        } else if (player.focus instanceof Monster) {
                            msg.writer().writeByte(1);
                            msg.writer().writeInt(player.focus.id);
                        } else if (player.focus instanceof Player) {
                            msg.writer().writeByte(0);
                            msg.writer().writeInt(player.focus.id);
                        }
                        msg.writer().writeShort(player.mySkill.getListPaint().next());
                        p.sendMessage(msg);
                        msg.cleanup();
                    } catch (Exception ex) {
                        logger.error("useSkill", ex);
                    }
                }
            }
        } finally {
            zone.lockPlayer.readLock().unlock();
        }
    }

    public void monsterInjure(Monster monster, long hpInjure, boolean isCritical) {
        zone.lockPlayer.readLock().lock();
        try {
            for (Player player : zone.players) {
                if (player.isPlayer()) {
                    try {
                        Message msg = new Message(MessageName.MONSTER_INJURE);
                        msg.writer().writeInt(monster.id);
                        msg.writer().writeLong(hpInjure);
                        if (hpInjure != 0) {
                            msg.writer().writeLong(monster.hp);
                            msg.writer().writeBoolean(isCritical);
                        }
                        player.sendMessage(msg);
                        msg.cleanup();
                    } catch (Exception ex) {
                        logger.error("monsterInjure", ex);
                    }
                }
            }
        } finally {
            zone.lockPlayer.readLock().unlock();
        }
    }

    public void monsterStartDie(Monster monster, long hpInjure, boolean isCritical) {
        zone.lockPlayer.readLock().lock();
        try {
            for (Player player : zone.players) {
                if (player.isPlayer()) {
                    try {
                        Message msg = new Message(MessageName.MONSTER_START_DIE);
                        msg.writer().writeInt(monster.id);
                        msg.writer().writeLong(hpInjure);
                        msg.writer().writeBoolean(isCritical);
                        player.sendMessage(msg);
                        msg.cleanup();
                    } catch (Exception ex) {
                        logger.error("monsterStartDie", ex);
                    }
                }
            }
        } finally {
            zone.lockPlayer.readLock().unlock();
        }
    }

    public void monsterRespawn(Monster monster) {
        zone.lockPlayer.readLock().lock();
        try {
            for (Player player : zone.players) {
                if (player.isPlayer()) {
                    try {
                        Message msg = new Message(MessageName.MONSTER_RESPAWN);
                        msg.writer().writeInt(monster.id);
                        msg.writer().writeByte(monster.levelBoss.ordinal());
                        msg.writer().writeLong(monster.hp);
                        player.sendMessage(msg);
                        msg.cleanup();
                    } catch (Exception ex) {
                        logger.error("monsterStartDie", ex);
                    }
                }
            }
        } finally {
            zone.lockPlayer.readLock().unlock();
        }
    }

    public void playerInjure(Player player, long hpInjure, boolean isCritical) {
        zone.lockPlayer.readLock().lock();
        try {
            for (Player p : zone.players) {
                if (p.isPlayer()) {
                    try {
                        Message msg = new Message(MessageName.PLAYER_INJURE);
                        msg.writer().writeInt(player.id);
                        msg.writer().writeLong(hpInjure);
                        if (hpInjure != 0) {
                            msg.writer().writeBoolean(isCritical);
                        }
                        p.sendMessage(msg);
                        msg.cleanup();
                    } catch (Exception ex) {
                        logger.error("playerInjure", ex);
                    }
                }
            }
        } finally {
            zone.lockPlayer.readLock().unlock();
        }
    }

    public void setTypePk(Player player) {
        zone.lockPlayer.readLock().lock();
        try {
            for (Player p : zone.players) {
                if (p != player && p.isPlayer()) {
                    try {
                        Message msg = new Message(MessageName.SET_TYPE_PK);
                        msg.writer().writeInt(player.id);
                        msg.writer().writeByte(player.typePk);
                        p.sendMessage(msg);
                        msg.cleanup();
                    } catch (Exception ex) {
                        logger.error("setTypePk", ex);
                    }
                }
            }
        } finally {
            zone.lockPlayer.readLock().unlock();
        }
    }

    public void setTypeFlag(Player player) {
        zone.lockPlayer.readLock().lock();
        try {
            for (Player p : zone.players) {
                if (p != player && p.isPlayer()) {
                    try {
                        Message msg = new Message(MessageName.SET_TYPE_FLAG);
                        msg.writer().writeInt(player.id);
                        msg.writer().writeByte(player.typeFlag);
                        p.sendMessage(msg);
                        msg.cleanup();
                    } catch (Exception ex) {
                        logger.error("setTypeFlag", ex);
                    }
                }
            }
        } finally {
            zone.lockPlayer.readLock().unlock();
        }
    }

    public void stopCharge(Player player) {
        zone.lockPlayer.readLock().lock();
        try {
            for (Player p : zone.players) {
                if (p != player && p.isPlayer()) {
                    try {
                        Message msg = new Message(MessageName.PLAYER_STOP_WAIT_ULTIMATE);
                        msg.writer().writeInt(player.id);
                        p.sendMessage(msg);
                        msg.cleanup();
                    } catch (Exception ex) {
                        logger.error("stopWaitUltimate", ex);
                    }
                }
            }
        } finally {
            zone.lockPlayer.readLock().unlock();
        }
    }

    public void playerAddEffect(Player player, Effect effect) {
        zone.lockPlayer.readLock().lock();
        try {
            for (Player p : zone.players) {
                if (p != player && p.isPlayer()) {
                    try {
                        Message msg = new Message(MessageName.PLAYER_ADD_EFFECT);
                        msg.writer().writeInt(player.id);
                        msg.writer().writeShort(effect.template.id);
                        msg.writer().writeLong(effect.time);
                        p.sendMessage(msg);
                        msg.cleanup();
                    } catch (Exception ex) {
                        logger.error("playerAddEffect", ex);
                    }
                }
            }
        } finally {
            zone.lockPlayer.readLock().unlock();
        }
    }

    public void monsterAddEffect(Monster monster, Effect effect) {
        zone.lockPlayer.readLock().lock();
        try {
            for (Player p : zone.players) {
                if (p.isPlayer()) {
                    try {
                        Message msg = new Message(MessageName.MONSTER_ADD_EFFECT);
                        msg.writer().writeInt(monster.id);
                        msg.writer().writeShort(effect.template.id);
                        msg.writer().writeLong(effect.time);
                        p.sendMessage(msg);
                        msg.cleanup();
                    } catch (Exception ex) {
                        logger.error("monsterAddEffect", ex);
                    }
                }
            }
        } finally {
            zone.lockPlayer.readLock().unlock();
        }
    }

    public void addItemMap(ItemMap itemMap) {
        zone.lockPlayer.readLock().lock();
        try {
            for (Player p : zone.players) {
                if (p.isPlayer()) {
                    try {
                        Message msg = new Message(MessageName.ADD_ITEM_MAP);
                        msg.writer().writeInt(itemMap.id);
                        msg.writer().writeShort(itemMap.template.id);
                        msg.writer().writeShort(itemMap.x);
                        msg.writer().writeShort(itemMap.y);
                        msg.writer().writeInt(itemMap.playerId);
                        p.sendMessage(msg);
                        msg.cleanup();
                    } catch (Exception ex) {
                        logger.error("addItemMap", ex);
                    }
                }
            }
        } finally {
            zone.lockPlayer.readLock().unlock();
        }
    }

    public void removeItemMap(ItemMap itemMap) {
        zone.lockPlayer.readLock().lock();
        try {
            for (Player p : zone.players) {
                if (p.isPlayer()) {
                    try {
                        Message msg = new Message(MessageName.REMOVE_ITEM_MAP);
                        msg.writer().writeInt(itemMap.id);
                        p.sendMessage(msg);
                        msg.cleanup();
                    } catch (Exception ex) {
                        logger.error("removeItemMap", ex);
                    }
                }
            }
        } finally {
            zone.lockPlayer.readLock().unlock();
        }
    }

    public void pickItem(Player player, ItemMap itemMap) {
        zone.lockPlayer.readLock().lock();
        try {
            for (Player p : zone.players) {
                if (p.isPlayer() && p != player) {
                    try {
                        Message msg = new Message(MessageName.PICK_ITEM_MAP);
                        msg.writer().writeInt(player.id);
                        msg.writer().writeInt(itemMap.id);
                        p.sendMessage(msg);
                        msg.cleanup();
                    } catch (Exception ex) {
                        logger.error("playerPickItem", ex);
                    }
                }
            }
        } finally {
            zone.lockPlayer.readLock().unlock();
        }
    }

    public void levelUp(Player player) {
        zone.lockPlayer.readLock().lock();
        try {
            for (Player p : zone.players) {
                if (p.isPlayer() && p != player) {
                    try {
                        Message msg = new Message(MessageName.LEVEL_UP);
                        msg.writer().writeInt(player.id);
                        msg.writer().writeShort(player.level);
                        p.sendMessage(msg);
                        msg.cleanup();
                    } catch (Exception ex) {
                        logger.error("levelUp", ex);
                    }
                }
            }
        } finally {
            zone.lockPlayer.readLock().unlock();
        }
    }

    public void playerFlight(Player player1, Player player2) {
        zone.lockPlayer.readLock().lock();
        try {
            for (Player p : zone.players) {
                if (p.isPlayer()) {
                    try {
                        Message msg = new Message(MessageName.PLAYER_SOLO);
                        msg.writer().writeInt(player1.id);
                        msg.writer().writeInt(player2.id);
                        p.sendMessage(msg);
                        msg.cleanup();
                    } catch (Exception ex) {
                        logger.error("playerSolo", ex);
                    }
                }
            }
        } finally {
            zone.lockPlayer.readLock().unlock();
        }
    }

    public void setPlayerName(Player player) {
        zone.lockPlayer.readLock().lock();
        try {
            for (Player p : zone.players) {
                if (p.isPlayer()) {
                    try {
                        Message msg = new Message(MessageName.PLAYER_NAME);
                        msg.writer().writeInt(player.id);
                        msg.writer().writeUTF(player.name);
                        p.sendMessage(msg);
                        msg.cleanup();
                    } catch (Exception ex) {
                        logger.error("setPlayerName", ex);
                    }
                }
            }
        } finally {
            zone.lockPlayer.readLock().unlock();
        }
    }

    public void fusion(Player player, int type) {
        zone.lockPlayer.readLock().lock();
        try {
            for (Player p : zone.players) {
                if (p.isPlayer() && p != player) {
                    p.service.fusion(player, type);
                }
            }
        } finally {
            zone.lockPlayer.readLock().unlock();
        }
    }

    public void hideDragon(Dragon dragon) {
        zone.lockPlayer.readLock().lock();
        try {
            for (Player p : zone.players) {
                if (p.isPlayer()) {
                    p.service.summonDragon(dragon, false);
                }
            }
        } finally {
            zone.lockPlayer.readLock().unlock();
        }
    }

    public void summonDragon(Dragon dragon) {
        zone.lockPlayer.readLock().lock();
        try {
            for (Player p : zone.players) {
                if (p.isPlayer()) {
                    p.service.summonDragon(dragon, true);
                }
            }
        } finally {
            zone.lockPlayer.readLock().unlock();
        }
    }

    public void addNpc(Npc npc) {
        zone.lockPlayer.readLock().lock();
        try {
            for (Player p : zone.players) {
                if (p.isPlayer()) {
                    try {
                        Message msg = new Message(MessageName.ADD_NPC);
                        msg.writer().writeByte(npc.template.id);
                        msg.writer().writeShort(npc.x);
                        msg.writer().writeShort(npc.y);
                        p.sendMessage(msg);
                        msg.cleanup();
                    } catch (Exception ex) {
                        logger.error("addNpc", ex);
                    }
                }
            }
        } finally {
            zone.lockPlayer.readLock().unlock();
        }
    }

    public void removeNpc(Npc npc) {
        zone.lockPlayer.readLock().lock();
        try {
            for (Player p : zone.players) {
                if (p.isPlayer()) {
                    try {
                        Message msg = new Message(MessageName.REMOVE_NPC);
                        msg.writer().writeByte(npc.template.id);
                        p.sendMessage(msg);
                        msg.cleanup();
                    } catch (Exception ex) {
                        logger.error("removeNpc", ex);
                    }
                }
            }
        } finally {
            zone.lockPlayer.readLock().unlock();
        }
    }

    public void npcChat(Npc npc, String content) {
        zone.lockPlayer.readLock().lock();
        try {
            for (Player p : zone.players) {
                if (p.isPlayer()) {
                    npc.chat(p, content);
                }
            }
        } finally {
            zone.lockPlayer.readLock().unlock();
        }
    }

    public void setBag(Player player) {
        zone.lockPlayer.readLock().lock();
        try {
            for (Player p : zone.players) {
                if (p.isPlayer()) {
                    try {
                        Message msg = new Message(MessageName.UPDATE_CLAN_PART);
                        msg.writer().writeInt(player.id);
                        msg.writer().writeShort(player.bag);
                        p.sendMessage(msg);
                        msg.cleanup();
                    } catch (Exception ex) {
                        logger.error("setClanPart", ex);
                    }
                }
            }
        } finally {
            zone.lockPlayer.readLock().unlock();
        }
    }


    public void removeBigMonster(BigMonster monster) {
        zone.lockPlayer.readLock().lock();
        try {
            for (Player p : zone.players) {
                if (p.isPlayer()) {
                    try {
                        Message msg = new Message(MessageName.BIG_MONSTER);
                        msg.writer().writeByte(1);
                        msg.writer().writeInt(monster.id);
                        p.sendMessage(msg);
                        msg.cleanup();
                    } catch (Exception ex) {
                        logger.error("removeBigMonster", ex);
                    }
                }
            }
        } finally {
            zone.lockPlayer.readLock().unlock();
        }
    }

    public void removeMonster(Monster monster) {
        zone.lockPlayer.readLock().lock();
        try {
            for (Player p : zone.players) {
                if (p.isPlayer()) {
                    try {
                        Message msg = new Message(MessageName.REMOVE_MONSTER);
                        msg.writer().writeInt(monster.id);
                        p.sendMessage(msg);
                        msg.cleanup();
                    } catch (Exception ex) {
                        logger.error("removeMonster", ex);
                    }
                }
            }
        } finally {
            zone.lockPlayer.readLock().unlock();
        }
    }

    public void addMonster(Monster monster) {
        zone.lockPlayer.readLock().lock();
        try {
            for (Player p : zone.players) {
                if (p.isPlayer()) {
                    try {
                        Message msg = new Message(MessageName.ADD_MONSTER);
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
                        p.session.sendMessage(msg);
                        msg.cleanup();
                    } catch (Exception ex) {
                        logger.error("addMonster", ex);
                    }
                }
            }
        } finally {
            zone.lockPlayer.readLock().unlock();
        }
    }

    public void addBigMonster(BigMonster monster) {
        zone.lockPlayer.readLock().lock();
        try {
            for (Player p : zone.players) {
                if (p.isPlayer()) {
                    try {
                        Message msg = new Message(MessageName.BIG_MONSTER);
                        msg.writer().writeByte(0); // add big monster to area
                        msg.writer().writeShort(monster.template.id);
                        msg.writer().writeInt(monster.id);
                        msg.writer().writeShort(monster.level);
                        msg.writer().writeByte(monster.levelBoss.ordinal());
                        msg.writer().writeShort(monster.x);
                        msg.writer().writeShort(monster.y);
                        msg.writer().writeLong(monster.maxHp);
                        msg.writer().writeLong(monster.hp);
                        msg.writer().writeByte(monster.status.ordinal());
                        p.session.sendMessage(msg);
                        msg.cleanup();
                    } catch (Exception ex) {
                        logger.error("addBigMonster", ex);
                    }
                }
            }
        } finally {
            zone.lockPlayer.readLock().unlock();
        }
    }

    public void bigMonsterAttack(HashMap<Integer, Long> targets) {
        zone.lockPlayer.readLock().lock();
        try {
            for (Player p : zone.players) {
                if (p.isPlayer()) {
                    try {
                        Message msg = new Message(MessageName.BIG_MONSTER);
                        msg.writer().writeByte(2);
                        msg.writer().writeByte(targets.size());
                        for (Integer id : targets.keySet()) {
                            msg.writer().writeInt(id);
                            msg.writer().writeLong(targets.get(id));
                        }
                        p.sendMessage(msg);
                        msg.cleanup();
                    } catch (Exception ex) {
                        logger.error("bigMonsterAttack", ex);
                    }
                }
            }
        } finally {
            zone.lockPlayer.readLock().unlock();
        }
    }

    public void addEffectFly(int effectImageId, int x, int y, int speedX, int speedY) {
        zone.lockPlayer.readLock().lock();
        try {
            for (Player p : zone.players) {
                if (p.isPlayer()) {
                    try {
                        Message msg = new Message(MessageName.ADD_EFFECT_FLY);
                        msg.writer().writeShort(effectImageId);
                        msg.writer().writeShort(x);
                        msg.writer().writeShort(y);
                        msg.writer().writeShort(speedX);
                        msg.writer().writeShort(speedY);
                        p.sendMessage(msg);
                        msg.cleanup();
                    } catch (Exception ex) {
                        logger.error("addEffectFly", ex);
                    }
                }
            }
        } finally {
            zone.lockPlayer.readLock().unlock();
        }
    }

    public void move(Pet pet) {
        zone.lockPlayer.readLock().lock();
        try {
            for (Player p : zone.players) {
                if (p.isPlayer()) {
                    try {
                        Message msg = new Message(MessageName.PET_MOVE);
                        msg.writer().writeInt(pet.id);
                        msg.writer().writeShort(pet.x);
                        msg.writer().writeShort(pet.y);
                        msg.writer().writeByte(pet.master.speed);
                        p.sendMessage(msg);
                        msg.cleanup();
                    } catch (Exception ex) {
                        logger.error("petMove", ex);
                    }
                }
            }
        } finally {
            zone.lockPlayer.readLock().unlock();
        }
    }

    public void refreshHp(Monster monster) {
        zone.lockPlayer.readLock().lock();
        try {
            for (Player p : zone.players) {
                if (p.isPlayer()) {
                    try {
                        Message msg = new Message(MessageName.MONSTER_REFRESH_HP);
                        msg.writer().writeInt(monster.id);
                        msg.writer().writeLong(monster.maxHp);
                        msg.writer().writeLong(monster.hp);
                        p.sendMessage(msg);
                        msg.cleanup();
                    } catch (Exception ex) {
                        logger.error("refreshHp", ex);
                    }
                }
            }
        } finally {
            zone.lockPlayer.readLock().unlock();
        }
    }

}
