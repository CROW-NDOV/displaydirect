package nl.crowndov.displaydirect.commonclient.client;

import nl.crowndov.displaydirect.common.messages.DisplayDirectMessage;
import nl.crowndov.displaydirect.commonclient.domain.PassTime;

import java.util.List;

/**
 * Copyright 2017 CROW-NDOV
 *
 * This file is subject to the terms and conditions defined in file 'LICENSE.txt', which is part of this source code package.
 */
public interface OnDisplayDirectListener {

    void onScreenContentsChange(List<PassTime> times);

    void onSubscriptionResponse(DisplayDirectMessage.SubscriptionResponse response);

    void onMessage(DisplayDirectMessage.Container value);
}
