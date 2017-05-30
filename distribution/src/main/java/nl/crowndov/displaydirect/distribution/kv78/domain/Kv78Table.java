package nl.crowndov.displaydirect.distribution.kv78.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Copyright 2017 CROW-NDOV
 *
 * This file is subject to the terms and conditions defined in file 'LICENSE.txt', which is part of this source code package.
 */
public class Kv78Table {

    private String tableName;
    private String tableComment;

    private List<Map<String, String>> records = new ArrayList<>();


    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public void setTableComment(String tableComment) {
        this.tableComment = tableComment;
    }

    public void setRecords(List<Map<String, String>> records) {
        this.records = records;
    }

    public String getTableName() {
        return tableName;
    }

    public String getTableComment() {
        return tableComment;
    }

    public List<Map<String, String>> getRecords() {
        return records;
    }
}
