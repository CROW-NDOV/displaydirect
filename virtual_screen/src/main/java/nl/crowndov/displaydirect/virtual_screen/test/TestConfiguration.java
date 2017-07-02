package nl.crowndov.displaydirect.virtual_screen.test;

import nl.crowndov.displaydirect.virtual_screen.Configuration;

import java.util.UUID;

/**
 * Copyright 2017 CROW-NDOV
 * <p>
 * This file is subject to the terms and conditions defined in file 'LICENSE.txt', which is part of this source code package.
 */
public class TestConfiguration extends Configuration {

    private final String uuid;

    public TestConfiguration() {
        uuid = UUID.randomUUID().toString();
    }

    @Override
    public String getClientGroup() {
        return "TEST";
    }

    @Override
    public String getClientId() {
        return uuid;
    }
}
