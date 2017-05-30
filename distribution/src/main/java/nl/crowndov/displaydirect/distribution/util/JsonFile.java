package nl.crowndov.displaydirect.distribution.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import nl.crowndov.displaydirect.distribution.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.ZonedDateTime;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Copyright 2017 CROW-NDOV
 *
 * This file is subject to the terms and conditions defined in file 'LICENSE.txt', which is part of this source code package.
 */
public class JsonFile {

    private static final Logger LOGGER = LoggerFactory.getLogger(JsonFile.class);

    private static Gson GSON = new GsonBuilder()
            .registerTypeAdapter(ZonedDateTime.class, new TypeAdapter<ZonedDateTime>() {
                @Override
                public void write(JsonWriter out, ZonedDateTime value) throws IOException {
                    out.nullValue();
                }

                @Override
                public ZonedDateTime read(JsonReader in) throws IOException {
                    return null;
                }
            })
            .create();

    public static Gson gson() {
        return GSON;
    }

    public static void write(String key, String json) {
        Path output = getPath(key);
        try {
            Files.write(output, json.getBytes(), StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
        } catch (IOException e) {
            LOGGER.error("Failed to write data to file {}", output.toString(), e);
        }
    }

    private static Path getPath(String key) {
        return Paths.get(Configuration.getFileBasePath(), key+".json");
    }

    public static Optional<String> read(String key) {
        Path input = getPath(key);
        if (!input.toFile().exists()) {
            return Optional.empty();
        }
        try {
            return Optional.ofNullable(Files.readAllLines(input).stream().collect(Collectors.joining("\\n")));
        } catch (IOException e) {
            LOGGER.error("Failed to read data from file {}", input.toString(), e);
        }
        return Optional.empty();
    }
}
