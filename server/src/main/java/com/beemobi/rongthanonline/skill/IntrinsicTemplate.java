package com.beemobi.rongthanonline.skill;

import com.beemobi.rongthanonline.data.IntrinsicTemplateData;

public class IntrinsicTemplate {
    public int id;
    public String name;
    public int iconId;
    public int min;
    public int max;
    public int gender;
    public int skillTemplateId;
    public boolean isUpgrade;
    public boolean isCoolDown;
    public int priceCoin;
    public int priceDiamond;

    public IntrinsicTemplate(IntrinsicTemplateData data) {
        id = data.id;
        name = data.name;
        iconId = data.icon;
        min = data.min;
        max = data.max;
        gender = data.gender;
        skillTemplateId = data.skillTemplateId;
        isUpgrade = data.isUpgrade;
        isCoolDown = data.isCoolDown;
        priceCoin = data.priceCoin;
        priceDiamond = data.priceDiamond;
    }

}
