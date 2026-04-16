package com.beemobi.rongthanonline.item;

import com.beemobi.rongthanonline.common.RandomCollection;
import com.beemobi.rongthanonline.effect.EffectName;
import com.beemobi.rongthanonline.entity.monster.pet.Pet;
import com.beemobi.rongthanonline.entity.player.json.ItemGiftInfo;
import com.beemobi.rongthanonline.entity.player.json.ItemInfo;
import com.beemobi.rongthanonline.entity.player.json.ItemOptionInfo;
import com.beemobi.rongthanonline.network.Message;
import com.beemobi.rongthanonline.upgrade.Polish;
import com.beemobi.rongthanonline.upgrade.UpgradeItem;
import com.beemobi.rongthanonline.util.Utils;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

public class Item {
    public ItemTemplate template;
    public ArrayList<ItemOption> options = new ArrayList<>();
    public int quantity;
    public boolean isLock;
    public transient int indexUI;

    public Item() {
    }

    public Item(ItemInfo info) {
        template = ItemManager.getInstance().itemTemplates.get(info.id);
        quantity = Math.max(info.quantity, 1);
        if (!template.isUpToUp && quantity > 1) {
            quantity = 1;
        }
        isLock = info.isLock;
        if (info.options != null) {
            for (int[] option : info.options) {
                if (option[0] == 72 || option[0] == 73 || option[0] == 79 || option[0] == 105) {
                    option[1] = 1;
                }
                ItemOption itemOption = new ItemOption(option[0], option[1]);
                if (itemOption.template.type == 4 && template.type == ItemType.TYPE_PET) {
                    continue;
                }
                options.add(itemOption);
            }
        }
    }

    public Item(ItemGiftInfo info) {
        template = ItemManager.getInstance().itemTemplates.get(info.id);
        quantity = Math.max(info.quantity, 1);
        if (!template.isUpToUp && quantity > 1) {
            quantity = 1;
        }
        isLock = info.isLock;
        if (info.isDefault) {
            setDefaultOption();
        }
        if (info.expiry > 0) {
            setExpiry(info.expiry);
        }
        if (info.options != null) {
            for (int[] option : info.options) {
                ItemOption itemOption = options.stream().filter(o -> o.template.id == option[0]).findFirst().orElse(null);
                if (itemOption == null) {
                    options.add(new ItemOption(option[0], option[1]));
                } else {
                    itemOption.param = option[1];
                }
            }
        }
        if (info.isMax) {
            randomParam(15, 15);
        }
    }

    public Item cloneItem() {
        Item item = new Item();
        item.template = template;
        item.quantity = quantity;
        item.indexUI = indexUI;
        item.isLock = isLock;
        for (ItemOption option : options) {
            item.options.add(new ItemOption(option.template.id, option.param));
        }
        return item;
    }

    public ItemMap cloneItemMap() {
        ItemMap item = new ItemMap();
        item.template = template;
        item.quantity = quantity;
        item.indexUI = indexUI;
        item.isLock = isLock;
        for (ItemOption option : options) {
            item.options.add(new ItemOption(option.template.id, option.param));
        }
        return item;
    }

    public Item cloneParam(int minPercent, int maxPercent) {
        Item item = cloneItem();
        item.randomParam(minPercent, maxPercent);
        return item;
    }

    public int getIndexBody() {
        int type = template.type;
        if (type == ItemType.TYPE_BAG) {
            return 14;
        }
        if (type == ItemType.TYPE_PET) {
            return 15;
        }
        if (type == ItemType.TYPE_AURA) {
            return 16;
        }
        return type;
    }

    public void randomParam(int minPercent, int maxPercent) {
        for (ItemOption option : options) {
            if (option.isCanRandomParam()) {
                int percent = Utils.nextInt(minPercent, maxPercent);
                option.param = Math.round(((float) option.param) / 100 * (float) (percent + 100));
                if (option.param < 1) {
                    option.param = 1;
                }
            }
        }
    }

    public void setMaxParam() {
        for (ItemOption option : options) {
            if (option.isCanRandomParam()) {
                int percent = 15;
                option.param = Math.round(((float) option.param) / 100 * (float) (percent + 100));
                if (option.param < 1) {
                    option.param = 1;
                }
            }
        }
    }

    public int getExpiry() {
        ItemOption option = this.getOption(50);
        if (option != null) {
            return option.param;
        }
        return -1;
    }

    public boolean isItemBody() {
        return template.type < 14 || template.type == ItemType.TYPE_BAG || template.type == ItemType.TYPE_PET || template.type == ItemType.TYPE_AURA;
    }

    public boolean isItemRider() {
        return template.id >= 522 && template.id <= 545;
    }

    public boolean isSkin() {
        return template.type == ItemType.TYPE_AO || template.type == ItemType.TYPE_AVATAR
                || template.type == ItemType.TYPE_THU_CUOI || template.type == ItemType.TYPE_BAG;
    }

    public int getIndexPet() {
        if (template.type != ItemType.TYPE_BODY_PET) {
            return -1;
        }
        if (template.id == ItemName.VUOT_LONG_THAN) {
            return 0;
        }
        if (template.id == ItemName.GIAP_LONG_BAO) {
            return 1;
        }
        if (template.id == ItemName.MU_VUONG_LONG) {
            return 2;
        }
        if (template.id == ItemName.GONG_THIEN_LONG) {
            return 3;
        }
        if (template.id == ItemName.LINH_HON_LONG_THE) {
            return 4;
        }
        if (template.id == ItemName.YEN_THAN_LONG) {
            return 5;
        }
        if (template.id == ItemName.DAY_CUONG_THAN_THU) {
            return 6;
        }
        if (template.id == ItemName.GIAP_VAI_LONG_THAN) {
            return 7;
        }
        return -1;
    }

    public int getMaxQuantity() {
        return template.maxQuantity;
    }

    public int getUpgrade() {
        for (ItemOption i : this.options) {
            if (i.template.id == 19) {
                return i.param;
            }
        }
        return 0;
    }

    public int getStarUse() {
        for (ItemOption i : this.options) {
            if (i.template.id == 68) {
                return i.param;
            }
        }
        return 0;
    }

    public int getMaxStar() {
        for (ItemOption i : this.options) {
            if (i.template.id == 67) {
                return i.param;
            }
        }
        return 0;
    }

    public ItemOption getOption(int optionId) {
        for (ItemOption itemOption : options) {
            if (itemOption.template.id == optionId) {
                return itemOption;
            }
        }
        return null;
    }

    public void removeOption(int optionId) {
        for (int i = 0; i < this.options.size(); i++) {
            if (options.get(i).template.id == optionId) {
                this.options.remove(i);
                return;
            }
        }
    }

    public void removeOptionLegend() {
        for (int i = 0; i < this.options.size(); i++) {
            int id = options.get(i).template.id;
            if (id == 33 || id == 34 || id == 35 || id == 78 || id == 111) {
                this.options.remove(i);
                i--;
            }
        }
    }

    public int getParam(int optionId) {
        for (ItemOption itemOption : options) {
            if (itemOption.template.id == optionId) {
                return itemOption.param;
            }
        }
        return 0;
    }

    public boolean isExpiry() {
        return getOption(50) != null;
    }

    public void setExpiry(int day) {
        if (day == 0) {
            return;
        }
        ItemOption option = getOption(50);
        if (day < 0) {
            if (option != null) {
                this.options.remove(option);
            }
            return;
        }
        if (option == null) {
            options.add(new ItemOption(50, day));
        } else {
            option.param = day;
        }
    }

    public void write(Message message) throws IOException {
        message.writer().writeShort(template.id);
        message.writer().writeInt(quantity);
        message.writer().writeBoolean(isLock);
        List<ItemOption> options = getOptionDisplays();
        message.writer().writeByte(options.size());
        for (ItemOption itemOption : options) {
            message.writer().writeShort(itemOption.template.id);
            message.writer().writeInt(itemOption.param);
        }
    }

