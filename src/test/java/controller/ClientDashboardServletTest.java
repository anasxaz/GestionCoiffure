package controller;

import static org.mockito.Mockito.*;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import dao.OfferDAO;
import model.OfferRedemption;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ClientDashboardServletTest {

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private HttpSession session;

    @Mock
    private RequestDispatcher dispatcher;

    @Mock
    private OfferDAO offerDAO;

    @InjectMocks
    private ClientDashboardServlet servlet;

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

    // --- doGet : session existe mais userType ≠ "client" → redirect login ---
    @Test
    void testDoGet_typeNonClient_redirectLogin() throws ServletException, IOException {
        when(request.getSession(false)).thenReturn(session);
        when(session.getAttribute("userType")).thenReturn("barber");

        servlet.doGet(request, response);

        verify(response).sendRedirect("/gestionCoiffure/login");
        verify(dispatcher, never()).forward(request, response);
    }

    // --- doGet : session valide, offres présentes ---
    @Test
    void testDoGet_sessionValide_afficheDashboardAvecOffres() throws ServletException, IOException {
        when(request.getSession(false)).thenReturn(session);
        when(session.getAttribute("userType")).thenReturn("client");
        when(session.getAttribute("userId")).thenReturn(1);

        List<OfferRedemption> offres = Arrays.asList(new OfferRedemption(), new OfferRedemption(), new OfferRedemption());
        when(offerDAO.findUnusedRedeemedOffersByClient(1)).thenReturn(offres);

        servlet.doGet(request, response);

        verify(request).setAttribute("unusedOffersCount", 3);
        verify(dispatcher).forward(request, response);
    }

    // --- doGet : DAO retourne null → compteur 0 ---
    @Test
    void testDoGet_offresNulles_compteurZero() throws ServletException, IOException {
        when(request.getSession(false)).thenReturn(session);
        when(session.getAttribute("userType")).thenReturn("client");
        when(session.getAttribute("userId")).thenReturn(1);
        when(offerDAO.findUnusedRedeemedOffersByClient(1)).thenReturn(null);

        servlet.doGet(request, response);

        verify(request).setAttribute("unusedOffersCount", 0);
        verify(dispatcher).forward(request, response);
    }

    // --- doGet : exception dans le DAO → compteur 0, forward quand même ---
    @Test
    void testDoGet_exceptionDAO_compteurZero() throws ServletException, IOException {
        when(request.getSession(false)).thenReturn(session);
        when(session.getAttribute("userType")).thenReturn("client");
        when(session.getAttribute("userId")).thenReturn(1);
        when(offerDAO.findUnusedRedeemedOffersByClient(1)).thenThrow(new RuntimeException("DB error"));

        servlet.doGet(request, response);

        verify(request).setAttribute("unusedOffersCount", 0);
        verify(dispatcher).forward(request, response);
    }
}
