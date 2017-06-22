package nl.crowndov.displaydirect.virtual_screen;

import com.netflix.config.ConfigurationManager;
import nl.crowndov.displaydirect.commonclient.configuration.AbstractConfiguration;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

/**
 * Copyright 2017 CROW-NDOV
 *
 * This file is subject to the terms and conditions defined in file 'LICENSE.txt', which is part of this source code package.
 */
public class Configuration extends AbstractConfiguration {

    private static org.apache.commons.configuration.AbstractConfiguration config = ConfigurationManager.getConfigInstance();
    private static String uuid = UUID.randomUUID().toString();

    public String getConnectionString() {
        return config.getString("virtual_screen.mqtt.host", "tls://acc.opendris.nl:1883");
    }

    @Override
    public List<String> getStopCodes() {
        String[] stops = config.getStringArray("virtual_screen.subscription.stops");
        if (stops == null || stops.length == 0) { // Provide a default, Neude
            return Collections.singletonList("NL:Q:50000360");
        }
        return Arrays.asList(stops);
    }

    @Override
    public String getEmail() {
        return config.getString("virtual_screen.subscription.email", "displaydirect@opengeo.nl"); // Mailing list will receive this so we can name & shame
    }

    @Override
    public String getDescription() {
        return config.getString("virtual_screen.subscription.description", "<none entered, Default>");
    }

    @Override
    public String getClientId() {
        String id = config.getString("virtual_screen.mqtt.client_id", null);
        if (id == null || id.isEmpty()) {
            id = uuid;
        }
        return id;
    }

    @Override
    public int getSubscriptionRetryMinutes() {
        return config.getInt("virtual_screen.subscription.retry", 1);
    }

    @Override
    public String getClientGroup() {
        return config.getString("virtual_screen.mqtt.client_group", "UNKNOWN");
    }

}
