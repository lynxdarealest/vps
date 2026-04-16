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
@Table(name = "rto_achievement")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AchievementTemplateData {
    @Id
    @Column(name = "id")
    public Integer id;

    @Column(name = "name")
    public String name;

    @Column(name = "description")
    public String description;

    @Column(name = "max_param")
    public Integer maxParam;

    @Column(name = "ruby")
    public Integer ruby;
}
