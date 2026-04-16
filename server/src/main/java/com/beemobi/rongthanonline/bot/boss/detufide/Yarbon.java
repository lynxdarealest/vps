package com.beemobi.rongthanonline.bot.boss.detufide;

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
import java.util.Collections;
import java.util.List;

public class Yarbon extends Boss {
    private static final Logger logger = Logger.getLogger(Yarbon.class);
    private static final int[] MAPS = {MapName.LANG_OC_SEN, MapName.THI_TRAN_HOANG, MapName.LANG_GURU};

    public Yarbon() {
        super(BossName.YARBON, -1);
        typePk = 3;
    }

    @Override
    public void setSkill() {
        skills.add(SkillManager.getInstance().createSkill(SkillName.KARAK, 7, 1));
        skills.add(SkillManager.getInstance().createSkill(SkillName.KARAP, 7, 1));
        skills.add(SkillManager.getInstance().createSkill(SkillName.KARAV, 7, 1));
        skills.add(SkillManager.getInstance().createSkill(SkillName.KAME, 7));
        skills.add(SkillManager.getInstance().createSkill(SkillName.MASENDAN, 7));
        skills.add(SkillManager.getInstance().createSkill(SkillName.SOKIDAN, 7));
        //skills.add(SkillManager.getInstance().createSkill(SkillName.TAI_TAO_NANG_LUONG, 3));
    }


    @Override
    public void joinClient() {
        setLocation(MAPS[Utils.nextInt(MAPS.length)], -2);
    }

    @Override
    public List<ItemMap> throwItem(Entity killer) {
        List<ItemMap> itemMaps = new ArrayList<>();
        if (isNotThrowItem()) {
            return itemMaps;
        }
        if (Utils.isPercent(20)) {
            itemMaps.add(ItemManager.getInstance().createItemMap(ItemName.XU, Utils.nextInt(500000, 1000000), killer.id));
        } else {
            int xu = 10000 * level;
            if (xu < 100000) {
                xu = 100000;
            }
            itemMaps.add(ItemManager.getInstance().createItemMap(ItemName.XU, Utils.nextInt(xu - xu / 10, xu), killer.id));
        }
        if (Utils.isPercent(5)) {
            itemMaps.add(ItemManager.getInstance().createItemMap(ItemName.LOI_KI_NANG, Utils.nextInt(1, 3), killer.id));
        }
        if (Utils.isPercent(40)) {
            itemMaps.add(ItemManager.getInstance().createItemMap(ItemName.DA_7, 1, killer.id));
        }
        return itemMaps;
    }
}
