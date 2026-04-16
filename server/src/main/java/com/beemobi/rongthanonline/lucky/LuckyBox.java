package com.beemobi.rongthanonline.lucky;

import com.beemobi.rongthanonline.common.Language;
import com.beemobi.rongthanonline.common.RandomCollection;
import com.beemobi.rongthanonline.entity.player.Player;
import com.beemobi.rongthanonline.event.Event;
import com.beemobi.rongthanonline.item.Item;
import com.beemobi.rongthanonline.item.ItemName;
import com.beemobi.rongthanonline.network.Message;
import com.beemobi.rongthanonline.network.MessageName;
import com.beemobi.rongthanonline.server.Server;
import com.beemobi.rongthanonline.shop.TypePrice;
import com.beemobi.rongthanonline.util.Utils;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

public class LuckyBox {
    private static final Logger logger = Logger.getLogger(LuckyBox.class);

    public RandomCollection<ItemLuckyBox> items;

    public LuckyBox() {
        items = new RandomCollection<>();
        items.add(0.3, new ItemLuckyBox(ItemName.CAI_TRANG_BROLY_HUYEN_THOAI));
        items.add(0.3, new ItemLuckyBox(ItemName.HUY_HIEU_RONG_THAN));
        items.add(0.2, new ItemLuckyBox(ItemName.CAI_TRANG_PUI_PUI));
        items.add(0.2, new ItemLuckyBox(ItemName.CAI_TRANG_YACON));
        items.add(5, new ItemLuckyBox(ItemName.XU, 70000000, 100000000));
        items.add(15, new ItemLuckyBox(ItemName.XU, 30000000, 50000000));
        items.add(10, new ItemLuckyBox(ItemName.XU, 50000000, 70000000));
        items.add(5, new ItemLuckyBox(ItemName.XU_KHOA, 100000000, 150000000));
        items.add(5, new ItemLuckyBox(ItemName.DA_11, 10, 20));
        items.add(5, new ItemLuckyBox(ItemName.DA_12, 5, 10));
        items.add(5, new ItemLuckyBox(ItemName.DA_7, 150, 200));
        items.add(5, new ItemLuckyBox(ItemName.DA_8, 70, 150));
        items.add(5, new ItemLuckyBox(ItemName.DA_9, 50, 70));
        items.add(5, new ItemLuckyBox(ItemName.DA_10, 20, 50));
        items.add(5, new ItemLuckyBox(ItemName.BUA_BAO_VE_CAP_3, 3, 5));
        items.add(5, new ItemLuckyBox(ItemName.BUA_BAO_VE_CAP_4));

        for (ItemLuckyBox item : items.getMap().values()) {
            switch (item.template.id) {
                case ItemName.GOKU_HIEP_SI_RONG:
                case ItemName.CAI_TRANG_CUMBER_SUPER_BLUE:
                case ItemName.CAI_TRANG_BROLY_HUYEN_THOAI:
                case ItemName.HUY_HIEU_RONG_THAN:
                    item.setDefaultOption();
                    break;

            }
        }
    }

    public void show(Player player, int type) {
        try {
            player.typeLuckyBox = type;
            Message msg = new Message(MessageName.TAB_LUCKY);
            msg.writer().writeByte(0);
            if (type == 1) {
                msg.writer().writeUTF("1 Thẻ vận may");
            } else {
                msg.writer().writeUTF("50 Kim cương");
            }
            player.sendMessage(msg);
            msg.cleanup();
        } catch (Exception ex) {
            logger.error("show", ex);
        }
    }

    public void next(Player player) {
        player.lockAction.lock();
        try {
            if (player.isBagFull()) {
                player.addInfo(Player.INFO_RED, Language.ME_BAG_FULL);
                return;
            }
            if (player.typeLuckyBox == 1) {
                if (player.getQuantityItemInBag(ItemName.THE_VAN_MAY) < 1) {
                    player.addInfo(Player.INFO_RED, "Bạn không có đủ Thẻ vận may");
                    return;
                }
                player.removeQuantityItemBagById(ItemName.THE_VAN_MAY, 1);
            } else {
                long price = 50;
                if (!player.isEnoughMoney(TypePrice.DIAMOND, price)) {
                    return;
                }
                player.downMoney(TypePrice.DIAMOND, price);
                if (Event.isEvent()) {
                    player.upParamMissionEvent((int) price);
                }
            }
            ItemLuckyBox itemLucky = items.next();
            List<Item> itemList = new ArrayList<>();
            itemList.add(itemLucky.next());
            List<ItemLuckyBox> randoms = new ArrayList<>(items.getMap().values());
            for (int i = 0; i < 8; i++) {
                ItemLuckyBox itemLuckyBox = randoms.get(Utils.nextInt(randoms.size()));
                if (itemLuckyBox.template.isAvatarLegendary()) {
                    itemLuckyBox = randoms.get(Utils.nextInt(randoms.size()));
                }
                if (itemLuckyBox.template.isAvatarLegendary()) {
                    itemLuckyBox = randoms.get(Utils.nextInt(randoms.size()));
                }
                itemList.add(itemLuckyBox.show());
            }
            player.service.luckyBox(itemList);
            Item item = itemList.get(0);
            player.addItem(item, true);
            if (item.template.isAvatarLegendary() || item.isItemRider()) {
                Server.getInstance().service.serverChat(String.format("Chúc mừng %s tham gia Ô may mắn nhận được %s", player.name, item.template.name));
            } else if (item.template.id == ItemName.XU) {
                if (item.quantity >= 20000000) {
                    Server.getInstance().service.serverChat(String.format("Chúc mừng %s tham gia Ô may mắn nhận được %s Xu", player.name, Utils.getMoneys(item.quantity)));
                }
            } else if (item.template.id == ItemName.XU_KHOA) {
                Server.getInstance().service.serverChat(String.format("Chúc mừng %s tham gia Ô may mắn nhận được %s Xu khóa", player.name, Utils.getMoneys(item.quantity)));
            }
        } catch (Exception ex) {
            logger.error("next", ex);
        } finally {
            player.lockAction.unlock();
        }
    }
}
