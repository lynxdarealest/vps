package com.beemobi.rongthanonline.item;

import com.beemobi.rongthanonline.data.ItemOptionData;

public class ItemOption {
    public ItemOptionTemplate template;
    public int param;

    public ItemOption(int id, int param) {
        template = ItemManager.getInstance().itemOptionTemplates.get(id);
        this.param = param;
    }

    public ItemOption(ItemOptionTemplate template, int param) {
        this.template = template;
        this.param = param;
    }


    public ItemOption(ItemOptionData data) {
        template = ItemManager.getInstance().itemOptionTemplates.get(data.optionId);
        param = data.param;
    }

    public boolean isCanRandomParam() {
        return template.id != 19 && template.id != 50 && template.id != 67 && template.id != 68 && template.id != 162;
    }

    @Override
    public String toString() {
        return template.name.replace("#", String.valueOf(param));
    }
}
