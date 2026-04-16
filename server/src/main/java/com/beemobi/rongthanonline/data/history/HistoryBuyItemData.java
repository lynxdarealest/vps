package com.beemobi.rongthanonline.data.history;

import com.beemobi.rongthanonline.entity.player.Player;
import com.beemobi.rongthanonline.history.HistoryType;
import com.beemobi.rongthanonline.item.Item;
import com.beemobi.rongthanonline.item.ItemShop;
import com.beemobi.rongthanonline.repository.GameRepository;
import com.beemobi.rongthanonline.shop.TypePrice;
import com.beemobi.rongthanonline.util.Utils;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "rto_history_buy_item")
@Getter
@Setter
public class HistoryBuyItemData extends HistoryData {

    @Column(name = "type_price")
    @Enumerated(EnumType.STRING)
    public TypePrice typePrice;

    @Column(name = "coin_before")
    public Long coinBefore;

    @Column(name = "coin_after")
    public Long coinAfter;

    @Column(name = "price")
    public Long price;

    @Column(name = "item_after")
    public String itemsAfter;

    @Column(name = "item")
    public String item;

    public HistoryBuyItemData(Player player, ItemShop itemShop, long[] coin) {
        super(player, HistoryType.BUY_ITEM);
        typePrice = itemShop.typePrice;
        price = (long) itemShop.price;
        this.item = itemShop.toJsonObject().toString();
        itemsAfter = Utils.getJsonArrayItem(player.itemsBag);
        coinBefore = coin[0];
        coinAfter = coin[1];
    }

    public void save() {
        if (typePrice == TypePrice.COIN || typePrice == TypePrice.DIAMOND) {
            GameRepository.getInstance().historyBuyItem.save(this);
        }
    }
}

