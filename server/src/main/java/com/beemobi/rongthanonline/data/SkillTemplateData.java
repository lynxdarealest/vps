package com.beemobi.rongthanonline.data;

import com.beemobi.rongthanonline.skill.SkillType;
import com.beemobi.rongthanonline.skill.SkillTypeMana;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "rto_skill_template")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SkillTemplateData implements Cloneable {
    @Id
    @Column(name = "id")
    public Integer id;

    @Column(name = "name")
    public String name;

    @Column(name = "description")
    public String description;

    @Column(name = "max_level")
    public String maxLevel;

    @Column(name = "icon_id")
    public String iconId;

    @Column(name = "is_proactive")
    public Boolean isProactive;

    @Column(name = "level_require")
    public String levelRequire;

    @Column(name = "dx")
    public String dx;

    @Column(name = "dy")
    public String dy;

    @Column(name = "cool_down")
    public String coolDown;

    @Column(name = "type_mana")
    @Enumerated(EnumType.STRING)
    public SkillTypeMana typeMana;

    @Column(name = "mana")
    public String mana;

    @Column(name = "type")
    @Enumerated(EnumType.STRING)
    public SkillType type;

    @Column(name = "item")
    public String item;

    @Column(name = "day_upgrade")
    public String dayUpgrade;

    @Column(name = "percent_upgrade")
    public String percentUpgrade;

    @Column(name = "diamond_upgrade")
    public Integer diamondUpgrade;

    @Column(name = "point_upgrade")
    public String pointUpgrade;

    @Column(name = "option_require")
    public Integer optionRequire;

    @Override
    public SkillTemplateData clone() {
        try {
            SkillTemplateData clone = (SkillTemplateData) super.clone();
            // TODO: copy mutable state here, so the clone can't change the internals of the original
            return clone;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
}
