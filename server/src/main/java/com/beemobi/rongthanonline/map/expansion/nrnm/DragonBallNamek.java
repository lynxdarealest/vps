package com.beemobi.rongthanonline.map.expansion.nrnm;

import com.beemobi.rongthanonline.clan.ClanMember;
import com.beemobi.rongthanonline.common.Language;
import com.beemobi.rongthanonline.effect.Effect;
import com.beemobi.rongthanonline.effect.EffectName;
import com.beemobi.rongthanonline.entity.player.Player;
import com.beemobi.rongthanonline.entity.player.PlayerManager;
import com.beemobi.rongthanonline.item.Item;
import com.beemobi.rongthanonline.item.ItemManager;
import com.beemobi.rongthanonline.item.ItemName;
import com.beemobi.rongthanonline.map.Map;
import com.beemobi.rongthanonline.map.MapManager;
import com.beemobi.rongthanonline.map.MapName;
import com.beemobi.rongthanonline.map.expansion.Expansion;
import com.beemobi.rongthanonline.server.Server;
import com.beemobi.rongthanonline.util.Utils;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

public class DragonBallNamek extends Expansion implements Runnable {
    private static final Logger logger = Logger.getLogger(DragonBallNamek.class);
    public static final int[] MAP_IDS = {MapName.LANG_MONAITO, MapName.LANG_DRAVO, MapName.LANG_TSUBURI,
            MapName.LANG_SHIRO, MapName.LANG_KAMI, MapName.LANG_MORI_NRNM, MapName.LANG_TSUNO};
    public static List<String> notes;

    static {
        notes = new ArrayList<>();
        notes.add("Mỗi ngày từ 21h đến 21h30 các ngôi làng sẽ xảy ra 1 cuộc đại chiến");
        notes.add("Người nào tìm thấy và giữ được Ngọc Rồng Namek sẽ mang phần thưởng về cho bang của mình trong vòng 1 ngày");
        notes.add("Lưu ý mỗi bang có thể chiếm hữu nhiều viên khác nhau nhưng nếu cùng loại cũng chỉ nhận được 1 lần phần thưởng đó");
        notes.add("Có 3 cách để thắng:");
        notes.add("- 1) Giữ Ngọc rồng Namek trên người hơn 5 phút liên tục");
        notes.add("- 2) Sau 60 phút tham gia tàu sẽ đón về và đang giữ Ngọc rồng Namek trên người");
        notes.add("- 3) Sau 60 phút tham gia tàu sẽ đón về và không ai đang giữ Ngọc rồng Namek trên người thì nếu bạn hoặc thành viên trong bang hạ Boss thì sẽ chiến thắng");
        notes.add("Các phần thưởng như sau");
        notes.add("- 1 sao: +20% sức đánh cho toàn bang");
        notes.add("- 2 sao: +50% HP và KI tối đa cho toàn bang");
        notes.add("- 3 sao: Mỗi giờ 10 viên đá cấp 10 cho toàn bang");
        notes.add("- 4 sao: Mỗi giờ 30 viên đậu thần cấp 10 cho toàn bang");
        notes.add("- 5 sao: Mỗi giờ 5 Ruby cho toàn bang");
        notes.add("- 6 sao: Mỗi giờ 1tr xu cho toàn bang");
        notes.add("- 7 sao: Mỗi giờ ngẫu nhiên từ 20 đến 30 capsule kì bí cho toàn bang");
        notes.add("Các phần thường mỗi giờ đến gặp tôi để nhận nhé");
    }

    public DragonBallNamek() {
        super(1800000);
        Server.getInstance().service.serverChat("Chiến trường Ngọc Rồng Namek đã mở cửa");
        for (int i = 0; i < MAP_IDS.length; i++) {
            int mapId = MAP_IDS[i];
            Map map = new Map(MapManager.getInstance().mapTemplates.get(mapId), this);
            for (int j = 0; j < 2; j++) {
                map.zones.add(new ZoneDragonBallNamek(map, this, i));
            }
            maps.add(map);
        }
        isRunning = true;
        new Thread(this).start();
    }

