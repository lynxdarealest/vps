package com.beemobi.rongthanonline.shop;

import com.beemobi.rongthanonline.entity.player.Player;
import com.beemobi.rongthanonline.item.ItemConsignment;
import com.beemobi.rongthanonline.item.ItemOption;
import com.beemobi.rongthanonline.item.ItemShop;
import com.beemobi.rongthanonline.network.Message;
import com.beemobi.rongthanonline.network.MessageName;
import com.beemobi.rongthanonline.util.Utils;
import org.apache.log4j.Logger;

import java.util.*;
import java.util.stream.Collectors;

public abstract class Shop {
    private static final Logger logger = Logger.getLogger(Shop.class);
    public ShopType type;

    public Shop(ShopType type) {
        this.type = type;
    }

    public int formatType() {
        if (type == ShopType.SHOP_BUNMA_EQUIP || type == ShopType.SHOP_BUNMA_OTHER
                || type == ShopType.SHOP_AMULET_OTHER || type == ShopType.SHOP_AMULET_TIME
                || type == ShopType.SHOP_CLAN || type == ShopType.SHOP_REPURCHASE
                || type == ShopType.SHOP_YARDRAT || type == ShopType.SHOP_BLACK_FRIDAY
                || type == ShopType.SHOP_RIDER) {
            return 0;
        }
        if (type == ShopType.SHOP_BARRACK || type == ShopType.SHOP_ACTIVE || type == ShopType.SHOP_EVENT || type == ShopType.SHOP_SPACESHIP
                || type == ShopType.SHOP_FLAG_WAR) {
            return 2;
        }
        return 1;
    }

    public abstract LinkedHashMap<String, List<ItemShop>> createShop(Player player);

    public void show(Player player) {
        try {
            LinkedHashMap<String, List<ItemShop>> itemShops = createShop(player);
            player.itemsShop = itemShops;
            if (player.itemsShop == null || player.itemsShop.isEmpty()) {
                player.addInfo(Player.INFO_RED, "Hiện tại cửa hàng trống");
                return;
            }
            if (this.type != ShopType.SHOP_CONSIGNMENT) {
                int id = 0;
                for (List<ItemShop> items : itemShops.values()) {
                    for (ItemShop item : items) {
                        item.id = id++;
                    }
                }
            }
            long now = System.currentTimeMillis();
            Message msg = new Message(MessageName.SHOW_SHOP);
            int type = formatType();
            msg.writer().writeByte(type);
            msg.writer().writeByte(itemShops.size());
            for (String name : itemShops.keySet()) {
                msg.writer().writeUTF(name);
                List<ItemShop> items;
                if (this.type == ShopType.SHOP_EVENT) {
                    items = new ArrayList<>(itemShops.get(name));
                } else {
                    /*items = itemShops.get(name).stream()
                            .sorted(Comparator.comparing((ItemShop i) -> i.template.type).reversed()
                                    .thenComparing((ItemShop i) -> i.template.gender)
                                    .thenComparing((ItemShop i) -> i.template.id, Comparator.reverseOrder())
                                    .thenComparing((ItemShop i) -> i.quantity, Comparator.reverseOrder())
                                    .thenComparing((ItemShop i) -> i.price))
                            .collect(Collectors.toList());*/
                    items = new ArrayList<>(itemShops.get(name));
                    Collections.reverse(items);
                }
                msg.writer().writeShort(items.size());
                for (ItemShop item : items) {
                    if (item == null) {
                        continue;
                    }
                    msg.writer().writeInt(item.id);
                    msg.writer().writeShort(item.template.id);
                    msg.writer().writeByte(item.typePrice.ordinal());
                    msg.writer().writeInt(item.price);
                    msg.writer().writeInt(item.quantity);
                    msg.writer().writeBoolean(item.isLock);
                    msg.writer().writeByte(item.options.size());
                    for (ItemOption itemOption : item.options) {
                        msg.writer().writeShort(itemOption.template.id);
                        msg.writer().writeInt(itemOption.param);
                    }
                    if (this.type == ShopType.SHOP_CONSIGNMENT) {
                        ItemConsignment itemConsignment = (ItemConsignment) item;
                        msg.writer().writeUTF(itemConsignment.sellerName);
                        msg.writer().writeUTF(Utils.formatTime(itemConsignment.getTimeLeft(now)));
                        msg.writer().writeByte(itemConsignment.status.ordinal());
                    }
                }
            }
            if (this.type == ShopType.SHOP_BARRACK) {
                msg.writer().writeInt(player.pointBarrack);
            } else if (this.type == ShopType.SHOP_ACTIVE) {
                msg.writer().writeInt(player.pointActive);
            } else if (this.type == ShopType.SHOP_EVENT) {
                msg.writer().writeInt(player.pointRewardEvent);
            } else if (this.type == ShopType.SHOP_SPACESHIP) {
                msg.writer().writeInt(player.pointSpaceship);
            } else if (this.type == ShopType.SHOP_FLAG_WAR) {
                msg.writer().writeInt(player.pointFlagWar);
            }
            player.sendMessage(msg);
            msg.cleanup();
        } catch (Exception ex) {
            logger.error("show", ex);
        }
        player.service.setListItemLight(getIndexBagLightInShop(player));
    }

    public boolean[] getIndexBagLightInShop(Player player) {
        boolean[] flags = new boolean[player.itemsBag.length];
        Arrays.fill(flags, true);
        return flags;
    }

}
