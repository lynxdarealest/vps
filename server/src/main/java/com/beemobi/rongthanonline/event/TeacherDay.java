package com.beemobi.rongthanonline.event;

import com.beemobi.rongthanonline.command.Command;
import com.beemobi.rongthanonline.command.CommandName;
import com.beemobi.rongthanonline.common.Language;
import com.beemobi.rongthanonline.entity.Entity;
import com.beemobi.rongthanonline.entity.player.Player;
import com.beemobi.rongthanonline.item.*;
import com.beemobi.rongthanonline.npc.NpcName;
import com.beemobi.rongthanonline.server.Server;
import com.beemobi.rongthanonline.top.Top;
import com.beemobi.rongthanonline.top.TopManager;
import com.beemobi.rongthanonline.top.TopType;
import com.beemobi.rongthanonline.util.Utils;
import org.apache.log4j.Logger;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class TeacherDay {
    /*private static final Logger logger = Logger.getLogger(TeacherDay.class);

    public static final Object[][] ITEM_EXCHANGE_POINT = new Object[][]{
            {ItemName.SAO_PHA_LE_DANH_BONG, 1, ItemShopTypePrice.POINT_EVENT, 40, 7, 0},
            {ItemName.NANG_CAP_CHIEU_1_DE_TU, 1, ItemShopTypePrice.POINT_EVENT, 300, 7, 0},
            {ItemName.NANG_CAP_CHIEU_2_DE_TU, 1, ItemShopTypePrice.POINT_EVENT, 500, 7, 0},
            {ItemName.NANG_CAP_CHIEU_3_DE_TU, 1, ItemShopTypePrice.POINT_EVENT, 700, 7, 0},
            {ItemName.NANG_CAP_CHIEU_4_DE_TU, 1, ItemShopTypePrice.POINT_EVENT, 900, 7, 0},
            {ItemName.NANG_CAP_CHIEU_5_DE_TU, 1, ItemShopTypePrice.POINT_EVENT, 1100, 7, 0},
            {ItemName.DUA_NAU, 1, ItemShopTypePrice.POINT_EVENT, 10, 7, 1},
            {ItemName.DUA_VANG, 1, ItemShopTypePrice.POINT_EVENT, 10, 7, 1},
            {ItemName.DUA_XANH, 1, ItemShopTypePrice.POINT_EVENT, 10, 7, 1},
            {ItemName.LOI_KI_NANG, 1, ItemShopTypePrice.POINT_EVENT, 4, 7, 1},
            {ItemName.BI_KIP_KY_NANG, 1, ItemShopTypePrice.POINT_EVENT, 1, 7, 1},
    };

    public TeacherDay(String name, Timestamp startTime, Timestamp endTime) {
        super(name, startTime, endTime);
        GIFT_NORMAL.add(15, ItemName.DA_7);
        GIFT_NORMAL.add(15, ItemName.DA_8);
        GIFT_NORMAL.add(5, ItemName.DA_9);
        GIFT_NORMAL.add(2, ItemName.DA_10);
        GIFT_NORMAL.add(3, ItemName.NGOC_RONG_5_SAO);
        GIFT_NORMAL.add(8, ItemName.NGOC_RONG_6_SAO);
        GIFT_NORMAL.add(15, ItemName.NGOC_RONG_7_SAO);
        GIFT_NORMAL.add(10, ItemName.BUA_BAO_VE_CAP_1);
        GIFT_NORMAL.add(10, ItemName.DUA_XANH);
        GIFT_NORMAL.add(10, ItemName.DUA_VANG);
        GIFT_NORMAL.add(10, ItemName.DUA_NAU);
        GIFT_NORMAL.add(5, ItemName.MAM_THIEN_MOC);
        GIFT_NORMAL.add(5, ItemName.THIEN_MOC_QUA);
        GIFT_NORMAL.add(1, ItemName.HUY_HIEU_20_11);
        GIFT_NORMAL.add(1, ItemName.MANH_GIAY);

        GIFT_SPECIAL.add(5, ItemName.DA_11);
        GIFT_SPECIAL.add(5, ItemName.DA_10);
        GIFT_SPECIAL.add(5, ItemName.DA_9);
        GIFT_SPECIAL.add(5, ItemName.BUA_BAO_VE_CAP_2);
        GIFT_SPECIAL.add(5, ItemName.BUA_BAO_VE_CAP_3);
        GIFT_SPECIAL.add(10, ItemName.DUA_XANH);
        GIFT_SPECIAL.add(10, ItemName.DUA_VANG);
        GIFT_SPECIAL.add(10, ItemName.DUA_NAU);
        GIFT_SPECIAL.add(5, ItemName.MAM_HAC_LONG);
        GIFT_SPECIAL.add(5, ItemName.HAC_LONG_QUA);
        GIFT_SPECIAL.add(5, ItemName.VUOT_LONG_THAN);
        GIFT_SPECIAL.add(5, ItemName.GIAP_LONG_BAO);
        GIFT_SPECIAL.add(5, ItemName.MU_VUONG_LONG);
        GIFT_SPECIAL.add(5, ItemName.GONG_THIEN_LONG);
        GIFT_SPECIAL.add(5, ItemName.LINH_HON_LONG_THE);
        GIFT_SPECIAL.add(2, ItemName.HUY_HIEU_20_11);
        GIFT_SPECIAL.add(5, ItemName.CAPSULE_TEACHER);
    }

    @Override
    public void openMenu(Player player) {
        List<Command> commands = new ArrayList<>();
        commands.add(new Command(CommandName.SHOW_EXCHANGE_EVENT, "Chế tạo", player));
        //commands.add(new Command(CommandName.SHOW_REWARD_EVENT, "Nhận thưởng", player));
        commands.add(new Command(CommandName.SHOW_SHOP_EXCHANGE_POINT_EVENT, "Đổi điểm", player));
        commands.add(new Command(CommandName.THONG_TIN_SU_KIEN, "Thông tin", player));
        player.createMenu(NpcName.ME, String.format("Sự kiện %s", name), commands);
    }

    @Override
    public void showMenuExchange(Player player) {
        List<Command> commands = new ArrayList<>();
        commands.add(new Command(CommandName.MENU_EVENT_EXCHANGE_QUANTITY, "Hộp quà đỏ", player, 0));
        commands.add(new Command(CommandName.MENU_EVENT_EXCHANGE_QUANTITY, "Hộp quà xanh", player, 1));
        commands.add(new Command(CommandName.MENU_EVENT_EXCHANGE_QUANTITY, "Giỏ vật tư", player, 2));
        player.createMenu(NpcName.ME, "Chế tạo vật phẩm sự kiện", commands);
    }

    @Override
    public void exchangeQuantity(Player player, int type) {
        List<Command> commands = new ArrayList<>();
        commands.add(new Command(CommandName.CONFIRM_EVENT_EXCHANGE_QUANTITY, "Số lượng\n1", player, type, 1));
        commands.add(new Command(CommandName.CONFIRM_EVENT_EXCHANGE_QUANTITY, "Số lượng\n10", player, type, 10));
        commands.add(new Command(CommandName.CONFIRM_EVENT_EXCHANGE_QUANTITY, "Số lượng\n100", player, type, 100));
        commands.add(new Command(CommandName.CONFIRM_EVENT_EXCHANGE_QUANTITY, "Số lượng\n1000", player, type, 1000));
        player.createMenu(NpcName.ME, "", commands);
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
                items = new int[]{ItemName.HOA_HONG_DO, ItemName.BAI_THI_10_DIEM};
                requires = new int[]{99, 1};
            } else if (type == 1) {
                items = new int[]{ItemName.HOA_HONG_XANH, ItemName.BAI_THI_9_DIEM};
                requires = new int[]{5, 1};
            } else {
                items = new int[]{ItemName.PHAN_TRANG, ItemName.BUT_CHI, ItemName.THUOC_KE, ItemName.SO_DAU_BAI, ItemName.CAP_SACH};
                requires = new int[]{10, 10, 10, 1, 1};
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
            player.addItem(ItemManager.getInstance()
                    .createItem(type == 0 ? ItemName.HOP_QUA_DO : (type == 1 ? ItemName.HOP_QUA_XANH : ItemName.GIO_VAT_TU),
                            quantity, true), true);
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
    public List<ItemShop> createShop(Player player) {
        List<ItemShop> itemShops = new ArrayList<>();
        itemShops.add(new ItemShop(ItemName.BAI_THI_9_DIEM, 1, ItemShopTypePrice.COIN, 1000000));
        itemShops.add(new ItemShop(ItemName.BAI_THI_9_DIEM, 100, ItemShopTypePrice.COIN, 100000000));
        itemShops.add(new ItemShop(ItemName.CAP_SACH, 1, ItemShopTypePrice.COIN, 3000000));
        itemShops.add(new ItemShop(ItemName.CAP_SACH, 100, ItemShopTypePrice.COIN, 300000000));
        itemShops.add(new ItemShop(ItemName.CAPSULE_TEACHER, 1, ItemShopTypePrice.DIAMOND, 500));
        itemShops.add(new ItemShop(ItemName.CAPSULE_TEACHER, 10, ItemShopTypePrice.DIAMOND, 4500));
        itemShops.add(new ItemShop(ItemName.CAPSULE_TEACHER, 100, ItemShopTypePrice.DIAMOND, 40000));
        itemShops.add(new ItemShop(ItemName.BAI_THI_10_DIEM, 10, ItemShopTypePrice.DIAMOND, 50));
        ItemShop itemShop = new ItemShop(ItemName.BAI_THI_10_DIEM, 1000, ItemShopTypePrice.DIAMOND, 4900);
        ItemOption itemOption = itemShop.getOption(74);
        if (itemOption != null) {
            itemOption.param *= 98;
        }
        itemShops.add(itemShop);
        return itemShops;
    }

    @Override
    public List<ItemMap> throwItem(Player killer, Entity entity) {
        List<ItemMap> itemMaps = new ArrayList<>();
        if (entity.isMonster() && entity.level > 10 && Math.abs(killer.level - entity.level) < 10) {
            if (Utils.isPercent(10)) {
                itemMaps.add(ItemManager.getInstance().createItemMap(ItemName.HOA_HONG_DO, 1, killer.id));
            }
            if (Utils.isPercent(10)) {
                if (killer.gender == 0) {
                    itemMaps.add(ItemManager.getInstance().createItemMap(ItemName.PHAN_TRANG, 1, killer.id));
                } else if (killer.gender == 1) {
                    itemMaps.add(ItemManager.getInstance().createItemMap(ItemName.BUT_CHI, 1, killer.id));
                } else if (killer.gender == 2) {
                    itemMaps.add(ItemManager.getInstance().createItemMap(ItemName.THUOC_KE, 1, killer.id));
                }
            }
        }
        if (entity.isBoss() && entity.zone != null && !entity.zone.map.isSurvival()) {
            itemMaps.add(ItemManager.getInstance().createItemMap(ItemName.SO_DAU_BAI, 1, killer.id));
        }
        return itemMaps;
    }

    @Override
    public List<ItemShop> showShopExchangePoint(Player player) {
        List<ItemShop> itemShopList = new ArrayList<>();
        for (Object[] objects : ITEM_EXCHANGE_POINT) {
            ItemShop itemShop = new ItemShop((int) objects[0], (int) objects[1], (ItemShopTypePrice) objects[2], (int) objects[3]);
            int expiry = (int) objects[4];
            if (expiry != 0) {
                itemShop.setExpiry(expiry);
            }
            itemShopList.add(itemShop);
            if (itemShop.template.isUpToUp && (int) objects[5] == 1) {
                int[][] quantities = new int[][]{{10, 10}, {100, 95}};
                for (int[] quantity : quantities) {
                    ItemShop item = new ItemShop(itemShop.template, itemShop.quantity * quantity[0], itemShop.typePrice, itemShop.price * quantity[1]);
                    for (ItemOption option : item.options) {
                        if (option.template.id == 24) {
                            item.quantity = 1;
                            option.param *= quantity[0];
                            break;
                        }
                    }
                    itemShopList.add(item);
                }
            }
        }
        return itemShopList;
    }

    @Override
    public void rewardTaskDaily(Player player) {
        player.addItem(ItemManager.getInstance().createItem(ItemName.HOA_HONG_XANH, Utils.nextInt(1, 5), true), true);
    }

    @Override
    public void rewardRecharge(Player player, int diamond) {
        if (diamond >= 700) {
            player.addItem(ItemManager.getInstance().createItem(ItemName.CAPSULE_TEACHER, diamond / 700, true), true);
        }
    }

    @Override
    public void useItem(Player player, Item item) {
        if (player.isBagFull()) {
            player.addInfo(Player.INFO_RED, Language.ME_BAG_FULL);
            return;
        }
        switch (item.template.id) {
            case ItemName.CAPSULE_TEACHER: {
                player.removeQuantityItemBag(item, 1);
                int[] items = {ItemName.PUAR_LEM_LINH, ItemName.PUAR_TINH_NGHICH, ItemName.PUAR_TRI_THUC, ItemName.CAI_TRANG_QUY_LAO,
                        ItemName.CAI_TRANG_THAN_VU_TRU, ItemName.RUA_BAY};
                Item newItem = ItemManager.getInstance().createItem(Utils.nextArray(items), 1, false);
                newItem.createOptionEvent();
                player.addItem(newItem, true);
                if (newItem.getExpiry() == -1) {
                    Server.getInstance().service.serverChat(String.format("Chúc mừng %s mở %s nhận được %s vĩnh viễn", player.name, item.template.name, newItem.template.name));
                }
                return;
            }

            case ItemName.HOP_QUA_DO:
            case ItemName.HOP_QUA_XANH:
            case ItemName.GIO_VAT_TU: {
                player.removeQuantityItemBag(item, 1);
                boolean isSpecial = item.template.id == ItemName.HOP_QUA_DO;
                int itemId = next(isSpecial ? Event.TYPE_SPECIAL : Event.TYPE_NORMAL);
                Item itm = ItemManager.getInstance().createItem(itemId, 1, false);
                if (itm.template.id == ItemName.HUY_HIEU_20_11) {
                    itm.createOptionEvent();
                } else if (itm.template.type == ItemType.TYPE_BODY_PET) {
                    itm.setExpiry(7);
                } else {
                    itm.setDefaultOption();
                }
                player.addItem(itm, true);
                player.updateTimeEvent = System.currentTimeMillis();
                if (isSpecial) {
                    player.pointEvent++;
                    Top top = TopManager.getInstance().tops.get(TopType.TOP_EVENT);
                    if (top != null) {
                        top.update(player);
                    }
                } else {
                    player.pointOtherEvent++;
                    Top top = TopManager.getInstance().tops.get(TopType.TOP_EVENT_OTHER);
                    if (top != null) {
                        top.update(player);
                    }
                }
                player.pointRewardEvent += isSpecial ? 20 : 2;
                return;
            }
        }

    }

    @Override
    public void start() {

    }*/
}
