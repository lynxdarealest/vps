package com.beemobi.rongthanonline.mission;

import com.beemobi.rongthanonline.item.Item;

import java.util.List;

public class MissionWeek implements IMission {
    public MissionWeekTemplate template;
    public int type;

    public MissionWeek(int[] info) {
        template = MissionManager.getInstance().missionWeekTemplates.get(info[0]);
        type = info[1];
    }

    public MissionWeek(MissionWeekTemplate template) {
        this.template = template;
        type = 0;
    }

    @Override
    public int getTemplateId() {
        return template.id;
    }

    @Override
    public String getName() {
        return template.name;
    }

    @Override
    public String getDescription() {
        return template.description;
    }

    @Override
    public int getType() {
        return type;
    }

    @Override
    public void setType(int type) {
        this.type = type;
    }

    @Override
    public List<Item> getItems() {
        return template.items;
    }
}
