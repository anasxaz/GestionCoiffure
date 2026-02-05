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
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ClientAppointmentsServletTest {

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
    private ClientAppointmentsServlet servlet;

    @BeforeEach
    void setUp() {
        lenient().when(request.getContextPath()).thenReturn("/gestionCoiffure");
        lenient().when(request.getSession(false)).thenReturn(session);
        lenient().when(session.getAttribute("userType")).thenReturn("client");
        lenient().when(session.getAttribute("userId")).thenReturn(5);
        lenient().when(request.getRequestDispatcher("/WEB-INF/views/client/appointments.jsp")).thenReturn(dispatcher);
    }

    @Test
    void testDoGet_sessionNull_redirectLogin() throws ServletException, IOException {
        when(request.getSession(false)).thenReturn(null);

        servlet.doGet(request, response);

        verify(response).sendRedirect("/gestionCoiffure/login");
        verify(request, never()).getRequestDispatcher(anyString());
    }

    @Test
    void testDoGet_userTypeNotClient_redirectLogin() throws ServletException, IOException {
        when(session.getAttribute("userType")).thenReturn("barber");

        servlet.doGet(request, response);

        verify(response).sendRedirect("/gestionCoiffure/login");
        verify(request, never()).getRequestDispatcher(anyString());
    }

    @Test
    void testDoGet_listAppointments_success() throws ServletException, IOException {
        when(request.getParameter("action")).thenReturn(null);

        List<Appointment> appointments = new ArrayList<>();
        Appointment apt = new Appointment();
        apt.setAppointmentId(1);
        apt.setServiceId(10);
        appointments.add(apt);

        when(appointmentDAO.findByClientId(5)).thenReturn(appointments);
        when(serviceDAO.findById(10)).thenReturn(new service.Service(10, "Coupe", "Desc", 30, 20.0, true));
        when(offerDAO.findRedemptionByAppointmentId(1)).thenReturn(null);

        servlet.doGet(request, response);

        verify(request).setAttribute(eq("appointments"), eq(appointments));
        verify(request).setAttribute(eq("serviceMap"), any());
        verify(request).setAttribute(eq("redemptionMap"), any());
        verify(dispatcher).forward(request, response);
    }

    @Test
    void testDoGet_cancelAction_success() throws ServletException, IOException {
        when(request.getParameter("action")).thenReturn("cancel");
        when(request.getParameter("id")).thenReturn("10");

        Appointment apt = new Appointment();
        apt.setAppointmentId(10);
        apt.setClientId(5);

        when(appointmentDAO.findById(10)).thenReturn(apt);
        when(appointmentDAO.cancel(10, "Annulé par le client")).thenReturn(true);

        servlet.doGet(request, response);

        verify(appointmentDAO).cancel(10, "Annulé par le client");
        verify(response).sendRedirect("/gestionCoiffure/client/appointments?success=cancelled");
    }

    @Test
    void testDoGet_cancelAction_appointmentNotFound() throws ServletException, IOException {
        when(request.getParameter("action")).thenReturn("cancel");
        when(request.getParameter("id")).thenReturn("99");

        when(appointmentDAO.findById(99)).thenReturn(null);

        servlet.doGet(request, response);

        verify(appointmentDAO, never()).cancel(anyInt(), anyString());
        verify(response).sendRedirect("/gestionCoiffure/client/appointments?error=cannotCancel");
    }

    @Test
    void testDoGet_cancelAction_wrongClient() throws ServletException, IOException {
        when(request.getParameter("action")).thenReturn("cancel");
        when(request.getParameter("id")).thenReturn("10");

        Appointment apt = new Appointment();
        apt.setAppointmentId(10);
        apt.setClientId(999);

        when(appointmentDAO.findById(10)).thenReturn(apt);

        servlet.doGet(request, response);

        verify(appointmentDAO, never()).cancel(anyInt(), anyString());
        verify(response).sendRedirect("/gestionCoiffure/client/appointments?error=cannotCancel");
    }

    @Test
    void testDoGet_cancelAction_cancelFails() throws ServletException, IOException {
        when(request.getParameter("action")).thenReturn("cancel");
        when(request.getParameter("id")).thenReturn("10");

        Appointment apt = new Appointment();
        apt.setAppointmentId(10);
        apt.setClientId(5);

        when(appointmentDAO.findById(10)).thenReturn(apt);
        when(appointmentDAO.cancel(10, "Annulé par le client")).thenReturn(false);

        servlet.doGet(request, response);

        verify(appointmentDAO).cancel(10, "Annulé par le client");
        verify(response).sendRedirect("/gestionCoiffure/client/appointments?error=cannotCancel");
    }

    @Test
    void testDoGet_cancelAction_exception() throws ServletException, IOException {
        when(request.getParameter("action")).thenReturn("cancel");
        when(request.getParameter("id")).thenReturn("invalid");

        servlet.doGet(request, response);

        verify(response).sendRedirect("/gestionCoiffure/client/appointments?error=unknown");
    }

    @Test
    void testDoGet_appointmentWithRedemption() throws ServletException, IOException {
        when(request.getParameter("action")).thenReturn(null);

        Appointment apt = new Appointment();
        apt.setAppointmentId(1);
        apt.setServiceId(10);

        OfferRedemption redemption = new OfferRedemption();
        redemption.setRedemptionId(5);

        when(appointmentDAO.findByClientId(5)).thenReturn(List.of(apt));
        when(serviceDAO.findById(10)).thenReturn(new service.Service(10, "Coupe", "Desc", 30, 20.0, true));
        when(offerDAO.findRedemptionByAppointmentId(1)).thenReturn(redemption);

        servlet.doGet(request, response);

        verify(offerDAO).findRedemptionByAppointmentId(1);
        verify(request).setAttribute(eq("redemptionMap"), any());
        verify(dispatcher).forward(request, response);
    }
}
