package com.beemobi.rongthanonline.upgrade;

import com.beemobi.rongthanonline.command.Command;
import com.beemobi.rongthanonline.command.CommandName;
import com.beemobi.rongthanonline.common.Language;
import com.beemobi.rongthanonline.entity.player.Player;
import com.beemobi.rongthanonline.item.Item;
import com.beemobi.rongthanonline.item.ItemManager;
import com.beemobi.rongthanonline.item.ItemName;
import com.beemobi.rongthanonline.item.ItemOption;
import com.beemobi.rongthanonline.network.Message;
import com.beemobi.rongthanonline.npc.NpcName;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

public class DoiCapsuleHuyenBi extends Upgrade {
    private static final Logger logger = Logger.getLogger(DoiCapsuleHuyenBi.class);

    public DoiCapsuleHuyenBi(UpgradeType type, String name, String command) {
        super(type, name, command);
        notes.add("- Vào túi đồ chọn Trang bị thần linh +16 6 sao trở lên");
        notes.add("- Sau đó chọn Làm phép");
        notes.add("* Lưu ý: Capsule huyền bí nhận được sẽ cùng cấp và cùng số sao với trang bị");
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
            if (item == null || item.template.id < 337 || item.template.id > 352 || item.getUpgrade() < 16 || item.getMaxStar() < 6) {
                player.addInfo(Player.INFO_RED, Language.ITEM_INVALID);
                return;
            }
            StringBuilder content = new StringBuilder();
            content.append("Chế tạo Capsule Huyền bí").append("\n");
            content.append("Tiêu tốn 500 Kim cương").append("\n");
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
            if (item == null || item.template.id < 337 || item.template.id > 352 ) {
                player.addInfo(Player.INFO_RED, Language.ITEM_INVALID);
                return;
            }
            int upgrade = item.getUpgrade();
            if (upgrade < 16) {
                return;
            }
            int star = item.getMaxStar();
            if (star < 6) {
                return;
            }
            if (player.diamond < 500) {
                player.addInfo(Player.INFO_RED, "Bạn không đủ Kim cương");
                return;
            }
            player.itemsBag[item.indexUI] = null;
            player.upDiamond(-500);
            Item newItem = ItemManager.getInstance().createItem(ItemName.CAPSULE_HUYEN_BI, 1, true);
            newItem.isLock = item.isLock;
            while (upgrade > 0) {
                newItem.nextUpgrade();
                upgrade--;
            }
            newItem.options.add(new ItemOption(67, star));
            newItem.options.add(new ItemOption(68, star));
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
            if (item != null && item.template.id >= 337 && item.template.id <= 352 && item.getUpgrade() >= 16 && item.getMaxStar() >= 6) {
                flags[i] = true;
            }
        }
        return flags;
    }
}
