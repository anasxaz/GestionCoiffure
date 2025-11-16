package util;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseMigration {

    public static void main(String[] args) {
        System.out.println("Starting database migration...");

        try {
            runMigration();
            System.out.println("\n✅ Migration completed successfully!");
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

            System.out.println("\n1. Creating OfferRedemption table...");
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
            System.out.println("   ✓ OfferRedemption table created/verified");

            System.out.println("\n2. Adding redemption_id column to Appointment table...");
            try {
                String alterTableSQL = "ALTER TABLE Appointment ADD COLUMN redemption_id INT NULL";
                stmt.execute(alterTableSQL);
                System.out.println("   ✓ Column redemption_id added to Appointment table");
            } catch (SQLException e) {
                if (e.getMessage().contains("Duplicate column name")) {
                    System.out.println("   ℹ Column redemption_id already exists, skipping");
                } else {
                    throw e;
                }
            }

            System.out.println("\n2b. Adding final_price column to Appointment table...");
            try {
                String alterTableSQL2 = "ALTER TABLE Appointment ADD COLUMN final_price DECIMAL(10,2) NULL";
                stmt.execute(alterTableSQL2);
                System.out.println("   ✓ Column final_price added to Appointment table");
            } catch (SQLException e) {
                if (e.getMessage().contains("Duplicate column name")) {
                    System.out.println("   ℹ Column final_price already exists, skipping");
                } else {
                    throw e;
                }
            }

            System.out.println("\n3. Adding foreign key constraint...");
            try {
                String fkSQL = "ALTER TABLE Appointment " +
                        "ADD CONSTRAINT fk_appointment_redemption " +
                        "FOREIGN KEY (redemption_id) REFERENCES OfferRedemption(redemption_id) " +
                        "ON DELETE SET NULL";
                stmt.execute(fkSQL);
                System.out.println("   ✓ Foreign key constraint added");
            } catch (SQLException e) {
                if (e.getMessage().contains("Duplicate") || e.getMessage().contains("already exists")) {
                    System.out.println("   ℹ Foreign key constraint already exists, skipping");
                } else {
                    // This might fail if the FK can't be created due to circular dependency
                    // We'll handle it by updating the OfferRedemption table
                    System.out.println("   ⚠ Could not add FK constraint (might be due to circular dependency)");
                }
            }

            System.out.println("\n4. Updating OfferRedemption table to add FK to Appointment...");
            try {
                String updateFkSQL = "ALTER TABLE OfferRedemption " +
                        "ADD CONSTRAINT fk_redemption_appointment " +
                        "FOREIGN KEY (appointment_id) REFERENCES Appointment(appointment_id) " +
                        "ON DELETE SET NULL";
                stmt.execute(updateFkSQL);
                System.out.println("   ✓ Foreign key constraint added to OfferRedemption");
            } catch (SQLException e) {
                if (e.getMessage().contains("Duplicate") || e.getMessage().contains("already exists")) {
                    System.out.println("   ℹ Foreign key constraint already exists, skipping");
                } else {
                    System.out.println("   ⚠ Warning: " + e.getMessage());
                }
            }

            System.out.println("\n5. Creating indexes for better performance...");
            try {
                stmt.execute("CREATE INDEX idx_redemption_client ON OfferRedemption(client_id, is_used)");
                System.out.println("   ✓ Index idx_redemption_client created");
            } catch (SQLException e) {
                if (e.getMessage().contains("Duplicate") || e.getMessage().contains("already exists")) {
                    System.out.println("   ℹ Index idx_redemption_client already exists, skipping");
                } else {
                    System.out.println("   ⚠ Warning: " + e.getMessage());
                }
            }

            try {
                stmt.execute("CREATE INDEX idx_appointment_redemption ON Appointment(redemption_id)");
                System.out.println("   ✓ Index idx_appointment_redemption created");
            } catch (SQLException e) {
                if (e.getMessage().contains("Duplicate") || e.getMessage().contains("already exists")) {
                    System.out.println("   ℹ Index idx_appointment_redemption already exists, skipping");
                } else {
                    System.out.println("   ⚠ Warning: " + e.getMessage());
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
