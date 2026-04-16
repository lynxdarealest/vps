package com.beemobi.rongthanonline.event;

import com.beemobi.rongthanonline.command.Command;
import com.beemobi.rongthanonline.command.CommandName;
import com.beemobi.rongthanonline.common.Language;
import com.beemobi.rongthanonline.entity.Entity;
import com.beemobi.rongthanonline.entity.monster.big.XichQuyBiNgo;
import com.beemobi.rongthanonline.entity.player.Player;
import com.beemobi.rongthanonline.item.*;
import com.beemobi.rongthanonline.map.Map;
import com.beemobi.rongthanonline.map.MapManager;
import com.beemobi.rongthanonline.map.Zone;
import com.beemobi.rongthanonline.model.PointWeeklyType;
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

public class Haloween2024 extends Event {
    public static final Object[][] ITEM_EXCHANGE_POINT = new Object[][]
            {
                    {ItemName.AURA_HALOWEEN, 1, TypePrice.POINT_EVENT, 20000, -1, 0},
                    {ItemName.AURA_HALOWEEN, 1, TypePrice.POINT_EVENT, 500, 7, 0},
                    {ItemName.KEO_CAM_LAU_HALLOWEEN_2023, 1, TypePrice.POINT_EVENT, 2, -1, 0},
                    {ItemName.KEO_DAU_LAU_HALLOWEEN_2023, 1, TypePrice.POINT_EVENT, 2, -1, 0},
                    {ItemName.KEO_MAT_XANH_HALLOWEEN_2023, 1, TypePrice.POINT_EVENT, 2, -1, 0},
                    {ItemName.KEO_THAN_CHET_HALLOWEEN_2023, 1, TypePrice.POINT_EVENT, 2, -1, 0},
                    {ItemName.TRAN_LONG_THACH, 1, TypePrice.POINT_EVENT, 10, -1, 0},
                    {ItemName.SAO_PHA_LE_DANH_BONG, 1, TypePrice.POINT_EVENT, 40, 30, 0},
                    {ItemName.NANG_CAP_CHIEU_1_DE_TU, 1, TypePrice.POINT_EVENT, 300, 7, 0},
                    {ItemName.NANG_CAP_CHIEU_2_DE_TU, 1, TypePrice.POINT_EVENT, 500, 7, 0},
                    {ItemName.NANG_CAP_CHIEU_3_DE_TU, 1, TypePrice.POINT_EVENT, 700, 7, 0},
                    {ItemName.NANG_CAP_CHIEU_4_DE_TU, 1, TypePrice.POINT_EVENT, 900, 7, 0},
                    {ItemName.NANG_CAP_CHIEU_5_DE_TU, 1, TypePrice.POINT_EVENT, 1100, 7, 0},
                    {ItemName.DUA_NAU, 1, TypePrice.POINT_EVENT, 10, 30, 1},
                    {ItemName.DUA_VANG, 1, TypePrice.POINT_EVENT, 10, 30, 1},
                    {ItemName.DUA_XANH, 1, TypePrice.POINT_EVENT, 10, 30, 1},
                    {ItemName.LOI_KI_NANG, 1, TypePrice.POINT_EVENT, 4, -1, 1},
                    {ItemName.BI_KIP_KY_NANG, 1, TypePrice.POINT_EVENT, 1, 7, 1},
                    {ItemName.THE_DOI_KY_NANG_DE_TU, 1, TypePrice.POINT_EVENT, 1500, 7, 0},
                    {ItemName.SACH_KY_NANG, 1, TypePrice.POINT_EVENT, 500, 7, 0},
            };
    private static final Logger logger = Logger.getLogger(CoHon2024.class);

