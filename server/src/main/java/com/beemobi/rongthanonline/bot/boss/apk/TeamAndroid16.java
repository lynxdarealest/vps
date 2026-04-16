package com.beemobi.rongthanonline.bot.boss.apk;

import com.beemobi.rongthanonline.bot.boss.Boss;
import com.beemobi.rongthanonline.bot.boss.TeamBoss;
import com.beemobi.rongthanonline.bot.boss.TeamBossStatus;
import com.beemobi.rongthanonline.map.Map;
import com.beemobi.rongthanonline.map.MapManager;
import com.beemobi.rongthanonline.map.MapName;
import com.beemobi.rongthanonline.util.Utils;
import org.apache.log4j.Logger;

public class TeamAndroid16 extends TeamBoss {
    private static final Logger logger = Logger.getLogger(TeamAndroid16.class);
    public static final int[] MAPS = {MapName.RUNG_AMAZON, MapName.RUNG_CHET_3, MapName.RUNG_CHET_4, MapName.RUNG_CHET_5};
    private final Android16 android16;
    private final Android17 android17;
    private final Android18 android18;

    public TeamAndroid16() {
        android16 = new Android16(this);
        android17 = new Android17(this);
        android18 = new Android18(this);
        saysWhenBeforeAttack = new Object[][]{
                {android16, "Mục đích của chúng ta là hạ Sôgôku"},
                {android16, "Chúng ta sẽ giết tất cả những kẻ cản đường"},
                {android18, "Tớ không có hứng với chúng chút nào"},
                {android17, "Chúng ta có thể dùng chúng để làm nóng tay chân"},
                {android18, "Một ý kiến không tệ"},
                {android17, "Các ngươi nghĩ sao về việc làm nô lệ cho bọn ta"},
                {null, "Cái gì? Bọn ta mà làm nô lệ ư?"},
                {null, "Bọn ta sẽ bảo vệ Goku bằng mọi giá"},
                {android16, "Thế thì nộp mạng đi!"}
        };
    }

    @Override
    public void next(Boss boss) {
        if (android16.isDead() && android17.isDead() && android18.isDead()) {
            end();
        }
    }

    @Override
    public void end() {
        Utils.setTimeout(this::born, android16.delayRespawn);
    }

    @Override
    public void born() {
        status = TeamBossStatus.WAIT_PLAYER;
        chatter = null;
        indexChat = 0;
        if (android16.isDead()) {
            android16.wakeUpFromDead();
        }
        if (android17.isDead()) {
            android17.wakeUpFromDead();
        }
        if (android18.isDead()) {
            android18.wakeUpFromDead();
        }
        android16.timeBorn = android17.timeBorn = android18.timeBorn = System.currentTimeMillis();
        Map map = MapManager.getInstance().maps.get(Utils.nextArray(MAPS));
        if (map != null) {
            android16.teleport(map, -2);
            android17.teleport(map, android16.zone.id);
            android18.teleport(map, android16.zone.id);
            android16.setTypePk(0);
            android17.setTypePk(0);
            android18.setTypePk(0);
        }
    }

    @Override
    public void setStatus(TeamBossStatus status) {
        try {
            if (status == TeamBossStatus.ATTACK) {
                android16.setTypePk(3);
                android17.setTypePk(3);
                android18.setTypePk(3);
            }
        } finally {
            super.setStatus(status);
        }
    }
}
