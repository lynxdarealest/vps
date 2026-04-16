package com.beemobi.rongthanonline.shop;

import com.beemobi.rongthanonline.entity.player.Player;
import com.beemobi.rongthanonline.event.Event;
import com.beemobi.rongthanonline.item.*;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;

public class ShopBunmaOther extends Shop {
    private static final Logger logger = Logger.getLogger(ShopBunmaOther.class);

    private final Object[][][] items = new Object[][][]{
            {
                    {ItemName.COM_NAM, 1, TypePrice.COIN_LOCK, 1000, 1},
                    {ItemName.SAND_WITCH, 1, TypePrice.COIN_LOCK, 3000, 1},
                    {ItemName.GA_QUAY, 1, TypePrice.COIN_LOCK, 10000, 1},
                    {ItemName.DAU_THAN_CAP_1, 1, TypePrice.COIN_LOCK, 5000, 1},
                    {ItemName.DAU_THAN_CAP_2, 1, TypePrice.COIN_LOCK, 55000, 1},
                    {ItemName.DAU_THAN_CAP_3, 1, TypePrice.COIN_LOCK, 105000, 1},
                    {ItemName.VONG_KIM_HAM_BAC, 1, TypePrice.COIN, 100000000, 1},
                    {ItemName.VE_THAM_GIA_DAO_KHO_BAU, 1, TypePrice.COIN_LOCK, 100000000, 0},
                    {ItemName.VE_SINH_TON, 1, TypePrice.COIN, 500000000, 0},
                    {ItemName.THE_TRIEU_HOI, 1, TypePrice.COIN_LOCK, 1000000, 1},
            },
            {
                    {ItemName.CAN_DAU_VAN, 1, TypePrice.RUBY, 50, 0},
                    {ItemName.HOA_VAN, 1, TypePrice.RUBY, 500, 0},
                    {ItemName.DAU_THAN_CAP_8, 30, TypePrice.RUBY, 30, 1},
                    {ItemName.CAPSULE_TAU_BAY_THUONG, 30, TypePrice.RUBY, 10, 0},
                    {ItemName.CAPSULE_TAU_BAY_DAC_BIET, 1, TypePrice.RUBY, 1000, 0},
                    {ItemName.THE_TIEN_ICH, 1, TypePrice.RUBY, 50, 0},
                    {ItemName.BI_KIP_KY_NANG, 10, TypePrice.RUBY, 10, 1},
                    {ItemName.TDLT, 1, TypePrice.RUBY, 2, 1},
                    {ItemName.RADAR_RT, 1, TypePrice.RUBY, 5, 1},
                    {ItemName.RADAR_RT2, 1, TypePrice.DIAMOND, 5, 1},
                    {ItemName.MAY_DO_CAPSULE_DONG, 1, TypePrice.DIAMOND, 10, 1},
                    {ItemName.THE_DOI_DE_TU, 1, TypePrice.DIAMOND, 100, 0},
                    {ItemName.DOI_TEN_DE_TU, 1, TypePrice.RUBY, 50, 0},
                    {ItemName.BONG_TAI_PORATA, 1, TypePrice.RUBY, 1000, 0},
                    {ItemName.NANG_CAP_CHIEU_1_DE_TU, 1, TypePrice.RUBY, 100, 0},
                    {ItemName.NANG_CAP_CHIEU_2_DE_TU, 1, TypePrice.RUBY, 150, 0},
                    {ItemName.NANG_CAP_CHIEU_3_DE_TU, 1, TypePrice.RUBY, 200, 0},
                    {ItemName.NANG_CAP_CHIEU_4_DE_TU, 1, TypePrice.RUBY, 250, 0},
                    {ItemName.NANG_CAP_CHIEU_5_DE_TU, 1, TypePrice.RUBY, 300, 0},
                    {ItemName.VONG_KIM_HAM_BAC, 1, TypePrice.RUBY, 30, 0},
                    {ItemName.VONG_KIM_HAM_VANG, 1, TypePrice.RUBY, 300, 0},
                    {ItemName.VONG_KIM_HAM_BACH_KIM, 1, TypePrice.DIAMOND, 1000, 0},
                    {ItemName.THE_DOI_TEN, 1, TypePrice.DIAMOND, 1500, 0},
                    {ItemName.KINH_RAM, 1, TypePrice.RUBY, 10, 0},
                    {ItemName.KHUYEN_TAI, 1, TypePrice.DIAMOND, 30, 0},
                    {ItemName.VE_THAM_GIA_DAO_KHO_BAU, 1, TypePrice.RUBY, 100, 0},
                    {ItemName.THE_DOI_KY_NANG_DE_TU, 1, TypePrice.RUBY, 500, 0},
                    {ItemName.CAPSULE_THOI_KHONG, 1, TypePrice.RUBY, 50, 0},
                    {ItemName.VE_SINH_TON, 1, TypePrice.DIAMOND, 500, 0},
            }
    };

