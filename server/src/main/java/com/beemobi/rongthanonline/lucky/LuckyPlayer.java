package com.beemobi.rongthanonline.lucky;

import com.beemobi.rongthanonline.entity.player.Player;

import java.util.ArrayList;

public class LuckyPlayer {

    public int id;
    public long quantity;
    public String name;
    public ArrayList<Integer> numbers;

    public LuckyPlayer(Player player, long coin) {
        id = player.id;
        name = player.name;
        numbers = new ArrayList<>();
        quantity = coin;
    }
}
