package com.beemobi.rongthanonline.event;

import com.beemobi.rongthanonline.bot.escort.MiNuong;
import com.beemobi.rongthanonline.command.Command;
import com.beemobi.rongthanonline.command.CommandName;
import com.beemobi.rongthanonline.common.Language;
import com.beemobi.rongthanonline.entity.Entity;
import com.beemobi.rongthanonline.entity.monster.Monster;
import com.beemobi.rongthanonline.entity.monster.event.DuaHau;
import com.beemobi.rongthanonline.entity.player.Player;
import com.beemobi.rongthanonline.item.*;
import com.beemobi.rongthanonline.map.Map;
import com.beemobi.rongthanonline.map.MapManager;
import com.beemobi.rongthanonline.map.MapName;
import com.beemobi.rongthanonline.map.Zone;
import com.beemobi.rongthanonline.model.input.ClientInputType;
import com.beemobi.rongthanonline.model.input.TextField;
import com.beemobi.rongthanonline.network.Message;
import com.beemobi.rongthanonline.npc.NpcName;
import com.beemobi.rongthanonline.server.Server;
import com.beemobi.rongthanonline.shop.Shop;
import com.beemobi.rongthanonline.shop.ShopManager;
import com.beemobi.rongthanonline.shop.ShopType;
import com.beemobi.rongthanonline.shop.TypePrice;
import com.beemobi.rongthanonline.top.TopManager;
import com.beemobi.rongthanonline.top.TopType;
import com.beemobi.rongthanonline.util.Utils;
import org.apache.log4j.Logger;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.temporal.WeekFields;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

public class HungVuong extends Event {
    private static final Logger logger = Logger.getLogger(HungVuong.class);

    public static final Object[][] ITEM_EXCHANGE_POINT = new Object[][]{
            {ItemName.CAI_TRANG_MI_NUONG, 1, TypePrice.POINT_EVENT, 40000, 0, 0},
            {ItemName.HUY_HIEU_HUNG_VUONG, 1, TypePrice.POINT_EVENT, 25000, 0, 0},
            {ItemName.CAI_TRANG_QUY_LAO, 1, TypePrice.POINT_EVENT, 50000, 0, 0},
            {ItemName.SAO_PHA_LE_DANH_BONG, 1, TypePrice.POINT_EVENT, 100, 7, 0},
            {ItemName.NANG_CAP_CHIEU_1_DE_TU, 1, TypePrice.POINT_EVENT, 300, 7, 0},
            {ItemName.NANG_CAP_CHIEU_2_DE_TU, 1, TypePrice.POINT_EVENT, 500, 7, 0},
            {ItemName.NANG_CAP_CHIEU_3_DE_TU, 1, TypePrice.POINT_EVENT, 700, 7, 0},
            {ItemName.NANG_CAP_CHIEU_4_DE_TU, 1, TypePrice.POINT_EVENT, 900, 7, 0},
            {ItemName.NANG_CAP_CHIEU_5_DE_TU, 1, TypePrice.POINT_EVENT, 1100, 7, 0},
            {ItemName.DUA_NAU, 1, TypePrice.POINT_EVENT, 10, 7, 1},
            {ItemName.DUA_VANG, 1, TypePrice.POINT_EVENT, 10, 7, 1},
            {ItemName.DUA_XANH, 1, TypePrice.POINT_EVENT, 10, 7, 1},
            {ItemName.LOI_KI_NANG, 1, TypePrice.POINT_EVENT, 4, 7, 1},
            {ItemName.BI_KIP_KY_NANG, 1, TypePrice.POINT_EVENT, 1, 7, 1},
            {ItemName.THE_DOI_KY_NANG_DE_TU, 1, TypePrice.POINT_EVENT, 1500, 7, 0},
            {ItemName.SACH_KY_NANG, 1, TypePrice.POINT_EVENT, 500, 7, 0},
    };

    public int countSpecial;

    public int countNormal;

