package com.beemobi.rongthanonline.model;

import com.google.gson.annotations.SerializedName;

public class SkillPaint {

    public SkillPaintInfo[] info;
    @SerializedName("dx_fly")
    public int dxFly;

    @SerializedName("dy_fly")
    public int dyFly;
}
