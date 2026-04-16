package com.beemobi.rongthanonline.dragon;

import com.beemobi.rongthanonline.entity.player.Player;
import com.beemobi.rongthanonline.map.Zone;

public abstract class Dragon {
    public boolean isActive;
    public int masterId;
    public Zone zone;
    public int[] icons;
    public int[] items;
    public int x;
    public int y;

    public abstract void showMenu(Player player);

    public abstract void confirm(Player player, int index);

    public abstract void wish(Player player, int index);

    public abstract void summonDragon(Player player);

    public abstract void showWish(Player player);

    public abstract void close();
}
