package nl.crowndov.displaydirect.distribution.input;

import nl.crowndov.displaydirect.distribution.Configuration;
import nl.crowndov.displaydirect.distribution.util.ZeroMQUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zeromq.ZMQ;
import org.zeromq.ZMsg;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Copyright 2017 CROW-NDOV
 *
 * This file is subject to the terms and conditions defined in file 'LICENSE.txt', which is part of this source code package.
 */
public class Kv78ReceiveTask implements Runnable {

    private static final Logger LOGGER = LoggerFactory.getLogger(Kv78ReceiveTask.class);

    private final String[] kvPublishers = Configuration.getKvPublishers();
    private final List<String> kvEndpoints = Configuration.getKvEndpoints();

    private final int port;

    private final ZMQ.Context receiveContext;
    private final ZMQ.Socket subscriber;

    int addressPointer = 0;
    private boolean stop;

    public Kv78ReceiveTask(int port) {
        this.port = port;
        this.receiveContext = ZMQ.context(1);
        this.subscriber = receiveContext.socket(ZMQ.SUB);
    }

    @Override
    public void run() {
        LOGGER.info("Started ZMQ receiver");

        subscribe();

        ZMQ.Socket push = receiveContext.socket(ZMQ.PUSH);
        push.bind("tcp://*:" + port);

        ZMQ.Poller poller = receiveContext.poller();
        poller.register(subscriber);

        while (!Thread.interrupted()) {
            if (poller.poll(TimeUnit.MINUTES.toMillis(5L)) > 0){
                try{
                    String[] m = ZeroMQUtils.gunzipMultifameZMsg(ZMsg.recvMsg(subscriber));
                    push.send(m[1]);
                } catch (Exception e) {
                    LOGGER.error("Error in KV receiving", e);
                }
            } else {
                subscriber.disconnect(kvPublishers[addressPointer]);
                addressPointer++;
                if (addressPointer >= kvPublishers.length){
                    addressPointer = 0;
                }
                LOGGER.error("Connection to {} lost, reconnecting", kvPublishers[addressPointer]);
                subscribe();
            }
        }

        LOGGER.debug("Receive task is interrupted");
        disconnect();
    }

    private void subscribe() {
        LOGGER.info("Connect to " + kvPublishers[addressPointer]);
        subscriber.connect(kvPublishers[addressPointer]);
        kvEndpoints.forEach(e -> subscriber.subscribe(e.getBytes()));
    }

    private void disconnect() {
        kvEndpoints.forEach(e -> subscriber.unsubscribe(e.getBytes()));
        subscriber.close();
        receiveContext.term();
    }

    public void stop() {
        disconnect();
    }
}
