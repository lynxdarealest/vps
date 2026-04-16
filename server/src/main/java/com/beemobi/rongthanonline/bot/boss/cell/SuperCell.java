package com.beemobi.rongthanonline.bot.boss.cell;

import com.beemobi.rongthanonline.bot.boss.Boss;
import com.beemobi.rongthanonline.bot.boss.BossName;
import com.beemobi.rongthanonline.entity.Entity;
import com.beemobi.rongthanonline.entity.player.Player;
import com.beemobi.rongthanonline.item.ItemManager;
import com.beemobi.rongthanonline.item.ItemMap;
import com.beemobi.rongthanonline.item.ItemName;
import com.beemobi.rongthanonline.item.ItemOption;
import com.beemobi.rongthanonline.map.Zone;
import com.beemobi.rongthanonline.skill.SkillManager;
import com.beemobi.rongthanonline.skill.SkillName;
import com.beemobi.rongthanonline.util.Utils;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class SuperCell extends Boss {
    private static final Logger logger = Logger.getLogger(SuperCell.class);

    public SuperCell(long maxHp, long damage) {
        super(BossName.SUPER_CELL, -1);
        saysBeforeAttack.add("Bất ngờ chưa?");
        saysBeforeAttack.add("Ta cũng bất ngờ như các ngươi thôi");
        saysBeforeAttack.add("Ta đã sống sót sau vụ nổ đó");
        saysBeforeAttack.add("Giờ thì kiểm tra sức mạnh hoàn hảo nào");
        sayTheLastWordBeforeDie = "Ta sẽ quay trở lại!";
        saysWhenAttack.add("Còn ai có thể cản được ta nữa?");
        saysWhenAttack.add("No one can stop me!");
        saysWhenAttack.add("Vẫn còn muốn thử sức mạnh tối thượng của ta hả?");
        hp = this.maxHp = maxHp * 2;
        this.damage = damage * 3 / 2;
        options[81] = 20;
        options[107] = 1;
        options[121] = 50;
    }

    @Override
    public void joinClient() {
        setLocation(115, -2);
    }

    @Override
    public void setSkill() {
        skills.add(SkillManager.getInstance().createSkill(SkillName.KARAK, 7, 7));
        skills.add(SkillManager.getInstance().createSkill(SkillName.KARAP, 7, 7));
        skills.add(SkillManager.getInstance().createSkill(SkillName.KARAV, 7, 7));
        skills.add(SkillManager.getInstance().createSkill(SkillName.KAME, 7, 7));
        skills.add(SkillManager.getInstance().createSkill(SkillName.MASENDAN, 7, 7));
        skills.add(SkillManager.getInstance().createSkill(SkillName.SOKIDAN, 7, 7));
        //skills.add(SkillManager.getInstance().createSkill(SkillName.TAI_TAO_NANG_LUONG, 7, 7));
        skills.add(SkillManager.getInstance().createSkill(SkillName.KHIEN_NANG_LUONG, 7));
        skills.add(SkillManager.getInstance().createSkill(SkillName.THAI_DUONG_HA_SAN, 7));
    }

    @Override
    public List<ItemMap> throwItem(Entity killer) {
        List<ItemMap> itemMaps = new ArrayList<>();
        if (isNotThrowItem()) {
            return itemMaps;
        }
        if (Utils.isPercent(70)) {
            itemMaps.add(ItemManager.getInstance().createItemMap(ItemName.XU, Utils.nextInt(10000000, 15000000), killer.id));
        } else {
            itemMaps.add(ItemManager.getInstance().createItemMap(ItemName.XU, Utils.nextInt(2000000, 3000000), killer.id));
        }
        if (Utils.isPercent(40)) {
            itemMaps.add(ItemManager.getInstance().createItemMap(ItemName.DA_10, 1, killer.id));
        }
        int per = Utils.nextInt(100);
        if (per < 10) {
            itemMaps.add(ItemManager.getInstance().createItemMap(ItemName.NGOC_RONG_2_SAO, 1, killer.id));
        } else if (per < 60) {
            itemMaps.add(ItemManager.getInstance().createItemMap(ItemName.NGOC_RONG_3_SAO, 1, killer.id));
        } else {
            itemMaps.add(ItemManager.getInstance().createItemMap(ItemName.NGOC_RONG_4_SAO, 1, killer.id));
        }
        if (Utils.isPercent(20)) {
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
    public void updateEveryOneMinutes(long now) {
        super.updateEveryOneMinutes(now);
        if (typePk == 3) {
            Player player = Utils.nextArray(zone.getPlayers(Zone.TYPE_PLAYER, Zone.TYPE_DISCIPLE).stream().filter(p -> !p.isDead()).collect(Collectors.toList()));
            if (player != null) {
                long hp = player.hp;
                maxHp += Math.max(player.maxHp / 20, 100000000L);
                recovery(Player.RECOVERY_ALL, hp);
                player.injure(this, hp, false, false);
                chat(String.format("Sao ngươi bất cẩn thế %s?", player.name));
            }
        }
    }

    @Override
    public void startDie(Entity killer) {
        super.startDie(killer);
        Utils.setTimeout(() -> {
            Boss boss = new Cell(0, -1);
            boss.joinClient();
        }, delayRespawn);
    }
}