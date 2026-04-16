package com.beemobi.rongthanonline.achievement;

import com.beemobi.rongthanonline.data.AchievementTemplateData;

public class AchievementTemplate {
    public int id;
    public String name;
    public String description;
    public int maxParam;
    public int ruby;

    public AchievementTemplate(AchievementTemplateData data) {
        id = data.id;
        name = data.name;
        description = data.description;
        maxParam = data.maxParam;
        ruby = data.ruby;
    }
}
