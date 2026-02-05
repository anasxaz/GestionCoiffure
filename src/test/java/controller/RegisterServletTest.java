package controller;

import static org.mockito.Mockito.*;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import service.AuthenticationService;

@ExtendWith(MockitoExtension.class)
class RegisterServletTest {

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private RequestDispatcher dispatcher;

    @Mock
    private AuthenticationService authService;

    @InjectMocks
    private RegisterServlet servlet;

    @BeforeEach
    void setUp() {
        lenient().when(request.getRequestDispatcher(anyString())).thenReturn(dispatcher);
        lenient().when(request.getContextPath()).thenReturn("/gestionCoiffure");
    }

    // --- doGet : forward vers register.jsp ---
    @Test
    void testDoGet_forwardsToRegisterJsp() throws ServletException, IOException {
        servlet.doGet(request, response);

        verify(request).getRequestDispatcher("/WEB-INF/views/register.jsp");
        verify(dispatcher).forward(request, response);
    }

    // --- doPost : champs obligatoires manquants ---
    @Test
    void testDoPost_champsVides_affichError() throws ServletException, IOException {
        when(request.getParameter("name")).thenReturn("  ");
        when(request.getParameter("email")).thenReturn("test@test.com");
        when(request.getParameter("password")).thenReturn("password123");
        when(request.getParameter("confirmPassword")).thenReturn("password123");
        when(request.getParameter("phone")).thenReturn("0600000000");

        servlet.doPost(request, response);

        verify(request).setAttribute("error", "Tous les champs sont requis");
        verify(dispatcher).forward(request, response);
    }

    @Test
    void testDoPost_emptyEmail_showsError() throws ServletException, IOException {
        when(request.getParameter("name")).thenReturn("Jean");
        when(request.getParameter("email")).thenReturn("  ");
        when(request.getParameter("password")).thenReturn("password123");
        when(request.getParameter("confirmPassword")).thenReturn("password123");
        when(request.getParameter("phone")).thenReturn("0600000000");

        servlet.doPost(request, response);

        verify(request).setAttribute("error", "Tous les champs sont requis");
        verify(dispatcher).forward(request, response);
    }

    @Test
    void testDoPost_emptyPassword_showsError() throws ServletException, IOException {
        when(request.getParameter("name")).thenReturn("Jean");
        when(request.getParameter("email")).thenReturn("jean@test.com");
        when(request.getParameter("password")).thenReturn("  ");
        when(request.getParameter("confirmPassword")).thenReturn("  "); // Confirm password also empty to avoid mismatch error
        when(request.getParameter("phone")).thenReturn("0600000000");

        servlet.doPost(request, response);

        verify(request).setAttribute("error", "Tous les champs sont requis");
        verify(dispatcher).forward(request, response);
    }

    @Test
    void testDoPost_emptyPhone_showsError() throws ServletException, IOException {
        when(request.getParameter("name")).thenReturn("Jean");
        when(request.getParameter("email")).thenReturn("jean@test.com");
        when(request.getParameter("password")).thenReturn("password123");
        when(request.getParameter("confirmPassword")).thenReturn("password123");
        when(request.getParameter("phone")).thenReturn("  ");

        servlet.doPost(request, response);

        verify(request).setAttribute("error", "Tous les champs sont requis");
        verify(dispatcher).forward(request, response);
    }

    // --- doPost : format email invalide ---
    @Test
    void testDoPost_emailInvalide_affichError() throws ServletException, IOException {
        when(request.getParameter("name")).thenReturn("Jean");
        when(request.getParameter("email")).thenReturn("invalidemail");
        when(request.getParameter("password")).thenReturn("password123");
        when(request.getParameter("confirmPassword")).thenReturn("password123");
        when(request.getParameter("phone")).thenReturn("0600000000");

        servlet.doPost(request, response);

        verify(request).setAttribute("error", "Format d'email invalide");
        verify(dispatcher).forward(request, response);
    }

