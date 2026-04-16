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
@Table(name = "rto_item_weekly")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MissionItemWeeklyTemplateData {
    @Id
    @Column(name = "id")
    public Integer id;

    @Column(name = "rank")
    public Integer rank;

    @Column(name = "type")
    public Integer type;

    @Column(name = "name")
    public String name;

    @Column(name = "description")
    public String description;

    @Column(name = "items")
    public String items;
}
