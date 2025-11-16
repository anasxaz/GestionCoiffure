package controller;


import java.io.IOException;
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
import dao.OfferDAO;
import dao.ServiceDAO;
import model.Appointment;
import model.OfferRedemption;
import service.Service;



@WebServlet("/client/appointments")
public class ClientAppointmentsServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private AppointmentDAO appointmentDAO;
    private ServiceDAO serviceDAO;
    private OfferDAO offerDAO;

    @Override
    public void init() throws ServletException {
        appointmentDAO = new AppointmentDAO();
        serviceDAO = new ServiceDAO();
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
        
        int clientId = (Integer) session.getAttribute("userId");
        
        String action = request.getParameter("action");
        if ("cancel".equals(action)) {
            cancelAppointment(request, response, clientId);
            return;
        }
        
        List<Appointment> appointments = appointmentDAO.findByClientId(clientId);

        // Load service data for pricing
        Map<Integer, Service> serviceMap = new HashMap<>();
        Map<Integer, OfferRedemption> redemptionMap = new HashMap<>();

        for (Appointment apt : appointments) {
            // Load service if not already loaded
            if (!serviceMap.containsKey(apt.getServiceId())) {
                Service svc = serviceDAO.findById(apt.getServiceId());
                if (svc != null) {
                    serviceMap.put(apt.getServiceId(), svc);
                }
            }

            // Load offer redemption if exists
            if (!redemptionMap.containsKey(apt.getAppointmentId())) {
                OfferRedemption redemption = ((OfferDAO) offerDAO).findRedemptionByAppointmentId(apt.getAppointmentId());
                if (redemption != null) {
                    redemptionMap.put(apt.getAppointmentId(), redemption);
                }
            }
        }

        request.setAttribute("appointments", appointments);
        request.setAttribute("serviceMap", serviceMap);
        request.setAttribute("redemptionMap", redemptionMap);
        request.getRequestDispatcher("/WEB-INF/views/client/appointments.jsp").forward(request, response);
    }
    
    private void cancelAppointment(HttpServletRequest request, HttpServletResponse response, int clientId) 
            throws ServletException, IOException {
        
        try {
            int appointmentId = Integer.parseInt(request.getParameter("id"));
            Appointment appointment = appointmentDAO.findById(appointmentId);
            
            // Verify appointment belongs to client
            if (appointment != null && appointment.getClientId() == clientId) {
                boolean success = appointmentDAO.cancel(appointmentId, "Annulé par le client");
                if (success) {
                    response.sendRedirect(request.getContextPath() + "/client/appointments?success=cancelled");
                    return;
                }
            }
            
            response.sendRedirect(request.getContextPath() + "/client/appointments?error=cannotCancel");
            
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() + "/client/appointments?error=unknown");
        }
    }
}