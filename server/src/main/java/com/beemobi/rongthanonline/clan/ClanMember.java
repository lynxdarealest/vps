package com.beemobi.rongthanonline.clan;

import com.beemobi.rongthanonline.clan.json.ClanMemberPointInfo;
import com.beemobi.rongthanonline.data.ClanMemberData;
import com.beemobi.rongthanonline.repository.GameRepository;
import com.beemobi.rongthanonline.util.Utils;
import com.google.gson.reflect.TypeToken;

import java.sql.Date;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;

public class ClanMember {
    public long id;
    public int clanId;
    public int playerId;
    public String name;
    public int role;
    public int gender;
    public long power;
    public Timestamp joinTime;
    public transient boolean isOnline;
    public int point;
    public int pointDay;
    public transient boolean isCharged;

    public ClanMember(ClanMemberData data) {
        id = data.id;
        clanId = data.clanId;
        playerId = data.playerId;
        name = data.name;
        role = data.role;
        gender = data.gender;
        power = data.power;
        joinTime = data.joinTime;
        ClanMemberPointInfo pointInfo = Utils.gson.fromJson(data.point, new TypeToken<ClanMemberPointInfo>() {
        }.getType());
        point = pointInfo.total;
        pointDay = pointInfo.day;
    }

    public String getStrJoinTime() {
        Date date = new Date(joinTime.getTime());
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss dd-MM-yyyy");
        return formatter.format(date);
    }

    public void setRole(int role) {
        this.role = role;
        isCharged = true;
    }

    public void setName(String name) {
        if (!this.name.equals(name)) {
            this.name = name;
            isCharged = true;
        }
    }

    public void setPower(long power) {
        if (this.power != power) {
            this.power = power;
            isCharged = true;
        }
    }

    public void upPoint(int point) {
        this.point += point;
        this.pointDay += point;
        isCharged = true;
    }

    public void setPointDay(int point) {
        if (this.pointDay != point) {
            this.pointDay = point;
            isCharged = true;
        }
    }

    public void saveData() {
        if (!isCharged) {
            return;
        }
        try {
            ClanMemberPointInfo clanMemberPointInfo = new ClanMemberPointInfo(point, pointDay);
            GameRepository.getInstance().clanMemberData.saveData(id, name, role, power, Utils.gson.toJson(clanMemberPointInfo));
            isCharged = false;
        } finally {
        }
    }
}
