package com.beemobi.rongthanonline.team;

import org.apache.log4j.Logger;

import java.util.HashMap;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class TeamManager {
    private static final Logger logger = Logger.getLogger(TeamManager.class);
    private static TeamManager instance;
    public int autoIncrease;
    public HashMap<Integer, Team> teams = new HashMap<>();
    public ReadWriteLock lock = new ReentrantReadWriteLock();

    public static TeamManager getInstance() {
        if (instance == null) {
            instance = new TeamManager();
        }
        return instance;
    }

    public void addTeam(Team team) {
        lock.writeLock().lock();
        try {
            teams.put(team.id, team);
        } finally {
            lock.writeLock().unlock();
        }
    }

    public Team findTeamById(int id) {
        if (id == -1) {
            return null;
        }
        lock.readLock().lock();
        try {
            return teams.get(id);
        } finally {
            lock.readLock().unlock();
        }
    }

    public Team findTeamByPlayerId(int playerId) {
        lock.readLock().lock();
        try {
            for (Team team : teams.values()) {
                team.lock.readLock().lock();
                try {
                    for (TeamMember member : team.members) {
                        if (member.playerId == playerId) {
                            return team;
                        }
                    }
                } finally {
                    team.lock.readLock().unlock();
                }
            }
        } finally {
            lock.readLock().unlock();
        }
        return null;
    }
}
