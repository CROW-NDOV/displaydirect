package nl.crowndov.displaydirect.virtual_screen.resources;

import com.google.protobuf.InvalidProtocolBufferException;
import nl.crowndov.displaydirect.common.messages.DisplayDirectMessage;
import nl.crowndov.displaydirect.commonclient.mqtt.MqttClient;
import nl.crowndov.displaydirect.virtual_screen.Configuration;
import org.glassfish.jersey.media.sse.EventOutput;
import org.glassfish.jersey.media.sse.OutboundEvent;
import org.glassfish.jersey.media.sse.SseFeature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import java.io.IOException;
import java.util.Collections;
import java.util.UUID;
import java.util.stream.Collectors;

@Path("/halte")
@Produces(SseFeature.SERVER_SENT_EVENTS)
public class StopSystemEvents {

    private static final Logger LOGGER = LoggerFactory.getLogger(StopSystemEvents.class);

    @GET
    @Path("/{stopCode}")
    public EventOutput getServerSentEvents(@PathParam("stopCode") final String stopCode) {
        final EventOutput eventOutput = new EventOutput();
        new Thread(() -> new MqttClient(Configuration.getHostname(), UUID.randomUUID().toString(), Collections.singletonList(stopCode), new OnMessageCallback(eventOutput))).start();
        return eventOutput;
    }

    private static class OnMessageCallback implements MqttClient.onNewMessage {
        private final EventOutput eventOutput;

        public OnMessageCallback(EventOutput eventOutput) {
            this.eventOutput = eventOutput;
        }

        @Override
        public void onMessage(String topic, byte[] data) {
            try {
                    if (topic.endsWith("travel_information")) {
                        DisplayDirectMessage.Container value = DisplayDirectMessage.Container.parseFrom(data);
                        final OutboundEvent.Builder eventBuilder
                                = new OutboundEvent.Builder();
                        eventBuilder.name("message-to-client");
                        eventBuilder.data(String.class, "Got info for stops " + value.getPassingTimes(0).getStopCodeList().stream().collect(Collectors.joining(", ")));
                        final OutboundEvent event = eventBuilder.build();
                        eventOutput.write(event);
                    } else if (topic.endsWith("subscription_response")) {
                        DisplayDirectMessage.SubscriptionResponse response = DisplayDirectMessage.SubscriptionResponse.parseFrom(data);
                        LOGGER.error("Got '{}' response of status '{}'", response.getSuccess(), response.getStatus());
                }
            } catch (InvalidProtocolBufferException e) {
                LOGGER.error("Failed to parse message", e);
            } catch (IOException e) {
                throw new RuntimeException("Error when writing the event.", e);
            }
        }
    }
}