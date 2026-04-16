package com.beemobi.rongthanonline.shop;

import com.beemobi.rongthanonline.common.Language;
import com.beemobi.rongthanonline.data.ItemConsignmentData;
import com.beemobi.rongthanonline.entity.player.Player;
import com.beemobi.rongthanonline.item.*;
import com.beemobi.rongthanonline.repository.GameRepository;
import com.beemobi.rongthanonline.server.Server;
import com.beemobi.rongthanonline.util.Utils;
import org.apache.log4j.Logger;

import java.sql.Timestamp;
import java.util.*;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class ShopConsignment extends Shop {
    private static final Logger logger = Logger.getLogger(ShopConsignment.class);
    public static final String TRANG_PHUC = "Trang phục";
    public static final String TRANG_SUC = "Trang sức";
    public static final String LINH_TINH = "Linh tinh";
    public static final String GIAN_HANG = "Gian hàng";
    public static List<String> notes = new ArrayList<>();

    public HashMap<Integer, ItemConsignment> items;
    public ReadWriteLock lock;

    static {
        notes.add("- Bee Consignment giúp các chiến binh trao đổi mua bán một cách dễ dàng thuận tiện");
        notes.add("- Cần ít nhất 100 điểm năng động để sử dụng tính năng này");
        notes.add("- Mỗi chiến binh chỉ có thể đăng bán tối đa 10 vật phẩm");
        notes.add("- Phí đăng bán mỗi vật phẩm là 1 Ruby");
        notes.add("- Phí dịch vụ là 1% giá trị vật phẩm sau khi bán");
        notes.add("- Vật phẩm hết hạn sử dụng sẽ bị xóa khỏi gian hàng");
        notes.add("- Vật phẩm sẽ bị trừ hạn sử dụng nhanh hơn bình thường nếu máy chủ bảo trì đột xuất vào 0h sáng");
    }

    public ShopConsignment(ShopType type) {
        super(type);
        items = new HashMap<>();
        lock = new ReentrantReadWriteLock();
    }

    public void addItem(Player player, int index, int quantity, int price) {
        if (quantity < 1 || price > 2000000000 || index < 0 || index >= player.itemsBag.length) {
            return;
        }
        if (price < 1000000) {
            player.addInfo(Player.INFO_RED, "Giá bán tối thiểu là 1tr xu");
            return;
        }
        lock.writeLock().lock();
        try {
            if (player.pointActive < 100) {
                player.addInfo(Player.INFO_RED, "Yêu cầu điểm năng động tối thiểu 100");
                return;
            }
            if (!player.isEnoughMoney(TypePrice.RUBY, 1)) {
                return;
            }
            if (player.isTrading()) {
                player.addInfo(Player.INFO_RED, Language.CANCEL_ACTION_WHEN_TRADE);
                return;
            }
            if (items.values().stream()
                    .filter(i -> i.sellerId == player.id
                            && (i.status == ItemConsignmentStatus.ON_SALE || i.status == ItemConsignmentStatus.SOLD))
                    .count() > 10) {
                player.addInfo(Player.INFO_RED, "Chỉ có thể bán tối đa 10 vật phẩm");
                return;
            }
            Item item = player.itemsBag[index];
            if (item == null || item.quantity < quantity) {
                return;
            }
            int require = item.getQuantityConsignment();
            if (require == -1) {
                player.addInfo(Player.INFO_RED, "Không thể bán vật phẩm này");
                return;
            }
            if (quantity < require) {
                player.addInfo(Player.INFO_RED, String.format("Yêu cầu số lượng bán tối thiểu %d", require));
                return;
            }
            ItemConsignmentData data = new ItemConsignmentData(player, item, quantity, price);
            GameRepository.getInstance().itemConsignmentData.save(data);
            player.downMoney(TypePrice.RUBY, 1);
            player.removeQuantityItemBag(index, quantity);
            ItemConsignment itemConsignment = new ItemConsignment(data);
            items.put(itemConsignment.id, itemConsignment);
            player.addInfo(Player.INFO_YELLOW, String.format("Treo bán %s thành công", item.template.name));
            player.service.showTab(-1);
        } finally {
            lock.writeLock().unlock();
        }
    }

    public void buyItem(Player player, int id) {
        ItemConsignment itemConsignment = items.get(id);
        if (itemConsignment == null || itemConsignment.sellerId == player.id) {
            return;
        }
        itemConsignment.lock.lock();
        try {
            if (itemConsignment.status != ItemConsignmentStatus.ON_SALE) {
                player.addInfo(Player.INFO_RED, "Vật phẩm đã được bán, hủy bán hoặc hết hạn sử dụng");
                return;
            }
            int day = itemConsignment.getExpiry();
            if (day != -1 && day <= 0) {
                player.addInfo(Player.INFO_RED, "Vật phẩm đã hết hạn sử dụng");
                return;
            }
            long now = System.currentTimeMillis();
            long time = itemConsignment.getTimeLeft(now);
            if (time <= 0) {
                return;
            }
            if (player.xu < itemConsignment.price) {
                player.addInfo(Player.INFO_RED, String.format("Bạn còn thiếu %s xu", Utils.getMoneys(itemConsignment.price - player.xu)));
                return;
            }
            Item item = itemConsignment.cloneItem();
            if (player.addItem(item)) {
                player.upXu(-itemConsignment.price);
                itemConsignment.setStatus(ItemConsignmentStatus.SOLD);
                itemConsignment.buyerId = player.id;
                itemConsignment.buyerName = player.name;
                itemConsignment.buyTime = new Timestamp(now);
                if (item.quantity > 1) {
                    player.addInfo(Player.INFO_YELLOW, String.format("Bạn nhận được x%d %s", item.quantity, item.template.name));
                } else {
                    player.addInfo(Player.INFO_YELLOW, String.format("Bạn nhận được %s", item.template.name));
                }
                show(player);
            }
        } finally {
            itemConsignment.lock.unlock();
        }
    }

    public void getItem(Player player, int id) {
        ItemConsignment itemConsignment = items.get(id);
        if (itemConsignment == null || itemConsignment.sellerId != player.id) {
            return;
        }
        itemConsignment.lock.lock();
        try {
            if (itemConsignment.status == ItemConsignmentStatus.ON_SALE) {
                if (player.isBagFull()) {
                    player.addInfo(Player.INFO_YELLOW, Language.ME_BAG_FULL);
                    return;
                }
                Item item = itemConsignment.cloneItem();
                if (player.addItem(item)) {
                    itemConsignment.setStatus(ItemConsignmentStatus.CANCEL_SALE);
                    itemConsignment.receiveTime = new Timestamp(System.currentTimeMillis());
                    player.addInfo(Player.INFO_YELLOW, "Hủy bán vật phẩm thành công");
                    show(player);
                }
            } else if (itemConsignment.status == ItemConsignmentStatus.SOLD) {
                itemConsignment.setStatus(ItemConsignmentStatus.RECEIVED_MONEY);
                long price = itemConsignment.price - itemConsignment.price / 100;
                player.upXu(price);
                player.addInfo(Player.INFO_YELLOW, String.format("Bạn nhận được %s", Utils.formatNumber(price)));
                show(player);
            }
        } finally {
            itemConsignment.lock.unlock();
        }
    }

    @Override
    public LinkedHashMap<String, List<ItemShop>> createShop(Player player) {
        lock.readLock().lock();
        try {
            LinkedHashMap<String, List<ItemShop>> itemShops = new LinkedHashMap<>();
            String[] tabs = new String[]{TRANG_PHUC, TRANG_SUC, LINH_TINH, GIAN_HANG};
            for (String tab : tabs) {
                itemShops.put(tab, new ArrayList<>());
            }
            long now = System.currentTimeMillis();
            for (ItemConsignment item : items.values()) {
                if (item.status == ItemConsignmentStatus.CANCEL_SALE
                        || item.status == ItemConsignmentStatus.EXPIRED
                        || item.status == ItemConsignmentStatus.RECEIVED_MONEY) {
                    continue;
                }
                if (item.sellerId == player.id) {
                    itemShops.get(GIAN_HANG).add(item);
                } else if (item.status != ItemConsignmentStatus.SOLD) {
                    long time = item.getTimeLeft(now);
                    if (time > 0) {
                        if (item.template.isItemAo() || item.template.isItemGang() || item.template.isItemQuan() || item.template.isItemGiay()) {
                            itemShops.get(TRANG_PHUC).add(item);
                        } else if (item.template.isItemRadar() || item.template.isItemDayChuyen() || item.template.isItemNhan() || item.template.isItemBoi()) {
                            itemShops.get(TRANG_SUC).add(item);
                        } else {
                            itemShops.get(LINH_TINH).add(item);
                        }
                    }
                }
            }
            return itemShops;
        } finally {
            lock.readLock().unlock();
        }
    }

    public void init() {
        if (Server.getInstance().isInterServer()) {
            return;
        }
        ItemConsignmentStatus[] statuses = new ItemConsignmentStatus[]{
                ItemConsignmentStatus.ON_SALE,
                ItemConsignmentStatus.SOLD
        };
        List<ItemConsignmentData> dataList = GameRepository.getInstance().itemConsignmentData.findByServerAndStatusIn(Server.ID, statuses);
        for (ItemConsignmentData data : dataList) {
            ItemConsignment item = new ItemConsignment(data);
            items.put(item.id, item);
        }
        Utils.setScheduled(this::updateNewDay, 86400, 0, 0);
    }

    @Override
    public boolean[] getIndexBagLightInShop(Player player) {
        boolean[] flags = new boolean[player.itemsBag.length];
        for (int i = 0; i < player.itemsBag.length; i++) {
            Item item = player.itemsBag[i];
            if (item == null) {
                flags[i] = false;
            } else {
                flags[i] = item.getQuantityConsignment() != -1;
            }
        }
        return flags;
    }

    public void saveData() {
        //lock.readLock().lock();
        try {
            for (ItemConsignment itemConsignment : items.values()) {
                itemConsignment.saveData();
            }
        } finally {
            //lock.readLock().unlock();
        }
    }

    public void updateNewDay() {
        lock.writeLock().lock();
        try {
            for (ItemConsignment itemConsignment : items.values()) {
                if (itemConsignment.status == ItemConsignmentStatus.ON_SALE) {
                    itemConsignment.lock.lock();
                    try {
                        if (itemConsignment.status == ItemConsignmentStatus.ON_SALE) {
                            for (ItemOption option : itemConsignment.options) {
                                if (option.template.id == 50) {
                                    option.param--;
                                    if (option.param <= 0) {
                                        itemConsignment.setStatus(ItemConsignmentStatus.EXPIRED);
                                        break;
                                    }
                                }
                            }
                        }
                    } finally {
                        itemConsignment.lock.unlock();
                    }
                }
            }
        } finally {
            lock.writeLock().unlock();
        }
    }
}