    public void write(Message message, int quantity) throws IOException {
        message.writer().writeShort(template.id);
        message.writer().writeInt(quantity);
        message.writer().writeBoolean(isLock);
        List<ItemOption> options = getOptionDisplays();
        message.writer().writeByte(options.size());
        for (ItemOption itemOption : options) {
            message.writer().writeShort(itemOption.template.id);
            message.writer().writeInt(itemOption.param);
        }
    }

    public boolean isCanTrade() {
        if (getOption(113) != null) {
            return true;
        }
        return !template.isLock && !isLock && !template.isItemBean();
    }

    public boolean isHaveOption(int optionId) {
        for (ItemOption itemOption : options) {
            if (itemOption.template.id == optionId) {
                return true;
            }
        }
        return false;
    }

    public boolean isCanCrystallize() {
        return template.isItemGang() || template.isItemGiay() || template.isItemQuan() || template.isItemAo()
                || template.isItemRadar() || template.isItemDayChuyen() || template.isItemNhan() || template.isItemBoi() || template.isItemAvatar();
    }

    public boolean isThanLinh() {
        return template.id >= 337 && template.id <= 352;
    }

    public void setDefaultOption() {
        if (options == null) {
            options = new ArrayList<>();
        }
        ArrayList<ItemOption> optionArrayList = ItemManager.getInstance().itemOptions.get(template.id);
        if (optionArrayList == null) {
            return;
        }
        for (ItemOption option : optionArrayList) {
            options.add(new ItemOption(option.template.id, option.param));
        }
    }

    public void setRandomDefaultOption() {
        if (options == null) {
            options = new ArrayList<>();
        }
        ArrayList<ItemOption> optionArrayList = ItemManager.getInstance().itemOptions.get(template.id);
        if (optionArrayList == null) {
            return;
        }
        for (ItemOption option : optionArrayList) {
            if (option.template.type != 0 || Utils.nextInt(100) < 50) {
                options.add(new ItemOption(option.template.id, option.param));
            }
        }
    }

    public ArrayList<ItemOption> getOptionDisplays() {
        if (template.type == ItemType.TYPE_MEDAL || template.type == ItemType.TYPE_THU_CUOI
                || template.type == ItemType.TYPE_AVATAR || template.type == ItemType.TYPE_AURA || template.type == ItemType.TYPE_BAG) {
            if (options.stream().allMatch(o -> o.template.id != 176)) {
                return options;
            }
            LinkedHashMap<Integer, ItemOption> itemOptions = new LinkedHashMap<>();
            for (ItemOption option : options) {
                if (option.template.id != 176) {
                    ItemOption itemOption = itemOptions.get(option.template.id);
                    if (itemOption == null) {
                        itemOptions.put(option.template.id, new ItemOption(option.template, option.param));
                    } else {
                        if (option.template.id == 25 || option.template.id == 31 || option.template.id == 32
                                || option.template.id == 109 || option.template.id == 1 || option.template.id == 2
                                || option.template.id == 5 || option.template.id == 6 || option.template.id == 26
                                || option.template.id == 27 || option.template.id == 28) {
                            itemOption.param += option.param;
                        } else {
                            itemOption.param = Math.max(option.param, itemOption.param);
                        }
                    }
                }
            }
            ArrayList<ItemOption> results = new ArrayList<>(itemOptions.values());
            for (ItemOption option : options) {
                if (option.template.id == 176) {
                    results.add(option);
                }
            }
            return results;
        }
        return options;
    }

    public int getCountStone() {
        return switch (template.id) {
            case ItemName.DA_1 -> 1;
            case ItemName.DA_2 -> 4;
            case ItemName.DA_3 -> 16;
            case ItemName.DA_4 -> 64;
            case ItemName.DA_5 -> 256;
            case ItemName.DA_6 -> 1024;
            case ItemName.DA_7 -> 4096;
            case ItemName.DA_8 -> 16384;
            case ItemName.DA_9 -> 65536;
            case ItemName.DA_10 -> 262144;
            case ItemName.DA_11 -> 1048576;
            case ItemName.DA_12 -> 3096576;
            default -> 0;
        };
    }

    public void nextUpgradePet() {
        ItemOption upgrade = getOption(19);
        if (upgrade != null && upgrade.param >= Pet.MAX_LEVEL) {
            return;
        }
        if (upgrade == null) {
            this.options.add(new ItemOption(19, 0));
        }
        for (ItemOption itemOption : this.options) {
            if (itemOption.template.id == 68 || itemOption.template.id == 67 || itemOption.template.id == 131
                    || itemOption.template.id == 126 || itemOption.template.id == 127 || itemOption.template.id == 162) {
                continue;
            }
            if (itemOption.template.id == 122 || itemOption.template.id == 123 || itemOption.template.id == 124) {
                itemOption.param += 150;
            } else if (itemOption.template.id == 19 || itemOption.template.id == 125 || itemOption.template.id == 25
                    || itemOption.template.id == 31 || itemOption.template.id == 32) {
                itemOption.param++;
            } else {
                itemOption.param += Math.max(itemOption.param / 10, 1);
            }
        }
    }

    public void nextStarPet() {
        for (ItemOption itemOption : this.options) {
            if (itemOption.template.id == 68 || itemOption.template.id == 67 || itemOption.template.id == 126 || itemOption.template.id == 127) {
                itemOption.param++;
            } else if (itemOption.template.id == 131) {
                itemOption.param = 0;
            } else if (itemOption.template.id == 19) {
                itemOption.param = 1;
            } else if (itemOption.template.id == 122 || itemOption.template.id == 123 || itemOption.template.id == 124) {
                itemOption.param -= 2000;
            } else if (itemOption.template.id == 125 || itemOption.template.id == 25
                    || itemOption.template.id == 31 || itemOption.template.id == 32) {
                itemOption.param -= 13;
            } else {
                itemOption.param = itemOption.param * 50 / 167;
            }
        }
    }

    public void downUpgradePet() {
        for (ItemOption itemOption : this.options) {
            if (itemOption.template.id == 68 || itemOption.template.id == 67 || itemOption.template.id == 131) {
                continue;
            }
            if (itemOption.template.id == 122 || itemOption.template.id == 123 || itemOption.template.id == 124) {
                itemOption.param -= 150;
            } else if (itemOption.template.id == 19 || itemOption.template.id == 125) {
                itemOption.param--;
            } else if (itemOption.template.id != 126 && itemOption.template.id != 127) {
                itemOption.param -= Math.max(itemOption.param / 11, 1);
            }
        }
    }

    public int nextUpgrade() {
        int upgrade = getUpgrade();
        if (upgrade >= UpgradeItem.MAX_UPGRADE) {
            return upgrade;
        }
        if (upgrade == 0) {
            this.options.add(new ItemOption(19, 0));
        }
        for (ItemOption itemOption : this.options) {
            if (itemOption.template.id == 19 || itemOption.template.id == 162) {
                continue;
            }
            if (itemOption.template.type == 0) {
                int per = 10;
                if (itemOption.template.id == 0 || itemOption.template.id == 3 || itemOption.template.id == 4) {
                    if (upgrade >= 14) {
                        per = 30;
                    } else if (upgrade >= 12) {
                        per = 25;
                    } else if (upgrade >= 8) {
                        per = 20;
                    } else if (upgrade >= 4) {
                        per = 15;
                    }
                }
                int num = itemOption.param * per / 100;
                if (num < 1) {
                    num = 1;
                }
                itemOption.param += num;
            }
            if (itemOption.template.type == 2) {
                itemOption.param += 10;
            }
        }
        for (ItemOption itemOption : options) {
            if (itemOption.template.id == 19) {
                itemOption.param++;
                upgrade = itemOption.param;
                break;
            }
        }
        return upgrade;
    }

