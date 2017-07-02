package nl.crowndov.displaydirect.virtual_screen.test;

import nl.crowndov.displaydirect.virtual_screen.Main;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.stream.IntStream;

/**
 * Copyright 2017 CROW-NDOV
 *
 * This file is subject to the terms and conditions defined in file 'LICENSE.txt', which is part of this source code package.
 */
public class TestMain {

    private static final Logger LOGGER = LoggerFactory.getLogger(TestMain.class);

    public static void main(String[] args) throws InterruptedException {
        Executor exec = Executors.newFixedThreadPool(50);
        IntStream.range(0, 50).forEach(i -> {
                exec.execute(() -> Main.run(new TestConfiguration()));
        });
        Thread.sleep(60*1000);

    }
}
