package nl.crowndov.displaydirect.distribution.input;

import nl.crowndov.displaydirect.distribution.domain.travelinfo.Line;
import nl.crowndov.displaydirect.distribution.util.AbstractService;
import nl.crowndov.displaydirect.distribution.util.Store;

import java.util.List;
import java.util.Map;

/**
 * Copyright 2017 CROW-NDOV
 *
 * This file is subject to the terms and conditions defined in file 'LICENSE.txt', which is part of this source code package.
 */
public class LineProvider extends AbstractService {

    private static final Store.LineStore lines = new Store.LineStore();

    public static void start() {
        readFile("lines", Store.LineStore.class).ifPresent(lines::putAll);
    }

    public static void stop() {
        backup();
    }

    public static void backup() {
        writeFile("lines", lines);
    }

    public static void updateLines(List<Line> input) {
        input.forEach(d -> lines.putIfAbsent(d.getCode(), d));
    }

    public static Line get(String code) {
        return lines.get(code);
    }

    public static Line get(String dataowner, String linePlanningNumber) {
        return get(dataowner+":"+linePlanningNumber);
    }

    public static Map<String, Line> getAll() {
        return lines;
    }
}
