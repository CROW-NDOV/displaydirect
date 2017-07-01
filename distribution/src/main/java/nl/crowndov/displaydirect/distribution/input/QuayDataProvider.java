package nl.crowndov.displaydirect.distribution.input;

import nl.crowndov.displaydirect.common.transport.mqtt.TopicFactory;
import nl.crowndov.displaydirect.distribution.Configuration;
import nl.crowndov.displaydirect.distribution.domain.QuayStore;
import nl.crowndov.displaydirect.distribution.domain.travelinfo.DeleteMessage;
import nl.crowndov.displaydirect.distribution.domain.travelinfo.PassTime;
import nl.crowndov.displaydirect.distribution.domain.travelinfo.RealtimeMessage;
import nl.crowndov.displaydirect.distribution.domain.travelinfo.UpdateMessage;
import nl.crowndov.displaydirect.distribution.kv78.PlanningLoader;
import nl.crowndov.displaydirect.distribution.kv78.PlanningMapper;
import nl.crowndov.displaydirect.distribution.messages.DisplayDirectMessageFactory;
import nl.crowndov.displaydirect.distribution.messages.SubscriptionStore;
import nl.crowndov.displaydirect.distribution.messages.processing.LineOrDestinationProcessor;
import nl.crowndov.displaydirect.distribution.stats.MetricStore;
import nl.crowndov.displaydirect.distribution.transport.Transport;
import nl.crowndov.displaydirect.distribution.transport.TransportFactory;
import nl.crowndov.displaydirect.distribution.util.AbstractService;
import nl.crowndov.displaydirect.distribution.util.CatchableRunnable;
import nl.crowndov.displaydirect.distribution.util.Store;
import org.fusesource.mqtt.client.QoS;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Copyright 2017 CROW-NDOV
 *
 * This file is subject to the terms and conditions defined in file 'LICENSE.txt', which is part of this source code package.
 */
public class QuayDataProvider extends AbstractService {

    private static final Logger LOGGER = LoggerFactory.getLogger(QuayDataProvider.class);

    private static final String FILE_JOURNEYS = "data_journeys";
    private static final String FILE_MESSAGES = "data_messages";

    private static ScheduledExecutorService schedule = Executors.newScheduledThreadPool(2);
    private static LineOrDestinationProcessor lineDestProcess = new LineOrDestinationProcessor();

    private static final Store.PlanningStore quays = new Store.PlanningStore();

    private static ScheduledFuture<?> backupTask;

    private static Transport transport = TransportFactory.get();

    public static void start() {
        LOGGER.debug("Loading data");
        Optional<List<PassTime>> lastData = readSerializedFile(FILE_JOURNEYS);
        if (lastData.isPresent() && lastData.get().size() > 0) {
            replace(lastData.get());
            LOGGER.info("Restored {} entries to planning from backupTask", lastData.get().size());
            cleanupPlanning();

            Optional<List<UpdateMessage>> messages = readSerializedFile(FILE_MESSAGES);
            if (messages.isPresent()) {
                replaceMessages(messages.get());
            } else {
                LOGGER.error("Failed to recover general messages");
            }

        } else {
            LOGGER.info("Backup empty, loading new planning", quays.size());
            schedule.submit(getPlanningLoader(true));
        }

        schedule(schedule, "planningLoader", getPlanningLoader(false), Configuration.getPlanningLoaderTime(), 24);
        schedule(schedule, "planningCleanup", new CatchableRunnable(QuayDataProvider::cleanupPlanning), Configuration.getPlanningCleanupTime(), 24);
    }

    private static void replaceMessages(List<UpdateMessage> updateMessages) {
        int[] records = { 0 };
        updateMessages.stream()
                .filter(t -> t.getQuayCode() != null)
                .collect(Collectors.groupingBy(UpdateMessage::getQuayCode))
                .forEach((key, value) -> {
                    if (!quays.containsKey(key)) {
                        quays.put(key, new QuayStore());
                    }
                    value.forEach(v -> quays.get(key).getMessages().put(v.getMessageHash(), v));
                });
        LOGGER.info("QuayStore now contains {} messages for {} stops", records[0], quays.size());
    }

    private static void cleanupPlanning() {
        LocalDate now = LocalDate.now(Configuration.getZoneId());
        List<String> remove = new ArrayList<>();
        quays.forEach((stop, quayStore) ->
                quayStore.getPassTimes().forEach((hash, pass) -> {
                    if (pass.getDate().isBefore(now)) {
                        remove.add(stop+"|"+hash);
                    }
                }));
        remove.forEach(rem -> {
            String[] spl = rem.split("\\|");
            quays.get(spl[0]).getPassTimes().remove(Integer.valueOf(spl[1]));
        });
        long remainingRecords = quays.entrySet().stream()
                .mapToLong(s -> s.getValue().getPassTimes().entrySet().size())
                .sum();
        LOGGER.debug("Removed {} journey records, {} records remain for {} stops", remove.size(), remainingRecords, quays.size());
    }

    private static Runnable getPlanningLoader(boolean initial) {
        return new CatchableRunnable(() -> new PlanningLoader().loadFiles(initial));
    }

