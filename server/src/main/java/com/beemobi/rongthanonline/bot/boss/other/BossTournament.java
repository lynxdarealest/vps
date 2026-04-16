package com.beemobi.rongthanonline.bot.boss.other;

import com.beemobi.rongthanonline.bot.boss.Boss;
import com.beemobi.rongthanonline.bot.boss.BossName;
import com.beemobi.rongthanonline.bot.boss.congress.CellCongress;
import com.beemobi.rongthanonline.entity.Entity;
import com.beemobi.rongthanonline.item.ItemMap;
import com.beemobi.rongthanonline.item.OptionName;
import com.beemobi.rongthanonline.map.Zone;
import com.beemobi.rongthanonline.skill.SkillManager;
import com.beemobi.rongthanonline.skill.SkillName;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

public class BossTournament  extends Boss {
    private static final Logger logger = Logger.getLogger(BossTournament.class);
    public static final long[] HP = {
            1_000_000L, //1
            5_000_000L, //2
            10_000_000L, //3
            50_000_000L, //4
            100_000_000L, //5
            500_000_000L, //6
            1_000_000_000L, //7
            2_000_000_000L, //8
            5_000_000_000L, //9
            10_000_000_000L, //10
            25_000_000_000L, //11
            50_000_000_000L, //12
            100_000_000_000L, //13
            250_000_000_000L, //14
            500_000_000_000L, //15
            1_000_000_000_000L, //16
    };

    public static final long[] DAMAGE = new long[]{
            1_000L, //1
            5_000L, //2
            25_000L, //3
            50_000L, //4
            100_000L, //5
            250_000L, //6
            500_000L, //7
            1_000_000L, //8
            2_500_000L, //9
            5_000_000L, //10
            10_000_000L, //11
            25_000_000L, //12
            50_000_000L, //13
            150_000_000L, //14
            250_000_000L, //15
            300_000_000L, //16
    };

    public BossTournament(int level) {
        super(BossName.SUPER_CELL, level + 1);
        this.name = String.format("Cell [%d]", level + 1);
        options[OptionName.GIAM_SAT_THUONG] = 500 * level;
        hp = maxHp = HP[level];
        damage = DAMAGE[level];
        options[OptionName.NE_DON] = 500 * level;
        options[121] = 100;
        isTask = false;
        isHasPoint = false;
    }

    @Override
    public void sendNotificationWhenAppear(Zone zone) {

    }

    @Override
    public void sendNotificationWhenDead(String name) {

    }

    @Override
    public void setSkill() {
        skills.add(SkillManager.getInstance().createSkill(SkillName.KARAK, 7, 7));
        skills.add(SkillManager.getInstance().createSkill(SkillName.KARAP, 7, 7));
        skills.add(SkillManager.getInstance().createSkill(SkillName.KARAV, 7, 7));
        skills.add(SkillManager.getInstance().createSkill(SkillName.KAME, 7, 7));
        skills.add(SkillManager.getInstance().createSkill(SkillName.MASENDAN, 7, 7));
        skills.add(SkillManager.getInstance().createSkill(SkillName.SOKIDAN, 7, 7));
        skills.add(SkillManager.getInstance().createSkill(SkillName.TAI_TAO_NANG_LUONG, 7, 1));
        skills.add(SkillManager.getInstance().createSkill(SkillName.THAI_DUONG_HA_SAN, 1));
    }

    @Override
    public List<ItemMap> throwItem(Entity killer) {
        return new ArrayList<>();
    }

    @Override
    public boolean isAttack() {
        return super.isAttack() && typePk == 1 && testPlayerId != -1;
    }
}
