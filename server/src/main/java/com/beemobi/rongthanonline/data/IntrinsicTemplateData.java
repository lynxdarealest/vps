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
@Table(name = "rto_intrinsic_template")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class IntrinsicTemplateData {
    @Id
    @Column(name = "id")
    public Integer id;

    @Column(name = "name")
    public String name;

    @Column(name = "icon")
    public Integer icon;

    @Column(name = "skill_template_id")
    public Integer skillTemplateId;

    @Column(name = "min")
    public Integer min;

    @Column(name = "max")
    public Integer max;

    @Column(name = "gender")
    public Integer gender;

    @Column(name = "is_upgrade")
    public Boolean isUpgrade;

    @Column(name = "is_cool_down")
    public Boolean isCoolDown;

    @Column(name = "price_diamond")
    public Integer priceDiamond;

    @Column(name = "price_coin")
    public Integer priceCoin;
}
