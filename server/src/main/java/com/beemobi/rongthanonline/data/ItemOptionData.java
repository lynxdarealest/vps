package com.beemobi.rongthanonline.data;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "rto_item_option")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ItemOptionData {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    public Integer id;

    @Column(name = "template_id")
    public Integer templateId;

    @Column(name = "option_id")
    public Integer optionId;

    @Column(name = "param")
    public Integer param;
}
