package com.beemobi.rongthanonline.shop;

import com.beemobi.rongthanonline.entity.player.Player;
import com.beemobi.rongthanonline.item.ItemName;
import com.beemobi.rongthanonline.item.ItemShop;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

public class ShopAmuletOther extends Shop {
    private static final Logger logger = Logger.getLogger(ShopAmuletOther.class);

    private final Object[][][] items = new Object[][][]{
            {
                    {ItemName.BUA_HOAN_LUONG, 1, TypePrice.DIAMOND, 10, 1},
                    {ItemName.BUA_TAY_TIEM_NANG, 1, TypePrice.DIAMOND, 30, 1},
                    {ItemName.BUA_BAO_VE_CAP_1, 1, TypePrice.RUBY, 10, 1},
                    {ItemName.BUA_BAO_VE_CAP_2, 1, TypePrice.RUBY, 30, 1},
                    {ItemName.BUA_BAO_VE_CAP_3, 1, TypePrice.RUBY, 50, 1},
                    {ItemName.BUA_BAO_VE_CAP_4, 1, TypePrice.RUBY, 100, 1},
                    {ItemName.BUA_THOI_GIAN, 1, TypePrice.DIAMOND, 150, 1},
                    {ItemName.BUA_MO_RONG_TUI, 1, TypePrice.DIAMOND, 150, 0},
                    {ItemName.BUA_MO_RONG_RUONG, 1, TypePrice.DIAMOND, 150, 0},
                    {ItemName.BUA_TAY_TNSM_DE_TU, 1, TypePrice.DIAMOND, 150, 0}
            }
    };

    public ShopAmuletOther(ShopType type) {
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
