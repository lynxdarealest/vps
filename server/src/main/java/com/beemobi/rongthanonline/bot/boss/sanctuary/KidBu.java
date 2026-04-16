package com.beemobi.rongthanonline.bot.boss.sanctuary;

import com.beemobi.rongthanonline.entity.Entity;
import com.beemobi.rongthanonline.entity.player.Player;
import com.beemobi.rongthanonline.skill.SkillName;

public class KidBu extends Mabu {
    public KidBu() {
        super(Mabu.BU.length - 1);
    }

    @Override
    public long formatDamageInjure(Entity attacker, long damage, boolean isCritical) {
        long limit = 1000000;
        if (this.hp > limit) {
            return Math.min(super.formatDamageInjure(attacker, damage, isCritical), this.hp - limit);
        } else {
            if (attacker != null && attacker.isPlayer()) {
                Player player = (Player) attacker;
                if (player.mySkill != null) {
                    int id = player.mySkill.template.id;
                    if (id == SkillName.QUA_CAU_GENKI || id == SkillName.TU_PHAT_NO || id == SkillName.LAZE) {
                        return super.formatDamageInjure(attacker, damage, isCritical);
                    }
                }
            }
            return 0;
        }
    }
}
