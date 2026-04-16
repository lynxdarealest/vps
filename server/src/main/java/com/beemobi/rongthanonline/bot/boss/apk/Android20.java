package com.beemobi.rongthanonline.bot.boss.apk;

import com.beemobi.rongthanonline.bot.boss.BossName;
import com.beemobi.rongthanonline.bot.boss.TeamBoss;
import org.apache.log4j.Logger;

public class Android20 extends Android {
    private static final Logger logger = Logger.getLogger(Android20.class);

    public Android20(TeamBoss team) {
        super(BossName.ANDROID_20, team);
        sayTheLastWordBeforeDie = "Được đấy, sẽ có lúc chúng mày bại dưới tay tao";
        saysWhenAttack.add("Mau đền mạng cho thằng em ta");
        saysWhenAttack.add("Ta sẽ tiễn chúng bay xuống địa ngục");
        saysWhenAttack.add("Chết đi...");
        options[117] = 2000;
    }
}
