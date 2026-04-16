package com.beemobi.rongthanonline.skill;

import com.beemobi.rongthanonline.common.RandomCollection;
import com.beemobi.rongthanonline.map.Zone;

public class Skill {
    public SkillTemplate template;
    public int level;
    public int point;
    public int upgrade;
    public long timeCanUse;
    public int coolDownReduction;
    public int coolDownIntrinsic;
    public Zone zone;

    public Skill clone() {
        Skill skill = new Skill();
        skill.timeCanUse = timeCanUse;
        skill.template = template;
        skill.level = level;
        skill.point = point;
        skill.upgrade = upgrade;
        return skill;
    }

    public int getDx() {
        if (level <= 0) {
            return 0;
        }
        if (upgrade > 0) {
            int index = upgrade - 1;
            if (index >= template.dx[1].length) {
                return template.dx[1][template.dx[1].length - 1];
            }
            return template.dx[1][index];
        }
        int index = level - 1;
        if (index >= template.dx[0].length) {
            return template.dx[0][template.dx[0].length - 1];
        }
        return template.dx[0][index];
    }

    public int getDy() {
        if (level <= 0) {
            return 0;
        }
        if (upgrade > 0) {
            int index = upgrade - 1;
            if (index >= template.dy[1].length) {
                return template.dy[1][template.dy[1].length - 1];
            }
            return template.dy[1][index];
        }
        int index = level - 1;
        if (index >= template.dy[0].length) {
            return template.dy[0][template.dy[0].length - 1];
        }
        return template.dy[0][index];
    }

    public int getPercentUpgrade() {
        if (upgrade <= 0) {
            return 100;
        }
        int index = upgrade - 1;
        if (index >= template.percentUpgrade.length) {
            return template.percentUpgrade[template.percentUpgrade.length - 1];
        }
        return template.percentUpgrade[index];
    }

    public int getDiamondUpgrade() {
        return template.diamondUpgrade + 10 * upgrade;
    }

    public String getName() {
        if (upgrade > 0) {
            return template.name[template.name.length - 1];
        }
        return template.name[0];
    }

    public int getPointUpgrade() {
        int index = upgrade;
        if (index >= template.pointUpgrade.length) {
            return template.pointUpgrade[template.pointUpgrade.length - 1];
        }
        return template.pointUpgrade[index];
    }

    public int getManaUse() {
        if (level <= 0) {
            return 0;
        }
        if (upgrade > 0) {
            int index = upgrade - 1;
            if (index >= template.mana[1].length) {
                return template.mana[1][template.mana[1].length - 1];
            }
            return template.mana[1][index];
        }
        int index = level - 1;
        if (index >= template.mana[0].length) {
            return template.mana[0][template.mana[0].length - 1];
        }
        return template.mana[0][index];
    }

    public long getCoolDown() {
        long time = getCoolDownTemplate();
        if (coolDownReduction > 0) {
            time -= time * coolDownReduction / 100;
        }
        if (coolDownIntrinsic > 0) {
            time -= time * coolDownIntrinsic / 100;
        }
        if (time < 100) {
            time = 100;
        }
        return time;
    }

    public long getCoolDownTemplate() {
        if (level <= 0) {
            return 0;
        }
        if (upgrade > 0) {
            int index = upgrade - 1;
            if (index >= template.coolDown[1].length) {
                return template.coolDown[1][template.coolDown[1].length - 1];
            }
            return template.coolDown[1][index];
        }
        int index = level - 1;
        if (index >= template.coolDown[0].length) {
            return template.coolDown[0][template.coolDown[0].length - 1];
        }
        return template.coolDown[0][index];
    }

    public int[] getQuantityItem() {
        int index = level + upgrade;
        int[] quantities = new int[2];
        if (index < template.item[0].length) {
            quantities[0] = template.item[0][index];
        } else {
            quantities[0] = template.item[0][template.item[0].length - 1];
        }
        if (index < template.item[1].length) {
            quantities[1] = template.item[1][index];
        } else {
            quantities[1] = template.item[1][template.item[1].length - 1];
        }
        return quantities;
    }

    public int getParam(int optionId) {
        if (level <= 0) {
            return 0;
        }
        for (SkillOption option : this.template.options) {
            if (option.template.id == optionId) {
                return option.getParam(level, upgrade);
            }
        }
        return 0;
    }

    public boolean isSkillUltimate() {
        return template.id == SkillName.QUA_CAU_GENKI || template.id == SkillName.TU_PHAT_NO || template.id == SkillName.LAZE;
    }

