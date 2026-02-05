package controller;

import dao.ServiceDAO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import service.Service;

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
class ManageServicesServletTest {

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private HttpSession session;

    @Mock
    private RequestDispatcher dispatcher;

    @Mock
    private ServiceDAO serviceDAO;

    @InjectMocks
    private ManageServicesServlet servlet;

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

        List<Service> services = new ArrayList<>();
        when(serviceDAO.findAll(1, 10)).thenReturn(services);
        when(serviceDAO.getTotalCount()).thenReturn(5);

        servlet.doGet(request, response);

        verify(request).setAttribute(eq("services"), eq(services));
        verify(request).setAttribute(eq("currentPage"), eq(1));
        verify(request).setAttribute(eq("totalPages"), eq(1));
        verify(dispatcher).forward(request, response);
    }

    @Test
    void testDoGet_addAction_showForm() throws ServletException, IOException {
        when(request.getParameter("action")).thenReturn("add");

        servlet.doGet(request, response);

        verify(request).getRequestDispatcher("/WEB-INF/views/admin/service-form.jsp");
        verify(dispatcher).forward(request, response);
    }

    @Test
    void testDoGet_editAction_serviceFound() throws ServletException, IOException {
        when(request.getParameter("action")).thenReturn("edit");
        when(request.getParameter("id")).thenReturn("1");

        Service service = new Service();
        service.setServiceId(1);
        when(serviceDAO.findById(1)).thenReturn(service);

        servlet.doGet(request, response);

        verify(request).setAttribute("service", service);
        verify(dispatcher).forward(request, response);
    }

    @Test
    void testDoGet_editAction_serviceNotFound() throws ServletException, IOException {
        when(request.getParameter("action")).thenReturn("edit");
        when(request.getParameter("id")).thenReturn("99");

        when(serviceDAO.findById(99)).thenReturn(null);
        when(serviceDAO.findAll(1, 10)).thenReturn(new ArrayList<>());
        when(serviceDAO.getTotalCount()).thenReturn(0);

        servlet.doGet(request, response);

        verify(request).setAttribute(eq("error"), eq("Service non trouvé"));
        verify(dispatcher).forward(request, response);
    }

    @Test
    void testDoGet_deleteAction_success() throws ServletException, IOException {
        when(request.getParameter("action")).thenReturn("delete");
        when(request.getParameter("id")).thenReturn("1");

        when(serviceDAO.delete(1)).thenReturn(true);

        servlet.doGet(request, response);

        verify(serviceDAO).delete(1);
        verify(response).sendRedirect("/gestionCoiffure/admin/services?success=deleted");
    }

    @Test
    void testDoGet_deleteAction_failure() throws ServletException, IOException {
        when(request.getParameter("action")).thenReturn("delete");
        when(request.getParameter("id")).thenReturn("1");

        when(serviceDAO.delete(1)).thenReturn(false);

        servlet.doGet(request, response);

        verify(response).sendRedirect("/gestionCoiffure/admin/services?error=deleteFailed");
    }

    @Test
    void testDoGet_toggleAction() throws ServletException, IOException {
        when(request.getParameter("action")).thenReturn("toggle");
        when(request.getParameter("id")).thenReturn("1");

        servlet.doGet(request, response);

        verify(serviceDAO).toggleActive(1);
        verify(response).sendRedirect("/gestionCoiffure/admin/services");
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
        when(request.getParameter("name")).thenReturn("Coupe");
        when(request.getParameter("description")).thenReturn("Coupe classique");
        when(request.getParameter("duration")).thenReturn("30");
        when(request.getParameter("price")).thenReturn("20.50");
        when(request.getParameter("isActive")).thenReturn("on");

        when(serviceDAO.create(any(Service.class))).thenReturn(true);

        servlet.doPost(request, response);

        verify(serviceDAO).create(any(Service.class));
        verify(response).sendRedirect("/gestionCoiffure/admin/services?success=created");
    }

    @Test
    void testDoPost_createAction_missingFields() throws ServletException, IOException {
        when(request.getParameter("action")).thenReturn("create");
        when(request.getParameter("name")).thenReturn("");
        when(request.getParameter("description")).thenReturn("Desc");
        when(request.getParameter("duration")).thenReturn(null);
        when(request.getParameter("price")).thenReturn("20.50");

        servlet.doPost(request, response);

        verify(request).setAttribute(eq("error"), eq("Les champs nom, durée et prix sont requis"));
        verify(serviceDAO, never()).create(any(Service.class));
        verify(dispatcher).forward(request, response);
    }

    @Test
    void testDoPost_createAction_invalidNumberFormat() throws ServletException, IOException {
        when(request.getParameter("action")).thenReturn("create");
        when(request.getParameter("name")).thenReturn("Coupe");
        when(request.getParameter("description")).thenReturn("Desc");
        when(request.getParameter("duration")).thenReturn("invalid");
        when(request.getParameter("price")).thenReturn("20.50");

        servlet.doPost(request, response);

        verify(request).setAttribute(eq("error"), eq("Format de nombre invalide"));
        verify(serviceDAO, never()).create(any(Service.class));
        verify(dispatcher).forward(request, response);
    }

    @Test
    void testDoPost_createAction_failure() throws ServletException, IOException {
        when(request.getParameter("action")).thenReturn("create");
        when(request.getParameter("name")).thenReturn("Coupe");
        when(request.getParameter("description")).thenReturn("Desc");
        when(request.getParameter("duration")).thenReturn("30");
        when(request.getParameter("price")).thenReturn("20.50");
        when(request.getParameter("isActive")).thenReturn(null);

        when(serviceDAO.create(any(Service.class))).thenReturn(false);

        servlet.doPost(request, response);

        verify(request).setAttribute(eq("error"), eq("Erreur lors de la création"));
        verify(dispatcher).forward(request, response);
    }

    @Test
    void testDoPost_updateAction_success() throws ServletException, IOException {
        when(request.getParameter("action")).thenReturn("update");
        when(request.getParameter("serviceId")).thenReturn("1");
        when(request.getParameter("name")).thenReturn("Coupe Updated");
        when(request.getParameter("description")).thenReturn("Desc");
        when(request.getParameter("duration")).thenReturn("45");
        when(request.getParameter("price")).thenReturn("25.00");
        when(request.getParameter("isActive")).thenReturn("on");

        when(serviceDAO.update(any(Service.class))).thenReturn(true);

        servlet.doPost(request, response);

        verify(serviceDAO).update(any(Service.class));
        verify(response).sendRedirect("/gestionCoiffure/admin/services?success=updated");
    }

    @Test
    void testDoPost_updateAction_failure() throws ServletException, IOException {
        when(request.getParameter("action")).thenReturn("update");
        when(request.getParameter("serviceId")).thenReturn("1");
        when(request.getParameter("name")).thenReturn("Coupe");
        when(request.getParameter("description")).thenReturn("Desc");
        when(request.getParameter("duration")).thenReturn("30");
        when(request.getParameter("price")).thenReturn("20.50");
        when(request.getParameter("isActive")).thenReturn(null);

        when(serviceDAO.update(any(Service.class))).thenReturn(false);

        servlet.doPost(request, response);

        verify(request).setAttribute(eq("error"), eq("Erreur lors de la mise à jour"));
    }

    @Test
    void testDoGet_nullAction_defaultsToList() throws ServletException, IOException {
        when(request.getParameter("action")).thenReturn(null);
        when(request.getParameter("page")).thenReturn(null);

        when(serviceDAO.findAll(1, 10)).thenReturn(new ArrayList<>());
        when(serviceDAO.getTotalCount()).thenReturn(0);

        servlet.doGet(request, response);

        verify(request).setAttribute(eq("services"), any());
        verify(dispatcher).forward(request, response);
    }
}
