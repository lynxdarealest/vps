package com.beemobi.rongthanonline.network;

import com.beemobi.rongthanonline.common.Language;
import com.beemobi.rongthanonline.data.PlayerData;
import com.beemobi.rongthanonline.data.PlayerNameData;
import com.beemobi.rongthanonline.entity.player.Player;
import com.beemobi.rongthanonline.entity.player.PlayerManager;
import com.beemobi.rongthanonline.entity.player.json.EventInfo;
import com.beemobi.rongthanonline.entity.player.json.ListItemInfo;
import com.beemobi.rongthanonline.entity.player.json.LockInfo;
import com.beemobi.rongthanonline.history.HistoryManager;
import com.beemobi.rongthanonline.item.Item;
import com.beemobi.rongthanonline.item.ItemManager;
import com.beemobi.rongthanonline.item.ItemType;
import com.beemobi.rongthanonline.model.Icon;
import com.beemobi.rongthanonline.server.ServerMaintenance;
import com.beemobi.rongthanonline.user.User;
import com.beemobi.rongthanonline.user.UserStatus;
import com.beemobi.rongthanonline.repository.GameRepository;
import com.beemobi.rongthanonline.server.Server;
import com.beemobi.rongthanonline.service.Service;
import com.beemobi.rongthanonline.util.Utils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.apache.log4j.Logger;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.Socket;
import java.nio.file.Files;
import java.sql.Timestamp;
import java.util.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.regex.Pattern;

public class Session {
    public static final Lock lockCreate = new ReentrantLock();
    public static final HashMap<String, Long> userLogoutTimes = new HashMap<>();
    private static final Logger logger = Logger.getLogger(Session.class);
    private static final Lock lock = new ReentrantLock();
    private static int autoIncrease;
    private final byte[] keys;
    public Lock lockRegister = new ReentrantLock();
    public int id;
    public MessageHandler messageHandler;
    public boolean isConnected;
    public Socket socket;
    public DataInputStream dataInputStream;
    public DataOutputStream dataOutputStream;
    public Player player;
    public Service service;
    public User user;
    public String ip;
    public boolean isLoginCompleted;
    public int waitId;
    public HashMap<Integer, Long> requestIconTimes = new HashMap<>();
    private byte curR, curW;
    private MessageCollector collector;
    private Sender sender;
    private boolean isSendKeyComplete;
    public String version;

    public Session(Socket sc, String ip) throws IOException {
        keys = UUID.randomUUID().toString().getBytes();
        socket = sc;
        socket.setTcpNoDelay(true);
        id = autoIncrease++;
        this.ip = ip;
        this.dataInputStream = new DataInputStream(sc.getInputStream());
        this.dataOutputStream = new DataOutputStream(sc.getOutputStream());
        setHandler(new MessageHandler(this));
        service = new Service(this);
        sender = new Sender();
        collector = new MessageCollector();
        SessionManager.getInstance().addSession(this);
        // chuyển sang thread virtual -> dũng note 3/4/2021
    }

    public void start() {
        isConnected = true;
        sender.start();
        collector.start();
        messageHandler.onConnectOK();
        Server.getInstance().ips.put(ip, (byte) (Server.getInstance().ips.getOrDefault(ip, (byte) 0) + 1));
    }

    public boolean isConnected() {
        return isConnected;
    }

    public void setHandler(MessageHandler messageHandler) {
        this.messageHandler = messageHandler;
    }

