package dao;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import model.Appointment;
import util.DatabaseUtil;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AppointmentDAOTest {

    @Mock
    private Connection connection;

    @Mock
    private PreparedStatement preparedStatement;

    @Mock
    private Statement statement;

    @Mock
    private ResultSet resultSet;

    @Mock
    private ResultSet generatedKeys;

    private AppointmentDAO appointmentDAO;

    @BeforeEach
    void setUp() {
        appointmentDAO = new AppointmentDAO();
    }

    private void mockAppointmentResultSet() throws SQLException {
        when(resultSet.getInt("appointment_id")).thenReturn(1);
        when(resultSet.getInt("client_id")).thenReturn(1);
        when(resultSet.getInt("barber_id")).thenReturn(1);
        when(resultSet.getInt("service_id")).thenReturn(1);
        when(resultSet.getDate("date")).thenReturn(Date.valueOf(LocalDate.of(2024, 12, 25)));
        when(resultSet.getTime("start_time")).thenReturn(Time.valueOf(LocalTime.of(10, 0)));
        when(resultSet.getTime("end_time")).thenReturn(Time.valueOf(LocalTime.of(11, 0)));
        when(resultSet.getString("status")).thenReturn("pending");
        when(resultSet.getString("cancellation_reason")).thenReturn(null);
        when(resultSet.getInt("redemption_id")).thenReturn(0);
        when(resultSet.wasNull()).thenReturn(true);
        when(resultSet.getTimestamp("created_at")).thenReturn(null);
        when(resultSet.getTimestamp("updated_at")).thenReturn(null);
        when(resultSet.getString("client_name")).thenReturn("Marie");
        when(resultSet.getString("barber_name")).thenReturn("Jean");
        when(resultSet.getString("service_name")).thenReturn("Coupe");
        when(resultSet.getDouble("service_price")).thenReturn(20.0);
        when(resultSet.getDouble("final_price")).thenReturn(0.0);
    }

    @Test
    void testFindById_success() throws SQLException {
        try (MockedStatic<DatabaseUtil> dbUtil = mockStatic(DatabaseUtil.class)) {
            dbUtil.when(DatabaseUtil::getConnection).thenReturn(connection);
            when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
            when(preparedStatement.executeQuery()).thenReturn(resultSet);
            when(resultSet.next()).thenReturn(true);
            mockAppointmentResultSet();

            Appointment result = appointmentDAO.findById(1);

            assertNotNull(result);
            assertEquals(1, result.getAppointmentId());
            assertEquals("Marie", result.getClientName());
            verify(preparedStatement).setInt(1, 1);
        }
    }

    @Test
    void testFindById_notFound() throws SQLException {
        try (MockedStatic<DatabaseUtil> dbUtil = mockStatic(DatabaseUtil.class)) {
            dbUtil.when(DatabaseUtil::getConnection).thenReturn(connection);
            when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
            when(preparedStatement.executeQuery()).thenReturn(resultSet);
            when(resultSet.next()).thenReturn(false);

            Appointment result = appointmentDAO.findById(99);

            assertNull(result);
        }
    }

    @Test
    void testFindById_sqlException() throws SQLException {
        try (MockedStatic<DatabaseUtil> dbUtil = mockStatic(DatabaseUtil.class)) {
            dbUtil.when(DatabaseUtil::getConnection).thenReturn(connection);
            when(connection.prepareStatement(anyString())).thenThrow(new SQLException("Error"));

            Appointment result = appointmentDAO.findById(1);

            assertNull(result);
        }
    }

    @Test
    void testFindAll_success() throws SQLException {
        try (MockedStatic<DatabaseUtil> dbUtil = mockStatic(DatabaseUtil.class)) {
            dbUtil.when(DatabaseUtil::getConnection).thenReturn(connection);
            when(connection.createStatement()).thenReturn(statement);
            when(statement.executeQuery(anyString())).thenReturn(resultSet);
            when(resultSet.next()).thenReturn(true, false);
            mockAppointmentResultSet();

            List<Appointment> result = appointmentDAO.findAll();

            assertNotNull(result);
            assertEquals(1, result.size());
        }
    }

    @Test
    void testFindAll_sqlException() throws SQLException {
        try (MockedStatic<DatabaseUtil> dbUtil = mockStatic(DatabaseUtil.class)) {
            dbUtil.when(DatabaseUtil::getConnection).thenReturn(connection);
            when(connection.createStatement()).thenThrow(new SQLException("Error"));

            List<Appointment> result = appointmentDAO.findAll();

            assertNotNull(result);
            assertTrue(result.isEmpty());
        }
    }

    @Test
    void testFindAllPaginated_success() throws SQLException {
        try (MockedStatic<DatabaseUtil> dbUtil = mockStatic(DatabaseUtil.class)) {
            dbUtil.when(DatabaseUtil::getConnection).thenReturn(connection);
            when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
            when(preparedStatement.executeQuery()).thenReturn(resultSet);
            when(resultSet.next()).thenReturn(true, false);
            mockAppointmentResultSet();

            List<Appointment> result = appointmentDAO.findAll(1, 10);

            assertNotNull(result);
            assertEquals(1, result.size());
            verify(preparedStatement).setInt(1, 10);
            verify(preparedStatement).setInt(2, 0);
        }
    }

    @Test
    void testGetTotalCount_success() throws SQLException {
        try (MockedStatic<DatabaseUtil> dbUtil = mockStatic(DatabaseUtil.class)) {
            dbUtil.when(DatabaseUtil::getConnection).thenReturn(connection);
            when(connection.createStatement()).thenReturn(statement);
            when(statement.executeQuery(anyString())).thenReturn(resultSet);
            when(resultSet.next()).thenReturn(true);
            when(resultSet.getInt("total")).thenReturn(50);

            int result = appointmentDAO.getTotalCount();

            assertEquals(50, result);
        }
    }

    @Test
    void testGetTotalCount_sqlException() throws SQLException {
        try (MockedStatic<DatabaseUtil> dbUtil = mockStatic(DatabaseUtil.class)) {
            dbUtil.when(DatabaseUtil::getConnection).thenReturn(connection);
            when(connection.createStatement()).thenThrow(new SQLException("Error"));

            int result = appointmentDAO.getTotalCount();

            assertEquals(0, result);
        }
    }

    @Test
    void testGetCountByStatus_success() throws SQLException {
        try (MockedStatic<DatabaseUtil> dbUtil = mockStatic(DatabaseUtil.class)) {
            dbUtil.when(DatabaseUtil::getConnection).thenReturn(connection);
            when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
            when(preparedStatement.executeQuery()).thenReturn(resultSet);
            when(resultSet.next()).thenReturn(true);
            when(resultSet.getInt("total")).thenReturn(15);

            int result = appointmentDAO.getCountByStatus("pending");

            assertEquals(15, result);
            verify(preparedStatement).setString(1, "pending");
        }
    }

    @Test
    void testGetCountByStatus_sqlException() throws SQLException {
        try (MockedStatic<DatabaseUtil> dbUtil = mockStatic(DatabaseUtil.class)) {
            dbUtil.when(DatabaseUtil::getConnection).thenReturn(connection);
            when(connection.prepareStatement(anyString())).thenThrow(new SQLException("Error"));

            int result = appointmentDAO.getCountByStatus("pending");

            assertEquals(0, result);
        }
    }

    @Test
    void testFindByClientId_success() throws SQLException {
        try (MockedStatic<DatabaseUtil> dbUtil = mockStatic(DatabaseUtil.class)) {
            dbUtil.when(DatabaseUtil::getConnection).thenReturn(connection);
            when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
            when(preparedStatement.executeQuery()).thenReturn(resultSet);
            when(resultSet.next()).thenReturn(true, false);
            mockAppointmentResultSet();

            List<Appointment> result = appointmentDAO.findByClientId(1);

            assertNotNull(result);
            assertEquals(1, result.size());
            verify(preparedStatement).setInt(1, 1);
        }
    }

    @Test
    void testFindByClientId_sqlException() throws SQLException {
        try (MockedStatic<DatabaseUtil> dbUtil = mockStatic(DatabaseUtil.class)) {
            dbUtil.when(DatabaseUtil::getConnection).thenReturn(connection);
            when(connection.prepareStatement(anyString())).thenThrow(new SQLException("Error"));

            List<Appointment> result = appointmentDAO.findByClientId(1);

            assertNotNull(result);
            assertTrue(result.isEmpty());
        }
    }

    @Test
    void testFindByBarberId_success() throws SQLException {
        try (MockedStatic<DatabaseUtil> dbUtil = mockStatic(DatabaseUtil.class)) {
            dbUtil.when(DatabaseUtil::getConnection).thenReturn(connection);
            when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
            when(preparedStatement.executeQuery()).thenReturn(resultSet);
            when(resultSet.next()).thenReturn(true, false);
            mockAppointmentResultSet();

            List<Appointment> result = appointmentDAO.findByBarberId(1);

            assertNotNull(result);
            assertEquals(1, result.size());
            verify(preparedStatement).setInt(1, 1);
        }
    }

    @Test
    void testFindByBarberIdPaginated_success() throws SQLException {
        try (MockedStatic<DatabaseUtil> dbUtil = mockStatic(DatabaseUtil.class)) {
            dbUtil.when(DatabaseUtil::getConnection).thenReturn(connection);
            when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
            when(preparedStatement.executeQuery()).thenReturn(resultSet);
            when(resultSet.next()).thenReturn(true, false);
            mockAppointmentResultSet();

            List<Appointment> result = appointmentDAO.findByBarberId(1, 1, 10);

            assertNotNull(result);
            assertEquals(1, result.size());
            verify(preparedStatement).setInt(1, 1);
            verify(preparedStatement).setInt(2, 10);
            verify(preparedStatement).setInt(3, 0);
        }
    }

    @Test
    void testGetTotalCountByBarberId_success() throws SQLException {
        try (MockedStatic<DatabaseUtil> dbUtil = mockStatic(DatabaseUtil.class)) {
            dbUtil.when(DatabaseUtil::getConnection).thenReturn(connection);
            when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
            when(preparedStatement.executeQuery()).thenReturn(resultSet);
            when(resultSet.next()).thenReturn(true);
            when(resultSet.getInt("total")).thenReturn(20);

            int result = appointmentDAO.getTotalCountByBarberId(1);

            assertEquals(20, result);
            verify(preparedStatement).setInt(1, 1);
        }
    }

    @Test
    void testFindUpcomingByClientId_success() throws SQLException {
        try (MockedStatic<DatabaseUtil> dbUtil = mockStatic(DatabaseUtil.class)) {
            dbUtil.when(DatabaseUtil::getConnection).thenReturn(connection);
            when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
            when(preparedStatement.executeQuery()).thenReturn(resultSet);
            when(resultSet.next()).thenReturn(true, false);
            mockAppointmentResultSet();

            List<Appointment> result = appointmentDAO.findUpcomingByClientId(1);

            assertNotNull(result);
            assertEquals(1, result.size());
            verify(preparedStatement).setInt(1, 1);
        }
    }

    @Test
    void testCreate_success() throws SQLException {
        try (MockedStatic<DatabaseUtil> dbUtil = mockStatic(DatabaseUtil.class)) {
            dbUtil.when(DatabaseUtil::getConnection).thenReturn(connection);
            when(connection.prepareStatement(anyString(), eq(Statement.RETURN_GENERATED_KEYS))).thenReturn(preparedStatement);
            when(preparedStatement.executeUpdate()).thenReturn(1);
            when(preparedStatement.getGeneratedKeys()).thenReturn(generatedKeys);
            when(generatedKeys.next()).thenReturn(true);
            when(generatedKeys.getInt(1)).thenReturn(5);

            Appointment appointment = new Appointment();
            appointment.setClientId(1);
            appointment.setBarberId(1);
            appointment.setServiceId(1);
            appointment.setDate(LocalDate.of(2024, 12, 25));
            appointment.setStartTime(LocalTime.of(10, 0));
            appointment.setEndTime(LocalTime.of(11, 0));
            appointment.setStatus("pending");
            appointment.setRedemptionId(null);
            appointment.setFinalPrice(null);

            boolean result = appointmentDAO.create(appointment);

            assertTrue(result);
            assertEquals(5, appointment.getAppointmentId());
        }
    }

    @Test
    void testCreate_sqlException() throws SQLException {
        try (MockedStatic<DatabaseUtil> dbUtil = mockStatic(DatabaseUtil.class)) {
            dbUtil.when(DatabaseUtil::getConnection).thenReturn(connection);
            when(connection.prepareStatement(anyString(), eq(Statement.RETURN_GENERATED_KEYS))).thenThrow(new SQLException("Error"));

            Appointment appointment = new Appointment();
            appointment.setClientId(1);

            boolean result = appointmentDAO.create(appointment);

            assertFalse(result);
        }
    }

    @Test
    void testUpdateStatus_success() throws SQLException {
        try (MockedStatic<DatabaseUtil> dbUtil = mockStatic(DatabaseUtil.class)) {
            dbUtil.when(DatabaseUtil::getConnection).thenReturn(connection);
            when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
            when(preparedStatement.executeUpdate()).thenReturn(1);

            boolean result = appointmentDAO.updateStatus(1, "confirmed");

            assertTrue(result);
            verify(preparedStatement).setString(1, "confirmed");
            verify(preparedStatement).setInt(2, 1);
        }
    }

    @Test
    void testUpdateStatus_sqlException() throws SQLException {
        try (MockedStatic<DatabaseUtil> dbUtil = mockStatic(DatabaseUtil.class)) {
            dbUtil.when(DatabaseUtil::getConnection).thenReturn(connection);
            when(connection.prepareStatement(anyString())).thenThrow(new SQLException("Error"));

            boolean result = appointmentDAO.updateStatus(1, "confirmed");

            assertFalse(result);
        }
    }

    @Test
    void testCancel_success() throws SQLException {
        try (MockedStatic<DatabaseUtil> dbUtil = mockStatic(DatabaseUtil.class)) {
            dbUtil.when(DatabaseUtil::getConnection).thenReturn(connection);
            when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
            when(preparedStatement.executeUpdate()).thenReturn(1);

            boolean result = appointmentDAO.cancel(1, "Client request");

            assertTrue(result);
            verify(preparedStatement).setString(1, "Client request");
            verify(preparedStatement).setInt(2, 1);
        }
    }

    @Test
    void testCancel_sqlException() throws SQLException {
        try (MockedStatic<DatabaseUtil> dbUtil = mockStatic(DatabaseUtil.class)) {
            dbUtil.when(DatabaseUtil::getConnection).thenReturn(connection);
            when(connection.prepareStatement(anyString())).thenThrow(new SQLException("Error"));

            boolean result = appointmentDAO.cancel(1, "Client request");

            assertFalse(result);
        }
    }

    @Test
    void testIsTimeSlotAvailable_available() throws SQLException {
        try (MockedStatic<DatabaseUtil> dbUtil = mockStatic(DatabaseUtil.class)) {
            dbUtil.when(DatabaseUtil::getConnection).thenReturn(connection);
            when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
            when(preparedStatement.executeQuery()).thenReturn(resultSet);
            when(resultSet.next()).thenReturn(true);
            when(resultSet.getInt("count")).thenReturn(0);

            boolean result = appointmentDAO.isTimeSlotAvailable(1, LocalDate.of(2024, 12, 25), LocalTime.of(10, 0), LocalTime.of(11, 0));

            assertTrue(result);
        }
    }

    @Test
    void testIsTimeSlotAvailable_notAvailable() throws SQLException {
        try (MockedStatic<DatabaseUtil> dbUtil = mockStatic(DatabaseUtil.class)) {
            dbUtil.when(DatabaseUtil::getConnection).thenReturn(connection);
            when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
            when(preparedStatement.executeQuery()).thenReturn(resultSet);
            when(resultSet.next()).thenReturn(true);
            when(resultSet.getInt("count")).thenReturn(1);

            boolean result = appointmentDAO.isTimeSlotAvailable(1, LocalDate.of(2024, 12, 25), LocalTime.of(10, 0), LocalTime.of(11, 0));

            assertFalse(result);
        }
    }

    @Test
    void testIsTimeSlotAvailable_sqlException() throws SQLException {
        try (MockedStatic<DatabaseUtil> dbUtil = mockStatic(DatabaseUtil.class)) {
            dbUtil.when(DatabaseUtil::getConnection).thenReturn(connection);
            when(connection.prepareStatement(anyString())).thenThrow(new SQLException("Error"));

            boolean result = appointmentDAO.isTimeSlotAvailable(1, LocalDate.of(2024, 12, 25), LocalTime.of(10, 0), LocalTime.of(11, 0));

            assertFalse(result);
        }
    }

    @Test
    void testFindByBarberAndDate_success() throws SQLException {
        try (MockedStatic<DatabaseUtil> dbUtil = mockStatic(DatabaseUtil.class)) {
            dbUtil.when(DatabaseUtil::getConnection).thenReturn(connection);
            when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
            when(preparedStatement.executeQuery()).thenReturn(resultSet);
            when(resultSet.next()).thenReturn(true, false);
            mockAppointmentResultSet();

            List<Appointment> result = appointmentDAO.findByBarberAndDate(1, LocalDate.of(2024, 12, 25));

            assertNotNull(result);
            assertEquals(1, result.size());
            verify(preparedStatement).setInt(1, 1);
        }
    }

    @Test
    void testDelete_success() throws SQLException {
        try (MockedStatic<DatabaseUtil> dbUtil = mockStatic(DatabaseUtil.class)) {
            dbUtil.when(DatabaseUtil::getConnection).thenReturn(connection);
            when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
            when(preparedStatement.executeUpdate()).thenReturn(1);

            boolean result = appointmentDAO.delete(1);

            assertTrue(result);
            verify(preparedStatement).setInt(1, 1);
        }
    }

    @Test
    void testDelete_sqlException() throws SQLException {
        try (MockedStatic<DatabaseUtil> dbUtil = mockStatic(DatabaseUtil.class)) {
            dbUtil.when(DatabaseUtil::getConnection).thenReturn(connection);
            when(connection.prepareStatement(anyString())).thenThrow(new SQLException("Error"));

            boolean result = appointmentDAO.delete(1);

            assertFalse(result);
        }
    }
}
