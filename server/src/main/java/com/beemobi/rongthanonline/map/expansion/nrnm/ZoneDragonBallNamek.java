package com.beemobi.rongthanonline.map.expansion.nrnm;

import com.beemobi.rongthanonline.bot.boss.other.BossNrnm;
import com.beemobi.rongthanonline.entity.player.Player;
import com.beemobi.rongthanonline.item.ItemManager;
import com.beemobi.rongthanonline.item.ItemMap;
import com.beemobi.rongthanonline.item.ItemName;
import com.beemobi.rongthanonline.map.Zone;
import com.beemobi.rongthanonline.map.Map;
import com.beemobi.rongthanonline.map.MapName;
import com.beemobi.rongthanonline.model.MessageTime;
import com.beemobi.rongthanonline.model.PointWeeklyType;
import com.beemobi.rongthanonline.server.Server;
import com.beemobi.rongthanonline.util.Utils;
import org.apache.log4j.Logger;

import java.util.List;

public class ZoneDragonBallNamek extends Zone {
    private static final Logger logger = Logger.getLogger(ZoneDragonBallNamek.class);
    private static final int[] DRAGON_BALL_NAMEK = new int[]{
            ItemName.NGOC_RONG_NAMEK_1_SAO,
            ItemName.NGOC_RONG_NAMEK_2_SAO,
            ItemName.NGOC_RONG_NAMEK_3_SAO,
            ItemName.NGOC_RONG_NAMEK_4_SAO,
            ItemName.NGOC_RONG_NAMEK_5_SAO,
            ItemName.NGOC_RONG_NAMEK_6_SAO,
            ItemName.NGOC_RONG_NAMEK_7_SAO
    };
    private static final long COUNT_TIME_HOLD = 300000;
    public DragonBallNamek dragonBallNamek;
    public Player holder;
    public int killerId = -1;
    public int index;
    public ItemMap itemMapStone;
    public long timeStartHold;
    public long lastTimeSendNotification;
    public boolean isReward;

    public ZoneDragonBallNamek(Map map, DragonBallNamek dragonBallNamek, int index) {
        super(map);
        this.dragonBallNamek = dragonBallNamek;
        this.index = index;
        itemMapStone = ItemManager.getInstance().createItemMap(ItemName.NGOC_RONG_NAMEK_STONE, 1, -1);
        itemMapStone.x = map.template.width / 2;
        itemMapStone.y = map.getYSd(itemMapStone.x);
        itemMapStone.id = itemMapAutoIncrease++;
        itemMaps.add(itemMapStone);
        BossNrnm bossNrnm = new BossNrnm(index, this);
        enter(bossNrnm);
    }

    @Override
    public void enter(Player player) {
        super.enter(player);
        setRandomTypeFlag(player);
        sendMessageTime();
    }

    @Override
    public void update() {
        super.update();
        if (killerId == -1) {
            return;
        }
        if (itemMapStone == null) {
            return;
        }
        ItemMap itemMap = findItemMapByTemplateId(ItemName.NGOC_RONG_NAMEK_STONE);
        if (itemMap != null) {
            removeItemMap(itemMap, true);
            return;
        }
        if (holder == null) {
            itemMap = findItemMapByTemplateId(DRAGON_BALL_NAMEK[index]);
            if (itemMap == null) {
                itemMap = ItemManager.getInstance().createItemMap(DRAGON_BALL_NAMEK[index], 1, -1);
                List<Player> playerList = getPlayers(TYPE_PLAYER);
                if (playerList.isEmpty()) {
                    itemMap.x = Utils.nextInt(200, map.template.width - 200);
                } else {
                    itemMap.x = playerList.get(Utils.nextInt(playerList.size())).x;
                }
                itemMap.y = map.getYSd(itemMap.x);
                addItemMap(itemMap);
            }
            return;
        }
        long now = System.currentTimeMillis();
        long time = COUNT_TIME_HOLD + timeStartHold - now;
        if (time <= 0) {
            reward();
            return;
        }
        if (holder.zone == this) {
            if (now - lastTimeSendNotification > 10000) {
                lastTimeSendNotification = now;
                holder.addInfo(Player.INFO_YELLOW, String.format("Cố giữ ngọc thêm %d giây nữa sẽ thắng", time / 1000));
            }
        } else {
            setHolder(null);
        }
    }

    public void setHolder(Player player) {
        timeStartHold = System.currentTimeMillis();
        holder = player;
        setFlag(player);
        sendMessageTime();
    }

    public void sendMessageTime() {
        if (holder != null) {
            List<Player> playerList = getPlayers(TYPE_PLAYER);
            MessageTime messageTime = new MessageTime(MessageTime.TIME_WIN_NRNM, COUNT_TIME_HOLD + timeStartHold - System.currentTimeMillis());
            String info = holder.name;
            if (holder.clan != null) {
                info = String.format("%s (%s)", holder.name, holder.clan.name);
            }
            messageTime.text = messageTime.text.replace("#", info);
            for (Player player : playerList) {
                player.addMessageTime(MessageTime.TIME_WIN_NRNM, messageTime.getCountDown(), info);
            }
        }
    }

    public void setFlag(Player player) {
        if (player == null) {
            List<Player> playerList = getPlayers(TYPE_PLAYER);
            int flag = getTypeFlagFree(playerList);
            for (Player p : playerList) {
                if (p != null && p.zone == this && p.typeFlag == 1) {
                    p.setTypeFlag(flag);
                }
            }
            return;
        }
        if (player.zone != this) {
            return;
        }
        if (player.typeFlag != 1) {
            player.typeFlag = 1;
            player.setTypeFlag(1);
        }
        if (player.clan != null) {
            List<Player> players = findAllPlayerSameClan(player);
            for (Player p : players) {
                if (p != null && p.zone == this) {
                    p.setTypeFlag(player.typeFlag);
                }
            }
        }
    }

    @Override
    public void close() {
        reward();
        clearPlayer();
        super.close();
    }

    public void clearPlayer() {
        List<Player> playerList = getPlayers(TYPE_PLAYER);
        for (Player player : playerList) {
            if (player != null && player.zone == this) {
                player.addInfo(Player.INFO_YELLOW, "Trận chiến đã kết thúc, hẹn bạn vào ngày mai");
                player.bag = -1;
                service.setBag(player);
                player.teleport(MapName.TRAM_TAU_VU_TRU_NAMEK, true);
            }
        }
    }

    public void reward() {
        if (isReward) {
            return;
        }
        isReward = true;
        if (holder == null) {
            holder = findPlayerById(killerId);
        }
        if (holder != null) {
            dragonBallNamek.addReward(index, holder);
            if (holder.clan == null) {
                Server.getInstance().service.serverChat(String.format("Chúc mừng %s đã giành được Ngọc Rồng Namek tại %s", holder.name, map.template.name));
            } else {
                Server.getInstance().service.serverChat(String.format("Chúc mừng %s đã giành được Ngọc Rồng Namek cho bang hội %s tại %s", holder.name, holder.clan.name, map.template.name));
            }
            holder.upPointWeekly(PointWeeklyType.ACTIVE, 10);
        }
        clearPlayer();
    }
}
