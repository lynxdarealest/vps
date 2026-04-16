package com.beemobi.rongthanonline.map.expansion.spaceship;

import com.beemobi.rongthanonline.bot.boss.spaceship.BossSpaceship;
import com.beemobi.rongthanonline.entity.player.Player;
import com.beemobi.rongthanonline.map.Map;
import com.beemobi.rongthanonline.map.MapName;
import com.beemobi.rongthanonline.map.Zone;
import com.beemobi.rongthanonline.model.MessageTime;
import com.beemobi.rongthanonline.util.Utils;

import java.util.List;
import java.util.stream.Collectors;

public class ZoneSpaceship extends Zone {
    public Spaceship spaceship;
    public long lastTimeUpdate;

    public ZoneSpaceship(Map map, Spaceship spaceship, int index) {
        super(map);
        this.spaceship = spaceship;
        enter(new BossSpaceship(spaceship, index));
    }

    @Override
    public void enter(Player player) {
        super.enter(player);
        if (player.isPlayer()) {
            player.pointCurrSpaceship = 0;
            //player.addMessageTime(MessageTime.SPACESHIP, spaceship.getCountDown());
            setEnergy(player);
            if (map.template.id == MapName.SPACESHIP_1) {
                player.setTypeFlag(Utils.nextInt(9, 10));
            }
        }
    }

    public void setEnergy(Player player) {
        MessageTime messageTime = new MessageTime(MessageTime.NANG_LUONG, spaceship.getCountDown());
        messageTime.text = messageTime.text.replace("#", player.pointCurrSpaceship + "");
        player.addMessageTime(MessageTime.NANG_LUONG, messageTime.getCountDown(), messageTime.text);
    }

    @Override
    public void close() {
        List<Player> playerList = getPlayers(Zone.TYPE_PLAYER);
        for (Player player : playerList) {
            try {
                player.teleport(MapName.DAO_KAME, false);
                player.addInfo(Player.INFO_YELLOW, "Trận chiến đã kết thúc");
            } catch (Exception ignored) {
            }
        }
        super.close();
    }

    @Override
    public void update() {
        super.update();
        long now = System.currentTimeMillis();
        if (now - lastTimeUpdate >= 30000L) {
            lastTimeUpdate = now;
            List<Player> playerList = getPlayers(Zone.TYPE_PLAYER);
            if (playerList.size() > 1) {
                List<Player> kaios = playerList.stream().filter(p -> p.typeFlag == 9).collect(Collectors.toList());
                List<Player> mabus = playerList.stream().filter(p -> p.typeFlag == 10).collect(Collectors.toList());
                if (kaios.size() < mabus.size()) {
                    Player player = Utils.nextArray(mabus);
                    if (player != null) {
                        player.setTypeFlag(9);
                        player.addInfo(Player.INFO_YELLOW, "Bạn đã được phù phép hóa giải");
                    }
                } else if (kaios.size() > mabus.size()) {
                    Player player = Utils.nextArray(kaios);
                    if (player != null) {
                        player.setTypeFlag(10);
                        player.addInfo(Player.INFO_YELLOW, "Bạn đã được phù phép mê hoặc");
                    }
                }
            }
        }
    }
}
