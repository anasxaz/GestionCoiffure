package model;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class AdminTest {

    @Test
    void testConstructeurParDefaut() {
        Admin admin = new Admin();

        assertEquals(0, admin.getAdminId());
        assertNull(admin.getName());
        assertNull(admin.getEmail());
        assertNull(admin.getPasswordHash());
        assertNull(admin.getCreatedAt());
    }

    @Test
    void testConstructeurParametres() {
        LocalDateTime now = LocalDateTime.now();
        Admin admin = new Admin(1, "Jean", "jean@test.com", "hashedPwd", now);

        assertEquals(1, admin.getAdminId());
        assertEquals("Jean", admin.getName());
        assertEquals("jean@test.com", admin.getEmail());
        assertEquals("hashedPwd", admin.getPasswordHash());
        assertEquals(now, admin.getCreatedAt());
    }

    @Test
    void testGettersSetters() {
        Admin admin = new Admin();
        LocalDateTime now = LocalDateTime.now();

        admin.setAdminId(42);
        admin.setName("Marie");
        admin.setEmail("marie@test.com");
        admin.setPasswordHash("newHash");
        admin.setCreatedAt(now);

        assertEquals(42, admin.getAdminId());
        assertEquals("Marie", admin.getName());
        assertEquals("marie@test.com", admin.getEmail());
        assertEquals("newHash", admin.getPasswordHash());
        assertEquals(now, admin.getCreatedAt());
    }

    @Test
    void testToString() {
        Admin admin = new Admin(1, "Jean", "jean@test.com", "hash", LocalDateTime.now());
        String str = admin.toString();

        assertTrue(str.contains("adminId=1"));
        assertTrue(str.contains("name='Jean'"));
        assertTrue(str.contains("email='jean@test.com'"));
    }
}
