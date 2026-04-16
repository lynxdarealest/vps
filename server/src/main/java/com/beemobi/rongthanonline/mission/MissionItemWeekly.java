package com.beemobi.rongthanonline.mission;

import com.beemobi.rongthanonline.item.Item;

import java.util.List;

public class MissionItemWeekly implements IMission {
    public MissionItemWeeklyTemplate template;

    public MissionItemWeekly(int[] info) {
        template = MissionManager.getInstance().missionItemWeeklyTemplates.get(info[0]);
    }

    public MissionItemWeekly(MissionItemWeeklyTemplate template) {
        this.template = template;
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
        return MissionType.DA_XONG;
    }

    @Override
    public void setType(int type) {

    }

    @Override
    public List<Item> getItems() {
        return template.items;
    }
}
