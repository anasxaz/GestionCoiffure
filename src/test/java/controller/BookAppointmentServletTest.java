package controller;

import dao.AppointmentDAO;
import dao.AvailabilityDAO;
import dao.BarberDAO;
import dao.ClientDAO;
import dao.OfferDAO;
import dao.ServiceDAO;
import model.*;
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
class BookAppointmentServletTest {

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private HttpSession session;

    @Mock
    private RequestDispatcher dispatcher;

    @Mock
    private BarberDAO barberDAO;

    @Mock
    private ServiceDAO serviceDAO;

    @Mock
    private AppointmentDAO appointmentDAO;

    @Mock
    private OfferDAO offerDAO;

    @Mock
    private AvailabilityDAO availabilityDAO;

    @Mock
    private ClientDAO clientDAO;

    @InjectMocks
    private BookAppointmentServlet servlet;

    @BeforeEach
    void setUp() {
        lenient().when(request.getContextPath()).thenReturn("/gestionCoiffure");
        lenient().when(request.getSession(false)).thenReturn(session);
        lenient().when(session.getAttribute("userType")).thenReturn("client");
        lenient().when(session.getAttribute("userId")).thenReturn(1);
        lenient().when(request.getRequestDispatcher("/WEB-INF/views/client/book-appointment.jsp")).thenReturn(dispatcher);

        lenient().when(barberDAO.findAllActive()).thenReturn(new ArrayList<>());
        lenient().when(serviceDAO.findAllActive()).thenReturn(new ArrayList<>());
        lenient().when(offerDAO.findUnusedRedeemedOffersByClient(anyInt())).thenReturn(new ArrayList<>());
        lenient().when(clientDAO.findById(anyInt())).thenReturn(new Client());
        lenient().when(availabilityDAO.findByBarberId(anyInt())).thenReturn(new ArrayList<>());
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
    void testDoGet_validSession_forwardWithData() throws ServletException, IOException {
        List<Barber> barbers = List.of(new Barber(1, "Paul", "paul@test.com", "hash", "0600", "Bio", "active", null));
        List<service.Service> services = List.of(new service.Service(1, "Coupe", "Coupe classique", 30, 20.0, true));

        when(barberDAO.findAllActive()).thenReturn(barbers);
        when(serviceDAO.findAllActive()).thenReturn(services);

        servlet.doGet(request, response);

        verify(request).setAttribute("barbers", barbers);
        verify(request).setAttribute("services", services);
        verify(dispatcher).forward(request, response);
    }

    @Test
    void testDoPost_serviceNotFound_errorMessage() throws ServletException, IOException {
        when(request.getParameter("serviceId")).thenReturn("99");
        when(request.getParameter("barberId")).thenReturn("1");
        when(request.getParameter("date")).thenReturn("2024-12-25");
        when(request.getParameter("startTime")).thenReturn("10:00");
        when(request.getParameter("redemptionId")).thenReturn("");
        when(serviceDAO.findById(99)).thenReturn(null);

        servlet.doPost(request, response);

        verify(request).setAttribute(eq("error"), anyString());
        verify(appointmentDAO, never()).create(any(Appointment.class));
    }

    @Test
    void testDoPost_barberNotAvailable_errorMessage() throws ServletException, IOException {
        service.Service service = new service.Service(1, "Coupe", "Coupe classique", 30, 20.0, true);
        when(request.getParameter("serviceId")).thenReturn("1");
        when(request.getParameter("barberId")).thenReturn("2");
        when(request.getParameter("date")).thenReturn("2024-12-25");
        when(request.getParameter("startTime")).thenReturn("10:00");
        when(request.getParameter("redemptionId")).thenReturn("");
        when(serviceDAO.findById(1)).thenReturn(service);
        when(appointmentDAO.isBarberAvailable(eq(2), any(LocalDate.class), any(LocalTime.class), any(LocalTime.class))).thenReturn(false);

        servlet.doPost(request, response);

        verify(request).setAttribute(eq("error"), contains("n'est pas disponible"));
        verify(appointmentDAO, never()).create(any(Appointment.class));
    }

    @Test
    void testDoPost_success_withoutRedemption() throws ServletException, IOException {
        service.Service service = new service.Service(1, "Coupe", "Coupe classique", 30, 20.0, true);
        when(request.getParameter("serviceId")).thenReturn("1");
        when(request.getParameter("barberId")).thenReturn("2");
        when(request.getParameter("date")).thenReturn("2024-12-25");
        when(request.getParameter("startTime")).thenReturn("10:00");
        when(request.getParameter("redemptionId")).thenReturn("");
        when(serviceDAO.findById(1)).thenReturn(service);
        when(appointmentDAO.isBarberAvailable(eq(2), any(LocalDate.class), any(LocalTime.class), any(LocalTime.class))).thenReturn(true);
        when(appointmentDAO.create(any(Appointment.class))).thenReturn(true);

        servlet.doPost(request, response);

        verify(appointmentDAO).create(argThat(apt ->
            apt.getClientId() == 1 &&
            apt.getBarberId() == 2 &&
            apt.getServiceId() == 1 &&
            apt.getFinalPrice() == 20.0
        ));
        verify(response).sendRedirect("/gestionCoiffure/client/appointments?success=created");
    }

    @Test
    void testDoPost_success_withRedemption() throws ServletException, IOException {
        service.Service service = new service.Service(1, "Coupe", "Coupe classique", 30, 20.0, true);
        when(request.getParameter("serviceId")).thenReturn("1");
        when(request.getParameter("barberId")).thenReturn("2");
        when(request.getParameter("date")).thenReturn("2024-12-25");
        when(request.getParameter("startTime")).thenReturn("10:00");
        when(request.getParameter("redemptionId")).thenReturn("5");
        when(serviceDAO.findById(1)).thenReturn(service);
        when(appointmentDAO.isBarberAvailable(eq(2), any(LocalDate.class), any(LocalTime.class), any(LocalTime.class))).thenReturn(true);
        when(appointmentDAO.create(any(Appointment.class))).thenReturn(true);

        servlet.doPost(request, response);

        verify(appointmentDAO).create(argThat(apt ->
            apt.getClientId() == 1 &&
            apt.getBarberId() == 2 &&
            apt.getServiceId() == 1 &&
            apt.getFinalPrice() == 18.0
        ));
        verify(offerDAO).markRedemptionAsUsed(eq(5), anyInt());
        verify(response).sendRedirect("/gestionCoiffure/client/appointments?success=created");
    }

    @Test
    void testDoPost_creationFailure_errorMessage() throws ServletException, IOException {
        service.Service service = new service.Service(1, "Coupe", "Coupe classique", 30, 20.0, true);
        when(request.getParameter("serviceId")).thenReturn("1");
        when(request.getParameter("barberId")).thenReturn("2");
        when(request.getParameter("date")).thenReturn("2024-12-25");
        when(request.getParameter("startTime")).thenReturn("10:00");
        when(request.getParameter("redemptionId")).thenReturn("");
        when(serviceDAO.findById(1)).thenReturn(service);
        when(appointmentDAO.isBarberAvailable(eq(2), any(LocalDate.class), any(LocalTime.class), any(LocalTime.class))).thenReturn(true);
        when(appointmentDAO.create(any(Appointment.class))).thenReturn(false);

        servlet.doPost(request, response);

        verify(request).setAttribute(eq("error"), anyString());
        verify(appointmentDAO).create(any(Appointment.class));
    }
}
