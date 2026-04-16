package com.beemobi.rongthanonline.bot.tournament;

import com.beemobi.rongthanonline.bot.boss.Boss;
import com.beemobi.rongthanonline.entity.player.Player;

public class TournamentAthlete extends Player {
    public int playerId;
    public int rank;
    public boolean isOnline;
    public boolean isBattle;

    public TournamentAthlete() {
        id = Boss.autoIncrease++;
    }
}
