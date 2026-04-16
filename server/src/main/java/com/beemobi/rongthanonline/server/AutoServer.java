package com.beemobi.rongthanonline.server;

import com.beemobi.rongthanonline.data.DiscipleData;
import com.beemobi.rongthanonline.data.ItemConsignmentData;
import com.beemobi.rongthanonline.data.PlayerData;
import com.beemobi.rongthanonline.entity.player.Player;
import com.beemobi.rongthanonline.entity.player.json.ItemInfo;
import com.beemobi.rongthanonline.entity.player.json.ListItemInfo;
import com.beemobi.rongthanonline.item.Item;
import com.beemobi.rongthanonline.item.ItemName;
import com.beemobi.rongthanonline.item.ItemOption;
import com.beemobi.rongthanonline.repository.GameRepository;
import com.beemobi.rongthanonline.util.Utils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

import static com.beemobi.rongthanonline.entity.player.Player.*;

public class AutoServer implements Runnable {

    private static final Logger logger = Logger.getLogger(AutoServer.class);

    @Override
    public void run() {
        try {
            while (!Server.getInstance().isRunning) {
                Thread.sleep(1000);
            }
            Thread.sleep(1000);
            resetStar();
            System.out.println("xong!");
        } catch (Exception ex) {
            logger.error("run", ex);
        }
    }

    public static int getMaxParam(int star, int level) {
        //2 sao
        int param = 1000;
        while (star > 1) {
            star--;
            for (int i = 0; i < 15; i++) {
                param += Math.max(param / 10, 1);
            }
            param = param * 50 / 167;
        }
        for (int i = 1; i < level; i++) {
            param += Math.max(param / 10, 1);
        }
        return param;
    }

    public void resetAll() {
        Gson gson = new Gson();
        List<PlayerData> playerDataList = GameRepository.getInstance().playerData.findAll();
        for (PlayerData data : playerDataList) {
            int quantity = 0;
            try {
                ListItemInfo items = Utils.gson.fromJson(data.itemsBag, new TypeToken<ListItemInfo>() {
                }.getType());
                Item[] itemsBag = new Item[Math.max(items.size, DEFAULT_BAG)];
                for (int i = 0; i < items.items.size(); i++) {
                    ItemInfo info = items.items.get(i);
                    Item item = new Item(info);
                    if (item.options.removeIf(o -> o.template.id == 36)) {
                        item.isLock = true;
                    }
                    item.indexUI = i;
                    itemsBag[i] = item;
                    if (item.quantity < 1) {
                        itemsBag[i] = null;
                    }
                    if (item.template.id == ItemName.THE_KIM_CUONG) {
                        quantity += item.quantity;
                    }
                }
                data.itemsBag = Utils.getJsonArrayItem(itemsBag);
            } catch (Exception ex) {
                ex.printStackTrace();
                return;
            }
            try {
                ListItemInfo items = Utils.gson.fromJson(data.itemsBox, new TypeToken<ListItemInfo>() {
                }.getType());
                Item[] itemsBox = new Item[Math.max(items.size, DEFAULT_BOX)];
                for (int i = 0; i < items.items.size(); i++) {
                    ItemInfo info = items.items.get(i);
                    Item item = new Item(info);
                    if (item.options.removeIf(o -> o.template.id == 36)) {
                        item.isLock = true;
                    }
                    item.indexUI = i;
                    itemsBox[i] = item;
                    if (item.quantity < 1) {
                        itemsBox[i] = null;
                    }
                    if (item.template.id == ItemName.THE_KIM_CUONG) {
                        quantity += item.quantity;
                    }
                }
                data.itemsBox = Utils.getJsonArrayItem(itemsBox);
            } catch (Exception ex) {
                ex.printStackTrace();
                return;
            }
            try {
                ListItemInfo items = Utils.gson.fromJson(data.itemsBody, new TypeToken<ListItemInfo>() {
                }.getType());
                Item[] itemsBody = new Item[Math.max(items.size, DEFAULT_BODY)];
                for (int i = 0; i < items.items.size(); i++) {
                    ItemInfo info = items.items.get(i);
                    Item item = new Item(info);
                    if (item.options.removeIf(o -> o.template.id == 36)) {
                        item.isLock = true;
                    }
                    item.indexUI = item.getIndexBody();
                    itemsBody[item.indexUI] = item;
                    if (item.quantity < 1) {
                        itemsBody[item.indexUI] = null;
                    }
                }
                data.itemsBody = Utils.getJsonArrayItem(itemsBody);
            } catch (Exception ex) {
                ex.printStackTrace();
                return;
            }
            try {
                ListItemInfo items = Utils.gson.fromJson(data.itemsOther, new TypeToken<ListItemInfo>() {
                }.getType());
                Item[] itemsOther = new Item[Math.max(items.size, DEFAULT_BODY)];
                for (int i = 0; i < items.items.size(); i++) {
                    ItemInfo info = items.items.get(i);
                    Item item = new Item(info);
                    if (item.options.removeIf(o -> o.template.id == 36)) {
                        item.isLock = true;
                    }
                    item.indexUI = item.getIndexBody();
                    itemsOther[item.indexUI] = item;
                    if (item.quantity < 1) {
                        itemsOther[item.indexUI] = null;
                    }
                }
                data.itemsOther = Utils.getJsonArrayItem(itemsOther);
            } catch (Exception ex) {
                ex.printStackTrace();
                return;
            }
            try {
                ListItemInfo items = Utils.gson.fromJson(data.itemsPet, new TypeToken<ListItemInfo>() {
                }.getType());
                Item[] itemsPet = new Item[Math.max(items.size, DEFAULT_BODY)];
                for (int i = 0; i < items.items.size(); i++) {
                    ItemInfo info = items.items.get(i);
                    Item item = new Item(info);
                    if (item.options.removeIf(o -> o.template.id == 36)) {
                        item.isLock = true;
                    }
                    item.indexUI = item.getIndexPet();
                    itemsPet[item.indexUI] = item;
                    if (item.quantity < 1) {
                        itemsPet[item.indexUI] = null;
                    }
                }
                data.itemsPet = Utils.getJsonArrayItem(itemsPet);
            } catch (Exception ex) {
                ex.printStackTrace();
                return;
            }
            if (quantity == 0) {
                data.ruby = Integer.valueOf(String.valueOf(data.diamond));
                data.diamond = 0;
            } else if (quantity * 10 < data.diamond){
                data.ruby = data.diamond - quantity * 10;
                data.diamond = quantity * 10;
            }
            System.out.println(data.id);
        }
        GameRepository.getInstance().playerData.saveAll(playerDataList);

    }

