package com.beemobi.rongthanonline.bot.boss.apk;

import com.beemobi.rongthanonline.bot.boss.BossName;
import com.beemobi.rongthanonline.bot.boss.TeamBoss;
import com.beemobi.rongthanonline.skill.SkillManager;
import com.beemobi.rongthanonline.skill.SkillName;
import org.apache.log4j.Logger;

public class Android17 extends Android {
    private static final Logger logger = Logger.getLogger(Android17.class);

    public Android17(TeamBoss team) {
        super(BossName.ANDROID_17, team);
    }

    @Override
    public void setSkill() {
        super.setSkill();
        skills.add(SkillManager.getInstance().createSkill(SkillName.KHIEN_NANG_LUONG, 7));
        saysWhenAttack.add("Chúng quá yếu");
        saysWhenAttack.add("Các ngươi không xứng làm nô lệ của bọn tao");
        saysWhenAttack.add("Chúng tao sẽ cho bọn mày đi trước Sôgôku 1 đoạn");
    }
}
