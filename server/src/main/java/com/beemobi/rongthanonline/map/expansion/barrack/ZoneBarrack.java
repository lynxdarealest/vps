package com.beemobi.rongthanonline.map.expansion.barrack;

import com.beemobi.rongthanonline.bot.boss.barrack.BlackVI;
import com.beemobi.rongthanonline.bot.boss.barrack.MetaV;
import com.beemobi.rongthanonline.bot.boss.barrack.WhiteIII;
import com.beemobi.rongthanonline.bot.boss.barrack.YellowIV;
import com.beemobi.rongthanonline.entity.monster.Monster;
import com.beemobi.rongthanonline.entity.monster.MonsterLevelBoss;
import com.beemobi.rongthanonline.entity.player.Player;
import com.beemobi.rongthanonline.item.OptionName;
import com.beemobi.rongthanonline.map.Zone;
import com.beemobi.rongthanonline.map.Map;
import com.beemobi.rongthanonline.map.MapName;
import com.beemobi.rongthanonline.model.MessageTime;
import com.beemobi.rongthanonline.util.Utils;

import java.util.List;
import java.util.stream.Collectors;

public class ZoneBarrack extends Zone {
    public Barrack barrack;
    public boolean isUpdateEntity;
    public long timeStartBattle;

    public ZoneBarrack(Map map, Barrack barrack) {
        super(map);
        this.barrack = barrack;
        int per = (barrack.level - 9) * (barrack.level / 10);
        int def = per / 25;
        if (def < 1) {
            def = 1;
        }
        float fdef = (float) (0.5 * (float) ((int) (barrack.level / 10) + 1));
        if (fdef < 1) {
            fdef = 1;
        }
        for (Monster monster : monsters) {
            monster.level = barrack.level;
            monster.baseHp = monster.template.hp * per * def;
            if (barrack.level >= 40) {
                if (barrack.countPlayer < 3) {
                    monster.baseHp = monster.baseHp / 3;
                } else if (barrack.countPlayer < 5) {
                    monster.baseHp -= monster.baseHp / 3;
                }
            }
            monster.maxHp = monster.getMaxHp();
            monster.hp = monster.maxHp;
            monster.damage = (long) ((float) monster.template.damage * (float) per * fdef);
            if (monster.level > 40) {
                monster.damage = (long) ((float) monster.damage * fdef);
            }
            if (map.template.id == MapName.TANG_1 || map.template.id == MapName.TANG_LAU) {
                monster.options[OptionName.PHAN_SAT_THUONG] = 8000;
            }
        }
        switch (map.template.id) {
            case MapName.SAN_TRUOC: {
                Monster monster = findAndRandomMonster(MonsterLevelBoss.NORMAL);
                if (monster != null) {
                    monster.upLevelBoss(MonsterLevelBoss.TINH_ANH, false);
                }
                break;
            }

            case MapName.SAN_SAU: {
                Monster monster = findAndRandomMonster(MonsterLevelBoss.NORMAL);
                if (monster != null) {
                    monster.upLevelBoss(MonsterLevelBoss.TINH_ANH, false);
                }
                Monster m = findAndRandomMonster(MonsterLevelBoss.NORMAL);
                if (m != null) {
                    m.upLevelBoss(MonsterLevelBoss.TINH_ANH, false);
                }
                break;
            }
        }
    }

    @Override
    public void enter(Player player) {
        super.enter(player);
        if (player.isPlayer()) {
            player.addMessageTime(MessageTime.BAN_DOANH_RED, barrack.getCountDown());
        }
    }

