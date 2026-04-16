package com.beemobi.rongthanonline.model;

import com.google.gson.annotations.SerializedName;

public class MonsterDartTemplate {
    public int id;

    public DartTemplateInfo light;

    public DartTemplateInfo bullet;

    public DartTemplateInfo explode;

    @SerializedName("is_meteorite")
    public boolean isMeteorite;
}
