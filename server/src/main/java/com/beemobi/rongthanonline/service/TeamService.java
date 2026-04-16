package com.beemobi.rongthanonline.service;

import com.beemobi.rongthanonline.entity.player.Player;
import com.beemobi.rongthanonline.entity.player.PlayerManager;
import com.beemobi.rongthanonline.network.Message;
import com.beemobi.rongthanonline.network.MessageName;
import com.beemobi.rongthanonline.team.Team;
import com.beemobi.rongthanonline.team.TeamMember;
import org.apache.log4j.Logger;

public class TeamService {
    private static final Logger logger = Logger.getLogger(TeamService.class);
    public final Team team;

    public TeamService(Team team) {
        this.team = team;
    }

    public void addMember(TeamMember member) {
        team.lock.readLock().lock();
        try {
            for (TeamMember m : team.members) {
                if (m.playerId != member.playerId) {
                    Player p = PlayerManager.getInstance().findPlayerById(m.playerId);
                    if (p != null) {
                        p.service.addPlayerToTeam(member);
                    }
                }
            }
        } finally {
            team.lock.readLock().unlock();
        }
    }

    public void removePlayer(Player player) {
        team.lock.readLock().lock();
        try {
            for (TeamMember member : team.members) {
                if (member.playerId != player.id) {
                    Player p = PlayerManager.getInstance().findPlayerById(member.playerId);
                    if (p != null) {
                        p.service.removePlayerFromTeam(player.id);
                        p.addInfo(Player.INFO_RED, String.format("%s đã rời tổ đội", player.name));
                    }
                }
            }
        } finally {
            team.lock.readLock().unlock();
        }
    }

    public void removeMemberByHost(TeamMember member) {
        team.lock.readLock().lock();
        try {
            for (TeamMember m : team.members) {
                Player p = PlayerManager.getInstance().findPlayerById(m.playerId);
                if (p != null) {
                    p.service.removePlayerFromTeam(member.playerId);
                    if (m.playerId == team.leaderId) {
                        p.addInfo(Player.INFO_RED, String.format("Bạn đã loại %s khỏi tổ đội", member.name));
                    } else {
                        p.addInfo(Player.INFO_RED, String.format("%s đã loại %s khỏi tổ đội", team.members.get(0).name, member.name));
                    }
                }
            }
        } finally {
            team.lock.readLock().unlock();
        }
    }

    public void changeStatus() {
        team.lock.readLock().lock();
        try {
            for (TeamMember member : team.members) {
                Player p = PlayerManager.getInstance().findPlayerById(member.playerId);
                if (p != null) {
                    p.service.changeStatusTeam(team.status);
                }
            }
        } finally {
            team.lock.readLock().unlock();
        }
    }

    public void chatTeam(Player player, String content) {
        team.lock.readLock().lock();
        try {
            for (TeamMember member : team.members) {
                Player p = PlayerManager.getInstance().findPlayerById(member.playerId);
                if (p != null) {
                    try {
                        Message message = new Message(MessageName.CHAT_TEAM);
                        message.writer().writeInt(player.id);
                        message.writer().writeUTF(content);
                        message.writer().writeShort(player.head);
                        p.sendMessage(message);
                        message.cleanup();
                    } catch (Exception ex) {
                        logger.error("chatTeam", ex);
                    }
                }
            }
        } finally {
            team.lock.readLock().unlock();
        }
    }
}
