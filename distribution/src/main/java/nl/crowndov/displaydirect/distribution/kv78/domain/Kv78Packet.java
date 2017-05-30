package nl.crowndov.displaydirect.distribution.kv78.domain;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Copyright 2017 CROW-NDOV
 *
 * This file is subject to the terms and conditions defined in file 'LICENSE.txt', which is part of this source code package.
 */
public class Kv78Packet {

    private String type;
    private String comment;
    private String encoding;
    private String version;
    private ZonedDateTime generated;

    private String sourceFile;

    private List<Kv78Table> tables = new ArrayList<>();

    public void setType(String type) {
        this.type = type;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public void setEncoding(String encoding) {
        this.encoding = encoding;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public void setGenerated(ZonedDateTime generated) {
        this.generated = generated;
    }

    public void addTable(Kv78Table table) {
        tables.add(table);
    }

    public List<Kv78Table> getTables() {
        return tables;
    }

    public String getType() {
        return type;
    }

    public String getComment() {
        return comment;
    }

    public String getEncoding() {
        return encoding;
    }

    public String getVersion() {
        return version;
    }

    public ZonedDateTime getGenerated() {
        return generated;
    }

    public String getSourceFile() {
        return sourceFile;
    }

    public void setSourceFile(String sourceFile) {
        this.sourceFile = sourceFile;
    }
}
