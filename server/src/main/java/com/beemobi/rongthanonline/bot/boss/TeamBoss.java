package com.beemobi.rongthanonline.bot.boss;

import com.beemobi.rongthanonline.entity.player.Player;
import com.beemobi.rongthanonline.map.Zone;

import java.util.List;

public abstract class TeamBoss {
    public Object[][] saysWhenBeforeAttack;
    public TeamBossStatus status = TeamBossStatus.WAIT_PLAYER;
    public long lastTimeChat;
    public int indexChat;
    public Player chatter;

    public abstract void next(Boss boss);

    public abstract void end();

    public abstract void born();

    public void update(Zone zone) {
        switch (status) {
            case WAIT_PLAYER: {
                List<Player> players = zone.getPlayers(Zone.TYPE_PLAYER);
                if (!players.isEmpty()) {
                    chatter = players.get(0);
                    setStatus(TeamBossStatus.WAIT_CHAT);
                }
                return;
            }

            case WAIT_CHAT: {
                long now = System.currentTimeMillis();
                if (lastTimeChat == 0) {
                    lastTimeChat = now;
                    return;
                }
                if (now - lastTimeChat < 4000) {
                    return;
                }
                lastTimeChat = now;
                if (saysWhenBeforeAttack == null || indexChat >= saysWhenBeforeAttack.length) {
                    setStatus(TeamBossStatus.ATTACK);
                    return;
                }
                Object[] chats = saysWhenBeforeAttack[indexChat];
                indexChat++;
                if (chats[0] instanceof Boss) {
                    ((Boss) chats[0]).chat((String) chats[1]);
                } else if (chatter != null && chatter.zone == zone) {
                    chatter.chat((String) chats[1]);
                }
                return;
            }
        }
    }

    public void setStatus(TeamBossStatus status) {
        this.status = status;
    }
}
