package dao;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import model.Offer;
import model.OfferRedemption;
import util.DatabaseUtil;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OfferDAOTest {

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

    private OfferDAO offerDAO;

    @BeforeEach
    void setUp() {
        offerDAO = new OfferDAO();
    }

    private void mockOfferResultSet() throws SQLException {
        when(resultSet.getInt("offer_id")).thenReturn(1);
        when(resultSet.getString("title")).thenReturn("Remise 10%");
        when(resultSet.getString("description")).thenReturn("10% de réduction");
        when(resultSet.getInt("points_required")).thenReturn(100);
        when(resultSet.getBoolean("is_active")).thenReturn(true);
        when(resultSet.getTimestamp("created_at")).thenReturn(Timestamp.valueOf(LocalDateTime.now()));
    }

    @Test
    void testFindById_success() throws SQLException {
        try (MockedStatic<DatabaseUtil> dbUtil = mockStatic(DatabaseUtil.class)) {
            dbUtil.when(DatabaseUtil::getConnection).thenReturn(connection);
            when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
            when(preparedStatement.executeQuery()).thenReturn(resultSet);
            when(resultSet.next()).thenReturn(true);
            mockOfferResultSet();

            Offer result = offerDAO.findById(1);

            assertNotNull(result);
            assertEquals(1, result.getOfferId());
            assertEquals("Remise 10%", result.getTitle());
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

            Offer result = offerDAO.findById(99);

            assertNull(result);
        }
    }

    @Test
    void testFindById_sqlException() throws SQLException {
        try (MockedStatic<DatabaseUtil> dbUtil = mockStatic(DatabaseUtil.class)) {
            dbUtil.when(DatabaseUtil::getConnection).thenReturn(connection);
            when(connection.prepareStatement(anyString())).thenThrow(new SQLException("Error"));

            Offer result = offerDAO.findById(1);

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
            mockOfferResultSet();

            List<Offer> result = offerDAO.findAll();

            assertNotNull(result);
            assertEquals(2, result.size());
        }
    }

    @Test
    void testFindAll_sqlException() throws SQLException {
        try (MockedStatic<DatabaseUtil> dbUtil = mockStatic(DatabaseUtil.class)) {
            dbUtil.when(DatabaseUtil::getConnection).thenReturn(connection);
            when(connection.createStatement()).thenThrow(new SQLException("Error"));

            List<Offer> result = offerDAO.findAll();

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
            mockOfferResultSet();

            List<Offer> result = offerDAO.findAllActive();

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

            List<Offer> result = offerDAO.findAllActive();

            assertNotNull(result);
            assertTrue(result.isEmpty());
        }
    }

    @Test
    void testFindAffordableOffers_success() throws SQLException {
        try (MockedStatic<DatabaseUtil> dbUtil = mockStatic(DatabaseUtil.class)) {
            dbUtil.when(DatabaseUtil::getConnection).thenReturn(connection);
            when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
            when(preparedStatement.executeQuery()).thenReturn(resultSet);
            when(resultSet.next()).thenReturn(true, false);
            mockOfferResultSet();

            List<Offer> result = offerDAO.findAffordableOffers(150);

            assertNotNull(result);
            assertEquals(1, result.size());
            verify(preparedStatement).setInt(1, 150);
        }
    }

    @Test
    void testFindAffordableOffers_sqlException() throws SQLException {
        try (MockedStatic<DatabaseUtil> dbUtil = mockStatic(DatabaseUtil.class)) {
            dbUtil.when(DatabaseUtil::getConnection).thenReturn(connection);
            when(connection.prepareStatement(anyString())).thenThrow(new SQLException("Error"));

            List<Offer> result = offerDAO.findAffordableOffers(150);

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

            Offer offer = new Offer();
            offer.setTitle("Remise 10%");
            offer.setDescription("10% de réduction");
            offer.setPointsRequired(100);
            offer.setActive(true);

            boolean result = offerDAO.create(offer);

            assertTrue(result);
            assertEquals(5, offer.getOfferId());
            verify(preparedStatement).setString(1, "Remise 10%");
            verify(preparedStatement).setString(2, "10% de réduction");
            verify(preparedStatement).setInt(3, 100);
            verify(preparedStatement).setBoolean(4, true);
        }
    }

    @Test
    void testCreate_sqlException() throws SQLException {
        try (MockedStatic<DatabaseUtil> dbUtil = mockStatic(DatabaseUtil.class)) {
            dbUtil.when(DatabaseUtil::getConnection).thenReturn(connection);
            when(connection.prepareStatement(anyString(), eq(Statement.RETURN_GENERATED_KEYS))).thenThrow(new SQLException("Error"));

            Offer offer = new Offer();
            offer.setTitle("Remise 10%");

            boolean result = offerDAO.create(offer);

            assertFalse(result);
        }
    }

    @Test
    void testUpdate_success() throws SQLException {
        try (MockedStatic<DatabaseUtil> dbUtil = mockStatic(DatabaseUtil.class)) {
            dbUtil.when(DatabaseUtil::getConnection).thenReturn(connection);
            when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
            when(preparedStatement.executeUpdate()).thenReturn(1);

            Offer offer = new Offer();
            offer.setOfferId(1);
            offer.setTitle("Remise 15%");
            offer.setDescription("15% de réduction");
            offer.setPointsRequired(150);
            offer.setActive(true);

            boolean result = offerDAO.update(offer);

            assertTrue(result);
            verify(preparedStatement).setString(1, "Remise 15%");
            verify(preparedStatement).setInt(5, 1);
        }
    }

    @Test
    void testUpdate_sqlException() throws SQLException {
        try (MockedStatic<DatabaseUtil> dbUtil = mockStatic(DatabaseUtil.class)) {
            dbUtil.when(DatabaseUtil::getConnection).thenReturn(connection);
            when(connection.prepareStatement(anyString())).thenThrow(new SQLException("Error"));

            Offer offer = new Offer();
            offer.setOfferId(1);

            boolean result = offerDAO.update(offer);

            assertFalse(result);
        }
    }

    @Test
    void testDelete_success() throws SQLException {
        try (MockedStatic<DatabaseUtil> dbUtil = mockStatic(DatabaseUtil.class)) {
            dbUtil.when(DatabaseUtil::getConnection).thenReturn(connection);
            when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
            when(preparedStatement.executeUpdate()).thenReturn(1);

            boolean result = offerDAO.delete(1);

            assertTrue(result);
            verify(preparedStatement).setInt(1, 1);
        }
    }

    @Test
    void testDelete_sqlException() throws SQLException {
        try (MockedStatic<DatabaseUtil> dbUtil = mockStatic(DatabaseUtil.class)) {
            dbUtil.when(DatabaseUtil::getConnection).thenReturn(connection);
            when(connection.prepareStatement(anyString())).thenThrow(new SQLException("Error"));

            boolean result = offerDAO.delete(1);

            assertFalse(result);
        }
    }

    @Test
    void testToggleActive_success() throws SQLException {
        try (MockedStatic<DatabaseUtil> dbUtil = mockStatic(DatabaseUtil.class)) {
            dbUtil.when(DatabaseUtil::getConnection).thenReturn(connection);
            when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
            when(preparedStatement.executeUpdate()).thenReturn(1);

            boolean result = offerDAO.toggleActive(1);

            assertTrue(result);
            verify(preparedStatement).setInt(1, 1);
        }
    }

    @Test
    void testToggleActive_sqlException() throws SQLException {
        try (MockedStatic<DatabaseUtil> dbUtil = mockStatic(DatabaseUtil.class)) {
            dbUtil.when(DatabaseUtil::getConnection).thenReturn(connection);
            when(connection.prepareStatement(anyString())).thenThrow(new SQLException("Error"));

            boolean result = offerDAO.toggleActive(1);

            assertFalse(result);
        }
    }

    @Test
    void testRedeemOffer_success() throws SQLException {
        try (MockedStatic<DatabaseUtil> dbUtil = mockStatic(DatabaseUtil.class)) {
            dbUtil.when(DatabaseUtil::getConnection).thenReturn(connection);

            PreparedStatement stmt1 = mock(PreparedStatement.class);
            PreparedStatement stmt2 = mock(PreparedStatement.class);
            PreparedStatement stmt3 = mock(PreparedStatement.class);
            ResultSet rs = mock(ResultSet.class);

            when(connection.prepareStatement(contains("SELECT points_required"))).thenReturn(stmt1);
            when(connection.prepareStatement(contains("UPDATE Client"))).thenReturn(stmt2);
            when(connection.prepareStatement(contains("INSERT INTO OfferRedemption"))).thenReturn(stmt3);

            when(stmt1.executeQuery()).thenReturn(rs);
            when(rs.next()).thenReturn(true);
            when(rs.getInt("points_required")).thenReturn(100);

            when(stmt2.executeUpdate()).thenReturn(1);
            when(stmt3.executeUpdate()).thenReturn(1);

            boolean result = offerDAO.redeemOffer(1, 1);

            assertTrue(result);
            verify(connection).setAutoCommit(false);
            verify(connection).commit();
        }
    }

    @Test
    void testRedeemOffer_offerNotFound() throws SQLException {
        try (MockedStatic<DatabaseUtil> dbUtil = mockStatic(DatabaseUtil.class)) {
            dbUtil.when(DatabaseUtil::getConnection).thenReturn(connection);

            PreparedStatement stmt1 = mock(PreparedStatement.class);
            ResultSet rs = mock(ResultSet.class);

            when(connection.prepareStatement(contains("SELECT points_required"))).thenReturn(stmt1);
            when(stmt1.executeQuery()).thenReturn(rs);
            when(rs.next()).thenReturn(false);

            boolean result = offerDAO.redeemOffer(1, 99);

            assertFalse(result);
            verify(connection).rollback();
        }
    }

    @Test
    void testRedeemOffer_insufficientPoints() throws SQLException {
        try (MockedStatic<DatabaseUtil> dbUtil = mockStatic(DatabaseUtil.class)) {
            dbUtil.when(DatabaseUtil::getConnection).thenReturn(connection);

            PreparedStatement stmt1 = mock(PreparedStatement.class);
            PreparedStatement stmt2 = mock(PreparedStatement.class);
            ResultSet rs = mock(ResultSet.class);

            when(connection.prepareStatement(contains("SELECT points_required"))).thenReturn(stmt1);
            when(connection.prepareStatement(contains("UPDATE Client"))).thenReturn(stmt2);

            when(stmt1.executeQuery()).thenReturn(rs);
            when(rs.next()).thenReturn(true);
            when(rs.getInt("points_required")).thenReturn(100);

            when(stmt2.executeUpdate()).thenReturn(0);

            boolean result = offerDAO.redeemOffer(1, 1);

            assertFalse(result);
            verify(connection).rollback();
        }
    }

    @Test
    void testRedeemOffer_sqlException() throws SQLException {
        try (MockedStatic<DatabaseUtil> dbUtil = mockStatic(DatabaseUtil.class)) {
            dbUtil.when(DatabaseUtil::getConnection).thenReturn(connection);
            when(connection.prepareStatement(anyString())).thenThrow(new SQLException("Error"));

            boolean result = offerDAO.redeemOffer(1, 1);

            assertFalse(result);
        }
    }

    @Test
    void testFindRedeemedOffersByClient_success() throws SQLException {
        try (MockedStatic<DatabaseUtil> dbUtil = mockStatic(DatabaseUtil.class)) {
            dbUtil.when(DatabaseUtil::getConnection).thenReturn(connection);
            when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
            when(preparedStatement.executeQuery()).thenReturn(resultSet);
            when(resultSet.next()).thenReturn(true, false);
            when(resultSet.getInt("redemption_id")).thenReturn(1);
            when(resultSet.getInt("client_id")).thenReturn(1);
            when(resultSet.getInt("offer_id")).thenReturn(1);
            when(resultSet.getBoolean("is_used")).thenReturn(false);
            when(resultSet.getInt("appointment_id")).thenReturn(0);
            when(resultSet.wasNull()).thenReturn(true);
            when(resultSet.getTimestamp("redeemed_at")).thenReturn(Timestamp.valueOf(LocalDateTime.now()));
            when(resultSet.getTimestamp("used_at")).thenReturn(null);
            when(resultSet.getString("title")).thenReturn("Remise 10%");
            when(resultSet.getString("description")).thenReturn("10% de réduction");
            when(resultSet.getInt("points_required")).thenReturn(100);

            List<OfferRedemption> result = offerDAO.findRedeemedOffersByClient(1);

            assertNotNull(result);
            assertEquals(1, result.size());
            verify(preparedStatement).setInt(1, 1);
        }
    }

    @Test
    void testFindRedeemedOffersByClient_sqlException() throws SQLException {
        try (MockedStatic<DatabaseUtil> dbUtil = mockStatic(DatabaseUtil.class)) {
            dbUtil.when(DatabaseUtil::getConnection).thenReturn(connection);
            when(connection.prepareStatement(anyString())).thenThrow(new SQLException("Error"));

            List<OfferRedemption> result = offerDAO.findRedeemedOffersByClient(1);

            assertNotNull(result);
            assertTrue(result.isEmpty());
        }
    }

    @Test
    void testFindUnusedRedeemedOffersByClient_success() throws SQLException {
        try (MockedStatic<DatabaseUtil> dbUtil = mockStatic(DatabaseUtil.class)) {
            dbUtil.when(DatabaseUtil::getConnection).thenReturn(connection);
            when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
            when(preparedStatement.executeQuery()).thenReturn(resultSet);
            when(resultSet.next()).thenReturn(true, false);
            when(resultSet.getInt("redemption_id")).thenReturn(1);
            when(resultSet.getInt("client_id")).thenReturn(1);
            when(resultSet.getInt("offer_id")).thenReturn(1);
            when(resultSet.getBoolean("is_used")).thenReturn(false);
            when(resultSet.getInt("appointment_id")).thenReturn(0);
            when(resultSet.wasNull()).thenReturn(true);
            when(resultSet.getTimestamp("redeemed_at")).thenReturn(Timestamp.valueOf(LocalDateTime.now()));
            when(resultSet.getTimestamp("used_at")).thenReturn(null);
            when(resultSet.getString("title")).thenReturn("Remise 10%");
            when(resultSet.getString("description")).thenReturn("10% de réduction");
            when(resultSet.getInt("points_required")).thenReturn(100);

            List<OfferRedemption> result = offerDAO.findUnusedRedeemedOffersByClient(1);

            assertNotNull(result);
            assertEquals(1, result.size());
            assertFalse(result.get(0).isUsed());
            verify(preparedStatement).setInt(1, 1);
        }
    }

    @Test
    void testMarkRedemptionAsUsed_success() throws SQLException {
        try (MockedStatic<DatabaseUtil> dbUtil = mockStatic(DatabaseUtil.class)) {
            dbUtil.when(DatabaseUtil::getConnection).thenReturn(connection);
            when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
            when(preparedStatement.executeUpdate()).thenReturn(1);

            boolean result = offerDAO.markRedemptionAsUsed(1, 10);

            assertTrue(result);
            verify(preparedStatement).setInt(1, 10);
            verify(preparedStatement).setInt(2, 1);
        }
    }

    @Test
    void testMarkRedemptionAsUsed_sqlException() throws SQLException {
        try (MockedStatic<DatabaseUtil> dbUtil = mockStatic(DatabaseUtil.class)) {
            dbUtil.when(DatabaseUtil::getConnection).thenReturn(connection);
            when(connection.prepareStatement(anyString())).thenThrow(new SQLException("Error"));

            boolean result = offerDAO.markRedemptionAsUsed(1, 10);

            assertFalse(result);
        }
    }

    @Test
    void testFindRedemptionByAppointmentId_success() throws SQLException {
        try (MockedStatic<DatabaseUtil> dbUtil = mockStatic(DatabaseUtil.class)) {
            dbUtil.when(DatabaseUtil::getConnection).thenReturn(connection);
            when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
            when(preparedStatement.executeQuery()).thenReturn(resultSet);
            when(resultSet.next()).thenReturn(true);
            when(resultSet.getInt("redemption_id")).thenReturn(1);
            when(resultSet.getInt("client_id")).thenReturn(1);
            when(resultSet.getInt("offer_id")).thenReturn(1);
            when(resultSet.getBoolean("is_used")).thenReturn(true);
            when(resultSet.getInt("appointment_id")).thenReturn(10);
            when(resultSet.wasNull()).thenReturn(false);
            when(resultSet.getTimestamp("redeemed_at")).thenReturn(Timestamp.valueOf(LocalDateTime.now()));
            when(resultSet.getTimestamp("used_at")).thenReturn(Timestamp.valueOf(LocalDateTime.now()));
            when(resultSet.getString("title")).thenReturn("Remise 10%");
            when(resultSet.getString("description")).thenReturn("10% de réduction");
            when(resultSet.getInt("points_required")).thenReturn(100);

            OfferRedemption result = offerDAO.findRedemptionByAppointmentId(10);

            assertNotNull(result);
            assertEquals(1, result.getRedemptionId());
            assertEquals(10, result.getAppointmentId());
            verify(preparedStatement).setInt(1, 10);
        }
    }

    @Test
    void testFindRedemptionByAppointmentId_notFound() throws SQLException {
        try (MockedStatic<DatabaseUtil> dbUtil = mockStatic(DatabaseUtil.class)) {
            dbUtil.when(DatabaseUtil::getConnection).thenReturn(connection);
            when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
            when(preparedStatement.executeQuery()).thenReturn(resultSet);
            when(resultSet.next()).thenReturn(false);

            OfferRedemption result = offerDAO.findRedemptionByAppointmentId(99);

            assertNull(result);
        }
    }

    @Test
    void testFindRedemptionByAppointmentId_sqlException() throws SQLException {
        try (MockedStatic<DatabaseUtil> dbUtil = mockStatic(DatabaseUtil.class)) {
            dbUtil.when(DatabaseUtil::getConnection).thenReturn(connection);
            when(connection.prepareStatement(anyString())).thenThrow(new SQLException("Error"));

            OfferRedemption result = offerDAO.findRedemptionByAppointmentId(10);

            assertNull(result);
        }
    }
}
