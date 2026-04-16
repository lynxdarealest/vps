package com.beemobi.rongthanonline.data;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "rto_gift")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GiftData {
    @Id
    @Column(name = "id")
    public Integer id;

    @Column(name = "item_id")
    public Integer itemId;

    @Column(name = "quantity")
    public Integer quantity;

    @Column(name = "is_lock")
    public Boolean isLock;

    @Column(name = "is_max_param")
    public Boolean isMaxParam;

    @Column(name = "expiry")
    public Integer expiry;

    @Column(name = "is_default_option")
    public Boolean isDefaultOption;

    @Column(name = "option")
    public String option;
}