    public void resetStar() {
        List<PlayerData> playerDataList = GameRepository.getInstance().playerData.findAll();
        //List<PlayerData> changes = new ArrayList<>();
        for (PlayerData data : playerDataList) {
            boolean isChanged = false;
            try {
                ListItemInfo items = Utils.gson.fromJson(data.itemsBag, new TypeToken<ListItemInfo>() {
                }.getType());
                Item[] itemsBag = new Item[Math.max(items.size, DEFAULT_BAG)];
                for (int i = 0; i < items.items.size(); i++) {
                    ItemInfo info = items.items.get(i);
                    Item item = new Item(info);
                    if (item.template.id == ItemName.BONG_TAI_PORATA_DAC_BIET) {
                        isChanged = true;
                        item.options.removeIf(o -> o.template.type == 4);
                        for (ItemOption option : item.options) {
                            if (option.template.id == 68) {
                                option.param = 0;
                                break;
                            }
                        }
                    }
                    item.indexUI = i;
                    itemsBag[i] = item;
                    if (item.quantity < 1) {
                        itemsBag[i] = null;
                    }
                }
                data.itemsBag = Utils.getJsonArrayItem(itemsBag);
            } catch (Exception ex) {
                ex.printStackTrace();
                return;
            }
            try {
                ListItemInfo items = Utils.gson.fromJson(data.itemsBox, new TypeToken<ListItemInfo>() {
                }.getType());
                Item[] itemsBox = new Item[Math.max(items.size, DEFAULT_BOX)];
                for (int i = 0; i < items.items.size(); i++) {
                    ItemInfo info = items.items.get(i);
                    Item item = new Item(info);
                    if (item.template.id == ItemName.BONG_TAI_PORATA_DAC_BIET) {
                        isChanged = true;
                        item.options.removeIf(o -> o.template.type == 4);
                        for (ItemOption option : item.options) {
                            if (option.template.id == 68) {
                                option.param = 0;
                                break;
                            }
                        }
                    }
                    item.indexUI = i;
                    itemsBox[i] = item;
                    if (item.quantity < 1) {
                        itemsBox[i] = null;
                    }
                }
                data.itemsBox = Utils.getJsonArrayItem(itemsBox);
            } catch (Exception ex) {
                ex.printStackTrace();
                return;
            }
            try {
                ListItemInfo items = Utils.gson.fromJson(data.itemsBody, new TypeToken<ListItemInfo>() {
                }.getType());
                Item[] itemsBody = new Item[Math.max(items.size, DEFAULT_BODY)];
                for (int i = 0; i < items.items.size(); i++) {
                    ItemInfo info = items.items.get(i);
                    Item item = new Item(info);
                    if (item.template.id == ItemName.BONG_TAI_PORATA_DAC_BIET) {
                        isChanged = true;
                        item.options.removeIf(o -> o.template.type == 4);
                        for (ItemOption option : item.options) {
                            if (option.template.id == 68) {
                                option.param = 0;
                                break;
                            }
                        }
                    }
                    item.indexUI = item.getIndexBody();
                    itemsBody[item.indexUI] = item;
                    if (item.quantity < 1) {
                        itemsBody[item.indexUI] = null;
                    }
                }
                data.itemsBody = Utils.getJsonArrayItem(itemsBody);
            } catch (Exception ex) {
                ex.printStackTrace();
                return;
            }
            /*if (isChanged) {
                changes.add(data);
                System.out.println(data.id);
            }*/
        }
        GameRepository.getInstance().playerData.saveAll(playerDataList);

    }

