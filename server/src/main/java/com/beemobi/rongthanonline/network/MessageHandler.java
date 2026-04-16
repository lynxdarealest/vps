package com.beemobi.rongthanonline.network;

import com.beemobi.rongthanonline.entity.player.Player;
import com.beemobi.rongthanonline.lucky.LuckyManager;
import com.beemobi.rongthanonline.server.ServerMaintenance;
import com.beemobi.rongthanonline.upgrade.Upgrade;
import com.beemobi.rongthanonline.upgrade.UpgradeManager;
import org.apache.log4j.Logger;

public class MessageHandler {
    private static final Logger logger = Logger.getLogger(MessageHandler.class);
    private final Session session;
    private Player player;

    public MessageHandler(Session client) {
        this.session = client;
    }

    public void onMessage(Message message) {
        if (!session.isConnected) {
            return;
        }
        if (ServerMaintenance.getInstance().isRunning && ServerMaintenance.getInstance().seconds <= 120) {
            return;
        }
        int id = message.getId();
        try {
            switch (id) {
                case MessageName.CONNECT_SERVER: {
                    session.sendKey();
                    session.service.versionSource();
                    break;
                }

                case MessageName.LOGIN: {
                    session.login(message);
                    break;
                }

                case MessageName.UPDATE_DATA: {
                    session.updateAction(message);
                    break;
                }

                case MessageName.FINISH_LOAD_MAP: {
                    if (player != null) {
                        player.finishLoadMap();
                    }
                    break;
                }

                case MessageName.REQUEST_CHANGE_MAP: {
                    if (player != null) {
                        player.requestChangeMap();
                    }
                    break;
                }

                case MessageName.REQUEST_CHANGE_AREA: {
                    if (player != null) {
                        player.requestChangeZone(message);
                    }
                    break;
                }


                case MessageName.PLAYER_MOVE: {
                    if (player != null) {
                        player.move(message);
                    }
                    break;
                }

                case MessageName.CHAT_PLAYER:
                    if (player != null) {
                        player.chatPlayer(message);
                    }
                    break;

                case MessageName.CHAT_PUBLIC: {
                    if (player != null) {
                        player.chatPublic(message);
                    }
                    break;
                }

                case MessageName.CHAT_GLOBAL: {
                    if (player != null) {
                        player.chatGlobal(message);
                    }
                    break;
                }

                case MessageName.CHAT_TEAM: {
                    // chat team
                    if (player != null) {
                        player.chatTeam(message);
                    }
                    break;
                }

                case MessageName.CHAT_CLAN: {
                    // chat clan
                    if (player != null) {
                        player.chatClan(message);
                    }
                    break;
                }

                case MessageName.OPEN_NPC: {
                    if (player != null) {
                        player.openNpc(message);
                    }
                    break;
                }

                case MessageName.TRADE_ACTION: {
                    if (player != null) {
                        player.tradeAction.action(message);
                    }
                    break;
                }

                case MessageName.CONFIRM_MENU: {
                    if (player != null) {
                        player.confirmMenu(message);
                    }
                    break;
                }

                case MessageName.ITEM_ACTION: {
                    if (player != null) {
                        player.itemAction(message);
                    }
                    break;
                }

                case MessageName.CLIENT_INPUT:
                    if (player != null) {
                        player.clientInput(message);
                    }
                    break;

                case MessageName.BUY_ITEM: {
                    if (player != null) {
                        player.buyItem(message);
                    }
                    break;
                }

                case MessageName.TEAM_ACTION: {
                    if (player != null) {
                        player.teamAction.action(message);
                    }
                    break;
                }

                case MessageName.CLAN_INFO:
                    if (player != null) {
                        player.clanAction.action(message);
                    }
                    break;

                case MessageName.UP_BASE_POINT: {
                    if (player != null) {
                        player.upBasePoint(message);
                    }
                    break;
                }

                case MessageName.UP_SKILL_POINT: {
                    if (player != null) {
                        player.upSkillPoint(message);
                    }
                    break;
                }

                case MessageName.SAVE_KEY_SKILL: {
                    if (player != null) {
                        player.saveKeySkill(message);
                    }
                    break;
                }

                case MessageName.SELECT_SKILL: {
                    if (player != null) {
                        player.selectSkill(message);
                    }
                    break;
                }

                case MessageName.UPGRADE: {
                    if (player != null && player.upgradeType != null) {
                        Upgrade upgrade = UpgradeManager.getInstance().upgrades.get(player.upgradeType);
                        if (upgrade != null) {
                            upgrade.upgrade(message, player);
                        }
                    }
                    break;
                }

                case MessageName.RETURN_TOWN_FROM_DIE: {
                    if (player != null) {
                        player.returnTownFromDead();
                    }
                    break;
                }

                case MessageName.WAKE_UP_FROM_DIE: {
                    if (player != null) {
                        player.wakeUpFromDead();
                    }
                    break;
                }

                case MessageName.USE_SKILL: {
                    if (player != null) {
                        player.attack(message);
                    }
                    break;
                }

                case MessageName.PLAYER_START_USE_ULTIMATE: {
                    if (player != null) {
                        player.useSkill(message);
                    }
                    break;
                }

                case MessageName.CHANGE_PK: {
                    if (player != null) {
                        player.changePk(message);
                    }
                    break;
                }

                case MessageName.ME_PICK_ITEM: {
                    if (player != null) {
                        player.pickItem(message);
                    }
                    break;
                }

                case MessageName.SELECT_MAP_SPACESHIP: {
                    if (player != null) {
                        player.selectMapSpaceship(message);
                    }
                    break;
                }

                case MessageName.PLAYER_SOLO: {
                    if (player != null) {
                        player.sendInviteSolo(message);
                    }
                    break;
                }

                case MessageName.VIEW_PLAYER: {
                    if (player != null) {
                        player.requestInfoPlayer(message);
                    }
                    break;
                }

                case MessageName.CREATE_PALYER: {
                    if (player == null) {
                        session.createPlayer(message);
                    }
                    break;
                }

                case MessageName.REGISTER_USER: {
                    if (player == null) {
                        session.register(message);
                    }
                    break;
                }

                case MessageName.DISCIPLE_INFO: {
                    if (player != null) {
                        player.discipleInfo(message);
                    }
                    break;
                }

                case MessageName.FRIEND: {
                    if (player != null) {
                        player.friendAction.action(message);
                    }
                    break;
                }

                case MessageName.ENEMY: {
                    if (player != null) {
                        player.enemyAction.action(message);
                    }
                    break;
                }

                case MessageName.ADMIN_MINI_GAME: {
                    if (player != null) {
                        player.adminMiniGame(message);
                    }
                    break;
                }

                case MessageName.REWARD: {
                    if (player != null) {
                        player.reward(message);
                    }
                    break;
                }

                case MessageName.RECEIVE_ACHIEVEMENT: {
                    if (player != null) {
                        player.receiveAchievement(message);
                    }
                    break;
                }

                case MessageName.BLOCK_STRANGER: {
                    if (player != null) {
                        player.blockStrangers();
                    }
                    break;
                }

                case MessageName.MISSION_ACTION: {
                    if (player != null) {
                        player.missionAction.action(message);
                    }
                    break;
                }

                case MessageName.CONSIGNMENT: {
                    if (player != null) {
                        player.consignmentAction.action(message);
                    }
                    break;
                }

                case MessageName.REQUEST_ICON: {
                    if (player != null || session.user != null) {
                        session.requestIcon(message);
                    }
                    break;
                }

                case MessageName.HIDE_MARK: {
                    if (player != null) {
                        player.setHideMark();
                    }
                    break;
                }

                case MessageName.SET_AREA_GO_BACK: {
                    if (player != null) {
                        player.setGoBack();
                    }
                    break;
                }

                case MessageName.TELEPORT_TO_PLAYER: {
                    if (player != null) {
                        player.teleportToPlayer(message);
                    }
                    break;
                }

                case MessageName.INTRINSIC: {
                    if (player != null) {
                        player.intrinsic(message);
                    }
                    break;
                }

                case MessageName.LUCKY_PICK_ME: {
                    if (player != null) {
                        LuckyManager.getInstance().luckyCoin.read(message, player);
                    }
                    break;
                }

                case MessageName.TAB_ACTION: {
                    if (player != null) {
                        player.tabAction(message);
                    }
                    break;
                }

                case MessageName.TAB_LUCKY: {
                    if (player != null) {
                        LuckyManager.getInstance().luckyBox.next(player);
                    }
                    break;
                }

                case MessageName.VIEW_TOP: {
                    if (player != null) {
                        player.viewTop();
                    }
                    break;
                }

                case MessageName.VIEW_LUCKY: {
                    if (player != null) {
                        player.viewLucky();
                    }
                    break;
                }

            }
        } catch (Exception ex) {
            logger.error(String.format("failed! - message: %d", id), ex);
        }
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public void onDisconnected() {
        logger.debug(String.format("Client %d: Disconnect! [%s]", session.id, session.ip));
    }

    public void onConnectOK() {
        logger.debug(String.format("Client %d: Connect success! [%s]", session.id, session.ip));
    }

    public void close() {

    }
}
