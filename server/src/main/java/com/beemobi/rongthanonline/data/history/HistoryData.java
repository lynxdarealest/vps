package com.beemobi.rongthanonline.data.history;

import com.beemobi.rongthanonline.data.PlayerData;
import com.beemobi.rongthanonline.entity.player.Player;
import com.beemobi.rongthanonline.history.HistoryType;
import com.beemobi.rongthanonline.server.Server;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.sql.Timestamp;

@Getter
@Setter
@MappedSuperclass
public class HistoryData {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    public Long id;

    @Column(name = "type")
    @Enumerated(EnumType.STRING)
    public HistoryType type;

    @Column(name = "server")
    public Integer server;

    @Column(name = "player_id")
    public Integer playerId;

    @Column(name = "ip")
    public String ip;

    @Column(name = "create_time")
    public Timestamp createTime;

    public HistoryData(Player player, HistoryType type) {
        this.type = type;
        this.server = Server.ID;
        this.playerId = player.id;
        this.ip = player.getIpAddress();
        this.createTime = new Timestamp(System.currentTimeMillis());
    }

    public HistoryData(PlayerData player, HistoryType type, String ip) {
        this.type = type;
        this.server = Server.ID;
        this.playerId = player.id;
        this.ip = ip;
        this.createTime = new Timestamp(System.currentTimeMillis());
    }
}
