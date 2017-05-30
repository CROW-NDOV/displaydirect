package nl.crowndov.displaydirect.distribution.domain.travelinfo;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * Copyright 2017 CROW-NDOV
 *
 * This file is subject to the terms and conditions defined in file 'LICENSE.txt', which is part of this source code package.
 */
public class RealtimeMessage implements Serializable {

    private static final long serialVersionUID = -8692508260190453615L;

    private Type type;
    private LocalDate date;
    private String dataOwnerCode;
    private String stopCode;

    private String quayCode;

    public RealtimeMessage() {
    }

    public RealtimeMessage(Type type, LocalDate date, String dataOwnerCode, String stopCode) {
        this.type = type;
        this.date = date;
        this.dataOwnerCode = dataOwnerCode;
        this.stopCode = stopCode;
    }

    public String getDataOwnerCode() {
        return dataOwnerCode;
    }

    public LocalDate getDate() {
        return date;
    }

    public Type getType() {
        return type;
    }

    public String getStopCode() {
        return stopCode;
    }

    public String getQuayCode() {
        return quayCode;
    }

    public void setQuayCode(String quayCode) {
        this.quayCode = quayCode;
    }

    public enum Type {
        PASSTIME,
        MESSAGE_UPDATE,
        MESSAGE_DELETE,
    }
}
