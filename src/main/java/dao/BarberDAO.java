package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import model.Barber;
import util.DatabaseUtil;

public class BarberDAO {

    public Barber findByEmail(String email) {
        String sql = "SELECT * FROM Barber WHERE email = ?";
        
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return extractBarberFromResultSet(rs);
            }
            
        } catch (SQLException e) {
            System.err.println("Error finding barber by email: " + e.getMessage());
            e.printStackTrace();
        }
        
        return null;
    }

    public Barber findById(int barberId) {
        String sql = "SELECT * FROM Barber WHERE barber_id = ?";
        
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, barberId);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return extractBarberFromResultSet(rs);
            }
            
        } catch (SQLException e) {
            System.err.println("Error finding barber by ID: " + e.getMessage());
            e.printStackTrace();
        }
        
        return null;
    }

    public List<Barber> findAll() {
        List<Barber> barbers = new ArrayList<>();
        String sql = "SELECT * FROM Barber ORDER BY name";
        
        try (Connection conn = DatabaseUtil.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                barbers.add(extractBarberFromResultSet(rs));
            }
            
        } catch (SQLException e) {
            System.err.println("Error finding all barbers: " + e.getMessage());
            e.printStackTrace();
        }
        
        return barbers;
    }

    public List<Barber> findAllActive() {
        List<Barber> barbers = new ArrayList<>();
        String sql = "SELECT * FROM Barber WHERE status = 'active' ORDER BY name";

        try (Connection conn = DatabaseUtil.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                barbers.add(extractBarberFromResultSet(rs));
            }

        } catch (SQLException e) {
            System.err.println("Error finding active barbers: " + e.getMessage());
            e.printStackTrace();
        }

        return barbers;
    }

    public List<Barber> findAll(int page, int pageSize) {
        List<Barber> barbers = new ArrayList<>();
        int offset = (page - 1) * pageSize;
        String sql = "SELECT * FROM Barber ORDER BY name LIMIT ? OFFSET ?";

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, pageSize);
            stmt.setInt(2, offset);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                barbers.add(extractBarberFromResultSet(rs));
            }

        } catch (SQLException e) {
            System.err.println("Error finding paginated barbers: " + e.getMessage());
            e.printStackTrace();
        }

        return barbers;
    }

    public int getTotalCount() {
        String sql = "SELECT COUNT(*) as total FROM Barber";

        try (Connection conn = DatabaseUtil.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            if (rs.next()) {
                return rs.getInt("total");
            }

        } catch (SQLException e) {
            System.err.println("Error getting barber count: " + e.getMessage());
            e.printStackTrace();
        }

        return 0;
    }

    public boolean create(Barber barber) {
        String sql = "INSERT INTO Barber (name, email, password_hash, phone, bio, status) VALUES (?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setString(1, barber.getName());
            stmt.setString(2, barber.getEmail());
            stmt.setString(3, barber.getPasswordHash());
            stmt.setString(4, barber.getPhone());
            stmt.setString(5, barber.getBio());
            stmt.setString(6, barber.getStatus());
            
            int rowsAffected = stmt.executeUpdate();
            
            if (rowsAffected > 0) {
                ResultSet generatedKeys = stmt.getGeneratedKeys();
                if (generatedKeys.next()) {
                    barber.setBarberId(generatedKeys.getInt(1));
                }
                return true;
            }
            
        } catch (SQLException e) {
            System.err.println("Error creating barber: " + e.getMessage());
            e.printStackTrace();
        }
        
        return false;
    }

    public boolean update(Barber barber) {
        String sql = "UPDATE Barber SET name = ?, email = ?, phone = ?, bio = ?, status = ? WHERE barber_id = ?";
        
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, barber.getName());
            stmt.setString(2, barber.getEmail());
            stmt.setString(3, barber.getPhone());
            stmt.setString(4, barber.getBio());
            stmt.setString(5, barber.getStatus());
            stmt.setInt(6, barber.getBarberId());
            
            return stmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            System.err.println("Error updating barber: " + e.getMessage());
            e.printStackTrace();
        }
        
        return false;
    }

    public boolean delete(int barberId) {
        String sql = "DELETE FROM Barber WHERE barber_id = ?";
        
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, barberId);
            return stmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            System.err.println("Error deleting barber: " + e.getMessage());
            e.printStackTrace();
        }
        
        return false;
    }

    public boolean updateStatus(int barberId, String status) {
        String sql = "UPDATE Barber SET status = ? WHERE barber_id = ?";
        
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, status);
            stmt.setInt(2, barberId);
            
            return stmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            System.err.println("Error updating barber status: " + e.getMessage());
            e.printStackTrace();
        }
        
        return false;
    }

    private Barber extractBarberFromResultSet(ResultSet rs) throws SQLException {
        Barber barber = new Barber();
        barber.setBarberId(rs.getInt("barber_id"));
        barber.setName(rs.getString("name"));
        barber.setEmail(rs.getString("email"));
        barber.setPasswordHash(rs.getString("password_hash"));
        barber.setPhone(rs.getString("phone"));
        barber.setBio(rs.getString("bio"));
        barber.setStatus(rs.getString("status"));
        
        Timestamp timestamp = rs.getTimestamp("created_at");
        if (timestamp != null) {
            barber.setCreatedAt(timestamp.toLocalDateTime());
        }
        
        return barber;
    }
}