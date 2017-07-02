package nl.crowndov.displaydirect.virtual_screen;

import com.netflix.config.ConfigurationManager;
import nl.crowndov.displaydirect.common.messages.DisplayDirectMessage;
import nl.crowndov.displaydirect.commonclient.client.DisplayDirectClient;
import nl.crowndov.displaydirect.commonclient.client.OnDisplayDirectListener;
import nl.crowndov.displaydirect.commonclient.domain.PassTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import java.io.IOException;
import java.util.List;

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

        try {
            ConfigurationManager.loadCascadedPropertiesFromResources("configuration");
        } catch (IOException e) {
            LOGGER.error("Failed to load properties", e);
        }

        LOGGER.info("Got boot initialized");
        t = new DisplayDirectClient(new Configuration());
        t.setListener(new OnDisplayDirectListener() {
            @Override
            public void onScreenContentsChange(List<PassTime> times) { }

            @Override
            public void onSubscriptionResponse(DisplayDirectMessage.SubscriptionResponse response) {
                LOGGER.info("Got subscription response {}", response.getStatus());
            }

            @Override
            public void onMessage(DisplayDirectMessage.Container value) {

            }
        });
        t.start();
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        t.stop();
        LOGGER.info("Got boot destroyed");
    }

}
