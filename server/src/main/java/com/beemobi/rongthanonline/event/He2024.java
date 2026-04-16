package com.beemobi.rongthanonline.event;

import com.beemobi.rongthanonline.bot.boss.other.PicSummer;
import com.beemobi.rongthanonline.bot.boss.other.PocSummer;
import com.beemobi.rongthanonline.bot.escort.BunmaBikini;
import com.beemobi.rongthanonline.command.Command;
import com.beemobi.rongthanonline.command.CommandName;
import com.beemobi.rongthanonline.common.Language;
import com.beemobi.rongthanonline.common.RandomCollection;
import com.beemobi.rongthanonline.entity.Entity;
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
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

public class He2024 extends Event {
    public static final Object[][] ITEM_EXCHANGE_POINT = new Object[][]{
            {ItemName.CAI_TRANG_BUNMA_BIKINI, 1, TypePrice.POINT_EVENT, 50000, 0, 0},
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
            {ItemName.SACH_KY_NANG, 1, TypePrice.POINT_EVENT, 500, 7, 0},
    };
    private static final Logger logger = Logger.getLogger(He2024.class);

    public CopyOnWriteArrayList<Integer> rewards = new CopyOnWriteArrayList<>();
    public CopyOnWriteArrayList<Integer> used = new CopyOnWriteArrayList<>();
    public ConcurrentHashMap<Integer, Long> diamondUses = new ConcurrentHashMap<>();
    public int[] countSpecial;
    public int[] countNormal;
    public RandomCollection<Integer> VO_SO = new RandomCollection<>();
    public RandomCollection<Integer> SAO_BIEN = new RandomCollection<>();
    public RandomCollection<Integer> CUA_BIEN = new RandomCollection<>();

    public He2024(String name, Timestamp startTime, Timestamp endTime) {
        super(name, startTime, endTime);
        GIFT_NORMAL.add(80, ItemName.VO_SO);
        GIFT_NORMAL.add(19, ItemName.SAO_BIEN);
        GIFT_NORMAL.add(1, ItemName.CUA_BIEN);

        GIFT_SPECIAL.add(10, ItemName.VO_SO);
        GIFT_SPECIAL.add(50, ItemName.SAO_BIEN);
        GIFT_SPECIAL.add(40, ItemName.CUA_BIEN);

        VO_SO.add(25, ItemName.DA_7);
        VO_SO.add(25, ItemName.NGOC_RONG_6_SAO);
        VO_SO.add(25, ItemName.NGOC_RONG_7_SAO);
        VO_SO.add(25, ItemName.BUA_BAO_VE_CAP_1);
        VO_SO.add(1, ItemName.MANH_GIAY);

        SAO_BIEN.add(5, ItemName.NGOC_RONG_5_SAO);
        SAO_BIEN.add(5, ItemName.DA_8);
        SAO_BIEN.add(5, ItemName.DA_9);
        SAO_BIEN.add(5, ItemName.MAM_DUA_EXP);
        SAO_BIEN.add(5, ItemName.TRAI_DUA_EXP);
        SAO_BIEN.add(5, ItemName.HUY_HIEU_HE_2024);
        SAO_BIEN.add(5, ItemName.CAI_TRANG_POC_HE_2024);
        SAO_BIEN.add(5, ItemName.CAI_TRANG_PIC_HE_2024);
        SAO_BIEN.add(5, ItemName.VAN_LUOT_SONG_THAN);
        SAO_BIEN.add(5, ItemName.CANH_THUY_TE);
        SAO_BIEN.add(5, ItemName.O_BACH_TUOC);
        SAO_BIEN.add(5, ItemName.BABY_SHARK);
        SAO_BIEN.add(5, ItemName.VUOT_LONG_THAN);
        SAO_BIEN.add(5, ItemName.GIAP_LONG_BAO);
        SAO_BIEN.add(5, ItemName.MU_VUONG_LONG);
        SAO_BIEN.add(5, ItemName.GONG_THIEN_LONG);
        SAO_BIEN.add(5, ItemName.LINH_HON_LONG_THE);
        SAO_BIEN.add(5, ItemName.BUA_BAO_VE_CAP_2);

        CUA_BIEN.add(10, ItemName.BUA_BAO_VE_CAP_3);
        CUA_BIEN.add(10, ItemName.MAM_DUA_EXP);
        CUA_BIEN.add(10, ItemName.TRAI_DUA_EXP);
        CUA_BIEN.add(10, ItemName.DA_10);
        CUA_BIEN.add(10, ItemName.HUY_HIEU_HE_2024);
        CUA_BIEN.add(10, ItemName.CAI_TRANG_POC_HE_2024);
        CUA_BIEN.add(10, ItemName.CAI_TRANG_PIC_HE_2024);
        CUA_BIEN.add(10, ItemName.VAN_LUOT_SONG_THAN);
        CUA_BIEN.add(10, ItemName.CANH_THUY_TE);
        CUA_BIEN.add(10, ItemName.O_BACH_TUOC);
        CUA_BIEN.add(10, ItemName.BABY_SHARK);
        CUA_BIEN.add(5, ItemName.DA_HAI_LAM);
        CUA_BIEN.add(1, ItemName.BUA_TAY_CHI_SO);
        CUA_BIEN.add(1, ItemName.TAM_LINH_THACH);

        countSpecial = new int[]{0, Utils.nextInt(2000, 2500)};
        countNormal = new int[]{0, Utils.nextInt(5000, 5500)};
    }

