package com.beemobi.rongthanonline.event;

import com.beemobi.rongthanonline.common.RandomCollection;
import com.beemobi.rongthanonline.entity.Entity;
import com.beemobi.rongthanonline.entity.player.Player;
import com.beemobi.rongthanonline.item.Item;
import com.beemobi.rongthanonline.item.ItemMap;
import com.beemobi.rongthanonline.item.ItemShop;
import com.beemobi.rongthanonline.network.Message;
import lombok.Getter;

import java.sql.Timestamp;
import java.util.List;

public abstract class Event {
    public static final int TYPE_NORMAL = 0;
    public static final int TYPE_SPECIAL = 1;
    public RandomCollection<Integer> GIFT_NORMAL;
    public RandomCollection<Integer> GIFT_SPECIAL;

    @Getter
    public String name;

    @Getter
    public Timestamp startTime, endTime;

    public Event(String name, Timestamp startTime, Timestamp endTime) {
        GIFT_NORMAL = new RandomCollection<>();
        GIFT_SPECIAL = new RandomCollection<>();
        this.name = name;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public static Event event;

    public static boolean isEvent() {
        return event != null;
    }

    public static boolean isHaveTop() {
        return isNoel();
    }

    public static boolean isNoel() {
        return event instanceof Noel2023;
    }

    public static boolean isTet() {
        return event instanceof Tet2024;
    }

    public static boolean isWomanDay() {
        return event instanceof WomanDay;
    }

    public static boolean isHungVuong() {
        return event instanceof HungVuong;
    }

    public static boolean isHe2024() {
        return event instanceof He2024;
    }

    public static boolean isTuuTruong2024() {
        return event instanceof TuuTruong2024;
    }

    public static boolean isCoHon2024() {
        return event instanceof CoHon2024;
    }

    public static boolean isTrungThu2024() {
        return event instanceof TrungThu2024;
    }

    public static boolean isHaloween2024() {
        return event instanceof Haloween2024;
    }

    public static boolean isTeacherDay2024() {
        return event instanceof TeacherDay2024;
    }

    public abstract void openMenu(Player player, Object[] elements);

    public abstract void confirmClientInput(Player player, Message message);

    public abstract void exchange(Player player, int type, int quantity);

    public abstract void useItem(Player player, Item item);

    public abstract List<ItemShop> createShop(Player player);

    public abstract List<ItemMap> throwItem(Player killer, Entity entity);

    public abstract void rewardTaskDaily(Player killer);

    public abstract void rewardRecharge(Player player, int diamond);

    public abstract List<ItemShop> showShopExchangePoint(Player player);

    public abstract void start();

}
