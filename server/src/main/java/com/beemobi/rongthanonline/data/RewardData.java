package com.beemobi.rongthanonline.data;

import com.beemobi.rongthanonline.entity.player.Player;
import com.beemobi.rongthanonline.item.Item;
import com.beemobi.rongthanonline.model.Reward;
import com.beemobi.rongthanonline.server.Server;
import com.beemobi.rongthanonline.util.Utils;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.List;

@Entity
@Table(name = "rto_reward")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RewardData {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    public Long id;

    @Column(name = "server")
    public Integer server;

    @Column(name = "player_id")
    public Integer playerId;

    @Column(name = "items")
    public String items;

    @Column(name = "info")
    public String info;

    @Column(name = "reward_time")
    public Timestamp rewardTime;

    @Column(name = "expiry_time")
    public Timestamp expiryTime;

    @Column(name = "create_time")
    public Timestamp createTime;

    public RewardData(int playerId, String info, List<Item> items) {
        server = Server.ID;
        this.playerId = playerId;
        this.info = info;
        this.items = Utils.getJsonListItem(items);
        long now = System.currentTimeMillis();
        expiryTime = new Timestamp(now + 604800000L);
        createTime = new Timestamp(now);
    }

}
