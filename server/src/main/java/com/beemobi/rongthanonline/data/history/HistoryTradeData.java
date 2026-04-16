package com.beemobi.rongthanonline.data.history;

import com.beemobi.rongthanonline.entity.player.Player;
import com.beemobi.rongthanonline.history.HistoryType;
import com.beemobi.rongthanonline.item.Item;
import com.beemobi.rongthanonline.repository.GameRepository;
import com.beemobi.rongthanonline.util.Utils;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.List;

@Entity
@Table(name = "rto_history_trade")
@Getter
@Setter
public class HistoryTradeData extends HistoryData {

    @Column(name = "target_id")
    public Integer targetId;

    @Column(name = "target_name")
    public String targetName;

    @Column(name = "item_send")
    public String itemsSend;

    @Column(name = "item_receive")
    public String itemsReceive;

    @Column(name = "xu_send")
    public Long xuSend;

    @Column(name = "xu_receive")
    public Long xuReceive;

    @Column(name = "map_id")
    public Integer mapId;

    @Column(name = "zone_id")
    public Integer zoneId;

    @Column(name = "item_before")
    public String itemsBefore;

    @Column(name = "item_after")
    public String itemsAfter;

    public HistoryTradeData(Player player, Player target) {
        super(player, HistoryType.TRADE);
        this.itemsBefore = Utils.getJsonArrayItem(player.itemsBag);
        this.mapId = player.zone.map.template.id;
        this.zoneId = player.zone.id;
        this.targetId = target.id;
        this.targetName = target.name;
    }

    public void setItemTrade(List<Item> sends, long bacSend, List<Item> receives, long bacReceive) {
        this.itemsSend = Utils.getJsonListItem(sends);
        this.itemsReceive = Utils.getJsonListItem(receives);
        this.xuSend = bacSend;
        this.xuReceive = bacReceive;
    }

    public void setAfterAndSave(Player player) {
        this.itemsAfter = Utils.getJsonArrayItem(player.itemsBag);
        save();
    }

    public void save() {
        GameRepository.getInstance().historyTrade.save(this);
    }
}
