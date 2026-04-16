package com.beemobi.rongthanonline.event;

import com.beemobi.rongthanonline.bot.boss.other.DarkSanta;
import com.beemobi.rongthanonline.bot.escort.TuanLoc;
import com.beemobi.rongthanonline.command.Command;
import com.beemobi.rongthanonline.command.CommandName;
import com.beemobi.rongthanonline.common.Language;
import com.beemobi.rongthanonline.entity.Entity;
import com.beemobi.rongthanonline.entity.monster.big.TuyetLinh;
import com.beemobi.rongthanonline.entity.player.Player;
import com.beemobi.rongthanonline.item.*;
import com.beemobi.rongthanonline.map.Map;
import com.beemobi.rongthanonline.map.MapManager;
import com.beemobi.rongthanonline.map.MapName;
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
import java.util.List;
import java.util.Locale;

public class Noel2023 extends Event {
    private static final Logger logger = Logger.getLogger(Noel2023.class);

    public static final Object[][] ITEM_EXCHANGE_POINT = new Object[][]{
            {ItemName.SAO_PHA_LE_DANH_BONG, 1, TypePrice.POINT_EVENT, 40, 7, 0},
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
    };

    public Noel2023(String name, Timestamp startTime, Timestamp endTime) {
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
        GIFT_NORMAL.add(5, ItemName.THIEN_MOC_QUA);
        GIFT_NORMAL.add(1, ItemName.HUY_HIEU_NOEL);
        GIFT_NORMAL.add(1, ItemName.MANH_GIAY);
        GIFT_NORMAL.add(1, ItemName.CAI_TRANG_CHI_CHI);
        GIFT_NORMAL.add(1, ItemName.XE_KEO_NOEL);

        GIFT_SPECIAL.add(5, ItemName.DA_11);
        GIFT_SPECIAL.add(5, ItemName.DA_10);
        GIFT_SPECIAL.add(5, ItemName.DA_9);
        GIFT_SPECIAL.add(5, ItemName.BUA_BAO_VE_CAP_2);
        GIFT_SPECIAL.add(5, ItemName.BUA_BAO_VE_CAP_3);
        GIFT_SPECIAL.add(10, ItemName.DUA_XANH);
        GIFT_SPECIAL.add(10, ItemName.DUA_VANG);
        GIFT_SPECIAL.add(10, ItemName.DUA_NAU);
        GIFT_SPECIAL.add(5, ItemName.MAM_HAC_LONG);
        GIFT_SPECIAL.add(10, ItemName.HAC_LONG_QUA);
        GIFT_SPECIAL.add(5, ItemName.MAM_CAY_THONG);
        GIFT_SPECIAL.add(10, ItemName.QUA_THONG_EXP);
        GIFT_SPECIAL.add(5, ItemName.VUOT_LONG_THAN);
        GIFT_SPECIAL.add(5, ItemName.GIAP_LONG_BAO);
        GIFT_SPECIAL.add(5, ItemName.MU_VUONG_LONG);
        GIFT_SPECIAL.add(5, ItemName.GONG_THIEN_LONG);
        GIFT_SPECIAL.add(5, ItemName.LINH_HON_LONG_THE);
        GIFT_SPECIAL.add(5, ItemName.HUY_HIEU_NOEL);
        GIFT_SPECIAL.add(5, ItemName.KARIN_MAY_MAN);
        GIFT_SPECIAL.add(5, ItemName.KEO_GAY_NOEL);
        GIFT_SPECIAL.add(5, ItemName.BUBBLES_HUYEN_BI);
        GIFT_SPECIAL.add(5, ItemName.TOTORO_LINH_THIENG);
        GIFT_SPECIAL.add(0.01, ItemName.SACH_THOI_MIEN);
    }

