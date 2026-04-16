package com.beemobi.rongthanonline.skill;

import com.beemobi.rongthanonline.data.IntrinsicTemplateData;
import com.beemobi.rongthanonline.data.SkillOptionData;
import com.beemobi.rongthanonline.data.SkillOptionTemplateData;
import com.beemobi.rongthanonline.data.SkillTemplateData;
import com.beemobi.rongthanonline.entity.player.PlayerGender;
import com.beemobi.rongthanonline.model.Intrinsic;
import com.beemobi.rongthanonline.repository.GameRepository;
import com.beemobi.rongthanonline.util.Utils;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class SkillManager {
    private static SkillManager instance;
    public HashMap<Integer, SkillOptionTemplate> skillOptionTemplates;
    public HashMap<Integer, SkillTemplate> skillTemplates;
    public ArrayList<Skill> skillsDiscipleNotOpen;
    public HashMap<Integer, IntrinsicTemplate> intrinsicTemplates;

    public static SkillManager getInstance() {
        if (instance == null) {
            instance = new SkillManager();
        }
        return instance;
    }

    public void init() {
        initSkillOptionTemplate();
        initSkillTemplate();
        initIntrinsicTemplate();
    }

    public List<IntrinsicTemplate> getIntrinsicTemplates(int gender) {
        return intrinsicTemplates.values().stream().filter(i -> i.gender == -1 || i.gender == gender).collect(Collectors.toList());
    }

    public List<Skill> createSkillsByGender(int gender) {
        List<Skill> skills = new ArrayList<>();
        int[] arrClass;
        switch (gender) {
            case PlayerGender.EARTH:
                arrClass = new int[]{0, 3, 6, 9, 12, 15, 30, 31, 32, 33, 36};
                break;
            case PlayerGender.NAMEK:
                arrClass = new int[]{1, 4, 7, 10, 13, 16, 30, 31, 32, 34, 36};
                break;
            case PlayerGender.SAYAIN:
                arrClass = new int[]{2, 5, 8, 11, 14, 17, 30, 31, 32, 35, 36};
                break;
            default:
                arrClass = new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28};
                break;
        }
        for (int templateId : arrClass) {
            Skill skill = new Skill();
            skill.template = skillTemplates.get(templateId);
            skill.level = 0;
            skill.point = 0;
            skill.upgrade = 0;
            skills.add(skill);
        }
        return skills;
    }

    public Skill createSkill(int templateId, int level) {
        Skill skill = new Skill();
        skill.template = skillTemplates.get(templateId);
        skill.level = level;
        return skill;
    }

    public Skill createSkill(int templateId, int level, int upgrade) {
        Skill skill = new Skill();
        skill.template = skillTemplates.get(templateId);
        skill.level = level;
        skill.upgrade = upgrade;
        return skill;
    }

    public void initSkillOptionTemplate() {
        skillOptionTemplates = new HashMap<>();
        List<SkillOptionTemplateData> skillOptionTemplateDataList = GameRepository.getInstance().skillOptionTemplateData.findAll();
        for (SkillOptionTemplateData data : skillOptionTemplateDataList) {
            skillOptionTemplates.put(data.id, new SkillOptionTemplate(data));
        }
    }

    public void initIntrinsicTemplate() {
        intrinsicTemplates = new HashMap<>();
        List<IntrinsicTemplateData> dataList = GameRepository.getInstance().intrinsic.findAll();
        for (IntrinsicTemplateData data : dataList) {
            intrinsicTemplates.put(data.id, new IntrinsicTemplate(data));
        }
    }

    public void initSkillTemplate() {
        skillTemplates = new HashMap<>();
        List<SkillTemplateData> skillTemplateDataList = GameRepository.getInstance().skillTemplateData.findAll();
        for (SkillTemplateData data : skillTemplateDataList) {
            skillTemplates.put(data.id, new SkillTemplate(data));
        }
        List<SkillOptionData> skillOptionDataList = GameRepository.getInstance().skillOptionData.findAll();
        for (SkillOptionData data : skillOptionDataList) {
            SkillTemplate skillTemplate = skillTemplates.get(data.templateId);
            if (skillTemplate != null) {
                SkillOption option = new SkillOption();
                option.template = skillOptionTemplates.get(data.optionId);
                option.params = Utils.gson.fromJson(data.paramNormal, new TypeToken<int[]>() {
                }.getType());
                option.upgrades = Utils.gson.fromJson(data.paramUpgrade, new TypeToken<int[]>() {
                }.getType());
                skillTemplate.options.add(option);
            }
        }
        skillsDiscipleNotOpen = new ArrayList<>();
        for (int i = 27; i < 30; i++) {
            Skill skill = new Skill();
            skill.template = skillTemplates.get(i);
            skill.level = 0;
            skill.upgrade = 0;
            skillsDiscipleNotOpen.add(skill);
        }
        /*try {
            SkillTemplateData template = skillTemplateDataList.get(18).clone();
            template.id = 50;
            template.name = "[\"Chiêu ?\"]";
            template.description = "[\"Cần cấp độ 30 để mở\"]";
            template.iconId = "[774]";
            GameRepository.getInstance().skillTemplateData.save(template);
        }
        catch (Exception ex) {

        }*/

    }


}
