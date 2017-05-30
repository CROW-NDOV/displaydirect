package nl.crowndov.displaydirect.distribution;

import com.netflix.config.ConfigurationManager;
import nl.crowndov.displaydirect.common.stats.domain.LogCode;
import nl.crowndov.displaydirect.distribution.authorization.AuthorizationWhitelist;
import nl.crowndov.displaydirect.distribution.chb.PassengerStopAssignmentLoader;
import nl.crowndov.displaydirect.distribution.chb.StopStore;
import nl.crowndov.displaydirect.distribution.input.*;
import nl.crowndov.displaydirect.distribution.messages.SubscriptionHandler;
import nl.crowndov.displaydirect.distribution.messages.SubscriptionStore;
import nl.crowndov.displaydirect.distribution.stats.MetricPusher;
import nl.crowndov.displaydirect.distribution.stats.logging.Log;
import nl.crowndov.displaydirect.distribution.transport.Transport;
import nl.crowndov.displaydirect.distribution.transport.TransportFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import java.io.IOException;
import java.util.Map;

/**
 * Copyright 2017 CROW-NDOV
 *
 * This file is subject to the terms and conditions defined in file 'LICENSE.txt', which is part of this source code package.
 */
@WebListener
public class BootListener implements ServletContextListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(BootListener.class);

    private static Transport OUTPUT_TRANSPORT;

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        LOGGER.info("Starting application");

        try {
            ConfigurationManager.loadCascadedPropertiesFromResources("configuration");
        } catch (IOException e) {
            LOGGER.error("Failed to load properties", e);
        }
        Log.send(LogCode.SYSTEM_STARTED, "System started");

        MetricPusher.start();

        Map<String,String> result = PassengerStopAssignmentLoader.load();
        if (result != null && result.size() > 0) {
            StopStore.setStore(result);
        }

        SubscriptionStore.start();
        AuthorizationWhitelist.start();

        OUTPUT_TRANSPORT = TransportFactory.get();
        OUTPUT_TRANSPORT.registerListener(SubscriptionHandler.listener);

        LineProvider.start();
        DestinationProvider.start();
        TimingPointProvider.start();

        RealTimeProvider.start();
        QuayDataProvider.start();
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        LOGGER.info("Stopping application");
        Log.send(LogCode.SYSTEM_STOPPED, "System stopped");
        MetricPusher.stop();
        OUTPUT_TRANSPORT.stop();
        SubscriptionStore.stop();
        RealTimeProvider.stop();
        AuthorizationWhitelist.stop();

        TimingPointProvider.stop();
        LineProvider.stop();
        DestinationProvider.stop();

        QuayDataProvider.stop();
    }


}
