package controller;

import dao.AppointmentDAO;
import dao.ClientDAO;
import model.Client;
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

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ViewClientsServletTest {

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private HttpSession session;

    @Mock
    private RequestDispatcher dispatcher;

    @Mock
    private ClientDAO clientDAO;

    @Mock
    private AppointmentDAO appointmentDAO;

    @InjectMocks
    private ViewClientsServlet servlet;

    @BeforeEach
    void setUp() {
        lenient().when(request.getContextPath()).thenReturn("/gestionCoiffure");
        lenient().when(request.getSession(false)).thenReturn(session);
        lenient().when(request.getRequestDispatcher("/WEB-INF/views/admin/clients.jsp")).thenReturn(dispatcher);
        lenient().when(clientDAO.getLoyalClientCount()).thenReturn(0);
        lenient().when(clientDAO.getTotalPoints()).thenReturn(0);
        lenient().when(clientDAO.getCompletedAppointmentCount(anyInt())).thenReturn(0);
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
    void testDoGet_defaultPage_page1() throws ServletException, IOException {
        when(session.getAttribute("userType")).thenReturn("admin");
        when(request.getParameter("page")).thenReturn(null);
        when(clientDAO.getTotalCount()).thenReturn(25);
        List<Client> clients = new ArrayList<>();
        when(clientDAO.findAll(1, 10)).thenReturn(clients);

        servlet.doGet(request, response);

        verify(request).setAttribute("clients", clients);
        verify(request).setAttribute("currentPage", 1);
        verify(request).setAttribute("totalPages", 3);
        verify(dispatcher).forward(request, response);
    }

    @Test
    void testDoGet_validPageParam_page2() throws ServletException, IOException {
        when(session.getAttribute("userType")).thenReturn("admin");
        when(request.getParameter("page")).thenReturn("2");
        when(clientDAO.getTotalCount()).thenReturn(25);
        List<Client> clients = List.of(
            new Client(11, "Client11", "c11@test.com", "hash", "0600", 0, "standard", null)
        );
        when(clientDAO.findAll(2, 10)).thenReturn(clients);

        servlet.doGet(request, response);

        verify(request).setAttribute("clients", clients);
        verify(request).setAttribute("currentPage", 2);
        verify(request).setAttribute("totalPages", 3);
        verify(dispatcher).forward(request, response);
    }

    @Test
    void testDoGet_invalidPageParam_resetsToPage1() throws ServletException, IOException {
        when(session.getAttribute("userType")).thenReturn("admin");
        when(request.getParameter("page")).thenReturn("invalid");
        when(clientDAO.getTotalCount()).thenReturn(15);
        List<Client> clients = new ArrayList<>();
        when(clientDAO.findAll(1, 10)).thenReturn(clients);

        servlet.doGet(request, response);

        verify(request).setAttribute("clients", clients);
        verify(request).setAttribute("currentPage", 1);
        verify(request).setAttribute("totalPages", 2);
        verify(dispatcher).forward(request, response);
    }

    @Test
    void testDoGet_paginationMath_exactMultiple() throws ServletException, IOException {
        when(session.getAttribute("userType")).thenReturn("admin");
        when(request.getParameter("page")).thenReturn("1");
        when(clientDAO.getTotalCount()).thenReturn(20);
        List<Client> clients = new ArrayList<>();
        when(clientDAO.findAll(1, 10)).thenReturn(clients);

        servlet.doGet(request, response);

        verify(request).setAttribute("totalPages", 2);
    }

    @Test
    void testDoGet_paginationMath_zeroClients() throws ServletException, IOException {
        when(session.getAttribute("userType")).thenReturn("admin");
        when(request.getParameter("page")).thenReturn("1");
        when(clientDAO.getTotalCount()).thenReturn(0);
        List<Client> clients = new ArrayList<>();
        when(clientDAO.findAll(1, 10)).thenReturn(clients);

        servlet.doGet(request, response);

        verify(request).setAttribute("totalPages", 0);
    }

}
