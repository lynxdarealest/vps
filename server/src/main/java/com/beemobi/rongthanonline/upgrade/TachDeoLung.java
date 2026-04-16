package com.beemobi.rongthanonline.upgrade;

import com.beemobi.rongthanonline.command.Command;
import com.beemobi.rongthanonline.command.CommandName;
import com.beemobi.rongthanonline.entity.player.Player;
import com.beemobi.rongthanonline.item.*;
import com.beemobi.rongthanonline.network.Message;
import com.beemobi.rongthanonline.npc.NpcName;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

public class TachDeoLung extends Upgrade {
    private static final Logger logger = Logger.getLogger(TachDeoLung.class);

    public TachDeoLung(UpgradeType type, String name, String command) {
        super(type, name, command);
        notes.add("- Vào túi đồ chọn Vật phẩm đeo lưng muốn tách");
        notes.add("- Sau đó chọn Tách");
        notes.add("(!) Lưu ý: chỉ có thể tách Vật phẩm đeo lưng đã ghép");
    }

    @Override
    public void upgrade(Message message, Player player) {
        player.lockAction.lock();
        try {
            int size = message.reader().readByte();
            if (size != 1) {
                player.addInfo(Player.INFO_RED, "Số lượng vật phẩm không hợp lệ");
                return;
            }
            int index = message.reader().readByte();
            if (index < 0 || index >= player.itemsBag.length) {
                return;
            }
            Item item = player.itemsBag[index];
            if (!isValidItem(item)) {
                player.addInfo(Player.INFO_RED, "Vật phẩm không hợp lệ");
                return;
            }
            StringBuilder content = new StringBuilder();
            content.append(String.format("Tách %s sẽ nhận được:", item.template.name)).append("\n");
            for (ItemOption option : item.options) {
                if (option.template.id == 176) {
                    content.append(ItemManager.getInstance().itemTemplates.get(option.param).name).append("\n");
                }
            }
            content.append("Tỉ lệ thành công 100%").append("\n");
            List<Command> commands = new ArrayList<>();
            commands.add(new Command(CommandName.CONFIRM_UPGRADE, String.format("Tách\n%d KC", 200), player, index));
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
            int index = (int) objects[0];
            if (index < 0 || index >= player.itemsBag.length) {
                return;
            }
            Item item = player.itemsBag[index];
            if (!isValidItem(item)) {
                player.addInfo(Player.INFO_RED, "Vật phẩm không hợp lệ");
                return;
            }
            int count = (int) item.options.stream().filter(o -> o.template.id == 176).count();
            if (player.getCountItemBagEmpty() < count) {
                player.addInfo(Player.INFO_RED, String.format("Cần ít nhất %d ô trống trong túi đồ", count));
                return;
            }
            int diamond = 200;
            if (player.diamond < diamond) {
                player.addInfo(Player.INFO_RED, "Bạn không có đủ Kim cương");
                return;
            }
            player.upDiamond(-diamond);
            List<Item> items = new ArrayList<>();
            int maxStar = item.getParam(67);
            item.options.removeIf(o -> o.template.type == 4 || o.template.id == 68 || o.template.id == 67);
            while (item.options.stream().anyMatch(o -> o.template.id == 176)) {
                List<ItemOption> removes = new ArrayList<>();
                for (int i = 0; i < item.options.size(); i++) {
                    ItemOption option = item.options.get(i);
                    if (option.template.id == 176) {
                        ItemTemplate template = ItemManager.getInstance().itemTemplates.get(option.param);
                        if (template != null) {
                            Item newItem = ItemManager.getInstance().createItem(template.id, 1, false);
                            for (int j = i + 1; j < item.options.size(); j++) {
                                ItemOption itemOption = item.options.get(j);
                                if (itemOption.template.id == 176) {
                                    break;
                                }
                                newItem.options.add(new ItemOption(itemOption.template, itemOption.param));
                                removes.add(itemOption);
                            }
                            items.add(newItem);
                        }
                        removes.add(option);
                        break;
                    }
                }
                item.options.removeAll(removes);
            }
            if (maxStar > 0) {
                item.options.add(new ItemOption(67, maxStar));
                item.options.add(new ItemOption(68, 0));
            }
            if (!items.isEmpty()) {
                for (Item newItem : items) {
                    player.addItem(newItem);
                }
            }
            player.service.refreshItemBag(index);
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
        return item != null && item.template.type == ItemType.TYPE_BAG && item.getExpiry() == -1 && item.options.stream().anyMatch(o -> o.template.id == 176);
    }
}
