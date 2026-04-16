package com.beemobi.rongthanonline.event;

import com.beemobi.rongthanonline.bot.boss.other.PicSummer;
import com.beemobi.rongthanonline.bot.boss.other.PocSummer;
import com.beemobi.rongthanonline.bot.escort.BunmaBikini;
import com.beemobi.rongthanonline.command.Command;
import com.beemobi.rongthanonline.command.CommandName;
import com.beemobi.rongthanonline.common.Language;
import com.beemobi.rongthanonline.entity.Entity;
import com.beemobi.rongthanonline.entity.player.Player;
import com.beemobi.rongthanonline.item.*;
import com.beemobi.rongthanonline.map.Map;
import com.beemobi.rongthanonline.map.MapManager;
import com.beemobi.rongthanonline.map.MapName;
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
import java.util.ArrayList;
import java.util.List;

public class TuuTruong2024 extends Event {
    public static final Object[][] ITEM_EXCHANGE_POINT = new Object[][]{
            {ItemName.AURA_GOKU, 1, TypePrice.POINT_EVENT, 25000, -1, 0},
            {ItemName.AURA_PICOLO, 1, TypePrice.POINT_EVENT, 25000, -1, 0},
            {ItemName.AURA_VEGETA, 1, TypePrice.POINT_EVENT, 25000, -1, 0},
            {ItemName.SAO_PHA_LE_DANH_BONG, 1, TypePrice.POINT_EVENT, 40, 7, 0},
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
    private static final Logger logger = Logger.getLogger(TuuTruong2024.class);

    public int[] countSpecial;

    public TuuTruong2024(String name, Timestamp startTime, Timestamp endTime) {
        super(name, startTime, endTime);
        GIFT_SPECIAL.add(5, ItemName.NGOC_RONG_3_SAO);
        GIFT_SPECIAL.add(5, ItemName.BUA_BAO_VE_CAP_1);
        GIFT_SPECIAL.add(5, ItemName.BUA_BAO_VE_CAP_2);
        GIFT_SPECIAL.add(2, ItemName.BUA_BAO_VE_CAP_3);
        GIFT_SPECIAL.add(1, ItemName.BUA_BAO_VE_CAP_4);
        GIFT_SPECIAL.add(5, ItemName.DA_10);
        GIFT_SPECIAL.add(5, ItemName.MAM_DUA_EXP);
        GIFT_SPECIAL.add(5, ItemName.TRAI_DUA_EXP);
        GIFT_SPECIAL.add(5, ItemName.VUOT_LONG_THAN);
        GIFT_SPECIAL.add(5, ItemName.GIAP_LONG_BAO);
        GIFT_SPECIAL.add(5, ItemName.MU_VUONG_LONG);
        GIFT_SPECIAL.add(5, ItemName.GONG_THIEN_LONG);
        GIFT_SPECIAL.add(5, ItemName.LINH_HON_LONG_THE);
        GIFT_SPECIAL.add(10, ItemName.AURA_RONG_NAMEK);
        GIFT_SPECIAL.add(10, ItemName.XU_KHOA);
        GIFT_SPECIAL.add(5, ItemName.XU);
        GIFT_SPECIAL.add(5, ItemName.DA_HAI_LAM);
        GIFT_SPECIAL.add(1, ItemName.BUA_TAY_CHI_SO);
        GIFT_SPECIAL.add(1, ItemName.TAM_LINH_THACH);

        countSpecial = new int[]{0, Utils.nextInt(2000, 2500)};
    }

    @Override
    public void openMenu(Player player, Object[] elements) {

        int action = (int) elements[0];
        switch (action) {
            case -1: {
                List<Command> commands = new ArrayList<>();
                commands.add(new Command(CommandName.SHOW_EVENT, "Thông tin sự kiện", player, 0));
                commands.add(new Command(CommandName.SHOW_EVENT, "TOP\nSự kiện", player, 1, -1));
                commands.add(new Command(CommandName.SHOW_EVENT, "Chế tạo Giỏ vật tư", player, 2, -1));
                commands.add(new Command(CommandName.SHOW_EVENT, "Đổi điểm", player, 3));
                commands.add(new Command(CommandName.SHOW_EVENT, "Tích lũy\nTiêu dùng", player, 5));
                if (player.zone.map.template.id != MapName.NUI_PAOZU) {
                    commands.add(new Command(CommandName.SHOW_EVENT, "Về núi Paozu", player, 4, 0));
                } else {
                    commands.add(new Command(CommandName.SHOW_EVENT, "Đến\nĐảo hoa\n1KC", player, 4, 1));
                }
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
                    commands.add(new Command(CommandName.SHOW_EVENT, "Điểm\nSự kiện", player, 1, 0));
                    commands.add(new Command(CommandName.SHOW_EVENT, "TOP\nTuần", player, 1, 1));
                    player.createMenu(NpcName.ME, String.format("Sự kiện %s", this.name), commands);
                } else if (index == 0) {
                    TopManager.getInstance().showTop(player, TopType.TOP_EVENT);
                } else if (index == 1) {
                    TopManager.getInstance().showTop(player, TopType.TOP_EVENT_WEEKLY);
                }
                return;
            }

            case 2: {
                int quantity = (int) elements[1];
                if (quantity == -1) {
                    player.startClientInput(ClientInputType.INPUT_EVENT, "Nhập số lượng", new TextField("Số lượng", TextField.TYPE_NORMAL));
                    player.clientInputType.elements = new Object[]{action};
                } else if (quantity > 0) {
                    exchange(player, 0, quantity);
                }
                return;
            }

            case 3: {
                Shop shop = ShopManager.getInstance().shops.get(ShopType.SHOP_EVENT);
                if (shop != null) {
                    shop.show(player);
                }
                return;
            }

            case 4: {
                if (player.level < 10) {
                    player.addInfo(Player.INFO_RED, "Yêu cầu từ cấp 10 trở lên");
                    return;
                }
                int type = (int) elements[1];
                Map map = MapManager.getInstance().maps.get(type == 1 ? MapName.DAO_HOA_1 : MapName.NUI_PAOZU);
                if (map != null) {
                    if (type == 1) {
                        if (!player.isEnoughMoney(TypePrice.RUBY, 1)) {
                            return;
                        }
                        player.downMoney(TypePrice.RUBY, 1);
                    }
                    player.teleport(map, false);
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
            if (action == 2) {
                int quantity = Integer.parseInt(message.reader().readUTF());
                if (quantity <= 0) {
                    player.addInfo(Player.INFO_RED, "Số lượng không hợp lệ");
                    return;
                }
                List<Command> commands = new ArrayList<>();
                commands.add(new Command(CommandName.SHOW_EVENT, "OK", player, 2, quantity));
                commands.add(new Command(CommandName.CANCEL, "Hủy", player));
                player.createMenu(NpcName.ME, String.format("Bạn có chắc chắn muốn chế tạo %d Giỏ vật tư", quantity), commands);
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
        int[] items = new int[]{ItemName.CAP_SACH, ItemName.THUOC_KE, ItemName.PHAN_TRANG, ItemName.SO_DAU_BAI, ItemName.BUT_CHI};
        int[] requires = new int[]{1, 1, 2, 1, 2};
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
        player.addItem(ItemManager.getInstance().createItem(ItemName.GIO_VAT_TU, quantity, true), true);
    }

    @Override
    public void useItem(Player player, Item item) {
        if (player.isBagFull()) {
            player.addInfo(Player.INFO_RED, Language.ME_BAG_FULL);
            return;
        }
        switch (item.template.id) {

            case ItemName.GIO_VAT_TU: {
                player.removeQuantityItemBag(item, 1);
                int itemId = GIFT_SPECIAL.next();
                Item itm = ItemManager.getInstance().createItem(itemId, 1, false);
                if (itm.template.id == ItemName.AURA_RONG_NAMEK) {
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
                } else if (itm.template.type == ItemType.TYPE_BODY_PET) {
                    itm.setExpiry(7);
                } else {
                    itm.setDefaultOption();
                }
                player.addItem(itm, true);
                player.updateTimeEvent = System.currentTimeMillis();
                player.pointEvent++;
                player.upPointWeekly(PointWeeklyType.EVENT, 1);
                player.pointRewardEvent += 10;
                return;
            }
            default:
                throw new IllegalStateException("Unexpected value: " + item.template.id);
        }
    }

    @Override
    public List<ItemShop> createShop(Player player) {
        List<ItemShop> itemShops = new ArrayList<>();
        itemShops.add(new ItemShop(ItemName.CAP_SACH, 1, TypePrice.DIAMOND, 5));
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
        if (entity.isMonster() && killer.level > 10) {
            if (entity.zone.map.template.id == MapName.DAO_HOA_1 || entity.zone.map.template.id == MapName.DAO_HOA_2) {
                if (Utils.nextInt(2000) == 0 && killer.level >= 50) {
                    itemMaps.add(ItemManager.getInstance().createItemMap(ItemName.QUA_NAM_HAC_HOA, 1, killer.id));
                }
                if (Utils.isPercent(5)) {
                    itemMaps.add(ItemManager.getInstance().createItemMap(ItemName.THUOC_KE, 1, killer.id));
                }
            } else if (entity.zone.map.isBarrack()) {
                itemMaps.add(ItemManager.getInstance().createItemMap(ItemName.PHAN_TRANG, 1, killer.id));
            }
        }
        return itemMaps;
    }

    @Override
    public void rewardTaskDaily(Player player) {
        if (!player.isBagFull()) {
            player.addItem(ItemManager.getInstance().createItem(ItemName.BUT_CHI, Utils.nextInt(10, 20), true), true);
        }
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
            if (expiry != 0) {
                itemShop.setExpiry(expiry);
            }
            itemShopList.add(itemShop);
        }
        return itemShopList;
    }

    @Override
    public void start() {
        for (int i = 0; i < 50; i++) {
            new BunmaBikini().enter(BunmaBikini.MAPS);
        }
        new PocSummer().joinClient();
        new PicSummer().joinClient();
        return;
    }
}

