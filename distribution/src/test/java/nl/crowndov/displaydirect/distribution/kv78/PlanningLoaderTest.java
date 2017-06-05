package nl.crowndov.displaydirect.distribution.kv78;

import nl.crowndov.displaydirect.distribution.chb.PassengerStopAssignmentLoader;
import nl.crowndov.displaydirect.distribution.chb.StopStore;
import com.netflix.config.ConfigurationManager;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.io.IOException;
import java.util.Map;

/**
 * Copyright 2017 CROW-NDOV
 *
 * This file is subject to the terms and conditions defined in file 'LICENSE.txt', which is part of this source code package.
 */
public class PlanningLoaderTest {

    @Before
    public void setup() {
        try {
            ConfigurationManager.loadCascadedPropertiesFromResources("configuration");
        } catch (IOException ignored) {}
    }

    @Test
    @Ignore
    public void testPlanningLoader() {
        Map<String,String> result = PassengerStopAssignmentLoader.load();
        if (result.size() > 0) {
            StopStore.setStore(result);
        }

        new PlanningLoader().loadFiles(true);
    }
}