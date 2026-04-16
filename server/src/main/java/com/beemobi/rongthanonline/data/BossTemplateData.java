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
@Table(name = "rto_boss_template")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BossTemplateData {
    @Id
    @Column(name = "id")
    public Integer id;

    @Column(name = "name")
    public String name;

    @Column(name = "hp")
    public Long hp;

    @Column(name = "head")
    public Integer head;

    @Column(name = "body")
    public Integer body;

    @Column(name = "level")
    public Integer level;

    @Column(name = "damage")
    public Long damage;

    @Column(name = "is_auto_respawn")
    public Boolean isAutoRespawn;
}
