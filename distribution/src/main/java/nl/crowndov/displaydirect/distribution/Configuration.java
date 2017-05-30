package nl.crowndov.displaydirect.distribution;

import com.netflix.config.ConfigurationManager;
import org.apache.commons.configuration.AbstractConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.ZoneId;
import java.util.Arrays;
import java.util.List;

/**
 * Copyright 2017 CROW-NDOV
 *
 * This file is subject to the terms and conditions defined in file 'LICENSE.txt', which is part of this source code package.
 */
public class Configuration {

    private static final Logger LOGGER = LoggerFactory.getLogger(Configuration.class);

    private static AbstractConfiguration config = ConfigurationManager.getConfigInstance();

    public static List<String> getKvEndpoints() {
        return Arrays.asList(config.getStringArray("kv78turbo.endpoints"));
    }

    public static String[] getKvPublishers() {
        return config.getStringArray("kv78turbo.publishers");
    }

    public static ZoneId getZoneId() {
        return ZoneId.of(config.getString("timezone.default"));
    }

    public static String getOutputTransport() {
        return "mqtt";
    }

    public static String getMqttBrokerHost() {
        return config.getString("mqtt.host", "localhost:1883");
    }

    public static int getZmqInternalPort() {
        return 9800;
    }

    public static String getMqttClientId() {
        return config.getString("mqtt.client_id", "distribution");
    }

    public static int getAuthorizationTokenMaxAge() {
        return config.getInt("authorization.max_age_hours");
    }

    public static String getBaseUrl() {
        return config.getString("install.base_url", "http://localhost:8080");
    }

    public static String getSmtpUsername() {
        return config.getString("smtp.username", null);
    }

    public static String getSmtpPassword() {
        return config.getString("smtp.password", null);
    }

    public static String getSmtpFrom() {
        return config.getString("smtp.from");
    }

    public static String getSmtpHostname() {
        return config.getString("smtp.hostname");
    }

    public static String getSmtpReplyTo() {
        return config.getString("smtp.reply_to");
    }

    public static int getSmtpPort() {
        return config.getInt("smtp.port", 25);
    }

    public static int getSmtpConnectTimeout() {
        return  config.getInt("smtp.connect_timeout", 10*1000);
    }

    public static String getFileBasePath() {
        return config.getString("backup.path", System.getProperty("java.io.tmpdir"));
    }

    public static String getKv7PlanningPath() {
        return config.getString("kv78turbo.path.planning");
    }

    public static String getKv7CalendarPath() {
        return config.getString("kv78turbo.path.calendar");
    }

    public static String getChbPath() {
        return config.getString("chb.path");
    }


    public static String getPlanningLoaderTime() {
        return config.getString("planning.loadTime", "03:30");
    }

    public static String getPlanningCleanupTime() {
        return config.getString("planning.loadTime", "06:00");
    }
}
