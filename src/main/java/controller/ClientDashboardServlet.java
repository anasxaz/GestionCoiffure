package controller;


import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import dao.ClientDAO;
import model.Client;

@WebServlet("/client/dashboard")
public class ClientDashboardServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private ClientDAO clientDAO;

    @Override
    public void init() throws ServletException {
        clientDAO = new ClientDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Check if user is logged in as client
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("userType") == null ||
            !"client".equals(session.getAttribute("userType"))) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        // Get client data
        int clientId = (Integer) session.getAttribute("userId");
        Client client = clientDAO.findById(clientId);

        request.setAttribute("client", client);

        // Forward to client dashboard page
        request.getRequestDispatcher("/WEB-INF/views/client/dashboard.jsp").forward(request, response);
    }
}