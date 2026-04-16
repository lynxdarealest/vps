package com.beemobi.rongthanonline.map;

import com.beemobi.rongthanonline.bot.npc.Referee;
import com.beemobi.rongthanonline.entity.player.Player;
import com.beemobi.rongthanonline.model.MessageTime;
import com.beemobi.rongthanonline.server.Server;
import com.beemobi.rongthanonline.util.Utils;

import java.util.List;

public class ArenaCustom extends Zone {
    public static final int STATUS_WAIT = 0;
    public static final int STATUS_START = 1;
    public static final int STATUS_COMBAT = 2;
    public static final int STATUS_CLOSE = 3;
    public static final int[][] POSITION_REFEREE = {{1450, 1296}, {1450, 1008}};
    public static final int[][] POSITION = {{1250, 1008}, {1650, 1008}};
    public static final int DELAY = 60;
    public static final int CHAT = 15;
    public static final int SOLO = 300;
    public Referee referee;
    public Player[] warriors;
    public long coin;
    public long updateTime;
    public int status;
    public int time;

    public ArenaCustom(Player[] warriors, long coin) {
        super(new Map(MapManager.getInstance().mapTemplates.get(MapName.DAU_TRUONG)));
        referee = new Referee();
        referee.x = POSITION_REFEREE[0][0];
        referee.y = POSITION_REFEREE[0][1];
        players.add(referee);
        referee.zone = this;
        this.warriors = warriors;
        this.coin = coin;
        status = STATUS_WAIT;
        updateTime = System.currentTimeMillis();
        time = DELAY;
        npcs.clear();
        resetPosition(false);
        enter(warriors[0]);
        enter(warriors[1]);
        if (coin >= 10_000_000L) {
            Server.getInstance().service.serverChat(String.format("%s đang thách đấu với %s tại %s (NPC Quy Lão Kamê) với mức cược %s xu",
                    warriors[0].name, warriors[1].name, map.template.name, Utils.currencyFormat(coin)));
        }
    }

    @Override
    public void update() {
        super.update();
        long now = System.currentTimeMillis();
        if (now - updateTime < 1000) {
            return;
        }
        updateTime = now;
        time--;
        switch (status) {
            case STATUS_WAIT: {
                if (time <= 0) {
                    status = STATUS_START;
                    time = CHAT;
                    resetPosition(true);
                    setPosition(referee, POSITION_REFEREE[1][0], POSITION_REFEREE[1][1], true);
                }
                break;
            }

            case STATUS_START: {
                if (time == 12) {
                    service.chatPublic(referee, String.format("Trận đấu giữa %s và %s sắp diễn ra", warriors[0].name, warriors[1].name));
                } else if (time == 8) {
                    service.chatPublic(referee, "Xin quý vị khán giả cho 1 tràng pháo tay cổ vũ cho 2 đấu thủ nào");
                } else if (time == 4) {
                    service.chatPublic(referee, "Mọi người hãy ổn định chỗ ngồi, trận đấu sẽ bắt đầu sau 3 giây nữa");
                } else if (time == 1) {
                    service.chatPublic(referee, "Trận đấu bắt đầu");
                } else if (time == 0) {
                    startFight();
                    status = STATUS_COMBAT;
                    time = SOLO;
                    List<Player> playerList = getPlayers(TYPE_PLAYER);
                    for (Player player : playerList) {
                        player.addMessageTime(MessageTime.TIME_COMBAT_ARENA_CUSTOM, time * 1000L);
                    }
                }
                break;
            }

            case STATUS_COMBAT: {
                if (time <= 0) {
                    warriors[0].addInfo(Player.INFO_RED, String.format("Bạn đã thua vì không thể hạ được %s", warriors[1].name));
                    warriors[1].addInfo(Player.INFO_YELLOW, String.format("Bạn đã chiến thắng %s", warriors[0].name));
                    endCombat(warriors[1]);
                } else {
                    if (warriors[0].zone != this) {
                        warriors[0].addInfo(Player.INFO_RED, "Bạn đã thua vì bỏ chạy");
                        warriors[1].addInfo(Player.INFO_YELLOW, String.format("Bạn đã thắng vì %s bỏ chạy", warriors[0].name));
                        endCombat(warriors[1]);
                    } else if (warriors[1].zone != this) {
                        warriors[1].addInfo(Player.INFO_RED, "Bạn đã thua vì bỏ chạy");
                        warriors[0].addInfo(Player.INFO_YELLOW, String.format("Bạn đã thắng vì %s bỏ chạy", warriors[1].name));
                        endCombat(warriors[0]);
                    } else if (warriors[0].isDead()) {
                        warriors[0].addInfo(Player.INFO_RED, "Bạn đã thua vì kiệt sức");
                        warriors[1].addInfo(Player.INFO_YELLOW, String.format("Bạn đã hạ được %s", warriors[0].name));
                        endCombat(warriors[1]);
                    } else if (warriors[1].isDead()) {
                        warriors[1].addInfo(Player.INFO_RED, "Bạn đã thua vì kiệt sức");
                        warriors[0].addInfo(Player.INFO_YELLOW, String.format("Bạn đã hạ được %s", warriors[1].name));
                        endCombat(warriors[0]);
                    } else if (warriors[0].y > 1008) {
                        warriors[0].addInfo(Player.INFO_RED, "Bạn đã thua vì rớt đài");
                        warriors[1].addInfo(Player.INFO_YELLOW, String.format("Bạn đã thắng %s", warriors[0].name));
                        endCombat(warriors[1]);
                    } else if (warriors[1].y > 1008) {
                        warriors[1].addInfo(Player.INFO_RED, "Bạn đã thua vì rớt đài");
                        warriors[0].addInfo(Player.INFO_YELLOW, String.format("Bạn đã thắng %s", warriors[1].name));
                        endCombat(warriors[0]);
                    }
                }
                break;
            }
        }
    }

