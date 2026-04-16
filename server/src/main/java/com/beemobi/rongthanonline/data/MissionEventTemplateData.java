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
@Table(name = "rto_mission_event")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MissionEventTemplateData {
    @Id
    @Column(name = "id")
    public Integer id;

    @Column(name = "name")
    public String name;

    @Column(name = "param")
    public Integer param;

    @Column(name = "description")
    public String description;

    @Column(name = "items")
    public String items;
}
