package com.beemobi.rongthanonline.skill;

public class SkillOption {
    public SkillOptionTemplate template;

    public int[] params;

    public int[] upgrades;

    public int getParam(int level, int upgrade) {
        if (level < 0) {
            return 0;
        }
        if (upgrade > 0) {
            int index = upgrade - 1;
            if (index >= upgrades.length) {
                return upgrades[upgrades.length - 1];
            }
            return upgrades[index];
        }
        int index = level - 1;
        if (index >= params.length) {
            return params[params.length - 1];
        }
        return params[index];
    }
}
