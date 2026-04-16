package com.beemobi.rongthanonline.entity.player.action;

import com.beemobi.rongthanonline.command.Command;
import com.beemobi.rongthanonline.command.CommandName;
import com.beemobi.rongthanonline.common.Language;
import com.beemobi.rongthanonline.data.history.HistoryTradeData;
import com.beemobi.rongthanonline.entity.player.Player;
import com.beemobi.rongthanonline.entity.player.PlayerManager;
import com.beemobi.rongthanonline.entity.player.json.ListItemInfo;
import com.beemobi.rongthanonline.item.Item;
import com.beemobi.rongthanonline.map.MapName;
import com.beemobi.rongthanonline.model.TradeStatus;
import com.beemobi.rongthanonline.network.Message;
import com.beemobi.rongthanonline.npc.NpcName;
import com.beemobi.rongthanonline.server.Server;
import com.beemobi.rongthanonline.server.ServerMaintenance;
import com.beemobi.rongthanonline.util.Utils;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

public class TradeAction extends Action {
    private static final Logger logger = Logger.getLogger(TradeAction.class);

    private static final int MOI_GIAO_DICH = 0;
    private static final int DONG_Y_GIAO_DICH = 1;
    private static final int HUY_GIAO_DICH = 2;
    private static final int ADD_ITEM = 3;
    private static final int REMOVE_ITEM = 4;
    private static final int ADD_COIN = 5;
    private static final int LOCK_OR_CONFIRM = 6;

    public boolean isTrading;
    public int targetId = -1;
    public TradeStatus status;
    public LinkedHashMap<Integer, Integer> items;
    public long coin;
    public int requestId;
    public long timeRequestTrade;

    public TradeAction(Player player) {
        super(player);
        items = new LinkedHashMap<>();
    }

    @Override
    public void action(Message message) {
        player.lockAction.lock();
        try {
            if (Server.getInstance().isInterServer()) {
                player.addInfo(Player.INFO_RED, Language.CANCEL_ACTION_WHEN_SERVER_IS_INTER_SERVER);
                return;
            }
            if (player.isProtect) {
                player.addInfo(Player.INFO_RED, Language.TAI_KHOAN_DANG_DUOC_BAO_VE);
                return;
            }
            long now = System.currentTimeMillis();
            if (player.lockInfo != null && now < player.lockInfo.tradeTime) {
                player.service.startDialogOk(String.format("Tài khoản của bạn bị khóa giao dịch đến %s", Utils.formatTime(player.lockInfo.tradeTime)));
                return;
            }
            if (player.level < 10) {
                player.addInfo(Player.INFO_RED, "Yêu cầu trình độ cấp 10 trở lên");
                return;
            }
            if (player.zone == null || player.isInSurvival() || player.zone.map.template.id == MapName.DAI_HOI_VO_THUAT) {
                player.addInfo(Player.INFO_RED, Language.CANT_ACTION);
                return;
            }
            if (ServerMaintenance.getInstance().isRunning) {
                player.addInfo(Player.INFO_RED, Language.CANCEL_ACTION_WHEN_SERVER_MAINTENANCE);
                return;
            }
            int type = message.reader().readByte();
            switch (type) {
                case MOI_GIAO_DICH:
                    requestTrade(message.reader().readInt());
                    return;

                case DONG_Y_GIAO_DICH:
                    return;

                case HUY_GIAO_DICH:
                    cancelTrade();
                    return;

                case ADD_ITEM:
                    addItemTrade(message.reader().readByte(), message.reader().readInt());
                    return;

                case REMOVE_ITEM:
                    removeItemTrade(message.reader().readByte());
                    return;

                case ADD_COIN:
                    addCoin(message.reader().readLong());
                    return;

                case LOCK_OR_CONFIRM: {
                    if (status == TradeStatus.GIAO_DICH) {
                        acceptItemTrade();
                        return;
                    }
                    if (status == TradeStatus.KHOA_GIAO_DICH) {
                        confirmTrade();
                        return;
                    }
                    return;
                }
            }
        } catch (Exception ex) {
            logger.error("action", ex);
        } finally {
            player.lockAction.unlock();
        }
    }

