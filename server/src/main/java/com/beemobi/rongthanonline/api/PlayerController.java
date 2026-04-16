package com.beemobi.rongthanonline.api;

import com.beemobi.rongthanonline.server.Server;
import com.beemobi.rongthanonline.upgrade.Crystallize;
import com.beemobi.rongthanonline.upgrade.UpgradeItem;
import com.beemobi.rongthanonline.util.Utils;
import org.apache.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;

@RestController
@RequestMapping(path = "/api/players")
public class PlayerController {
    private static final Logger logger = Logger.getLogger(PlayerController.class);

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
