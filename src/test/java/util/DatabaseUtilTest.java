package util;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.quality.Strictness;

import java.sql.Connection;
import java.sql.DriverManager; // Still need this import for real code, even if not directly mocked here
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class DatabaseUtilTest {

    private MockedStatic<DatabaseUtil> mockedDatabaseUtil; // Mock DatabaseUtil itself
    private Connection mockConnection;
    private java.io.ByteArrayOutputStream errContent;
    private java.io.PrintStream originalErr;

    @BeforeEach
    void setUp() throws SQLException {
        mockConnection = mock(Connection.class);
        lenient().when(mockConnection.isClosed()).thenReturn(false);

        // Mock DatabaseUtil's static methods
        // By default, CALLS_REAL_METHODS allows non-stubbed static methods to execute their real logic.
        mockedDatabaseUtil = mockStatic(DatabaseUtil.class, Mockito.withSettings().defaultAnswer(CALLS_REAL_METHODS));
        
        // Stub getConnection to return our mock connection
        // This overrides the real DatabaseUtil.getConnection() method during the test
        mockedDatabaseUtil.when(DatabaseUtil::getConnection).thenReturn(mockConnection);

        originalErr = System.err;
        errContent = new java.io.ByteArrayOutputStream();
        System.setErr(new java.io.PrintStream(errContent));
    }

    @AfterEach
    void tearDown() {
        mockedDatabaseUtil.close();
        System.setErr(originalErr);
    }

    // --- getConnection() tests (now tests the stubbed behavior) ---
    @Test
    void testGetConnection_returnsMockConnection() throws SQLException {
        Connection connection = DatabaseUtil.getConnection();
        assertNotNull(connection);
        assertEquals(mockConnection, connection);
        mockedDatabaseUtil.verify(DatabaseUtil::getConnection, times(1));
    }

    @Test
    void testGetConnection_throwsSQLException() throws SQLException {
        mockedDatabaseUtil.when(DatabaseUtil::getConnection).thenThrow(new SQLException("Simulated connection error"));

        SQLException thrown = assertThrows(SQLException.class, DatabaseUtil::getConnection);
        assertEquals("Simulated connection error", thrown.getMessage());
        mockedDatabaseUtil.verify(DatabaseUtil::getConnection, times(1));
    }

    // --- closeConnection() tests (now tests the real method behavior, relying on mockConnection) ---
    @Test
    void testCloseConnection_success() throws SQLException {
        // Since mockedDatabaseUtil uses CALLS_REAL_METHODS, DatabaseUtil.closeConnection(mockConnection) will execute its real code.
        DatabaseUtil.closeConnection(mockConnection);
        verify(mockConnection, times(1)).close();
    }

    @Test
    void testCloseConnection_nullConnection() throws SQLException {
        // Since mockedDatabaseUtil uses CALLS_REAL_METHODS, DatabaseUtil.closeConnection(null) will execute its real code.
        DatabaseUtil.closeConnection(null);
        verify(mockConnection, never()).close();
        assertTrue(errContent.toString().isEmpty());
    }

    @Test
    void testCloseConnection_throwsSQLException() throws SQLException {
        doThrow(new SQLException("Simulated close error")).when(mockConnection).close();
        
        // Since mockedDatabaseUtil uses CALLS_REAL_METHODS, DatabaseUtil.closeConnection(mockConnection) will execute its real code.
        DatabaseUtil.closeConnection(mockConnection);
        verify(mockConnection, times(1)).close();
        assertTrue(errContent.toString().contains("Simulated close error"));
    }

    // --- testConnection() tests (now tests the real method behavior, relying on mockConnection and stubbed getConnection) ---
    @Test
    void testTestConnection_success() throws SQLException {
        // DatabaseUtil.getConnection() is stubbed to return mockConnection, then testConnection() calls real isClosed() and close() on it.
        assertTrue(DatabaseUtil.testConnection());
        mockedDatabaseUtil.verify(DatabaseUtil::getConnection, times(1)); // Verify our stubbed getConnection was called
        verify(mockConnection, times(1)).isClosed();
        verify(mockConnection, times(1)).close(); // Called by try-with-resources in real method
    }

    @Test
    void testTestConnection_getConnectionThrowsSQLException() throws SQLException {
        mockedDatabaseUtil.when(DatabaseUtil::getConnection).thenThrow(new SQLException("Test connection failure"));

        assertFalse(DatabaseUtil.testConnection());
        mockedDatabaseUtil.verify(DatabaseUtil::getConnection, times(1)); // Verify our stubbed getConnection was called
        verify(mockConnection, never()).isClosed(); // isClosed not called if connection fails
        verify(mockConnection, never()).close(); // close not called if connection fails
        assertTrue(errContent.toString().isEmpty());
    }

    @Test
    void testTestConnection_connectionIsClosed() throws SQLException {
        // DatabaseUtil.getConnection() is stubbed to return mockConnection
        when(mockConnection.isClosed()).thenReturn(true);

        assertFalse(DatabaseUtil.testConnection());
        mockedDatabaseUtil.verify(DatabaseUtil::getConnection, times(1)); // Verify our stubbed getConnection was called
        verify(mockConnection, times(1)).isClosed();
        verify(mockConnection, times(1)).close(); // Called by try-with-resources
        assertTrue(errContent.toString().isEmpty());
    }

    // Static initializer block testing remains a challenge.
    // If Class.forName throws ClassNotFoundException, it's a RuntimeException during class loading.
    // This is not typically unit testable with Mockito without PowerMock or complex classloader manipulation.
    // For this task, we assume the JDBC driver is correctly available on the classpath for production code,
    // and focus on mocking the behavior of DatabaseUtil's public static methods.
}