package nl.crowndov.displaydirect.distribution.domain.travelinfo;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.ZonedDateTime;

/**
 * Copyright 2017 CROW-NDOV
 *
 * This file is subject to the terms and conditions defined in file 'LICENSE.txt', which is part of this source code package.
 */
public class UpdateMessage extends RealtimeMessage implements Serializable {

    private static final long serialVersionUID = -3939110132456463688L;

    private final int messageCode;

    private String messageContent;
    private ZonedDateTime messageStart;
    private ZonedDateTime messageEnd;

    public UpdateMessage(String dataOwnerCode, LocalDate date, int messageCode, String stopCode) {
        super(Type.MESSAGE_UPDATE, date, dataOwnerCode, stopCode);
        this.messageCode = messageCode;
    }

    public Integer getMessageHash() {
        return String.format("%s|%s|%s", getDataOwnerCode(), getDate().toString(), getMessageCode()).hashCode();
    }

    public int getMessageCode() {
        return messageCode;
    }

    public String getMessageContent() {
        return messageContent;
    }

    public ZonedDateTime getMessageStart() {
        return messageStart;
    }

    public ZonedDateTime getMessageEnd() {
        return messageEnd;
    }

    public void setMessageContent(String messageContent) {
        this.messageContent = messageContent;
    }

    public void setMessageStart(ZonedDateTime messageStart) {
        this.messageStart = messageStart;
    }

    public void setMessageEnd(ZonedDateTime messageEnd) {
        this.messageEnd = messageEnd;
    }
}