    public void nextUpgradeSurvival() {
        int upgrade = getUpgrade();
        if (template.id >= 176 && template.id <= 190 && upgrade >= 4) {
            return;
        }
        if (template.id >= 192 && template.id <= 207 && upgrade >= 8) {
            return;
        }
        if (upgrade >= UpgradeItem.MAX_UPGRADE) {
            return;
        }
        if (upgrade == 0) {
            this.options.add(new ItemOption(19, 0));
        }
        for (ItemOption itemOption : this.options) {
            if (itemOption.template.type == 0) {
                int num = itemOption.param * 10 / 100;
                if (num < 1) {
                    num = 1;
                }
                itemOption.param += num;
            }
            if (itemOption.template.type == 2) {
                itemOption.param += 10;
            }
            if (itemOption.template.id == 19) {
                itemOption.param += 1;
            }
        }
    }

    public int downUpgrade() {
        int upgrade = getUpgrade();
        if (upgrade <= 0) {
            return upgrade;
        }
        for (ItemOption itemOption : options) {
            if (itemOption.template.id == 19 || itemOption.template.id == 162) {
                continue;
            }
            if (itemOption.template.type == 0) {
                int per = 10;
                if (itemOption.template.id == 0 || itemOption.template.id == 3 || itemOption.template.id == 4) {
                    if (upgrade > 14) {
                        per = 30;
                    } else if (upgrade > 12) {
                        per = 25;
                    } else if (upgrade > 8) {
                        per = 20;
                    } else if (upgrade > 4) {
                        per = 15;
                    }
                }
                int num = itemOption.param * per / (100 + per);
                if (num < 1) {
                    num = 1;
                }
                itemOption.param -= num;
                if (itemOption.param < 1) {
                    itemOption.param = 1;
                }
            }
            if (itemOption.template.type == 2) {
                itemOption.param -= 10;
                if (itemOption.param < 1) {
                    itemOption.param = 1;
                }
            }
        }
        for (ItemOption itemOption : options) {
            if (itemOption.template.id == 19) {
                itemOption.param--;
                upgrade = itemOption.param;
                break;
            }
        }
        return upgrade;
    }

    public String getContentHistory() {
        String content = template.name;
        int upgrade = getUpgrade();
        if (upgrade > 0) {
            content += " +" + upgrade;
        }
        ItemOption option = getOption(67);
        if (option != null) {
            content += " " + option.param + "sao";
        }
        return content;
    }

    public int getQuantityConsignment() {
        if (template.isLock) {
            return -1;
        }
        if (isLock) {
            return -1;
        }
        if (template.type < 8) {
            return 1;
        }
        if (template.type == ItemType.TYPE_EVENT) {
            switch (template.id) {
                case ItemName.NGOC_TRAI_DAC_BIET:
                case ItemName.VAY_CA_MAP:
                    return 10;
                default:
                    return 100;
            }
        }
        switch (template.id) {
            case ItemName.KEO_DO:
            case ItemName.KEO_LUC:
            case ItemName.KEO_VANG:
            case ItemName.KEO_XANH:
            case ItemName.NGOC_RONG_4_SAO:
            case ItemName.SAO_PHA_LE:
            case ItemName.MANH_GIAY:
            case ItemName.TINH_THACH:
                return 100;

            case ItemName.DUA_NAU:
            case ItemName.DUA_VANG:
            case ItemName.DUA_XANH:
            case ItemName.NGOC_RONG_2_SAO:
            case ItemName.NGOC_RONG_3_SAO:
            case ItemName.SAO_PHA_LE_DANH_BONG:
                return 10;

            case ItemName.NGOC_RONG_7_SAO:
            case ItemName.NGOC_RONG_6_SAO:
            case ItemName.NGOC_RONG_5_SAO:
            case ItemName.MANH_GANG_3X:
            case ItemName.MANH_GIAY_3X:
            case ItemName.MANH_QUAN_3X:
            case ItemName.MANH_AO_3X:
            case ItemName.MANH_BOI_3X:
            case ItemName.MANH_NHAN_3X:
            case ItemName.MANH_DAY_CHUYEN_3X:
            case ItemName.MANH_RADAR_3X:
            case ItemName.MANH_GANG_4X:
            case ItemName.MANH_GIAY_4X:
            case ItemName.MANH_QUAN_4X:
            case ItemName.MANH_AO_4X:
            case ItemName.MANH_BOI_4X:
            case ItemName.MANH_NHAN_4X:
            case ItemName.MANH_DAY_CHUYEN_4X:
            case ItemName.MANH_RADAR_4X:
            case ItemName.CAPSULE_DONG:
            case ItemName.DA_NGU_SAC:
                return 1000;

            case ItemName.NGOC_RONG_1_SAO:
                return 1;
        }
        return -1;
    }

    public String getContentGift() {
        String content = template.name;
        int upgrade = getUpgrade();
        if (upgrade > 0) {
            content += String.format(" +%d", upgrade);
        }
        ItemOption option = getOption(67);
        if (option != null) {
            content += String.format(" %d sao", option.param);
        }
        int expiry = getExpiry();
        if (expiry == -1) {
            content += " (HSD vĩnh viễn)";
        } else {
            content += String.format(" (HSD %d ngày)", expiry);
        }
        return content;
    }