    @Override
    public void update() {
        super.update();
        long now = System.currentTimeMillis();
        if (now > barrack.endTime) {
            barrack.close();
            return;
        }
        switch (map.template.id) {
            case MapName.SAN_TAP: {
                if (!isUpdateEntity) {
                    List<Monster> monsterList = findMonsterDies();
                    if (!monsterList.isEmpty()) {
                        isUpdateEntity = true;
                    }
                    if (isUpdateEntity) {
                        upLevelBossMonsterAndRefresh(MonsterLevelBoss.THU_LINH, 1);
                    }
                }
                break;
            }
            case MapName.PHONG_DIEU_KHIEN:
            case MapName.NHA_KHO:
            case MapName.TUONG_THANH_1: {
                if (!isUpdateEntity) {
                    List<Monster> monsterList = findMonsterDies();
                    if (!monsterList.isEmpty()) {
                        isUpdateEntity = true;
                    }
                    if (isUpdateEntity) {
                        upLevelBossMonsterAndRefresh(MonsterLevelBoss.THU_LINH, 2);
                    }
                }
                break;
            }

            case MapName.PHONG_GIAM:
            case MapName.PHONG_CHO_1:
            case MapName.PHONG_CHO_2: {
                if (!isUpdateEntity) {
                    List<Player> playerList = getPlayers(TYPE_PLAYER);
                    if (!playerList.isEmpty()) {
                        isUpdateEntity = true;
                    }
                    if (isUpdateEntity) {
                        timeStartBattle = now;
                        barrack.timeBattle = 190 - barrack.players.size() * 10;
                        barrack.service.setTimeFlight(this);
                        refreshAllMonster();
                        if (map.template.id == MapName.PHONG_CHO_1) {
                            upLevelBossMonsterAndRefresh(MonsterLevelBoss.TINH_ANH, 2);
                        } else if (map.template.id == MapName.PHONG_CHO_2) {
                            upLevelBossMonsterAndRefresh(MonsterLevelBoss.THU_LINH, 1);
                        }
                    }
                }
                break;
            }

            case MapName.THAP_CANH: {
                if (!isUpdateEntity) {
                    isUpdateEntity = true;
                    WhiteIII boss = new WhiteIII(barrack.level);
                    boss.setLocation(this);
                }
                break;
            }

            case MapName.TUONG_THANH_2: {
                if (!isUpdateEntity) {
                    isUpdateEntity = true;
                    YellowIV boss = new YellowIV(barrack.level);
                    boss.setLocation(this);
                }
                break;
            }

            case MapName.PHONG_CHO_3: {
                if (!isUpdateEntity) {
                    isUpdateEntity = true;
                    MetaV boss = new MetaV(barrack.level);
                    boss.setLocation(this);
                }
                break;
            }

            case MapName.TANG_LAU: {
                if (!isUpdateEntity) {
                    isUpdateEntity = true;
                    BlackVI boss = new BlackVI(barrack.level);
                    boss.setLocation(this);
                }
                break;
            }
        }
        if (!getPlayers(TYPE_BOSS).isEmpty()) {
            return;
        }
        if (map.isFinish) {
            return;
        }
        map.isFinish = findMonsterLives().isEmpty();
        if (map.isFinish && map.template.id == MapName.TANG_LAU) {
            barrack.endTime = now + 60000;
            barrack.service.setTime();
            barrack.upPoint(this, 50, 2);
            barrack.service.addInfo(Player.INFO_RED, "Chúc mừng bạn đã phá đảo Bản doanh Red");
            List<Player> playerList = getPlayers(TYPE_PLAYER);
            for (Player player : playerList) {
                if (player.taskMain != null && player.taskMain.template.id == 21 && player.taskMain.index == 1) {
                    player.nextTaskParam();
                }
            }
            return;
        }
        updateAutoRefreshMonster();
        if (isUpdateEntity && timeStartBattle > 0 && now - timeStartBattle > barrack.timeBattle * 1000L
                && (map.template.id == MapName.PHONG_GIAM || map.template.id == MapName.PHONG_CHO_1 || map.template.id == MapName.PHONG_CHO_2)) {
            if (findMonsterLives().isEmpty()) {
                return;
            }
            for (int i = MapName.PHONG_GIAM; i <= this.map.template.id; i++) {
                Map map = barrack.findMap(i);
                map.isFinish = false;
                for (Zone zone : map.zones) {
                    ZoneBarrack areaBarrack = (ZoneBarrack) zone;
                    areaBarrack.timeStartBattle = 0;
                    areaBarrack.isUpdateEntity = false;
                    areaBarrack.monsters.get(0).respawn();
                }
            }
            barrack.service.resetPosition();
            barrack.service.addInfo(Player.INFO_RED, "Hết thời gian, vui lòng chiến đấu lại");
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

    public void upLevelBossMonsterAndRefresh(MonsterLevelBoss levelBoss, int count) {
        lockMonster.readLock().lock();
        try {
            List<Monster> monsterList = monsters.stream().filter(m -> m.levelBoss == MonsterLevelBoss.NORMAL && !m.isDead() && m.isMonster()).collect(Collectors.toList());
            while (count > 0 && !monsterList.isEmpty()) {
                Monster monster = monsterList.get(Utils.nextInt(monsterList.size()));
                monster.upLevelBoss(levelBoss, false);
                service.monsterRespawn(monster);
                monsterList.remove(monster);
                count--;
            }
            for (Monster monster : monsters) {
                //monster.numRefresh = -1;
                if (monster.levelBoss == MonsterLevelBoss.NORMAL) {
                    monster.isAutoRefresh = true;
                }
            }
        } finally {
            lockMonster.readLock().unlock();
        }
    }

    public void refreshAllMonster() {
        lockMonster.readLock().lock();
        try {
            for (Monster monster : monsters) {
                if (!monster.isMonster()) {
                    continue;
                }
                if (monster.isDead()) {
                    monster.respawn();
                } else {
                    monster.levelBoss = MonsterLevelBoss.NORMAL;
                    monster.maxHp = monster.getMaxHp();
                    monster.hp = monster.maxHp;
                    service.monsterRespawn(monster);
                }
                monster.isAutoRefresh = true;
            }
        } finally {
            lockMonster.readLock().unlock();
        }
    }

    public void updateAutoRefreshMonster() {
        lockMonster.readLock().lock();
        try {
            for (Monster monster : monsters) {
                if (!monster.isMonster()) {
                    continue;
                }
                if (!monster.isDead() && (monster.levelBoss == MonsterLevelBoss.TINH_ANH || monster.levelBoss == MonsterLevelBoss.THU_LINH)) {
                    return;
                }
            }
            for (Monster monster : monsters) {
                if (!monster.isMonster()) {
                    continue;
                }
                monster.isAutoRefresh = false;
            }
        } finally {
            lockMonster.readLock().unlock();
        }
    }
}
