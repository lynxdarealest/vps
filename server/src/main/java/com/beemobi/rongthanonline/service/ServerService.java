package com.beemobi.rongthanonline.service;

import com.beemobi.rongthanonline.data.OrderData;
import com.beemobi.rongthanonline.data.PlayerData;
import com.beemobi.rongthanonline.dragon.Dragon;
import com.beemobi.rongthanonline.entity.player.Player;
import com.beemobi.rongthanonline.entity.player.PlayerManager;
import com.beemobi.rongthanonline.entity.player.json.LockInfo;
import com.beemobi.rongthanonline.event.Event;
import com.beemobi.rongthanonline.item.ItemManager;
import com.beemobi.rongthanonline.item.ItemName;
import com.beemobi.rongthanonline.network.Message;
import com.beemobi.rongthanonline.network.MessageName;
import com.beemobi.rongthanonline.repository.GameRepository;
import com.beemobi.rongthanonline.server.Server;
import com.beemobi.rongthanonline.util.Utils;
import com.google.gson.reflect.TypeToken;
import org.apache.log4j.Logger;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class ServerService {
    private static final Logger logger = Logger.getLogger(ServerService.class);

    public final PlayerManager manager;

    public ServerService(PlayerManager manager) {
        this.manager = manager;
    }

    public void chatGlobal(Player player, String content) {
        manager.lock.readLock().lock();
        try {
            for (Player p : manager.players.values()) {
                try {
                    Message message = new Message(MessageName.CHAT_GLOBAL);
                    message.writer().writeInt(player.id);
                    if (p.id != player.id) {
                        message.writer().writeUTF(player.name);
                        message.writer().writeShort(player.head);
                    }
                    message.writer().writeUTF(content);
                    p.sendMessage(message);
                    message.cleanup();
                } catch (Exception ex) {
                    logger.error("chatGlobal", ex);
                }
            }
        } finally {
            manager.lock.readLock().unlock();
        }
    }

    public void serverChat(String content) {
        manager.lock.readLock().lock();
        try {
            for (Player player : manager.players.values()) {
                try {
                    Message message = new Message(MessageName.SERVER_CHAT);
                    message.writer().writeUTF(content);
                    player.sendMessage(message);
                    message.cleanup();
                } catch (Exception ex) {
                    logger.error("serverChat", ex);
                }
            }
        } finally {
            manager.lock.readLock().unlock();
        }
    }

    public void serverChat(Player player, String content) {
        try {
            Message message = new Message(MessageName.SERVER_CHAT);
            message.writer().writeUTF(content);
            player.sendMessage(message);
            message.cleanup();
        } catch (Exception ex) {
            logger.error("serverChat", ex);
        }
    }

    public void serverNotify(String content) {
        manager.lock.readLock().lock();
        try {
            for (Player player : manager.players.values()) {
                if (player.service != null) {
                    player.service.addInfo(2, content); // Try 2 for White/Blue?
                }
            }
        } finally {
            manager.lock.readLock().unlock();
        }
    }

    public void serverDialog(String content) {
        manager.lock.readLock().lock();
        try {
            for (Player player : manager.players.values()) {
                if (player.service != null) {
                    player.service.startDialogOk(content);
                }
            }
        } finally {
            manager.lock.readLock().unlock();
        }
    }

    public void hideDragon(Dragon dragon) {
        manager.lock.readLock().lock();
        try {
            for (Player player : manager.players.values()) {
                player.service.summonDragon(dragon, false);
            }
        } finally {
            manager.lock.readLock().unlock();
        }
    }

    public Integer lockPlayer(String name, int hourLock, String info, boolean isMulti, int type) {
        int num = 0;
        long time = System.currentTimeMillis() + hourLock * 60L * 60L * 1000L;
        try {
            Player player = PlayerManager.getInstance().findPlayerByName(name);
            if (player != null) {
                List<Player> players = new ArrayList<>();
                if (isMulti) {
                    players.addAll(PlayerManager.getInstance().findListPlayerInIp(player.session.ip));
                } else {
                    players.add(player);
                }
                for (Player p : players) {
                    try {
                        if (p.lockInfo == null) {
                            p.lockInfo = new LockInfo();
                        }
                        if (type == 0) {
                            // lock login
                            p.lockInfo.loginTime = time;
                            p.lockInfo.loginInfo = info;
                        } else if (type == 1) {
                            // lock chat
                            p.lockInfo.chatTime = time;
                            p.lockInfo.chatInfo = info;
                        } else {
                            // lock trade
                            p.lockInfo.tradeTime = time;
                            p.lockInfo.tradeInfo = info;
                        }
                        GameRepository.getInstance().playerData.setLockInfo(p.id, Utils.gson.toJson(p.lockInfo));
                        p.session.disconnect();
                        num++;
                    } catch (Exception ignored) {
                    }
                }
            } else {
                List<PlayerData> players = GameRepository.getInstance().playerData.findByName(name);
                for (PlayerData data : players) {
                    try {
                        LockInfo lockInfo = Utils.gson.fromJson(data.lockInfo, new TypeToken<LockInfo>() {
                        }.getType());
                        if (lockInfo == null) {
                            lockInfo = new LockInfo();
                        }
                        if (type == 0) {
                            // lock login
                            lockInfo.loginTime = time;
                            lockInfo.loginInfo = info;
                        } else if (type == 1) {
                            // lock chat
                            lockInfo.chatTime = time;
                            lockInfo.chatInfo = info;
                        } else {
                            // lock trade
                            lockInfo.tradeTime = time;
                            lockInfo.tradeInfo = info;
                        }
                        GameRepository.getInstance().playerData.setLockInfo(data.id, Utils.gson.toJson(lockInfo));
                        num++;
                    } catch (Exception ignored) {
                    }
                }
            }
        } catch (Exception ex) {
            logger.error("lockPlayer", ex);
        }
        return num;
    }

    public Integer createOrder(long orderId, int userId, int diamond, long coin, String orderCode, int type) {
        try {
            if (type > 4 || (diamond <= 0 && type <= 3) || (coin <= 0 && type == 4)) {
                return -1;
            }
            List<OrderData> orders = GameRepository.getInstance().orderData.findByOrderIdAndType(orderId, type);
            if (!orders.isEmpty()) {
                return 0;
            }
            OrderData order = new OrderData();
            order.orderId = orderId;
            order.userId = userId;
            order.diamond = diamond;
            order.coin = coin;
            order.type = type;
            order.createTime = new Timestamp(System.currentTimeMillis());
            order.status = 0;
            order.orderCode = orderCode;
            order.server = Server.ID;

            List<Player> players = PlayerManager.getInstance().findPlayerByUserId(userId);
            if (players.size() > 1) {
                for (Player player : players) {
                    player.session.disconnect();
                }
                return 2;
            }
            if (players.size() == 1) {
                Player player = players.get(0);
                if (type == 4) {
                    player.upXu(order.coin);
                    order.status = 1;
                    order.updateTime = new Timestamp(System.currentTimeMillis());
                } else {
                    int index = player.getIndexItemBagEmpty();
                    if (index != -1) {
                        int count = 0;
                        if (order.type != 3) {
                            count += order.diamond / 10;
                        }
                        player.upDiamond(order.diamond);
                        player.diamondRecharge += order.diamond;
                        order.status = 1;
                        order.updateTime = new Timestamp(System.currentTimeMillis());
                        if (count > 0) {
                            player.addItem(ItemManager.getInstance().createItem(ItemName.THE_KIM_CUONG, count, true), true);
                        }
                        if (diamond >= 50) {
                            player.completeMissionWeek();
                        }
                        player.upPointAchievement(9, diamond);
                        player.upParamMissionRecharge(diamond);
                        player.service.startDialogOk(String.format("Bạn đã nạp thành công %d kim cương", order.diamond));
                        if (Event.isEvent()) {
                            Event.event.rewardRecharge(player, diamond);
                        }
                    }
                }
            }
            GameRepository.getInstance().orderData.save(order);
            return 1;
        } catch (Exception ex) {
            logger.error("createOrder", ex);
        }
        return 2;
    }
}
