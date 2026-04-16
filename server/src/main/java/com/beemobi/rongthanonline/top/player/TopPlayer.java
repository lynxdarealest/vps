package com.beemobi.rongthanonline.top.player;

import com.beemobi.rongthanonline.entity.player.Player;
import com.beemobi.rongthanonline.top.Top;
import com.beemobi.rongthanonline.top.TopType;

public abstract class TopPlayer extends Top {

    public TopPlayer(TopType type, String name) {
        super(name, type);
    }

    @Override
    public void clearObject(Object object) {
        lock.readLock().lock();
        try {
            Player player = (Player) object;
            elements.stream().filter(i -> i.id == player.id).findFirst().ifPresent(info -> info.clearObject(player));
        } finally {
            lock.readLock().unlock();
        }
    }

}
