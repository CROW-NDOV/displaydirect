package nl.crowndov.displaydirect.commonclient.configuration;

/**
 * Copyright 2017 CROW-NDOV
 *
 * This file is subject to the terms and conditions defined in file 'LICENSE.txt', which is part of this source code package.
 */
interface SystemParameters {


    String getConnectionString(); // In the form of hostname, hostname:port or tls://hostname:port

    String getQuayCodes();

    String getClientGroup();

    String getClientId();

}
