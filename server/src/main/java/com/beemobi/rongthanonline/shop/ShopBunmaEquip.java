package com.beemobi.rongthanonline.shop;

import com.beemobi.rongthanonline.entity.player.Player;
import com.beemobi.rongthanonline.item.*;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

public class ShopBunmaEquip extends Shop {
    private static final Logger logger = Logger.getLogger(ShopBunmaEquip.class);

    public static final Object[][][] items = new Object[][][]{
            {
                    {ItemName.GANG_TAN_BINH_TRAI_DAT, 1, TypePrice.COIN_LOCK, 1000},
                    {ItemName.GIAY_TAN_BINH_TRAI_DAT, 1, TypePrice.COIN_LOCK, 1000},
                    {ItemName.QUAN_TAN_BINH_TRAI_DAT, 1, TypePrice.COIN_LOCK, 1000},
                    {ItemName.AO_TAN_BINH_TRAI_DAT, 1, TypePrice.COIN_LOCK, 1000},
                    {ItemName.GANG_TAN_BINH_NAMEK, 1, TypePrice.COIN_LOCK, 1000},
                    {ItemName.GIAY_TAN_BINH_NAMEK, 1, TypePrice.COIN_LOCK, 1000},
                    {ItemName.QUAN_TAN_BINH_NAMEK, 1, TypePrice.COIN_LOCK, 1000},
                    {ItemName.AO_TAN_BINH_NAMEK, 1, TypePrice.COIN_LOCK, 1000},
                    {ItemName.GANG_TAN_BINH_SAYAIN, 1, TypePrice.COIN_LOCK, 1000},
                    {ItemName.GIAY_TAN_BINH_SAYAIN, 1, TypePrice.COIN_LOCK, 1000},
                    {ItemName.QUAN_TAN_BINH_SAYAIN, 1, TypePrice.COIN_LOCK, 1000},
                    {ItemName.AO_TAN_BINH_SAYAIN, 1, TypePrice.COIN_LOCK, 1000},
                    {ItemName.GANG_VE_BINH_TRAI_DAT, 1, TypePrice.COIN_LOCK, 20000},
                    {ItemName.GIAY_VE_BINH_TRAI_DAT, 1, TypePrice.COIN_LOCK, 20000},
                    {ItemName.QUAN_VE_BINH_TRAI_DAT, 1, TypePrice.COIN_LOCK, 20000},
                    {ItemName.AO_VE_BINH_TRAI_DAT, 1, TypePrice.COIN_LOCK, 20000},
                    {ItemName.GANG_VE_BINH_NAMEK, 1, TypePrice.COIN_LOCK, 20000},
                    {ItemName.GIAY_VE_BINH_NAMEK, 1, TypePrice.COIN_LOCK, 20000},
                    {ItemName.QUAN_VE_BINH_NAMEK, 1, TypePrice.COIN_LOCK, 20000},
                    {ItemName.AO_VE_BINH_NAMEK, 1, TypePrice.COIN_LOCK, 20000},
                    {ItemName.GANG_VE_BINH_SAYAIN, 1, TypePrice.COIN_LOCK, 20000},
                    {ItemName.GIAY_VE_BINH_SAYAIN, 1, TypePrice.COIN_LOCK, 20000},
                    {ItemName.QUAN_VE_BINH_SAYAIN, 1, TypePrice.COIN_LOCK, 20000},
                    {ItemName.AO_VE_BINH_SAYAIN, 1, TypePrice.COIN_LOCK, 20000},
                    {ItemName.GANG_CHIEN_BINH_TRAI_DAT, 1, TypePrice.COIN_LOCK, 100000},
                    {ItemName.GIAY_CHIEN_BINH_TRAI_DAT, 1, TypePrice.COIN_LOCK, 100000},
                    {ItemName.QUAN_CHIEN_BINH_TRAI_DAT, 1, TypePrice.COIN_LOCK, 100000},
                    {ItemName.AO_CHIEN_BINH_TRAI_DAT, 1, TypePrice.COIN_LOCK, 100000},
                    {ItemName.GANG_CHIEN_BINH_NAMEK, 1, TypePrice.COIN_LOCK, 100000},
                    {ItemName.GIAY_CHIEN_BINH_NAMEK, 1, TypePrice.COIN_LOCK, 100000},
                    {ItemName.QUAN_CHIEN_BINH_NAMEK, 1, TypePrice.COIN_LOCK, 100000},
                    {ItemName.AO_CHIEN_BINH_NAMEK, 1, TypePrice.COIN_LOCK, 100000},
                    {ItemName.GANG_CHIEN_BINH_SAYAIN, 1, TypePrice.COIN_LOCK, 100000},
                    {ItemName.GIAY_CHIEN_BINH_SAYAIN, 1, TypePrice.COIN_LOCK, 100000},
                    {ItemName.QUAN_CHIEN_BINH_SAYAIN, 1, TypePrice.COIN_LOCK, 100000},
                    {ItemName.AO_CHIEN_BINH_SAYAIN, 1, TypePrice.COIN_LOCK, 100000}
            },
            {
                    {ItemName.NGOC_BOI_TAN_BINH, 1, TypePrice.COIN_LOCK, 1000},
                    {ItemName.NHAN_TAN_BINH, 1, TypePrice.COIN_LOCK, 1000},
                    {ItemName.DAY_CHUYEN_TAN_BINH, 1, TypePrice.COIN_LOCK, 1000},
                    {ItemName.RADAR_TAN_BINH, 1, TypePrice.COIN_LOCK, 1000},
                    {ItemName.NGOC_BOI_VE_BINH, 1, TypePrice.COIN_LOCK, 20000},
                    {ItemName.NHAN_VE_BINH, 1, TypePrice.COIN_LOCK, 20000},
                    {ItemName.DAY_CHUYEN_VE_BINH, 1, TypePrice.COIN_LOCK, 20000},
                    {ItemName.RADAR_VE_BINH, 1, TypePrice.COIN_LOCK, 20000},
                    {ItemName.NGOC_BOI_CHIEN_BINH, 1, TypePrice.COIN_LOCK, 100000},
                    {ItemName.NHAN_CHIEN_BINH, 1, TypePrice.COIN_LOCK, 100000},
                    {ItemName.DAY_CHUYEN_CHIEN_BINH, 1, TypePrice.COIN_LOCK, 100000},
                    {ItemName.RADAR_CHIEN_BINH, 1, TypePrice.COIN_LOCK, 100000}
            },
            {
                    {ItemName.AVATAR_BROLY, 1, TypePrice.RUBY, 100},
                    {ItemName.AVATAR_SUPER_VEGETA, 1, TypePrice.RUBY, 250},
                    {ItemName.AVATAR_SUPER_BROLY, 1, TypePrice.RUBY, 500},
                    {ItemName.AVATAR_TENSHINHAN, 1, TypePrice.RUBY, 100},
                    {ItemName.AVATAR_TRUNKS, 1, TypePrice.RUBY, 250},
                    {ItemName.AVATAR_SUPER_GOHAN, 1, TypePrice.RUBY, 500},
                    {ItemName.AVATAR_WARRIOR_NAMEK, 1, TypePrice.RUBY, 100},
                    {ItemName.AVATAR_DEVIL_NAMEK, 1, TypePrice.RUBY, 250},
                    {ItemName.AVATAR_ASSASSIN_NAMEK, 1, TypePrice.RUBY, 500},
                    {ItemName.CAI_TRANG_YARBON, 1, TypePrice.RUBY, 500},
                    {ItemName.CAI_TRANG_KUI, 1, TypePrice.RUBY, 150},
                    {ItemName.CAI_TRANG_DODO, 1, TypePrice.RUBY, 1000},
                    {ItemName.CAI_TRANG_SO_4, 1, TypePrice.RUBY, 500},
                    {ItemName.CAI_TRANG_SO_3, 1, TypePrice.RUBY, 1200},
                    {ItemName.CAI_TRANG_SO_2, 1, TypePrice.RUBY, 1300},
                    {ItemName.CAI_TRANG_SO_1, 1, TypePrice.RUBY, 1500},
                    {ItemName.CAI_TRANG_TIEU_DOI_TRUONG, 1, TypePrice.RUBY, 1700},
                    {ItemName.CAI_TRANG_FIDE_WHITE, 1, TypePrice.RUBY, 2000},
                    {ItemName.CAI_TRANG_FIDE_GOD, 1, TypePrice.RUBY, 2000},
                    {ItemName.CAI_TRANG_UUB, 1, TypePrice.RUBY, 1400},
                    {ItemName.CAI_TRANG_VO_HINH, 1, TypePrice.RUBY, 1400},
                    {ItemName.CAI_TRANG_ANDROID_16, 1, TypePrice.RUBY, 2000},
                    {ItemName.CAI_TRANG_ANDROID_17, 1, TypePrice.RUBY, 3500},
                    {ItemName.CAI_TRANG_ANDROID_18, 1, TypePrice.RUBY, 2000},
                    {ItemName.CAI_TRANG_ANDROID_19, 1, TypePrice.RUBY, 1500},
                    {ItemName.CAI_TRANG_ANDROID_20, 1, TypePrice.RUBY, 2000},
                    {ItemName.AVATAR_LEGENDARY_BROLY, 1, TypePrice.DIAMOND, 5000},
                    {ItemName.AVATAR_BEAST_GOHAN, 1, TypePrice.DIAMOND, 5000},
                    {ItemName.AVATAR_SUPER_DENDE, 1, TypePrice.DIAMOND, 5000},
                    {ItemName.AVATAR_TRUNK_SUPER_BLUE, 1, TypePrice.DIAMOND, 7500},
                    {ItemName.AVATAR_PICOLO_SUPER_METAL, 1, TypePrice.DIAMOND, 7500},
                    {ItemName.AVATAR_GOKU_SUPER_GOD, 1, TypePrice.DIAMOND, 7500},
                    {ItemName.CAI_TRANG_SUPER_EVIL_XICOR, 1, TypePrice.DIAMOND, 10000},
                    {ItemName.CAI_TRANG_PICOLO_SUPER_GOLD, 1, TypePrice.DIAMOND, 10000},
                    {ItemName.CAI_TRANG_GOKU_SUPER_ULTRA, 1, TypePrice.DIAMOND, 10000},
                    {ItemName.AVATAR_SUPER_GOHAN_PURPLE, 1, TypePrice.DIAMOND, 12500},
                    {ItemName.AVATAR_SUPER_NAMEK_DRAGON, 1, TypePrice.DIAMOND, 12500},
                    {ItemName.AVATAR_BLACK_GOKU_ROSE, 1, TypePrice.DIAMOND, 12500},
                    {ItemName.CAI_TRANG_DRABURA, 1, TypePrice.DIAMOND, 10000},
                    {ItemName.CAI_TRANG_MABU, 1, TypePrice.DIAMOND, 10000},
                    /*{ItemName.CAI_TRANG_APK_21_20_10_2024, 1, TypePrice.DIAMOND, 2500},
                    {ItemName.CAI_TRANG_BUNMA_BIKINI_20_10_2024, 1, TypePrice.DIAMOND, 2500},
                    {ItemName.CAI_TRANG_HANG_NGA_20_10_2024, 1, TypePrice.DIAMOND, 2500},
                    {ItemName.CAI_TRANG_POC_HE_20_10_2024, 1, TypePrice.DIAMOND, 2500},
                    {ItemName.CAI_TRANG_PAN_NGUYEN_DAN_XANH_20_10_2024, 1, TypePrice.DIAMOND, 2500},
                    {ItemName.CAI_TRANG_PAN_NGUYEN_DAN_XANH_20_10_2024, 1, TypePrice.DIAMOND, 2500},
                    {ItemName.CAI_TRANG_PAN_NGUYEN_DAN_DO_20_10_2024, 1, TypePrice.DIAMOND, 2500},*/

            }
    };

