package com.beemobi.rongthanonline.bot.boss.cumber;

import com.beemobi.rongthanonline.bot.boss.Boss;
import com.beemobi.rongthanonline.bot.boss.BossName;
import com.beemobi.rongthanonline.bot.boss.cell.SuperCell;
import com.beemobi.rongthanonline.entity.Entity;
import com.beemobi.rongthanonline.item.*;
import com.beemobi.rongthanonline.map.expansion.island.Island;
import com.beemobi.rongthanonline.skill.SkillManager;
import com.beemobi.rongthanonline.skill.SkillName;
import com.beemobi.rongthanonline.util.Utils;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class SuperCumber extends Boss {
    private static final Logger logger = Logger.getLogger(SuperCumber.class);

    public Island island;

    public SuperCumber(Island island) {
        super(BossName.SUPER_CUMBER, -1);
        this.island = island;
        typePk = 0;
        options[OptionName.NE_DON] = 5000;
        options[OptionName.GIAM_SAT_THUONG_CHI_MANG] = 50 * this.level;
    }

    @Override
    public void setSkill() {
        skills.add(SkillManager.getInstance().createSkill(SkillName.KARAK, 7, 7));
        skills.add(SkillManager.getInstance().createSkill(SkillName.KARAP, 7, 7));
        skills.add(SkillManager.getInstance().createSkill(SkillName.KARAV, 7, 7));
        skills.add(SkillManager.getInstance().createSkill(SkillName.KAME, 7));
        skills.add(SkillManager.getInstance().createSkill(SkillName.MASENDAN, 7));
        skills.add(SkillManager.getInstance().createSkill(SkillName.SOKIDAN, 7));
        skills.add(SkillManager.getInstance().createSkill(SkillName.KHIEN_NANG_LUONG, 7));
        //skills.add(SkillManager.getInstance().createSkill(SkillName.TAI_TAO_NANG_LUONG, 3));
    }

    @Override
    public void joinClient() {
        setLocation(island.maps.get(Utils.nextInt(island.maps.size())), -2);
    }

    @Override
    public List<ItemMap> throwItem(Entity killer) {
        List<ItemMap> itemMaps = new ArrayList<>();
        int size = Utils.nextInt(1, 3);
        for (int i = 0; i < size; i++) {
            itemMaps.add(ItemManager.getInstance().createItemMap(ItemName.RUBY, 1, -1));
        }
        size = Utils.nextInt(5, 7);
        for (int i = 0; i < size; i++) {
            itemMaps.add(ItemManager.getInstance().createItemMap(ItemName.XU, Utils.nextInt(10000000, 20000000), -1));
        }
        size = Utils.nextInt(5, 10);
        for (int i = 0; i < size; i++) {
            itemMaps.add(ItemManager.getInstance().createItemMap(ItemName.TAM_LINH_THACH, Utils.nextInt(5, 7), -1));
        }
        size = Utils.nextInt(2, 4);
        for (int i = 0; i < size; i++) {
            itemMaps.add(ItemManager.getInstance().createItemMap(ItemName.NGOC_RONG_3_SAO, 1, -1));
        }
        itemMaps.add(ItemManager.getInstance().createItemMap(ItemName.NGOC_RONG_3_SAO, 1, killer.id));
        if (Utils.isPercent(60)) {
            itemMaps.add(ItemManager.getInstance().createItemMap(ItemName.MANH_GIAY_CHIEU_HUT_DAY, 1, killer.id));
        }
        if (Utils.isPercent(5)) {
            ItemMap itemMap = ItemManager.getInstance().createItemMap(ItemName.CAI_TRANG_SUPER_CUMBER, 1, killer.id);
            itemMap.options.add(new ItemOption(25, Utils.nextInt(15, 20)));
            itemMap.options.add(new ItemOption(31, Utils.nextInt(15, 20)));
            itemMap.options.add(new ItemOption(32, Utils.nextInt(15, 20)));
            itemMaps.add(itemMap);
        }
        long min = maxHp / 10;
        ArrayList<Integer> players = enemies.entrySet().stream()
                .filter(entry -> entry.getValue() >= min)
                .map(Map.Entry::getKey)
                .collect(Collectors.toCollection(ArrayList::new));
        for (ItemMap itemMap : itemMaps) {
            itemMap.players = players;
        }
        return itemMaps;
    }

    @Override
    public void startDie(Entity killer) {
        super.startDie(killer);
        Utils.setTimeout(() -> {
            Boss boss = new Cumber(island);
            boss.joinClient();
        }, 900000);
    }

}