    public ShopBunmaOther(ShopType type) {
        super(type);
    }

    @Override
    public LinkedHashMap<String, List<ItemShop>> createShop(Player player) {
        LinkedHashMap<String, List<ItemShop>> itemShops = new LinkedHashMap<>();
        ArrayList<String> tabs = new ArrayList<>(Arrays.asList("Xu/Xu khóa", "KC/Ruby", "Sự kiện"));
        for (String tab : tabs) {
            itemShops.put(tab, new ArrayList<>());
        }
        for (int i = 0; i < items.length; i++) {
            for (int j = 0; j < items[i].length; j++) {
                ItemShop itemShop = new ItemShop((int) items[i][j][0], (int) items[i][j][1], (TypePrice) items[i][j][2], (int) items[i][j][3]);
                if (itemShop.template.gender == -1 || itemShop.template.gender == player.gender) {
                    itemShops.get(tabs.get(i)).add(itemShop);
                }
            }
        }
        for (Item item : player.itemsBag) {
            if (item != null && item.template.id == ItemName.THE_GIAM_GIA) {
                int sale = item.getParam(84);
                if (sale > 0 && sale < 100) {
                    tabs.add("Giảm giá");
                    itemShops.put(tabs.get(3), new ArrayList<>());
                    for (int j = 0; j < ShopBunmaEquip.items[2].length; j++) {
                        ItemShop itemShop = new ItemShop((int) ShopBunmaEquip.items[2][j][0],
                                (int) ShopBunmaEquip.items[2][j][1],
                                (TypePrice) ShopBunmaEquip.items[2][j][2],
                                (int) ShopBunmaEquip.items[2][j][3] * (100 - sale) / 100);
                        itemShop.isSaleCard = true;
                        if (itemShop.template.gender == -1 || itemShop.template.gender == player.gender) {
                            itemShops.get(tabs.get(3)).add(itemShop);
                        }
                    }
                }
                break;
            }
        }
        itemShops.get(tabs.get(1)).add(new ItemShop(ItemName.MAY_DO_TINH_THACH, 10, TypePrice.DIAMOND, 50));
        if (Event.isEvent()) {
            itemShops.get(tabs.get(2)).addAll(Event.event.createShop(player));
        } else {
            itemShops.remove(tabs.get(2));
        }
        if (player.spaceship == 0) {
            itemShops.get(tabs.get(1)).add(new ItemShop(ItemName.CHIEN_THUYEN_TENIS, 1, TypePrice.DIAMOND, 50));
        }
        if (player.countBarrack == 0 && player.numBuyBarrack < 2) {
            if (player.numBuyBarrack == 0) {
                itemShops.get(tabs.get(0)).add(new ItemShop(ItemName.VE_BAN_DOANH, 1, TypePrice.COIN, 500000 + player.level * 50000));
            } else {
                itemShops.get(tabs.get(1)).add(new ItemShop(ItemName.VE_BAN_DOANH, 1, TypePrice.DIAMOND, 50 + player.level * 2));
            }
        }

        ItemShop itemShop1 = new ItemShop(ItemName.CO_DO_SAO_VANG, 1, TypePrice.DIAMOND, 500);
        itemShop1.options.clear();
        itemShop1.options.add(new ItemOption(168, 100));
        itemShop1.options.add(new ItemOption(50, 30));
        itemShops.get(tabs.get(1)).add(itemShop1);

       /* ItemShop itemShop = new ItemShop(ItemName.CO_DO_SAO_VANG, 1, TypePrice.DIAMOND, 500);
        itemShop.options.add(new ItemOption(109, 5));
        itemShops.get(tabs.get(1)).add(itemShop);

        ItemShop itemShop1 = new ItemShop(ItemName.CO_DO_SAO_VANG, 1, TypePrice.DIAMOND, 5000);
        itemShop1.options.add(new ItemOption(109, 20));
        itemShops.get(tabs.get(1)).add(itemShop1);

        ItemShop itemShop3 = new ItemShop(ItemName.HUY_HIEU_VIET_NAM, 1, TypePrice.DIAMOND, 500);
        itemShop3.options.add(new ItemOption(109, 5));
        itemShops.get(tabs.get(1)).add(itemShop3);

        ItemShop itemShop2 = new ItemShop(ItemName.HUY_HIEU_VIET_NAM, 1, TypePrice.DIAMOND, 5000);
        itemShop2.options.add(new ItemOption(109, 20));
        itemShops.get(tabs.get(1)).add(itemShop2);*/

        return itemShops;
    }
}
