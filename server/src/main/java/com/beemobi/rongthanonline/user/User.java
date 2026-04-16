package com.beemobi.rongthanonline.user;

import com.beemobi.rongthanonline.entity.player.PlayerManager;
import com.beemobi.rongthanonline.network.Session;
import com.beemobi.rongthanonline.server.Server;
import org.apache.log4j.Logger;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Timestamp;

public class User {
    private static final Logger logger = Logger.getLogger(User.class);
    public static final String SERVER_URL = "http://rto.server.rongthanonline.com:5000";
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
        /*if () {
            return UserStatus.SERVER_MAINTAIN;
        }*/
        /*String response = loginServer();
        if (response.equals("false")) {
            return UserStatus.PASSWORD_INVALID;
        }
        if (response.startsWith("lock")) {
            return UserStatus.PERMANENT_LOCK;
        }
        if (!response.startsWith("true")) {
            return UserStatus.SERVER_MAINTAIN;
        }
        String[] array = response.split("\\|");
        int index = Integer.parseInt(array[2]);
        *//*if (index != -1 && index != server && !Server.getInstance().isInterServer()) {
            server = index;
            return UserStatus.SERVER_INVALID;
        }*//*
        server = index;
        id = Integer.parseInt(array[1]);*/
        server = -1;
        id = 1;
        return UserStatus.OK;
        /*if (!CharMatcher.javaLetterOrDigit().matchesAllOf(username) || !CharMatcher.javaLetterOrDigit().matchesAllOf(password)) {
            return UserStatus.USERNAME_AND_PASSWORD_INVALID;
        }
        List<UserData> userDataList = GameRepository.getInstance().userData.findByUsernameAndPassword(username, password);
        if (userDataList.size() == 0) {
            return UserStatus.PASSWORD_INVALID;
        }
        UserData userData = userDataList.get(0);
        server = userData.server;
        if (server != -1 && server != Server.SERVER_CLIENT) {
            return UserStatus.SERVER_INVALID;
        }
        if (userData.isLock) {
            return UserStatus.PERMANENT_LOCK;
        }
        if (userData.lockTime != null && userData.lockTime.getTime() > System.currentTimeMillis()) {
            lockTime = userData.lockTime;
            return UserStatus.TIME_LOCK;
        }
        id = userData.id;
        return UserStatus.OK;*/
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
            String base_url = String.format("%s/5000/api/register-from-client?email=%s&username=%s&password=%s&server=%d", SERVER_URL, email, username, password, server);
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
            logger.error("registerServer", ex);
        }
        return "error";
    }
}

