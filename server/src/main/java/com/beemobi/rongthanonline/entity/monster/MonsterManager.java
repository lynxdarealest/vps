package com.beemobi.rongthanonline.entity.monster;

import com.beemobi.rongthanonline.data.MonsterTemplateData;
import com.beemobi.rongthanonline.repository.GameRepository;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

public class MonsterManager {
    private static MonsterManager instance;

    public HashMap<Integer, MonsterTemplate> monsterTemplates;

    public static MonsterManager getInstance() {
        if (instance == null) {
            instance = new MonsterManager();
        }
        return instance;
    }

    public void init() {
        initMonsterTemplates();
    }

    public void initMonsterTemplates() {
        monsterTemplates = new LinkedHashMap<>();
        List<MonsterTemplateData> monsterTemplateDataList = GameRepository.getInstance().monsterTemplateData.findAll();
        for (MonsterTemplateData data : monsterTemplateDataList) {
            monsterTemplates.put(data.id, new MonsterTemplate(data));
        }
    }
}
