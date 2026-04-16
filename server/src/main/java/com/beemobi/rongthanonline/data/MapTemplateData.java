package com.beemobi.rongthanonline.data;

import com.beemobi.rongthanonline.map.MapPlanet;
import com.beemobi.rongthanonline.map.MapType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "rto_map_template")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MapTemplateData {
    @Id
    @Column(name = "id")
    public Integer id;

    @Column(name = "type")
    @Enumerated(EnumType.STRING)
    public MapType type;

    @Column(name = "name")
    public String name;

    @Column(name = "planet")
    @Enumerated(EnumType.STRING)
    public MapPlanet planet;

    @Column(name = "max_zone")
    public Integer maxZone;

    @Column(name = "min_zone")
    public Integer minZone;

    @Column(name = "max_player")
    public Integer maxPlayer;

    @Column(name = "row")
    public Integer row;

    @Column(name = "column")
    public Integer column;

    @Column(name = "data")
    public String data;

    @Column(name = "images_bgr")
    public String imagesBgr;

    @Column(name = "colors_bgr")
    public String colorsBgr;

    @Column(name = "icon_id")
    public Integer iconId;
}
