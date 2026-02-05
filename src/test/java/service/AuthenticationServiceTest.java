package service;

import dao.AdminDAO;
import dao.BarberDAO;
import dao.ClientDAO;
import model.Admin;
import model.Barber;
import model.Client;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import util.PasswordUtil;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthenticationServiceTest {

    @Mock
    private AdminDAO adminDAO;

    @Mock
    private BarberDAO barberDAO;

    @Mock
    private ClientDAO clientDAO;

    @InjectMocks
    private AuthenticationService authService;

    @Test
    void testAuthenticateAdmin_success() {
        Admin admin = new Admin(1, "Admin", "admin@test.com", "hashedPassword", null);
        when(adminDAO.findByEmail("admin@test.com")).thenReturn(admin);

        try (MockedStatic<PasswordUtil> passwordUtil = mockStatic(PasswordUtil.class)) {
            passwordUtil.when(() -> PasswordUtil.verifyPassword("password123", "hashedPassword"))
                       .thenReturn(true);

            Admin result = authService.authenticateAdmin("admin@test.com", "password123");

            assertNotNull(result);
            assertEquals(1, result.getAdminId());
            assertEquals("admin@test.com", result.getEmail());
            verify(adminDAO).findByEmail("admin@test.com");
        }
    }

    @Test
    void testAuthenticateAdmin_wrongPassword() {
        Admin admin = new Admin(1, "Admin", "admin@test.com", "hashedPassword", null);
        when(adminDAO.findByEmail("admin@test.com")).thenReturn(admin);

        try (MockedStatic<PasswordUtil> passwordUtil = mockStatic(PasswordUtil.class)) {
            passwordUtil.when(() -> PasswordUtil.verifyPassword("wrongPassword", "hashedPassword"))
                       .thenReturn(false);

            Admin result = authService.authenticateAdmin("admin@test.com", "wrongPassword");

            assertNull(result);
            verify(adminDAO).findByEmail("admin@test.com");
        }
    }

    @Test
    void testAuthenticateAdmin_emailNotFound() {
        when(adminDAO.findByEmail("notfound@test.com")).thenReturn(null);

        Admin result = authService.authenticateAdmin("notfound@test.com", "password123");

        assertNull(result);
        verify(adminDAO).findByEmail("notfound@test.com");
    }

    @Test
    void testAuthenticateBarber_success_active() {
        Barber barber = new Barber(1, "Paul", "paul@test.com", "hashedPassword", "0600000001", "Expert", "active", null);
        when(barberDAO.findByEmail("paul@test.com")).thenReturn(barber);

        try (MockedStatic<PasswordUtil> passwordUtil = mockStatic(PasswordUtil.class)) {
            passwordUtil.when(() -> PasswordUtil.verifyPassword("password123", "hashedPassword"))
                       .thenReturn(true);

            Barber result = authService.authenticateBarber("paul@test.com", "password123");

            assertNotNull(result);
            assertEquals(1, result.getBarberId());
            assertEquals("active", result.getStatus());
            verify(barberDAO).findByEmail("paul@test.com");
        }
    }

    @Test
    void testAuthenticateBarber_inactive() {
        Barber barber = new Barber(1, "Paul", "paul@test.com", "hashedPassword", "0600000001", "Expert", "inactive", null);
        when(barberDAO.findByEmail("paul@test.com")).thenReturn(barber);

        try (MockedStatic<PasswordUtil> passwordUtil = mockStatic(PasswordUtil.class)) {
            passwordUtil.when(() -> PasswordUtil.verifyPassword("password123", "hashedPassword"))
                       .thenReturn(true);

            Barber result = authService.authenticateBarber("paul@test.com", "password123");

            assertNull(result);
            verify(barberDAO).findByEmail("paul@test.com");
        }
    }

    @Test
    void testAuthenticateBarber_wrongPassword() {
        Barber barber = new Barber(1, "Paul", "paul@test.com", "hashedPassword", "0600000001", "Expert", "active", null);
        when(barberDAO.findByEmail("paul@test.com")).thenReturn(barber);

        try (MockedStatic<PasswordUtil> passwordUtil = mockStatic(PasswordUtil.class)) {
            passwordUtil.when(() -> PasswordUtil.verifyPassword("wrongPassword", "hashedPassword"))
                       .thenReturn(false);

            Barber result = authService.authenticateBarber("paul@test.com", "wrongPassword");

            assertNull(result);
            verify(barberDAO).findByEmail("paul@test.com");
        }
    }

    @Test
    void testAuthenticateClient_success() {
        Client client = new Client(1, "Jean", "jean@test.com", "hashedPassword", "0600000001", 0, "standard", null);
        when(clientDAO.findByEmail("jean@test.com")).thenReturn(client);

        try (MockedStatic<PasswordUtil> passwordUtil = mockStatic(PasswordUtil.class)) {
            passwordUtil.when(() -> PasswordUtil.verifyPassword("password123", "hashedPassword"))
                       .thenReturn(true);

            Client result = authService.authenticateClient("jean@test.com", "password123");

            assertNotNull(result);
            assertEquals(1, result.getClientId());
            assertEquals("jean@test.com", result.getEmail());
            verify(clientDAO).findByEmail("jean@test.com");
        }
    }

    @Test
    void testAuthenticateClient_wrongPassword() {
        Client client = new Client(1, "Jean", "jean@test.com", "hashedPassword", "0600000001", 0, "standard", null);
        when(clientDAO.findByEmail("jean@test.com")).thenReturn(client);

        try (MockedStatic<PasswordUtil> passwordUtil = mockStatic(PasswordUtil.class)) {
            passwordUtil.when(() -> PasswordUtil.verifyPassword("wrongPassword", "hashedPassword"))
                       .thenReturn(false);

            Client result = authService.authenticateClient("jean@test.com", "wrongPassword");

            assertNull(result);
            verify(clientDAO).findByEmail("jean@test.com");
        }
    }

    @Test
    void testRegisterClient_success() {
        when(clientDAO.findByEmail("newuser@test.com")).thenReturn(null);
        when(clientDAO.create(any(Client.class))).thenReturn(true);

        try (MockedStatic<PasswordUtil> passwordUtil = mockStatic(PasswordUtil.class)) {
            passwordUtil.when(() -> PasswordUtil.isValidPassword("password123"))
                       .thenReturn(true);
            passwordUtil.when(() -> PasswordUtil.hashPassword("password123"))
                       .thenReturn("hashedPassword");

            boolean result = authService.registerClient("NewUser", "newuser@test.com", "password123", "0600000001");

            assertTrue(result);
            verify(clientDAO).findByEmail("newuser@test.com");
            verify(clientDAO).create(any(Client.class));
        }
    }

    @Test
    void testRegisterClient_emailExists() {
        when(clientDAO.findByEmail("existing@test.com")).thenReturn(new Client());

        try (MockedStatic<PasswordUtil> passwordUtil = mockStatic(PasswordUtil.class)) {
            passwordUtil.when(() -> PasswordUtil.isValidPassword("password123"))
                       .thenReturn(true);

            boolean result = authService.registerClient("User", "existing@test.com", "password123", "0600000001");

            assertFalse(result);
            verify(clientDAO).findByEmail("existing@test.com");
            verify(clientDAO, never()).create(any(Client.class));
        }
    }

    @Test
    void testRegisterClient_invalidPassword() {
        when(clientDAO.findByEmail("user@test.com")).thenReturn(null);

        try (MockedStatic<PasswordUtil> passwordUtil = mockStatic(PasswordUtil.class)) {
            passwordUtil.when(() -> PasswordUtil.isValidPassword("123"))
                       .thenReturn(false);

            boolean result = authService.registerClient("User", "user@test.com", "123", "0600000001");

            assertFalse(result);
            verify(clientDAO).findByEmail("user@test.com");
            verify(clientDAO, never()).create(any(Client.class));
        }
    }

    @Test
    void testEmailExists_adminExists() {
        when(adminDAO.findByEmail("admin@test.com")).thenReturn(new Admin());
        lenient().when(barberDAO.findByEmail("admin@test.com")).thenReturn(null);
        lenient().when(clientDAO.findByEmail("admin@test.com")).thenReturn(null);

        boolean result = authService.emailExists("admin@test.com");

        assertTrue(result);
        verify(adminDAO).findByEmail("admin@test.com");
    }

    @Test
    void testEmailExists_barberExists() {
        when(adminDAO.findByEmail("barber@test.com")).thenReturn(null);
        when(barberDAO.findByEmail("barber@test.com")).thenReturn(new Barber());
        lenient().when(clientDAO.findByEmail("barber@test.com")).thenReturn(null);

        boolean result = authService.emailExists("barber@test.com");

        assertTrue(result);
        verify(barberDAO).findByEmail("barber@test.com");
    }

    @Test
    void testEmailExists_clientExists() {
        when(adminDAO.findByEmail("client@test.com")).thenReturn(null);
        when(barberDAO.findByEmail("client@test.com")).thenReturn(null);
        when(clientDAO.findByEmail("client@test.com")).thenReturn(new Client());

        boolean result = authService.emailExists("client@test.com");

        assertTrue(result);
        verify(clientDAO).findByEmail("client@test.com");
    }

    @Test
    void testEmailExists_noMatch() {
        when(adminDAO.findByEmail("notfound@test.com")).thenReturn(null);
        when(barberDAO.findByEmail("notfound@test.com")).thenReturn(null);
        when(clientDAO.findByEmail("notfound@test.com")).thenReturn(null);

        boolean result = authService.emailExists("notfound@test.com");

        assertFalse(result);
        verify(adminDAO).findByEmail("notfound@test.com");
        verify(barberDAO).findByEmail("notfound@test.com");
        verify(clientDAO).findByEmail("notfound@test.com");
    }
}
