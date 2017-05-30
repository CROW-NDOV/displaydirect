package nl.crowndov.displaydirect.distribution.domain.travelinfo;

import java.io.Serializable;

/**
 * Copyright 2017 CROW-NDOV
 *
 * This file is subject to the terms and conditions defined in file 'LICENSE.txt', which is part of this source code package.
 */
public class Destination implements Serializable {

    private static final long serialVersionUID = 6227585395331229936L;

    private String code;
    private DestinationValue destinationName;
    private DestinationValue destinationDetail;

    public Destination(String code, DestinationValue destinationName, DestinationValue destinationDetail) {
        this.code = code;
        this.destinationName = destinationName;
        this.destinationDetail = destinationDetail;
    }

    public String getCode() {
        return code;
    }

    public String getBestDestinationName(int length) {
        return getBest(destinationName, length);
    }

    public String getBestDestinationDetail(int length) {
        return getBest(destinationDetail, length);
    }

    private String getBest(DestinationValue value, int length) {
        // TODO: add case for 'next best value' where value is empty
        if (value == null) {
            return null;
        }
        if (length == 0) {
            return value.getValue50();
        } else if (length < 16) {
            return value.getValue16().substring(0, length);
        } else if (length < 19) {
            return value.getValue16();
        } else if (length < 24) {
            return value.getValue19();
        } else if (length < 50){
            return value.getValue24();
        } else {
            return value.getValue50();
        }
    }


    public DestinationValue getDestinationName() {
        return destinationName;
    }

    public DestinationValue getDestinationDetail() {
        return destinationDetail;
    }

    public boolean hasDetail() {
        return destinationDetail != null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Destination that = (Destination) o;

        if (!code.equals(that.code)) return false;
        if (!destinationName.equals(that.destinationName)) return false;
        return destinationDetail != null ? destinationDetail.equals(that.destinationDetail) : that.destinationDetail == null;
    }

    @Override
    public int hashCode() {
        int result = code.hashCode();
        result = 31 * result + destinationName.hashCode();
        result = 31 * result + (destinationDetail != null ? destinationDetail.hashCode() : 0);
        return result;
    }
}
