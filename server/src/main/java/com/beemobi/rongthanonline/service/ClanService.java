package com.beemobi.rongthanonline.service;

import com.beemobi.rongthanonline.clan.Clan;
import com.beemobi.rongthanonline.clan.ClanMember;
import com.beemobi.rongthanonline.entity.player.Player;
import com.beemobi.rongthanonline.entity.player.PlayerManager;
import com.beemobi.rongthanonline.network.Message;
import com.beemobi.rongthanonline.network.MessageName;
import org.apache.log4j.Logger;

public class ClanService {
    private static final Logger logger = Logger.getLogger(ClanService.class);
    private final Clan clan;

    public ClanService(Clan clan) {
        this.clan = clan;
    }

    public void addInfo(int type, String info) {
        clan.lock.readLock().lock();
        try {
            for (ClanMember member : clan.members) {
                Player player = PlayerManager.getInstance().findPlayerById(member.playerId);
                if (player != null) {
                    player.addInfo(type, info);
                }
            }
        } finally {
            clan.lock.readLock().unlock();
        }
    }

    public void setSlogan() {
        clan.lock.readLock().lock();
        try {
            for (ClanMember member : clan.members) {
                Player player = PlayerManager.getInstance().findPlayerById(member.playerId);
                if (player != null) {
                    player.service.setClanSlogan(clan.slogan);
                    player.addInfo(Player.INFO_YELLOW, "Khẩu hiệu của bang hội đã được thay đổi");
                }
            }
        } finally {
            clan.lock.readLock().unlock();
        }
    }

    public void setNotification() {
        clan.lock.readLock().lock();
        try {
            for (ClanMember member : clan.members) {
                Player player = PlayerManager.getInstance().findPlayerById(member.playerId);
                if (player != null) {
                    player.service.setClanNotification(clan.getNotification());
                    player.addInfo(Player.INFO_YELLOW, "Thông báo của bang hội đã được thay đổi");
                }
            }
        } finally {
            clan.lock.readLock().unlock();
        }
    }

    public void setCoin() {
        clan.lock.readLock().lock();
        try {
            for (ClanMember member : clan.members) {
                Player player = PlayerManager.getInstance().findPlayerById(member.playerId);
                if (player != null) {
                    player.service.setClanCoin(clan.coin);
                }
            }
        } finally {
            clan.lock.readLock().unlock();
        }
    }

    public void chatClan(Player player, String content) {
        clan.lock.readLock().lock();
        try {
            for (ClanMember member : clan.members) {
                Player p = PlayerManager.getInstance().findPlayerById(member.playerId);
                if (p != null) {
                    try {
                        Message message = new Message(MessageName.CHAT_CLAN);
                        message.writer().writeInt(player.id);
                        message.writer().writeUTF(content);
                        message.writer().writeUTF(player.name);
                        message.writer().writeShort(player.head);
                        p.sendMessage(message);
                        message.cleanup();
                    } catch (Exception ex) {
                        logger.error("chatClan", ex);
                    }
                }
            }
        } finally {
            clan.lock.readLock().unlock();
        }
    }
}
