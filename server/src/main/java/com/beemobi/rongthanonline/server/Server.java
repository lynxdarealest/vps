package com.beemobi.rongthanonline.server;

import com.beemobi.rongthanonline.achievement.AchievementManager;
import com.beemobi.rongthanonline.bot.boss.BossManager;
import com.beemobi.rongthanonline.clan.ClanManager;
import com.beemobi.rongthanonline.data.*;
import com.beemobi.rongthanonline.dragon.Dragon;
import com.beemobi.rongthanonline.dragon.DragonEarth;
import com.beemobi.rongthanonline.dragon.DragonTet2024;
import com.beemobi.rongthanonline.effect.EffectManager;
import com.beemobi.rongthanonline.entity.monster.MonsterManager;
import com.beemobi.rongthanonline.event.*;
import com.beemobi.rongthanonline.gift.GiftCodeManager;
import com.beemobi.rongthanonline.lucky.LuckyManager;
import com.beemobi.rongthanonline.mission.MissionManager;
import com.beemobi.rongthanonline.model.*;
import com.beemobi.rongthanonline.npc.NpcManager;
import com.beemobi.rongthanonline.entity.player.PlayerManager;
import com.beemobi.rongthanonline.item.ItemManager;
import com.beemobi.rongthanonline.map.MapManager;
import com.beemobi.rongthanonline.network.Session;
import com.beemobi.rongthanonline.repository.GameRepository;
import com.beemobi.rongthanonline.service.ServerService;
import com.beemobi.rongthanonline.shop.ShopManager;
import com.beemobi.rongthanonline.skill.SkillManager;
import com.beemobi.rongthanonline.task.TaskManager;
import com.beemobi.rongthanonline.top.TopManager;
import com.beemobi.rongthanonline.upgrade.UpgradeManager;
import com.beemobi.rongthanonline.util.Utils;
import com.google.gson.reflect.TypeToken;
import org.apache.log4j.Logger;
import org.springframework.core.env.Environment;

import java.io.File;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

public class Server {
    public static final int ITEM_TEMPLATE_VERSION = 0;
    public static final int ITEM_OPTION_VERSION = 1;
    public static final int NPC_TEMPLATE_VERSION = 2;
    public static final int EFFECT_TEMPLATE_VERSION = 3;
    public static final int MONSTER_TEMPLATE_VERSION = 4;
    public static final int MEDAL_VERSION = 5;
    public static final int LEVEL_VERSION = 6;
    public static final int FRAME_VERSION = 7;
    public static final int MOUNT_VERSION = 8;
    public static final int BAG_VERSION = 9;
    public static final int SKILL_PAIN_VERSION = 10;
    public static final int AURA_VERSION = 11;
    public static final Object[][] NAME_CONFIG = new Object[][]{
            {ITEM_TEMPLATE_VERSION, "server.item.template.version"},
            {ITEM_OPTION_VERSION, "server.item.option.version"},
            {NPC_TEMPLATE_VERSION, "server.npc.template.version"},
            {EFFECT_TEMPLATE_VERSION, "server.effect.template.version"},
            {MONSTER_TEMPLATE_VERSION, "server.monster.version"},
            {MEDAL_VERSION, "server.medal.version"},
            {LEVEL_VERSION, "server.level.version"},
            {FRAME_VERSION, "server.frame.version"},
            {MOUNT_VERSION, "server.mount.version"},
            {BAG_VERSION, "server.bag.version"},
            {SKILL_PAIN_VERSION, "server.skill.paint.version"},
            {AURA_VERSION, "server.aura.version"}
    };
    public static final int ID = 1;
    public static final int PORT = 1707;
    public static final int COUNT_SESSION_ON_IP = 20;
    public static final String VERSION = "0.9.5";
    public static final int MAX_LEVEL = 100;
    public static final int VERSION_LOGIN = 1;
    private static final Logger logger = Logger.getLogger(Server.class);
    private static Server instance;
    public boolean isRunning;
    public ArrayList<Level> levels;
    public ArrayList<Medal> medals;
    public ArrayList<Aura> auras;
    public ArrayList<Bag> bags;
    public HashMap<Integer, MonsterDartTemplate> monsterDartTemplates;
    public HashMap<Integer, PlayerDartTemplate> playerDartTemplates;
    public HashMap<Integer, SkillEffectInfo> skillEffects;
    public HashMap<Integer, SkillPaint> skillPaints;
    public HashMap<String, Byte> ips = new HashMap<>();
    public HashMap<Integer, Frame> frames;
    public ArrayList<Mount> mounts;
    public Dragon dragon;
    public Dragon dragonTet2024;
    public ServerService service;
    public LocalDateTime endTimeMultiExp;
    public int multiExp;
    public int[] VERSION_DATA;
    public int versionIcon;
    public ConcurrentHashMap<Integer, Icon> icons = new ConcurrentHashMap<>();
    public List<String> notes;
    public List<String> notifications;
    public long maxDamage = 100000000L;
    private ServerSocket socket;
    public boolean isMaintained;
    public int pointUpgrade;
    public int countUpgrade;
    public int pointStar;
    public int countStar;

