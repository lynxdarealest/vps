package com.beemobi.rongthanonline.bot.escort;

import com.beemobi.rongthanonline.item.ItemManager;
import com.beemobi.rongthanonline.item.ItemName;
import com.beemobi.rongthanonline.map.MapName;
import com.beemobi.rongthanonline.util.Utils;

public class TuanLoc extends Escort {
    public static int[] MAPS = {MapName.THAC_NUOC_KEISE, MapName.RUNG_NAM_FUKA, MapName.DOI_HOANG_AKA, MapName.THUNG_LUNG_MARIA,
            MapName.DONG_BANG_MIKA, MapName.CAO_NGUYEN_TAKA, MapName.RUNG_GOZA, MapName.LANG_ARU, MapName.THANH_PHO_PEIN,
            MapName.THANH_PHO_NAM, MapName.THANH_PHO_NAM, MapName.THANH_PHO_PHIA_NAM, MapName.BO_VUC_GIRAN, MapName.VUC_GIRAN,
            MapName.RUNG_GIRAN, MapName.RUNG_NGUYEN_THUY, MapName.DAO_KAME};

    public TuanLoc() {
        super("#Tuần lộc");
    }

    @Override
    public void setSkin() {
        head = 313;
        body = 314;
    }

    @Override
    public boolean isEnd() {
        return zone != null && zone.map.template.id == MapName.NUI_PAOZU;
    }

    @Override
    public void end() {
        if (!follower.isBagFull()) {
            follower.addItem(ItemManager.getInstance().createItem(ItemName.CHUONG_VANG, Utils.nextInt(5, 10), true), true);
        }
        Utils.setTimeout(() -> {
            enter(MAPS);
        }, 300000);
        super.end();
    }

}
