package com.beemobi.rongthanonline.shop;

import com.beemobi.rongthanonline.entity.player.Player;
import com.beemobi.rongthanonline.event.Event;
import com.beemobi.rongthanonline.item.ItemShop;
import org.apache.log4j.Logger;

import java.util.LinkedHashMap;
import java.util.List;

public class ShopEvent extends Shop {
    private static final Logger logger = Logger.getLogger(ShopEvent.class);

    public ShopEvent(ShopType type) {
        super(type);
    }

    @Override
    public LinkedHashMap<String, List<ItemShop>> createShop(Player player) {
        LinkedHashMap<String, List<ItemShop>> itemShops = new LinkedHashMap<>();
        if (Event.isEvent()) {
            itemShops.put("Đổi điểm", Event.event.showShopExchangePoint(player));
        }
        return itemShops;
    }
}
