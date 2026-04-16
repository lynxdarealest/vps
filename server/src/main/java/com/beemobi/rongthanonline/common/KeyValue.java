package com.beemobi.rongthanonline.common;

public class KeyValue<K, V> {

    public KeyValue(K key, V value, Object... obj) {
        this.key = key;
        this.value = value;
        this.elements = obj;
    }

    public KeyValue(K key, V value) {
        this.key = key;
        this.value = value;
        this.elements = null;
    }

    public K key;
    public V value;
    public Object[] elements;
}
