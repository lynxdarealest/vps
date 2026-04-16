package com.beemobi.rongthanonline.bot.boss.apk;

import com.beemobi.rongthanonline.bot.boss.BossName;
import com.beemobi.rongthanonline.bot.boss.TeamBoss;
import com.beemobi.rongthanonline.skill.SkillManager;
import com.beemobi.rongthanonline.skill.SkillName;
import org.apache.log4j.Logger;

public class Android16 extends Android {
    private static final Logger logger = Logger.getLogger(Android16.class);

    public Android16(TeamBoss team) {
        super(BossName.ANDROID_16, team);
    }

    @Override
    public void setSkill() {
        super.setSkill();
        skills.add(SkillManager.getInstance().createSkill(SkillName.KHIEN_NANG_LUONG, 7));
        saysWhenAttack.add("Các ngươi tính bắt nạt 1 cô gái?");
        saysWhenAttack.add("Các ngươi không có cửa thắng bọn ta đâu");
        saysWhenAttack.add("Có ít sức mạnh thế hả?");
    }
}
