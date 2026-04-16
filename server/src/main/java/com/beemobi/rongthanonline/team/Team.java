package com.beemobi.rongthanonline.team;

import com.beemobi.rongthanonline.entity.player.Player;
import com.beemobi.rongthanonline.entity.player.PlayerManager;
import com.beemobi.rongthanonline.service.TeamService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class Team {
    public int id;
    public ArrayList<TeamMember> members;
    public int leaderId;
    public ReadWriteLock lock;
    public TeamStatus status;
    public TeamService service;

    public Team(Player player) {
        id = TeamManager.getInstance().autoIncrease++;
        leaderId = player.id;
        members = new ArrayList<>();
        lock = new ReentrantReadWriteLock();
        status = TeamStatus.NOT_LOCK;
        service = new TeamService(this);
    }

    public void addPlayer(Player player) {
        lock.writeLock().lock();
        try {
            members.add(new TeamMember(player));
        } finally {
            lock.writeLock().unlock();
        }
    }

    public void removePlayer(int playerId) {
        lock.writeLock().lock();
        try {
            for (int i = 0; i < members.size(); i++) {
                if (members.get(i).playerId == playerId) {
                    members.remove(i);
                    return;
                }
            }
        } finally {
            lock.writeLock().unlock();
        }
    }

    public ArrayList<TeamMember> getMembers() {
        lock.readLock().lock();
        try {
            return members;
        } finally {
            lock.readLock().unlock();
        }
    }

    public TeamMember findMemberByPlayerId(int playerId) {
        lock.readLock().lock();
        try {
            return members.stream().filter(m -> m.playerId == playerId).findFirst().orElse(null);
        } finally {
            lock.readLock().unlock();
        }
    }

    public void changeHost(int playerId) {
        lock.readLock().lock();
        try {
            if (playerId == -1) {
                leaderId = members.get(0).playerId;
            } else {
                for (int i = 0; i < members.size(); i++) {
                    if (members.get(i).playerId == playerId) {
                        Collections.swap(members, 0, i);
                        break;
                    }
                }
                leaderId = playerId;
            }
            for (TeamMember member : members) {
                Player p = PlayerManager.getInstance().findPlayerById(member.playerId);
                if (p != null) {
                    if (playerId != -1) {
                        p.service.teamInfo();
                    }
                    if (p.id == leaderId) {
                        p.addInfo(Player.INFO_YELLOW, "Bạn được lên làm đội trưởng");
                    } else {
                        p.addInfo(Player.INFO_YELLOW, String.format("%s được lên làm đội trưởng", members.get(0).name));
                    }
                }
            }
        } finally {
            lock.readLock().unlock();
        }
    }

    public void autoAccept(Player player) {
        lock.writeLock().lock();
        try {
            if (player.getTeam() != null) {
                return;
            }
            if (members.size() >= 6) {
                player.addInfo(Player.INFO_RED, "Tổ đội đã đủ thành viên");
                return;
            }
            if (status == TeamStatus.LOCK) {
                player.addInfo(0, "Tổ đội đã khóa");
                return;
            }
            for (TeamMember member : members) {
               if (member.playerId == player.id) {
                   return;
               }
            }
            members.add(new TeamMember(player));
            player.teamId = this.id;
        } finally {
            lock.writeLock().unlock();
        }
        player.service.teamInfo();
        service.addMember(findMemberByPlayerId(player.id));
    }
}
