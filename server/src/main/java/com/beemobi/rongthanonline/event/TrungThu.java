package com.beemobi.rongthanonline.event;

import com.beemobi.rongthanonline.bot.boss.Boss;
import com.beemobi.rongthanonline.bot.boss.other.Gorillas;
import com.beemobi.rongthanonline.command.Command;
import com.beemobi.rongthanonline.command.CommandName;
import com.beemobi.rongthanonline.common.Language;
import com.beemobi.rongthanonline.entity.Entity;
import com.beemobi.rongthanonline.entity.monster.Monster;
import com.beemobi.rongthanonline.entity.monster.event.ThoNgoc;
import com.beemobi.rongthanonline.entity.player.Player;
import com.beemobi.rongthanonline.item.*;
import com.beemobi.rongthanonline.map.Zone;
import com.beemobi.rongthanonline.map.Map;
import com.beemobi.rongthanonline.map.MapManager;
import com.beemobi.rongthanonline.map.MapName;
import com.beemobi.rongthanonline.model.Level;
import com.beemobi.rongthanonline.npc.NpcName;
import com.beemobi.rongthanonline.server.Server;
import com.beemobi.rongthanonline.util.Utils;
import org.apache.log4j.Logger;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class TrungThu {
    private static final Logger logger = Logger.getLogger(TrungThu.class);

    /*public static final Object[][] ITEM_EXCHANGE_POINT = new Object[][]{
            {ItemName.LONG_DEN_KEO_QUAN, 1, ItemShopTypePrice.POINT_EVENT, 100, 7},
            {ItemName.LONG_DEN_KY_LAN, 1, ItemShopTypePrice.POINT_EVENT, 100, 7},
            {ItemName.LONG_DEN_ONG_SAO, 1, ItemShopTypePrice.POINT_EVENT, 100, 7},
            {ItemName.LONG_DEN_CA_CHEP, 1, ItemShopTypePrice.POINT_EVENT, 200, 7},
            {ItemName.LONG_DEN_HOA_DANG, 1, ItemShopTypePrice.POINT_EVENT, 200, 7},
            {ItemName.LONG_DEN_ONG_TRANG, 1, ItemShopTypePrice.POINT_EVENT, 200, 7},
            {ItemName.SAO_PHA_LE_DANH_BONG, 1, ItemShopTypePrice.POINT_EVENT, 200, 7},
            {ItemName.MAY_LINH_LUNG, 1, ItemShopTypePrice.POINT_EVENT, 10000, -1},
            {ItemName.CAI_TRANG_HANG_NGA, 1, ItemShopTypePrice.POINT_EVENT, 15000, -1},
            {ItemName.HUY_HIEU_TRUNG_THU, 1, ItemShopTypePrice.POINT_EVENT, 300, 7},
            {ItemName.DEN_TROI_SU_KIEN_TRUNG_THU_2023, 1, ItemShopTypePrice.POINT_EVENT, 50, 7},
            {ItemName.NANG_CAP_CHIEU_1_DE_TU, 1, ItemShopTypePrice.POINT_EVENT, 100, 7},
            {ItemName.NANG_CAP_CHIEU_2_DE_TU, 1, ItemShopTypePrice.POINT_EVENT, 150, 7},
            {ItemName.NANG_CAP_CHIEU_3_DE_TU, 1, ItemShopTypePrice.POINT_EVENT, 200, 7},
            {ItemName.NANG_CAP_CHIEU_4_DE_TU, 1, ItemShopTypePrice.POINT_EVENT, 250, 7},
            {ItemName.NANG_CAP_CHIEU_5_DE_TU, 1, ItemShopTypePrice.POINT_EVENT, 300, 7},
            {ItemName.DUA_NAU, 1, ItemShopTypePrice.POINT_EVENT, 50, 7},
            {ItemName.DUA_VANG, 1, ItemShopTypePrice.POINT_EVENT, 50, 7},
            {ItemName.DUA_XANH, 1, ItemShopTypePrice.POINT_EVENT, 50, 7},
            {ItemName.LOI_KI_NANG, 1, ItemShopTypePrice.POINT_EVENT, 3, 7},
            {ItemName.BI_KIP_KY_NANG, 1, ItemShopTypePrice.POINT_EVENT, 1, 7},
    };

    public TrungThu(String name, Timestamp startTime, Timestamp endTime) {
        super(name, startTime, endTime);
        GIFT_NORMAL.add(15, ItemName.DA_7);
        GIFT_NORMAL.add(15, ItemName.DA_8);
        GIFT_NORMAL.add(5, ItemName.DA_9);
        GIFT_NORMAL.add(2, ItemName.DA_10);
        GIFT_NORMAL.add(3, ItemName.NGOC_RONG_5_SAO);
        GIFT_NORMAL.add(8, ItemName.NGOC_RONG_6_SAO);
        GIFT_NORMAL.add(15, ItemName.NGOC_RONG_7_SAO);
        GIFT_NORMAL.add(10, ItemName.DUA_XANH);
        GIFT_NORMAL.add(10, ItemName.DUA_VANG);
        GIFT_NORMAL.add(10, ItemName.DUA_NAU);
        GIFT_SPECIAL.add(1, ItemName.HUY_HIEU_TRUNG_THU);
        GIFT_SPECIAL.add(1, ItemName.MANH_GIAY);
        GIFT_NORMAL.setDefault(ItemName.POWER);

        GIFT_SPECIAL.add(5, ItemName.DA_11);
        GIFT_SPECIAL.add(5, ItemName.DA_10);
        GIFT_SPECIAL.add(5, ItemName.DA_9);
        GIFT_SPECIAL.add(5, ItemName.BUA_BAO_VE_CAP_2);
        GIFT_SPECIAL.add(5, ItemName.BUA_BAO_VE_CAP_3);
        GIFT_SPECIAL.add(10, ItemName.DUA_XANH);
        GIFT_SPECIAL.add(10, ItemName.DUA_VANG);
        GIFT_SPECIAL.add(10, ItemName.DUA_NAU);
        GIFT_SPECIAL.add(5, ItemName.LONG_DEN_KEO_QUAN);
        GIFT_SPECIAL.add(5, ItemName.LONG_DEN_KY_LAN);
        GIFT_SPECIAL.add(5, ItemName.LONG_DEN_ONG_SAO);
        GIFT_SPECIAL.add(5, ItemName.LONG_DEN_CA_CHEP);
        GIFT_SPECIAL.add(5, ItemName.LONG_DEN_HOA_DANG);
        GIFT_SPECIAL.add(5, ItemName.LONG_DEN_ONG_TRANG);
        GIFT_SPECIAL.add(2, ItemName.HUY_HIEU_TRUNG_THU);
        GIFT_SPECIAL.add(5, ItemName.MANH_GIAY);
        GIFT_SPECIAL.add(5, ItemName.CAI_TRANG_HANG_NGA);
        GIFT_SPECIAL.setDefault(ItemName.POWER);

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
        commands.add(new Command(CommandName.MENU_EVENT_EXCHANGE_QUANTITY, "Thường", player, 0));
        commands.add(new Command(CommandName.MENU_EVENT_EXCHANGE_QUANTITY, "Đặc biệt", player, 1));
        commands.add(new Command(CommandName.DOI_DEN_TROI_SU_KIEN_TRUNG_THU_2023, "Đèn trời", player));
        player.createMenu(NpcName.ME, "Chế tạo vật phẩm sự kiện", commands);
    }

    @Override
    public void exchangeQuantity(Player player, int type) {
        List<Command> commands = new ArrayList<>();
        commands.add(new Command(CommandName.CONFIRM_EVENT_EXCHANGE_QUANTITY, "Số lượng\n1", player, type, 1));
        commands.add(new Command(CommandName.CONFIRM_EVENT_EXCHANGE_QUANTITY, "Số lượng\n10", player, type, 10));
        commands.add(new Command(CommandName.CONFIRM_EVENT_EXCHANGE_QUANTITY, "Số lượng\n100", player, type, 100));
        commands.add(new Command(CommandName.CONFIRM_EVENT_EXCHANGE_QUANTITY, "Số lượng\n1000", player, type, 1000));
        player.createMenu(NpcName.ME, String.format("Chế tạo Bánh trung thu %s", type == 0 ? "thường" : "Đặc biệt"), commands);
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
                items = new int[]{ItemName.TRUNG_GA_SU_KIEN_TRUNG_THU_2023,
                        ItemName.BOT_MI_SU_KIEN_TRUNG_THU_2023,
                        ItemName.DUONG_PHEN_SU_KIEN_TRUNG_THU_2023,
                        ItemName.NHAN_BANH_THUONG_SU_KIEN_TRUNG_THU_2023};
                requires = new int[]{1, 2, 2, 1};
            } else {
                items = new int[]{ItemName.TRUNG_GA_SU_KIEN_TRUNG_THU_2023,
                        ItemName.BOT_MI_SU_KIEN_TRUNG_THU_2023,
                        ItemName.DUONG_PHEN_SU_KIEN_TRUNG_THU_2023,
                        ItemName.NHAN_BANH_DAC_BIET_SU_KIEN_TRUNG_THU_2023};
                requires = new int[]{2, 3, 2, 1};
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
            Item item = ItemManager.getInstance()
                    .createItem(type == 0 ? ItemName.BANH_TRUNG_THU_THUONG_SU_KIEN_TRUNG_THU_2023 : ItemName.BANH_TRUNG_THU_DAC_BIET_SU_KIEN_TRUNG_THU_2023
                            , quantity, true);
            player.addItem(item);
            player.addInfo(Player.INFO_YELLOW, String.format("Bạn nhận được x%d %s", item.quantity, item.template.name));
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
    public void useItem(Player player, Item item) {
        switch (item.template.id) {
            case ItemName.BANH_TRUNG_THU_THUONG_SU_KIEN_TRUNG_THU_2023:
            case ItemName.BANH_TRUNG_THU_DAC_BIET_SU_KIEN_TRUNG_THU_2023: {
                if (player.isBagFull()) {
                    player.addInfo(Player.INFO_RED, Language.ME_BAG_FULL);
                    return;
                }
                boolean isSpecial = item.template.id == ItemName.BANH_TRUNG_THU_DAC_BIET_SU_KIEN_TRUNG_THU_2023;
                player.removeQuantityItemBag(item, 1);
                int itemId = Event.event.next(isSpecial ? Event.TYPE_SPECIAL : Event.TYPE_NORMAL);
                if (itemId == ItemName.POWER) {
                    long power = Utils.nextInt(1000000, 2000000);
                    ArrayList<Level> levels = Server.getInstance().levels;
                    long dis = levels.get(player.level + 1).power - levels.get(player.level).power;
                    if (power > dis / 10) {
                        power = dis / 10;
                    }
                    player.upPower(power);
                    player.addInfo(Player.INFO_YELLOW, String.format("Bạn nhận được %s sức mạnh", Utils.getMoneys(power)));
                } else {
                    Item itm = ItemManager.getInstance().createItem(itemId, 1, true);
                    if (itm.isItemBody()) {
                        itm.randomParam(-15, 15);
                        itm.setExpiry(Utils.nextInt(2, 7));
                    }
                    player.addItem(itm);
                    player.addInfo(Player.INFO_YELLOW, String.format("Bạn nhận được %s", itm.template.name));
                }
                long power = Utils.nextInt(100000, 200000);
                ArrayList<Level> levels = Server.getInstance().levels;
                long dis = levels.get(player.level + 1).power - levels.get(player.level).power;
                if (power > dis / 10) {
                    power = dis / 10;
                }
                player.upPower(power);
                player.addInfo(Player.INFO_YELLOW, String.format("Bạn nhận được %s sức mạnh", Utils.getMoneys(power)));
                player.pointEvent += isSpecial ? 20 : 1;
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
        }
    }

    @Override
    public List<ItemShop> createShop(Player player) {
        List<ItemShop> itemShops = new ArrayList<>();
        itemShops.add(new ItemShop(ItemName.NHAN_BANH_THUONG_SU_KIEN_TRUNG_THU_2023, 1, ItemShopTypePrice.COIN, 1000000));
        itemShops.add(new ItemShop(ItemName.NHAN_BANH_THUONG_SU_KIEN_TRUNG_THU_2023, 100, ItemShopTypePrice.COIN, 100000000));
        itemShops.add(new ItemShop(ItemName.NHAN_BANH_DAC_BIET_SU_KIEN_TRUNG_THU_2023, 10, ItemShopTypePrice.DIAMOND, 50));
        ItemShop itemShop = new ItemShop(ItemName.NHAN_BANH_DAC_BIET_SU_KIEN_TRUNG_THU_2023, 1000, ItemShopTypePrice.DIAMOND, 4900);
        ItemOption itemOption = itemShop.getOption(74);
        if (itemOption != null) {
            itemOption.param *= 100;
        }
        itemShops.add(itemShop);
        return itemShops;
    }

    @Override
    public List<ItemMap> throwItem(Player killer, Entity entity) {
        List<ItemMap> itemMaps = new ArrayList<>();
        if (entity.isMonster()) {
            if (entity.level > 10 && Math.abs(killer.level - entity.level) < 10 && Utils.isPercent(5)) {
                if (killer.gender == 0) {
                    itemMaps.add(ItemManager.getInstance().createItemMap(ItemName.DUONG_PHEN_SU_KIEN_TRUNG_THU_2023, 1, killer.id));
                } else if (killer.gender == 1) {
                    itemMaps.add(ItemManager.getInstance().createItemMap(ItemName.TRUNG_GA_SU_KIEN_TRUNG_THU_2023, 1, killer.id));
                } else if (killer.gender == 2) {
                    itemMaps.add(ItemManager.getInstance().createItemMap(ItemName.BOT_MI_SU_KIEN_TRUNG_THU_2023, 1, killer.id));
                }
            }
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
                            zoneList.addAll(map.zones.stream().filter(area -> area.id > 0).collect(Collectors.toList()));
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
    }*/
}
