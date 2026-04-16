package com.beemobi.rongthanonline.mission;

import com.beemobi.rongthanonline.data.MissionDailyTemplateData;
import com.beemobi.rongthanonline.entity.player.json.ItemInfo;
import com.beemobi.rongthanonline.item.Item;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

public class MissionDailyTemplate {
    public int id;
    public String name;
    public String description;
    public List<Item> items;
    public int param;

    public MissionDailyTemplate(MissionDailyTemplateData data) {
        id = data.id;
        name = data.name;
        description = data.description;
        param = data.param;
        items = new ArrayList<>();
        ArrayList<ItemInfo> itemInfoList = new Gson().fromJson(data.items, new TypeToken<ArrayList<ItemInfo>>() {
        }.getType());
        for (ItemInfo info : itemInfoList) {
            Item item = new Item(info);
            item.setDefaultOption();
            this.items.add(item);
        }
    }
}
