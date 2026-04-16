package com.beemobi.rongthanonline.data;

import com.beemobi.rongthanonline.bot.disciple.DiscipleStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "rto_disciple")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DiscipleData {
    @Id
    @Column(name = "id")
    public Integer id;

    @Column(name = "name")
    public String name;

    @Column(name = "gender")
    public Integer gender;

    @Column(name = "base_info")
    public String baseInfo;

    @Column(name = "power_info")
    public String powerInfo;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    public DiscipleStatus status;

    @Column(name = "item_body")
    public String itemsBody = "";

    @Column(name = "item_other")
    public String itemsOther = "";

    @Column(name = "skill_info")
    public String skillsInfo;

    @Column(name = "map_info")
    public String mapInfo;

    @Column(name = "type")
    public Integer type;

    @Column(name = "create_time")
    public Timestamp createTime;
}
