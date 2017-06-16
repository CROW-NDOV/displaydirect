package nl.crowndov.displaydirect.virtual_screen;

import com.netflix.config.ConfigurationManager;
import org.apache.commons.configuration.AbstractConfiguration;

import java.util.UUID;

/**
 * Copyright 2017 CROW-NDOV
 *
 * This file is subject to the terms and conditions defined in file 'LICENSE.txt', which is part of this source code package.
 */
public class Configuration {

    private static AbstractConfiguration config = ConfigurationManager.getConfigInstance();

    public static String getHostname() {
        return config.getString("virtual_screen.mqtt.host", "tls://acc.opendris.nl:1883");
    }

    public static String getClientId() {
        return config.getString("virtual_screen.mqtt.client_id", "UNKNOWN_"+ UUID.randomUUID().toString());
    }
}
