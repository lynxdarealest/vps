package com.beemobi.rongthanonline.api.web;

import com.beemobi.rongthanonline.data.PlayerData;
import com.beemobi.rongthanonline.data.UserData;
import com.beemobi.rongthanonline.entity.player.Player;
import com.beemobi.rongthanonline.entity.player.PlayerManager;
import com.beemobi.rongthanonline.event.Event;
import com.beemobi.rongthanonline.repository.GameRepository;
import com.beemobi.rongthanonline.user.User;
import com.beemobi.rongthanonline.user.UserStatus;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

@RestController
@RequestMapping(path = "/api/web")
@CrossOrigin(origins = {
        "http://localhost:5173",
        "http://localhost:4173",
    "https://rto.lynxphg.me"
}, allowCredentials = "true")
public class WebAuthController {
    private static final String SESSION_USER_ID = "webUserId";
    private static final String SESSION_USERNAME = "webUsername";

    @GetMapping(path = "/me")
    public Map<String, Object> me(HttpSession session) {
        Integer userId = (Integer) session.getAttribute(SESSION_USER_ID);
        if (userId == null) {
            return guest();
        }

        UserData userData = GameRepository.getInstance().userData.findById(userId).orElse(null);
        if (userData == null) {
            session.removeAttribute(SESSION_USER_ID);
            session.removeAttribute(SESSION_USERNAME);
            return guest();
        }

        Map<String, Object> response = new HashMap<>();
        response.put("status", "ok");
        response.put("userId", userData.id);
        response.put("username", userData.username);

        Player player = PlayerManager.getInstance().findPlayerByUserId(userData.id).stream().findFirst().orElse(null);
        if (player != null) {
            response.put("online", true);
            response.put("diamond", player.diamond);
            response.put("coin", player.xu + player.xuKhoa);
            response.put("ruby", player.ruby);
            response.put("level", player.level);
        } else {
            List<PlayerData> playerDataList = GameRepository.getInstance().playerData.findByUserIdAndServer(userData.id, userData.server == null ? -1 : userData.server);
            PlayerData playerData = playerDataList.isEmpty() ? null : playerDataList.get(0);
            response.put("online", false);
            response.put("diamond", playerData == null || playerData.diamond == null ? 0 : playerData.diamond);
            response.put("coin", playerData == null || playerData.xu == null ? 0L : playerData.xu + (playerData.xuKhoa == null ? 0L : playerData.xuKhoa));
            response.put("ruby", playerData == null || playerData.ruby == null ? 0 : playerData.ruby);
            response.put("level", 1);
        }
        return response;
    }

    @PostMapping(path = "/login")
    public Map<String, Object> login(@RequestBody LoginRequest body, HttpSession session) {
        String username = normalize(body.username);
        String password = normalize(body.password);
        if (username.isEmpty() || password.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Vui long nhap tai khoan va mat khau");
        }

        User user = new User(username, password, null);
        UserStatus status = user.login();
        if (status != UserStatus.OK) {
            throw mapUserStatus(status, user);
        }

        session.setAttribute(SESSION_USER_ID, user.id);
        session.setAttribute(SESSION_USERNAME, user.username);
        Map<String, Object> response = me(session);
        response.put("message", "Đăng nhập thành công");
        return response;
    }

