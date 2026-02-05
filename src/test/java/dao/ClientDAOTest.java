package dao;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import model.Client;
import util.DatabaseUtil;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ClientDAOTest {

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

    private ClientDAO clientDAO;

    @BeforeEach
    void setUp() {
        clientDAO = new ClientDAO();
    }

    @Test
    void testFindByEmail_success() throws SQLException {
        try (MockedStatic<DatabaseUtil> dbUtil = mockStatic(DatabaseUtil.class)) {
            dbUtil.when(DatabaseUtil::getConnection).thenReturn(connection);
            when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
            when(preparedStatement.executeQuery()).thenReturn(resultSet);
            when(resultSet.next()).thenReturn(true);
            when(resultSet.getInt("client_id")).thenReturn(1);
            when(resultSet.getString("name")).thenReturn("Marie");
            when(resultSet.getString("email")).thenReturn("marie@test.com");
            when(resultSet.getString("password_hash")).thenReturn("hashedPassword");
            when(resultSet.getString("phone")).thenReturn("0600000001");
            when(resultSet.getInt("points_balance")).thenReturn(100);
            when(resultSet.getString("loyalty_status")).thenReturn("fidele");
            when(resultSet.getTimestamp("created_at")).thenReturn(Timestamp.valueOf(LocalDateTime.now()));

            Client result = clientDAO.findByEmail("marie@test.com");

            assertNotNull(result);
            assertEquals(1, result.getClientId());
            assertEquals("Marie", result.getName());
            assertEquals("marie@test.com", result.getEmail());
            verify(preparedStatement).setString(1, "marie@test.com");
        }
    }

    @Test
    void testFindByEmail_notFound() throws SQLException {
        try (MockedStatic<DatabaseUtil> dbUtil = mockStatic(DatabaseUtil.class)) {
            dbUtil.when(DatabaseUtil::getConnection).thenReturn(connection);
            when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
            when(preparedStatement.executeQuery()).thenReturn(resultSet);
            when(resultSet.next()).thenReturn(false);

            Client result = clientDAO.findByEmail("notfound@test.com");

            assertNull(result);
        }
    }

    @Test
    void testFindByEmail_sqlException() throws SQLException {
        try (MockedStatic<DatabaseUtil> dbUtil = mockStatic(DatabaseUtil.class)) {
            dbUtil.when(DatabaseUtil::getConnection).thenReturn(connection);
            when(connection.prepareStatement(anyString())).thenThrow(new SQLException("Error"));

            Client result = clientDAO.findByEmail("marie@test.com");

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
            when(resultSet.getInt("client_id")).thenReturn(1);
            when(resultSet.getString("name")).thenReturn("Marie");
            when(resultSet.getString("email")).thenReturn("marie@test.com");
            when(resultSet.getString("password_hash")).thenReturn("hashedPassword");
            when(resultSet.getString("phone")).thenReturn("0600000001");
            when(resultSet.getInt("points_balance")).thenReturn(100);
            when(resultSet.getString("loyalty_status")).thenReturn("fidele");
            when(resultSet.getTimestamp("created_at")).thenReturn(null);

            Client result = clientDAO.findById(1);

            assertNotNull(result);
            assertEquals(1, result.getClientId());
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

            Client result = clientDAO.findById(99);

            assertNull(result);
        }
    }

    @Test
    void testFindById_sqlException() throws SQLException {
        try (MockedStatic<DatabaseUtil> dbUtil = mockStatic(DatabaseUtil.class)) {
            dbUtil.when(DatabaseUtil::getConnection).thenReturn(connection);
            when(connection.prepareStatement(anyString())).thenThrow(new SQLException("Error"));

            Client result = clientDAO.findById(1);

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
            when(resultSet.getInt("client_id")).thenReturn(1, 2);
            when(resultSet.getString("name")).thenReturn("Marie", "Luc");
            when(resultSet.getString("email")).thenReturn("marie@test.com", "luc@test.com");
            when(resultSet.getString("password_hash")).thenReturn("hash1", "hash2");
            when(resultSet.getString("phone")).thenReturn("0600000001", "0600000002");
            when(resultSet.getInt("points_balance")).thenReturn(100, 50);
            when(resultSet.getString("loyalty_status")).thenReturn("fidele", "standard");
            when(resultSet.getTimestamp("created_at")).thenReturn(null);

            List<Client> result = clientDAO.findAll();

            assertNotNull(result);
            assertEquals(2, result.size());
            assertEquals("Marie", result.get(0).getName());
            assertEquals("Luc", result.get(1).getName());
        }
    }

    @Test
    void testFindAll_sqlException() throws SQLException {
        try (MockedStatic<DatabaseUtil> dbUtil = mockStatic(DatabaseUtil.class)) {
            dbUtil.when(DatabaseUtil::getConnection).thenReturn(connection);
            when(connection.createStatement()).thenThrow(new SQLException("Error"));

            List<Client> result = clientDAO.findAll();

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
            when(resultSet.getInt("client_id")).thenReturn(1);
            when(resultSet.getString("name")).thenReturn("Marie");
            when(resultSet.getString("email")).thenReturn("marie@test.com");
            when(resultSet.getString("password_hash")).thenReturn("hash");
            when(resultSet.getString("phone")).thenReturn("0600000001");
            when(resultSet.getInt("points_balance")).thenReturn(100);
            when(resultSet.getString("loyalty_status")).thenReturn("fidele");
            when(resultSet.getTimestamp("created_at")).thenReturn(null);

            List<Client> result = clientDAO.findAll(1, 10);

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
            when(resultSet.getInt("total")).thenReturn(25);

            int result = clientDAO.getTotalCount();

            assertEquals(25, result);
        }
    }

    @Test
    void testGetTotalCount_sqlException() throws SQLException {
        try (MockedStatic<DatabaseUtil> dbUtil = mockStatic(DatabaseUtil.class)) {
            dbUtil.when(DatabaseUtil::getConnection).thenReturn(connection);
            when(connection.createStatement()).thenThrow(new SQLException("Error"));

            int result = clientDAO.getTotalCount();

            assertEquals(0, result);
        }
    }

    @Test
    void testGetLoyalClientCount_success() throws SQLException {
        try (MockedStatic<DatabaseUtil> dbUtil = mockStatic(DatabaseUtil.class)) {
            dbUtil.when(DatabaseUtil::getConnection).thenReturn(connection);
            when(connection.createStatement()).thenReturn(statement);
            when(statement.executeQuery(anyString())).thenReturn(resultSet);
            when(resultSet.next()).thenReturn(true);
            when(resultSet.getInt("total")).thenReturn(10);

            int result = clientDAO.getLoyalClientCount();

            assertEquals(10, result);
        }
    }

    @Test
    void testGetLoyalClientCount_sqlException() throws SQLException {
        try (MockedStatic<DatabaseUtil> dbUtil = mockStatic(DatabaseUtil.class)) {
            dbUtil.when(DatabaseUtil::getConnection).thenReturn(connection);
            when(connection.createStatement()).thenThrow(new SQLException("Error"));

            int result = clientDAO.getLoyalClientCount();

            assertEquals(0, result);
        }
    }

    @Test
    void testGetTotalPoints_success() throws SQLException {
        try (MockedStatic<DatabaseUtil> dbUtil = mockStatic(DatabaseUtil.class)) {
            dbUtil.when(DatabaseUtil::getConnection).thenReturn(connection);
            when(connection.createStatement()).thenReturn(statement);
            when(statement.executeQuery(anyString())).thenReturn(resultSet);
            when(resultSet.next()).thenReturn(true);
            when(resultSet.getInt("total")).thenReturn(5000);

            int result = clientDAO.getTotalPoints();

            assertEquals(5000, result);
        }
    }

    @Test
    void testGetTotalPoints_sqlException() throws SQLException {
        try (MockedStatic<DatabaseUtil> dbUtil = mockStatic(DatabaseUtil.class)) {
            dbUtil.when(DatabaseUtil::getConnection).thenReturn(connection);
            when(connection.createStatement()).thenThrow(new SQLException("Error"));

            int result = clientDAO.getTotalPoints();

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

            Client client = new Client();
            client.setName("Marie");
            client.setEmail("marie@test.com");
            client.setPasswordHash("hash");
            client.setPhone("0600000001");
            client.setPointsBalance(0);
            client.setLoyaltyStatus("standard");

            boolean result = clientDAO.create(client);

            assertTrue(result);
            assertEquals(5, client.getClientId());
            verify(preparedStatement).setString(1, "Marie");
            verify(preparedStatement).setString(2, "marie@test.com");
            verify(preparedStatement).setString(3, "hash");
            verify(preparedStatement).setString(4, "0600000001");
            verify(preparedStatement).setInt(5, 0);
            verify(preparedStatement).setString(6, "standard");
        }
    }

    @Test
    void testCreate_sqlException() throws SQLException {
        try (MockedStatic<DatabaseUtil> dbUtil = mockStatic(DatabaseUtil.class)) {
            dbUtil.when(DatabaseUtil::getConnection).thenReturn(connection);
            when(connection.prepareStatement(anyString(), eq(Statement.RETURN_GENERATED_KEYS))).thenThrow(new SQLException("Error"));

            Client client = new Client();
            client.setName("Marie");

            boolean result = clientDAO.create(client);

            assertFalse(result);
        }
    }

    @Test
    void testUpdate_success() throws SQLException {
        try (MockedStatic<DatabaseUtil> dbUtil = mockStatic(DatabaseUtil.class)) {
            dbUtil.when(DatabaseUtil::getConnection).thenReturn(connection);
            when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
            when(preparedStatement.executeUpdate()).thenReturn(1);

            Client client = new Client();
            client.setClientId(1);
            client.setName("Marie Updated");
            client.setEmail("marie@test.com");
            client.setPhone("0600000001");

            boolean result = clientDAO.update(client);

            assertTrue(result);
            verify(preparedStatement).setString(1, "Marie Updated");
            verify(preparedStatement).setInt(4, 1);
        }
    }

    @Test
    void testUpdate_sqlException() throws SQLException {
        try (MockedStatic<DatabaseUtil> dbUtil = mockStatic(DatabaseUtil.class)) {
            dbUtil.when(DatabaseUtil::getConnection).thenReturn(connection);
            when(connection.prepareStatement(anyString())).thenThrow(new SQLException("Error"));

            Client client = new Client();
            client.setClientId(1);

            boolean result = clientDAO.update(client);

            assertFalse(result);
        }
    }

    @Test
    void testUpdatePoints_success() throws SQLException {
        try (MockedStatic<DatabaseUtil> dbUtil = mockStatic(DatabaseUtil.class)) {
            dbUtil.when(DatabaseUtil::getConnection).thenReturn(connection);
            when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
            when(preparedStatement.executeUpdate()).thenReturn(1);

            boolean result = clientDAO.updatePoints(1, 200);

            assertTrue(result);
            verify(preparedStatement).setInt(1, 200);
            verify(preparedStatement).setInt(2, 1);
        }
    }

    @Test
    void testUpdatePoints_sqlException() throws SQLException {
        try (MockedStatic<DatabaseUtil> dbUtil = mockStatic(DatabaseUtil.class)) {
            dbUtil.when(DatabaseUtil::getConnection).thenReturn(connection);
            when(connection.prepareStatement(anyString())).thenThrow(new SQLException("Error"));

            boolean result = clientDAO.updatePoints(1, 200);

            assertFalse(result);
        }
    }

    @Test
    void testUpdateLoyaltyStatus_success() throws SQLException {
        try (MockedStatic<DatabaseUtil> dbUtil = mockStatic(DatabaseUtil.class)) {
            dbUtil.when(DatabaseUtil::getConnection).thenReturn(connection);
            when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
            when(preparedStatement.executeUpdate()).thenReturn(1);

            boolean result = clientDAO.updateLoyaltyStatus(1, "fidele");

            assertTrue(result);
            verify(preparedStatement).setString(1, "fidele");
            verify(preparedStatement).setInt(2, 1);
        }
    }

    @Test
    void testUpdateLoyaltyStatus_sqlException() throws SQLException {
        try (MockedStatic<DatabaseUtil> dbUtil = mockStatic(DatabaseUtil.class)) {
            dbUtil.when(DatabaseUtil::getConnection).thenReturn(connection);
            when(connection.prepareStatement(anyString())).thenThrow(new SQLException("Error"));

            boolean result = clientDAO.updateLoyaltyStatus(1, "fidele");

            assertFalse(result);
        }
    }

    @Test
    void testGetCompletedAppointmentCount_success() throws SQLException {
        try (MockedStatic<DatabaseUtil> dbUtil = mockStatic(DatabaseUtil.class)) {
            dbUtil.when(DatabaseUtil::getConnection).thenReturn(connection);
            when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
            when(preparedStatement.executeQuery()).thenReturn(resultSet);
            when(resultSet.next()).thenReturn(true);
            when(resultSet.getInt("count")).thenReturn(15);

            int result = clientDAO.getCompletedAppointmentCount(1);

            assertEquals(15, result);
            verify(preparedStatement).setInt(1, 1);
        }
    }

    @Test
    void testGetCompletedAppointmentCount_sqlException() throws SQLException {
        try (MockedStatic<DatabaseUtil> dbUtil = mockStatic(DatabaseUtil.class)) {
            dbUtil.when(DatabaseUtil::getConnection).thenReturn(connection);
            when(connection.prepareStatement(anyString())).thenThrow(new SQLException("Error"));

            int result = clientDAO.getCompletedAppointmentCount(1);

            assertEquals(0, result);
        }
    }
}
