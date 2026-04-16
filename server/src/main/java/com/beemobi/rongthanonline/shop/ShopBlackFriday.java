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

public class ShopBlackFriday extends Shop {
    public static final Object[][] items = new Object[][]{
            {ItemName.CAI_TRANG_PIC_HE_2024, 1, TypePrice.DIAMOND, 5000},
            {ItemName.CAI_TRANG_POC_HE_2024, 1, TypePrice.DIAMOND, 5000},
            {ItemName.CAI_TRANG_CHI_CHI, 1, TypePrice.DIAMOND, 5000},
            {ItemName.AVATAR_BROLY, 1, TypePrice.DIAMOND, 50},
            {ItemName.AVATAR_SUPER_VEGETA, 1, TypePrice.DIAMOND, 125},
            {ItemName.AVATAR_SUPER_BROLY, 1, TypePrice.DIAMOND, 250},
            {ItemName.AVATAR_TENSHINHAN, 1, TypePrice.DIAMOND, 50},
            {ItemName.AVATAR_TRUNKS, 1, TypePrice.DIAMOND, 125},
            {ItemName.AVATAR_SUPER_GOHAN, 1, TypePrice.DIAMOND, 250},
            {ItemName.AVATAR_WARRIOR_NAMEK, 1, TypePrice.DIAMOND, 50},
            {ItemName.AVATAR_DEVIL_NAMEK, 1, TypePrice.DIAMOND, 125},
            {ItemName.AVATAR_ASSASSIN_NAMEK, 1, TypePrice.DIAMOND, 250},
            {ItemName.CAI_TRANG_YARBON, 1, TypePrice.DIAMOND, 250},
            {ItemName.CAI_TRANG_KUI, 1, TypePrice.DIAMOND, 75},
            {ItemName.CAI_TRANG_DODO, 1, TypePrice.DIAMOND, 500},
            {ItemName.CAI_TRANG_SO_4, 1, TypePrice.DIAMOND, 250},
            {ItemName.CAI_TRANG_SO_3, 1, TypePrice.DIAMOND, 600},
            {ItemName.CAI_TRANG_SO_2, 1, TypePrice.DIAMOND, 650},
            {ItemName.CAI_TRANG_SO_1, 1, TypePrice.DIAMOND, 750},
            {ItemName.CAI_TRANG_TIEU_DOI_TRUONG, 1, TypePrice.DIAMOND, 850},
            {ItemName.CAI_TRANG_FIDE_WHITE, 1, TypePrice.DIAMOND, 1000},
            {ItemName.CAI_TRANG_FIDE_GOD, 1, TypePrice.DIAMOND, 1000},
            {ItemName.CAI_TRANG_UUB, 1, TypePrice.DIAMOND, 700},
            {ItemName.CAI_TRANG_VO_HINH, 1, TypePrice.DIAMOND, 700},
            {ItemName.CAI_TRANG_ANDROID_16, 1, TypePrice.DIAMOND, 1000},
            {ItemName.CAI_TRANG_ANDROID_17, 1, TypePrice.DIAMOND, 1750},
            {ItemName.CAI_TRANG_ANDROID_18, 1, TypePrice.DIAMOND, 1000},
            {ItemName.CAI_TRANG_ANDROID_19, 1, TypePrice.DIAMOND, 750},
            {ItemName.CAI_TRANG_ANDROID_20, 1, TypePrice.DIAMOND, 1000},
            {ItemName.AVATAR_LEGENDARY_BROLY, 1, TypePrice.DIAMOND, 2500},
            {ItemName.AVATAR_BEAST_GOHAN, 1, TypePrice.DIAMOND, 2500},
            {ItemName.AVATAR_SUPER_DENDE, 1, TypePrice.DIAMOND, 2500},
            {ItemName.AVATAR_TRUNK_SUPER_BLUE, 1, TypePrice.DIAMOND, 3500},
            {ItemName.AVATAR_PICOLO_SUPER_METAL, 1, TypePrice.DIAMOND, 3500},
            {ItemName.AVATAR_GOKU_SUPER_GOD, 1, TypePrice.DIAMOND, 3500},
            {ItemName.CAI_TRANG_SUPER_EVIL_XICOR, 1, TypePrice.DIAMOND, 5000},
            {ItemName.CAI_TRANG_PICOLO_SUPER_GOLD, 1, TypePrice.DIAMOND, 5000},
            {ItemName.CAI_TRANG_GOKU_SUPER_ULTRA, 1, TypePrice.DIAMOND, 5000},
            {ItemName.AVATAR_SUPER_GOHAN_PURPLE, 1, TypePrice.DIAMOND, 5000},
            {ItemName.AVATAR_SUPER_NAMEK_DRAGON, 1, TypePrice.DIAMOND, 5000},
            {ItemName.AVATAR_BLACK_GOKU_ROSE, 1, TypePrice.DIAMOND, 5000},
            {ItemName.CAI_TRANG_DRABURA, 1, TypePrice.DIAMOND, 5000},
            {ItemName.CAI_TRANG_MABU, 1, TypePrice.DIAMOND, 5000},
    };