    public static Server getInstance() {
        if (instance == null) {
            instance = new Server();
        }
        return instance;
    }

    private void init(Environment environment) {
        Event.event = new TeacherDay2024(Timestamp.valueOf(LocalDateTime.of(2024, 11, 20, 0, 0)),
                Timestamp.valueOf(LocalDateTime.of(2024, 12, 17, 23, 59)));
        SkillManager.getInstance().init();
        ItemManager.getInstance().init();
        TaskManager.getInstance().init();
        MonsterManager.getInstance().init();
        NpcManager.getInstance().init();
        MapManager.getInstance().init();
        ShopManager.getInstance().init();
        EffectManager.getInstance().init();
        UpgradeManager.getInstance().init();
        ClanManager.getInstance().init();
        BossManager.getInstance().init();
        AchievementManager.getInstance().init();
        initLevel();
        initMedal();
        initAura();
        initMount();
        initFrame();
        initNotes();
        initBag();
        initMonsterDartTemplate();
        initSkillPaint();
        ServerRandom.init();
        GiftCodeManager.getInstance().init();
        MissionManager.getInstance().init();
        VERSION_DATA = new int[NAME_CONFIG.length];
        for (int i = 0; i < NAME_CONFIG.length; i++) {
            VERSION_DATA[i] = Integer.parseInt(Objects.requireNonNull(environment.getProperty((String) NAME_CONFIG[i][1])));
        }
        versionIcon = Integer.parseInt(Objects.requireNonNull(environment.getProperty("server.image.version")));
        dragon = new DragonEarth();
        dragonTet2024 = new DragonTet2024();
        service = new ServerService(PlayerManager.getInstance());
        endTimeMultiExp = LocalDateTime.of(2024, 11, 22, 0, 0);
        multiExp = 200;
        initNotification();
        if (Event.isEvent()) {
            Event.event.start();
        }
        LuckyManager.getInstance().init();
        TopManager.getInstance().init();
    }

    public void start(Environment environment) {
        try {
            logger.debug("Start Server");
            init(environment);
            System.gc();
            run();
        } catch (Exception ex) {
            logger.error("Start Server Error", ex);
        }
    }

    private void run() {
        try {
            socket = new ServerSocket(PORT + ID - 1);
            isRunning = true;
            Thread save = new Thread(new AutoSaveData());
            save.start();
            Thread login = new Thread(new AutoLogPlayer());
            login.start();
           /* Thread auto = new Thread(new AutoServer());
            auto.start();*/
            MapManager.getInstance().openExpansion();
            logger.debug("Server Start Accept Client");
            while (isRunning) {
                try {
                    Socket client = socket.accept();
                    InetSocketAddress socketAddress = (InetSocketAddress) client.getRemoteSocketAddress();
                    String ip = socketAddress.getAddress().getHostAddress();
                    if (ips.getOrDefault(ip, (byte) 0) < COUNT_SESSION_ON_IP) {
                        Session session = new Session(client, ip);
                        session.start();
                    } else {
                        client.close();
                    }
                } catch (Exception ignored) {
                }
            }
        } catch (Exception ex) {
            logger.error("Run Server Error", ex);
        }
    }

    public void stop() {
        if (isRunning) {
            isRunning = false;
            close();
        }
    }

    private void close() {
        try {
            socket.close();
            socket = null;
            System.gc();
            logger.debug("End Socket");
        } catch (IOException e) {
            logger.error("Server Close Error", e);
        }
        ClanManager.getInstance().close();
        MapManager.getInstance().close();
    }

    public void saveData() {
        if (isMaintained) {
            return;
        }
        if (isInterServer()) {
            return;
        }
        PlayerManager.getInstance().saveData();
        ClanManager.getInstance().saveData();
        ShopManager.getInstance().consignment.saveData();
    }

    public boolean isInterServer() {
        return ID == -1;
    }

    public void initLevel() {
        levels = new ArrayList<>();
        List<LevelData> levelDataList = GameRepository.getInstance().levelData.findAll();
        for (LevelData data : levelDataList) {
            levels.add(new Level(data));
        }
    }

    public void initAura() {
        auras = new ArrayList<>();
        List<AuraData> auraDataList = GameRepository.getInstance().aura.findAll();
        for (AuraData data : auraDataList) {
            auras.add(new Aura(data));
        }
    }

    public void initMedal() {
        medals = new ArrayList<>();
        List<MedalData> medalDataList = GameRepository.getInstance().medalData.findAll();
        for (MedalData data : medalDataList) {
            medals.add(new Medal(data));
        }
    }

    public void initMount() {
        mounts = new ArrayList<>();
        List<MountData> mountDataList = GameRepository.getInstance().mountData.findAll();
        for (MountData data : mountDataList) {
            mounts.add(new Mount(data));
        }
    }

    public void initBag() {
        bags = new ArrayList<>();
        List<BagData> bagDataList = GameRepository.getInstance().bagData.findAll();
        for (BagData data : bagDataList) {
            bags.add(new Bag(data));
        }
    }

