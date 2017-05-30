package nl.crowndov.displaydirect.distribution.messages.processing;

import nl.crowndov.displaydirect.distribution.domain.travelinfo.PassTime;
import nl.crowndov.displaydirect.distribution.input.QuayDataProvider;

public class PassTimePersistenceProcessor implements Processor<PassTime> {
    @Override
    public PassTime process(PassTime input) {
        QuayDataProvider.updatePassTime(input);
        return input;
    }
}
