package com.beemobi.rongthanonline.item;

import com.beemobi.rongthanonline.common.KeyValue;
import com.beemobi.rongthanonline.common.RandomCollection;
import com.beemobi.rongthanonline.util.Utils;

public class ItemRandom {
    private int[] id;
    public int minQuantity;
    public int maxQuantity;
    public KeyValue[] expiry;
    public boolean isRandomParam;

    public ItemRandom(int[] id, int minQuantity, int maxQuantity, KeyValue... expiry) {
        this.id = id;
        this.minQuantity = minQuantity;
        this.maxQuantity = maxQuantity;
        this.expiry = expiry;
    }

    public ItemRandom(int[] id, KeyValue... expiry) {
        this.id = id;
        this.expiry = expiry;
    }

    public ItemRandom(int[] id) {
        this.id = id;
    }

    public ItemRandom(int id) {
        this.id = new int[]{id};
    }

    public ItemRandom(int[] id, int minQuantity, int maxQuantity) {
        this.id = id;
        this.minQuantity = minQuantity;
        this.maxQuantity = maxQuantity;
    }

    public ItemRandom(int id, int minQuantity, int maxQuantity) {
        this.id = new int[]{id};
        this.minQuantity = minQuantity;
        this.maxQuantity = maxQuantity;
    }

    public ItemRandom(int id, int quantity) {
        this.id = new int[]{id};
        this.minQuantity = quantity;
        this.maxQuantity = quantity;
    }

    public ItemRandom(int id, boolean isRandomParam) {
        this.id = new int[]{id};
        this.isRandomParam = isRandomParam;
    }

    public ItemRandom(int[] id, boolean isRandomParam) {
        this.id = id;
        this.isRandomParam = isRandomParam;
    }

    public int nextQuantity() {
        if (maxQuantity <= 1) {
            return 1;
        }
        if (minQuantity >= maxQuantity) {
            return maxQuantity;
        }
        return Math.max(Utils.nextInt(minQuantity, maxQuantity), 1);
    }

    public int nextExpiry() {
        if (expiry == null) {
            return -1;
        }
        if (expiry.length == 1) {
            return (int) expiry[0].value;
        }
        RandomCollection<Integer> days = new RandomCollection<>();
        for (KeyValue time : expiry) {
            days.add(Double.parseDouble(time.key.toString()), (int) time.value);
        }
        return days.next();
    }

    public int getId(int gender) {
        if (gender >= id.length) {
            return id[id.length - 1];
        }
        if (gender < 0) {
            return id[0];
        }
        return id[gender];
    }

    public int getId() {
        return id[0];
    }
}
