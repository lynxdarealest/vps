package com.beemobi.rongthanonline.map.expansion.flagwar;

import com.beemobi.rongthanonline.bot.disciple.Disciple;
import com.beemobi.rongthanonline.clan.Clan;
import com.beemobi.rongthanonline.clan.ClanMember;
import com.beemobi.rongthanonline.entity.player.Player;
import com.beemobi.rongthanonline.item.ItemManager;
import com.beemobi.rongthanonline.item.ItemMap;
import com.beemobi.rongthanonline.item.ItemName;
import com.beemobi.rongthanonline.map.Map;
import com.beemobi.rongthanonline.map.MapName;
import com.beemobi.rongthanonline.map.Zone;
import com.beemobi.rongthanonline.model.MessageTime;
import com.beemobi.rongthanonline.npc.Npc;
import com.beemobi.rongthanonline.npc.NpcManager;
import com.beemobi.rongthanonline.npc.NpcName;
import com.beemobi.rongthanonline.server.Server;
import com.beemobi.rongthanonline.util.Utils;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class ZoneFlagWar extends Zone {
    private static final Logger logger = Logger.getLogger(ZoneFlagWar.class);
    public FlagWar flagWar;
    public Player holder;
    public Npc npc;
    public ReadWriteLock lockPoint = new ReentrantReadWriteLock();
    public HashMap<Clan, Integer> points = new HashMap<>();

    public ZoneFlagWar(Map map, FlagWar flagWar) {
        super(map);
        this.flagWar = flagWar;
        npc = new Npc();
        npc.template = NpcManager.getInstance().npcTemplates.get(NpcName.PU);
    }

    @Override
    public void enter(Player player) {
        if (player instanceof Disciple) {
            return;
        }
        super.enter(player);
        player.service.setPart();
        setRandomTypeFlag(player);
        sendMessageTime();
    }

    @Override
    public void update() {
        super.update();
        if (holder == null) {
            ItemMap itemMap = findItemMapByTemplateId(ItemName.CO_TRANH_DOAT);
            if (itemMap == null) {
                itemMap = ItemManager.getInstance().createItemMap(ItemName.CO_TRANH_DOAT, 1, -1);
                itemMap.x = Utils.nextInt(200, map.template.width - 200);
                itemMap.y = map.getYSd(itemMap.x);
                itemMap.throwTime = System.currentTimeMillis() + flagWar.getCountDown();
                addItemMap(itemMap);
            }

            Npc npc = findNpcById(NpcName.PU);
            if (npc == null) {
                this.npc.x = Utils.nextInt(200, map.template.width - 200);
                this.npc.y = map.getYSd(this.npc.x);
                enter(this.npc);
            }
            return;
        }
    }

    public void setHolder(Player player) {
        holder = player;
        setFlag(player);
        sendMessageTime();
    }

    public void sendMessageTime() {
        if (holder != null) {
            String info = holder.name;
            if (holder.clan != null) {
                info = String.format("%s (%s)", holder.name, holder.clan.name);
            }
            String text = MessageTime.templates.get(MessageTime.TRANH_DOAT_CO).replace("#", info);
            List<Player> playerList = getPlayers(TYPE_PLAYER);
            for (Player player : playerList) {
                player.addMessageTime(MessageTime.TRANH_DOAT_CO, -1, text);
            }
        } else {
            List<Player> playerList = getPlayers(TYPE_PLAYER);
            for (Player player : playerList) {
                player.addMessageTime(MessageTime.TRANH_DOAT_CO, 0, null);
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
                player.teleport(MapName.TRAM_TAU_VU_TRU, true);
            }
        }
    }

    public void upPoint(Player player, int point) {
        Clan clan;
        lockPoint.writeLock().lock();
        try {
            clan = player.clan;
            if (clan == null) {
                return;
            }
            points.put(clan, points.getOrDefault(clan, 0) + point);
        } finally {
            lockPoint.writeLock().unlock();
        }
        clan.service.addInfo(Player.INFO_YELLOW, player.name + " vừa tăng " + point + " cho bang của bạn");
    }

    public List<String> getTops() {
        List<String> tops = new ArrayList<>();
        lockPoint.readLock().lock();
        try {
            int i = 1;
            for (Clan clan : points.keySet()) {
                String content = i + ". " + clan.name + ": " + points.get(clan) + " điểm";
                tops.add(content);
                i++;
                if (i >= 10) {
                    break;
                }
            }
        } finally {
            lockPoint.readLock().unlock();
        }
        return tops;
    }

    public void reward() {
        lockPoint.readLock().lock();
        try {
            if (points.isEmpty()) {
                return;
            }
            List<java.util.Map.Entry<Clan, Integer>> list
                    = new ArrayList<>(points.entrySet());
            list.sort(new Comparator<java.util.Map.Entry<Clan, Integer>>() {
                // Comparing two entries by value
                @Override
                public int compare(
                        java.util.Map.Entry<Clan, Integer> entry1,
                        java.util.Map.Entry<Clan, Integer> entry2) {
                    // Subtracting the entries
                    return entry2.getValue().compareTo(entry1.getValue());
                }
            });
            Server.getInstance().service.serverChat("Chúc mừng Bang hội " + list.get(0).getKey().name + " đã chiến thắng trong chế độ cướp cờ");
            int size = Math.min(3, list.size());
            for (int i = 0; i < size; i++) {
                int point = switch (i){
                    case 0 -> 500;
                    case 1 -> 200;
                    default -> 100;
                };
                Clan clan = list.get(0).getKey();
                clan.upExpFlagWar(point);
            }
        } finally {
            lockPoint.readLock().unlock();
        }
    }
}
