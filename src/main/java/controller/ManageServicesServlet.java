package controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import dao.ServiceDAO;
import service.Service;

@WebServlet("/admin/services")
public class ManageServicesServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private ServiceDAO serviceDAO;
    
    @Override
    public void init() throws ServletException {
        serviceDAO = new ServiceDAO();
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
                    listServices(request, response);
                    break;
                case "add":
                    showAddForm(request, response);
                    break;
                case "edit":
                    showEditForm(request, response);
                    break;
                case "delete":
                    deleteService(request, response);
                    break;
                case "toggle":
                    toggleService(request, response);
                    break;
                default:
                    listServices(request, response);
            }
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Une erreur est survenue");
            listServices(request, response);
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
        
        String action = request.getParameter("action");
        
        try {
            if ("create".equals(action)) {
                createService(request, response);
            } else if ("update".equals(action)) {
                updateService(request, response);
            }
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Une erreur est survenue");
            listServices(request, response);
        }
    }
    
    private void listServices(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

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

        List<Service> services = serviceDAO.findAll(page, pageSize);
        int totalServices = serviceDAO.getTotalCount();
        int totalPages = (int) Math.ceil((double) totalServices / pageSize);

        request.setAttribute("services", services);
        request.setAttribute("currentPage", page);
        request.setAttribute("totalPages", totalPages);
        request.setAttribute("totalServices", totalServices);
        request.setAttribute("pageSize", pageSize);

        request.getRequestDispatcher("/WEB-INF/views/admin/services.jsp").forward(request, response);
    }
    
    private void showAddForm(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        request.getRequestDispatcher("/WEB-INF/views/admin/service-form.jsp").forward(request, response);
    }
    
    private void showEditForm(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        int serviceId = Integer.parseInt(request.getParameter("id"));
        Service service = serviceDAO.findById(serviceId);
        
        if (service != null) {
            request.setAttribute("service", service);
            request.getRequestDispatcher("/WEB-INF/views/admin/service-form.jsp").forward(request, response);
        } else {
            request.setAttribute("error", "Service non trouvé");
            listServices(request, response);
        }
    }
    
    private void createService(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        String name = request.getParameter("name");
        String description = request.getParameter("description");
        String durationStr = request.getParameter("duration");
        String priceStr = request.getParameter("price");
        String activeStr = request.getParameter("isActive");
        
        if (name == null || name.trim().isEmpty() || durationStr == null || priceStr == null) {
            request.setAttribute("error", "Les champs nom, durée et prix sont requis");
            showAddForm(request, response);
            return;
        }
        
        try {
            Service service = new Service();
            service.setName(name);
            service.setDescription(description);
            service.setDuration(Integer.parseInt(durationStr));
            service.setPrice(Double.parseDouble(priceStr));
            service.setActive(activeStr != null);
            
            boolean success = serviceDAO.create(service);
            
            if (success) {
                response.sendRedirect(request.getContextPath() + "/admin/services?success=created");
            } else {
                request.setAttribute("error", "Erreur lors de la création");
                showAddForm(request, response);
            }
        } catch (NumberFormatException e) {
            request.setAttribute("error", "Format de nombre invalide");
            showAddForm(request, response);
        }
    }
    
    private void updateService(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        int serviceId = Integer.parseInt(request.getParameter("serviceId"));
        String name = request.getParameter("name");
        String description = request.getParameter("description");
        int duration = Integer.parseInt(request.getParameter("duration"));
        double price = Double.parseDouble(request.getParameter("price"));
        boolean isActive = request.getParameter("isActive") != null;
        
        Service service = new Service();
        service.setServiceId(serviceId);
        service.setName(name);
        service.setDescription(description);
        service.setDuration(duration);
        service.setPrice(price);
        service.setActive(isActive);
        
        boolean success = serviceDAO.update(service);
        
        if (success) {
            response.sendRedirect(request.getContextPath() + "/admin/services?success=updated");
        } else {
            request.setAttribute("error", "Erreur lors de la mise à jour");
            request.setAttribute("service", service);
            showEditForm(request, response);
        }
    }
    
    private void deleteService(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        int serviceId = Integer.parseInt(request.getParameter("id"));
        boolean success = serviceDAO.delete(serviceId);
        
        if (success) {
            response.sendRedirect(request.getContextPath() + "/admin/services?success=deleted");
        } else {
            response.sendRedirect(request.getContextPath() + "/admin/services?error=deleteFailed");
        }
    }
    
    private void toggleService(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        int serviceId = Integer.parseInt(request.getParameter("id"));
        serviceDAO.toggleActive(serviceId);
        response.sendRedirect(request.getContextPath() + "/admin/services");
    }
}