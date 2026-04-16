package com.beemobi.rongthanonline.bot.boss.manor;

import com.beemobi.rongthanonline.bot.boss.Boss;
import com.beemobi.rongthanonline.bot.boss.BossName;
import com.beemobi.rongthanonline.common.RandomCollection;
import com.beemobi.rongthanonline.effect.Effect;
import com.beemobi.rongthanonline.effect.EffectName;
import com.beemobi.rongthanonline.entity.Entity;
import com.beemobi.rongthanonline.entity.player.Player;
import com.beemobi.rongthanonline.item.ItemManager;
import com.beemobi.rongthanonline.item.ItemMap;
import com.beemobi.rongthanonline.item.ItemName;
import com.beemobi.rongthanonline.map.Zone;
import com.beemobi.rongthanonline.skill.SkillManager;
import com.beemobi.rongthanonline.skill.SkillName;
import com.beemobi.rongthanonline.util.Utils;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

public class FideCold extends Boss {
    private static final Logger logger = Logger.getLogger(FideCold.class);

    public FideCold(int level) {
        super(BossName.FIDE_COLD, level);
        int per = Math.max(level / 10, 1) * level * level / 2;
        if (level < 20) {
            per = Math.max(per / 10, 1);
        } else if (level < 30) {
            per = Math.max(per / 8, 1);
        } else if (level < 40) {
            per = Math.max(per / 6, 1);
        } else if (level < 50) {
            per = Math.max(per / 4, 1);
        }
        hp = maxHp = template.hp * per;
        damage = (long) template.damage * per;
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
        skills.add(SkillManager.getInstance().createSkill(SkillName.KHIEN_NANG_LUONG, 7));
    }

    @Override
    public List<ItemMap> throwItem(Entity killer) {
        List<ItemMap> itemMaps = new ArrayList<>();
        for (int i = 0; i < level / 6; i++) {
            itemMaps.add(ItemManager.getInstance().createItemMap(ItemName.XU, Utils.nextInt(20000 * level, 30000 * level), -1));
        }
        RandomCollection<Integer> r = new RandomCollection<>();
        r.add(1, ItemName.DA_12);
        r.add(3, ItemName.DA_11);
        r.add(10, ItemName.DA_10);
        r.add(1, ItemName.BUA_BAO_VE_CAP_3);
        r.add(3, ItemName.BUA_BAO_VE_CAP_2);
        r.add(6, ItemName.BUA_BAO_VE_CAP_1);
        for (int i = 0; i < level / 3; i++) {
            ItemMap itemMap = ItemManager.getInstance().createItemMap(r.next(), 1, -1);
            if (itemMap.template.id == ItemName.BUA_BAO_VE_CAP_3
                    || itemMap.template.id == ItemName.BUA_BAO_VE_CAP_2
                    || itemMap.template.id == ItemName.BUA_BAO_VE_CAP_1) {
                itemMap.setExpiry(7);
            }
            itemMaps.add(itemMap);
        }
        return itemMaps;
    }

    @Override
    public void sendNotificationWhenAppear(Zone zone) {

    }

    @Override
    public void sendNotificationWhenDead(String name) {

    }

    @Override
    public void updateEveryOneMinutes(long now) {
        super.updateEveryOneMinutes(now);
        List<Player> playerList = zone.getPlayers(Zone.TYPE_PLAYER, Zone.TYPE_DISCIPLE);
        for (Player player : playerList) {
            if (!player.isDead()) {
                player.injure(this, player.maxHp / 2, false, false);
                if (!player.isDead()) {
                    player.addEffect(new Effect(player, EffectName.DONG_BANG, 5000));
                }
            }
        }
    }
}
