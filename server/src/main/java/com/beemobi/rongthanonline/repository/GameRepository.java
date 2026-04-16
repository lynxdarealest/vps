package com.beemobi.rongthanonline.repository;

public class GameRepository {
    private static GameRepository instance;
    public NpcTemplateDataRepository npcTemplateData;
    public MonsterTemplateDataRepository monsterTemplateData;
    public WayPointDataRepository wayPointData;
    public MapNpcDataRepository mapNpcData;
    public MapMonsterDataRepository mapMonsterData;
    public MapTemplateDataRepository mapTemplateData;
    public SkillOptionTemplateDataRepository skillOptionTemplateData;
    public SkillOptionDataRepository skillOptionData;
    public SkillTemplateDataRepository skillTemplateData;
    public TaskTemplateDataRepository taskTemplateData;
    public TaskSubTemplateDataRepository taskSubTemplateData;
    public ItemOptionDataRepository itemOptionData;
    public ItemOptionTemplateDataRepository itemOptionTemplateData;
    public ItemTemplateDataRepository itemTemplateData;
    public PlayerDataRepository playerData;
    public LevelDataRepository levelData;
    public EffectTemplateDataRepository effectTemplateData;
    public UserDataRepository userData;
    public ClanDataRepository clanData;
    public ClanMemberDataRepository clanMemberData;
    public BossTemplateDataRepository bossTemplateData;
    public OrderDataRepository orderData;
    public DiscipleDataRepository discipleData;
    public GiftDataRepository giftData;
    public GiftCodeDataRepository giftCodeData;
    public GiftCodeHistoryDataRepository giftCodeHistoryData;
    public AchievementTemplateDataRepository achievementTemplateData;
    public PlayerNameDataRepository playerNameData;
    public EffectImageDataRepository effectImageData;
    public MedalDataRepository medalData;
    public MissionWeekTemplateDataRepository missionWeekTemplateData;
    public MissionDailyTemplateDataRepository missionDailyTemplateData;
    public MissionRechargeTemplateDataRepository missionRechargeTemplateData;
    public ItemConsignmentDataRepository itemConsignmentData;
    public MountDataRepository mountData;
    public RewardDataRepository rewardData;
    public BagDataRepository bagData;
    public IntrinsicRepository intrinsic;
    public HistoryLogDataRepository historyLog;
    public HistoryTradeDataRepository historyTrade;
    public HistoryBuyItemDataRepository historyBuyItem;
    public HistoryGiveDiamondDataRepository historyGiveDiamond;
    public AuraDataRepository aura;
    public MissionEventTemplateDataRepository missionEventTemplate;
    public MissionItemWeeklyTemplateDataRepository missionItemWeeklyTemplate;
    public PointWeeklyDataRepository pointWeekly;

    public GameRepository() {

    }

    public static GameRepository getInstance() {
        if (instance == null) {
            instance = new GameRepository();
        }
        return instance;
    }

}
