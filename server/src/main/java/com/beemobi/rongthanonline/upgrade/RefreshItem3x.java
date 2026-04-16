package com.beemobi.rongthanonline.upgrade;

import com.beemobi.rongthanonline.command.Command;
import com.beemobi.rongthanonline.command.CommandName;
import com.beemobi.rongthanonline.common.KeyValue;
import com.beemobi.rongthanonline.common.Language;
import com.beemobi.rongthanonline.entity.player.Player;
import com.beemobi.rongthanonline.item.Item;
import com.beemobi.rongthanonline.item.ItemManager;
import com.beemobi.rongthanonline.item.ItemName;
import com.beemobi.rongthanonline.item.ItemOption;
import com.beemobi.rongthanonline.network.Message;
import com.beemobi.rongthanonline.npc.NpcName;
import com.beemobi.rongthanonline.util.Utils;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class RefreshItem3x extends Upgrade {
    private static final Logger logger = Logger.getLogger(RefreshItem3x.class);
    private static final int COIN = 500000;
    private static final int DIAMOND = 10;
    public static HashMap<Integer, Integer> ITEM_REFRESH;

    static {
        ITEM_REFRESH = new HashMap<>();
        ITEM_REFRESH.put(ItemName.GANG_SIEU_CHIEN_BINH_TRAI_DAT, ItemName.MANH_GANG_3X);
        ITEM_REFRESH.put(ItemName.GIAY_SIEU_CHIEN_BINH_TRAI_DAT, ItemName.MANH_GIAY_3X);
        ITEM_REFRESH.put(ItemName.QUAN_SIEU_CHIEN_BINH_TRAI_DAT, ItemName.MANH_QUAN_3X);
        ITEM_REFRESH.put(ItemName.AO_SIEU_CHIEN_BINH_TRAI_DAT, ItemName.MANH_AO_3X);
        ITEM_REFRESH.put(ItemName.GANG_SIEU_CHIEN_BINH_NAMEK, ItemName.MANH_GANG_3X);
        ITEM_REFRESH.put(ItemName.GIAY_SIEU_CHIEN_BINH_NAMEK, ItemName.MANH_GIAY_3X);
        ITEM_REFRESH.put(ItemName.QUAN_SIEU_CHIEN_BINH_NAMEK, ItemName.MANH_QUAN_3X);
        ITEM_REFRESH.put(ItemName.AO_SIEU_CHIEN_BINH_NAMEK, ItemName.MANH_AO_3X);
        ITEM_REFRESH.put(ItemName.GANG_SIEU_CHIEN_BINH_SAYAIN, ItemName.MANH_GANG_3X);
        ITEM_REFRESH.put(ItemName.GIAY_SIEU_CHIEN_BINH_SAYAIN, ItemName.MANH_GIAY_3X);
        ITEM_REFRESH.put(ItemName.QUAN_SIEU_CHIEN_BINH_SAYAIN, ItemName.MANH_QUAN_3X);
        ITEM_REFRESH.put(ItemName.AO_SIEU_CHIEN_BINH_SAYAIN, ItemName.MANH_AO_3X);
        ITEM_REFRESH.put(ItemName.NGOC_BOI_SIEU_CHIEN_BINH, ItemName.MANH_BOI_3X);
        ITEM_REFRESH.put(ItemName.NHAN_SIEU_CHIEN_BINH, ItemName.MANH_NHAN_3X);
        ITEM_REFRESH.put(ItemName.DAY_CHUYEN_SIEU_CHIEN_BINH, ItemName.MANH_DAY_CHUYEN_3X);
        ITEM_REFRESH.put(ItemName.RADAR_SIEU_CHIEN_BINH, ItemName.MANH_RADAR_3X);
    }

    public RefreshItem3x(UpgradeType type, String name, String command) {
        super(type, name, command);
        notes.add("- Vào túi đồ chọn trang bị 3x cần tái chế");
        notes.add("- Sau đó chọn mảnh trang bị 3x cùng loại (cần tối thiếu 100 mảnh)");
        notes.add("- Sau đó chọn Tái chế");
    }

    @Override
    public void upgrade(Message message, Player player) {
        player.lockAction.lock();
        try {
            if (player.level < 30) {
                player.addInfo(Player.INFO_RED, "Yêu cầu cấp độ từ 30 trở lên");
                return;
            }
            if (player.isBagFull()) {
                player.addInfo(Player.INFO_RED, Language.ME_BAG_FULL);
                return;
            }
            int size = message.reader().readByte();
            if (size != 2) {
                player.addInfo(Player.INFO_RED, "Chỉ được chọn duy nhất 1 loại trang bị 3x và 1 loại Mảnh trang bị cùng loại");
                return;
            }
            Item[] items = new Item[2];
            for (int i = 0; i < items.length; i++) {
                int index = message.reader().readByte();
                if (index < 0 || index >= player.itemsBag.length) {
                    return;
                }
                Item item = player.itemsBag[index];
                if (item == null) {
                    player.addInfo(Player.INFO_RED, Language.ITEM_INVALID);
                    return;
                }
                if (items[0] == null && ITEM_REFRESH.containsKey(item.template.id)) {
                    items[0] = item;
                }
                if (items[1] == null && ITEM_REFRESH.containsValue(item.template.id)) {
                    items[1] = item;
                }
            }
            if (items[0] == null || items[1] == null || ITEM_REFRESH.get(items[0].template.id) != items[1].template.id) {
                player.addInfo(Player.INFO_RED, Language.ITEM_INVALID);
                return;
            }
            if (items[1].quantity < 100) {
                player.addInfo(Player.INFO_RED, String.format("Số lượng %s không đủ", items[1].template.name));
                return;
            }
            List<Command> commands = new ArrayList<>();
            StringBuilder content = new StringBuilder();
            content.append(String.format("Tái chế %s", items[0].template.name)).append("\n");
            content.append(String.format("Sau khi tái chế sẽ nhận được %s có chỉ số hoàn toàn mới", items[0].template.name)).append("\n");
            content.append("Cấp độ và Sao pha lê sẽ được bảo toàn").append("\n");
            content.append("Tái chế đặc biệt sẽ tăng tỉ lệ ra trang bị có chỉ số cao").append("\n");
            content.append("Tỉ lệ thành công 100%").append("\n");
            content.append("Vật phẩm sẽ bị khóa sau khi tái chế");
            commands.add(new Command(CommandName.CONFIRM_UPGRADE, String.format("Tái chế\nthường\n%s xu", Utils.formatNumber(COIN)), player, 0, items[0].indexUI, items[1].indexUI));
            commands.add(new Command(CommandName.CONFIRM_UPGRADE, String.format("Tái chế\nđặc biệt\n%s KC", Utils.formatNumber(DIAMOND)), player, 1, items[0].indexUI, items[1].indexUI));
            commands.add(new Command(CommandName.CANCEL, "Hủy", player));
            player.createMenu(NpcName.ME, content.toString(), commands);
        } catch (Exception ex) {
            logger.error("upgrade", ex);
        } finally {
            player.lockAction.unlock();
        }
    }

    @Override
    public void confirmUpgrade(Player player, Object[] objects) {
        player.lockAction.lock();
        try {
            if (player.level < 30) {
                player.addInfo(Player.INFO_RED, "Yêu cầu cấp độ từ 30 trở lên");
                return;
            }
            if (player.isBagFull()) {
                player.addInfo(Player.INFO_RED, Language.ME_BAG_FULL);
                return;
            }
            int type = (int) objects[0];
            int[] index = new int[]{(int) objects[1], (int) objects[2]};
            Item[] items = new Item[2];
            for (int i = 0; i < items.length; i++) {
                if (index[i] < 0 || index[i] >= player.itemsBag.length) {
                    return;
                }
                Item item = player.itemsBag[index[i]];
                if (item == null) {
                    player.addInfo(Player.INFO_RED, Language.ITEM_INVALID);
                    return;
                }
                if (items[0] == null && ITEM_REFRESH.containsKey(item.template.id)) {
                    items[0] = item;
                }
                if (items[1] == null && ITEM_REFRESH.containsValue(item.template.id)) {
                    items[1] = item;
                }
            }
            if (items[0] == null || items[1] == null || ITEM_REFRESH.get(items[0].template.id) != items[1].template.id) {
                player.addInfo(Player.INFO_RED, Language.ITEM_INVALID);
                return;
            }
            if (items[1].quantity < 100) {
                player.addInfo(Player.INFO_RED, String.format("Số lượng %s không đủ", items[1].template.name));
                return;
            }
            if (type == 0) {
                if (player.xu < COIN) {
                    player.addInfo(Player.INFO_RED, "Số lượng xu không đủ");
                    return;
                }
                player.upXu(-COIN);
            } else {
                if (player.diamond < DIAMOND) {
                    player.addInfo(Player.INFO_RED, "Số lượng kim cương không đủ");
                    return;
                }
                player.upDiamond(-DIAMOND);
            }
            items[1].quantity -= 100;
            if (items[1].quantity <= 0) {
                player.itemsBag[items[1].indexUI] = null;
            }
            Item item = ItemManager.getInstance().createItem(items[0].template.id, 1, true);
            if (type == 0) {
                item.randomParam(-15, 10);
            } else {
                item.randomParam(0, 15);
            }
            int upgrade = items[0].getUpgrade();
            for (int i = 0; i < upgrade; i++) {
                item.nextUpgrade();
            }
            item.isLock = true;
            ItemOption optionStar = items[0].getOption(67);
            if (optionStar != null) {
                item.options.add(new ItemOption(67, optionStar.param));
                ItemOption itemOption = items[0].getOption(68);
                if (itemOption == null) {
                    item.options.add(new ItemOption(68, 0));
                } else {
                    item.options.add(new ItemOption(68, itemOption.param));
                    for (ItemOption option : items[0].options) {
                        if (option.template.type == 4) {
                            item.options.add(new ItemOption(option.template.id, option.param));
                        }
                    }
                }
            }
            item.indexUI = items[0].indexUI;
            player.itemsBag[items[0].indexUI] = item;
            player.service.setItemBag();
            player.addInfo(Player.INFO_YELLOW, String.format("Bạn nhận được %s", item.template.name));
        } catch (Exception ex) {
            logger.error("confirmUpgrade", ex);
        } finally {
            player.lockAction.unlock();
        }
    }

    @Override
    public boolean[] getIndexCanUpgrade(Player player) {
        boolean[] flags = new boolean[player.itemsBag.length];
        for (int i = 0; i < flags.length; i++) {
            Item item = player.itemsBag[i];
            if (item != null && (ITEM_REFRESH.containsKey(item.template.id) || ITEM_REFRESH.containsValue(item.template.id))) {
                flags[i] = true;
            }
        }
        return flags;
    }
}
