package model;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.LocalDateTime;

public class Appointment {
    private int appointmentId;
    private int clientId;
    private int barberId;
    private int serviceId;
    private LocalDate date;
    private LocalTime startTime;
    private LocalTime endTime;
    private String status;  
    private Integer redemptionId;  
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String cancellationReason;

    private String clientName;
    private String barberName;
    private String serviceName;
    private Double servicePrice;
    private Double finalPrice;  

    public Appointment() {
    }
    
    public Appointment(int appointmentId, int clientId, int barberId, int serviceId,
                       LocalDate date, LocalTime startTime, LocalTime endTime, String status,
                       LocalDateTime createdAt, LocalDateTime updatedAt, String cancellationReason) {
        this.appointmentId = appointmentId;
        this.clientId = clientId;
        this.barberId = barberId;
        this.serviceId = serviceId;
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
        this.status = status;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.cancellationReason = cancellationReason;
    }

    public int getAppointmentId() {
        return appointmentId;
    }
    
    public void setAppointmentId(int appointmentId) {
        this.appointmentId = appointmentId;
    }
    
    public int getClientId() {
        return clientId;
    }
    
    public void setClientId(int clientId) {
        this.clientId = clientId;
    }
    
    public int getBarberId() {
        return barberId;
    }
    
    public void setBarberId(int barberId) {
        this.barberId = barberId;
    }
    
    public int getServiceId() {
        return serviceId;
    }
    
    public void setServiceId(int serviceId) {
        this.serviceId = serviceId;
    }
    
    public LocalDate getDate() {
        return date;
    }
    
    public void setDate(LocalDate date) {
        this.date = date;
    }
    
    public LocalTime getStartTime() {
        return startTime;
    }
    
    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }
    
    public LocalTime getEndTime() {
        return endTime;
    }
    
    public void setEndTime(LocalTime endTime) {
        this.endTime = endTime;
    }
    
    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    
    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
    
    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
    
    public String getCancellationReason() {
        return cancellationReason;
    }

    public void setCancellationReason(String cancellationReason) {
        this.cancellationReason = cancellationReason;
    }

    public Integer getRedemptionId() {
        return redemptionId;
    }

    public void setRedemptionId(Integer redemptionId) {
        this.redemptionId = redemptionId;
    }

    public String getClientName() {
        return clientName;
    }
    
    public void setClientName(String clientName) {
        this.clientName = clientName;
    }
    
    public String getBarberName() {
        return barberName;
    }
    
    public void setBarberName(String barberName) {
        this.barberName = barberName;
    }
    
    public String getServiceName() {
        return serviceName;
    }
    
    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public Double getServicePrice() {
        return servicePrice;
    }

    public void setServicePrice(Double servicePrice) {
        this.servicePrice = servicePrice;
    }

    public Double getFinalPrice() {
        return finalPrice;
    }

    public void setFinalPrice(Double finalPrice) {
        this.finalPrice = finalPrice;
    }

    public boolean isPending() {
        return "pending".equals(status);
    }
    
    public boolean isConfirmed() {
        return "confirmed".equals(status);
    }
    
    public boolean isCancelled() {
        return "cancelled".equals(status);
    }
    
    public boolean isCompleted() {
        return "completed".equals(status);
    }
    
    @Override
    public String toString() {
        return "Appointment{" +
                "appointmentId=" + appointmentId +
                ", clientId=" + clientId +
                ", barberId=" + barberId +
                ", serviceId=" + serviceId +
                ", date=" + date +
                ", startTime=" + startTime +
                ", status='" + status + '\'' +
                '}';
    }
}