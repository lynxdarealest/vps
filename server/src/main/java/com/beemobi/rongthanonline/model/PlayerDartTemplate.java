package com.beemobi.rongthanonline.model;

import com.google.gson.annotations.SerializedName;

public class PlayerDartTemplate {
    public DartTemplateInfo bullet;

    public DartTemplateInfo explode;

    @SerializedName("is_target")
    public boolean isTarget;

    @SerializedName("is_line")
    public boolean isLine;
}
