package nl.crowndov.displaydirect.common_web.gson;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;

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
                .registerTypeAdapter(ZonedDateTime.class, new ZonedDateTimeAdapter())
                .create();
    }
}
