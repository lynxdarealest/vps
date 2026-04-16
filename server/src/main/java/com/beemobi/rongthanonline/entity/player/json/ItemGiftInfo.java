package com.beemobi.rongthanonline.entity.player.json;

import com.google.gson.annotations.SerializedName;

public class ItemGiftInfo {

    @SerializedName("id")
    public int id;

    @SerializedName("quantity")
    public int quantity;

    @SerializedName("expiry")
    public int expiry;

    @SerializedName("is_lock")
    public boolean isLock;

    @SerializedName("options")
    public int[][] options;

    @SerializedName("is_default")
    public boolean isDefault;

    @SerializedName("is_max")
    public boolean isMax;
}
