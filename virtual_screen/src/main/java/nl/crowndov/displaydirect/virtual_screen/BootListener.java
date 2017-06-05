package nl.crowndov.displaydirect.virtual_screen;

import nl.crowndov.displaydirect.commonclient.client.DisplayDirectClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

/**
 * Copyright 2017 CROW-NDOV
 *
 * This file is subject to the terms and conditions defined in file 'LICENSE.txt', which is part of this source code package.
 */
@WebListener
public class BootListener implements ServletContextListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(BootListener.class);

    private static DisplayDirectClient t;

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        LOGGER.info("Got boot initialized");
        t = new DisplayDirectClient(Configuration.getHostname(), Configuration.getClientId());
        t.start();
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        LOGGER.info("Got boot destroyed");
        t.stop();
    }

}
