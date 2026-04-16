package com.beemobi.rongthanonline.model;

import com.beemobi.rongthanonline.data.RewardData;
import com.beemobi.rongthanonline.entity.player.json.ItemGiftInfo;
import com.beemobi.rongthanonline.entity.player.json.ItemInfo;
import com.beemobi.rongthanonline.item.Item;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

public class Reward {
    public long id;
    public int playerId;
    public int server;
    public String info;
    public long expiryTime;
    public long createTime;
    public List<Item> items;

    public Reward(RewardData data) {
        id = data.id;
        info = data.info;
        server = data.server;
        expiryTime = data.expiryTime.getTime();
        items = new ArrayList<>();
        ArrayList<ItemGiftInfo> itemInfoList = new Gson().fromJson(data.items, new TypeToken<ArrayList<ItemGiftInfo>>() {
        }.getType());
        for (ItemGiftInfo info : itemInfoList) {
            this.items.add(new Item(info));
        }
    }

    public long getExpiry(long now) {
        return expiryTime - now;
    }

}
