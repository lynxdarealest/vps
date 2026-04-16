package com.beemobi.rongthanonline.entity.player.json;

import com.google.gson.annotations.SerializedName;

public class ItemInfo {
    @SerializedName("id")
    public int id;

    @SerializedName("quantity")
    public int quantity;

    @SerializedName("is_lock")
    public boolean isLock;

    @SerializedName("options")
    public int[][] options;

}
