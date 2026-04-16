package com.beemobi.rongthanonline.bot.boss.ginyu;

import com.beemobi.rongthanonline.bot.boss.BossName;
import com.beemobi.rongthanonline.bot.boss.TeamBoss;
import com.beemobi.rongthanonline.skill.SkillManager;
import com.beemobi.rongthanonline.skill.SkillName;
import org.apache.log4j.Logger;

public class Ginyu extends NumberAssassin {
    private static final Logger logger = Logger.getLogger(Ginyu.class);

    public Ginyu(TeamBoss team) {
        super(BossName.GINYU, team);
    }

    @Override
    public void setSkill() {
        skills.clear();
        skills.add(SkillManager.getInstance().createSkill(SkillName.KARAK, 7, 1));
        skills.add(SkillManager.getInstance().createSkill(SkillName.KARAP, 7, 1));
        skills.add(SkillManager.getInstance().createSkill(SkillName.KARAV, 7, 1));
        skills.add(SkillManager.getInstance().createSkill(SkillName.KAME, 7));
        skills.add(SkillManager.getInstance().createSkill(SkillName.MASENDAN, 7));
        skills.add(SkillManager.getInstance().createSkill(SkillName.SOKIDAN, 7));
        skills.add(SkillManager.getInstance().createSkill(SkillName.TAI_TAO_NANG_LUONG, 5));
    }
}
