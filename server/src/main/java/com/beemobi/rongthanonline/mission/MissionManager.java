package com.beemobi.rongthanonline.mission;

import com.beemobi.rongthanonline.data.*;
import com.beemobi.rongthanonline.item.Item;
import com.beemobi.rongthanonline.repository.GameRepository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MissionManager {
    private static MissionManager instance;
    public HashMap<Integer, MissionWeekTemplate> missionWeekTemplates;
    public HashMap<Integer, MissionDailyTemplate> missionDailyTemplates;
    public HashMap<Integer, MissionRechargeTemplate> missionRechargeTemplates;
    public HashMap<Integer, MissionEventTemplate> missionEventTemplates;
    public HashMap<Integer, MissionItemWeeklyTemplate> missionItemWeeklyTemplates;
    public List<IMission> missionItemWeeklies;

    public MissionManager() {

    }

    public static MissionManager getInstance() {
        if (instance == null) {
            instance = new MissionManager();
        }
        return instance;
    }

    public void init() {
        missionWeekTemplates = new HashMap<>();
        List<MissionWeekTemplateData> missionWeekTemplateDataList = GameRepository.getInstance().missionWeekTemplateData.findAll();
        for (MissionWeekTemplateData data : missionWeekTemplateDataList) {
            missionWeekTemplates.put(data.id, new MissionWeekTemplate(data));
        }
        missionDailyTemplates = new HashMap<>();
        List<MissionDailyTemplateData> missionDailyTemplateDataList = GameRepository.getInstance().missionDailyTemplateData.findAll();
        for (MissionDailyTemplateData data : missionDailyTemplateDataList) {
            missionDailyTemplates.put(data.id, new MissionDailyTemplate(data));
        }
        missionRechargeTemplates = new HashMap<>();
        List<MissionRechargeTemplateData> missionRechargeTemplateDataList = GameRepository.getInstance().missionRechargeTemplateData.findAll();
        for (MissionRechargeTemplateData data : missionRechargeTemplateDataList) {
            missionRechargeTemplates.put(data.id, new MissionRechargeTemplate(data));
        }
        missionEventTemplates = new HashMap<>();
        List<MissionEventTemplateData> missionEventTemplateDataList = GameRepository.getInstance().missionEventTemplate.findAll();
        for (MissionEventTemplateData data : missionEventTemplateDataList) {
            missionEventTemplates.put(data.id, new MissionEventTemplate(data));
        }
        missionItemWeeklyTemplates = new HashMap<>();
        List<MissionItemWeeklyTemplateData> missionItemWeeklyTemplateDataList = GameRepository.getInstance().missionItemWeeklyTemplate.findAll();
        for (MissionItemWeeklyTemplateData data : missionItemWeeklyTemplateDataList) {
            missionItemWeeklyTemplates.put(data.id, new MissionItemWeeklyTemplate(data));
        }
        missionItemWeeklies = new ArrayList<>();
        for (MissionItemWeeklyTemplate template : missionItemWeeklyTemplates.values()) {
            missionItemWeeklies.add(new MissionItemWeekly(template));
        }
    }

    public List<Item> getItems(int type, int rank) {
        List<Item> items = new ArrayList<>();
        for (IMission mission : missionItemWeeklies) {
            MissionItemWeekly item = (MissionItemWeekly) mission;
            if (item.template.type == type && item.template.rank == rank) {
                items.addAll(item.getItems());
                break;
            }
        }
        return items;
    }
}