    // --- doPost : mot de passe trop court (< 6 caractères, vérifié par PasswordUtil.isValidPassword) ---
    @Test
    void testDoPost_motDePasseTropCourt_affichError() throws ServletException, IOException {
        when(request.getParameter("name")).thenReturn("Jean");
        when(request.getParameter("email")).thenReturn("jean@test.com");
        when(request.getParameter("password")).thenReturn("abc");
        when(request.getParameter("confirmPassword")).thenReturn("abc");
        when(request.getParameter("phone")).thenReturn("0600000000");

        servlet.doPost(request, response);

        verify(request).setAttribute("error", "Le mot de passe doit contenir au moins 6 caractères");
        verify(dispatcher).forward(request, response);
    }

    // --- doPost : mots de passe ne correspondent pas ---
    @Test
    void testDoPost_motDePasseNemCorrespond_affichError() throws ServletException, IOException {
        when(request.getParameter("name")).thenReturn("Jean");
        when(request.getParameter("email")).thenReturn("jean@test.com");
        when(request.getParameter("password")).thenReturn("password123");
        when(request.getParameter("confirmPassword")).thenReturn("differentpass");
        when(request.getParameter("phone")).thenReturn("0600000000");

        servlet.doPost(request, response);

        verify(request).setAttribute("error", "Les mots de passe ne correspondent pas");
        verify(dispatcher).forward(request, response);
    }

    // --- doPost : email déjà utilisé ---
    @Test
    void testDoPost_emailDejaUtilise_affichError() throws ServletException, IOException {
        when(request.getParameter("name")).thenReturn("Jean");
        when(request.getParameter("email")).thenReturn("jean@test.com");
        when(request.getParameter("password")).thenReturn("password123");
        when(request.getParameter("confirmPassword")).thenReturn("password123");
        when(request.getParameter("phone")).thenReturn("0600000000");
        when(authService.emailExists("jean@test.com")).thenReturn(true);

        servlet.doPost(request, response);

        verify(request).setAttribute("error", "Cet email est déjà utilisé");
        verify(dispatcher).forward(request, response);
    }

    // --- doPost : inscription réussie → redirect ---
    @Test
    void testDoPost_inscriptionReussie_redirect() throws ServletException, IOException {
        when(request.getParameter("name")).thenReturn("Jean");
        when(request.getParameter("email")).thenReturn("jean@test.com");
        when(request.getParameter("password")).thenReturn("password123");
        when(request.getParameter("confirmPassword")).thenReturn("password123");
        when(request.getParameter("phone")).thenReturn("0600000000");
        when(authService.emailExists("jean@test.com")).thenReturn(false);
        when(authService.registerClient("Jean", "jean@test.com", "password123", "0600000000")).thenReturn(true);

        servlet.doPost(request, response);

        verify(response).sendRedirect("/gestionCoiffure/login?registration=success");
    }

    // --- doPost : registerClient retourne false ---
    @Test
    void testDoPost_inscriptionEchouee_affichError() throws ServletException, IOException {
        when(request.getParameter("name")).thenReturn("Jean");
        when(request.getParameter("email")).thenReturn("jean@test.com");
        when(request.getParameter("password")).thenReturn("password123");
        when(request.getParameter("confirmPassword")).thenReturn("password123");
        when(request.getParameter("phone")).thenReturn("0600000000");
        when(authService.emailExists("jean@test.com")).thenReturn(false);
        when(authService.registerClient("Jean", "jean@test.com", "password123", "0600000000")).thenReturn(false);

        servlet.doPost(request, response);

        verify(request).setAttribute("error", "Erreur lors de l'inscription. Veuillez réessayer.");
        verify(dispatcher).forward(request, response);
    }

    // --- doPost : exception interne ---
    @Test
    void testDoPost_exception_affichError() throws ServletException, IOException {
        when(request.getParameter("name")).thenReturn("Jean");
        when(request.getParameter("email")).thenReturn("jean@test.com");
        when(request.getParameter("password")).thenReturn("password123");
        when(request.getParameter("confirmPassword")).thenReturn("password123");
        when(request.getParameter("phone")).thenReturn("0600000000");
        when(authService.emailExists("jean@test.com")).thenReturn(false);
        when(authService.registerClient(anyString(), anyString(), anyString(), anyString()))
                .thenThrow(new RuntimeException("DB error"));

        servlet.doPost(request, response);

        verify(request).setAttribute("error", "Erreur lors de l'inscription. Veuillez réessayer.");
        verify(dispatcher).forward(request, response);
    }
}
