package com.beemobi.rongthanonline.bot.boss.apk;

import com.beemobi.rongthanonline.bot.boss.BossName;
import com.beemobi.rongthanonline.bot.boss.TeamBoss;
import org.apache.log4j.Logger;

public class Android15 extends Android {
    private static final Logger logger = Logger.getLogger(Android15.class);

    public Android15(TeamBoss team) {
        super(BossName.ANDROID_15, team);
    }
}
