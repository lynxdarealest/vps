package com.beemobi.rongthanonline.bot.boss.other;

import com.beemobi.rongthanonline.bot.boss.Boss;
import com.beemobi.rongthanonline.bot.boss.BossName;
import com.beemobi.rongthanonline.entity.Entity;
import com.beemobi.rongthanonline.item.*;
import com.beemobi.rongthanonline.map.Zone;
import com.beemobi.rongthanonline.map.expansion.survival.Survival;
import com.beemobi.rongthanonline.skill.SkillManager;
import com.beemobi.rongthanonline.skill.SkillName;
import com.beemobi.rongthanonline.util.Utils;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class BossSurvival extends Boss {
    private static final Logger logger = Logger.getLogger(BossSurvival.class);
    public Survival survival;

    public BossSurvival(Survival survival, int level) {
        super(BossName.BOSS_SURVIVAL, level);
        typePk = 3;
        List<ItemTemplate> items = ItemManager.getInstance().itemTemplates.values().stream().filter(i -> i.type == 8 && i.body != -1 && i.head != -1).collect(Collectors.toList());
        ItemTemplate item = items.get(Utils.nextInt(items.size()));
        head = item.head;
        body = item.body;
        options[OptionName.CHINH_XAC] = 200000;
        hp = maxHp = template.hp * level;
        damage = (long) template.damage * level;
        this.survival = survival;
    }

    @Override
    public void setSkill() {
        skills.add(SkillManager.getInstance().createSkill(SkillName.KARAK, 7, 1));
        skills.add(SkillManager.getInstance().createSkill(SkillName.KARAP, 7, 1));
        skills.add(SkillManager.getInstance().createSkill(SkillName.KARAV, 7, 1));
        skills.add(SkillManager.getInstance().createSkill(SkillName.KAME, 7));
        skills.add(SkillManager.getInstance().createSkill(SkillName.MASENDAN, 7));
        skills.add(SkillManager.getInstance().createSkill(SkillName.SOKIDAN, 7));
    }

    @Override
    public List<ItemMap> throwItem(Entity killer) {
        List<ItemMap> itemMaps = new ArrayList<>();
        long now = System.currentTimeMillis();
        int size = Utils.nextInt(3, 5);
        for (int i = 0; i < size; i++) {
            int item;
            if (Utils.nextInt(100) < 20) {
                item = ItemName.DAU_THAN_CAP_1;
            } else if (survival.round < 3) {
                item = Utils.nextInt(176, 191);
            } else if (survival.round < 7) {
                item = Utils.nextInt(192, 207);
            } else {
                item = Utils.nextInt(208, 223);
            }
            ItemMap itemMap = ItemManager.getInstance().createItemMap(item, 1, killer.id);
            itemMap.throwTime = now + 30000;
        }
        return itemMaps;
    }

    @Override
    public void sendNotificationWhenAppear(Zone zone) {

    }

    @Override
    public void sendNotificationWhenDead(String name) {

    }
}
