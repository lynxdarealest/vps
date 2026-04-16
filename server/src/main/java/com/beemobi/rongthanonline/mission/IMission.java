package com.beemobi.rongthanonline.mission;

import com.beemobi.rongthanonline.item.Item;

import java.util.List;

public interface IMission {
    int getTemplateId();
    String getName();
    String getDescription();
    int getType();
    void setType(int type);
    List<Item> getItems();
}
