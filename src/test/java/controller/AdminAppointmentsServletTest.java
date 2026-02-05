package controller;

import dao.AppointmentDAO;
import model.Appointment;
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
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AdminAppointmentsServletTest {

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private HttpSession session;

    @Mock
    private RequestDispatcher dispatcher;

    @Mock
    private AppointmentDAO appointmentDAO;

    @InjectMocks
    private AdminAppointmentsServlet servlet;

    @BeforeEach
    void setUp() {
        lenient().when(request.getContextPath()).thenReturn("/gestionCoiffure");
        lenient().when(request.getSession(false)).thenReturn(session);
        lenient().when(request.getRequestDispatcher("/WEB-INF/views/admin/appointments.jsp")).thenReturn(dispatcher);
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
    void testDoGet_listAppointments_success() throws ServletException, IOException {
        when(session.getAttribute("userType")).thenReturn("admin");
        when(request.getParameter("action")).thenReturn(null);
        when(request.getParameter("page")).thenReturn(null);

        List<Appointment> appointments = new ArrayList<>();
        when(appointmentDAO.findAll(1, 10)).thenReturn(appointments);
        when(appointmentDAO.getTotalCount()).thenReturn(5);
        when(appointmentDAO.getCountByStatus("pending")).thenReturn(1);
        when(appointmentDAO.getCountByStatus("confirmed")).thenReturn(2);
        when(appointmentDAO.getCountByStatus("completed")).thenReturn(2);

        servlet.doGet(request, response);

        verify(request).setAttribute(eq("appointments"), eq(appointments));
        verify(request).setAttribute(eq("currentPage"), eq(1));
        verify(request).setAttribute(eq("totalPages"), eq(1));
        verify(request).setAttribute(eq("pendingCount"), eq(1));
        verify(request).setAttribute(eq("confirmedCount"), eq(2));
        verify(request).setAttribute(eq("completedCount"), eq(2));
        verify(dispatcher).forward(request, response);
    }

    @Test
    void testDoGet_cancelAction_success() throws ServletException, IOException {
        when(session.getAttribute("userType")).thenReturn("admin");
        when(request.getParameter("action")).thenReturn("cancel");
        when(request.getParameter("id")).thenReturn("10");

        when(appointmentDAO.cancel(10, "Annulé par l'administrateur")).thenReturn(true);

        servlet.doGet(request, response);

        verify(appointmentDAO).cancel(10, "Annulé par l'administrateur");
        verify(response).sendRedirect("/gestionCoiffure/admin/appointments?success=cancelled");
    }

    @Test
    void testDoGet_cancelAction_failure() throws ServletException, IOException {
        when(session.getAttribute("userType")).thenReturn("admin");
        when(request.getParameter("action")).thenReturn("cancel");
        when(request.getParameter("id")).thenReturn("10");

        when(appointmentDAO.cancel(10, "Annulé par l'administrateur")).thenReturn(false);

        servlet.doGet(request, response);

        verify(appointmentDAO).cancel(10, "Annulé par l'administrateur");
        verify(response).sendRedirect("/gestionCoiffure/admin/appointments?error=failed");
    }

    @Test
    void testDoGet_cancelAction_exception() throws ServletException, IOException {
        when(session.getAttribute("userType")).thenReturn("admin");
        when(request.getParameter("action")).thenReturn("cancel");
        when(request.getParameter("id")).thenReturn("invalid");

        servlet.doGet(request, response);

        verify(response).sendRedirect("/gestionCoiffure/admin/appointments?error=unknown");
    }

    @Test
    void testDoGet_pagination_page2() throws ServletException, IOException {
        when(session.getAttribute("userType")).thenReturn("admin");
        when(request.getParameter("action")).thenReturn(null);
        when(request.getParameter("page")).thenReturn("2");

        when(appointmentDAO.findAll(2, 10)).thenReturn(new ArrayList<>());
        when(appointmentDAO.getTotalCount()).thenReturn(25);
        when(appointmentDAO.getCountByStatus(anyString())).thenReturn(0);

        servlet.doGet(request, response);

        verify(request).setAttribute(eq("currentPage"), eq(2));
        verify(request).setAttribute(eq("totalPages"), eq(3));
        verify(dispatcher).forward(request, response);
    }

    @Test
    void testDoGet_invalidPageParam_defaultsToPage1() throws ServletException, IOException {
        when(session.getAttribute("userType")).thenReturn("admin");
        when(request.getParameter("action")).thenReturn(null);
        when(request.getParameter("page")).thenReturn("invalid");

        when(appointmentDAO.findAll(1, 10)).thenReturn(new ArrayList<>());
        when(appointmentDAO.getTotalCount()).thenReturn(10);
        when(appointmentDAO.getCountByStatus(anyString())).thenReturn(0);

        servlet.doGet(request, response);

        verify(request).setAttribute(eq("currentPage"), eq(1));
        verify(dispatcher).forward(request, response);
    }
}