    public HungVuong(String name, Timestamp startTime, Timestamp endTime) {
        super(name, startTime, endTime);
        GIFT_NORMAL.add(15, ItemName.DA_7);
        GIFT_NORMAL.add(15, ItemName.DA_8);
        GIFT_NORMAL.add(5, ItemName.DA_9);
        GIFT_NORMAL.add(2, ItemName.DA_10);
        GIFT_NORMAL.add(3, ItemName.NGOC_RONG_5_SAO);
        GIFT_NORMAL.add(8, ItemName.NGOC_RONG_6_SAO);
        GIFT_NORMAL.add(15, ItemName.NGOC_RONG_7_SAO);
        GIFT_NORMAL.add(10, ItemName.BUA_BAO_VE_CAP_1);
        GIFT_NORMAL.add(5, ItemName.MAM_THIEN_MOC);
        GIFT_NORMAL.add(1, ItemName.HUY_HIEU_HUNG_VUONG);
        GIFT_NORMAL.add(1, ItemName.MANH_GIAY);
        GIFT_NORMAL.add(5, ItemName.VUOT_LONG_THAN);
        GIFT_NORMAL.add(5, ItemName.GIAP_LONG_BAO);
        GIFT_NORMAL.add(5, ItemName.MU_VUONG_LONG);
        GIFT_NORMAL.add(5, ItemName.GONG_THIEN_LONG);
        GIFT_NORMAL.add(5, ItemName.LINH_HON_LONG_THE);
        GIFT_NORMAL.add(0.05, ItemName.DA_HAI_LAM);
        GIFT_NORMAL.add(0.5, ItemName.BUA_TAY_CHI_SO);

        GIFT_SPECIAL.add(5, ItemName.DA_11);
        GIFT_SPECIAL.add(5, ItemName.DA_10);
        GIFT_SPECIAL.add(5, ItemName.DA_9);
        GIFT_SPECIAL.add(5, ItemName.BUA_BAO_VE_CAP_2);
        GIFT_SPECIAL.add(5, ItemName.BUA_BAO_VE_CAP_3);
        GIFT_SPECIAL.add(3, ItemName.NGOC_RONG_4_SAO);
        GIFT_SPECIAL.add(1, ItemName.NGOC_RONG_3_SAO);
        GIFT_SPECIAL.add(10, ItemName.DUA_XANH);
        GIFT_SPECIAL.add(10, ItemName.DUA_VANG);
        GIFT_SPECIAL.add(10, ItemName.DUA_NAU);
        GIFT_SPECIAL.add(5, ItemName.HOA_CHUOI);
        GIFT_SPECIAL.add(5, ItemName.HUY_HIEU_HUNG_VUONG);
        GIFT_SPECIAL.add(5, ItemName.CAI_TRANG_MI_NUONG);
        GIFT_SPECIAL.add(5, ItemName.CHIM_LAC_TAM_LINH);
        GIFT_SPECIAL.add(5, ItemName.DO_LONG_DAO);
        GIFT_SPECIAL.add(5, ItemName.CANH_DONG_SON);
        GIFT_SPECIAL.add(5, ItemName.DA_HAI_LAM);
        GIFT_SPECIAL.add(5, ItemName.BUA_TAY_CHI_SO);
    }

