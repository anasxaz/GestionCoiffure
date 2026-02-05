package controller;

import static org.mockito.Mockito.*;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class LogoutServletTest {

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private HttpSession session;

    @InjectMocks
    private LogoutServlet servlet;

    @BeforeEach
    void setUp() {
        when(request.getContextPath()).thenReturn("/gestionCoiffure");
    }

    // --- doGet : session existe ---
    @Test
    void testDoGet_sessionExiste_invalidateEtRedirect() throws ServletException, IOException {
        when(request.getSession(false)).thenReturn(session);
        when(session.getAttribute("userName")).thenReturn("Jean");
        when(session.getAttribute("userType")).thenReturn("client");

        servlet.doGet(request, response);

        verify(session).getAttribute("userName");
        verify(session).getAttribute("userType");
        verify(session).invalidate();
        verify(response).sendRedirect("/gestionCoiffure/login?logout=success");
    }

    // --- doGet : pas de session active ---
    @Test
    void testDoGet_sessionNulle_pasInvalidate_redirectQuand_meme() throws ServletException, IOException {
        when(request.getSession(false)).thenReturn(null);

        servlet.doGet(request, response);

        verify(session, never()).invalidate();
        verify(response).sendRedirect("/gestionCoiffure/login?logout=success");
    }

    // --- doPost délègue à doGet ---
    @Test
    void testDoPost_delegueADoGet() throws ServletException, IOException {
        when(request.getSession(false)).thenReturn(session);
        when(session.getAttribute("userName")).thenReturn("Marie");
        when(session.getAttribute("userType")).thenReturn("barber");

        servlet.doPost(request, response);

        verify(session).invalidate();
        verify(response).sendRedirect("/gestionCoiffure/login?logout=success");
    }
}
