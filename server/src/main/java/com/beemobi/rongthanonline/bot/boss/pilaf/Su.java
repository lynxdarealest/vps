package com.beemobi.rongthanonline.bot.boss.pilaf;

import com.beemobi.rongthanonline.bot.boss.Boss;
import com.beemobi.rongthanonline.bot.boss.BossName;
import com.beemobi.rongthanonline.entity.Entity;
import com.beemobi.rongthanonline.map.MapName;
import com.beemobi.rongthanonline.skill.SkillManager;
import com.beemobi.rongthanonline.skill.SkillName;
import com.beemobi.rongthanonline.util.Utils;
import org.apache.log4j.Logger;

public class Su extends Boss {
    private static final Logger logger = Logger.getLogger(Su.class);

    private static final int[] MAPS = {MapName.THANH_PHO_PHIA_NAM};

    public Su() {
        super(BossName.SU, -1);
        typePk = 3;
        taskId = 9;
        taskIndex = 1;
        delayRespawn = 300000;
        isHasPoint = false;
    }

    @Override
    public void setSkill() {
        skills.add(SkillManager.getInstance().createSkill(SkillName.KAME, 3));
        skills.add(SkillManager.getInstance().createSkill(SkillName.MASENDAN, 3));
        skills.add(SkillManager.getInstance().createSkill(SkillName.SOKIDAN, 3));
    }

    @Override
    public void joinClient() {
        setLocation(MAPS[Utils.nextInt(MAPS.length)], -2);
    }
}
