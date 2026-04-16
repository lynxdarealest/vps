package com.beemobi.rongthanonline.bot.boss.lucifer;

import com.beemobi.rongthanonline.bot.boss.Boss;
import com.beemobi.rongthanonline.bot.boss.BossManager;
import com.beemobi.rongthanonline.bot.boss.BossName;
import com.beemobi.rongthanonline.entity.Entity;
import com.beemobi.rongthanonline.map.Zone;
import com.beemobi.rongthanonline.map.MapName;
import com.beemobi.rongthanonline.skill.SkillManager;
import com.beemobi.rongthanonline.skill.SkillName;
import com.beemobi.rongthanonline.util.Utils;
import org.apache.log4j.Logger;

public class Lucifer extends Boss {
    private static final Logger logger = Logger.getLogger(Lucifer.class);
    private static final int[] MAPS = {MapName.BO_SONG_PU, MapName.THAC_NUOC_KEISE,
            MapName.RUNG_NAM_FUKA, MapName.DOI_HOANG_AKA, MapName.THUNG_LUNG_MARIA,
            MapName.DONG_BANG_MIKA, MapName.CAO_NGUYEN_TAKA, MapName.RUNG_GOZA, MapName.LANG_ARU};

    public int index;

    public Lucifer(int index) {
        super(BossName.MA_VUONG, -1);
        typePk = 3;
        name += " " + (index + 1);
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
        skills.add(SkillManager.getInstance().createSkill(SkillName.TAI_TAO_NANG_LUONG, 1));
    }

    @Override
    public void joinClient() {
        setLocation(MAPS[Utils.nextInt(MAPS.length)], -2);
    }

    @Override
    public void startDie(Entity killer) {
        Zone zone = this.zone;
        Utils.setTimeout(() -> {
            SuperLucifer superLucifer = BossManager.getInstance().superLucifer[index];
            if (superLucifer.isDead()) {
                superLucifer.wakeUpFromDead();
            }
            superLucifer.recovery(RECOVERY_ALL, 100, false);
            superLucifer.setLocation(zone);
        }, waitingTimeToLeave + 1000);
        super.startDie(killer);
    }

    @Override
    public long formatDamageInjure(Entity attacker, long damage, boolean isCritical) {
        return Math.min(maxHp / 150, damage);
    }
}
