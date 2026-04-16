package com.beemobi.rongthanonline.item;

import com.beemobi.rongthanonline.data.ItemOptionTemplateData;

public class ItemOptionTemplate {
    public int id;

    public String name;

    public int type;

    public ItemOptionTemplate(ItemOptionTemplateData data) {
        id = data.id;
        name = data.name;
        type = data.type;
    }
}
