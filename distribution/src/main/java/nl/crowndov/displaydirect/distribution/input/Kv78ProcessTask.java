package nl.crowndov.displaydirect.distribution.input;

import nl.crowndov.displaydirect.common.transport.mqtt.TopicFactory;
import nl.crowndov.displaydirect.distribution.messages.DisplayDirectMessageFactory;
import nl.crowndov.displaydirect.distribution.messages.processing.ProcessFactory;
import nl.crowndov.displaydirect.distribution.domain.travelinfo.RealtimeMessage;
import nl.crowndov.displaydirect.distribution.kv78.Kv78Parser;
import nl.crowndov.displaydirect.distribution.kv78.RealtimeMessageMapper;
import nl.crowndov.displaydirect.distribution.messages.SubscriptionStore;
import nl.crowndov.displaydirect.distribution.stats.MetricStore;
import nl.crowndov.displaydirect.distribution.transport.Transport;
import nl.crowndov.displaydirect.distribution.transport.TransportFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zeromq.ZMQ;
import org.zeromq.ZMQException;

import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

public class Kv78ProcessTask implements Runnable {

    private static final Logger LOGGER = LoggerFactory.getLogger(Kv78ProcessTask.class);
    private final int port;

    private ZMQ.Context processContext = ZMQ.context(1);
    private ZMQ.Socket pull = processContext.socket(ZMQ.PULL);

    private ProcessFactory factory = new ProcessFactory();

    private Transport transport = TransportFactory.get();
    private MetricStore metrics = MetricStore.getInstance();
    private AtomicBoolean stopped = new AtomicBoolean(false);

    public Kv78ProcessTask(int port) {
        this.port = port;
    }

    @Override
    public void run() {
        LOGGER.info("Started ZMQ pusher");

        pull.connect("tcp://127.0.0.1:"+ port);

        while (!Thread.interrupted() || stopped.get()) {
            String m = null;
            try {
                m = pull.recvStr();
                // TODO: move this
                Map<String, List<RealtimeMessage>> realtime = RealtimeMessageMapper.toRealtimeMessage(Kv78Parser.parseMessage(m))
                        .stream()
                        .map(r -> factory.process(r))
                        .filter(Objects::nonNull)
                        .collect(Collectors.groupingBy(RealtimeMessage::getQuayCode));
                realtime.forEach((key, value) -> {
                    SubscriptionStore.getForQuay(key).forEach(sub -> {
                        if (value.size() > 0) {
                            LOGGER.trace("Publishing to {}", sub.getId());
                            metrics.increaseBucketValue("kv78turbo.messages.sent", ChronoUnit.HOURS);
                            transport.sendMessage(TopicFactory.travelInformation(sub.getId()), DisplayDirectMessageFactory.fromRealTime(value, sub));
                        }
                    });
                });

            } catch (ZMQException ex) {
                LOGGER.error("ZMQ error in KV7/8 processing", ex);
            } catch (Exception e) {
                LOGGER.error("Error in KV7/8 processing", e);
                metrics.increaseBucketValue("kv78turbo.messages.errors", ChronoUnit.HOURS);
                if (m != null) {
                    LOGGER.debug("Got message {}", m);
                }
            }
        }

        LOGGER.debug("Processing task is interrupted");
        disconnect();
    }

    private void disconnect() {
        pull.setLinger(0);
        pull.disconnect("tcp://127.0.0.1:"+ port);
        pull.close();
        processContext.term();
    }

    public void stop() {
        stopped.set(true);
        disconnect();
    }
}
