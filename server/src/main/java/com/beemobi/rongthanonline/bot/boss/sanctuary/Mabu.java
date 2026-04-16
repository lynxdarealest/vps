package com.beemobi.rongthanonline.bot.boss.sanctuary;

import com.beemobi.rongthanonline.bot.boss.Boss;
import com.beemobi.rongthanonline.bot.boss.BossName;
import com.beemobi.rongthanonline.entity.Entity;
import com.beemobi.rongthanonline.item.ItemManager;
import com.beemobi.rongthanonline.item.ItemMap;
import com.beemobi.rongthanonline.item.ItemName;
import com.beemobi.rongthanonline.map.MapName;
import com.beemobi.rongthanonline.map.Zone;
import com.beemobi.rongthanonline.map.expansion.sanctuary.ZoneSanctuary;
import com.beemobi.rongthanonline.skill.SkillManager;
import com.beemobi.rongthanonline.skill.SkillName;
import com.beemobi.rongthanonline.util.Utils;

import java.util.ArrayList;
import java.util.List;

public class Mabu extends Boss {
    public static int[] BU = {BossName.BU_MAP, BossName.BU_OM, BossName.SUPER_BU, BossName.BU_TENK, BossName.BU_POCO, BossName.BU_HAN, BossName.KID_BU};

    public final int type;

    public Mabu(int type) {
        super(BU[type], -1);
        this.type = type;
        waitingTimeToLeave = 0;
    }

    @Override
    public void setSkill() {
        skills.add(SkillManager.getInstance().createSkill(SkillName.KARAK, 7, 7));
        skills.add(SkillManager.getInstance().createSkill(SkillName.KARAP, 7, 7));
        skills.add(SkillManager.getInstance().createSkill(SkillName.KARAV, 7, 7));
        skills.add(SkillManager.getInstance().createSkill(SkillName.KAME, 7, 7));
        skills.add(SkillManager.getInstance().createSkill(SkillName.MASENDAN, 7, 7));
        skills.add(SkillManager.getInstance().createSkill(SkillName.SOKIDAN, 7, 7));
        //skills.add(SkillManager.getInstance().createSkill(SkillName.TAI_TAO_NANG_LUONG, 3));
    }

    @Override
    public void startDie(Entity killer) {
        Zone zone = this.zone;
        super.startDie(killer);
        if (zone.map.template.id == MapName.BUNG_BU) {
            //Utils.setTimeout(zone::close, waitingTimeToLeave + 1000);
        } else if (type < BU.length - 1) {
            int type = this.type + 1;
            Utils.setTimeout(() -> {
                if (type == BU.length - 1) {
                    new KidBu().setLocation(zone);
                } else if (type == BU.length - 2) {
                    new BuHan().setLocation(zone);
                } else {
                    new Mabu(type).setLocation(zone);
                }
            }, waitingTimeToLeave + 1000);
        }
    }

    @Override
    public List<ItemMap> throwItem(Entity killer) {
        List<ItemMap> itemMaps = new ArrayList<>();
        int size = Utils.nextInt(5, 10);
        for (int i = 0; i < size; i++) {
            itemMaps.add(ItemManager.getInstance().createItemMap(ItemName.XU, Utils.nextInt(500000, 1000000), -1));
        }
        if (Utils.isPercent(10 * type)) {
            itemMaps.add(ItemManager.getInstance().createItemMap(Utils.nextInt(522, 545), 1, killer.id));
        }
        if (Utils.isPercent(5 * type)) {
            itemMaps.add(ItemManager.getInstance().createItemMap(ItemName.TRUNG_MABU, 1, killer.id));
        }
        return itemMaps;
    }

    @Override
    public void sendNotificationWhenDead(String name) {

    }

    @Override
    public void sendNotificationWhenAppear(Zone zone) {

    }
}
