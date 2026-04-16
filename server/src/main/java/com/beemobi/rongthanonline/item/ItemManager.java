package com.beemobi.rongthanonline.item;

import com.beemobi.rongthanonline.data.ItemOptionData;
import com.beemobi.rongthanonline.data.ItemOptionTemplateData;
import com.beemobi.rongthanonline.data.ItemTemplateData;
import com.beemobi.rongthanonline.repository.GameRepository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ItemManager {
    private static ItemManager instance;
    public HashMap<Integer, ItemOptionTemplate> itemOptionTemplates;
    public HashMap<Integer, ItemTemplate> itemTemplates;
    public HashMap<Integer, ArrayList<ItemOption>> itemOptions;

    public static ItemManager getInstance() {
        if (instance == null) {
            instance = new ItemManager();
        }
        return instance;
    }

    public void init() {
        initItemOptionTemplate();
        initItemTemplate();
        initItemOption();
        //createOptionForItem6x();
    }

    public Item createItem(int templateId, int quantity, boolean isDefaultOption) {
        Item item = new Item();
        item.template = itemTemplates.get(templateId);
        item.quantity = quantity;
        if (isDefaultOption) {
            item.setDefaultOption();
        }
        return item;
    }

    public Item createItem(int templateId, int quantity, boolean isDefaultOption, boolean isRandomParam) {
        Item item = new Item();
        item.template = itemTemplates.get(templateId);
        item.quantity = quantity;
        if (isDefaultOption) {
            item.setDefaultOption();
        }
        if (isRandomParam) {
            item.randomParam(-15, 15);
        }
        return item;
    }

    public Item createItem(int templateId, boolean isDefaultOption) {
        Item item = new Item();
        item.template = itemTemplates.get(templateId);
        item.quantity = 1;
        if (isDefaultOption) {
            item.setDefaultOption();
        }
        return item;
    }

    public ItemMap createItemMap(int templateId, int quantity, int playerId) {
        ItemMap itemMap = new ItemMap();
        itemMap.template = itemTemplates.get(templateId);
        itemMap.quantity = quantity;
        itemMap.setDefaultOption();
        itemMap.playerId = playerId;
        return itemMap;
    }

    public ItemMap createItemMap(int templateId, int quantity, int playerId, int expiry) {
        ItemMap itemMap = new ItemMap();
        itemMap.template = itemTemplates.get(templateId);
        itemMap.quantity = quantity;
        itemMap.setDefaultOption();
        itemMap.setExpiry(expiry);
        itemMap.playerId = playerId;
        return itemMap;
    }

    public ItemMap createItemMapRandomOption(int templateId, int quantity, int playerId) {
        ItemMap itemMap = new ItemMap();
        itemMap.template = itemTemplates.get(templateId);
        itemMap.quantity = quantity;
        itemMap.setRandomDefaultOption();
        itemMap.randomParam(-15, 10);
        itemMap.playerId = playerId;
        return itemMap;
    }

    public void initItemOptionTemplate() {
        itemOptionTemplates = new HashMap<>();
        List<ItemOptionTemplateData> itemOptionTemplateDataList = GameRepository.getInstance().itemOptionTemplateData.findAll();
        for (ItemOptionTemplateData data : itemOptionTemplateDataList) {
            itemOptionTemplates.put(data.id, new ItemOptionTemplate(data));
        }
    }

    public void initItemTemplate() {
        itemTemplates = new HashMap<>();
        List<ItemTemplateData> itemTemplateDataList = GameRepository.getInstance().itemTemplateData.findAll();
        for (ItemTemplateData data : itemTemplateDataList) {
            itemTemplates.put(data.id, new ItemTemplate(data));
        }
    }

    public void initItemOption() {
        itemOptions = new HashMap<>();
        List<ItemOptionData> itemOptionDataList = GameRepository.getInstance().itemOptionData.findAll();
        for (ItemOptionData data : itemOptionDataList) {
            if (!itemOptions.containsKey(data.templateId)) {
                itemOptions.put(data.templateId, new ArrayList<>());
            }
            itemOptions.get(data.templateId).add(new ItemOption(data));
        }
    }

    public void createOptionForItem6x() {
        for (int i = 292; i < 308; i++) {
            Item item4x = createItem(i, 1, true);
            Item item6x = createItem(i + 45, 1, true);
            for (ItemOption option : item4x.options) {
                if (option.template.type == 0) {
                    int optionId = option.template.id;
                    if (optionId == 0 || optionId == 3 || optionId == 4) {
                        item6x.options.add(new ItemOption(optionId, option.param * 5 / 2));
                    } else {
                        item6x.options.add(new ItemOption(optionId, option.param + 10));
                    }
                }
            }
            for (ItemOption option : item4x.options) {
                if (option.template.type == 2) {
                    item6x.options.add(new ItemOption(option.template.id, option.param + 10));
                }
            }
            for (ItemOption option : item4x.options) {
                if (option.template.type == 1) {
                    int optionId = option.template.id;
                    if (optionId == 8 || optionId == 37 || optionId == 39) {
                        item6x.options.add(new ItemOption(optionId, option.param + 10));
                    } else if (optionId == 9) {
                        item6x.options.add(new ItemOption(optionId, option.param + 50));
                    } else if (optionId == 10 || optionId == 11) {
                        item6x.options.add(new ItemOption(optionId, option.param + 1));
                    } else if (optionId == 12 || optionId == 13 || optionId == 41 || optionId == 42 || optionId == 43 || (optionId >= 86 && optionId <= 93)) {
                        item6x.options.add(new ItemOption(optionId, option.param + 5));
                    } else if (optionId == 15 || optionId == 16 || optionId == 40 || optionId == 44) {
                        item6x.options.add(new ItemOption(optionId, option.param + 100));
                    } else {
                        System.out.println(optionId);
                    }
                }
            }
            for (ItemOption option : item4x.options) {
                if (option.template.type == 5) {
                    int optionId = option.template.id;
                    if (optionId == 94 || optionId == 95) {
                        item6x.options.add(new ItemOption(optionId, option.param + 50));
                    }
                    if (optionId == 96) {
                        item6x.options.add(new ItemOption(optionId, option.param + 100));
                    }
                    if (optionId == 97 || optionId == 98) {
                        item6x.options.add(new ItemOption(optionId, option.param + 1));
                    }
                }
            }
            for (ItemOption option : item6x.options) {
                ItemOptionData optionData = new ItemOptionData();
                optionData.templateId = item6x.template.id;
                optionData.optionId = option.template.id;
                optionData.param = option.param;
                GameRepository.getInstance().itemOptionData.save(optionData);
            }
        }
    }
}
