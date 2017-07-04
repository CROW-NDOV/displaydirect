package nl.crowndov.displaydirect.distribution.transport.mqtt;

import nl.crowndov.displaydirect.distribution.transport.Transport;
import org.fusesource.mqtt.client.QoS;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Copyright 2017 CROW-NDOV
 *
 * This file is subject to the terms and conditions defined in file 'LICENSE.txt', which is part of this source code package.
 */
public class MqttTransport implements Transport {

    private static final Logger LOGGER = LoggerFactory.getLogger(MqttTransport.class);

    private OnMessageReceivedListener listener = null;

    private final MqttClient.OnMessageListener PUBLISH_LISTENER = (topic, data, ack) -> {
        LOGGER.trace("Got incoming message on topic {}", topic);
        boolean result = false;
        if (listener != null) {
             result = listener.onMessageReceived(topic, data);
        }
        if (result) { // If not we'll retry this message
            ack.run();
        }
    };

    private MqttClient c = new MqttClient(PUBLISH_LISTENER);

    @Override
    public boolean sendMessage(String topic, byte[] data) {
        return sendMessage(topic, data, QoS.AT_LEAST_ONCE);
    }

    @Override
    public boolean sendMessage(String topic, byte[] data, QoS quality) {
        return data != null && c.send(topic, data, quality);
    }

    @Override
    public void registerListener(OnMessageReceivedListener listener) {
        this.listener = listener;
    }

    @Override
    public void stop() {
        c.stop();
    }

}