    @Override
    public void openMenu(Player player, Object[] elements) {

        int action = (int) elements[0];
        switch (action) {
            case -1: {
                List<Command> commands = new ArrayList<>();
                commands.add(new Command(CommandName.SHOW_EVENT, "Thông tin sự kiện", player, 0));
                commands.add(new Command(CommandName.SHOW_EVENT, "TOP\nSự kiện", player, 1, -1));
                commands.add(new Command(CommandName.SHOW_EVENT, "Chế tạo lễ vật", player, 2));
                commands.add(new Command(CommandName.SHOW_EVENT, "Đổi điểm", player, 4));
                player.createMenu(NpcName.ME, String.format("Sự kiện %s", this.name), commands);
                return;
            }

            case 0: {
                player.service.startDialogOk("Thông tin chi tiết xem tại website rongthanonline.vn");
                return;
            }

            case 1: {
                int index = (int) elements[1];
                if (index == -1) {
                    List<Command> commands = new ArrayList<>();
                    commands.add(new Command(CommandName.SHOW_EVENT, "Top\nđặc biệt", player, 1, 0));
                    commands.add(new Command(CommandName.SHOW_EVENT, "Top\nthường", player, 1, 1));
                    commands.add(new Command(CommandName.SHOW_EVENT, String.format("Top\ntuần\n[%d/%d]", Utils.getWeekOfYearNow(), Utils.getYearNow()), player, 1, 2, -1));
                    LocalDateTime previousWeek = LocalDateTime.now().minusWeeks(1);
                    commands.add(new Command(CommandName.SHOW_EVENT, String.format("Top\ntuần trước\n[%d/%d]", previousWeek.get(WeekFields.of(Locale.getDefault()).weekOfYear()), previousWeek.getYear()), player, 1, 3, -1));
                    player.createMenu(NpcName.ME, "Bạn đứng top bao nhiêu?", commands);
                } else if (index == 0) {
                    TopManager.getInstance().showTop(player, TopType.TOP_EVENT);
                } else if (index == 1) {
                    TopManager.getInstance().showTop(player, TopType.TOP_EVENT_OTHER);
                } else if (index == 2) {
                    int type = (int) elements[2];
                    if (type == -1) {
                        List<Command> commands = new ArrayList<>();
                        commands.add(new Command(CommandName.SHOW_EVENT, "Lễ vật đặc biệt", player, 1, 2, 0));
                        commands.add(new Command(CommandName.SHOW_EVENT, "Lễ vật thường", player, 1, 2, 1));
                        player.createMenu(NpcName.ME, "Bạn đứng top bao nhiêu?", commands);
                    } else if (type == 0) {
                        TopManager.getInstance().showTop(player, TopType.TOP_EVENT_WEEK);
                    } else if (type == 1) {
                        TopManager.getInstance().showTop(player, TopType.TOP_EVENT_WEEK_OTHER);
                    }
                } else if (index == 3) {
                    int type = (int) elements[2];
                    if (type == -1) {
                        List<Command> commands = new ArrayList<>();
                        commands.add(new Command(CommandName.SHOW_EVENT, "Lễ vật đặc biệt", player, 1, 3, 0));
                        commands.add(new Command(CommandName.SHOW_EVENT, "Lễ vật thường", player, 1, 3, 1));
                        player.createMenu(NpcName.ME, "Bạn đứng top bao nhiêu?", commands);
                    } else if (type == 0) {
                        TopManager.getInstance().showTop(player, TopType.TOP_EVENT_LAST_WEEK);
                    } else if (type == 1) {
                        TopManager.getInstance().showTop(player, TopType.TOP_EVENT_LAST_WEEK_OTHER);
                    }
                }
                return;
            }

            case 2: {
                List<Command> commands = new ArrayList<>();
                commands.add(new Command(CommandName.SHOW_EVENT, "Lễ vật thường", player, 3, 0, -1));
                commands.add(new Command(CommandName.SHOW_EVENT, "Lễ vật đặc biệt", player, 3, 1, -1));
                player.createMenu(NpcName.ME, "Chế tạo lễ vật", commands);
                return;
            }

            case 3: {
                int type = (int) elements[1];
                int quantity = (int) elements[2];
                if (quantity == -1) {
                    player.startClientInput(ClientInputType.INPUT_EVENT, "Nhập số lượng", new TextField("Số lượng", TextField.TYPE_NORMAL));
                    player.clientInputType.elements = new Object[]{action, type};
                } else if (quantity > 0) {
                    exchange(player, type, quantity);
                }
                return;
            }

            case 4: {
                Shop shop = ShopManager.getInstance().shops.get(ShopType.SHOP_EVENT);
                if (shop != null) {
                    shop.show(player);
                }
                return;
            }

        }
    }

    public void confirmClientInput(Player player, Message message) {
        try {
            ClientInputType clientInputType = player.clientInputType;
            if (clientInputType == null) {
                return;
            }
            int action = (int) clientInputType.elements[0];
            if (action == 3) {
                int type = (int) clientInputType.elements[1];
                int quantity = Integer.parseInt(message.reader().readUTF());
                if (quantity <= 0) {
                    player.addInfo(Player.INFO_RED, "Số lượng không hợp lệ");
                    return;
                }
                List<Command> commands = new ArrayList<>();
                commands.add(new Command(CommandName.SHOW_EVENT, "OK", player, 3, type, quantity));
                commands.add(new Command(CommandName.CANCEL, "Hủy", player, 3, type, quantity));
                player.createMenu(NpcName.ME, String.format("Bạn có chắc chắn muốn làm x%d Lễ vật %s", quantity, type == 0 ? "thường" : "đặc biệt"), commands);
            }
        } catch (Exception ex) {
            logger.error("confirmClientInput", ex);
        }
    }

