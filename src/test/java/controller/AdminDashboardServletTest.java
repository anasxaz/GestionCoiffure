package controller;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.quality.Strictness;
import util.DatabaseUtil;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AdminDashboardServletTest {

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private HttpSession session;

    @Mock
    private RequestDispatcher dispatcher;

    @InjectMocks
    private AdminDashboardServlet servlet;

    private MockedStatic<DatabaseUtil> mockedDatabaseUtil;
    private ByteArrayOutputStream errContent;
    private PrintStream originalErr;

    @BeforeEach
    void setUp() throws SQLException {
        lenient().when(request.getContextPath()).thenReturn("/gestionCoiffure");
        lenient().when(request.getRequestDispatcher("/WEB-INF/views/admin/dashboard.jsp")).thenReturn(dispatcher);
        lenient().when(request.getSession(false)).thenReturn(session);

        mockedDatabaseUtil = mockStatic(DatabaseUtil.class, Mockito.withSettings().strictness(Strictness.LENIENT));

        originalErr = System.err;
        errContent = new ByteArrayOutputStream();
        System.setErr(new PrintStream(errContent));
    }

    @AfterEach
    void tearDown() {
        mockedDatabaseUtil.close();
        System.setErr(originalErr);
    }

    @Test
    void testDoGet_sessionNull_redirectLogin() throws ServletException, IOException {
        when(request.getSession(false)).thenReturn(null);

        servlet.doGet(request, response);

        verify(response).sendRedirect("/gestionCoiffure/login");
        verify(request, never()).getRequestDispatcher(anyString());
    }

    @Test
    void testDoGet_userTypeNotAdmin_redirectLogin() throws ServletException, IOException {
        when(session.getAttribute("userType")).thenReturn("client");

        servlet.doGet(request, response);

        verify(response).sendRedirect("/gestionCoiffure/login");
        verify(request, never()).getRequestDispatcher(anyString());
    }

    @Test
    void testDoGet_validSession_forwardWithStats() throws ServletException, IOException, SQLException {
        when(session.getAttribute("userType")).thenReturn("admin");

        // --- Mocks for getTotalBarbers ---
        Connection mockConnectionBarbers = mock(Connection.class);
        PreparedStatement mockPreparedStatementBarbers = mock(PreparedStatement.class);
        ResultSet mockResultSetBarbers = mock(ResultSet.class);
        when(mockConnectionBarbers.prepareStatement(anyString())).thenReturn(mockPreparedStatementBarbers);
        when(mockPreparedStatementBarbers.executeQuery()).thenReturn(mockResultSetBarbers);
        when(mockResultSetBarbers.next()).thenReturn(true, false);
        when(mockResultSetBarbers.getInt("count")).thenReturn(10);
        doNothing().when(mockConnectionBarbers).close();

        // --- Mocks for getTodayAppointments ---
        Connection mockConnectionAppointments = mock(Connection.class);
        PreparedStatement mockPreparedStatementAppointments = mock(PreparedStatement.class);
        ResultSet mockResultSetAppointments = mock(ResultSet.class);
        when(mockConnectionAppointments.prepareStatement(anyString())).thenReturn(mockPreparedStatementAppointments);
        doNothing().when(mockPreparedStatementAppointments).setDate(eq(1), any(java.sql.Date.class));
        when(mockPreparedStatementAppointments.executeQuery()).thenReturn(mockResultSetAppointments);
        when(mockResultSetAppointments.next()).thenReturn(true, false);
        when(mockResultSetAppointments.getInt("count")).thenReturn(5);
        doNothing().when(mockConnectionAppointments).close();

        // --- Mocks for getTotalClients ---
        Connection mockConnectionClients = mock(Connection.class);
        PreparedStatement mockPreparedStatementClients = mock(PreparedStatement.class);
        ResultSet mockResultSetClients = mock(ResultSet.class);
        when(mockConnectionClients.prepareStatement(anyString())).thenReturn(mockPreparedStatementClients);
        when(mockPreparedStatementClients.executeQuery()).thenReturn(mockResultSetClients);
        when(mockResultSetClients.next()).thenReturn(true, false);
        when(mockResultSetClients.getInt("count")).thenReturn(100);
        doNothing().when(mockConnectionClients).close();

        // --- Mocks for getTotalServices ---
        Connection mockConnectionServices = mock(Connection.class);
        PreparedStatement mockPreparedStatementServices = mock(PreparedStatement.class);
        ResultSet mockResultSetServices = mock(ResultSet.class);
        when(mockConnectionServices.prepareStatement(anyString())).thenReturn(mockPreparedStatementServices);
        when(mockPreparedStatementServices.executeQuery()).thenReturn(mockResultSetServices);
        when(mockResultSetServices.next()).thenReturn(true, false);
        when(mockResultSetServices.getInt("count")).thenReturn(20);
        doNothing().when(mockConnectionServices).close();

        // Stubbing the sequence of getConnection calls
        mockedDatabaseUtil.when(DatabaseUtil::getConnection)
                .thenReturn(mockConnectionBarbers)
                .thenReturn(mockConnectionAppointments)
                .thenReturn(mockConnectionClients)
                .thenReturn(mockConnectionServices);

        servlet.doGet(request, response);

        verify(request).setAttribute(eq("totalBarbers"), eq(10));
        verify(request).setAttribute(eq("todayAppointments"), eq(5));
        verify(request).setAttribute(eq("totalClients"), eq(100));
        verify(request).setAttribute(eq("totalServices"), eq(20));
        verify(dispatcher).forward(request, response);
        verify(response, never()).sendRedirect(anyString());
        assertTrue(errContent.toString().isEmpty(), "No error should be printed to System.err");
    }

    @Test
    void testDoGet_getTotalBarbersThrowsException_attributesSetToZero() throws ServletException, IOException, SQLException {
        when(session.getAttribute("userType")).thenReturn("admin");

        // First call to getConnection (by getTotalBarbers) throws an exception.
        // The servlet's outer catch block will handle this, setting all counts to 0.
        mockedDatabaseUtil.when(DatabaseUtil::getConnection)
                .thenThrow(new SQLException("DB error for barbers"));

        servlet.doGet(request, response);

        verify(request).setAttribute(eq("totalBarbers"), eq(0));
        verify(request).setAttribute(eq("todayAppointments"), eq(0));
        verify(request).setAttribute(eq("totalClients"), eq(0));
        verify(request).setAttribute(eq("totalServices"), eq(0));
        verify(dispatcher).forward(request, response);
        verify(response, never()).sendRedirect(anyString());
        assertTrue(errContent.toString().contains("Error getting total barbers: DB error for barbers"));
    }

    @Test
    void testDoGet_getTodayAppointmentsThrowsException_attributesSetToZero() throws ServletException, IOException, SQLException {
        when(session.getAttribute("userType")).thenReturn("admin");

        // Mock for getTotalBarbers (succeeds)
        Connection mockConnForBarbers = mock(Connection.class);
        PreparedStatement mockPsForBarbers = mock(PreparedStatement.class);
        ResultSet mockRsForBarbers = mock(ResultSet.class);
        lenient().when(mockConnForBarbers.prepareStatement(anyString())).thenReturn(mockPsForBarbers);
        lenient().when(mockPsForBarbers.executeQuery()).thenReturn(mockRsForBarbers);
        lenient().when(mockRsForBarbers.next()).thenReturn(true, false);
        lenient().when(mockRsForBarbers.getInt("count")).thenReturn(10);
        lenient().doNothing().when(mockConnForBarbers).close();

        // Setup sequential calls for DatabaseUtil::getConnection
        mockedDatabaseUtil.when(DatabaseUtil::getConnection)
                .thenReturn(mockConnForBarbers) // For getTotalBarbers (succeeds)
                .thenThrow(new SQLException("DB error for appointments")); // For getTodayAppointments (fails)

        servlet.doGet(request, response);

        verify(request).setAttribute(eq("totalBarbers"), eq(10)); // Barbers should be loaded
        verify(request).setAttribute(eq("todayAppointments"), eq(0)); // Appointments should be 0 due to error
        verify(request).setAttribute(eq("totalClients"), eq(0)); // Subsequent calls will also fail due to outer catch
        verify(request).setAttribute(eq("totalServices"), eq(0)); // Subsequent calls will also fail due to outer catch
        verify(dispatcher).forward(request, response);
        verify(response, never()).sendRedirect(anyString());
        assertTrue(errContent.toString().contains("Error getting today's appointments: DB error for appointments"));
    }

    @Test
    void testDoGet_getTotalClientsThrowsException_attributesSetToZero() throws ServletException, IOException, SQLException {
        when(session.getAttribute("userType")).thenReturn("admin");

        // Mock for getTotalBarbers (succeeds)
        Connection mockConnForBarbers = mock(Connection.class);
        PreparedStatement mockPsForBarbers = mock(PreparedStatement.class);
        ResultSet mockRsForBarbers = mock(ResultSet.class);
        lenient().when(mockConnForBarbers.prepareStatement(anyString())).thenReturn(mockPsForBarbers);
        lenient().when(mockPsForBarbers.executeQuery()).thenReturn(mockRsForBarbers);
        lenient().when(mockRsForBarbers.next()).thenReturn(true, false);
        lenient().when(mockRsForBarbers.getInt("count")).thenReturn(10);
        lenient().doNothing().when(mockConnForBarbers).close();

        // Mock for getTodayAppointments (succeeds)
        Connection mockConnForAppointments = mock(Connection.class);
        PreparedStatement mockPsForAppointments = mock(PreparedStatement.class);
        ResultSet mockRsForAppointments = mock(ResultSet.class);
        lenient().when(mockConnForAppointments.prepareStatement(anyString())).thenReturn(mockPsForAppointments);
        lenient().doNothing().when(mockPsForAppointments).setDate(eq(1), any(java.sql.Date.class));
        lenient().when(mockPsForAppointments.executeQuery()).thenReturn(mockRsForAppointments);
        lenient().when(mockRsForAppointments.next()).thenReturn(true, false);
        lenient().when(mockRsForAppointments.getInt("count")).thenReturn(5);
        lenient().doNothing().when(mockConnForAppointments).close();

        // Setup sequential calls for DatabaseUtil::getConnection
        mockedDatabaseUtil.when(DatabaseUtil::getConnection)
                .thenReturn(mockConnForBarbers) // For getTotalBarbers (succeeds)
                .thenReturn(mockConnForAppointments) // For getTodayAppointments (succeeds)
                .thenThrow(new SQLException("DB error for clients")); // For getTotalClients (fails)

        servlet.doGet(request, response);

        verify(request).setAttribute(eq("totalBarbers"), eq(10));
        verify(request).setAttribute(eq("todayAppointments"), eq(5));
        verify(request).setAttribute(eq("totalClients"), eq(0)); // Clients should be 0 due to error
        verify(request).setAttribute(eq("totalServices"), eq(0)); // Subsequent calls will also fail due to outer catch
        verify(dispatcher).forward(request, response);
        verify(response, never()).sendRedirect(anyString());
        assertTrue(errContent.toString().contains("Error getting total clients: DB error for clients"));
    }

    @Test
    void testDoGet_getTotalServicesThrowsException_attributesSetToZero() throws ServletException, IOException, SQLException {
        when(session.getAttribute("userType")).thenReturn("admin");

        // Mock for getTotalBarbers (succeeds)
        Connection mockConnForBarbers = mock(Connection.class);
        PreparedStatement mockPsForBarbers = mock(PreparedStatement.class);
        ResultSet mockRsForBarbers = mock(ResultSet.class);
        lenient().when(mockConnForBarbers.prepareStatement(anyString())).thenReturn(mockPsForBarbers);
        lenient().when(mockPsForBarbers.executeQuery()).thenReturn(mockRsForBarbers);
        lenient().when(mockRsForBarbers.next()).thenReturn(true, false);
        lenient().when(mockRsForBarbers.getInt("count")).thenReturn(10);
        lenient().doNothing().when(mockConnForBarbers).close();

        // Mock for getTodayAppointments (succeeds)
        Connection mockConnForAppointments = mock(Connection.class);
        PreparedStatement mockPsForAppointments = mock(PreparedStatement.class);
        ResultSet mockRsForAppointments = mock(ResultSet.class);
        lenient().when(mockConnForAppointments.prepareStatement(anyString())).thenReturn(mockPsForAppointments);
        lenient().doNothing().when(mockPsForAppointments).setDate(eq(1), any(java.sql.Date.class));
        lenient().when(mockPsForAppointments.executeQuery()).thenReturn(mockRsForAppointments);
        lenient().when(mockRsForAppointments.next()).thenReturn(true, false);
        lenient().when(mockRsForAppointments.getInt("count")).thenReturn(5);
        lenient().doNothing().when(mockConnForAppointments).close();

        // Mock for getTotalClients (succeeds)
        Connection mockConnForClients = mock(Connection.class);
        PreparedStatement mockPsForClients = mock(PreparedStatement.class);
        ResultSet mockRsForClients = mock(ResultSet.class);
        lenient().when(mockConnForClients.prepareStatement(anyString())).thenReturn(mockPsForClients);
        lenient().when(mockPsForClients.executeQuery()).thenReturn(mockRsForClients);
        lenient().when(mockRsForClients.next()).thenReturn(true, false);
        lenient().when(mockRsForClients.getInt("count")).thenReturn(100);
        lenient().doNothing().when(mockConnForClients).close();

        // Setup sequential calls for DatabaseUtil::getConnection
        mockedDatabaseUtil.when(DatabaseUtil::getConnection)
                .thenReturn(mockConnForBarbers) // For getTotalBarbers (succeeds)
                .thenReturn(mockConnForAppointments) // For getTodayAppointments (succeeds)
                .thenReturn(mockConnForClients) // For getTotalClients (succeeds)
                .thenThrow(new SQLException("DB error for services")); // For getTotalServices (fails)

        servlet.doGet(request, response);

        verify(request).setAttribute(eq("totalBarbers"), eq(10));
        verify(request).setAttribute(eq("todayAppointments"), eq(5));
        verify(request).setAttribute(eq("totalClients"), eq(100));
        verify(request).setAttribute(eq("totalServices"), eq(0)); // Services should be 0 due to error
        verify(dispatcher).forward(request, response);
        verify(response, never()).sendRedirect(anyString());
        assertTrue(errContent.toString().contains("Error getting total services: DB error for services"));
    }

    @Test
    void testDoGet_noStatsReturnedFromDB_attributesSetToZero() throws ServletException, IOException, SQLException {
        when(session.getAttribute("userType")).thenReturn("admin");

        // Use individual mocks to ensure each getConnection call returns a working connection but with empty results
        Connection mockConnectionBarbers = mock(Connection.class);
        PreparedStatement mockPreparedStatementBarbers = mock(PreparedStatement.class);
        ResultSet mockResultSetBarbers = mock(ResultSet.class);
        lenient().when(mockConnectionBarbers.prepareStatement(anyString())).thenReturn(mockPreparedStatementBarbers);
        lenient().when(mockPreparedStatementBarbers.executeQuery()).thenReturn(mockResultSetBarbers);
        lenient().when(mockResultSetBarbers.next()).thenReturn(false); // No rows
        lenient().doNothing().when(mockConnectionBarbers).close();

        Connection mockConnectionAppointments = mock(Connection.class);
        PreparedStatement mockPreparedStatementAppointments = mock(PreparedStatement.class);
        ResultSet mockResultSetAppointments = mock(ResultSet.class);
        lenient().when(mockConnectionAppointments.prepareStatement(anyString())).thenReturn(mockPreparedStatementAppointments);
        lenient().doNothing().when(mockPreparedStatementAppointments).setDate(eq(1), any(java.sql.Date.class));
        lenient().when(mockPreparedStatementAppointments.executeQuery()).thenReturn(mockResultSetAppointments);
        lenient().when(mockResultSetAppointments.next()).thenReturn(false); // No rows
        lenient().doNothing().when(mockConnectionAppointments).close();

        Connection mockConnectionClients = mock(Connection.class);
        PreparedStatement mockPreparedStatementClients = mock(PreparedStatement.class);
        ResultSet mockResultSetClients = mock(ResultSet.class);
        lenient().when(mockConnectionClients.prepareStatement(anyString())).thenReturn(mockPreparedStatementClients);
        lenient().when(mockPreparedStatementClients.executeQuery()).thenReturn(mockResultSetClients);
        lenient().when(mockResultSetClients.next()).thenReturn(false); // No rows
        lenient().doNothing().when(mockConnectionClients).close();

        Connection mockConnectionServices = mock(Connection.class);
        PreparedStatement mockPreparedStatementServices = mock(PreparedStatement.class);
        ResultSet mockResultSetServices = mock(ResultSet.class);
        lenient().when(mockConnectionServices.prepareStatement(anyString())).thenReturn(mockPreparedStatementServices);
        lenient().when(mockPreparedStatementServices.executeQuery()).thenReturn(mockResultSetServices);
        lenient().when(mockResultSetServices.next()).thenReturn(false); // No rows
        lenient().doNothing().when(mockConnectionServices).close();

        mockedDatabaseUtil.when(DatabaseUtil::getConnection)
                .thenReturn(mockConnectionBarbers)
                .thenReturn(mockConnectionAppointments)
                .thenReturn(mockConnectionClients)
                .thenReturn(mockConnectionServices);

        servlet.doGet(request, response);

        verify(request).setAttribute(eq("totalBarbers"), eq(0));
        verify(request).setAttribute(eq("todayAppointments"), eq(0));
        verify(request).setAttribute(eq("totalClients"), eq(0));
        verify(request).setAttribute(eq("totalServices"), eq(0));
        verify(dispatcher).forward(request, response);
        verify(response, never()).sendRedirect(anyString());
        assertTrue(errContent.toString().isEmpty(), "No error should be printed to System.err");
    }
}