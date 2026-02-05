package controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AdminDashboardServletTest {

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private HttpSession session;

    @Mock
    private RequestDispatcher dispatcher;

    @InjectMocks
    private AdminDashboardServlet servlet;

    @BeforeEach
    void setUp() {
        lenient().when(request.getContextPath()).thenReturn("/gestionCoiffure");
        lenient().when(request.getRequestDispatcher("/WEB-INF/views/admin/dashboard.jsp")).thenReturn(dispatcher);
        lenient().when(request.getSession(false)).thenReturn(session);
    }

    @Test
    void testDoGet_sessionNull_redirectLogin() throws ServletException, IOException {
        when(request.getSession(false)).thenReturn(null);

        servlet.doGet(request, response);

        verify(response).sendRedirect("/gestionCoiffure/login");
        verify(request, never()).getRequestDispatcher(anyString());
    }

    @Test
    void testDoGet_userTypeNotAdmin_redirectLogin() throws ServletException, IOException {
        when(session.getAttribute("userType")).thenReturn("client");

        servlet.doGet(request, response);

        verify(response).sendRedirect("/gestionCoiffure/login");
        verify(request, never()).getRequestDispatcher(anyString());
    }

    @Test
    void testDoGet_validSession_forwardWithStats() throws ServletException, IOException {
        when(session.getAttribute("userType")).thenReturn("admin");

        servlet.doGet(request, response);

        verify(request).setAttribute(eq("totalBarbers"), anyInt());
        verify(request).setAttribute(eq("todayAppointments"), anyInt());
        verify(request).setAttribute(eq("totalClients"), anyInt());
        verify(request).setAttribute(eq("totalServices"), anyInt());
        verify(dispatcher).forward(request, response);
        verify(response, never()).sendRedirect(anyString());
    }

}