    private void confirmTrade() {
        if (!isTrading || status != TradeStatus.KHOA_GIAO_DICH) {
            return;
        }
        Player trader = player.zone.findPlayerById(targetId);
        if (trader == null) {
            cancelTrade();
            return;
        }
        TradeAction traderAction = (TradeAction) trader.tradeAction;
        if (traderAction.status == TradeStatus.GIAO_DICH) {
            player.addInfo(Player.INFO_RED, "Vui lòng chờ đối phương khóa giao dịch");
            return;
        }
        status = TradeStatus.DONG_Y_GIAO_DICH;
        if (traderAction.status == TradeStatus.KHOA_GIAO_DICH) {
            player.service.confirmTrade();
            return;
        }
        if (traderAction.status != TradeStatus.DONG_Y_GIAO_DICH) {
            return;
        }
        // chuyển item cho nhau
        if (items.size() > trader.getCountItemBagEmpty()) {
            cancelTrade();
            player.addInfo(Player.INFO_RED, String.format("Giao dịch bị hủy bỏ vì túi đồ của %s không đủ chỗ chứa", trader.name));
            trader.addInfo(Player.INFO_RED, "Giao dịch bị hủy bỏ vì túi đồ của bạn không đủ chỗ chứa");
            return;
        }
        if (traderAction.items.size() > player.getCountItemBagEmpty()) {
            cancelTrade();
            trader.addInfo(Player.INFO_RED, String.format("Giao dịch bị hủy bỏ vì túi đồ của %s không đủ chỗ chứa", player.name));
            player.addInfo(Player.INFO_RED, "Giao dịch bị hủy bỏ vì túi đồ của bạn không đủ chỗ chứa");
            return;
        }
        if (player.xu < coin || coin < 0 || trader.xu < traderAction.coin || traderAction.coin < 0) {
            cancelTrade();
            return;
        }
        HistoryTradeData[] histories = new HistoryTradeData[2];
        histories[0] = new HistoryTradeData(player, trader);
        histories[1] = new HistoryTradeData(trader, player);
        if (coin > 0 || traderAction.coin > 0) {
            long xu = traderAction.coin - coin;
            if (xu != 0) {
                player.upXu(xu);
            }
            xu = coin - traderAction.coin;
            if (xu != 0) {
                trader.upXu(xu);
            }
        }
        ArrayList<Item> itemsMe = new ArrayList<>();
        for (int index : items.keySet()) {
            Item item = player.itemsBag[index];
            int quantity = items.get(index);
            if (item != null && item.quantity >= quantity) {
                Item trade = item.cloneItem();
                trade.quantity = quantity;
                itemsMe.add(trade);
                item.quantity -= quantity;
                if (item.quantity <= 0) {
                    player.itemsBag[index] = null;
                }
            }
        }
        ArrayList<Item> itemsTrader = new ArrayList<>();
        for (int index : traderAction.items.keySet()) {
            Item item = trader.itemsBag[index];
            int quantity = traderAction.items.get(index);
            if (item != null && item.quantity >= quantity) {
                Item trade = item.cloneItem();
                trade.quantity = quantity;
                itemsTrader.add(trade);
                item.quantity -= quantity;
                if (item.quantity <= 0) {
                    trader.itemsBag[index] = null;
                }
            }
        }
        for (Item item : itemsMe) {
            trader.addItem(item);
        }
        for (Item item : itemsTrader) {
            player.addItem(item);
        }
        histories[0].setItemTrade(itemsMe, coin, itemsTrader, traderAction.coin);
        histories[1].setItemTrade(itemsTrader, traderAction.coin, itemsMe, coin);
        histories[0].setAfterAndSave(player);
        histories[1].setAfterAndSave(trader);
        player.service.setItemBag();
        trader.service.setItemBag();
        clearTrade();
        traderAction.clearTrade();
        trader.service.completeTrade();
        player.service.completeTrade();
    }

    private void acceptItemTrade() {
        if (!isTrading || status != TradeStatus.GIAO_DICH) {
            return;
        }
        Player trader = player.zone.findPlayerById(targetId);
        if (trader == null) {
            cancelTrade();
            return;
        }
        status = TradeStatus.KHOA_GIAO_DICH;
        player.service.lockTrade();
        player.service.sendListItemTrade(trader);
    }

    private void addCoin(long xu) {
        if (!isTrading || status != TradeStatus.GIAO_DICH) {
            return;
        }
        if (xu <= 0) {
            return;
        }
        if (xu > player.xu) {
            player.addInfo(Player.INFO_RED, "Bạn không đủ xu để giao dịch");
            return;
        }
        coin = xu;
        player.service.setCoinTrade();
    }

    private void removeItemTrade(int index) {
        if (!isTrading || status != TradeStatus.GIAO_DICH) {
            return;
        }
        if (index < 0 || index >= player.itemsBag.length) {
            return;
        }
        if (!items.containsKey(index)) {
            return;
        }
        items.remove(index);
        player.service.removeItemTrade(index);
    }