    public int getEffectTemplateId() {
        switch (template.id) {
            case ItemName.COM_NAM:
                return EffectName.THUC_AN_0X;
            case ItemName.SAND_WITCH:
                return EffectName.THUC_AN_1X;
            case ItemName.GA_QUAY:
                return EffectName.THUC_AN_2X;
            case ItemName.BEEF_STEAK:
                return EffectName.THUC_AN_3X;
            case ItemName.BUA_TRI_TUE:
                return EffectName.BUA_TRI_TUE;
            case ItemName.BUA_MANH_ME:
                return EffectName.BUA_SUC_MANH;
            case ItemName.BUA_CUNG_CAP:
                return EffectName.BUA_CUNG_CAP;
            case ItemName.BUA_THU_HUT:
                return EffectName.BUA_THU_HUT;
            case ItemName.BUA_MAY_MAN:
                return EffectName.BUA_MAY_MAN;
            case ItemName.TDLT:
                return EffectName.TU_DONG_LUYEN_TAP;
            case ItemName.BUA_BAO_HO:
                return EffectName.BUA_BAO_HO;
            case ItemName.BUA_NGUNG_DONG:
                return EffectName.BUA_NGUNG_DONG;
            case ItemName.KEO_DO:
                return EffectName.TANG_PHAN_TRAM_HP_TU_KEO_DO;
            case ItemName.KEO_VANG:
                return EffectName.TANG_PHAN_TRAM_SUC_DANH_TU_KEO_VANG;
            case ItemName.RADAR_RT:
                return EffectName.DO_MANH_TRANG_BI;
            case ItemName.KEO_LUC:
                return EffectName.TANG_PHAN_TRAM_GIAM_SAT_THUONG_TU_KEO_LUC;
            case ItemName.KEO_XANH:
                return EffectName.TANG_PHAN_TRAM_KI_TU_KEO_XANH;
            case ItemName.RADAR_RT2:
                return EffectName.DO_TIM_DA_NGU_SAC;
            case ItemName.DUA_HAU:
                return EffectName.DE_LAY_THU_DUA_HAU_EVENT_HUNG_VUONG;
            case ItemName.QUA_TRUNG:
                return EffectName.QUA_TRUNG_DE_TU;
            case ItemName.BUA_DE_TU:
                return EffectName.BUA_DE_TU;
            case ItemName.DUA_XANH:
                return EffectName.TANG_TNSM_DUA_XANH;
            case ItemName.DUA_NAU:
                return EffectName.TANG_PHAN_TRAM_HP_KI_DUA_NAU;
            case ItemName.DUA_VANG:
                return EffectName.TANG_PHAN_TRAM_SUC_DANH_DUA_VANG;
            case ItemName.MAY_DO_CAPSULE_DONG:
                return EffectName.MAY_DO_CAPSULE_DONG;
            case ItemName.KINH_RAM:
                return EffectName.KHANG_THAI_DUONG_HA_SAN_KINH_RAM;
            case ItemName.MAY_DO_MANH_YARDRAT:
                return EffectName.DO_MANH_YARDRAT;
            case ItemName.KEO_DAU_LAU_HALLOWEEN_2023:
                return EffectName.KEO_DAU_LAU_TNSM;
            case ItemName.KEO_CAM_LAU_HALLOWEEN_2023:
                return EffectName.KEO_DAU_LAU_HP_KI;
            case ItemName.KEO_MAT_XANH_HALLOWEEN_2023:
                return EffectName.KEO_DAU_LAU_SUC_DANH;
            case ItemName.KEO_THAN_CHET_HALLOWEEN_2023:
                return EffectName.KEO_DAU_LAU_NE_DON;
            case ItemName.MAY_DO_TINH_THACH:
                return EffectName.DO_TINH_THACH;
            case ItemName.BUA_SIEU_TRI_TUE:
                return EffectName.BUA_SIEU_TRI_TUE;
            case ItemName.BUA_SIEU_CAP_TRI_TUE:
                return EffectName.BUA_SIEU_CAP_TRI_TUE;
            case ItemName.BUA_SIEU_DE_TU:
                return EffectName.BUA_SIEU_DE_TU;
            case ItemName.BUA_SIEU_CAP_DE_TU:
                return EffectName.BUA_SIEU_CAP_DE_TU;
        }
        if (template.type < 8) {
            switch (template.type) {
                case ItemType.TYPE_AO:
                    return EffectName.TANG_PHAN_TRAM_GIAM_SAT_THUONG_TU_AO_16;
                case ItemType.TYPE_QUAN:
                    return EffectName.TANG_PHAN_TRAM_HP_TU_QUAN_16;
                case ItemType.TYPE_GANG:
                    return EffectName.TANG_PHAN_TRAM_SUC_DANH_TU_GANG_16;
                case ItemType.TYPE_GIAY:
                    return EffectName.TANG_PHAN_TRAM_NE_DON_TU_GIAY_16;
                case ItemType.TYPE_RADAR:
                    return EffectName.TANG_PHAN_TRAM_CHINH_XAC_TU_RADAR_16;
                case ItemType.TYPE_DAY_CHUYEN:
                    return EffectName.TANG_PHAN_TRAM_HOI_HP_TU_DAY_CHUYEN_16;
                case ItemType.TYPE_NHAN:
                    return EffectName.TANG_PHAN_TRAM_CHI_MANG_TU_NHAN_16;
                case ItemType.TYPE_BOI:
                    return EffectName.TANG_PHAN_TRAM_HOI_KI_TU_BOI_16;
            }
        }
        return -1;
    }

    public int nextStoneId() {
        if (template.id >= 161) {
            return 162;
        }
        if (template.id == 57) {
            return 158;
        }
        return template.id + 1;
    }

    public void createOptionSpec(int count, int level) {
        HashMap<Integer, Integer[]> params = new HashMap<>();
        params.put(31, new Integer[]{5, 10, 5});
        params.put(32, new Integer[]{5, 10, 5});
        params.put(25, new Integer[]{5, 10, 5});
        params.put(1, new Integer[]{500, 1000, 500});
        params.put(6, new Integer[]{500, 1000, 500});
        params.put(17, new Integer[]{500, 1000, 500});
        params.put(99, new Integer[]{500, 1000, 500});
        Integer[] options = params.keySet().toArray(new Integer[0]);
        Integer[] array = Utils.nextInt(options, count);
        for (int value : array) {
            int min = params.get(value)[0];
            int max = params.get(value)[1];
            int range = params.get(value)[2];
            this.options.add(new ItemOption(value, Utils.nextInt(min + (level - 1) * range, max + (level - 1) * range)));
        }
    }

    public void createOptionEquipPet() {
        HashMap<Integer, Integer[]> params = new HashMap<>();
        params.put(0, new Integer[]{300, 1000});
        params.put(1, new Integer[]{300, 1000});
        params.put(2, new Integer[]{1000, 2000});
        params.put(3, new Integer[]{300, 1000});
        params.put(4, new Integer[]{300, 1000});
        params.put(5, new Integer[]{300, 1000});
        params.put(6, new Integer[]{300, 1000});
        params.put(25, new Integer[]{3, 10});
        params.put(26, new Integer[]{500, 1000});
        params.put(27, new Integer[]{1000, 2000});
        params.put(28, new Integer[]{500, 1000});
        params.put(29, new Integer[]{500, 1000});
        params.put(30, new Integer[]{500, 1000});
        params.put(31, new Integer[]{3, 10});
        params.put(32, new Integer[]{3, 10});
        params.put(99, new Integer[]{1000, 2000});
        Integer[] options = params.keySet().toArray(new Integer[0]);
        Integer[] array = Utils.nextInt(options, 1);
        for (int value : array) {
            int min = params.get(value)[0];
            int max = params.get(value)[1];
            RandomCollection<Integer> values = new RandomCollection<>();
            int percent = 1;
            for (int i = max; i >= min; i--) {
                values.add(percent += 10, i);
            }
            this.options.add(new ItemOption(value, values.next()));
        }

       /* int index = getIndexPet();
        if (index == -1) {
            return;
        }
        switch (index) {
            case 0:
                options.add(new ItemOption(0, Utils.nextInt(100, 300)));
                options.add(new ItemOption(25, Utils.nextInt(1, 3)));
                return;

            case 1:
                options.add(new ItemOption(6, Utils.nextInt(300, 500)));
                options.add(new ItemOption(27, Utils.nextInt(1000, 3000)));
                options.add(new ItemOption(28, Utils.nextInt(300, 500)));
                return;

            case 2:
                options.add(new ItemOption(1, Utils.nextInt(300, 500)));
                options.add(new ItemOption(2, Utils.nextInt(1000, 3000)));
                options.add(new ItemOption(26, Utils.nextInt(300, 500)));
                options.add(new ItemOption(71, Utils.nextInt(300, 500)));
                return;

            case 3:
                options.add(new ItemOption(4, Utils.nextInt(300, 500)));
                options.add(new ItemOption(32, Utils.nextInt(1, 3)));
                return;

            case 4:
                options.add(new ItemOption(3, Utils.nextInt(300, 500)));
                options.add(new ItemOption(31, Utils.nextInt(1, 3)));
                return;
        }*/
    }

