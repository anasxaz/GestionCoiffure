package controller;

import static org.mockito.Mockito.*;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import model.Admin;
import model.Barber;
import model.Client;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import service.AuthenticationService;

@ExtendWith(MockitoExtension.class)
class LoginServletTest {

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private HttpSession session;

    @Mock
    private RequestDispatcher dispatcher;

    @Mock
    private AuthenticationService authService;

    @InjectMocks
    private LoginServlet servlet;

    @BeforeEach
    void setUp() {
        lenient().when(request.getRequestDispatcher(anyString())).thenReturn(dispatcher);
        lenient().when(request.getContextPath()).thenReturn("/gestionCoiffure");
        lenient().when(request.getSession()).thenReturn(session);
    }

    // --- doGet : forward vers login.jsp ---
    @Test
    void testDoGet_forwardsToLoginJsp() throws ServletException, IOException {
        servlet.doGet(request, response);

        verify(request).getRequestDispatcher("/WEB-INF/views/login.jsp");
        verify(dispatcher).forward(request, response);
    }

    // --- doPost : champs vides ---
    @Test
    void testDoPost_champsVides_affichError() throws ServletException, IOException {
        when(request.getParameter("email")).thenReturn("  ");
        when(request.getParameter("password")).thenReturn("pass");
        when(request.getParameter("userType")).thenReturn("client");

        servlet.doPost(request, response);

        verify(request).setAttribute("error", "Tous les champs sont requis");
        verify(dispatcher).forward(request, response);
    }

    // --- doPost : connexion admin réussie ---
    @Test
    void testDoPost_connexionAdminReussie_redirectDashboard() throws ServletException, IOException {
        Admin admin = new Admin();
        admin.setAdminId(1);
        admin.setName("Admin");

        when(request.getParameter("email")).thenReturn("admin@test.com");
        when(request.getParameter("password")).thenReturn("password123");
        when(request.getParameter("userType")).thenReturn("admin");
        when(authService.authenticateAdmin("admin@test.com", "password123")).thenReturn(admin);

        servlet.doPost(request, response);

        verify(session).setAttribute("user", admin);
        verify(session).setAttribute("userType", "admin");
        verify(session).setAttribute("userId", 1);
        verify(session).setAttribute("userName", "Admin");
        verify(response).sendRedirect("/gestionCoiffure/admin/dashboard");
    }

    // --- doPost : connexion barber réussie ---
    @Test
    void testDoPost_connexionBarberReussie_redirectDashboard() throws ServletException, IOException {
        Barber barber = new Barber();
        barber.setBarberId(2);
        barber.setName("Barber");

        when(request.getParameter("email")).thenReturn("barber@test.com");
        when(request.getParameter("password")).thenReturn("password123");
        when(request.getParameter("userType")).thenReturn("barber");
        when(authService.authenticateBarber("barber@test.com", "password123")).thenReturn(barber);

        servlet.doPost(request, response);

        verify(session).setAttribute("user", barber);
        verify(session).setAttribute("userType", "barber");
        verify(session).setAttribute("userId", 2);
        verify(session).setAttribute("userName", "Barber");
        verify(response).sendRedirect("/gestionCoiffure/barber/dashboard");
    }

    // --- doPost : connexion client réussie ---
    @Test
    void testDoPost_connexionClientReussie_redirectDashboard() throws ServletException, IOException {
        Client client = new Client();
        client.setClientId(3);
        client.setName("Jean");

        when(request.getParameter("email")).thenReturn("jean@test.com");
        when(request.getParameter("password")).thenReturn("password123");
        when(request.getParameter("userType")).thenReturn("client");
        when(authService.authenticateClient("jean@test.com", "password123")).thenReturn(client);

        servlet.doPost(request, response);

        verify(session).setAttribute("user", client);
        verify(session).setAttribute("userType", "client");
        verify(session).setAttribute("userId", 3);
        verify(session).setAttribute("userName", "Jean");
        verify(response).sendRedirect("/gestionCoiffure/client/dashboard");
    }

    // --- doPost : credentials invalides (auth retourne null) ---
    @Test
    void testDoPost_credentialsInvalides_affichError() throws ServletException, IOException {
        when(request.getParameter("email")).thenReturn("jean@test.com");
        when(request.getParameter("password")).thenReturn("wrongpass");
        when(request.getParameter("userType")).thenReturn("client");
        when(authService.authenticateClient("jean@test.com", "wrongpass")).thenReturn(null);

        servlet.doPost(request, response);

        verify(request).setAttribute("error", "Email ou mot de passe incorrect");
        verify(request).setAttribute("email", "jean@test.com");
        verify(request).setAttribute("userType", "client");
        verify(dispatcher).forward(request, response);
    }

    // --- doPost : type utilisateur inconnu (default du switch) ---
    @Test
    void testDoPost_typeUtilisateurInvalide_affichError() throws ServletException, IOException {
        when(request.getParameter("email")).thenReturn("test@test.com");
        when(request.getParameter("password")).thenReturn("password123");
        when(request.getParameter("userType")).thenReturn("unknown");

        servlet.doPost(request, response);

        verify(request).setAttribute("error", "Type d'utilisateur invalide");
        verify(dispatcher).forward(request, response);
    }

    // --- doPost : exception interne ---
    @Test
    void testDoPost_exceptionLorsDelaConnexion_affichError() throws ServletException, IOException {
        when(request.getParameter("email")).thenReturn("jean@test.com");
        when(request.getParameter("password")).thenReturn("password123");
        when(request.getParameter("userType")).thenReturn("client");
        when(authService.authenticateClient(anyString(), anyString())).thenThrow(new RuntimeException("DB error"));

        servlet.doPost(request, response);

        verify(request).setAttribute("error", "Erreur lors de la connexion. Veuillez réessayer.");
        verify(dispatcher).forward(request, response);
    }
}
