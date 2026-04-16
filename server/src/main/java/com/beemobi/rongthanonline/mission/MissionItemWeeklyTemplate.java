package com.beemobi.rongthanonline.mission;

import com.beemobi.rongthanonline.data.MissionItemWeeklyTemplateData;
import com.beemobi.rongthanonline.entity.player.json.ItemGiftInfo;
import com.beemobi.rongthanonline.entity.player.json.ItemInfo;
import com.beemobi.rongthanonline.item.Item;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

public class MissionItemWeeklyTemplate {
    public int id;
    public String name;
    public String description;
    public int rank;
    public int type;
    public List<Item> items;

    public MissionItemWeeklyTemplate(MissionItemWeeklyTemplateData data) {
        id = data.id;
        name = data.name;
        description = data.description;
        rank = data.rank;
        type = data.type;
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
