package com.beemobi.rongthanonline.bot.boss.fide;

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

public class Fide extends Boss {
    private static final Logger logger = Logger.getLogger(Fide.class);
    private static final int[] MAPS = {MapName.THUNG_LUNG_GRU, MapName.THUNG_LUNG_SIROI, MapName.THUNG_LUNG_TURI};

    public Fide() {
        super(BossName.FIDE_DAI_CA, -1);
        typePk = 3;
    }

    @Override
    public void setSkill() {
        skills.add(SkillManager.getInstance().createSkill(SkillName.KARAK, 7, 1));
        skills.add(SkillManager.getInstance().createSkill(SkillName.KARAP, 7, 1));
        skills.add(SkillManager.getInstance().createSkill(SkillName.KARAV, 7, 1));
        skills.add(SkillManager.getInstance().createSkill(SkillName.KAME, 7, 1));
        skills.add(SkillManager.getInstance().createSkill(SkillName.MASENDAN, 7, 1));
        skills.add(SkillManager.getInstance().createSkill(SkillName.SOKIDAN, 7, 1));
        //skills.add(SkillManager.getInstance().createSkill(SkillName.TAI_TAO_NANG_LUONG, 6));
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
            itemMaps.add(ItemManager.getInstance().createItemMap(ItemName.XU, Utils.nextInt(10000000, 15000000), killer.id));
        } else {
            itemMaps.add(ItemManager.getInstance().createItemMap(ItemName.XU, Utils.nextInt(1000000, 1500000), killer.id));
        }
        if (Utils.isPercent(20)) {
            itemMaps.add(ItemManager.getInstance().createItemMap(ItemName.LOI_KI_NANG, Utils.nextInt(3, 5), killer.id));
        }
        if (Utils.isPercent(40)) {
            itemMaps.add(ItemManager.getInstance().createItemMap(ItemName.DA_9, 1, killer.id));
        }
        if (Utils.isPercent(5)) {
            itemMaps.add(ItemManager.getInstance().createItemMap(ItemName.NGOC_RONG_4_SAO, 1, killer.id));
        } else {
            itemMaps.add(ItemManager.getInstance().createItemMap(ItemName.NGOC_RONG_5_SAO, 1, killer.id));
        }
        return itemMaps;
    }
}
