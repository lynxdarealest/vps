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
@Table(name = "rto_item_template")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ItemTemplateData {
    @Id
    @Column(name = "id")
    public Integer id;

    @Column(name = "type")
    public Integer type;

    @Column(name = "gender")
    public Integer gender;

    @Column(name = "name")
    public String name;

    @Column(name = "description")
    public String description;

    @Column(name = "icon_id")
    public Integer iconId;

    @Column(name = "head")
    public Integer head;

    @Column(name = "body")
    public Integer body;

    @Column(name = "head_disciple")
    public Integer headDisciple;

    @Column(name = "body_disciple")
    public Integer bodyDisciple;

    @Column(name = "medal")
    public Integer medal;

    @Column(name = "aura")
    public Integer aura;

    @Column(name = "mount")
    public Integer mount;

    @Column(name = "bag")
    public Integer bag;

    @Column(name = "is_up")
    public Boolean isUp;

    @Column(name = "level_require")
    public Integer levelRequire;

    @Column(name = "is_lock")
    public Boolean isLock;

    @Column(name = "max_quantity")
    public Integer maxQuantity;

    @Column(name = "is_master")
    public Boolean isMaster;

    @Column(name = "is_disciple")
    public Boolean isDisciple;

    @Column(name = "is_pet")
    public Boolean isPet;

}
