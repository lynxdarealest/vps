package com.beemobi.rongthanonline.mission;

import com.beemobi.rongthanonline.data.MissionEventTemplateData;
import com.beemobi.rongthanonline.entity.player.json.ItemGiftInfo;
import com.beemobi.rongthanonline.entity.player.json.ItemInfo;
import com.beemobi.rongthanonline.item.Item;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

public class MissionEventTemplate {
    public int id;
    public String name;
    public String description;
    public List<Item> items;
    public int param;

    public MissionEventTemplate(MissionEventTemplateData data) {
        id = data.id;
        name = data.name;
        description = data.description;
        param = data.param;
        items = new ArrayList<>();
        ArrayList<ItemGiftInfo> itemInfoList = new Gson().fromJson(data.items, new TypeToken<ArrayList<ItemGiftInfo>>() {
        }.getType());
        for (ItemGiftInfo info : itemInfoList) {
            this.items.add(new Item(info));
        }
    }
}
