package com.beemobi.rongthanonline.upgrade;

import com.beemobi.rongthanonline.entity.player.Player;
import com.beemobi.rongthanonline.network.Message;
import com.beemobi.rongthanonline.network.MessageName;
import org.apache.log4j.Logger;

import java.util.ArrayList;

public abstract class Upgrade {
    private static final Logger logger = Logger.getLogger(Upgrade.class);
    public ArrayList<String> notes;
    public UpgradeType type;
    public String name;
    public String command;

    public Upgrade(UpgradeType type, String name, String command) {
        this.type = type;
        this.name = name;
        this.command = command;
        notes = new ArrayList<>();
    }

    public int formatType() {
        if (type == UpgradeType.UPGRADE_ITEM) {
            return 0;
        }
        if (type == UpgradeType.UPGRADE_STONE) {
            return 1;
        }
        return 2;
    }

    public void showTab(Player player) {
        try {
            player.upgradeType = type;
            Message msg = new Message(MessageName.SHOW_TAB_PANEL);
            msg.writer().writeByte(2);
            msg.writer().writeUTF(name);
            msg.writer().writeUTF(command);
            msg.writer().writeByte(formatType());
            msg.writer().writeByte(notes.size());
            for (String des : notes) {
                msg.writer().writeUTF(des);
            }
            player.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
            logger.error("showTab", e);
        }
        player.service.setListItemLight(getIndexCanUpgrade(player));
    }

    public abstract boolean[] getIndexCanUpgrade(Player player);

    public abstract void upgrade(Message message, Player player);

    public abstract void confirmUpgrade(Player player, Object[] objects);
}
