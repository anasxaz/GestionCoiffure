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
import dao.ClientDAO;
import dao.OfferDAO;
import dao.ServiceDAO;
import model.Appointment;
import model.Client;
import model.OfferRedemption;
import service.Service;

@WebServlet("/barber/appointments")
public class BarberAppointmentsServlet extends HttpServlet {
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
        if (session == null || !"barber".equals(session.getAttribute("userType"))) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
        
        int barberId = (Integer) session.getAttribute("userId");
        
        String action = request.getParameter("action");
        
        if ("confirm".equals(action)) {
            confirmAppointment(request, response, barberId);
            return;
        } else if ("refuse".equals(action)) {
            refuseAppointment(request, response, barberId);
            return;
        } else if ("complete".equals(action)) {
            completeAppointment(request, response, barberId);
            return;
        }

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

        List<Appointment> appointments = appointmentDAO.findByBarberId(barberId, page, pageSize);
        int totalAppointments = appointmentDAO.getTotalCountByBarberId(barberId);
        int totalPages = (int) Math.ceil((double) totalAppointments / pageSize);

        java.util.Map<Integer, Service> serviceMap = new java.util.HashMap<>();
        java.util.Map<Integer, OfferRedemption> redemptionMap = new java.util.HashMap<>();

        for (Appointment apt : appointments) {
             
            if (!serviceMap.containsKey(apt.getServiceId())) {
                Service svc = serviceDAO.findById(apt.getServiceId());
                if (svc != null) {
                    serviceMap.put(apt.getServiceId(), svc);
                }
            }

            if (apt.getRedemptionId() != null && !redemptionMap.containsKey(apt.getRedemptionId())) {
                 
                List<OfferRedemption> redemptions = offerDAO.findRedeemedOffersByClient(apt.getClientId());
                for (OfferRedemption r : redemptions) {
                    if (r.getRedemptionId() == apt.getRedemptionId()) {
                        redemptionMap.put(apt.getRedemptionId(), r);
                        break;
                    }
                }
            }
        }

        request.setAttribute("appointments", appointments);
        request.setAttribute("serviceMap", serviceMap);
        request.setAttribute("redemptionMap", redemptionMap);
        request.setAttribute("currentPage", page);
        request.setAttribute("totalPages", totalPages);
        request.setAttribute("totalAppointments", totalAppointments);
        request.setAttribute("pageSize", pageSize);

        request.getRequestDispatcher("/WEB-INF/views/barber/appointments.jsp").forward(request, response);
    }
    
    private void confirmAppointment(HttpServletRequest request, HttpServletResponse response, int barberId) 
            throws ServletException, IOException {
        
        try {
            int appointmentId = Integer.parseInt(request.getParameter("id"));
            Appointment appointment = appointmentDAO.findById(appointmentId);
            
            if (appointment != null && appointment.getBarberId() == barberId) {
                appointmentDAO.updateStatus(appointmentId, "confirmed");
                response.sendRedirect(request.getContextPath() + "/barber/appointments?success=confirmed");
                return;
            }
            
            response.sendRedirect(request.getContextPath() + "/barber/appointments?error=notFound");
            
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() + "/barber/appointments?error=unknown");
        }
    }
    
    private void refuseAppointment(HttpServletRequest request, HttpServletResponse response, int barberId) 
            throws ServletException, IOException {
        
        try {
            int appointmentId = Integer.parseInt(request.getParameter("id"));
            Appointment appointment = appointmentDAO.findById(appointmentId);
            
            if (appointment != null && appointment.getBarberId() == barberId) {
                appointmentDAO.updateStatus(appointmentId, "refused");
                response.sendRedirect(request.getContextPath() + "/barber/appointments?success=refused");
                return;
            }
            
            response.sendRedirect(request.getContextPath() + "/barber/appointments?error=notFound");
            
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() + "/barber/appointments?error=unknown");
        }
    }
    
    private void completeAppointment(HttpServletRequest request, HttpServletResponse response, int barberId) 
            throws ServletException, IOException {
        
        try {
            int appointmentId = Integer.parseInt(request.getParameter("id"));
            Appointment appointment = appointmentDAO.findById(appointmentId);
            
            if (appointment != null && appointment.getBarberId() == barberId) {
                 
                appointmentDAO.updateStatus(appointmentId, "completed");

                int clientId = appointment.getClientId();
                ClientDAO clientDAO = new ClientDAO();
                Client client = clientDAO.findById(clientId);
                
                if (client != null) {
                    int pointsToAdd = 10;
                    int newBalance = client.getPointsBalance() + pointsToAdd;
                    clientDAO.updatePoints(clientId, newBalance);

                    int completedCount = clientDAO.getCompletedAppointmentCount(clientId);
                    if (completedCount >= 3 && !"fidele".equals(client.getLoyaltyStatus())) {
                        clientDAO.updateLoyaltyStatus(clientId, "fidele");
                    }
                }
                
                response.sendRedirect(request.getContextPath() + "/barber/appointments?success=completed");
                return;
            }
            
            response.sendRedirect(request.getContextPath() + "/barber/appointments?error=notFound");
            
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() + "/barber/appointments?error=unknown");
        }
    }
}