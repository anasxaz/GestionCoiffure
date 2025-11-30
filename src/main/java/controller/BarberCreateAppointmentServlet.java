package controller;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import dao.AppointmentDAO;
import dao.ClientDAO;
import dao.ServiceDAO;
import model.Appointment;
import model.Client;
import service.Service;

@WebServlet("/barber/create-appointment")
public class BarberCreateAppointmentServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private ClientDAO clientDAO;
    private ServiceDAO serviceDAO;
    private AppointmentDAO appointmentDAO;
    
    @Override
    public void init() throws ServletException {
        clientDAO = new ClientDAO();
        serviceDAO = new ServiceDAO();
        appointmentDAO = new AppointmentDAO();
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        HttpSession session = request.getSession(false);
        if (session == null || !"barber".equals(session.getAttribute("userType"))) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
        
        List<Client> clients = clientDAO.findAll();
        List<Service> services = serviceDAO.findAllActive();
        
        request.setAttribute("clients", clients);
        request.setAttribute("services", services);
        request.getRequestDispatcher("/WEB-INF/views/barber/create-appointment.jsp").forward(request, response);
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        HttpSession session = request.getSession(false);
        if (session == null || !"barber".equals(session.getAttribute("userType"))) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
        
        try {
            int barberId = (Integer) session.getAttribute("userId");
            int clientId = Integer.parseInt(request.getParameter("clientId"));
            int serviceId = Integer.parseInt(request.getParameter("serviceId"));
            String dateStr = request.getParameter("date");
            String timeStr = request.getParameter("startTime");

            LocalDate date = LocalDate.parse(dateStr);
            LocalTime startTime = LocalTime.parse(timeStr);

            Service service = serviceDAO.findById(serviceId);
            if (service == null) {
                request.setAttribute("error", "Service non trouvé");
                doGet(request, response);
                return;
            }

            LocalTime endTime = startTime.plusMinutes(service.getDuration());

            boolean isAvailable = appointmentDAO.isBarberAvailable(barberId, date, startTime, endTime);

            if (!isAvailable) {
                request.setAttribute("error", "Vous n'êtes pas disponible à ce créneau. Veuillez vérifier votre planning ou choisir un autre horaire.");
                doGet(request, response);
                return;
            }

            Appointment appointment = new Appointment();
            appointment.setClientId(clientId);
            appointment.setBarberId(barberId);
            appointment.setServiceId(serviceId);
            appointment.setDate(date);
            appointment.setStartTime(startTime);
            appointment.setEndTime(endTime);
            appointment.setStatus("confirmed");
            appointment.setFinalPrice(service.getPrice());

            boolean success = appointmentDAO.create(appointment);

            if (success) {
                response.sendRedirect(request.getContextPath() + "/barber/appointments?success=created");
            } else {
                request.setAttribute("error", "Erreur lors de la création du rendez-vous. Veuillez vérifier les informations.");
                doGet(request, response);
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Erreur: " + e.getMessage());
            doGet(request, response);
        }
    }
}