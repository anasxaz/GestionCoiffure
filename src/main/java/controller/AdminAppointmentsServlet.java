package controller;


import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import dao.AppointmentDAO;
import model.Appointment;


@WebServlet("/admin/appointments")
public class AdminAppointmentsServlet extends HttpServlet {
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
        if (session == null || !"admin".equals(session.getAttribute("userType"))) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
        
        String action = request.getParameter("action");
        
        if ("cancel".equals(action)) {
            cancelAppointment(request, response);
            return;
        }
        
        List<Appointment> appointments = appointmentDAO.findAll();
        request.setAttribute("appointments", appointments);
        request.getRequestDispatcher("/WEB-INF/views/admin/appointments.jsp").forward(request, response);
    }
    
    private void cancelAppointment(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        try {
            int appointmentId = Integer.parseInt(request.getParameter("id"));
            boolean success = appointmentDAO.cancel(appointmentId, "Annulé par l'administrateur");
            
            if (success) {
                response.sendRedirect(request.getContextPath() + "/admin/appointments?success=cancelled");
            } else {
                response.sendRedirect(request.getContextPath() + "/admin/appointments?error=failed");
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() + "/admin/appointments?error=unknown");
        }
    }
}