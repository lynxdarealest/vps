package com.beemobi.rongthanonline.task;

import com.beemobi.rongthanonline.data.TaskStepTemplateData;
import com.beemobi.rongthanonline.data.TaskTemplateData;
import com.beemobi.rongthanonline.repository.GameRepository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TaskManager {
    private static TaskManager instance;
    public HashMap<Integer, TaskTemplate> taskTemplates;

    public static TaskManager getInstance() {
        if (instance == null) {
            instance = new TaskManager();
        }
        return instance;
    }

    public void init() {
        HashMap<Integer, ArrayList<TaskStepTemplate>> taskSubs = new HashMap<>();
        List<TaskStepTemplateData> taskStepTemplateDataList = GameRepository.getInstance().taskSubTemplateData.findAll();
        for (TaskStepTemplateData data : taskStepTemplateDataList) {
            if (!taskSubs.containsKey(data.taskId)) {
                taskSubs.put(data.taskId, new ArrayList<>());
            }
            taskSubs.get(data.taskId).add(new TaskStepTemplate(data));
        }

        taskTemplates = new HashMap<>();
        List<TaskTemplateData> taskTemplateDataList = GameRepository.getInstance().taskTemplateData.findAll();
        for (TaskTemplateData data : taskTemplateDataList) {
            ArrayList<TaskStepTemplate> taskStepTemplates = taskSubs.get(data.id);
            if (taskStepTemplates != null) {
                TaskTemplate taskTemplate = new TaskTemplate(data);
                taskTemplate.steps = taskStepTemplates.toArray(new TaskStepTemplate[taskStepTemplates.size()]);
                taskTemplates.put(data.id, taskTemplate);
            }
        }
    }

}
