package model;

import java.time.LocalTime;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class AvailabilityTest {

    @Test
    void testConstructeurParDefaut() {
        Availability avail = new Availability();

        assertEquals(0, avail.getAvailabilityId());
        assertEquals(0, avail.getBarberId());
        assertNull(avail.getDayOfWeek());
        assertNull(avail.getStartTime());
        assertNull(avail.getEndTime());
        assertNull(avail.getBarberName());
    }

    @Test
    void testConstructeurParametres() {
        LocalTime start = LocalTime.of(9, 0);
        LocalTime end = LocalTime.of(17, 0);

        Availability avail = new Availability(1, 2, "Mon", start, end);

        assertEquals(1, avail.getAvailabilityId());
        assertEquals(2, avail.getBarberId());
        assertEquals("Mon", avail.getDayOfWeek());
        assertEquals(start, avail.getStartTime());
        assertEquals(end, avail.getEndTime());
        // barberName n'est pas dans le constructeur
        assertNull(avail.getBarberName());
    }

    @Test
    void testGettersSetters() {
        Availability avail = new Availability();
        LocalTime start = LocalTime.of(10, 30);
        LocalTime end = LocalTime.of(18, 0);

        avail.setAvailabilityId(5);
        avail.setBarberId(3);
        avail.setDayOfWeek("Fri");
        avail.setStartTime(start);
        avail.setEndTime(end);
        avail.setBarberName("Paul");

        assertEquals(5, avail.getAvailabilityId());
        assertEquals(3, avail.getBarberId());
        assertEquals("Fri", avail.getDayOfWeek());
        assertEquals(start, avail.getStartTime());
        assertEquals(end, avail.getEndTime());
        assertEquals("Paul", avail.getBarberName());
    }

    @Test
    void testToString() {
        LocalTime start = LocalTime.of(9, 0);
        LocalTime end = LocalTime.of(17, 0);
        Availability avail = new Availability(1, 2, "Mon", start, end);

        String str = avail.toString();

        assertTrue(str.contains("availabilityId=1"));
        assertTrue(str.contains("barberId=2"));
        assertTrue(str.contains("dayOfWeek='Mon'"));
        assertTrue(str.contains("startTime=09:00"));
        assertTrue(str.contains("endTime=17:00"));
    }
}
