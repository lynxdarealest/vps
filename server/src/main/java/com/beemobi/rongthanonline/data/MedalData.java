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
@Table(name = "rto_medal")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MedalData {
    @Id
    @Column(name = "id")
    public Integer id;

    @Column(name = "description")
    public String description;

    @Column(name = "icons")
    public String icons;

    @Column(name = "dx")
    public Integer dx;

    @Column(name = "dy")
    public Integer dy;

    @Column(name = "delay")
    public Integer delay;
}
