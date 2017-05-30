package nl.crowndov.displaydirect.dashboard;

import com.netflix.config.ConfigurationManager;
import nl.crowndov.displaydirect.dashboard.mqtt.MonitoringHandler;
import nl.crowndov.displaydirect.dashboard.mqtt.MqttClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import java.io.IOException;

/**
 * Copyright 2017 CROW-NDOV
 *
 * This file is subject to the terms and conditions defined in file 'LICENSE.txt', which is part of this source code package.
 */
@WebListener
public class BootListener implements ServletContextListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(BootListener.class);
    private MqttClient mqtt;

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        try {
            ConfigurationManager.loadCascadedPropertiesFromResources("configuration");
        } catch (IOException e) {
            LOGGER.error("Failed to load properties", e);
        }

        mqtt = new MqttClient(new MonitoringHandler());
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        mqtt.stop();
    }

}