    @Override
    public void openMenu(Player player, Object[] elements) {

        int action = (int) elements[0];
        switch (action) {
            case -1: {
                List<Command> commands = new ArrayList<>();
                if (!rewards.contains(player.id)) {
                    commands.add(new Command(CommandName.SHOW_EVENT, "Nhận thưởng đầu ngày", player, 6));
                }
                long diamond = diamondUses.getOrDefault(player.id, 0L);
                StringBuilder content = new StringBuilder();
                content.append(String.format("Sự kiện %s", this.name)).append("\n");
                content.append("Mỗi ngày, khi mua vật phẩm trong cửa hàng tại các mốc 1000, 3000 và 5000 kim cương sẽ nhận thêm phần thưởng.").append("\n");
                content.append(String.format("Hôm nay bạn đã sử dụng %s kim cương.", diamond)).append("\n");
                if (diamond >= 1000) {
                    long count = used.stream().filter(i -> i == player.id).count();
                    if ((diamond >= 5000 && count < 3) || (diamond >= 3000 && count < 2) || count == 0) {
                        commands.add(new Command(CommandName.SHOW_EVENT, "Nhận thưởng mốc sử dụng Kim cương", player, 7));
                    }
                    if (count == 1) {
                        content.append("Bạn đã nhận mốc 1000 Kim cương (50tr xu)");
                    } else if (count == 2) {
                        content.append("Bạn đã nhận mốc 3000 Kim cương (100tr xu)");
                    } else if (count == 3) {
                        content.append("Bạn đã nhận mốc 5000 Kim cương (Capsule Huyền bí)");
                    }
                }
                commands.add(new Command(CommandName.SHOW_EVENT, "Thông tin sự kiện", player, 0));
                commands.add(new Command(CommandName.SHOW_EVENT, "TOP\nSự kiện", player, 1, -1));
                commands.add(new Command(CommandName.SHOW_EVENT, "Chế tạo bộ cần câu", player, 2));
                commands.add(new Command(CommandName.SHOW_EVENT, "Đổi điểm", player, 4));
                if (player.zone.map.template.id != MapName.NUI_PAOZU) {
                    commands.add(new Command(CommandName.SHOW_EVENT, "Về núi Paozu", player, 5, 0));
                } else {
                    commands.add(new Command(CommandName.SHOW_EVENT, "Đến\nĐảo hoa\n1KC", player, 5, 1));
                }
                player.createMenu(NpcName.ME, content.toString(), commands);
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
                        commands.add(new Command(CommandName.SHOW_EVENT, "Đặc biệt", player, 1, 2, 0));
                        commands.add(new Command(CommandName.SHOW_EVENT, "Thường", player, 1, 2, 1));
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
                        commands.add(new Command(CommandName.SHOW_EVENT, "Đặc biệt", player, 1, 3, 0));
                        commands.add(new Command(CommandName.SHOW_EVENT, "Thường", player, 1, 3, 1));
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
                commands.add(new Command(CommandName.SHOW_EVENT, "Thường", player, 3, 0, -1));
                commands.add(new Command(CommandName.SHOW_EVENT, "Đặc biệt", player, 3, 1, -1));
                player.createMenu(NpcName.ME, "Chế tạo bộ cần câu", commands);
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

            case 6: {
                if (!rewards.contains(player.id)) {
                    if (player.isBagFull()) {
                        player.addInfo(Player.INFO_RED, Language.ME_BAG_FULL);
                        return;
                    }
                    rewards.add(player.id);
                    player.addItem(ItemManager.getInstance().createItem(ItemName.CAPSULE_HE_2024, 1, false), true);
                } else {
                    player.addInfo(Player.INFO_RED, "Mỗi ngày chỉ có thể nhận 1 lần");
                }
                return;
            }

            case 7: {
                if (player.isBagFull()) {
                    player.addInfo(Player.INFO_RED, Language.ME_BAG_FULL);
                    return;
                }
                long diamond = diamondUses.getOrDefault(player.id, 0L);
                if (diamond >= 1000) {
                    long count = used.stream().filter(i -> i == player.id).count();
                    if (count == 0) {
                        used.add(player.id);
                        player.upXu(50000000L);
                    } else if (count == 1) {
                        used.add(player.id);
                        player.upXu(100000000L);
                    } else if (count == 2) {
                        used.add(player.id);
                        player.addItem(ItemManager.getInstance().createItem(ItemName.CAPSULE_HUYEN_BI, 1, false), true);
                    }
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
                player.createMenu(NpcName.ME, String.format("Bạn có chắc chắn muốn chế tạo x%d bộ cần câu %s", quantity, type == 0 ? "đỏ" : "tím"), commands);
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
            items = new int[]{ItemName.CAN_CAU_THUONG, ItemName.DAY_CUOC, ItemName.MOI_CAU_CA, ItemName.PHAO_CAU_CA};
        } else {
            items = new int[]{ItemName.CAN_CAU_DAC_BIET, ItemName.DAY_CUOC, ItemName.MOI_CAU_CA, ItemName.PHAO_CAU_CA};
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
        Item item = ItemManager.getInstance().createItem(type == 0 ? ItemName.BO_CAN_CAU_THUONG : ItemName.BO_CAN_CAU_DAC_BIET, quantity, true);
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
            case ItemName.CAPSULE_HE_2024: {
                player.removeQuantityItemBag(item, 1);
                int[] items = {ItemName.CAI_TRANG_PIC_HE_2024, ItemName.CAI_TRANG_POC_HE_2024, ItemName.HUY_HIEU_HE_2024,
                        ItemName.O_BACH_TUOC, ItemName.BABY_SHARK, ItemName.CANH_THUY_TE, ItemName.VAN_LUOT_SONG_THAN};
                Item newItem = ItemManager.getInstance().createItem(Utils.nextArray(items), 1, false);
                newItem.createOptionEvent();
                int percent = item.getParam(161);
                if (percent > 0) {
                    if (Utils.isPercent(percent)) {
                        Server.getInstance().service.serverChat(String.format("Chúc mừng %s mở %s nhận được %s vĩnh viễn", player.name, item.template.name, newItem.template.name));
                    } else {
                        newItem.setExpiry(Utils.nextInt(7, 15));
                    }
                } else {
                    newItem.setExpiry(Utils.nextInt(1, 3));
                }
                player.addItem(newItem, true);
                return;
            }

            case ItemName.VO_SO: {
                player.removeQuantityItemBag(item, 1);
                player.addItem(ItemManager.getInstance().createItem(VO_SO.next(), 1, true), true);
                return;
            }

            case ItemName.SAO_BIEN: {
                player.removeQuantityItemBag(item, 1);
                Item newItem = ItemManager.getInstance().createItem(SAO_BIEN.next(), 1, false);
                if (newItem.template.id == ItemName.HUY_HIEU_HE_2024 || newItem.template.id == ItemName.CAI_TRANG_POC_HE_2024
                        || newItem.template.id == ItemName.CAI_TRANG_PIC_HE_2024 || newItem.template.id == ItemName.VAN_LUOT_SONG_THAN
                        || newItem.template.id == ItemName.BABY_SHARK || newItem.template.id == ItemName.O_BACH_TUOC || newItem.template.id == ItemName.CANH_THUY_TE) {
                    newItem.createOptionEvent();
                    newItem.setExpiry(1);
                } else if (newItem.template.type == ItemType.TYPE_BODY_PET) {
                    newItem.setExpiry(7);
                } else {
                    newItem.setDefaultOption();
                }
                player.addItem(newItem, true);
                return;
            }

            case ItemName.CUA_BIEN: {
                player.removeQuantityItemBag(item, 1);
                Item newItem = ItemManager.getInstance().createItem(CUA_BIEN.next(), 1, false);
                if (newItem.template.id == ItemName.HUY_HIEU_HE_2024 || newItem.template.id == ItemName.CAI_TRANG_POC_HE_2024
                        || newItem.template.id == ItemName.CAI_TRANG_PIC_HE_2024 || newItem.template.id == ItemName.VAN_LUOT_SONG_THAN
                        || newItem.template.id == ItemName.BABY_SHARK || newItem.template.id == ItemName.O_BACH_TUOC || newItem.template.id == ItemName.CANH_THUY_TE) {
                    newItem.createOptionEvent();
                    if (Utils.nextInt(1000) == 0) {
                        newItem.setExpiry(-1);
                        Server.getInstance().service.serverChat(String.format("Chúc mừng %s mở %s nhận được %s vĩnh viễn", player.name, item.template.name, newItem.template.name));
                    } else {
                        newItem.setExpiry(1);
                    }
                } else if (newItem.template.type == ItemType.TYPE_BODY_PET) {
                    newItem.setExpiry(7);
                } else {
                    newItem.setDefaultOption();
                }
                player.addItem(newItem, true);
                return;
            }

            case ItemName.BO_CAN_CAU_DAC_BIET:
            case ItemName.BO_CAN_CAU_THUONG: {
                if (player.zone == null || player.zone.map.template.id != MapName.DAO_KAME) {
                    player.addInfo(Player.INFO_RED, "Chỉ có thể sử dụng tại Đảo Kame");
                    return;
                }
                player.removeQuantityItemBag(item, 1);
                boolean isSpecial = item.template.id == ItemName.BO_CAN_CAU_DAC_BIET;
                int itemId;
                if (isSpecial) {
                    itemId = GIFT_SPECIAL.next();
                } else {
                    itemId = GIFT_NORMAL.next();
                }
                Item itm = ItemManager.getInstance().createItem(itemId, 1, false);
                if (item.isLock) {
                    itm.isLock = true;
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
                    countSpecial[0]++;
                    if (countSpecial[0] > countSpecial[1]) {
                        isHaveItem = true;
                    }
                } else {
                    countNormal[0]++;
                    if (countNormal[0] > countNormal[1]) {
                        isHaveItem = true;
                    }
                }
                if (isHaveItem) {
                    Item newItem = ItemManager.getInstance().createItem(ItemName.CAPSULE_HUYEN_BI, 1, false);
                    newItem.setExpiry(3);
                    if (player.addItem(newItem)) {
                        Server.getInstance().service.serverChat(String.format("Chúc mừng %s mở %s nhận được %s", player.name, item.template.name, newItem.template.name));
                        if (isSpecial) {
                            countSpecial[0] = 0;
                            countSpecial[1] = Utils.nextInt(2000, 2500);
                        } else {
                            countNormal[0] = 0;
                            countNormal[1] = Utils.nextInt(5000, 5500);
                        }
                    }
                }
                return;
            }
            default:
                throw new IllegalStateException("Unexpected value: " + item.template.id);
        }
    }

    @Override
    public List<ItemShop> createShop(Player player) {
        List<ItemShop> itemShops = new ArrayList<>();
        itemShops.add(new ItemShop(ItemName.CAN_CAU_THUONG, 1, TypePrice.COIN, 1000000));
        itemShops.add(new ItemShop(ItemName.CAN_CAU_THUONG, 100, TypePrice.COIN, 100000000));

        ItemShop itemShop1 = new ItemShop(ItemName.CAN_CAU_DAC_BIET, 25, TypePrice.DIAMOND, 50);
        itemShop1.options.add(new ItemOption(74, 5));
        itemShops.add(itemShop1);

        ItemShop itemShop2 = new ItemShop(ItemName.CAN_CAU_DAC_BIET, 2500, TypePrice.DIAMOND, 4900);
        itemShop2.options.add(new ItemOption(74, 490));
        itemShops.add(itemShop2);

        ItemShop itemShop3 = new ItemShop(ItemName.CAN_CAU_DAC_BIET, 10, TypePrice.DIAMOND, 50);
        itemShop3.isLock = true;
        itemShops.add(itemShop3);

        ItemShop itemShop4 = new ItemShop(ItemName.CAN_CAU_DAC_BIET, 1000, TypePrice.DIAMOND, 5000);
        itemShop4.isLock = true;
        itemShops.add(itemShop4);

        itemShops.add(new ItemShop(ItemName.CAPSULE_HE_2024, 1, TypePrice.DIAMOND, 500));
        itemShops.add(new ItemShop(ItemName.CAPSULE_HE_2024, 10, TypePrice.DIAMOND, 4900));
        itemShops.add(new ItemShop(ItemName.CAPSULE_HE_2024, 100, TypePrice.DIAMOND, 45000));

        ItemShop itemShop5 = new ItemShop(ItemName.CAPSULE_HE_2024, 1, TypePrice.DIAMOND, 10);
        itemShop5.options.clear();
        itemShops.add(itemShop5);

        ItemShop itemShop6 = new ItemShop(ItemName.CAPSULE_HE_2024, 1, TypePrice.DIAMOND, 50);
        itemShop6.options.clear();
        itemShop6.options.add(new ItemOption(161, 1));
        itemShops.add(itemShop6);

        ItemShop itemShop7 = new ItemShop(ItemName.CAPSULE_HE_2024, 10, TypePrice.DIAMOND, 500);
        itemShop7.options.clear();
        itemShop7.options.add(new ItemOption(161, 1));
        itemShops.add(itemShop7);

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
                    itemMaps.add(ItemManager.getInstance().createItemMap(ItemName.MOI_CAU_CA, 1, killer.id));
                }
            } else if (entity.zone.map.isBarrack()) {
                itemMaps.add(ItemManager.getInstance().createItemMap(ItemName.PHAO_CAU_CA, 1, killer.id));
            }
        }
        return itemMaps;
    }

    @Override
    public void rewardTaskDaily(Player player) {
        if (!player.isBagFull()) {
            player.addItem(ItemManager.getInstance().createItem(ItemName.DAY_CUOC, Utils.nextInt(5, 10), true), true);
        }
    }

    @Override
    public void rewardRecharge(Player player, int diamond) {
        if (!player.isBagFull()) {
            int quantity = diamond / 700;
            if (quantity > 0) {
                player.addItem(ItemManager.getInstance().createItem(ItemName.CAPSULE_HE_2024, quantity, true), true);
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
        }
        return itemShopList;
    }

    @Override
    public void start() {
        long day = 86400;
        Utils.setScheduled(() -> {
            try {
                rewards.clear();
                used.clear();
                diamondUses.clear();
            } catch (Exception ignored) {
            }
        }, day, 0, 0);
        for (int i = 0; i < 50; i++) {
            new BunmaBikini().enter(BunmaBikini.MAPS);
        }
        new PocSummer().joinClient();
        new PicSummer().joinClient();
        return;
    }
}