    @Override
    public void openMenu(Player player, Object[] elements) {
        int action = (int) elements[0];
        switch (action) {
            case -1: {
                List<Command> commands = new ArrayList<>();
                commands.add(new Command(CommandName.SHOW_EVENT, "Thông tin sự kiện", player, 0));
                commands.add(new Command(CommandName.SHOW_EVENT, "TOP\nSự kiện", player, 1, -1));
                commands.add(new Command(CommandName.SHOW_EVENT, "Chế tạo\nhộp quà", player, 2));
                commands.add(new Command(CommandName.SHOW_EVENT, "Đổi điểm", player, 4));
                player.createMenu(NpcName.ME, String.format("Sự kiện %s", this.name), commands);
                return;
            }

            case 0: {
                player.service.startDialogOk("Thông tin chi tiết xem tại website rto.lynxphg.me");
                return;
            }

            case 1: {
                int index = (int) elements[1];
                if (index == -1) {
                    List<Command> commands = new ArrayList<>();
                    commands.add(new Command(CommandName.SHOW_EVENT, "Top\nđặc biệt", player, 1, 0));
                    commands.add(new Command(CommandName.SHOW_EVENT, "Top\nthường", player, 1, 1));
                    commands.add(new Command(CommandName.SHOW_EVENT, String.format("Top\ntuần\n[%d/%d]", Utils.getWeekOfYearNow(), Utils.getYearNow()), player, 1, 2));
                    LocalDateTime previousWeek = LocalDateTime.now().minusWeeks(1);
                    commands.add(new Command(CommandName.SHOW_EVENT, String.format("Top\ntuần trước\n[%d/%d]", previousWeek.get(WeekFields.of(Locale.getDefault()).weekOfYear()), previousWeek.getYear()), player, 1, 3));
                    player.createMenu(NpcName.ME, "Bạn đứng top bao nhiêu?", commands);
                } else if (index == 0) {
                    TopManager.getInstance().showTop(player, TopType.TOP_EVENT);
                } else if (index == 1) {
                    TopManager.getInstance().showTop(player, TopType.TOP_EVENT_OTHER);
                } else if (index == 2) {
                    TopManager.getInstance().showTop(player, TopType.TOP_EVENT_WEEK);
                } else if (index == 3) {
                    TopManager.getInstance().showTop(player, TopType.TOP_EVENT_LAST_WEEK);
                }
                return;
            }

            case 2: {
                List<Command> commands = new ArrayList<>();
                commands.add(new Command(CommandName.SHOW_EVENT, "Hộp quà\nthường", player, 3, 0, -1));
                commands.add(new Command(CommandName.SHOW_EVENT, "Hộp quà\nđặc biệt", player, 3, 1, -1));
                player.createMenu(NpcName.ME, "Chế tạo hộp quà", commands);
                return;
            }

            case 3: {
                int type = (int) elements[1];
                int quantity = (int) elements[2];
                if (quantity == -1) {
                    player.startClientInput(ClientInputType.INPUT_EVENT, "Nhập số lượng muốn chế tạo",
                            new TextField("Số lượng", TextField.TYPE_NORMAL));
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
                player.createMenu(NpcName.ME, String.format("Bạn có chắc chắn muốn chế tạo x%d Hộp quà %s", quantity, type == 0 ? "thường" : "đặc biệt"), commands);
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
        int[] requires = new int[]{1, 10, 1, 1, 1};
        if (type == 0) {
            items = new int[]{ItemName.GIAY_GOI_THUONG_NOEL, ItemName.DAY_TRANG_TRI, ItemName.CHUONG_VANG, ItemName.QUA_CAU_XANH, ItemName.NGOI_SAO_VANG};
        } else {
            items = new int[]{ItemName.GIAY_GOI_DAC_BIET_NOEL, ItemName.DAY_TRANG_TRI, ItemName.CHUONG_VANG, ItemName.QUA_CAU_DO, ItemName.NGUOI_TUYET};
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
                .createItem(type == 0 ? ItemName.HOP_QUA_NOEL_THUONG : ItemName.HOP_QUA_NOEL_DAC_BIET, quantity, true), true);
    }

    @Override
    public void useItem(Player player, Item item) {
        if (player.isBagFull()) {
            player.addInfo(Player.INFO_RED, Language.ME_BAG_FULL);
            return;
        }
        switch (item.template.id) {
            case ItemName.CAPSULE_NOEL: {
                player.removeQuantityItemBag(item, 1);
                int[] items = {ItemName.CAI_TRANG_CHI_CHI, ItemName.XE_KEO_NOEL, ItemName.KEO_GAY_NOEL, ItemName.KARIN_MAY_MAN,
                        ItemName.BUBBLES_HUYEN_BI, ItemName.TOTORO_LINH_THIENG};
                Item newItem = ItemManager.getInstance().createItem(Utils.nextArray(items), 1, false);
                newItem.createOptionEvent();
                player.addItem(newItem, true);
                if (newItem.getExpiry() == -1) {
                    Server.getInstance().service.serverChat(String.format("Chúc mừng %s mở %s nhận được %s vĩnh viễn", player.name, item.template.name, newItem.template.name));
                }
                return;
            }

            case ItemName.HOP_QUA_NOEL_DAC_BIET:
            case ItemName.HOP_QUA_NOEL_THUONG: {
                player.removeQuantityItemBag(item, 1);
                boolean isSpecial = item.template.id == ItemName.HOP_QUA_NOEL_DAC_BIET;
                int itemId;
                if (isSpecial) {
                    itemId = GIFT_SPECIAL.next();
                } else {
                    itemId = GIFT_NORMAL.next();
                }
                Item itm = ItemManager.getInstance().createItem(itemId, 1, false);
                if (itm.template.id == ItemName.HUY_HIEU_NOEL || itm.template.id == ItemName.CAI_TRANG_CHI_CHI || itm.template.id == ItemName.XE_KEO_NOEL
                        || itm.template.id == ItemName.KARIN_MAY_MAN || itm.template.id == ItemName.BUBBLES_HUYEN_BI || itm.template.id == ItemName.TOTORO_LINH_THIENG
                        || itm.template.id == ItemName.KEO_GAY_NOEL) {
                    itm.createOptionEvent();
                    if (Utils.nextInt(1000) == 0) {
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
                }
                player.pointRewardEvent += isSpecial ? 20 : 2;
                return;
            }
        }
    }

    @Override
    public List<ItemShop> createShop(Player player) {
        List<ItemShop> itemShops = new ArrayList<>();
        itemShops.add(new ItemShop(ItemName.GIAY_GOI_THUONG_NOEL, 1, TypePrice.COIN, 1000000));
        itemShops.add(new ItemShop(ItemName.GIAY_GOI_THUONG_NOEL, 100, TypePrice.COIN, 100000000));
        itemShops.add(new ItemShop(ItemName.GIAY_GOI_DAC_BIET_NOEL, 10, TypePrice.DIAMOND, 50));
        ItemShop itemShop = new ItemShop(ItemName.GIAY_GOI_DAC_BIET_NOEL, 1000, TypePrice.DIAMOND, 4900);
        ItemOption itemOption = itemShop.getOption(74);
        if (itemOption != null) {
            itemOption.param *= 98;
        }
        itemShops.add(itemShop);
        itemShops.add(new ItemShop(ItemName.CAPSULE_NOEL, 1, TypePrice.DIAMOND, 500));
        itemShops.add(new ItemShop(ItemName.CAPSULE_NOEL, 10, TypePrice.DIAMOND, 4500));
        itemShops.add(new ItemShop(ItemName.CAPSULE_NOEL, 100, TypePrice.DIAMOND, 40000));
        return itemShops;
    }

    @Override
    public List<ItemMap> throwItem(Player killer, Entity entity) {
        List<ItemMap> itemMaps = new ArrayList<>();
        if (entity.isMonster() && killer.level > 10 && entity.level > 10 && Math.abs(killer.level - entity.level) < 10) {
            if (Utils.isPercent(10)) {
                itemMaps.add(ItemManager.getInstance().createItemMap(ItemName.DAY_TRANG_TRI, 1, killer.id));
            }
            if (killer.zone != null && killer.zone.map.isBarrack() && Utils.isPercent(20)) {
                itemMaps.add(ItemManager.getInstance().createItemMap(ItemName.NGUOI_TUYET, 1, killer.id));
            }
        }
        if (entity.isBoss()) {
            itemMaps.add(ItemManager.getInstance().createItemMap(ItemName.QUA_CAU_DO, Utils.nextInt(5, 10), killer.id));
        }
        return itemMaps;
    }

    @Override
    public void rewardTaskDaily(Player player) {
        if (!player.isBagFull()) {
            player.addItem(ItemManager.getInstance().createItem(ItemName.QUA_CAU_XANH, 1, true), true);
        }
    }

    @Override
    public void rewardRecharge(Player player, int diamond) {
        if (!player.isBagFull()) {
            int quantity = diamond / 700;
            if (quantity > 0) {
                player.addItem(ItemManager.getInstance().createItem(ItemName.CAPSULE_NOEL, quantity, true), true);
            }
        }
    }

    @Override
    public List<ItemShop> showShopExchangePoint(Player player) {
        List<ItemShop> itemShopList = new ArrayList<>();
        for (Object[] objects : ITEM_EXCHANGE_POINT) {
            ItemShop itemShop = new ItemShop((int) objects[0], (int) objects[1], (TypePrice) objects[2], (int) objects[3]);
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
            new TuanLoc().enter(TuanLoc.MAPS);
        }
        for (int i = 0; i < 10; i++) {
            new DarkSanta().joinClient();
        }
        long day = 86400;
        Utils.setScheduled(() -> {
            Map map = MapManager.getInstance().maps.get(MapName.VACH_NUI_ARU);
            for (int i = 1; i < map.zones.size(); i++) {
                map.zones.get(i).enterBigMonster(new TuyetLinh());
            }
        }, day, 22, 0);
        Utils.setScheduled(() -> {
            Map map = MapManager.getInstance().maps.get(MapName.VACH_NUI_ARU);
            for (int i = 1; i < map.zones.size(); i++) {
                map.zones.get(i).enterBigMonster(new TuyetLinh());
            }
        }, day, 23, 0);
    }
}

