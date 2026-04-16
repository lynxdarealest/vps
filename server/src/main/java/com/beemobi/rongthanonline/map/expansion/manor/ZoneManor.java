package com.beemobi.rongthanonline.map.expansion.manor;

import com.beemobi.rongthanonline.bot.boss.manor.FideCold;
import com.beemobi.rongthanonline.bot.boss.manor.FideFire;
import com.beemobi.rongthanonline.entity.monster.Monster;
import com.beemobi.rongthanonline.entity.monster.MonsterLevelBoss;
import com.beemobi.rongthanonline.entity.player.Player;
import com.beemobi.rongthanonline.item.OptionName;
import com.beemobi.rongthanonline.map.Zone;
import com.beemobi.rongthanonline.map.Map;
import com.beemobi.rongthanonline.map.MapName;
import com.beemobi.rongthanonline.model.MessageTime;
import com.beemobi.rongthanonline.model.PointWeeklyType;
import com.beemobi.rongthanonline.util.Utils;

import java.util.List;
import java.util.stream.Collectors;

public class ZoneManor extends Zone {
    public Manor manor;
    public boolean isUpdateEntity;

    public ZoneManor(Map map, Manor manor) {
        super(map);
        this.manor = manor;
        int per = manor.level * manor.level * Math.max(manor.level / 10, 1) / 3;
        if (manor.level < 20) {
            per = Math.max(per / 10, 1);
        } else if (manor.level < 30) {
            per = Math.max(per / 8, 1);
        } else if (manor.level < 40) {
            per = Math.max(per / 6, 1);
        } else if (manor.level < 50) {
            per = Math.max(per / 4, 1);
        }
        for (Monster monster : monsters) {
            monster.level = manor.level;
            monster.baseHp = monster.template.hp * per;
            monster.maxHp = monster.getMaxHp();
            monster.hp = monster.maxHp;
            monster.damage = (long) monster.template.damage * per / 4;
            monster.isAutoRefresh = false;
        }
        switch (map.template.id) {
            case MapName.CUA_LUA_1:
            case MapName.CUA_BANG_1:
                for (Monster monster : monsters) {
                    monster.options[OptionName.GIAM_SAT_THUONG] = 10000;
                }
                for (int i = 0; i < 5; i++) {
                    Monster monster = findAndRandomMonster(MonsterLevelBoss.NORMAL);
                    if (monster != null) {
                        monster.options[OptionName.GIAM_SAT_THUONG] = 0;
                        monster.upLevelBoss(Utils.nextInt(4) == 0 ? MonsterLevelBoss.THU_LINH : MonsterLevelBoss.TINH_ANH, false);
                    }
                }
                break;
            case MapName.CUA_LUA_2:
            case MapName.CUA_BANG_2:
                for (Monster monster : monsters) {
                    monster.upLevelBoss(Utils.nextInt(2) == 0 ? MonsterLevelBoss.TINH_ANH : MonsterLevelBoss.THU_LINH, false);
                }
                break;
            case MapName.CUA_LUA_3:
            case MapName.CUA_BANG_3:
                for (Monster monster : monsters) {
                    monster.options[OptionName.NE_DON] = 10000;
                }
                break;
            case MapName.CUA_LUA_4:
            case MapName.CUA_BANG_4:
                for (Monster monster : monsters) {
                    monster.options[OptionName.GIAM_SAT_THUONG] = 40000;
                }
                break;
            case MapName.CUA_LUA_5:
            case MapName.CUA_BANG_5:
                for (Monster monster : monsters) {
                    monster.options[OptionName.PHAN_SAT_THUONG] = 40000;
                }
                break;
            case MapName.CUA_LUA_6:
            case MapName.CUA_BANG_6:
                for (Monster monster : monsters) {
                    int percent = Utils.nextInt(3);
                    if (percent == 0) {
                        monster.options[OptionName.NE_DON] = 10000;
                    } else if (percent == 1) {
                        monster.options[OptionName.GIAM_SAT_THUONG] = 40000;
                    } else if (percent == 2) {
                        monster.options[OptionName.PHAN_SAT_THUONG] = 40000;
                    }
                }
                break;
        }
    }

    @Override
    public void update() {
        super.update();
        long now = System.currentTimeMillis();
        if (now > manor.endTime) {
            manor.close();
            return;
        }
        if (map.isFinish) {
            return;
        }
        List<Monster> monsterList = findMonsterLives();
        switch (map.template.id) {
            case MapName.CUA_LUA_1:
            case MapName.CUA_BANG_1:
                if (!isUpdateEntity) {
                    boolean flag = false;
                    for (Monster monster : monsterList) {
                        if (monster.levelBoss != MonsterLevelBoss.NORMAL) {
                            flag = true;
                            break;
                        }
                    }
                    if (!flag) {
                        isUpdateEntity = true;
                        for (Monster monster : monsterList) {
                            monster.options[OptionName.GIAM_SAT_THUONG] = 0;
                        }
                    }
                }
                break;
            case MapName.CUA_LUA_6:
                if (!isUpdateEntity) {
                    if (monsterList.isEmpty()) {
                        isUpdateEntity = true;
                        FideFire boss = new FideFire(manor.level);
                        boss.setLocation(this);
                        return;
                    }
                }
                break;
            case MapName.CUA_BANG_6:
                if (!isUpdateEntity) {
                    if (monsterList.isEmpty()) {
                        isUpdateEntity = true;
                        FideCold boss = new FideCold(manor.level);
                        boss.setLocation(this);
                        return;
                    }
                }
                break;

        }
        map.isFinish = monsterList.isEmpty() && getPlayers(TYPE_BOSS).isEmpty();
        if (map.isFinish && (map.template.id == MapName.CUA_LUA_6 || map.template.id == MapName.CUA_BANG_6)) {
            manor.endTime = now + 60000;
            manor.service.setTimeRemaining(60000);
            manor.service.addInfo(Player.INFO_RED, "Chúc mừng bang hội của bạn đã phá đảo Vùng đất bí ẩn");
            List<Player> playerList = getPlayers(TYPE_PLAYER);
            for (Player player : playerList) {
                if (player.taskMain != null && player.taskMain.template.id == 22 && player.taskMain.index == 2) {
                    player.nextTaskParam();
                }
                player.upPointWeekly(PointWeeklyType.ACTIVE, 10);
            }
            return;
        }

    }

    @Override
    public List<Monster> findMonsterLives() {
        lockMonster.readLock().lock();
        try {
            return monsters.stream().filter(m -> !m.isDead() && m.isMonster()).collect(Collectors.toList());
        } finally {
            lockMonster.readLock().unlock();
        }
    }

    @Override
    public void enter(Player player) {
        super.enter(player);
        if (player.isPlayer()) {
            player.addMessageTime(MessageTime.FORGOT_CITY, manor.getCountDown());
        }
    }
}
