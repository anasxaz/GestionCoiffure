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

    public Admin authenticateAdmin(String email, String password) {
        Admin admin = adminDAO.findByEmail(email);
        
        if (admin != null && PasswordUtil.verifyPassword(password, admin.getPasswordHash())) {
            return admin;
        }
        
        return null;
    }

    public Barber authenticateBarber(String email, String password) {
        Barber barber = barberDAO.findByEmail(email);
        
        if (barber != null && PasswordUtil.verifyPassword(password, barber.getPasswordHash())) {
             
            if ("active".equals(barber.getStatus())) {
                return barber;
            } else {
            }
        }
        return null;
    }

    public Client authenticateClient(String email, String password) {
        Client client = clientDAO.findByEmail(email);
        
        if (client != null && PasswordUtil.verifyPassword(password, client.getPasswordHash())) {
            return client;
        }
        
        return null;
    }

    public boolean registerClient(String name, String email, String password, String phone) {
         
        if (clientDAO.findByEmail(email) != null) {
            return false;
        }
        if (!PasswordUtil.isValidPassword(password)) {
            return false;
        }
        Client client = new Client();
        client.setName(name);
        client.setEmail(email);
        client.setPasswordHash(PasswordUtil.hashPassword(password));
        client.setPhone(phone);
        client.setPointsBalance(0);
        client.setLoyaltyStatus("standard");
        
        return clientDAO.create(client);
    }

    public boolean emailExists(String email) {
        return adminDAO.findByEmail(email) != null ||
               barberDAO.findByEmail(email) != null ||
               clientDAO.findByEmail(email) != null;
    }
}