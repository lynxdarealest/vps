package com.beemobi.rongthanonline.task;

import com.beemobi.rongthanonline.data.TaskTemplateData;
import com.beemobi.rongthanonline.entity.player.json.ItemGiftInfo;
import com.beemobi.rongthanonline.item.Item;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

public class TaskTemplate {
    public int id;
    public String name;
    public int fromNpcId;
    public TaskStepTemplate[] steps;
    public List<Item> items;

    public TaskTemplate(TaskTemplateData data) {
        id = data.id;
        name = data.name;
        fromNpcId = data.fromNpcId;
        items = new ArrayList<>();
        ArrayList<ItemGiftInfo> itemInfoList = new Gson().fromJson(data.items, new TypeToken<ArrayList<ItemGiftInfo>>() {
        }.getType());
        for (ItemGiftInfo info : itemInfoList) {
            items.add(new Item(info));
        }
    }
}
