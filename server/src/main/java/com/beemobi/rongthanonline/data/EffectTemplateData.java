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
@Table(name = "rto_effect_template")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EffectTemplateData {
    @Id
    @Column(name = "id")
    public Integer id;

    @Column(name = "name")
    public String name;

    @Column(name = "icon_id")
    public Integer iconId;

    @Column(name = "effect_image_id")
    public Integer effectImageId;

    @Column(name = "is_active_when_online")
    public Boolean isActiveWhenOnline;

    @Column(name = "is_clear_when_die")
    public Boolean isClearWhenDie;

    @Column(name = "is_me")
    public Boolean isMe;

    @Column(name = "is_stun")
    public Boolean isStun;

    @Column(name = "is_save")
    public Boolean isSave;

    @Column(name = "is_refresh_info")
    public Boolean isRefreshInfo;

    @Column(name = "is_refresh_part")
    public Boolean isRefreshPart;

}
