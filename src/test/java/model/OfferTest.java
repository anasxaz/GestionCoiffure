package model;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class OfferTest {

    @Test
    void testConstructeurParDefaut() {
        Offer offer = new Offer();

        assertEquals(0, offer.getOfferId());
        assertNull(offer.getTitle());
        assertNull(offer.getDescription());
        assertEquals(0, offer.getPointsRequired());
        assertFalse(offer.isActive());
        assertNull(offer.getCreatedAt());
    }

    @Test
    void testConstructeurParametres() {
        LocalDateTime now = LocalDateTime.now();
        Offer offer = new Offer(1, "Promo 50%", "Réduction de 50%", 100, true, now);

        assertEquals(1, offer.getOfferId());
        assertEquals("Promo 50%", offer.getTitle());
        assertEquals("Réduction de 50%", offer.getDescription());
        assertEquals(100, offer.getPointsRequired());
        assertTrue(offer.isActive());
        assertEquals(now, offer.getCreatedAt());
    }

    @Test
    void testGettersSetters() {
        Offer offer = new Offer();
        LocalDateTime now = LocalDateTime.now();

        offer.setOfferId(42);
        offer.setTitle("Offre VIP");
        offer.setDescription("Pour les fidèles");
        offer.setPointsRequired(200);
        offer.setActive(true);
        offer.setCreatedAt(now);

        assertEquals(42, offer.getOfferId());
        assertEquals("Offre VIP", offer.getTitle());
        assertEquals("Pour les fidèles", offer.getDescription());
        assertEquals(200, offer.getPointsRequired());
        assertTrue(offer.isActive());
        assertEquals(now, offer.getCreatedAt());
    }

    @Test
    void testSetActive_toggle() {
        Offer offer = new Offer();

        assertFalse(offer.isActive());

        offer.setActive(true);
        assertTrue(offer.isActive());

        offer.setActive(false);
        assertFalse(offer.isActive());
    }

    @Test
    void testToString() {
        Offer offer = new Offer(1, "Promo", "Desc", 100, true, LocalDateTime.now());

        String str = offer.toString();

        assertTrue(str.contains("offerId=1"));
        assertTrue(str.contains("title='Promo'"));
        assertTrue(str.contains("pointsRequired=100"));
        assertTrue(str.contains("isActive=true"));
    }
}
