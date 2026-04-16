package com.beemobi.rongthanonline;

import com.beemobi.rongthanonline.repository.*;
import com.beemobi.rongthanonline.server.Server;
import com.beemobi.rongthanonline.service.MySQLConnect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.env.Environment;

@SpringBootApplication
public class Main implements CommandLineRunner {
    public static void main(String[] args) {
        System.setProperty("java.awt.headless", "false");
        SpringApplication.run(Main.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        boolean enableSwingGui = Boolean.parseBoolean(environment.getProperty("server.swing.gui.enabled", "false"));
        if (enableSwingGui) {
            try {
                javax.swing.SwingUtilities.invokeLater(() -> {
                    new com.beemobi.rongthanonline.server.ServerGUI().setVisible(true);
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        init();
        Server.getInstance().start(environment);
    }

    public void init() {
        MySQLConnect.URL_DATABASE = environment.getProperty("spring.datasource.url");
        MySQLConnect.USERNAME_DATABASE = environment.getProperty("spring.datasource.username");
        MySQLConnect.PASSWORD_DATABASE = environment.getProperty("spring.datasource.password");

        GameRepository.getInstance().npcTemplateData = npcTemplateDataRepository;
        GameRepository.getInstance().monsterTemplateData = monsterTemplateDataRepository;
        GameRepository.getInstance().wayPointData = wayPointDataRepository;
        GameRepository.getInstance().mapNpcData = mapNpcDataRepository;
        GameRepository.getInstance().mapMonsterData = mapMonsterDataRepository;
        GameRepository.getInstance().mapTemplateData = mapTemplateDataRepository;
        GameRepository.getInstance().skillOptionTemplateData = skillOptionTemplateDataRepository;
        GameRepository.getInstance().skillTemplateData = skillTemplateDataRepository;
        GameRepository.getInstance().skillOptionData = skillOptionDataRepository;
        GameRepository.getInstance().taskSubTemplateData = taskSubTemplateDataRepository;
        GameRepository.getInstance().taskTemplateData = taskTemplateDataRepository;
        GameRepository.getInstance().itemOptionData = itemOptionDataRepository;
        GameRepository.getInstance().itemTemplateData = itemTemplateDataRepository;
        GameRepository.getInstance().itemOptionTemplateData = itemOptionTemplateDataRepository;
        GameRepository.getInstance().playerData = playerDataRepository;
        GameRepository.getInstance().levelData = levelDataRepository;
        GameRepository.getInstance().effectTemplateData = effectTemplateDataRepository;
        GameRepository.getInstance().userData = userDataRepository;
        GameRepository.getInstance().clanData = clanDataRepository;
        GameRepository.getInstance().clanMemberData = clanMemberDataRepository;
        GameRepository.getInstance().bossTemplateData = bossTemplateDataRepository;
        GameRepository.getInstance().orderData = orderDataRepository;
        GameRepository.getInstance().discipleData = discipleDataRepository;
        GameRepository.getInstance().giftData = giftDataRepository;
        GameRepository.getInstance().giftCodeData = giftCodeDataRepository;
        GameRepository.getInstance().giftCodeHistoryData = giftCodeHistoryDataRepository;
        GameRepository.getInstance().achievementTemplateData = achievementTemplateDataRepository;
        GameRepository.getInstance().playerNameData = playerNameDataRepository;
        GameRepository.getInstance().effectImageData = effectImageDataRepository;
        GameRepository.getInstance().medalData = medalDataRepository;
        GameRepository.getInstance().missionWeekTemplateData = missionWeekTemplateDataRepository;
        GameRepository.getInstance().missionDailyTemplateData = missionDailyTemplateDataRepository;
        GameRepository.getInstance().missionRechargeTemplateData = missionRechargeTemplateDataRepository;
        GameRepository.getInstance().itemConsignmentData = itemConsignmentDataRepository;
        GameRepository.getInstance().mountData = mountDataRepository;
        GameRepository.getInstance().rewardData = rewardDataRepository;
        GameRepository.getInstance().bagData = bagDataRepository;
        GameRepository.getInstance().intrinsic = intrinsicRepository;
        GameRepository.getInstance().historyLog = historyLogDataRepository;
        GameRepository.getInstance().historyTrade = historyTradeDataRepository;
        GameRepository.getInstance().historyBuyItem = historyBuyItemDataRepository;
        GameRepository.getInstance().historyGiveDiamond = historyGiveDiamondDataRepository;
        GameRepository.getInstance().aura = auraDataRepository;
        GameRepository.getInstance().missionEventTemplate = missionEventTemplateDataRepository;
        GameRepository.getInstance().missionItemWeeklyTemplate = missionItemWeeklyTemplateDataRepository;
        GameRepository.getInstance().pointWeekly = pointWeeklyDataRepository;

    }

    @Autowired
    Environment environment;

    @Autowired
    NpcTemplateDataRepository npcTemplateDataRepository;

    @Autowired
    MonsterTemplateDataRepository monsterTemplateDataRepository;

    @Autowired
    WayPointDataRepository wayPointDataRepository;

    @Autowired
    MapNpcDataRepository mapNpcDataRepository;

    @Autowired
    MapMonsterDataRepository mapMonsterDataRepository;

    @Autowired
    MapTemplateDataRepository mapTemplateDataRepository;

    @Autowired
    SkillOptionTemplateDataRepository skillOptionTemplateDataRepository;

    @Autowired
    SkillTemplateDataRepository skillTemplateDataRepository;

    @Autowired
    SkillOptionDataRepository skillOptionDataRepository;

    @Autowired
    TaskSubTemplateDataRepository taskSubTemplateDataRepository;

    @Autowired
    TaskTemplateDataRepository taskTemplateDataRepository;

    @Autowired
    ItemOptionTemplateDataRepository itemOptionTemplateDataRepository;

    @Autowired
    ItemOptionDataRepository itemOptionDataRepository;

    @Autowired
    ItemTemplateDataRepository itemTemplateDataRepository;

    @Autowired
    PlayerDataRepository playerDataRepository;

    @Autowired
    LevelDataRepository levelDataRepository;

    @Autowired
    EffectTemplateDataRepository effectTemplateDataRepository;

    @Autowired
    UserDataRepository userDataRepository;

    @Autowired
    ClanDataRepository clanDataRepository;

    @Autowired
    ClanMemberDataRepository clanMemberDataRepository;

    @Autowired
    BossTemplateDataRepository bossTemplateDataRepository;

    @Autowired
    OrderDataRepository orderDataRepository;

    @Autowired
    DiscipleDataRepository discipleDataRepository;

    @Autowired
    GiftDataRepository giftDataRepository;

    @Autowired
    GiftCodeDataRepository giftCodeDataRepository;

    @Autowired
    GiftCodeHistoryDataRepository giftCodeHistoryDataRepository;

    @Autowired
    AchievementTemplateDataRepository achievementTemplateDataRepository;

    @Autowired
    PlayerNameDataRepository playerNameDataRepository;

    @Autowired
    EffectImageDataRepository effectImageDataRepository;

    @Autowired
    MedalDataRepository medalDataRepository;

    @Autowired
    MissionWeekTemplateDataRepository missionWeekTemplateDataRepository;

    @Autowired
    MissionDailyTemplateDataRepository missionDailyTemplateDataRepository;

    @Autowired
    MissionRechargeTemplateDataRepository missionRechargeTemplateDataRepository;

    @Autowired
    ItemConsignmentDataRepository itemConsignmentDataRepository;

    @Autowired
    MountDataRepository mountDataRepository;

    @Autowired
    RewardDataRepository rewardDataRepository;

    @Autowired
    BagDataRepository bagDataRepository;

    @Autowired
    IntrinsicRepository intrinsicRepository;

    @Autowired
    HistoryLogDataRepository historyLogDataRepository;

    @Autowired
    HistoryTradeDataRepository historyTradeDataRepository;

    @Autowired
    HistoryBuyItemDataRepository historyBuyItemDataRepository;

    @Autowired
    HistoryGiveDiamondDataRepository historyGiveDiamondDataRepository;

    @Autowired
    AuraDataRepository auraDataRepository;

    @Autowired
    MissionEventTemplateDataRepository missionEventTemplateDataRepository;

    @Autowired
    MissionItemWeeklyTemplateDataRepository missionItemWeeklyTemplateDataRepository;

    @Autowired
    PointWeeklyDataRepository pointWeeklyDataRepository;

}
