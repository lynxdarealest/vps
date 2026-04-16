package com.beemobi.rongthanonline.effect;

import com.beemobi.rongthanonline.data.EffectImageData;
import com.beemobi.rongthanonline.data.EffectTemplateData;
import com.beemobi.rongthanonline.repository.GameRepository;
import com.beemobi.rongthanonline.server.Server;
import org.apache.log4j.Logger;

import java.util.HashMap;
import java.util.List;

public class EffectManager {
    private static final Logger logger = Logger.getLogger(Server.class);
    private static EffectManager instance;
    public HashMap<Integer, EffectTemplate> effectTemplates;
    public HashMap<Integer, EffectImage> effectImages;

    public static EffectManager getInstance() {
        if (instance == null) {
            instance = new EffectManager();
        }
        return instance;
    }

    public void init() {
        effectImages = new HashMap<>();
        List<EffectImageData> effectImageDataList = GameRepository.getInstance().effectImageData.findAll();
        for (EffectImageData data : effectImageDataList) {
            effectImages.put(data.id, new EffectImage(data));
        }
        effectTemplates = new HashMap<>();
        List<EffectTemplateData> effectTemplateDataList = GameRepository.getInstance().effectTemplateData.findAll();
        for (EffectTemplateData data : effectTemplateDataList) {
            effectTemplates.put(data.id, new EffectTemplate(data));
        }
    }
}
