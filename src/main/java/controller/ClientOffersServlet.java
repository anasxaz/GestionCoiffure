package controller;


import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import dao.ClientDAO;
import dao.OfferDAO;
import model.Client;
import model.Offer;
import model.OfferRedemption;

@WebServlet("/client/offers")
public class ClientOffersServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private OfferDAO offerDAO;
    private ClientDAO clientDAO;
    
    @Override
    public void init() throws ServletException {
        offerDAO = new OfferDAO();
        clientDAO = new ClientDAO();
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        if (session == null || !"client".equals(session.getAttribute("userType"))) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        int clientId = (Integer) session.getAttribute("userId");
        Client client = clientDAO.findById(clientId);

        String action = request.getParameter("action");

        if ("redeem".equals(action)) {
            redeemOffer(request, response, client);
            return;
        }

        // Get all active offers
        List<Offer> offers = offerDAO.findAllActive();

        // Get redeemed offers (unused ones that can be applied to appointments)
        List<OfferRedemption> redeemedOffers = offerDAO.findUnusedRedeemedOffersByClient(clientId);

        request.setAttribute("client", client);
        request.setAttribute("offers", offers);
        request.setAttribute("redeemedOffers", redeemedOffers);
        request.getRequestDispatcher("/WEB-INF/views/client/offers.jsp").forward(request, response);
    }
    
    private void redeemOffer(HttpServletRequest request, HttpServletResponse response, Client client) 
            throws ServletException, IOException {
        
        try {
            int offerId = Integer.parseInt(request.getParameter("id"));
            Offer offer = offerDAO.findById(offerId);
            
            if (offer == null || !offer.isActive()) {
                response.sendRedirect(request.getContextPath() + "/client/offers?error=offerNotAvailable");
                return;
            }
            
            // Check if client has enough points
            if (client.getPointsBalance() < offer.getPointsRequired()) {
                response.sendRedirect(request.getContextPath() + "/client/offers?error=notEnoughPoints");
                return;
            }
            
            // Redeem offer
            boolean redeemed = offerDAO.redeemOffer(client.getClientId(), offerId);
            
            if (redeemed) {
                // Deduct points
                int newBalance = client.getPointsBalance() - offer.getPointsRequired();
                clientDAO.updatePoints(client.getClientId(), newBalance);
                
                response.sendRedirect(request.getContextPath() + "/client/offers?success=redeemed");
            } else {
                response.sendRedirect(request.getContextPath() + "/client/offers?error=redeemFailed");
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() + "/client/offers?error=unknown");
        }
    }
}