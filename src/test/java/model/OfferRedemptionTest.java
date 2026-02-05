package model;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class OfferRedemptionTest {

    @Test
    void testConstructeurParDefaut() {
        OfferRedemption redemption = new OfferRedemption();

        assertEquals(0, redemption.getRedemptionId());
        assertEquals(0, redemption.getClientId());
        assertEquals(0, redemption.getOfferId());
        assertFalse(redemption.isUsed());
        assertNull(redemption.getAppointmentId());
        assertNull(redemption.getRedeemedAt());
        assertNull(redemption.getUsedAt());
        // champs hors constructeur
        assertNull(redemption.getOfferTitle());
        assertNull(redemption.getOfferDescription());
        assertEquals(0, redemption.getPointsRequired());
    }

    @Test
    void testConstructeurParametres() {
        LocalDateTime redeemed = LocalDateTime.now();
        LocalDateTime used = redeemed.plusHours(2);

        OfferRedemption redemption = new OfferRedemption(1, 10, 5, false, 42, redeemed, used);

        assertEquals(1, redemption.getRedemptionId());
        assertEquals(10, redemption.getClientId());
        assertEquals(5, redemption.getOfferId());
        assertFalse(redemption.isUsed());
        assertEquals(42, redemption.getAppointmentId());
        assertEquals(redeemed, redemption.getRedeemedAt());
        assertEquals(used, redemption.getUsedAt());
        // champs hors constructeur restent par défaut
        assertNull(redemption.getOfferTitle());
        assertNull(redemption.getOfferDescription());
        assertEquals(0, redemption.getPointsRequired());
    }

    @Test
    void testConstructeurParametres_appointmentIdNull() {
        LocalDateTime redeemed = LocalDateTime.now();

        OfferRedemption redemption = new OfferRedemption(2, 20, 3, true, null, redeemed, null);

        assertEquals(2, redemption.getRedemptionId());
        assertTrue(redemption.isUsed());
        assertNull(redemption.getAppointmentId());
        assertNull(redemption.getUsedAt());
    }

    @Test
    void testGettersSetters() {
        OfferRedemption redemption = new OfferRedemption();
        LocalDateTime now = LocalDateTime.now();

        redemption.setRedemptionId(7);
        redemption.setClientId(20);
        redemption.setOfferId(3);
        redemption.setUsed(true);
        redemption.setAppointmentId(99);
        redemption.setRedeemedAt(now);
        redemption.setUsedAt(now);
        redemption.setOfferTitle("Promo");
        redemption.setOfferDescription("Réduction spéciale");
        redemption.setPointsRequired(150);

        assertEquals(7, redemption.getRedemptionId());
        assertEquals(20, redemption.getClientId());
        assertEquals(3, redemption.getOfferId());
        assertTrue(redemption.isUsed());
        assertEquals(99, redemption.getAppointmentId());
        assertEquals(now, redemption.getRedeemedAt());
        assertEquals(now, redemption.getUsedAt());
        assertEquals("Promo", redemption.getOfferTitle());
        assertEquals("Réduction spéciale", redemption.getOfferDescription());
        assertEquals(150, redemption.getPointsRequired());
    }

    @Test
    void testSetUsed_toggle() {
        OfferRedemption redemption = new OfferRedemption();

        assertFalse(redemption.isUsed());

        redemption.setUsed(true);
        assertTrue(redemption.isUsed());

        redemption.setUsed(false);
        assertFalse(redemption.isUsed());
    }

    @Test
    void testAppointmentId_nullToggle() {
        OfferRedemption redemption = new OfferRedemption();

        assertNull(redemption.getAppointmentId());

        redemption.setAppointmentId(42);
        assertEquals(42, redemption.getAppointmentId());

        redemption.setAppointmentId(null);
        assertNull(redemption.getAppointmentId());
    }

    @Test
    void testToString() {
        LocalDateTime now = LocalDateTime.now();
        OfferRedemption redemption = new OfferRedemption(1, 10, 5, true, 42, now, now);

        String str = redemption.toString();

        assertTrue(str.contains("redemptionId=1"));
        assertTrue(str.contains("clientId=10"));
        assertTrue(str.contains("offerId=5"));
        assertTrue(str.contains("isUsed=true"));
        assertTrue(str.contains("appointmentId=42"));
    }
}
