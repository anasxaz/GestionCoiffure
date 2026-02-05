package controller;

import dao.AvailabilityDAO;
import dao.BarberDAO;
import model.Availability;
import model.Barber;
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
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ManageAvailabilityServletTest {

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private HttpSession session;

    @Mock
    private RequestDispatcher dispatcher;

    @Mock
    private AvailabilityDAO availabilityDAO;

    @Mock
    private BarberDAO barberDAO;

    @InjectMocks
    private ManageAvailabilityServlet servlet;

    @BeforeEach
    void setUp() {
        lenient().when(request.getContextPath()).thenReturn("/gestionCoiffure");
        lenient().when(request.getSession(false)).thenReturn(session);
        lenient().when(session.getAttribute("userType")).thenReturn("admin");
        lenient().when(request.getRequestDispatcher(anyString())).thenReturn(dispatcher);
    }

    @Test
    void testDoGet_sessionNull_redirectLogin() throws ServletException, IOException {
        when(request.getSession(false)).thenReturn(null);

        servlet.doGet(request, response);

        verify(response).sendRedirect("/gestionCoiffure/login");
    }

    @Test
    void testDoGet_userTypeNotAdmin_redirectLogin() throws ServletException, IOException {
        when(session.getAttribute("userType")).thenReturn("client");

        servlet.doGet(request, response);

        verify(response).sendRedirect("/gestionCoiffure/login");
    }

    @Test
    void testDoGet_actionList_listAvailabilities() throws ServletException, IOException {
        when(request.getParameter("action")).thenReturn("list");
        List<Availability> availabilities = List.of(
            new Availability(1, 1, "Monday", LocalTime.of(9, 0), LocalTime.of(17, 0))
        );
        when(availabilityDAO.findAll()).thenReturn(availabilities);

        servlet.doGet(request, response);

        verify(request).setAttribute("availabilities", availabilities);
        verify(dispatcher).forward(request, response);
    }

    @Test
    void testDoGet_actionAdd_showAddForm() throws ServletException, IOException {
        when(request.getParameter("action")).thenReturn("add");
        List<Barber> barbers = List.of(new Barber(1, "Paul", "paul@test.com", "hash", "0600", "Bio", "active", null));
        when(barberDAO.findAllActive()).thenReturn(barbers);

        servlet.doGet(request, response);

        verify(request).setAttribute("barbers", barbers);
        verify(dispatcher).forward(request, response);
    }

    @Test
    void testDoGet_actionDelete_deleteAvailability() throws ServletException, IOException {
        when(request.getParameter("action")).thenReturn("delete");
        when(request.getParameter("id")).thenReturn("5");
        when(availabilityDAO.delete(5)).thenReturn(true);

        servlet.doGet(request, response);

        verify(availabilityDAO).delete(5);
        verify(response).sendRedirect(contains("/gestionCoiffure/admin/availability"));
    }

    @Test
    void testDoGet_actionNull_defaultToList() throws ServletException, IOException {
        when(request.getParameter("action")).thenReturn(null);
        List<Availability> availabilities = new ArrayList<>();
        when(availabilityDAO.findAll()).thenReturn(availabilities);

        servlet.doGet(request, response);

        verify(request).setAttribute("availabilities", availabilities);
        verify(dispatcher).forward(request, response);
    }

    @Test
    void testDoPost_endTimeBeforeStartTime_error() throws ServletException, IOException {
        when(request.getParameter("barberId")).thenReturn("1");
        when(request.getParameter("dayOfWeek")).thenReturn("Monday");
        when(request.getParameter("startTime")).thenReturn("14:00");
        when(request.getParameter("endTime")).thenReturn("10:00");
        List<Barber> barbers = List.of(new Barber(1, "Paul", "paul@test.com", "hash", "0600", "Bio", "active", null));
        when(barberDAO.findAllActive()).thenReturn(barbers);

        servlet.doPost(request, response);

        verify(request).setAttribute(eq("error"), contains("doit être après"));
        verify(availabilityDAO, never()).create(any(Availability.class));
        verify(dispatcher).forward(request, response);
    }

    @Test
    void testDoPost_endTimeEqualsStartTime_error() throws ServletException, IOException {
        when(request.getParameter("barberId")).thenReturn("1");
        when(request.getParameter("dayOfWeek")).thenReturn("Monday");
        when(request.getParameter("startTime")).thenReturn("10:00");
        when(request.getParameter("endTime")).thenReturn("10:00");
        List<Barber> barbers = List.of(new Barber(1, "Paul", "paul@test.com", "hash", "0600", "Bio", "active", null));
        when(barberDAO.findAllActive()).thenReturn(barbers);

        servlet.doPost(request, response);

        verify(request).setAttribute(eq("error"), contains("doit être après"));
        verify(availabilityDAO, never()).create(any(Availability.class));
    }

    @Test
    void testDoPost_validTimes_success() throws ServletException, IOException {
        when(request.getParameter("barberId")).thenReturn("1");
        when(request.getParameter("dayOfWeek")).thenReturn("Monday");
        when(request.getParameter("startTime")).thenReturn("09:00");
        when(request.getParameter("endTime")).thenReturn("17:00");
        when(availabilityDAO.create(any(Availability.class))).thenReturn(true);

        servlet.doPost(request, response);

        verify(availabilityDAO).create(argThat(avail ->
            avail.getBarberId() == 1 &&
            "Monday".equals(avail.getDayOfWeek())
        ));
        verify(response).sendRedirect(contains("/gestionCoiffure/admin/availability"));
    }

    @Test
    void testDoPost_creationFailure_error() throws ServletException, IOException {
        when(request.getParameter("barberId")).thenReturn("1");
        when(request.getParameter("dayOfWeek")).thenReturn("Monday");
        when(request.getParameter("startTime")).thenReturn("09:00");
        when(request.getParameter("endTime")).thenReturn("17:00");
        when(availabilityDAO.create(any(Availability.class))).thenReturn(false);
        List<Barber> barbers = List.of(new Barber(1, "Paul", "paul@test.com", "hash", "0600", "Bio", "active", null));
        when(barberDAO.findAllActive()).thenReturn(barbers);

        servlet.doPost(request, response);

        verify(request).setAttribute(eq("error"), anyString());
        verify(dispatcher).forward(request, response);
    }
}
