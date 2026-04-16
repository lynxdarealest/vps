package com.beemobi.rongthanonline.top.clan;

import com.beemobi.rongthanonline.clan.Clan;
import com.beemobi.rongthanonline.clan.ClanMember;
import com.beemobi.rongthanonline.top.TopInfo;

public class TopClanInfo extends TopInfo {
    public Clan clan;

    public TopClanInfo(Clan clan) {
        this.clan = clan;
        if (clan != null) {
            this.id = clan.id;
        }
    }

    @Override
    public void setObject(Object object) {

    }

    @Override
    public void clearObject(Object object) {

    }

    @Override
    public long getScore() {
        if (clan != null) {
            score = clan.getTotalExp();
        }
        return score;
    }

    @Override
    public String getInfo() {
        if (clan != null) {
            info = String.format("Cấp %d: %d exp", clan.level, clan.exp);
        }
        return info;
    }

    @Override
    public long getUpdateTime() {
        if (clan != null && clan.updateTime != null) {
            updateTime = clan.updateTime.getTime();
        }
        return updateTime;
    }

    @Override
    public String getName() {
        if (clan != null) {
            ClanMember member = clan.getLeader();
            if (member != null) {
                name = clan.name + " (" + member.name + ")";
                gender = member.gender;
            } else {
                name = clan.name;
            }
        }
        return name;
    }

}
