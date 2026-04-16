package com.beemobi.rongthanonline.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class SkillPaintInfo {
    @SerializedName("sound_id")
    public int soundId;

    @SerializedName("dart_id")
    public int dartId;

    public int[] action;

    @SerializedName("effect")
    public ArrayList<SkillEffect> effects;

    @SerializedName("time_out")
    public int timeOut;
}
