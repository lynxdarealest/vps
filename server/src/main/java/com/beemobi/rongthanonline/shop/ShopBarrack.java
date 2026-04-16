package com.beemobi.rongthanonline.shop;

import com.beemobi.rongthanonline.entity.player.Player;
import com.beemobi.rongthanonline.item.ItemName;
import com.beemobi.rongthanonline.item.ItemShop;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

public class ShopBarrack extends Shop {
    private static final Logger logger = Logger.getLogger(ShopBarrack.class);
    private final Object[][][] items = new Object[][][]{
            {
                    {ItemName.CAPSULE_KI_BI, 1, TypePrice.POINT_BARRACK, 10, 1},
                    {ItemName.BINH_NUOC_PHEP, 1, TypePrice.POINT_BARRACK, 10, 1},
                    {ItemName.BI_KIP_KY_NANG, 10, TypePrice.POINT_BARRACK, 50, 1},
                    {ItemName.THE_DOI_KY_NANG_DE_TU, 1, TypePrice.POINT_BARRACK, 2000, 0},
            }
    };

    public ShopBarrack(ShopType type) {
        super(type);
    }

    @Override
    public LinkedHashMap<String, List<ItemShop>> createShop(Player player) {
        LinkedHashMap<String, List<ItemShop>> itemShops = new LinkedHashMap<>();
        String[] tabs = new String[]{"Cửa hàng"};
        for (String tab : tabs) {
            itemShops.put(tab, new ArrayList<>());
        }
        for (int i = 0; i < items.length; i++) {
            for (int j = 0; j < items[i].length; j++) {
                ItemShop itemShop = new ItemShop((int) items[i][j][0], (int) items[i][j][1], (TypePrice) items[i][j][2], (int) items[i][j][3]);
                if (itemShop.template.gender == -1 || itemShop.template.gender == player.gender) {
                    itemShops.get(tabs[i]).add(itemShop);
                }
            }
        }

        return itemShops;
    }
}
