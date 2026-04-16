package com.beemobi.rongthanonline.item;

import com.beemobi.rongthanonline.data.ItemTemplateData;

public class ItemTemplate {
    public int id;
    public int type;
    public int gender;
    public String name;
    public String description;
    public int iconId;
    public int head;
    public int body;
    public int headDisciple;
    public int bodyDisciple;
    public int medal;
    public int mount;
    public int bag;
    public int aura;
    public boolean isUpToUp;
    public int levelRequire;
    public boolean isLock;
    public int maxQuantity;
    public boolean isMaster;
    public boolean isDisciple;
    public boolean isPet;

    public ItemTemplate(ItemTemplateData data) {
        id = data.id;
        type = data.type;
        gender = data.gender;
        name = data.name;
        description = data.description;
        iconId = data.iconId;
        head = data.head;
        body = data.body;
        headDisciple = data.headDisciple;
        bodyDisciple = data.bodyDisciple;
        medal = data.medal;
        mount = data.mount;
        aura = data.aura;
        bag = data.bag;
        isUpToUp = data.isUp;
        levelRequire = data.levelRequire;
        isLock = data.isLock;
        maxQuantity = data.maxQuantity;
        isMaster = data.isMaster;
        isDisciple = data.isDisciple;
        isPet = data.isPet;
    }

    public boolean isItemAo() {
        return type == ItemType.TYPE_AO;
    }

    public boolean isItemGang() {
        return type == ItemType.TYPE_GANG;
    }

    public boolean isItemQuan() {
        return type == ItemType.TYPE_QUAN;
    }

    public boolean isItemGiay() {
        return type == ItemType.TYPE_GIAY;
    }

    public boolean isItemBoi() {
        return type == ItemType.TYPE_BOI;
    }

    public boolean isItemNhan() {
        return type == ItemType.TYPE_NHAN;
    }

    public boolean isItemDayChuyen() {
        return type == ItemType.TYPE_DAY_CHUYEN;
    }

    public boolean isItemRadar() {
        return type == ItemType.TYPE_RADAR;
    }

    public boolean isItemMount() {
        return type == ItemType.TYPE_THU_CUOI;
    }

    public boolean isItemAvatar() {
        return type == ItemType.TYPE_AVATAR;
    }

    public boolean isItemEvent() {
        return type == ItemType.TYPE_EVENT;
    }

    public boolean isAvatarLegendary() {
        return id == ItemName.AVATAR_LEGENDARY_BROLY
                || id == ItemName.AVATAR_BEAST_GOHAN
                || id == ItemName.AVATAR_SUPER_DENDE
                || id == ItemName.AVATAR_TRUNK_SUPER_BLUE
                || id == ItemName.AVATAR_PICOLO_SUPER_METAL
                || id == ItemName.AVATAR_GOKU_SUPER_GOD
                || id == ItemName.CAI_TRANG_SUPER_EVIL_XICOR
                || id == ItemName.CAI_TRANG_PICOLO_SUPER_GOLD
                || id == ItemName.CAI_TRANG_GOKU_SUPER_ULTRA
                || id == ItemName.CAI_TRANG_MI_NUONG
                || id == ItemName.AVATAR_SUPER_GOHAN_PURPLE
                || id == ItemName.AVATAR_SUPER_NAMEK_DRAGON
                || id == ItemName.AVATAR_BLACK_GOKU_ROSE
                || id == ItemName.CAI_TRANG_DRABURA
                || id == ItemName.CAI_TRANG_YACON
                || id == ItemName.CAI_TRANG_PUI_PUI
                || id == ItemName.CAI_TRANG_MABU
                || id == ItemName.GOKU_HIEP_SI_RONG
                || id == ItemName.HUY_HIEU_RONG_THAN
                || id == ItemName.CAI_TRANG_BROLY_HUYEN_THOAI
                || id == ItemName.CAI_TRANG_CUMBER_SUPER_BLUE;
    }

    public boolean isAvatarTet() {
        return id == ItemName.CAI_TRANG_MEO_CUTE
                || id == ItemName.AVATAR_TET_GOHAN
                || id == ItemName.AVATAR_TET_KRILIN
                || id == ItemName.AVATAR_TET_DENDE
                || id == ItemName.AVATAR_TET_PICOLO
                || id == ItemName.AVATAR_TET_VEGETA
                || id == ItemName.AVATAR_TET_GOKU;
    }

    public boolean isAvatarNoel() {
        return id == ItemName.CAI_TRANG_ONG_GIA_NOEL
                || id == ItemName.AVATAR_NOEL_GOHAN
                || id == ItemName.AVATAR_NOEL_KRILIN
                || id == ItemName.AVATAR_NOEL_DENDE
                || id == ItemName.AVATAR_NOEL_PICOLO
                || id == ItemName.AVATAR_NOEL_VEGETA
                || id == ItemName.AVATAR_NOEL_GOKU;
    }

    public boolean isAvatarEvent() {
        return isAvatarNoel() || isAvatarTet();
    }

    public boolean isItemBean() {
        return type == ItemType.TYPE_BEAN;
    }

    public boolean isItemStone() {
        return type == ItemType.TYPE_STONE;
    }
}