    public Haloween2024(Timestamp startTime, Timestamp endTime) {
        super("Haloween 2024", startTime, endTime);
        GIFT_SPECIAL.add(5, ItemName.NGOC_RONG_3_SAO);
        GIFT_SPECIAL.add(5, ItemName.BUA_BAO_VE_CAP_1);
        GIFT_SPECIAL.add(5, ItemName.BUA_BAO_VE_CAP_2);
        GIFT_SPECIAL.add(2, ItemName.BUA_BAO_VE_CAP_3);
        GIFT_SPECIAL.add(1, ItemName.BUA_BAO_VE_CAP_4);
        GIFT_SPECIAL.add(5, ItemName.DA_10);
        GIFT_SPECIAL.add(5, ItemName.TRAI_DUA_EXP);
        GIFT_SPECIAL.add(5, ItemName.VUOT_LONG_THAN);
        GIFT_SPECIAL.add(5, ItemName.GIAP_LONG_BAO);
        GIFT_SPECIAL.add(5, ItemName.MU_VUONG_LONG);
        GIFT_SPECIAL.add(5, ItemName.GONG_THIEN_LONG);
        GIFT_SPECIAL.add(5, ItemName.LINH_HON_LONG_THE);
        GIFT_SPECIAL.add(5, ItemName.YEN_THAN_LONG);
        GIFT_SPECIAL.add(5, ItemName.DAY_CUONG_THAN_THU);
        GIFT_SPECIAL.add(5, ItemName.GIAP_VAI_LONG_THAN);
        GIFT_SPECIAL.add(10, ItemName.CANH_DOI_HALLOWEEN_2023);
        GIFT_SPECIAL.add(10, ItemName.CHOI_BAY_HALLOWEEN);
        GIFT_SPECIAL.add(10, ItemName.MA_TROI_BI_NGO_HALLOWEEN_2023);
        GIFT_SPECIAL.add(10, ItemName.MA_TROI_PHU_THUY_HALLOWEEN_2023);
        GIFT_SPECIAL.add(10, ItemName.XU_KHOA);
        GIFT_SPECIAL.add(5, ItemName.XU);
        GIFT_SPECIAL.add(5, ItemName.DA_HAI_LAM);
        GIFT_SPECIAL.add(1, ItemName.BUA_TAY_CHI_SO);
        GIFT_SPECIAL.add(1, ItemName.TAM_LINH_THACH);
        GIFT_SPECIAL.add(5, ItemName.TRUNG_HAC_LONG);
        GIFT_SPECIAL.add(5, ItemName.TRUNG_HAN_LONG);
        GIFT_SPECIAL.add(5, ItemName.TRUNG_HOA_LONG);
        GIFT_SPECIAL.add(5, ItemName.TRAN_LONG_THACH);
    }

