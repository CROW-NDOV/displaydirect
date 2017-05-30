package nl.crowndov.displaydirect.distribution.input;

import org.junit.Test;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Kv78ProcessTaskTest {

    @Test
    public void testShutdown() throws InterruptedException {
        ExecutorService e = Executors.newFixedThreadPool(1);
        Kv78ProcessTask proc = new Kv78ProcessTask(9100);
        Kv78ReceiveTask receive = new Kv78ReceiveTask(9100);
        e.submit(proc);
        e.submit(receive);

        Thread.sleep(1000L);

        receive.stop();
        proc.stop();
        e.shutdown();
        e.shutdownNow();
    }

}