    public void endCombat(Player winner) {
        status = STATUS_CLOSE;
        warriors[0].clearPk();
        warriors[1].clearPk();
        coin = coin * 2;
        winner.upXu(coin - coin / 10);
        Utils.setTimeout(this::close, 2000);
    }

    @Override
    public void enter(Player player) {
        super.enter(player);
        if (player.isPlayer()) {
            if (status == STATUS_WAIT) {
                player.addMessageTime(MessageTime.TIME_WAIT_ARENA_CUSTOM, time * 1000L);
            } else if (status == STATUS_COMBAT) {
                player.addMessageTime(MessageTime.TIME_COMBAT_ARENA_CUSTOM, time * 1000L);
            }
        }
    }

    @Override
    public void close() {
        super.close();
        MapManager.getInstance().arenaCustoms.remove(this);
        List<Player> playerList = getPlayers(Zone.TYPE_PLAYER);
        for (Player player : playerList) {
            try {
                player.addInfo(Player.INFO_RED, "Trận chiến đã kết thúc");
                player.teleport(MapName.DAO_KAME, true);
            } catch (Exception ignored) {
            }
        }
    }

    public void resetPosition(boolean isUpdate) {
        setPosition(warriors[0], POSITION[0][0], POSITION[0][1], isUpdate);
        setPosition(warriors[1], POSITION[1][0], POSITION[1][1], isUpdate);
    }

    public void setPosition(Player player, int x, int y, boolean isUpdate) {
        player.x = x;
        player.y = y;
        if (isUpdate) {
            service.setPosition(player);
        }
    }

    public void startFight() {
        setPosition(referee, POSITION_REFEREE[0][0], POSITION_REFEREE[0][1], true);
        warriors[0].recovery(Player.RECOVERY_ALL, 100, true);
        warriors[1].recovery(Player.RECOVERY_ALL, 100, true);
        resetPosition(true);
        warriors[0].testPlayerId = warriors[1].id;
        warriors[1].testPlayerId = warriors[0].id;
        warriors[0].setTypePk(1);
        warriors[1].setTypePk(1);
        service.playerFlight(warriors[0], warriors[1]);
    }

    public String[] getInfo() {
        return new String[]{String.format("%s (lv%d) vs %s (lv%d)", warriors[0].name, warriors[0].level, warriors[1].name, warriors[1].level), String.format("%s xu", Utils.formatNumber(coin))};
    }
}
