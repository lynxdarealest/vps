package com.beemobi.rongthanonline.bot.boss.other;

import com.beemobi.rongthanonline.bot.boss.Boss;
import com.beemobi.rongthanonline.bot.boss.BossName;
import com.beemobi.rongthanonline.entity.Entity;
import com.beemobi.rongthanonline.entity.player.Player;
import com.beemobi.rongthanonline.item.ItemManager;
import com.beemobi.rongthanonline.item.ItemMap;
import com.beemobi.rongthanonline.item.ItemName;
import com.beemobi.rongthanonline.map.MapName;
import com.beemobi.rongthanonline.skill.SkillManager;
import com.beemobi.rongthanonline.skill.SkillName;
import com.beemobi.rongthanonline.util.Utils;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

public class Gorillas extends Boss {
    private static final Logger logger = Logger.getLogger(Gorillas.class);

    private static final int[] MAPS = {MapName.BO_SONG_PU, MapName.THAC_NUOC_KEISE,
            MapName.RUNG_NAM_FUKA, MapName.DOI_HOANG_AKA, MapName.THUNG_LUNG_MARIA,
            MapName.DONG_BANG_MIKA, MapName.CAO_NGUYEN_TAKA, MapName.RUNG_GOZA, MapName.LANG_ARU};

    private final boolean isSuper;
    private final int areaId;

    public Gorillas(boolean isSuper, int areaId) {
        super(BossName.KHI_DOT, -1);
        typePk = 3;
        this.isSuper = isSuper;
        this.areaId = areaId;
        if (isSuper) {
            name = template.name + " 2";
            hp = maxHp = 1000;
            head = 237;
            body = 238;
        }
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
        skills.add(SkillManager.getInstance().createSkill(SkillName.TAI_TAO_NANG_LUONG, 3));
    }

    @Override
    public void joinClient() {
        setLocation(MapName.VACH_NUI_ARU, areaId);
    }

    @Override
    public long formatDamageInjure(Entity attacker, long damage, boolean isCritical) {
        return 1;
    }

    @Override
    public void startDie(Entity killer) {
        super.startDie(killer);
        if (!isSuper) {
            Utils.setTimeout(() -> {
                Boss boss = new Gorillas(true, areaId);
                boss.joinClient();
            }, waitingTimeToLeave + 1000);
        }
        if (killer != null && killer.isPlayer()) {
            ((Player) killer).pointEvent += isSuper ? 20 : 50;
        }
    }

    @Override
    public List<ItemMap> throwItem(Entity killer) {
        List<ItemMap> itemMaps = new ArrayList<>();
        int size = Utils.nextInt(5, 10);
        for (int i = 0; i < size; i++) {
            itemMaps.add(ItemManager.getInstance().createItemMap(ItemName.RUBY, 1, -1));
        }
        size = Utils.nextInt(5, 10);
        for (int i = 0; i < size; i++) {
            itemMaps.add(ItemManager.getInstance().createItemMap(ItemName.XU, Utils.nextInt(500000, 1000000), -1));
        }
        size = Utils.nextInt(5, 10);
        for (int i = 0; i < size; i++) {
            itemMaps.add(ItemManager.getInstance().createItemMap(ItemName.XU_KHOA, Utils.nextInt(500000, 1000000), -1));
        }
        return itemMaps;
    }
}
