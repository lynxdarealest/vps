package com.beemobi.rongthanonline.upgrade;

import com.beemobi.rongthanonline.command.Command;
import com.beemobi.rongthanonline.command.CommandName;
import com.beemobi.rongthanonline.npc.NpcName;
import com.beemobi.rongthanonline.entity.player.Player;
import com.beemobi.rongthanonline.item.Item;
import com.beemobi.rongthanonline.item.ItemManager;
import com.beemobi.rongthanonline.item.ItemName;
import com.beemobi.rongthanonline.item.ItemType;
import com.beemobi.rongthanonline.network.Message;
import com.beemobi.rongthanonline.server.Server;
import com.beemobi.rongthanonline.shop.TypePrice;
import com.beemobi.rongthanonline.task.Task;
import com.beemobi.rongthanonline.util.Utils;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class UpgradeItem extends Upgrade {
    private static final Logger logger = Logger.getLogger(UpgradeItem.class);
    private static final int[] XU_UPGRADE = new int[]{10000, 20000, 40000, 80000, 160000, 320000, 640000, 1280000, 2560000, 5120000, 7680000, 10240000, 15360000, 20480000, 25600000, 30720000, 40000000, 50000000, 60000000};
    private static final int[] DIAMOND_UPGRADE = new int[]{10, 20, 30, 40, 50, 60, 70, 80, 90, 100, 110, 120, 130, 140, 150, 160, 170, 180, 190};
    private static final int[] STONE_UPGRADE = new int[]{10, 30, 120, 480, 768, 1152, 2304, 9216, 16384, 40960, 163840, 327680, 655360, 1310720, 2621440, 5242880, 14141867, 23723806, 41516660};
    private static final int[] REQUIRE_STONE_UPGRADE = new int[]{50, 50, 51, 52, 53, 53, 54, 55, 56, 56, 57, 57, 158, 158, 159, 159, 159, 159, 159};
    public static final double[] PERCENT_UPGRADE = {70, 65, 60, 55, 50, 45, 40, 35, 30, 25, 20, 15, 10, 5, 3, 1, 0.5, 0.25, 0.1};
    public static final int MAX_UPGRADE = 19;
    public static final int UPGRADE_NORMAL = 0;
    public static final int UPGRADE_VIP = 1;

    public static HashMap<String, HashMap<Integer, Integer>> players = new HashMap<>();

    public UpgradeItem(UpgradeType type, String name, String command) {
        super(type, name, command);
        notes.add("- Vào túi đồ chọn trang bị cần cường hóa");
        notes.add("- Sau đó chọn Đá cường hóa");
        notes.add("- Có thể chọn thêm Bùa bảo vệ để chống giảm cấp khi cường hóa thất bại (chỉ có thể sử dụng đối với trang bị từ cấp 8 trở lên)");
        notes.add("- Có thể chọn thêm Thẻ xu để không mất Xu/Xu khóa khi cường hóa");
        notes.add("- Cường hóa thất bại có thể bị giảm cấp nhưng không bị giảm chỉ số gốc");
        notes.add("Lưu ý:");
        notes.add("- Nếu không đủ Xu khóa sẽ trừ Xu");
        notes.add("- Nếu không đủ Ruby sẽ trừ Kim cương");
        notes.add("- Vật phẩm sẽ khóa khi sử dụng Xu khóa hoặc Ruby");
    }

    @Override
    public void upgrade(Message message, Player player) {
        player.lockAction.lock();
        try {
            /*
                index: 0 -> item upgrade
                index: 1 -> item stone
                index: 2 -> item bảo vệ
             */
            Item[] items = new Item[5];
            int size = message.reader().readByte();
            if (size > 4) {
                player.addInfo(Player.INFO_RED, "Chỉ được sử dụng tối đa 1 loại đá và 1 bùa bảo vệ và 1 thẻ xu");
                return;
            }
            for (int i = 0; i < size; i++) {
                int index = message.reader().readByte();
                if (index < 0 || index >= player.itemsBag.length) {
                    return;
                }
                Item item = player.itemsBag[index];
                if (item == null) {
                    return;
                }
                if (item.template.type == ItemType.TYPE_GANG || item.template.type == ItemType.TYPE_GIAY || item.template.type == ItemType.TYPE_AO || item.template.type == ItemType.TYPE_QUAN
                        || item.template.type == ItemType.TYPE_RADAR || item.template.type == ItemType.TYPE_DAY_CHUYEN || item.template.type == ItemType.TYPE_NHAN || item.template.type == ItemType.TYPE_BOI) {
                    if (items[0] == null) {
                        items[0] = item;
                    } else {
                        player.addInfo(Player.INFO_RED, "Không thể nâng cấp 2 trang bị cùng lúc");
                        return;
                    }
                } else if (item.template.type == ItemType.TYPE_STONE) {
                    if (items[1] == null) {
                        items[1] = item;
                    } else {
                        player.addInfo(Player.INFO_RED, "Chỉ có thể chọn duy nhất 1 loại đá");
                        return;
                    }
                } else if (item.template.id == ItemName.BUA_BAO_VE_CAP_1 || item.template.id == ItemName.BUA_BAO_VE_CAP_2
                        || item.template.id == ItemName.BUA_BAO_VE_CAP_3 || item.template.id == ItemName.BUA_BAO_VE_CAP_4) {
                    if (items[2] == null) {
                        items[2] = item;
                    } else {
                        player.addInfo(Player.INFO_RED, "Chỉ có thể chọn duy nhất 1 loại bùa");
                        return;
                    }
                } else if (item.template.id == ItemName.THE_XU) {
                    if (items[3] == null) {
                        items[3] = item;
                    } else {
                        player.addInfo(Player.INFO_RED, "Chỉ có thể chọn duy nhất 1 loại thẻ xu");
                        return;
                    }
                } else if (item.template.id == ItemName.TAM_LINH_THACH) {
                    if (items[4] == null) {
                        items[4] = item;
                    } else {
                        player.addInfo(Player.INFO_RED, "Chỉ có thể chọn duy nhất 1 loại Tam linh thạch");
                        return;
                    }
                } else {
                    player.addInfo(Player.INFO_RED, "Vật phẩm nâng cấp không hợp lệ");
                    return;
                }
            }
            if (items[0] == null || items[1] == null) {
                player.addInfo(Player.INFO_RED, "Bạn chưa chọn trang bị hoặc đá để cường hóa");
                return;
            }
            if (items[0].isItemRider() && items[0].options.stream().noneMatch(o -> o.template.type == 7)) {
                player.addInfo(Player.INFO_RED, "Trang bị hiệp sĩ chưa được chế tạo chỉ số");
                return;
            }
            int current_upgrade = items[0].getUpgrade();
            if (current_upgrade >= MAX_UPGRADE) {
                player.addInfo(Player.INFO_RED, "Trang bị đã đạt cấp tối đa");
                return;
            }
            if (items[1].template.id < REQUIRE_STONE_UPGRADE[current_upgrade]) {
                player.addInfo(Player.INFO_RED, String.format("Chỉ có thể sử dụng %s trở lên", ItemManager.getInstance().itemTemplates.get(REQUIRE_STONE_UPGRADE[current_upgrade]).name));
                return;
            }
            if ((long) items[1].getCountStone() * (long) items[1].quantity < STONE_UPGRADE[current_upgrade]) {
                player.addInfo(Player.INFO_RED, "Hãy chọn thêm đá cường hóa");
                return;
            }
            if (items[3] != null && items[3].getParam(108) <= current_upgrade) {
                player.addInfo(Player.INFO_RED, String.format("Cấp của trang bị đã vượt quá hỗ trợ của %s", items[3].template.name));
                return;
            }
            boolean isDown = true;
            if (items[2] != null) {
                if (current_upgrade < 8) {
                    player.addInfo(Player.INFO_RED, "Chỉ sử dụng cho trang bị từ +8 trở lên");
                    return;
                }
                switch (items[2].template.id) {
                    case ItemName.BUA_BAO_VE_CAP_1:
                        if (current_upgrade < 12) {
                            isDown = false;
                        } else {
                            player.addInfo(Player.INFO_RED, "Bùa bảo vệ không hợp lệ");
                            return;
                        }
                        break;
                    case ItemName.BUA_BAO_VE_CAP_2:
                        if (current_upgrade < 14) {
                            isDown = false;
                        } else {
                            player.addInfo(Player.INFO_RED, "Bùa bảo vệ không hợp lệ");
                            return;
                        }
                        break;
                    case ItemName.BUA_BAO_VE_CAP_3:
                        if (current_upgrade < 16) {
                            isDown = false;
                        } else {
                            player.addInfo(Player.INFO_RED, "Bùa bảo vệ không hợp lệ");
                            return;
                        }
                        break;

                    case ItemName.BUA_BAO_VE_CAP_4:
                        isDown = false;
                        break;
                }
            }
            int down_upgrade = (current_upgrade % 2 == 0 ? current_upgrade : (current_upgrade - (isDown ? 1 : 0)));
            if (current_upgrade < 5) {
                down_upgrade = current_upgrade;
            } else if (current_upgrade > 16) {
                down_upgrade = 16;
            }
            int[] index = new int[5];
            index[0] = items[0].indexUI;
            index[1] = items[1].indexUI;
            if (items[2] == null) {
                index[2] = -1;
            } else {
                index[2] = items[2].indexUI;
            }
            if (items[3] == null) {
                index[3] = -1;
            } else {
                index[3] = items[3].indexUI;
            }
            if (items[4] == null) {
                index[4] = -1;
            } else {
                index[4] = items[4].indexUI;
            }
            double percent = PERCENT_UPGRADE[current_upgrade];
            if (player.options[120] > 0) {
                percent += percent * player.options[120] / 100;
            }
            if (items[0].template.levelRequire < 30 && player.options[130] > 0) {
                percent += percent * player.options[130] / 100;
            }
            if (player.options[172] > 0) {
                percent += Math.max(percent * player.options[172] / 100, 3);
            }
            int coin = XU_UPGRADE[current_upgrade];
            if (items[3] != null) {
                coin = 0;
            }
            String content = String.format("Cường hóa: %s +%d", items[0].template.name, current_upgrade + 1) + "\n";
            if (current_upgrade >= 16) {
                content += "Cần sử dụng " + (current_upgrade - 15) * 5 + " Tam linh thạch\n";
            }
            content += "Thường: tỉ lệ thành công " + percent + "%, tiêu hao " + Utils.formatNumber(coin) + " Xu khóa" + "\n" +
                    "Đặc biệt: tỉ lệ thành công " + (percent + percent / 2) + "%, tiêu hao " + Utils.formatNumber(coin) + " Xu khóa và " + Utils.formatNumber(DIAMOND_UPGRADE[current_upgrade]) + " kim cương" + "\n" +
                    String.format("Thất bại sẽ trở về cấp %d", down_upgrade) + "\n";
            if (items[3] != null || items[1].isLock || (items[2] != null && items[2].isLock)) {
                content += "Trang bị sẽ bị khóa sau khi cường hóa thành công";
            }
            List<Command> commands = new ArrayList<>();
            commands.add(new Command(CommandName.CONFIRM_UPGRADE, "Thường\n(Xu)", player, UPGRADE_NORMAL, index, 0));
            commands.add(new Command(CommandName.CONFIRM_UPGRADE, "Đặc biệt\n(Xu + KC)", player, UPGRADE_VIP, index, 0));
            commands.add(new Command(CommandName.CONFIRM_UPGRADE, "Thường\n(Xu khóa)", player, UPGRADE_NORMAL, index, 1));
            commands.add(new Command(CommandName.CONFIRM_UPGRADE, "Đặc biệt\n(Xu khóa + Ruby)", player, UPGRADE_VIP, index, 1));
            player.createMenu(NpcName.ME, content, commands);
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
            int type = (int) objects[0];
            int[] index = (int[]) objects[1];
            int typeCoin = (int) objects[2];
            Item[] items = new Item[5];
            /*
                index: 0 -> item upgrade
                index: 1 -> item stone
                index: 2 -> item bảo vệ
                index: 3 -> item free xu
             */
            int size = index.length;
            if (size > 5) {
                return;
            }
            for (int i : index) {
                if (i < 0 || i >= player.itemsBag.length) {
                    continue;
                }
                Item item = player.itemsBag[i];
                if (item == null) {
                    return;
                }
                if (item.template.type == ItemType.TYPE_GANG || item.template.type == ItemType.TYPE_GIAY || item.template.type == ItemType.TYPE_AO || item.template.type == ItemType.TYPE_QUAN
                        || item.template.type == ItemType.TYPE_RADAR || item.template.type == ItemType.TYPE_DAY_CHUYEN || item.template.type == ItemType.TYPE_NHAN || item.template.type == ItemType.TYPE_BOI) {
                    if (items[0] == null) {
                        items[0] = item;
                    } else {
                        player.addInfo(Player.INFO_RED, "Không thể nâng cấp 2 trang bị cùng lúc");
                        return;
                    }
                } else if (item.template.type == ItemType.TYPE_STONE) {
                    if (items[1] == null) {
                        items[1] = item;
                    } else {
                        player.addInfo(Player.INFO_RED, "Chỉ có thể chọn duy nhất 1 loại đá");
                        return;
                    }
                } else if (item.template.id == ItemName.BUA_BAO_VE_CAP_1 || item.template.id == ItemName.BUA_BAO_VE_CAP_2
                        || item.template.id == ItemName.BUA_BAO_VE_CAP_3 || item.template.id == ItemName.BUA_BAO_VE_CAP_4) {
                    if (items[2] == null) {
                        items[2] = item;
                    } else {
                        player.addInfo(Player.INFO_RED, "Chỉ có thể chọn duy nhất 1 loại bùa");
                        return;
                    }
                } else if (item.template.id == ItemName.THE_XU) {
                    if (items[3] == null) {
                        items[3] = item;
                    } else {
                        player.addInfo(Player.INFO_RED, "Chỉ có thể chọn duy nhất 1 loại thẻ xu");
                        return;
                    }
                }
                else if (item.template.id == ItemName.TAM_LINH_THACH) {
                    if (items[4] == null) {
                        items[4] = item;
                    } else {
                        player.addInfo(Player.INFO_RED, "Chỉ có thể chọn duy nhất 1 loại Tam linh thạch");
                        return;
                    }
                }else {
                    player.addInfo(Player.INFO_RED, "Vật phẩm nâng cấp không hợp lệ");
                    return;
                }
            }
            if (items[0] == null || items[1] == null) {
                player.addInfo(Player.INFO_RED, "Bạn chưa chọn trang bị hoặc đá để cường hóa");
                return;
            }
            if (items[0].isItemRider() && items[0].options.stream().noneMatch(o -> o.template.type == 7)) {
                player.addInfo(Player.INFO_RED, "Trang bị hiệp sĩ chưa được chế tạo chỉ số");
                return;
            }
            int current_upgrade = items[0].getUpgrade();
            if (current_upgrade >= MAX_UPGRADE) {
                player.addInfo(Player.INFO_RED, "Trang bị đã đạt cấp tối đa");
                return;
            }
            if (items[3] != null && items[3].getParam(108) <= current_upgrade) {
                player.addInfo(Player.INFO_RED, String.format("Cấp của trang bị đã vượt quá hỗ trợ của %s", items[3].template.name));
                return;
            }
            long coin = XU_UPGRADE[current_upgrade];
            boolean isLock = items[1].isLock || (items[2] != null && items[2].isLock);
            if (items[3] != null) {
                isLock = true;
                coin = 0;
            }
            if ((typeCoin == 1 && !player.isEnoughMoney(TypePrice.COIN_LOCK, coin)) || (typeCoin == 0 && !player.isEnoughMoney(TypePrice.COIN, coin))) {
                return;
            }
            if (type == 1 && ((typeCoin == 1 && !player.isEnoughMoney(TypePrice.RUBY, DIAMOND_UPGRADE[current_upgrade])) || (typeCoin == 0 && !player.isEnoughMoney(TypePrice.DIAMOND, DIAMOND_UPGRADE[current_upgrade])))) {
                return;
            }
            if (items[1].template.id < REQUIRE_STONE_UPGRADE[current_upgrade]) {
                player.addInfo(Player.INFO_RED, String.format("Chỉ có thể sử dụng %s trở lên", ItemManager.getInstance().itemTemplates.get(REQUIRE_STONE_UPGRADE[current_upgrade]).name));
                return;
            }
            if ((long) items[1].getCountStone() * (long) items[1].quantity < STONE_UPGRADE[current_upgrade]) {
                player.addInfo(Player.INFO_RED, "Hãy chọn thêm đá cường hóa");
                return;
            }
            boolean isDown = true;
            if (items[2] != null) {
                if (current_upgrade < 8) {
                    player.addInfo(Player.INFO_RED, "Chỉ sử dụng cho trang bị từ +8 trở lên");
                    return;
                }
                switch (items[2].template.id) {
                    case ItemName.BUA_BAO_VE_CAP_1:
                        if (current_upgrade < 12) {
                            isDown = false;
                        } else {
                            player.addInfo(Player.INFO_RED, "Bùa bảo vệ không hợp lệ");
                            return;
                        }
                        break;
                    case ItemName.BUA_BAO_VE_CAP_2:
                        if (current_upgrade < 14) {
                            isDown = false;
                        } else {
                            player.addInfo(Player.INFO_RED, "Bùa bảo vệ không hợp lệ");
                            return;
                        }
                        break;
                    case ItemName.BUA_BAO_VE_CAP_3:
                        if (current_upgrade < 16) {
                            isDown = false;
                        } else {
                            player.addInfo(Player.INFO_RED, "Bùa bảo vệ không hợp lệ");
                            return;
                        }
                        break;

                    case ItemName.BUA_BAO_VE_CAP_4:
                        isDown = false;
                        break;
                }
            }
            if (current_upgrade >= 16) {
                if (items[4] == null || items[4].quantity < (current_upgrade - 15) * 5) {
                    player.addInfo(Player.INFO_RED, "Số lượng Tam linh thạch không đủ");
                    return;
                }
                items[4].quantity -= (current_upgrade - 15) * 5;
                if (items[4].quantity <= 0) {
                    player.itemsBag[items[4].indexUI] = null;
                }
            }
            int stone_use = STONE_UPGRADE[current_upgrade];
            while (stone_use > 0 && items[1].quantity > 0) {
                items[1].quantity--;
                stone_use -= items[1].getCountStone();
            }
            if (items[1].quantity <= 0) {
                player.itemsBag[items[1].indexUI] = null;
            }
            if (items[2] != null) {
                items[2].quantity--;
                if (items[2].quantity <= 0) {
                    player.itemsBag[items[2].indexUI] = null;
                }
            }
            if (items[3] != null) {
                items[3].quantity--;
                if (items[3].quantity <= 0) {
                    player.itemsBag[items[3].indexUI] = null;
                }
            }
            if (typeCoin == 1) {
                player.downMoney(TypePrice.COIN_LOCK, coin);
                if (type == 1) {
                    player.downMoney(TypePrice.RUBY, DIAMOND_UPGRADE[current_upgrade]);
                }
            } else {
                player.downMoney(TypePrice.COIN, coin);
                if (type == 1) {
                    player.downMoney(TypePrice.DIAMOND, DIAMOND_UPGRADE[current_upgrade]);
                }
            }
            double percent = (PERCENT_UPGRADE[current_upgrade] + (type == 1 ? PERCENT_UPGRADE[current_upgrade] / 2 : 0)) * 10;
            if (player.options[120] > 0) {
                percent += percent * player.options[120] / 100;
            }
            if (items[0].template.levelRequire < 30 && player.options[130] > 0) {
                percent += percent * player.options[130] / 100;
            }
            if (player.options[172] > 0) {
                percent += Math.max(percent * player.options[172] / 100, 30);
            }
            if (current_upgrade >= 14) {
                percent /= 2;
                if (type == 0) {
                    percent /= 2;
                }
            } else if (current_upgrade >= 12) {
                percent -= percent / 3;
            }
            boolean isUp = Utils.nextInt(1000) < (int) percent;
            // buff
            HashMap<Integer, Integer> players = UpgradeItem.players.getOrDefault(player.name, null);
            if (players != null) {
                int count = players.getOrDefault(current_upgrade + 1, -1);
                if (count != -1) {
                    if (count <= 1) {
                        isUp = true;
                        players.remove(current_upgrade + 1);
                    } else {
                        isUp = false;
                        players.put(current_upgrade + 1, --count);
                    }
                }
            }
            if (isLock || typeCoin == 1) {
                items[0].isLock = true;
            }
            if (isUp) {
                current_upgrade = items[0].nextUpgrade();
                player.addInfo(Player.INFO_YELLOW, String.format("Trang bị được cường hóa lên cấp %d", current_upgrade));
            } else {
                if (isDown) {
                    int num = 0;
                    while (current_upgrade > 4 && (current_upgrade % 2 != 0 || current_upgrade > 16) && num < MAX_UPGRADE) {
                        current_upgrade = items[0].downUpgrade();
                        num++;
                    }
                }
                player.addInfo(Player.INFO_RED, String.format("Cường hóa thất bại, trang bị trở về cấp %d", current_upgrade));
            }
            player.service.setItemBag();
            if (isUp) {
                if (player.taskMain != null) {
                    Task taskMain = player.taskMain;
                    if (taskMain.template.id == 7 && taskMain.index >= 1 && taskMain.index <= 8
                            && items[0].template.type == taskMain.index - 1) {
                        player.nextTaskIndex();
                    }
                    if (taskMain.template.id == 12 && taskMain.index >= 5 && taskMain.index <= 9
                            && taskMain.index <= current_upgrade + 3) {
                        player.nextTaskParam();
                    }
                    if (taskMain.template.id == 17 && current_upgrade >= 8 && items[0].template.levelRequire >= 40 && items[0].template.levelRequire < 50) {
                        if (items[0].template.type == ItemType.TYPE_GANG && taskMain.index == 9) {
                            player.nextTaskIndex();
                        } else if (items[0].template.type == ItemType.TYPE_BOI && taskMain.index == 10) {
                            player.nextTaskIndex();
                        } else if (items[0].template.type == ItemType.TYPE_GIAY && taskMain.index == 11) {
                            player.nextTaskIndex();
                        } else if (items[0].template.type == ItemType.TYPE_NHAN && taskMain.index == 12) {
                            player.nextTaskIndex();
                        } else if (items[0].template.type == ItemType.TYPE_QUAN && taskMain.index == 13) {
                            player.nextTaskIndex();
                        } else if (items[0].template.type == ItemType.TYPE_DAY_CHUYEN && taskMain.index == 14) {
                            player.nextTaskIndex();
                        } else if (items[0].template.type == ItemType.TYPE_AO && taskMain.index == 15) {
                            player.nextTaskIndex();
                        } else if (items[0].template.type == ItemType.TYPE_RADAR && taskMain.index == 16) {
                            player.nextTaskIndex();
                        }
                    }
                }
                if (player.taskDaily != null && player.taskDaily.type == 1) {
                    player.upTaskDailyParam(1);
                }
                if (current_upgrade >= 8) {
                    player.upParamMissionDaily(8, 1);
                }
                if (current_upgrade >= 12) {
                    Server.getInstance().service.serverChat(String.format("Chúc mừng %s đã cường hóa thành công %s lên cấp %s", player.name, items[0].template.name, current_upgrade));
                }
                if (current_upgrade == 14) {
                    player.upPointAchievement(6, 1);
                }
                if (current_upgrade == 16) {
                    player.upPointAchievement(7, 1);
                }
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
            if (item != null && (item.template.type < 8 || item.template.type == ItemType.TYPE_STONE
                    || item.template.id == ItemName.BUA_BAO_VE_CAP_1 || item.template.id == ItemName.BUA_BAO_VE_CAP_2
                    || item.template.id == ItemName.BUA_BAO_VE_CAP_3 || item.template.id == ItemName.BUA_BAO_VE_CAP_4
                    || item.template.id == ItemName.TAM_LINH_THACH)) {
                flags[i] = true;
            }
        }
        return flags;
    }
}