    public void resetDisciple() {
        List<DiscipleData> discipleDataList = GameRepository.getInstance().discipleData.findAll();
        for (DiscipleData data : discipleDataList) {
            try {
                ListItemInfo items = Utils.gson.fromJson(data.itemsBody, new TypeToken<ListItemInfo>() {
                }.getType());
                Item[] itemsBody = new Item[Math.max(items.size, Player.DEFAULT_BODY)];
                for (int i = 0; i < items.items.size(); i++) {
                    ItemInfo info = items.items.get(i);
                    Item item = new Item(info);
                    if (item.options.removeIf(o -> o.template.id == 36)) {
                        item.isLock = true;
                    }
                    item.indexUI = item.getIndexBody();
                    itemsBody[item.indexUI] = item;
                    if (item.quantity < 1) {
                        itemsBody[item.indexUI] = null;
                    }
                }
                data.itemsBody = Utils.getJsonArrayItem(itemsBody);
            } catch (Exception ex) {
                ex.printStackTrace();
                return;
            }
            try {
                ListItemInfo items = Utils.gson.fromJson(data.itemsOther, new TypeToken<ListItemInfo>() {
                }.getType());
                Item[] itemsOther = new Item[Math.max(items.size, Player.DEFAULT_BODY)];
                for (int i = 0; i < items.items.size(); i++) {
                    ItemInfo info = items.items.get(i);
                    Item item = new Item(info);
                    if (item.options.removeIf(o -> o.template.id == 36)) {
                        item.isLock = true;
                    }
                    item.indexUI = item.getIndexBody();
                    itemsOther[item.indexUI] = item;
                    if (item.quantity < 1) {
                        itemsOther[item.indexUI] = null;
                    }
                }
                data.itemsOther = Utils.getJsonArrayItem(itemsOther);
            } catch (Exception ex) {
                ex.printStackTrace();
                return;
            }
            System.out.println(data.id);
        }
        GameRepository.getInstance().discipleData.saveAll(discipleDataList);

    }

    public void resetConsignment() {
        List<ItemConsignmentData> dataList = GameRepository.getInstance().itemConsignmentData.findAll();
        for (ItemConsignmentData data : dataList) {
            ItemInfo itemInfo = Utils.gson.fromJson(data.item, new TypeToken<ItemInfo>() {
            }.getType());
            data.item = new Item(itemInfo).toJsonObject().toString();
            System.out.println(data.id);
        }
        GameRepository.getInstance().itemConsignmentData.saveAll(dataList);
    }
}
