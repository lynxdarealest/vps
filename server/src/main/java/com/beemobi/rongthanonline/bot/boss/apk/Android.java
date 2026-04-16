package com.beemobi.rongthanonline.bot.boss.apk;

import com.beemobi.rongthanonline.bot.boss.Boss;
import com.beemobi.rongthanonline.bot.boss.TeamBoss;
import com.beemobi.rongthanonline.common.RandomCollection;
import com.beemobi.rongthanonline.entity.Entity;
import com.beemobi.rongthanonline.item.ItemManager;
import com.beemobi.rongthanonline.item.ItemMap;
import com.beemobi.rongthanonline.item.ItemName;
import com.beemobi.rongthanonline.item.ItemOption;
import com.beemobi.rongthanonline.skill.SkillManager;
import com.beemobi.rongthanonline.skill.SkillName;
import com.beemobi.rongthanonline.util.Utils;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

public class Android extends Boss {
    private static final Logger logger = Logger.getLogger(Android.class);

    public static RandomCollection<Integer> DRAGON_BALL = new RandomCollection<>();

    static {
        DRAGON_BALL.add(20, ItemName.NGOC_RONG_3_SAO);
        DRAGON_BALL.add(40, ItemName.NGOC_RONG_4_SAO);
        DRAGON_BALL.add(40, ItemName.NGOC_RONG_5_SAO);
    }

    public Android(int id, TeamBoss team) {
        super(id, -1);
        typePk = 0;
        this.team = team;
        options[121] = 20;
    }


    @Override
    public void setSkill() {
        skills.add(SkillManager.getInstance().createSkill(SkillName.KARAK, 7, 2));
        skills.add(SkillManager.getInstance().createSkill(SkillName.KARAP, 7, 2));
        skills.add(SkillManager.getInstance().createSkill(SkillName.KARAV, 7, 2));
        skills.add(SkillManager.getInstance().createSkill(SkillName.KAME, 7, 2));
        skills.add(SkillManager.getInstance().createSkill(SkillName.MASENDAN, 7, 2));
        skills.add(SkillManager.getInstance().createSkill(SkillName.SOKIDAN, 7, 2));
        //skills.add(SkillManager.getInstance().createSkill(SkillName.TAI_TAO_NANG_LUONG, 7));
    }

    @Override
    public List<ItemMap> throwItem(Entity killer) {
        List<ItemMap> itemMaps = new ArrayList<>();
        if (isNotThrowItem()) {
            return itemMaps;
        }
        if (Utils.isPercent(10)) {
            itemMaps.add(ItemManager.getInstance().createItemMap(ItemName.XU, Utils.nextInt(10000000, 15000000), killer.id));
        } else {
            itemMaps.add(ItemManager.getInstance().createItemMap(ItemName.XU, Utils.nextInt(1000000, 2000000), killer.id));
        }
        if (Utils.isPercent(40)) {
            itemMaps.add(ItemManager.getInstance().createItemMap(ItemName.DA_9, 1, killer.id));
        }
        itemMaps.add(ItemManager.getInstance().createItemMap(DRAGON_BALL.next(), 1, killer.id));
        if (Utils.isPercent(10)) {
            ItemMap itemMap = ItemManager.getInstance().createItemMapRandomOption(Utils.nextInt(292, 307), 1, killer.id);
            if (Utils.isPercent(10)) {
                if (Utils.isPercent(1)) {
                    itemMap.options.add(new ItemOption(67, Utils.nextInt(4, 6)));
                } else {
                    itemMap.options.add(new ItemOption(67, Utils.nextInt(1, 3)));
                }
            }
            itemMaps.add(itemMap);
        }
        return itemMaps;
    }

    @Override
    public void startDie(Entity killer) {
        try {
            super.startDie(killer);
        } finally {
            team.next(this);
        }
    }
}