    public void initMonsterDartTemplate() {
        try {
            File file = new File("resources/json/MonsterDartTemplate.json");
            if (file.exists()) {
                byte[] bytes = Files.readAllBytes(file.toPath());
                String json = new String(bytes, StandardCharsets.UTF_8);
                monsterDartTemplates = Utils.gson.fromJson(json, new TypeToken<HashMap<Integer, MonsterDartTemplate>>() {
                }.getType());
            }
        } catch (Exception ex) {
            logger.error("initMonsterDartTemplate", ex);
        }
    }

    public void initFrame() {
        try {
            File file = new File("resources/json/Frame.json");
            if (file.exists()) {
                byte[] bytes = Files.readAllBytes(file.toPath());
                String json = new String(bytes, StandardCharsets.UTF_8);
                frames = Utils.gson.fromJson(json, new TypeToken<HashMap<Integer, Frame>>() {
                }.getType());
            }
        } catch (Exception ex) {
            logger.error("initFrame", ex);
        }
    }

    public void initSkillPaint() {
        try {
            File file = new File("resources/json/Dart.json");
            if (file.exists()) {
                byte[] bytes = Files.readAllBytes(file.toPath());
                String json = new String(bytes, StandardCharsets.UTF_8);
                playerDartTemplates = Utils.gson.fromJson(json, new TypeToken<HashMap<Integer, PlayerDartTemplate>>() {
                }.getType());
            }

            file = new File("resources/json/SkillEffect.json");
            if (file.exists()) {
                byte[] bytes = Files.readAllBytes(file.toPath());
                String json = new String(bytes, StandardCharsets.UTF_8);
                skillEffects = Utils.gson.fromJson(json, new TypeToken<HashMap<Integer, SkillEffectInfo>>() {
                }.getType());
            }

            file = new File("resources/json/SkillPaint.json");
            if (file.exists()) {
                byte[] bytes = Files.readAllBytes(file.toPath());
                String json = new String(bytes, StandardCharsets.UTF_8);
                skillPaints = Utils.gson.fromJson(json, new TypeToken<HashMap<Integer, SkillPaint>>() {
                }.getType());
            }
        } catch (Exception ex) {
            logger.error("initSkillPaint", ex);
        }
    }

    public boolean isMultiExp() {
        return LocalDateTime.now().isBefore(endTimeMultiExp);
    }

    public int getMultiExp() {
        return isMultiExp() ? multiExp : 0;
    }

    public boolean isHourSupportTask() {
        int hour = LocalDateTime.now().getHour();
        return hour == 19 || hour == 7 || hour == 8 || hour == 9;
    }

    public void initNotes() {
        notes = new ArrayList<>();
        notes.add("Danh sách các hoạt động trong Rồng Thần Online");
        notes.add("- Bản doanh Red: tham gia tự do ở NPC Đội trưởng vàng tại Rừng Giran");
        notes.add("- Vùng đất bí ẩn: tham gia tự do ở NPC Chi Chi tại Lãnh địa Bang hội (yêu cầu có Bang hội)");
        notes.add("- Thành phố lãng quên: mở vào 22h hàng ngày ở NPC Pu tại Trạm tàu vũ trụ");
        notes.add("- Chiến trường Ngọc rồng Namek: mở vào 21h hàng ngày ở NPC Kardo tại Trạm tàu Namek");
        notes.add("- Chiến trường Sinh tồn: mở vào 12h và 20h hàng ngày ở NPC Pu tại Trạm tàu vũ trụ");
        notes.add("- Động kho báu: mở vào 21h30 hàng ngày ở NPC Quy Lão tại Đảo Kame");
        notes.add("- Đảo băng hỏa: chưa có thông tin");
        notes.add("- Đại hội võ thuật: chưa có thông tin");
        notes.add("- Đại hội võ thuật dành cho đệ tử: chưa có thông tin");
        notes.add("- Võ đài Siêu hạng: chưa có thông tin");
        notes.add("- Võ đài Siêu hạng liên máy chủ: chưa có thông tin");
        notes.add("- Đại hội võ thuật liên máy chủ: chưa có thông tin");
        notes.add("- Chiến trường Sinh tồn liên máy chủ: chưa có thông tin");
    }

    public void initNotification() {
        notifications = new ArrayList<>();
        notifications.add("Rồng Thần Online chúc chiến binh chơi game vui vẻ.");
        notifications.add("- Khuyến mãi 50% giá trị nạp thẻ tại rongthanonline.vn đến 0h 23/12/2024");
        int multiExp = getMultiExp();
        if (multiExp > 0) {
            notifications.add(String.format("Sự kiện tăng %d%% exp khi đánh quái sẽ kết thúc vào %s.", multiExp, Utils.formatTime(Timestamp.valueOf(endTimeMultiExp))));
        }
        if (Event.isEvent()) {
            notifications.add(String.format("Sự kiện %s sẽ kết thúc vào %s.", Event.event.getName(), Utils.formatTime(Event.event.getEndTime())));
        }
        notifications.add("Hỗ trợ nhiệm vụ: Từ 7h đến 9h và 19h đến 20h hàng ngày chỉ những chiến binh đang làm nhiệm vụ hạ boss mới có thể vào khu vực có boss thuộc nhiệm vụ đó.");
    }
}
