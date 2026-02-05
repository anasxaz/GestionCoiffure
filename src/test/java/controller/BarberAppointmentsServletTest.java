package controller;

import dao.AppointmentDAO;
import dao.OfferDAO;
import dao.ServiceDAO;
import model.Appointment;
import model.OfferRedemption;
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
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BarberAppointmentsServletTest {

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

    @Mock
    private ServiceDAO serviceDAO;

    @Mock
    private OfferDAO offerDAO;

    @InjectMocks
    private BarberAppointmentsServlet servlet;

    @BeforeEach
    void setUp() {
        lenient().when(request.getContextPath()).thenReturn("/gestionCoiffure");
        lenient().when(request.getSession(false)).thenReturn(session);
        lenient().when(session.getAttribute("userType")).thenReturn("barber");
        lenient().when(session.getAttribute("userId")).thenReturn(1);
        lenient().when(request.getRequestDispatcher("/WEB-INF/views/barber/appointments.jsp")).thenReturn(dispatcher);
    }

    @Test
    void testDoGet_sessionNull_redirectLogin() throws ServletException, IOException {
        when(request.getSession(false)).thenReturn(null);

        servlet.doGet(request, response);

        verify(response).sendRedirect("/gestionCoiffure/login");
        verify(request, never()).getRequestDispatcher(anyString());
    }

    @Test
    void testDoGet_userTypeNotBarber_redirectLogin() throws ServletException, IOException {
        when(session.getAttribute("userType")).thenReturn("client");

        servlet.doGet(request, response);

        verify(response).sendRedirect("/gestionCoiffure/login");
        verify(request, never()).getRequestDispatcher(anyString());
    }

    @Test
    void testDoGet_listAppointments_success() throws ServletException, IOException {
        when(request.getParameter("action")).thenReturn(null);
        when(request.getParameter("page")).thenReturn(null);

        List<Appointment> appointments = new ArrayList<>();
        Appointment apt = new Appointment();
        apt.setAppointmentId(1);
        apt.setClientId(10);
        apt.setServiceId(5);
        apt.setRedemptionId(null);
        appointments.add(apt);

        when(appointmentDAO.findByBarberId(eq(1), eq(1), eq(10))).thenReturn(appointments);
        when(appointmentDAO.getTotalCountByBarberId(1)).thenReturn(1);
        when(serviceDAO.findById(5)).thenReturn(new service.Service(5, "Coupe", "Desc", 30, 20.0, true));

        servlet.doGet(request, response);

        verify(request).setAttribute(eq("appointments"), any());
        verify(request).setAttribute(eq("currentPage"), eq(1));
        verify(request).setAttribute(eq("totalPages"), eq(1));
        verify(dispatcher).forward(request, response);
    }

    @Test
    void testDoGet_confirmAction_success() throws ServletException, IOException {
        when(request.getParameter("action")).thenReturn("confirm");
        when(request.getParameter("id")).thenReturn("10");

        Appointment apt = new Appointment();
        apt.setAppointmentId(10);
        apt.setBarberId(1);

        when(appointmentDAO.findById(10)).thenReturn(apt);
        when(appointmentDAO.updateStatus(10, "confirmed")).thenReturn(true);

        servlet.doGet(request, response);

        verify(appointmentDAO).updateStatus(10, "confirmed");
        verify(response).sendRedirect("/gestionCoiffure/barber/appointments?success=confirmed");
    }

    @Test
    void testDoGet_confirmAction_appointmentNotFound() throws ServletException, IOException {
        when(request.getParameter("action")).thenReturn("confirm");
        when(request.getParameter("id")).thenReturn("99");

        when(appointmentDAO.findById(99)).thenReturn(null);

        servlet.doGet(request, response);

        verify(appointmentDAO, never()).updateStatus(anyInt(), anyString());
        verify(response).sendRedirect("/gestionCoiffure/barber/appointments?error=notFound");
    }

    @Test
    void testDoGet_confirmAction_wrongBarber() throws ServletException, IOException {
        when(request.getParameter("action")).thenReturn("confirm");
        when(request.getParameter("id")).thenReturn("10");

        Appointment apt = new Appointment();
        apt.setAppointmentId(10);
        apt.setBarberId(999);

        when(appointmentDAO.findById(10)).thenReturn(apt);

        servlet.doGet(request, response);

        verify(appointmentDAO, never()).updateStatus(anyInt(), anyString());
        verify(response).sendRedirect("/gestionCoiffure/barber/appointments?error=notFound");
    }

    @Test
    void testDoGet_refuseAction_success() throws ServletException, IOException {
        when(request.getParameter("action")).thenReturn("refuse");
        when(request.getParameter("id")).thenReturn("10");

        Appointment apt = new Appointment();
        apt.setAppointmentId(10);
        apt.setBarberId(1);

        when(appointmentDAO.findById(10)).thenReturn(apt);
        when(appointmentDAO.updateStatus(10, "refused")).thenReturn(true);

        servlet.doGet(request, response);

        verify(appointmentDAO).updateStatus(10, "refused");
        verify(response).sendRedirect("/gestionCoiffure/barber/appointments?success=refused");
    }

    @Test
    void testDoGet_completeAction_success() throws ServletException, IOException {
        when(request.getParameter("action")).thenReturn("complete");
        when(request.getParameter("id")).thenReturn("10");

        Appointment apt = new Appointment();
        apt.setAppointmentId(10);
        apt.setBarberId(1);
        apt.setClientId(5);

        when(appointmentDAO.findById(10)).thenReturn(apt);
        when(appointmentDAO.updateStatus(10, "completed")).thenReturn(true);

        servlet.doGet(request, response);

        verify(appointmentDAO).updateStatus(10, "completed");
        verify(response).sendRedirect("/gestionCoiffure/barber/appointments?success=completed");
    }

    @Test
    void testDoGet_completeAction_appointmentNotFound() throws ServletException, IOException {
        when(request.getParameter("action")).thenReturn("complete");
        when(request.getParameter("id")).thenReturn("99");

        when(appointmentDAO.findById(99)).thenReturn(null);

        servlet.doGet(request, response);

        verify(appointmentDAO, never()).updateStatus(anyInt(), anyString());
        verify(response).sendRedirect("/gestionCoiffure/barber/appointments?error=notFound");
    }

    @Test
    void testDoGet_confirmAction_exception() throws ServletException, IOException {
        when(request.getParameter("action")).thenReturn("confirm");
        when(request.getParameter("id")).thenReturn("invalid");

        servlet.doGet(request, response);

        verify(response).sendRedirect("/gestionCoiffure/barber/appointments?error=unknown");
    }

    @Test
    void testDoGet_pagination_page2() throws ServletException, IOException {
        when(request.getParameter("action")).thenReturn(null);
        when(request.getParameter("page")).thenReturn("2");

        when(appointmentDAO.findByBarberId(eq(1), eq(2), eq(10))).thenReturn(new ArrayList<>());
        when(appointmentDAO.getTotalCountByBarberId(1)).thenReturn(25);

        servlet.doGet(request, response);

        verify(request).setAttribute(eq("currentPage"), eq(2));
        verify(request).setAttribute(eq("totalPages"), eq(3));
        verify(dispatcher).forward(request, response);
    }

    @Test
    void testDoGet_appointmentWithRedemption() throws ServletException, IOException {
        when(request.getParameter("action")).thenReturn(null);
        when(request.getParameter("page")).thenReturn(null);

        Appointment apt = new Appointment();
        apt.setAppointmentId(1);
        apt.setClientId(10);
        apt.setServiceId(5);
        apt.setRedemptionId(3);

        OfferRedemption redemption = new OfferRedemption();
        redemption.setRedemptionId(3);

        when(appointmentDAO.findByBarberId(eq(1), eq(1), eq(10))).thenReturn(List.of(apt));
        when(appointmentDAO.getTotalCountByBarberId(1)).thenReturn(1);
        when(serviceDAO.findById(5)).thenReturn(new service.Service(5, "Coupe", "Desc", 30, 20.0, true));
        when(offerDAO.findRedeemedOffersByClient(10)).thenReturn(List.of(redemption));

        servlet.doGet(request, response);

        verify(offerDAO).findRedeemedOffersByClient(10);
        verify(request).setAttribute(eq("redemptionMap"), any());
        verify(dispatcher).forward(request, response);
    }
}
