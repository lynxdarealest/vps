package com.beemobi.rongthanonline.bot.boss.lucifer;

import com.beemobi.rongthanonline.bot.boss.Boss;
import com.beemobi.rongthanonline.bot.boss.BossManager;
import com.beemobi.rongthanonline.bot.boss.BossName;
import com.beemobi.rongthanonline.entity.Entity;
import com.beemobi.rongthanonline.item.ItemManager;
import com.beemobi.rongthanonline.item.ItemMap;
import com.beemobi.rongthanonline.item.ItemName;
import com.beemobi.rongthanonline.skill.SkillManager;
import com.beemobi.rongthanonline.skill.SkillName;
import com.beemobi.rongthanonline.util.Utils;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SuperLucifer extends Boss {
    private static final Logger logger = Logger.getLogger(SuperLucifer.class);
    public int index;

    public SuperLucifer(int index) {
        super(BossName.SUPER_MA_VUONG, -1);
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
    public List<ItemMap> throwItem(Entity killer) {
        List<ItemMap> itemMaps = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            itemMaps.add(ItemManager.getInstance().createItemMap(ItemName.XU, Utils.nextInt(50000, 100000), -1));
        }
        itemMaps.add(ItemManager.getInstance().createItemMap(ItemName.QUA_TRUNG, 1, killer.id));
        return itemMaps;
    }

    @Override
    public void startDie(Entity killer) {
        Utils.setTimeout(() -> {
            Lucifer lucifer = BossManager.getInstance().lucifer[index];
            if (lucifer.isDead()) {
                lucifer.wakeUpFromDead();
            }
            lucifer.recovery(RECOVERY_ALL, 100, false);
            lucifer.joinClient();
        }, waitingTimeToLeave + 1000 + 900000);
        super.startDie(killer);
    }

    @Override
    public long formatDamageInjure(Entity attacker, long damage, boolean isCritical) {
        return Math.min(maxHp / 300, damage);
    }
}
