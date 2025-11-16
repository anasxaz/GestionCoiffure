package model;

import java.time.LocalDateTime;

public class OfferRedemption {
    private int redemptionId;
    private int clientId;
    private int offerId;
    private boolean isUsed;
    private Integer appointmentId; // Nullable - set when the offer is used in an appointment
    private LocalDateTime redeemedAt;
    private LocalDateTime usedAt;

    // For display purposes
    private String offerTitle;
    private String offerDescription;
    private int pointsRequired;

    // Constructors
    public OfferRedemption() {
    }

    public OfferRedemption(int redemptionId, int clientId, int offerId, boolean isUsed,
                          Integer appointmentId, LocalDateTime redeemedAt, LocalDateTime usedAt) {
        this.redemptionId = redemptionId;
        this.clientId = clientId;
        this.offerId = offerId;
        this.isUsed = isUsed;
        this.appointmentId = appointmentId;
        this.redeemedAt = redeemedAt;
        this.usedAt = usedAt;
    }

    // Getters and Setters
    public int getRedemptionId() {
        return redemptionId;
    }

    public void setRedemptionId(int redemptionId) {
        this.redemptionId = redemptionId;
    }

    public int getClientId() {
        return clientId;
    }

    public void setClientId(int clientId) {
        this.clientId = clientId;
    }

    public int getOfferId() {
        return offerId;
    }

    public void setOfferId(int offerId) {
        this.offerId = offerId;
    }

    public boolean isUsed() {
        return isUsed;
    }

    public void setUsed(boolean used) {
        isUsed = used;
    }

    public Integer getAppointmentId() {
        return appointmentId;
    }

    public void setAppointmentId(Integer appointmentId) {
        this.appointmentId = appointmentId;
    }

    public LocalDateTime getRedeemedAt() {
        return redeemedAt;
    }

    public void setRedeemedAt(LocalDateTime redeemedAt) {
        this.redeemedAt = redeemedAt;
    }

    public LocalDateTime getUsedAt() {
        return usedAt;
    }

    public void setUsedAt(LocalDateTime usedAt) {
        this.usedAt = usedAt;
    }

    public String getOfferTitle() {
        return offerTitle;
    }

    public void setOfferTitle(String offerTitle) {
        this.offerTitle = offerTitle;
    }

    public String getOfferDescription() {
        return offerDescription;
    }

    public void setOfferDescription(String offerDescription) {
        this.offerDescription = offerDescription;
    }

    public int getPointsRequired() {
        return pointsRequired;
    }

    public void setPointsRequired(int pointsRequired) {
        this.pointsRequired = pointsRequired;
    }

    @Override
    public String toString() {
        return "OfferRedemption{" +
                "redemptionId=" + redemptionId +
                ", clientId=" + clientId +
                ", offerId=" + offerId +
                ", isUsed=" + isUsed +
                ", appointmentId=" + appointmentId +
                ", redeemedAt=" + redeemedAt +
                '}';
    }
}
