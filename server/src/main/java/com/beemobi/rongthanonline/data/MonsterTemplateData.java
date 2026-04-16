package com.beemobi.rongthanonline.data;

import com.beemobi.rongthanonline.entity.monster.MonsterTypeMove;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "rto_monster_template")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MonsterTemplateData {

    @Id
    @Column(name = "id")
    public Integer id;

    @Column(name = "speed")
    public Integer speed;

    @Column(name = "range_move")
    public Integer rangeMove;

    @Column(name = "type_move")
    @Enumerated(EnumType.STRING)
    public MonsterTypeMove typeMove;

    @Column(name = "hp")
    public Long hp;

    @Column(name = "name")
    public String name;

    @Column(name = "level")
    public Integer level;

    @Column(name = "damage")
    public Integer damage;

    @Column(name = "icon_move")
    public String iconsMove;

    @Column(name = "icon_attack")
    public Integer iconsAttack;

    @Column(name = "icon_injure")
    public Integer iconsInjure;

    @Column(name = "dart_id")
    public Integer dartId;

    @Column(name = "w")
    public Integer w;

    @Column(name = "h")
    public Integer h;

    @Column(name = "dx")
    public Integer dx;

    @Column(name = "dy")
    public Integer dy;
}
