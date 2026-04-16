package com.beemobi.rongthanonline.top.expansion.festival;

import com.beemobi.rongthanonline.top.Top;
import com.beemobi.rongthanonline.top.TopType;
import org.apache.log4j.Logger;

public class TopMartialArtsFestival extends Top {
    private static final Logger logger = Logger.getLogger(TopMartialArtsFestival.class);

    public TopMartialArtsFestival(String name) {
        super(name, TopType.MARTIAL_ARTS_FESTIVAL);
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
