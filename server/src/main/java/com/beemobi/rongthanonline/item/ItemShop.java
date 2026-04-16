package com.beemobi.rongthanonline.item;

import com.beemobi.rongthanonline.shop.TypePrice;

public class ItemShop extends Item {
    public int id;
    public TypePrice typePrice;
    public int price;
    public boolean isSaleCard;
    public boolean isRepurchase;

    public ItemShop() {

    }

    public ItemShop(int templateId, int quantity, TypePrice typePrice, int price) {
        template = ItemManager.getInstance().itemTemplates.get(templateId);
        this.quantity = quantity;
        this.typePrice = typePrice;
        this.price = price;
        setDefaultOption();
    }

    public ItemShop(ItemTemplate template, int quantity, TypePrice typePrice, int price) {
        this.template = template;
        this.quantity = quantity;
        this.typePrice = typePrice;
        this.price = price;
        setDefaultOption();
    }

    public ItemShop(ItemTemplate template, TypePrice typePrice, int price) {
        this.template = template;
        this.quantity = 1;
        this.typePrice = typePrice;
        this.price = price;
        setDefaultOption();
    }
}
