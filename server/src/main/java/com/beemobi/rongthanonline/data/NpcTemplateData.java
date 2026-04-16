package com.beemobi.rongthanonline.data;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "rto_npc_template")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class NpcTemplateData {
    @Id
    @Column(name = "id")
    public Integer id;

    @Column(name = "name")
    public String name;

    @Column(name = "icons")
    public String icons;

    @Column(name = "avatar")
    public Integer avatar;

    @Column(name = "w")
    public Integer w;

    @Column(name = "h")
    public Integer h;

    @Column(name = "dx")
    public Integer dx;

    @Column(name = "dy")
    public Integer dy;
}
