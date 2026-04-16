package com.beemobi.rongthanonline.top.player;

import com.beemobi.rongthanonline.entity.player.Player;
import com.beemobi.rongthanonline.top.TopInfo;

public abstract class TopPlayerInfo extends TopInfo {
    public transient Player player;

    public TopPlayerInfo(Player player) {
        this.player = player;
        if (player != null) {
            this.id = player.id;
        }
    }

    @Override
    public void setObject(Object object) {
        if (object == null) {
            player = null;
        } else {
            player = (Player) object;
        }
    }

    @Override
    public void clearObject(Object object) {
        setObject(null);
    }

    @Override
    public boolean isOnline() {
        return player != null;
    }

    @Override
    public String getName() {
        if (player != null) {
            name = player.name;
        }
        return name;
    }

    @Override
    public int getGender() {
        if (player != null) {
            gender = player.gender;
        }
        return gender;
    }
}
