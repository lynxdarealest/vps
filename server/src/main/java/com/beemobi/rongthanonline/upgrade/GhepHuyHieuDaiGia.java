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

public class GhepHuyHieuDaiGia  extends Upgrade {
    private static final Logger logger = Logger.getLogger(GhepHuyHieuDaiGia.class);

    public GhepHuyHieuDaiGia(UpgradeType type, String name, String command) {
        super(type, name, command);
        notes.add("- Vào túi đồ chọn 5 Huy hiệu Đại gia cùng loại");
        notes.add("- Trang bị yêu cầu có hạn sử dụng");
        notes.add("- Sau đó chọn Ghép");
        notes.add("- Sau khi ghép sẽ nhận Huy hiệu Đại gia vĩnh viễn");
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
            if (size != 5) {
                player.addInfo(Player.INFO_RED, "Số lượng vật phẩm không hợp lệ");
                return;
            }
            ArrayList<Integer> values = new ArrayList<>();
            Item[] items = new Item[size];
            for (int i = 0; i < size; i++) {
                int index = message.reader().readByte();
                if (index < 0 || index >= player.itemsBag.length) {
                    return;
                }
                if (values.contains(index)) {
                    return;
                }
                values.add(index);
                Item item = player.itemsBag[index];
                if (!isValidItem(item)) {
                    player.addInfo(Player.INFO_RED, "Vật phẩm không hợp lệ");
                    return;
                }
                items[i] = item;
            }
            StringBuilder content = new StringBuilder();
            content.append(String.format("Ghép %s HSD vĩnh viễn", items[0].template.name)).append("\n");
            content.append("Tỉ lệ thành công 100%").append("\n");
            List<Command> commands = new ArrayList<>();
            commands.add(new Command(CommandName.CONFIRM_UPGRADE, "Ghép\n500 KC", player, values));
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
            if (player.isBagFull()) {
                player.addInfo(Player.INFO_RED, Language.ME_BAG_FULL);
                return;
            }
            if (player.diamond < 500) {
                player.addInfo(Player.INFO_RED, "Bạn không đủ kim cương");
                return;
            }
            ArrayList<Integer> values = (ArrayList<Integer>) objects[0];
            Item[] items = new Item[5];
            for (int i = 0; i < values.size(); i++) {
                int index = values.get(i);
                if (index < 0 || index >= player.itemsBag.length) {
                    return;
                }
                Item item = player.itemsBag[index];
                if (!isValidItem(item)) {
                    player.addInfo(Player.INFO_RED, "Vật phẩm không hợp lệ");
                    return;
                }
                items[i] = item;
            }
            player.upDiamond(-500);
            for (int i = 1; i < items.length; i++) {
                player.itemsBag[items[i].indexUI] = null;
            }
            items[0].setExpiry(-1);
            items[0].setMaxParam();
            player.service.setItemBag();
            player.addInfo(Player.INFO_YELLOW, "Làm phép thành công");
            player.service.showTab(-1);
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
            if (isValidItem(item)) {
                flags[i] = true;
            }
        }
        return flags;
    }

    public boolean isValidItem(Item item) {
        return item != null && item.template.id == ItemName.HUY_HIEU_DAI_GIA && item.getExpiry() != -1
                && item.options.stream().allMatch(o -> o.template.id != 176);
    }
}
