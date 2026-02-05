package dao;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import model.Availability;
import util.DatabaseUtil;

import java.sql.*;
import java.time.LocalTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AvailabilityDAOTest {

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

    private AvailabilityDAO availabilityDAO;

    @BeforeEach
    void setUp() {
        availabilityDAO = new AvailabilityDAO();
    }

    @Test
    void testFindById_success() throws SQLException {
        try (MockedStatic<DatabaseUtil> dbUtil = mockStatic(DatabaseUtil.class)) {
            dbUtil.when(DatabaseUtil::getConnection).thenReturn(connection);
            when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
            when(preparedStatement.executeQuery()).thenReturn(resultSet);
            when(resultSet.next()).thenReturn(true);
            when(resultSet.getInt("availability_id")).thenReturn(1);
            when(resultSet.getInt("barber_id")).thenReturn(1);
            when(resultSet.getString("day_of_week")).thenReturn("Mon");
            when(resultSet.getTime("start_time")).thenReturn(Time.valueOf(LocalTime.of(9, 0)));
            when(resultSet.getTime("end_time")).thenReturn(Time.valueOf(LocalTime.of(17, 0)));

            Availability result = availabilityDAO.findById(1);

            assertNotNull(result);
            assertEquals(1, result.getAvailabilityId());
            assertEquals(1, result.getBarberId());
            assertEquals("Mon", result.getDayOfWeek());
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

            Availability result = availabilityDAO.findById(99);

            assertNull(result);
        }
    }

    @Test
    void testFindById_sqlException() throws SQLException {
        try (MockedStatic<DatabaseUtil> dbUtil = mockStatic(DatabaseUtil.class)) {
            dbUtil.when(DatabaseUtil::getConnection).thenReturn(connection);
            when(connection.prepareStatement(anyString())).thenThrow(new SQLException("Error"));

            Availability result = availabilityDAO.findById(1);

            assertNull(result);
        }
    }

    @Test
    void testFindByBarberId_success() throws SQLException {
        try (MockedStatic<DatabaseUtil> dbUtil = mockStatic(DatabaseUtil.class)) {
            dbUtil.when(DatabaseUtil::getConnection).thenReturn(connection);
            when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
            when(preparedStatement.executeQuery()).thenReturn(resultSet);
            when(resultSet.next()).thenReturn(true, true, false);
            when(resultSet.getInt("availability_id")).thenReturn(1, 2);
            when(resultSet.getInt("barber_id")).thenReturn(1, 1);
            when(resultSet.getString("day_of_week")).thenReturn("Mon", "Tue");
            when(resultSet.getTime("start_time")).thenReturn(Time.valueOf(LocalTime.of(9, 0)));
            when(resultSet.getTime("end_time")).thenReturn(Time.valueOf(LocalTime.of(17, 0)));

            List<Availability> result = availabilityDAO.findByBarberId(1);

            assertNotNull(result);
            assertEquals(2, result.size());
            verify(preparedStatement).setInt(1, 1);
        }
    }

    @Test
    void testFindByBarberId_sqlException() throws SQLException {
        try (MockedStatic<DatabaseUtil> dbUtil = mockStatic(DatabaseUtil.class)) {
            dbUtil.when(DatabaseUtil::getConnection).thenReturn(connection);
            when(connection.prepareStatement(anyString())).thenThrow(new SQLException("Error"));

            List<Availability> result = availabilityDAO.findByBarberId(1);

            assertNotNull(result);
            assertTrue(result.isEmpty());
        }
    }

    @Test
    void testFindByBarberAndDay_success() throws SQLException {
        try (MockedStatic<DatabaseUtil> dbUtil = mockStatic(DatabaseUtil.class)) {
            dbUtil.when(DatabaseUtil::getConnection).thenReturn(connection);
            when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
            when(preparedStatement.executeQuery()).thenReturn(resultSet);
            when(resultSet.next()).thenReturn(true);
            when(resultSet.getInt("availability_id")).thenReturn(1);
            when(resultSet.getInt("barber_id")).thenReturn(1);
            when(resultSet.getString("day_of_week")).thenReturn("Mon");
            when(resultSet.getTime("start_time")).thenReturn(Time.valueOf(LocalTime.of(9, 0)));
            when(resultSet.getTime("end_time")).thenReturn(Time.valueOf(LocalTime.of(17, 0)));

            Availability result = availabilityDAO.findByBarberAndDay(1, "Mon");

            assertNotNull(result);
            assertEquals("Mon", result.getDayOfWeek());
            verify(preparedStatement).setInt(1, 1);
            verify(preparedStatement).setString(2, "Mon");
        }
    }

    @Test
    void testFindByBarberAndDay_notFound() throws SQLException {
        try (MockedStatic<DatabaseUtil> dbUtil = mockStatic(DatabaseUtil.class)) {
            dbUtil.when(DatabaseUtil::getConnection).thenReturn(connection);
            when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
            when(preparedStatement.executeQuery()).thenReturn(resultSet);
            when(resultSet.next()).thenReturn(false);

            Availability result = availabilityDAO.findByBarberAndDay(1, "Sun");

            assertNull(result);
        }
    }

    @Test
    void testFindByBarberAndDay_sqlException() throws SQLException {
        try (MockedStatic<DatabaseUtil> dbUtil = mockStatic(DatabaseUtil.class)) {
            dbUtil.when(DatabaseUtil::getConnection).thenReturn(connection);
            when(connection.prepareStatement(anyString())).thenThrow(new SQLException("Error"));

            Availability result = availabilityDAO.findByBarberAndDay(1, "Mon");

            assertNull(result);
        }
    }

    @Test
    void testFindAll_success() throws SQLException {
        try (MockedStatic<DatabaseUtil> dbUtil = mockStatic(DatabaseUtil.class)) {
            dbUtil.when(DatabaseUtil::getConnection).thenReturn(connection);
            when(connection.createStatement()).thenReturn(statement);
            when(statement.executeQuery(anyString())).thenReturn(resultSet);
            when(resultSet.next()).thenReturn(true, true, false);
            when(resultSet.getInt("availability_id")).thenReturn(1, 2);
            when(resultSet.getInt("barber_id")).thenReturn(1, 2);
            when(resultSet.getString("day_of_week")).thenReturn("Mon", "Tue");
            when(resultSet.getTime("start_time")).thenReturn(Time.valueOf(LocalTime.of(9, 0)));
            when(resultSet.getTime("end_time")).thenReturn(Time.valueOf(LocalTime.of(17, 0)));
            when(resultSet.getString("barber_name")).thenReturn("Jean", "Paul");

            List<Availability> result = availabilityDAO.findAll();

            assertNotNull(result);
            assertEquals(2, result.size());
            assertEquals("Jean", result.get(0).getBarberName());
        }
    }

    @Test
    void testFindAll_sqlException() throws SQLException {
        try (MockedStatic<DatabaseUtil> dbUtil = mockStatic(DatabaseUtil.class)) {
            dbUtil.when(DatabaseUtil::getConnection).thenReturn(connection);
            when(connection.createStatement()).thenThrow(new SQLException("Error"));

            List<Availability> result = availabilityDAO.findAll();

            assertNotNull(result);
            assertTrue(result.isEmpty());
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

            Availability availability = new Availability();
            availability.setBarberId(1);
            availability.setDayOfWeek("Mon");
            availability.setStartTime(LocalTime.of(9, 0));
            availability.setEndTime(LocalTime.of(17, 0));

            boolean result = availabilityDAO.create(availability);

            assertTrue(result);
            assertEquals(5, availability.getAvailabilityId());
            verify(preparedStatement).setInt(1, 1);
            verify(preparedStatement).setString(2, "Mon");
        }
    }

    @Test
    void testCreate_sqlException() throws SQLException {
        try (MockedStatic<DatabaseUtil> dbUtil = mockStatic(DatabaseUtil.class)) {
            dbUtil.when(DatabaseUtil::getConnection).thenReturn(connection);
            when(connection.prepareStatement(anyString(), eq(Statement.RETURN_GENERATED_KEYS))).thenThrow(new SQLException("Error"));

            Availability availability = new Availability();
            availability.setBarberId(1);

            boolean result = availabilityDAO.create(availability);

            assertFalse(result);
        }
    }

    @Test
    void testUpdate_success() throws SQLException {
        try (MockedStatic<DatabaseUtil> dbUtil = mockStatic(DatabaseUtil.class)) {
            dbUtil.when(DatabaseUtil::getConnection).thenReturn(connection);
            when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
            when(preparedStatement.executeUpdate()).thenReturn(1);

            Availability availability = new Availability();
            availability.setAvailabilityId(1);
            availability.setDayOfWeek("Tue");
            availability.setStartTime(LocalTime.of(10, 0));
            availability.setEndTime(LocalTime.of(18, 0));

            boolean result = availabilityDAO.update(availability);

            assertTrue(result);
            verify(preparedStatement).setString(1, "Tue");
            verify(preparedStatement).setInt(4, 1);
        }
    }

    @Test
    void testUpdate_sqlException() throws SQLException {
        try (MockedStatic<DatabaseUtil> dbUtil = mockStatic(DatabaseUtil.class)) {
            dbUtil.when(DatabaseUtil::getConnection).thenReturn(connection);
            when(connection.prepareStatement(anyString())).thenThrow(new SQLException("Error"));

            Availability availability = new Availability();
            availability.setAvailabilityId(1);

            boolean result = availabilityDAO.update(availability);

            assertFalse(result);
        }
    }

    @Test
    void testDelete_success() throws SQLException {
        try (MockedStatic<DatabaseUtil> dbUtil = mockStatic(DatabaseUtil.class)) {
            dbUtil.when(DatabaseUtil::getConnection).thenReturn(connection);
            when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
            when(preparedStatement.executeUpdate()).thenReturn(1);

            boolean result = availabilityDAO.delete(1);

            assertTrue(result);
            verify(preparedStatement).setInt(1, 1);
        }
    }

    @Test
    void testDelete_sqlException() throws SQLException {
        try (MockedStatic<DatabaseUtil> dbUtil = mockStatic(DatabaseUtil.class)) {
            dbUtil.when(DatabaseUtil::getConnection).thenReturn(connection);
            when(connection.prepareStatement(anyString())).thenThrow(new SQLException("Error"));

            boolean result = availabilityDAO.delete(1);

            assertFalse(result);
        }
    }

    @Test
    void testDeleteByBarberId_success() throws SQLException {
        try (MockedStatic<DatabaseUtil> dbUtil = mockStatic(DatabaseUtil.class)) {
            dbUtil.when(DatabaseUtil::getConnection).thenReturn(connection);
            when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
            when(preparedStatement.executeUpdate()).thenReturn(3);

            boolean result = availabilityDAO.deleteByBarberId(1);

            assertTrue(result);
            verify(preparedStatement).setInt(1, 1);
        }
    }

    @Test
    void testDeleteByBarberId_noRowsAffected() throws SQLException {
        try (MockedStatic<DatabaseUtil> dbUtil = mockStatic(DatabaseUtil.class)) {
            dbUtil.when(DatabaseUtil::getConnection).thenReturn(connection);
            when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
            when(preparedStatement.executeUpdate()).thenReturn(0);

            boolean result = availabilityDAO.deleteByBarberId(99);

            assertTrue(result);
        }
    }

    @Test
    void testDeleteByBarberId_sqlException() throws SQLException {
        try (MockedStatic<DatabaseUtil> dbUtil = mockStatic(DatabaseUtil.class)) {
            dbUtil.when(DatabaseUtil::getConnection).thenReturn(connection);
            when(connection.prepareStatement(anyString())).thenThrow(new SQLException("Error"));

            boolean result = availabilityDAO.deleteByBarberId(1);

            assertFalse(result);
        }
    }
}
