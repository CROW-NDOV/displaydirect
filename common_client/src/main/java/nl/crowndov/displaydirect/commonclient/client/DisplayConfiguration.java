package nl.crowndov.displaydirect.commonclient.client;

import nl.crowndov.displaydirect.commonclient.configuration.DisplayParameters;
import nl.crowndov.displaydirect.commonclient.configuration.SystemParameters;

import java.time.ZoneId;

/**
 * Copyright 2017 CROW-NDOV
 *
 * This file is subject to the terms and conditions defined in file 'LICENSE.txt', which is part of this source code package.
 */
public class DisplayConfiguration {

    private static DisplayParameters display;
    private static SystemParameters system;

    public static DisplayParameters getDisplayParameters() {
        return display;
    }

    public static SystemParameters getSystemParameters() {
        return system;
    }

    public static synchronized void setDisplay(DisplayParameters display) {
        DisplayConfiguration.display = display;
    }

    public static synchronized void setSystem(SystemParameters system) {
        DisplayConfiguration.system = system;
    }

    public static ZoneId getTimezone() {
        return ZoneId.of("UTC");
    }
}
