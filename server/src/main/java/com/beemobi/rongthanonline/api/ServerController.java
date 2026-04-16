package com.beemobi.rongthanonline.api;

import com.beemobi.rongthanonline.server.Server;
import com.beemobi.rongthanonline.server.ServerMaintenance;
import com.beemobi.rongthanonline.upgrade.Crystallize;
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
@RequestMapping(path = "/api/server-manager")
public class ServerController {
    private static final Logger logger = Logger.getLogger(ServerController.class);

    @GetMapping(path = "/maintenance")
    public ResponseEntity<Integer> maintenance(@RequestParam("min") Integer min,
                                               @RequestParam("message") String message) {
        int rs = -1;
        try {
            if (!ServerMaintenance.getInstance().isRunning) {
                ServerMaintenance.getInstance().start(message, min * 60);
                rs = 1;
            } else {
                rs = 0;
            }
        } catch (Exception ex) {
            logger.error("maintenance", ex);
        }
        return new ResponseEntity<>(rs, HttpStatus.OK);
    }

    @GetMapping(path = "/multi-exp")
    public ResponseEntity<Integer> multiExp(@RequestParam("min") Integer min, @RequestParam("message") String message) {
        int rs = -1;
        try {
            if (!ServerMaintenance.getInstance().isRunning) {
                ServerMaintenance.getInstance().start(message, min * 60);
                rs = 1;
            } else {
                rs = 0;
            }
        } catch (Exception ex) {
            logger.error("maintenance", ex);
        }
        return new ResponseEntity<>(rs, HttpStatus.OK);
    }

    @GetMapping(path = "/crystallize")
    public ResponseEntity<String> crystallize(
            @RequestParam("star") Integer star, @RequestParam("count") Integer count) {
        String rs = "error";
        try {
            Server.getInstance().pointStar = star;
            Server.getInstance().countStar = count;
            rs = "star: " + star + ", count: " + count;
        } catch (Exception ex) {
            logger.error("crystallize", ex);
        }
        return new ResponseEntity<>(rs, HttpStatus.OK);
    }

    @GetMapping(path = "/upgrade")
    public ResponseEntity<String> upgrade(@RequestParam("upgrade") Integer star, @RequestParam("count") Integer count) {
        String rs = "error";
        try {
            Server.getInstance().pointUpgrade = star;
            Server.getInstance().countUpgrade = count;
            rs = "star: " + star + ", count: " + count;
        } catch (Exception ex) {
            logger.error("upgrade", ex);
        }
        return new ResponseEntity<>(rs, HttpStatus.OK);
    }
}
