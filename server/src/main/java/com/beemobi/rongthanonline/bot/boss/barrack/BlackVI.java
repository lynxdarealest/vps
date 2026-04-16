package com.beemobi.rongthanonline.bot.boss.barrack;

import com.beemobi.rongthanonline.bot.boss.Boss;
import com.beemobi.rongthanonline.bot.boss.BossName;
import com.beemobi.rongthanonline.entity.Entity;
import com.beemobi.rongthanonline.item.ItemManager;
import com.beemobi.rongthanonline.item.ItemMap;
import com.beemobi.rongthanonline.item.ItemName;
import com.beemobi.rongthanonline.item.OptionName;
import com.beemobi.rongthanonline.map.Zone;
import com.beemobi.rongthanonline.server.ServerRandom;
import com.beemobi.rongthanonline.skill.SkillManager;
import com.beemobi.rongthanonline.skill.SkillName;
import com.beemobi.rongthanonline.util.Utils;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

public class BlackVI extends Boss {
    private static final Logger logger = Logger.getLogger(BlackVI.class);

    public BlackVI(int level) {
        super(BossName.BLACK_VI, -1);
        typePk = 3;
        options[OptionName.PHAN_SAT_THUONG] = 10000;
        this.level = level;
        int per = (level / 10) * level;
        if (per < 1) {
            per = 1;
        }
        hp = maxHp = template.hp * per * Math.max(per / 20, 1);
        damage = (long) template.damage * per * Math.max(level / 40, 1L);
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
        //skills.add(SkillManager.getInstance().createSkill(SkillName.TAI_TAO_NANG_LUONG, 3));
    }

    @Override
    public List<ItemMap> throwItem(Entity killer) {
        List<ItemMap> itemMaps = new ArrayList<>();
        itemMaps.add(ItemManager.getInstance().createItemMap(ItemName.XU, Utils.nextInt(5000 * level, 10000 * level), killer.id));
        itemMaps.add(ItemManager.getInstance().createItemMap(ServerRandom.DRAGON_BALL_BARRACK.next(), 1, killer.id));
        return itemMaps;
    }

    @Override
    public void sendNotificationWhenAppear(Zone zone) {

    }

    @Override
    public void sendNotificationWhenDead(String name) {

    }
}
