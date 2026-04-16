package com.beemobi.rongthanonline.bot.boss.apk;

import com.beemobi.rongthanonline.bot.boss.Boss;
import com.beemobi.rongthanonline.bot.boss.TeamBoss;
import com.beemobi.rongthanonline.bot.boss.TeamBossStatus;
import com.beemobi.rongthanonline.map.Map;
import com.beemobi.rongthanonline.map.MapManager;
import com.beemobi.rongthanonline.map.MapName;
import com.beemobi.rongthanonline.util.Utils;
import org.apache.log4j.Logger;

public class TeamAndroid13 extends TeamBoss {
    private static final Logger logger = Logger.getLogger(TeamAndroid13.class);
    private final Android13 android13;
    private final Android14 android14;
    private final Android15 android15;

    public TeamAndroid13() {
        android13 = new Android13(this);
        android14 = new Android14(this);
        android15 = new Android15(this);
        saysWhenBeforeAttack = new Object[][]{
                {null, "Các ngươi là ai?"},
                {null, "Ta không thể cảm nhận KI từ các ngươi, các ngươi không phải là con người!"},
                {null, "Ta biết rồi, các ngươi là Robot sát thủ được tạo ra bởi tiến sĩ Kôrê phải không?"},
                {null, "Hắn ta vẫn chưa từ bỏ hả"},
                {android13, "Hmm"},
                {android13, "Có vẻ như Sôngôku sẽ không chiến đấu được như ta nghĩ!"},
                {null, "Lão giáo sư đó không biết đã tạo ra bao nhiêu Robot sát thủ đây?"},
                {android13, "Chúng ta là người máy được tạo ra từ máy tính của tiến sĩ Kôrê"},
                {android13, "Với mục đích giết Sôngôku"},
                {android14, "Khát vọng trả thù của tiến sĩ đã được chuyển vào máy tính"},
                {android14, "Bọn ta là những gì được tạo ra từ sự thù hận ngày càng tăng"},
                {android15, "Mục tiêu duy nhất của bọn ta là tiêu diệt Sôngôku"},
                {android15, "Tuy nhiên, nếu các ngươi cản đường thì đó sẽ là chuyện khác"},
                {android15, "Bọn ta sẽ giết hết tất cả những kẻ bảo vệ Sôngôku"},
                {android13, "Nộp mạng đi"},
        };
    }

    @Override
    public void next(Boss boss) {
        if (android13.isDead() && android14.isDead() && android15.isDead()) {
            end();
        }
    }

    @Override
    public void end() {
        Utils.setTimeout(this::born, android13.delayRespawn);
    }

    @Override
    public void born() {
        status = TeamBossStatus.WAIT_PLAYER;
        chatter = null;
        indexChat = 0;
        if (android13.isDead()) {
            android13.wakeUpFromDead();
        }
        if (android14.isDead()) {
            android14.wakeUpFromDead();
        }
        if (android15.isDead()) {
            android15.wakeUpFromDead();
        }
        android13.timeBorn = android14.timeBorn = android15.timeBorn = System.currentTimeMillis();
        Map map = MapManager.getInstance().maps.get(MapName.THANH_PHO_PHIA_BAC);
        if (map != null) {
            android13.teleport(map, -2);
            android14.teleport(map, android13.zone.id);
            android15.teleport(map, android13.zone.id);
            android13.setTypePk(0);
            android14.setTypePk(0);
            android15.setTypePk(0);
        }
    }

    @Override
    public void setStatus(TeamBossStatus status) {
        try {
            if (status == TeamBossStatus.ATTACK) {
                android13.setTypePk(3);
                android14.setTypePk(3);
                android15.setTypePk(3);
            }
        } finally {
            super.setStatus(status);
        }
    }
}
