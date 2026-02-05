package model;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class BarberTest {

    @Test
    void testConstructeurParDefaut() {
        Barber barber = new Barber();

        assertEquals(0, barber.getBarberId());
        assertNull(barber.getName());
        assertNull(barber.getEmail());
        assertNull(barber.getPasswordHash());
        assertNull(barber.getPhone());
        assertNull(barber.getBio());
        assertNull(barber.getStatus());
        assertNull(barber.getCreatedAt());
    }

    @Test
    void testConstructeurParametres() {
        LocalDateTime now = LocalDateTime.now();
        Barber barber = new Barber(1, "Paul", "paul@test.com", "hash", "0600000001", "Expert coiffeur", "active", now);

        assertEquals(1, barber.getBarberId());
        assertEquals("Paul", barber.getName());
        assertEquals("paul@test.com", barber.getEmail());
        assertEquals("hash", barber.getPasswordHash());
        assertEquals("0600000001", barber.getPhone());
        assertEquals("Expert coiffeur", barber.getBio());
        assertEquals("active", barber.getStatus());
        assertEquals(now, barber.getCreatedAt());
    }

    @Test
    void testGettersSetters() {
        Barber barber = new Barber();
        LocalDateTime now = LocalDateTime.now();

        barber.setBarberId(42);
        barber.setName("Marie");
        barber.setEmail("marie@test.com");
        barber.setPasswordHash("newHash");
        barber.setPhone("0600000002");
        barber.setBio("Nouvelle bio");
        barber.setStatus("inactive");
        barber.setCreatedAt(now);

        assertEquals(42, barber.getBarberId());
        assertEquals("Marie", barber.getName());
        assertEquals("marie@test.com", barber.getEmail());
        assertEquals("newHash", barber.getPasswordHash());
        assertEquals("0600000002", barber.getPhone());
        assertEquals("Nouvelle bio", barber.getBio());
        assertEquals("inactive", barber.getStatus());
        assertEquals(now, barber.getCreatedAt());
    }

    // --- isActive() ---
    @Test
    void testIsActive_statusActive() {
        Barber barber = new Barber();
        barber.setStatus("active");

        assertTrue(barber.isActive());
    }

    @Test
    void testIsActive_statusInactive() {
        Barber barber = new Barber();
        barber.setStatus("inactive");

        assertFalse(barber.isActive());
    }

    @Test
    void testIsActive_statusNull() {
        Barber barber = new Barber();
        barber.setStatus(null);

        assertFalse(barber.isActive());
    }

    @Test
    void testIsActive_statusAutre() {
        Barber barber = new Barber();
        barber.setStatus("suspendu");

        assertFalse(barber.isActive());
    }

    @Test
    void testToString() {
        Barber barber = new Barber(1, "Paul", "paul@test.com", "hash", "0600000001", "Expert", "active", LocalDateTime.now());

        String str = barber.toString();

        assertTrue(str.contains("barberId=1"));
        assertTrue(str.contains("name='Paul'"));
        assertTrue(str.contains("email='paul@test.com'"));
        assertTrue(str.contains("phone='0600000001'"));
        assertTrue(str.contains("status='active'"));
    }
}