    public void createOptionEvent() {
        switch (template.id) {

            case ItemName.CAI_TRANG_QUY_LAO_VIP: {
                options.add(new ItemOption(25, 30));
                options.add(new ItemOption(31, 30));
                options.add(new ItemOption(32, 30));
                options.add(new ItemOption(194, 5));
                options.add(new ItemOption(128, 150));
                options.add(new ItemOption(33, 3));
                options.add(new ItemOption(34, 3));
                options.add(new ItemOption(35, 3));
                return;
            }

            case ItemName.CAI_TRANG_THAN_VU_TRU_VIP: {
                options.add(new ItemOption(25, 30));
                options.add(new ItemOption(31, 30));
                options.add(new ItemOption(32, 30));
                options.add(new ItemOption(194, 5));
                options.add(new ItemOption(195, 20));
                options.add(new ItemOption(33, 3));
                options.add(new ItemOption(34, 3));
                options.add(new ItemOption(35, 3));
                return;
            }


            case ItemName.HUY_HIEU_UONG_NUOC_NHO_NGUON: {
                options.add(new ItemOption(25, 30));
                options.add(new ItemOption(31, 30));
                options.add(new ItemOption(32, 30));
                options.add(new ItemOption(194, 5));
                options.add(new ItemOption(33, 2));
                options.add(new ItemOption(34, 2));
                options.add(new ItemOption(35, 2));
                return;
            }

            case ItemName.AURA_HALOWEEN:
            case ItemName.CHOI_BAY_HALLOWEEN:
            case ItemName.CANH_DOI_HALLOWEEN_2023:
            case ItemName.MA_TROI_AC_QUY_HALLOWEEN_2023:
            case ItemName.MA_TROI_BI_NGO_HALLOWEEN_2023:
            case ItemName.MA_TROI_PHU_THUY_HALLOWEEN_2023: {
                options.add(new ItemOption(25, Utils.nextInt(40, 50)));
                options.add(new ItemOption(31, Utils.nextInt(40, 50)));
                options.add(new ItemOption(32, Utils.nextInt(40, 50)));
                options.add(new ItemOption(193, Utils.nextInt(5, 10)));
                options.add(new ItemOption(33, 2));
                options.add(new ItemOption(34, 2));
                options.add(new ItemOption(35, 2));
                return;
            }

            case ItemName.CAI_TRANG_HANG_NGA_2024: {
                options.add(new ItemOption(25, Utils.nextInt(40, 50)));
                options.add(new ItemOption(31, Utils.nextInt(40, 50)));
                options.add(new ItemOption(32, Utils.nextInt(40, 50)));
                options.add(new ItemOption(188, Utils.nextInt(10, 15)));
                options.add(new ItemOption(33, 2));
                options.add(new ItemOption(34, 2));
                options.add(new ItemOption(35, 2));
                return;
            }

            case ItemName.LONG_DEN_CA_CHEP: {
                options.add(new ItemOption(25, Utils.nextInt(25, 35)));
                options.add(new ItemOption(1, Utils.nextInt(500, 1000)));
                options.add(new ItemOption(33, 3));
                return;
            }

            case ItemName.LONG_DEN_HOA_DANG: {
                options.add(new ItemOption(31, Utils.nextInt(25, 35)));
                options.add(new ItemOption(32, Utils.nextInt(25, 35)));
                options.add(new ItemOption(6, Utils.nextInt(500, 1000)));
                options.add(new ItemOption(27, Utils.nextInt(500, 1000)));
                options.add(new ItemOption(34, 3));
                options.add(new ItemOption(35, 3));
                return;
            }

            case ItemName.LONG_DEN_ONG_TRANG: {
                options.add(new ItemOption(25, Utils.nextInt(15, 20)));
                options.add(new ItemOption(31, Utils.nextInt(15, 20)));
                options.add(new ItemOption(32, Utils.nextInt(15, 20)));
                options.add(new ItemOption(188, Utils.nextInt(10, 15)));
                options.add(new ItemOption(33, 2));
                options.add(new ItemOption(34, 2));
                options.add(new ItemOption(35, 2));
                return;
            }

            case ItemName.AURA_HANG_NGA: {
                options.add(new ItemOption(25, Utils.nextInt(25, 35)));
                options.add(new ItemOption(31, Utils.nextInt(25, 35)));
                options.add(new ItemOption(32, Utils.nextInt(25, 35)));
                options.add(new ItemOption(188, Utils.nextInt(10, 15)));
                options.add(new ItemOption(33, 2));
                options.add(new ItemOption(34, 2));
                options.add(new ItemOption(35, 2));
                return;
            }

            case ItemName.AURA_MA_TOC_DO: {
                options.add(new ItemOption(25, 30));
                options.add(new ItemOption(31, 30));
                options.add(new ItemOption(32, 30));
                options.add(new ItemOption(178, 500));
                options.add(new ItemOption(179, 5));
                options.add(new ItemOption(33, 1));
                options.add(new ItemOption(34, 1));
                options.add(new ItemOption(35, 1));
                return;
            }

            case ItemName.AURA_RONG_NAMEK: {
                options.add(new ItemOption(25, Utils.nextInt(20, 30)));
                options.add(new ItemOption(31, Utils.nextInt(20, 30)));
                options.add(new ItemOption(32, Utils.nextInt(20, 30)));
                options.add(new ItemOption(Utils.isPercent(50) ? 135 : 174, Utils.nextInt(10, 20)));
                options.add(new ItemOption(33, 1));
                options.add(new ItemOption(34, 1));
                options.add(new ItemOption(35, 1));
                return;
            }

            case ItemName.CAI_TRANG_PIC_HE_2024: {
                options.add(new ItemOption(25, Utils.nextInt(30, 40)));
                options.add(new ItemOption(31, Utils.nextInt(40, 50)));
                options.add(new ItemOption(32, Utils.nextInt(40, 50)));
                options.add(new ItemOption(6, Utils.nextInt(3000, 5000)));
                options.add(new ItemOption(169, Utils.nextInt(3, 5)));
                options.add(new ItemOption(33, 2));
                options.add(new ItemOption(34, 2));
                options.add(new ItemOption(35, 2));
                options.add(new ItemOption(111, 50));
                return;
            }

            case ItemName.CAI_TRANG_POC_HE_2024: {
                options.add(new ItemOption(25, Utils.nextInt(35, 45)));
                options.add(new ItemOption(31, Utils.nextInt(35, 40)));
                options.add(new ItemOption(32, Utils.nextInt(35, 40)));
                options.add(new ItemOption(1, Utils.nextInt(1000, 2000)));
                options.add(new ItemOption(2, Utils.nextInt(3000, 5000)));
                options.add(new ItemOption(169, Utils.nextInt(3, 5)));
                options.add(new ItemOption(33, 2));
                options.add(new ItemOption(34, 2));
                options.add(new ItemOption(35, 2));
                options.add(new ItemOption(78, 60));
                return;
            }

            case ItemName.HUY_HIEU_HE_2024: {
                options.add(new ItemOption(25, Utils.nextInt(35, 45)));
                options.add(new ItemOption(31, Utils.nextInt(35, 45)));
                options.add(new ItemOption(32, Utils.nextInt(35, 45)));
                options.add(new ItemOption(99, Utils.nextInt(1000, 3000)));
                options.add(new ItemOption(169, Utils.nextInt(3, 5)));
                options.add(new ItemOption(33, 1));
                options.add(new ItemOption(34, 1));
                options.add(new ItemOption(35, 1));
                return;
            }

            case ItemName.VAN_LUOT_SONG_THAN: {
                options.add(new ItemOption(25, Utils.nextInt(35, 45)));
                options.add(new ItemOption(31, Utils.nextInt(35, 45)));
                options.add(new ItemOption(32, Utils.nextInt(35, 45)));
                options.add(new ItemOption(99, Utils.nextInt(1000, 3000)));
                options.add(new ItemOption(167, Utils.nextInt(10, 20)));
                options.add(new ItemOption(7, 200));
                options.add(new ItemOption(169, Utils.nextInt(3, 5)));
                options.add(new ItemOption(33, 2));
                options.add(new ItemOption(34, 2));
                options.add(new ItemOption(35, 2));
                return;
            }

            case ItemName.CANH_THUY_TE:
            case ItemName.BABY_SHARK: {
                options.add(new ItemOption(25, Utils.nextInt(30, 35)));
                options.add(new ItemOption(31, Utils.nextInt(35, 45)));
                options.add(new ItemOption(32, Utils.nextInt(35, 45)));
                options.add(new ItemOption(99, Utils.nextInt(1000, 3000)));
                options.add(new ItemOption(166, Utils.nextInt(20, 30)));
                options.add(new ItemOption(169, Utils.nextInt(3, 5)));
                options.add(new ItemOption(33, 2));
                options.add(new ItemOption(34, 2));
                options.add(new ItemOption(35, 2));
                return;
            }

            case ItemName.O_BACH_TUOC: {
                options.add(new ItemOption(25, Utils.nextInt(35, 45)));
                options.add(new ItemOption(31, Utils.nextInt(20, 25)));
                options.add(new ItemOption(32, Utils.nextInt(20, 25)));
                options.add(new ItemOption(1, Utils.nextInt(1000, 1500)));
                options.add(new ItemOption(2, Utils.nextInt(3000, 5000)));
                options.add(new ItemOption(164, Utils.nextInt(30, 50)));
                options.add(new ItemOption(169, Utils.nextInt(3, 5)));
                options.add(new ItemOption(33, 2));
                options.add(new ItemOption(34, 2));
                options.add(new ItemOption(35, 2));
                return;
            }

            case ItemName.CAI_TRANG_MI_NUONG: {
                options.add(new ItemOption(25, Utils.nextInt(35, 45)));
                options.add(new ItemOption(31, Utils.nextInt(45, 55)));
                options.add(new ItemOption(32, Utils.nextInt(45, 55)));
                options.add(new ItemOption(1, Utils.nextInt(1000, 2000)));
                options.add(new ItemOption(2, Utils.nextInt(3000, 5000)));
                options.add(new ItemOption(6, Utils.nextInt(1500, 2500)));
                options.add(new ItemOption(85, Utils.nextInt(15, 25)));
                options.add(new ItemOption(167, Utils.nextInt(20, 30)));
                options.add(new ItemOption(33, 2));
                options.add(new ItemOption(34, 2));
                options.add(new ItemOption(35, 2));
                options.add(new ItemOption(78, 50));
                return;
            }

            case ItemName.HUY_HIEU_HUNG_VUONG: {
                options.add(new ItemOption(25, Utils.nextInt(35, 45)));
                options.add(new ItemOption(31, Utils.nextInt(35, 45)));
                options.add(new ItemOption(32, Utils.nextInt(35, 45)));
                options.add(new ItemOption(99, Utils.nextInt(1000, 3000)));
                options.add(new ItemOption(165, 1));
                options.add(new ItemOption(33, 1));
                options.add(new ItemOption(34, 1));
                options.add(new ItemOption(35, 1));
                return;
            }

            case ItemName.CHIM_LAC_TAM_LINH: {
                options.add(new ItemOption(25, Utils.nextInt(35, 45)));
                options.add(new ItemOption(31, Utils.nextInt(35, 45)));
                options.add(new ItemOption(32, Utils.nextInt(35, 45)));
                options.add(new ItemOption(99, Utils.nextInt(1000, 3000)));
                options.add(new ItemOption(167, Utils.nextInt(10, 20)));
                options.add(new ItemOption(7, 200));
                options.add(new ItemOption(33, 2));
                options.add(new ItemOption(34, 2));
                options.add(new ItemOption(35, 2));
                return;
            }

            case ItemName.CANH_DONG_SON: {
                options.add(new ItemOption(25, Utils.nextInt(30, 35)));
                options.add(new ItemOption(31, Utils.nextInt(35, 45)));
                options.add(new ItemOption(32, Utils.nextInt(35, 45)));
                options.add(new ItemOption(99, Utils.nextInt(1000, 3000)));
                options.add(new ItemOption(166, Utils.nextInt(20, 30)));
                options.add(new ItemOption(33, 2));
                options.add(new ItemOption(34, 2));
                options.add(new ItemOption(35, 2));
                return;
            }

            case ItemName.DO_LONG_DAO: {
                options.add(new ItemOption(25, Utils.nextInt(35, 45)));
                options.add(new ItemOption(31, Utils.nextInt(20, 25)));
                options.add(new ItemOption(32, Utils.nextInt(20, 25)));
                options.add(new ItemOption(1, Utils.nextInt(1000, 1500)));
                options.add(new ItemOption(2, Utils.nextInt(3000, 5000)));
                options.add(new ItemOption(164, Utils.nextInt(30, 50)));
                options.add(new ItemOption(33, 2));
                options.add(new ItemOption(34, 2));
                options.add(new ItemOption(35, 2));
                return;
            }

            case ItemName.CANH_BUOM_LADY_GIRL: {
                options.add(new ItemOption(25, Utils.nextInt(25, 30)));
                options.add(new ItemOption(31, Utils.nextInt(30, 40)));
                options.add(new ItemOption(32, Utils.nextInt(30, 40)));
                options.add(new ItemOption(99, Utils.nextInt(1000, 3000)));
                options.add(new ItemOption(160, Utils.nextInt(3, 5)));
                options.add(new ItemOption(33, 2));
                options.add(new ItemOption(34, 2));
                options.add(new ItemOption(35, 2));
                return;
            }

            case ItemName.FIDE_CUPID: {
                options.add(new ItemOption(25, Utils.nextInt(30, 40)));
                options.add(new ItemOption(31, Utils.nextInt(15, 20)));
                options.add(new ItemOption(32, Utils.nextInt(15, 20)));
                options.add(new ItemOption(1, Utils.nextInt(1000, 1500)));
                options.add(new ItemOption(2, Utils.nextInt(3000, 5000)));
                options.add(new ItemOption(160, Utils.nextInt(3, 5)));
                options.add(new ItemOption(33, 2));
                options.add(new ItemOption(34, 2));
                options.add(new ItemOption(35, 2));
                return;
            }

            case ItemName.CAI_TRANG_ANDROID_21: {
                options.add(new ItemOption(25, Utils.nextInt(35, 45)));
                options.add(new ItemOption(31, Utils.nextInt(45, 55)));
                options.add(new ItemOption(32, Utils.nextInt(45, 55)));
                options.add(new ItemOption(1, Utils.nextInt(1000, 2000)));
                options.add(new ItemOption(2, Utils.nextInt(3000, 5000)));
                options.add(new ItemOption(6, Utils.nextInt(1500, 2500)));
                options.add(new ItemOption(85, Utils.nextInt(15, 25)));
                options.add(new ItemOption(106, Utils.nextInt(15, 25)));
                options.add(new ItemOption(160, Utils.nextInt(3, 5)));
                options.add(new ItemOption(33, 2));
                options.add(new ItemOption(34, 2));
                options.add(new ItemOption(35, 2));
                options.add(new ItemOption(78, 50));
                return;
            }

            case ItemName.HUY_HIEU_LADY_GIRL: {
                options.add(new ItemOption(25, Utils.nextInt(30, 40)));
                options.add(new ItemOption(31, Utils.nextInt(30, 40)));
                options.add(new ItemOption(32, Utils.nextInt(30, 40)));
                options.add(new ItemOption(99, Utils.nextInt(1000, 3000)));
                options.add(new ItemOption(160, Utils.nextInt(3, 5)));
                return;
            }

            case ItemName.VAN_LUOT_LADY_GIRL: {
                options.add(new ItemOption(25, Utils.nextInt(30, 40)));
                options.add(new ItemOption(31, Utils.nextInt(30, 40)));
                options.add(new ItemOption(32, Utils.nextInt(30, 40)));
                options.add(new ItemOption(99, Utils.nextInt(1000, 3000)));
                options.add(new ItemOption(160, Utils.nextInt(3, 5)));
                options.add(new ItemOption(33, 2));
                options.add(new ItemOption(34, 2));
                options.add(new ItemOption(35, 2));
                return;
            }

            case ItemName.HUY_HIEU_TET_2024: {
                options.add(new ItemOption(25, Utils.nextInt(25, 35)));
                options.add(new ItemOption(31, Utils.nextInt(25, 35)));
                options.add(new ItemOption(32, Utils.nextInt(25, 35)));
                options.add(new ItemOption(99, Utils.nextInt(1000, 3000)));
                if (!Utils.isPercent(3)) {
                    options.add(new ItemOption(50, Utils.nextInt(15, 30)));
                }
                return;
            }

            case ItemName.VAN_BAY_RONG: {
                options.add(new ItemOption(25, Utils.nextInt(25, 35)));
                options.add(new ItemOption(31, Utils.nextInt(25, 35)));
                options.add(new ItemOption(32, Utils.nextInt(25, 35)));
                options.add(new ItemOption(99, Utils.nextInt(1000, 3000)));
                options.add(new ItemOption(33, 2));
                options.add(new ItemOption(34, 2));
                options.add(new ItemOption(35, 2));
                if (!Utils.isPercent(3)) {
                    options.add(new ItemOption(50, Utils.nextInt(15, 30)));
                }
                return;
            }

            case ItemName.CAI_TRANG_PAN_NGUYEN_DAN_XANH: {
                options.add(new ItemOption(25, Utils.nextInt(30, 40)));
                options.add(new ItemOption(1, Utils.nextInt(1000, 2000)));
                options.add(new ItemOption(2, Utils.nextInt(3000, 5000)));
                options.add(new ItemOption(138, 50));
                options.add(new ItemOption(85, Utils.nextInt(15, 25)));
                options.add(new ItemOption(78, 50));
                options.add(new ItemOption(33, 2));
                if (!Utils.isPercent(3)) {
                    options.add(new ItemOption(50, Utils.nextInt(7, 15)));
                }
                return;
            }

            case ItemName.CAI_TRANG_PAN_NGUYEN_DAN_DO: {
                options.add(new ItemOption(31, Utils.nextInt(40, 50)));
                options.add(new ItemOption(32, Utils.nextInt(40, 50)));
                options.add(new ItemOption(6, Utils.nextInt(1500, 2500)));
                options.add(new ItemOption(138, 50));
                options.add(new ItemOption(85, Utils.nextInt(15, 25)));
                options.add(new ItemOption(34, 2));
                options.add(new ItemOption(35, 2));
                if (!Utils.isPercent(3)) {
                    options.add(new ItemOption(50, Utils.nextInt(7, 15)));
                }
                return;
            }

            case ItemName.MA_LONG: {
                options.add(new ItemOption(25, Utils.nextInt(25, 30)));
                options.add(new ItemOption(31, Utils.nextInt(25, 30)));
                options.add(new ItemOption(32, Utils.nextInt(25, 30)));
                options.add(new ItemOption(33, 2));
                options.add(new ItemOption(34, 2));
                options.add(new ItemOption(35, 2));
                if (!Utils.isPercent(3)) {
                    options.add(new ItemOption(50, Utils.nextInt(15, 30)));
                }
                return;
            }

            case ItemName.CAY_NEU_TRANG_PHAO:
            case ItemName.CAY_NEU_BANH_CHUNG:
            case ItemName.CAY_NEU_DAU_RONG: {
                options.add(new ItemOption(25, Utils.nextInt(22, 25)));
                options.add(new ItemOption(31, Utils.nextInt(15, 20)));
                options.add(new ItemOption(32, Utils.nextInt(15, 20)));
                options.add(new ItemOption(1, Utils.nextInt(1000, 1500)));
                options.add(new ItemOption(2, Utils.nextInt(3000, 5000)));
                options.add(new ItemOption(33, 1));
                options.add(new ItemOption(34, 1));
                options.add(new ItemOption(35, 1));
                if (!Utils.isPercent(3)) {
                    options.add(new ItemOption(50, Utils.nextInt(15, 30)));
                }
                return;
            }

            case ItemName.PUAR_LEM_LINH: {
                options.add(new ItemOption(25, 30));
                options.add(new ItemOption(31, 30));
                options.add(new ItemOption(32, 30));
                options.add(new ItemOption(6, 2000));
                options.add(new ItemOption(27, 4000));
                options.add(new ItemOption(194, 5));
                options.add(new ItemOption(33, 2));
                options.add(new ItemOption(34, 2));
                options.add(new ItemOption(35, 2));
                return;
            }

            case ItemName.PUAR_TINH_NGHICH: {
                options.add(new ItemOption(25, 30));
                options.add(new ItemOption(31, 30));
                options.add(new ItemOption(32, 30));
                options.add(new ItemOption(1, 1500));
                options.add(new ItemOption(2, 4000));
                options.add(new ItemOption(194, 5));
                options.add(new ItemOption(33, 2));
                options.add(new ItemOption(34, 2));
                options.add(new ItemOption(35, 2));
                return;
            }

            case ItemName.PUAR_TRI_THUC: {
                options.add(new ItemOption(25, 30));
                options.add(new ItemOption(31, 30));
                options.add(new ItemOption(32, 30));
                options.add(new ItemOption(112, 20));
                options.add(new ItemOption(99, 4000));
                options.add(new ItemOption(194, 5));
                options.add(new ItemOption(33, 2));
                options.add(new ItemOption(34, 2));
                options.add(new ItemOption(35, 2));
                return;
            }

            case ItemName.RUA_BAY: {
                setDefaultOption();
                randomParam(-15, 15);
                if (!Utils.isPercent(5)) {
                    options.add(new ItemOption(50, Utils.nextInt(15, 30)));
                }
                return;
            }

            case ItemName.HUY_HIEU_20_11: {
                options.add(new ItemOption(25, Utils.nextInt(18, 22)));
                options.add(new ItemOption(31, Utils.nextInt(18, 22)));
                options.add(new ItemOption(32, Utils.nextInt(18, 22)));
                options.add(new ItemOption(99, Utils.nextInt(1000, 3000)));
                if (!Utils.isPercent(1)) {
                    options.add(new ItemOption(50, Utils.nextInt(3, 7)));
                }
                return;
            }

            case ItemName.CAI_TRANG_CHI_CHI: {
                if (Utils.nextInt(2) == 0) {
                    options.add(new ItemOption(25, Utils.nextInt(25, 30)));
                    options.add(new ItemOption(1, Utils.nextInt(1000, 2000)));
                    options.add(new ItemOption(2, Utils.nextInt(3000, 5000)));
                    options.add(new ItemOption(132, Utils.nextInt(10, 20)));
                    options.add(new ItemOption(133, Utils.nextInt(10, 20)));
                    options.add(new ItemOption(72, 1));
                    options.add(new ItemOption(85, Utils.nextInt(15, 25)));
                    options.add(new ItemOption(78, 50));
                    options.add(new ItemOption(33, 2));
                } else {
                    options.add(new ItemOption(31, Utils.nextInt(35, 45)));
                    options.add(new ItemOption(32, Utils.nextInt(35, 45)));
                    options.add(new ItemOption(6, Utils.nextInt(1000, 2000)));
                    options.add(new ItemOption(132, Utils.nextInt(10, 20)));
                    options.add(new ItemOption(133, Utils.nextInt(10, 20)));
                    options.add(new ItemOption(72, 1));
                    options.add(new ItemOption(85, Utils.nextInt(15, 25)));
                    options.add(new ItemOption(34, 2));
                    options.add(new ItemOption(35, 2));
                }
                if (!Utils.isPercent(5)) {
                    options.add(new ItemOption(50, Utils.nextInt(15, 30)));
                }
                return;
            }

            case ItemName.HUY_HIEU_NOEL: {
                if (Utils.nextInt(2) == 0) {
                    options.add(new ItemOption(25, Utils.nextInt(20, 25)));
                    options.add(new ItemOption(1, Utils.nextInt(1000, 2000)));
                    options.add(new ItemOption(2, Utils.nextInt(3000, 5000)));
                    options.add(new ItemOption(26, Utils.nextInt(3000, 5000)));
                    options.add(new ItemOption(71, Utils.nextInt(3000, 5000)));
                    options.add(new ItemOption(33, 1));
                } else {
                    options.add(new ItemOption(31, Utils.nextInt(25, 30)));
                    options.add(new ItemOption(32, Utils.nextInt(25, 30)));
                    options.add(new ItemOption(6, Utils.nextInt(1000, 2000)));
                    options.add(new ItemOption(18, Utils.nextInt(3000, 5000)));
                    options.add(new ItemOption(34, 1));
                    options.add(new ItemOption(35, 1));
                }
                if (!Utils.isPercent(1)) {
                    options.add(new ItemOption(50, Utils.nextInt(15, 30)));
                }
                return;
            }

            case ItemName.XE_KEO_NOEL: {
                options.add(new ItemOption(25, Utils.nextInt(15, 20)));
                options.add(new ItemOption(31, Utils.nextInt(15, 20)));
                options.add(new ItemOption(32, Utils.nextInt(15, 20)));
                options.add(new ItemOption(134, Utils.nextInt(15, 20)));
                options.add(new ItemOption(7, 150));
                options.add(new ItemOption(33, 1));
                options.add(new ItemOption(34, 1));
                options.add(new ItemOption(35, 1));
                if (!Utils.isPercent(1)) {
                    options.add(new ItemOption(50, Utils.nextInt(15, 30)));
                }
                return;
            }

            case ItemName.KEO_GAY_NOEL: {
                options.add(new ItemOption(25, Utils.nextInt(25, 27)));
                options.add(new ItemOption(1, Utils.nextInt(1000, 1500)));
                options.add(new ItemOption(2, Utils.nextInt(3000, 5000)));
                options.add(new ItemOption(33, 1));
                if (!Utils.isPercent(5)) {
                    options.add(new ItemOption(50, Utils.nextInt(15, 30)));
                }
                return;
            }

            case ItemName.KARIN_MAY_MAN: {
                options.add(new ItemOption(130, 50));
                options.add(new ItemOption(134, 50));
                options.add(new ItemOption(120, 20));
                options.add(new ItemOption(135, 20));
                options.add(new ItemOption(33, 1));
                options.add(new ItemOption(34, 1));
                options.add(new ItemOption(35, 1));
                if (!Utils.isPercent(5)) {
                    options.add(new ItemOption(50, Utils.nextInt(15, 30)));
                }
                return;
            }

            case ItemName.BUBBLES_HUYEN_BI: {
                options.add(new ItemOption(31, Utils.nextInt(22, 25)));
                options.add(new ItemOption(32, Utils.nextInt(22, 25)));
                options.add(new ItemOption(112, Utils.nextInt(15, 20)));
                options.add(new ItemOption(99, Utils.nextInt(3000, 5000)));
                options.add(new ItemOption(34, 1));
                options.add(new ItemOption(35, 1));
                if (!Utils.isPercent(5)) {
                    options.add(new ItemOption(50, Utils.nextInt(15, 30)));
                }
                return;
            }

            case ItemName.TOTORO_LINH_THIENG: {
                options.add(new ItemOption(51, Utils.nextInt(40, 50)));
                options.add(new ItemOption(99, Utils.nextInt(3000, 5000)));
                options.add(new ItemOption(48, Utils.nextInt(3000, 5000)));
                options.add(new ItemOption(49, Utils.nextInt(3000, 5000)));
                options.add(new ItemOption(33, 1));
                options.add(new ItemOption(34, 1));
                options.add(new ItemOption(35, 1));
                if (!Utils.isPercent(5)) {
                    options.add(new ItemOption(50, Utils.nextInt(15, 30)));
                }
                return;
            }

        }
    }

