package com.beemobi.rongthanonline.mission;

import com.beemobi.rongthanonline.item.Item;

import java.util.List;

public class MissionDaily implements IMission {
    public MissionDailyTemplate template;
    public int param;
    public int type;

    public MissionDaily(int[] info) {
        template = MissionManager.getInstance().missionDailyTemplates.get(info[0]);
        type = info[1];
        param = info[2];
    }

    public MissionDaily(MissionDailyTemplate template) {
        this.template = template;
        type = 0;
        param = 0;
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
        return String.format("%s [%d/%d]", template.description, param, template.param);
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
