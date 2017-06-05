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
        return Arrays.asList(config.getStringArray("distribution.kv78turbo.endpoints"));
    }

    public static String[] getKvPublishers() {
        return config.getStringArray("distribution.kv78turbo.publishers");
    }

    public static ZoneId getZoneId() {
        return ZoneId.of(config.getString("distribution.timezone.default"));
    }

    public static String getOutputTransport() {
        return config.getString("distribution.transport", "mqtt");
    }

    public static String getMqttBrokerHost() {
        return config.getString("distribution.mqtt.host", "localhost:1883");
    }

    public static int getZmqInternalPort() {
        return 9800;
    }

    public static String getMqttClientId() {
        return config.getString("distribution.mqtt.client_id", "distribution");
    }

    public static int getAuthorizationTokenMaxAge() {
        return config.getInt("distribution.authorization.max_age_hours");
    }

    public static String getBaseUrl() {
        return config.getString("distribution.install.base_url", "http://localhost:8080");
    }

    public static String getSmtpUsername() {
        return config.getString("distribution.smtp.username", null);
    }

    public static String getSmtpPassword() {
        return config.getString("distribution.smtp.password", null);
    }

    public static String getSmtpFrom() {
        return config.getString("distribution.smtp.from");
    }

    public static String getSmtpHostname() {
        return config.getString("distribution.smtp.hostname");
    }

    public static String getSmtpReplyTo() {
        return config.getString("distribution.smtp.reply_to");
    }

    public static int getSmtpPort() {
        return config.getInt("distribution.smtp.port", 25);
    }

    public static int getSmtpConnectTimeout() {
        return  config.getInt("distribution.smtp.connect_timeout", 10*1000);
    }

    public static String getFileBasePath() {
        return config.getString("distribution.backup.path", System.getProperty("java.io.tmpdir"));
    }

    public static String getKv7PlanningPath() {
        return config.getString("distribution.kv78turbo.path.planning");
    }

    public static String getKv7CalendarPath() {
        return config.getString("distribution.kv78turbo.path.calendar");
    }

    public static String getChbPath() {
        return config.getString("distribution.chb.path");
    }


    public static String getPlanningLoaderTime() {
        return config.getString("distribution.planning.loadTime", "03:30");
    }

    public static String getPlanningCleanupTime() {
        return config.getString("distribution.planning.cleanupTime", "04:00");
    }
}
