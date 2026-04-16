package com.beemobi.rongthanonline.data;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "rto_skill_option")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SkillOptionData  implements Cloneable{
    @Id
    @Column(name = "id")
    public Integer id;

    @Column(name = "template_id")
    public Integer templateId;

    @Column(name = "option_id")
    public Integer optionId;

    @Column(name = "param")
    public String paramNormal;

    @Column(name = "param_upgrade")
    public String paramUpgrade;

    @Override
    public SkillOptionData clone() {
        try {
            SkillOptionData clone = (SkillOptionData) super.clone();
            // TODO: copy mutable state here, so the clone can't change the internals of the original
            return clone;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
}
