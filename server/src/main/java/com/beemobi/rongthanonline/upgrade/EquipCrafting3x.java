package com.beemobi.rongthanonline.upgrade;

import com.beemobi.rongthanonline.command.Command;
import com.beemobi.rongthanonline.command.CommandName;
import com.beemobi.rongthanonline.common.KeyValue;
import com.beemobi.rongthanonline.common.Language;
import com.beemobi.rongthanonline.entity.player.Player;
import com.beemobi.rongthanonline.item.Item;
import com.beemobi.rongthanonline.item.ItemManager;
import com.beemobi.rongthanonline.item.ItemName;
import com.beemobi.rongthanonline.network.Message;
import com.beemobi.rongthanonline.npc.NpcName;
import com.beemobi.rongthanonline.util.Utils;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class EquipCrafting3x extends Upgrade {
    private static final Logger logger = Logger.getLogger(EquipCrafting3x.class);

    public static final HashMap<Integer, KeyValue<Integer, Integer[]>> ITEM_CRAFTING;
    private static final int REQUIRE = 499;

    static {
        ITEM_CRAFTING = new HashMap<>();
        ITEM_CRAFTING.put(ItemName.MANH_GANG_3X, new KeyValue<>(5000000, new Integer[]{ItemName.GANG_SIEU_CHIEN_BINH_TRAI_DAT, ItemName.GANG_SIEU_CHIEN_BINH_NAMEK, ItemName.GANG_SIEU_CHIEN_BINH_SAYAIN}));
        ITEM_CRAFTING.put(ItemName.MANH_GIAY_3X, new KeyValue<>(2000000, new Integer[]{ItemName.GIAY_SIEU_CHIEN_BINH_TRAI_DAT, ItemName.GIAY_SIEU_CHIEN_BINH_NAMEK, ItemName.GIAY_SIEU_CHIEN_BINH_SAYAIN}));
        ITEM_CRAFTING.put(ItemName.MANH_AO_3X, new KeyValue<>(2000000, new Integer[]{ItemName.AO_SIEU_CHIEN_BINH_TRAI_DAT, ItemName.AO_SIEU_CHIEN_BINH_NAMEK, ItemName.AO_SIEU_CHIEN_BINH_SAYAIN}));
        ITEM_CRAFTING.put(ItemName.MANH_QUAN_3X, new KeyValue<>(2000000, new Integer[]{ItemName.QUAN_SIEU_CHIEN_BINH_TRAI_DAT, ItemName.QUAN_SIEU_CHIEN_BINH_NAMEK, ItemName.QUAN_SIEU_CHIEN_BINH_SAYAIN}));
        ITEM_CRAFTING.put(ItemName.MANH_RADAR_3X, new KeyValue<>(3500000, new Integer[]{ItemName.RADAR_SIEU_CHIEN_BINH, ItemName.RADAR_SIEU_CHIEN_BINH, ItemName.RADAR_SIEU_CHIEN_BINH}));
        ITEM_CRAFTING.put(ItemName.MANH_DAY_CHUYEN_3X, new KeyValue<>(3500000, new Integer[]{ItemName.DAY_CHUYEN_SIEU_CHIEN_BINH, ItemName.DAY_CHUYEN_SIEU_CHIEN_BINH, ItemName.DAY_CHUYEN_SIEU_CHIEN_BINH}));
        ITEM_CRAFTING.put(ItemName.MANH_NHAN_3X, new KeyValue<>(3500000, new Integer[]{ItemName.NHAN_SIEU_CHIEN_BINH, ItemName.NHAN_SIEU_CHIEN_BINH, ItemName.NHAN_SIEU_CHIEN_BINH}));
        ITEM_CRAFTING.put(ItemName.MANH_BOI_3X, new KeyValue<>(3500000, new Integer[]{ItemName.NGOC_BOI_SIEU_CHIEN_BINH, ItemName.NGOC_BOI_SIEU_CHIEN_BINH, ItemName.NGOC_BOI_SIEU_CHIEN_BINH}));
    }

    public EquipCrafting3x(UpgradeType type, String name, String command) {
        super(type, name, command);
        notes.add("- Vào túi đồ chọn Mảnh trang bị 3x");
        notes.add("- Sau đó chọn làm phép");
    }

    @Override
    public void upgrade(Message message, Player player) {
        player.lockAction.lock();
        try {
            if (player.isBagFull()) {
                player.addInfo(Player.INFO_RED, Language.ME_BAG_FULL);
                return;
            }
            int size = message.reader().readByte();
            if (size != 1) {
                player.addInfo(Player.INFO_RED, "Chỉ được chọn duy nhất 1 loại Mảnh trang bị");
                return;
            }
            int index = message.reader().readByte();
            if (index < 0 || index >= player.itemsBag.length) {
                return;
            }
            Item item = player.itemsBag[index];
            if (item == null || !ITEM_CRAFTING.containsKey(item.template.id)) {
                player.addInfo(Player.INFO_RED, Language.ITEM_INVALID);
                return;
            }
            if (item.quantity < REQUIRE) {
                player.addInfo(Player.INFO_RED, String.format("Cần ít nhất x%d %s", REQUIRE, item.template.name));
                return;
            }
            KeyValue<Integer, Integer[]> values = ITEM_CRAFTING.get(item.template.id);
            List<Command> commands = new ArrayList<>();
            commands.add(new Command(CommandName.CONFIRM_UPGRADE, String.format("Chế tạo\n%s xu", Utils.formatNumber(values.key)), player, index));
            commands.add(new Command(CommandName.CANCEL, "Hủy", player));
            player.createMenu(NpcName.ME, String.format("Chế tạo %s", ItemManager.getInstance().itemTemplates.get(values.value[player.gender]).name), commands);
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
            if (player.isBagFull()) {
                player.addInfo(Player.INFO_RED, Language.ME_BAG_FULL);
                return;
            }
            int index = (int) objects[0];
            if (index < 0 || index >= player.itemsBag.length) {
                return;
            }
            Item item = player.itemsBag[index];
            if (item == null || !ITEM_CRAFTING.containsKey(item.template.id)) {
                player.addInfo(Player.INFO_RED, Language.ITEM_INVALID);
                return;
            }
            if (item.quantity < REQUIRE) {
                player.addInfo(Player.INFO_RED, String.format("Cần ít nhất x%d %s", REQUIRE, item.template.name));
                return;
            }
            KeyValue<Integer, Integer[]> values = ITEM_CRAFTING.get(item.template.id);
            if (player.xu < values.key) {
                player.addInfo(Player.INFO_RED, String.format("Bạn còn thiếu %s xu", Utils.getMoneys(values.key - player.xu)));
                return;
            }
            player.removeQuantityItemBag(index, REQUIRE);
            player.upXu(-values.key);
            item = ItemManager.getInstance().createItem(values.value[player.gender], 1, true);
            item.randomParam(-15, 15);
            player.addItem(item);
            player.addInfo(Player.INFO_YELLOW, String.format("Bạn nhận được %s", item.template.name));
            if (player.taskMain != null && player.taskMain.template.id == 12 && player.taskMain.index == 2) {
                player.nextTaskParam();
            }
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
            if (item != null && ITEM_CRAFTING.containsKey(item.template.id)) {
                flags[i] = true;
            }
        }
        return flags;
    }
}
