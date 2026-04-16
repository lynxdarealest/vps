package com.beemobi.rongthanonline.data;

import com.beemobi.rongthanonline.entity.player.Player;
import com.beemobi.rongthanonline.entity.player.json.ItemInfo;
import com.beemobi.rongthanonline.entity.player.json.ItemOptionInfo;
import com.beemobi.rongthanonline.item.Item;
import com.beemobi.rongthanonline.item.ItemConsignment;
import com.beemobi.rongthanonline.item.ItemConsignmentStatus;
import com.beemobi.rongthanonline.item.ItemOption;
import com.beemobi.rongthanonline.server.Server;
import com.beemobi.rongthanonline.util.Utils;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "rto_consignment")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ItemConsignmentData {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    public Long id;

    @Column(name = "server")
    public Integer server;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    public ItemConsignmentStatus status;

    @Column(name = "seller_id")
    public Integer sellerId;

    @Column(name = "seller_name")
    public String sellerName;

    @Column(name = "buyer_id")
    public Integer buyerId;

    @Column(name = "buyer_name")
    public String buyerName;

    @Column(name = "item")
    public String item;

    @Column(name = "price")
    public Integer price;

    @Column(name = "buy_time")
    public Timestamp buyTime;

    @Column(name = "receive_time")
    public Timestamp receiveTime;

    @Column(name = "create_time")
    public Timestamp createTime;

    public ItemConsignmentData(Player player, Item item, int quantity, int price) {
        server = Server.ID;
        status = ItemConsignmentStatus.ON_SALE;
        sellerId = player.id;
        sellerName = player.name;
        buyerId = -1;
        this.price = price;
        createTime = new Timestamp(System.currentTimeMillis());
        Item newItem = item.cloneItem();
        newItem.quantity = quantity;
        this.item = newItem.toJsonObject().toString();
    }

}
