package com.beemobi.rongthanonline.data;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.sql.Timestamp;

@Entity
@Table(name = "rto_gift_code")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GiftCodeData {
    @Id
    @Column(name = "id")
    public Integer id;

    @Column(name = "server")
    public Integer server;

    @Column(name = "code")
    public String code;

    @Column(name = "items")
    public String items;

    @Column(name = "level_require")
    public Integer levelRequire;

    @Column(name = "task_require")
    public Integer taskRequire;

    @Column(name = "active_point_require")
    public Integer activePointRequire;

    @Column(name = "expiry_time")
    public Timestamp expiryTime;

    @Column(name = "create_time")
    public Timestamp createTime;
}
