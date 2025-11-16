package dao;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import model.Client;
import util.DatabaseUtil;

public class ClientDAO {
    
    /**
     * Find client by email
     */
    public Client findByEmail(String email) {
        String sql = "SELECT * FROM Client WHERE email = ?";
        
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return extractClientFromResultSet(rs);
            }
            
        } catch (SQLException e) {
            System.err.println("Error finding client by email: " + e.getMessage());
            e.printStackTrace();
        }
        
        return null;
    }
    
    /**
     * Find client by ID
     */
    public Client findById(int clientId) {
        String sql = "SELECT * FROM Client WHERE client_id = ?";
        
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, clientId);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return extractClientFromResultSet(rs);
            }
            
        } catch (SQLException e) {
            System.err.println("Error finding client by ID: " + e.getMessage());
            e.printStackTrace();
        }
        
        return null;
    }
    
    /**
     * Get all clients
     */
    public List<Client> findAll() {
        List<Client> clients = new ArrayList<>();
        String sql = "SELECT * FROM Client ORDER BY name";
        
        try (Connection conn = DatabaseUtil.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                clients.add(extractClientFromResultSet(rs));
            }
            
        } catch (SQLException e) {
            System.err.println("Error finding all clients: " + e.getMessage());
            e.printStackTrace();
        }
        
        return clients;
    }
    
    /**
     * Create new client (registration)
     */
    public boolean create(Client client) {
        String sql = "INSERT INTO Client (name, email, password_hash, phone, points_balance, loyalty_status) VALUES (?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setString(1, client.getName());
            stmt.setString(2, client.getEmail());
            stmt.setString(3, client.getPasswordHash());
            stmt.setString(4, client.getPhone());
            stmt.setInt(5, client.getPointsBalance());
            stmt.setString(6, client.getLoyaltyStatus());
            
            int rowsAffected = stmt.executeUpdate();
            
            if (rowsAffected > 0) {
                ResultSet generatedKeys = stmt.getGeneratedKeys();
                if (generatedKeys.next()) {
                    client.setClientId(generatedKeys.getInt(1));
                }
                return true;
            }
            
        } catch (SQLException e) {
            System.err.println("Error creating client: " + e.getMessage());
            e.printStackTrace();
        }
        
        return false;
    }
    
    /**
     * Update client profile
     */
    public boolean update(Client client) {
        String sql = "UPDATE Client SET name = ?, email = ?, phone = ? WHERE client_id = ?";
        
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, client.getName());
            stmt.setString(2, client.getEmail());
            stmt.setString(3, client.getPhone());
            stmt.setInt(4, client.getClientId());
            
            return stmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            System.err.println("Error updating client: " + e.getMessage());
            e.printStackTrace();
        }
        
        return false;
    }
    
    /**
     * Update client points
     */
    public boolean updatePoints(int clientId, int points) {
        String sql = "UPDATE Client SET points_balance = ? WHERE client_id = ?";
        
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, points);
            stmt.setInt(2, clientId);
            
            return stmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            System.err.println("Error updating client points: " + e.getMessage());
            e.printStackTrace();
        }
        
        return false;
    }
    
    /**
     * Update loyalty status
     */
    public boolean updateLoyaltyStatus(int clientId, String status) {
        String sql = "UPDATE Client SET loyalty_status = ? WHERE client_id = ?";
        
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, status);
            stmt.setInt(2, clientId);
            
            return stmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            System.err.println("Error updating loyalty status: " + e.getMessage());
            e.printStackTrace();
        }
        
        return false;
    }
    
    /**
     * Get appointment count for client (for loyalty program)
     */
    public int getCompletedAppointmentCount(int clientId) {
        String sql = "SELECT COUNT(*) as count FROM Appointment WHERE client_id = ? AND status = 'completed'";
        
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, clientId);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return rs.getInt("count");
            }
            
        } catch (SQLException e) {
            System.err.println("Error getting appointment count: " + e.getMessage());
            e.printStackTrace();
        }
        
        return 0;
    }
    
    /**
     * Extract Client object from ResultSet
     */
    private Client extractClientFromResultSet(ResultSet rs) throws SQLException {
        Client client = new Client();
        client.setClientId(rs.getInt("client_id"));
        client.setName(rs.getString("name"));
        client.setEmail(rs.getString("email"));
        client.setPasswordHash(rs.getString("password_hash"));
        client.setPhone(rs.getString("phone"));
        client.setPointsBalance(rs.getInt("points_balance"));
        client.setLoyaltyStatus(rs.getString("loyalty_status"));
        
        Timestamp timestamp = rs.getTimestamp("created_at");
        if (timestamp != null) {
            client.setCreatedAt(timestamp.toLocalDateTime());
        }
        
        return client;
    }
}