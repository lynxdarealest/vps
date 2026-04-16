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
import com.beemobi.rongthanonline.map.Map;
import com.beemobi.rongthanonline.map.MapManager;
import com.beemobi.rongthanonline.map.MapName;
import com.beemobi.rongthanonline.skill.SkillManager;
import com.beemobi.rongthanonline.skill.SkillName;
import com.beemobi.rongthanonline.util.Utils;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Cell extends Boss {
    public static final int[] MAPS = {MapName.RUNG_LILY, MapName.RUNG_CHET_6, MapName.RUNG_CHET_7, MapName.RUNG_CHET_8};
    private static final Logger logger = Logger.getLogger(Cell.class);
    private final int type;

    public Cell(int type, long maxHp) {
        super(BossName.CELL, -1);
        typePk = 0;
        this.type = type;
        if (type == 0) {
            saysBeforeAttack.add("Hehehe...");
            saysBeforeAttack.add("Ta sẽ hấp thụ tất cả các ngươi!");
            saysBeforeAttack.add("Mau giao nộp bọn Android ra đây!");
            saysBeforeAttack.add("Ta sẽ tha mạng các bọn mi!");
            saysBeforeAttack.add("Mọi dữ liệu chiến đấu của bọn mi ta đã nắm bắt được, các ngươi không thể thắng ta đâu");
            saysBeforeAttack.add("Ngoan cố quá nhỉ, vậy thì ta sẽ xử các ngươi trước khi tìm bọn Android");
            sayTheLastWordBeforeDie = "Lợi hại đó";
            saysWhenAttack.add("Mau giao nộp lũ Android ra đây");
            saysWhenAttack.add("Bọn Android chỉ là 1 phần của ta, các ngươi cố gắng để làm gì?");
            saysWhenAttack.add("Mọi sự kháng cự chỉ là vô dụng!");
        } else if (type == 1) {
            name = "Cell bán hoàn thiện";
            saysBeforeAttack.add("Hơ hơ...");
            saysBeforeAttack.add("Ta đã hấp thụ được Pic");
            saysBeforeAttack.add("Giờ là lúc các ngươi ngập hành");
            sayTheLastWordBeforeDie = "Chỉ là ta chưa hoàn thiện thôi";
            saysWhenAttack.add("Giao nộp Poc ra đây, ta sẽ tha mạng cho các ngươi");
            saysWhenAttack.add("Không ai có thể cản được ta");
            saysWhenAttack.add("Mọi sự kháng cự chỉ là vô dụng!");
            hp = this.maxHp = maxHp * 3 / 2;
            damage = template.damage * 3 / 2;
            head = 281;
            body = 282;
        } else if (type == 2) {
            name = "Cell hoàn thiện";
            saysBeforeAttack.add("Hahaha");
            saysBeforeAttack.add("Cám ơn... Ta đã đạt được trạng thái hoàn thiện!");
            saysBeforeAttack.add("Giờ ta là bất bại!");
            sayTheLastWordBeforeDie = "Ta sẽ quay trở lại! Hẹn các ngươi tại võ đài.";
            saysWhenAttack.add("Còn ai có thể cản được ta nữa?");
            saysWhenAttack.add("No one can stop me!");
            saysWhenAttack.add("Vẫn còn muốn thử sức mạnh tối thượng của ta hả?");
            hp = this.maxHp = maxHp * 2;
            damage = template.damage * 2;
            head = 285;
            body = 286;
        }
        options[107] = 1;
        options[121] = 50;
    }

    @Override
    public void joinClient() {
        setLocation(MAPS[Utils.nextInt(MAPS.length)], -2);
    }

    @Override
    public void setSkill() {
        skills.add(SkillManager.getInstance().createSkill(SkillName.KARAK, 7, 5));
        skills.add(SkillManager.getInstance().createSkill(SkillName.KARAP, 7, 5));
        skills.add(SkillManager.getInstance().createSkill(SkillName.KARAV, 7, 5));
        skills.add(SkillManager.getInstance().createSkill(SkillName.KAME, 7, 5));
        skills.add(SkillManager.getInstance().createSkill(SkillName.MASENDAN, 7, 5));
        skills.add(SkillManager.getInstance().createSkill(SkillName.SOKIDAN, 7, 5));
        //skills.add(SkillManager.getInstance().createSkill(SkillName.TAI_TAO_NANG_LUONG, 7, 5));
        skills.add(SkillManager.getInstance().createSkill(SkillName.KHIEN_NANG_LUONG, 7));
        skills.add(SkillManager.getInstance().createSkill(SkillName.THAI_DUONG_HA_SAN, 7));
    }

    @Override
    public List<ItemMap> throwItem(Entity killer) {
        List<ItemMap> itemMaps = new ArrayList<>();
        if (isNotThrowItem()) {
            return itemMaps;
        }
        if (Utils.isPercent(40)) {
            itemMaps.add(ItemManager.getInstance().createItemMap(ItemName.XU, Utils.nextInt(10000000, 15000000), killer.id));
        } else {
            itemMaps.add(ItemManager.getInstance().createItemMap(ItemName.XU, Utils.nextInt(2000000, 3000000), killer.id));
        }
        if (Utils.isPercent(40)) {
            itemMaps.add(ItemManager.getInstance().createItemMap(ItemName.DA_10, 1, killer.id));
        }
        if (Utils.isPercent(3 + type)) {
            itemMaps.add(ItemManager.getInstance().createItemMap(ItemName.NGOC_RONG_2_SAO, 1, killer.id));
        } else if (Utils.isPercent(40)) {
            itemMaps.add(ItemManager.getInstance().createItemMap(ItemName.NGOC_RONG_3_SAO, 1, killer.id));
        }
        if (Utils.isPercent(30)) {
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
    public void updateEveryOneSeconds(long now) {
        super.updateEveryOneSeconds(now);
        if (type == 2 && !isDead() && typePk == 3 && this.hp < this.maxHp / 3 && zone != null && zone.map.template.id != MapName.VO_DAI_CELL) {
            Map map = MapManager.getInstance().maps.get(MapName.VO_DAI_CELL);
            teleport(map, -2);
        }
    }

    @Override
    public void startDie(Entity killer) {
        Zone zone = this.zone;
        super.startDie(killer);
        if (type < 2) {
            Utils.setTimeout(() -> {
                Boss boss = new Cell(type + 1, this.maxHp);
                boss.setLocation(zone);
            }, waitingTimeToLeave + 1000);
        } else {
            long maxHp = this.maxHp;
            long damage = this.damage;
            Utils.setTimeout(() -> {
                Boss boss = new SuperCell(maxHp, damage);
                boss.joinClient();
            }, waitingTimeToLeave + 1000);
        }
    }
}
