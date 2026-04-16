package com.beemobi.rongthanonline.data;

import com.beemobi.rongthanonline.entity.player.Player;
import com.beemobi.rongthanonline.server.Server;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "rto_clan_member")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ClanMemberData {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    public Long id;

    @Column(name = "clan_id")
    public Integer clanId;

    @Column(name = "server")
    public Integer server;

    @Column(name = "player_id")
    public Integer playerId;

    @Column(name = "name")
    public String name;

    @Column(name = "role")
    public Integer role;

    @Column(name = "gender")
    public Integer gender;

    @Column(name = "power")
    public Long power;

    @Column(name = "point")
    public String point;

    @Column(name = "join_time")
    public Timestamp joinTime;

    public ClanMemberData(Player player, int clanId, int role) {
        server = Server.ID;
        this.clanId = clanId;
        playerId = player.id;
        name = player.name;
        this.role = role;
        gender = player.gender;
        power = player.power;
        point = "{\"total\":0,\"day\":0}";
        joinTime = new Timestamp(System.currentTimeMillis());
    }

}
