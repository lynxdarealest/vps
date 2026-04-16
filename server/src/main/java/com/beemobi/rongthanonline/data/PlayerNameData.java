package com.beemobi.rongthanonline.data;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "rto_player_name")
@Getter
@Setter
public class PlayerNameData {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    public Long id;

    @Column(name = "player_id")
    public Integer playerId;

    @Column(name = "name")
    public String name;

    @Column(name = "name_base")
    public String nameBase;

    @Column(name = "create_time")
    @CreationTimestamp
    public Timestamp createTime;
}
