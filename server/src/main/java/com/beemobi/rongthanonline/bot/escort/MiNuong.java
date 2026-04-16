package com.beemobi.rongthanonline.bot.escort;

import com.beemobi.rongthanonline.item.ItemManager;
import com.beemobi.rongthanonline.item.ItemName;
import com.beemobi.rongthanonline.map.MapName;
import com.beemobi.rongthanonline.util.Utils;

public class MiNuong extends Escort {
    public static int[] MAPS = {MapName.THAC_NUOC_KEISE, MapName.RUNG_NAM_FUKA, MapName.DOI_HOANG_AKA, MapName.THUNG_LUNG_MARIA,
            MapName.DONG_BANG_MIKA, MapName.CAO_NGUYEN_TAKA, MapName.RUNG_GOZA, MapName.LANG_ARU, MapName.THANH_PHO_PEIN,
            MapName.THANH_PHO_NAM, MapName.THANH_PHO_NAM, MapName.THANH_PHO_PHIA_NAM, MapName.BO_VUC_GIRAN, MapName.VUC_GIRAN,
            MapName.RUNG_GIRAN, MapName.RUNG_NGUYEN_THUY, MapName.DAO_KAME};

    public MiNuong() {
        super("#Mị nương");
    }

    @Override
    public void setSkin() {
        head = 81;
        body = 82;
    }

    @Override
    public void chatWhenMove() {
        String[] text = new String[]{"Thiếp sợ", "Mong chàng bảo vệ thiếp"};
        chat(text[Utils.nextInt(text.length)]);
    }

    @Override
    public boolean isEnd() {
        return zone != null && zone.map.template.id == MapName.NUI_PAOZU;
    }

    @Override
    public void end() {
        if (!follower.isBagFull()) {
            int min = follower.level;
            follower.addItem(ItemManager.getInstance().createItem(ItemName.NGUA_9_HONG_MAO, Utils.nextInt(min, min + 10), true), true);
            if (Utils.isPercent(10)) {
                follower.addItem(ItemManager.getInstance().createItem(ItemName.DUA_XANH, 1, true), true);
            } else {
                follower.addItem(ItemManager.getInstance().createItem(
                        Utils.nextArray(new int[]{ItemName.KEO_DO, ItemName.KEO_VANG, ItemName.KEO_LUC, ItemName.KEO_XANH}),
                        Utils.nextInt(1, 3), true), true);
            }
        }
        Utils.setTimeout(() -> {
            enter(MAPS);
        }, 300000);
        super.end();
    }

}
