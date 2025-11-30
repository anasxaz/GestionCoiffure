package model;

import java.time.LocalDateTime;

public class Client {
    private int clientId;
    private String name;
    private String email;
    private String passwordHash;
    private String phone;
    private int pointsBalance;
    private String loyaltyStatus;  
    private LocalDateTime createdAt;

    public Client() {
    }
    
    public Client(int clientId, String name, String email, String passwordHash,
                  String phone, int pointsBalance, String loyaltyStatus, LocalDateTime createdAt) {
        this.clientId = clientId;
        this.name = name;
        this.email = email;
        this.passwordHash = passwordHash;
        this.phone = phone;
        this.pointsBalance = pointsBalance;
        this.loyaltyStatus = loyaltyStatus;
        this.createdAt = createdAt;
    }

    public int getClientId() {
        return clientId;
    }
    
    public void setClientId(int clientId) {
        this.clientId = clientId;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public String getPasswordHash() {
        return passwordHash;
    }
    
    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }
    
    public String getPhone() {
        return phone;
    }
    
    public void setPhone(String phone) {
        this.phone = phone;
    }
    
    public int getPointsBalance() {
        return pointsBalance;
    }
    
    public void setPointsBalance(int pointsBalance) {
        this.pointsBalance = pointsBalance;
    }
    
    public String getLoyaltyStatus() {
        return loyaltyStatus;
    }
    
    public void setLoyaltyStatus(String loyaltyStatus) {
        this.loyaltyStatus = loyaltyStatus;
    }
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    
    public boolean isFidele() {
        return "fidele".equals(loyaltyStatus);
    }
    
    @Override
    public String toString() {
        return "Client{" +
                "clientId=" + clientId +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                ", pointsBalance=" + pointsBalance +
                ", loyaltyStatus='" + loyaltyStatus + '\'' +
                '}';
    }
}