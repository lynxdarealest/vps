package com.beemobi.rongthanonline.event;

import com.beemobi.rongthanonline.command.Command;
import com.beemobi.rongthanonline.command.CommandName;
import com.beemobi.rongthanonline.common.Language;
import com.beemobi.rongthanonline.entity.Entity;
import com.beemobi.rongthanonline.entity.monster.big.XichQuyBiNgo;
import com.beemobi.rongthanonline.entity.player.Player;
import com.beemobi.rongthanonline.item.*;
import com.beemobi.rongthanonline.map.Zone;
import com.beemobi.rongthanonline.map.Map;
import com.beemobi.rongthanonline.map.MapManager;
import com.beemobi.rongthanonline.npc.NpcName;
import com.beemobi.rongthanonline.util.Utils;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class Halloween {

    /*public Halloween(String name, Timestamp startTime, Timestamp endTime) {
        super(name, startTime, endTime);
        GIFT_NORMAL.add(3, ItemName.DA_8);
        GIFT_NORMAL.add(5, ItemName.DA_7);
        GIFT_NORMAL.add(15, ItemName.DA_6);
        GIFT_NORMAL.add(20, ItemName.DA_5);
        GIFT_NORMAL.add(3, ItemName.NGOC_RONG_5_SAO);
        GIFT_NORMAL.add(15, ItemName.NGOC_RONG_6_SAO);
        GIFT_NORMAL.add(20, ItemName.NGOC_RONG_7_SAO);
        GIFT_NORMAL.add(5, ItemName.MANH_GIAY);
        GIFT_NORMAL.add(5, ItemName.THE_XU);
        GIFT_NORMAL.add(1, ItemName.MA_TROI_PHU_THUY_HALLOWEEN_2023);
        GIFT_NORMAL.add(1, ItemName.MA_TROI_AC_QUY_HALLOWEEN_2023);

        GIFT_SPECIAL.add(0.5, ItemName.SACH_THOI_MIEN);
        GIFT_SPECIAL.add(10, ItemName.XU);
        GIFT_SPECIAL.add(5, ItemName.BUA_BAO_VE_CAP_3);
        GIFT_SPECIAL.add(5, ItemName.NGOC_RONG_3_SAO);
        GIFT_SPECIAL.add(15, ItemName.NGOC_RONG_4_SAO);
        GIFT_SPECIAL.add(3, ItemName.HUY_HIEU_HALLOWEEN);
        GIFT_SPECIAL.add(3, ItemName.CHOI_BAY_HALLOWEEN);
        GIFT_SPECIAL.add(15, ItemName.KEO_CAM_LAU_HALLOWEEN_2023);
        GIFT_SPECIAL.add(15, ItemName.KEO_MAT_XANH_HALLOWEEN_2023);
        GIFT_SPECIAL.add(15, ItemName.KEO_DAU_LAU_HALLOWEEN_2023);
        GIFT_SPECIAL.add(15, ItemName.KEO_THAN_CHET_HALLOWEEN_2023);
        GIFT_SPECIAL.add(1, ItemName.SAO_PHA_LE_DANH_BONG);
    }

    @Override
    public void openMenu(Player player) {
        List<Command> commands = new ArrayList<>();
        commands.add(new Command(CommandName.SHOW_EXCHANGE_EVENT, "Đổi vật phẩm", player));
        commands.add(new Command(CommandName.THONG_TIN_SU_KIEN, "Thông tin", player));
        player.createMenu(NpcName.ME, String.format("Sự kiện %s", name), commands);
    }

    @Override
    public void showMenuExchange(Player player) {
        List<Command> commands = new ArrayList<>();
        commands.add(new Command(CommandName.MENU_EVENT_EXCHANGE_QUANTITY, "Huy hiệu Halloween", player, 0));
        commands.add(new Command(CommandName.MENU_EVENT_EXCHANGE_QUANTITY, "Capsule bạc Halloween", player, 1));
        commands.add(new Command(CommandName.MENU_EVENT_EXCHANGE_QUANTITY, "Cánh dơi", player, 2));
        commands.add(new Command(CommandName.MENU_EVENT_EXCHANGE_QUANTITY, "Ma trơi bí ngô", player, 3));
        commands.add(new Command(CommandName.MENU_EVENT_EXCHANGE_QUANTITY, "Capsule Bí ngô", player, 4));
        player.createMenu(NpcName.ME, "Đổi vật phẩm", commands);
    }

    @Override
    public void exchangeQuantity(Player player, int type) {
        StringBuilder content = new StringBuilder();
        if (type == 0) {
            content.append("Đổi Huy hiệu Halloween").append("\n");
            content.append("Yêu cầu x99 Kẹo bí (nhận được khi hoàn thành nhiệm vụ hàng ngày)");
        } else if (type == 1) {
            content.append("Đổi Capsule bạc Halloween").append("\n");
            content.append("Vật phẩm sử dụng:").append("\n");
            content.append("x99 Kẹo nhện xanh (nhận được khi hạ quái)").append("\n");
            content.append("x1 Bình nước phép cam (bán tại cửa hàng Bunma)");
        } else if (type == 2) {
            content.append("Đổi Cánh dơi (có tỉ lệ ra vĩnh viễn)").append("\n");
            content.append("Vật phẩm sử dụng:").append("\n");
            content.append("x99 Bí ngô (nhận được khi hạ boss bất kì)").append("\n");
            content.append("x1 Bình nước thần kì (bán tại cửa hàng Bunma)");
        } else if (type == 3) {
            content.append("Đổi Ma trơi bí ngô (có tỉ lệ ra vĩnh viễn)").append("\n");
            content.append("Vật phẩm sử dụng:").append("\n");
            content.append("x99 Bí ngô (nhận được khi hạ boss bất kì)").append("\n");
            content.append("x1 Bình nước thần kì (bán tại cửa hàng Bunma)");
        } else if (type == 4) {
            content.append("Đổi Capsule Bí ngô").append("\n");
            content.append("Vật phẩm sử dụng:").append("\n");
            content.append("x3 Ma lửa xanh (nhận được khi hạ boss Xích Quỷ Bí Ngô)").append("\n");
            content.append("x3 Ma lửa tím (nhận được khi hạ boss Xích Quỷ Bí Ngô)").append("\n");
            content.append("x3 Ma lửa hồng (nhận được khi hạ boss Xích Quỷ Bí Ngô)").append("\n");
            content.append("x2 Bình nước phép tím (bán tại cửa hàng Bunma)");
        }
        List<Command> commands = new ArrayList<>();
        commands.add(new Command(CommandName.CONFIRM_EVENT_EXCHANGE_QUANTITY, "Đổi", player, type, 1));
        commands.add(new Command(CommandName.CANCEL, "Hủy", player));
        player.createMenu(NpcName.ME, content.toString(), commands);
    }

    @Override
    public void exchange(Player player, int type, int quantity) {
        player.lockAction.lock();
        try {
            if (player.isTrading()) {
                player.addInfo(Player.INFO_RED, Language.CANCEL_ACTION_WHEN_TRADE);
                return;
            }
            if (player.isBagFull()) {
                player.addInfo(Player.INFO_RED, Language.ME_BAG_FULL);
                return;
            }
            int[] items;
            int[] requires;
            if (type == 0) {
                items = new int[]{ItemName.KEO_BI_HALLOWEEN_2023};
                requires = new int[]{99};
            } else if (type == 1) {
                items = new int[]{ItemName.KEO_NHEN_XANH_HALLOWEEN_2023,
                        ItemName.BINH_NUOC_PHEP_CAM_HALLOWEEN_2023};
                requires = new int[]{99, 1};
            } else if (type == 2 || type == 3) {
                items = new int[]{ItemName.BI_NGO_HALLOWEEN_2023,
                        ItemName.BINH_NUOC_THAN_KI_HALLOWEEN_2023};
                requires = new int[]{99, 1};
            } else {
                items = new int[]{ItemName.MA_LUA_XANH_HALLOWEEN_2023,
                        ItemName.MA_LUA_HONG_HALLOWEEN_2023,
                        ItemName.MA_LUA_TIM_HALLOWEEN_2023,
                        ItemName.BINH_NUOC_PHEP_TIM_HALLOWEEN_2023};
                requires = new int[]{3, 3, 3, 2};
            }
            for (int i = 0; i < requires.length; i++) {
                requires[i] *= quantity;
            }
            int[] quantities = new int[items.length];
            for (Item item : player.itemsBag) {
                if (item != null) {
                    for (int i = 0; i < items.length; i++) {
                        if (item.template.id == items[i]) {
                            quantities[i] += item.quantity;
                            break;
                        }
                    }
                }
            }
            for (int i = 0; i < items.length; i++) {
                if (quantities[i] < requires[i]) {
                    player.addInfo(Player.INFO_RED, "Nguyên liệu không đủ");
                    return;
                }
            }
            for (int i = 0; i < player.itemsBag.length; i++) {
                Item item = player.itemsBag[i];
                if (item != null) {
                    for (int j = 0; j < items.length; j++) {
                        if (item.template.id == items[j] && requires[j] > 0) {
                            if (item.quantity > requires[j]) {
                                item.quantity -= requires[j];
                                requires[j] = 0;
                            } else {
                                requires[j] -= item.quantity;
                                player.itemsBag[i] = null;
                            }
                            break;
                        }
                    }
                }
            }
            player.service.setItemBag();
            int itemID = ItemName.HUY_HIEU_HALLOWEEN; // type == 0
            if (type == 1) {
                itemID = ItemName.CAPSULE_BAC_HALLOWEEN_2023;
            } else if (type == 2) {
                itemID = ItemName.CANH_DOI_HALLOWEEN_2023;
            } else if (type == 3) {
                itemID = ItemName.MA_TROI_BI_NGO_HALLOWEEN_2023;
            } else if (type == 4) {
                itemID = ItemName.CAPSULE_BI_NGO_HALLOWEEN_2023;
            }
            Item item = ItemManager.getInstance().createItem(itemID, quantity, true, true);
            if (item.template.id == ItemName.HUY_HIEU_HALLOWEEN) {
                item.setExpiry(Utils.nextInt(5, 10));
            } else if (item.template.id == ItemName.CANH_DOI_HALLOWEEN_2023 || item.template.id == ItemName.MA_TROI_BI_NGO_HALLOWEEN_2023) {
                if (Utils.nextInt(1000) != 0) {
                    item.setExpiry(Utils.nextInt(5, 10));
                }
            }
            if (player.addItem(item)) {
                player.addInfo(Player.INFO_YELLOW, String.format("Bạn nhận được x%d %s", item.quantity, item.template.name));
            }
        } finally {
            player.lockAction.unlock();
        }
    }

    @Override
    public int next(int type) {
        if (type == 0) {
            return GIFT_NORMAL.next();
        }
        return GIFT_SPECIAL.next();
    }

    @Override
    public void reward(Player player) {

    }

    @Override
    public void rewardTaskDaily(Player player) {
        if (player.isBagFull()) {
            return;
        }
        Item item = ItemManager.getInstance().createItem(ItemName.KEO_BI_HALLOWEEN_2023, Utils.nextInt(1, 3), true);
        if (player.addItem(item)) {
            player.addInfo(Player.INFO_YELLOW, String.format("Bạn nhận được x%d %s", item.quantity, item.template.name));
        }
    }

    @Override
    public void useItem(Player player, Item item) {
        switch (item.template.id) {
            case ItemName.CAPSULE_BAC_HALLOWEEN_2023:
            case ItemName.CAPSULE_BI_NGO_HALLOWEEN_2023: {
                if (player.getCountItemBagEmpty() < 2) {
                    player.addInfo(Player.INFO_RED, "Cần ít nhất 2 ô trống trong túi đồ");
                    return;
                }
                boolean isSpecial = item.template.id == ItemName.CAPSULE_BI_NGO_HALLOWEEN_2023;
                player.removeQuantityItemBag(item, 1);
                int itemId = Event.event.next(isSpecial ? Event.TYPE_SPECIAL : Event.TYPE_NORMAL);
                if (itemId == ItemName.XU) {
                    int coin = Utils.nextInt(5000000, 10000000);
                    player.upXu(coin);
                    player.addInfo(Player.INFO_YELLOW, String.format("Bạn nhận được %d xu", coin));
                } else {
                    Item itm = ItemManager.getInstance().createItem(itemId, 1, true);
                    if (itm.template.id == ItemName.SAO_PHA_LE_DANH_BONG) {
                        itm.options.clear();
                        itm.options.add(itm.createOptionCrystal(true, true));
                        itm.setExpiry(7);
                    } else {
                        if (!isSpecial) {
                            itm.removeOptionLegend();
                        }
                        if (itm.isItemBody()) {
                            itm.randomParam(-15, 15);
                            if (Utils.nextInt(1000) != 0) {
                                itm.setExpiry(Utils.nextInt(5, 10));
                            }
                        }
                    }
                    player.addItem(itm, true);
                }
                if (item.template.id == ItemName.CAPSULE_BAC_HALLOWEEN_2023) {
                    player.addItem(ItemManager.getInstance().createItem(ItemName.LOI_KI_NANG, Utils.nextInt(7, 13), true), true);
                }
                return;
            }
        }

    }

    @Override
    public List<ItemShop> createShop(Player player) {
        List<ItemShop> itemShops = new ArrayList<>();
        itemShops.add(new ItemShop(ItemName.MA_TROI_BI_NGO_HALLOWEEN_2023, 1, ItemShopTypePrice.DIAMOND, 5000));
        itemShops.add(new ItemShop(ItemName.CANH_DOI_HALLOWEEN_2023, 1, ItemShopTypePrice.DIAMOND, 5000));
        itemShops.add(new ItemShop(ItemName.MA_TROI_AC_QUY_HALLOWEEN_2023, 1, ItemShopTypePrice.DIAMOND, 3000));
        itemShops.add(new ItemShop(ItemName.MA_TROI_PHU_THUY_HALLOWEEN_2023, 1, ItemShopTypePrice.DIAMOND, 3000));
        itemShops.add(new ItemShop(ItemName.BINH_NUOC_PHEP_CAM_HALLOWEEN_2023, 1, ItemShopTypePrice.COIN, 10000000));
        itemShops.add(new ItemShop(ItemName.BINH_NUOC_PHEP_TIM_HALLOWEEN_2023, 1, ItemShopTypePrice.DIAMOND, 5));
        itemShops.add(new ItemShop(ItemName.BINH_NUOC_THAN_KI_HALLOWEEN_2023, 1, ItemShopTypePrice.COIN, 10000000));
        itemShops.add(new ItemShop(ItemName.CAPSULE_BI_NGO_HALLOWEEN_2023, 1, ItemShopTypePrice.COIN, 50000000));
        itemShops.add(new ItemShop(ItemName.CAPSULE_BI_NGO_HALLOWEEN_2023, 10, ItemShopTypePrice.COIN, 500000000));
        itemShops.add(new ItemShop(ItemName.CAPSULE_BI_NGO_HALLOWEEN_2023, 1, ItemShopTypePrice.DIAMOND, 20));
        itemShops.add(new ItemShop(ItemName.CAPSULE_BI_NGO_HALLOWEEN_2023, 100, ItemShopTypePrice.DIAMOND, 2000));
        return itemShops;
    }

    @Override
    public List<ItemMap> throwItem(Player killer, Entity entity) {
        List<ItemMap> itemMaps = new ArrayList<>();
        if (entity.isMonster()) {
            if (entity.level > 10 && Math.abs(killer.level - entity.level) < 10) {
                if (Utils.isPercent(5)) {
                    itemMaps.add(ItemManager.getInstance().createItemMap(ItemName.KEO_NHEN_XANH_HALLOWEEN_2023, 1, killer.id));
                }
            }
        } else if (entity.isBoss()) {
            itemMaps.add(ItemManager.getInstance().createItemMap(ItemName.BI_NGO_HALLOWEEN_2023, 1, killer.id));
        }
        return itemMaps;
    }

    @Override
    public List<ItemShop> showShopExchangePoint(Player player) {
        return null;
    }

    @Override
    public void start() {
        Utils.setTimeout(() -> {
            for (int i = 0; i < 10; i++) {
                Map map = MapManager.getInstance().maps.get(XichQuyBiNgo.MAPS[Utils.nextInt(XichQuyBiNgo.MAPS.length)]);
                if (map != null) {
                    Zone zone = map.findOrRandomZone(-2);
                    zone.enterBigMonster(new XichQuyBiNgo());
                }
            }
        }, 300000);
    }*/
}
