package com.beemobi.rongthanonline.event;

import com.beemobi.rongthanonline.bot.boss.Boss;
import com.beemobi.rongthanonline.bot.boss.other.Gorillas;
import com.beemobi.rongthanonline.command.Command;
import com.beemobi.rongthanonline.command.CommandName;
import com.beemobi.rongthanonline.common.Language;
import com.beemobi.rongthanonline.data.RewardData;
import com.beemobi.rongthanonline.entity.Entity;
import com.beemobi.rongthanonline.entity.monster.Monster;
import com.beemobi.rongthanonline.entity.monster.event.ThoNgoc;
import com.beemobi.rongthanonline.entity.player.Player;
import com.beemobi.rongthanonline.item.*;
import com.beemobi.rongthanonline.map.Map;
import com.beemobi.rongthanonline.map.MapManager;
import com.beemobi.rongthanonline.map.MapName;
import com.beemobi.rongthanonline.map.Zone;
import com.beemobi.rongthanonline.model.PointWeeklyType;
import com.beemobi.rongthanonline.model.input.ClientInputType;
import com.beemobi.rongthanonline.model.input.TextField;
import com.beemobi.rongthanonline.network.Message;
import com.beemobi.rongthanonline.npc.NpcName;
import com.beemobi.rongthanonline.repository.GameRepository;
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

