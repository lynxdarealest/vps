package com.beemobi.rongthanonline.command;

import com.beemobi.rongthanonline.bot.disciple.Disciple;
import com.beemobi.rongthanonline.bot.disciple.DiscipleStatus;
import com.beemobi.rongthanonline.bot.tournament.TournamentAthlete;
import com.beemobi.rongthanonline.clan.Clan;
import com.beemobi.rongthanonline.common.KeyValue;
import com.beemobi.rongthanonline.common.Language;
import com.beemobi.rongthanonline.dragon.Dragon;
import com.beemobi.rongthanonline.effect.Effect;
import com.beemobi.rongthanonline.effect.EffectName;
import com.beemobi.rongthanonline.entity.player.PlayerManager;
import com.beemobi.rongthanonline.entity.player.action.ClanAction;
import com.beemobi.rongthanonline.entity.player.action.EnemyAction;
import com.beemobi.rongthanonline.entity.player.action.TeamAction;
import com.beemobi.rongthanonline.entity.player.action.TradeAction;
import com.beemobi.rongthanonline.event.Event;
import com.beemobi.rongthanonline.item.ItemOption;
import com.beemobi.rongthanonline.lucky.LuckyManager;
import com.beemobi.rongthanonline.map.*;
import com.beemobi.rongthanonline.map.Map;
import com.beemobi.rongthanonline.map.expansion.ExpansionState;
import com.beemobi.rongthanonline.map.expansion.congress.MartialCongress;
import com.beemobi.rongthanonline.map.expansion.congress.ZoneMartialCongress;
import com.beemobi.rongthanonline.map.expansion.dark.DarkVillage;
import com.beemobi.rongthanonline.map.expansion.flagwar.FlagWar;
import com.beemobi.rongthanonline.map.expansion.flagwar.ZoneFlagWar;
import com.beemobi.rongthanonline.map.expansion.sanctuary.Sanctuary;
import com.beemobi.rongthanonline.map.expansion.spaceship.Spaceship;
import com.beemobi.rongthanonline.map.expansion.tournament.Tournament;
import com.beemobi.rongthanonline.map.expansion.treasure.Treasure;
import com.beemobi.rongthanonline.mission.*;
import com.beemobi.rongthanonline.item.Item;
import com.beemobi.rongthanonline.item.ItemManager;
import com.beemobi.rongthanonline.item.ItemName;
import com.beemobi.rongthanonline.map.expansion.barrack.Barrack;
import com.beemobi.rongthanonline.map.expansion.city.ForgottenCity;
import com.beemobi.rongthanonline.map.expansion.festival.Flight;
import com.beemobi.rongthanonline.map.expansion.festival.MartialArtsFestival;
import com.beemobi.rongthanonline.map.expansion.festival.Warrior;
import com.beemobi.rongthanonline.map.expansion.manor.Manor;
import com.beemobi.rongthanonline.map.expansion.nrnm.ZoneDragonBallNamek;
import com.beemobi.rongthanonline.map.expansion.nrnm.DragonBallNamek;
import com.beemobi.rongthanonline.map.expansion.survival.Gamer;
import com.beemobi.rongthanonline.map.expansion.survival.Survival;
import com.beemobi.rongthanonline.model.*;
import com.beemobi.rongthanonline.network.MessageDiscipleInfoName;
import com.beemobi.rongthanonline.npc.Npc;
import com.beemobi.rongthanonline.entity.player.Player;
import com.beemobi.rongthanonline.model.input.ClientInputType;
import com.beemobi.rongthanonline.model.input.TextField;
import com.beemobi.rongthanonline.npc.NpcName;
import com.beemobi.rongthanonline.npc.NpcTree;
import com.beemobi.rongthanonline.server.Server;
import com.beemobi.rongthanonline.server.ServerRandom;
import com.beemobi.rongthanonline.shop.*;
import com.beemobi.rongthanonline.skill.Skill;
import com.beemobi.rongthanonline.skill.SkillManager;
import com.beemobi.rongthanonline.task.TaskClan;
import com.beemobi.rongthanonline.team.Team;
import com.beemobi.rongthanonline.top.Top;
import com.beemobi.rongthanonline.top.TopManager;
import com.beemobi.rongthanonline.top.TopType;
import com.beemobi.rongthanonline.upgrade.Upgrade;
import com.beemobi.rongthanonline.upgrade.UpgradeManager;
import com.beemobi.rongthanonline.upgrade.UpgradeType;
import com.beemobi.rongthanonline.util.Utils;
import org.apache.log4j.Logger;

import java.util.*;
import java.util.stream.Collectors;

public class Command {
    private static final Logger logger = Logger.getLogger(Command.class);

    public int id;
    public Player player;
    public String caption;
    public Object[] elements;

    public Command(int id, String caption, Player player) {
        this.id = id;
        this.caption = caption;
        this.player = player;
        this.elements = null;
    }

    public Command(int id, String caption, Player player, Object... objects) {
        this.id = id;
        this.caption = caption;
        this.player = player;
        this.elements = objects;
    }

