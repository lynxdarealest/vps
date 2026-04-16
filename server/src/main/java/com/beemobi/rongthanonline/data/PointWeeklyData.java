package com.beemobi.rongthanonline.data;

import com.beemobi.rongthanonline.model.PointWeekly;
import com.beemobi.rongthanonline.server.Server;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.sql.Timestamp;
import java.time.LocalDate;

@Entity
@Table(name = "rto_weekly")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PointWeeklyData {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    public Long id;

    @Column(name = "start_week")
    public LocalDate startWeek;

    @Column(name = "server")
    public Integer server;

    @Column(name = "player_id")
    public Integer playerId;

    @Column(name = "name")
    public String name;

    @Column(name = "gender")
    public Integer gender;

    @Column(name = "active_point")
    public Long activePoint;

    @Column(name = "active_time")
    public Timestamp activeTime;

    @Column(name = "event_point")
    public Long eventPoint;

    @Column(name = "event_time")
    public Timestamp eventTime;

    @Column(name = "event_other_point")
    public Long eventOtherPoint;

    @Column(name = "event_other_time")
    public Timestamp eventOtherTime;

    @Column(name = "boss_point")
    public Long bossPoint;

    @Column(name = "boss_time")
    public Timestamp bossTime;

    public PointWeeklyData(PointWeekly point) {
        this.startWeek = point.startWeek;
        this.server = Server.ID;
        this.playerId = point.player.id;
        this.name = point.player.name;
        this.gender = point.player.gender;
        this.activePoint = point.activePoint;
        this.activeTime = point.activeTime;
        this.eventPoint = point.eventPoint;
        this.eventTime = point.eventTime;
        this.eventOtherPoint = point.eventOtherPoint;
        this.eventOtherTime = point.eventOtherTime;
        this.bossPoint = point.bossPoint;
        this.bossTime = point.bossTime;
    }

    public PointWeeklyData(LocalDate startWeek) {
        this.startWeek = startWeek;
        this.server = Server.ID;
        this.playerId = -1;
        this.name = "Hệ thống";
        this.gender = 0;
        this.activePoint = 0L;
        this.eventPoint = 0L;
        this.bossPoint = 0L;
        this.eventOtherPoint = 0L;
    }
}
