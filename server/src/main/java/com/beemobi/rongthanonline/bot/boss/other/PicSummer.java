package com.beemobi.rongthanonline.bot.boss.other;

import com.beemobi.rongthanonline.bot.boss.Boss;
import com.beemobi.rongthanonline.bot.boss.BossName;
import com.beemobi.rongthanonline.entity.Entity;
import com.beemobi.rongthanonline.item.ItemManager;
import com.beemobi.rongthanonline.item.ItemMap;
import com.beemobi.rongthanonline.item.ItemName;
import com.beemobi.rongthanonline.map.MapName;
import com.beemobi.rongthanonline.skill.SkillManager;
import com.beemobi.rongthanonline.skill.SkillName;
import com.beemobi.rongthanonline.util.Utils;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

public class PicSummer extends Boss {
    private static final Logger logger = Logger.getLogger(PicSummer.class);

    private static final int[] MAPS = {MapName.DAO_HOA_1};

    public PicSummer() {
        super(BossName.PIC_SUMMER, -1);
        typePk = 3;
        delayRespawn = 1800000;
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
        skills.add(SkillManager.getInstance().createSkill(SkillName.TAI_TAO_NANG_LUONG, 3));
    }

    @Override
    public void joinClient() {
        setLocation(MAPS[Utils.nextInt(MAPS.length)], -2);
    }

    @Override
    public long formatDamageInjure(Entity attacker, long damage, boolean isCritical) {
        return Math.min(maxHp / 200, damage);
    }

    @Override
    public List<ItemMap> throwItem(Entity killer) {
        List<ItemMap> itemMaps = new ArrayList<>();
        if (Utils.isPercent(20)) {
            itemMaps.add(ItemManager.getInstance().createItemMap(ItemName.THE_GIAM_GIA, 1, killer.id));
        }
        int size = Utils.nextInt(3, 5);
        for (int i = 0; i < size; i++) {
            itemMaps.add(ItemManager.getInstance().createItemMap(ItemName.RUBY, 1, -1));
        }
        size = Utils.nextInt(5, 10);
        for (int i = 0; i < size; i++) {
            itemMaps.add(ItemManager.getInstance().createItemMap(ItemName.XU_KHOA, Utils.nextInt(100000, 200000), -1));
        }
        size = Utils.nextInt(5, 10);
        for (int i = 0; i < size; i++) {
            itemMaps.add(ItemManager.getInstance().createItemMap(ItemName.DA_5, 1, -1));
        }
        return itemMaps;
    }

}

