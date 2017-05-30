package nl.crowndov.displaydirect.distribution.messages.processing;

import nl.crowndov.displaydirect.distribution.domain.travelinfo.DeleteMessage;
import nl.crowndov.displaydirect.distribution.domain.travelinfo.RealtimeMessage;
import nl.crowndov.displaydirect.distribution.domain.travelinfo.UpdateMessage;
import nl.crowndov.displaydirect.distribution.input.QuayDataProvider;

public class MessagePersistenceProcessor implements Processor<RealtimeMessage> {

    @Override
    public RealtimeMessage process(RealtimeMessage input) {
        if (input.getType() == RealtimeMessage.Type.MESSAGE_UPDATE) {
            QuayDataProvider.updateMessage((UpdateMessage) input);
        } else if (input.getType() == RealtimeMessage.Type.MESSAGE_DELETE) {
            QuayDataProvider.deleteMessage((DeleteMessage) input);
        }
        return input;
    }
}
