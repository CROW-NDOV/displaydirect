package nl.crowndov.displaydirect.distribution.domain.travelinfo;

import java.io.Serializable;

/**
 * Copyright 2017 CROW-NDOV
 *
 * This file is subject to the terms and conditions defined in file 'LICENSE.txt', which is part of this source code package.
 */
public class Line implements Serializable {

    private static final long serialVersionUID = -7614598114730697681L;

    private String code;
    private String publicNumber;
    private TransportType type;

    public Line(String code, String publicNumber, TransportType type) {
        this.code = code;
        this.publicNumber = publicNumber;
        this.type = type;
    }

    public String getCode() {
        return code;
    }

    public String getPublicNumber() {
        return publicNumber;
    }

    public TransportType getType() {
        return type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Line line = (Line) o;

        if (code != null ? !code.equals(line.code) : line.code != null) return false;
        if (publicNumber != null ? !publicNumber.equals(line.publicNumber) : line.publicNumber != null) return false;
        return type == line.type;
    }

    @Override
    public int hashCode() {
        int result = code != null ? code.hashCode() : 0;
        result = 31 * result + (publicNumber != null ? publicNumber.hashCode() : 0);
        result = 31 * result + (type != null ? type.hashCode() : 0);
        return result;
    }
}
