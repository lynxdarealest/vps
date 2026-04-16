package com.beemobi.rongthanonline.shop;

import com.beemobi.rongthanonline.entity.player.Player;
import com.beemobi.rongthanonline.item.ItemName;
import com.beemobi.rongthanonline.item.ItemOption;
import com.beemobi.rongthanonline.item.ItemShop;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

public class ShopRider extends Shop {
    private static final Logger logger = Logger.getLogger(ShopRider.class);
    private final Object[][] items = new Object[][]{
            {ItemName.THE_VAN_NANG, 1, TypePrice.DIAMOND, 1000, 1},
            {ItemName.THE_VAN_NANG, 1, TypePrice.DIAMOND, 2500, 1},
            {ItemName.THE_VAN_NANG, 1, TypePrice.DIAMOND, 3000, 1},
            {ItemName.THE_VAN_NANG, 1, TypePrice.DIAMOND, 3500, 1},
            {ItemName.THE_VAN_NANG, 1, TypePrice.DIAMOND, 4000, 1},
    };

    public ShopRider(ShopType type) {
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
            ItemShop itemShop = new ItemShop((int) items[i][0], (int) items[i][1], (TypePrice) items[i][2], (int) items[i][3]);
            if (i == 0) {
                itemShop.options.add(new ItemOption(163, 14));
            } else if (i == 1) {
                itemShop.options.add(new ItemOption(163, 16));
            } else if (i == 2) {
                itemShop.options.add(new ItemOption(163, 17));
            } else if (i == 3) {
                itemShop.options.add(new ItemOption(163, 18));
            } else if (i == 4) {
                itemShop.options.add(new ItemOption(163, 19));
            }
            itemShop.isLock = true;
            itemShops.get(tabs[0]).add(itemShop);
        }
        return itemShops;
    }
}