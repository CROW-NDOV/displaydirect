package nl.crowndov.displaydirect.distribution.authorization.domain;

import nl.crowndov.displaydirect.distribution.Configuration;
import nl.crowndov.displaydirect.distribution.domain.client.Subscription;

import java.time.ZoneId;
import java.time.ZonedDateTime;

/**
 * Copyright 2017 CROW-NDOV
 *
 * This file is subject to the terms and conditions defined in file 'LICENSE.txt', which is part of this source code package.
 */
public class ValidationToken {

    private final String token;
    private final String systemId;
    private final String email;
    private final ZonedDateTime generated;
    private final Subscription subscription;

    public ValidationToken(String token, String systemId, String email, ZonedDateTime generated, Subscription subscribe) {
        this.token = token;
        this.systemId = systemId;
        this.email = email;
        this.generated = generated;
        this.subscription = subscribe;
    }

    public boolean isValid() {
        return generated.plusHours(Configuration.getAuthorizationTokenMaxAge()).isAfter(ZonedDateTime.now(ZoneId.of("UTC")));
    }

    public String getEmail() {
        return email;
    }

    public String getToken() {
        return token;
    }

    public ZonedDateTime getGenerated() {
        return generated;
    }

    public String getSystemId() {
        return systemId;
    }

    public Subscription getSubscription() {
        return subscription;
    }
}
