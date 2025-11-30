package util;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseMigration {

    public static void main(String[] args) {
        try {
            runMigration();
        } catch (Exception e) {
            System.err.println("\n❌ Migration failed: " + e.getMessage());
            e.printStackTrace();
        }
    }
    public static void runMigration() throws SQLException {
        Connection conn = null;
        Statement stmt = null;

        try {
            conn = DatabaseUtil.getConnection();
            stmt = conn.createStatement();

            String createTableSQL = "CREATE TABLE IF NOT EXISTS OfferRedemption (" +
                    "redemption_id INT AUTO_INCREMENT PRIMARY KEY, " +
                    "client_id INT NOT NULL, " +
                    "offer_id INT NOT NULL, " +
                    "is_used BOOLEAN DEFAULT FALSE, " +
                    "appointment_id INT NULL, " +
                    "redeemed_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP, " +
                    "used_at TIMESTAMP NULL, " +
                    "FOREIGN KEY (client_id) REFERENCES Client(client_id) ON DELETE CASCADE, " +
                    "FOREIGN KEY (offer_id) REFERENCES Offer(offer_id) ON DELETE CASCADE" +
                    ")";
            stmt.execute(createTableSQL);
            try {
                String alterTableSQL = "ALTER TABLE Appointment ADD COLUMN redemption_id INT NULL";
                stmt.execute(alterTableSQL);
            } catch (SQLException e) {
                if (e.getMessage().contains("Duplicate column name")) {
                } else {
                    throw e;
                }
            }
            try {
                String alterTableSQL2 = "ALTER TABLE Appointment ADD COLUMN final_price DECIMAL(10,2) NULL";
                stmt.execute(alterTableSQL2);
            } catch (SQLException e) {
                if (e.getMessage().contains("Duplicate column name")) {
                } else {
                    throw e;
                }
            }
            try {
                String fkSQL = "ALTER TABLE Appointment " +
                        "ADD CONSTRAINT fk_appointment_redemption " +
                        "FOREIGN KEY (redemption_id) REFERENCES OfferRedemption(redemption_id) " +
                        "ON DELETE SET NULL";
                stmt.execute(fkSQL);
            } catch (SQLException e) {
                if (e.getMessage().contains("Duplicate") || e.getMessage().contains("already exists")) {
                } else {
                }
            }
            try {
                String updateFkSQL = "ALTER TABLE OfferRedemption " +
                        "ADD CONSTRAINT fk_redemption_appointment " +
                        "FOREIGN KEY (appointment_id) REFERENCES Appointment(appointment_id) " +
                        "ON DELETE SET NULL";
                stmt.execute(updateFkSQL);
            } catch (SQLException e) {
                if (e.getMessage().contains("Duplicate") || e.getMessage().contains("already exists")) {
                } else {
                }
            }
            try {
                stmt.execute("CREATE INDEX idx_redemption_client ON OfferRedemption(client_id, is_used)");
            } catch (SQLException e) {
                if (e.getMessage().contains("Duplicate") || e.getMessage().contains("already exists")) {
                } else {
                }
            }
            try {
                stmt.execute("CREATE INDEX idx_appointment_redemption ON Appointment(redemption_id)");
            } catch (SQLException e) {
                if (e.getMessage().contains("Duplicate") || e.getMessage().contains("already exists")) {
                } else {
                }
            }
        } finally {
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (conn != null) {
                DatabaseUtil.closeConnection(conn);
            }
        }
    }
}
