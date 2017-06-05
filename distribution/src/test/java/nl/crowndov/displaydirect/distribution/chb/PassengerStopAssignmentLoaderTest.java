package nl.crowndov.displaydirect.distribution.chb;

import com.netflix.config.ConfigurationManager;
import org.junit.Assert;
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
public class PassengerStopAssignmentLoaderTest {

    @Before
    public void setup() {
        try {
            ConfigurationManager.loadCascadedPropertiesFromResources("configuration");
        } catch (IOException ignored) {}
    }

    @Test
    @Ignore
    public void load() throws Exception {

        Map<String,String> bla = PassengerStopAssignmentLoader.load();
        Assert.assertTrue(bla.size() > 1);
    }

}