    public ShopBunmaEquip(ShopType type) {
        super(type);
    }

    @Override
    public LinkedHashMap<String, List<ItemShop>> createShop(Player player) {
        LinkedHashMap<String, List<ItemShop>> itemShops = new LinkedHashMap<>();
        String[] tabs = new String[]{"Trang phục", "Trang sức", "Cải trang"};
        for (String tab : tabs) {
            itemShops.put(tab, new ArrayList<>());
        }
        for (int i = 0; i < items.length; i++) {
            for (int j = 0; j < items[i].length; j++) {
                ItemShop itemShop = new ItemShop((int) items[i][j][0], (int) items[i][j][1], (TypePrice) items[i][j][2], (int) items[i][j][3]);
                if (itemShop.template.id == ItemName.CAI_TRANG_APK_21_20_10_2024) {
                    itemShop.options.clear();
                    itemShop.options.add(new ItemOption(25, 40));
                    itemShop.options.add(new ItemOption(31, 40));
                    itemShop.options.add(new ItemOption(32, 40));
                    itemShop.options.add(new ItemOption(166, 10));
                    itemShop.options.add(new ItemOption(33, 2));
                    itemShop.options.add(new ItemOption(34, 2));
                    itemShop.options.add(new ItemOption(35, 2));
                }
                if (itemShop.template.id == ItemName.CAI_TRANG_BUNMA_BIKINI_20_10_2024) {
                    itemShop.options.clear();
                    itemShop.options.add(new ItemOption(25, 30));
                    itemShop.options.add(new ItemOption(31, 30));
                    itemShop.options.add(new ItemOption(32, 30));
                    itemShop.options.add(new ItemOption(106, 3));
                    itemShop.options.add(new ItemOption(33, 2));
                    itemShop.options.add(new ItemOption(34, 2));
                    itemShop.options.add(new ItemOption(35, 2));
                }
                if (itemShop.template.id == ItemName.CAI_TRANG_HANG_NGA_20_10_2024) {
                    itemShop.options.clear();
                    itemShop.options.add(new ItemOption(25, 30));
                    itemShop.options.add(new ItemOption(31, 30));
                    itemShop.options.add(new ItemOption(32, 30));
                    itemShop.options.add(new ItemOption(188, 5));
                    itemShop.options.add(new ItemOption(33, 2));
                    itemShop.options.add(new ItemOption(34, 2));
                    itemShop.options.add(new ItemOption(35, 2));
                }
                if (itemShop.template.id == ItemName.CAI_TRANG_POC_HE_20_10_2024) {
                    itemShop.options.clear();
                    itemShop.options.add(new ItemOption(25, 25));
                    itemShop.options.add(new ItemOption(31, 25));
                    itemShop.options.add(new ItemOption(32, 25));
                    itemShop.options.add(new ItemOption(1, 800));
                    itemShop.options.add(new ItemOption(2, 1500));
                    itemShop.options.add(new ItemOption(169, 3));
                    itemShop.options.add(new ItemOption(33, 2));
                    itemShop.options.add(new ItemOption(34, 2));
                    itemShop.options.add(new ItemOption(35, 2));
                    itemShop.options.add(new ItemOption(78, 30));
                }
                if (itemShop.template.id == ItemName.CAI_TRANG_PAN_NGUYEN_DAN_DO_20_10_2024
                        || itemShop.template.id == ItemName.CAI_TRANG_PAN_NGUYEN_DAN_XANH_20_10_2024) {
                    itemShop.options.clear();
                    itemShop.options.add(new ItemOption(25, 25));
                    itemShop.options.add(new ItemOption(31, 25));
                    itemShop.options.add(new ItemOption(32, 25));
                    itemShop.options.add(new ItemOption(1, 1000));
                    itemShop.options.add(new ItemOption(2, 1500));
                    itemShop.options.add(new ItemOption(186, 10));
                    itemShop.options.add(new ItemOption(85, 10));
                    itemShop.options.add(new ItemOption(33, 2));
                    itemShop.options.add(new ItemOption(34, 2));
                    itemShop.options.add(new ItemOption(35, 2));
                    itemShop.options.add(new ItemOption(78, 25));
                }
                itemShops.get(tabs[i]).add(itemShop);
            }
        }
        /*Object[][] items = new Object[][]{
                {ItemName.CAI_TRANG_APK_21_20_10_2024, 1, TypePrice.DIAMOND, 2500},
                {ItemName.CAI_TRANG_BUNMA_BIKINI_20_10_2024, 1, TypePrice.DIAMOND, 2500},
                {ItemName.CAI_TRANG_HANG_NGA_20_10_2024, 1, TypePrice.DIAMOND, 2500},
                {ItemName.CAI_TRANG_POC_HE_20_10_2024, 1, TypePrice.DIAMOND, 2500},
                {ItemName.CAI_TRANG_PAN_NGUYEN_DAN_XANH_20_10_2024, 1, TypePrice.DIAMOND, 2500},
                {ItemName.CAI_TRANG_PAN_NGUYEN_DAN_XANH_20_10_2024, 1, TypePrice.DIAMOND, 2500},
                {ItemName.CAI_TRANG_PAN_NGUYEN_DAN_DO_20_10_2024, 1, TypePrice.DIAMOND, 2500},
        };
        for (Object[] item : items) {
            ItemShop itemShop = new ItemShop((int) item[0], (int) item[1], (TypePrice) item[2], (int) item[3]);
            if (itemShop.template.id == ItemName.CAI_TRANG_APK_21_20_10_2024) {
                itemShop.options.clear();
                itemShop.options.add(new ItemOption(25, 40));
                itemShop.options.add(new ItemOption(31, 40));
                itemShop.options.add(new ItemOption(32, 40));
                itemShop.options.add(new ItemOption(166, 10));
                itemShop.options.add(new ItemOption(33, 2));
                itemShop.options.add(new ItemOption(34, 2));
                itemShop.options.add(new ItemOption(35, 2));
            }
            if (itemShop.template.id == ItemName.CAI_TRANG_BUNMA_BIKINI_20_10_2024) {
                itemShop.options.clear();
                itemShop.options.add(new ItemOption(25, 30));
                itemShop.options.add(new ItemOption(31, 30));
                itemShop.options.add(new ItemOption(32, 30));
                itemShop.options.add(new ItemOption(106, 3));
                itemShop.options.add(new ItemOption(33, 2));
                itemShop.options.add(new ItemOption(34, 2));
                itemShop.options.add(new ItemOption(35, 2));
            }
            if (itemShop.template.id == ItemName.CAI_TRANG_HANG_NGA_20_10_2024) {
                itemShop.options.clear();
                itemShop.options.add(new ItemOption(25, 30));
                itemShop.options.add(new ItemOption(31, 30));
                itemShop.options.add(new ItemOption(32, 30));
                itemShop.options.add(new ItemOption(188, 5));
                itemShop.options.add(new ItemOption(33, 2));
                itemShop.options.add(new ItemOption(34, 2));
                itemShop.options.add(new ItemOption(35, 2));
            }
            if (itemShop.template.id == ItemName.CAI_TRANG_POC_HE_20_10_2024) {
                itemShop.options.clear();
                itemShop.options.add(new ItemOption(25, 25));
                itemShop.options.add(new ItemOption(31, 25));
                itemShop.options.add(new ItemOption(32, 25));
                itemShop.options.add(new ItemOption(1, 800));
                itemShop.options.add(new ItemOption(2, 1500));
                itemShop.options.add(new ItemOption(169, 3));
                itemShop.options.add(new ItemOption(33, 2));
                itemShop.options.add(new ItemOption(34, 2));
                itemShop.options.add(new ItemOption(35, 2));
                itemShop.options.add(new ItemOption(78, 30));
            }
            if (itemShop.template.id == ItemName.CAI_TRANG_PAN_NGUYEN_DAN_DO_20_10_2024
                    || itemShop.template.id == ItemName.CAI_TRANG_PAN_NGUYEN_DAN_XANH_20_10_2024) {
                itemShop.options.clear();
                itemShop.options.add(new ItemOption(25, 25));
                itemShop.options.add(new ItemOption(31, 25));
                itemShop.options.add(new ItemOption(32, 25));
                itemShop.options.add(new ItemOption(1, 1000));
                itemShop.options.add(new ItemOption(2, 1500));
                itemShop.options.add(new ItemOption(186, 10));
                itemShop.options.add(new ItemOption(85, 10));
                itemShop.options.add(new ItemOption(33, 2));
                itemShop.options.add(new ItemOption(34, 2));
                itemShop.options.add(new ItemOption(35, 2));
                itemShop.options.add(new ItemOption(78, 25));
            }
            itemShops.get(tabs[2]).add(itemShop);
        }*/
        return itemShops;
    }


}
