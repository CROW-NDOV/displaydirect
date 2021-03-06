package nl.crowndov.displaydirect.distribution.transport;

import org.fusesource.mqtt.client.QoS;

/**
 * Copyright 2017 CROW-NDOV
 *
 * This file is subject to the terms and conditions defined in file 'LICENSE.txt', which is part of this source code package.
 */
public class TestTransport implements Transport {

    private OnMessageReceivedListener listener = null;
    private OnMessageSentListener onSentListener = null;

    @Override
    public boolean sendMessage(String topic, byte[] data) {
        return sendMessage(topic, data, QoS.AT_LEAST_ONCE);
    }

    @Override
    public boolean sendMessage(String topic, byte[] data, QoS quality) {
        if (onSentListener != null) {
            onSentListener.onMessageSent(topic, data);
            return true;
        }
        return false;
    }

    @Override
    public void registerListener(OnMessageReceivedListener listener) {
        this.listener = listener;
    }

    @Override
    public void stop() { /* NOOP */ }

    public void registerListener(OnMessageSentListener listener) {
        this.onSentListener = listener;
    }

    public interface OnMessageSentListener {
        void onMessageSent(String topic, byte[] data);
    }
}
