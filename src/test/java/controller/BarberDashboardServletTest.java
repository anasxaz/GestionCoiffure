package controller;

import static org.mockito.Mockito.*;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import dao.AppointmentDAO;
import model.Appointment;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class BarberDashboardServletTest {

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
    private BarberDashboardServlet servlet;

    @BeforeEach
    void setUp() {
        lenient().when(request.getRequestDispatcher(anyString())).thenReturn(dispatcher);
        lenient().when(request.getContextPath()).thenReturn("/gestionCoiffure");
    }

    // --- doGet : pas de session → redirect login ---
    @Test
    void testDoGet_sessionNulle_redirectLogin() throws ServletException, IOException {
        when(request.getSession(false)).thenReturn(null);

        servlet.doGet(request, response);

        verify(response).sendRedirect("/gestionCoiffure/login");
        verify(dispatcher, never()).forward(request, response);
    }

    // --- doGet : userType ≠ "barber" → redirect login ---
    @Test
    void testDoGet_typeNonBarber_redirectLogin() throws ServletException, IOException {
        when(request.getSession(false)).thenReturn(session);
        when(session.getAttribute("userType")).thenReturn("client");

        servlet.doGet(request, response);

        verify(response).sendRedirect("/gestionCoiffure/login");
        verify(dispatcher, never()).forward(request, response);
    }

    // --- doGet : barberId null dans la session → redirect login ---
    @Test
    void testDoGet_barberIdNull_redirectLogin() throws ServletException, IOException {
        when(request.getSession(false)).thenReturn(session);
        when(session.getAttribute("userType")).thenReturn("barber");
        when(session.getAttribute("userId")).thenReturn(null);

        servlet.doGet(request, response);

        verify(response).sendRedirect("/gestionCoiffure/login");
        verify(dispatcher, never()).forward(request, response);
    }

    // --- doGet : cas normal → statistiques calculées correctement ---
    @Test
    void testDoGet_sessionValide_afficheDashboardAvecStats() throws ServletException, IOException {
        when(request.getSession(false)).thenReturn(session);
        when(session.getAttribute("userType")).thenReturn("barber");
        when(session.getAttribute("userId")).thenReturn(1);

        // 2 confirmed (clients 10 et 10), 1 pending (client 20), tous dans le mois courant
        Appointment confirmed1 = new Appointment();
        confirmed1.setStatus("confirmed");
        confirmed1.setClientId(10);
        confirmed1.setDate(LocalDate.now());

        Appointment pending1 = new Appointment();
        pending1.setStatus("pending");
        pending1.setClientId(20);
        pending1.setDate(LocalDate.now());

        Appointment confirmed2 = new Appointment();
        confirmed2.setStatus("confirmed");
        confirmed2.setClientId(10);
        confirmed2.setDate(LocalDate.now());

        List<Appointment> allAppointments = Arrays.asList(confirmed1, pending1, confirmed2);

        // aujourd'hui : 1 RDV
        Appointment todayApt = new Appointment();
        todayApt.setStatus("confirmed");
        List<Appointment> todayAppointments = Collections.singletonList(todayApt);

        when(appointmentDAO.findByBarberId(1)).thenReturn(allAppointments);
        when(appointmentDAO.findByBarberAndDate(eq(1), any(LocalDate.class))).thenReturn(todayAppointments);

        servlet.doGet(request, response);

        verify(request).setAttribute("todayAppointments", 1);
        verify(request).setAttribute("confirmedAppointments", 2);
        verify(request).setAttribute("pendingAppointments", 1);
        verify(request).setAttribute("monthlyClients", 2); // clients uniques : 10, 20
        verify(dispatcher).forward(request, response);
    }

    // --- doGet : exception dans le DAO → tous les compteurs à 0 ---
    @Test
    void testDoGet_exceptionDAO_compteurZero() throws ServletException, IOException {
        when(request.getSession(false)).thenReturn(session);
        when(session.getAttribute("userType")).thenReturn("barber");
        when(session.getAttribute("userId")).thenReturn(1);
        when(appointmentDAO.findByBarberId(1)).thenThrow(new RuntimeException("DB error"));

        servlet.doGet(request, response);

        verify(request).setAttribute("todayAppointments", 0);
        verify(request).setAttribute("confirmedAppointments", 0);
        verify(request).setAttribute("pendingAppointments", 0);
        verify(request).setAttribute("monthlyClients", 0);
        verify(dispatcher).forward(request, response);
    }
}
