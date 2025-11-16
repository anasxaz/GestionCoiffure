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
import model.Offer;


@WebServlet("/admin/offers")
public class ManageOffersServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private OfferDAO offerDAO;
    
    @Override
    public void init() throws ServletException {
        offerDAO = new OfferDAO();
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");

        HttpSession session = request.getSession(false);
        if (session == null || !"admin".equals(session.getAttribute("userType"))) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
        
        String action = request.getParameter("action");
        if (action == null) action = "list";
        
        try {
            switch (action) {
                case "list":
                    listOffers(request, response);
                    break;
                case "add":
                    showAddForm(request, response);
                    break;
                case "edit":
                    showEditForm(request, response);
                    break;
                case "delete":
                    deleteOffer(request, response);
                    break;
                case "toggle":
                    toggleOffer(request, response);
                    break;
                default:
                    listOffers(request, response);
            }
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Une erreur est survenue");
            listOffers(request, response);
        }
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");

        HttpSession session = request.getSession(false);
        if (session == null || !"admin".equals(session.getAttribute("userType"))) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
        
        String action = request.getParameter("action");
        
        try {
            if ("create".equals(action)) {
                createOffer(request, response);
            } else if ("update".equals(action)) {
                updateOffer(request, response);
            }
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Une erreur est survenue");
            listOffers(request, response);
        }
    }
    
    private void listOffers(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        List<Offer> offers = offerDAO.findAll();
        request.setAttribute("offers", offers);
        request.getRequestDispatcher("/WEB-INF/views/admin/offers.jsp").forward(request, response);
    }
    
    private void showAddForm(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        request.getRequestDispatcher("/WEB-INF/views/admin/offer-form.jsp").forward(request, response);
    }
    
    private void showEditForm(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        int offerId = Integer.parseInt(request.getParameter("id"));
        Offer offer = offerDAO.findById(offerId);
        
        if (offer != null) {
            request.setAttribute("offer", offer);
            request.getRequestDispatcher("/WEB-INF/views/admin/offer-form.jsp").forward(request, response);
        } else {
            request.setAttribute("error", "Offre non trouvée");
            listOffers(request, response);
        }
    }
    
    private void createOffer(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        String title = request.getParameter("title");
        String description = request.getParameter("description");
        int pointsRequired = Integer.parseInt(request.getParameter("pointsRequired"));
        boolean isActive = request.getParameter("isActive") != null;
        
        Offer offer = new Offer();
        offer.setTitle(title);
        offer.setDescription(description);
        offer.setPointsRequired(pointsRequired);
        offer.setActive(isActive);
        
        boolean success = offerDAO.create(offer);
        
        if (success) {
            response.sendRedirect(request.getContextPath() + "/admin/offers?success=created");
        } else {
            request.setAttribute("error", "Erreur lors de la création");
            showAddForm(request, response);
        }
    }
    
    private void updateOffer(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        int offerId = Integer.parseInt(request.getParameter("offerId"));
        String title = request.getParameter("title");
        String description = request.getParameter("description");
        int pointsRequired = Integer.parseInt(request.getParameter("pointsRequired"));
        boolean isActive = request.getParameter("isActive") != null;
        
        Offer offer = new Offer();
        offer.setOfferId(offerId);
        offer.setTitle(title);
        offer.setDescription(description);
        offer.setPointsRequired(pointsRequired);
        offer.setActive(isActive);
        
        boolean success = offerDAO.update(offer);
        
        if (success) {
            response.sendRedirect(request.getContextPath() + "/admin/offers?success=updated");
        } else {
            request.setAttribute("error", "Erreur lors de la mise à jour");
            request.setAttribute("offer", offer);
            showEditForm(request, response);
        }
    }
    
    private void deleteOffer(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        int offerId = Integer.parseInt(request.getParameter("id"));
        boolean success = offerDAO.delete(offerId);
        
        if (success) {
            response.sendRedirect(request.getContextPath() + "/admin/offers?success=deleted");
        } else {
            response.sendRedirect(request.getContextPath() + "/admin/offers?error=deleteFailed");
        }
    }
    
    private void toggleOffer(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        int offerId = Integer.parseInt(request.getParameter("id"));
        offerDAO.toggleActive(offerId);
        response.sendRedirect(request.getContextPath() + "/admin/offers");
    }
}