    private void addItemTrade(int index, int quantity) {
        if (!isTrading || status != TradeStatus.GIAO_DICH) {
            return;
        }
        if (index < 0 || index >= player.itemsBag.length) {
            return;
        }
        if (items.containsKey(index)) {
            return;
        }
        Item item = player.itemsBag[index];
        if (item == null) {
            return;
        }
        if (!item.isCanTrade()) {
            player.addInfo(Player.INFO_RED, "Không thể giao dịch vật phẩm này");
            return;
        }
        if (item.quantity < quantity || quantity < 1) {
            player.addInfo(Player.INFO_RED, "Số lượng không hợp lệ");
            return;
        }
        items.put(index, quantity);
        player.service.addItemTrade(index, quantity);
    }

    public void cancelTrade() {
        if (!isTrading) {
            return;
        }
        if (status == TradeStatus.DONG_Y_GIAO_DICH) {
            return;
        }
        Player trader = PlayerManager.getInstance().findPlayerById(targetId);
        clearTrade();
        if (trader != null) {
            ((TradeAction) trader.tradeAction).clearTrade();
        }
    }

    public void clearTrade() {
        if (status != TradeStatus.DONG_Y_GIAO_DICH) {
            player.service.cancelTrade();
        }
        timeRequestTrade = System.currentTimeMillis();
        isTrading = false;
        coin = 0;
        items.clear();
        targetId = -1;
        requestId = -1;
        status = TradeStatus.NORMAL;
    }

    public void acceptTradeRequest(int playerId) {
        if (player.id == playerId) {
            return;
        }
        if (player.level < 10) {
            player.addInfo(Player.INFO_RED, "Yêu cầu trình độ cấp 10 trở lên");
            return;
        }
        if (isTrading) {
            player.addInfo(Player.INFO_RED, Language.CANCEL_ACTION_WHEN_TRADE);
            return;
        }
        long now = System.currentTimeMillis();
        if (now - timeRequestTrade < 5000) {
            player.addInfo(Player.INFO_RED, "Vui lòng thử lại sau giây lát");
            return;
        }
        timeRequestTrade = now;
        Player trader = player.zone.findPlayerById(playerId);
        if (trader == null) {
            player.addInfo(Player.INFO_RED, "Người chơi không có trong khu vực");
            return;
        }
        if (trader.level < 10) {
            player.addInfo(Player.INFO_RED, "Người chơi chưa đạt cấp 10");
            return;
        }
        if (trader.isTrading()) {
            player.addInfo(Player.INFO_RED, "Người chơi đang giao dịch khác");
            return;
        }
        TradeAction traderAction = (TradeAction) trader.tradeAction;
        if (traderAction.requestId != player.id) {
            return;
        }
        if (now - traderAction.timeRequestTrade > 15000) {
            player.addInfo(Player.INFO_RED, "Lời mời đã hết hạn");
            return;
        }
        traderAction.coin = 0;
        traderAction.items.clear();
        traderAction.status = TradeStatus.GIAO_DICH;
        traderAction.targetId = player.id;
        coin = 0;
        items.clear();
        status = TradeStatus.GIAO_DICH;
        targetId = trader.id;
        isTrading = true;
        traderAction.isTrading = true;
        player.service.startTrade();
        trader.service.startTrade();
    }

    private void requestTrade(int playerId) {
        if (isTrading) {
            player.addInfo(Player.INFO_RED, Language.CANCEL_ACTION_WHEN_TRADE);
            return;
        }
        long now = System.currentTimeMillis();
        if (now - timeRequestTrade < 5000) {
            player.addInfo(Player.INFO_RED, "Vui lòng thử lại sau giây lát");
            return;
        }
        timeRequestTrade = now;
        if (playerId == player.id || playerId < 0) {
            return;
        }
        Player trader = player.zone.findPlayerById(playerId);
        if (trader == null) {
            player.addInfo(Player.INFO_RED, "Người chơi không có trong khu vực");
            return;
        }
        if (trader.level < 10) {
            player.addInfo(Player.INFO_RED, "Người chơi chưa đạt cấp 10");
            return;
        }
        TradeAction traderAction = (TradeAction) trader.tradeAction;
        if (traderAction.isTrading) {
            player.addInfo(Player.INFO_RED, "Người chơi đang giao dịch khác");
            return;
        }
        if (!player.isCanSendAction(trader)) {
            player.addInfo(Player.INFO_RED, "Người chơi đang chặn người lạ, bạn cần phải kết bạn trước");
            return;
        }
        requestId = playerId;
        List<Command> commands = new ArrayList<>();
        commands.add(new Command(CommandName.ACCEPT_INVITE_TRADE, "Đồng ý", trader, player.id));
        commands.add(new Command(CommandName.CANCEL, "Không", trader));
        trader.createMenu(NpcName.ME, String.format("%s muốn giao dịch với bạn, bạn có đồng ý không?", player.name), commands);
        player.addInfo(Player.INFO_YELLOW, String.format("Đã gửi lời mời giao dịch đến %s", trader.name));
    }
}
