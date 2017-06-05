package nl.crowndov.displaydirect.distribution.input;

import nl.crowndov.displaydirect.distribution.stats.MetricStore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zeromq.ZMQ;
import org.zeromq.ZMQException;

import java.time.temporal.ChronoUnit;
import java.util.concurrent.atomic.AtomicBoolean;

public class Kv78ProcessTask implements Runnable {

    private static final Logger LOGGER = LoggerFactory.getLogger(Kv78ProcessTask.class);
    private final int port;

    private ZMQ.Context processContext = ZMQ.context(1);
    private ZMQ.Socket pull = processContext.socket(ZMQ.PULL);

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
                InputHandler.handleMessage(m);
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
