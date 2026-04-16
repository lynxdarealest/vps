package com.beemobi.rongthanonline.bot.boss.other;

import com.beemobi.rongthanonline.bot.boss.Boss;
import com.beemobi.rongthanonline.bot.boss.BossName;
import com.beemobi.rongthanonline.entity.Entity;
import com.beemobi.rongthanonline.entity.player.Player;
import com.beemobi.rongthanonline.item.ItemManager;
import com.beemobi.rongthanonline.item.ItemMap;
import com.beemobi.rongthanonline.item.ItemName;
import com.beemobi.rongthanonline.item.OptionName;
import com.beemobi.rongthanonline.map.MapName;
import com.beemobi.rongthanonline.map.Zone;
import com.beemobi.rongthanonline.skill.SkillManager;
import com.beemobi.rongthanonline.skill.SkillName;
import com.beemobi.rongthanonline.util.Utils;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Beerus extends Boss {
    private static final Logger logger = Logger.getLogger(Beerus.class);

    private static final int[] MAPS = {MapName.PHIA_BAC_HANH_TINH_BILL, MapName.PHIA_DONG_HANH_TINH_BILL,
            MapName.PHIA_NAM_HANH_TINH_BILL, MapName.PHIA_TAY_HANH_TINH_BILL};

    public Beerus() {
        super(BossName.THAN_HUY_DIET, -1);
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
        setLocation(MAPS[Utils.nextInt(MAPS.length)], -2);
    }

    @Override
    public List<ItemMap> throwItem(Entity killer) {
        List<ItemMap> itemMaps = new ArrayList<>();
        int size = Utils.nextInt(1, 3);
        for (int i = 0; i < size; i++) {
            itemMaps.add(ItemManager.getInstance().createItemMap(ItemName.RUBY, 1, -1));
        }
        size = Utils.nextInt(3, 5);
        for (int i = 0; i < size; i++) {
            itemMaps.add(ItemManager.getInstance().createItemMap(ItemName.XU, Utils.nextInt(10000000, 20000000), -1));
        }
        size = Utils.nextInt(5, 10);
        for (int i = 0; i < size; i++) {
            itemMaps.add(ItemManager.getInstance().createItemMap(ItemName.TAM_LINH_THACH, Utils.nextInt(5, 7), -1));
        }
        if (Utils.isPercent(60)) {
            itemMaps.add(ItemManager.getInstance().createItemMap(ItemName.NGOC_RONG_2_SAO, 1, killer.id));
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
    public void updateEveryThirtySeconds(long now) {
        super.updateEveryThirtySeconds(now);

    }

    @Override
    public void updateEveryOneMinutes(long now) {
        super.updateEveryOneMinutes(now);
        if (typePk == 3) {
            Player player = Utils.nextArray(zone.getPlayers(Zone.TYPE_PLAYER, Zone.TYPE_DISCIPLE).stream().filter(p -> !p.isDead()).collect(Collectors.toList()));
            if (player != null) {
                player.injure(this, player.maxHp, false, false);
                chat(String.format("Hakai %s", player.name));
            }
        }
    }
}