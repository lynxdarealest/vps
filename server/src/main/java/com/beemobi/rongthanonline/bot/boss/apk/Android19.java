package com.beemobi.rongthanonline.bot.boss.apk;

import com.beemobi.rongthanonline.bot.boss.BossName;
import com.beemobi.rongthanonline.bot.boss.TeamBoss;
import org.apache.log4j.Logger;

public class Android19 extends Android {
    private static final Logger logger = Logger.getLogger(Android19.class);

    public Android19(TeamBoss team) {
        super(BossName.ANDROID_19, team);
        sayTheLastWordBeforeDie = "Chúng mạnh quá đại ca ơi, ngoài sức tưởng tượng của em rồi!";
        saysWhenAttack.add("Mi khá đấy nhưng so với ta chỉ là hạng tôm tép");
        saysWhenAttack.add("Ta sẽ hút cạn năng lượng của chúng bây");
        saysWhenAttack.add("Chưởng lực chỉ có vô dụng với ta thôi hahaha");
        options[117] = 1000;
    }
}
