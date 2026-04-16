package com.beemobi.rongthanonline.upgrade;

import com.beemobi.rongthanonline.command.Command;
import com.beemobi.rongthanonline.command.CommandName;
import com.beemobi.rongthanonline.common.Language;
import com.beemobi.rongthanonline.entity.player.Player;
import com.beemobi.rongthanonline.entity.player.PlayerManager;
import com.beemobi.rongthanonline.item.Item;
import com.beemobi.rongthanonline.item.ItemManager;
import com.beemobi.rongthanonline.item.ItemName;
import com.beemobi.rongthanonline.item.ItemOption;
import com.beemobi.rongthanonline.network.Message;
import com.beemobi.rongthanonline.npc.NpcName;
import com.beemobi.rongthanonline.server.Server;
import com.beemobi.rongthanonline.shop.TypePrice;
import com.beemobi.rongthanonline.util.Utils;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

public class UpgradePorata extends Upgrade {
    private static final Logger logger = Logger.getLogger(UpgradePorata.class);

    private static final int COIN = 50000000;
    private static final int DIAMOND = 5;
    private static final int QUANITY_MANH_VO = 10;
    private static final int QUANTITY_3_SA0 = 1;
    private static final int QUANTITY_NUOC_PHEP = 10;
    private static final int QUANTITY_DA_HO_PHACH = 10;
    private static final int QUANTITY_TAM_LINH_THACH = 10;

    public UpgradePorata(UpgradeType type, String name, String command) {
        super(type, name, command);
        notes.add("- Vào túi đồ chọn Bông tai Porata đặc biệt");
        notes.add("- Chọn Mảnh vỡ bông tai");
        notes.add("- Chọn Nước phép ma thuật");
        notes.add("- Chọn Ngọc rồng 3 sao");
        notes.add("- Chọn Đá Hổ phách");
        notes.add("- Chọn Tam Linh Thạch");
        notes.add("- Sau đó chọn Làm phép");
        notes.add("(!) Lưu ý:");
        notes.add("- Sao pha lê đã ép sẽ được bảo toàn");
        notes.add("- Chỉ có thể nâng tối đa 19 cấp");
        notes.add("- Nâng cấp sẽ tăng 10-20% chỉ số hiện tại của Bông tai");
        notes.add("- Tại các mốc cấp độ 4, 9, 14, 19 sẽ nhận thêm option huyền thoại");
    }

