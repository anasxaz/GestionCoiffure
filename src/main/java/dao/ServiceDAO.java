package dao;



import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import service.Service;
import util.DatabaseUtil;

public class ServiceDAO {
    
    /**
     * Find service by ID
     */
    public Service findById(int serviceId) {
        String sql = "SELECT * FROM Service WHERE service_id = ?";
        
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, serviceId);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return extractServiceFromResultSet(rs);
            }
            
        } catch (SQLException e) {
            System.err.println("Error finding service by ID: " + e.getMessage());
            e.printStackTrace();
        }
        
        return null;
    }
    
    /**
     * Get all services
     */
    public List<Service> findAll() {
        List<Service> services = new ArrayList<>();
        String sql = "SELECT * FROM Service ORDER BY name";
        
        try (Connection conn = DatabaseUtil.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                services.add(extractServiceFromResultSet(rs));
            }
            
        } catch (SQLException e) {
            System.err.println("Error finding all services: " + e.getMessage());
            e.printStackTrace();
        }
        
        return services;
    }
    
    /**
     * Get all active services
     */
    public List<Service> findAllActive() {
        List<Service> services = new ArrayList<>();
        String sql = "SELECT * FROM Service WHERE is_active = TRUE ORDER BY name";

        try (Connection conn = DatabaseUtil.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                services.add(extractServiceFromResultSet(rs));
            }

        } catch (SQLException e) {
            System.err.println("Error finding active services: " + e.getMessage());
            e.printStackTrace();
        }

        return services;
    }

    /**
     * Get paginated list of services
     * @param page Page number (1-based)
     * @param pageSize Number of records per page
     * @return List of services for the specified page
     */
    public List<Service> findAll(int page, int pageSize) {
        List<Service> services = new ArrayList<>();
        int offset = (page - 1) * pageSize;
        String sql = "SELECT * FROM Service ORDER BY name LIMIT ? OFFSET ?";

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, pageSize);
            stmt.setInt(2, offset);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                services.add(extractServiceFromResultSet(rs));
            }

        } catch (SQLException e) {
            System.err.println("Error finding paginated services: " + e.getMessage());
            e.printStackTrace();
        }

        return services;
    }

    /**
     * Get total count of services
     * @return Total number of services
     */
    public int getTotalCount() {
        String sql = "SELECT COUNT(*) as total FROM Service";

        try (Connection conn = DatabaseUtil.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            if (rs.next()) {
                return rs.getInt("total");
            }

        } catch (SQLException e) {
            System.err.println("Error getting service count: " + e.getMessage());
            e.printStackTrace();
        }

        return 0;
    }
    
    /**
     * Create new service
     */
    public boolean create(Service service) {
        String sql = "INSERT INTO Service (name, description, duration, price, is_active) VALUES (?, ?, ?, ?, ?)";
        
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setString(1, service.getName());
            stmt.setString(2, service.getDescription());
            stmt.setInt(3, service.getDuration());
            stmt.setDouble(4, service.getPrice());
            stmt.setBoolean(5, service.isActive());
            
            int rowsAffected = stmt.executeUpdate();
            
            if (rowsAffected > 0) {
                ResultSet generatedKeys = stmt.getGeneratedKeys();
                if (generatedKeys.next()) {
                    service.setServiceId(generatedKeys.getInt(1));
                }
                return true;
            }
            
        } catch (SQLException e) {
            System.err.println("Error creating service: " + e.getMessage());
            e.printStackTrace();
        }
        
        return false;
    }
    
    /**
     * Update service
     */
    public boolean update(Service service) {
        String sql = "UPDATE Service SET name = ?, description = ?, duration = ?, price = ?, is_active = ? WHERE service_id = ?";
        
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, service.getName());
            stmt.setString(2, service.getDescription());
            stmt.setInt(3, service.getDuration());
            stmt.setDouble(4, service.getPrice());
            stmt.setBoolean(5, service.isActive());
            stmt.setInt(6, service.getServiceId());
            
            return stmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            System.err.println("Error updating service: " + e.getMessage());
            e.printStackTrace();
        }
        
        return false;
    }
    
    /**
     * Delete service
     */
    public boolean delete(int serviceId) {
        String sql = "DELETE FROM Service WHERE service_id = ?";
        
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, serviceId);
            return stmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            System.err.println("Error deleting service: " + e.getMessage());
            e.printStackTrace();
        }
        
        return false;
    }
    
    /**
     * Toggle service active status
     */
    public boolean toggleActive(int serviceId) {
        String sql = "UPDATE Service SET is_active = NOT is_active WHERE service_id = ?";
        
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, serviceId);
            return stmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            System.err.println("Error toggling service status: " + e.getMessage());
            e.printStackTrace();
        }
        
        return false;
    }
    
    /**
     * Extract Service object from ResultSet
     */
    private Service extractServiceFromResultSet(ResultSet rs) throws SQLException {
        Service service = new Service();
        service.setServiceId(rs.getInt("service_id"));
        service.setName(rs.getString("name"));
        service.setDescription(rs.getString("description"));
        service.setDuration(rs.getInt("duration"));
        service.setPrice(rs.getDouble("price"));
        service.setActive(rs.getBoolean("is_active"));
        
        return service;
    }
}