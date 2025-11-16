package controller;


import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import util.DatabaseUtil;


@WebServlet("/barber/clients")
public class BarberClientsServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        HttpSession session = request.getSession(false);
        if (session == null || !"barber".equals(session.getAttribute("userType"))) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
        
        int barberId = (Integer) session.getAttribute("userId");
        
        try {
            // Get clients who have had appointments with this barber
            List<Map<String, Object>> clients = getBarberClients(barberId);
            
            request.setAttribute("clients", clients);
            request.getRequestDispatcher("/WEB-INF/views/barber/clients.jsp").forward(request, response);
            
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Erreur lors du chargement des clients");
            request.getRequestDispatcher("/WEB-INF/views/barber/clients.jsp").forward(request, response);
        }
    }
    
    private List<Map<String, Object>> getBarberClients(int barberId) {
        List<Map<String, Object>> clients = new ArrayList<>();
        
        try (Connection conn = DatabaseUtil.getConnection()) {
            String sql = "SELECT DISTINCT c.client_id, c.name, c.email, c.phone, " +
                        "c.points_balance, c.loyalty_status, " +
                        "COUNT(a.appointment_id) as total_appointments, " +
                        "MAX(a.date) as last_visit " +
                        "FROM Client c " +
                        "JOIN Appointment a ON c.client_id = a.client_id " +
                        "WHERE a.barber_id = ? " +
                        "GROUP BY c.client_id " +
                        "ORDER BY last_visit DESC";
            
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, barberId);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                Map<String, Object> client = new HashMap<>();
                client.put("clientId", rs.getInt("client_id"));
                client.put("name", rs.getString("name"));
                client.put("email", rs.getString("email"));
                client.put("phone", rs.getString("phone"));
                client.put("pointsBalance", rs.getInt("points_balance"));
                client.put("loyaltyStatus", rs.getString("loyalty_status"));
                client.put("totalAppointments", rs.getInt("total_appointments"));
                client.put("lastVisit", rs.getDate("last_visit"));
                clients.add(client);
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return clients;
    }
}