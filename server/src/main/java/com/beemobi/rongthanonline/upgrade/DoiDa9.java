package com.beemobi.rongthanonline.upgrade;

import com.beemobi.rongthanonline.command.Command;
import com.beemobi.rongthanonline.command.CommandName;
import com.beemobi.rongthanonline.common.Language;
import com.beemobi.rongthanonline.entity.player.Player;
import com.beemobi.rongthanonline.item.*;
import com.beemobi.rongthanonline.network.Message;
import com.beemobi.rongthanonline.npc.NpcName;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

public class DoiDa9 extends Upgrade {
    private static final Logger logger = Logger.getLogger(DoiDa9.class);

    public DoiDa9(UpgradeType type, String name, String command) {
        super(type, name, command);
        notes.add("- Vào túi đồ chọn Tam Linh Thạch");
        notes.add("- Sau đó chọn Làm phép");
        notes.add("* Lưu ý:");
        notes.add("- Cứ 10 Tam Linh Thạch sẽ đổi được 10 Đá cấp 9");
        notes.add("- Mỗi lần đổi tối thiểu 1000 viên");
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
            if (item == null || item.template.id != ItemName.TAM_LINH_THACH) {
                player.addInfo(Player.INFO_RED, Language.ITEM_INVALID);
                return;
            }
            StringBuilder content = new StringBuilder();
            content.append("Chế tạo 1000 Đá cấp 9").append("\n");
            content.append("Cần 1000 " + item.template.name).append("\n");
            content.append("Tiêu tốn 100 Kim cương").append("\n");
            content.append("Tỉ lệ thành công 100%").append("\n");
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
            if (item == null || item.template.id != ItemName.TAM_LINH_THACH) {
                player.addInfo(Player.INFO_RED, Language.ITEM_INVALID);
                return;
            }
            if (item.quantity < 1000) {
                player.addInfo(Player.INFO_RED, String.format("Bạn không đủ %s", item.template.name));
                return;
            }
            if (player.diamond < 100) {
                player.addInfo(Player.INFO_RED, "Bạn không đủ Kim cương");
                return;
            }
            player.upDiamond(-100);
            item.quantity -= 1000;
            if (item.quantity <= 0) {
                player.itemsBag[item.indexUI] = null;
            }
            Item newItem = ItemManager.getInstance().createItem(ItemName.DA_9, 1000, true);
            newItem.isLock = true;
            player.addItem(newItem, true);
            player.addInfo(Player.INFO_YELLOW, "Làm phép thành công");
            player.service.setItemBag();
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
            if (item != null && item.template.id == ItemName.TAM_LINH_THACH) {
                flags[i] = true;
            }
        }
        return flags;
    }
}
