package com.beemobi.rongthanonline.achievement;

public class Achievement {
    public AchievementTemplate template;
    public int param;
    public boolean isReceived;

    public Achievement(int id) {
        template = AchievementManager.getInstance().achievements.get(id);
        param = 0;
        isReceived = false;
    }

    public Achievement(int[] data) {
        template = AchievementManager.getInstance().achievements.get(data[0]);
        param = data[1];
        isReceived = data[2] == 1;
    }
}