    public void addReward(int index, Player winner) {
        int effectId = -1;
        long delay = 0;
        int param = 0;
        switch (index) {
            case 0:
                // 1 sao - tăng 20% sức đánh
                effectId = EffectName.TANG_PHAN_TRAM_SUC_DANH_TU_PHAN_THUONG_NRNM_1_SAO;
                param = 20;
                break;
            case 1:
                // 2 sao - tăng 50% Hp, KI
                effectId = EffectName.TANG_PHAN_TRAM_HP_KI_TU_PHAN_THUONG_NRNM_2_SAO;
                param = 50;
                break;
            case 2:
                // 3 sao - 1h 1v da cap 10
                effectId = EffectName.NGOC_RONG_NAMEK_3_SAO;
                delay = 3600000;
                break;
            case 3:
                // 4 sao - 30 dau 10
                effectId = EffectName.NGOC_RONG_NAMEK_4_SAO;
                delay = 3600000;
                break;
            case 4:
                // 5 sao - 5 kc
                effectId = EffectName.NGOC_RONG_NAMEK_5_SAO;
                delay = 3600000;
                break;
            case 5:
                // 6 sao - 500k xu
                effectId = EffectName.NGOC_RONG_NAMEK_6_SAO;
                delay = 3600000;
                break;
            case 6:
                // 7 sao - 20-30 cs ki bi
                effectId = EffectName.NGOC_RONG_NAMEK_7_SAO;
                delay = 3600000;
                break;

        }
        if (effectId != -1) {
            Effect effect = winner.findEffectByTemplateId(effectId);
            if (effect == null) {
                winner.addEffect(new Effect(winner, effectId, 23 * 60 * 60 * 1000L, delay, param));
                if (index == 0 || index == 1) {
                    winner.service.setInfo();
                }

            }
            if (winner.clan != null) {
                boolean flag = true;
                for (ClanMember member : winner.clan.members) {
                    if (member.playerId == winner.id) {
                        continue;
                    }
                    Player player = PlayerManager.getInstance().findPlayerById(member.playerId);
                    if (player != null) {
                        effect = player.findEffectByTemplateId(effectId);
                        if (effect == null) {
                            player.addEffect(new Effect(player, effectId, 23 * 60 * 60 * 1000L, delay, param));
                            player.service.setInfo();
                        } else {
                            flag = false;
                        }
                    }
                }
                if (flag) {
                    winner.clan.upExp(100, false);
                    winner.clan.upPointMember(winner.id, 100);
                }
            }
        }
        if (winner.clan != null) {
            for (ClanMember member : winner.clan.members) {
                Player player = PlayerManager.getInstance().findPlayerById(member.playerId);
                if (player != null && player.taskMain != null && player.taskMain.template.id == 18) {
                    if (index == 0 && player.taskMain.index == 2
                            || index == 1 && player.taskMain.index == 3
                            || index == 2 && player.taskMain.index == 4
                            || index == 3 && player.taskMain.index == 5
                            || index == 4 && player.taskMain.index == 6
                            || index == 5 && player.taskMain.index == 7
                            || index == 6 && player.taskMain.index == 8) {
                        player.nextTaskIndex();
                    }
                }
            }
        } else if (winner.taskMain != null && winner.taskMain.template.id == 18) {
            if (index == 0 && winner.taskMain.index == 2
                    || index == 1 && winner.taskMain.index == 3
                    || index == 2 && winner.taskMain.index == 4
                    || index == 3 && winner.taskMain.index == 5
                    || index == 4 && winner.taskMain.index == 6
                    || index == 5 && winner.taskMain.index == 7
                    || index == 6 && winner.taskMain.index == 8) {
                winner.nextTaskIndex();
            }
        }
    }

    public static void reward(Player player, int effectId) {
        if (player.isBagFull()) {
            player.addInfo(Player.INFO_RED, Language.ME_BAG_FULL);
            return;
        }
        Effect effect = player.findEffectByTemplateId(effectId);
        if (effect == null) {
            return;
        }
        long now = System.currentTimeMillis();
        long time = effect.delay + effect.updateTime - now;
        if (time > 0) {
            player.addInfo(Player.INFO_RED, "Chờ đợi là hạnh phúc");
            return;
        }
        effect.updateTime = now;
        switch (effect.template.id) {
            case EffectName.NGOC_RONG_NAMEK_3_SAO: {
                Item item = ItemManager.getInstance().createItem(ItemName.DA_10, 10, true);
                player.addItem(item);
                player.addInfo(Player.INFO_YELLOW, String.format("Bạn nhận được %s", item.template.name));
                break;
            }

            case EffectName.NGOC_RONG_NAMEK_4_SAO: {
                Item item = ItemManager.getInstance().createItem(ItemName.DAU_THAN_CAP_10, 30, true);
                player.addItem(item);
                player.addInfo(Player.INFO_YELLOW, String.format("Bạn nhận được x%d %s", item.quantity, item.template.name));
                break;
            }

            case EffectName.NGOC_RONG_NAMEK_5_SAO: {
                int diamond = 5;
                player.upRuby(diamond);
                player.addInfo(Player.INFO_YELLOW, String.format("Bạn nhận được %d Ruby", diamond));
                break;
            }

            case EffectName.NGOC_RONG_NAMEK_6_SAO: {
                long xu = 1000000;
                player.upXu(xu);
                player.addInfo(Player.INFO_YELLOW, String.format("Bạn nhận được %s xu", Utils.getMoneys(xu)));
                break;
            }

            case EffectName.NGOC_RONG_NAMEK_7_SAO: {
                Item item = ItemManager.getInstance().createItem(ItemName.CAPSULE_KI_BI, Utils.nextInt(20, 30), true);
                item.setExpiry(3);
                player.addItem(item);
                player.addInfo(Player.INFO_YELLOW, String.format("Bạn nhận được x%d %s", item.quantity, item.template.name));
                break;
            }
        }
    }

    @Override
    public void close() {
        isRunning = false;
        for (Map map : maps) {
            map.close();
        }
        maps.clear();
        MapManager.getInstance().dragonBallNamek = null;
    }

    @Override
    public void update() {
        if (isRunning && endTime < System.currentTimeMillis()) {
            close();
        }
    }


    @Override
    public void run() {
        while (isRunning) {
            long delay = 1000;
            try {
                long l1 = System.currentTimeMillis();
                update();
                long l2 = System.currentTimeMillis();
                long l3 = l2 - l1;
                if (l3 > delay) {
                    continue;
                }
                Thread.sleep(delay - l3);
            } catch (Exception ignored) {
            }
        }
    }
}
