package service;

public class Service {
    private int serviceId;
    private String name;
    private String description;
    private int duration; // in minutes
    private double price;
    private boolean isActive;
    
    // Constructors
    public Service() {
    }
    
    public Service(int serviceId, String name, String description, int duration, 
                   double price, boolean isActive) {
        this.serviceId = serviceId;
        this.name = name;
        this.description = description;
        this.duration = duration;
        this.price = price;
        this.isActive = isActive;
    }
    
    // Getters and Setters
    public int getServiceId() {
        return serviceId;
    }
    
    public void setServiceId(int serviceId) {
        this.serviceId = serviceId;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public int getDuration() {
        return duration;
    }
    
    public void setDuration(int duration) {
        this.duration = duration;
    }
    
    public double getPrice() {
        return price;
    }
    
    public void setPrice(double price) {
        this.price = price;
    }
    
    public boolean isActive() {
        return isActive;
    }
    
    public void setActive(boolean active) {
        isActive = active;
    }
    
    @Override
    public String toString() {
        return "Service{" +
                "serviceId=" + serviceId +
                ", name='" + name + '\'' +
                ", duration=" + duration +
                ", price=" + price +
                ", isActive=" + isActive +
                '}';
    }
}