    public void performAction() {
        player.lockAction.lock();
        if (id < 0) {
            return;
        }
        if (player.commands != null) {
            player.commands.clear();
        }
        try {
            switch (id) {

                case CommandName.SHOW_TASK: {
                    Npc npc = null;
                    if (elements[1] != null) {
                        npc = (Npc) elements[1];
                    }
                    player.npcTaskController.openMenu(npc, (int) elements[0]);
                    return;
                }

                case CommandName.CONFIRM_TASK: {
                    Npc npc = null;
                    if (elements[2] != null) {
                        npc = (Npc) elements[2];
                    }
                    player.npcTaskController.confirmMenu(npc, (int) elements[0], (int) elements[1]);
                    return;
                }

                case CommandName.SHOW_TOP: {
                    List<Command> commands = new ArrayList<>();
                    commands.add(new Command(CommandName.SHOW_TOP_PRO, "Sức chiến đấu", player));
                    commands.add(new Command(CommandName.SHOW_TOP_POWER, "Sức mạnh", player));
                    commands.add(new Command(CommandName.SHOW_TOP_DISCIPLE, "Đệ tử", player));
                    if (Event.isEvent()) {
                        commands.add(new Command(CommandName.SHOW_EVENT, "TOP\nSự kiện", player, 1, -1));
                    }
                    commands.add(new Command(CommandName.SHOW_TOP_CLAN, "Bang hội", player));
                    commands.add(new Command(CommandName.SHOW_TOP_WEEK, "Đua top\ntuần", player));
                    player.createMenu(NpcName.ME, "", commands);
                    return;
                }

                case CommandName.SHOW_SHOP_BUNMA: {
                    List<Command> commands = new ArrayList<>();
                    commands.add(new Command(CommandName.SHOW_SHOP_BUNMA_EQUIP, "Trang phục", player));
                    commands.add(new Command(CommandName.SHOW_SHOP_BUNMA_OTHER, "Đặc biệt", player));
                    //commands.add(new Command(CommandName.CUA_HANG_DA_BAN, "Vật phẩm đã bán", player));
                    if (elements == null || elements.length == 0) {
                        player.createMenu(NpcName.ME, "Cậu muốn mua gì nào?", commands);
                    } else {
                        player.createMenu(((Npc) elements[0]).template.id, "Cậu muốn mua gì nào?", commands);
                    }
                    return;
                }

                case CommandName.SHOW_SHOP_BUNMA_EQUIP: {
                    Shop shop = ShopManager.getInstance().shops.get(ShopType.SHOP_BUNMA_EQUIP);
                    if (shop != null) {
                        shop.show(player);
                    }
                    return;
                }

                case CommandName.SHOW_SHOP_BUNMA_OTHER: {
                    Shop shop = ShopManager.getInstance().shops.get(ShopType.SHOP_BUNMA_OTHER);
                    if (shop != null) {
                        shop.show(player);
                    }
                    return;
                }

                case CommandName.SHOW_SHOP_BA_HAT_MIT: {
                    List<Command> commands = new ArrayList<>();
                    commands.add(new Command(CommandName.SHOW_SHOP_BA_HAT_MIT_AMULET_OTHER, "Bùa đặc biệt", player));
                    commands.add(new Command(CommandName.SHOW_SHOP_BA_HAT_MIT_AMULET_TIME, "Bùa thời gian", player));
                    commands.add(new Command(CommandName.XOA_HIEU_UNG_BUA, "Xóa hiệu ứng bùa", player));
                    if (elements == null || elements.length == 0) {
                        player.createMenu(NpcName.ME, "Ta có rất nhiều loại bùa, người có muốn mua không?", commands);
                    } else {
                        player.createMenu(((Npc) elements[0]).template.id, "Ta có rất nhiều loại bùa, người có muốn mua không?", commands);
                    }
                    return;
                }

                case CommandName.SHOW_SHOP_BA_HAT_MIT_AMULET_OTHER: {
                    Shop shop = ShopManager.getInstance().shops.get(ShopType.SHOP_AMULET_OTHER);
                    if (shop != null) {
                        shop.show(player);
                    }
                    return;
                }

                case CommandName.SHOW_SHOP_BA_HAT_MIT_AMULET_TIME: {
                    Shop shop = ShopManager.getInstance().shops.get(ShopType.SHOP_AMULET_TIME);
                    if (shop != null) {
                        shop.show(player);
                    }
                    return;
                }

                case CommandName.CONFIRM_SELL_ITEM: {
                    player.confirmSellItem((int) elements[0]);
                    return;
                }

                case CommandName.UPGRADE_ITEM: {
                    Upgrade upgrade = UpgradeManager.getInstance().upgrades.get(UpgradeType.UPGRADE_ITEM);
                    if (upgrade != null) {
                        upgrade.showTab(player);
                    }
                    return;
                }

                case CommandName.UPGRADE_STONE: {
                    Upgrade upgrade = UpgradeManager.getInstance().upgrades.get(UpgradeType.UPGRADE_STONE);
                    if (upgrade != null) {
                        upgrade.showTab(player);
                    }
                    return;
                }

                case CommandName.CRYSTALLIZE: {
                    Upgrade upgrade = UpgradeManager.getInstance().upgrades.get(UpgradeType.CRYSTALLIZE);
                    if (upgrade != null) {
                        upgrade.showTab(player);
                    }
                    return;
                }

                case CommandName.POLISH: {
                    Upgrade upgrade = UpgradeManager.getInstance().upgrades.get(UpgradeType.POLISH);
                    if (upgrade != null) {
                        upgrade.showTab(player);
                    }
                    return;
                }

                case CommandName.ENCHANT_ITEM: {
                    Upgrade upgrade = UpgradeManager.getInstance().upgrades.get(UpgradeType.ENCHANT_ITEM);
                    if (upgrade != null) {
                        upgrade.showTab(player);
                    }
                    return;
                }

                case CommandName.CONFIRM_UPGRADE: {
                    if (Server.getInstance().isInterServer()) {
                        player.addInfo(Player.INFO_RED, Language.CANCEL_ACTION_WHEN_SERVER_IS_INTER_SERVER);
                        return;
                    }
                    if (player.isTrading()) {
                        player.addInfo(Player.INFO_RED, Language.CANCEL_ACTION_WHEN_TRADE);
                        return;
                    }
                    if (player.isProtect) {
                        player.addInfo(Player.INFO_RED, Language.TAI_KHOAN_DANG_DUOC_BAO_VE);
                        return;
                    }
                    Upgrade upgrade = UpgradeManager.getInstance().upgrades.get(player.upgradeType);
                    if (upgrade != null) {
                        upgrade.confirmUpgrade(player, elements);
                    }
                    return;
                }

                case CommandName.ACCEPT_INVITE_JOIN_TEAM: {
                    ((TeamAction) player.teamAction).acceptInviteJoinTeam((Team) elements[0]);
                    return;
                }

                case CommandName.ACCEPT_PLAYER_JOIN_TEAM: {
                    ((TeamAction) player.teamAction).acceptJoinTeam((int) elements[0]);
                    return;
                }

                case CommandName.ACCEPT_INVITE_TRADE: {
                    ((TradeAction) player.tradeAction).acceptTradeRequest((int) elements[0]);
                    return;
                }

                case CommandName.CREATE_CLAN: {
                    player.startClientInput(ClientInputType.INPUT_CREATE_CLAN, "Tạo bang hội", new TextField("Tên bang (5-10 kí tự)", TextField.TYPE_NORMAL));
                    return;
                }

                case CommandName.ACCEPT_INVITE_JOIN_CLAN: {
                    ((ClanAction) player.clanAction).acceptInviteJoinClan((Clan) elements[0]);
                    return;
                }

                case CommandName.CONFIRM_EXPEL_CLAN_MEMBER: {
                    ((ClanAction) player.clanAction).confirmExpelClanMember((int) elements[0]);
                    return;
                }

                case CommandName.CONFIRM_LEAVE_CLAN: {
                    ((ClanAction) player.clanAction).confirmLeaveClan();
                    return;
                }

                case CommandName.EARTH_TO_NAMEK: {
                    if (player.taskMain == null || player.taskMain.template.id < 14) {
                        player.addInfo(Player.INFO_RED, "Chưa thể đến khu vực này");
                        return;
                    }
                    player.teleport(MapName.TRAM_TAU_VU_TRU_NAMEK, false);
                    return;
                }

                case CommandName.OPEN_BARRACK: {
                    Barrack barrack = player.getBarrack();
                    if (barrack != null) {
                        player.joinBarrack(barrack);
                    } else {
                        player.openBarrack();
                    }
                    return;
                }

                case CommandName.RUNG_KARIN_TO_THAP_KARIN: {
                    Map map = MapManager.getInstance().maps.get(MapName.THAP_KARIN);
                    if (map != null) {
                        player.x = 800;
                        player.y = 650;
                        player.joinMap(map, -1);
                    }
                    return;
                }

                case CommandName.THAP_KARIN_TO_RUNG_KARIN: {
                    Map map = MapManager.getInstance().maps.get(MapName.THANH_DIA_KARIN);
                    if (map != null) {
                        player.x = 1600;
                        player.y = 200;
                        player.joinMap(map, -1);
                    }
                    return;
                }

                case CommandName.CONFIRM_GIVE_DIAMOND: {
                    player.confirmGiveDiamond((int) elements[0], (int) elements[1]);
                    return;
                }

                case CommandName.SHOW_TASK_DAILY: {
                    StringBuilder text = new StringBuilder();
                    List<Command> commands = new ArrayList<>();
                    if (player.taskDaily == null) {
                        text.append("Bạn có muốn nhận nhiệm vụ không?");
                        text.append("\n");
                        text.append("Số lượt miễn phí còn lại: ");
                        text.append(player.countTaskDaily);
                        text.append("\n");
                        text.append("Đã hoàn thành: ");
                        text.append(player.countCompleteTaskDaily);
                        commands.add(new Command(CommandName.RECEIVE_TASK_DAILY, String.format("Nhận%s", player.countTaskDaily <= 0 ? "\n1 KC" : ""), player));
                        commands.add(new Command(CommandName.CANCEL, "Từ chối", player));
                        player.createMenu(NpcName.ME, text.toString(), commands);
                    } else {
                        text.append("Nhiệm vụ hiện tại của bạn là:");
                        text.append("\n");
                        text.append(player.taskDaily.toString());
                        if (player.taskDaily.param >= player.taskDaily.maxParam) {
                            commands.add(new Command(CommandName.COMPLETE_TASK_DAILY, "Hoàn thành", player));
                        } else if (player.countTaskDaily <= 0) {
                            commands.add(new Command(CommandName.COMPLETE_TASK_DAILY, "Hoàn thành\n5 KC", player));
                        }
                        commands.add(new Command(CommandName.CANCEL_TASK_DAILY, "Hủy nhiệm vụ", player));
                    }
                    player.createMenu(NpcName.ME, text.toString(), commands);
                    return;
                }

                case CommandName.RECEIVE_TASK_DAILY: {
                    if (player.taskDaily != null) {
                        return;
                    }
                    if (player.countTaskDaily <= 0 && player.countCompleteTaskDaily < 10000 && player.diamond < 1) {
                        player.addInfo(Player.INFO_RED, "Bạn không đủ kim cương để nhận nhiệm vụ");
                        return;
                    }
                    if (player.countTaskDaily > 0 || player.countCompleteTaskDaily < 10000) {
                        player.taskDaily = player.createTaskDaily();
                        player.countTaskDaily--;
                        player.addInfo(Player.INFO_YELLOW, "Nhiệm vụ của bạn là " + player.taskDaily.toString());
                        if (player.countTaskDaily <= 0) {
                            player.upDiamond(-1);
                        }
                        player.service.setTaskDaily();
                    } else {
                        player.addInfo(Player.INFO_RED, "Nhận nhiệm vụ thất bại");
                    }
                    return;
                }

                case CommandName.COMPLETE_TASK_DAILY: {
                    if (player.countCompleteTaskDaily >= 10000) {
                        player.addInfo(Player.INFO_RED, "Chỉ được hoàn thành 10000 nhiệm vụ mỗi ngày");
                        return;
                    }
                    if (player.isBagFull()) {
                        player.addInfo(Player.INFO_RED, Language.ME_BAG_FULL);
                        return;
                    }
                    int diamond = 5;
                    if (player.taskDaily.param >= player.taskDaily.maxParam || player.countTaskDaily > 0) {
                        diamond = 0;
                    }
                    if (diamond > 0 && player.diamond < diamond) {
                        player.addInfo(Player.INFO_RED, "Bạn không đủ kim cương");
                        break;
                    }
                    if (diamond > 0) {
                        player.upDiamond(-diamond);
                    }
                    player.countCompleteTaskDaily++;
                    player.taskDaily = null;
                    player.service.setTaskDaily();
                    player.upPointActive(1);
                    if (player.taskMain != null && player.taskMain.template.id == 13 && player.taskMain.index == 1) {
                        player.nextTaskParam();
                    }
                    int percent = Utils.nextInt(100);
                    if (percent < 10) {
                        int reward = Utils.nextInt(1, 2);
                        player.upRuby(reward);
                        player.addInfo(Player.INFO_YELLOW, String.format("Bạn nhận được %d Ruby", reward));
                    } else if (percent < 20) {
                        int max = Math.max(20000, 100000 * player.level);
                        int xu = Utils.nextInt(max - max / 10, max);
                        player.upXuKhoa(xu);
                        player.addInfo(Player.INFO_YELLOW, String.format("Bạn nhận được %d Xu Khóa", xu));
                    } else if (percent < 60) {
                        Item item = ItemManager.getInstance().createItem(ItemName.BI_KIP_KY_NANG, Utils.nextInt(1, 2), true);
                        if (player.addItem(item)) {
                            player.addInfo(Player.INFO_YELLOW, String.format("Bạn nhận được x%d %s", item.quantity, item.template.name));
                        }
                    } else {
                        Item item = ItemManager.getInstance().createItem(ItemName.DAU_THAN_CAP_5, 30, true);
                        if (player.addItem(item)) {
                            player.addInfo(Player.INFO_YELLOW, String.format("Bạn nhận được x%d %s", item.quantity, item.template.name));
                        }
                    }
                    if (player.countTaskDaily > 0 && player.disciple == null) {
                        Effect effect = player.findEffectByTemplateId(EffectName.QUA_TRUNG_DE_TU);
                        if (effect != null) {
                            long time = Utils.nextLong(1800000L, 3600000L);
                            if (time > effect.time - 10000L) {
                                time = effect.time - 10000L;
                            }
                            player.addTimeEffect(EffectName.QUA_TRUNG_DE_TU, -time);
                        }
                    }
                    if (diamond == 0) {
                        if (((player.countCompleteTaskDaily == 30 && player.countTaskDaily == 0) || player.countTaskDaily > 0)) {
                            player.upParamMissionDaily(5, 1);
                            if (player.clan != null) {
                                int point = 1;
                                player.clan.upExp(point, false);
                                player.clan.upPointMember(player.id, point);
                            }
                            player.upPointWeekly(PointWeeklyType.ACTIVE, 1);
                        }
                        if (Event.isEvent()) {
                            Event.event.rewardTaskDaily(player);
                        }
                        if (player.level >= 50) {
                            player.addItem(ItemManager.getInstance().createItem(ItemName.NUOC_PHEP_MA_THUAT, 1, false), true);
                        }
                    }
                    return;
                }

                case CommandName.CANCEL_TASK_DAILY: {
                    if (player.taskDaily != null) {
                        player.taskDaily = null;
                        player.service.setTaskDaily();
                        player.addInfo(Player.INFO_RED, "Đã hủy nhiệm vụ");
                    }
                    return;
                }

                case CommandName.CONFIRM_PK: {
                    player.confirmInviteSolo((int) elements[0]);
                    return;
                }

                case CommandName.MENU_NAP_THE: {
                    List<Command> commands = new ArrayList<>();
                    commands.add(new Command(CommandName.GO_TO_WEB_NAP, "Nạp ngay", player));
                    commands.add(new Command(CommandName.NHAN_THUONG_NAP_THE, "Nhận thưởng", player));
                    for (IMission mission : player.missionRecharges) {
                        MissionRecharge recharge = (MissionRecharge) mission;
                        if (recharge.template.param == 10000 && recharge.getType() == MissionType.DA_XONG) {
                            commands.add(new Command(CommandName.RESET_MOC_NAP_THE, "Làm mới mốc tích điểm", player));
                            break;
                        }
                    }
                    StringBuilder content = new StringBuilder();
                    content.append("Khi nạp hoặc nhận kim cương từ người chơi khác, bạn sẽ nhận được điểm thưởng");
                    content.append("\n");
                    content.append("Tích lũy các mốc điểm để nhận các phần thưởng quý giá");
                    content.append("\n");
                    content.append("Tích lũy sẽ được đặt lại khi bạn hoàn thành xong các mốc");
                    player.createMenu(NpcName.ME, content.toString(), commands);
                    return;
                }

                case CommandName.GO_TO_WEB_NAP: {
                    player.service.openURL("http://rongthanonline.vn/account/nap-tien");
                    return;
                }

                case CommandName.NHAN_THUONG_NAP_THE: {
                    player.service.showMission(2, player.missionRecharges);
                    return;
                }

                case CommandName.CHANGE_STATUS_DISCIPLE: {
                    if (player.disciple == null) {
                        return;
                    }
                    int status = (int) elements[0];
                    DiscipleStatus discipleStatus = null;
                    if (status == 0) {
                        discipleStatus = DiscipleStatus.FOLLOW;
                    } else if (status == 1) {
                        discipleStatus = DiscipleStatus.PROTECT;
                    } else if (status == 2) {
                        discipleStatus = DiscipleStatus.ATTACK;
                    } else if (status == 3) {
                        discipleStatus = DiscipleStatus.GO_HOME;
                    } else if (status == 4) {
                        discipleStatus = DiscipleStatus.FUSION;
                    }
                    if (discipleStatus != null) {
                        player.setDiscipleStatus(discipleStatus);
                    }
                    return;
                }

                case CommandName.SHOW_MAGIC_BEAN: {
                    MagicBean magicBean = player.magicBean;
                    if (magicBean == null) {
                        magicBean = player.magicBean = new MagicBean();
                    }
                    List<Command> commands = new ArrayList<>();
                    StringBuilder content = new StringBuilder();
                    content.append(String.format("Đậu thần hiện tại đạt cấp %d", magicBean.level));
                    content.append("\n");
                    long now = System.currentTimeMillis();
                    if (magicBean.isUpdate) {
                        long time = MagicBean.MINUTES[magicBean.level] * 60000L + magicBean.updateTime - now;
                        if (time <= 0) {
                            magicBean.isUpdate = false;
                            magicBean.level++;
                            magicBean.updateTime = 0;
                        }
                    }
                    if (magicBean.isUpdate) {
                        content.append(String.format("Đang nâng cấp lên cấp %d", magicBean.level + 1));
                        content.append("\n");
                        long time = MagicBean.MINUTES[magicBean.level] * 60000L + magicBean.updateTime - now;
                        content.append(String.format("Thời gian còn lại: %s", Utils.formatTime(time)));
                        content.append("\n");
                        content.append("Không thể hủy nâng cấp");
                        int diamond = (int) (time / 600000);
                        if (diamond < 100) {
                            diamond = 100;
                        }
                        commands.add(new Command(CommandName.UPGRADE_MAGIC_BEAN_NOW, String.format("Nâng cấp\nnhanh\n%d KC", diamond), player));
                        commands.add(new Command(CommandName.CANCEL, "Đóng", player));
                    } else {
                        int count = (int) Math.min((now - magicBean.updateTime) / (magicBean.level * 60000L), 5L + (magicBean.level - 1) * 2L);
                        if (count < 0) {
                            count = 0;
                        }
                        content.append(String.format("Số lượng còn lại %d hạt", count));
                        content.append("\n");
                        content.append(String.format("Ta mất %d phút để tạo ra 1 hạt", magicBean.level));
                        content.append("\n");
                        long coin = MagicBean.COINS[magicBean.level];
                        long time = MagicBean.MINUTES[magicBean.level] * 60000L;
                        content.append(String.format("Nâng cấp lên cấp %d sẽ tốn %s xu, thời gian nâng cấp là %s", magicBean.level + 1, Utils.formatNumber(coin), Utils.formatTime(time)));
                        commands.add(new Command(CommandName.THU_HOACH_MAGIC_BEAN, "Thu hoạch", player));
                        commands.add(new Command(CommandName.UPGRADE_MAGIC_BEAN, "Nâng cấp", player));
                    }
                    player.createMenu(((Npc) elements[0]).template.id, content.toString(), commands);
                    return;
                }

                case CommandName.THU_HOACH_MAGIC_BEAN: {
                    MagicBean magicBean = player.magicBean;
                    if (magicBean == null || magicBean.isUpdate) {
                        return;
                    }
                    magicBean.harvest(player);
                    return;
                }

                case CommandName.UPGRADE_MAGIC_BEAN_NOW: {
                    MagicBean magicBean = player.magicBean;
                    if (magicBean == null) {
                        return;
                    }
                    magicBean.upgradeNow(player);
                    return;
                }

                case CommandName.UPGRADE_MAGIC_BEAN: {
                    MagicBean magicBean = player.magicBean;
                    if (magicBean == null) {
                        return;
                    }
                    magicBean.upgrade(player);
                    return;
                }

                case CommandName.CONFIRM_UPGRADE_MAGIC_BEAN: {
                    MagicBean magicBean = player.magicBean;
                    if (magicBean == null) {
                        return;
                    }
                    magicBean.confirmUpgrade(player);
                    return;
                }

                case CommandName.CONFIRM_UPGRADE_MAGIC_BEAN_NOW: {
                    MagicBean magicBean = player.magicBean;
                    if (magicBean == null) {
                        return;
                    }
                    magicBean.confirmUpgradeNow(player);
                    return;
                }

                case CommandName.CALL_DRAGON: {
                    Dragon dragon = (Dragon) elements[0];
                    if (dragon != null) {
                        dragon.showMenu(player);
                    }
                    return;
                }

                case CommandName.SUMMON_DRAGON: {
                    Dragon dragon = (Dragon) elements[0];
                    if (dragon != null) {
                        dragon.summonDragon(player);
                    }
                    return;
                }

                case CommandName.WISH_DRAGON: {
                    Dragon dragon = (Dragon) elements[0];
                    if (dragon != null) {
                        dragon.wish(player, (int) elements[1]);
                    }
                    return;
                }

                case CommandName.CONFIRM_WISH_DRAGON: {
                    Dragon dragon = (Dragon) elements[0];
                    if (dragon != null) {
                        dragon.confirm(player, (int) elements[1]);
                    }
                    return;
                }

                case CommandName.COMBINE: {
                    Upgrade upgrade = UpgradeManager.getInstance().upgrades.get(UpgradeType.COMBINE);
                    if (upgrade != null) {
                        upgrade.showTab(player);
                    }
                    return;
                }

                case CommandName.EQUIP_CRAFTING_3X: {
                    Upgrade upgrade = UpgradeManager.getInstance().upgrades.get(UpgradeType.EQUIP_CRAFTING_3X);
                    if (upgrade != null) {
                        upgrade.showTab(player);
                    }
                    return;
                }

                case CommandName.EQUIP_CRAFTING_4X: {
                    Upgrade upgrade = UpgradeManager.getInstance().upgrades.get(UpgradeType.EQUIP_CRAFTING_4X);
                    if (upgrade != null) {
                        upgrade.showTab(player);
                    }
                    return;
                }

                case CommandName.SHOW_EQUIP_CRAFTING: {
                    List<Command> commands = new ArrayList<>();
                    commands.add(new Command(CommandName.EQUIP_CRAFTING_3X, "Trang bị 3x", player));
                    commands.add(new Command(CommandName.EQUIP_CRAFTING_4X, "Trang bị 4x", player));
                    commands.add(new Command(CommandName.EQUIP_CRAFTING_6X, "Trang bị 6x", player));
                    if (elements == null || elements.length == 0) {
                        player.createMenu(NpcName.ME, "Cậu muốn chế tạo gì?", commands);
                    } else {
                        player.createMenu(((Npc) elements[0]).template.id, "Cậu muốn chế tạo gì?", commands);
                    }
                    return;
                }

                case CommandName.OPEN_LIMIT_POWER: {
                    List<Command> commands = new ArrayList<>();
                    commands.add(new Command(CommandName.OPEN_LIMIT_POWER_MASTER, "Sư phụ", player));
                    commands.add(new Command(CommandName.OPEN_LIMIT_POWER_DISCIPLE, "Đệ tử", player));
                    if (elements == null || elements.length == 0) {
                        player.createMenu(NpcName.ME, "Mở giới hạn sức mạnh sẽ giúp bản thân con hoặc đệ tử mạnh mẽ hơn", commands);
                    } else {
                        player.createMenu(((Npc) elements[0]).template.id, "Mở giới hạn sức mạnh sẽ giúp bản thân con hoặc đệ tử mạnh mẽ hơn", commands);
                    }
                    return;
                }

                case CommandName.OPEN_LIMIT_POWER_MASTER: {
                    StringBuilder text = new StringBuilder();
                    List<Command> commands = new ArrayList<>();
                    Effect effect = player.findEffectByTemplateId(EffectName.MO_GIOI_HAN_SUC_MANH);
                    text.append(String.format("Sức mạnh hiện tại của con là %s (cấp %d)", Utils.formatNumber(player.power), player.level));
                    text.append("\n");
                    text.append(String.format("Giới hạn sức mạnh hiện tại của con là cấp %d", player.limitLevel));
                    text.append("\n");
                    if (effect == null) {
                        text.append(String.format("Con có muốn mở giới hạn sức mạnh lên cấp %d không?", player.limitLevel + 1));
                        text.append("\n");
                        if (player.limitLevel >= 49) {
                            text.append("Yêu cầu set trang bị 4x +12 trở lên");
                        }
                        text.append("\n");
                        text.append("(Sử dụng kim cương sẽ mở giới hạn ngay lập tức)");
                        text.append("\n");
                        text.append("(Sử dụng xu sẽ tốn 8h)");
                        int diamond = 100 + (50 * (Math.max(0, player.limitLevel - 40) / 10));
                        commands.add(new Command(CommandName.OPEN_LIMIT_POWER_BY_DIAMOND_MASTER, String.format("Sử dụng\n%d KC", diamond), player));
                        commands.add(new Command(CommandName.OPEN_LIMIT_POWER_BY_COIN_MASTER, String.format("Sử dụng\n%s xu", Utils.formatNumber(10000000L * (player.limitLevel - 40) + 30000000L)), player));
                    } else {
                        text.append(String.format("Hiện tại đang mở giới hạn sức mạnh lên cấp %d", player.limitLevel + 1));
                        text.append("\n");
                        text.append(String.format("Thời gian còn lại: %s", Utils.formatTime(effect.time)));
                        commands.add(new Command(CommandName.OPEN_LIMIT_POWER_BY_DIAMOND_MASTER, "Mở ngay\n100 KC", player));
                        commands.add(new Command(CommandName.CANCEL, "Đóng", player));
                    }
                    player.createMenu(NpcName.ME, text.toString(), commands);
                    return;
                }

                case CommandName.OPEN_LIMIT_POWER_BY_DIAMOND_MASTER: {
                    if (player.level >= Server.MAX_LEVEL || player.limitLevel >= Server.MAX_LEVEL) {
                        player.addInfo(Player.INFO_RED, "Cấp độ đã đạt tối đa");
                        return;
                    }
                    if (player.limitLevel >= 49) {
                        for (int i = 0; i < 8; i++) {
                            Item item = player.itemsBody[i];
                            if (item == null || item.template.levelRequire < 40 || item.getUpgrade() < 12) {
                                player.addInfo(Player.INFO_RED, "Yêu cầu set trang bị 4x +12 trở lên");
                                return;
                            }
                        }
                    }
                    int diamond = 100 + (50 * (Math.max(0, player.limitLevel - 40) / 10));
                    if (!player.isEnoughMoney(TypePrice.RUBY, diamond)) {
                        return;
                    }
                    player.downMoney(TypePrice.RUBY, diamond);
                    player.removeEffectById(EffectName.MO_GIOI_HAN_SUC_MANH);
                    player.limitLevel++;
                    player.addInfo(Player.INFO_YELLOW, String.format("Giới hạn sức mạnh của bạn đã được nâng lên cấp %d", player.limitLevel));
                    return;
                }

                case CommandName.OPEN_LIMIT_POWER_BY_COIN_MASTER: {
                    if (player.level < player.limitLevel) {
                        player.addInfo(Player.INFO_RED, String.format("Bạn cần phải đạt cấp độ %d để có thể mở giới hạn sức mạnh tiếp", player.limitLevel));
                        return;
                    }
                    if (player.limitLevel >= Server.MAX_LEVEL) {
                        player.addInfo(Player.INFO_RED, "Cấp độ đã đạt tối đa");
                        return;
                    }
                    Effect effect = player.findEffectByTemplateId(EffectName.MO_GIOI_HAN_SUC_MANH);
                    if (effect != null) {
                        player.addInfo(Player.INFO_RED, Language.CANT_ACTION);
                        return;
                    }
                    if (player.limitLevel >= 49) {
                        for (int i = 0; i < 8; i++) {
                            Item item = player.itemsBody[i];
                            if (item == null || item.template.levelRequire < 40 || item.getUpgrade() < 12) {
                                player.addInfo(Player.INFO_RED, "Yêu cầu set trang bị 4x +12 trở lên");
                                return;
                            }
                        }
                    }
                    long coin = 10000000L * (player.limitLevel - 40) + 30000000L;
                    if (player.xu < coin) {
                        player.addInfo(Player.INFO_RED, String.format("Bạn còn thiếu %s xu", Utils.getMoneys(coin - player.xu)));
                        return;
                    }
                    player.upXu(-coin);
                    player.addEffect(new Effect(player, EffectName.MO_GIOI_HAN_SUC_MANH, 60000L * 60 * 8));
                    return;
                }

                case CommandName.OPEN_LIMIT_POWER_DISCIPLE: {
                    Disciple disciple = player.disciple;
                    if (disciple == null) {
                        player.addInfo(Player.INFO_RED, "Hiện tại bạn chưa có đệ tử");
                        return;
                    }
                    StringBuilder text = new StringBuilder();
                    List<Command> commands = new ArrayList<>();
                    text.append(String.format("Sức mạnh hiện tại của đệ tử là %s (cấp %d)", Utils.formatNumber(disciple.power), disciple.level));
                    text.append("\n");
                    text.append(String.format("Giới hạn sức mạnh hiện tại của đệ tử là cấp %d", disciple.limitLevel));
                    text.append("\n");
                    text.append(String.format("Con có muốn mở giới hạn sức mạnh cho đệ tử lên cấp %d không?", disciple.limitLevel + 1));
                    text.append("\n");
                    if (disciple.limitLevel < 50) {
                        text.append("Yêu cầu set trang bị 3x +10 trở lên");
                    } else {
                        text.append("Yêu cầu set trang bị 4x +12 trở lên");
                    }
                    int diamond = 100 + (50 * (Math.max(0, disciple.limitLevel - 40) / 10));
                    commands.add(new Command(CommandName.OPEN_LIMIT_POWER_BY_DIAMOND_DISCIPLE, String.format("Mở\ngiới hạn\n%d KC", diamond), player));
                    commands.add(new Command(CommandName.CANCEL, "Đóng", player));
                    player.createMenu(NpcName.ME, text.toString(), commands);
                    return;
                }

                case CommandName.OPEN_LIMIT_POWER_BY_DIAMOND_DISCIPLE: {
                    Disciple disciple = player.disciple;
                    if (disciple == null) {
                        return;
                    }
                    if (disciple.limitLevel >= Server.MAX_LEVEL) {
                        player.addInfo(Player.INFO_RED, "Cấp độ đã đạt tối đa");
                        return;
                    }
                    if (disciple.limitLevel >= 49) {
                        for (int i = 0; i < 8; i++) {
                            Item item = disciple.itemsBody[i];
                            if (item == null || item.template.levelRequire < 40 || item.getUpgrade() < 12) {
                                player.addInfo(Player.INFO_RED, "Yêu cầu set trang bị 4x +12 trở lên");
                                return;
                            }
                        }
                    }
                    int diamond = 100 + (50 * (Math.max(0, disciple.limitLevel - 40) / 10));
                    if (!player.isEnoughMoney(TypePrice.RUBY, diamond)) {
                        return;
                    }
                    player.downMoney(TypePrice.RUBY, diamond);
                    disciple.limitLevel++;
                    player.addInfo(Player.INFO_YELLOW, String.format("Giới hạn sức mạnh của đệ tử đã được nâng lên cấp %d", disciple.limitLevel));
                    return;
                }

                case CommandName.TTVT_NAMEK_TO_LANG_CO_GIRA: {
                    /*if (player.pointActive < 100) {
                        player.addInfo(Player.INFO_RED, "Điểm năng động từ 100 trở lên mới có thể sử dụng tính năng này");
                        return;
                    }*/
                    if (player.taskMain == null || player.taskMain.template.id < 15) {
                        ((Npc) elements[0]).chat(player, "Con cần phải làm xong nhiệm vụ Bất khả thi");
                        return;
                    }
                    DarkVillage village = MapManager.getInstance().village;
                    if (village == null) {
                        ((Npc) elements[0]).chat(player, "Làng cổ chỉ mở cửa vào 23h hàng ngày");
                        return;
                    }
                    village.enter(player);
                    return;
                }

                case CommandName.SHOW_TOP_POWER: {
                    Top top = TopManager.getInstance().tops.get(TopType.POWER_MASTER);
                    if (top != null) {
                        top.show(player);
                    }
                    return;
                }

                case CommandName.SHOW_TOP_DISCIPLE: {
                    Top top = TopManager.getInstance().tops.get(TopType.POWER_DISCIPLE);
                    if (top != null) {
                        top.show(player);
                    }
                    return;
                }

                case CommandName.THONG_TIN_DAI_HOI_VO_THUAT: {
                    player.service.showNotification(MartialArtsFestival.notes);
                    return;
                }

                case CommandName.CREATE_DAI_HOI_VO_THUAT: {
                    List<Command> commands = new ArrayList<>();
                    StringBuilder content = new StringBuilder();
                    content.append("Lệ phí tạo giải đấu là 100 kim cương");
                    content.append("\n");
                    content.append("Giải đấu sẽ bị hủy nếu trùng giờ với giải của Máy chủ");
                    commands.add(new Command(CommandName.CONFIRM_CREATE_DAI_HOI_VO_THUAT, "Tạo ngay", player));
                    commands.add(new Command(CommandName.CANCEL, "Không", player));
                    player.createMenu(((Npc) elements[0]).template.id, content.toString(), commands);
                    return;
                }

                case CommandName.DANG_KY_DAI_HOI_VO_THUAT: {
                    if (MapManager.getInstance().martialArtsFestival != null) {
                        MapManager.getInstance().martialArtsFestival.addWarrior(player);
                    }
                    return;
                }

                case CommandName.CHIEN_TRUONG_NGOC_RONG_NAMEK: {
                    List<Command> commands = new ArrayList<>();
                    commands.add(new Command(CommandName.THAM_GIA_NRNM, "Tham gia", player));
                    if (player.isHaveEffect(EffectName.NGOC_RONG_NAMEK_3_SAO, EffectName.NGOC_RONG_NAMEK_4_SAO, EffectName.NGOC_RONG_NAMEK_5_SAO, EffectName.NGOC_RONG_NAMEK_6_SAO, EffectName.NGOC_RONG_NAMEK_7_SAO)) {
                        commands.add(new Command(CommandName.DANH_SACH_THUONG_NRNM, "Nhận thưởng", player));
                    }
                    commands.add(new Command(CommandName.HUONG_DAN_NRNM, "Thông tin", player));
                    player.createMenu(NpcName.ME, "Chiến trường Ngọc rồng Namek đem lại rất nhiều phần thưởng hấp dẫn", commands);
                    return;
                }

                case CommandName.THAM_GIA_NRNM: {
                    if (MapManager.getInstance().dragonBallNamek != null) {
                        DragonBallNamek dragonBallNamek = MapManager.getInstance().dragonBallNamek;
                        ArrayList<KeyValue<Map, String>> maps = new ArrayList<>();
                        for (Map map : dragonBallNamek.maps) {
                            List<Zone> zoneList = map.zones.stream().filter(z -> !((ZoneDragonBallNamek) z).isReward).collect(Collectors.toList());
                            if (!zoneList.isEmpty()) {
                                maps.add(new KeyValue<>(map, map.template.name, String.format("Còn %d khu vực", zoneList.size())));
                            }
                        }
                        if (maps.isEmpty()) {
                            player.addInfo(Player.INFO_RED, "Trận chiến đã kết thúc");
                            return;
                        }
                        player.showListMapSpaceship(maps);
                    } else {
                        player.addInfo(Player.INFO_RED, "Chiến trường Ngọc rồng Namek chưa mở cửa");
                    }
                    return;
                }

                case CommandName.HUONG_DAN_NRNM: {
                    player.service.showNotification(DragonBallNamek.notes);
                    return;
                }

                case CommandName.DANH_SACH_THUONG_NRNM: {
                    HashMap<Integer, String> names = new HashMap<>();
                    names.put(EffectName.NGOC_RONG_NAMEK_3_SAO, "3 sao");
                    names.put(EffectName.NGOC_RONG_NAMEK_4_SAO, "4 sao");
                    names.put(EffectName.NGOC_RONG_NAMEK_5_SAO, "5 sao");
                    names.put(EffectName.NGOC_RONG_NAMEK_6_SAO, "6 sao");
                    names.put(EffectName.NGOC_RONG_NAMEK_7_SAO, "7 sao");
                    List<Command> commands = new ArrayList<>();
                    long now = System.currentTimeMillis();
                    List<Effect> effects = player.getEffects();
                    for (Effect effect : effects) {
                        if (effect.template.id == EffectName.NGOC_RONG_NAMEK_3_SAO || effect.template.id == EffectName.NGOC_RONG_NAMEK_4_SAO || effect.template.id == EffectName.NGOC_RONG_NAMEK_5_SAO || effect.template.id == EffectName.NGOC_RONG_NAMEK_6_SAO || effect.template.id == EffectName.NGOC_RONG_NAMEK_7_SAO) {
                            long time = effect.delay + effect.updateTime - now;
                            if (time > 0) {
                                commands.add(new Command(CommandName.NHAN_THUONG_NRNM, String.format("%s\n%s", names.get(effect.template.id), Utils.formatTime(time)), player, effect.template.id));
                            } else {
                                commands.add(new Command(CommandName.NHAN_THUONG_NRNM, names.get(effect.template.id), player, effect.template.id));
                            }
                        }
                    }
                    if (commands.isEmpty()) {
                        player.addInfo(Player.INFO_RED, "Hiện tại không có phần thưởng nào");
                        return;
                    }
                    player.createMenu(NpcName.ME, "Nhận thưởng Ngọc rồng Namek", commands);
                    return;
                }

                case CommandName.NHAN_THUONG_NRNM: {
                    DragonBallNamek.reward(player, (int) elements[0]);
                    return;
                }

                case CommandName.XOA_HIEU_UNG_BUA: {
                    Command yes = new Command(CommandName.CONFIRM_XOA_HIEU_UNG_BUA, "Xóa", player);
                    Command no = new Command(CommandName.CANCEL, "Đóng", player);
                    player.startYesNo("Bạn có chắc chắn muốn xóa hết hiệu ứng bùa thời gian không?", yes, no);
                    return;
                }

                case CommandName.CONFIRM_XOA_HIEU_UNG_BUA: {
                    List<Effect> effects = player.getEffects();
                    long now = System.currentTimeMillis();
                    for (Effect effect : effects) {
                        if (effect.template.id == EffectName.BUA_TRI_TUE || effect.template.id == EffectName.BUA_SUC_MANH || effect.template.id == EffectName.BUA_DE_TU || effect.template.id == EffectName.BUA_THU_HUT || effect.template.id == EffectName.BUA_CUNG_CAP || effect.template.id == EffectName.BUA_NGUNG_DONG || effect.template.id == EffectName.BUA_BAO_HO || effect.template.id == EffectName.BUA_MAY_MAN) {
                            effect.time = 0;
                            effect.endTime = now;
                        }
                    }
                    player.addInfo(Player.INFO_YELLOW, "Đã xóa hết hiệu ứng bùa thời gian");
                    return;
                }

                case CommandName.SHOW_MENU_PHU_HO: {
                    DragonBallNamek dragonBallNamek = MapManager.getInstance().dragonBallNamek;
                    if (dragonBallNamek == null) {
                        return;
                    }
                    List<Command> commands = new ArrayList<>();
                    LinkedHashMap<String, Integer> names = new LinkedHashMap<>();
                    names.put("x10 HP", 10);
                    names.put("x4 SĐ", 5);
                    names.put("x4 KI", 5);
                    for (String key : names.keySet()) {
                        commands.add(new Command(CommandName.PHU_HO_NRNM, String.format("%s\n%d Ruby", key, names.get(key)), player, key));
                    }
                    player.createMenu(NpcName.ME, "Phù hộ sẽ giúp bản thân mạnh mẽ hơn", commands);
                    return;
                }

                case CommandName.PHU_HO_NRNM: {
                    DragonBallNamek dragonBallNamek = MapManager.getInstance().dragonBallNamek;
                    if (dragonBallNamek == null) {
                        return;
                    }
                    long time = dragonBallNamek.endTime - System.currentTimeMillis();
                    if (time <= 0) {
                        break;
                    }
                    LinkedHashMap<String, Integer[]> names = new LinkedHashMap<>();
                    names.put("x10 HP", new Integer[]{10, 10});
                    names.put("x4 SĐ", new Integer[]{5, 4});
                    names.put("x4 KI", new Integer[]{5, 4});
                    String key = (String) elements[0];
                    if (key == null || !names.containsKey(key)) {
                        return;
                    }
                    if (key.contains("HP")) {
                        Effect effect = player.findEffectByTemplateId(EffectName.PHU_HO_HP);
                        if (effect != null) {
                            player.addInfo(Player.INFO_RED, "Không thể phù hộ thêm");
                            return;
                        }
                        int diamond = names.get(key)[0];
                        if (!player.isEnoughMoney(TypePrice.RUBY, diamond)) {
                            return;
                        }
                        player.downMoney(TypePrice.RUBY, diamond);
                        player.addEffect(new Effect(player, EffectName.PHU_HO_HP, time, 0, names.get(key)[1]));
                        player.service.setInfo();
                        player.recoverSkill();
                        if (player.hp != player.maxHp) {
                            player.recovery(Player.RECOVERY_HP, 100, true);
                        }
                        return;
                    }
                    if (key.contains("KI")) {
                        Effect effect = player.findEffectByTemplateId(EffectName.PHU_HO_KI);
                        if (effect != null) {
                            player.addInfo(Player.INFO_RED, "Không thể phù hộ thêm");
                            return;
                        }
                        int diamond = names.get(key)[0];
                        if (!player.isEnoughMoney(TypePrice.RUBY, diamond)) {
                            return;
                        }
                        player.downMoney(TypePrice.RUBY, diamond);
                        player.addEffect(new Effect(player, EffectName.PHU_HO_KI, time, 0, names.get(key)[1]));
                        player.service.setInfo();
                        player.recoverSkill();
                        if (player.mp != player.maxMp) {
                            player.recovery(Player.RECOVERY_MP, 100, true);
                        }
                        return;
                    }
                    if (key.contains("SĐ")) {
                        Effect effect = player.findEffectByTemplateId(EffectName.PHU_HO_SUC_DANH);
                        if (effect != null) {
                            player.addInfo(Player.INFO_RED, "Không thể phù hộ thêm");
                            return;
                        }
                        int diamond = names.get(key)[0];
                        if (!player.isEnoughMoney(TypePrice.RUBY, diamond)) {
                            player.addInfo(Player.INFO_RED, "Bạn không đủ Ruby");
                            return;
                        }
                        player.downMoney(TypePrice.RUBY, diamond);
                        player.addEffect(new Effect(player, EffectName.PHU_HO_SUC_DANH, time, 0, names.get(key)[1]));
                        player.service.setInfo();
                        player.recoverSkill();
                        return;
                    }
                    return;
                }

                case CommandName.BXH_DHVT: {
                    ArrayList<Top> tops = TopManager.getInstance().topMartialArtsFestival;
                    if (tops.isEmpty()) {
                        player.addInfo(Player.INFO_RED, "Bảng xếp hạng trống");
                        return;
                    }
                    List<Command> commands = new ArrayList<>();
                    for (Top top : tops) {
                        commands.add(new Command(CommandName.SHOW_BXH_DHVT, top.name, player, top));
                    }
                    player.createMenu(NpcName.ME, "Bảng xếp các giải đấu trước", commands);
                    return;
                }

                case CommandName.SHOW_BXH_DHVT: {
                    Top top = (Top) elements[0];
                    if (top != null) {
                        top.show(player);
                    }
                    return;
                }

                case CommandName.THANH_PHO_LANG_QUEN: {
                    List<Command> commands = new ArrayList<>();
                    StringBuilder content = new StringBuilder();
                    ForgottenCity forgottenCity = MapManager.getInstance().forgottenCity;
                    if (forgottenCity == null) {
                        content.append("Thành phố lãng quên mở cửa từ 21h30 đến 22h30 hàng ngày");
                        commands.add(new Command(CommandName.CANCEL, "Đóng", player));
                    } else {
                        content.append("Thành phố lãng quên đã mở cửa, ngươi có muốn tham gia không?");
                        commands.add(new Command(CommandName.THAM_GIA_THANH_PHO_LANG_QUEN, "Tham gia", player, elements[0]));
                        commands.add(new Command(CommandName.CANCEL, "Không", player));
                    }
                    player.createMenu(((Npc) elements[0]).template.id, content.toString(), commands);
                    return;
                }

                case CommandName.THAM_GIA_THANH_PHO_LANG_QUEN: {
                    ForgottenCity forgottenCity = MapManager.getInstance().forgottenCity;
                    if (forgottenCity == null) {
                        player.addInfo(Player.INFO_RED, "Thành phố lãng quên chưa mở cửa");
                        return;
                    }
                    if (player.level < 39) {
                        ((Npc) elements[0]).chat(player, "Yêu cầu cấp độ tối thiểu 39");
                        return;
                    }
                    for (int i = 0; i < 8; i++) {
                        Item item = player.itemsBody[i];
                        if (item == null || item.getUpgrade() < 12) {
                            ((Npc) elements[0]).chat(player, "Yêu cầu sỡ hữu sét trang bị +12 trở lên");
                            return;
                        }
                    }
                    player.teleport(forgottenCity.maps.get(0), true);
                    return;
                }

                case CommandName.CONFIRM_CREATE_DAI_HOI_VO_THUAT: {
                    player.startClientInput(ClientInputType.INPUT_CREATE_DHVT, "Tạo giải đấu", new TextField("Cấp độ thấp nhất có thể tham gia (1-1000)", TextField.TYPE_NUMBER), new TextField("Cấp độ cao nhất có thể tham gia (1-1000)", TextField.TYPE_NUMBER));
                    return;
                }

                case CommandName.TRAO_THUONG_DHVT: {
                    player.startClientInput(ClientInputType.INPUT_TRAO_THUONG_DHVT, "Trao thưởng", new TextField("Top 1 (tối thiểu 10tr xu)", TextField.TYPE_NUMBER), new TextField("Top 2 (tối thiểu 10tr xu)", TextField.TYPE_NUMBER), new TextField("Top 3 (tối thiểu 10tr xu)", TextField.TYPE_NUMBER), new TextField("Top 4-10 (tối thiểu 10tr xu)", TextField.TYPE_NUMBER));
                    return;
                }

                case CommandName.HISTORY_DHVT: {
                    if (MartialArtsFestival.histories.isEmpty()) {
                        return;
                    }
                    List<String> notes = new ArrayList<>();
                    for (Integer round : MartialArtsFestival.histories.keySet()) {
                        notes.add(String.format("Vòng %s:", round));
                        for (String history : MartialArtsFestival.histories.get(round)) {
                            notes.add(String.format("- %s", history));
                        }
                        notes.add("");
                    }
                    player.service.showNotification(notes);
                    return;
                }

                case CommandName.SHOW_LIST_WARRIOR_DHVT: {
                    MartialArtsFestival festival = MapManager.getInstance().martialArtsFestival;
                    if (festival == null) {
                        return;
                    }
                    HashMap<Integer, Warrior> warriors = festival.getWarriors();
                    if (warriors.isEmpty()) {
                        player.addInfo(Player.INFO_RED, "Danh sách trống");
                        return;
                    }
                    List<MemberMiniGame> members = new ArrayList<>();
                    for (Warrior warrior : warriors.values()) {
                        MemberMiniGame member = new MemberMiniGame();
                        member.playerId = warrior.playerId;
                        member.name = warrior.name;
                        member.gender = warrior.gender;
                        Player player = PlayerManager.getInstance().findPlayerById(warrior.playerId);
                        if (player != null) {
                            member.isOnline = true;
                            if (player.clan != null) {
                                member.name = String.format("%s (%s)", player.name, player.clan.name);
                            }
                        } else {
                            member.isOnline = false;
                        }
                        member.info = String.format("Lv%s: %s", warrior.level, Utils.formatNumber(warrior.power));
                        members.add(member);
                    }
                    player.service.showMemberMiniGame(members);
                    return;
                }

                case CommandName.MOVE_DHVT: {
                    if (player.zone != null && player.zone.map.template.id == MapName.DAI_HOI_VO_THUAT) {
                        int type = (int) elements[0];
                        if (type == 0) {
                            player.x = Flight.POSITION[0][0];
                            player.y = Flight.POSITION[0][1];
                        } else if (type == 1) {
                            player.x = Flight.POSITION_WAIT[0];
                            player.y = Flight.POSITION_WAIT[1];
                        }
                        player.zone.service.setPosition(player);
                    }
                    return;
                }

                case CommandName.VIEW_INFO_PLAYER: {
                    player.viewInfoPlayer((int) elements[0]);
                    return;
                }

                case CommandName.SHOW_LIST_UPGRADE_SKILL: {
                    List<Command> commands = new ArrayList<>();
                    commands.add(new Command(CommandName.HUONG_DAN_CUONG_HOA_SKILL, "Hướng dẫn", player));
                    for (Skill skill : player.skills) {
                        commands.add(new Command(CommandName.UPGRADE_SKILL, skill.getName(), player, skill));
                    }
                    player.createMenu(((Npc) elements[0]).template.id, "Cường hóa kỹ năng sẽ giúp con mạnh mẽ hơn", commands);
                    return;
                }

                case CommandName.UPGRADE_SKILL: {
                    Skill skill = (Skill) elements[0];
                    List<Command> commands = new ArrayList<>();
                    StringBuilder content = new StringBuilder();
                    content.append(String.format("Cường hóa %s", skill.getName())).append("\n");
                    content.append(String.format("Yêu cầu cấp %d", skill.template.levelRequire[1])).append("\n");
                    content.append(String.format("Cần sử dụng %d Bí kíp kỹ năng", skill.getQuantityItem()[0])).append("\n");
                    content.append(String.format("Cần sử dụng %d Lõi kỹ năng", skill.getQuantityItem()[1])).append("\n");
                    content.append("Cần 1 điểm kỹ năng").append("\n");
                    content.append(String.format("Tiêu tốn %d kim cương", Math.max(skill.getDiamondUpgrade() / 5, 10))).append("\n");
                    content.append("Tỉ lệ thành công ").append(skill.getPercentUpgrade()).append("%").append("\n");
                    content.append("Cường hóa thất bại sẽ mất " + skill.getQuantityItem()[0] / 5 + " Bí kíp kỹ năng và " + skill.getQuantityItem()[1] / 5 + " Lõi kỹ năng");
                    commands.add(new Command(CommandName.CONFIRM_UPGRADE_SKILL, "Cường hóa", player, skill));
                    commands.add(new Command(CommandName.CANCEL, "Hủy", player));
                    player.createMenu(NpcName.ME, content.toString(), commands);
                    return;
                }

                case CommandName.CONFIRM_UPGRADE_SKILL: {
                    Skill skill = (Skill) elements[0];
                    if (player.pointSkill < 1) {
                        player.addInfo(Player.INFO_RED, "Bạn không đủ điểm kỹ năng");
                        return;
                    }
                    if (player.level < skill.template.levelRequire[1]) {
                        player.addInfo(Player.INFO_RED, String.format("Yêu cầu cấp %d trở lên", skill.template.levelRequire[1]));
                        return;
                    }
                    if (skill.level < skill.template.maxLevel[0]) {
                        player.addInfo(Player.INFO_RED, "Kỹ năng chưa đạt cấp tối đa");
                        return;
                    }
                    if (skill.upgrade >= skill.template.maxLevel[1]) {
                        player.addInfo(Player.INFO_RED, "Kỹ năng đã cường hóa tối đa");
                        return;
                    }
                    int point = skill.getPointUpgrade();
                    if (skill.point < point) {
                        player.addInfo(Player.INFO_RED, "Điểm thông thạo chưa đủ");
                        return;
                    }
                    int[] quantities = new int[2];
                    for (Item item : player.itemsBag) {
                        if (item != null) {
                            if (item.template.id == ItemName.BI_KIP_KY_NANG) {
                                quantities[0] += item.quantity;
                            }
                            if (item.template.id == ItemName.LOI_KI_NANG) {
                                quantities[1] += item.quantity;
                            }
                        }
                    }
                    int[] require = skill.getQuantityItem();
                    if (quantities[0] < require[0] || quantities[1] < require[1]) {
                        player.addInfo(Player.INFO_RED, "Bạn không đủ Bíp kíp kỹ năng hoặc Lõi kỹ năng");
                        return;
                    }
                    int diamond = Math.max(skill.getDiamondUpgrade() / 5, 10);
                    if (player.diamond < diamond) {
                        player.addInfo(Player.INFO_RED, "Bạn không đủ kim cương");
                        return;
                    }
                    player.upDiamond(-diamond);
                    int percent = skill.getPercentUpgrade();
                    if (Utils.nextInt(100) < percent) {
                        player.removeQuantityItemBagById(ItemName.BI_KIP_KY_NANG, require[0]);
                        player.removeQuantityItemBagById(ItemName.LOI_KI_NANG, require[1]);
                        player.pointSkill--;
                        player.service.refreshSkill(1, null);
                        if (skill.template.isProactive) {
                            skill.point = 0;
                            player.upPointUpgradeSkill(player.skills.get(1), 1);
                        } else {
                            skill.point -= point;
                        }
                        skill.upgrade++;
                        player.service.refreshSkill(3, skill);
                        player.addInfo(Player.INFO_YELLOW, String.format("Cường hóa thành công %s lên cấp %d", skill.getName(), skill.upgrade));
                    } else {
                        player.removeQuantityItemBagById(ItemName.BI_KIP_KY_NANG, require[0] / 5);
                        player.removeQuantityItemBagById(ItemName.LOI_KI_NANG, require[1] / 5);
                        player.addInfo(Player.INFO_RED, "Cường hóa thất bại");
                    }
                    return;
                }

                case CommandName.HUONG_DAN_CUONG_HOA_SKILL: {
                    List<String> notes = new ArrayList<>();
                    notes.add("Con có thể tăng điểm thông thạo kỹ năng bằng các cách sau:");
                    notes.add("- Đối với kỹ năng đấm và chưởng: hạ quái chênh lệch không quá 3 cấp sẽ tăng 1 điểm, 2 điểm đối với Tinh ranh, 3 điểm đối với Đầu đàn");
                    notes.add("- Nội tại: khi cường hóa thành công các kỹ năng khác, sẽ tự động tăng điểm");
                    notes.add("- Kỹ năng Thái dương hạ san: khi sử dụng lên quái (chênh lệch không quá 3 cấp) tăng 1 điểm, lên người trong Đại hội võ thuật tăng 1 điểm, lên Boss tăng 3 điểm");
                    notes.add("- Kỹ năng Socola: khi sử dụng lên người tăng 1 điểm, lên Boss tăng 3 điểm");
                    notes.add("- Kỹ năng Trị thương: khi sử dụng lên đồng đội tăng 1 điểm, hồi sinh người khác tăng 1 điểm");
                    notes.add("- Kỹ năng Tái tạo năng lượng: trong lúc vận sức bị quái (chênh lệch không quá 3 cấp) tấn công tăng 1 điểm, bị Boss tấn công tăng 5 điểm");
                    notes.add("- Kỹ năng đặc biệt: tăng 1 điểm khi hạ Boss");
                    player.service.showNotification(notes);
                    return;
                }

                case CommandName.CLAN_SHOP: {
                    Shop shop = ShopManager.getInstance().shops.get(ShopType.SHOP_CLAN);
                    if (shop != null) {
                        shop.show(player);
                    }
                    return;
                }

                case CommandName.SINH_TON: {
                    List<Command> commands = new ArrayList<>();
                    StringBuilder content = new StringBuilder();
                    Survival survival = MapManager.getInstance().survival;
                    if (survival == null) {
                        content.append("Chiến trường sinh tồn mở cửa vào 12h trưa và 20h tối hàng ngày");
                        commands.add(new Command(CommandName.TAO_GIAI_SINH_TON, "Tạo giải", player, elements[0]));
                        commands.add(new Command(CommandName.XEM_DANH_SACH_BANG_XEP_HANG_SINH_TON, "Bảng xếp hạng", player));
                        commands.add(new Command(CommandName.HUONG_DAN_SINH_TON, "Thông tin", player));
                    } else {
                        content.append("Chiến trường sinh tồn đã mở cửa").append("\n");
                        long[] rewards = survival.rewards;
                        if (rewards[0] > 0) {
                            content.append(String.format("Phần thưởng thêm cho top 1: %s xu", Utils.formatNumber(rewards[0]))).append("\n");
                        }
                        if (rewards[1] > 0) {
                            content.append(String.format("Phần thưởng thêm cho top 2: %s xu", Utils.formatNumber(rewards[1]))).append("\n");
                        }
                        if (rewards[2] > 0) {
                            content.append(String.format("Phần thưởng thêm cho top 3: %s xu", Utils.formatNumber(rewards[2]))).append("\n");
                        }
                        if (rewards[3] > 0) {
                            content.append(String.format("Phần thưởng thêm cho top 4-10: %s xu", Utils.formatNumber(rewards[3]))).append("\n");
                        }
                        if (!survival.donors.isEmpty()) {
                            if (survival.donors.size() == 1) {
                                content.append("Nhà tài trợ: ");
                            } else {
                                content.append("Các tài trợ: ");
                            }
                            int num = 0;
                            for (String name : survival.donors.keySet()) {
                                content.append(String.format("%s (%s)", name, Utils.formatNumber(survival.donors.get(name))));
                                if (num < survival.donors.size() - 1) {
                                    content.append(", ");
                                }
                                num++;
                            }
                            content.append("\n");
                        }
                        content.append("Ngươi có muốn tham gia không?");
                        commands.add(new Command(CommandName.THAM_GIA_SINH_TON, "Tham gia", player, elements[0]));
                        commands.add(new Command(CommandName.TRAO_THUONG_SINH_TON, "Trao thưởng", player));
                        commands.add(new Command(CommandName.CANCEL, "Không", player));
                    }
                    player.createMenu(((Npc) elements[0]).template.id, content.toString(), commands);
                    return;
                }

                case CommandName.THAM_GIA_SINH_TON: {
                    Survival survival = MapManager.getInstance().survival;
                    if (survival == null) {
                        player.addInfo(Player.INFO_RED, "Chiến trường sinh tồn chưa mở cửa");
                        return;
                    }
                    if (player.isFusion()) {
                        player.addInfo(Player.INFO_RED, "Không thể tham gia khi hợp thể");
                        return;
                    }
                    /*if (player.pointActive < 100) {
                        ((Npc) elements[0]).chat(player, "Điểm năng động từ 100 trở lên mới có thể tham gia");
                        return;
                    }*/
                    Effect effect = player.findEffectByTemplateId(EffectName.TU_DONG_LUYEN_TAP);
                    if (effect != null) {
                        player.addInfo(Player.INFO_RED, "Yêu cầu tắt Tự động luyện tập trước khi tham gia");
                        return;
                    }
                    if (survival.state != ExpansionState.WAIT_REG) {
                        player.addInfo(Player.INFO_RED, "Đã hết thời gian đăng ký");
                        return;
                    }
                    player.teleport(survival.maps.get(0), true);
                    survival.join(player);
                    return;
                }

                case CommandName.CUA_KHONG_GIAN_TO_TTVT_TRAI_DAT: {
                    player.teleport(MapName.TRAM_TAU_VU_TRU, true);
                    return;
                }

                case CommandName.HUONG_DAN_SINH_TON: {
                    player.service.showNotification(Survival.notes);
                    return;
                }

                case CommandName.XEM_DANH_SACH_BANG_XEP_HANG_SINH_TON: {
                    ArrayList<Top> tops = TopManager.getInstance().topSurvival;
                    if (tops.isEmpty()) {
                        player.addInfo(Player.INFO_RED, "Bảng xếp hạng trống");
                        return;
                    }
                    List<Command> commands = new ArrayList<>();
                    for (Top top : tops) {
                        commands.add(new Command(CommandName.BANG_XEP_HANG_SINH_TON, top.name, player, top));
                    }
                    player.createMenu(NpcName.ME, "Bảng xếp các trận chiến trước", commands);
                    return;
                }

                case CommandName.BANG_XEP_HANG_SINH_TON: {
                    Top top = (Top) elements[0];
                    if (top != null) {
                        top.show(player);
                    } else {
                        player.addInfo(Player.INFO_RED, "Không có thông tin trận chiến");
                    }
                    return;
                }

                case CommandName.TAO_GIAI_SINH_TON: {
                    List<Command> commands = new ArrayList<>();
                    StringBuilder content = new StringBuilder();
                    if (Server.getInstance().isInterServer()) {
                        content.append("Cần có vé sinh tồn trong túi đồ để tạo trận chiến");
                    } else {
                        content.append("Lệ phí tạo trận chiến là 100 kim cương");
                    }
                    content.append("\n");
                    content.append("Trận chiến sẽ bị hủy nếu trùng giờ với trận chiến của Máy chủ");
                    commands.add(new Command(CommandName.CONFIRM_TAO_GIAI_SINH_TON, "Tạo ngay", player));
                    commands.add(new Command(CommandName.CANCEL, "Không", player));
                    player.createMenu(((Npc) elements[0]).template.id, content.toString(), commands);
                    return;
                }

                case CommandName.CONFIRM_TAO_GIAI_SINH_TON: {
                    player.createSurvival();
                    return;
                }

                case CommandName.SHOW_LIST_GAMER_SURVIVAL: {
                    Survival survival = MapManager.getInstance().survival;
                    if (survival == null) {
                        return;
                    }
                    ArrayList<Gamer> gamers = survival.getGamers();
                    if (gamers.isEmpty()) {
                        player.addInfo(Player.INFO_RED, "Danh sách trống");
                        return;
                    }
                    List<MemberMiniGame> members = new ArrayList<>();
                    for (Gamer gamer : gamers) {
                        MemberMiniGame member = new MemberMiniGame();
                        member.playerId = gamer.playerId;
                        member.name = gamer.name;
                        member.gender = gamer.gender;
                        Player player = PlayerManager.getInstance().findPlayerById(gamer.playerId);
                        member.isOnline = player != null;
                        if (player != null) {
                            if (player.clan != null) {
                                member.name = String.format("%s (%s)", player.name, player.clan.name);
                            }
                            member.info = String.format("%s khu vực %d", player.zone.map.template.name, player.zone.id);
                        } else {
                            member.info = "OFFLINE";
                        }
                        members.add(member);
                    }
                    player.service.showMemberMiniGame(members);
                    return;
                }

                case CommandName.GO_TO_PLAYER: {
                    player.teleportToPlayerSurvival((int) elements[0]);
                    return;
                }

                case CommandName.TRAO_THUONG_SINH_TON: {
                    player.startClientInput(ClientInputType.INPUT_TRAO_THUONG_SINH_TON, "Trao thưởng", new TextField("Top 1 (tối thiểu 10tr xu)", TextField.TYPE_NUMBER), new TextField("Top 2 (tối thiểu 10tr xu)", TextField.TYPE_NUMBER), new TextField("Top 3 (tối thiểu 10tr xu)", TextField.TYPE_NUMBER), new TextField("Top 4-10 (tối thiểu 10tr xu)", TextField.TYPE_NUMBER));
                    return;
                }

                case CommandName.SHOW_TOP_EVENT: {
                    int type = (int) elements[0];
                    if (type == -1) {
                        List<Command> commands = new ArrayList<>();
                        commands.add(new Command(CommandName.SHOW_TOP_EVENT, "Đặc biệt", player, 0));
                        commands.add(new Command(CommandName.SHOW_TOP_EVENT, "Thường", player, 1));
                        player.createMenu(NpcName.ME, "Top của bạn là bao nhiêu?", commands);
                    } else if (type == 0) {
                        Top top = TopManager.getInstance().tops.get(TopType.TOP_EVENT);
                        if (top != null) {
                            top.show(player);
                        }
                    } else if (type == 1) {
                        Top top = TopManager.getInstance().tops.get(TopType.TOP_EVENT_OTHER);
                        if (top != null) {
                            top.show(player);
                        }
                    }
                    return;
                }

                case CommandName.TRUNG_TAM_QUA_TANG: {
                    List<Command> commands = new ArrayList<>();
                    commands.add(new Command(CommandName.QUA_CUA_TOI, "Kho quà", player));
                    commands.add(new Command(CommandName.NHAP_GIFT_CODE, "Nhập mã quà tặng", player));
                    commands.add(new Command(CommandName.TANG_QUA, "Tặng quà", player));
                    player.createMenu(NpcName.ME, "Con có thể tặng vật phẩm cho người khác và nhận vật phẩm từ người khác tại đây", commands);
                    return;
                }

                case CommandName.TANG_QUA: {
                    player.addInfo(Player.INFO_RED, "Vui lòng chờ bản cập nhật sắp tới");
                    return;
                }

                case CommandName.QUA_CUA_TOI: {
                    if (player.rewards.isEmpty()) {
                        player.addInfo(Player.INFO_RED, "Danh sách quà tặng trống");
                        return;
                    }
                    player.showReward();
                    return;
                }

                case CommandName.NHAP_GIFT_CODE: {
                    player.startClientInput(ClientInputType.INPUT_GIFT_CODE, "Mã quà tặng", new TextField("mã quà tặng (5-30 kí tự)", TextField.TYPE_NORMAL));
                    return;
                }

                case CommandName.COSTUME_MERGING: {
                    Upgrade upgrade = UpgradeManager.getInstance().upgrades.get(UpgradeType.COSTUME_MERGING);
                    if (upgrade != null) {
                        upgrade.showTab(player);
                    }
                    return;
                }

                case CommandName.SHOW_SHOP_BARRACK: {
                    Shop shop = ShopManager.getInstance().shops.get(ShopType.SHOP_BARRACK);
                    if (shop != null) {
                        shop.show(player);
                    }
                    return;
                }

                case CommandName.HUONG_DAN_LUCKY: {
                    List<Command> commands = new ArrayList<>();
                    commands.add(new Command(CommandName.CANCEL, "OK", player));
                    StringBuilder content = new StringBuilder();
                    content.append("Mỗi lượt chơi có 2 giải thưởng").append("\n");
                    content.append("Được chọn tối đa 10 lần mỗi giải").append("\n");
                    content.append("Thời gian 1 lượt chọn là 2 phút").append("\n");
                    content.append("Khi hết giờ, hệ thống sẽ ngẫu nhiên chọn ra 1 người may mắn của từng giải và trao thưởng").append("\n");
                    content.append("Lưu ý: Nếu trong lúc trao giải mà người chiến trắng không online thì sẽ không nhận được giải thưởng");
                    player.createMenu(NpcName.ME, content.toString(), commands);
                    return;
                }

                case CommandName.SHOW_TAI_CHE: {
                    List<Command> commands = new ArrayList<>();
                    commands.add(new Command(CommandName.TAI_CHE_3X, "Trang bị\n3x", player));
                    commands.add(new Command(CommandName.TAI_CHE_4X, "Trang bị\n4x", player));
                    commands.add(new Command(CommandName.TAI_CHE_MAX_CHI_SO, "Tái chế đặc biệt", player));
                    player.createMenu(NpcName.ME, "", commands);
                    return;
                }

                case CommandName.TAI_CHE_3X: {
                    Upgrade upgrade = UpgradeManager.getInstance().upgrades.get(UpgradeType.REFRESH_ITEM_3X);
                    if (upgrade != null) {
                        upgrade.showTab(player);
                    }
                    return;
                }

                case CommandName.TAI_CHE_4X: {
                    Upgrade upgrade = UpgradeManager.getInstance().upgrades.get(UpgradeType.REFRESH_ITEM_4X);
                    if (upgrade != null) {
                        upgrade.showTab(player);
                    }
                    return;
                }

                case CommandName.SHOW_ACHIEVEMENT: {
                    player.service.showAchievement();
                    return;
                }

                case CommandName.SHOW_TOP_WEEK: {
                    List<Command> commands = new ArrayList<>();
                    commands.add(new Command(CommandName.SHOW_TOP_BOSS_WEEKLY, "Săn Boss", player));
                    commands.add(new Command(CommandName.SHOW_TOP_ACTIVE_WEEKLY, "Hoạt động", player));
                    commands.add(new Command(CommandName.SHOW_PHAN_THUONG_DUA_TOP_TUAN, "Phần thưởng", player, -1));
                    commands.add(new Command(CommandName.SHOW_LIST_DA_TRAO_TOP, "Đã trao", player));
                    commands.add(new Command(CommandName.SHOW_TOP_WEEK_HUONG_DAN, "Hướng dẫn", player));
                    player.createMenu(NpcName.ME, "", commands);
                    return;
                }

                case CommandName.SHOW_TOP_HERO: {
                    Top top = TopManager.getInstance().tops.get(TopType.TOP_HERO);
                    if (top != null) {
                        top.show(player);
                    }
                    return;
                }

                case CommandName.SHOW_TOP_WEEK_HUONG_DAN: {
                    List<String> texts = new ArrayList<>();
                    texts.add("Top Tuần sẽ chốt và trao giải vào 0h0p thứ 2 hàng tuần\n");
                    texts.add("Tích lũy điểm như sau:\n");
                    texts.add("1) Top Săn Boss\n");
                    texts.add(" - Tham gia hạ Boss sẽ nhận được số điểm tương đương với lượng sát thương gây ra\n");
                    texts.add(" - Khi kết liễu Boss sẽ nhận thêm điểm HP của boss\n");
                    texts.add("2) Top Hoạt động\n");
                    texts.add(" - Hoàn thành 1 nhiệm vụ hàng ngày nhận 1 điểm (Tối đa 30đ/ngày)\n");
                    texts.add(" - Chiến thắng mỗi vòng Đại hội võ thuật nhận số điểm tương ứng với vòng tham gia\n");
                    texts.add(" - Phá đảo Bản doanh Red nhận 5 điểm\n");
                    texts.add(" - Phá đảo Lãnh địa Bang hội nhận 10 điểm\n");
                    texts.add(" - Chiến thắng Ngọc Rồng Namek 10 điểm với mỗi khu vực\n");
                    texts.add(" - Sống sót trong Phó bản Sinh tồn nhận số điểm tương ứng với số phút sống sót (cộng dồn mỗi phút)\n");
                    texts.add(" - Tham gia Động kho báu tới phút cuối nhận 10 điểm\n");
                    player.service.setNotification(texts);
                    return;
                }

                case CommandName.LANH_DIA_BANG_HOI: {
                    if (player.clan != null) {
                        player.teleport(player.clan.map, true);
                    }
                    return;
                }

                case CommandName.NAMEK_TO_EARTH:
                case CommandName.LANH_DIA_BANG_TO_TTVT_TRAI_DAT: {
                    player.teleport(MapName.TRAM_TAU_VU_TRU, false);
                    return;
                }

                /*case CommandName.THONG_TIN_SU_KIEN: {
                    if (Event.isEvent()) {
                        player.service.startDialogOk("Thông tin chi tiết xem tại website rongthanonline.vn");
                    }
                    return;
                }

                case CommandName.SHOW_EXCHANGE_EVENT: {
                    if (Event.isEvent()) {
                        Event.event.showMenuExchange(player);
                    }
                    return;
                }

                case CommandName.MENU_EVENT_EXCHANGE_QUANTITY: {
                    if (Event.isEvent()) {
                        Event.event.exchangeQuantity(player, (int) elements[0]);
                    }
                    return;
                }

                case CommandName.CONFIRM_EVENT_EXCHANGE_QUANTITY: {
                    if (Event.isEvent()) {
                        Event.event.exchange(player, (int) elements[0], (int) elements[1]);
                    }
                    return;
                }*/

                case CommandName.SHOW_EVENT: {
                    if (Event.isEvent()) {
                        if (player.isProtect) {
                            player.addInfo(Player.INFO_RED, Language.TAI_KHOAN_DANG_DUOC_BAO_VE);
                            return;
                        }
                        Event.event.openMenu(player, elements);
                    }
                    return;
                }

                /*case CommandName.SHOW_REWARD_EVENT: {
                    List<Command> commands = new ArrayList<>();
                    commands.add(new Command(CommandName.REWARD_EVENT, "Nhận", player));
                    StringBuilder content = new StringBuilder();
                    content.append(String.format("Điểm sự kiện hiện tại của bạn là: %d", player.pointEvent));
                    content.append("\n");
                    if (Event.event instanceof NationalDay) {
                        content.append("Bạn sẽ có phần thưởng tại mốc điểm 200, 500, 1000");
                    } else {
                        content.append("Bạn sẽ có phần thưởng tại mốc điểm 200, 500, 1000, 5000 và 10000");
                    }
                    content.append("\n");
                    content.append(String.format("Bạn đã nhận thưởng đến mốc: %d", player.indexRewardEvent));
                    content.append("\n");
                    content.append("Sau khi nhận thưởng, quà sẽ được chuyển về trung tâm quà tặng");
                    player.createMenu(NpcName.ME, content.toString(), commands);
                    return;
                }

                case CommandName.REWARD_EVENT: {
                    Event.event.reward(player);
                    return;
                }*/

                case CommandName.REVENGE: {
                    int playerId = (int) elements[0];
                    if (playerId == this.id) {
                        return;
                    }
                    Enemy enemy = player.enemies.stream().filter(e -> e.playerId == playerId).findFirst().orElse(null);
                    if (enemy == null) {
                        return;
                    }
                    Player target = PlayerManager.getInstance().findPlayerById(enemy.playerId);
                    if (target == null) {
                        player.addInfo(Player.INFO_RED, String.format("%s đang offline", enemy.name));
                        return;
                    }
                    if (target.zone == null) {
                        player.addInfo(Player.INFO_RED, String.format("Không tìm thấy vị trí của %s", enemy.name));
                        return;
                    }
                    Map map = target.zone.map;
                    if (!player.isCanJoinMap(map) || map.template.id == MapName.DAI_HOI_VO_THUAT || map.template.id == MapName.HANH_TINH_NGUC_TU
                            || map.template.id == MapName.DAU_TRUONG || map.isExpansion() || map.template.id == MapName.LANH_DIA_BANG_HOI) {
                        player.addInfo(Player.INFO_RED, "Không thể đến khu vực này");
                        return;
                    }
                    ((EnemyAction) player.enemyAction).revenge(target, true);
                    return;
                }

                case CommandName.SHOW_TOP_CLAN: {
                    TopManager.getInstance().showTop(player, TopType.TOP_CLAN);
                    return;
                }

                case CommandName.SHOW_TASK_CLAN: {
                    Clan clan = player.clan;
                    if (clan == null) {
                        return;
                    }
                    StringBuilder content = new StringBuilder();
                    List<Command> commands = new ArrayList<>();
                    TaskClan taskClan = clan.taskClan;
                    if (taskClan == null) {
                        content.append("Hiện tại bang hội chưa có nhiệm vụ nào").append("\n");
                        content.append(String.format("Nhận nhiệm vụ sẽ tốn %s xu", Utils.formatNumber(clan.getCoinCreateTask()))).append("\n");
                        content.append("Chỉ bang chủ hoặc bang phó mới có thể nhận nhiệm vụ").append("\n");
                        commands.add(new Command(CommandName.RECEIVE_TASK_CLAN, "Đồng ý", player));
                        commands.add(new Command(CommandName.CANCEL, "Từ chối", player));
                    } else {
                        content.append("Bang của bạn đang có nhiệm vụ:").append("\n");
                        content.append(taskClan.toString()).append("\n");
                        content.append(String.format("Thời gian nhận: %s trước", Utils.formatTime(System.currentTimeMillis() - taskClan.startTime))).append("\n");
                        content.append("Không thể hủy nhiệm vụ").append("\n");
                        if (taskClan.param >= taskClan.maxParam) {
                            commands.add(new Command(CommandName.COMPLETE_TASK_CLAN, "Hoàn thành", player));
                        }
                        commands.add(new Command(CommandName.CANCEL, "Đóng", player));
                    }
                    player.createMenu(NpcName.ME, content.toString(), commands);
                    return;
                }

                case CommandName.RECEIVE_TASK_CLAN: {
                    Clan clan = player.clan;
                    if (clan == null) {
                        return;
                    }
                    clan.createTask(player);
                    return;
                }

                case CommandName.COMPLETE_TASK_CLAN: {
                    Clan clan = player.clan;
                    if (clan == null) {
                        return;
                    }
                    TaskClan taskClan = clan.taskClan;
                    if (taskClan != null && taskClan.param >= taskClan.maxParam) {
                        long now = System.currentTimeMillis();
                        long time = now - taskClan.startTime;
                        clan.taskClan = null;
                        clan.service.addInfo(Player.INFO_YELLOW, "Nhiệm vụ bang hội đã được hoàn thành");
                        clan.upExp(Math.max(1, 10 - (int) (time / 3600000)), true);
                        List<Player> playerList = player.zone.findAllPlayerSameClan(player);
                        for (Player p : playerList) {
                            if (p.taskMain != null && p.taskMain.template.id == 22 && p.taskMain.index == 1) {
                                p.nextTaskParam();
                            }
                        }
                    }
                    return;
                }

                case CommandName.UPGRADE_CLAN: {
                    Clan clan = player.clan;
                    if (clan == null) {
                        return;
                    }
                    List<Command> commands = new ArrayList<>();
                    StringBuilder content = new StringBuilder();
                    content.append(String.format("Hiện tại bang hội %s đang cấp %d", clan.name, clan.level)).append("\n");
                    content.append(String.format("Sau khi nâng lên cấp %d", clan.level + 1)).append("\n");
                    content.append("+ 1 tối đa số lượng thành viên").append("\n");
                    content.append(String.format("+ Mở bán lệnh bài bang cấp %d", clan.level + 1)).append("\n");
                    content.append(String.format("Phí nâng cấp là %s xu", Utils.formatNumber(clan.getCoinUpgrade())));
                    commands.add(new Command(CommandName.CONFIRM_UPGRADE_CLAN, "Đồng ý", player));
                    commands.add(new Command(CommandName.CANCEL, "Từ chối", player));
                    player.createMenu(NpcName.ME, content.toString(), commands);
                    return;
                }

                case CommandName.CONFIRM_UPGRADE_CLAN: {
                    Clan clan = player.clan;
                    if (clan == null) {
                        return;
                    }
                    clan.upgrade(player);
                    return;
                }

                case CommandName.CUA_HANG_DA_BAN: {
                    Shop shop = ShopManager.getInstance().shops.get(ShopType.SHOP_REPURCHASE);
                    if (shop != null) {
                        shop.show(player);
                    }
                    return;
                }

                case CommandName.HUONG_DAN_NANG_CAP_BANG: {
                    player.service.showNotification(Clan.notes);
                    return;
                }

                case CommandName.CUA_HANG_NANG_DONG: {
                    Shop shop = ShopManager.getInstance().shops.get(ShopType.SHOP_ACTIVE);
                    if (shop != null) {
                        shop.show(player);
                    }
                    return;
                }

                case CommandName.SHOW_MENU_TASK_DAILY: {
                    List<Command> commands = new ArrayList<>();
                    commands.add(new Command(CommandName.SHOW_TASK_DAILY, "Nhiệm vụ", player));
                    commands.add(new Command(CommandName.CUA_HANG_NANG_DONG, "Cửa hàng", player));
                    player.createMenu(NpcName.ME, "Chăm chỉ làm nhiệm vụ sẽ nhận được nhiều phần thưởng quý giá", commands);
                    return;
                }

                case CommandName.SHOW_MENU_VUNG_DAT_BI_AN: {
                    List<Command> commands = new ArrayList<>();
                    commands.add(new Command(CommandName.THAM_GIA_VUNG_DAT_BI_AN, "Tham gia", player));
                    commands.add(new Command(CommandName.HUONG_DAN_VUNG_DAT_BI_AN, "Hướng dẫn", player));
                    player.createMenu(NpcName.ME, "Vùng đất bí ẩn vô cùng nguy hiểm nhưng sẽ mang lại nhiều phần thưởng hấp dẫn cho bản thân và bang hội", commands);
                    return;
                }

                case CommandName.THAM_GIA_VUNG_DAT_BI_AN: {
                    Clan clan = player.clan;
                    if (clan == null) {
                        return;
                    }
                    if (clan.manor == null) {
                        List<Command> commands = new ArrayList<>();
                        commands.add(new Command(CommandName.MO_CUA_VUNG_DAT_BI_AN, "Mở cửa", player));
                        commands.add(new Command(CommandName.CANCEL, "Đóng", player));
                        List<Player> playerList = player.zone.findAllPlayerSameClan(player).stream().filter(Player::isCanJoinManor).collect(Collectors.toList());
                        StringBuilder content = new StringBuilder();
                        content.append("Bạn có muốn mở cửa Vùng đất bí ẩn không?").append("\n");
                        if (!playerList.isEmpty()) {
                            content.append("Các chiến binh đủ điều kiện và có thể tham gia lúc này:").append("\n");
                            if (player.isCanJoinManor()) {
                                content.append(String.format("%s (lv%d)", player.name, player.level));
                            }
                            for (Player p : playerList) {
                                if (p != player) {
                                    content.append(String.format(", %s (lv%d)", p.name, p.level));
                                }
                            }
                        } else {
                            content.append("Không có chiến binh nào đủ điều kiện tham gia");
                        }
                        content.append("\n");
                        content.append("Các chiến binh không có mặt hoặc không đủ điều kiện sẽ không thể tham gia");
                        player.createMenu(NpcName.ME, content.toString(), commands);
                    } else {
                        player.joinManor(clan.manor);
                    }
                    return;
                }

                case CommandName.MO_CUA_VUNG_DAT_BI_AN: {
                    Clan clan = player.clan;
                    if (clan == null) {
                        return;
                    }
                    if (clan.manor == null) {
                        player.openManor();
                    }
                    return;
                }

                case CommandName.HUONG_DAN_VUNG_DAT_BI_AN: {
                    player.service.showNotification(Manor.notes);
                    return;
                }

                case CommandName.HUONG_DAN_BAN_DOANH_RED: {
                    player.service.showNotification(Barrack.notes);
                    return;
                }

                case CommandName.BAO_DANH_HANG_NGAY: {
                    for (IMission mission : player.missionDailies) {
                        if (mission.getTemplateId() < 5) {
                            MissionDaily daily = (MissionDaily) mission;
                            daily.param = player.onlineMinuteDay;
                            if (daily.param >= daily.template.param && daily.type == MissionType.CHUA_HOAN_THANH) {
                                daily.setType(MissionType.CHUA_NHAN_THUONG);
                            }
                        }
                    }
                    player.service.showMission(1, player.missionDailies);
                    return;
                }

                case CommandName.NAP_KIM_CUONG_HANG_NGAY: {
                    player.service.showMission(0, player.missionWeeks);
                    player.service.startDialogOk("Nếu bỏ lỡ 1 ngày nạp, mốc sẽ tự động đặt lại");
                    return;
                }

                case CommandName.PHU_HO_LANH_DIA: {
                    Clan clan = player.clan;
                    if (clan == null) {
                        return;
                    }
                    if (clan.manor == null) {
                        return;
                    }
                    if (!player.isEnoughMoney(TypePrice.RUBY, 10)) {
                        return;
                    }
                    if (player.isHaveEffect(EffectName.PHU_HO_SUC_DANH_VA_GIAM_SAT_THUONG_LANH_DIA)) {
                        player.addInfo(Player.INFO_RED, "Không thể phù hộ thêm");
                        return;
                    }
                    player.downMoney(TypePrice.RUBY, 10);
                    player.addEffect(new Effect(player, EffectName.PHU_HO_SUC_DANH_VA_GIAM_SAT_THUONG_LANH_DIA, clan.manor.endTime - System.currentTimeMillis(), 0, 2));
                    player.service.setInfo();
                    return;
                }

                case CommandName.RESET_MOC_NAP_THE: {
                    Command yes = new Command(CommandName.CONFIRM_RESET_MOC_NAP_THE, "Đồng ý", player);
                    Command no = new Command(CommandName.CANCEL, "Đóng", player);
                    player.startYesNo("Bạn có chắc chắn muốn làm mới mốc tích điểm không?\nHãy nhận các phần thưởng trước khi làm mới để tránh bỏ lỡ những vật phẩm quý giá", yes, no);
                    return;
                }

                case CommandName.CONFIRM_RESET_MOC_NAP_THE: {
                    int max = 0;
                    for (IMission mission : player.missionRecharges) {
                        MissionRecharge recharge = (MissionRecharge) mission;
                        max = Math.max(max, recharge.param);
                    }
                    player.resetMissionRecharge();
                    player.addInfo(Player.INFO_YELLOW, "Mốc tích điểm đã được đặt lại");
                    if (max > 35000) {
                        player.upParamMissionRecharge(max - 35000);
                    }
                    return;
                }

                case CommandName.SHOW_TOP_ACTIVE: {
                    Top top = TopManager.getInstance().tops.get(TopType.TOP_ACTIVE);
                    if (top != null) {
                        top.show(player);
                    }
                    return;
                }

                case CommandName.SHOW_TOP_LAST_WEEK: {
                    List<Command> commands = new ArrayList<>();
                    commands.add(new Command(CommandName.SHOW_TOP_HERO_LAST_WEEK, "Anh hùng", player));
                    commands.add(new Command(CommandName.SHOW_TOP_ACTIVE_LAST_WEEK, "Vang danh", player));
                    player.createMenu(NpcName.ME, "", commands);
                    return;
                }

                case CommandName.SHOW_TOP_ACTIVE_LAST_WEEK: {
                    Top top = TopManager.getInstance().tops.get(TopType.TOP_ACTIVE_LAST_WEEK);
                    if (top != null) {
                        top.show(player);
                    }
                    return;
                }

                case CommandName.SHOW_TOP_HERO_LAST_WEEK: {
                    Top top = TopManager.getInstance().tops.get(TopType.TOP_HERO_LAST_WEEK);
                    if (top != null) {
                        top.show(player);
                    }
                    return;
                }

                case CommandName.BEE_CONSIGNMENT: {
                    ShopManager.getInstance().consignment.show(player);
                    return;
                }

                case CommandName.HUONG_DAN_BEE_CONSIGNMENT: {
                    player.service.showNotification(ShopConsignment.notes);
                    return;
                }

                case CommandName.TREASURE_TO_DAO_KAME: {
                    player.teleport(MapName.DAO_KAME, true);
                    return;
                }

                case CommandName.THAM_GIA_DONG_KHO_BAU: {
                    List<Command> commands = new ArrayList<>();
                    StringBuilder content = new StringBuilder();
                    Treasure treasure = MapManager.getInstance().treasure;
                    commands.add(new Command(CommandName.HUONG_DAN_DONG_KHO_BAU, "Hướng dẫn", player));
                    if (treasure == null) {
                        content.append("Động kho báu mở cửa từ 21h30 đến 22h hàng ngày");
                        commands.add(new Command(CommandName.BANG_XEP_HANG_DONG_KHO_BAU, "Bảng xếp hạng", player));
                        commands.add(new Command(CommandName.CANCEL, "Đóng", player));
                    } else {
                        if (treasure.state == ExpansionState.WAIT_REG) {
                            content.append("Động kho báu đã mở cửa, con có muốn tham gia không?").append("\n");
                            long now = System.currentTimeMillis();
                            long time = Treasure.MINUTE_WAIT_REG * 60000L + treasure.startTime - now;
                            if (time > 0) {
                                content.append(String.format("Thời gian báo danh còn %s", Utils.formatTime(time)));
                            }
                            commands.add(new Command(CommandName.DONG_Y_THAM_GIA_DONG_KHO_BAU, "Tham gia", player, elements[0]));
                            commands.add(new Command(CommandName.CANCEL, "Không", player));
                        } else {
                            content.append("Đã hết thời gian báo danh");
                            commands.add(new Command(CommandName.CANCEL, "Đóng", player));
                        }
                    }
                    player.createMenu(((Npc) elements[0]).template.id, content.toString(), commands);
                    return;
                }

                case CommandName.DONG_Y_THAM_GIA_DONG_KHO_BAU: {
                    Treasure treasure = MapManager.getInstance().treasure;
                    if (treasure == null) {
                        player.addInfo(Player.INFO_RED, "Động kho báu chưa mở cửa");
                        return;
                    }
                    if (treasure.state == ExpansionState.RUN) {
                        player.addInfo(Player.INFO_RED, "Đã hết thời gian báo danh");
                        return;
                    }
                    /*if (player.pointActive < 100) {
                        player.addInfo(Player.INFO_RED, "Yêu cầu điểm năng động từ 100 trở lên");
                        return;
                    }*/
                    if (player.level < 10) {
                        player.addInfo(Player.INFO_RED, "Yêu cầu cấp độ tối thiểu 10");
                        return;
                    }
                    int max = 10;
                    if (player.level <= 29) {
                        max = 4;
                    } else if (player.level <= 39) {
                        max = 6;
                    } else if (player.level <= 49) {
                        max = 8;
                    }
                    for (int i = 0; i < 8; i++) {
                        Item item = player.itemsBody[i];
                        if (item == null || item.getUpgrade() < max) {
                            player.addInfo(Player.INFO_RED, String.format("Yêu cầu set trang bị cấp %d trở lên", max));
                            return;
                        }
                    }
                    if (player.isHaveEffect(EffectName.TU_DONG_LUYEN_TAP)) {
                        player.addInfo(Player.INFO_RED, "Bạn cần phải tắt Tự động luyện tập trước khi tham gia");
                        return;
                    }
                    treasure.addPlayer(player);
                    return;
                }

                case CommandName.HUONG_DAN_DONG_KHO_BAU: {
                    player.service.showNotification(Treasure.notes);
                    return;
                }

                case CommandName.BANG_XEP_HANG_DONG_KHO_BAU: {
                    Top top = TopManager.getInstance().tops.get(TopType.TOP_TREASURE);
                    if (top != null) {
                        top.show(player);
                    }
                    return;
                }

                case CommandName.TAI_CHE_MAX_CHI_SO: {
                    Upgrade upgrade = UpgradeManager.getInstance().upgrades.get(UpgradeType.REFRESH_MAX_ITEM);
                    if (upgrade != null) {
                        upgrade.showTab(player);
                    }
                    return;
                }

                case CommandName.EQUIP_CRAFTING_6X: {
                    Upgrade upgrade = UpgradeManager.getInstance().upgrades.get(UpgradeType.EQUIP_CRAFTING_6X);
                    if (upgrade != null) {
                        upgrade.showTab(player);
                    }
                    return;
                }

                case CommandName.CONFIRM_CHANGE_ALL_SKILL_DISCIPLE: {
                    int quantity = player.getQuantityItemInBag(ItemName.THE_DOI_KY_NANG_DE_TU);
                    if (quantity <= 0) {
                        return;
                    }
                    Disciple disciple = player.disciple;
                    if (disciple == null) {
                        player.addInfo(Player.INFO_RED, "Bạn chưa có đệ tử");
                        return;
                    }
                    if (player.isFusion()) {
                        player.addInfo(Player.INFO_RED, "Không thể thực hiện khi đang hợp thể");
                        return;
                    }
                    player.removeQuantityItemBagById(ItemName.THE_DOI_KY_NANG_DE_TU, 1);
                    if (!disciple.skills.isEmpty()) {
                        Skill skill = new Skill();
                        skill.template = SkillManager.getInstance().skillTemplates.get(ServerRandom.SKILL_DISCIPLE_1.next());
                        skill.level = 1;
                        skill.upgrade = 0;
                        skill.point = 0;
                        disciple.skills.set(0, skill);
                    }
                    if (disciple.skills.size() >= 2) {
                        Skill skill = new Skill();
                        skill.template = SkillManager.getInstance().skillTemplates.get(ServerRandom.SKILL_DISCIPLE_2.next());
                        skill.level = 1;
                        skill.upgrade = 0;
                        skill.point = 0;
                        disciple.skills.set(1, skill);
                    }
                    if (disciple.skills.size() >= 3) {
                        Skill skill = new Skill();
                        skill.template = SkillManager.getInstance().skillTemplates.get(ServerRandom.SKILL_DISCIPLE_3.next());
                        skill.level = 1;
                        skill.upgrade = 0;
                        skill.point = 0;
                        disciple.skills.set(2, skill);
                    }
                    if (disciple.skills.size() >= 4) {
                        Skill skill = new Skill();
                        skill.template = SkillManager.getInstance().skillTemplates.get(ServerRandom.SKILL_DISCIPLE_4.next());
                        skill.level = 1;
                        skill.upgrade = 0;
                        skill.point = 0;
                        disciple.skills.set(3, skill);
                    }
                    player.service.discipleInfo(MessageDiscipleInfoName.SKILL_INFO);
                    player.addInfo(Player.INFO_YELLOW, "Kỹ năng của đệ tử đã được đặt lại");
                    return;
                }

                case CommandName.GO_TO_THAN_DIEN: {
                    player.teleport(MapName.THAN_DIEN, true);
                    return;
                }

                case CommandName.VE_THAP_KARIN: {
                    player.teleport(MapName.THAP_KARIN, true);
                    return;
                }

                case CommandName.TRAIN_OFFLINE: {
                    StringBuilder content = new StringBuilder();
                    long now = System.currentTimeMillis();
                    long power = player.expTraining * 1000 / (now - player.timeLogin) / 2;
                    content.append(String.format("Luyện tập tại đây sẽ tăng %s sức mạnh và tiềm năng mỗi giây", Utils.formatNumber(Math.max(power, 0L)))).append("\n");
                    List<Command> commands = new ArrayList<>();
                    if (!player.isTrainingOfflineForMaster) {
                        commands.add(new Command(CommandName.TRAIN_OFFLINE_FOR_MASTER, "Đăng ký\n1 KC", player));
                    }
                    if (player.disciple != null) {
                        long disciple = player.disciple.expTraining * 1000 / (now - player.disciple.timeLogin) / 3;
                        content.append(String.format("Đệ tử sẽ tăng %s sức mạnh và tiềm năng mỗi giây", Utils.formatNumber(Math.max(disciple, 0L)))).append("\n");
                        if (!player.isTrainingOfflineForDisciple) {
                            commands.add(new Command(CommandName.TRAIN_OFFLINE_FOR_DISCIPLE, "Đăng ký\ncho đệ\n10 KC", player));
                        }
                    }
                    if (player.isTrainingOfflineForDisciple || player.isTrainingOfflineForMaster) {
                        commands.add(new Command(CommandName.CANCEL_TRAIN_OFFLINE, "Hủy đăng ký", player));
                    }
                    content.append("Chỉ bắt đầu khi bạn offline và kết thúc khi bạn online").append("\n");
                    content.append("Thời gian luyện tập tối đa là 24 tiếng");
                    player.createMenu(NpcName.ME, content.toString(), commands);
                    return;
                }

                case CommandName.TRAIN_OFFLINE_FOR_MASTER: {
                    if (player.isTrainingOfflineForMaster) {
                        return;
                    }
                    if (player.level >= 80) {
                        player.addInfo(Player.INFO_RED, "Chỉ có thể hỗ trợ dưới cấp 80");
                        return;
                    }
                    long time = System.currentTimeMillis() - player.timeLogin;
                    if (time < 600000) {
                        player.addInfo(Player.INFO_RED, "Bạn cần phải tham gia đánh quái tối thiểu 10 phút");
                        return;
                    }
                    if (player.diamond < 1) {
                        player.addInfo(Player.INFO_RED, "Bạn không đủ Kim cương");
                        return;
                    }
                    player.upDiamond(-1);
                    player.isTrainingOfflineForMaster = true;
                    player.addInfo(Player.INFO_YELLOW, "Đăng ký thành công");
                    return;
                }

                case CommandName.TRAIN_OFFLINE_FOR_DISCIPLE: {
                    if (player.isTrainingOfflineForDisciple || player.disciple == null) {
                        return;
                    }
                    if (player.disciple.level >= 80) {
                        player.addInfo(Player.INFO_RED, "Chỉ có thể hỗ trợ dưới cấp 80");
                        return;
                    }
                    long time = System.currentTimeMillis() - player.disciple.timeLogin;
                    if (time < 600000) {
                        player.addInfo(Player.INFO_RED, "Đệ tử cần phải tham gia đánh quái tối thiểu 10 phút");
                        return;
                    }
                    if (player.diamond < 10) {
                        player.addInfo(Player.INFO_RED, "Bạn không đủ Kim cương");
                        return;
                    }
                    player.upDiamond(-10);
                    player.isTrainingOfflineForDisciple = true;
                    player.addInfo(Player.INFO_YELLOW, "Đăng ký thành công");
                    return;
                }

                case CommandName.CANCEL_TRAIN_OFFLINE: {
                    if (player.isTrainingOfflineForDisciple || player.isTrainingOfflineForMaster) {
                        player.isTrainingOfflineForMaster = false;
                        player.isTrainingOfflineForDisciple = false;
                        player.addInfo(Player.INFO_YELLOW, "Hủy luyện tập thành công");
                    }
                    return;
                }

                case CommandName.TELEPORT_DAO_BANG_HOA: {
                    /*if (player.level < 30) {
                        player.addInfo(Player.INFO_RED, "Yêu cầu cấp độ từ 30 trở lên để tham gia khu vực này");
                        return;
                    }*/
                   /* if (player.pointActive < 50) {
                        player.addInfo(Player.INFO_RED, "Yêu cầu điểm năng động từ 50 trở lên để tham gia khu vực này");
                        return;
                    }*/
                    if (player.pointPk > 0) {
                        player.addInfo(Player.INFO_RED, "Không thể tham gia khi điểm hiếu chiến quá cao");
                        return;
                    }
                    long coin = 500000L + Math.max(100000L * (player.level - 30), 0L);
                    if (player.xu < coin) {
                        player.addInfo(Player.INFO_RED, String.format("Bạn còn thiếu %s xu", Utils.getMoneys(coin - player.xu)));
                        return;
                    }
                    player.upXu(-coin);
                    MapManager.getInstance().island.enter(player);
                    return;
                }

                case CommandName.TELEPORT_HANH_TINH_YARDRAT: {
                    player.teleport(MapName.YARDRAT_2, true);
                    return;
                }

                case CommandName.TELEPORT_THUNG_LUNG_TURI: {
                    player.teleport(MapName.THUNG_LUNG_TURI, true);
                    return;
                }

                case CommandName.CUA_HANG_YARDRAT: {
                    Shop shop = ShopManager.getInstance().shops.get(ShopType.SHOP_YARDRAT);
                    if (shop != null) {
                        shop.show(player);
                    }
                    return;
                }

                case CommandName.DONG_Y_RESET_NAP_TRONG_NGAY: {
                    if (!player.isRechargedInDay) {
                        return;
                    }
                    if (player.diamond < 100) {
                        player.addInfo(Player.INFO_RED, String.format("Bạn còn thiếu %d kim cương", 100 - player.diamond));
                        return;
                    }
                    player.upDiamond(-100);
                    player.isRechargedInDay = false;
                    player.addInfo(Player.INFO_YELLOW, "Lượt nạp tích lũy trong ngày đã được đặt lại");
                    return;
                }

                case CommandName.CONFIRM_CHANGE_ALL_SKILL_DISCIPLE_BY_EVENT: {
                    Disciple disciple = player.disciple;
                    if (disciple == null) {
                        player.addInfo(Player.INFO_RED, "Bạn chưa có đệ tử");
                        return;
                    }
                    if (player.isFusion()) {
                        player.addInfo(Player.INFO_RED, "Không thể thực hiện khi đang hợp thể");
                        return;
                    }
                    if (!disciple.skills.isEmpty()) {
                        Skill skill = new Skill();
                        skill.template = SkillManager.getInstance().skillTemplates.get(ServerRandom.SKILL_DISCIPLE_1_EVENT.next());
                        skill.level = 1;
                        skill.upgrade = 0;
                        skill.point = 0;
                        disciple.skills.set(0, skill);
                    }
                    if (disciple.skills.size() >= 2) {
                        Skill skill = new Skill();
                        skill.template = SkillManager.getInstance().skillTemplates.get(ServerRandom.SKILL_DISCIPLE_2_EVENT.next());
                        skill.level = 1;
                        skill.upgrade = 0;
                        skill.point = 0;
                        disciple.skills.set(1, skill);
                    }
                    if (disciple.skills.size() >= 3) {
                        Skill skill = new Skill();
                        skill.template = SkillManager.getInstance().skillTemplates.get(ServerRandom.SKILL_DISCIPLE_3_EVENT.next());
                        skill.level = 1;
                        skill.upgrade = 0;
                        skill.point = 0;
                        disciple.skills.set(2, skill);
                    }
                    if (disciple.skills.size() >= 4) {
                        Skill skill = new Skill();
                        skill.template = SkillManager.getInstance().skillTemplates.get(ServerRandom.SKILL_DISCIPLE_4_EVENT.next());
                        skill.level = 1;
                        skill.upgrade = 0;
                        skill.point = 0;
                        disciple.skills.set(3, skill);
                    }
                    player.service.discipleInfo(MessageDiscipleInfoName.SKILL_INFO);
                    player.addInfo(Player.INFO_YELLOW, "Kỹ năng của đệ tử đã được đặt lại");
                    return;
                }

                case CommandName.DOI_DEN_TROI_SU_KIEN_TRUNG_THU_2023: {
                    int quantity = player.getQuantityItemInBag(ItemName.MANH_THO_NGOC_SU_KIEN_TRUNG_THU_2023);
                    if (quantity < 10) {
                        player.addInfo(Player.INFO_RED, "Cần ít nhất x10 mảnh để đổi");
                        return;
                    }
                    player.removeQuantityItemBagById(ItemName.MANH_THO_NGOC_SU_KIEN_TRUNG_THU_2023, 10);
                    Item item = ItemManager.getInstance().createItem(ItemName.DEN_TROI_SU_KIEN_TRUNG_THU_2023, 1, true);
                    player.addItem(item);
                    player.addInfo(Player.INFO_YELLOW, String.format("Bạn nhận được %s", item.template.name));
                    return;
                }

                case CommandName.SHOW_SHOP_EXCHANGE_POINT_EVENT: {
                    Shop shop = ShopManager.getInstance().shops.get(ShopType.SHOP_EVENT);
                    if (shop != null) {
                        shop.show(player);
                    }
                    return;
                }

                case CommandName.VAO_NHA: {
                    Map map = new Map(MapManager.getInstance().mapTemplates.get(MapName.NHA_GO_HAN));
                    for (int i = 0; i < player.npcTrees.size(); i++) {
                        NpcTree npcTree = player.npcTrees.get(i);
                        npcTree.x = 1425 + 200 * i;
                        npcTree.y = 846;
                        map.zones.get(0).enter(npcTree);
                    }
                    player.x = 935;
                    player.y = 864;
                    player.joinMap(map, 0);
                    return;
                }

                case CommandName.ROI_NHA: {
                    if (player.zone.map.template.id == MapName.NHA_GO_HAN) {
                        player.x = 600;
                        player.y = 936;
                        player.joinMap(MapManager.getInstance().maps.get(MapName.NUI_PAOZU), -1);
                    }
                    return;
                }

                case CommandName.THU_HOACH_QUA: {
                    NpcTree npcTree = (NpcTree) elements[0];
                    if (npcTree.master.id != player.id) {
                        player.session.disconnect();
                        return;
                    }
                    if (npcTree.template.id != NpcName.HAC_MOC_MAX
                            && npcTree.template.id != NpcName.THIEN_MOC_MAX
                            && npcTree.template.id != NpcName.CAY_THONG_MAX
                            && npcTree.template.id != NpcName.CAY_DAO_MAX
                            && npcTree.template.id != NpcName.CAY_NAM_MAX
                            && npcTree.template.id != NpcName.CAY_CHUOI_MAX
                            && npcTree.template.id != NpcName.CAY_DUA_MAX) {
                        return;
                    }
                    if (npcTree.time > 0) {
                        player.addInfo(Player.INFO_RED, "Chờ đợi là hạnh phúc!!!");
                        return;
                    }
                    if (player.isBagFull()) {
                        player.addInfo(Player.INFO_RED, Language.ME_BAG_FULL);
                        return;
                    }
                    int itemID = ItemName.THIEN_MOC_QUA;
                    if (npcTree.template.id == NpcName.HAC_MOC_MAX) {
                        itemID = ItemName.HAC_LONG_QUA;
                    } else if (npcTree.template.id == NpcName.CAY_THONG_MAX) {
                        itemID = ItemName.QUA_THONG_EXP;
                    } else if (npcTree.template.id == NpcName.CAY_DAO_MAX) {
                        itemID = ItemName.QUA_DAO_EXP;
                    } else if (npcTree.template.id == NpcName.CAY_NAM_MAX) {
                        itemID = ItemName.QUA_NAM_HAC_HOA;
                    } else if (npcTree.template.id == NpcName.CAY_CHUOI_MAX) {
                        itemID = ItemName.QUA_CHUOI;
                    } else if (npcTree.template.id == NpcName.CAY_DUA_MAX) {
                        itemID = ItemName.TRAI_DUA_EXP;
                    }
                    player.addItem(ItemManager.getInstance().createItem(itemID, 10, true), true);
                    player.npcTrees.removeIf(n -> npcTree.template.id == n.template.id);
                    if (player.zone != null && player.zone.map.template.id == MapName.NHA_GO_HAN) {
                        player.zone.leave(npcTree);
                    }
                    return;
                }

                case CommandName.SHOP_SALE: {
                    Shop shop = ShopManager.getInstance().shops.get(ShopType.SHOP_BLACK_FRIDAY);
                    if (shop != null) {
                        shop.show(player);
                    }
                    return;
                }

                case CommandName.UP_STAR_PET: {
                    Upgrade upgrade = UpgradeManager.getInstance().upgrades.get(UpgradeType.UP_STAR_PET);
                    if (upgrade != null) {
                        upgrade.showTab(player);
                    }
                    return;
                }

                case CommandName.MENU_LOI_DAI: {
                    List<Command> commands = new ArrayList<>();
                    commands.add(new Command(CommandName.THACH_DAU_LOI_DAI, "Thách đấu", player));
                    commands.add(new Command(CommandName.XEM_THI_DAU, "Xem thi đấu", player));
                    player.createMenu(NpcName.ME, "Thắng bại tại kỹ năng?", commands);
                    return;
                }

                case CommandName.THACH_DAU_LOI_DAI: {
                    player.startClientInput(ClientInputType.INPUT_LOI_DAI, "Thách đấu", new TextField("Tên nhân vật", TextField.TYPE_NORMAL), new TextField("Xu thách đấu (tối thiểu 1tr xu)", TextField.TYPE_NUMBER));
                    return;
                }

                case CommandName.DONG_Y_THACH_DAU_LOI_DAI: {
                    int playerID = (int) elements[0];
                    long coin = (long) elements[1];
                    Player target = player.zone.findPlayerById(playerID);
                    if (target == null) {
                        player.addInfo(Player.INFO_RED, "Người chơi đã rời khu vực");
                        return;
                    }
                    if (player.xu < coin) {
                        player.addInfo(Player.INFO_RED, "Bạn không có đủ xu");
                        return;
                    }
                    if (target.xu < coin) {
                        player.addInfo(Player.INFO_RED, String.format("%s không có đủ xu", target.name));
                        return;
                    }
                    player.upXu(-coin);
                    target.upXu(-coin);
                    MapManager.getInstance().arenaCustoms.add(new ArenaCustom(new Player[]{target, player}, coin));
                    return;
                }

                case CommandName.XEM_THI_DAU: {
                    if (MapManager.getInstance().arenaCustoms.isEmpty()) {
                        player.addInfo(Player.INFO_RED, "Không có trận chiến nào đang diễn ra");
                        return;
                    }
                    ArrayList<KeyValue<Map, String>> mapList = new ArrayList<>();
                    for (ArenaCustom arenaCustom : MapManager.getInstance().arenaCustoms) {
                        if (arenaCustom.isRunning && arenaCustom.status != ArenaCustom.STATUS_CLOSE) {
                            String[] info = arenaCustom.getInfo();
                            mapList.add(new KeyValue<>(arenaCustom.map, info[0], info[1], arenaCustom));
                        }
                    }
                    player.showListMapSpaceship(mapList);
                    return;
                }

                case CommandName.DAU_TRUONG_CELL: {
                    MartialCongress martialCongress = MapManager.getInstance().martialCongress;
                    if (martialCongress != null) {
                        martialCongress.enter(player);
                    }
                    return;
                }

                case CommandName.VE_DAO_KAME: {
                    player.x = 1050;
                    player.y = 936;
                    player.joinMap(MapManager.getInstance().maps.get(MapName.DAO_KAME), -1);
                    return;
                }

                case CommandName.DANG_KY_THI_DAU_DAU_TRUONG: {
                    if (player.zone instanceof ZoneMartialCongress) {
                        ((ZoneMartialCongress) player.zone).addWaiter(player, -1);
                    }
                    return;
                }

                case CommandName.NHAN_THUONG_DAU_TRUONG: {
                    MartialCongress martialCongress = MapManager.getInstance().martialCongress;
                    if (martialCongress != null) {
                        if (martialCongress.isReward(player.id)) {
                            player.addInfo(Player.INFO_RED, "Bạn đã nhận thưởng rồi, không thể nhận thưởng thêm");
                        } else {
                            if (player.isBagFull()) {
                                player.addInfo(Player.INFO_RED, Language.ME_BAG_FULL);
                                return;
                            }
                            int level = martialCongress.getLevel(player);
                            if (level <= 0) {
                                player.addInfo(Player.INFO_RED, Language.CANT_ACTION);
                                return;
                            }
                            martialCongress.addReward(player);
                            Item item = ItemManager.getInstance().createItem(ItemName.CAPSULE_CELL, 1, false);
                            item.options.add(new ItemOption(19, level));
                            item.setExpiry(3);
                            player.addItem(item, true);
                        }
                    }
                    return;
                }

                case CommandName.MO_NOI_TAI: {
                    if (player.level < 20) {
                        player.addInfo(Player.INFO_RED, "Yêu cầu cấp độ từ 20 trở lên");
                        return;
                    }
                    if (player.isProtect) {
                        player.addInfo(Player.INFO_RED, Language.TAI_KHOAN_DANG_DUOC_BAO_VE);
                        return;
                    }
                    int templateId = (int) elements[0];
                    int type = (int) elements[1];
                    Intrinsic intrinsic = player.intrinsics.get(templateId);
                    if (intrinsic == null) {
                        return;
                    }
                    if (type == 0) {
                        if (intrinsic.template.priceCoin > player.xu) {
                            player.addInfo(Player.INFO_RED, "Bạn không đủ xu");
                            return;
                        }
                        player.upXu(-intrinsic.template.priceCoin);
                    } else if (type == 1) {
                        if (intrinsic.template.priceDiamond > player.diamond) {
                            player.addInfo(Player.INFO_RED, "Bạn không đủ kim cương");
                            return;
                        }
                        player.upDiamond(-intrinsic.template.priceDiamond);
                    }
                    intrinsic.param = intrinsic.nextParam(type);
                    player.service.updateIntrinsic(intrinsic);
                    if (intrinsic.template.isCoolDown) {
                        Skill skill = player.getSkill(intrinsic.template.skillTemplateId);
                        if (skill != null) {
                            skill.coolDownIntrinsic = intrinsic.param;
                            player.service.setCoolDownIntrinsic(skill);
                        }
                    }
                    if (intrinsic.template.id == 31 || intrinsic.template.id == 32) {
                        player.service.setInfo();
                    }
                    player.addInfo(Player.INFO_YELLOW, "Thay đổi thành công");
                    return;
                }

                case CommandName.CHE_TAO_TRANG_BI_RIDER: {
                    Upgrade upgrade = UpgradeManager.getInstance().upgrades.get(UpgradeType.EQUIP_CRAFTING_RIDER);
                    if (upgrade != null) {
                        upgrade.showTab(player);
                    }
                    return;
                }

                case CommandName.THAM_GIA_THANH_DIA: {
                    Sanctuary sanctuary = MapManager.getInstance().sanctuary;
                    if (sanctuary == null) {
                        Spaceship spaceship = MapManager.getInstance().spaceship;
                        if (spaceship != null) {
                            spaceship.enter(player);
                            return;
                        }
                        player.addInfo(Player.INFO_RED, "Phó bản chưa mở cửa");
                        return;
                    }
                    sanctuary.enter(player);
                    return;
                }

                case CommandName.DOI_CHI_SO_HIEP_SI: {
                    Upgrade upgrade = UpgradeManager.getInstance().upgrades.get(UpgradeType.CHANGE_OPTION_RIDER);
                    if (upgrade != null) {
                        upgrade.showTab(player);
                    }
                    return;
                }

                case CommandName.DOI_DE_MABU: {
                    if (player.getQuantityItemInBag(ItemName.TRUNG_MABU) < 1) {
                        return;
                    }
                    int tempId = 45;
                    Effect effect = player.findEffectByTemplateId(tempId);
                    if (effect != null) {
                        player.addInfo(Player.INFO_RED, Language.CANT_ACTION);
                        return;
                    }
                    if (player.disciple != null) {
                        player.deleteDisciple();
                    }
                    player.removeQuantityItemBagById(ItemName.TRUNG_MABU, 1);
                    effect = new Effect(player, tempId, 1000L * 60 * 60 * 24 * 7);
                    effect.param = 1;
                    player.addEffect(effect);
                    return;
                }

                case CommandName.VE_NUI_PAOZU: {
                    player.teleport(MapName.NUI_PAOZU, false);
                    return;
                }

                case CommandName.UPGRADE_ITEM_RIDER: {
                    Upgrade upgrade = UpgradeManager.getInstance().upgrades.get(UpgradeType.UPGRADE_ITEM_RIDER);
                    if (upgrade != null) {
                        upgrade.showTab(player);
                    }
                    return;
                }

                case CommandName.CUA_HANG_HIEP_SI: {
                    Shop shop = ShopManager.getInstance().shops.get(ShopType.SHOP_RIDER);
                    if (shop != null) {
                        shop.show(player);
                    }
                    return;
                }

                case CommandName.MENU_MA_BAO_VE: {
                    if (Server.getInstance().isInterServer()) {
                        player.addInfo(Player.INFO_RED, Language.CANCEL_ACTION_WHEN_SERVER_IS_INTER_SERVER);
                        return;
                    }
                    List<Command> commands = new ArrayList<>();
                    commands.add(new Command(CommandName.NHAP_MA_BAO_VE, "Nhập mã", player));
                    StringBuilder content = new StringBuilder();
                    content.append("Mã bảo vệ sẽ giúp bạn bảo vệ tài khoản.");
                    if (player.pin == null || player.pin.isEmpty()) {
                        content.append("\n");
                        content.append("Phí kích hoạt mã bảo vệ là 50 KC");
                    } else {
                        commands.add(new Command(CommandName.DOI_MA_BAO_VE, "Đổi mã bảo vệ", player));
                    }
                    player.createMenu(NpcName.ME, content.toString(), commands);
                    return;
                }

                case CommandName.NHAP_MA_BAO_VE: {
                    player.startClientInput(ClientInputType.NHAP_MA_BAO_VE, "Nhập mã bảo vệ",
                            new TextField("mã bảo vệ (5-10 kí tự)", TextField.TYPE_NORMAL));
                    return;
                }

                case CommandName.DOI_MA_BAO_VE: {
                    player.startClientInput(ClientInputType.DOI_MA_BAO_VE, "Đổi mã bảo vệ",
                            new TextField("mã bảo vệ cũ (5-10 kí tự)", TextField.TYPE_NORMAL),
                            new TextField("mã bảo vệ mới (5-10 kí tự)", TextField.TYPE_NORMAL));
                    return;
                }

                case CommandName.DEN_HANH_TINH_BILL:
                    player.teleport(MapName.PHIA_BAC_HANH_TINH_BILL, false);
                    return;

                case CommandName.TELEPORT_DAO_KAME:
                    player.teleport(MapName.DAO_KAME, false);
                    return;

                case CommandName.PACK_AVATAR: {
                    Upgrade upgrade = UpgradeManager.getInstance().upgrades.get(UpgradeType.PACK_AVATAR);
                    if (upgrade != null) {
                        upgrade.showTab(player);
                    }
                    return;
                }

                case CommandName.CRAFTING_PORATA: {
                    Upgrade upgrade = UpgradeManager.getInstance().upgrades.get(UpgradeType.CRAFTING_PORATA);
                    if (upgrade != null) {
                        upgrade.showTab(player);
                    }
                    return;
                }

                case CommandName.OPEN_OPTION_PORATA: {
                    Upgrade upgrade = UpgradeManager.getInstance().upgrades.get(UpgradeType.OPEN_OPTION_PORATA);
                    if (upgrade != null) {
                        upgrade.showTab(player);
                    }
                    return;
                }

                case CommandName.REMOVE_OPTION: {
                    Upgrade upgrade = UpgradeManager.getInstance().upgrades.get(UpgradeType.REMOVE_OPTION);
                    if (upgrade != null) {
                        upgrade.showTab(player);
                    }
                    return;
                }

                case CommandName.CHE_TAO_TRANG_BI_THIEN_SU: {
                    Upgrade upgrade = UpgradeManager.getInstance().upgrades.get(UpgradeType.CHE_TAO_THIEN_SU);
                    if (upgrade != null) {
                        upgrade.showTab(player);
                    }
                    return;
                }

                case CommandName.PACK_THU_CUOI: {
                    Upgrade upgrade = UpgradeManager.getInstance().upgrades.get(UpgradeType.PACK_THU_CUOI);
                    if (upgrade != null) {
                        upgrade.showTab(player);
                    }
                    return;
                }

                case CommandName.CUA_HANG_SPACESHIP: {
                    Shop shop = ShopManager.getInstance().shops.get(ShopType.SHOP_SPACESHIP);
                    if (shop != null) {
                        shop.show(player);
                    }
                    return;
                }

                case CommandName.XUONG_TANG_SPACESHIP: {
                    Spaceship spaceship = MapManager.getInstance().spaceship;
                    if (spaceship != null) {
                        spaceship.next(player);
                    }
                    return;
                }

                case CommandName.PHU_HO_MABU: {
                    Spaceship spaceship = MapManager.getInstance().spaceship;
                    if (spaceship != null) {
                        if (player.isHaveEffect(EffectName.PHU_HO_MABU)) {
                            player.addInfo(Player.INFO_RED, "Không thể phù hộ thêm");
                            return;
                        }
                        if (!player.isEnoughMoney(TypePrice.RUBY, 5)) {
                            return;
                        }
                        player.downMoney(TypePrice.RUBY, 5);
                        player.addEffect(new Effect(player, EffectName.PHU_HO_MABU, spaceship.getCountDown()));
                        player.service.setInfo();
                        if (player.hp != player.maxHp) {
                            player.recovery(Player.RECOVERY_ALL, 100, true);
                        }
                    }
                    return;
                }

                case CommandName.O_MAY_MAN: {
                    LuckyManager.getInstance().luckyBox.show(player, (int) elements[0]);
                    return;
                }

                case CommandName.CHON_AI_DAY: {
                    LuckyManager.getInstance().luckyCoin.open(player);
                    return;
                }

                case CommandName.UPGRADE_PORATA: {
                    Upgrade upgrade = UpgradeManager.getInstance().upgrades.get(UpgradeType.UPGRADE_PORATA);
                    if (upgrade != null) {
                        upgrade.showTab(player);
                    }
                    return;
                }

                case CommandName.SHOW_TOP_PRO: {
                    Top top = TopManager.getInstance().tops.get(TopType.TOP_PRO);
                    if (top != null) {
                        top.show(player);
                    }
                    return;
                }

                case CommandName.SHOW_TOP_EVENT_WEEKLY: {
                    Top top = TopManager.getInstance().tops.get(TopType.TOP_EVENT_WEEKLY);
                    if (top != null) {
                        top.show(player);
                    }
                    return;
                }

                case CommandName.SHOW_TOP_BOSS_WEEKLY: {
                    Top top = TopManager.getInstance().tops.get(TopType.TOP_BOSS_WEEKLY);
                    if (top != null) {
                        top.show(player);
                    }
                    return;
                }

                case CommandName.SHOW_TOP_ACTIVE_WEEKLY: {
                    Top top = TopManager.getInstance().tops.get(TopType.TOP_ACTIVE_WEEKLY);
                    if (top != null) {
                        top.show(player);
                    }
                    return;
                }

                case CommandName.SHOW_PHAN_THUONG_DUA_TOP_TUAN: {
                    int index = (int) elements[0];
                    if (index == -1) {
                        List<Command> commands = new ArrayList<>();
                        commands.add(new Command(CommandName.SHOW_PHAN_THUONG_DUA_TOP_TUAN, "Săn Boss", player, 0));
                        commands.add(new Command(CommandName.SHOW_PHAN_THUONG_DUA_TOP_TUAN, "Hoạt động", player, 1));
                        commands.add(new Command(CommandName.SHOW_PHAN_THUONG_DUA_TOP_TUAN, "Sự kiện", player, 2));
                        player.createMenu(NpcName.ME, "", commands);
                    } else if (index == 0) {
                        player.service.showMission(-2, MissionManager.getInstance().missionItemWeeklies.stream().filter(m -> m.getName().equals("Săn Boss")).toList());
                    } else if (index == 1) {
                        player.service.showMission(-2, MissionManager.getInstance().missionItemWeeklies.stream().filter(m -> m.getName().equals("Hoạt động")).toList());
                    } else if (index == 2) {
                        player.service.showMission(-2, MissionManager.getInstance().missionItemWeeklies.stream().filter(m -> m.getName().equals("Sự kiện")).toList());
                    }
                    return;
                }

                case CommandName.REMOVE_STAR: {
                    Upgrade upgrade = UpgradeManager.getInstance().upgrades.get(UpgradeType.REMOVE_STAR);
                    if (upgrade != null) {
                        upgrade.showTab(player);
                    }
                    return;
                }

                case CommandName.SHOW_LIST_DA_TRAO_TOP: {
                    List<Command> commands = new ArrayList<>();
                    commands.add(new Command(CommandName.SHOW_TOP_BOSS_PREVIOUS_WEEKLY, "Săn Boss", player));
                    commands.add(new Command(CommandName.SHOW_TOP_ACTIVE_PREVIOUS_WEEKLY, "Hoạt động", player));
                    if (Event.isEvent()) {
                        commands.add(new Command(CommandName.SHOW_TOP_EVENT_PREVIOUS_WEEKLY, "Sự kiện", player, 0));
                    }
                    player.createMenu(NpcName.ME, "", commands);
                    return;
                }

                case CommandName.SHOW_TOP_BOSS_PREVIOUS_WEEKLY: {
                    Top top = TopManager.getInstance().tops.get(TopType.TOP_BOSS_PREVIOUS_WEEKLY);
                    if (top != null) {
                        top.show(player);
                    }
                    return;
                }

                case CommandName.SHOW_TOP_ACTIVE_PREVIOUS_WEEKLY: {
                    Top top = TopManager.getInstance().tops.get(TopType.TOP_ACTIVE_PREVIOUS_WEEKLY);
                    if (top != null) {
                        top.show(player);
                    }
                    return;
                }

                case CommandName.SHOW_TOP_EVENT_PREVIOUS_WEEKLY: {
                    Top top = TopManager.getInstance().tops.get(TopType.TOP_EVENT_PREVIOUS_WEEKLY);
                    if (top != null) {
                        top.show(player);
                    }
                    return;
                }

                case CommandName.GHEP_HUY_HIEU: {
                    Upgrade upgrade = UpgradeManager.getInstance().upgrades.get(UpgradeType.GHEP_HUY_HIEU);
                    if (upgrade != null) {
                        upgrade.showTab(player);
                    }
                    return;
                }

                case CommandName.TACH_HUY_HIEU: {
                    Upgrade upgrade = UpgradeManager.getInstance().upgrades.get(UpgradeType.TACH_HUY_HIEU);
                    if (upgrade != null) {
                        upgrade.showTab(player);
                    }
                    return;
                }

                case CommandName.OPEN_UPGRADE: {
                    Upgrade upgrade = UpgradeManager.getInstance().upgrades.get((UpgradeType) elements[0]);
                    if (upgrade != null) {
                        upgrade.showTab(player);
                    }
                    return;
                }

                case CommandName.DOI_SACH_HUT_DAY: {
                    if (player.isBagFull()) {
                        player.addInfo(Player.INFO_RED, Language.ME_BAG_FULL);
                        return;
                    }
                    int quantity = player.getQuantityItemInBag(ItemName.MANH_GIAY_CHIEU_HUT_DAY);
                    if (quantity < 50) {
                        player.addInfo(Player.INFO_RED, "Bạn không có đủ mảnh giấy");
                        return;
                    }
                    if (player.diamond < 100) {
                        player.addInfo(Player.INFO_RED, "Bạn không có đủ Kim cương");
                        return;
                    }
                    player.upDiamond(-100);
                    player.removeQuantityItemBagById(ItemName.MANH_GIAY_CHIEU_HUT_DAY, 50);
                    player.addItem(ItemManager.getInstance().createItem(ItemName.SACH_CHIEU_HUT_DAY, 1, true), true);
                    return;
                }

                case CommandName.SHOW_LIST_TOURNAMENT_ATHLETE: {
                    Tournament tournament = MapManager.getInstance().tournament;
                    if (tournament == null) {
                        return;
                    }
                    List<TournamentAthlete> members = tournament.getAthletes();
                    if (members.isEmpty()) {
                        player.addInfo(Player.INFO_RED, "Danh sách trống");
                        return;
                    }
                    for (TournamentAthlete member : members) {
                        Player player = PlayerManager.getInstance().findPlayerById(member.playerId);
                        if (player != null) {
                            member.isOnline = true;
                            member.pointPro = player.pointPro;
                        } else {
                            member.isOnline = false;
                        }
                    }
                    player.service.showListTournamentAthlete(members);
                    return;
                }

                case CommandName.VAO_MAP_TOURNAMENT: {
                    Tournament tournament = MapManager.getInstance().tournament;
                    if (tournament != null) {
                        tournament.enter(player);
                    }
                    return;
                }

                case CommandName.THACH_DAU_TOURNAMENT: {
                    Tournament tournament = MapManager.getInstance().tournament;
                    if (tournament != null) {
                        tournament.attack(player, (int) elements[0]);
                    }
                    return;
                }

                case CommandName.PHAN_THUONG_TOURNAMENT: {
                    List<String> notes = new ArrayList<>();
                    notes.add("Giải đấu được chốt và trao thưởng vào 19h hàng ngày:");
                    notes.add("- Top 1: 1 tỉ xu, Huy hiệu Chí tôn HSD 2 ngày (chỉ số mạnh hơn 20% so với huy hiệu đang mang trên người)");
                    notes.add("- Top 2: 100tr xu");
                    notes.add("- Top 3-10: 50tr, 45tr, 40tr,...");
                    notes.add("- Top 11-20: 5tr xu");
                    player.service.showNotification(notes);
                    return;
                }

                case CommandName.FLAG_WAR: {
                    int action = (int) elements[0];
                    if (action == -1) {
                        List<Command> commands = new ArrayList<>();
                        commands.add(new Command(CommandName.FLAG_WAR, "Tham gia", player, 0, -1));
                        commands.add(new Command(CommandName.FLAG_WAR, "Hướng dẫn", player, 1));
                        commands.add(new Command(CommandName.FLAG_WAR, "Đổi thưởng", player, 2));
                        player.createMenu(NpcName.ME, "", commands);
                    } else if (action == 0) {
                        FlagWar flagWar = MapManager.getInstance().flagWar;
                        if (flagWar == null) {
                            player.addInfo(Player.INFO_RED, "Phó bản chưa mở, hãy quay lại vào 20h30");
                            return;
                        }
                        if (player.clan == null) {
                            player.addInfo(Player.INFO_RED, "Bạn cần vào bang hội trước");
                            return;
                        }
                        if (!flagWar.vips.contains(player.id)) {
                            int index = (int) elements[1];
                            if (index == -1) {
                                List<Command> commands = new ArrayList<>();
                                commands.add(new Command(CommandName.FLAG_WAR, "OK", player, 0, 0));
                                commands.add(new Command(CommandName.FLAG_WAR, "Không", player, 0, 1));
                                player.createMenu(NpcName.ME, "Bạn có muốn phù hộ với giá 20 Ruby không?\n(Phù hộ sẽ giúp bạn mạnh mẽ hơn)", commands);
                                return;
                            }
                            if (index == 0) {
                                if (!player.isEnoughMoney(TypePrice.RUBY, 20)) {
                                    return;
                                }
                                player.downMoney(TypePrice.RUBY, 20);
                                flagWar.vips.add(player.id);
                            }
                        }
                        flagWar.enter(player);
                    } else if (action == 1) {
                        player.service.setNotification(FlagWar.notes);
                    } else if (action == 2) {
                        Shop shop = ShopManager.getInstance().shops.get(ShopType.SHOP_FLAG_WAR);
                        if (shop != null) {
                            shop.show(player);
                        }
                    } else if (action == 3) {
                        FlagWar flagWar = MapManager.getInstance().flagWar;
                        if (flagWar == null) {
                            return;
                        }
                        if (player.zone instanceof ZoneFlagWar zoneFlagWar) {
                            if (zoneFlagWar.holder != null && zoneFlagWar.holder.id == player.id) {
                                zoneFlagWar.setHolder(null);
                                player.bagLoot = -1;
                                player.bag = -1;
                                zoneFlagWar.service.setBag(player);
                                zoneFlagWar.leave(zoneFlagWar.npc);
                                zoneFlagWar.upPoint(player, 100);
                            }
                        }
                    } else if (action == 4) {
                        FlagWar flagWar = MapManager.getInstance().flagWar;
                        if (flagWar == null) {
                            return;
                        }
                        if (player.zone instanceof ZoneFlagWar zoneFlagWar) {
                            player.service.setNotification(zoneFlagWar.getTops());
                        }
                    }
                    return;
                }

                case CommandName.TUI_TRANG_BI_PET: {
                    if (player.isBagFull()) {
                        player.addInfo(Player.INFO_RED, Language.ME_BAG_FULL);
                        return;
                    }
                    Item itemUse = (Item) elements[1];
                    if (itemUse.quantity < 1) {
                        return;
                    }
                    if (Arrays.stream(player.itemsBag).noneMatch(itemUse::equals)) {
                        return;
                    }
                    player.removeQuantityItemBag(itemUse, 1);
                    int optionID = (int) elements[0];
                    HashMap<Integer, Integer[]> params = new HashMap<>();
                    params.put(0, new Integer[]{300, 1000});
                    params.put(1, new Integer[]{300, 1000});
                    params.put(2, new Integer[]{1000, 2000});
                    params.put(3, new Integer[]{300, 1000});
                    params.put(4, new Integer[]{300, 1000});
                    params.put(5, new Integer[]{300, 1000});
                    params.put(6, new Integer[]{300, 1000});
                    params.put(25, new Integer[]{3, 10});
                    params.put(26, new Integer[]{500, 1000});
                    params.put(27, new Integer[]{1000, 2000});
                    params.put(28, new Integer[]{500, 1000});
                    params.put(29, new Integer[]{500, 1000});
                    params.put(30, new Integer[]{500, 1000});
                    params.put(31, new Integer[]{3, 10});
                    params.put(32, new Integer[]{3, 10});
                    params.put(99, new Integer[]{1000, 2000});
                    int param = params.get(optionID)[1];
                    int upgrade = itemUse.getUpgrade();
                    for (int i = 0; i < upgrade; i++) {
                        param = param * 11 / 10;
                    }
                    Item item = ItemManager.getInstance().createItem(Utils.nextArray(new int[]{
                            ItemName.VUOT_LONG_THAN,
                            ItemName.GIAP_LONG_BAO,
                            ItemName.MU_VUONG_LONG,
                            ItemName.GONG_THIEN_LONG,
                            ItemName.LINH_HON_LONG_THE,
                            ItemName.YEN_THAN_LONG,
                            ItemName.DAY_CUONG_THAN_THU,
                            ItemName.GIAP_VAI_LONG_THAN,
                    }), false);
                    item.options.add(new ItemOption(optionID, param));
                    item.options.add(new ItemOption(19, upgrade));
                    int star = itemUse.getMaxStar();
                    if (star > 0) {
                        item.options.add(new ItemOption(67, star));
                    }
                    item.isLock = true;
                    player.addItem(item, true);
                    return;
                }

            }
        } catch (Exception e) {
            logger.error("performAction", e);
        } finally {
            player.lockAction.unlock();
        }
    }
}