    public void exchange(Player player, int type, int quantity) {
        if (player.isTrading()) {
            player.addInfo(Player.INFO_RED, Language.CANCEL_ACTION_WHEN_TRADE);
            return;
        }
        if (player.isBagFull()) {
            player.addInfo(Player.INFO_RED, Language.ME_BAG_FULL);
            return;
        }
        int[] items;
        int[] requires = new int[]{1, 10, 10, 10};
        if (type == 0) {
            items = new int[]{ItemName.MANH_BAU_VAT_THUONG, ItemName.VOI_9_NGA, ItemName.GA_9_CUA, ItemName.NGUA_9_HONG_MAO};
        } else {
            items = new int[]{ItemName.MANH_BAU_VAT_DAC_BIET, ItemName.VOI_9_NGA, ItemName.GA_9_CUA, ItemName.NGUA_9_HONG_MAO};
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
        boolean isLock = false;
        for (int i = 0; i < player.itemsBag.length; i++) {
            Item item = player.itemsBag[i];
            if (item != null) {
                for (int j = 0; j < items.length; j++) {
                    if (item.template.id == items[j] && requires[j] > 0) {
                        /*if (item.isLock()) {
                            isLock = true;
                        }*/
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
        Item item = ItemManager.getInstance().createItem(type == 0 ? ItemName.LE_VAT_THUONG : ItemName.LE_VAT_DAC_BIET, quantity, true);
        if (isLock) {
           // item.setLock();
        }
        player.addItem(item, true);
    }

    @Override
    public void useItem(Player player, Item item) {
        if (player.isBagFull()) {
            player.addInfo(Player.INFO_RED, Language.ME_BAG_FULL);
            return;
        }
        switch (item.template.id) {
            case ItemName.DUA_HAU: {
                player.removeQuantityItemBag(item, 1);
                if (Utils.isPercent(70)) {
                    int coin;
                    int percent = Utils.nextInt(100);
                    if (percent < 10) {
                        coin = Utils.nextInt(700000, 1000000);
                    } else if (percent < 40) {
                        coin = Utils.nextInt(200000, 500000);
                    } else {
                        coin = Utils.nextInt(50000, 100000);
                    }
                    player.upXu(coin);
                    player.addInfo(Player.INFO_YELLOW, String.format("Bạn nhận được %s xu", Utils.getMoneys(coin)));
                } else {
                    player.upDiamond(1);
                    player.addInfo(Player.INFO_YELLOW, "Bạn nhận được 1 Kim cương");
                }
                return;
            }

            case ItemName.CAPSULE_HUNG_VUONG: {
                player.removeQuantityItemBag(item, 1);
                int[] items = {ItemName.HUY_HIEU_HUNG_VUONG, ItemName.CAI_TRANG_MI_NUONG, ItemName.CANH_DONG_SON, ItemName.DO_LONG_DAO, ItemName.CHIM_LAC_TAM_LINH};
                Item newItem = ItemManager.getInstance().createItem(Utils.nextArray(items), 1, false);
                newItem.createOptionEvent();
                int percent = item.getParam(161);
                if (Utils.isPercent(Math.max(3, percent))) {
                    Server.getInstance().service.serverChat(String.format("Chúc mừng %s mở %s nhận được %s vĩnh viễn", player.name, item.template.name, newItem.template.name));
                } else {
                    newItem.setExpiry(Utils.nextInt(7, 15));
                }
                player.addItem(newItem, true);
                return;
            }

            case ItemName.LE_VAT_DAC_BIET:
            case ItemName.LE_VAT_THUONG: {
                player.removeQuantityItemBag(item, 1);
                boolean isSpecial = item.template.id == ItemName.LE_VAT_DAC_BIET;
                int itemId;
                if (isSpecial) {
                    itemId = GIFT_SPECIAL.next();
                } else {
                    itemId = GIFT_NORMAL.next();
                }
                Item itm = ItemManager.getInstance().createItem(itemId, 1, false);
                if (itm.template.id == ItemName.HUY_HIEU_HUNG_VUONG || itm.template.id == ItemName.CAI_TRANG_MI_NUONG
                        || itm.template.id == ItemName.CANH_DONG_SON || itm.template.id == ItemName.DO_LONG_DAO || itm.template.id == ItemName.CHIM_LAC_TAM_LINH) {
                    itm.createOptionEvent();
                    if ((isSpecial && Utils.nextInt(1000) == 0) || (!isSpecial && Utils.nextInt(10000) == 0)) {
                        itm.setExpiry(-1);
                        Server.getInstance().service.serverChat(String.format("Chúc mừng %s mở %s nhận được %s vĩnh viễn", player.name, item.template.name, itm.template.name));
                    } else {
                        itm.setExpiry(1);
                    }
                } else if (itm.template.type == ItemType.TYPE_BODY_PET) {
                    itm.setExpiry(7);
                } else {
                    itm.setDefaultOption();
                }
                player.addItem(itm, true);
                player.updateTimeEvent = System.currentTimeMillis();
                if (isSpecial) {
                    player.pointEvent++;
                    //player.upPointTopWeek(TopType.TOP_EVENT_WEEK, 1);
                } else {
                    player.pointOtherEvent++;
                    //player.upPointTopWeek(TopType.TOP_EVENT_WEEK_OTHER, 1);
                }
                player.pointRewardEvent += isSpecial ? 10 : 2;
                boolean isHaveItem = false;
                if (isSpecial) {
                    countSpecial++;
                    if (countSpecial > 2000) {
                        isHaveItem = true;
                    }
                } else {
                    countNormal++;
                    if (countNormal > 5000) {
                        isHaveItem = true;
                    }
                }
                if (isHaveItem) {
                    Item newItem = ItemManager.getInstance().createItem(ItemName.CAPSULE_HUYEN_BI, 1, false);
                    newItem.setExpiry(3);
                    if (player.addItem(newItem)) {
                        Server.getInstance().service.serverChat(String.format("Chúc mừng %s mở %s nhận được %s", player.name, item.template.name, newItem.template.name));
                        if (isSpecial) {
                            countSpecial = 0;
                        } else {
                            countNormal = 0;
                        }
                    }
                }
                return;
            }
        }
    }

    @Override
    public List<ItemShop> createShop(Player player) {
        List<ItemShop> itemShops = new ArrayList<>();
        itemShops.add(new ItemShop(ItemName.MANH_BAU_VAT_THUONG, 1, TypePrice.COIN, 1000000));
        itemShops.add(new ItemShop(ItemName.MANH_BAU_VAT_THUONG, 100, TypePrice.COIN, 100000000));

        ItemShop itemShop1 = new ItemShop(ItemName.MANH_BAU_VAT_DAC_BIET, 25, TypePrice.DIAMOND, 50);
        itemShop1.options.add(new ItemOption(74, 5));
        itemShops.add(itemShop1);

        ItemShop itemShop2 = new ItemShop(ItemName.MANH_BAU_VAT_DAC_BIET, 2500, TypePrice.DIAMOND, 4900);
        itemShop2.options.add(new ItemOption(74, 490));
        itemShops.add(itemShop2);

        ItemShop itemShop3 = new ItemShop(ItemName.MANH_BAU_VAT_DAC_BIET, 10, TypePrice.DIAMOND, 50);
        //itemShop3.setLock();
        itemShops.add(itemShop3);

        ItemShop itemShop4 = new ItemShop(ItemName.MANH_BAU_VAT_DAC_BIET, 1000, TypePrice.DIAMOND, 5000);
        //itemShop4.setLock();
        itemShops.add(itemShop4);

        itemShops.add(new ItemShop(ItemName.CAPSULE_HUNG_VUONG, 1, TypePrice.DIAMOND, 500));
        itemShops.add(new ItemShop(ItemName.CAPSULE_HUNG_VUONG, 10, TypePrice.DIAMOND, 4900));
        itemShops.add(new ItemShop(ItemName.CAPSULE_HUNG_VUONG, 100, TypePrice.DIAMOND, 45000));
        itemShops.add(new ItemShop(ItemName.THE_DOI_TEN_DAC_BIET, 1, TypePrice.DIAMOND, 2000));
        itemShops.add(new ItemShop(ItemName.THE_DOI_TEN_DE_TU_DAC_BIET, 1, TypePrice.DIAMOND, 2000));
        itemShops.add(new ItemShop(ItemName.SACH_SUPER_KAMEJOKO, 1, TypePrice.DIAMOND, 1000));
        itemShops.add(new ItemShop(ItemName.SACH_MA_PHONG_BA, 1, TypePrice.DIAMOND, 1000));
        itemShops.add(new ItemShop(ItemName.SACH_BIGBANG_FALSH, 1, TypePrice.DIAMOND, 1000));
        itemShops.add(new ItemShop(ItemName.SACH_THOI_MIEN, 1, TypePrice.DIAMOND, 2000));
        return itemShops;
    }

    @Override
    public List<ItemMap> throwItem(Player killer, Entity entity) {
        List<ItemMap> itemMaps = new ArrayList<>();
        if (entity.isMonster() && killer.level > 10 && Math.abs(killer.level - entity.level) < 10) {
            if (Utils.isPercent(10)) {
                itemMaps.add(ItemManager.getInstance().createItemMap(ItemName.VOI_9_NGA, 1, killer.id));
            }
        }
        return itemMaps;
    }

    @Override
    public void rewardTaskDaily(Player player) {
        if (!player.isBagFull() && player.level >= 10) {
            int min = (player.level / 10) * 10 + 1;
            if (player.level < 20) {
                min = min / 4 + 1;
            } else if (player.level < 30) {
                min = min / 3 + 1;
            } else if (player.level < 40) {
                min = min / 2 + 1;
            }
            player.addItem(ItemManager.getInstance().createItem(ItemName.GA_9_CUA, Utils.nextInt(min, 2 * min), true), true);
        }
    }

    @Override
    public void rewardRecharge(Player player, int diamond) {
        if (!player.isBagFull()) {
            int quantity = diamond / 700;
            if (quantity > 0) {
                player.addItem(ItemManager.getInstance().createItem(ItemName.CAPSULE_HUNG_VUONG, quantity, true), true);
            }
        }
    }

    @Override
    public List<ItemShop> showShopExchangePoint(Player player) {
        List<ItemShop> itemShopList = new ArrayList<>();
        for (Object[] objects : ITEM_EXCHANGE_POINT) {
            ItemShop itemShop = new ItemShop((int) objects[0], (int) objects[1], (TypePrice) objects[2], (int) objects[3]);
            if (itemShop.template.id == ItemName.CAI_TRANG_MI_NUONG) {
                itemShop.options.clear();
                itemShop.options.add(new ItemOption(25, 40));
                itemShop.options.add(new ItemOption(31, 50));
                itemShop.options.add(new ItemOption(32, 50));
                itemShop.options.add(new ItemOption(1, 1500));
                itemShop.options.add(new ItemOption(2, 4000));
                itemShop.options.add(new ItemOption(6, 2000));
                itemShop.options.add(new ItemOption(85, 20));
                itemShop.options.add(new ItemOption(167, 25));
                itemShop.options.add(new ItemOption(33, 2));
                itemShop.options.add(new ItemOption(34, 2));
                itemShop.options.add(new ItemOption(35, 2));
                itemShop.options.add(new ItemOption(78, 50));
            }
            if (itemShop.template.id == ItemName.HUY_HIEU_HUNG_VUONG) {
                itemShop.options.clear();
                itemShop.options.add(new ItemOption(25, 40));
                itemShop.options.add(new ItemOption(31, 40));
                itemShop.options.add(new ItemOption(32, 40));
                itemShop.options.add(new ItemOption(99, 1500));
                itemShop.options.add(new ItemOption(165, 1));
                itemShop.options.add(new ItemOption(33, 1));
                itemShop.options.add(new ItemOption(34, 1));
                itemShop.options.add(new ItemOption(35, 1));
            }
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
    public void start() {
        for (int i = 0; i < 50; i++) {
            new MiNuong().enter(MiNuong.MAPS);
        }
        int[] MAPS = {MapName.BO_SONG_PU, MapName.THAC_NUOC_KEISE,
                MapName.RUNG_NAM_FUKA, MapName.DOI_HOANG_AKA, MapName.THUNG_LUNG_MARIA,
                MapName.DONG_BANG_MIKA, MapName.CAO_NGUYEN_TAKA, MapName.RUNG_GOZA,
                MapName.LANG_ARU, MapName.VACH_NUI_ARU, MapName.THANH_PHO_PEIN, MapName.THANH_PHO_NAM,
                MapName.THANH_PHO_PHIA_NAM, MapName.BO_VUC_GIRAN, MapName.VUC_GIRAN, MapName.RUNG_NGUYEN_THUY,
                MapName.HOANG_MAC_SARA, MapName.PHIA_DONG_SARA, MapName.PHIA_TAY_SARA, MapName.PHIA_BAC_SARA};
        Utils.setTimeout(() -> {
            while (Server.getInstance().isRunning) {
                try {
                    List<Zone> zoneList = new ArrayList<>();
                    for (int mapId : MAPS) {
                        Map map = MapManager.getInstance().maps.get(mapId);
                        if (map != null) {
                            zoneList.addAll(map.zones.stream().filter(area -> area.id > 0).collect(Collectors.toList()));
                        }
                    }
                    Collections.shuffle(zoneList);
                    int length = Math.min(30, zoneList.size());
                    for (int i = 0; i < length; i++) {
                        Zone zone = zoneList.get(i);
                        Monster monster = new DuaHau();
                        monster.x = zone.map.template.width / 2;
                        monster.y = zone.map.getYSd(monster.x);
                        zone.enter(monster);
                    }
                    Thread.sleep(1800000);
                } catch (Exception ex) {
                    logger.error("update monster error", ex);
                }
            }
        }, 10000);
    }
}
