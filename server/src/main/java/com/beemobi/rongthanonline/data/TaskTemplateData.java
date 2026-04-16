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
@Table(name = "rto_task")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TaskTemplateData {
    @Id
    @Column(name = "id")
    public Integer id;

    @Column(name = "name")
    public String name;

    @Column(name = "from_npc_id")
    public Integer fromNpcId;

    @Column(name = "items")
    public String items;

    @Column(name = "steps")
    public String steps;
}
