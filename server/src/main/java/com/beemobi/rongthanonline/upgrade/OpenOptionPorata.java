package com.beemobi.rongthanonline.upgrade;

import com.beemobi.rongthanonline.command.Command;
import com.beemobi.rongthanonline.command.CommandName;
import com.beemobi.rongthanonline.common.Language;
import com.beemobi.rongthanonline.entity.player.Player;
import com.beemobi.rongthanonline.item.Item;
import com.beemobi.rongthanonline.item.ItemName;
import com.beemobi.rongthanonline.item.ItemOption;
import com.beemobi.rongthanonline.network.Message;
import com.beemobi.rongthanonline.npc.NpcName;
import com.beemobi.rongthanonline.util.Utils;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class OpenOptionPorata extends Upgrade {
    private static final Logger logger = Logger.getLogger(OpenOptionPorata.class);

    private static final int DIAMOND = 100;
    private static final int QUANTITY_3_SA0 = 1;
    private static final int QUANTITY_NUOC_PHEP = 10;

    public OpenOptionPorata(UpgradeType type, String name, String command) {
        super(type, name, command);
        notes.add("- Vào túi đồ chọn Bông tai Porata đã nâng cấp");
        notes.add("- Chọn Nước phép ma thuật");
        notes.add("- Chọn Ngọc rồng 3 sao");
        notes.add("- Sau đó chọn Làm phép");
        notes.add("- Lưu ý: chỉ số sẽ được tăng theo cấp của Bông tai");
    }

    @Override
    public void upgrade(Message message, Player player) {
        player.lockAction.lock();
        try {
            int size = message.reader().readByte();
            if (size != 3) {
                player.addInfo(Player.INFO_RED, Language.ITEM_INVALID);
                return;
            }
            Item[] items = new Item[3];
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
                if (item.template.id == ItemName.BONG_TAI_PORATA_DAC_BIET) {
                    items[0] = item;
                } else if (item.template.id == ItemName.NUOC_PHEP_MA_THUAT) {
                    items[1] = item;
                } else if (item.template.id == ItemName.NGOC_RONG_3_SAO) {
                    items[2] = item;
                }
            }
            if (items[0] == null || items[1] == null || items[2] == null) {
                player.addInfo(Player.INFO_RED, Language.ITEM_INVALID);
                return;
            }
            StringBuilder content = new StringBuilder();
            content.append(String.format("Nâng cấp %s", items[0].template.name)).append("\n");
            content.append(String.format("Cần sử dụng x%d %s", QUANTITY_NUOC_PHEP, items[1].template.name)).append("\n");
            content.append(String.format("Cần sử dụng x%d %s", QUANTITY_3_SA0, items[2].template.name)).append("\n");
            content.append("Tỉ lệ thành công 100%").append("\n");
            content.append("Sau khi làm phép thành công sẽ có 5 chỉ số ngẫu nhiên").append("\n");
            List<Command> commands = new ArrayList<>();
            commands.add(new Command(CommandName.CONFIRM_UPGRADE, String.format("Làm phép\n%s KC", Utils.currencyFormat(DIAMOND)), player,
                    items[0].indexUI, items[1].indexUI, items[2].indexUI));
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
            int[] index = new int[]{(int) objects[0], (int) objects[1], (int) objects[2]};
            Item[] items = new Item[3];
            for (int i = 0; i < items.length; i++) {
                Item item = player.itemsBag[index[i]];
                if (item == null) {
                    player.addInfo(Player.INFO_RED, Language.ITEM_INVALID);
                    return;
                }
                if (item.template.id == ItemName.BONG_TAI_PORATA_DAC_BIET) {
                    items[0] = item;
                } else if (item.template.id == ItemName.NUOC_PHEP_MA_THUAT) {
                    items[1] = item;
                } else if (item.template.id == ItemName.NGOC_RONG_3_SAO) {
                    items[2] = item;
                }
            }
            if (items[0] == null || items[1] == null || items[2] == null) {
                player.addInfo(Player.INFO_RED, Language.ITEM_INVALID);
                return;
            }
            if (items[1].quantity < QUANTITY_NUOC_PHEP) {
                player.addInfo(Player.INFO_RED, String.format("Bạn còn thiếu %d %s", QUANTITY_NUOC_PHEP - items[1].quantity, items[2].template.name));
                return;
            }
            if (items[2].quantity < QUANTITY_3_SA0) {
                player.addInfo(Player.INFO_RED, String.format("Bạn còn thiếu %d %s", QUANTITY_3_SA0 - items[2].quantity, items[3].template.name));
                return;
            }
            if (player.diamond < DIAMOND) {
                player.addInfo(Player.INFO_RED, "Bạn không có đủ Kim cương");
                return;
            }
            player.upDiamond(-DIAMOND);
            items[1].quantity -= QUANTITY_NUOC_PHEP;
            if (items[1].quantity <= 0) {
                player.itemsBag[items[1].indexUI] = null;
            }
            items[2].quantity -= QUANTITY_3_SA0;
            if (items[2].quantity <= 0) {
                player.itemsBag[items[2].indexUI] = null;
            }
            HashMap<Integer, Integer[]> params = new HashMap<>();
            params.put(0, new Integer[]{500, 1000});
            params.put(1, new Integer[]{500, 1000});
            params.put(2, new Integer[]{1000, 2000});
            params.put(3, new Integer[]{500, 1000});
            params.put(4, new Integer[]{500, 1000});
            params.put(5, new Integer[]{500, 1000});
            params.put(6, new Integer[]{500, 1000});
            params.put(25, new Integer[]{5, 10});
            params.put(26, new Integer[]{500, 1000});
            params.put(27, new Integer[]{1000, 2000});
            params.put(28, new Integer[]{500, 1000});
            params.put(29, new Integer[]{500, 1000});
            params.put(30, new Integer[]{500, 1000});
            params.put(31, new Integer[]{5, 10});
            params.put(32, new Integer[]{5, 10});
            params.put(99, new Integer[]{1000, 2000});
            items[0].options.removeIf(o -> params.containsKey(o.template.id));
            Integer[] options = params.keySet().toArray(new Integer[0]);
            Integer[] array = Utils.nextInt(options, 5);
            for (int value : array) {
                int min = params.get(value)[0];
                int max = params.get(value)[1];
                items[0].options.add(new ItemOption(value, Utils.nextInt(min, max)));
            }
            int upgrade = items[0].getUpgrade();
            if (upgrade > 1) {
                for (ItemOption option : items[0].options) {
                    if (option.template.type == 4) {
                        continue;
                    }
                    int id = option.template.id;
                    if (id == 19 || id == 33 || id == 34 || id == 35 || id == 67 || id == 68) {
                        continue;
                    }
                    if (id == 25 || id == 31 || id == 32) {
                        option.param += 2 * (upgrade - 1);
                    } else {
                        for (int i = 1; i < upgrade; i++) {
                            option.param += option.param / 10;
                        }
                    }
                }
            }
            player.service.setItemBag();
            player.addInfo(Player.INFO_YELLOW, "Làm phép thành công");
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
        return item != null && (item.template.id == ItemName.BONG_TAI_PORATA_DAC_BIET
                || item.template.id == ItemName.NUOC_PHEP_MA_THUAT
                || item.template.id == ItemName.NGOC_RONG_3_SAO);
    }
}