    public boolean isCanHapThu() {
        return template.id == SkillName.KAME || template.id == SkillName.MASENDAN || template.id == SkillName.SOKIDAN || template.id == SkillName.QUA_CAU_GENKI || template.id == SkillName.LAZE || template.id == SkillName.KAME_DISCIPLE || template.id == SkillName.MASENDAN_DISCIPLE || template.id == SkillName.SOKIDAN_DISCIPLE;
    }

    public RandomCollection<Integer> getListPaint() {
        RandomCollection<Integer> paints = new RandomCollection<>();
        switch (template.id) {
            case SkillName.KARAV_DISCIPLE:
            case SkillName.KARAV:
                if (upgrade > 0) {
                    paints.add(50, 2);
                    paints.add(30, 3);
                    paints.add(10, 4);
                    paints.add(10, 5);
                } else if (level >= template.maxLevel[0]) {
                    paints.add(50, 2);
                    paints.add(50, 3);
                } else {
                    paints.add(50, 0);
                    paints.add(50, 1);
                }
                break;

            case SkillName.KARAP_DISCIPLE:
            case SkillName.KARAP:
                if (upgrade > 0) {
                    paints.add(50, 14);
                    paints.add(30, 15);
                    paints.add(10, 16);
                    paints.add(10, 17);
                } else if (level >= template.maxLevel[0]) {
                    paints.add(50, 14);
                    paints.add(50, 15);
                } else {
                    paints.add(50, 0);
                    paints.add(50, 1);
                }
                break;

            case SkillName.KARAK_DISCIPLE:
            case SkillName.KARAK:
                if (upgrade > 0) {
                    paints.add(50, 22);
                    paints.add(30, 23);
                    paints.add(10, 24);
                    paints.add(10, 25);
                } else if (level >= template.maxLevel[0]) {
                    paints.add(50, 22);
                    paints.add(50, 23);
                } else {
                    paints.add(50, 0);
                    paints.add(50, 1);
                }
                break;

            case SkillName.KAME_DISCIPLE:
            case SkillName.KAME:
                if (level < 4) {
                    paints.add(100, 32);
                } else if (level < 7) {
                    paints.add(100, 33);
                } else {
                    paints.add(100, 34);
                }
                break;

            case SkillName.MASENDAN_DISCIPLE:
            case SkillName.MASENDAN:
                if (level < 4) {
                    paints.add(100, 26);
                } else if (level < 7) {
                    paints.add(100, 27);
                } else {
                    paints.add(100, 28);
                }
                break;

            case SkillName.SOKIDAN_DISCIPLE:
            case SkillName.SOKIDAN:
                if (level < 4) {
                    paints.add(100, 29);
                } else if (level < 7) {
                    paints.add(100, 30);
                } else {
                    paints.add(100, 31);
                }
                break;

            case SkillName.THAI_DUONG_HA_SAN:
                paints.add(100, 36);
                break;

            case SkillName.KHIEN_NANG_LUONG:
            case SkillName.TAI_TAO_NANG_LUONG:
                paints.add(100, 37);
                break;

            case SkillName.TRI_THUONG:
                paints.add(100, 38);
                break;

            case SkillName.LAZE:
                paints.add(100, 41);
                break;

            case SkillName.TU_PHAT_NO:
                paints.add(100, 40);
                break;

            case SkillName.QUA_CAU_GENKI:
                paints.add(100, 39);
                break;

            case SkillName.HOA_KHONG_LO:
                paints.add(100, 43);
                break;

            case SkillName.BIEN_KHI:
                paints.add(100, 42);
                break;

            case SkillName.KAIOKEN:
                paints.add(100, 44);
                break;

            case SkillName.THOI_MIEN:
                paints.add(100, 45);
                break;

            case SkillName.DICH_CHUYEN_TUC_THOI:
                paints.add(10, 5);
                paints.add(10, 17);
                paints.add(10, 25);
                break;

            case SkillName.SUPER_KAME:
                if (upgrade > 0) {
                    paints.add(100, 48);
                } else {
                    paints.add(100, 35);
                }
                break;

            case SkillName.MA_PHONG_BA:
                if (upgrade > 0) {
                    paints.add(100, 50);
                } else {
                    paints.add(100, 47);
                }
                break;

            case SkillName.BIGBANG_FLASH:
                if (upgrade > 0) {
                    paints.add(100, 49);
                } else {
                    paints.add(100, 46);
                }
                break;
        }
        return paints;
    }
}
