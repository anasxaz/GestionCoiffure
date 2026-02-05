package controller;

import dao.BarberDAO;
import model.Barber;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import util.PasswordUtil;

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
class ManageBarbersServletTest {

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

    @InjectMocks
    private ManageBarbersServlet servlet;

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
    void testDoGet_listAction_success() throws ServletException, IOException {
        when(request.getParameter("action")).thenReturn("list");
        when(request.getParameter("page")).thenReturn(null);

        List<Barber> barbers = new ArrayList<>();
        when(barberDAO.findAll(1, 10)).thenReturn(barbers);
        when(barberDAO.getTotalCount()).thenReturn(5);

        servlet.doGet(request, response);

        verify(request).setAttribute(eq("barbers"), eq(barbers));
        verify(request).setAttribute(eq("currentPage"), eq(1));
        verify(request).setAttribute(eq("totalPages"), eq(1));
        verify(dispatcher).forward(request, response);
    }

    @Test
    void testDoGet_addAction_showForm() throws ServletException, IOException {
        when(request.getParameter("action")).thenReturn("add");

        servlet.doGet(request, response);

        verify(request).getRequestDispatcher("/WEB-INF/views/admin/barber-form.jsp");
        verify(dispatcher).forward(request, response);
    }

    @Test
    void testDoGet_editAction_barberFound() throws ServletException, IOException {
        when(request.getParameter("action")).thenReturn("edit");
        when(request.getParameter("id")).thenReturn("1");

        Barber barber = new Barber();
        barber.setBarberId(1);
        when(barberDAO.findById(1)).thenReturn(barber);

        servlet.doGet(request, response);

        verify(request).setAttribute("barber", barber);
        verify(dispatcher).forward(request, response);
    }

    @Test
    void testDoGet_editAction_barberNotFound() throws ServletException, IOException {
        when(request.getParameter("action")).thenReturn("edit");
        when(request.getParameter("id")).thenReturn("99");

        when(barberDAO.findById(99)).thenReturn(null);
        when(barberDAO.findAll(1, 10)).thenReturn(new ArrayList<>());
        when(barberDAO.getTotalCount()).thenReturn(0);

        servlet.doGet(request, response);

        verify(request).setAttribute(eq("error"), eq("Coiffeur non trouvé"));
        verify(dispatcher).forward(request, response);
    }

    @Test
    void testDoGet_deleteAction_success() throws ServletException, IOException {
        when(request.getParameter("action")).thenReturn("delete");
        when(request.getParameter("id")).thenReturn("1");

        when(barberDAO.delete(1)).thenReturn(true);

        servlet.doGet(request, response);

        verify(barberDAO).delete(1);
        verify(response).sendRedirect("/gestionCoiffure/admin/barbers?success=deleted");
    }

    @Test
    void testDoGet_deleteAction_failure() throws ServletException, IOException {
        when(request.getParameter("action")).thenReturn("delete");
        when(request.getParameter("id")).thenReturn("1");

        when(barberDAO.delete(1)).thenReturn(false);

        servlet.doGet(request, response);

        verify(response).sendRedirect("/gestionCoiffure/admin/barbers?error=deleteFailed");
    }

    @Test
    void testDoGet_toggleStatusAction_activeToInactive() throws ServletException, IOException {
        when(request.getParameter("action")).thenReturn("toggleStatus");
        when(request.getParameter("id")).thenReturn("1");

        Barber barber = new Barber();
        barber.setBarberId(1);
        barber.setStatus("active");

        when(barberDAO.findById(1)).thenReturn(barber);
        when(barberDAO.updateStatus(1, "inactive")).thenReturn(true);

        servlet.doGet(request, response);

        verify(barberDAO).updateStatus(1, "inactive");
        verify(response).sendRedirect("/gestionCoiffure/admin/barbers");
    }

    @Test
    void testDoPost_sessionNull_redirectLogin() throws ServletException, IOException {
        when(request.getSession(false)).thenReturn(null);

        servlet.doPost(request, response);

        verify(response).sendRedirect("/gestionCoiffure/login");
    }

    @Test
    void testDoPost_createAction_success() throws ServletException, IOException {
        when(request.getParameter("action")).thenReturn("create");
        when(request.getParameter("name")).thenReturn("Jean");
        when(request.getParameter("email")).thenReturn("jean@test.com");
        when(request.getParameter("password")).thenReturn("password123");
        when(request.getParameter("phone")).thenReturn("0600000001");
        when(request.getParameter("bio")).thenReturn("Expert");
        when(request.getParameter("status")).thenReturn("active");

        when(barberDAO.findByEmail("jean@test.com")).thenReturn(null);
        when(barberDAO.create(any(Barber.class))).thenAnswer(invocation -> {
            Barber b = invocation.getArgument(0);
            b.setBarberId(5);
            return true;
        });

        try (MockedStatic<PasswordUtil> passwordUtil = mockStatic(PasswordUtil.class)) {
            passwordUtil.when(() -> PasswordUtil.isValidPassword("password123")).thenReturn(true);
            passwordUtil.when(() -> PasswordUtil.hashPassword("password123")).thenReturn("hashedPassword");

            servlet.doPost(request, response);

            verify(barberDAO).create(any(Barber.class));
            verify(response).sendRedirect(contains("/admin/barbers?success=created"));
        }
    }

