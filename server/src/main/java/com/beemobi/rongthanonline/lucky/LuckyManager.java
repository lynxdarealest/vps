package com.beemobi.rongthanonline.lucky;

import lombok.Getter;
import org.apache.log4j.Logger;

public class LuckyManager {
    private static final Logger logger = Logger.getLogger(LuckyManager.class);

    @Getter
    private static final LuckyManager instance = new LuckyManager();

    public LuckyCoin luckyCoin;

    public LuckyBox luckyBox;

    public void init() {
        luckyCoin = new LuckyCoin();
        luckyBox = new LuckyBox();
    }

}
