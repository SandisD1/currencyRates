package app;

import app.dto.CurrencyEntry;
import app.util.DatabaseConnection;

import java.sql.*;
import java.util.List;

public class UpdateCurrencyValuesInDb {
    public static void update() {

        try {
            Connection dbConnection = DatabaseConnection.getConnection();

            createCurrencyTableIfNotExists(dbConnection);

            String url = "https://www.bank.lv/vk/ecb_rss.xml";

            List<CurrencyEntry> currencyEntries = CurrencyValuesInput.readCurrencyValues(url);

            insertRates(currencyEntries, dbConnection);

            dbConnection.close();

            System.out.println("Currency Rates Updated");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void createCurrencyTableIfNotExists(Connection conn) {

        try {

            Statement stmt = conn.createStatement();

            String sql2 = "CREATE TABLE IF NOT EXISTS currency\n" +
                    "(\n" +
                    "    entry_id      serial PRIMARY KEY,\n" +
                    "    entry_date    timestamp NOT NULL,\n" +
                    "    currency_code text      NOT NULL,\n" +
                    "    currency_rate text      NOT NULL,\n" +
                    "    timezone      text      NOT NULL\n" +
                    ");";

            stmt.execute(sql2);
            stmt.close();
            conn.commit();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    public static boolean isDuplicateEntry(CurrencyEntry entry, Connection connection) {

        try {
            String sqlReq = "Select * from currency c where c.currency_code=? && c.entry_date=?";
            PreparedStatement pstmt = connection.prepareStatement(sqlReq);
            pstmt.setString(1, entry.getCurrencyCode());
            pstmt.setTimestamp(2, entry.getEntryDate());
            ResultSet rs = pstmt.executeQuery();

            boolean isDuplicate = rs.next();
            rs.close();
            return isDuplicate;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void insertRates(List<CurrencyEntry> entries, Connection connection) {

        try {
            for (CurrencyEntry entry : entries) {
                if (!isDuplicateEntry(entry, connection)) {

                    Statement stmt = connection.createStatement();

                    String sql = "INSERT INTO currency (entry_date,currency_code,currency_rate,timezone)" + "VALUES ('" + entry.getEntryDate() + "', '" + entry.getCurrencyCode() + "', '" + entry.getCurrencyRate() + "', '" + entry.getTimezone() + "');";

                    stmt.execute(sql);
                    stmt.close();
                    connection.commit();
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
