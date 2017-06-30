package nl.crowndov.displaydirect.distribution.transport;

import org.fusesource.mqtt.client.QoS;

/**
 * Copyright 2017 CROW-NDOV
 *
 * This file is subject to the terms and conditions defined in file 'LICENSE.txt', which is part of this source code package.
 */
public interface Transport {

    boolean sendMessage(String topic, byte[] data);

    boolean sendMessage(String topic, byte[] data, QoS quality);

    void registerListener(OnMessageReceivedListener listener);

    void stop();

    interface OnMessageReceivedListener {

        boolean onMessageReceived(String topic, byte[] data);
    }

}
