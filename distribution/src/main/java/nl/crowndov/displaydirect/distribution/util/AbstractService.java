package nl.crowndov.displaydirect.distribution.util;

import com.google.gson.Gson;
import nl.crowndov.displaydirect.distribution.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.file.*;
import java.util.Optional;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Copyright 2017 CROW-NDOV
 *
 * This file is subject to the terms and conditions defined in file 'LICENSE.txt', which is part of this source code package.
 */
public abstract class AbstractService {

    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractService.class);
    private static final Gson GSON = JsonFile.gson();

    protected static void backup(Runnable func) {
        func.run();
    }

    protected static <T> Optional<T> readFile(String key, Class<T> clazz) {
        return JsonFile.read(key).map(s1 -> (T) GSON.fromJson(s1, clazz));
    }

    protected static <T> Optional<T> readSerializedFile(String key) {
        Path input = getPathSerialized(key);
        LOGGER.info("Reading from {}", input.toString());
        if (!input.toFile().exists()) {
            return Optional.empty();
        }
        try (  FileInputStream fis = new FileInputStream(input.toString());
               ObjectInputStream ois = new ObjectInputStream(fis)) {
            T result = (T) ois.readObject();
            return Optional.of(result);
        } catch (ClassNotFoundException e) {
            LOGGER.error("Error deserializing {}", key, e);
        } catch (Exception e) {
            LOGGER.error("Error reading {}", key, e);
        }
        return Optional.empty();
    }

    protected static <T> void writeFile(String key, T data) {
        String dataStr = GSON.toJson(data);
        LOGGER.info("Got serialized data {} (len {})", key, dataStr.length());
        JsonFile.write(key, dataStr);
    }

    protected static <T> void writeFileSerialize(String key, T data) {
        String out = getPathSerialized(key+"-temp").toString();
        LOGGER.info("Writing to {}", out);
        try (FileOutputStream fos = new FileOutputStream(out, false);
             ObjectOutputStream oos = new ObjectOutputStream(fos)) {
            oos.writeObject(data);
            // Once done, move to the location we read, so that in case the (long) export is interupted, we don't lose a backup
            Files.move(getPathSerialized(key+"-temp"), getPathSerialized(key), StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.ATOMIC_MOVE);
        } catch (IOException e) {
            LOGGER.error("Error writing {}", key, e);
        }
    }

    private static Path getPathSerialized(String key) {
        return Paths.get(Configuration.getFileBasePath(), key+".out");
    }

    protected static void schedule(ScheduledExecutorService executor, String label, Runnable run, String firstTime, int hoursRepeated) {
        long minutes = DateTimeUtil.getMinutesTill(firstTime);
        LOGGER.info("Scheduling task {} to first run in {} minutes, every {} hours thereafter", label, minutes, hoursRepeated);
        executor.scheduleAtFixedRate(run, minutes, hoursRepeated*60, TimeUnit.MINUTES);
    }
}
