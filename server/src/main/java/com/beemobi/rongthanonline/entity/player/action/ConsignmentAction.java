package com.beemobi.rongthanonline.entity.player.action;

import com.beemobi.rongthanonline.command.Command;
import com.beemobi.rongthanonline.command.CommandName;
import com.beemobi.rongthanonline.common.Language;
import com.beemobi.rongthanonline.entity.player.Player;
import com.beemobi.rongthanonline.network.Message;
import com.beemobi.rongthanonline.npc.NpcName;
import com.beemobi.rongthanonline.server.Server;
import com.beemobi.rongthanonline.server.ServerMaintenance;
import com.beemobi.rongthanonline.shop.ShopManager;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

public class ConsignmentAction extends Action {
    private static final Logger logger = Logger.getLogger(ConsignmentAction.class);

    public ConsignmentAction(Player player) {
        super(player);
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
            if (ServerMaintenance.getInstance().isRunning) {
                player.addInfo(Player.INFO_RED, Language.CANCEL_ACTION_WHEN_SERVER_MAINTENANCE);
                return;
            }
            if (player.isTrading()) {
                return;
            }
            int type = message.reader().readByte();
            if (type == -1) {
                List<Command> commands = new ArrayList<>();
                commands.add(new Command(CommandName.BEE_CONSIGNMENT, "Chợ", player));
                commands.add(new Command(CommandName.HUONG_DAN_BEE_CONSIGNMENT, "Hướng dẫn", player));
                player.createMenu(NpcName.ME, "", commands);
            } else if (type == 0) {
                int index = message.reader().readByte();
                int quantity = message.reader().readInt();
                int price = message.reader().readInt();
                ShopManager.getInstance().consignment.addItem(player, index, quantity, price);
            } else if (type == 1) {
                ShopManager.getInstance().consignment.buyItem(player, message.reader().readInt());
            } else if (type == 2) {
                ShopManager.getInstance().consignment.getItem(player, message.reader().readInt());
            }
        } catch (Exception ex) {
            logger.error("action", ex);
        } finally {
            player.lockAction.unlock();
        }
    }
}
