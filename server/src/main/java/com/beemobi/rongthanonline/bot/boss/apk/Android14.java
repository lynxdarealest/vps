package com.beemobi.rongthanonline.bot.boss.apk;

import com.beemobi.rongthanonline.bot.boss.BossName;
import com.beemobi.rongthanonline.bot.boss.TeamBoss;
import org.apache.log4j.Logger;

public class Android14 extends Android {
    private static final Logger logger = Logger.getLogger(Android14.class);

    public Android14(TeamBoss team) {
        super(BossName.ANDROID_14, team);
    }

}
