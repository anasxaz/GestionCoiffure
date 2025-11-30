package controller;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import util.DatabaseUtil;

@WebServlet("/admin/dashboard")
public class AdminDashboardServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("userType") == null ||
            !"admin".equals(session.getAttribute("userType"))) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        int totalBarbers = 0;
        int todayAppointments = 0;
        int totalClients = 0;
        int totalServices = 0;

        try {
            totalBarbers = getTotalBarbers();
            todayAppointments = getTodayAppointments();
            totalClients = getTotalClients();
            totalServices = getTotalServices();
        } catch (Exception e) {
            System.err.println("Error loading dashboard statistics: " + e.getMessage());
            e.printStackTrace();
        }

        request.setAttribute("totalBarbers", totalBarbers);
        request.setAttribute("todayAppointments", todayAppointments);
        request.setAttribute("totalClients", totalClients);
        request.setAttribute("totalServices", totalServices);

        request.getRequestDispatcher("/WEB-INF/views/admin/dashboard.jsp").forward(request, response);
    }

    private int getTotalBarbers() {
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement("SELECT COUNT(*) as count FROM Barber WHERE status = 'active'");
             ResultSet rs = stmt.executeQuery()) {

            if (rs.next()) {
                return rs.getInt("count");
            }
        } catch (Exception e) {
            System.err.println("Error getting total barbers: " + e.getMessage());
            e.printStackTrace();
        }
        return 0;
    }

    private int getTodayAppointments() {
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement("SELECT COUNT(*) as count FROM Appointment WHERE date = ?")) {

            stmt.setDate(1, java.sql.Date.valueOf(LocalDate.now()));
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("count");
                }
            }
        } catch (Exception e) {
            System.err.println("Error getting today's appointments: " + e.getMessage());
            e.printStackTrace();
        }
        return 0;
    }

    private int getTotalClients() {
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement("SELECT COUNT(*) as count FROM Client");
             ResultSet rs = stmt.executeQuery()) {

            if (rs.next()) {
                return rs.getInt("count");
            }
        } catch (Exception e) {
            System.err.println("Error getting total clients: " + e.getMessage());
            e.printStackTrace();
        }
        return 0;
    }

    private int getTotalServices() {
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement("SELECT COUNT(*) as count FROM Service WHERE is_active = TRUE");
             ResultSet rs = stmt.executeQuery()) {

            if (rs.next()) {
                return rs.getInt("count");
            }
        } catch (Exception e) {
            System.err.println("Error getting total services: " + e.getMessage());
            e.printStackTrace();
        }
        return 0;
    }
}