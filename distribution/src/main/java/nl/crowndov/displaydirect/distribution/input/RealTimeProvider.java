package nl.crowndov.displaydirect.distribution.input;

import nl.crowndov.displaydirect.distribution.util.CatchableRunnable;
import nl.crowndov.displaydirect.distribution.Configuration;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Copyright 2017 CROW-NDOV
 *
 * This file is subject to the terms and conditions defined in file 'LICENSE.txt', which is part of this source code package.
 */
public class RealTimeProvider {
    private static ExecutorService executor = Executors.newFixedThreadPool(2);
    private static Kv78ReceiveTask receive;
    private static Kv78ProcessTask process;

    public static void start() {
        receive = new Kv78ReceiveTask(Configuration.getZmqInternalPort());
        process = new Kv78ProcessTask(Configuration.getZmqInternalPort());
        executor.submit(new CatchableRunnable(receive, RealTimeProvider.class));
        executor.submit(new CatchableRunnable(process, RealTimeProvider.class));
    }

    public static void stop() {
        receive.stop(); // Dry up the incoming messages first
        process.stop();
        executor.shutdownNow();
    }
}
