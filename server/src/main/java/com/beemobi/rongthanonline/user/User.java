package com.beemobi.rongthanonline.user;

import com.beemobi.rongthanonline.data.UserData;
import com.beemobi.rongthanonline.repository.GameRepository;
import com.beemobi.rongthanonline.entity.player.PlayerManager;
import com.beemobi.rongthanonline.network.Session;
import com.beemobi.rongthanonline.server.Server;
import org.apache.log4j.Logger;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.sql.Timestamp;

public class User {
    private static final Logger logger = Logger.getLogger(User.class);
    public static final String SERVER_URL = "https://rto.lynxphg.me";
    public int id;
    public int server;
    public String email;
    public String username;
    public String password;
    public Timestamp lockTime;
    public Session session;

    public User(String username, String password, Session session) {
        this.username = username.toLowerCase();
        this.password = password.toLowerCase();
        this.session = session;
        server = Server.ID;
    }

    public User(String email, String username, String password, Session session) {
        this.email = email.toLowerCase();
        this.username = username.toLowerCase();
        this.password = password.toLowerCase();
        this.session = session;
        server = Server.ID;
    }

    public UserStatus login() {
        if (username == null || password == null || username.isEmpty() || password.isEmpty()) {
            return UserStatus.USERNAME_AND_PASSWORD_INVALID;
        }

        List<UserData> userDataList = GameRepository.getInstance().userData.findByUsername(username);
        if (userDataList.isEmpty()) {
            return UserStatus.PASSWORD_INVALID;
        }

        UserData userData = userDataList.get(0);
        if (!password.equals(userData.password)) {
            return UserStatus.PASSWORD_INVALID;
        }
        server = userData.server == null ? -1 : userData.server;
        if (server != -1 && server != Server.ID) {
            return UserStatus.SERVER_INVALID;
        }
        if (Boolean.TRUE.equals(userData.isLock)) {
            return UserStatus.PERMANENT_LOCK;
        }
        if (userData.lockTime != null && userData.lockTime.getTime() > System.currentTimeMillis()) {
            lockTime = userData.lockTime;
            return UserStatus.TIME_LOCK;
        }

        id = userData.id;
        return UserStatus.OK;
    }

    public boolean isAdmin() {
        return username.equals("admin") || username.equals("kerhoangde") || username.equals("admindangha");
    }

    public String loginServer() {
        try {
            String base_url = String.format("%s/api/clients/login?username=%s&password=%s&server=%d", SERVER_URL, username, password, server);
            URL url = new URL(base_url);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            if (conn.getResponseCode() != 200) {
                return "error 200";
            }
            BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));
            String output;
            while ((output = br.readLine()) != null) {
                return output.replace("\"", "");
            }
            conn.disconnect();
        } catch (Exception ex) {
            logger.error("loginServer", ex);
        }
        return "error";
    }

    public String register() {
        try {
            List<UserData> users = GameRepository.getInstance().userData.findByUsername(username);
            if (!users.isEmpty()) {
                return "false|Tài khoản đã tồn tại";
            }

            UserData userData = new UserData();
            userData.username = username;
            userData.password = password;
            userData.email = email;
            userData.server = Server.ID;
            userData.roleId = 0;
            userData.isLock = false;
            userData.orderFailLeft = 3;
            userData.lockTime = null;
            userData.updateDate = new Timestamp(System.currentTimeMillis());
            userData.createDate = new Timestamp(System.currentTimeMillis());
            GameRepository.getInstance().userData.save(userData);

            id = userData.id;
            server = Server.ID;
            return "true|" + id + "|" + server;
        } catch (Exception ex) {
            logger.error("registerServer", ex);
        }
        return "error";
    }
}

