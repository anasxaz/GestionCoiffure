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
import dao.ClientDAO;
import model.Client;


@WebServlet("/admin/clients")
public class ViewClientsServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private ClientDAO clientDAO;
    private AppointmentDAO appointmentDAO;
    
    @Override
    public void init() throws ServletException {
        clientDAO = new ClientDAO();
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
        
        List<Client> clients = clientDAO.findAll();
        
        // Get appointment count for each client
        Map<Integer, Integer> appointmentCounts = new HashMap<>();
        for (Client client : clients) {
            int count = clientDAO.getCompletedAppointmentCount(client.getClientId());
            appointmentCounts.put(client.getClientId(), count);
        }
        
        request.setAttribute("clients", clients);
        request.setAttribute("appointmentCounts", appointmentCounts);
        request.getRequestDispatcher("/WEB-INF/views/admin/clients.jsp").forward(request, response);
    }
}