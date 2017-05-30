package nl.crowndov.displaydirect.dashboard;

import com.netflix.config.ConfigurationManager;
import org.apache.commons.configuration.AbstractConfiguration;

/**
 * Copyright 2017 CROW-NDOV
 *
 * This file is subject to the terms and conditions defined in file 'LICENSE.txt', which is part of this source code package.
 */
public class Configuration {

    private static AbstractConfiguration config = ConfigurationManager.getConfigInstance();

    public static String getHostname() {
        return config.getString("mqtt.host", "localhost:1883");
    }

}
