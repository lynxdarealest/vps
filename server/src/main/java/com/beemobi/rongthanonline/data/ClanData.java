package com.beemobi.rongthanonline.data;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "rto_clan")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ClanData {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    public Integer id;

    @Column(name = "server")
    public Integer server;

    @Column(name = "name", unique = true)
    public String name;

    @Column(name = "slogan")
    public String slogan;

    @Column(name = "notification")
    public String notification;

    @Column(name = "level")
    public Integer level;

    @Column(name = "leader_id")
    public Integer leaderId;

    @Column(name = "bonus_slot")
    public Integer bonusSlot;

    @Column(name = "exp")
    public Long exp;

    @Column(name = "coin")
    public Long coin;

    @Column(name = "count_manor")
    public Integer countManor;

    @Column(name = "reset_time")
    public Timestamp resetTime;

    @Column(name = "update_time")
    public Timestamp updateTime;

    @Column(name = "create_time")
    public Timestamp createTime;
}
