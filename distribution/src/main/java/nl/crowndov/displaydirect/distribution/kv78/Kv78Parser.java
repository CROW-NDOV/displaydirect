package nl.crowndov.displaydirect.distribution.kv78;

import nl.crowndov.displaydirect.distribution.Configuration;
import nl.crowndov.displaydirect.distribution.kv78.domain.Kv78Packet;
import nl.crowndov.displaydirect.distribution.kv78.domain.Kv78Table;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * Copyright 2017 CROW-NDOV
 *
 * This file is subject to the terms and conditions defined in file 'LICENSE.txt', which is part of this source code package.
 */
public class Kv78Parser {

    private static final Logger LOGGER = LoggerFactory.getLogger(Configuration.class);

    private static final String CHAR_SPLIT = "\\|";
    private static final String CHAR_CONTROL = "\\";
    private static final String CHAR_EMPTY = "\\0";
    private static final String CHAR_NEWLINE = "\r\n";

    public static Kv78Packet parseMessage(String input) {
        Kv78Packet p = new Kv78Packet();
        Kv78Table t = null;
        String[] lines = input.split(CHAR_NEWLINE);
        String[] headers = null;
        for (String line : lines) {
            if (line.startsWith(CHAR_CONTROL)) {
                String[] columns = line.substring(2).split("\\|");
                switch (line.substring(1,2)) {
                    case "G":
                        p.setType(columns[0]);
                        p.setComment(columns[2]);
                        p.setEncoding(columns[5]);
                        p.setVersion(columns[6]);
                        p.setGenerated(ZonedDateTime.parse(columns[7]));
                        break;
                    case "T":
                        if (t != null) {
                            p.addTable(t);
                        }
                        t = new Kv78Table();
                        t.setTableName(columns[0]);
                        t.setTableComment(columns[2]);
                        break;
                    case "L":
                        headers = columns;
                        break;
                    default:
                        break;
                }
            } else {
                if (headers != null) {
                    Map<String, String> record = new HashMap<>();
                    String[] cells = line.split(CHAR_SPLIT);
                    for (int i = 0; i < cells.length; i++) {
                        if (!CHAR_EMPTY.contentEquals(cells[i])) {
                            record.put(headers[i], cells[i]);
                        }
                    }
                    if (t == null) {
                        t = new Kv78Table();
                    }
                    t.getRecords().add(record);
                } else {
                    // Invalid message!
                    LOGGER.error("Failed to parse message");
                }
            }
        }
        p.addTable(t);
        return p;
    }


}
