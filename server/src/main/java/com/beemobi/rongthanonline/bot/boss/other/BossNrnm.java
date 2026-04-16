package com.beemobi.rongthanonline.bot.boss.other;

import com.beemobi.rongthanonline.bot.boss.Boss;
import com.beemobi.rongthanonline.bot.boss.BossName;
import com.beemobi.rongthanonline.entity.Entity;
import com.beemobi.rongthanonline.map.Zone;
import com.beemobi.rongthanonline.map.expansion.nrnm.ZoneDragonBallNamek;
import com.beemobi.rongthanonline.skill.SkillManager;
import com.beemobi.rongthanonline.skill.SkillName;
import org.apache.log4j.Logger;

public class BossNrnm extends Boss {
    private static final Logger logger = Logger.getLogger(BossNrnm.class);

    private final ZoneDragonBallNamek zoneDragonBallNamek;

    public BossNrnm(int index, ZoneDragonBallNamek zoneDragonBallNamek) {
        super(BossName.BOSS_NRNM, -1);
        this.zoneDragonBallNamek = zoneDragonBallNamek;
        typePk = 3;
        switch (index) {
            case 0:
                name = "Số 1";
                head = 65;
                body = 66;
                break;
            case 1:
                name = "Số 2";
                head = 63;
                body = 64;
                break;
            case 2:
                name = "Số 3";
                head = 61;
                body = 64;
                break;
            case 3:
                name = "Số 4";
                head = 59;
                body = 60;
                break;
            case 4:
                name = "Yarbon";
                head = 36;
                body = 37;
                break;
            case 5:
                name = "Dodo";
                head = 40;
                body = 41;
                break;
            default:
                name = "Kui";
                head = 38;
                body = 39;
                break;
        }
        isHasPoint = false;
    }

    @Override
    public void setSkill() {
        skills.add(SkillManager.getInstance().createSkill(SkillName.KARAK, 7, 1));
        skills.add(SkillManager.getInstance().createSkill(SkillName.KARAP, 7, 1));
        skills.add(SkillManager.getInstance().createSkill(SkillName.KARAV, 7, 1));
        skills.add(SkillManager.getInstance().createSkill(SkillName.KAME, 7));
        skills.add(SkillManager.getInstance().createSkill(SkillName.MASENDAN, 7));
        skills.add(SkillManager.getInstance().createSkill(SkillName.SOKIDAN, 7));
        skills.add(SkillManager.getInstance().createSkill(SkillName.TAI_TAO_NANG_LUONG, 6));
    }

    @Override
    public void startDie(Entity killer) {
        super.startDie(killer);
        if (killer != null) {
            zoneDragonBallNamek.killerId = killer.id;
        }
    }

    @Override
    public long formatDamageInjure(Entity attacker, long damage, boolean isCritical) {
        return Math.min(maxHp / 100, damage);
    }

    @Override
    public void sendNotificationWhenAppear(Zone zone) {

    }
}
