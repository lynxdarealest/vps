package com.beemobi.rongthanonline.model;

import com.google.gson.annotations.SerializedName;

import java.util.HashMap;

public class Frame {
    public int type;
    @SerializedName("hp_bar")
    public int hpBar;
    public int chat;
    public int[] dead;
    public int[] stand;
    public int[] run;
    public int fly;
    public int jump;
    public int fall;
    public int injure;
    public HashMap<Integer, Integer> action;
    public int dx;
    public int dy;
    public int width;
    public int height;
}
