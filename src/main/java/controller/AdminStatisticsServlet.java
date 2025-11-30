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

import dao.AppointmentDAO;
import dao.BarberDAO;
import dao.ClientDAO;
import dao.ServiceDAO;
import util.DatabaseUtil;

@WebServlet("/admin/statistics")
public class AdminStatisticsServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private AppointmentDAO appointmentDAO;
    private BarberDAO barberDAO;
    private ClientDAO clientDAO;
    private ServiceDAO serviceDAO;
    
    @Override
    public void init() throws ServletException {
        appointmentDAO = new AppointmentDAO();
        barberDAO = new BarberDAO();
        clientDAO = new ClientDAO();
        serviceDAO = new ServiceDAO();
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        HttpSession session = request.getSession(false);
        if (session == null || !"admin".equals(session.getAttribute("userType"))) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
        
        try {
             
            int totalBarbers = barberDAO.findAll().size();
            int activeBarbers = barberDAO.findAllActive().size();
            int totalClients = clientDAO.findAll().size();
            int totalServices = serviceDAO.findAll().size();
            int totalAppointments = appointmentDAO.findAll().size();

            Map<String, Integer> appointmentsByStatus = getAppointmentsByStatus();

            double totalRevenue = getTotalRevenue();
            double monthlyRevenue = getMonthlyRevenue();

            List<Map<String, Object>> topServices = getTopServices();

            List<Map<String, Object>> topBarbers = getTopBarbers();

            List<Map<String, Object>> monthlyTrends = getMonthlyTrends();

            Map<String, Integer> loyaltyStats = getLoyaltyStatistics();

            List<Map<String, Object>> recentAppointments = getRecentAppointments();

            request.setAttribute("totalBarbers", totalBarbers);
            request.setAttribute("activeBarbers", activeBarbers);
            request.setAttribute("totalClients", totalClients);
            request.setAttribute("totalServices", totalServices);
            request.setAttribute("totalAppointments", totalAppointments);
            request.setAttribute("appointmentsByStatus", appointmentsByStatus);
            request.setAttribute("totalRevenue", totalRevenue);
            request.setAttribute("monthlyRevenue", monthlyRevenue);
            request.setAttribute("topServices", topServices);
            request.setAttribute("topBarbers", topBarbers);
            request.setAttribute("monthlyTrends", monthlyTrends);
            request.setAttribute("loyaltyStats", loyaltyStats);
            request.setAttribute("recentAppointments", recentAppointments);
            
            request.getRequestDispatcher("/WEB-INF/views/admin/statistics.jsp").forward(request, response);
            
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Erreur lors du chargement des statistiques");
            request.getRequestDispatcher("/WEB-INF/views/admin/statistics.jsp").forward(request, response);
        }
    }
    
    private Map<String, Integer> getAppointmentsByStatus() {
        Map<String, Integer> stats = new HashMap<>();
        try (Connection conn = DatabaseUtil.getConnection()) {
            String sql = "SELECT status, COUNT(*) as count FROM Appointment GROUP BY status";
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                stats.put(rs.getString("status"), rs.getInt("count"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return stats;
    }
    
    private double getTotalRevenue() {
        double revenue = 0;
        try (Connection conn = DatabaseUtil.getConnection()) {
            String sql = "SELECT SUM(s.price) as total FROM Appointment a " +
                        "JOIN Service s ON a.service_id = s.service_id " +
                        "WHERE a.status = 'completed'";
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                revenue = rs.getDouble("total");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return revenue;
    }
    
    private double getMonthlyRevenue() {
        double revenue = 0;
        try (Connection conn = DatabaseUtil.getConnection()) {
            String sql = "SELECT SUM(s.price) as total FROM Appointment a " +
                        "JOIN Service s ON a.service_id = s.service_id " +
                        "WHERE a.status = 'completed' AND MONTH(a.date) = MONTH(CURRENT_DATE()) " +
                        "AND YEAR(a.date) = YEAR(CURRENT_DATE())";
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                revenue = rs.getDouble("total");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return revenue;
    }
    
    private List<Map<String, Object>> getTopServices() {
        List<Map<String, Object>> services = new ArrayList<>();
        try (Connection conn = DatabaseUtil.getConnection()) {
            String sql = "SELECT s.name, COUNT(a.appointment_id) as count, SUM(s.price) as revenue " +
                        "FROM Service s LEFT JOIN Appointment a ON s.service_id = a.service_id " +
                        "WHERE a.status = 'completed' " +
                        "GROUP BY s.service_id ORDER BY count DESC LIMIT 5";
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                Map<String, Object> service = new HashMap<>();
                service.put("name", rs.getString("name"));
                service.put("count", rs.getInt("count"));
                service.put("revenue", rs.getDouble("revenue"));
                services.add(service);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return services;
    }
    
    private List<Map<String, Object>> getTopBarbers() {
        List<Map<String, Object>> barbers = new ArrayList<>();
        try (Connection conn = DatabaseUtil.getConnection()) {
            String sql = "SELECT b.name, COUNT(a.appointment_id) as count " +
                        "FROM Barber b LEFT JOIN Appointment a ON b.barber_id = a.barber_id " +
                        "WHERE a.status = 'completed' " +
                        "GROUP BY b.barber_id ORDER BY count DESC LIMIT 5";
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                Map<String, Object> barber = new HashMap<>();
                barber.put("name", rs.getString("name"));
                barber.put("count", rs.getInt("count"));
                barbers.add(barber);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return barbers;
    }
    
    private List<Map<String, Object>> getMonthlyTrends() {
        List<Map<String, Object>> trends = new ArrayList<>();
        try (Connection conn = DatabaseUtil.getConnection()) {
            String sql = "SELECT DATE_FORMAT(date, '%Y-%m') as month, COUNT(*) as count, " +
                        "SUM(CASE WHEN status='completed' THEN 1 ELSE 0 END) as completed " +
                        "FROM Appointment " +
                        "WHERE date >= DATE_SUB(CURRENT_DATE(), INTERVAL 6 MONTH) " +
                        "GROUP BY month ORDER BY month";
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                Map<String, Object> trend = new HashMap<>();
                trend.put("month", rs.getString("month"));
                trend.put("count", rs.getInt("count"));
                trend.put("completed", rs.getInt("completed"));
                trends.add(trend);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return trends;
    }
    
    private Map<String, Integer> getLoyaltyStatistics() {
        Map<String, Integer> stats = new HashMap<>();
        try (Connection conn = DatabaseUtil.getConnection()) {
            String sql = "SELECT loyalty_status, COUNT(*) as count FROM Client GROUP BY loyalty_status";
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                stats.put(rs.getString("loyalty_status"), rs.getInt("count"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return stats;
    }
    
    private List<Map<String, Object>> getRecentAppointments() {
        List<Map<String, Object>> appointments = new ArrayList<>();
        try (Connection conn = DatabaseUtil.getConnection()) {
            String sql = "SELECT a.date, a.start_time, c.name as client, b.name as barber, " +
                        "s.name as service, a.status " +
                        "FROM Appointment a " +
                        "JOIN Client c ON a.client_id = c.client_id " +
                        "JOIN Barber b ON a.barber_id = b.barber_id " +
                        "JOIN Service s ON a.service_id = s.service_id " +
                        "ORDER BY a.created_at DESC LIMIT 10";
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                Map<String, Object> apt = new HashMap<>();
                apt.put("date", rs.getDate("date"));
                apt.put("time", rs.getTime("start_time"));
                apt.put("client", rs.getString("client"));
                apt.put("barber", rs.getString("barber"));
                apt.put("service", rs.getString("service"));
                apt.put("status", rs.getString("status"));
                appointments.add(apt);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return appointments;
    }
}