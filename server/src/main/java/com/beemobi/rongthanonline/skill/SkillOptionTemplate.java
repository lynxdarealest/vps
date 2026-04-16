package com.beemobi.rongthanonline.skill;

import com.beemobi.rongthanonline.data.SkillOptionTemplateData;

public class SkillOptionTemplate {
    public int id;

    public String name;

    public SkillOptionTemplate(SkillOptionTemplateData data) {
        id = data.id;
        name = data.name;
    }
}
