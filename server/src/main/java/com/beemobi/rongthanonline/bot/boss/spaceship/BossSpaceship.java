package com.beemobi.rongthanonline.bot.boss.spaceship;

import com.beemobi.rongthanonline.bot.boss.Boss;
import com.beemobi.rongthanonline.bot.boss.BossName;
import com.beemobi.rongthanonline.entity.Entity;
import com.beemobi.rongthanonline.entity.player.Player;
import com.beemobi.rongthanonline.item.*;
import com.beemobi.rongthanonline.map.Zone;
import com.beemobi.rongthanonline.map.expansion.spaceship.Spaceship;
import com.beemobi.rongthanonline.map.expansion.spaceship.ZoneSpaceship;
import com.beemobi.rongthanonline.skill.SkillManager;
import com.beemobi.rongthanonline.skill.SkillName;
import com.beemobi.rongthanonline.util.Utils;

import java.util.ArrayList;
import java.util.List;

public class BossSpaceship extends Boss {
    public static int[] BU = {BossName.DRABURA, BossName.PUI_PUI, BossName.YACON, BossName.MABU};

    public final int type;
    public Spaceship spaceship;

    public BossSpaceship(Spaceship spaceship, int type) {
        super(BU[type], -1);
        this.type = type;
        waitingTimeToLeave = -1;
        delayRespawn = 60000L;
        this.spaceship = spaceship;
        switch (type) {
            case 0:
                options[136] = 5;
                break;
            case 1:
                options[170] = 80;
                break;
            case 2:
                options[171] = 1;
                break;
            case 3:
                options[137] = 5;
                break;
        }
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
        if (killer instanceof Player) {
            Player player = (Player) killer;
            if (player.zone instanceof ZoneSpaceship) {
                player.pointCurrSpaceship++;
                player.pointSpaceship += 10;
                ((ZoneSpaceship) player.zone).setEnergy(player);
            }
        }
        if (type == 3) {
            List<Player> playerList = zone.getPlayers(Zone.TYPE_PLAYER);
            ZoneSpaceship zoneSpaceship = (ZoneSpaceship) zone;
            for (Player player : playerList) {
                try {
                    player.pointCurrSpaceship = 0;
                    zoneSpaceship.setEnergy(player);
                } catch (Exception ignored) {
                }
            }
        }
    }

    @Override
    public List<ItemMap> throwItem(Entity killer) {
        List<ItemMap> itemMaps = new ArrayList<>();
        int size = Utils.nextInt(5, 10);
        for (int i = 0; i < size; i++) {
            itemMaps.add(ItemManager.getInstance().createItemMap(ItemName.XU, Utils.nextInt(100000 * (type + 1), 200000 * (type + 1)), -1));
        }
        if ((type == 3 && Utils.isPercent(10)) || (type != 3 && Utils.isPercent(5))) {
            itemMaps.add(ItemManager.getInstance().createItemMap(Utils.nextInt(522, 545), 1, killer.id));
        }
        if ((type == 3 && Utils.isPercent(10)) || (type != 3 && Utils.isPercent(1))) {
            ItemMap itemMap = ItemManager.getInstance().createItemMap(Utils.nextInt(337, 352), 1, killer.id);
            itemMap.randomParam(-15, 15);
            int per = Utils.nextInt(100);
            if (per == 0) {
                itemMap.options.add(new ItemOption(67, Utils.nextInt(4, 5)));
            } else if (per < 10) {
                itemMap.options.add(new ItemOption(67, Utils.nextInt(1, 3)));
            }
            itemMaps.add(itemMap);
        }
        if (type == 3 && Utils.isPercent(20)) {
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

    @Override
    public long formatDamageInjure(Entity attacker, long damage, boolean isCritical) {
        return Math.min(damage, maxHp / 20);
    }
}