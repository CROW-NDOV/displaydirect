package nl.crowndov.displaydirect.distribution.messages.processing;

import nl.crowndov.displaydirect.distribution.domain.travelinfo.PassTime;
import nl.crowndov.displaydirect.distribution.input.DestinationProvider;
import nl.crowndov.displaydirect.distribution.input.LineProvider;

/**
 * Copyright 2017 CROW-NDOV
 *
 * This file is subject to the terms and conditions defined in file 'LICENSE.txt', which is part of this source code package.
 */
public class LineOrDestinationProcessor implements Processor<PassTime>{
    @Override
    public PassTime process(PassTime input) {
        if (input.getLinePlanningNumber() != null) {
            input.setLine(LineProvider.get(input.getDataOwnerCode(), input.getLinePlanningNumber()));
        }
        if (input.getDestinationCode() != null) {
            input.setDestination(DestinationProvider.get(input.getDataOwnerCode(), input.getDestinationCode()));
        }
        return input;
    }
}
