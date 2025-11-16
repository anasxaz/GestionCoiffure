package model;

import java.time.LocalDateTime;

public class Offer {
    private int offerId;
    private String title;
    private String description;
    private int pointsRequired;
    private boolean isActive;
    private LocalDateTime createdAt;
    
    // Constructors
    public Offer() {
    }
    
    public Offer(int offerId, String title, String description, int pointsRequired,
                 boolean isActive, LocalDateTime createdAt) {
        this.offerId = offerId;
        this.title = title;
        this.description = description;
        this.pointsRequired = pointsRequired;
        this.isActive = isActive;
        this.createdAt = createdAt;
    }
    
    // Getters and Setters
    public int getOfferId() {
        return offerId;
    }
    
    public void setOfferId(int offerId) {
        this.offerId = offerId;
    }
    
    public String getTitle() {
        return title;
    }
    
    public void setTitle(String title) {
        this.title = title;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public int getPointsRequired() {
        return pointsRequired;
    }
    
    public void setPointsRequired(int pointsRequired) {
        this.pointsRequired = pointsRequired;
    }
    
    public boolean isActive() {
        return isActive;
    }
    
    public void setActive(boolean active) {
        isActive = active;
    }
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    
    @Override
    public String toString() {
        return "Offer{" +
                "offerId=" + offerId +
                ", title='" + title + '\'' +
                ", pointsRequired=" + pointsRequired +
                ", isActive=" + isActive +
                '}';
    }
}