    public void sendMessage(Message message) {
        if (!isConnected) {
            return;
        }
        if (player != null && !player.isPlayer()) {
            return;
        }
        sender.addMessage(message);
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void updateAction(Message message) {
        try {
            int type = message.reader().readByte();
            switch (type) {
                case -1:
                    service.sendVersionData();
                    break;

                case 0:
                    service.sendItemTemplates();
                    break;

                case 1:
                    service.sendItemOptionTemplates();
                    break;

                case 2:
                    service.sendNpcTemplates();
                    break;

                case 3:
                    service.sendEffectTemplates();
                    break;

                case 4:
                    service.sendMonsterTemplates();
                    break;

                case 5:
                    service.sendMedals();
                    break;

                case 6:
                    service.sendLevel();
                    break;

                case 7:
                    service.sendFrames();
                    break;

                case 8:
                    service.sendMounts();
                    break;

                case 9:
                    service.sendBags();
                    break;

                case 10:
                    service.sendSkillPaint();
                    break;

                case 11:
                    service.sendAuras();
                    break;
            }
        } catch (Exception ex) {
            logger.error("updateAction", ex);
        }
    }

    public void login(Message message) {
        try {
            if (isLoginCompleted) {
                return;
            }
            if (ServerMaintenance.getInstance().isRunning && ServerMaintenance.getInstance().seconds <= 180) {
                service.startDialogOk("Không thể thực hiện khi máy chủ sắp bảo trì");
                return;
            }
            long now = System.currentTimeMillis();
            this.version = message.reader().readUTF().toLowerCase();
            String username = message.reader().readUTF().toLowerCase();
            String password = message.reader().readUTF();
            /*if (!version.equals(Server.VERSION)) {
                return;
            }*/
            if (message.reader().available() > 0) {
                if (Server.VERSION_LOGIN > message.reader().readByte()) {
                    service.startDialogOk("Vui lòng cập nhật phiên bản mới tại diễn đàn, CHplay, Testflight");
                    return;
                }
            }
            if (!Pattern.compile("^[a-z0-9]+$").matcher(username).find()) {
                service.startDialogOk("Tài khoản chỉ bao gồm chữ và số");
                return;
            }
            if (!Pattern.compile("^[a-z0-9]+$").matcher(password).find()) {
                service.startDialogOk("Mật khẩu chỉ bao gồm chữ và số");
                return;
            }
            User user = new User(username, password, this);
            if (!user.isAdmin()) {
                long lastTimeLogin = SessionManager.getInstance().getTimeUserLogin(username);
                long delayLogin = 5000L;
                long time = lastTimeLogin + delayLogin - now;
                if (time > 0) {
                    service.startDialogOk(String.format("Vui lòng thử lại sau %s", Utils.formatTime(time)));
                    return;
                }
            }
            UserStatus status = user.login();
            switch (status) {
                case PASSWORD_INVALID:
                    service.startDialogOk("Tài khoản hoặc mật khẩu không chính xác");
                    return;
                case USERNAME_AND_PASSWORD_INVALID:
                    service.startDialogOk("Tài khoản hoặc mật khẩu không được chứa ký tự đặc biệt");
                    return;
                case PERMANENT_LOCK:
                    service.startDialogOk("Tài khoản của bạn đã bị khóa. Vui lòng liên hệ Admin để biết thêm chi tiết");
                    return;
                case SERVER_MAINTAIN:
                    service.startDialogOk(String.format("Không thể kết nối đến máy chủ đăng nhập (%d)", Utils.nextInt(1, 10)));
                    return;
                case TIME_LOCK:
                    service.startDialogOk(String.format("Tài khoản của bạn bị khóa đến %s", Utils.formatTime(user.lockTime)));
                    return;
                case SERVER_INVALID:
                    service.startDialogOk(String.format("Sai máy chủ đăng nhập, vui lòng đăng nhập sang máy chủ %d sao", user.server));
                    return;
                default:
                    break;
            }
            List<Player> playerList = PlayerManager.getInstance().findPlayerByUserId(user.id);
            if (!playerList.isEmpty()) {
                for (Player p : playerList) {
                    p.service.startDialogOk("Có người đã đăng nhập vào tài khoản của bạn");
                    p.session.disconnect();
                }
                service.startDialogOk("Bạn đang đăng nhập trên thiết bị khác");
                return;
            }
            List<PlayerData> dataList = GameRepository.getInstance().playerData.findByUserIdAndServer(user.id, user.server);
            if (dataList.isEmpty()) {
                if (Server.getInstance().isInterServer()) {
                    service.startDialogOk("Bạn cần phải tạo nhân vật ở máy chủ chính trước");
                    return;
                }
                setUser(user);
                service.startCreatePlayerScreen();
                return;
            }
            PlayerData data = dataList.get(0);
            LockInfo lockInfo = Utils.gson.fromJson(data.lockInfo, new TypeToken<LockInfo>() {
            }.getType());
            if (lockInfo.loginTime > 0 && now < lockInfo.loginTime) {
                service.startDialogOk(String.format("Tài khoản của bạn đến %s", Utils.formatTime(new Timestamp(lockInfo.loginTime))));
                return;
            }
            if (data.isLock) {
                service.startDialogOk("Tài khoản của bạn đã bị khóa. Vui lòng liên hệ Admin để biết thêm chi tiết");
                return;
            }
            if (!user.isAdmin() && data.logoutTime != null) {
                long timeDis = 20000 + data.logoutTime.getTime() - now;
                if (timeDis > 0) {
                    service.startDialogOk(String.format("Vui lòng thử lại sau %s", Utils.formatTime(timeDis)));
                    return;
                }
            }
            waitId = data.id;
            Player me = new Player(data);
            me.lockInfo = lockInfo;
            enter(me, user);
        } catch (Exception ex) {
            logger.error("login", ex);
        }
    }

    public void register(Message message) {
        lockRegister.lock();
        try {
            if (Server.getInstance().isInterServer()) {
                service.startDialogOk(Language.CANCEL_ACTION_WHEN_SERVER_IS_INTER_SERVER);
                return;
            }
            if (isLoginCompleted) {
                return;
            }
            if (ServerMaintenance.getInstance().isRunning) {
                service.startDialogOk("Không thể thực hiện khi máy chủ sắp bảo trì");
                return;
            }
            String email = message.reader().readUTF();
            String username = message.reader().readUTF();
            String password = message.reader().readUTF();
            if (email.isEmpty() || !Pattern.compile("^[a-z0-9@.]+$").matcher(email).find()) {
                service.startDialogOk("Email không hợp lệ");
                return;
            }
            if (username.length() < 5 || username.length() > 25 || !Pattern.compile("^[a-z0-9]+$").matcher(username).find()) {
                service.startDialogOk("Tài khoản chỉ từ 5 đến 25 kí tự bao gồm chữ và số");
                return;
            }
            if (password.length() < 5 || password.length() > 25 || !Pattern.compile("^[a-z0-9]+$").matcher(password).find()) {
                service.startDialogOk("Mật khẩu chỉ từ 5 đến 25 kí tự bao gồm chữ và số");
                return;
            }
            User user = new User(email, username, password, this);
            String response = user.register();
            if (response.startsWith("true")) {
                service.startDialogOk(String.format("Đăng ký thành công tài khoản %s\n(mật khẩu: %s, email: %s, máy chủ: %d sao)", username, password, email, user.server));
                return;
            }
            if (response.startsWith("false")) {
                service.startDialogOk(response.replace("false|", ""));
                return;
            }
            service.startDialogOk("Vui lòng thử lại sau hoặc đăng ký tại website https://rto.lynxphg.me/");
        } catch (Exception ex) {
            logger.error("register", ex);
        } finally {
            lockRegister.unlock();
        }
    }

    public void enter(Player player, User user) {
        lock.lock();
        try {
            if (!isLoginCompleted) {
                if (!PlayerManager.getInstance().addPlayer(player)) {
                    return;
                }
                isLoginCompleted = true;
                service.setPlayer(player);
                messageHandler.setPlayer(player);
                player.setService(service);
                player.setSession(this);
                setUser(user);
                setPlayer(player);
                player.join();
                HistoryManager.getInstance().saveLogin(player);
            }
        } finally {
            lock.unlock();
        }
    }

    public void createPlayer(Message message) {
        lockCreate.lock();
        try {
            if (Server.getInstance().isInterServer()) {
                service.startDialogOk(Language.CANCEL_ACTION_WHEN_SERVER_IS_INTER_SERVER);
                return;
            }
            if (isLoginCompleted) {
                return;
            }
            if (ServerMaintenance.getInstance().isRunning) {
                service.startDialogOk("Không thể thực hiện khi máy chủ sắp bảo trì");
                return;
            }
            String name = message.reader().readUTF().toLowerCase();
            int gender = message.reader().readByte();
            if (gender < 0 || gender > 2) {
                return;
            }
            if (name.length() < 5 || name.length() > 10 || !Pattern.compile("^[a-z0-9]+$").matcher(name).find()) {
                service.startDialogOk("Tên nhân vật chỉ từ 5 đến 10 kí tự bao gồm chữ và số");
                return;
            }
            if (name.contains("admin")) {
                service.startDialogOk("Tên nhân vật không được chứa \"admin\"");
                return;
            }
            if (!GameRepository.getInstance().playerNameData.findByName(name).isEmpty()) {
                service.startDialogOk("Tên nhân vật đã tồn tại hoặc đã từng được sử dụng");
                return;
            }
            if (!GameRepository.getInstance().playerData.findByUserIdOrName(user.id, name).isEmpty()) {
                service.startDialogOk("Tên nhân vật đã tồn tại");
                return;
            }
            Gson gson = new Gson();
            PlayerData data = new PlayerData();
            data.userId = user.id;
            data.name = name;
            data.nameBase = name;
            data.gender = gender;
            data.server = Server.ID;
            data.baseInfo = "[10,5,5,5]";
            data.powerInfo = "{\"power\":1,\"potential\":1,\"level\":1,\"limit_level\":40,\"point_skill\":1,\"power_time\":0}";
            data.ruby = 25;
            data.pro = 0L;
            data.diamond = 0;
            data.xuKhoa = 10000L;
            data.xu = 0L;
            data.clanInfo = "[-1,1,0]";
            data.itemsBag = Utils.getJsonArrayItem(new Item[Player.DEFAULT_BAG]);
            Item[] itemsBody = new Item[Player.DEFAULT_BODY];
            if (gender == 0) {
                itemsBody[ItemType.TYPE_QUAN] = ItemManager.getInstance().createItem(2, 1, true);
                itemsBody[ItemType.TYPE_AO] = ItemManager.getInstance().createItem(3, 1, true);
            } else if (gender == 1) {
                itemsBody[ItemType.TYPE_QUAN] = ItemManager.getInstance().createItem(6, 1, true);
                itemsBody[ItemType.TYPE_AO] = ItemManager.getInstance().createItem(7, 1, true);
            } else {
                itemsBody[ItemType.TYPE_QUAN] = ItemManager.getInstance().createItem(10, 1, true);
                itemsBody[ItemType.TYPE_AO] = ItemManager.getInstance().createItem(11, 1, true);
            }
            data.itemsBody = Utils.getJsonArrayItem(itemsBody);
            data.itemsBox = Utils.getJsonArrayItem(new Item[Player.DEFAULT_BOX]);
            data.itemsPet = Utils.getJsonArrayItem(new Item[Player.DEFAULT_BODY]);
            data.itemsOther = Utils.getJsonArrayItem(new Item[Player.DEFAULT_BODY]);
            data.taskInfo = "[0,0,0]";
            data.skillsInfo = "[{\"id\":" + gender + ",\"level\":1,\"upgrade\":0,\"point\":0,\"time_can_use\":0}]";
            data.keysSkill = "[" + gender + ",-1,-1,-1,-1,-1,-1,-1,-1,-1]";
            data.mapInfo = "[0,1250,648,100,100]";
            data.barrackInfo = "{\"count\":1,\"num_buy\":0,\"point\":0}";
            data.friend = "[]";
            data.enemy = "[]";
            data.effect = "[]";
            data.magicBean = "{\"level\":1,\"update_time\":0,\"is_update\":false}";
            data.taskDaily = "{\"count\":30,\"completed\":0,\"create_time\":0}";
            data.point = "{\"active\":0,\"pk\":0,\"event\":0,\"survival\":0}";
            data.spaceship = 0;
            data.recharge = "{\"diamond\":0,\"index\":0}";
            data.isOnline = false;
            data.onlineInfo = "{\"total\":0,\"day\":0}";
            data.missionWeek = "[]";
            data.missionDaily = "[]";
            data.missionRecharge = "[]";
            data.missionEvent = "[]";
            data.achievement = "[]";
            data.intrinsic = "[]";
            data.npcTree = "[]";
            data.pin = "";
            data.isLock = false;
            data.event = gson.toJson(new EventInfo());
            data.lockInfo = "{\"login_time\":0,\"login_info\":\"\",\"chat_time\":0,\"chat_info\":\"\",\"trade_time\":0,\"trade_info\":\"\"}";
            data.trainingOffline = "{\"master\":0,\"disciple\":0}";
            data.createTime = new Timestamp(System.currentTimeMillis());
            GameRepository.getInstance().playerData.save(data);
            PlayerNameData nameData = new PlayerNameData();
            nameData.playerId = data.id;
            nameData.name = data.name;
            nameData.nameBase = data.name;
            nameData.createTime = data.createTime;
            GameRepository.getInstance().playerNameData.save(nameData);
            enter(new Player(data), this.user);
        } catch (Exception ex) {
            logger.error("createPlayer", ex);
        } finally {
            lockCreate.unlock();
        }
    }

    public void requestIcon(Message message) {
        try {
            int iconId = message.reader().readShort();
            long now = System.currentTimeMillis();
            if (now - requestIconTimes.getOrDefault(iconId, 0L) < 2000) {
                return;
            }
            requestIconTimes.put(iconId, now);
            if (Server.getInstance().icons.containsKey(iconId)) {
                service.sendIcon(iconId, Server.getInstance().icons.get(iconId).data);
            } else {
                File file = new File(String.format("resources/icon/%d.png", iconId));
                if (file.exists()) {
                    byte[] bytes = Files.readAllBytes(file.toPath());
                    service.sendIcon(iconId, bytes);
                    Server.getInstance().icons.put(iconId, new Icon(iconId, bytes));
                } else {
                    File fallback = new File("resources/icon/0.png");
                    if (fallback.exists()) {
                        byte[] bytes = Files.readAllBytes(fallback.toPath());
                        service.sendIcon(iconId, bytes);
                    } else {
                        logger.warn("Missing icon file: resources/icon/" + iconId + ".png");
                    }
                }
            }
        } catch (Exception ex) {
            logger.error("requestIcon", ex);
        }
    }

    public void doSendMessage(Message msg) throws IOException {
        byte[] data = msg.getData();
        if (isSendKeyComplete) {
            byte b = writeKey(msg.id);
            dataOutputStream.writeByte(b);
        } else {
            dataOutputStream.writeByte(msg.id);
        }
        if (data != null) {
            int size = data.length;
            if (msg.id == -127 || msg.id == MessageName.REQUEST_ICON || msg.id == MessageName.UPDATE_DATA) {
                byte b = writeKey((byte) (size));
                dataOutputStream.writeByte(b - 128);
                byte b2 = writeKey((byte) (size >> 8));
                dataOutputStream.writeByte(b2 - 128);
                byte b3 = writeKey((byte) (size >> 16));
                dataOutputStream.writeByte(b3 - 128);
            } else if (isSendKeyComplete) {
                int byte1 = writeKey((byte) (size >> 8));
                dataOutputStream.writeByte(byte1);
                int byte2 = writeKey((byte) (size & 255));
                dataOutputStream.writeByte(byte2);
            } else {
                dataOutputStream.writeShort(size);
            }
            if (isSendKeyComplete) {
                for (int i = 0; i < data.length; i++) {
                    data[i] = writeKey(data[i]);
                }
            }
            dataOutputStream.write(data);
        } else {
            dataOutputStream.writeShort(0);
        }
        dataOutputStream.flush();
        msg.cleanup();
    }

    private byte writeKey(byte b) {
        byte i = (byte) ((keys[curW++] & 255) ^ (b & 255));
        if (curW >= keys.length) {
            curW %= (byte) keys.length;
        }
        return i;
    }

    private byte readKey(byte b) {
        byte i = (byte) ((keys[curR++] & 255) ^ (b & 255));
        if (curR >= keys.length) {
            curR %= (byte) keys.length;
        }
        return i;
    }

    public void sendKey() {
        try {
            Message msg = new Message(MessageName.SEND_SESSION_KEY);
            msg.writer().writeByte(keys.length);
            msg.writer().writeByte(keys[0]);
            for (int i = 1; i < keys.length; i++) {
                msg.writer().writeByte(keys[i] ^ keys[i - 1]);
            }
            this.doSendMessage(msg);
            msg.cleanup();
            isSendKeyComplete = true;
        } catch (Exception ex) {
            logger.error("sendKey", ex);
        }
    }

    public void close() {
        try {
            try {
                if (isConnected()) {
                    messageHandler.onDisconnected();
                }
                if (player != null) {
                    player.logout();
                }
                cleanNetwork();
            } finally {
                SessionManager.getInstance().removeSession(this);
                Server.getInstance().ips.put(ip, (byte) (Server.getInstance().ips.getOrDefault(ip, (byte) 0) - 1));
                if (Server.getInstance().ips.get(ip) < 0) {
                    Server.getInstance().ips.remove(ip);
                }
                if (user != null) {
                    SessionManager.getInstance().addUserLogout(user.username);
                }
                this.ip = null;
            }
        } finally {
            System.gc();
        }
    }

    public void disconnect() {
        if (socket != null && socket.isConnected()) {
            try {
                socket.close();
            } catch (IOException ex) {
                logger.error("failed!", ex);
            }
        }
    }

    private void cleanNetwork() {
        curR = 0;
        curW = 0;
        isConnected = false;
        try {
            if (socket != null) {
                socket.close();
                socket = null;
            }
            if (dataOutputStream != null) {
                dataOutputStream.close();
                dataOutputStream = null;
            }
            if (dataInputStream != null) {
                dataInputStream.close();
                dataInputStream = null;
            }
            if (sender != null && sender.isAlive()) {
                sender.interrupt();
                sender = null;
            }
            if (collector != null && collector.isAlive()) {
                collector.interrupt();
                collector = null;
            }
            System.gc();
        } catch (Exception e) {
            logger.error("cleanNetwork", e);
        }
    }

    class MessageCollector extends Thread {
        @Override
        public void run() {
            while (!socket.isClosed() && dataInputStream != null) {
                try {
                    Message message = readMessage();
                    try {
                        messageHandler.onMessage(message);
                    } finally {
                        message.cleanup();
                    }
                } catch (Exception e) {
                    break;
                }
            }
            close();
        }

        private Message readMessage() throws IOException {
            byte cmd = dataInputStream.readByte();
            if (isSendKeyComplete) {
                cmd = readKey(cmd);
            }
            int size;
            if (isSendKeyComplete) {
                byte b1 = dataInputStream.readByte();
                byte b2 = dataInputStream.readByte();
                size = (readKey(b1) & 255) << 8 | readKey(b2) & 255;
            } else {
                size = dataInputStream.readUnsignedShort();
            }
            byte[] data = new byte[size];
            int len = 0;
            int byteRead = 0;
            while (len != -1 && byteRead < size) {
                len = dataInputStream.read(data, byteRead, size - byteRead);
                if (len > 0) {
                    byteRead += len;
                }
            }
            if (isSendKeyComplete) {
                for (int i = 0; i < data.length; i++) {
                    data[i] = readKey(data[i]);
                }
            }
            return new Message(cmd, data);
        }
    }

    class Sender extends Thread {
        private final BlockingQueue<Message> messages;

        public Sender() {
            messages = new LinkedBlockingQueue<>();
        }

        public void addMessage(Message message) {
            messages.add(message);
        }

        @Override
        public void run() {
            while (isConnected) {
                try {
                    final Message msg = this.messages.take();
                    doSendMessage(msg);
                } catch (Exception ex) {
                    break;
                }
            }
        }
    }
}

