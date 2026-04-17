package com.beemobi.rongthanonline.api;

import com.beemobi.rongthanonline.data.UserData;
import com.beemobi.rongthanonline.repository.GameRepository;
import com.beemobi.rongthanonline.server.Server;
import com.beemobi.rongthanonline.upgrade.Crystallize;
import com.beemobi.rongthanonline.upgrade.UpgradeItem;
import com.beemobi.rongthanonline.util.Utils;
import org.apache.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RestController
@RequestMapping(path = "/api/players")
public class PlayerController {
    private static final Logger logger = Logger.getLogger(PlayerController.class);
    private static final int TOPUP_CODE_MIN = 100000;
    private static final int TOPUP_CODE_MOD = 900000;
    private static final int TOPUP_CODE_MULTIPLIER = 534971;
    private static final int TOPUP_CODE_MULTIPLIER_INV = 802931;
    private static final int TOPUP_CODE_INCREMENT = 72817;
    private static final Pattern SIX_DIGIT_PATTERN = Pattern.compile("(\\d{6})");

    @RequestMapping(path = {"/recharge/webhook", "/zalopay/webhook"}, method = {RequestMethod.GET, RequestMethod.POST})
    public ResponseEntity<Map<String, Object>> rechargeWebhook(@RequestParam(value = "secret", required = false) String secret,
                                                               @RequestParam(value = "username", required = false) String username,
                                                               @RequestParam(value = "userId", required = false) Integer userId,
                                                               @RequestParam(value = "user_id", required = false) Integer userIdAlt,
                                                               @RequestParam(value = "topupCode", required = false) String topupCode,
                                                               @RequestParam(value = "topup_code", required = false) String topupCodeAlt,
                                                               @RequestParam("order_id") Long orderId,
                                                               @RequestParam("order_code") String orderCode,
                                                               @RequestParam("type") Integer type,
                                                               @RequestParam(value = "diamond", required = false, defaultValue = "0") Integer diamond,
                                                               @RequestParam(value = "coin", required = false, defaultValue = "0") Long coin,
                                                               @RequestHeader(value = "X-Recharge-Secret", required = false) String secretHeader) {
        try {
            String expectedSecret = Server.getInstance().rechargeWebhookSecret;
            String providedSecret = normalize(secretHeader);
            if (providedSecret.isEmpty()) {
                providedSecret = normalize(secret);
            }
            if (!expectedSecret.isEmpty() && !expectedSecret.equals(providedSecret)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(result(false, "secret khong hop le", null));
            }

            Integer resolvedUserId = userId != null ? userId : userIdAlt;
            if (resolvedUserId == null) {
                String resolvedUsername = normalize(username);
                if (!resolvedUsername.isEmpty()) {
                    List<UserData> users = GameRepository.getInstance().userData.findByUsername(resolvedUsername);
                    if (users.isEmpty()) {
                        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(result(false, "khong tim thay tai khoan", null));
                    }
                    resolvedUserId = users.get(0).id;
                    username = users.get(0).username;
                } else {
                    String rawTopupCode = normalize(topupCode);
                    if (rawTopupCode.isEmpty()) {
                        rawTopupCode = normalize(topupCodeAlt);
                    }
                    if (rawTopupCode.isEmpty()) {
                        rawTopupCode = normalize(orderCode);
                    }

                    resolvedUserId = resolveUserIdByTopupCode(rawTopupCode);
                    if (resolvedUserId == null) {
                        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result(false, "thieu username, userId hoac topup_code hop le", null));
                    }

                    Optional<UserData> userDataOpt = GameRepository.getInstance().userData.findById(resolvedUserId);
                    if (!userDataOpt.isPresent()) {
                        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(result(false, "khong tim thay tai khoan", null));
                    }
                    username = userDataOpt.get().username;
                }
            }

            if (type == null || type < 0 || type > 4) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result(false, "type khong hop le", null));
            }
            if (orderId == null || orderCode == null || orderCode.trim().isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result(false, "thieu order_id hoac order_code", null));
            }
            if (type <= 3 && (diamond == null || diamond <= 0)) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result(false, "diamond phai > 0", null));
            }
            if (type == 4 && (coin == null || coin <= 0)) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result(false, "coin phai > 0", null));
            }

            Integer rs = Server.getInstance().service.createOrder(orderId, resolvedUserId, diamond == null ? 0 : diamond, coin == null ? 0 : coin, orderCode, type);
            Map<String, Object> payload = result(true, "ok", rs);
            payload.put("userId", resolvedUserId);
            payload.put("username", username);
            payload.put("orderId", orderId);
            payload.put("orderCode", orderCode);
            payload.put("type", type);
            payload.put("diamond", diamond);
            payload.put("coin", coin);
            return ResponseEntity.ok(payload);
        } catch (Exception ex) {
            logger.error("zalopayWebhook", ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(result(false, "server loi", null));
        }
    }

    @GetMapping(path = "/card/update-diamond")
    public ResponseEntity<Integer> updateDiamondByCard(@RequestParam("user_id") Integer userId,
                                                       @RequestParam Integer diamond,
                                                       @RequestParam("order_id") Long orderId,
                                                       @RequestParam("order_code") String orderCode) {
        Integer rs = Server.getInstance().service.createOrder(orderId, userId, diamond, 0, orderCode, 0);
        return new ResponseEntity<>(rs, HttpStatus.OK);
    }

    @GetMapping(path = "/atm/update-diamond")
    public ResponseEntity<Integer> updateDiamondByAtm(@RequestParam("user_id") Integer userId,
                                                      @RequestParam("diamond") Integer diamond,
                                                      @RequestParam("order_id") Long orderId,
                                                      @RequestParam("order_code") String orderCode) {
        Integer rs = Server.getInstance().service.createOrder(orderId, userId, diamond, 0, orderCode, 1);
        return new ResponseEntity<>(rs, HttpStatus.OK);
    }

    @GetMapping(path = "/momo/update-diamond")
    public ResponseEntity<Integer> updateDiamondByMomo(@RequestParam("user_id") Integer userId,
                                                       @RequestParam("diamond") Integer diamond,
                                                       @RequestParam("order_id") Long orderId,
                                                       @RequestParam("order_code") String orderCode) {
        Integer rs = Server.getInstance().service.createOrder(orderId, userId, diamond, 0, orderCode, 2);
        return new ResponseEntity<>(rs, HttpStatus.OK);
    }

    @GetMapping(path = "/gift/update-diamond")
    public ResponseEntity<Integer> updateDiamondByGift(@RequestParam("user_id") Integer userId,
                                                       @RequestParam("diamond") Integer diamond,
                                                       @RequestParam("order_id") Long orderId,
                                                       @RequestParam("order_code") String orderCode) {
        Integer rs = Server.getInstance().service.createOrder(orderId, userId, diamond, 0, orderCode, 3);
        return new ResponseEntity<>(rs, HttpStatus.OK);
    }

    @GetMapping(path = "/atm/update-coin")
    public ResponseEntity<Integer> updateCoinByAtm(@RequestParam("user_id") Integer userId,
                                                   @RequestParam("coin") Long coin,
                                                   @RequestParam("order_id") Long orderId,
                                                   @RequestParam("order_code") String orderCode) {
        Integer rs = Server.getInstance().service.createOrder(orderId, userId, 0, coin, orderCode, 4);
        return new ResponseEntity<>(rs, HttpStatus.OK);
    }

    private Map<String, Object> result(boolean success, String message, Integer code) {
        Map<String, Object> data = new HashMap<>();
        data.put("success", success);
        data.put("message", message);
        data.put("code", code);
        return data;
    }

    private String normalize(String value) {
        return value == null ? "" : value.trim();
    }

    private Integer resolveUserIdByTopupCode(String rawTopupCode) {
        Matcher matcher = SIX_DIGIT_PATTERN.matcher(rawTopupCode == null ? "" : rawTopupCode);
        if (!matcher.find()) {
            return null;
        }

        int code;
        try {
            code = Integer.parseInt(matcher.group(1));
        } catch (Exception ex) {
            return null;
        }

        if (code < TOPUP_CODE_MIN || code > (TOPUP_CODE_MIN + TOPUP_CODE_MOD - 1)) {
            return null;
        }

        long mixed = code - TOPUP_CODE_MIN;
        long base = mod(mixed - TOPUP_CODE_INCREMENT, TOPUP_CODE_MOD);
        long userIdCandidate = mod(base * TOPUP_CODE_MULTIPLIER_INV, TOPUP_CODE_MOD);
        if (userIdCandidate <= 0 || userIdCandidate > Integer.MAX_VALUE) {
            return null;
        }

        int candidate = (int) userIdCandidate;
        Optional<UserData> userDataOpt = GameRepository.getInstance().userData.findById(candidate);
        if (!userDataOpt.isPresent()) {
            return null;
        }

        String generatedCode = buildStableTopupCode(candidate);
        return generatedCode.equals(matcher.group(1)) ? candidate : null;
    }

    private String buildStableTopupCode(Integer userId) {
        long uid = userId == null ? 0L : Math.abs(userId.longValue());
        long mixed = (uid * TOPUP_CODE_MULTIPLIER + TOPUP_CODE_INCREMENT) % TOPUP_CODE_MOD;
        int code = (int) (TOPUP_CODE_MIN + mixed);
        return String.format("%06d", code);
    }

    private long mod(long value, long mod) {
        long result = value % mod;
        return result < 0 ? result + mod : result;
    }

    @GetMapping(path = "/give-away")
    public ResponseEntity<Integer> giveAway(@RequestParam("player_id") Integer id,
                                            @RequestParam("info") String info,
                                            @RequestParam("gift_id") Integer giftId) {
        int num = 0;
        try {
        } catch (Exception ex) {
            logger.error("giveAway", ex);
        }
        return new ResponseEntity<>(num, HttpStatus.OK);
    }

    @GetMapping(path = "/lock-user")
    public ResponseEntity<Integer> lock(@RequestParam("name") String name,
                                        @RequestParam("info") String info,
                                        @RequestParam("hour_lock") Integer hourLock,
                                        @RequestParam("type") Integer type) {
        return new ResponseEntity<>(Server.getInstance().service.lockPlayer(name, hourLock, info, true, type), HttpStatus.OK);
    }

    @GetMapping(path = "/upgrade")
    public ResponseEntity<String> upgrade(@RequestParam("name") String name,
                                          @RequestParam("type") Integer type,
                                          @RequestParam("upgrade") Integer upgrade,
                                          @RequestParam("count") Integer count) {
        String rs = "error";
        try {
            if (type == 0) {
                if (UpgradeItem.players.containsKey(name)) {
                    UpgradeItem.players.remove(name);
                    rs = name + " đã được xóa";
                } else {
                    rs = name + " không có trong danh sách";
                }
            } else if (type == 1) {
                HashMap<Integer, Integer> players = UpgradeItem.players.getOrDefault(name, null);
                if (players == null) {
                    players = new HashMap<>();
                    UpgradeItem.players.put(name, players);
                }
                players.put(upgrade, count);
                rs = Utils.gson.toJson(UpgradeItem.players);
            } else if (type == 2) {
                rs = Utils.gson.toJson(UpgradeItem.players);
            }
        } catch (Exception ex) {
            logger.error("upgrade", ex);
        }
        return new ResponseEntity<>(rs, HttpStatus.OK);
    }

    @GetMapping(path = "/crystallize")
    public ResponseEntity<String> crystallize(@RequestParam("name") String name,
                                              @RequestParam("type") Integer type,
                                              @RequestParam("upgrade") Integer upgrade,
                                              @RequestParam("count") Integer count) {
        String rs = "error";
        try {
            if (type == 0) {
                if (Crystallize.players.containsKey(name)) {
                    Crystallize.players.remove(name);
                    rs = name + " đã được xóa";
                } else {
                    rs = name + " không có trong danh sách";
                }
            } else if (type == 1) {
                HashMap<Integer, Integer> players = Crystallize.players.getOrDefault(name, null);
                if (players == null) {
                    players = new HashMap<>();
                    Crystallize.players.put(name, players);
                }
                players.put(upgrade, count);
                rs = Utils.gson.toJson(Crystallize.players);
            } else if (type == 2) {
                rs = Utils.gson.toJson(Crystallize.players);
            }
        } catch (Exception ex) {
            logger.error("crystallize", ex);
        }
        return new ResponseEntity<>(rs, HttpStatus.OK);
    }



}
