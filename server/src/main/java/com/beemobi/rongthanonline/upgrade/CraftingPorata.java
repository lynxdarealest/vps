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

public class CraftingPorata extends Upgrade {
    private static final Logger logger = Logger.getLogger(CraftingPorata.class);

    private static final int COIN = 50000000;
    private static final int QUANITY_MANH_VO = 100;
    private static final int QUANTITY_3_SA0 = 1;
    private static final int QUANTITY_NUOC_PHEP = 100;

    public CraftingPorata(UpgradeType type, String name, String command) {
        super(type, name, command);
        notes.add("- Vào túi đồ chọn Bông tai Porata cơ bản");
        notes.add("- Chọn Mảnh vỡ bông tai");
        notes.add("- Chọn Nước phép ma thuật");
        notes.add("- Chọn Ngọc rồng 3 sao");
        notes.add("- Sau đó chọn Làm phép");
        notes.add("- Lưu ý: sao pha lê đã ép sẽ được bảo toàn");
    }

    @Override
    public void upgrade(Message message, Player player) {
        player.lockAction.lock();
        try {
            int size = message.reader().readByte();
            if (size != 4) {
                player.addInfo(Player.INFO_RED, Language.ITEM_INVALID);
                return;
            }
            Item[] items = new Item[4];
            for (int i = 0; i < size; i++) {
                int index = message.reader().readByte();
                if (index < 0 || index >= player.itemsBag.length) {
                    return;
                }
                Item item = player.itemsBag[index];
                if (item == null) {
                    player.addInfo(Player.INFO_RED, Language.ITEM_INVALID);
                    return;
                }
                if (item.template.id == ItemName.BONG_TAI_PORATA) {
                    items[0] = item;
                } else if (item.template.id == ItemName.MANH_VO_BONG_TAI) {
                    items[1] = item;
                } else if (item.template.id == ItemName.NUOC_PHEP_MA_THUAT) {
                    items[2] = item;
                } else if (item.template.id == ItemName.NGOC_RONG_3_SAO) {
                    items[3] = item;
                }
            }
            if (items[0] == null || items[1] == null || items[2] == null || items[3] == null) {
                player.addInfo(Player.INFO_RED, Language.ITEM_INVALID);
                return;
            }
            StringBuilder content = new StringBuilder();
            content.append(String.format("Nâng cấp %s", items[0].template.name)).append("\n");
            content.append(String.format("Cần sử dụng x%d %s", QUANITY_MANH_VO, items[1].template.name)).append("\n");
            content.append(String.format("Cần sử dụng x%d %s", QUANTITY_NUOC_PHEP, items[2].template.name)).append("\n");
            content.append(String.format("Cần sử dụng x%d %s", QUANTITY_3_SA0, items[3].template.name)).append("\n");
            content.append("Tỉ lệ thành công 10%").append("\n");
            List<Command> commands = new ArrayList<>();
            commands.add(new Command(CommandName.CONFIRM_UPGRADE, String.format("Làm phép\n%s xu", Utils.currencyFormat(COIN)), player,
                    items[0].indexUI, items[1].indexUI, items[2].indexUI, items[3].indexUI));
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
            int[] index = new int[]{(int) objects[0], (int) objects[1], (int) objects[2], (int) objects[3]};
            Item[] items = new Item[4];
            for (int i = 0; i < items.length; i++) {
                Item item = player.itemsBag[index[i]];
                if (item == null) {
                    player.addInfo(Player.INFO_RED, Language.ITEM_INVALID);
                    return;
                }
                if (item.template.id == ItemName.BONG_TAI_PORATA) {
                    items[0] = item;
                } else if (item.template.id == ItemName.MANH_VO_BONG_TAI) {
                    items[1] = item;
                } else if (item.template.id == ItemName.NUOC_PHEP_MA_THUAT) {
                    items[2] = item;
                } else if (item.template.id == ItemName.NGOC_RONG_3_SAO) {
                    items[3] = item;
                }
            }
            if (items[0] == null || items[1] == null || items[2] == null || items[3] == null) {
                player.addInfo(Player.INFO_RED, Language.ITEM_INVALID);
                return;
            }
            if (items[1].quantity < QUANITY_MANH_VO) {
                player.addInfo(Player.INFO_RED, String.format("Bạn còn thiếu %d %s", QUANITY_MANH_VO - items[1].quantity, items[1].template.name));
                return;
            }
            if (items[2].quantity < QUANTITY_NUOC_PHEP) {
                player.addInfo(Player.INFO_RED, String.format("Bạn còn thiếu %d %s", QUANTITY_NUOC_PHEP - items[2].quantity, items[2].template.name));
                return;
            }
            if (items[3].quantity < QUANTITY_3_SA0) {
                player.addInfo(Player.INFO_RED, String.format("Bạn còn thiếu %d %s", QUANTITY_3_SA0 - items[3].quantity, items[3].template.name));
                return;
            }
            if (player.xu < COIN) {
                player.addInfo(Player.INFO_RED, "Bạn không có đủ xu");
                return;
            }
            player.upXu(-COIN);
            items[1].quantity -= QUANITY_MANH_VO;
            if (items[1].quantity <= 0) {
                player.itemsBag[items[1].indexUI] = null;
            }
            items[2].quantity -= QUANTITY_NUOC_PHEP;
            if (items[2].quantity <= 0) {
                player.itemsBag[items[2].indexUI] = null;
            }
            items[3].quantity -= QUANTITY_3_SA0;
            if (items[3].quantity <= 0) {
                player.itemsBag[items[3].indexUI] = null;
            }
            boolean isUp = Utils.isPercent(10);
            if (isUp) {
                Item item = ItemManager.getInstance().createItem(ItemName.BONG_TAI_PORATA_DAC_BIET, 1, true);
                item.indexUI = items[0].indexUI;
                for (ItemOption option : items[0].options) {
                    if (option.template.type == 4 || option.template.id == 67 || option.template.id == 68) {
                        item.options.add(new ItemOption(option.template, option.param));
                    }
                }
                item.options.add(new ItemOption(19, 1));
                player.itemsBag[item.indexUI] = item;
                player.addInfo(Player.INFO_YELLOW, "Làm phép thành công");
            } else {
                player.addInfo(Player.INFO_YELLOW, "Làm phép thất bại");
            }
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
            if (isValidItem(item)) {
                flags[i] = true;
            }
        }
        return flags;
    }

    public boolean isValidItem(Item item) {
        return item != null && (item.template.id == ItemName.BONG_TAI_PORATA
                || item.template.id == ItemName.MANH_VO_BONG_TAI
                || item.template.id == ItemName.NUOC_PHEP_MA_THUAT
                || item.template.id == ItemName.NGOC_RONG_3_SAO);
    }
}