    public static void stop() {
        backupTask.cancel(true);
        schedule.shutdownNow();
    }


    public static List<PassTime> getTimesForStop(String code) {
        return new ArrayList<>(quays.getOrDefault(code, new QuayStore()).getPassTimes().values());
    }

    public static List<UpdateMessage> getMessagesForStop(String code) {
        return new ArrayList<>(quays.getOrDefault(code, new QuayStore()).getMessages().values());
    }

    public static List<UpdateMessage> getAllMessages() { // TODO: Rename, this one is for debug
        return quays.entrySet().stream().flatMap(e -> e.getValue().getMessages().values().stream()).collect(Collectors.toList());
    }

    public static List<RealtimeMessage> getDataForQuay(List<String> quayCodes, boolean sendGeneralMessages) {
        Stream<RealtimeMessage> passTime = quayCodes.stream()
                .map(QuayDataProvider::getTimesForStop)
                .flatMap(Collection::stream)
                .map(lineDestProcess::process) // Add lines and destinations only when we send these messages out
                .map(PlanningMapper::setExpected); // Set expected to be target times
        Stream<RealtimeMessage> updateMessage = Stream.empty();
        if (sendGeneralMessages) {
            updateMessage = quayCodes.stream()
                    .map(QuayDataProvider::getMessagesForStop)
                    .flatMap(Collection::stream);
        }

        return Stream.concat(passTime, updateMessage)
                .collect(Collectors.toList());
    }

    public static synchronized void replace(List<PassTime> times) {
        // TODO: Do a delta here of already sent busses
        Set<String> rejected = times.stream()
                .filter(t -> t.getQuayCode() != null)
                .map(p -> p.getDataOwnerCode()+":"+p.getStopCode())
                .collect(Collectors.toSet());
        int[] records = { 0 };
        times.stream()
                .filter(t -> t.getQuayCode() != null)
                .collect(Collectors.groupingBy(PassTime::getQuayCode))
                .forEach((key, value) -> {
                        quays.put(key, new QuayStore());
                        Map<Integer, PassTime> out = quays.get(key).getPassTimes();
                        value.forEach(record -> {
                            if (!out.containsKey(record.getPassTimeHash())) {
                                out.put(record.getPassTimeHash(), record);
                                records[0] += 1;
                            } else {
                                PassTime current = out.get(record.getPassTimeHash());
                                if (record.getGeneratedTimestamp() > current.getGeneratedTimestamp()) {
                                    out.put(record.getPassTimeHash(), record);
                                }

                            }
                        });
                });
        LOGGER.info("Planning now contains {} records for {} stops, rejected {}", records[0], quays.size(), rejected.size());

        // TODO: Move this code elsewhere
        // TODO: This sends everything for two days, but needs to do a delta
        SubscriptionStore.getAllSystems().values().parallelStream().forEach(sub -> {
            List<RealtimeMessage> planning = QuayDataProvider.getDataForQuay(sub.getSubscribedQuayCodes(), false);
            if (times.size() > 0) {
                LOGGER.debug("New planning: got {} times to send to {}", times.size(), sub.getId());
                transport.sendMessage(TopicFactory.travelInformation(sub.getId()), DisplayDirectMessageFactory.fromRealTime(planning, sub), QoS.AT_LEAST_ONCE);
            }
        });

        MetricStore.getInstance().storeMetric("kv78turbo.planning.stops", quays.size());
        MetricStore.getInstance().storeMetric("kv78turbo.planning.rejected", rejected.size());
        MetricStore.getInstance().storeMetric("kv78turbo.planning.records", times.size());
        scheduleBackup();
    }

    public static synchronized void updatePassTime(PassTime passTime) {
        if (!quays.containsKey(passTime.getQuayCode())) {
            quays.put(passTime.getQuayCode(), new QuayStore());
        }
        // TODO: Out of order updates
        quays.get(passTime.getQuayCode()).getPassTimes().put(passTime.getPassTimeHash(), passTime);
    }

    public static synchronized void updateMessage(UpdateMessage message) {
        if (!quays.containsKey(message.getQuayCode())) {
            quays.put(message.getQuayCode(), new QuayStore());
        }
        // TODO: Out of order updates
        quays.get(message.getQuayCode()).getMessages().put(message.getMessageHash(), message);
    }

    public static synchronized void deleteMessage(DeleteMessage message) {
        if (!quays.containsKey(message.getQuayCode())) {
            quays.put(message.getQuayCode(), new QuayStore());
        }
        quays.get(message.getQuayCode()).getMessages().remove(message.getMessageHash());
    }

    private static void scheduleBackup() {
        backupTask = schedule.scheduleAtFixedRate(()-> {
            LOGGER.debug("Starting backupTask of planning data");
            writeFileSerialize(FILE_JOURNEYS, quays.values().stream().flatMap(h -> h.getPassTimes().values().stream()).collect(Collectors.toList()));
            writeFileSerialize(FILE_MESSAGES, quays.values().stream().flatMap(h -> h.getMessages().values().stream()).collect(Collectors.toList()));
            LOGGER.debug("Finished backupTask of planning data");
        }, 1, 15, TimeUnit.MINUTES);
    }
}
