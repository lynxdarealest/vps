package com.beemobi.rongthanonline.shop;

import com.beemobi.rongthanonline.entity.player.Player;
import com.beemobi.rongthanonline.item.ItemName;
import com.beemobi.rongthanonline.item.ItemOption;
import com.beemobi.rongthanonline.item.ItemShop;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

public class ShopActive extends Shop {
    private static final Logger logger = Logger.getLogger(ShopActive.class);
    private final Object[][][] items = new Object[][][]{
            {
                    {ItemName.CO_DO_SAO_VANG, 1, TypePrice.POINT_ACTIVE, 300, 30},
                    {ItemName.CAN_DAU_VAN, 1, TypePrice.POINT_ACTIVE, 200, 0},
                    {ItemName.HOA_VAN, 1, TypePrice.POINT_ACTIVE, 1000, 0},
                    {ItemName.DAU_THAN_CAP_4, 100, TypePrice.POINT_ACTIVE, 10, 0},
                    {ItemName.DA_5, 10, TypePrice.POINT_ACTIVE, 10, 0},
                    {ItemName.CAPSULE_TAU_BAY_THUONG, 30, TypePrice.POINT_ACTIVE, 10, 0},
                    //{ItemName.CAPSULE_BACH_KIM, 1, TypePrice.POINT_ACTIVE, 250, 0},
                    //{ItemName.CAPSULE_VANG, 1, TypePrice.POINT_ACTIVE, 100, 0},
                    {ItemName.BI_KIP_KY_NANG, 10, TypePrice.POINT_ACTIVE, 50, 0},
                    {ItemName.TDLT, 1, TypePrice.POINT_ACTIVE, 10, 0},
                    {ItemName.CAI_TRANG_SIEU_NHAN, 1, TypePrice.POINT_ACTIVE, 2000, -1},
                    {ItemName.CAI_TRANG_SIEU_NHAN, 1, TypePrice.POINT_ACTIVE, 300, 30},
                    {ItemName.TAU_BAY_FIDE, 1, TypePrice.POINT_ACTIVE, 50, 30},
                    {ItemName.TAU_BAY_FIDE, 1, TypePrice.POINT_ACTIVE, 300, -1},
            }
    };

    public ShopActive(ShopType type) {
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
                if (itemShop.template.id == ItemName.CO_DO_SAO_VANG) {
                    itemShop.options.add(new ItemOption(168, 50));
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
