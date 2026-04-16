package com.beemobi.rongthanonline.skill;

import com.beemobi.rongthanonline.data.SkillTemplateData;
import com.beemobi.rongthanonline.util.Utils;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;

public class SkillTemplate {
    public int id;
    public String[] name;
    public String[] description;
    public int[] maxLevel;
    public int[] iconId;
    public boolean isProactive;
    public int[] levelRequire;
    public int[][] dx;
    public int[][] dy;
    public long[][] coolDown;
    public SkillTypeMana typeMana;
    public int[][] mana;
    public ArrayList<SkillOption> options;
    public SkillType type;
    public int[][] item;
    public int[] dayUpgrade;
    public int[] percentUpgrade;
    public int diamondUpgrade;
    public int[] pointUpgrade;

    public int optionRequire;

    public SkillTemplate(SkillTemplateData data) {
        id = data.id;
        name = Utils.gson.fromJson(data.name, new TypeToken<String[]>() {
        }.getType());
        description = Utils.gson.fromJson(data.description, new TypeToken<String[]>() {
        }.getType());
        maxLevel = Utils.gson.fromJson(data.maxLevel, new TypeToken<int[]>() {
        }.getType());
        iconId = Utils.gson.fromJson(data.iconId, new TypeToken<int[]>() {
        }.getType());
        isProactive = data.isProactive;
        levelRequire = Utils.gson.fromJson(data.levelRequire, new TypeToken<int[]>() {
        }.getType());
        dx = Utils.gson.fromJson(data.dx, new TypeToken<int[][]>() {
        }.getType());
        dy = Utils.gson.fromJson(data.dy, new TypeToken<int[][]>() {
        }.getType());
        coolDown = Utils.gson.fromJson(data.coolDown, new TypeToken<long[][]>() {
        }.getType());
        typeMana = data.typeMana;
        mana = Utils.gson.fromJson(data.mana, new TypeToken<int[][]>() {
        }.getType());
        type = data.type;
        item = Utils.gson.fromJson(data.item, new TypeToken<int[][]>() {
        }.getType());
        dayUpgrade = Utils.gson.fromJson(data.dayUpgrade, new TypeToken<int[]>() {
        }.getType());
        percentUpgrade = Utils.gson.fromJson(data.percentUpgrade, new TypeToken<int[]>() {
        }.getType());
        diamondUpgrade = data.diamondUpgrade;
        pointUpgrade = Utils.gson.fromJson(data.pointUpgrade, new TypeToken<int[]>() {
        }.getType());
        optionRequire = data.optionRequire;
        options = new ArrayList<>();
    }
}
