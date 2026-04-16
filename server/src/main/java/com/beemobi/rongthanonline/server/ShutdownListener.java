package com.beemobi.rongthanonline.server;

import org.apache.log4j.Logger;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class ShutdownListener {
    private static final Logger logger = Logger.getLogger(ShutdownListener.class);

    @EventListener
    public void onShutdown(ContextClosedEvent event) {
        logger.debug("Run Hook");
        Server.getInstance().saveData();
        Server.getInstance().stop();
    }
}
