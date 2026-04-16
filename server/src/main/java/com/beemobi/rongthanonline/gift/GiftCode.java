package com.beemobi.rongthanonline.gift;

import com.beemobi.rongthanonline.data.GiftCodeData;
import com.beemobi.rongthanonline.data.GiftData;
import com.beemobi.rongthanonline.entity.player.json.FriendInfo;
import com.beemobi.rongthanonline.entity.player.json.ItemGiftInfo;
import com.beemobi.rongthanonline.item.Item;
import com.beemobi.rongthanonline.model.Friend;
import com.beemobi.rongthanonline.util.Utils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.json.JSONArray;
import org.json.JSONObject;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class GiftCode {
    public int id;
    public String code;
    public int levelRequire;
    public int taskRequire;
    public int activePointRequire;
    public Timestamp expiryTime;
    public Timestamp createTime;
    public List<Item> items;

    public GiftCode(GiftCodeData data) {
        id = data.id;
        code = data.code;
        levelRequire = data.levelRequire;
        taskRequire = data.taskRequire;
        activePointRequire = data.activePointRequire;
        expiryTime = data.expiryTime;
        createTime = data.createTime;
        items = new ArrayList<>();
        ArrayList<ItemGiftInfo> itemInfoList = new Gson().fromJson(data.items, new TypeToken<ArrayList<ItemGiftInfo>>() {
        }.getType());
        for (ItemGiftInfo info : itemInfoList) {
            items.add(new Item(info));
        }
    }

    public List<Item> getItems() {
        return items;
    }
}
