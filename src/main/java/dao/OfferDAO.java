package dao;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import model.Offer;
import model.OfferRedemption;
import util.DatabaseUtil;

public class OfferDAO {
    
    /**
     * Find offer by ID
     */
    public Offer findById(int offerId) {
        String sql = "SELECT * FROM Offer WHERE offer_id = ?";
        
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, offerId);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return extractOfferFromResultSet(rs);
            }
            
        } catch (SQLException e) {
            System.err.println("Error finding offer by ID: " + e.getMessage());
            e.printStackTrace();
        }
        
        return null;
    }
    
    /**
     * Get all offers
     */
    public List<Offer> findAll() {
        List<Offer> offers = new ArrayList<>();
        String sql = "SELECT * FROM Offer ORDER BY points_required ASC";
        
        try (Connection conn = DatabaseUtil.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                offers.add(extractOfferFromResultSet(rs));
            }
            
        } catch (SQLException e) {
            System.err.println("Error finding all offers: " + e.getMessage());
            e.printStackTrace();
        }
        
        return offers;
    }
    
    /**
     * Get all active offers
     */
    public List<Offer> findAllActive() {
        List<Offer> offers = new ArrayList<>();
        String sql = "SELECT * FROM Offer WHERE is_active = TRUE ORDER BY points_required ASC";
        
        try (Connection conn = DatabaseUtil.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                offers.add(extractOfferFromResultSet(rs));
            }
            
        } catch (SQLException e) {
            System.err.println("Error finding active offers: " + e.getMessage());
            e.printStackTrace();
        }
        
        return offers;
    }
    
    /**
     * Get offers that client can afford
     */
    public List<Offer> findAffordableOffers(int clientPoints) {
        List<Offer> offers = new ArrayList<>();
        String sql = "SELECT * FROM Offer WHERE is_active = TRUE AND points_required <= ? ORDER BY points_required ASC";
        
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, clientPoints);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                offers.add(extractOfferFromResultSet(rs));
            }
            
        } catch (SQLException e) {
            System.err.println("Error finding affordable offers: " + e.getMessage());
            e.printStackTrace();
        }
        
        return offers;
    }
    
    /**
     * Create new offer
     */
    public boolean create(Offer offer) {
        String sql = "INSERT INTO Offer (title, description, points_required, is_active) VALUES (?, ?, ?, ?)";
        
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setString(1, offer.getTitle());
            stmt.setString(2, offer.getDescription());
            stmt.setInt(3, offer.getPointsRequired());
            stmt.setBoolean(4, offer.isActive());
            
            int rowsAffected = stmt.executeUpdate();
            
            if (rowsAffected > 0) {
                ResultSet generatedKeys = stmt.getGeneratedKeys();
                if (generatedKeys.next()) {
                    offer.setOfferId(generatedKeys.getInt(1));
                }
                return true;
            }
            
        } catch (SQLException e) {
            System.err.println("Error creating offer: " + e.getMessage());
            e.printStackTrace();
        }
        
        return false;
    }
    
    /**
     * Update offer
     */
    public boolean update(Offer offer) {
        String sql = "UPDATE Offer SET title = ?, description = ?, points_required = ?, is_active = ? WHERE offer_id = ?";
        
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, offer.getTitle());
            stmt.setString(2, offer.getDescription());
            stmt.setInt(3, offer.getPointsRequired());
            stmt.setBoolean(4, offer.isActive());
            stmt.setInt(5, offer.getOfferId());
            
            return stmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            System.err.println("Error updating offer: " + e.getMessage());
            e.printStackTrace();
        }
        
        return false;
    }
    
    /**
     * Delete offer
     */
    public boolean delete(int offerId) {
        String sql = "DELETE FROM Offer WHERE offer_id = ?";
        
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, offerId);
            return stmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            System.err.println("Error deleting offer: " + e.getMessage());
            e.printStackTrace();
        }
        
        return false;
    }
    
    /**
     * Toggle offer active status
     */
    public boolean toggleActive(int offerId) {
        String sql = "UPDATE Offer SET is_active = NOT is_active WHERE offer_id = ?";
        
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, offerId);
            return stmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            System.err.println("Error toggling offer status: " + e.getMessage());
            e.printStackTrace();
        }
        
        return false;
    }
    
    /**
     * Redeem offer for client (deducts points and creates redemption record)
     */
    public boolean redeemOffer(int clientId, int offerId) {
        Connection conn = null;
        PreparedStatement stmt1 = null;
        PreparedStatement stmt2 = null;
        PreparedStatement stmt3 = null;
        ResultSet rs = null;

        try {
            conn = DatabaseUtil.getConnection();
            conn.setAutoCommit(false); // Start transaction

            // 1. Get offer details to know points required
            String getOfferSql = "SELECT points_required FROM Offer WHERE offer_id = ?";
            stmt1 = conn.prepareStatement(getOfferSql);
            stmt1.setInt(1, offerId);
            rs = stmt1.executeQuery();

            if (!rs.next()) {
                conn.rollback();
                return false; // Offer not found
            }

            int pointsRequired = rs.getInt("points_required");
            rs.close();
            stmt1.close();

            // 2. Deduct points from client
            String deductPointsSql = "UPDATE Client SET points_balance = points_balance - ? WHERE client_id = ? AND points_balance >= ?";
            stmt2 = conn.prepareStatement(deductPointsSql);
            stmt2.setInt(1, pointsRequired);
            stmt2.setInt(2, clientId);
            stmt2.setInt(3, pointsRequired);

            int rowsUpdated = stmt2.executeUpdate();
            if (rowsUpdated == 0) {
                conn.rollback();
                return false; // Not enough points
            }
            stmt2.close();

            // 3. Create redemption record
            String insertRedemptionSql = "INSERT INTO OfferRedemption (client_id, offer_id) VALUES (?, ?)";
            stmt3 = conn.prepareStatement(insertRedemptionSql);
            stmt3.setInt(1, clientId);
            stmt3.setInt(2, offerId);

            boolean success = stmt3.executeUpdate() > 0;

            if (success) {
                conn.commit();
                return true;
            } else {
                conn.rollback();
                return false;
            }

        } catch (SQLException e) {
            System.err.println("Error redeeming offer: " + e.getMessage());
            e.printStackTrace();
            try {
                if (conn != null) {
                    conn.rollback();
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        } finally {
            try {
                if (rs != null) rs.close();
                if (stmt1 != null) stmt1.close();
                if (stmt2 != null) stmt2.close();
                if (stmt3 != null) stmt3.close();
                if (conn != null) {
                    conn.setAutoCommit(true);
                    conn.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return false;
    }
    
    /**
     * Get all redeemed offers for a client with details
     */
    public List<OfferRedemption> findRedeemedOffersByClient(int clientId) {
        List<OfferRedemption> redemptions = new ArrayList<>();
        String sql = "SELECT r.*, o.title, o.description, o.points_required " +
                     "FROM OfferRedemption r " +
                     "JOIN Offer o ON r.offer_id = o.offer_id " +
                     "WHERE r.client_id = ? " +
                     "ORDER BY r.redeemed_at DESC";

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, clientId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                redemptions.add(extractOfferRedemptionFromResultSet(rs));
            }

        } catch (SQLException e) {
            System.err.println("Error finding redeemed offers by client: " + e.getMessage());
            e.printStackTrace();
        }

        return redemptions;
    }

    /**
     * Get unused (available) redeemed offers for a client
     */
    public List<OfferRedemption> findUnusedRedeemedOffersByClient(int clientId) {
        List<OfferRedemption> redemptions = new ArrayList<>();
        String sql = "SELECT r.*, o.title, o.description, o.points_required " +
                     "FROM OfferRedemption r " +
                     "JOIN Offer o ON r.offer_id = o.offer_id " +
                     "WHERE r.client_id = ? AND r.is_used = FALSE " +
                     "ORDER BY r.redeemed_at DESC";

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, clientId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                redemptions.add(extractOfferRedemptionFromResultSet(rs));
            }

        } catch (SQLException e) {
            System.err.println("Error finding unused redeemed offers: " + e.getMessage());
            e.printStackTrace();
        }

        return redemptions;
    }

    /**
     * Mark redemption as used for an appointment
     */
    public boolean markRedemptionAsUsed(int redemptionId, int appointmentId) {
        String sql = "UPDATE OfferRedemption SET is_used = TRUE, appointment_id = ?, used_at = CURRENT_TIMESTAMP WHERE redemption_id = ?";

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, appointmentId);
            stmt.setInt(2, redemptionId);

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Error marking redemption as used: " + e.getMessage());
            e.printStackTrace();
        }

        return false;
    }

    /**
     * Find offer redemption by appointment ID
     */
    public OfferRedemption findRedemptionByAppointmentId(int appointmentId) {
        String sql = "SELECT r.*, o.title, o.description, o.points_required " +
                     "FROM OfferRedemption r " +
                     "JOIN Offer o ON r.offer_id = o.offer_id " +
                     "WHERE r.appointment_id = ?";

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, appointmentId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return extractOfferRedemptionFromResultSet(rs);
            }

        } catch (SQLException e) {
            System.err.println("Error finding redemption by appointment ID: " + e.getMessage());
            e.printStackTrace();
        }

        return null;
    }

    /**
     * Extract Offer object from ResultSet
     */
    private Offer extractOfferFromResultSet(ResultSet rs) throws SQLException {
        Offer offer = new Offer();
        offer.setOfferId(rs.getInt("offer_id"));
        offer.setTitle(rs.getString("title"));
        offer.setDescription(rs.getString("description"));
        offer.setPointsRequired(rs.getInt("points_required"));
        offer.setActive(rs.getBoolean("is_active"));

        Timestamp timestamp = rs.getTimestamp("created_at");
        if (timestamp != null) {
            offer.setCreatedAt(timestamp.toLocalDateTime());
        }

        return offer;
    }

    /**
     * Extract OfferRedemption object from ResultSet
     */
    private OfferRedemption extractOfferRedemptionFromResultSet(ResultSet rs) throws SQLException {
        OfferRedemption redemption = new OfferRedemption();
        redemption.setRedemptionId(rs.getInt("redemption_id"));
        redemption.setClientId(rs.getInt("client_id"));
        redemption.setOfferId(rs.getInt("offer_id"));
        redemption.setUsed(rs.getBoolean("is_used"));

        int appointmentId = rs.getInt("appointment_id");
        if (!rs.wasNull()) {
            redemption.setAppointmentId(appointmentId);
        }

        Timestamp redeemedAt = rs.getTimestamp("redeemed_at");
        if (redeemedAt != null) {
            redemption.setRedeemedAt(redeemedAt.toLocalDateTime());
        }

        Timestamp usedAt = rs.getTimestamp("used_at");
        if (usedAt != null) {
            redemption.setUsedAt(usedAt.toLocalDateTime());
        }

        // Set offer details if present
        redemption.setOfferTitle(rs.getString("title"));
        redemption.setOfferDescription(rs.getString("description"));
        redemption.setPointsRequired(rs.getInt("points_required"));

        return redemption;
    }
}