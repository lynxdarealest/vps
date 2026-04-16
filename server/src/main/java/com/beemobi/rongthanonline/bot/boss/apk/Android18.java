package com.beemobi.rongthanonline.bot.boss.apk;

import com.beemobi.rongthanonline.bot.boss.BossName;
import com.beemobi.rongthanonline.bot.boss.TeamBoss;
import com.beemobi.rongthanonline.skill.SkillManager;
import com.beemobi.rongthanonline.skill.SkillName;
import org.apache.log4j.Logger;

public class Android18 extends Android {
    private static final Logger logger = Logger.getLogger(Android18.class);

    public Android18(TeamBoss team) {
        super(BossName.ANDROID_18, team);
    }

    @Override
    public void setSkill() {
        super.setSkill();
        skills.add(SkillManager.getInstance().createSkill(SkillName.KHIEN_NANG_LUONG, 7));
        saysWhenAttack.add("Ta có đẹp không?");
        saysWhenAttack.add("Ta sẽ giết tất cả kẻ cản đường nhiệm vụ của bọn ta");
        saysWhenAttack.add("Chụy sẽ nhẹ tay với chúng bay");
    }
}
