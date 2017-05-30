package nl.crowndov.displaydirect.distribution.input;

import nl.crowndov.displaydirect.distribution.domain.travelinfo.*;
import nl.crowndov.displaydirect.distribution.util.AbstractService;
import org.junit.Assert;
import org.junit.Test;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class QuayDataProviderTest {

    private class TestService extends AbstractService {
        public void write(List<PassTime> list) {
            writeFileSerialize("test_planning", list);
        }

        public Optional<List<PassTime>> read() {
            return readSerializedFile("test_planning");
        }
    }

    @Test
    public void testSerializeDeserializePassTime() {
        PassTime p1 = new PassTime("CXX", LocalDate.of(2017, 5, 20), "M001", 101, "3001000");
        Line l1 = new Line("M001", "1", TransportType.BUS);
        p1.setLine(l1);
        p1.setTargetArrivalTime(1495269885);
        p1.setTargetDepartureTime(1495269800);
        p1.setExpectedArrivalTime(1495269800);
        p1.setExpectedDepartureTime(1495269885);
        p1.setNumberOfCoaches(2);
        p1.setTripStopStatus(PassTime.Status.ARRIVED);
        p1.setWheelchairAccessible(true);
        p1.setDestinationCode("A11");
        Destination d1 = new Destination("A11", new DestinationValue("a", "a", null, null), null);
        p1.setDestination(d1);
        p1.setSideCode("A");
        p1.setQuayCode("NL:Q:10000");

        PassTime p2 = new PassTime("RET", LocalDate.of(2017, 5, 20), "M1", 101, "HA1001");
        List<PassTime> serialize = Arrays.asList(p1, p2);

        TestService t = new TestService();
        t.write(serialize);

        Optional<List<PassTime>> data = t.read();
        Assert.assertTrue(data.isPresent());
        // Constructor
        Assert.assertEquals(p1, data.get().get(0));
        Assert.assertEquals(p2, data.get().get(1));



    }

}