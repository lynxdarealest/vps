package com.beemobi.rongthanonline.bot.boss.ginyu;

import com.beemobi.rongthanonline.bot.boss.Boss;
import com.beemobi.rongthanonline.bot.boss.BossName;
import com.beemobi.rongthanonline.bot.boss.TeamBoss;
import com.beemobi.rongthanonline.entity.Entity;
import com.beemobi.rongthanonline.item.ItemManager;
import com.beemobi.rongthanonline.item.ItemMap;
import com.beemobi.rongthanonline.item.ItemName;
import com.beemobi.rongthanonline.skill.SkillManager;
import com.beemobi.rongthanonline.skill.SkillName;
import com.beemobi.rongthanonline.util.Utils;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

public class NumberAssassin extends Boss {
    private static final Logger logger = Logger.getLogger(NumberAssassin.class);

    public NumberAssassin(int id, TeamBoss team) {
        super(id, -1);
        typePk = 0;
        this.team = team;
    }

    @Override
    public void setSkill() {
        skills.add(SkillManager.getInstance().createSkill(SkillName.KARAK, 7, 1));
        skills.add(SkillManager.getInstance().createSkill(SkillName.KARAP, 7, 1));
        skills.add(SkillManager.getInstance().createSkill(SkillName.KARAV, 7, 1));
        skills.add(SkillManager.getInstance().createSkill(SkillName.KAME, 7));
        skills.add(SkillManager.getInstance().createSkill(SkillName.MASENDAN, 7));
        skills.add(SkillManager.getInstance().createSkill(SkillName.SOKIDAN, 7));
        //skills.add(SkillManager.getInstance().createSkill(SkillName.TAI_TAO_NANG_LUONG, 4));
    }

    @Override
    public List<ItemMap> throwItem(Entity killer) {
        List<ItemMap> itemMaps = new ArrayList<>();
        if (isNotThrowItem()) {
            return itemMaps;
        }
        if (Utils.isPercent(20)) {
            itemMaps.add(ItemManager.getInstance().createItemMap(ItemName.XU, Utils.nextInt(1000000, 1500000), killer.id));
        } else {
            int xu = 10000 * level;
            if (xu < 10000) {
                xu = 10000;
            }
            itemMaps.add(ItemManager.getInstance().createItemMap(ItemName.XU, Utils.nextInt(xu - xu / 10, xu), killer.id));
        }
        if (Utils.isPercent(20)) {
            itemMaps.add(ItemManager.getInstance().createItemMap(ItemName.LOI_KI_NANG, Utils.nextInt(3, 5), killer.id));
        }
        if (Utils.isPercent(30)) {
            itemMaps.add(ItemManager.getInstance().createItemMap(ItemName.DA_8, 1, killer.id));
        }
        if (Utils.isPercent(20)) {
            itemMaps.add(ItemManager.getInstance().createItemMap(ItemName.NGOC_RONG_5_SAO, 1, killer.id));
        }
        return itemMaps;
    }

    @Override
    public void startDie(Entity killer) {
        try {
            super.startDie(killer);
        } finally {
            if (team != null) {
                team.next(this);
            }
        }
    }
}