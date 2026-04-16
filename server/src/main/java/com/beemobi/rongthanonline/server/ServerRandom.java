package com.beemobi.rongthanonline.server;

import com.beemobi.rongthanonline.common.RandomCollection;
import com.beemobi.rongthanonline.item.ItemName;
import com.beemobi.rongthanonline.item.ItemRandom;
import com.beemobi.rongthanonline.skill.SkillName;

public class ServerRandom {
    public static final RandomCollection<ItemRandom> CAPSULE_DONG = new RandomCollection<>();
    public static final RandomCollection<ItemRandom> CAPSULE_BAC = new RandomCollection<>();
    public static final RandomCollection<ItemRandom> CAPSULE_VANG = new RandomCollection<>();
    public static final RandomCollection<ItemRandom> CAPSULE_BACH_KIM = new RandomCollection<>();
    public static final RandomCollection<ItemRandom> RUONG_KHO_BAU = new RandomCollection<>();
    public static final RandomCollection<Integer> SKILL_DISCIPLE_1 = new RandomCollection<>();
    public static final RandomCollection<Integer> SKILL_DISCIPLE_2 = new RandomCollection<>();
    public static final RandomCollection<Integer> SKILL_DISCIPLE_3 = new RandomCollection<>();
    public static final RandomCollection<Integer> SKILL_DISCIPLE_4 = new RandomCollection<>();
    public static final RandomCollection<Integer> SKILL_DISCIPLE_1_EVENT = new RandomCollection<>();
    public static final RandomCollection<Integer> SKILL_DISCIPLE_2_EVENT = new RandomCollection<>();
    public static final RandomCollection<Integer> SKILL_DISCIPLE_3_EVENT = new RandomCollection<>();
    public static final RandomCollection<Integer> SKILL_DISCIPLE_4_EVENT = new RandomCollection<>();
    public static final RandomCollection<Integer> DRAGON_BALL_BARRACK = new RandomCollection<>();

