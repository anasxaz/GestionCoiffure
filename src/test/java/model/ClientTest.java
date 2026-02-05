package model;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ClientTest {

    @Test
    void testConstructeurParDefaut() {
        Client client = new Client();

        assertEquals(0, client.getClientId());
        assertNull(client.getName());
        assertNull(client.getEmail());
        assertNull(client.getPasswordHash());
        assertNull(client.getPhone());
        assertEquals(0, client.getPointsBalance());
        assertNull(client.getLoyaltyStatus());
        assertNull(client.getCreatedAt());
    }

    @Test
    void testConstructeurParametres() {
        LocalDateTime now = LocalDateTime.now();
        Client client = new Client(1, "Jean", "jean@test.com", "hash", "0600000001", 150, "fidele", now);

        assertEquals(1, client.getClientId());
        assertEquals("Jean", client.getName());
        assertEquals("jean@test.com", client.getEmail());
        assertEquals("hash", client.getPasswordHash());
        assertEquals("0600000001", client.getPhone());
        assertEquals(150, client.getPointsBalance());
        assertEquals("fidele", client.getLoyaltyStatus());
        assertEquals(now, client.getCreatedAt());
    }

    @Test
    void testGettersSetters() {
        Client client = new Client();
        LocalDateTime now = LocalDateTime.now();

        client.setClientId(10);
        client.setName("Marie");
        client.setEmail("marie@test.com");
        client.setPasswordHash("newHash");
        client.setPhone("0600000002");
        client.setPointsBalance(500);
        client.setLoyaltyStatus("standard");
        client.setCreatedAt(now);

        assertEquals(10, client.getClientId());
        assertEquals("Marie", client.getName());
        assertEquals("marie@test.com", client.getEmail());
        assertEquals("newHash", client.getPasswordHash());
        assertEquals("0600000002", client.getPhone());
        assertEquals(500, client.getPointsBalance());
        assertEquals("standard", client.getLoyaltyStatus());
        assertEquals(now, client.getCreatedAt());
    }

    // --- isFidele() ---
    @Test
    void testIsFidele_statusFidele() {
        Client client = new Client();
        client.setLoyaltyStatus("fidele");

        assertTrue(client.isFidele());
    }

    @Test
    void testIsFidele_statusStandard() {
        Client client = new Client();
        client.setLoyaltyStatus("standard");

        assertFalse(client.isFidele());
    }

    @Test
    void testIsFidele_statusNull() {
        Client client = new Client();
        client.setLoyaltyStatus(null);

        assertFalse(client.isFidele());
    }

    @Test
    void testIsFidele_statusAutre() {
        Client client = new Client();
        client.setLoyaltyStatus("premium");

        assertFalse(client.isFidele());
    }

    @Test
    void testToString() {
        Client client = new Client(1, "Jean", "jean@test.com", "hash", "0600000001", 150, "fidele", LocalDateTime.now());

        String str = client.toString();

        assertTrue(str.contains("clientId=1"));
        assertTrue(str.contains("name='Jean'"));
        assertTrue(str.contains("email='jean@test.com'"));
        assertTrue(str.contains("pointsBalance=150"));
        assertTrue(str.contains("loyaltyStatus='fidele'"));
    }
}
