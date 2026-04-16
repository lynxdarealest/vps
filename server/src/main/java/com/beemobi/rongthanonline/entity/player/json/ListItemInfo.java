package com.beemobi.rongthanonline.entity.player.json;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class ListItemInfo {
    @SerializedName("size")
    public int size;

    @SerializedName("items")
    public ArrayList<ItemInfo> items;

}
