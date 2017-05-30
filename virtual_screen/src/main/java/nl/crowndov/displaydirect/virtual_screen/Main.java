package nl.crowndov.displaydirect.virtual_screen;

import nl.crowndov.displaydirect.common.messages.DisplayDirectMessage;
import nl.crowndov.displaydirect.commonclient.client.DisplayDirectClient;
import nl.crowndov.displaydirect.commonclient.client.OnDisplayDirectListener;
import nl.crowndov.displaydirect.commonclient.domain.PassTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Copyright 2017 CROW-NDOV
 *
 * This file is subject to the terms and conditions defined in file 'LICENSE.txt', which is part of this source code package.
 */
public class Main {

    private static final Logger LOGGER = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        DisplayDirectClient c = new DisplayDirectClient(Configuration.getHostname());
        c.setListener(new Listener());
        c.start();

        while (!Thread.interrupted()) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                c.stop();
                break;
            }
        }
    }

    private static class Listener implements OnDisplayDirectListener {

        @Override
        public void onScreenContentsChange(List<PassTime> times) {
            String display = times.stream()
                    .map(PassTime::toString)
                    .collect(Collectors.joining("\r\n"));
            System.out.println(display+"\r\n=============");
        }

        @Override
        public void onSubscriptionResponse(DisplayDirectMessage.SubscriptionResponse response) {
            LOGGER.info("Got subscription status response of '{}' with status '{}'", response.getSuccess(), response.getStatus());
        }

        @Override
        public void onMessage(DisplayDirectMessage.Container value) {

        }
    }
}
