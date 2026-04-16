package com.beemobi.rongthanonline.map.expansion.tournament;

import com.beemobi.rongthanonline.bot.boss.Boss;
import com.beemobi.rongthanonline.bot.tournament.TournamentAthlete;
import com.beemobi.rongthanonline.command.Command;
import com.beemobi.rongthanonline.command.CommandName;
import com.beemobi.rongthanonline.entity.player.Player;
import com.beemobi.rongthanonline.map.Map;
import com.beemobi.rongthanonline.map.MapManager;
import com.beemobi.rongthanonline.map.MapName;
import com.beemobi.rongthanonline.map.expansion.Expansion;
import com.beemobi.rongthanonline.network.Message;
import com.beemobi.rongthanonline.npc.NpcName;
import com.beemobi.rongthanonline.top.TopInfo;
import com.beemobi.rongthanonline.top.TopManager;
import com.beemobi.rongthanonline.top.TopType;
import com.beemobi.rongthanonline.util.Utils;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class Tournament extends Expansion {
    private static final Logger logger = Logger.getLogger(Tournament.class);

    public static int[] MAPS = {MapName.DAU_TRUONG};

    public List<TournamentAthlete> athletes = new ArrayList<>();
    public ReadWriteLock lockAthlete = new ReentrantReadWriteLock();

    public Tournament() {
        super(-1);
        for (int mapId : MAPS) {
            Map map = new Map(MapManager.getInstance().mapTemplates.get(mapId), this);
            for (int i = 0; i < 20; i++) {
                map.zones.add(new ZoneTournament(map, this));
            }
            maps.add(map);
        }
        init();
    }

    public void init() {

        if (athletes.isEmpty()) {
            List<TopInfo> topInfoList = TopManager.getInstance().tops.get(TopType.TOP_PRO).getTops();
            int size = Math.min(20, topInfoList.size());
            for (int i = 0; i < size; i++) {
                TopInfo info = topInfoList.get(i);
                TournamentAthlete athlete = new TournamentAthlete();
                athlete.playerId = info.id;
                athlete.name = info.name;
                athlete.pointPro = info.score;
                athlete.gender = info.gender;
                athlete.rank = i;
                athletes.add(athlete);
            }
        }
    }

    public void action(Player player, Message message) {
        try {
            int action = message.reader().readByte();
            if (action != 1) {
                return;
            }
            int id = message.reader().readInt();
            TournamentAthlete tournamentAthlete = findTournamentAthleteByPlayerId(id);
            if (tournamentAthlete == null) {
                player.addInfo(Player.INFO_RED, "Đối phương không còn trong bảng xếp hạng");
                return;
            }
            player.createMenu(NpcName.ME, "",
                    List.of(new Command(CommandName.THACH_DAU_TOURNAMENT, "Thách đấu\n" + tournamentAthlete.name, player, id)));
        } catch (Exception ex) {
            logger.error("action", ex);
        }
    }

    public void attack(Player player, int id) {
        if (player.id == id) {
            return;
        }
        int[] indexes = {getIndex(player.id), getIndex(id)};
        if (indexes[0] <= indexes[1] || indexes[1] > 20) {
            player.addInfo(Player.INFO_RED, "Không thể thách đấu với hạng thấp hơn");
            return;
        }
        if (indexes[0] > indexes[1] + 2) {
            player.addInfo(Player.INFO_RED, "Không thể thách đấu quá 2 hạng");
            return;
        }
        TournamentAthlete tournamentAthlete = findTournamentAthleteByPlayerId(id);
        if (tournamentAthlete == null) {
            return;
        }
        if (tournamentAthlete.isBattle) {
            player.addInfo(Player.INFO_RED, tournamentAthlete.name + " đang chiến đấu với chiến binh khác");
            return;
        }


    }

    public TournamentAthlete findTournamentAthleteByPlayerId(int id) {
        lockAthlete.readLock().lock();
        try {
            return athletes.stream().filter(a -> a.playerId == id).findFirst().orElse(null);
        } finally {
            lockAthlete.readLock().unlock();
        }
    }

    public int getIndex(int id) {
        lockAthlete.readLock().lock();
        try {
            athletes.sort(Comparator.comparingInt(a -> a.rank));
            int size = athletes.size();
            for (int i = 0; i < size; i++) {
                if (athletes.get(i).playerId == id) {
                    return i;
                }
            }
        } finally {
            lockAthlete.readLock().unlock();
        }
        return 21;
    }

    public List<TournamentAthlete> getAthletes() {
        lockAthlete.readLock().lock();
        try {
            return new ArrayList<>(athletes);
        } finally {
            lockAthlete.readLock().unlock();
        }
    }

    @Override
    public void close() {

    }

    @Override
    public void update() {

    }

    public void enter(Player player) {
        player.x = ZoneTournament.POSITION_REFEREE[0][0] - Utils.nextInt(100, 200);
        player.y = ZoneTournament.POSITION_REFEREE[0][1];
        player.joinMap(maps.get(0), -1);
    }

}
