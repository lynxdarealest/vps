package com.beemobi.rongthanonline.event;

import com.beemobi.rongthanonline.bot.boss.Boss;
import com.beemobi.rongthanonline.command.Command;
import com.beemobi.rongthanonline.command.CommandName;
import com.beemobi.rongthanonline.common.Language;
import com.beemobi.rongthanonline.entity.Entity;
import com.beemobi.rongthanonline.entity.monster.Monster;
import com.beemobi.rongthanonline.entity.player.Player;
import com.beemobi.rongthanonline.item.*;
import com.beemobi.rongthanonline.model.Level;
import com.beemobi.rongthanonline.npc.NpcName;
import com.beemobi.rongthanonline.server.Server;
import com.beemobi.rongthanonline.top.Top;
import com.beemobi.rongthanonline.top.TopManager;
import com.beemobi.rongthanonline.top.TopType;
import com.beemobi.rongthanonline.util.Utils;
import org.apache.log4j.Logger;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class NationalDay {
    /*private static final Logger logger = Logger.getLogger(NationalDay.class);

    public NationalDay(String name, Timestamp startTime, Timestamp endTime) {
        super(name, startTime, endTime);
        GIFT_SPECIAL.add(10, ItemName.DA_10);
        GIFT_SPECIAL.add(10, ItemName.DA_9);
        GIFT_SPECIAL.add(10, ItemName.DA_8);
        GIFT_SPECIAL.add(10, ItemName.DA_7);
        GIFT_SPECIAL.add(5, ItemName.BUA_BAO_VE_CAP_2);
        GIFT_SPECIAL.add(5, ItemName.BUA_BAO_VE_CAP_3);
        GIFT_SPECIAL.add(10, ItemName.DUA_XANH);
        GIFT_SPECIAL.add(10, ItemName.DUA_VANG);
        GIFT_SPECIAL.add(10, ItemName.DUA_NAU);
        GIFT_SPECIAL.add(10, ItemName.CO_DO_SAO_VANG);
        GIFT_SPECIAL.setDefault(ItemName.POWER);
    }

    @Override
    public void openMenu(Player player) {
        List<Command> commands = new ArrayList<>();
        commands.add(new Command(CommandName.SHOW_REWARD_EVENT, "Nhận thưởng", player));
        commands.add(new Command(CommandName.THONG_TIN_SU_KIEN, "Thông tin", player));
        player.createMenu(NpcName.ME, String.format("Sự kiện %s", name), commands);
    }

    @Override
    public void showMenuExchange(Player player) {

    }

    @Override
    public void exchangeQuantity(Player player, int type) {

    }

    @Override
    public void exchange(Player player, int type, int quantity) {

    }

    @Override
    public int next(int type) {
        return GIFT_SPECIAL.next();
    }

    @Override
    public void reward(Player player) {
        if (player.pointEvent < 200) {
            player.addInfo(Player.INFO_RED, "Hiện tại bạn chưa có phần thưởng nào");
            return;
        }
        if (player.indexRewardEvent == 0) {
            player.indexRewardEvent = 1;
            player.addReward(21, "ADMIN", "Phần thưởng mốc 200 điểm sự kiện Quốc khánh 2/9");
            player.addReward(22, "ADMIN", "Phần thưởng mốc 200 điểm sự kiện Quốc khánh 2/9");
            player.addReward(23, "ADMIN", "Phần thưởng mốc 200 điểm sự kiện Quốc khánh 2/9");
            player.addInfo(Player.INFO_YELLOW, "Nhận thưởng thành công");
            return;
        }
        if (player.indexRewardEvent == 1 && player.pointEvent >= 500) {
            player.indexRewardEvent = 2;
            player.addReward(24, "ADMIN", "Phần thưởng mốc 500 điểm sự kiện Quốc khánh 2/9");
            player.addReward(25, "ADMIN", "Phần thưởng mốc 500 điểm sự kiện Quốc khánh 2/9");
            player.addReward(26, "ADMIN", "Phần thưởng mốc 500 điểm sự kiện Quốc khánh 2/9");
            player.addInfo(Player.INFO_YELLOW, "Nhận thưởng thành công");
            return;
        }
        if (player.indexRewardEvent == 2 && player.pointEvent >= 1000) {
            player.indexRewardEvent = 3;
            player.addReward(27, "ADMIN", "Phần thưởng mốc 1000 điểm sự kiện Quốc khánh 2/9");
            player.addReward(28, "ADMIN", "Phần thưởng mốc 1000 điểm sự kiện Quốc khánh 2/9");
            player.addReward(29, "ADMIN", "Phần thưởng mốc 1000 điểm sự kiện Quốc khánh 2/9");
            player.addReward(30, "ADMIN", "Phần thưởng mốc 1000 điểm sự kiện Quốc khánh 2/9");
            player.addReward(31, "ADMIN", "Phần thưởng mốc 1000 điểm sự kiện Quốc khánh 2/9");
            player.addReward(156, "ADMIN", "Phần thưởng mốc 1000 điểm sự kiện Quốc khánh 2/9");
            player.addInfo(Player.INFO_YELLOW, "Nhận thưởng thành công");
            return;
        }
        player.addInfo(Player.INFO_RED, "Hiện tại bạn chưa có phần thưởng nào");
    }

    @Override
    public void useItem(Player player, Item item) {
        if (item.template.id == ItemName.TUI_MAY_MAN) {
            if (player.isBagFull()) {
                player.addInfo(Player.INFO_RED, Language.ME_BAG_FULL);
                return;
            }
            player.removeQuantityItemBag(item, 1);
            int itemId = Event.event.next(Event.TYPE_SPECIAL);
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
                if (itm.template.id == ItemName.CO_DO_SAO_VANG) {
                    itm.createOptionSpec(3, 2);
                    itm.setExpiry(1);
                } else if (itm.isItemBody()) {
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
            player.pointEvent++;
            player.updateTimeEvent = System.currentTimeMillis();
            Top top = TopManager.getInstance().tops.get(TopType.TOP_EVENT);
            if (top != null) {
                top.update(this);
            }
        }
    }

    @Override
    public List<ItemShop> createShop(Player player) {
        List<ItemShop> itemShops = new ArrayList<>();
        itemShops.add(new ItemShop(ItemName.TUI_MAY_MAN, 1, ItemShopTypePrice.COIN, 10000000));
        itemShops.add(new ItemShop(ItemName.TUI_MAY_MAN, 10, ItemShopTypePrice.COIN, 100000000));
        itemShops.add(new ItemShop(ItemName.TUI_MAY_MAN, 100, ItemShopTypePrice.COIN, 950000000));
        itemShops.add(new ItemShop(ItemName.TUI_MAY_MAN, 10, ItemShopTypePrice.DIAMOND, 50));
        itemShops.add(new ItemShop(ItemName.TUI_MAY_MAN, 100, ItemShopTypePrice.DIAMOND, 495));
        return itemShops;
    }

    @Override
    public List<ItemMap> throwItem(Player killer, Entity entity) {
        List<ItemMap> itemMaps = new ArrayList<>();
        if (entity.isBoss()) {
            ItemMap itemMap = ItemManager.getInstance().createItemMap(ItemName.TUI_MAY_MAN, 1, killer.id);
            itemMap.options.add(new ItemOption(113, 1));
            itemMaps.add(itemMap);
        }
        return itemMaps;
    }

    @Override
    public List<ItemShop> showShopExchangePoint(Player player) {
        return new ArrayList<>();
    }

    @Override
    public void start() {

    }*/

}
