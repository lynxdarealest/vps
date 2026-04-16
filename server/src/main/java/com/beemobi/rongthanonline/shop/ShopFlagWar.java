package com.beemobi.rongthanonline.shop;

import com.beemobi.rongthanonline.entity.player.Player;
import com.beemobi.rongthanonline.item.ItemName;
import com.beemobi.rongthanonline.item.ItemOption;
import com.beemobi.rongthanonline.item.ItemShop;
import com.beemobi.rongthanonline.util.Utils;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

public class ShopFlagWar extends Shop {
    private static final Logger logger = Logger.getLogger(ShopFlagWar.class);
    private final Object[][][] items = new Object[][][]{
            {
                    {ItemName.LOI_KI_NANG, 1, TypePrice.POINT_FLAG_WAR, 5, -1},
                    {ItemName.DA_10, 1, TypePrice.POINT_FLAG_WAR, 25, 7},
                    {ItemName.DAU_THAN_CAP_10, 10, TypePrice.POINT_FLAG_WAR, 50, -1},
                    {ItemName.TDLT, 1, TypePrice.POINT_FLAG_WAR, 50, -1},
                    {ItemName.CAPSULE_HIEP_SI, 1, TypePrice.POINT_FLAG_WAR, 1000, -1},
                    {ItemName.CAI_TRANG_FU, 1, TypePrice.POINT_FLAG_WAR, 15000, -1},

            }
    };

    public ShopFlagWar(ShopType type) {
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
            for (Object[] objects : items[i]) {
                ItemShop itemShop = new ItemShop((int) objects[0], (int) objects[1], (TypePrice) objects[2], (int) objects[3]);
                if (itemShop.template.id == ItemName.CAI_TRANG_FU) {
                    itemShop.options.add(new ItemOption(25, 30));
                    itemShop.options.add(new ItemOption(31, 30));
                    itemShop.options.add(new ItemOption(32, 30));
                    itemShop.options.add(new ItemOption(6, 1000));
                    itemShop.options.add(new ItemOption(33, 1));
                    itemShop.options.add(new ItemOption(34, 1));
                    itemShop.options.add(new ItemOption(35, 1));
                }
                int expiry = (int) objects[4];
                if (expiry != 0) {
                    itemShop.setExpiry(expiry);
                }
                itemShops.get(tabs[i]).add(itemShop);
            }
        }

        return itemShops;
    }
}
