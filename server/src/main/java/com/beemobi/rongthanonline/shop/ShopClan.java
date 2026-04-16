package com.beemobi.rongthanonline.shop;

import com.beemobi.rongthanonline.entity.player.Player;
import com.beemobi.rongthanonline.item.ItemName;
import com.beemobi.rongthanonline.item.ItemOption;
import com.beemobi.rongthanonline.item.ItemShop;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

public class ShopClan extends Shop {
    private static final Logger logger = Logger.getLogger(ShopBunmaOther.class);
    public static final Object[][][] items = new Object[][][]{
            {
                    {ItemName.LENH_BAI_CAP_1, 1, TypePrice.COIN_LOCK, 1000000},
                    {ItemName.LENH_BAI_CAP_2, 1, TypePrice.COIN_LOCK, 5000000},
                    {ItemName.LENH_BAI_CAP_3, 1, TypePrice.COIN_LOCK, 20000000},
                    {ItemName.LENH_BAI_CAP_4, 1, TypePrice.COIN_LOCK, 50000000},
                    {ItemName.LENH_BAI_CAP_5, 1, TypePrice.COIN_LOCK, 100000000},
                    {ItemName.LENH_BAI_CAP_6, 1, TypePrice.COIN_LOCK, 150000000},
                    {ItemName.LENH_BAI_CAP_7, 1, TypePrice.COIN_LOCK, 200000000},
                    {ItemName.LENH_BAI_CAP_8, 1, TypePrice.COIN_LOCK, 250000000},
                    {ItemName.LENH_BAI_CAP_9, 1, TypePrice.COIN_LOCK, 300000000},
                    {ItemName.LENH_BAI_CAP_10, 1, TypePrice.COIN_LOCK, 350000000},
            }
    };

    public ShopClan(ShopType type) {
        super(type);
    }

    @Override
    public LinkedHashMap<String, List<ItemShop>> createShop(Player player) {
        LinkedHashMap<String, List<ItemShop>> itemShops = new LinkedHashMap<>();
        if (player.clan == null) {
            return itemShops;
        }
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

            ItemShop itemShop = new ItemShop(ItemName.HUY_HIEU_BANG_HOI_CAP_6, 1, TypePrice.COIN, 100000000);
            itemShop.options.add(new ItemOption(190, 5));
            itemShop.options.add(new ItemOption(50, 7));
            itemShops.get(tabs[i]).add(itemShop);

            ItemShop itemShop1 = new ItemShop(ItemName.HUY_HIEU_BANG_HOI_CAP_6, 1, TypePrice.DIAMOND, 250);
            itemShop1.options.add(new ItemOption(189, 20));
            itemShop1.options.add(new ItemOption(50, 7));
            itemShops.get(tabs[i]).add(itemShop1);
        }

        return itemShops;
    }
}
