package com.beemobi.rongthanonline.upgrade;

import com.beemobi.rongthanonline.command.Command;
import com.beemobi.rongthanonline.command.CommandName;
import com.beemobi.rongthanonline.common.Language;
import com.beemobi.rongthanonline.entity.player.Player;
import com.beemobi.rongthanonline.item.*;
import com.beemobi.rongthanonline.network.Message;
import com.beemobi.rongthanonline.npc.NpcName;
import com.beemobi.rongthanonline.util.Utils;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

public class DoiTrangBiPet extends Upgrade {
    private static final Logger logger = Logger.getLogger(DoiTrangBiPet.class);

    public DoiTrangBiPet(UpgradeType type, String name, String command) {
        super(type, name, command);
        notes.add("- Vào túi đồ chọn Trang bị Pet +16 trở lên");
        notes.add("- Sau đó chọn Làm phép");
        notes.add("- Sau khi làm phép sẽ nhận được 1 Trang bị Pet ngẫu nhiên có cùng chỉ số với trang bị ban đầu");
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
                player.addInfo(Player.INFO_RED, Language.ITEM_INVALID);
                return;
            }
            int index = message.reader().readByte();
            if (index < 0 || index >= player.itemsBag.length) {
                return;
            }
            Item item = player.itemsBag[index];
            if (item == null || !item.template.isPet || item.getUpgrade() < 16) {
                player.addInfo(Player.INFO_RED, Language.ITEM_INVALID);
                return;
            }
            StringBuilder content = new StringBuilder();
            content.append("Đổi Trang bị Pet").append("\n");
            content.append("Tiêu tốn 200 Kim cương").append("\n");
            content.append("Tỉ lệ thành công 100%").append("\n");
            content.append("Sau khi làm phép sẽ nhận được 1 trang bị Pet bất kì (không trùng lặp với trang bị ban đầu)").append("\n");
            List<Command> commands = new ArrayList<>();
            commands.add(new Command(CommandName.CONFIRM_UPGRADE, "Làm phép", player, index));
            commands.add(new Command(CommandName.CANCEL, "Từ chối", player));
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
            if (player.isBagFull()) {
                player.addInfo(Player.INFO_RED, Language.ME_BAG_FULL);
                return;
            }
            int index = (int) objects[0];
            Item item = player.itemsBag[index];
            if (item == null || !item.template.isPet) {
                player.addInfo(Player.INFO_RED, Language.ITEM_INVALID);
                return;
            }
            int upgrade = item.getUpgrade();
            if (upgrade < 16) {
                return;
            }
            if (player.diamond < 200) {
                player.addInfo(Player.INFO_RED, "Bạn không đủ Kim cương");
                return;
            }
            player.itemsBag[item.indexUI] = null;
            player.upDiamond(-200);
            List<ItemTemplate> templates = ItemManager.getInstance().itemTemplates.values().stream().filter(i -> i.isPet && i.id != item.template.id).toList();
            Item newItem = ItemManager.getInstance().createItem(templates.get(Utils.nextInt(templates.size())).id, 1, false);
            newItem.isLock = item.isLock;
            for (ItemOption option : item.options) {
                newItem.options.add(new ItemOption(option.template, option.param));
            }
            newItem.indexUI = item.indexUI;
            player.itemsBag[newItem.indexUI] = newItem;
            player.addInfo(Player.INFO_YELLOW, "Làm phép thành công");
            player.service.refreshItemBag(newItem.indexUI);
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
            if (item != null && item.template.isPet && item.getUpgrade() >= 16) {
                flags[i] = true;
            }
        }
        return flags;
    }
}
