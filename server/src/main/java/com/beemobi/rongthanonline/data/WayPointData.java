package com.beemobi.rongthanonline.data;

import com.beemobi.rongthanonline.model.waypoint.WayPointType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "rto_map_waypoint")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class WayPointData {
    @Id
    @Column(name = "id")
    public Integer id;

    @Column(name = "map_id")
    public Integer mapId;

    @Column(name = "x")
    public Integer x;

    @Column(name = "y")
    public Integer y;

    @Column(name = "type")
    @Enumerated(EnumType.STRING)
    public WayPointType type;

    @Column(name = "go_map")
    public Integer goMap;

    @Column(name = "go_x")
    public Integer goX;

    @Column(name = "go_y")
    public Integer goY;
}
