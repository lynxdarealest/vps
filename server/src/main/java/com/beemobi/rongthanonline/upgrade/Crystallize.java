package com.beemobi.rongthanonline.upgrade;

import com.beemobi.rongthanonline.command.Command;
import com.beemobi.rongthanonline.command.CommandName;
import com.beemobi.rongthanonline.common.Language;
import com.beemobi.rongthanonline.item.ItemType;
import com.beemobi.rongthanonline.npc.NpcName;
import com.beemobi.rongthanonline.entity.player.Player;
import com.beemobi.rongthanonline.item.Item;
import com.beemobi.rongthanonline.item.ItemOption;
import com.beemobi.rongthanonline.network.Message;
import com.beemobi.rongthanonline.server.Server;
import com.beemobi.rongthanonline.shop.TypePrice;
import com.beemobi.rongthanonline.util.Utils;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Crystallize extends Upgrade {

    private static final Logger logger = Logger.getLogger(Crystallize.class);

    public static final int[] XU_CRYSTAL_ITEM = new int[]{1000000, 5000000, 10000000, 20000000, 40000000, 60000000, 90000000, 120000000};

    public static final double[] PERCENT_CRYSTAL_ITEM = new double[]{80, 50, 30, 20, 10, 5, 3, 1};

    public static HashMap<String, HashMap<Integer, Integer>> players = new HashMap<>();

    public Crystallize(UpgradeType type, String name, String command) {
        super(type, name, command);
        notes.add("- Vào túi đồ chọn trang bị cần Pha Lê hóa");
        notes.add("- Trang bị nếu là trang phục hoặc trang sức sẽ yêu cầu từ cấp 8 trở lên mới có thể Pha Lê hóa");
        notes.add("- Sau đó chọn Pha Lê hóa");
        notes.add("* Lưu ý:");
        notes.add("- Nếu không đủ Xu khóa sẽ trừ Xu");
        notes.add("- Nếu không đủ Ruby sẽ trừ Kim cương");
        notes.add("- Vật phẩm sẽ khóa khi sử dụng Xu khóa hoặc Ruby");
    }

    @Override
    public void upgrade(Message message, Player player) {
        player.lockAction.lock();
        try {
            int size = message.reader().readByte();
            if (size != 1) {
                player.addInfo(Player.INFO_RED, "Chỉ được chọn duy nhất 1 vật phẩm");
                return;
            }
            int index = message.reader().readByte();
            if (index < 0 || index >= player.itemsBag.length) {
                return;
            }
            Item item = player.itemsBag[index];
            if (item == null || !item.isItemBody() || item.template.type == ItemType.TYPE_PET) {
                player.addInfo(Player.INFO_RED, Language.ITEM_INVALID);
                return;
            }
            if (item.template.type < 8 && item.getUpgrade() < 8) {
                player.addInfo(Player.INFO_RED, "Yêu cầu trang bị từ cấp 8 trở lên");
                return;
            }
            int upgrade = item.getParam(67);
            if (upgrade >= 8) {
                player.addInfo(Player.INFO_RED, Language.ITEM_MAX_UPGRADE);
                return;
            }
            int diamond = upgrade + 1;
            double percent = PERCENT_CRYSTAL_ITEM[upgrade];
            if (player.options[135] > 0) {
                percent += percent * (double) player.options[135] / 100;
            }
            String chat = "Pha lê hóa: " + item.template.name + "\n" +
                    "Pha lê: " + diamond + " ô\n" +
                    "Tỉ lệ thành công: " + percent + "%\n" +
                    "Tiêu hao: " + Utils.formatNumber(XU_CRYSTAL_ITEM[upgrade]) + " Xu khóa và " + diamond + " Ruby";
            List<Command> commands = new ArrayList<>();
            commands.add(new Command(CommandName.CONFIRM_UPGRADE, "Đồng ý(Xu + KC)", player, index, 0));
            commands.add(new Command(CommandName.CONFIRM_UPGRADE, "Đồng ý\n(Xu khóa + Ruby)", player, index, 1));
            commands.add(new Command(CommandName.CANCEL, "Hủy", player, index));
            player.createMenu(NpcName.ME, chat, commands);
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
            if (item == null || !item.isItemBody() || item.template.type == ItemType.TYPE_PET) {
                player.addInfo(Player.INFO_RED, Language.ITEM_INVALID);
                return;
            }
            if (item.template.type < 8 && item.getUpgrade() < 8) {
                player.addInfo(Player.INFO_RED, "Yêu cầu trang bị từ cấp 8 trở lên");
                return;
            }
            int upgrade = item.getParam(67);
            if (upgrade >= 8) {
                player.addInfo(Player.INFO_RED, Language.ITEM_MAX_UPGRADE);
                return;
            }
            int xu = XU_CRYSTAL_ITEM[upgrade];
            int type = (int) objects[1];
            if ((type == 1 && !player.isEnoughMoney(TypePrice.COIN_LOCK, xu)) || (type == 0 && !player.isEnoughMoney(TypePrice.COIN, xu))) {
                return;
            }
            int diamond = upgrade + 1;
            if ((type == 1 && !player.isEnoughMoney(TypePrice.RUBY, diamond)) || (type == 0 && !player.isEnoughMoney(TypePrice.DIAMOND, diamond))) {
                return;
            }
            if (type == 1) {
                player.downMoney(TypePrice.COIN_LOCK, xu);
                player.downMoney(TypePrice.RUBY, diamond);
            } else {
                player.downMoney(TypePrice.COIN, xu);
                player.downMoney(TypePrice.DIAMOND, diamond);
            }
            int percent = (int) (PERCENT_CRYSTAL_ITEM[upgrade] * 10);
            if (upgrade > 2) {
                percent /= 2;
            }
            if (player.options[135] > 0) {
                percent += percent * player.options[135] / 100;
            }
            boolean isUp = Utils.nextInt(1000) < percent;
            // buff
            HashMap<Integer, Integer> players = Crystallize.players.getOrDefault(player.name, null);
            if (players != null) {
                int count = players.getOrDefault(upgrade + 1, -1);
                if (count != -1) {
                    if (count <= 1) {
                        isUp = true;
                        players.remove(upgrade + 1);
                    } else {
                        isUp = false;
                        players.put(upgrade + 1, --count);
                    }
                }
            }
            if (isUp) {
                if (type == 1) {
                    item.isLock = true;
                }
                if (upgrade == 0) {
                    item.options.add(new ItemOption(67, 0));
                    item.options.add(new ItemOption(68, 0));
                }
                ItemOption option = item.getOption(67);
                option.param += 1;
                player.service.refreshItemBag(index);
                player.addInfo(Player.INFO_YELLOW, "Pha lê hóa thành công");
                if (option.param >= 5) {
                    Server.getInstance().service.serverChat(String.format("Chúc mừng %s đã pha lê hóa thành công %s lên %d sao", player.name, item.template.name, option.param));
                }
                if (option.param == 6) {
                    player.upPointAchievement(20, 1);
                }
                if (option.param == 8) {
                    player.upPointAchievement(21, 1);
                }
                if (option.param >= 3) {
                    player.upParamMissionDaily(9, 1);
                }
            } else {
                if (type == 1 && !item.isLock) {
                    item.isLock = true;
                    player.service.refreshItemBag(index);
                }
                player.addInfo(Player.INFO_RED, "Pha lê hóa thất bại");
            }
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
            if (item != null && item.isItemBody() && (item.template.type >= 8 || item.getUpgrade() >= 8) && item.template.type != ItemType.TYPE_PET) {
                flags[i] = true;
            }
        }
        return flags;
    }
}
