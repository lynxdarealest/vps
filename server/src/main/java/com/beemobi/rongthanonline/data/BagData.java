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
@Table(name = "rto_bag")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BagData {
    @Id
    @Column(name = "id")
    public Integer id;

    @Column(name = "description")
    public String description;

    @Column(name = "icons")
    public String icons;

    @Column(name = "dx_fly")
    public Integer dxFly;

    @Column(name = "dy_fly")
    public Integer dyFly;

    @Column(name = "delay")
    public Integer delay;

    @Column(name = "is_fly")
    public Boolean isFly;
}
