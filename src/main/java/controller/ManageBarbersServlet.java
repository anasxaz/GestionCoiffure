package controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import dao.BarberDAO;
import model.Barber;
import util.PasswordUtil;

@WebServlet("/admin/barbers")
public class ManageBarbersServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private BarberDAO barberDAO;
    
    @Override
    public void init() throws ServletException {
        barberDAO = new BarberDAO();
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        // Check if user is logged in as admin
        HttpSession session = request.getSession(false);
        if (session == null || !"admin".equals(session.getAttribute("userType"))) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
        
        String action = request.getParameter("action");
        
        if (action == null) {
            action = "list";
        }
        
        try {
            switch (action) {
                case "list":
                    listBarbers(request, response);
                    break;
                case "add":
                    showAddForm(request, response);
                    break;
                case "edit":
                    showEditForm(request, response);
                    break;
                case "delete":
                    deleteBarber(request, response);
                    break;
                case "toggleStatus":
                    toggleBarberStatus(request, response);
                    break;
                default:
                    listBarbers(request, response);
            }
        } catch (Exception e) {
            System.err.println("Error in ManageBarbersServlet: " + e.getMessage());
            e.printStackTrace();
            request.setAttribute("error", "Une erreur est survenue");
            listBarbers(request, response);
        }
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        // Check if user is logged in as admin
        HttpSession session = request.getSession(false);
        if (session == null || !"admin".equals(session.getAttribute("userType"))) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
        
        String action = request.getParameter("action");
        
        try {
            if ("create".equals(action)) {
                createBarber(request, response);
            } else if ("update".equals(action)) {
                updateBarber(request, response);
            }
        } catch (Exception e) {
            System.err.println("Error in ManageBarbersServlet POST: " + e.getMessage());
            e.printStackTrace();
            request.setAttribute("error", "Une erreur est survenue");
            listBarbers(request, response);
        }
    }
    
    private void listBarbers(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Get pagination parameters
        int page = 1;
        int pageSize = 10;

        String pageParam = request.getParameter("page");
        if (pageParam != null && !pageParam.isEmpty()) {
            try {
                page = Integer.parseInt(pageParam);
                if (page < 1) page = 1;
            } catch (NumberFormatException e) {
                page = 1;
            }
        }

        // Get paginated barbers
        List<Barber> barbers = barberDAO.findAll(page, pageSize);
        int totalBarbers = barberDAO.getTotalCount();
        int totalPages = (int) Math.ceil((double) totalBarbers / pageSize);

        // Set attributes
        request.setAttribute("barbers", barbers);
        request.setAttribute("currentPage", page);
        request.setAttribute("totalPages", totalPages);
        request.setAttribute("totalBarbers", totalBarbers);
        request.setAttribute("pageSize", pageSize);

        request.getRequestDispatcher("/WEB-INF/views/admin/barbers.jsp").forward(request, response);
    }
    
    private void showAddForm(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        request.getRequestDispatcher("/WEB-INF/views/admin/barber-form.jsp").forward(request, response);
    }
    
    private void showEditForm(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        int barberId = Integer.parseInt(request.getParameter("id"));
        Barber barber = barberDAO.findById(barberId);
        
        if (barber != null) {
            request.setAttribute("barber", barber);
            request.getRequestDispatcher("/WEB-INF/views/admin/barber-form.jsp").forward(request, response);
        } else {
            request.setAttribute("error", "Coiffeur non trouvé");
            listBarbers(request, response);
        }
    }
    
    private void createBarber(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        String name = request.getParameter("name");
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String phone = request.getParameter("phone");
        String bio = request.getParameter("bio");
        String status = request.getParameter("status");
        
        // Validate input
        if (name == null || name.trim().isEmpty() ||
            email == null || email.trim().isEmpty() ||
            password == null || password.trim().isEmpty()) {
            
            request.setAttribute("error", "Les champs nom, email et mot de passe sont requis");
            showAddForm(request, response);
            return;
        }
        
        // Check if email already exists
        if (barberDAO.findByEmail(email) != null) {
            request.setAttribute("error", "Cet email est déjà utilisé");
            showAddForm(request, response);
            return;
        }
        
        // Validate password
        if (!PasswordUtil.isValidPassword(password)) {
            request.setAttribute("error", "Le mot de passe doit contenir au moins 6 caractères");
            showAddForm(request, response);
            return;
        }
        
        // Create barber
        Barber barber = new Barber();
        barber.setName(name);
        barber.setEmail(email);
        barber.setPasswordHash(PasswordUtil.hashPassword(password));
        barber.setPhone(phone);
        barber.setBio(bio);
        barber.setStatus(status != null ? status : "active");

        boolean success = barberDAO.create(barber);

        if (success) {
            // Redirect with barberId to show availability setup message
            response.sendRedirect(request.getContextPath() + "/admin/barbers?success=created&barberId=" + barber.getBarberId());
        } else {
            request.setAttribute("error", "Erreur lors de la création du coiffeur");
            showAddForm(request, response);
        }
    }
    
    private void updateBarber(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        int barberId = Integer.parseInt(request.getParameter("barberId"));
        String name = request.getParameter("name");
        String email = request.getParameter("email");
        String phone = request.getParameter("phone");
        String bio = request.getParameter("bio");
        String status = request.getParameter("status");
        
        Barber barber = barberDAO.findById(barberId);
        
        if (barber == null) {
            request.setAttribute("error", "Coiffeur non trouvé");
            listBarbers(request, response);
            return;
        }
        
        // Check if email is being changed and if new email exists
        if (!email.equals(barber.getEmail())) {
            Barber existingBarber = barberDAO.findByEmail(email);
            if (existingBarber != null && existingBarber.getBarberId() != barberId) {
                request.setAttribute("error", "Cet email est déjà utilisé");
                request.setAttribute("barber", barber);
                showEditForm(request, response);
                return;
            }
        }
        
        barber.setName(name);
        barber.setEmail(email);
        barber.setPhone(phone);
        barber.setBio(bio);
        barber.setStatus(status);
        
        boolean success = barberDAO.update(barber);
        
        if (success) {
            response.sendRedirect(request.getContextPath() + "/admin/barbers?success=updated");
        } else {
            request.setAttribute("error", "Erreur lors de la mise à jour");
            request.setAttribute("barber", barber);
            showEditForm(request, response);
        }
    }
    
    private void deleteBarber(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        int barberId = Integer.parseInt(request.getParameter("id"));
        boolean success = barberDAO.delete(barberId);
        
        if (success) {
            response.sendRedirect(request.getContextPath() + "/admin/barbers?success=deleted");
        } else {
            response.sendRedirect(request.getContextPath() + "/admin/barbers?error=deleteFailed");
        }
    }
    
    private void toggleBarberStatus(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        int barberId = Integer.parseInt(request.getParameter("id"));
        Barber barber = barberDAO.findById(barberId);
        
        if (barber != null) {
            String newStatus = "active".equals(barber.getStatus()) ? "inactive" : "active";
            barberDAO.updateStatus(barberId, newStatus);
        }
        
        response.sendRedirect(request.getContextPath() + "/admin/barbers");
    }
}