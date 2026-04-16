package com.beemobi.rongthanonline.entity.player.json;

import com.beemobi.rongthanonline.model.MagicBean;
import com.google.gson.annotations.SerializedName;

public class MagicBeanInfo {
    @SerializedName("level")
    public int level;

    @SerializedName("update_time")
    public long updateTime;

    @SerializedName("is_update")
    public boolean isUpdate;

    public MagicBeanInfo(MagicBean magicBean) {
        level = magicBean.level;
        updateTime = magicBean.updateTime;
        isUpdate = magicBean.isUpdate;
    }

    public MagicBeanInfo() {

    }
}
