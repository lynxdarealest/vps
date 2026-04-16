package com.beemobi.rongthanonline.top.player.event;

import com.beemobi.rongthanonline.data.PointWeeklyData;
import com.beemobi.rongthanonline.data.RewardData;
import com.beemobi.rongthanonline.entity.player.Player;
import com.beemobi.rongthanonline.item.Item;
import com.beemobi.rongthanonline.item.ItemManager;
import com.beemobi.rongthanonline.item.ItemName;
import com.beemobi.rongthanonline.item.ItemOption;
import com.beemobi.rongthanonline.mission.MissionManager;
import com.beemobi.rongthanonline.repository.GameRepository;
import com.beemobi.rongthanonline.server.Server;
import com.beemobi.rongthanonline.top.Top;
import com.beemobi.rongthanonline.top.TopInfo;
import com.beemobi.rongthanonline.top.TopType;
import com.beemobi.rongthanonline.top.player.TopPlayer;
import com.beemobi.rongthanonline.top.player.power.TopPowerInfo;
import com.beemobi.rongthanonline.util.Utils;
import org.apache.log4j.Logger;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class TopEvent extends TopPlayer {
    private static final Logger logger = Logger.getLogger(TopEvent.class);

    public TopEvent() {
        super(TopType.TOP_EVENT, "Sự kiện");
        limit = 20;
    }

    @Override
    public void init() {
        List<Object[]> objects = GameRepository.getInstance().playerData.findTopEvent(Server.ID, limit);
        for (Object[] object : objects) {
            TopEventInfo top = new TopEventInfo(null);
            top.id = (int) object[0];
            top.name = (String) object[1];
            top.gender = (int) object[2];
            top.score = Long.parseLong(object[3].toString());
            top.updateTime = Long.parseLong(object[4].toString());
            top.info = String.format("Điểm: %s", Utils.getMoneys(top.score));
            elements.add(top);
        }
        //reward();
    }

    @Override
    public void setObject(Object object) {
        lock.writeLock().lock();
        try {
            Player player = (Player) object;
            TopInfo info = elements.stream().filter(i -> i.id == player.id).findFirst().orElse(null);
            if (info != null) {
                info.setObject(player);
                return;
            }
            elements.add(new TopEventInfo(player));
        } finally {
            lock.writeLock().unlock();
        }
    }

    public void reward() {
        List<TopInfo> tops = getTops();
        int size = Math.min(tops.size(), 10);
        if (size == 0) {
            return;
        }
        List<RewardData> rewardDataList = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            TopInfo info = tops.get(i);
            List<Item> itemList = new ArrayList<>();
            if (i == 0) {
                if (info.score >= 15000) {
                    Item item = ItemManager.getInstance().createItem(ItemName.CAPSULE_HUYEN_BI, 2, false);
                    item.setExpiry(7);
                    item.options.add(new ItemOption(19, 19));
                    item.options.add(new ItemOption(67, 8));
                    item.options.add(new ItemOption(68, 8));
                    itemList.add(item);

                    Item item1 = ItemManager.getInstance().createItem(ItemName.CAI_TRANG_THAN_HUY_DIET, 1, false);
                    item1.options.add(new ItemOption(25, 50));
                    item1.options.add(new ItemOption(31, 50));
                    item1.options.add(new ItemOption(32, 50));
                    item1.options.add(new ItemOption(192, 20));
                    item1.options.add(new ItemOption(33, 3));
                    item1.options.add(new ItemOption(34, 3));
                    item1.options.add(new ItemOption(35, 3));
                    itemList.add(item1);
                }
            } else if (i == 1) {
                if (info.score >= 10000) {
                    Item item = ItemManager.getInstance().createItem(ItemName.CAPSULE_HUYEN_BI, 1, false);
                    item.setExpiry(7);
                    item.options.add(new ItemOption(19, 18));
                    item.options.add(new ItemOption(67, 7));
                    item.options.add(new ItemOption(68, 7));
                    itemList.add(item);

                    Item item1 = ItemManager.getInstance().createItem(ItemName.CAI_TRANG_THAN_HUY_DIET, 1, false);
                    item1.options.add(new ItemOption(25, 50));
                    item1.options.add(new ItemOption(31, 50));
                    item1.options.add(new ItemOption(32, 50));
                    item1.options.add(new ItemOption(192, 15));
                    item1.options.add(new ItemOption(33, 3));
                    item1.options.add(new ItemOption(34, 3));
                    item1.options.add(new ItemOption(35, 3));
                    itemList.add(item1);
                }
            } else {
                if (info.score >= 7500) {
                    Item item = ItemManager.getInstance().createItem(ItemName.CAPSULE_HUYEN_BI, 1, false);
                    item.setExpiry(7);
                    item.options.add(new ItemOption(19, 17));
                    item.options.add(new ItemOption(67, 6));
                    item.options.add(new ItemOption(68, 6));
                    itemList.add(item);

                    Item item1 = ItemManager.getInstance().createItem(ItemName.CAI_TRANG_THAN_HUY_DIET, 1, false);
                    item1.options.add(new ItemOption(25, 50));
                    item1.options.add(new ItemOption(31, 50));
                    item1.options.add(new ItemOption(32, 50));
                    item1.options.add(new ItemOption(192, 10));
                    item1.options.add(new ItemOption(33, 3));
                    item1.options.add(new ItemOption(34, 3));
                    item1.options.add(new ItemOption(35, 3));
                    itemList.add(item1);
                }
            }
            rewardDataList.add(new RewardData(info.id, "Phần thưởng top " + (i + 1) + " " + this.name + " sự kiện", itemList));
        }
        if (!rewardDataList.isEmpty()) {
            GameRepository.getInstance().rewardData.saveAll(rewardDataList);
        }
    }
}
