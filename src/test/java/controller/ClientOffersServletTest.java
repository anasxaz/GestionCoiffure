package controller;

import dao.ClientDAO;
import dao.OfferDAO;
import model.Client;
import model.Offer;
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
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ClientOffersServletTest {

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

    @Mock
    private ClientDAO clientDAO;

    @InjectMocks
    private ClientOffersServlet servlet;

    @BeforeEach
    void setUp() {
        lenient().when(request.getContextPath()).thenReturn("/gestionCoiffure");
        lenient().when(request.getSession(false)).thenReturn(session);
        lenient().when(request.getRequestDispatcher("/WEB-INF/views/client/offers.jsp")).thenReturn(dispatcher);
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
    void testDoGet_validSession_listOffers() throws ServletException, IOException {
        when(session.getAttribute("userType")).thenReturn("client");
        when(session.getAttribute("userId")).thenReturn(1);
        when(request.getParameter("action")).thenReturn(null);
        Client client = new Client(1, "Jean", "jean@test.com", "hash", "0600", 150, "fidele", null);
        List<Offer> offers = List.of(
            new Offer(1, "Promo 50%", "Description", 100, true, LocalDateTime.now())
        );
        when(clientDAO.findById(1)).thenReturn(client);
        when(offerDAO.findAllActive()).thenReturn(offers);
        when(offerDAO.findUnusedRedeemedOffersByClient(1)).thenReturn(new ArrayList<>());

        servlet.doGet(request, response);

        verify(request).setAttribute("client", client);
        verify(request).setAttribute("offers", offers);
        verify(dispatcher).forward(request, response);
    }

    @Test
    void testDoGet_redeemAction_offerNotFound_redirect() throws ServletException, IOException {
        when(session.getAttribute("userType")).thenReturn("client");
        when(session.getAttribute("userId")).thenReturn(1);
        when(request.getParameter("action")).thenReturn("redeem");
        when(request.getParameter("id")).thenReturn("99");
        Client client = new Client(1, "Jean", "jean@test.com", "hash", "0600", 150, "fidele", null);
        when(clientDAO.findById(1)).thenReturn(client);
        when(offerDAO.findById(99)).thenReturn(null);

        servlet.doGet(request, response);

        verify(response).sendRedirect("/gestionCoiffure/client/offers?error=offerNotAvailable");
        verify(offerDAO, never()).redeemOffer(anyInt(), anyInt());
    }

    @Test
    void testDoGet_redeemAction_offerInactive_redirect() throws ServletException, IOException {
        when(session.getAttribute("userType")).thenReturn("client");
        when(session.getAttribute("userId")).thenReturn(1);
        when(request.getParameter("action")).thenReturn("redeem");
        when(request.getParameter("id")).thenReturn("1");
        Client client = new Client(1, "Jean", "jean@test.com", "hash", "0600", 150, "fidele", null);
        Offer offer = new Offer(1, "Promo", "Desc", 100, false, LocalDateTime.now());
        when(clientDAO.findById(1)).thenReturn(client);
        when(offerDAO.findById(1)).thenReturn(offer);

        servlet.doGet(request, response);

        verify(response).sendRedirect("/gestionCoiffure/client/offers?error=offerNotAvailable");
        verify(offerDAO, never()).redeemOffer(anyInt(), anyInt());
    }

    @Test
    void testDoGet_redeemAction_insufficientPoints_redirect() throws ServletException, IOException {
        when(session.getAttribute("userType")).thenReturn("client");
        when(session.getAttribute("userId")).thenReturn(1);
        when(request.getParameter("action")).thenReturn("redeem");
        when(request.getParameter("id")).thenReturn("1");
        Client client = new Client(1, "Jean", "jean@test.com", "hash", "0600", 50, "standard", null);
        Offer offer = new Offer(1, "Promo", "Desc", 100, true, LocalDateTime.now());
        when(clientDAO.findById(1)).thenReturn(client);
        when(offerDAO.findById(1)).thenReturn(offer);

        servlet.doGet(request, response);

        verify(response).sendRedirect("/gestionCoiffure/client/offers?error=notEnoughPoints");
        verify(offerDAO, never()).redeemOffer(anyInt(), anyInt());
        verify(clientDAO, never()).updatePoints(anyInt(), anyInt());
    }

    @Test
    void testDoGet_redeemAction_success() throws ServletException, IOException {
        when(session.getAttribute("userType")).thenReturn("client");
        when(session.getAttribute("userId")).thenReturn(1);
        when(request.getParameter("action")).thenReturn("redeem");
        when(request.getParameter("id")).thenReturn("1");
        Client client = new Client(1, "Jean", "jean@test.com", "hash", "0600", 150, "fidele", null);
        Offer offer = new Offer(1, "Promo", "Desc", 100, true, LocalDateTime.now());
        when(clientDAO.findById(1)).thenReturn(client);
        when(offerDAO.findById(1)).thenReturn(offer);
        when(offerDAO.redeemOffer(1, 1)).thenReturn(true);
        when(clientDAO.updatePoints(1, 50)).thenReturn(true);

        servlet.doGet(request, response);

        verify(offerDAO).redeemOffer(1, 1);
        verify(clientDAO).updatePoints(1, 50);
        verify(response).sendRedirect("/gestionCoiffure/client/offers?success=redeemed");
    }

    @Test
    void testDoGet_redeemAction_exactPoints() throws ServletException, IOException {
        when(session.getAttribute("userType")).thenReturn("client");
        when(session.getAttribute("userId")).thenReturn(1);
        when(request.getParameter("action")).thenReturn("redeem");
        when(request.getParameter("id")).thenReturn("1");
        Client client = new Client(1, "Jean", "jean@test.com", "hash", "0600", 100, "fidele", null);
        Offer offer = new Offer(1, "Promo", "Desc", 100, true, LocalDateTime.now());
        when(clientDAO.findById(1)).thenReturn(client);
        when(offerDAO.findById(1)).thenReturn(offer);
        when(offerDAO.redeemOffer(1, 1)).thenReturn(true);
        when(clientDAO.updatePoints(1, 0)).thenReturn(true);

        servlet.doGet(request, response);

        verify(offerDAO).redeemOffer(1, 1);
        verify(clientDAO).updatePoints(1, 0);
        verify(response).sendRedirect("/gestionCoiffure/client/offers?success=redeemed");
    }

    @Test
    void testDoGet_redeemAction_failure_redirect() throws ServletException, IOException {
        when(session.getAttribute("userType")).thenReturn("client");
        when(session.getAttribute("userId")).thenReturn(1);
        when(request.getParameter("action")).thenReturn("redeem");
        when(request.getParameter("id")).thenReturn("1");
        Client client = new Client(1, "Jean", "jean@test.com", "hash", "0600", 150, "fidele", null);
        Offer offer = new Offer(1, "Promo", "Desc", 100, true, LocalDateTime.now());
        when(clientDAO.findById(1)).thenReturn(client);
        when(offerDAO.findById(1)).thenReturn(offer);
        when(offerDAO.redeemOffer(1, 1)).thenReturn(false);

        servlet.doGet(request, response);

        verify(offerDAO).redeemOffer(1, 1);
        verify(clientDAO, never()).updatePoints(anyInt(), anyInt());
        verify(response).sendRedirect("/gestionCoiffure/client/offers?error=redeemFailed");
    }
}
