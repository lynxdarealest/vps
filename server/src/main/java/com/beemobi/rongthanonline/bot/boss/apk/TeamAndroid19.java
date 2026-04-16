package com.beemobi.rongthanonline.bot.boss.apk;

import com.beemobi.rongthanonline.bot.boss.Boss;
import com.beemobi.rongthanonline.bot.boss.TeamBoss;
import com.beemobi.rongthanonline.bot.boss.TeamBossStatus;
import com.beemobi.rongthanonline.map.Map;
import com.beemobi.rongthanonline.map.MapManager;
import com.beemobi.rongthanonline.map.MapName;
import com.beemobi.rongthanonline.util.Utils;
import org.apache.log4j.Logger;

public class TeamAndroid19 extends TeamBoss {
    private static final Logger logger = Logger.getLogger(TeamAndroid19.class);
    public static final int[] MAPS = {MapName.RUNG_CHET_1, MapName.RUNG_CHET_2};
    private final Android19 android19;
    private final Android20 android20;

    public TeamAndroid19() {
        android19 = new Android19(this);
        android20 = new Android20(this);
        saysWhenBeforeAttack = new Object[][]{
                {android20, "Quái lạ! Sao chúng biết rõ tung tích của bọn ta thế nhỉ?"},
                {android20, "Chúng còn biết chính xác ta sẽ xuất hiện ở đây để đón đánh nữa!"},
                {android20, "Chúng mày là ai từ đâu tới?"},
                {null, "Điều đó không quan trọng"},
                {android20, "Ừ! Bọn bây chỉ là hạng tép riu, ta chẳng cần biết tên làm gì!"},
                {android20, "Số 19! Xuất chiêu"},
                {android19, "Hehe! Tuân lệnh đại ca. Em sẽ xử bọn chúng"}
        };
    }

    @Override
    public void next(Boss boss) {
        if (boss instanceof Android19) {
            android20.setTypePk(3);
        }
        if (android19.isDead() && android20.isDead()) {
            end();
        }
    }

    @Override
    public void end() {
        Utils.setTimeout(this::born, android19.delayRespawn);
    }

    @Override
    public void born() {
        status = TeamBossStatus.WAIT_PLAYER;
        chatter = null;
        indexChat = 0;
        if (android19.isDead()) {
            android19.wakeUpFromDead();
        }
        if (android20.isDead()) {
            android20.wakeUpFromDead();
        }
        android19.timeBorn = android20.timeBorn = System.currentTimeMillis();
        Map map = MapManager.getInstance().maps.get(Utils.nextArray(MAPS));
        if (map != null) {
            android19.teleport(map, -2);
            android20.teleport(map, android19.zone.id);
            android19.setTypePk(0);
            android20.setTypePk(0);
        }
    }

    @Override
    public void setStatus(TeamBossStatus status) {
        try {
            if (status == TeamBossStatus.ATTACK) {
                android19.setTypePk(3);
                android20.setTypePk(0);
            }
        } finally {
            super.setStatus(status);
        }
    }
}
