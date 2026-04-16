package com.beemobi.rongthanonline.shop;

import com.beemobi.rongthanonline.entity.player.Player;
import com.beemobi.rongthanonline.item.ItemShop;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

public class ShopRepurchase extends Shop {
    private static final Logger logger = Logger.getLogger(ShopRepurchase.class);

    public ShopRepurchase(ShopType type) {
        super(type);
    }

    @Override
    public LinkedHashMap<String, List<ItemShop>> createShop(Player player) {
        LinkedHashMap<String, List<ItemShop>> itemShops = new LinkedHashMap<>();
        String[] tabs = new String[]{"Đã bán"};
        for (String tab : tabs) {
            itemShops.put(tab, new ArrayList<>());
        }
        if (ShopManager.getInstance().players.containsKey(player.id)) {
            itemShops.get(tabs[0]).addAll(ShopManager.getInstance().players.get(player.id));
        }
        return itemShops;
    }

}
