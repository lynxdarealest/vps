package com.beemobi.rongthanonline.data;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "rto_order")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderData {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    public Long id;

    @Column(name = "server")
    public Integer server;

    @Column(name = "user_id")
    public Integer userId;

    @Column(name = "type")
    public Integer type;

    @Column(name = "diamond")
    public Integer diamond;

    @Column(name = "coin")
    public Long coin;

    @Column(name = "status")
    public Integer status;

    @Column(name = "order_id")
    public Long orderId;

    @Column(name = "order_code")
    public String orderCode;

    @Column(name = "update_time")
    public Date updateTime;

    @Column(name = "create_time")
    @CreationTimestamp
    public Date createTime;
}
