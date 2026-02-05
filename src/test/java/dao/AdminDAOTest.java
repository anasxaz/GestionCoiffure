package dao;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import model.Admin;
import util.DatabaseUtil;

import java.sql.*;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AdminDAOTest {

    @Mock
    private Connection connection;

    @Mock
    private PreparedStatement preparedStatement;

    @Mock
    private ResultSet resultSet;

    @Mock
    private ResultSet generatedKeys;

    private AdminDAO adminDAO;

    @BeforeEach
    void setUp() {
        adminDAO = new AdminDAO();
    }

    @Test
    void testFindByEmail_success() throws SQLException {
        try (MockedStatic<DatabaseUtil> dbUtil = mockStatic(DatabaseUtil.class)) {
            dbUtil.when(DatabaseUtil::getConnection).thenReturn(connection);
            when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
            when(preparedStatement.executeQuery()).thenReturn(resultSet);
            when(resultSet.next()).thenReturn(true);
            when(resultSet.getInt("admin_id")).thenReturn(1);
            when(resultSet.getString("name")).thenReturn("Admin");
            when(resultSet.getString("email")).thenReturn("admin@test.com");
            when(resultSet.getString("password_hash")).thenReturn("hashedPassword");
            when(resultSet.getTimestamp("created_at")).thenReturn(Timestamp.valueOf(LocalDateTime.now()));

            Admin result = adminDAO.findByEmail("admin@test.com");

            assertNotNull(result);
            assertEquals(1, result.getAdminId());
            assertEquals("Admin", result.getName());
            assertEquals("admin@test.com", result.getEmail());
            verify(preparedStatement).setString(1, "admin@test.com");
        }
    }

    @Test
    void testFindByEmail_notFound() throws SQLException {
        try (MockedStatic<DatabaseUtil> dbUtil = mockStatic(DatabaseUtil.class)) {
            dbUtil.when(DatabaseUtil::getConnection).thenReturn(connection);
            when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
            when(preparedStatement.executeQuery()).thenReturn(resultSet);
            when(resultSet.next()).thenReturn(false);

            Admin result = adminDAO.findByEmail("notfound@test.com");

            assertNull(result);
        }
    }

    @Test
    void testFindByEmail_sqlException() throws SQLException {
        try (MockedStatic<DatabaseUtil> dbUtil = mockStatic(DatabaseUtil.class)) {
            dbUtil.when(DatabaseUtil::getConnection).thenReturn(connection);
            when(connection.prepareStatement(anyString())).thenThrow(new SQLException("Connection error"));

            Admin result = adminDAO.findByEmail("admin@test.com");

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
            when(resultSet.getInt("admin_id")).thenReturn(1);
            when(resultSet.getString("name")).thenReturn("Admin");
            when(resultSet.getString("email")).thenReturn("admin@test.com");
            when(resultSet.getString("password_hash")).thenReturn("hashedPassword");
            when(resultSet.getTimestamp("created_at")).thenReturn(null);

            Admin result = adminDAO.findById(1);

            assertNotNull(result);
            assertEquals(1, result.getAdminId());
            assertEquals("Admin", result.getName());
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

            Admin result = adminDAO.findById(99);

            assertNull(result);
        }
    }

    @Test
    void testFindById_sqlException() throws SQLException {
        try (MockedStatic<DatabaseUtil> dbUtil = mockStatic(DatabaseUtil.class)) {
            dbUtil.when(DatabaseUtil::getConnection).thenReturn(connection);
            when(connection.prepareStatement(anyString())).thenThrow(new SQLException("Error"));

            Admin result = adminDAO.findById(1);

            assertNull(result);
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

            Admin admin = new Admin();
            admin.setName("NewAdmin");
            admin.setEmail("newadmin@test.com");
            admin.setPasswordHash("hashedPassword");

            boolean result = adminDAO.create(admin);

            assertTrue(result);
            assertEquals(5, admin.getAdminId());
            verify(preparedStatement).setString(1, "NewAdmin");
            verify(preparedStatement).setString(2, "newadmin@test.com");
            verify(preparedStatement).setString(3, "hashedPassword");
        }
    }

    @Test
    void testCreate_noRowsAffected() throws SQLException {
        try (MockedStatic<DatabaseUtil> dbUtil = mockStatic(DatabaseUtil.class)) {
            dbUtil.when(DatabaseUtil::getConnection).thenReturn(connection);
            when(connection.prepareStatement(anyString(), eq(Statement.RETURN_GENERATED_KEYS))).thenReturn(preparedStatement);
            when(preparedStatement.executeUpdate()).thenReturn(0);

            Admin admin = new Admin();
            admin.setName("NewAdmin");
            admin.setEmail("newadmin@test.com");
            admin.setPasswordHash("hashedPassword");

            boolean result = adminDAO.create(admin);

            assertFalse(result);
        }
    }

    @Test
    void testCreate_sqlException() throws SQLException {
        try (MockedStatic<DatabaseUtil> dbUtil = mockStatic(DatabaseUtil.class)) {
            dbUtil.when(DatabaseUtil::getConnection).thenReturn(connection);
            when(connection.prepareStatement(anyString(), eq(Statement.RETURN_GENERATED_KEYS))).thenThrow(new SQLException("Error"));

            Admin admin = new Admin();
            admin.setName("NewAdmin");
            admin.setEmail("newadmin@test.com");
            admin.setPasswordHash("hashedPassword");

            boolean result = adminDAO.create(admin);

            assertFalse(result);
        }
    }
}
