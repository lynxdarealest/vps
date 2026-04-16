package com.beemobi.rongthanonline.top.expansion.survival;

import com.beemobi.rongthanonline.top.Top;
import com.beemobi.rongthanonline.top.TopType;
import org.apache.log4j.Logger;

public class TopSurvival extends Top {
    private static final Logger logger = Logger.getLogger(TopSurvival.class);

    public TopSurvival(String name) {
        super(name, TopType.TOP_SURVIVAL);
        limit = 10;
    }

    @Override
    public void init() {

    }

    @Override
    public void setObject(Object object) {

    }

    @Override
    public void clearObject(Object object) {

    }
}
