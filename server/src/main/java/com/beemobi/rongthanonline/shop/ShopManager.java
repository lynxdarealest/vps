package com.beemobi.rongthanonline.shop;

import com.beemobi.rongthanonline.common.KeyValue;
import com.beemobi.rongthanonline.entity.player.Player;
import com.beemobi.rongthanonline.event.Event;
import com.beemobi.rongthanonline.item.*;
import com.beemobi.rongthanonline.upgrade.EquipCrafting3x;
import com.beemobi.rongthanonline.upgrade.EquipCrafting4x;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.HashMap;

public class ShopManager {
    private static final Logger logger = Logger.getLogger(ShopManager.class);
    private static ShopManager instance;
    public HashMap<ShopType, Shop> shops;
    public HashMap<Integer, Integer> prices;
    public HashMap<Integer, ArrayList<ItemShop>> players;
    public ShopConsignment consignment;

    public static ShopManager getInstance() {
        if (instance == null) {
            instance = new ShopManager();
        }
        return instance;
    }

    public void init() {
        players = new HashMap<>();
        shops = new HashMap<>();
        shops.put(ShopType.SHOP_BUNMA_EQUIP, new ShopBunmaEquip(ShopType.SHOP_BUNMA_EQUIP));
        shops.put(ShopType.SHOP_BUNMA_OTHER, new ShopBunmaOther(ShopType.SHOP_BUNMA_OTHER));
        shops.put(ShopType.SHOP_AMULET_OTHER, new ShopAmuletOther(ShopType.SHOP_AMULET_OTHER));
        shops.put(ShopType.SHOP_AMULET_TIME, new ShopAmuletTime(ShopType.SHOP_AMULET_TIME));
        shops.put(ShopType.SHOP_CLAN, new ShopClan(ShopType.SHOP_CLAN));
        shops.put(ShopType.SHOP_BARRACK, new ShopBarrack(ShopType.SHOP_BARRACK));
        shops.put(ShopType.SHOP_REPURCHASE, new ShopRepurchase(ShopType.SHOP_REPURCHASE));
        shops.put(ShopType.SHOP_ACTIVE, new ShopActive(ShopType.SHOP_ACTIVE));
        shops.put(ShopType.SHOP_YARDRAT, new ShopYardrat(ShopType.SHOP_YARDRAT));
        shops.put(ShopType.SHOP_RIDER, new ShopRider(ShopType.SHOP_RIDER));
        shops.put(ShopType.SHOP_SPACESHIP, new ShopSpaceship(ShopType.SHOP_SPACESHIP));
        shops.put(ShopType.SHOP_FLAG_WAR, new ShopFlagWar(ShopType.SHOP_FLAG_WAR));
        shops.put(ShopType.SHOP_BLACK_FRIDAY, new ShopBlackFriday(ShopType.SHOP_BLACK_FRIDAY));
        if (Event.isEvent()) {
            shops.put(ShopType.SHOP_EVENT, new ShopEvent(ShopType.SHOP_EVENT));
        }
        initPrice();
        consignment = new ShopConsignment(ShopType.SHOP_CONSIGNMENT);
        consignment.init();
    }

    public void initPrice() {
        prices = new HashMap<>();
        for (int i = 0; i < ShopBunmaEquip.items.length; i++) {
            for (int j = 0; j < ShopBunmaEquip.items[i].length; j++) {
                int itemId = (int) ShopBunmaEquip.items[i][j][0];
                if (!prices.containsKey(itemId)) {
                    TypePrice typePrice = (TypePrice) ShopBunmaEquip.items[i][j][2];
                    if (typePrice == TypePrice.COIN) {
                        prices.put(itemId, (int) ShopBunmaEquip.items[i][j][3]);
                    }
                }
            }
        }
        for (int i = 0; i < ShopClan.items.length; i++) {
            for (int j = 0; j < ShopClan.items[i].length; j++) {
                int itemId = (int) ShopClan.items[i][j][0];
                if (!prices.containsKey(itemId)) {
                    TypePrice typePrice = (TypePrice) ShopClan.items[i][j][2];
                    if (typePrice == TypePrice.COIN) {
                        prices.put(itemId, (int) ShopClan.items[i][j][3]);
                    }
                }
            }
        }
        for (KeyValue<Integer, Integer[]> keyValue : EquipCrafting3x.ITEM_CRAFTING.values()) {
            for (int itemId : keyValue.value) {
                if (!prices.containsKey(itemId)) {
                    prices.put(itemId, keyValue.key);
                }
            }
        }
        for (KeyValue<Integer, Integer[]> keyValue : EquipCrafting4x.ITEM_CRAFTING.values()) {
            for (int itemId : keyValue.value) {
                if (!prices.containsKey(itemId)) {
                    prices.put(itemId, keyValue.key);
                }
            }
        }
    }

}
