package controller;


import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import dao.OfferDAO;
import model.OfferRedemption;

@WebServlet("/client/dashboard")
public class ClientDashboardServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private OfferDAO offerDAO;

    @Override
    public void init() throws ServletException {
        offerDAO = new OfferDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        if (session == null || !"client".equals(session.getAttribute("userType"))) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        try {
            // Get client ID from session
            Integer clientId = (Integer) session.getAttribute("userId");

            // Get unused redeemed offers count
            List<OfferRedemption> unusedOffers = offerDAO.findUnusedRedeemedOffersByClient(clientId);
            int unusedOffersCount = unusedOffers != null ? unusedOffers.size() : 0;

            request.setAttribute("unusedOffersCount", unusedOffersCount);

        } catch (Exception e) {
            System.err.println("Error loading client dashboard data: " + e.getMessage());
            e.printStackTrace();
            request.setAttribute("unusedOffersCount", 0);
        }

        request.getRequestDispatcher("/WEB-INF/views/client/dashboard.jsp").forward(request, response);
    }
}