public class TrungThu2024 extends Event {
    public static final Object[][] ITEM_EXCHANGE_POINT = new Object[][]{
            {ItemName.CAI_TRANG_QUY_LAO, 1, TypePrice.POINT_EVENT, 30000, -1, 0},
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
    private static final Logger logger = Logger.getLogger(TrungThu2024.class);

    public TrungThu2024(Timestamp startTime, Timestamp endTime) {
        super("Trung thu", startTime, endTime);
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
        GIFT_SPECIAL.add(5, ItemName.YEN_THAN_LONG);
        GIFT_SPECIAL.add(5, ItemName.DAY_CUONG_THAN_THU);
        GIFT_SPECIAL.add(5, ItemName.GIAP_VAI_LONG_THAN);
        GIFT_SPECIAL.add(10, ItemName.CAI_TRANG_HANG_NGA_2024);
        GIFT_SPECIAL.add(10, ItemName.XU_KHOA);
        GIFT_SPECIAL.add(5, ItemName.XU);
        GIFT_SPECIAL.add(5, ItemName.DA_HAI_LAM);
        GIFT_SPECIAL.add(1, ItemName.BUA_TAY_CHI_SO);
        GIFT_SPECIAL.add(1, ItemName.TAM_LINH_THACH);

        GIFT_NORMAL.add(5, ItemName.NGOC_RONG_3_SAO);
        GIFT_NORMAL.add(5, ItemName.BUA_BAO_VE_CAP_1);
        GIFT_NORMAL.add(5, ItemName.BUA_BAO_VE_CAP_2);
        GIFT_NORMAL.add(2, ItemName.BUA_BAO_VE_CAP_3);
        GIFT_NORMAL.add(1, ItemName.BUA_BAO_VE_CAP_4);
        GIFT_NORMAL.add(5, ItemName.DA_10);
        GIFT_NORMAL.add(5, ItemName.MAM_DUA_EXP);
        GIFT_NORMAL.add(5, ItemName.TRAI_DUA_EXP);
        GIFT_NORMAL.add(5, ItemName.VUOT_LONG_THAN);
        GIFT_NORMAL.add(5, ItemName.GIAP_LONG_BAO);
        GIFT_NORMAL.add(5, ItemName.MU_VUONG_LONG);
        GIFT_NORMAL.add(5, ItemName.GONG_THIEN_LONG);
        GIFT_NORMAL.add(5, ItemName.LINH_HON_LONG_THE);
        GIFT_NORMAL.add(5, ItemName.YEN_THAN_LONG);
        GIFT_NORMAL.add(5, ItemName.DAY_CUONG_THAN_THU);
        GIFT_NORMAL.add(5, ItemName.GIAP_VAI_LONG_THAN);
        GIFT_NORMAL.add(10, ItemName.AURA_HANG_NGA);
    }

    @Override
    public void openMenu(Player player, Object[] elements) {

        int action = (int) elements[0];
        switch (action) {
            case -1: {
                List<Command> commands = new ArrayList<>();
                commands.add(new Command(CommandName.SHOW_EVENT, "Thông tin sự kiện", player, 0));
                commands.add(new Command(CommandName.SHOW_EVENT, "TOP\nSự kiện", player, 1, -1));
                commands.add(new Command(CommandName.SHOW_EVENT, "Chế tạo Bánh Trung thu", player, 2, -1));
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
                    commands.add(new Command(CommandName.SHOW_EVENT, "Top\nĐặc biệt", player, 1, 0));
                    commands.add(new Command(CommandName.SHOW_EVENT, "Top\nThường", player, 1, 1));
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
                        commands.add(new Command(CommandName.SHOW_EVENT, "Top\nĐặc biệt", player, 1, 2, 0));
                        commands.add(new Command(CommandName.SHOW_EVENT, "Top\nThường", player, 1, 2, 1));
                        player.createMenu(NpcName.ME, "Bạn đứng top bao nhiêu?", commands);
                    } else if (type == 0) {
                        TopManager.getInstance().showTop(player, TopType.TOP_EVENT_WEEKLY);
                    } else if (type == 1) {
                        TopManager.getInstance().showTop(player, TopType.TOP_EVENT_OTHER_WEEKLY);
                    }
                } else if (index == 3) {
                    int type = (int) elements[2];
                    if (type == -1) {
                        List<Command> commands = new ArrayList<>();
                        commands.add(new Command(CommandName.SHOW_EVENT, "Top\nĐặc biệt", player, 1, 3, 0));
                        commands.add(new Command(CommandName.SHOW_EVENT, "Top\nThường", player, 1, 3, 1));
                        player.createMenu(NpcName.ME, "Bạn đứng top bao nhiêu?", commands);
                    } else if (type == 0) {
                        TopManager.getInstance().showTop(player, TopType.TOP_EVENT_PREVIOUS_WEEKLY);
                    } else if (type == 1) {
                        TopManager.getInstance().showTop(player, TopType.TOP_EVENT_OTHER_PREVIOUS_WEEKLY);
                    }
                }
                return;
            }

            case 2: {
                List<Command> commands = new ArrayList<>();
                commands.add(new Command(CommandName.SHOW_EVENT, "Thường", player, 3, 0, -1));
                commands.add(new Command(CommandName.SHOW_EVENT, "Đặc biệt", player, 3, 1, -1));
                player.createMenu(NpcName.ME, "Chế tạo Bánh Trung thu", commands);
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
                player.createMenu(NpcName.ME, String.format("Bạn có chắc chắn muốn chế tạo x%d Bánh Trung thu %s", quantity, type == 0 ? "thường" : "đặc biệt"), commands);
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
        int[] requires = new int[]{1, 1, 1, 1};
        if (type == 0) {
            items = new int[]{ItemName.NHAN_BANH_THUONG_SU_KIEN_TRUNG_THU_2023, ItemName.BOT_MI_SU_KIEN_TRUNG_THU_2023,
                    ItemName.DUONG_PHEN_SU_KIEN_TRUNG_THU_2023, ItemName.TRUNG_GA_SU_KIEN_TRUNG_THU_2023};
        } else {
            items = new int[]{ItemName.NHAN_BANH_DAC_BIET_SU_KIEN_TRUNG_THU_2023, ItemName.BOT_MI_SU_KIEN_TRUNG_THU_2023,
                    ItemName.DUONG_PHEN_SU_KIEN_TRUNG_THU_2023, ItemName.TRUNG_GA_SU_KIEN_TRUNG_THU_2023};
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
        Item item = ItemManager.getInstance().createItem(type == 0 ? ItemName.BANH_TRUNG_THU_THUONG_SU_KIEN_TRUNG_THU_2023 : ItemName.BANH_TRUNG_THU_DAC_BIET_SU_KIEN_TRUNG_THU_2023, quantity, true);
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

            case ItemName.BANH_TRUNG_THU_THUONG_SU_KIEN_TRUNG_THU_2023:
            case ItemName.BANH_TRUNG_THU_DAC_BIET_SU_KIEN_TRUNG_THU_2023: {
                player.removeQuantityItemBag(item, 1);
                boolean isSpec = item.template.id == ItemName.BANH_TRUNG_THU_DAC_BIET_SU_KIEN_TRUNG_THU_2023;
                int itemId;
                if (isSpec) {
                    itemId = GIFT_SPECIAL.next();
                } else {
                    itemId = GIFT_NORMAL.next();
                }
                Item itm = ItemManager.getInstance().createItem(itemId, 1, false);
                if (itm.template.id == ItemName.CAI_TRANG_HANG_NGA_2024 || itm.template.id == ItemName.AURA_HANG_NGA) {
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
                    itm.isLock = true;
                    itm.createOptionEquipPet();
                } else {
                    itm.setDefaultOption();
                }
                player.addItem(itm, true);
                if (isSpec) {
                    player.updateTimeEvent = System.currentTimeMillis();
                    player.pointEvent++;
                    player.upPointWeekly(PointWeeklyType.EVENT, 1);
                    player.pointRewardEvent += 7;
                } else {
                    player.updateTimeEventOther = System.currentTimeMillis();
                    player.pointOtherEvent++;
                    player.upPointWeekly(PointWeeklyType.EVENT_OTHER, 1);
                }
                return;
            }

            case ItemName.MANH_THO_NGOC_SU_KIEN_TRUNG_THU_2023: {
                if (item.quantity < 10) {
                    player.addInfo(Player.INFO_RED, "Cần ít nhất x10 mảnh để đổi");
                    return;
                }
                player.removeQuantityItemBag(item, 10);
                player.addItem(ItemManager.getInstance().createItem(ItemName.DEN_TROI_SU_KIEN_TRUNG_THU_2023, 1, true), true);
                return;
            }

            case ItemName.DEN_TROI_SU_KIEN_TRUNG_THU_2023: {
                int hour = LocalDateTime.now().getHour();
                if (hour != 20 && hour != 21 && hour != 22 && hour != 23) {
                    player.addInfo(Player.INFO_RED, "Chỉ có thể đốt đèn từ 20h đến 23h59 hàng ngày");
                    return;
                }
                if (player.isFusion()) {
                    player.addInfo(Player.INFO_RED, Language.CANCEL_ACTION_WHEN_FUSION);
                    return;
                }
                player.removeQuantityItemBag(item, 1);
                player.zone.service.addEffectFly(20, player.x + Utils.nextInt(-50, 50), player.y, 0, -3);
                Command yes = new Command(CommandName.CONFIRM_CHANGE_ALL_SKILL_DISCIPLE_BY_EVENT, "Có", player);
                Command no = new Command(CommandName.CANCEL, "Không", player);
                player.startYesNo("Bạn có muốn đổi toàn bộ kỹ năng của đệ tử không?\n Các kỹ năng sau khi đổi có thể trùng với kỹ năng trước đó", no, yes);
                return;
            }

            case ItemName.CAPSULE_TRUNG_THU: {
                player.removeQuantityItemBag(item, 1);
                int[] items = {ItemName.CAI_TRANG_HANG_NGA_2024,
                        ItemName.AURA_HANG_NGA,
                        ItemName.LONG_DEN_CA_CHEP,
                        ItemName.LONG_DEN_HOA_DANG,
                        ItemName.LONG_DEN_ONG_TRANG,
                };
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

            default:
                throw new IllegalStateException("Unexpected value: " + item.template.id);
        }
    }

    @Override
    public List<ItemShop> createShop(Player player) {
        List<ItemShop> itemShops = new ArrayList<>();

        ItemShop itemShop = new ItemShop(ItemName.NHAN_BANH_DAC_BIET_SU_KIEN_TRUNG_THU_2023, 1, TypePrice.DIAMOND, 5);
        itemShop.removeOption(74);
        itemShop.isLock = true;
        itemShops.add(itemShop);

        ItemShop itemShop1 = new ItemShop(ItemName.NHAN_BANH_DAC_BIET_SU_KIEN_TRUNG_THU_2023, 1, TypePrice.RUBY, 10);
        itemShop1.removeOption(74);
        itemShop1.isLock = true;
        itemShops.add(itemShop1);

        itemShops.add(new ItemShop(ItemName.NHAN_BANH_THUONG_SU_KIEN_TRUNG_THU_2023, 1, TypePrice.COIN, 15000000));
        itemShops.add(new ItemShop(ItemName.CAPSULE_TRUNG_THU, 1, TypePrice.DIAMOND, 300));

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
        if (entity.isMonster() && entity.zone.map.isBarrack() && killer.level > 10) {
            itemMaps.add(ItemManager.getInstance().createItemMap(ItemName.BOT_MI_SU_KIEN_TRUNG_THU_2023, 1, killer.id));
        }
        if (entity instanceof Boss) {
            itemMaps.add(ItemManager.getInstance().createItemMap(ItemName.DUONG_PHEN_SU_KIEN_TRUNG_THU_2023, Utils.nextInt(10, 20), killer.id));
        }
        return itemMaps;
    }

    @Override
    public void rewardTaskDaily(Player player) {
        if (!player.isBagFull()) {
            player.addItem(ItemManager.getInstance().createItem(ItemName.TRUNG_GA_SU_KIEN_TRUNG_THU_2023, Utils.nextInt(10, 20), true), true);
        }
    }

    @Override
    public void rewardRecharge(Player player, int diamond) {
        int quantity = diamond / 700;
        if (quantity > 0) {
            GameRepository.getInstance().rewardData.save(new RewardData(player.id, "Quà nạp Kim cương",
                    List.of(ItemManager.getInstance().createItem(ItemName.CAPSULE_TRUNG_THU, quantity, true))));
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
            if (itemShop.template.id == ItemName.CAI_TRANG_HANG_NGA_2024 || itemShop.template.id == ItemName.AURA_HANG_NGA) {
                itemShop.options.clear();
                itemShop.createOptionEvent();
            }
            itemShopList.add(itemShop);
        }
        return itemShopList;
    }

    @Override
    public void start() {
        int[] MAPS = {MapName.BO_SONG_PU, MapName.THAC_NUOC_KEISE,
                MapName.RUNG_NAM_FUKA, MapName.DOI_HOANG_AKA, MapName.THUNG_LUNG_MARIA,
                MapName.DONG_BANG_MIKA, MapName.CAO_NGUYEN_TAKA, MapName.RUNG_GOZA, MapName.LANG_ARU};
        Utils.setTimeout(() -> {
            while (Server.getInstance().isRunning) {
                try {
                    Server.getInstance().service.serverChat("Thỏ Ngọc đã xuất hiện tại các khu vực gần Núi Paozu");
                    List<Zone> zoneList = new ArrayList<>();
                    for (int mapId : MAPS) {
                        Map map = MapManager.getInstance().maps.get(mapId);
                        if (map != null) {
                            zoneList.addAll(map.zones.stream().filter(area -> area.id > 0).toList());
                        }
                    }
                    Collections.shuffle(zoneList);
                    int length = Math.min(30, zoneList.size());
                    for (int i = 0; i < length; i++) {
                        Zone zone = zoneList.get(i);
                        Monster monster = new ThoNgoc();
                        monster.x = zone.map.template.width / 2;
                        monster.y = zone.map.getYSd(monster.x);
                        zone.enter(monster);
                    }
                    Thread.sleep(1800000);
                } catch (Exception ex) {
                    logger.error("update monster error", ex);
                }
            }
        }, 300000);
        long day = 86400;
        Utils.setScheduled(() -> {
            Map map = MapManager.getInstance().maps.get(MapName.VACH_NUI_ARU);
            for (int i = 1; i < map.zones.size(); i++) {
                Boss boss = new Gorillas(false, i);
                boss.joinClient();
            }
        }, day, 22, 0);
        Utils.setScheduled(() -> {
            Map map = MapManager.getInstance().maps.get(MapName.VACH_NUI_ARU);
            for (int i = 1; i < map.zones.size(); i++) {
                Boss boss = new Gorillas(false, i);
                boss.joinClient();
            }
        }, day, 23, 0);
    }
}

