package nl.crowndov.displaydirect.distribution.domain.travelinfo;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * Copyright 2017 CROW-NDOV
 *
 * This file is subject to the terms and conditions defined in file 'LICENSE.txt', which is part of this source code package.
 */
public class DeleteMessage extends RealtimeMessage implements Serializable {

    private static final long serialVersionUID = -4335030844531563517L;

    private final int messageCode;

    public DeleteMessage(String dataOwnerCode, LocalDate date, int messageCode, String stopCode) {
        super(Type.MESSAGE_DELETE, date, dataOwnerCode, stopCode);
        this.messageCode = messageCode;
    }

    public int getMessageCode() {
        return messageCode;
    }

    public Integer getMessageHash() {
        return String.format("%s|%s|%s", getDataOwnerCode(), getDate().toString(), getMessageCode()).hashCode();
    }
}
