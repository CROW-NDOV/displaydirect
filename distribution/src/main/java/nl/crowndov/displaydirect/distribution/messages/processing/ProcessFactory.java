package nl.crowndov.displaydirect.distribution.messages.processing;

import nl.crowndov.displaydirect.distribution.domain.travelinfo.PassTime;
import nl.crowndov.displaydirect.distribution.domain.travelinfo.RealtimeMessage;

import java.util.Arrays;
import java.util.List;

/**
 * Copyright 2017 CROW-NDOV
 *
 * This file is subject to the terms and conditions defined in file 'LICENSE.txt', which is part of this source code package.
 */
public class ProcessFactory implements Processor<RealtimeMessage> {

    private final List<Processor<PassTime>> passTimeProcessors = Arrays.asList(new PassTimePersistenceProcessor(),
            new SubscriptionCheckProcessor(),
            new LineOrDestinationProcessor());

    private final Processor<RealtimeMessage> stopCodeProcessor = new StopCodeProcessor();
    private final Processor<RealtimeMessage> messagePeristence = new MessagePersistenceProcessor();

    @Override
    public RealtimeMessage process(RealtimeMessage input) {
        input = stopCodeProcessor.process(input);
        if (input != null && input.getType() == RealtimeMessage.Type.PASSTIME) {
            int i = 0;
            while (input != null && i < passTimeProcessors.size()) {
                input = passTimeProcessors.get(i).process((PassTime) input);
                i += 1;
            }
        } else if (input != null && input.getType() != RealtimeMessage.Type.PASSTIME) {
            input = messagePeristence.process(input);
            return input;
        }

        return input;
    }
}
