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
import dao.AvailabilityDAO;
import dao.BarberDAO;
import dao.OfferDAO;
import dao.ServiceDAO;
import model.Appointment;
import model.Availability;
import model.Barber;
import model.Offer;
import service.Service;



@WebServlet("/client/book-appointment")
public class BookAppointmentServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private BarberDAO barberDAO;
    private ServiceDAO serviceDAO;
    private AppointmentDAO appointmentDAO;
    private OfferDAO offerDAO;
    private AvailabilityDAO availabilityDAO;
    private dao.ClientDAO clientDAO;

    @Override
    public void init() throws ServletException {
        barberDAO = new BarberDAO();
        serviceDAO = new ServiceDAO();
        appointmentDAO = new AppointmentDAO();
        offerDAO = new OfferDAO();
        availabilityDAO = new AvailabilityDAO();
        clientDAO = new dao.ClientDAO();
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

        // Load client data
        model.Client client = clientDAO.findById(clientId);

        // Load barbers and services
        List<Barber> barbers = barberDAO.findAllActive();
        List<Service> services = serviceDAO.findAllActive();

        // Load unused redeemed offers (offers the client has already redeemed)
        List<model.OfferRedemption> redeemedOffers = offerDAO.findUnusedRedeemedOffersByClient(clientId);

        // Load availability for all barbers (for displaying available time slots)
        java.util.Map<Integer, List<Availability>> barberAvailability = new java.util.HashMap<>();
        for (Barber barber : barbers) {
            List<Availability> availability = availabilityDAO.findByBarberId(barber.getBarberId());
            barberAvailability.put(barber.getBarberId(), availability);
        }

        request.setAttribute("client", client);
        request.setAttribute("barbers", barbers);
        request.setAttribute("services", services);
        request.setAttribute("redeemedOffers", redeemedOffers);
        request.setAttribute("barberAvailability", barberAvailability);
        request.getRequestDispatcher("/WEB-INF/views/client/book-appointment.jsp").forward(request, response);
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        if (session == null || !"client".equals(session.getAttribute("userType"))) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        try {
            int clientId = (Integer) session.getAttribute("userId");
            int barberId = Integer.parseInt(request.getParameter("barberId"));
            int serviceId = Integer.parseInt(request.getParameter("serviceId"));
            String dateStr = request.getParameter("date");
            String timeStr = request.getParameter("startTime");

            // Optional: redemption ID if client selected a redeemed offer
            String redemptionIdStr = request.getParameter("redemptionId");
            Integer redemptionId = null;
            if (redemptionIdStr != null && !redemptionIdStr.trim().isEmpty()) {
                redemptionId = Integer.parseInt(redemptionIdStr);
            }

            LocalDate date = LocalDate.parse(dateStr);
            LocalTime startTime = LocalTime.parse(timeStr);

            // Get service to calculate end time and price
            Service service = serviceDAO.findById(serviceId);
            if (service == null) {
                request.setAttribute("error", "Service non trouvé");
                doGet(request, response);
                return;
            }

            LocalTime endTime = startTime.plusMinutes(service.getDuration());

            // Calculate final price (apply discount if redemption is used)
            double basePrice = service.getPrice();
            double finalPrice = basePrice;

            if (redemptionId != null) {
                // Apply 10% discount
                finalPrice = basePrice * 0.9;
            }

            // Check if barber is available (both schedule and appointments)
            boolean barberAvailable = appointmentDAO.isBarberAvailable(barberId, date, startTime, endTime);

            if (!barberAvailable) {
                Barber barber = barberDAO.findById(barberId);
                String barberName = (barber != null) ? barber.getName() : "Sélectionné";
                request.setAttribute("error", "Le coiffeur " + barberName +
                    " n'est pas disponible à ce créneau. Veuillez choisir un autre horaire ou un autre coiffeur.");
                doGet(request, response);
                return;
            }

            // Create appointment
            Appointment appointment = new Appointment();
            appointment.setClientId(clientId);
            appointment.setBarberId(barberId);
            appointment.setServiceId(serviceId);
            appointment.setDate(date);
            appointment.setStartTime(startTime);
            appointment.setEndTime(endTime);
            appointment.setStatus("pending");
            appointment.setRedemptionId(redemptionId);
            appointment.setFinalPrice(finalPrice);

            boolean success = appointmentDAO.create(appointment);

            if (success) {
                // If a redemption was used, mark it as used
                if (redemptionId != null) {
                    try {
                        offerDAO.markRedemptionAsUsed(redemptionId, appointment.getAppointmentId());
                    } catch (Exception e) {
                        // Continue anyway - appointment was created
                    }
                }

                response.sendRedirect(request.getContextPath() + "/client/appointments?success=created");
            } else {
                request.setAttribute("error", "Erreur lors de la création du rendez-vous. Veuillez réessayer.");
                doGet(request, response);
            }

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Erreur: " + e.getMessage());
            doGet(request, response);
        }
    }
}