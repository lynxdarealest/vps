package com.beemobi.rongthanonline.api.cpanel;

import com.beemobi.rongthanonline.data.GiftCodeData;
import com.beemobi.rongthanonline.data.OrderData;
import com.beemobi.rongthanonline.data.PlayerData;
import com.beemobi.rongthanonline.data.UserData;
import com.beemobi.rongthanonline.entity.player.Player;
import com.beemobi.rongthanonline.entity.player.PlayerManager;
import com.beemobi.rongthanonline.entity.player.json.ItemGiftInfo;
import com.beemobi.rongthanonline.event.*;
import com.beemobi.rongthanonline.gift.GiftCode;
import com.beemobi.rongthanonline.gift.GiftCodeManager;
import com.beemobi.rongthanonline.repository.GameRepository;
import com.beemobi.rongthanonline.server.Server;
import com.beemobi.rongthanonline.server.ServerMaintenance;
import com.beemobi.rongthanonline.util.Utils;
import com.google.gson.reflect.TypeToken;
import org.apache.log4j.Logger;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Constructor;
import java.sql.Timestamp;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@RestController
@RequestMapping(path = "/api/cpanel")
public class AdminPanelController {
    private static final Logger logger = Logger.getLogger(AdminPanelController.class);
    private static final long SESSION_TTL_MS = 8 * 60 * 60 * 1000L;

    private static final Set<String> ADMIN_USERNAMES = new HashSet<>(Arrays.asList(
            "admin", "kerhoangde", "admindangha"
    ));

    private static final LinkedHashMap<String, EventTemplate> EVENT_TEMPLATES = createEventTemplates();

    private final ConcurrentHashMap<String, AdminSession> sessions = new ConcurrentHashMap<>();

    @PostMapping(path = "/auth/login")
    public Map<String, Object> login(@RequestBody LoginRequest body) {
        String username = normalize(body.username);
        String password = normalize(body.password);

        if (username.isEmpty() || password.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Vui long nhap tai khoan va mat khau");
        }
        if (!ADMIN_USERNAMES.contains(username)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Tai khoan khong co quyen quan tri");
        }

        List<UserData> users = GameRepository.getInstance().userData.findByUsernameAndPassword(username, password);
        if (users.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Sai tai khoan hoac mat khau");
        }

        String token = UUID.randomUUID().toString();
        long expiredAt = System.currentTimeMillis() + SESSION_TTL_MS;
        sessions.put(token, new AdminSession(username, expiredAt));

        Map<String, Object> response = new HashMap<>();
        response.put("token", token);
        response.put("username", username);
        response.put("expiredAt", expiredAt);
        return response;
    }

    @PostMapping(path = "/auth/logout")
    public Map<String, Object> logout(HttpServletRequest request) {
        String token = getToken(request);
        if (!token.isEmpty()) {
            sessions.remove(token);
        }
        return ok("Da dang xuat");
    }

    @GetMapping(path = "/auth/me")
    public Map<String, Object> me(HttpServletRequest request) {
        AdminSession session = requireAdmin(request);
        Map<String, Object> result = new HashMap<>();
        result.put("username", session.username);
        result.put("expiredAt", session.expiredAt);
        return result;
    }

    @GetMapping(path = "/dashboard")
    public Map<String, Object> dashboard(HttpServletRequest request) {
        requireAdmin(request);

        Map<String, Object> result = new HashMap<>();
        result.put("serverId", Server.ID);
        result.put("serverVersion", Server.VERSION);
        result.put("onlinePlayers", PlayerManager.getInstance().getCountPlayer());
        result.put("maintenance", ServerMaintenance.getInstance().isRunning);
        result.put("activeEvent", buildEventInfo(Event.event));

        List<OrderData> recentOrders = GameRepository.getInstance().orderData.findAll(Sort.by(Sort.Direction.DESC, "createTime"));
        int pendingOrders = (int) recentOrders.stream().filter(order -> order.status != null && order.status == 0).count();
        result.put("pendingRechargeOrders", pendingOrders);
        result.put("recentRechargeCount", Math.min(recentOrders.size(), 100));
        return result;
    }

    @GetMapping(path = "/events/catalog")
    public List<Map<String, Object>> listEvents(HttpServletRequest request) {
        requireAdmin(request);

        List<Map<String, Object>> result = new ArrayList<>();
        for (EventTemplate template : EVENT_TEMPLATES.values()) {
            Map<String, Object> item = new LinkedHashMap<>();
            item.put("key", template.key);
            item.put("label", template.label);
            item.put("className", template.eventClass.getSimpleName());
            result.add(item);
        }
        return result;
    }

