package com.beemobi.rongthanonline.achievement;

import com.beemobi.rongthanonline.data.AchievementTemplateData;
import com.beemobi.rongthanonline.effect.Effect;
import com.beemobi.rongthanonline.repository.GameRepository;
import org.apache.log4j.Logger;

import java.util.HashMap;
import java.util.List;

public class AchievementManager {
    private static final Logger logger = Logger.getLogger(AchievementManager.class);
    private static AchievementManager instance;
    public HashMap<Integer, AchievementTemplate> achievements;
    public static AchievementManager getInstance() {
        if (instance == null) {
            instance = new AchievementManager();
        }
        return instance;
    }

    public void init() {
        achievements = new HashMap<>();
        List<AchievementTemplateData> achievementTemplateDataList = GameRepository.getInstance().achievementTemplateData.findAll();
        for (AchievementTemplateData data : achievementTemplateDataList) {
            achievements.put(data.id, new AchievementTemplate(data));
        }
    }
}