    public void createOptionPet() {
        HashMap<Integer, Integer[]> params = new HashMap<>();
        params.put(0, new Integer[]{500, 1000});
        params.put(1, new Integer[]{500, 1000});
        params.put(2, new Integer[]{1000, 2000});
        params.put(3, new Integer[]{500, 1000});
        params.put(4, new Integer[]{500, 1000});
        params.put(5, new Integer[]{500, 1000});
        params.put(6, new Integer[]{500, 1000});
        params.put(25, new Integer[]{5, 10});
        params.put(26, new Integer[]{500, 1000});
        params.put(27, new Integer[]{1000, 2000});
        params.put(28, new Integer[]{500, 1000});
        params.put(29, new Integer[]{500, 1000});
        params.put(30, new Integer[]{500, 1000});
        params.put(31, new Integer[]{5, 10});
        params.put(32, new Integer[]{5, 10});
        params.put(99, new Integer[]{1000, 2000});
        Integer[] options = params.keySet().toArray(new Integer[0]);
        Integer[] array = Utils.nextInt(options, 5);
        for (int value : array) {
            int min = params.get(value)[0];
            int max = params.get(value)[1];
            this.options.add(new ItemOption(value, Utils.nextInt(min, max)));
        }
        this.options.add(new ItemOption(126, Utils.nextInt(5, 10)));
        this.options.add(new ItemOption(127, Utils.nextInt(5, 10)));
        if (template.id == ItemName.PET_HAN_LONG) {
            this.options.add(new ItemOption(122, Utils.nextInt(2, 5) * 500));
        }
        if (template.id == ItemName.PET_HOA_LONG) {
            this.options.add(new ItemOption(123, Utils.nextInt(2, 5) * 500));
        }
        if (template.id == ItemName.PET_HAC_LONG) {
            this.options.add(new ItemOption(124, Utils.nextInt(2, 5) * 500));
        }
        this.options.add(new ItemOption(125, Utils.nextInt(1, 5)));
        this.options.add(new ItemOption(19, 1));
        this.options.add(new ItemOption(67, 1));
        this.options.add(new ItemOption(68, 1));
    }

