package nl.crowndov.displaydirect.distribution.kv78;

import nl.crowndov.displaydirect.common.stats.domain.LogCode;
import nl.crowndov.displaydirect.distribution.Configuration;
import nl.crowndov.displaydirect.distribution.domain.travelinfo.PassTime;
import nl.crowndov.displaydirect.distribution.input.DestinationProvider;
import nl.crowndov.displaydirect.distribution.input.LineProvider;
import nl.crowndov.displaydirect.distribution.input.QuayDataProvider;
import nl.crowndov.displaydirect.distribution.input.TimingPointProvider;
import nl.crowndov.displaydirect.distribution.kv78.domain.Kv78Packet;
import nl.crowndov.displaydirect.distribution.kv78.domain.Kv78Table;
import nl.crowndov.displaydirect.distribution.stats.logging.Log;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.zip.GZIPInputStream;

/**
 * Copyright 2017 CROW-NDOV
 *
 * This file is subject to the terms and conditions defined in file 'LICENSE.txt', which is part of this source code package.
 */
public class PlanningLoader {

    private static final Logger LOGGER = LoggerFactory.getLogger(PlanningLoader.class);
    private static final String KV7CALENDAR_TABLE_VALIDITY = "LOCALSERVICEGROUPVALIDITY";

    private final Map<String, LocalDate> localPlanningCodes = new HashMap<>();
    private List<LocalDate> interestingDates;

    private final List<PassTime> planningRecords = new ArrayList<>();

    public void loadFiles(boolean today) {
        // This seems backwards, but at 01:00, we want to download today (starting at 04:00)
        interestingDates = Arrays.asList(LocalDate.now(), LocalDate.now().plusDays(1));

        Log.send(LogCode.PLANNING_STARTED_LOADING, "Started loading");
        LOGGER.info("Starting to load calendar files");
        handleFiles(Paths.get(Configuration.getKv7CalendarPath()), this::getCalendar);
        LOGGER.info("Starting to load planning files");
        handleFiles(Paths.get(Configuration.getKv7PlanningPath()), this::getPlanning);
        QuayDataProvider.replace(planningRecords);
        LineProvider.backup();
        TimingPointProvider.backup();
        Log.send(LogCode.PLANNING_LOADED, String.format("Loaded %s records for %s and %s", planningRecords.size(), interestingDates.get(0).toString(),
                interestingDates.get(1).toString()));
    }

    private void getPlanning(Kv78Packet packet) {
        findTable(packet, "DESTINATION").ifPresent(t -> {
            DestinationProvider.updateDestinations(t.getRecords().stream().map(PlanningMapper::toDestination).collect(Collectors.toList()));
        });
        findTable(packet, "LINE").ifPresent(t -> {
            LineProvider.updateLines(t.getRecords().stream().map(PlanningMapper::toLine).collect(Collectors.toList()));
        });
        findTable(packet, "USERTIMINGPOINT").ifPresent(t -> {
            Map<String, String> points = new HashMap<>();
            t.getRecords().forEach(rec -> {
                String timingPoint = rec.get("TimingPointDataOwnerCode")+"|"+rec.get("TimingPointCode");
                String userStop = rec.get("DataOwnerCode")+"|"+rec.get("UserStopCode");
                points.put(timingPoint, userStop);
            });
            TimingPointProvider.updateTimingPoints(points);
        });
        findTable(packet, "LOCALSERVICEGROUPPASSTIME").ifPresent(t -> {
            planningRecords.addAll(t.getRecords().stream()
                    .filter(r -> localPlanningCodes.containsKey(r.get("DataOwnerCode")+":"+r.get("LocalServiceLevelCode")))
                    .filter(r -> !r.get("JourneyStopType").equalsIgnoreCase("INFOPOINT"))
                    .filter(r -> r.get("FortifyOrderNumber").equalsIgnoreCase("0"))
                    .map(r -> PlanningMapper.passTime(localPlanningCodes.get(r.get("DataOwnerCode")+":"+r.get("LocalServiceLevelCode")), r, packet.getGenerated()))
                    .collect(Collectors.toList()));
        });
    }

    private void getCalendar(Kv78Packet packet) {
        Optional<Kv78Table> table = findTable(packet, KV7CALENDAR_TABLE_VALIDITY);
        if (!table.isPresent()) {
            LOGGER.error("Failed to get correct table from planning packet");
            return;
        }
        table.get().getRecords().forEach(r -> {
            LocalDate l = LocalDate.parse(r.get("OperationDate"));
            if (interestingDates.contains(l)) {
                localPlanningCodes.put(r.get("DataOwnerCode")+":"+r.get("LocalServiceLevelCode"), l);
            }
        });
    }

    private Optional<Kv78Table> findTable(Kv78Packet packet, String tableName) {
        // TODO: Dit kan efficienter
        return packet.getTables().stream()
                    .filter(t -> t.getTableName().contentEquals(tableName))
                    .findFirst();
    }

    private void handleFiles(Path p, Consumer<Kv78Packet> func) {
        try {
            Files.list(p)
                    .filter(f -> f.toString().endsWith(".gz"))
                    .forEach(f -> handleFile(f, func));
        } catch (IOException e) {
            LOGGER.error("Error listing files", e);
        }
    }

    private void handleFile(Path f, Consumer<Kv78Packet> func) {
        try (FileInputStream file = new FileInputStream(f.toFile());
             GZIPInputStream g = new GZIPInputStream(file);
             BufferedReader br = new BufferedReader(new InputStreamReader(g))) {
            handlePacket(f.getFileName().toString(), br.lines().collect(Collectors.joining("\r\n")), func);
        } catch (FileNotFoundException ex) {
            LOGGER.error("Can't open file, not found", ex);
        } catch (IOException e) {
            LOGGER.error("Error reading file '{}'", f.toString(), e);
        }
    }

    private void handlePacket(String sourceFile, String packet, Consumer<Kv78Packet> func) {
        Kv78Packet p = Kv78Parser.parseMessage(packet);
        p.setSourceFile(sourceFile);
        func.accept(p);
    }
}