    private static final Logger logger = Logger.getLogger(ShopBlackFriday.class);

    public ShopBlackFriday(ShopType type) {
        super(type);
    }

    @Override
    public LinkedHashMap<String, List<ItemShop>> createShop(Player player) {
        LinkedHashMap<String, List<ItemShop>> itemShops = new LinkedHashMap<>();
        String[] tabs = new String[]{"Cửa hàng"};
        for (String tab : tabs) {
            itemShops.put(tab, new ArrayList<>());
        }
        for (Object[] item : items) {
            ItemShop itemShop = new ItemShop((int) item[0], (int) item[1], (TypePrice) item[2], (int) item[3]);
            switch (itemShop.template.id) {
                case ItemName.CAI_TRANG_PIC_HE_2024: {
                    itemShop.options.clear();
                    itemShop.options.add(new ItemOption(25, 40));
                    itemShop.options.add(new ItemOption(31, 50));
                    itemShop.options.add(new ItemOption(32, 50));
                    itemShop.options.add(new ItemOption(6, 5000));
                    itemShop.options.add(new ItemOption(169, 5));
                    itemShop.options.add(new ItemOption(33, 2));
                    itemShop.options.add(new ItemOption(34, 2));
                    itemShop.options.add(new ItemOption(35, 2));
                    itemShop.options.add(new ItemOption(111, 50));
                    break;
                }

                case ItemName.CAI_TRANG_POC_HE_2024: {
                    itemShop.options.clear();
                    itemShop.options.add(new ItemOption(25, 45));
                    itemShop.options.add(new ItemOption(31, 40));
                    itemShop.options.add(new ItemOption(32, 40));
                    itemShop.options.add(new ItemOption(1, 2000));
                    itemShop.options.add(new ItemOption(2, 5000));
                    itemShop.options.add(new ItemOption(169, 5));
                    itemShop.options.add(new ItemOption(33, 2));
                    itemShop.options.add(new ItemOption(34, 2));
                    itemShop.options.add(new ItemOption(35, 2));
                    itemShop.options.add(new ItemOption(78, 60));
                    break;
                }

                case ItemName.CAI_TRANG_CHI_CHI: {
                    itemShop.options.clear();
                    itemShop.options.add(new ItemOption(25, 30));
                    itemShop.options.add(new ItemOption(31, 45));
                    itemShop.options.add(new ItemOption(32, 45));
                    itemShop.options.add(new ItemOption(1, 2000));
                    itemShop.options.add(new ItemOption(2, 5000));
                    itemShop.options.add(new ItemOption(6, 2000));
                    itemShop.options.add(new ItemOption(132, 20));
                    itemShop.options.add(new ItemOption(133, 20));
                    itemShop.options.add(new ItemOption(72, 1));
                    itemShop.options.add(new ItemOption(85, 25));
                    itemShop.options.add(new ItemOption(78, 50));
                    itemShop.options.add(new ItemOption(33, 2));
                    itemShop.options.add(new ItemOption(34, 2));
                    itemShop.options.add(new ItemOption(35, 2));
                    break;
                }
            }
            itemShops.get(tabs[0]).add(itemShop);
        }
        return itemShops;
    }
}
