package com.beemobi.rongthanonline.entity.player.json;

import com.beemobi.rongthanonline.mission.MissionRecharge;
import com.google.gson.annotations.SerializedName;

public class MissionRechargeInfo {
    @SerializedName("id")
    public int id;

    @SerializedName("type")
    public int type;

    @SerializedName("param")
    public int param;

    public MissionRechargeInfo(MissionRecharge info) {
        this.id = info.template.id;
        this.type = info.type;
        this.param = info.param;
    }
}
