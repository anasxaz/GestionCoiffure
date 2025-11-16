package controller;


import java.io.IOException;
import java.time.LocalTime;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import dao.AvailabilityDAO;
import dao.BarberDAO;
import model.Availability;
import model.Barber;


@WebServlet("/admin/availability")
public class ManageAvailabilityServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private AvailabilityDAO availabilityDAO;
    private BarberDAO barberDAO;
    
    @Override
    public void init() throws ServletException {
        availabilityDAO = new AvailabilityDAO();
        barberDAO = new BarberDAO();
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
        if (action == null) action = "list";
        
        try {
            switch (action) {
                case "list":
                    listAvailability(request, response);
                    break;
                case "add":
                    showAddForm(request, response);
                    break;
                case "delete":
                    deleteAvailability(request, response);
                    break;
                default:
                    listAvailability(request, response);
            }
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Une erreur est survenue");
            listAvailability(request, response);
        }
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        HttpSession session = request.getSession(false);
        if (session == null || !"admin".equals(session.getAttribute("userType"))) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
        
        createAvailability(request, response);
    }
    
    private void listAvailability(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        List<Availability> availabilities = availabilityDAO.findAll();
        List<Barber> barbers = barberDAO.findAll();
        
        request.setAttribute("availabilities", availabilities);
        request.setAttribute("barbers", barbers);
        request.getRequestDispatcher("/WEB-INF/views/admin/availability.jsp").forward(request, response);
    }
    
    private void showAddForm(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        List<Barber> barbers = barberDAO.findAllActive();
        request.setAttribute("barbers", barbers);
        request.getRequestDispatcher("/WEB-INF/views/admin/availability-form.jsp").forward(request, response);
    }
    
    private void createAvailability(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        try {
            int barberId = Integer.parseInt(request.getParameter("barberId"));
            String dayOfWeek = request.getParameter("dayOfWeek");
            String startTimeStr = request.getParameter("startTime");
            String endTimeStr = request.getParameter("endTime");
            
            LocalTime startTime = LocalTime.parse(startTimeStr);
            LocalTime endTime = LocalTime.parse(endTimeStr);
            
            // Validate times
            if (endTime.isBefore(startTime) || endTime.equals(startTime)) {
                request.setAttribute("error", "L'heure de fin doit être après l'heure de début");
                showAddForm(request, response);
                return;
            }
            
            Availability availability = new Availability();
            availability.setBarberId(barberId);
            availability.setDayOfWeek(dayOfWeek);
            availability.setStartTime(startTime);
            availability.setEndTime(endTime);
            
            boolean success = availabilityDAO.create(availability);
            
            if (success) {
                response.sendRedirect(request.getContextPath() + "/admin/availability?success=created");
            } else {
                request.setAttribute("error", "Erreur lors de la création");
                showAddForm(request, response);
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Erreur: " + e.getMessage());
            showAddForm(request, response);
        }
    }
    
    private void deleteAvailability(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        try {
            int availabilityId = Integer.parseInt(request.getParameter("id"));
            boolean success = availabilityDAO.delete(availabilityId);
            
            if (success) {
                response.sendRedirect(request.getContextPath() + "/admin/availability?success=deleted");
            } else {
                response.sendRedirect(request.getContextPath() + "/admin/availability?error=deleteFailed");
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() + "/admin/availability?error=unknown");
        }
    }
}