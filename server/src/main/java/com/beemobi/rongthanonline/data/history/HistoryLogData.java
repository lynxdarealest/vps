package com.beemobi.rongthanonline.data.history;

import com.beemobi.rongthanonline.data.PlayerData;
import com.beemobi.rongthanonline.entity.player.Player;
import com.beemobi.rongthanonline.history.HistoryType;
import com.beemobi.rongthanonline.util.Utils;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "rto_history_log")
@Getter
@Setter
public class HistoryLogData extends HistoryData {

    @Column(name = "xu")
    public Long xu;

    @Column(name = "xu_khoa")
    public Long xuKhoa;

    @Column(name = "diamond")
    public Integer diamond;

    @Column(name = "ruby")
    public Integer ruby;

    @Column(name = "item_bag")
    public String itemsBag;

    @Column(name = "item_box")
    public String itemsBox;

    @Column(name = "item_body")
    public String itemsBody;

    @Column(name = "item_other")
    public String itemsOther;

    public HistoryLogData(Player player, HistoryType type) {
        super(player, type);
        this.xu = player.xu;
        this.xuKhoa = player.xuKhoa;
        this.diamond = player.diamond;
        this.ruby = player.ruby;
        this.itemsBag = Utils.getJsonArrayItem(player.itemsBag);
        this.itemsBox = Utils.getJsonArrayItem(player.itemsBox);
        this.itemsBody = Utils.getJsonArrayItem(player.itemsBody);
        this.itemsOther = Utils.getJsonArrayItem(player.itemsOther);
    }
}