    @PostMapping(path = "/register")
    public Map<String, Object> register(@RequestBody RegisterRequest body, HttpSession session) {
        String username = normalize(body.username);
        String password = normalize(body.password);
        String email = normalize(body.email);

        if (email.isEmpty() || !email.matches("^[a-z0-9@.]+$")) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email khong hop le");
        }
        if (username.length() < 5 || username.length() > 25 || !username.matches("^[a-z0-9]+$")) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Tai khoan chi tu 5 den 25 ky tu bao gom chu va so");
        }
        if (password.length() < 5 || password.length() > 25 || !password.matches("^[a-z0-9]+$")) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Mat khau chi tu 5 den 25 ky tu bao gom chu va so");
        }

        User user = new User(email, username, password, null);
        String response = user.register();
        if (response.startsWith("false|")) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, response.substring("false|".length()));
        }
        if (!response.startsWith("true|")) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Khong the dang ky tai khoan");
        }

        session.setAttribute(SESSION_USER_ID, user.id);
        session.setAttribute(SESSION_USERNAME, user.username);
        Map<String, Object> result = me(session);
        result.put("message", "Đăng ký thành công");
        return result;
    }

    @PostMapping(path = "/logout")
    public Map<String, Object> logout(HttpSession session) {
        session.invalidate();
        Map<String, Object> response = new HashMap<>();
        response.put("status", "ok");
        response.put("message", "Da dang xuat");
        return response;
    }

        @GetMapping(path = "/events")
        public Map<String, Object> events() {
        List<Map<String, String>> items = new ArrayList<>();

        if (Event.isHungVuong()) {
            items.add(eventItem(
                "Giới thiệu sự kiện Giỗ tổ Hùng Vương",
                "Đồng hành cùng Vua Hùng, thu thập lễ vật để mở quà hiếm, đua TOP và đổi điểm nhận vật phẩm giá trị."
            ));
            items.add(eventItem(
                "Cơ chế tham gia",
                "1) Gõ hv để nhận hộ tống Mị Nương.\n"
                    + "2) Hạ quái nhận Voi 9 ngà, làm nhiệm vụ ngày nhận Gà 9 cựa.\n"
                    + "3) Hoàn thành hộ tống nhận Ngựa 9 hồng mao.\n"
                    + "4) Chế tạo Lễ vật thường/đặc biệt để mở quà và tích điểm."
            ));
            items.add(eventItem(
                "Cơ chế nạp và đổi quà",
                "Mỗi 700 Kim cương nạp hoặc được tặng sẽ nhận 1 Capsule Hùng Vương. Điểm sự kiện dùng tại mục Đổi điểm để mua quà."
            ));
        } else if (Event.isEvent()) {
            items.add(eventItem(
                "Sự kiện hiện tại",
                String.format("Máy chủ đang chạy: %s", Event.event.getName())
            ));
            items.add(eventItem(
                "Hướng dẫn chung",
                "Mở NPC sự kiện tại Núi Paozu để xem đầy đủ cơ chế, nhiệm vụ và phần thưởng."
            ));
        } else {
            items.add(eventItem(
                "Hiện chưa có sự kiện hoạt động",
                "Vui lòng theo dõi mục Tin tức để cập nhật thời gian mở sự kiện mới."
            ));
        }

        Map<String, Object> response = new HashMap<>();
        response.put("status", "ok");
        response.put("events", items);
        return response;
        }

    private Map<String, Object> guest() {
        Map<String, Object> response = new HashMap<>();
        response.put("status", "guest");
        return response;
    }

    private Map<String, String> eventItem(String title, String description) {
        Map<String, String> item = new HashMap<>();
        item.put("title", title);
        item.put("description", description);
        return item;
    }

    private String normalize(String value) {
        return value == null ? "" : value.trim().toLowerCase(Locale.ROOT);
    }

    private ResponseStatusException mapUserStatus(UserStatus status, User user) {
        switch (status) {
            case PASSWORD_INVALID:
                return new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Tai khoan hoac mat khau khong chinh xac");
            case USERNAME_AND_PASSWORD_INVALID:
                return new ResponseStatusException(HttpStatus.BAD_REQUEST, "Tai khoan hoac mat khau khong duoc chua ky tu dac biet");
            case PERMANENT_LOCK:
                return new ResponseStatusException(HttpStatus.FORBIDDEN, "Tai khoan cua ban da bi khoa");
            case TIME_LOCK:
                return new ResponseStatusException(HttpStatus.FORBIDDEN, "Tai khoan cua ban bi khoa tam thoi");
            case SERVER_INVALID:
                return new ResponseStatusException(HttpStatus.FORBIDDEN, String.format("Sai may chu dang nhap, vui long dang nhap sang may chu %d sao", user.server));
            default:
                return new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Khong the dang nhap tai khoan");
        }
    }

    public static class LoginRequest {
        public String username;
        public String password;
    }

    public static class RegisterRequest {
        public String username;
        public String password;
        public String email;
    }
}