    @GetMapping(path = "/events/status")
    public Map<String, Object> eventStatus(HttpServletRequest request) {
        requireAdmin(request);
        return buildEventInfo(Event.event);
    }

    @PostMapping(path = "/events/activate")
    public Map<String, Object> activateEvent(HttpServletRequest request, @RequestBody ActivateEventRequest body) {
        requireAdmin(request);

        EventTemplate template = EVENT_TEMPLATES.get(normalize(body.key));
        if (template == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Event key khong hop le");
        }

        long now = System.currentTimeMillis();
        long startAt = body.startAt == null ? now : body.startAt;
        long endAt = body.endAt == null ? now + 7L * 24 * 60 * 60 * 1000 : body.endAt;
        if (endAt <= startAt) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Thoi gian ket thuc phai lon hon bat dau");
        }

        try {
            Event newEvent = instantiateEvent(template, new Timestamp(startAt), new Timestamp(endAt));
            Event.event = newEvent;
            newEvent.start();
            return buildEventInfo(newEvent);
        } catch (Exception ex) {
            logger.error("activateEvent", ex);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Khong the kich hoat event");
        }
    }

    @PostMapping(path = "/events/deactivate")
    public Map<String, Object> deactivateEvent(HttpServletRequest request) {
        requireAdmin(request);
        Event.event = null;
        return ok("Da tat event hien tai");
    }

    @GetMapping(path = "/recharge-logs")
    public List<Map<String, Object>> rechargeLogs(HttpServletRequest request,
                                                  @RequestParam(value = "limit", required = false) Integer limit) {
        requireAdmin(request);

        int safeLimit = Math.max(1, Math.min(limit == null ? 100 : limit, 300));
        List<OrderData> allOrders = GameRepository.getInstance().orderData.findAll(Sort.by(Sort.Direction.DESC, "createTime"));
        List<OrderData> orders = allOrders.stream().limit(safeLimit).collect(Collectors.toList());

        Set<Integer> userIds = orders.stream().map(order -> order.userId).filter(Objects::nonNull).collect(Collectors.toSet());
        Map<Integer, String> usernames = GameRepository.getInstance().userData.findAllById(userIds).stream()
                .collect(Collectors.toMap(user -> user.id, user -> user.username));

        List<Map<String, Object>> result = new ArrayList<>();
        for (OrderData order : orders) {
            Map<String, Object> row = new LinkedHashMap<>();
            row.put("id", order.id);
            row.put("userId", order.userId);
            row.put("username", usernames.getOrDefault(order.userId, "unknown"));
            row.put("type", order.type);
            row.put("typeName", getRechargeTypeName(order.type));
            row.put("diamond", order.diamond);
            row.put("coin", order.coin);
            row.put("status", order.status);
            row.put("statusName", order.status != null && order.status == 1 ? "DONE" : "PENDING");
            row.put("orderCode", order.orderCode);
            row.put("orderId", order.orderId);
            row.put("createTime", order.createTime == null ? null : order.createTime.getTime());
            row.put("updateTime", order.updateTime == null ? null : order.updateTime.getTime());
            result.add(row);
        }
        return result;
    }

    @GetMapping(path = "/users")
    public List<Map<String, Object>> users(HttpServletRequest request,
                                           @RequestParam(value = "keyword", required = false) String keyword,
                                           @RequestParam(value = "limit", required = false) Integer limit) {
        requireAdmin(request);

        String search = normalize(keyword);
        int safeLimit = Math.max(1, Math.min(limit == null ? 30 : limit, 100));

        List<UserData> allUsers = GameRepository.getInstance().userData.findAll(Sort.by(Sort.Direction.DESC, "id"));
        List<UserData> users = allUsers.stream()
                .filter(user -> search.isEmpty() || (user.username != null && user.username.toLowerCase(Locale.ROOT).contains(search)))
                .limit(safeLimit)
                .collect(Collectors.toList());

        List<Map<String, Object>> result = new ArrayList<>();
        for (UserData user : users) {
            Map<String, Object> row = new LinkedHashMap<>();
            row.put("id", user.id);
            row.put("username", user.username);
            row.put("server", user.server);
            row.put("isLock", user.isLock);
            row.put("lockTime", user.lockTime == null ? null : user.lockTime.getTime());
            row.put("characters", buildCharacterSnapshot(user.id));
            result.add(row);
        }
        return result;
    }

    @PostMapping(path = "/users/{userId}/password")
    public Map<String, Object> updatePassword(HttpServletRequest request,
                                              @PathVariable("userId") Integer userId,
                                              @RequestBody ChangePasswordRequest body) {
        requireAdmin(request);

        String newPassword = normalize(body.newPassword);
        if (!newPassword.matches("^[a-z0-9]{5,25}$")) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Mat khau chi gom chu thuong va so, do dai 5-25");
        }

        UserData user = GameRepository.getInstance().userData.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Khong tim thay tai khoan"));

        user.password = newPassword;
        GameRepository.getInstance().userData.save(user);
        return ok("Da cap nhat mat khau cho tai khoan " + user.username);
    }

    @PostMapping(path = "/grants")
    public Map<String, Object> grantCurrency(HttpServletRequest request, @RequestBody GrantCurrencyRequest body) {
        requireAdmin(request);

        if (body.userId == null || body.amount == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Thieu userId hoac amount");
        }
        if (body.amount <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "So luong phai lon hon 0");
        }

        String currency = normalize(body.currency).toUpperCase(Locale.ROOT);
        if (!currency.equals("DIAMOND") && !currency.equals("RUBY") && !currency.equals("GOLD")) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "currency chi ho tro DIAMOND, RUBY, GOLD");
        }

        if (currency.equals("DIAMOND")) {
            long orderId = System.currentTimeMillis();
            String orderCode = "CPANEL-" + orderId;
            int result = Server.getInstance().service.createOrder(orderId, body.userId, body.amount, 0, orderCode, 3);
            if (result != 1) {
                throw new ResponseStatusException(HttpStatus.CONFLICT, "Khong the cong ngoc ngay luc nay (ma " + result + ")");
            }

            Map<String, Object> response = ok("Da tao lenh cong ngoc thanh cong");
            response.put("orderId", orderId);
            response.put("orderCode", orderCode);
            response.put("eventRechargeCounted", true);
            return response;
        }

        List<Player> onlinePlayers = PlayerManager.getInstance().findPlayerByUserId(body.userId);
        String reason = body.reason == null ? "" : body.reason.trim();

        if (!onlinePlayers.isEmpty()) {
            for (Player player : onlinePlayers) {
                if (currency.equals("RUBY")) {
                    player.upRuby(body.amount);
                } else {
                    player.upXu(body.amount);
                }
                if (!reason.isEmpty()) {
                    player.service.startDialogOk("CPANEL cap tai nguyen: " + reason);
                }
            }
            Map<String, Object> response = ok("Da cap tai nguyen cho nhan vat online");
            response.put("affectedOnlinePlayers", onlinePlayers.size());
            return response;
        }

        List<PlayerData> playerDataList = GameRepository.getInstance().playerData.findByUserIdAndServer(body.userId, Server.ID);
        if (playerDataList.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Khong tim thay nhan vat cua tai khoan");
        }

        for (PlayerData data : playerDataList) {
            if (currency.equals("RUBY")) {
                int current = data.ruby == null ? 0 : data.ruby;
                data.ruby = current + body.amount;
            } else {
                long current = data.xu == null ? 0 : data.xu;
                data.xu = current + body.amount;
            }
        }
        GameRepository.getInstance().playerData.saveAll(playerDataList);

        Map<String, Object> response = ok("Da cap tai nguyen cho nhan vat offline");
        response.put("affectedOfflineCharacters", playerDataList.size());
        return response;
    }

    @GetMapping(path = "/gift-codes")
    public List<Map<String, Object>> listGiftCodes(HttpServletRequest request,
                                                   @RequestParam(value = "limit", required = false) Integer limit) {
        requireAdmin(request);

        int safeLimit = Math.max(1, Math.min(limit == null ? 50 : limit, 200));
        List<GiftCodeData> allCodes = GameRepository.getInstance().giftCodeData.findAll(Sort.by(Sort.Direction.DESC, "id"));

        List<Map<String, Object>> result = new ArrayList<>();
        for (GiftCodeData data : allCodes.stream().limit(safeLimit).collect(Collectors.toList())) {
            Map<String, Object> row = new LinkedHashMap<>();
            row.put("id", data.id);
            row.put("code", data.code);
            row.put("server", data.server);
            row.put("levelRequire", data.levelRequire);
            row.put("taskRequire", data.taskRequire);
            row.put("activePointRequire", data.activePointRequire);
            row.put("expiryTime", data.expiryTime == null ? null : data.expiryTime.getTime());
            row.put("createTime", data.createTime == null ? null : data.createTime.getTime());
            result.add(row);
        }
        return result;
    }

    @PostMapping(path = "/gift-codes")
    public Map<String, Object> createGiftCode(HttpServletRequest request, @RequestBody CreateGiftCodeRequest body) {
        requireAdmin(request);

        String code = normalize(body.code);
        if (!code.matches("^[a-z0-9_-]{5,30}$")) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Code chi gom chu thuong, so, _ hoac -, do dai 5-30");
        }

        String itemsJson = body.itemsJson == null ? "" : body.itemsJson.trim();
        if (itemsJson.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "itemsJson khong duoc rong");
        }

        ArrayList<ItemGiftInfo> itemInfos;
        try {
            itemInfos = Utils.gson.fromJson(itemsJson, new TypeToken<ArrayList<ItemGiftInfo>>() {
            }.getType());
        } catch (Exception ex) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "itemsJson khong hop le");
        }

        if (itemInfos == null || itemInfos.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Danh sach vat pham gift code khong duoc rong");
        }

        GiftCodeManager manager = GiftCodeManager.getInstance();
        if (manager.codes == null) {
            manager.init();
        }

        if (manager.codes.containsKey(code)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Gift code da ton tai");
        }

        List<GiftCodeData> allCodes = GameRepository.getInstance().giftCodeData.findAll(Sort.by(Sort.Direction.DESC, "id"));
        int nextId = allCodes.isEmpty() ? 1 : allCodes.get(0).id + 1;

        long now = System.currentTimeMillis();
        long expiry = body.expiryAt == null ? now + 30L * 24 * 60 * 60 * 1000 : body.expiryAt;

        GiftCodeData data = new GiftCodeData();
        data.id = nextId;
        data.server = Server.ID;
        data.code = code;
        data.items = Utils.gson.toJson(itemInfos);
        data.levelRequire = body.levelRequire == null ? 1 : Math.max(body.levelRequire, 1);
        data.taskRequire = body.taskRequire == null ? 0 : Math.max(body.taskRequire, 0);
        data.activePointRequire = body.activePointRequire == null ? 0 : Math.max(body.activePointRequire, 0);
        data.expiryTime = new Timestamp(expiry);
        data.createTime = new Timestamp(now);

        GameRepository.getInstance().giftCodeData.save(data);
        manager.codes.put(code, new GiftCode(data));

        Map<String, Object> response = ok("Da tao gift code moi");
        response.put("id", data.id);
        response.put("code", data.code);
        response.put("expiryTime", data.expiryTime.getTime());
        return response;
    }

    private AdminSession requireAdmin(HttpServletRequest request) {
        cleanupExpiredSessions();
        String token = getToken(request);
        if (token.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Thieu token quan tri");
        }
        AdminSession session = sessions.get(token);
        if (session == null || session.expiredAt < System.currentTimeMillis()) {
            sessions.remove(token);
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Token het han hoac khong hop le");
        }
        return session;
    }

    private void cleanupExpiredSessions() {
        long now = System.currentTimeMillis();
        sessions.entrySet().removeIf(entry -> entry.getValue().expiredAt < now);
    }

    private String getToken(HttpServletRequest request) {
        String header = request.getHeader("X-CPanel-Token");
        if (header != null && !header.trim().isEmpty()) {
            return header.trim();
        }
        String query = request.getParameter("token");
        return query == null ? "" : query.trim();
    }

    private List<Map<String, Object>> buildCharacterSnapshot(Integer userId) {
        List<Map<String, Object>> snapshots = new ArrayList<>();
        List<PlayerData> playerDataList = GameRepository.getInstance().playerData.findByUserIdAndServer(userId, Server.ID);
        Map<Integer, Player> onlineById = PlayerManager.getInstance().findPlayerByUserId(userId).stream()
                .collect(Collectors.toMap(player -> player.id, player -> player));

        for (PlayerData data : playerDataList) {
            Map<String, Object> item = new LinkedHashMap<>();
            item.put("playerId", data.id);
            item.put("name", data.name);

            Player online = onlineById.get(data.id);
            item.put("online", online != null);
            if (online != null) {
                item.put("diamond", online.diamond);
                item.put("ruby", online.ruby);
                item.put("gold", online.xu);
            } else {
                item.put("diamond", data.diamond == null ? 0 : data.diamond);
                item.put("ruby", data.ruby == null ? 0 : data.ruby);
                item.put("gold", data.xu == null ? 0 : data.xu);
            }
            snapshots.add(item);
        }
        return snapshots;
    }

    private static LinkedHashMap<String, EventTemplate> createEventTemplates() {
        LinkedHashMap<String, EventTemplate> templates = new LinkedHashMap<>();
        templates.put("haloween_2024", new EventTemplate("haloween_2024", "Haloween 2024", Haloween2024.class));
        templates.put("co_hon_2024", new EventTemplate("co_hon_2024", "Co Hon 2024", CoHon2024.class));
        templates.put("trung_thu_2024", new EventTemplate("trung_thu_2024", "Trung Thu 2024", TrungThu2024.class));
        templates.put("tuu_truong_2024", new EventTemplate("tuu_truong_2024", "Tuu Truong 2024", TuuTruong2024.class));
        templates.put("he_2024", new EventTemplate("he_2024", "He 2024", He2024.class));
        templates.put("tet_2024", new EventTemplate("tet_2024", "Tet 2024", Tet2024.class));
        templates.put("woman_day", new EventTemplate("woman_day", "Quoc te Phu nu", WomanDay.class));
        templates.put("hung_vuong", new EventTemplate("hung_vuong", "Gio to Hung Vuong", HungVuong.class));
        templates.put("noel_2023", new EventTemplate("noel_2023", "Noel 2023", Noel2023.class));
        return templates;
    }

    private Event instantiateEvent(EventTemplate template, Timestamp startTime, Timestamp endTime) throws Exception {
        Constructor<?>[] constructors = template.eventClass.getConstructors();
        for (Constructor<?> constructor : constructors) {
            Class<?>[] params = constructor.getParameterTypes();
            if (params.length == 2 && params[0] == Timestamp.class && params[1] == Timestamp.class) {
                return (Event) constructor.newInstance(startTime, endTime);
            }
            if (params.length == 3 && params[0] == String.class && params[1] == Timestamp.class && params[2] == Timestamp.class) {
                return (Event) constructor.newInstance(template.label, startTime, endTime);
            }
        }
        throw new IllegalStateException("Khong tim thay constructor hop le cho event " + template.key);
    }

    private Map<String, Object> buildEventInfo(Event event) {
        Map<String, Object> result = new LinkedHashMap<>();
        if (event == null) {
            result.put("active", false);
            return result;
        }

        result.put("active", true);
        result.put("name", event.getName());
        result.put("className", event.getClass().getSimpleName());
        result.put("startTime", event.getStartTime() == null ? null : event.getStartTime().getTime());
        result.put("endTime", event.getEndTime() == null ? null : event.getEndTime().getTime());

        String key = EVENT_TEMPLATES.values().stream()
                .filter(template -> template.eventClass == event.getClass())
                .map(template -> template.key)
                .findFirst()
                .orElse("");
        result.put("key", key);
        return result;
    }

    private String getRechargeTypeName(Integer type) {
        if (type == null) {
            return "UNKNOWN";
        }
        switch (type) {
            case 0:
                return "CARD";
            case 1:
                return "ATM";
            case 2:
                return "MOMO";
            case 3:
                return "GIFT";
            case 4:
                return "ZALOPAY";
            default:
                return "UNKNOWN";
        }
    }

    private Map<String, Object> ok(String message) {
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("success", true);
        response.put("message", message);
        response.put("time", System.currentTimeMillis());
        return response;
    }

    private String normalize(String value) {
        return value == null ? "" : value.trim().toLowerCase(Locale.ROOT);
    }

    private static class AdminSession {
        public final String username;
        public final long expiredAt;

        public AdminSession(String username, long expiredAt) {
            this.username = username;
            this.expiredAt = expiredAt;
        }
    }

    private static class EventTemplate {
        public final String key;
        public final String label;
        public final Class<? extends Event> eventClass;

        public EventTemplate(String key, String label, Class<? extends Event> eventClass) {
            this.key = key;
            this.label = label;
            this.eventClass = eventClass;
        }
    }

    public static class LoginRequest {
        public String username;
        public String password;
    }

    public static class ActivateEventRequest {
        public String key;
        public Long startAt;
        public Long endAt;
    }

    public static class ChangePasswordRequest {
        public String newPassword;
    }

    public static class GrantCurrencyRequest {
        public Integer userId;
        public String currency;
        public Integer amount;
        public String reason;
    }

    public static class CreateGiftCodeRequest {
        public String code;
        public String itemsJson;
        public Integer levelRequire;
        public Integer taskRequire;
        public Integer activePointRequire;
        public Long expiryAt;
    }
}
