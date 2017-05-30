package nl.crowndov.displaydirect.distribution.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Copyright 2017 CROW-NDOV
 *
 * This file is subject to the terms and conditions defined in file 'LICENSE.txt', which is part of this source code package.
 */
public class CatchableRunnable implements Runnable {
    private final Runnable runnable;
    private final Logger logger;

    public CatchableRunnable(Runnable runnable, Class c) {
        this.runnable = runnable;
        // Allow to customize the class used to log any error messages
        this.logger = LoggerFactory.getLogger(c);
    }

    public CatchableRunnable(Runnable runnable) {
        this(runnable, CatchableRunnable.class);
    }

    @Override
    public void run() {
        try {
            runnable.run();
        } catch (Exception e) {
            if (logger != null) {
                logger.error("Failed to run task {}, got exception", runnable.toString(), e);
            }
        }
    }
}