    public ItemOption createOptionCrystal(boolean isLucky, boolean isMax) {
        int optionId = Polish.options.next();
        int max = 10;
        switch (optionId) {
            case 53: {
                // tang ty le hp
                max = 500;
                break;
            }
            case 54: {
                // tang ty le ki
                max = 500;
                break;
            }
            case 55: {
                // giam sat thuong
                max = 100;
                break;
            }
            case 56: {
                // ty le tan cong
                max = 300;
                break;
            }
            case 57: {
                // chi mang
                max = 200;
                break;
            }
            case 58: {
                // tan cong khi danh chi mang
                max = 300;
                break;
            }
            case 59: {
                // ty le suc mang tiem nang
                max = 500;
                break;
            }
            case 60: {
                // ne don
                max = 100;
                break;
            }
            case 61: {
                // % xu
                max = 500;
                break;
            }
            case 62: {
                // chinh xác
                max = 500;
                break;
            }
            case 63: {
                // xuyen giap
                max = 300;
                break;
            }
            case 64: {
                // hut mau
                max = 200;
                break;
            }
            case 65: {
                // phan don
                max = 200;
                break;
            }
            case 66: {
                // hut ki
                max = 200;
                break;
            }
        }
        if (isMax) {
            return new ItemOption(optionId, max);
        }
        int min = max / 10;
        int per = Utils.nextInt(1000);
        if (isLucky) {
            per /= 3;
        }
        if (per > 700) {
            max = max / 3;
        } else if (per > 0) {
            min = max / 3;
            max = max * 2 / 3;
        } else {
            min = (max - max / 5);
        }
        return new ItemOption(optionId, Utils.nextInt(min, max));
    }

    public long getTotalOption() {
        long point = 0;
        int upgrade = 0;
        int star = 0;
        for (ItemOption option : options) {
            if (option.template.type == 4) {
                continue;
            }
            int id = option.template.id;
            if (id == 50 || id == 36 || id == 131) {
                continue;
            }
            if (id == 19) {
                upgrade = option.param;
            } else if (id == 67 || id == 68) {
                star += option.param;
            } else {
                point += option.param;
            }
        }
        if (upgrade > 0) {
            point *= upgrade;
        }
        if (star > 0) {
            point *= star;
        }
        return point;
    }

    public JSONObject toJsonObject() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("id", template.id);
        if (quantity > 1) {
            jsonObject.put("quantity", quantity);
        }
        if (!options.isEmpty()) {
            int[][] prams = new int[options.size()][2];
            for (int i = 0; i < prams.length; i++) {
                ItemOption option = options.get(i);
                prams[i][0] = option.template.id;
                prams[i][1] = option.param;
            }
            jsonObject.put("options", prams);
        }
        if (isLock) {
            jsonObject.put("is_lock", true);
        }
        return jsonObject;
    }
}
