package com.beemobi.rongthanonline.entity.player.action;

import com.beemobi.rongthanonline.entity.player.Player;
import com.beemobi.rongthanonline.network.Message;

public abstract class Action {
    public final Player player;

    public Action(Player player) {
        this.player = player;
    }

    public  abstract void action(Message message);
}
