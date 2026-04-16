package com.beemobi.rongthanonline.model;

import java.util.HashMap;

public class MessageTime {
    public static final int BAN_DOANH_RED = 1;
    public static final int WAR_BAN_DOANH_RED = 2;
    public static final int TIME_WAIT_ARENA_CUSTOM = 3;
    public static final int TIME_COMBAT_ARENA_CUSTOM = 4;
    public static final int TIME_COMBAT_ARENA_CELL = 5;
    public static final int FORGOT_CITY = 6;
    public static final int DARK_VILLAGE = 7;
    public static final int DAI_HOI_VO_THUAT = 8;
    public static final int MANOR = 9;
    public static final int TIME_WIN_NRNM = 10;
    public static final int SURVIVAL_TIME_WAIT = 11;
    public static final int SURVIVAL_ZONE_RED = 12;
    public static final int TREASURE_TIME_WAIT = 13;
    public static final int FLAG_TREASURE = 14;
    public static final int SANCTUARY = 15;
    public static final int SPACESHIP = 16;
    public static final int NANG_LUONG = 17;
    public static final int TRANH_DOAT_CO = 18;
    public static HashMap<Integer, String> templates = new HashMap<>();

    static {
        templates.put(BAN_DOANH_RED, "Bản doanh Red");
        templates.put(WAR_BAN_DOANH_RED, "Thời gian còn lại");
        templates.put(TIME_WAIT_ARENA_CUSTOM, "Trận chiến sẽ bắt đầu sau");
        templates.put(TIME_COMBAT_ARENA_CUSTOM, "Thời gian thi đấu");
        templates.put(TIME_COMBAT_ARENA_CELL, "Đấu trường Cell");
        templates.put(FORGOT_CITY, "Thành phố lãng quên");
        templates.put(DARK_VILLAGE, "Làng cổ Gira");
        templates.put(DAI_HOI_VO_THUAT, "Thời gian thi đấu");
        templates.put(MANOR, "Lãnh địa Bang hội");
        templates.put(TIME_WIN_NRNM, "# đang giữ Ngọc rồng");
        templates.put(SURVIVAL_TIME_WAIT, "Trận chiến sinh tồn sẽ bắt đầu sau");
        templates.put(SURVIVAL_ZONE_RED, "Khu vực đã đóng");
        templates.put(TREASURE_TIME_WAIT, "Trận chiến sẽ bắt đầu sau");
        templates.put(FLAG_TREASURE, "Phe # đang chiếm Thần thụ");
        templates.put(SANCTUARY, "Đại chiến Mabư");
        templates.put(SPACESHIP, "Cổng phi thuyền");
        templates.put(NANG_LUONG, "Năng lượng #/3");
        templates.put(TRANH_DOAT_CO, "# đang giữ Cờ");
    }

    public int id;
    public String text;
    public long endTime;

    public MessageTime(int id, long time) {
        this.id = id;
        text = templates.get(id);
        if (time == -1) {
            endTime = -1;
        } else {
            endTime = System.currentTimeMillis() + time;
        }
    }

    public long getCountDown() {
        if (endTime == -1) {
            return endTime;
        }
        return endTime - System.currentTimeMillis();
    }
}
