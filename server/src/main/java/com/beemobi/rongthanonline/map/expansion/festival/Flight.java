package com.beemobi.rongthanonline.map.expansion.festival;

import com.beemobi.rongthanonline.entity.player.Player;
import com.beemobi.rongthanonline.model.MessageTime;
import com.beemobi.rongthanonline.npc.Npc;
import com.beemobi.rongthanonline.npc.NpcManager;
import com.beemobi.rongthanonline.npc.NpcName;

public class Flight {
    public static final int[][] POSITION = new int[][]{{1200, 792}, {1600, 792}};
    public static final int[] POSITION_WAIT = new int[]{1200, 1008};
    public Player player1;
    public Player player2;
    public Arena arena;
    public Npc npc;
    public FlightState state;
    public long lastTimeUpdate;
    public int indexNpcChat;
    public MartialArtsFestival martialArtsFestival;

    public Flight(MartialArtsFestival martialArtsFestival, Player player1, Player player2, Arena arena) {
        state = FlightState.NONE;
        this.martialArtsFestival = martialArtsFestival;
        this.player1 = player1;
        this.player2 = player2;
        this.arena = arena;
        npc = new Npc();
        npc.template = NpcManager.getInstance().npcTemplates.get(NpcName.TRONG_TAI);
        npc.x = 1400;
        npc.y = 792;
        arena.flight = this;
    }

    public void start() {
        state = FlightState.WAIT_FOR_NPC_CHAT;
        lastTimeUpdate = System.currentTimeMillis() - 2000;
        arena.enter(player1);
        arena.enter(player2);
        arena.enter(npc);
        setPosition();
    }

    public void update() {
        if (state == FlightState.NONE) {
            return;
        }
        long now = System.currentTimeMillis();
        if (state == FlightState.WAIT_FOR_NPC_CHAT) {
            if (now - lastTimeUpdate < 4000) {
                return;
            }
            lastTimeUpdate = now;
            if (indexNpcChat == 0) {
                arena.npcChat(npc, String.format("Trận đấu giữa %s và %s sắp diễn ra", player1.name, player2.name));
            } else if (indexNpcChat == 1) {
                arena.npcChat(npc, "Xin quý vị khán giả cho một tràng pháo tay để cổ vũ cho 2 đối thủ");
            } else if (indexNpcChat == 2) {
                arena.npcChat(npc, "Mọi người hãy ổn định chỗ ngồi, trận đấu sẽ diễn ra sau 3 giây nữa");
            } else if (indexNpcChat == 3) {
                arena.npcChat(npc, "Trận đấu bắt đầu");
            } else {
                arena.leave(npc);
                Player winner = getWinner(now);
                if (winner != null) {
                    martialArtsFestival.tops.add(0, new Warrior(player1.id == winner.id ? player2 : player1));
                    martialArtsFestival.addParticipant(winner);
                    martialArtsFestival.addHistory(String.format("%s vs %s (%s thắng)", player1.name, player2.name, winner.name));
                    winner.x = POSITION[0][0];
                    winner.y = 1008;
                    arena.service.setPosition(winner);
                    arena.flight = null;
                    return;
                }
                setPosition();
                setTime(60);
                player1.testPlayerId = player2.id;
                player2.testPlayerId = player1.id;
                player1.setTypePk(1);
                player2.setTypePk(1);
                arena.service.playerFlight(player1, player2);
                state = FlightState.PK;
            }
            indexNpcChat++;
            return;
        }
        Player winner = getWinner(now);
        if (winner != null) {
            martialArtsFestival.tops.add(0, new Warrior(player1.id == winner.id ? player2 : player1));
            martialArtsFestival.addParticipant(winner);
            martialArtsFestival.addHistory(String.format("%s vs %s (%s thắng)", player1.name, player2.name, winner.name));
            winner.x = POSITION[0][0];
            winner.y = 1008;
            arena.service.setPosition(winner);
            arena.flight = null;
            player1.removeMessageTime(MessageTime.DAI_HOI_VO_THUAT);
            player2.removeMessageTime(MessageTime.DAI_HOI_VO_THUAT);
        }
    }

    public void setPosition() {
        player1.x = POSITION[0][0];
        player1.y = POSITION[0][1];
        arena.service.setPosition(player1);
        player2.x = POSITION[1][0];
        player2.y = POSITION[1][1];
        arena.service.setPosition(player2);
    }

    public void setTime(int second) {
        player1.addMessageTime(MessageTime.DAI_HOI_VO_THUAT, second * 1000L);
        player2.addMessageTime(MessageTime.DAI_HOI_VO_THUAT, second * 1000L);
    }

    public Player getWinner(long now) {
        if (now - lastTimeUpdate > 60000L) {
            long per1 = player1.hp * 100 / player1.maxHp;
            long per2 = player2.hp * 100 / player2.maxHp;
            if (per1 < per2) {
                player1.startDie(null);
                return player2;
            }
            player2.startDie(null);
            return player1;
        }
        if (player1.isDead() && !player2.isDead()) {
            return player2;
        }
        if (!player1.isDead() && player2.isDead()) {
            return player1;
        }
        if (player1.zone != arena) {
            return player2;
        }
        if (player2.zone != arena) {
            return player1;
        }
        return null;
    }


}
