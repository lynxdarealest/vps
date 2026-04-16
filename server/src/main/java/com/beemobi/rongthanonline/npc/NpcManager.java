package com.beemobi.rongthanonline.npc;

import com.beemobi.rongthanonline.data.NpcTemplateData;
import com.beemobi.rongthanonline.repository.GameRepository;

import java.util.HashMap;
import java.util.List;

public class NpcManager {
    private static NpcManager instance;
    public HashMap<Integer, NpcTemplate> npcTemplates;

    public static NpcManager getInstance() {
        if (instance == null) {
            instance = new NpcManager();
        }
        return instance;
    }

    public void init() {
        initNpcTemplates();
    }

    public void initNpcTemplates() {
        npcTemplates = new HashMap<>();
        List<NpcTemplateData> npcTemplateDataList = GameRepository.getInstance().npcTemplateData.findAll();
        for (NpcTemplateData data : npcTemplateDataList) {
            npcTemplates.put(data.id, new NpcTemplate(data));
        }
    }
}
