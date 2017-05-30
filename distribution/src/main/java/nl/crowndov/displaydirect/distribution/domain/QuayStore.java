package nl.crowndov.displaydirect.distribution.domain;

import nl.crowndov.displaydirect.distribution.domain.travelinfo.PassTime;
import nl.crowndov.displaydirect.distribution.domain.travelinfo.UpdateMessage;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class QuayStore {

    private Map<Integer, PassTime> passTimes = new ConcurrentHashMap<>();
    private Map<Integer, UpdateMessage> messages = new ConcurrentHashMap<>();

    public Map<Integer, PassTime> getPassTimes() {
        return passTimes;
    }

    public Map<Integer, UpdateMessage> getMessages() {
        return messages;
    }
}