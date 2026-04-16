package com.beemobi.rongthanonline.lucky;

import com.beemobi.rongthanonline.item.Item;
import com.beemobi.rongthanonline.item.ItemManager;
import com.beemobi.rongthanonline.util.Utils;

public class ItemLuckyBox extends Item {
    public int minQuantity;
    public int maxQuantity;

    public ItemLuckyBox(int templateId) {
        template = ItemManager.getInstance().itemTemplates.get(templateId);
        minQuantity = 1;
    }

    public ItemLuckyBox(int templateId, int minQuantity, int maxQuantity) {
        template = ItemManager.getInstance().itemTemplates.get(templateId);
        this.minQuantity = minQuantity;
        this.maxQuantity = maxQuantity;
    }

    public Item next() {
        Item item = new Item();
        item.template = template;
        item.setDefaultOption();
        if (item.isItemBody()) {
            item.randomParam(-15, 15);
        }
        if (minQuantity >= maxQuantity) {
            item.quantity = minQuantity;
        } else {
            item.quantity = Utils.nextInt(minQuantity, maxQuantity);
        }
        item.isLock = isLock;
        return item;
    }

    public Item show() {
        Item item = new Item();
        item.template = template;
        item.setDefaultOption();
        if (item.isItemBody()) {
            item.randomParam(-15, 15);
        }
        if (minQuantity >= maxQuantity) {
            item.quantity = minQuantity;
        } else {
            item.quantity = Utils.nextInt(minQuantity, maxQuantity);
        }
        item.isLock = isLock;
        return item;
    }
}
