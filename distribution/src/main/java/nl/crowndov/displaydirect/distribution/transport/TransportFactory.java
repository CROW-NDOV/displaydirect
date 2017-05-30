package nl.crowndov.displaydirect.distribution.transport;

import nl.crowndov.displaydirect.distribution.Configuration;
import nl.crowndov.displaydirect.distribution.transport.mqtt.MqttTransport;

/**
 * Copyright 2017 CROW-NDOV
 *
 * This file is subject to the terms and conditions defined in file 'LICENSE.txt', which is part of this source code package.
 */
public class TransportFactory {

    public static Transport instance = null;

    public static synchronized Transport get() {
        if (Configuration.getOutputTransport().equalsIgnoreCase("mqtt")) {
            if (instance == null || !(instance instanceof MqttTransport)) {
                if (instance != null) {
                    instance.stop();
                }
                instance = new MqttTransport();
            }
        } else {
            throw new IllegalArgumentException("Invalid Output Transport specified");
        }
        return instance;
    }
}
