package com.beemobi.rongthanonline.gift;

import com.beemobi.rongthanonline.data.GiftCodeData;
import com.beemobi.rongthanonline.data.GiftData;
import com.beemobi.rongthanonline.repository.GameRepository;
import com.beemobi.rongthanonline.repository.GiftDataRepository;
import com.beemobi.rongthanonline.server.Server;

import java.util.HashMap;
import java.util.List;

public class GiftCodeManager {
    private static GiftCodeManager instance;
    public HashMap<String, GiftCode> codes;

    public static GiftCodeManager getInstance() {
        if (instance == null) {
            instance = new GiftCodeManager();
        }
        return instance;
    }

    public void init() {
        codes = new HashMap<>();
        List<GiftCodeData> giftCodeDataList = GameRepository.getInstance().giftCodeData.findAll();
        for (GiftCodeData data : giftCodeDataList) {
            if (data.server == Server.ID || data.server == -1) {
                codes.put(data.code, new GiftCode(data));
            }
        }
    }
}
