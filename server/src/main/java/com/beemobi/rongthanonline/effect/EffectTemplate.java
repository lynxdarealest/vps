package com.beemobi.rongthanonline.effect;

import com.beemobi.rongthanonline.data.EffectTemplateData;

public class EffectTemplate {
    public int id;
    public String name;
    public int iconId;
    public EffectImage effectImage;
    public boolean isActiveWhenOnline;
    public boolean isClearWhenDie;
    public boolean isMe;
    public boolean isStun;
    public boolean isSave;
    public boolean isRefreshInfo;
    public boolean isRefreshPart;

    public EffectTemplate(EffectTemplateData data) {
        id = data.id;
        name = data.name;
        iconId = data.iconId;
        isActiveWhenOnline = data.isActiveWhenOnline;
        isClearWhenDie = data.isClearWhenDie;
        isMe = data.isMe;
        isSave = data.isSave;
        isStun = data.isStun;
        isRefreshInfo = data.isRefreshInfo;
        isRefreshPart = data.isRefreshPart;
        if (data.effectImageId != -1) {
            effectImage = EffectManager.getInstance().effectImages.get(data.effectImageId);
        }
    }
}
