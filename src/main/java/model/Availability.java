package model;

import java.time.LocalTime;

public class Availability {
    private int availabilityId;
    private int barberId;
    private String dayOfWeek;  
    private LocalTime startTime;
    private LocalTime endTime;

    private String barberName;

    public Availability() {
    }
    
    public Availability(int availabilityId, int barberId, String dayOfWeek,
                        LocalTime startTime, LocalTime endTime) {
        this.availabilityId = availabilityId;
        this.barberId = barberId;
        this.dayOfWeek = dayOfWeek;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public int getAvailabilityId() {
        return availabilityId;
    }
    
    public void setAvailabilityId(int availabilityId) {
        this.availabilityId = availabilityId;
    }
    
    public int getBarberId() {
        return barberId;
    }
    
    public void setBarberId(int barberId) {
        this.barberId = barberId;
    }
    
    public String getDayOfWeek() {
        return dayOfWeek;
    }
    
    public void setDayOfWeek(String dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
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
    
    public String getBarberName() {
        return barberName;
    }
    
    public void setBarberName(String barberName) {
        this.barberName = barberName;
    }
    
    @Override
    public String toString() {
        return "Availability{" +
                "availabilityId=" + availabilityId +
                ", barberId=" + barberId +
                ", dayOfWeek='" + dayOfWeek + '\'' +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                '}';
    }
}