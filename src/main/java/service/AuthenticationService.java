package service;

import dao.AdminDAO;
import dao.BarberDAO;
import dao.ClientDAO;
import model.Admin;
import model.Barber;
import model.Client;
import util.PasswordUtil;

public class AuthenticationService {
    
    private AdminDAO adminDAO;
    private BarberDAO barberDAO;
    private ClientDAO clientDAO;
    
    public AuthenticationService() {
        this.adminDAO = new AdminDAO();
        this.barberDAO = new BarberDAO();
        this.clientDAO = new ClientDAO();
    }
    
    /**
     * Authenticate admin
     */
    public Admin authenticateAdmin(String email, String password) {
        Admin admin = adminDAO.findByEmail(email);
        
        if (admin != null && PasswordUtil.verifyPassword(password, admin.getPasswordHash())) {
            return admin;
        }
        
        return null;
    }
    
    /**
     * Authenticate barber
     */
    public Barber authenticateBarber(String email, String password) {
        Barber barber = barberDAO.findByEmail(email);
        
        if (barber != null && PasswordUtil.verifyPassword(password, barber.getPasswordHash())) {
            // Check if barber is active
            if ("active".equals(barber.getStatus())) {
                return barber;
            } else {
                System.out.println("Barber account is inactive");
            }
        }
        
        return null;
    }
    
    /**
     * Authenticate client
     */
    public Client authenticateClient(String email, String password) {
        Client client = clientDAO.findByEmail(email);
        
        if (client != null && PasswordUtil.verifyPassword(password, client.getPasswordHash())) {
            return client;
        }
        
        return null;
    }
    
    /**
     * Register new client
     */
    public boolean registerClient(String name, String email, String password, String phone) {
        // Check if email already exists
        if (clientDAO.findByEmail(email) != null) {
            System.out.println("Email already exists");
            return false;
        }
        
        // Validate password
        if (!PasswordUtil.isValidPassword(password)) {
            System.out.println("Password does not meet requirements");
            return false;
        }
        
        // Create new client
        Client client = new Client();
        client.setName(name);
        client.setEmail(email);
        client.setPasswordHash(PasswordUtil.hashPassword(password));
        client.setPhone(phone);
        client.setPointsBalance(0);
        client.setLoyaltyStatus("standard");
        
        return clientDAO.create(client);
    }
    
    /**
     * Check if email exists in any user type
     */
    public boolean emailExists(String email) {
        return adminDAO.findByEmail(email) != null ||
               barberDAO.findByEmail(email) != null ||
               clientDAO.findByEmail(email) != null;
    }
}