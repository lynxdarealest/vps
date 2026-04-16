package com.beemobi.rongthanonline.bot.boss;

import com.beemobi.rongthanonline.bot.boss.apk.TeamAndroid13;
import com.beemobi.rongthanonline.bot.boss.apk.TeamAndroid16;
import com.beemobi.rongthanonline.bot.boss.apk.TeamAndroid19;
import com.beemobi.rongthanonline.bot.boss.cell.Cell;
import com.beemobi.rongthanonline.bot.boss.cumber.Cumber;
import com.beemobi.rongthanonline.bot.boss.detufide.Dodo;
import com.beemobi.rongthanonline.bot.boss.detufide.Kui;
import com.beemobi.rongthanonline.bot.boss.detufide.Yarbon;
import com.beemobi.rongthanonline.bot.boss.fide.Fide;
import com.beemobi.rongthanonline.bot.boss.ginyu.TeamGinyu;
import com.beemobi.rongthanonline.bot.boss.lucifer.Lucifer;
import com.beemobi.rongthanonline.bot.boss.lucifer.SuperLucifer;
import com.beemobi.rongthanonline.bot.boss.other.Beerus;
import com.beemobi.rongthanonline.bot.boss.other.CatCute;
import com.beemobi.rongthanonline.bot.boss.other.Yamcha;
import com.beemobi.rongthanonline.bot.boss.cadic.TeamCadic;
import com.beemobi.rongthanonline.bot.boss.pilaf.Mai;
import com.beemobi.rongthanonline.bot.boss.pilaf.Pilaf;
import com.beemobi.rongthanonline.bot.boss.pilaf.Su;
import com.beemobi.rongthanonline.data.BossTemplateData;
import com.beemobi.rongthanonline.map.MapManager;
import com.beemobi.rongthanonline.repository.GameRepository;
import com.beemobi.rongthanonline.server.Server;
import com.beemobi.rongthanonline.util.Utils;
import org.apache.log4j.Logger;

import java.util.HashMap;
import java.util.List;

public class BossManager {
    private static final Logger logger = Logger.getLogger(BossManager.class);
    private static BossManager instance;
    public HashMap<Integer, BossTemplate> bossTemplates;

    public Lucifer[] lucifer;
    public SuperLucifer[] superLucifer;

    public static BossManager getInstance() {
        if (instance == null) {
            instance = new BossManager();
        }
        return instance;
    }

    public void init() {
        bossTemplates = new HashMap<>();
        List<BossTemplateData> bossTemplateDataList = GameRepository.getInstance().bossTemplateData.findAll();
        for (BossTemplateData data : bossTemplateDataList) {
            bossTemplates.put(data.id, new BossTemplate(data));
        }
        if (!Server.getInstance().isInterServer()) {
            lucifer = new Lucifer[10];
            for (int i = 0; i < lucifer.length; i++) {
                lucifer[i] = new Lucifer(i);
            }
            superLucifer = new SuperLucifer[10];
            for (int i = 0; i < superLucifer.length; i++) {
                superLucifer[i] = new SuperLucifer(i);
            }
            bornBoss();
        }
    }

    public void bornBoss() {
        Utils.setTimeout(() -> {
            try {
                while (!Server.getInstance().isRunning) {
                    Thread.sleep(1000);
                }
                new Mai().joinClient();
                Thread.sleep(5000);
                new Su().joinClient();
                Thread.sleep(5000);
                new Pilaf().joinClient();
                Thread.sleep(5000);
                new Yamcha().joinClient();
                Thread.sleep(5000);
                new TeamCadic().born();
                Thread.sleep(5000);
                new Kui().joinClient();
                Thread.sleep(5000);
                new Dodo().joinClient();
                Thread.sleep(5000);
                new Yarbon().joinClient();
                Thread.sleep(5000);
                new TeamGinyu().born();
                Thread.sleep(5000);
                new Fide().joinClient();
                Thread.sleep(5000);
                for (Lucifer value : lucifer) {
                    value.joinClient();
                }
                Thread.sleep(5000);
                new CatCute().joinClient();
                Thread.sleep(5000);
                new TeamAndroid13().born();
                Thread.sleep(5000);
                new TeamAndroid19().born();
                Thread.sleep(5000);
                new TeamAndroid16().born();
                Thread.sleep(5000);
                new Cell(0, -1).joinClient();
                Thread.sleep(5000);
                new Beerus().joinClient();
                Thread.sleep(5000);
                new Cumber(MapManager.getInstance().island).joinClient();
            } catch (Exception ex) {
                logger.error("bornBoss", ex);
            }
        }, 10000);
    }
}
