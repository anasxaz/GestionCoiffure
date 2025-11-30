package controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import model.Admin;
import model.Barber;
import model.Client;
import service.AuthenticationService;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private AuthenticationService authService;
    
    @Override
    public void init() throws ServletException {
        authService = new AuthenticationService();
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
         
        request.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(request, response);
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String userType = request.getParameter("userType");  

        if (email == null || email.trim().isEmpty() || 
            password == null || password.trim().isEmpty() ||
            userType == null || userType.trim().isEmpty()) {
            request.setAttribute("error", "Tous les champs sont requis");
            request.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(request, response);
            return;
        }
        
        HttpSession session = request.getSession();
        
        try {
            switch (userType.toLowerCase()) {
                case "admin":
                    Admin admin = authService.authenticateAdmin(email, password);
                    if (admin != null) {
                        session.setAttribute("user", admin);
                        session.setAttribute("userType", "admin");
                        session.setAttribute("userId", admin.getAdminId());
                        session.setAttribute("userName", admin.getName());
                        response.sendRedirect(request.getContextPath() + "/admin/dashboard");
                        return;
                    }
                    break;
                    
                case "barber":
                    Barber barber = authService.authenticateBarber(email, password);
                    if (barber != null) {
                        session.setAttribute("user", barber);
                        session.setAttribute("userType", "barber");
                        session.setAttribute("userId", barber.getBarberId());
                        session.setAttribute("userName", barber.getName());
                        response.sendRedirect(request.getContextPath() + "/barber/dashboard");
                        return;
                    }
                    break;
                    
                case "client":
                    Client client = authService.authenticateClient(email, password);
                    if (client != null) {
                        session.setAttribute("user", client);
                        session.setAttribute("userType", "client");
                        session.setAttribute("userId", client.getClientId());
                        session.setAttribute("userName", client.getName());
                        response.sendRedirect(request.getContextPath() + "/client/dashboard");
                        return;
                    }
                    break;
                    
                default:
                    request.setAttribute("error", "Type d'utilisateur invalide");
                    request.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(request, response);
                    return;
            }

            request.setAttribute("error", "Email ou mot de passe incorrect");
            request.setAttribute("email", email);
            request.setAttribute("userType", userType);
            request.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(request, response);
            
        } catch (Exception e) {
            System.err.println("Login error: " + e.getMessage());
            e.printStackTrace();
            request.setAttribute("error", "Erreur lors de la connexion. Veuillez réessayer.");
            request.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(request, response);
        }
    }
}