    @Override
    public void openMenu(Player player, Object[] elements) {
        int action = (int) elements[0];
        switch (action) {
            case -1: {
                List<Command> commands = new ArrayList<>();
                commands.add(new Command(CommandName.SHOW_EVENT, "Thông tin sự kiện", player, 0));
                commands.add(new Command(CommandName.SHOW_EVENT, "TOP\nSự kiện", player, 1, -1));
                commands.add(new Command(CommandName.SHOW_EVENT, "Chế tạo Capsule Halowwen", player, 3, 0, -1));
                commands.add(new Command(CommandName.SHOW_EVENT, "Đổi điểm", player, 4));
                commands.add(new Command(CommandName.SHOW_EVENT, "Tích lũy\nTiêu dùng", player, 5));
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
                    commands.add(new Command(CommandName.SHOW_EVENT, "Top\nSự kiện", player, 1, 0));
                    commands.add(new Command(CommandName.SHOW_EVENT, String.format("Top\ntuần\n[%d/%d]", Utils.getWeekOfYearNow(), Utils.getYearNow()), player, 1, 2, -1));
                    LocalDateTime previousWeek = LocalDateTime.now().minusWeeks(1);
                    commands.add(new Command(CommandName.SHOW_EVENT, String.format("Top\ntuần trước\n[%d/%d]", previousWeek.get(WeekFields.of(Locale.getDefault()).weekOfYear()), previousWeek.getYear()), player, 1, 3, -1));
                    player.createMenu(NpcName.ME, "Bạn đứng top bao nhiêu?", commands);
                } else if (index == 0) {
                    TopManager.getInstance().showTop(player, TopType.TOP_EVENT);
                } else if (index == 1) {
                    TopManager.getInstance().showTop(player, TopType.TOP_EVENT_OTHER);
                } else if (index == 2) {
                    TopManager.getInstance().showTop(player, TopType.TOP_EVENT_WEEKLY);
                } else if (index == 3) {
                    TopManager.getInstance().showTop(player, TopType.TOP_EVENT_PREVIOUS_WEEKLY);
                }
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

            case 5: {
                player.service.showMission(3, player.missionEvents);
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
                player.createMenu(NpcName.ME, String.format("Bạn có chắc chắn muốn chế tạo %d Capsule Haloween", quantity), commands);
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
        int[] items = new int[]{ItemName.CAPSULE_BI_NGO, ItemName.BI_NGO};
        int[] requires = new int[]{1, 1};
        for (int i = 0; i < requires.length; i++) {
            requires[i] *= quantity;
        }
        int[] quantities = new int[items.length];
        for (Item item : player.itemsBag) {
            if (item != null) {
                for (int i = 0; i < items.length; i++) {
                    if (item.template.id == items[i]) {
                        if (item.template.id == items[0] && quantity > item.quantity) {
                            player.addInfo(Player.INFO_RED, "Chỉ có thể chế tạo tối đa " + item.quantity + " vật phẩm " + (item.isLock ? "khóa" : "không khóa"));
                            return;
                        }
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
                        if (item.isLock) {
                            isLock = true;
                        }
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
        Item item = ItemManager.getInstance().createItem(ItemName.CAPSULE_HALOWEEN, quantity, true);
        if (isLock) {
            item.isLock = true;
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

            case ItemName.CAPSULE_HALOWEEN: {
                player.removeQuantityItemBag(item, 1);
                int itemId = GIFT_SPECIAL.next();
                Item itm = ItemManager.getInstance().createItem(itemId, 1, false);
                if (itm.template.id == ItemName.MA_TROI_AC_QUY_HALLOWEEN_2023 || itm.template.id == ItemName.MA_TROI_BI_NGO_HALLOWEEN_2023
                        || itm.template.id == ItemName.MA_TROI_PHU_THUY_HALLOWEEN_2023 || itm.template.id == ItemName.CHOI_BAY_HALLOWEEN
                        || itm.template.id == ItemName.CANH_DOI_HALLOWEEN_2023) {
                    itm.createOptionEvent();
                    if (Utils.nextInt(1000) == 0) {
                        itm.setExpiry(-1);
                        Server.getInstance().service.serverChat(String.format("Chúc mừng %s mở %s nhận được %s vĩnh viễn", player.name, item.template.name, itm.template.name));
                    } else {
                        itm.setExpiry(1);
                    }
                } else if (itm.template.id == ItemName.XU_KHOA) {
                    itm.quantity = Utils.nextInt(20000000, 30000000);
                } else if (itm.template.id == ItemName.XU) {
                    itm.quantity = Utils.nextInt(5000000, 10000000);
                } else if (itm.template.id == ItemName.TRAI_DUA_EXP) {
                    itm.options.add(new ItemOption(129, 5));
                    itm.quantity = Utils.nextInt(5, 10);
                } else if (itm.template.type == ItemType.TYPE_BODY_PET) {
                    itm.isLock = true;
                    itm.createOptionEquipPet();
                } else {
                    itm.setDefaultOption();
                }
                player.addItem(itm, true);
                player.updateTimeEvent = System.currentTimeMillis();
                player.pointEvent++;
                player.upPointWeekly(PointWeeklyType.EVENT, 1);
                player.pointRewardEvent += item.isLock ? 1 : 10;
                return;
            }
            default:
                throw new IllegalStateException("Unexpected value: " + item.template.id);
        }
    }

    @Override
    public List<ItemShop> createShop(Player player) {
        List<ItemShop> itemShops = new ArrayList<>();
        ItemShop itemShop = new ItemShop(ItemName.CAPSULE_BI_NGO, 1, TypePrice.DIAMOND, 5);
        itemShop.removeOption(74);
        itemShops.add(itemShop);

        ItemShop itemShop1 = new ItemShop(ItemName.CAPSULE_BI_NGO, 1, TypePrice.RUBY, 10);
        itemShop1.removeOption(74);
        itemShop1.isLock = true;
        itemShops.add(itemShop1);

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
        return new ArrayList<>();
    }

    @Override
    public void rewardTaskDaily(Player player) {
    }

    @Override
    public void rewardRecharge(Player player, int diamond) {

    }

    @Override
    public List<ItemShop> showShopExchangePoint(Player player) {
        List<ItemShop> itemShopList = new ArrayList<>();
        for (Object[] objects : ITEM_EXCHANGE_POINT) {
            ItemShop itemShop = new ItemShop((int) objects[0], (int) objects[1], (TypePrice) objects[2], (int) objects[3]);
            int expiry = (int) objects[4];
            if (itemShop.template.id == ItemName.AURA_HALOWEEN) {
                itemShop.options.clear();
                itemShop.createOptionEvent();
            }
            if (expiry != 0) {
                itemShop.setExpiry(expiry);
            }
            itemShopList.add(itemShop);
        }
        return itemShopList;
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
    }
}

