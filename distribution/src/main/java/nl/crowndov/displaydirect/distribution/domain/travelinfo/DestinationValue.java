package nl.crowndov.displaydirect.distribution.domain.travelinfo;

import java.io.Serializable;

/**
 * Copyright 2017 CROW-NDOV
 *
 * This file is subject to the terms and conditions defined in file 'LICENSE.txt', which is part of this source code package.
 */
public class DestinationValue implements Serializable {

    private static final long serialVersionUID = -2544284159421983052L;

    private String value16;
    private String value19;
    private String value24;
    private String value50;

    public DestinationValue(String value16, String value19, String value24, String value50) {
        this.value16 = value16;
        this.value19 = value19;
        this.value24 = value24;
        this.value50 = value50;
    }

    public String getValue16() {
        return value16;
    }

    public String getValue19() {
        return value19;
    }

    public String getValue24() {
        return value24;
    }

    public String getValue50() {
        return value50;
    }

    public boolean isBlank() {
        return value16 == null && value19 == null && value24 == null && value50 == null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DestinationValue that = (DestinationValue) o;

        if (value16 != null ? !value16.equals(that.value16) : that.value16 != null) return false;
        if (value19 != null ? !value19.equals(that.value19) : that.value19 != null) return false;
        if (value24 != null ? !value24.equals(that.value24) : that.value24 != null) return false;
        return value50 != null ? value50.equals(that.value50) : that.value50 == null;
    }

    @Override
    public int hashCode() {
        int result = value16 != null ? value16.hashCode() : 0;
        result = 31 * result + (value19 != null ? value19.hashCode() : 0);
        result = 31 * result + (value24 != null ? value24.hashCode() : 0);
        result = 31 * result + (value50 != null ? value50.hashCode() : 0);
        return result;
    }
}
