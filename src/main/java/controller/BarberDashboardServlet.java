package controller;

import java.io.IOException;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import dao.AppointmentDAO;
import model.Appointment;

@WebServlet("/barber/dashboard")
public class BarberDashboardServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private AppointmentDAO appointmentDAO;

    @Override
    public void init() throws ServletException {
        appointmentDAO = new AppointmentDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("userType") == null ||
            !"barber".equals(session.getAttribute("userType"))) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        Integer barberId = (Integer) session.getAttribute("userId");
        if (barberId == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        try {
             
            List<Appointment> allAppointments = appointmentDAO.findByBarberId(barberId);

            LocalDate today = LocalDate.now();
            List<Appointment> todayAppointments = appointmentDAO.findByBarberAndDate(barberId, today);
            int todayCount = todayAppointments.size();

            int confirmedCount = 0;
            int pendingCount = 0;
            for (Appointment apt : allAppointments) {
                if ("confirmed".equals(apt.getStatus())) {
                    confirmedCount++;
                } else if ("pending".equals(apt.getStatus())) {
                    pendingCount++;
                }
            }

            Set<Integer> monthlyClientIds = new HashSet<>();
            LocalDate firstDayOfMonth = LocalDate.now().withDayOfMonth(1);
            for (Appointment apt : allAppointments) {
                if (apt.getDate() != null && !apt.getDate().isBefore(firstDayOfMonth)) {
                    monthlyClientIds.add(apt.getClientId());
                }
            }
            int monthlyClientsCount = monthlyClientIds.size();

            request.setAttribute("todayAppointments", todayCount);
            request.setAttribute("confirmedAppointments", confirmedCount);
            request.setAttribute("pendingAppointments", pendingCount);
            request.setAttribute("monthlyClients", monthlyClientsCount);

        } catch (Exception e) {
            System.err.println("Error calculating dashboard statistics: " + e.getMessage());
            e.printStackTrace();
             
            request.setAttribute("todayAppointments", 0);
            request.setAttribute("confirmedAppointments", 0);
            request.setAttribute("pendingAppointments", 0);
            request.setAttribute("monthlyClients", 0);
        }

        request.getRequestDispatcher("/WEB-INF/views/barber/dashboard.jsp").forward(request, response);
    }
}
