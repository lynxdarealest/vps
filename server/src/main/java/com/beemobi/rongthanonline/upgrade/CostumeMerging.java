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

public class CostumeMerging extends Upgrade {
    private static final Logger logger = Logger.getLogger(CostumeMerging.class);

    public CostumeMerging(UpgradeType type, String name, String command) {
        super(type, name, command);
        notes.add("- Vào túi đồ chọn 5 Cải trang hoặc Avatar cùng loại hoặc các vật phẩm đặc biệt");
        notes.add("- Trang bị yêu cầu không có hạn sử dụng");
        notes.add("- Sau đó chọn Ghép");
        notes.add("- Sau khi ghép sẽ nhận được 1 trang bị cùng loại có chỉ số tối đa");
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
            ItemTemplate itemTemplate = null;
            for (int i = 0; i < size; i++) {
                int index = message.reader().readByte();
                if (index < 0 || index >= player.itemsBag.length) {
                    return;
                }
                if (values.contains(index)) {
                    return;
                }
                Item item = player.itemsBag[index];
                if (!isValidItem(item)) {
                    player.addInfo(Player.INFO_RED, "Vật phẩm không hợp lệ");
                    return;
                }
                if (itemTemplate == null) {
                    itemTemplate = item.template;
                } else if (item.template.id != itemTemplate.id) {
                    player.addInfo(Player.INFO_RED, "Vật phẩm không hợp lệ");
                    return;
                }
                values.add(index);
            }
            if (values.size() != 5 || itemTemplate == null) {
                player.addInfo(Player.INFO_RED, "Số lượng vật phẩm không hợp lệ");
                return;
            }
            StringBuilder content = new StringBuilder();
            content.append(String.format("Ghép %s", itemTemplate.name)).append("\n");
            content.append(String.format("Sau khi ghép sẽ nhận được %s mới có chỉ số tối đa", itemTemplate.name)).append("\n");
            content.append("Tỉ lệ thành công 100%").append("\n");
            List<Command> commands = new ArrayList<>();
            commands.add(new Command(CommandName.CONFIRM_UPGRADE, "Ghép\n100 KC", player, values));
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
            if (player.diamond < 100) {
                player.addInfo(Player.INFO_RED, "Bạn không đủ kim cương");
                return;
            }
            ArrayList<Integer> values = (ArrayList<Integer>) objects[0];
            ItemTemplate itemTemplate = null;
            for (int index : values) {
                if (index < 0 || index >= player.itemsBag.length) {
                    return;
                }
                Item item = player.itemsBag[index];
                if (!isValidItem(item)) {
                    player.addInfo(Player.INFO_RED, "Vật phẩm không hợp lệ");
                    return;
                }
                if (itemTemplate == null) {
                    itemTemplate = item.template;
                } else if (item.template.id != itemTemplate.id) {
                    player.addInfo(Player.INFO_RED, "Vật phẩm không hợp lệ");
                    return;
                }
            }
            if (itemTemplate == null) {
                player.addInfo(Player.INFO_RED, "Vật phẩm không hợp lệ");
                return;
            }
            for (int index : values) {
                player.itemsBag[index] = null;
            }
            player.service.setItemBag();
            player.upDiamond(-100);
            Item item = ItemManager.getInstance().createItem(itemTemplate.id, true);
            item.setMaxParam();
            player.addItem(item);
            player.addInfo(Player.INFO_YELLOW, String.format("Bạn nhận được %s", itemTemplate.name));
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
        return item != null && ((item.template.type == ItemType.TYPE_AVATAR
                && item.template.id != ItemName.CAI_TRANG_HANG_NGA && item.template.id != ItemName.CAI_TRANG_QUY_LAO && item.template.id != ItemName.CAI_TRANG_THAN_VU_TRU
                && item.template.id != ItemName.CAI_TRANG_CHI_CHI && item.template.id != ItemName.CAI_TRANG_PAN_NGUYEN_DAN_DO && item.template.id != ItemName.CAI_TRANG_PAN_NGUYEN_DAN_XANH
                && item.template.id != ItemName.CAI_TRANG_ANDROID_21)
                || item.template.id == ItemName.MA_TROI_AC_QUY_HALLOWEEN_2023 || item.template.id == ItemName.MA_TROI_BI_NGO_HALLOWEEN_2023 || item.template.id == ItemName.MA_TROI_PHU_THUY_HALLOWEEN_2023
                || item.template.id == ItemName.CANH_DOI_HALLOWEEN_2023)
                && item.getExpiry() == -1;
    }
}
