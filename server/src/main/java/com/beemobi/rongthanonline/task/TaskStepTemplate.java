package com.beemobi.rongthanonline.task;

import com.beemobi.rongthanonline.data.TaskStepTemplateData;

public class TaskStepTemplate {
    public TaskStepType type;
    public String name;
    public int mapId;
    public int npcId;
    public int monsterId;
    public int bossId;
    public int itemId;
    public int param;
    public String description;

    public TaskStepTemplate(TaskStepTemplateData data) {
        type = data.type;
        name = data.name;
        mapId = data.mapId;
        npcId = data.npcId;
        monsterId = data.monsterId;
        bossId = data.bossId;
        itemId = data.itemId;
        param = data.param;
        description = data.description;
    }
}
