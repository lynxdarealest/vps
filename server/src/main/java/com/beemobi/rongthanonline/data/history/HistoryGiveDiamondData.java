package com.beemobi.rongthanonline.data.history;

import com.beemobi.rongthanonline.entity.player.Player;
import com.beemobi.rongthanonline.history.HistoryType;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "rto_history_give_diamond")
@Getter
@Setter
public class HistoryGiveDiamondData extends HistoryData {

    @Column(name = "target_id")
    public Integer targetID;

    @Column(name = "diamond_me_before")
    public Integer diamondMeBefore;

    @Column(name = "diamond_me_after")
    public Integer diamondMeAfter;

    @Column(name = "diamond_target_before")
    public Integer diamondTargetBefore;

    @Column(name = "diamond_target_after")
    public Integer diamondTargetAfter;

    @Column(name = "diamond")
    public Integer diamond;

    public HistoryGiveDiamondData(Player player, int targetID, int diamond, int[] before, int[] after) {
        super(player, HistoryType.GIVE_DIAMOND);
        this.targetID = targetID;
        diamondMeBefore = before[0];
        diamondTargetBefore = before[1];
        diamondMeAfter = after[0];
        diamondTargetAfter = after[1];
        this.diamond = diamond;
    }
}
