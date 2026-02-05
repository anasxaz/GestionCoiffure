package dao;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import service.Service;
import util.DatabaseUtil;

import java.sql.*;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ServiceDAOTest {

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

    private ServiceDAO serviceDAO;

    @BeforeEach
    void setUp() {
        serviceDAO = new ServiceDAO();
    }

    @Test
    void testFindById_success() throws SQLException {
        try (MockedStatic<DatabaseUtil> dbUtil = mockStatic(DatabaseUtil.class)) {
            dbUtil.when(DatabaseUtil::getConnection).thenReturn(connection);
            when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
            when(preparedStatement.executeQuery()).thenReturn(resultSet);
            when(resultSet.next()).thenReturn(true);
            when(resultSet.getInt("service_id")).thenReturn(1);
            when(resultSet.getString("name")).thenReturn("Coupe");
            when(resultSet.getString("description")).thenReturn("Coupe classique");
            when(resultSet.getInt("duration")).thenReturn(30);
            when(resultSet.getDouble("price")).thenReturn(20.0);
            when(resultSet.getBoolean("is_active")).thenReturn(true);

            Service result = serviceDAO.findById(1);

            assertNotNull(result);
            assertEquals(1, result.getServiceId());
            assertEquals("Coupe", result.getName());
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

            Service result = serviceDAO.findById(99);

            assertNull(result);
        }
    }

    @Test
    void testFindById_sqlException() throws SQLException {
        try (MockedStatic<DatabaseUtil> dbUtil = mockStatic(DatabaseUtil.class)) {
            dbUtil.when(DatabaseUtil::getConnection).thenReturn(connection);
            when(connection.prepareStatement(anyString())).thenThrow(new SQLException("Connection error"));

            Service result = serviceDAO.findById(1);

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
            when(resultSet.getInt("service_id")).thenReturn(1, 2);
            when(resultSet.getString("name")).thenReturn("Coupe", "Barbe");
            when(resultSet.getString("description")).thenReturn("Desc1", "Desc2");
            when(resultSet.getInt("duration")).thenReturn(30, 20);
            when(resultSet.getDouble("price")).thenReturn(20.0, 15.0);
            when(resultSet.getBoolean("is_active")).thenReturn(true, true);

            List<Service> result = serviceDAO.findAll();

            assertNotNull(result);
            assertEquals(2, result.size());
            assertEquals("Coupe", result.get(0).getName());
            assertEquals("Barbe", result.get(1).getName());
        }
    }

    @Test
    void testFindAll_sqlException() throws SQLException {
        try (MockedStatic<DatabaseUtil> dbUtil = mockStatic(DatabaseUtil.class)) {
            dbUtil.when(DatabaseUtil::getConnection).thenReturn(connection);
            when(connection.createStatement()).thenThrow(new SQLException("Error"));

            List<Service> result = serviceDAO.findAll();

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
            when(resultSet.getInt("service_id")).thenReturn(1);
            when(resultSet.getString("name")).thenReturn("Coupe");
            when(resultSet.getString("description")).thenReturn("Desc");
            when(resultSet.getInt("duration")).thenReturn(30);
            when(resultSet.getDouble("price")).thenReturn(20.0);
            when(resultSet.getBoolean("is_active")).thenReturn(true);

            List<Service> result = serviceDAO.findAllActive();

            assertNotNull(result);
            assertEquals(1, result.size());
            assertTrue(result.get(0).isActive());
        }
    }

    @Test
    void testFindAllPaginated_success() throws SQLException {
        try (MockedStatic<DatabaseUtil> dbUtil = mockStatic(DatabaseUtil.class)) {
            dbUtil.when(DatabaseUtil::getConnection).thenReturn(connection);
            when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
            when(preparedStatement.executeQuery()).thenReturn(resultSet);
            when(resultSet.next()).thenReturn(true, false);
            when(resultSet.getInt("service_id")).thenReturn(1);
            when(resultSet.getString("name")).thenReturn("Coupe");
            when(resultSet.getString("description")).thenReturn("Desc");
            when(resultSet.getInt("duration")).thenReturn(30);
            when(resultSet.getDouble("price")).thenReturn(20.0);
            when(resultSet.getBoolean("is_active")).thenReturn(true);

            List<Service> result = serviceDAO.findAll(1, 10);

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
            when(resultSet.getInt("total")).thenReturn(5);

            int result = serviceDAO.getTotalCount();

            assertEquals(5, result);
        }
    }

    @Test
    void testGetTotalCount_sqlException() throws SQLException {
        try (MockedStatic<DatabaseUtil> dbUtil = mockStatic(DatabaseUtil.class)) {
            dbUtil.when(DatabaseUtil::getConnection).thenReturn(connection);
            when(connection.createStatement()).thenThrow(new SQLException("Error"));

            int result = serviceDAO.getTotalCount();

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

            Service service = new Service();
            service.setName("Coupe");
            service.setDescription("Desc");
            service.setDuration(30);
            service.setPrice(20.0);
            service.setActive(true);

            boolean result = serviceDAO.create(service);

            assertTrue(result);
            assertEquals(5, service.getServiceId());
            verify(preparedStatement).setString(1, "Coupe");
            verify(preparedStatement).setString(2, "Desc");
            verify(preparedStatement).setInt(3, 30);
            verify(preparedStatement).setDouble(4, 20.0);
            verify(preparedStatement).setBoolean(5, true);
        }
    }

    @Test
    void testCreate_sqlException() throws SQLException {
        try (MockedStatic<DatabaseUtil> dbUtil = mockStatic(DatabaseUtil.class)) {
            dbUtil.when(DatabaseUtil::getConnection).thenReturn(connection);
            when(connection.prepareStatement(anyString(), eq(Statement.RETURN_GENERATED_KEYS))).thenThrow(new SQLException("Error"));

            Service service = new Service();
            service.setName("Coupe");

            boolean result = serviceDAO.create(service);

            assertFalse(result);
        }
    }

    @Test
    void testUpdate_success() throws SQLException {
        try (MockedStatic<DatabaseUtil> dbUtil = mockStatic(DatabaseUtil.class)) {
            dbUtil.when(DatabaseUtil::getConnection).thenReturn(connection);
            when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
            when(preparedStatement.executeUpdate()).thenReturn(1);

            Service service = new Service();
            service.setServiceId(1);
            service.setName("Coupe Updated");
            service.setDescription("Desc");
            service.setDuration(45);
            service.setPrice(25.0);
            service.setActive(false);

            boolean result = serviceDAO.update(service);

            assertTrue(result);
            verify(preparedStatement).setString(1, "Coupe Updated");
            verify(preparedStatement).setInt(6, 1);
        }
    }

    @Test
    void testUpdate_sqlException() throws SQLException {
        try (MockedStatic<DatabaseUtil> dbUtil = mockStatic(DatabaseUtil.class)) {
            dbUtil.when(DatabaseUtil::getConnection).thenReturn(connection);
            when(connection.prepareStatement(anyString())).thenThrow(new SQLException("Error"));

            Service service = new Service();
            service.setServiceId(1);

            boolean result = serviceDAO.update(service);

            assertFalse(result);
        }
    }

    @Test
    void testDelete_success() throws SQLException {
        try (MockedStatic<DatabaseUtil> dbUtil = mockStatic(DatabaseUtil.class)) {
            dbUtil.when(DatabaseUtil::getConnection).thenReturn(connection);
            when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
            when(preparedStatement.executeUpdate()).thenReturn(1);

            boolean result = serviceDAO.delete(1);

            assertTrue(result);
            verify(preparedStatement).setInt(1, 1);
        }
    }

    @Test
    void testDelete_sqlException() throws SQLException {
        try (MockedStatic<DatabaseUtil> dbUtil = mockStatic(DatabaseUtil.class)) {
            dbUtil.when(DatabaseUtil::getConnection).thenReturn(connection);
            when(connection.prepareStatement(anyString())).thenThrow(new SQLException("Error"));

            boolean result = serviceDAO.delete(1);

            assertFalse(result);
        }
    }

    @Test
    void testToggleActive_success() throws SQLException {
        try (MockedStatic<DatabaseUtil> dbUtil = mockStatic(DatabaseUtil.class)) {
            dbUtil.when(DatabaseUtil::getConnection).thenReturn(connection);
            when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
            when(preparedStatement.executeUpdate()).thenReturn(1);

            boolean result = serviceDAO.toggleActive(1);

            assertTrue(result);
            verify(preparedStatement).setInt(1, 1);
        }
    }

    @Test
    void testToggleActive_sqlException() throws SQLException {
        try (MockedStatic<DatabaseUtil> dbUtil = mockStatic(DatabaseUtil.class)) {
            dbUtil.when(DatabaseUtil::getConnection).thenReturn(connection);
            when(connection.prepareStatement(anyString())).thenThrow(new SQLException("Error"));

            boolean result = serviceDAO.toggleActive(1);

            assertFalse(result);
        }
    }
}