    @Override
    public void upgrade(Message message, Player player) {
        player.lockAction.lock();
        try {
            int size = message.reader().readByte();
            if (size != 6) {
                player.addInfo(Player.INFO_RED, Language.ITEM_INVALID);
                return;
            }
            Item[] items = new Item[6];
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
                } else if (item.template.id == ItemName.MANH_VO_BONG_TAI) {
                    items[1] = item;
                } else if (item.template.id == ItemName.NUOC_PHEP_MA_THUAT) {
                    items[2] = item;
                } else if (item.template.id == ItemName.NGOC_RONG_3_SAO) {
                    items[3] = item;
                } else if (item.template.id == ItemName.DA_HO_PHACH) {
                    items[4] = item;
                } else if (item.template.id == ItemName.TAM_LINH_THACH) {
                    items[5] = item;
                }
            }
            if (items[0] == null || items[1] == null || items[2] == null || items[3] == null || items[4] == null || items[5] == null) {
                player.addInfo(Player.INFO_RED, Language.ITEM_INVALID);
                return;
            }
            int upgrade = items[0].getUpgrade();
            if (upgrade >= 19) {
                player.addInfo(Player.INFO_RED, Language.ITEM_MAX_UPGRADE);
                return;
            }
            StringBuilder content = new StringBuilder();
            content.append(String.format("Nâng cấp %s [+%d]", items[0].template.name, upgrade + 1)).append("\n");
            content.append(String.format("Cần sử dụng %d %s", QUANITY_MANH_VO, items[1].template.name)).append("\n");
            content.append(String.format("Cần sử dụng %d %s", QUANTITY_NUOC_PHEP, items[2].template.name)).append("\n");
            content.append(String.format("Cần sử dụng %d %s", QUANTITY_3_SA0, items[3].template.name)).append("\n");
            content.append(String.format("Cần sử dụng %d %s", QUANTITY_DA_HO_PHACH, items[4].template.name)).append("\n");
            content.append(String.format("Cần sử dụng %d %s", QUANTITY_TAM_LINH_THACH, items[5].template.name)).append("\n");
            content.append(String.format("Cần sử dụng %s Xu khóa", Utils.getMoneys(COIN))).append("\n");
            content.append("Tỉ lệ thành công 5%").append("\n");
            List<Command> commands = new ArrayList<>();
            commands.add(new Command(CommandName.CONFIRM_UPGRADE, String.format("Làm phép\n%s KC", Utils.currencyFormat(DIAMOND)), player,
                    items[0].indexUI, items[1].indexUI, items[2].indexUI, items[3].indexUI, items[4].indexUI, items[5].indexUI));
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
            int[] index = new int[]{(int) objects[0], (int) objects[1], (int) objects[2], (int) objects[3], (int) objects[4], (int) objects[5]};
            Item[] items = new Item[6];
            for (int i = 0; i < items.length; i++) {
                Item item = player.itemsBag[index[i]];
                if (item == null) {
                    player.addInfo(Player.INFO_RED, Language.ITEM_INVALID);
                    return;
                }
                if (item.template.id == ItemName.BONG_TAI_PORATA_DAC_BIET) {
                    items[0] = item;
                } else if (item.template.id == ItemName.MANH_VO_BONG_TAI) {
                    items[1] = item;
                } else if (item.template.id == ItemName.NUOC_PHEP_MA_THUAT) {
                    items[2] = item;
                } else if (item.template.id == ItemName.NGOC_RONG_3_SAO) {
                    items[3] = item;
                } else if (item.template.id == ItemName.DA_HO_PHACH) {
                    items[4] = item;
                } else if (item.template.id == ItemName.TAM_LINH_THACH) {
                    items[5] = item;
                }
            }
            if (items[0] == null || items[1] == null || items[2] == null || items[3] == null || items[4] == null || items[5] == null) {
                player.addInfo(Player.INFO_RED, Language.ITEM_INVALID);
                return;
            }
            int upgrade = items[0].getUpgrade();
            if (upgrade >= 19) {
                player.addInfo(Player.INFO_RED, Language.ITEM_MAX_UPGRADE);
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
            if (items[4].quantity < QUANTITY_DA_HO_PHACH) {
                player.addInfo(Player.INFO_RED, String.format("Bạn còn thiếu %d %s", QUANTITY_DA_HO_PHACH - items[4].quantity, items[4].template.name));
                return;
            }
            if (items[5].quantity < QUANTITY_TAM_LINH_THACH) {
                player.addInfo(Player.INFO_RED, String.format("Bạn còn thiếu %d %s", QUANTITY_TAM_LINH_THACH - items[5].quantity, items[5].template.name));
                return;
            }
            if (!player.isEnoughMoney(TypePrice.COIN_LOCK, COIN)) {
                return;
            }
            if (player.diamond < DIAMOND) {
                player.addInfo(Player.INFO_RED, "Bạn không có đủ Kim cương");
                return;
            }
            player.downMoney(TypePrice.COIN_LOCK, COIN);
            player.upDiamond(-DIAMOND);
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
            items[4].quantity -= QUANTITY_DA_HO_PHACH;
            if (items[4].quantity <= 0) {
                player.itemsBag[items[4].indexUI] = null;
            }
            items[5].quantity -= QUANTITY_TAM_LINH_THACH;
            if (items[5].quantity <= 0) {
                player.itemsBag[items[5].indexUI] = null;
            }
           /* params.put(0, new Integer[]{500, 1000});
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
            params.put(99, new Integer[]{1000, 2000});*/
            boolean isUp = Utils.isPercent(upgrade < 10 ? 5 : 4);
            if (isUp) {
                for (ItemOption option : items[0].options) {
                    if (option.template.type == 4) {
                        continue;
                    }
                    int id = option.template.id;
                    if (id == 67 || id == 68) {
                        continue;
                    }
                    if (id == 19) {
                        option.param++;
                    } else if (id == 25 || id == 31 || id == 32) {
                        option.param += 2;
                    } else if (id == 33 || id == 34 || id == 35) {
                        if (upgrade == 8 || upgrade == 13 || upgrade == 18) {
                            option.param += 1;
                        }
                    } else {
                        option.param += option.param / 10;
                    }
                }
                if (upgrade == 3) {
                    items[0].options.add(new ItemOption(33, 1));
                    items[0].options.add(new ItemOption(34, 1));
                    items[0].options.add(new ItemOption(35, 1));
                }
                player.addInfo(Player.INFO_YELLOW, "Làm phép thành công");
                Server.getInstance().service.serverChat("Chúc mừng " + player.name + " đã cường hóa thành công " + items[0].template.name + " lên +" + (upgrade + 1));
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
        return item != null && (item.template.id == ItemName.BONG_TAI_PORATA_DAC_BIET
                || item.template.id == ItemName.MANH_VO_BONG_TAI
                || item.template.id == ItemName.NUOC_PHEP_MA_THUAT
                || item.template.id == ItemName.NGOC_RONG_3_SAO
                || item.template.id == ItemName.TAM_LINH_THACH
                || item.template.id == ItemName.DA_HO_PHACH);
    }
}
