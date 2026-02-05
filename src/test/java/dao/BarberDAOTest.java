package dao;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import model.Barber;
import util.DatabaseUtil;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BarberDAOTest {

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

    private BarberDAO barberDAO;

    @BeforeEach
    void setUp() {
        barberDAO = new BarberDAO();
    }

    @Test
    void testFindByEmail_success() throws SQLException {
        try (MockedStatic<DatabaseUtil> dbUtil = mockStatic(DatabaseUtil.class)) {
            dbUtil.when(DatabaseUtil::getConnection).thenReturn(connection);
            when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
            when(preparedStatement.executeQuery()).thenReturn(resultSet);
            when(resultSet.next()).thenReturn(true);
            when(resultSet.getInt("barber_id")).thenReturn(1);
            when(resultSet.getString("name")).thenReturn("Jean");
            when(resultSet.getString("email")).thenReturn("jean@test.com");
            when(resultSet.getString("password_hash")).thenReturn("hashedPassword");
            when(resultSet.getString("phone")).thenReturn("0600000001");
            when(resultSet.getString("bio")).thenReturn("Expert coiffeur");
            when(resultSet.getString("status")).thenReturn("active");
            when(resultSet.getTimestamp("created_at")).thenReturn(Timestamp.valueOf(LocalDateTime.now()));

            Barber result = barberDAO.findByEmail("jean@test.com");

            assertNotNull(result);
            assertEquals(1, result.getBarberId());
            assertEquals("Jean", result.getName());
            assertEquals("jean@test.com", result.getEmail());
            verify(preparedStatement).setString(1, "jean@test.com");
        }
    }

    @Test
    void testFindByEmail_notFound() throws SQLException {
        try (MockedStatic<DatabaseUtil> dbUtil = mockStatic(DatabaseUtil.class)) {
            dbUtil.when(DatabaseUtil::getConnection).thenReturn(connection);
            when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
            when(preparedStatement.executeQuery()).thenReturn(resultSet);
            when(resultSet.next()).thenReturn(false);

            Barber result = barberDAO.findByEmail("notfound@test.com");

            assertNull(result);
        }
    }

    @Test
    void testFindByEmail_sqlException() throws SQLException {
        try (MockedStatic<DatabaseUtil> dbUtil = mockStatic(DatabaseUtil.class)) {
            dbUtil.when(DatabaseUtil::getConnection).thenReturn(connection);
            when(connection.prepareStatement(anyString())).thenThrow(new SQLException("Error"));

            Barber result = barberDAO.findByEmail("jean@test.com");

            assertNull(result);
        }
    }

    @Test
    void testFindById_success() throws SQLException {
        try (MockedStatic<DatabaseUtil> dbUtil = mockStatic(DatabaseUtil.class)) {
            dbUtil.when(DatabaseUtil::getConnection).thenReturn(connection);
            when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
            when(preparedStatement.executeQuery()).thenReturn(resultSet);
            when(resultSet.next()).thenReturn(true);
            when(resultSet.getInt("barber_id")).thenReturn(1);
            when(resultSet.getString("name")).thenReturn("Jean");
            when(resultSet.getString("email")).thenReturn("jean@test.com");
            when(resultSet.getString("password_hash")).thenReturn("hashedPassword");
            when(resultSet.getString("phone")).thenReturn("0600000001");
            when(resultSet.getString("bio")).thenReturn("Expert");
            when(resultSet.getString("status")).thenReturn("active");
            when(resultSet.getTimestamp("created_at")).thenReturn(null);

            Barber result = barberDAO.findById(1);

            assertNotNull(result);
            assertEquals(1, result.getBarberId());
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

            Barber result = barberDAO.findById(99);

            assertNull(result);
        }
    }

    @Test
    void testFindById_sqlException() throws SQLException {
        try (MockedStatic<DatabaseUtil> dbUtil = mockStatic(DatabaseUtil.class)) {
            dbUtil.when(DatabaseUtil::getConnection).thenReturn(connection);
            when(connection.prepareStatement(anyString())).thenThrow(new SQLException("Error"));

            Barber result = barberDAO.findById(1);

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
            when(resultSet.getInt("barber_id")).thenReturn(1, 2);
            when(resultSet.getString("name")).thenReturn("Jean", "Paul");
            when(resultSet.getString("email")).thenReturn("jean@test.com", "paul@test.com");
            when(resultSet.getString("password_hash")).thenReturn("hash1", "hash2");
            when(resultSet.getString("phone")).thenReturn("0600000001", "0600000002");
            when(resultSet.getString("bio")).thenReturn("Bio1", "Bio2");
            when(resultSet.getString("status")).thenReturn("active", "active");
            when(resultSet.getTimestamp("created_at")).thenReturn(null);

            List<Barber> result = barberDAO.findAll();

            assertNotNull(result);
            assertEquals(2, result.size());
            assertEquals("Jean", result.get(0).getName());
            assertEquals("Paul", result.get(1).getName());
        }
    }

    @Test
    void testFindAll_sqlException() throws SQLException {
        try (MockedStatic<DatabaseUtil> dbUtil = mockStatic(DatabaseUtil.class)) {
            dbUtil.when(DatabaseUtil::getConnection).thenReturn(connection);
            when(connection.createStatement()).thenThrow(new SQLException("Error"));

            List<Barber> result = barberDAO.findAll();

            assertNotNull(result);
            assertTrue(result.isEmpty());
        }
    }

    @Test
    void testFindAllActive_success() throws SQLException {
        try (MockedStatic<DatabaseUtil> dbUtil = mockStatic(DatabaseUtil.class)) {
            dbUtil.when(DatabaseUtil::getConnection).thenReturn(connection);
            when(connection.createStatement()).thenReturn(statement);
            when(statement.executeQuery(anyString())).thenReturn(resultSet);
            when(resultSet.next()).thenReturn(true, false);
            when(resultSet.getInt("barber_id")).thenReturn(1);
            when(resultSet.getString("name")).thenReturn("Jean");
            when(resultSet.getString("email")).thenReturn("jean@test.com");
            when(resultSet.getString("password_hash")).thenReturn("hash");
            when(resultSet.getString("phone")).thenReturn("0600000001");
            when(resultSet.getString("bio")).thenReturn("Bio");
            when(resultSet.getString("status")).thenReturn("active");
            when(resultSet.getTimestamp("created_at")).thenReturn(null);

            List<Barber> result = barberDAO.findAllActive();

            assertNotNull(result);
            assertEquals(1, result.size());
            assertTrue(result.get(0).isActive());
        }
    }

    @Test
    void testFindAllActive_sqlException() throws SQLException {
        try (MockedStatic<DatabaseUtil> dbUtil = mockStatic(DatabaseUtil.class)) {
            dbUtil.when(DatabaseUtil::getConnection).thenReturn(connection);
            when(connection.createStatement()).thenThrow(new SQLException("Error"));

            List<Barber> result = barberDAO.findAllActive();

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
            when(resultSet.getInt("barber_id")).thenReturn(1);
            when(resultSet.getString("name")).thenReturn("Jean");
            when(resultSet.getString("email")).thenReturn("jean@test.com");
            when(resultSet.getString("password_hash")).thenReturn("hash");
            when(resultSet.getString("phone")).thenReturn("0600000001");
            when(resultSet.getString("bio")).thenReturn("Bio");
            when(resultSet.getString("status")).thenReturn("active");
            when(resultSet.getTimestamp("created_at")).thenReturn(null);

            List<Barber> result = barberDAO.findAll(1, 10);

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
            when(resultSet.getInt("total")).thenReturn(10);

            int result = barberDAO.getTotalCount();

            assertEquals(10, result);
        }
    }

    @Test
    void testGetTotalCount_sqlException() throws SQLException {
        try (MockedStatic<DatabaseUtil> dbUtil = mockStatic(DatabaseUtil.class)) {
            dbUtil.when(DatabaseUtil::getConnection).thenReturn(connection);
            when(connection.createStatement()).thenThrow(new SQLException("Error"));

            int result = barberDAO.getTotalCount();

            assertEquals(0, result);
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

            Barber barber = new Barber();
            barber.setName("Jean");
            barber.setEmail("jean@test.com");
            barber.setPasswordHash("hash");
            barber.setPhone("0600000001");
            barber.setBio("Expert");
            barber.setStatus("active");

            boolean result = barberDAO.create(barber);

            assertTrue(result);
            assertEquals(5, barber.getBarberId());
            verify(preparedStatement).setString(1, "Jean");
            verify(preparedStatement).setString(2, "jean@test.com");
            verify(preparedStatement).setString(3, "hash");
            verify(preparedStatement).setString(4, "0600000001");
            verify(preparedStatement).setString(5, "Expert");
            verify(preparedStatement).setString(6, "active");
        }
    }

    @Test
    void testCreate_sqlException() throws SQLException {
        try (MockedStatic<DatabaseUtil> dbUtil = mockStatic(DatabaseUtil.class)) {
            dbUtil.when(DatabaseUtil::getConnection).thenReturn(connection);
            when(connection.prepareStatement(anyString(), eq(Statement.RETURN_GENERATED_KEYS))).thenThrow(new SQLException("Error"));

            Barber barber = new Barber();
            barber.setName("Jean");

            boolean result = barberDAO.create(barber);

            assertFalse(result);
        }
    }

    @Test
    void testUpdate_success() throws SQLException {
        try (MockedStatic<DatabaseUtil> dbUtil = mockStatic(DatabaseUtil.class)) {
            dbUtil.when(DatabaseUtil::getConnection).thenReturn(connection);
            when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
            when(preparedStatement.executeUpdate()).thenReturn(1);

            Barber barber = new Barber();
            barber.setBarberId(1);
            barber.setName("Jean Updated");
            barber.setEmail("jean@test.com");
            barber.setPhone("0600000001");
            barber.setBio("Expert");
            barber.setStatus("active");

            boolean result = barberDAO.update(barber);

            assertTrue(result);
            verify(preparedStatement).setString(1, "Jean Updated");
            verify(preparedStatement).setInt(6, 1);
        }
    }

    @Test
    void testUpdate_sqlException() throws SQLException {
        try (MockedStatic<DatabaseUtil> dbUtil = mockStatic(DatabaseUtil.class)) {
            dbUtil.when(DatabaseUtil::getConnection).thenReturn(connection);
            when(connection.prepareStatement(anyString())).thenThrow(new SQLException("Error"));

            Barber barber = new Barber();
            barber.setBarberId(1);

            boolean result = barberDAO.update(barber);

            assertFalse(result);
        }
    }

    @Test
    void testDelete_success() throws SQLException {
        try (MockedStatic<DatabaseUtil> dbUtil = mockStatic(DatabaseUtil.class)) {
            dbUtil.when(DatabaseUtil::getConnection).thenReturn(connection);
            when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
            when(preparedStatement.executeUpdate()).thenReturn(1);

            boolean result = barberDAO.delete(1);

            assertTrue(result);
            verify(preparedStatement).setInt(1, 1);
        }
    }

    @Test
    void testDelete_sqlException() throws SQLException {
        try (MockedStatic<DatabaseUtil> dbUtil = mockStatic(DatabaseUtil.class)) {
            dbUtil.when(DatabaseUtil::getConnection).thenReturn(connection);
            when(connection.prepareStatement(anyString())).thenThrow(new SQLException("Error"));

            boolean result = barberDAO.delete(1);

            assertFalse(result);
        }
    }

    @Test
    void testUpdateStatus_success() throws SQLException {
        try (MockedStatic<DatabaseUtil> dbUtil = mockStatic(DatabaseUtil.class)) {
            dbUtil.when(DatabaseUtil::getConnection).thenReturn(connection);
            when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
            when(preparedStatement.executeUpdate()).thenReturn(1);

            boolean result = barberDAO.updateStatus(1, "inactive");

            assertTrue(result);
            verify(preparedStatement).setString(1, "inactive");
            verify(preparedStatement).setInt(2, 1);
        }
    }

    @Test
    void testUpdateStatus_sqlException() throws SQLException {
        try (MockedStatic<DatabaseUtil> dbUtil = mockStatic(DatabaseUtil.class)) {
            dbUtil.when(DatabaseUtil::getConnection).thenReturn(connection);
            when(connection.prepareStatement(anyString())).thenThrow(new SQLException("Error"));

            boolean result = barberDAO.updateStatus(1, "inactive");

            assertFalse(result);
        }
    }
}
