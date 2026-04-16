package com.beemobi.rongthanonline.item;

import java.util.ArrayList;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ItemMap extends Item {
    public int id;
    public int x;
    public int y;
    public int playerId;
    public boolean isPickedUp;
    public boolean isTask;
    public Lock lock = new ReentrantLock();
    public long throwTime;
    public long lockTime;
    public ArrayList<Integer> players;

    public ItemMap() {
        throwTime = System.currentTimeMillis();
        x = -1;
    }

    public void update() {

    }
}