    @Test
    void testDoPost_createAction_missingFields() throws ServletException, IOException {
        when(request.getParameter("action")).thenReturn("create");
        when(request.getParameter("name")).thenReturn("");
        when(request.getParameter("email")).thenReturn("jean@test.com");
        when(request.getParameter("password")).thenReturn("password123");

        servlet.doPost(request, response);

        verify(request).setAttribute(eq("error"), eq("Les champs nom, email et mot de passe sont requis"));
        verify(barberDAO, never()).create(any(Barber.class));
        verify(dispatcher).forward(request, response);
    }

    @Test
    void testDoPost_createAction_emailExists() throws ServletException, IOException {
        when(request.getParameter("action")).thenReturn("create");
        when(request.getParameter("name")).thenReturn("Jean");
        when(request.getParameter("email")).thenReturn("existing@test.com");
        when(request.getParameter("password")).thenReturn("password123");

        when(barberDAO.findByEmail("existing@test.com")).thenReturn(new Barber());

        servlet.doPost(request, response);

        verify(request).setAttribute(eq("error"), eq("Cet email est déjà utilisé"));
        verify(barberDAO, never()).create(any(Barber.class));
        verify(dispatcher).forward(request, response);
    }

    @Test
    void testDoPost_createAction_invalidPassword() throws ServletException, IOException {
        when(request.getParameter("action")).thenReturn("create");
        when(request.getParameter("name")).thenReturn("Jean");
        when(request.getParameter("email")).thenReturn("jean@test.com");
        when(request.getParameter("password")).thenReturn("123");

        when(barberDAO.findByEmail("jean@test.com")).thenReturn(null);

        try (MockedStatic<PasswordUtil> passwordUtil = mockStatic(PasswordUtil.class)) {
            passwordUtil.when(() -> PasswordUtil.isValidPassword("123")).thenReturn(false);

            servlet.doPost(request, response);

            verify(request).setAttribute(eq("error"), eq("Le mot de passe doit contenir au moins 6 caractères"));
            verify(barberDAO, never()).create(any(Barber.class));
            verify(dispatcher).forward(request, response);
        }
    }

    @Test
    void testDoPost_updateAction_success() throws ServletException, IOException {
        when(request.getParameter("action")).thenReturn("update");
        when(request.getParameter("barberId")).thenReturn("1");
        when(request.getParameter("name")).thenReturn("Jean Updated");
        when(request.getParameter("email")).thenReturn("jean@test.com");
        when(request.getParameter("phone")).thenReturn("0600000001");
        when(request.getParameter("bio")).thenReturn("Expert");
        when(request.getParameter("status")).thenReturn("active");

        Barber barber = new Barber();
        barber.setBarberId(1);
        barber.setEmail("jean@test.com");

        when(barberDAO.findById(1)).thenReturn(barber);
        when(barberDAO.update(any(Barber.class))).thenReturn(true);

        servlet.doPost(request, response);

        verify(barberDAO).update(any(Barber.class));
        verify(response).sendRedirect("/gestionCoiffure/admin/barbers?success=updated");
    }

    @Test
    void testDoPost_updateAction_barberNotFound() throws ServletException, IOException {
        when(request.getParameter("action")).thenReturn("update");
        when(request.getParameter("barberId")).thenReturn("99");
        when(request.getParameter("name")).thenReturn("Jean");
        when(request.getParameter("email")).thenReturn("jean@test.com");
        when(request.getParameter("phone")).thenReturn("0600");
        when(request.getParameter("bio")).thenReturn("Bio");
        when(request.getParameter("status")).thenReturn("active");

        when(barberDAO.findById(99)).thenReturn(null);
        when(barberDAO.findAll(1, 10)).thenReturn(new ArrayList<>());
        when(barberDAO.getTotalCount()).thenReturn(0);

        servlet.doPost(request, response);

        verify(request).setAttribute(eq("error"), eq("Coiffeur non trouvé"));
        verify(barberDAO, never()).update(any(Barber.class));
        verify(dispatcher).forward(request, response);
    }

    @Test
    void testDoPost_updateAction_emailAlreadyUsed() throws ServletException, IOException {
        when(request.getParameter("action")).thenReturn("update");
        when(request.getParameter("barberId")).thenReturn("1");
        when(request.getParameter("name")).thenReturn("Jean");
        when(request.getParameter("email")).thenReturn("other@test.com");
        when(request.getParameter("phone")).thenReturn("0600");
        when(request.getParameter("bio")).thenReturn("Bio");
        when(request.getParameter("status")).thenReturn("active");

        Barber barber = new Barber();
        barber.setBarberId(1);
        barber.setEmail("jean@test.com");

        Barber existingBarber = new Barber();
        existingBarber.setBarberId(2);
        existingBarber.setEmail("other@test.com");

        when(barberDAO.findById(1)).thenReturn(barber);
        when(barberDAO.findByEmail("other@test.com")).thenReturn(existingBarber);

        servlet.doPost(request, response);

        verify(request).setAttribute(eq("error"), eq("Cet email est déjà utilisé"));
        verify(barberDAO, never()).update(any(Barber.class));
    }
}
