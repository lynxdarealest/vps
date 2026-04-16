package com.beemobi.rongthanonline.top.expansion.treasure;

import com.beemobi.rongthanonline.map.expansion.treasure.Pirate;
import com.beemobi.rongthanonline.map.expansion.treasure.Treasure;
import com.beemobi.rongthanonline.top.TopInfo;

public class TopTreasureInfo extends TopInfo {
    public Pirate pirate;

    public TopTreasureInfo(Pirate pirate) {
        this.pirate = pirate;
    }

    @Override
    public void setObject(Object object) {
        this.pirate = (Pirate) object;
        this.id = pirate.playerId;
        this.name = String.format("%s (%s)", pirate.name, pirate.flag == Treasure.FLAGS[0] ? "đỏ" : "xanh");
        this.gender = pirate.gender;
        this.score = pirate.total;
        this.updateTime = pirate.updateTime;
        this.info = String.format("Thành tích: %d điểm", this.score);
    }

    @Override
    public void clearObject(Object object) {

    }

    @Override
    public String getName() {
        if (pirate != null) {
            name = String.format("%s (%s)", pirate.name, pirate.flag == Treasure.FLAGS[0] ? "đỏ" : "xanh");
        }
        return name;
    }

    @Override
    public int getGender() {
        if (pirate != null) {
            gender = pirate.gender;
        }
        return gender;
    }

    @Override
    public long getScore() {
        if (pirate != null) {
            score = pirate.total;
        }
        return score;
    }

    @Override
    public long getUpdateTime() {
        if (pirate != null) {
            updateTime = pirate.updateTime;
        }
        return updateTime;
    }

    @Override
    public String getInfo() {
        return String.format("Thành tích: %d điểm", score);
    }
}
