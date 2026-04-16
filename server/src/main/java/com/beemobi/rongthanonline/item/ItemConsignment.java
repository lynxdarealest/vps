package com.beemobi.rongthanonline.item;

import com.beemobi.rongthanonline.data.ItemConsignmentData;
import com.beemobi.rongthanonline.entity.player.json.ItemInfo;
import com.beemobi.rongthanonline.entity.player.json.ItemOptionInfo;
import com.beemobi.rongthanonline.repository.GameRepository;
import com.beemobi.rongthanonline.shop.TypePrice;
import com.beemobi.rongthanonline.util.Utils;
import com.google.gson.reflect.TypeToken;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ItemConsignment extends ItemShop {
    public static final long EXPIRY_TIME = 259200000L;
    public static int autoIncrease;
    public long databaseId;
    public int sellerId;
    public String sellerName;
    public int buyerId;
    public String buyerName;
    public ItemConsignmentStatus status;
    public Timestamp createTime;
    public Timestamp buyTime;
    public Timestamp receiveTime;
    public transient boolean isCharge;
    public Lock lock = new ReentrantLock();

    public ItemConsignment(ItemConsignmentData data) {
        id = autoIncrease++;
        databaseId = data.id;
        sellerId = data.sellerId;
        sellerName = data.sellerName;
        buyerId = data.buyerId;
        buyerName = data.buyerName;
        status = data.status;
        createTime = data.createTime;
        buyTime = data.buyTime;
        receiveTime = data.receiveTime;
        price = data.price;
        typePrice = TypePrice.COIN;
        ItemInfo itemInfo = Utils.gson.fromJson(data.item, new TypeToken<ItemInfo>() {
        }.getType());
        template = ItemManager.getInstance().itemTemplates.get(itemInfo.id);
        quantity = Math.max(itemInfo.quantity, 1);
        options = new ArrayList<>();
        if (itemInfo.options != null) {
            for (int[] option : itemInfo.options) {
                options.add(new ItemOption(option[0], option[1]));
            }
        }
    }

    public long getTimeLeft(long now) {
        return createTime.getTime() + EXPIRY_TIME - now;
    }

    public void saveData() {
        lock.lock();
        try {
            if (!isCharge) {
                return;
            }
            isCharge = false;
            GameRepository.getInstance().itemConsignmentData.saveData(databaseId, buyerId, status, toJsonObject().toString(), buyerName, buyTime, receiveTime);
        } finally {
            lock.unlock();
        }
    }

    public void setStatus(ItemConsignmentStatus status) {
        this.status = status;
        isCharge = true;
    }

}
