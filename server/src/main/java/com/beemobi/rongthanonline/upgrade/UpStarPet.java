package com.beemobi.rongthanonline.upgrade;

import com.beemobi.rongthanonline.command.Command;
import com.beemobi.rongthanonline.command.CommandName;
import com.beemobi.rongthanonline.entity.player.Player;
import com.beemobi.rongthanonline.item.Item;
import com.beemobi.rongthanonline.item.ItemName;
import com.beemobi.rongthanonline.item.ItemType;
import com.beemobi.rongthanonline.network.Message;
import com.beemobi.rongthanonline.npc.NpcName;
import com.beemobi.rongthanonline.server.Server;
import com.beemobi.rongthanonline.shop.TypePrice;
import com.beemobi.rongthanonline.util.Utils;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

public class UpStarPet extends Upgrade {

    private static final Logger logger = Logger.getLogger(Crystallize.class);
    private static final int[] XU = new int[]{500000, 1000000, 2000000, 3500000, 5500000, 8000000, 11000000, 15000000};

    public static final int[] QUANTITY = new int[]{50, 60, 70, 80, 90, 100, 110, 120};

    public static final double[] PERCENT = new double[]{50, 30, 20, 10, 5, 3, 1, 0.5};

    public static final int MAX_STAR = 8;

    public UpStarPet(UpgradeType type, String name, String command) {
        super(type, name, command);
        notes.add("- Vào túi đồ chọn Pet cần nâng sao");
        notes.add("- Sau đó chọn Tinh thạch");
        notes.add("- Chỉ có thể nâng sao cho Pet cấp 16");
        notes.add("- Sau đó chọn Làm phép");
    }

    @Override
    public void upgrade(Message message, Player player) {
        player.lockAction.lock();
        try {
            int size = message.reader().readByte();
            if (size != 2) {
                player.addInfo(Player.INFO_RED, "Vật phẩm làm phép không hợp lệ");
                return;
            }
            Item[] items = new Item[2];
            for (int i = 0; i < size; i++) {
                int index = message.reader().readByte();
                if (index < 0 || index >= player.itemsBag.length) {
                    return;
                }
                Item item = player.itemsBag[index];
                if (item == null) {
                    return;
                }
                if (item.template.type == ItemType.TYPE_PET) {
                    items[0] = item;
                } else if (item.template.id == ItemName.TINH_THACH) {
                    items[1] = item;
                } else {
                    player.addInfo(Player.INFO_RED, "Vật phẩm làm phép không hợp lệ");
                    return;
                }
            }
            if (items[0] == null || items[1] == null || items[0].getUpgrade() < 16) {
                player.addInfo(Player.INFO_RED, "Vật phẩm làm phép không hợp lệ");
                return;
            }
            int star = items[0].getParam(67);
            if (star >= MAX_STAR) {
                player.addInfo(Player.INFO_RED, "Pet đã đạt cấp tối đa");
                return;
            }
            StringBuilder chat = new StringBuilder();
            chat.append(String.format("Nâng %s lên %d sao", items[0].template.name, star + 1)).append("\n");
            chat.append(String.format("Cần %d %s", QUANTITY[star], items[1].template.name)).append("\n");
            chat.append(String.format("Cần %s xu khóa", Utils.getMoneys(XU[star]))).append("\n");
            chat.append(String.format("Tỉ lệ thành công %s%%", PERCENT[star])).append("\n");
            chat.append("Sau khi làm phép thành công Pet sẽ về lại cấp 1").append("\n");
            chat.append("Pet vẫn giữ nguyên cấp độ nếu làm phép thất bại").append("\n");
            List<Command> commands = new ArrayList<>();
            commands.add(new Command(CommandName.CONFIRM_UPGRADE, "Làm phép", player, items[0].indexUI, items[1].indexUI));
            commands.add(new Command(CommandName.CANCEL, "Hủy", player));
            player.createMenu(NpcName.ME, chat.toString(), commands);
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
            int[] index = new int[]{(int) objects[0], (int) objects[1]};
            if (index[0] < 0 || index[0] >= player.itemsBag.length || index[1] < 0 || index[1] >= player.itemsBag.length) {
                return;
            }
            Item[] items = new Item[]{player.itemsBag[index[0]], player.itemsBag[index[1]]};
            if (items[0] == null || items[1] == null || items[0].template.type != ItemType.TYPE_PET || items[1].template.id != ItemName.TINH_THACH || items[0].getUpgrade() < 16) {
                player.addInfo(Player.INFO_RED, "Vật phẩm làm phép không hợp lệ");
                return;
            }
            int star = items[0].getParam(67);
            if (star >= MAX_STAR) {
                player.addInfo(Player.INFO_RED, "Pet đã đạt cấp tối đa");
                return;
            }
            long coin = XU[star];
            if (!player.isEnoughMoney(TypePrice.COIN_LOCK, coin)) {
                return;
            }
            if (items[1].quantity < QUANTITY[star]) {
                player.addInfo(Player.INFO_RED, String.format("Bạn không có đủ %s", items[1].template.name));
                return;
            }
            player.downMoney(TypePrice.COIN_LOCK, coin);
            items[1].quantity -= QUANTITY[star];
            if (items[1].quantity < 1) {
                player.itemsBag[items[1].indexUI] = null;
            }
            boolean isUp = Utils.nextInt(1000) < (int) (PERCENT[star] * 10);
            if (isUp) {
                items[0].nextStarPet();
                player.addInfo(Player.INFO_YELLOW, String.format("Làm phép thành công, %s đã được nâng lên %d sao", items[0].template.name, star + 1));
                if (star >= 4) {
                    Server.getInstance().service.serverChat(String.format("Chúc mừng %s đã nâng thành công %s lên %d sao", player.name, items[0].template.name, star + 1));
                }
            } else {
                player.addInfo(Player.INFO_RED, "Làm phép thất bại");
            }
            player.service.setItemBag();
        } catch (Exception ex) {
            logger.error("upgrade", ex);
        } finally {
            player.lockAction.unlock();
        }
    }

    @Override
    public boolean[] getIndexCanUpgrade(Player player) {
        boolean[] flags = new boolean[player.itemsBag.length];
        for (int i = 0; i < flags.length; i++) {
            Item item = player.itemsBag[i];
            if (item != null && (item.template.type == ItemType.TYPE_PET || item.template.id == ItemName.TINH_THACH)) {
                flags[i] = true;
            }
        }
        return flags;
    }
}
