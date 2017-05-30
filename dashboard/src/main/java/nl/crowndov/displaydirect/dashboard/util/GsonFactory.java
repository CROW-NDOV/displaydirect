package nl.crowndov.displaydirect.dashboard.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Copyright 2017 CROW-NDOV
 *
 * This file is subject to the terms and conditions defined in file 'LICENSE.txt', which is part of this source code package.
 */
public class GsonFactory {

    public static Gson get() {
        final GsonBuilder gsonBuilder = new GsonBuilder();
        return gsonBuilder
                .registerTypeAdapter(LocalDate.class, new DateAdapter())
                .registerTypeAdapter(LocalDateTime.class, new DateTimeAdapter())
                .create();
    }
}
