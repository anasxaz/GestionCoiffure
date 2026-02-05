package model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class AppointmentTest {

    @Test
    void testConstructeurParDefaut() {
        Appointment apt = new Appointment();

        assertEquals(0, apt.getAppointmentId());
        assertEquals(0, apt.getClientId());
        assertEquals(0, apt.getBarberId());
        assertEquals(0, apt.getServiceId());
        assertNull(apt.getDate());
        assertNull(apt.getStartTime());
        assertNull(apt.getEndTime());
        assertNull(apt.getStatus());
        assertNull(apt.getRedemptionId());
        assertNull(apt.getCreatedAt());
        assertNull(apt.getUpdatedAt());
        assertNull(apt.getCancellationReason());
        assertNull(apt.getClientName());
        assertNull(apt.getBarberName());
        assertNull(apt.getServiceName());
        assertNull(apt.getServicePrice());
        assertNull(apt.getFinalPrice());
    }

    @Test
    void testConstructeurParametres() {
        LocalDate date = LocalDate.of(2026, 3, 15);
        LocalTime start = LocalTime.of(9, 0);
        LocalTime end = LocalTime.of(10, 0);
        LocalDateTime created = LocalDateTime.now();
        LocalDateTime updated = created.plusHours(1);

        Appointment apt = new Appointment(1, 10, 2, 3, date, start, end, "pending", created, updated, "Annulé");

        assertEquals(1, apt.getAppointmentId());
        assertEquals(10, apt.getClientId());
        assertEquals(2, apt.getBarberId());
        assertEquals(3, apt.getServiceId());
        assertEquals(date, apt.getDate());
        assertEquals(start, apt.getStartTime());
        assertEquals(end, apt.getEndTime());
        assertEquals("pending", apt.getStatus());
        assertEquals(created, apt.getCreatedAt());
        assertEquals(updated, apt.getUpdatedAt());
        assertEquals("Annulé", apt.getCancellationReason());
        // champs hors constructeur restent null
        assertNull(apt.getRedemptionId());
        assertNull(apt.getClientName());
        assertNull(apt.getBarberName());
        assertNull(apt.getServiceName());
        assertNull(apt.getServicePrice());
        assertNull(apt.getFinalPrice());
    }

    @Test
    void testGettersSetters() {
        Appointment apt = new Appointment();
        LocalDate date = LocalDate.of(2026, 1, 15);
        LocalTime start = LocalTime.of(14, 0);
        LocalTime end = LocalTime.of(15, 0);
        LocalDateTime now = LocalDateTime.now();

        apt.setAppointmentId(5);
        apt.setClientId(20);
        apt.setBarberId(3);
        apt.setServiceId(7);
        apt.setDate(date);
        apt.setStartTime(start);
        apt.setEndTime(end);
        apt.setStatus("confirmed");
        apt.setRedemptionId(99);
        apt.setCreatedAt(now);
        apt.setUpdatedAt(now);
        apt.setCancellationReason("Indisponible");
        apt.setClientName("Jean");
        apt.setBarberName("Paul");
        apt.setServiceName("Coupe classique");
        apt.setServicePrice(25.50);
        apt.setFinalPrice(20.00);

        assertEquals(5, apt.getAppointmentId());
        assertEquals(20, apt.getClientId());
        assertEquals(3, apt.getBarberId());
        assertEquals(7, apt.getServiceId());
        assertEquals(date, apt.getDate());
        assertEquals(start, apt.getStartTime());
        assertEquals(end, apt.getEndTime());
        assertEquals("confirmed", apt.getStatus());
        assertEquals(99, apt.getRedemptionId());
        assertEquals(now, apt.getCreatedAt());
        assertEquals(now, apt.getUpdatedAt());
        assertEquals("Indisponible", apt.getCancellationReason());
        assertEquals("Jean", apt.getClientName());
        assertEquals("Paul", apt.getBarberName());
        assertEquals("Coupe classique", apt.getServiceName());
        assertEquals(25.50, apt.getServicePrice());
        assertEquals(20.00, apt.getFinalPrice());
    }

    @Test
    void testRedemptionId_null() {
        Appointment apt = new Appointment();
        assertNull(apt.getRedemptionId());

        apt.setRedemptionId(42);
        assertEquals(42, apt.getRedemptionId());

        apt.setRedemptionId(null);
        assertNull(apt.getRedemptionId());
    }

    // --- statuts booléens ---
    @Test
    void testIsPending() {
        Appointment apt = new Appointment();
        apt.setStatus("pending");

        assertTrue(apt.isPending());
        assertFalse(apt.isConfirmed());
        assertFalse(apt.isCancelled());
        assertFalse(apt.isCompleted());
    }

    @Test
    void testIsConfirmed() {
        Appointment apt = new Appointment();
        apt.setStatus("confirmed");

        assertFalse(apt.isPending());
        assertTrue(apt.isConfirmed());
        assertFalse(apt.isCancelled());
        assertFalse(apt.isCompleted());
    }

    @Test
    void testIsCancelled() {
        Appointment apt = new Appointment();
        apt.setStatus("cancelled");

        assertFalse(apt.isPending());
        assertFalse(apt.isConfirmed());
        assertTrue(apt.isCancelled());
        assertFalse(apt.isCompleted());
    }

    @Test
    void testIsCompleted() {
        Appointment apt = new Appointment();
        apt.setStatus("completed");

        assertFalse(apt.isPending());
        assertFalse(apt.isConfirmed());
        assertFalse(apt.isCancelled());
        assertTrue(apt.isCompleted());
    }

    @Test
    void testStatut_inconnu_toutFalse() {
        Appointment apt = new Appointment();
        apt.setStatus("unknown");

        assertFalse(apt.isPending());
        assertFalse(apt.isConfirmed());
        assertFalse(apt.isCancelled());
        assertFalse(apt.isCompleted());
    }

    @Test
    void testToString() {
        Appointment apt = new Appointment();
        apt.setAppointmentId(1);
        apt.setClientId(10);
        apt.setBarberId(2);
        apt.setServiceId(3);
        apt.setStatus("pending");
        apt.setDate(LocalDate.of(2026, 3, 15));
        apt.setStartTime(LocalTime.of(9, 0));

        String str = apt.toString();

        assertTrue(str.contains("appointmentId=1"));
        assertTrue(str.contains("clientId=10"));
        assertTrue(str.contains("barberId=2"));
        assertTrue(str.contains("status='pending'"));
    }
}
