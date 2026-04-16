package com.beemobi.rongthanonline.common;

import lombok.Getter;

import java.util.NavigableMap;
import java.util.Random;
import java.util.TreeMap;

@Getter
public class RandomCollection<T> {

    private final NavigableMap<Double, T> map = new TreeMap<>();
    private final Random random = new Random();
    private double total = 0;

    public RandomCollection() {
    }

    public void add(double weight, T result) {
        if (weight <= 0) {
            return;
        }
        total += weight;
        map.put(total, result);
    }

    public void setDefault(T result) {
        double weight = 100 - total;
        if (weight <= 0) {
            return;
        }
        total += weight;
        map.put(total, result);
    }

    public boolean isEmpty() {
        return map.isEmpty();
    }

    public T next() {
        double value = random.nextDouble() * total;
        return map.higherEntry(value).getValue();
    }
}
