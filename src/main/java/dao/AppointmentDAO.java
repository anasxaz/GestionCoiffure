package dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Time;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import model.Appointment;
import model.Availability;
import util.DatabaseUtil;

public class AppointmentDAO {
    
    /**
     * Find appointment by ID
     */
    public Appointment findById(int appointmentId) {
        String sql = "SELECT a.*, c.name as client_name, b.name as barber_name, s.name as service_name, s.price as service_price " +
                     "FROM Appointment a " +
                     "JOIN Client c ON a.client_id = c.client_id " +
                     "JOIN Barber b ON a.barber_id = b.barber_id " +
                     "JOIN Service s ON a.service_id = s.service_id " +
                     "WHERE a.appointment_id = ?";
        
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, appointmentId);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return extractAppointmentFromResultSet(rs);
            }
            
        } catch (SQLException e) {
            System.err.println("Error finding appointment by ID: " + e.getMessage());
            e.printStackTrace();
        }
        
        return null;
    }
    
    /**
     * Get all appointments
     */
    public List<Appointment> findAll() {
        List<Appointment> appointments = new ArrayList<>();
        String sql = "SELECT a.*, c.name as client_name, b.name as barber_name, s.name as service_name, s.price as service_price " +
                     "FROM Appointment a " +
                     "JOIN Client c ON a.client_id = c.client_id " +
                     "JOIN Barber b ON a.barber_id = b.barber_id " +
                     "JOIN Service s ON a.service_id = s.service_id " +
                     "ORDER BY a.date DESC, a.start_time DESC";
        
        try (Connection conn = DatabaseUtil.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                appointments.add(extractAppointmentFromResultSet(rs));
            }
            
        } catch (SQLException e) {
            System.err.println("Error finding all appointments: " + e.getMessage());
            e.printStackTrace();
        }
        
        return appointments;
    }
    
    /**
     * Get appointments by client ID
     */
    public List<Appointment> findByClientId(int clientId) {
        List<Appointment> appointments = new ArrayList<>();
        String sql = "SELECT a.*, c.name as client_name, b.name as barber_name, s.name as service_name, s.price as service_price " +
                     "FROM Appointment a " +
                     "JOIN Client c ON a.client_id = c.client_id " +
                     "JOIN Barber b ON a.barber_id = b.barber_id " +
                     "JOIN Service s ON a.service_id = s.service_id " +
                     "WHERE a.client_id = ? " +
                     "ORDER BY a.date DESC, a.start_time DESC";
        
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, clientId);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                appointments.add(extractAppointmentFromResultSet(rs));
            }
            
        } catch (SQLException e) {
            System.err.println("Error finding appointments by client: " + e.getMessage());
            e.printStackTrace();
        }
        
        return appointments;
    }
    
    /**
     * Get appointments by barber ID
     */
    public List<Appointment> findByBarberId(int barberId) {
        List<Appointment> appointments = new ArrayList<>();
        String sql = "SELECT a.*, c.name as client_name, b.name as barber_name, s.name as service_name, s.price as service_price " +
                     "FROM Appointment a " +
                     "JOIN Client c ON a.client_id = c.client_id " +
                     "JOIN Barber b ON a.barber_id = b.barber_id " +
                     "JOIN Service s ON a.service_id = s.service_id " +
                     "WHERE a.barber_id = ? " +
                     "ORDER BY a.date DESC, a.start_time DESC";
        
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, barberId);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                appointments.add(extractAppointmentFromResultSet(rs));
            }
            
        } catch (SQLException e) {
            System.err.println("Error finding appointments by barber: " + e.getMessage());
            e.printStackTrace();
        }
        
        return appointments;
    }
    
    /**
     * Get upcoming appointments by client
     */
    public List<Appointment> findUpcomingByClientId(int clientId) {
        List<Appointment> appointments = new ArrayList<>();
        String sql = "SELECT a.*, c.name as client_name, b.name as barber_name, s.name as service_name, s.price as service_price " +
                     "FROM Appointment a " +
                     "JOIN Client c ON a.client_id = c.client_id " +
                     "JOIN Barber b ON a.barber_id = b.barber_id " +
                     "JOIN Service s ON a.service_id = s.service_id " +
                     "WHERE a.client_id = ? AND a.date >= CURDATE() AND a.status NOT IN ('cancelled', 'completed') " +
                     "ORDER BY a.date ASC, a.start_time ASC";
        
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, clientId);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                appointments.add(extractAppointmentFromResultSet(rs));
            }
            
        } catch (SQLException e) {
            System.err.println("Error finding upcoming appointments: " + e.getMessage());
            e.printStackTrace();
        }
        
        return appointments;
    }
    
    /**
     * Create new appointment
     */
    public boolean create(Appointment appointment) {
        // First try with final_price and redemption_id columns
        String sqlWithExtras = "INSERT INTO Appointment (client_id, barber_id, service_id, date, start_time, end_time, status, redemption_id, final_price) " +
                               "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        // Fallback SQL without new columns
        String sqlBasic = "INSERT INTO Appointment (client_id, barber_id, service_id, date, start_time, end_time, status) " +
                         "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseUtil.getConnection()) {

            // Try with full columns first
            try (PreparedStatement stmt = conn.prepareStatement(sqlWithExtras, Statement.RETURN_GENERATED_KEYS)) {
                stmt.setInt(1, appointment.getClientId());
                stmt.setInt(2, appointment.getBarberId());
                stmt.setInt(3, appointment.getServiceId());
                stmt.setDate(4, Date.valueOf(appointment.getDate()));
                stmt.setTime(5, Time.valueOf(appointment.getStartTime()));
                stmt.setTime(6, Time.valueOf(appointment.getEndTime()));
                stmt.setString(7, appointment.getStatus());

                // Handle nullable redemption_id
                if (appointment.getRedemptionId() != null) {
                    stmt.setInt(8, appointment.getRedemptionId());
                } else {
                    stmt.setNull(8, java.sql.Types.INTEGER);
                }

                // Handle final_price
                if (appointment.getFinalPrice() != null) {
                    stmt.setDouble(9, appointment.getFinalPrice());
                } else {
                    stmt.setNull(9, java.sql.Types.DOUBLE);
                }

                int rowsAffected = stmt.executeUpdate();

                if (rowsAffected > 0) {
                    ResultSet generatedKeys = stmt.getGeneratedKeys();
                    if (generatedKeys.next()) {
                        appointment.setAppointmentId(generatedKeys.getInt(1));
                    }
                    return true;
                }

            } catch (SQLException e) {
                // If columns don't exist, try basic insert
                if (e.getMessage().contains("Unknown column")) {
                    System.err.println("WARNING: final_price or redemption_id columns missing. Using basic insert.");
                    System.err.println("Please run: ALTER TABLE Appointment ADD COLUMN final_price DECIMAL(10,2), ADD COLUMN redemption_id INT;");

                    try (PreparedStatement stmt = conn.prepareStatement(sqlBasic, Statement.RETURN_GENERATED_KEYS)) {
                        stmt.setInt(1, appointment.getClientId());
                        stmt.setInt(2, appointment.getBarberId());
                        stmt.setInt(3, appointment.getServiceId());
                        stmt.setDate(4, Date.valueOf(appointment.getDate()));
                        stmt.setTime(5, Time.valueOf(appointment.getStartTime()));
                        stmt.setTime(6, Time.valueOf(appointment.getEndTime()));
                        stmt.setString(7, appointment.getStatus());

                        int rowsAffected = stmt.executeUpdate();

                        if (rowsAffected > 0) {
                            ResultSet generatedKeys = stmt.getGeneratedKeys();
                            if (generatedKeys.next()) {
                                appointment.setAppointmentId(generatedKeys.getInt(1));
                            }
                            return true;
                        }
                    }
                } else {
                    throw e;
                }
            }

        } catch (SQLException e) {
            System.err.println("Error creating appointment: " + e.getMessage());
            e.printStackTrace();
        }

        return false;
    }
    
    /**
     * Update appointment status
     */
    public boolean updateStatus(int appointmentId, String status) {
        String sql = "UPDATE Appointment SET status = ? WHERE appointment_id = ?";
        
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, status);
            stmt.setInt(2, appointmentId);
            
            return stmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            System.err.println("Error updating appointment status: " + e.getMessage());
            e.printStackTrace();
        }
        
        return false;
    }
    
    /**
     * Cancel appointment
     */
    public boolean cancel(int appointmentId, String reason) {
        String sql = "UPDATE Appointment SET status = 'cancelled', cancellation_reason = ? WHERE appointment_id = ?";
        
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, reason);
            stmt.setInt(2, appointmentId);
            
            return stmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            System.err.println("Error cancelling appointment: " + e.getMessage());
            e.printStackTrace();
        }
        
        return false;
    }
    
    /**
     * Check if time slot is available (checks against existing appointments only)
     */
    public boolean isTimeSlotAvailable(int barberId, LocalDate date, LocalTime startTime, LocalTime endTime) {
        String sql = "SELECT COUNT(*) as count FROM Appointment " +
                     "WHERE barber_id = ? AND date = ? AND status NOT IN ('cancelled', 'refused') " +
                     "AND ((start_time < ? AND end_time > ?) OR (start_time < ? AND end_time > ?) " +
                     "OR (start_time >= ? AND end_time <= ?))";

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, barberId);
            stmt.setDate(2, Date.valueOf(date));
            stmt.setTime(3, Time.valueOf(endTime));
            stmt.setTime(4, Time.valueOf(startTime));
            stmt.setTime(5, Time.valueOf(endTime));
            stmt.setTime(6, Time.valueOf(endTime));
            stmt.setTime(7, Time.valueOf(startTime));
            stmt.setTime(8, Time.valueOf(endTime));

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getInt("count") == 0;
            }

        } catch (SQLException e) {
            System.err.println("Error checking time slot availability: " + e.getMessage());
            e.printStackTrace();
        }

        return false;
    }

    /**
     * Check if barber is available at the requested time considering both schedule and existing appointments
     */
    public boolean isBarberAvailable(int barberId, LocalDate date, LocalTime startTime, LocalTime endTime) {
        // First check if barber works on this day
        String dayOfWeek = getDayOfWeekAbbreviation(date);
        String availabilitySQL = "SELECT * FROM Availability WHERE barber_id = ? AND day_of_week = ?";

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(availabilitySQL)) {

            stmt.setInt(1, barberId);
            stmt.setString(2, dayOfWeek);
            ResultSet rs = stmt.executeQuery();

            // If barber doesn't work on this day, return false
            if (!rs.next()) {
                return false;
            }

            // Check if requested time is within barber's working hours
            Time workStartTime = rs.getTime("start_time");
            Time workEndTime = rs.getTime("end_time");

            LocalTime barberStart = workStartTime.toLocalTime();
            LocalTime barberEnd = workEndTime.toLocalTime();

            // Check if requested time is within working hours
            if (startTime.isBefore(barberStart) || endTime.isAfter(barberEnd)) {
                return false;
            }

        } catch (SQLException e) {
            System.err.println("Error checking barber availability: " + e.getMessage());
            e.printStackTrace();
            return false;
        }

        // Now check if time slot is available (no conflicting appointments)
        return isTimeSlotAvailable(barberId, date, startTime, endTime);
    }

    /**
     * Get day of week abbreviation from LocalDate
     */
    private String getDayOfWeekAbbreviation(LocalDate date) {
        switch (date.getDayOfWeek()) {
            case MONDAY: return "Mon";
            case TUESDAY: return "Tue";
            case WEDNESDAY: return "Wed";
            case THURSDAY: return "Thu";
            case FRIDAY: return "Fri";
            case SATURDAY: return "Sat";
            case SUNDAY: return "Sun";
            default: return "";
        }
    }
    
    /**
     * Get appointments by date and barber
     */
    public List<Appointment> findByBarberAndDate(int barberId, LocalDate date) {
        List<Appointment> appointments = new ArrayList<>();
        String sql = "SELECT a.*, c.name as client_name, b.name as barber_name, s.name as service_name, s.price as service_price " +
                     "FROM Appointment a " +
                     "JOIN Client c ON a.client_id = c.client_id " +
                     "JOIN Barber b ON a.barber_id = b.barber_id " +
                     "JOIN Service s ON a.service_id = s.service_id " +
                     "WHERE a.barber_id = ? AND a.date = ? " +
                     "ORDER BY a.start_time ASC";
        
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, barberId);
            stmt.setDate(2, Date.valueOf(date));
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                appointments.add(extractAppointmentFromResultSet(rs));
            }
            
        } catch (SQLException e) {
            System.err.println("Error finding appointments by barber and date: " + e.getMessage());
            e.printStackTrace();
        }
        
        return appointments;
    }
    
    /**
     * Delete appointment (for admin only)
     */
    public boolean delete(int appointmentId) {
        String sql = "DELETE FROM Appointment WHERE appointment_id = ?";
        
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, appointmentId);
            return stmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            System.err.println("Error deleting appointment: " + e.getMessage());
            e.printStackTrace();
        }
        
        return false;
    }
    
    /**
     * Extract Appointment object from ResultSet
     */
    private Appointment extractAppointmentFromResultSet(ResultSet rs) throws SQLException {
        Appointment appointment = new Appointment();
        appointment.setAppointmentId(rs.getInt("appointment_id"));
        appointment.setClientId(rs.getInt("client_id"));
        appointment.setBarberId(rs.getInt("barber_id"));
        appointment.setServiceId(rs.getInt("service_id"));
        
        Date date = rs.getDate("date");
        if (date != null) {
            appointment.setDate(date.toLocalDate());
        }
        
        Time startTime = rs.getTime("start_time");
        if (startTime != null) {
            appointment.setStartTime(startTime.toLocalTime());
        }
        
        Time endTime = rs.getTime("end_time");
        if (endTime != null) {
            appointment.setEndTime(endTime.toLocalTime());
        }
        
        appointment.setStatus(rs.getString("status"));
        appointment.setCancellationReason(rs.getString("cancellation_reason"));

        // Extract redemption_id if present
        int redemptionId = rs.getInt("redemption_id");
        if (!rs.wasNull()) {
            appointment.setRedemptionId(redemptionId);
        }

        Timestamp createdAt = rs.getTimestamp("created_at");
        if (createdAt != null) {
            appointment.setCreatedAt(createdAt.toLocalDateTime());
        }
        
        Timestamp updatedAt = rs.getTimestamp("updated_at");
        if (updatedAt != null) {
            appointment.setUpdatedAt(updatedAt.toLocalDateTime());
        }
        
        // Set related names
        appointment.setClientName(rs.getString("client_name"));
        appointment.setBarberName(rs.getString("barber_name"));
        appointment.setServiceName(rs.getString("service_name"));

        // Set service price if available
        try {
            double price = rs.getDouble("service_price");
            if (!rs.wasNull()) {
                appointment.setServicePrice(price);
            }
        } catch (SQLException e) {
            // service_price column might not be in result set for some queries
        }

        // Set final price if available
        try {
            double finalPrice = rs.getDouble("final_price");
            if (!rs.wasNull()) {
                appointment.setFinalPrice(finalPrice);
            }
        } catch (SQLException e) {
            // final_price column might not be in result set for some queries
        }

        return appointment;
    }
}
