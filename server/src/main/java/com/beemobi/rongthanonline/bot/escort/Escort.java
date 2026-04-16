package com.beemobi.rongthanonline.bot.escort;

import com.beemobi.rongthanonline.bot.boss.Boss;
import com.beemobi.rongthanonline.entity.player.Player;
import com.beemobi.rongthanonline.map.Map;
import com.beemobi.rongthanonline.map.MapManager;
import com.beemobi.rongthanonline.map.Zone;
import com.beemobi.rongthanonline.service.Service;
import com.beemobi.rongthanonline.util.Utils;

public abstract class Escort extends Player {
    public static final int MAX_DISTANCE = 900;
    public Player follower;
    public int tick;

    public Escort(String name) {
        super();
        id = Boss.autoIncrease++;
        this.name = name;
        hp = maxHp = mp = maxMp = baseHp = baseMp = 500;
        damage = baseDamage = 10;
        service = new Service(this);
        speed = 4;
        refreshPart();
    }

    @Override
    public void updateEveryFiveSeconds(long now) {
        super.updateEveryFiveSeconds(now);
        if (zone != null && follower == null) {
            move();
            chatWhenMove();
        }
    }

    @Override
    public void updateEveryOneSeconds(long now) {
        super.updateEveryOneSeconds(now);
        if (zone != null && follower != null) {
            move();
            chatWhenMove();
        }
    }

    public void chatWhenMove() {
        chat("éc éc");
    }

    public void move() {
        int x;
        if (follower != null) {
            if (follower.zone != this.zone) {
                tick++;
                if (tick > 10) {
                    tick = 0;
                    stopFollow();
                    return;
                }
                return;
            } else {
                tick = 0;
            }
            x = this.x + (follower.x > this.x ? 1 : (-1)) * Utils.nextInt(300, 400);
        } else {
            x = this.x + Utils.nextInt(-150, 150);
        }
        if (x < 300) {
            x = Utils.nextInt(180, 270);
        } else if (x > zone.map.template.width - 300) {
            x = zone.map.template.width - Utils.nextInt(180, 270);
        }
        int y = zone.map.getYSd(x);
        moveTo(x, y);
    }

    public void moveTo(int x, int y) {
        this.x = x;
        this.y = y;
        if (zone != null) {
            zone.service.move(this);
        }
    }

    @Override
    public void refreshPart() {
        setSkin();
    }

    public abstract void setSkin();

    @Override
    public boolean isPlayer() {
        return false;
    }

    @Override
    public boolean isEscort() {
        return true;
    }

    public abstract boolean isEnd();

    public void end() {
        if (follower != null) {
            follower.escort = null;
            follower = null;
        }
        if (zone != null) {
            zone.leave(this);
        }
    }

    public void stopFollow() {
        if (follower != null) {
            follower.escort = null;
            follower.addInfo(Player.INFO_RED, String.format("Bạn đã di chuyển quá xa, %s đã mất dấu bạn", this.name));
            this.follower = null;
        }
    }

    public void enter(int[] maps) {
        Map map = MapManager.getInstance().maps.get(Utils.nextArray(maps));
        Zone zone = map.findOrRandomZone(-1);
        this.x = Utils.nextInt(300, zone.map.template.width - 300);
        this.y = zone.map.getYSd(this.x);
        zone.enter(this);
    }

    public void setFollower(Player player) {
        if (follower != null) {
            return;
        }
        /*if (follower != null) {
            follower.escort = null;
        } */
        tick = 0;
        this.follower = player;
        player.escort = this;
        chat(player.name);
    }
}
