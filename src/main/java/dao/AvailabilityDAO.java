package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

import model.Availability;
import util.DatabaseUtil;

public class AvailabilityDAO {

    public Availability findById(int availabilityId) {
        String sql = "SELECT * FROM Availability WHERE availability_id = ?";
        
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, availabilityId);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return extractAvailabilityFromResultSet(rs);
            }
            
        } catch (SQLException e) {
            System.err.println("Error finding availability by ID: " + e.getMessage());
            e.printStackTrace();
        }
        
        return null;
    }

    public List<Availability> findByBarberId(int barberId) {
        List<Availability> availabilities = new ArrayList<>();
        String sql = "SELECT * FROM Availability WHERE barber_id = ? ORDER BY FIELD(day_of_week, 'Mon','Tue','Wed','Thu','Fri','Sat','Sun'), start_time";
        
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, barberId);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                availabilities.add(extractAvailabilityFromResultSet(rs));
            }
            
        } catch (SQLException e) {
            System.err.println("Error finding availabilities by barber: " + e.getMessage());
            e.printStackTrace();
        }
        
        return availabilities;
    }

    public Availability findByBarberAndDay(int barberId, String dayOfWeek) {
        String sql = "SELECT * FROM Availability WHERE barber_id = ? AND day_of_week = ?";
        
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, barberId);
            stmt.setString(2, dayOfWeek);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return extractAvailabilityFromResultSet(rs);
            }
            
        } catch (SQLException e) {
            System.err.println("Error finding availability by barber and day: " + e.getMessage());
            e.printStackTrace();
        }
        
        return null;
    }

    public List<Availability> findAll() {
        List<Availability> availabilities = new ArrayList<>();
        String sql = "SELECT a.*, b.name as barber_name FROM Availability a " +
                     "JOIN Barber b ON a.barber_id = b.barber_id " +
                     "ORDER BY b.name, FIELD(a.day_of_week, 'Mon','Tue','Wed','Thu','Fri','Sat','Sun')";
        
        try (Connection conn = DatabaseUtil.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                Availability availability = extractAvailabilityFromResultSet(rs);
                availability.setBarberName(rs.getString("barber_name"));
                availabilities.add(availability);
            }
            
        } catch (SQLException e) {
            System.err.println("Error finding all availabilities: " + e.getMessage());
            e.printStackTrace();
        }
        
        return availabilities;
    }

    public boolean create(Availability availability) {
        String sql = "INSERT INTO Availability (barber_id, day_of_week, start_time, end_time) VALUES (?, ?, ?, ?)";
        
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setInt(1, availability.getBarberId());
            stmt.setString(2, availability.getDayOfWeek());
            stmt.setTime(3, Time.valueOf(availability.getStartTime()));
            stmt.setTime(4, Time.valueOf(availability.getEndTime()));
            
            int rowsAffected = stmt.executeUpdate();
            
            if (rowsAffected > 0) {
                ResultSet generatedKeys = stmt.getGeneratedKeys();
                if (generatedKeys.next()) {
                    availability.setAvailabilityId(generatedKeys.getInt(1));
                }
                return true;
            }
            
        } catch (SQLException e) {
            System.err.println("Error creating availability: " + e.getMessage());
            e.printStackTrace();
        }
        
        return false;
    }

    public boolean update(Availability availability) {
        String sql = "UPDATE Availability SET day_of_week = ?, start_time = ?, end_time = ? WHERE availability_id = ?";
        
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, availability.getDayOfWeek());
            stmt.setTime(2, Time.valueOf(availability.getStartTime()));
            stmt.setTime(3, Time.valueOf(availability.getEndTime()));
            stmt.setInt(4, availability.getAvailabilityId());
            
            return stmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            System.err.println("Error updating availability: " + e.getMessage());
            e.printStackTrace();
        }
        
        return false;
    }

    public boolean delete(int availabilityId) {
        String sql = "DELETE FROM Availability WHERE availability_id = ?";
        
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, availabilityId);
            return stmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            System.err.println("Error deleting availability: " + e.getMessage());
            e.printStackTrace();
        }
        
        return false;
    }

    public boolean deleteByBarberId(int barberId) {
        String sql = "DELETE FROM Availability WHERE barber_id = ?";
        
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, barberId);
            return stmt.executeUpdate() >= 0;
            
        } catch (SQLException e) {
            System.err.println("Error deleting availabilities by barber: " + e.getMessage());
            e.printStackTrace();
        }
        
        return false;
    }

    private Availability extractAvailabilityFromResultSet(ResultSet rs) throws SQLException {
        Availability availability = new Availability();
        availability.setAvailabilityId(rs.getInt("availability_id"));
        availability.setBarberId(rs.getInt("barber_id"));
        availability.setDayOfWeek(rs.getString("day_of_week"));
        
        Time startTime = rs.getTime("start_time");
        if (startTime != null) {
            availability.setStartTime(startTime.toLocalTime());
        }
        
        Time endTime = rs.getTime("end_time");
        if (endTime != null) {
            availability.setEndTime(endTime.toLocalTime());
        }
        
        return availability;
    }
}