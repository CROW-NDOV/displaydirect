package nl.crowndov.displaydirect.commonclient.configuration;

/**
 * Copyright 2017 CROW-NDOV
 *
 * This file is subject to the terms and conditions defined in file 'LICENSE.txt', which is part of this source code package.
 */
interface DisplayParameters {

    /* ALTERBEST */ long getDestinationAlternatingSeconds();

    /* TOONTIJD */ long getJourneyMinimumDepartureMinutes();

    /* TOONMAX */ long getMaxCombinedDirections();

    /* SNELWIS */ boolean hideJourneyOnArrival();

    /* VERTREKTIMEOUT */ long getDepartureTimeoutSeconds();

    /* RITRESET */ long getJourneyTimeoutSeconds();

    /* INDICATORVANAF */ long getUnplannedJourneyTimeoutSeconds();

    /* VERVALTWEERGAVE */ boolean showCancelledTrip();

    /* WITREGEL */ boolean showBlankLine();

    /* VRIJETEKSTTIMEOUT */ int getGeneralMessageTimeoutMinutes();

    /* KEEPALIVETIME */ int getMqttKeepaliveTime();
}
