package com.beemobi.rongthanonline.data;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "rto_player")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PlayerData {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    public Integer id;

    @Column(name = "user_id")
    public Integer userId;

    @Column(name = "server")
    public Integer server;

    @Column(name = "name")
    public String name;

    @Column(name = "name_base")
    public String nameBase;

    @Column(name = "gender")
    public Integer gender;

    @Column(name = "base_info")
    public String baseInfo;

    @Column(name = "power_info")
    public String powerInfo;

    @Column(name = "pro")
    public Long pro;

    @Column(name = "pin")
    public String pin;

    @Column(name = "diamond")
    public Integer diamond;

    @Column(name = "ruby")
    public Integer ruby;

    @Column(name = "is_lock")
    public Boolean isLock;

    @Column(name = "xu")
    public Long xu;

    @Column(name = "xu_khoa")
    public Long xuKhoa;

    @Column(name = "clan_info")
    public String clanInfo;

    @Column(name = "item_bag")
    public String itemsBag = "[null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null]";

    @Column(name = "item_body")
    public String itemsBody = "[null,null,null,null,null,null,null,null,null,null,null,null,null,null]";

    @Column(name = "item_box")
    public String itemsBox = "[null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null]";

    @Column(name = "item_pet")
    public String itemsPet = "[null,null,null,null,null,null,null,null,null,null,null,null,null,null]";

    @Column(name = "item_other")
    public String itemsOther = "[null,null,null,null,null,null,null,null,null,null,null,null,null,null]";

    @Column(name = "task_info")
    public String taskInfo;

    @Column(name = "skill_info")
    public String skillsInfo;

    @Column(name = "key_skill")
    public String keysSkill;

    @Column(name = "intrinsic")
    public String intrinsic;

    @Column(name = "map_info")
    public String mapInfo;

    @Column(name = "barrack_info")
    public String barrackInfo;

    @Column(name = "friend")
    public String friend;

    @Column(name = "enemy")
    public String enemy;

    @Column(name = "effect")
    public String effect;

    @Column(name = "magic_bean")
    public String magicBean;

    @Column(name = "task_daily")
    public String taskDaily;

    @Column(name = "point")
    public String point;

    @Column(name = "online_info")
    public String onlineInfo;

    @Column(name = "spaceship")
    public Integer spaceship;

    @Column(name = "recharge")
    public String recharge;

    @Column(name = "mission_week")
    public String missionWeek;

    @Column(name = "mission_daily")
    public String missionDaily;

    @Column(name = "mission_recharge")
    public String missionRecharge;

    @Column(name = "mission_event")
    public String missionEvent;

    @Column(name = "achievement")
    public String achievement;

    @Column(name = "event")
    public String event;

    @Column(name = "is_online")
    public Boolean isOnline;

    @Column(name = "lock_info")
    public String lockInfo;

    @Column(name = "npc_tree")
    public String npcTree;

    @Column(name = "training_offline")
    public String trainingOffline;

    @Column(name = "reset_time")
    public Timestamp resetTime;

    @Column(name = "login_time")
    public Timestamp loginTime;

    @Column(name = "logout_time")
    public Timestamp logoutTime;

    @Column(name = "create_time")
    public Timestamp createTime;
}
