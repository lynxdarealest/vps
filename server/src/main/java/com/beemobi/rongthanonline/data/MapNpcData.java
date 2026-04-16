package com.beemobi.rongthanonline.data;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "rto_map_npc")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MapNpcData {
    @Id
    @Column(name = "id")
    public Integer id;

    @Column(name = "map_id")
    public Integer mapId;

    @Column(name = "x")
    public Integer x;

    @Column(name = "y")
    public Integer y;

    @Column(name = "npc_id")
    public Integer npcId;
}
