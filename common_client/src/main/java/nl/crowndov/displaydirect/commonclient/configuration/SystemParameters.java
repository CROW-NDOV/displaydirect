package nl.crowndov.displaydirect.commonclient.configuration;

import java.util.List;

/**
 * Copyright 2017 CROW-NDOV
 *
 * This file is subject to the terms and conditions defined in file 'LICENSE.txt', which is part of this source code package.
 */
public interface SystemParameters {


    String getConnectionString(); // In the form of hostname, hostname:port or tls://hostname:port

    List<String> getStopCodes();

    String getEmail();

    String getDescription();

    String getSessionId(); // This should consist of group_id, eg. OPENGEO_123

    String getClientGroup();

    String getClientId();

    int getSubscriptionRetryMinutes();
}