    public static void init() {
        CAPSULE_DONG.add(3, new ItemRandom(ItemName.XU, 7000000, 10000000));
        CAPSULE_DONG.add(7, new ItemRandom(ItemName.XU, 1000000, 7000000));
        CAPSULE_DONG.add(10, new ItemRandom(ItemName.XU, 500000, 1000000));
        CAPSULE_DONG.add(30, new ItemRandom(ItemName.XU, 50000, 100000));
        CAPSULE_DONG.add(15, new ItemRandom(ItemName.KEO_DO));
        CAPSULE_DONG.add(15, new ItemRandom(ItemName.KEO_VANG));
        CAPSULE_DONG.add(15, new ItemRandom(ItemName.KEO_XANH));
        CAPSULE_DONG.add(15, new ItemRandom(ItemName.KEO_LUC));

        CAPSULE_BAC.add(0.1, new ItemRandom(ItemName.CAI_TRANG_YARBON));
        CAPSULE_BAC.add(0.1, new ItemRandom(ItemName.CAI_TRANG_KUI));
        CAPSULE_BAC.add(0.1, new ItemRandom(ItemName.CAI_TRANG_DODO));
        CAPSULE_BAC.add(0.1, new ItemRandom(new int[]{ItemName.AVATAR_SUPER_GOHAN, ItemName.AVATAR_ASSASSIN_NAMEK, ItemName.AVATAR_SUPER_BROLY}));
        CAPSULE_BAC.add(0.1, new ItemRandom(new int[]{ItemName.AVATAR_TRUNKS, ItemName.AVATAR_DEVIL_NAMEK, ItemName.AVATAR_SUPER_VEGETA}));
        CAPSULE_BAC.add(0.1, new ItemRandom(new int[]{ItemName.AVATAR_TENSHINHAN, ItemName.AVATAR_WARRIOR_NAMEK, ItemName.AVATAR_BROLY}));
        CAPSULE_BAC.add(15, new ItemRandom(ItemName.BUA_TAY_TIEM_NANG));
        CAPSULE_BAC.add(15, new ItemRandom(ItemName.BUA_BAO_VE_CAP_1));
        CAPSULE_BAC.add(15, new ItemRandom(ItemName.DAU_THAN_CAP_4, 30));
        CAPSULE_BAC.add(15, new ItemRandom(ItemName.BUA_HOAN_LUONG));
        CAPSULE_BAC.add(15, new ItemRandom(ItemName.DA_4, 10));
        CAPSULE_BAC.add(15, new ItemRandom(ItemName.DA_5, 3));
        CAPSULE_BAC.add(15, new ItemRandom(ItemName.DA_6, 1));

        CAPSULE_VANG.add(0.1, new ItemRandom(ItemName.CAI_TRANG_YARBON));
        CAPSULE_VANG.add(0.1, new ItemRandom(ItemName.CAI_TRANG_KUI));
        CAPSULE_VANG.add(0.1, new ItemRandom(ItemName.CAI_TRANG_DODO));
        CAPSULE_VANG.add(0.1, new ItemRandom(new int[]{ItemName.AVATAR_SUPER_GOHAN, ItemName.AVATAR_ASSASSIN_NAMEK, ItemName.AVATAR_SUPER_BROLY}));
        CAPSULE_VANG.add(0.1, new ItemRandom(new int[]{ItemName.AVATAR_TRUNKS, ItemName.AVATAR_DEVIL_NAMEK, ItemName.AVATAR_SUPER_VEGETA}));
        CAPSULE_VANG.add(0.1, new ItemRandom(new int[]{ItemName.AVATAR_TENSHINHAN, ItemName.AVATAR_WARRIOR_NAMEK, ItemName.AVATAR_BROLY}));
        CAPSULE_VANG.add(5, new ItemRandom(ItemName.BUA_TAY_TIEM_NANG));
        CAPSULE_VANG.add(5, new ItemRandom(ItemName.BUA_BAO_VE_CAP_1));
        CAPSULE_VANG.add(25, new ItemRandom(ItemName.DAU_THAN_CAP_5, 30));
        CAPSULE_VANG.add(5, new ItemRandom(ItemName.BUA_HOAN_LUONG));
        CAPSULE_VANG.add(20, new ItemRandom(ItemName.DA_5, 20));
        CAPSULE_VANG.add(20, new ItemRandom(ItemName.DA_6, 10));
        CAPSULE_VANG.add(20, new ItemRandom(ItemName.DA_7, 5));

        CAPSULE_BACH_KIM.add(0.5, new ItemRandom(ItemName.GOKU_HIEP_SI_RONG));
        CAPSULE_BACH_KIM.add(0.1, new ItemRandom(ItemName.CAI_TRANG_PUI_PUI));
        CAPSULE_BACH_KIM.add(0.1, new ItemRandom(ItemName.CAI_TRANG_YACON));
        CAPSULE_BACH_KIM.add(1, new ItemRandom(ItemName.CAI_TRANG_SO_4));
        CAPSULE_BACH_KIM.add(1, new ItemRandom(ItemName.CAI_TRANG_SO_3));
        CAPSULE_BACH_KIM.add(1, new ItemRandom(ItemName.CAI_TRANG_SO_2));
        CAPSULE_BACH_KIM.add(1, new ItemRandom(ItemName.CAI_TRANG_SO_1));
        CAPSULE_BACH_KIM.add(2, new ItemRandom(ItemName.CAI_TRANG_YARBON));
        CAPSULE_BACH_KIM.add(2, new ItemRandom(ItemName.CAI_TRANG_KUI));
        CAPSULE_BACH_KIM.add(2, new ItemRandom(ItemName.CAI_TRANG_DODO));
        CAPSULE_BACH_KIM.add(5, new ItemRandom(new int[]{ItemName.AVATAR_SUPER_GOHAN, ItemName.AVATAR_ASSASSIN_NAMEK, ItemName.AVATAR_SUPER_BROLY}));
        CAPSULE_BACH_KIM.add(5, new ItemRandom(new int[]{ItemName.AVATAR_TRUNKS, ItemName.AVATAR_DEVIL_NAMEK, ItemName.AVATAR_SUPER_VEGETA}));
        CAPSULE_BACH_KIM.add(10, new ItemRandom(new int[]{ItemName.AVATAR_TENSHINHAN, ItemName.AVATAR_WARRIOR_NAMEK, ItemName.AVATAR_BROLY}));
        CAPSULE_BACH_KIM.add(5, new ItemRandom(ItemName.HOA_VAN));
        CAPSULE_BACH_KIM.add(10, new ItemRandom(ItemName.BUA_TAY_TIEM_NANG));
        CAPSULE_BACH_KIM.add(10, new ItemRandom(ItemName.BUA_BAO_VE_CAP_1));
        CAPSULE_BACH_KIM.add(10, new ItemRandom(ItemName.BUA_BAO_VE_CAP_2));
        CAPSULE_BACH_KIM.add(5, new ItemRandom(ItemName.BUA_BAO_VE_CAP_3));
        CAPSULE_BACH_KIM.add(10, new ItemRandom(ItemName.DAU_THAN_CAP_8, 30));
        CAPSULE_BACH_KIM.add(10, new ItemRandom(ItemName.BUA_HOAN_LUONG));
        CAPSULE_BACH_KIM.add(10, new ItemRandom(ItemName.DA_7, 50));
        CAPSULE_BACH_KIM.add(10, new ItemRandom(ItemName.DA_8, 30));
        CAPSULE_BACH_KIM.add(10, new ItemRandom(ItemName.DA_9, 15));
        CAPSULE_BACH_KIM.add(5, new ItemRandom(ItemName.DA_10, 5));
        CAPSULE_BACH_KIM.add(3, new ItemRandom(ItemName.DA_11, 3));
        CAPSULE_BACH_KIM.add(5, new ItemRandom(ItemName.TAM_LINH_THACH, 10, 20));
        CAPSULE_BACH_KIM.add(5, new ItemRandom(ItemName.XU, 20000000, 50000000));
        CAPSULE_BACH_KIM.add(15, new ItemRandom(ItemName.XU, 5000000, 10000000));
        CAPSULE_BACH_KIM.add(10, new ItemRandom(ItemName.XU, 10000000, 15000000));

        RUONG_KHO_BAU.add(1, new ItemRandom(ItemName.RUBY, 80, 100));
        RUONG_KHO_BAU.add(5, new ItemRandom(ItemName.RUBY, 50, 80));
        RUONG_KHO_BAU.add(10, new ItemRandom(ItemName.RUBY, 20, 50));
        RUONG_KHO_BAU.add(84, new ItemRandom(ItemName.RUBY, 10, 20));

        SKILL_DISCIPLE_1.add(1, SkillName.KARAK_DISCIPLE);
        SKILL_DISCIPLE_1.add(1, SkillName.KARAP_DISCIPLE);
        SKILL_DISCIPLE_1.add(1, SkillName.KARAV_DISCIPLE);

        SKILL_DISCIPLE_2.add(1, SkillName.NOI_TAI_TRAI_DAT_DISCIPLE);
        SKILL_DISCIPLE_2.add(1, SkillName.NOI_TAI_NAMEK_DISCIPLE);
        SKILL_DISCIPLE_2.add(1, SkillName.NOI_TAI_SAYAIN_DISCIPLE);

        SKILL_DISCIPLE_3.add(0.5, SkillName.KAME_DISCIPLE);
        SKILL_DISCIPLE_3.add(1.5, SkillName.MASENDAN_DISCIPLE);
        SKILL_DISCIPLE_3.add(2, SkillName.SOKIDAN_DISCIPLE);

        SKILL_DISCIPLE_4.add(1, SkillName.TAI_TAO_NANG_LUONG);
        SKILL_DISCIPLE_4.add(0.2, SkillName.THAI_DUONG_HA_SAN);

        SKILL_DISCIPLE_1_EVENT.add(0.5, SkillName.KARAK_DISCIPLE);
        SKILL_DISCIPLE_1_EVENT.add(1, SkillName.KARAP_DISCIPLE);
        SKILL_DISCIPLE_1_EVENT.add(0.5, SkillName.KARAV_DISCIPLE);

        SKILL_DISCIPLE_2_EVENT.add(1, SkillName.NOI_TAI_TRAI_DAT_DISCIPLE);
        SKILL_DISCIPLE_2_EVENT.add(1, SkillName.NOI_TAI_NAMEK_DISCIPLE);
        SKILL_DISCIPLE_2_EVENT.add(1, SkillName.NOI_TAI_SAYAIN_DISCIPLE);

        SKILL_DISCIPLE_3_EVENT.add(0.5, SkillName.KAME_DISCIPLE);
        SKILL_DISCIPLE_3_EVENT.add(3, SkillName.MASENDAN_DISCIPLE);
        SKILL_DISCIPLE_3_EVENT.add(1, SkillName.SOKIDAN_DISCIPLE);

        SKILL_DISCIPLE_4_EVENT.add(1, SkillName.TAI_TAO_NANG_LUONG);
        SKILL_DISCIPLE_4_EVENT.add(0.2, SkillName.THAI_DUONG_HA_SAN);

        DRAGON_BALL_BARRACK.add(10, ItemName.NGOC_RONG_4_SAO);
        DRAGON_BALL_BARRACK.add(30, ItemName.NGOC_RONG_5_SAO);
        DRAGON_BALL_BARRACK.add(30, ItemName.NGOC_RONG_6_SAO);
        DRAGON_BALL_BARRACK.add(30, ItemName.NGOC_RONG_7_SAO);
    }
}
