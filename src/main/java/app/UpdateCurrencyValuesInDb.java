package app;

import app.dto.CurrencyEntry;
import app.util.DatabaseConnection;

import java.sql.*;
import java.util.List;

public class UpdateCurrencyValuesInDb {

    public static void update() {

        String url = "https://www.bank.lv/vk/ecb_rss.xml";

        try (Connection connection = DatabaseConnection.getConnection()) {

            createCurrencyTableIfNotExists(connection);

            List<CurrencyEntry> currencyEntries = CurrencyValuesInput.readCurrencyValues(url);

            insertRates(currencyEntries, connection);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        System.out.println("Currency Rates Updated");
    }

    private static void createCurrencyTableIfNotExists(Connection connection) throws SQLException {

        Statement statement = connection.createStatement();

        String createTable = "CREATE TABLE IF NOT EXISTS currency\n" +
                "(\n" +
                "    entry_id      serial PRIMARY KEY,\n" +
                "    entry_date    timestamp NOT NULL,\n" +
                "    currency_code text      NOT NULL,\n" +
                "    currency_rate text      NOT NULL,\n" +
                "    timezone      text      NOT NULL\n" +
                ");";

        statement.execute(createTable);
        statement.close();

        connection.commit();

    }

    private static boolean isDuplicateEntry(CurrencyEntry entry, Connection connection) throws SQLException {

        String sqlQuery = "Select * from currency c where c.currency_code=? && c.entry_date=?";

        PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery);

        preparedStatement.setString(1, entry.getCurrencyCode());
        preparedStatement.setTimestamp(2, entry.getEntryDate());

        ResultSet resultSet = preparedStatement.executeQuery();
        preparedStatement.close();

        boolean isDuplicate = resultSet.next();

        resultSet.close();
        return isDuplicate;


    }

    private static void insertRates(List<CurrencyEntry> entries, Connection connection) throws SQLException {

        for (CurrencyEntry entry : entries) {
            if (!isDuplicateEntry(entry, connection)) {
                Statement insertStatement = connection.createStatement();

                String sql = "INSERT INTO currency (entry_date,currency_code,currency_rate,timezone)" +
                        "VALUES ('" + entry.getEntryDate() + "', '" + entry.getCurrencyCode() + "', '" +
                        entry.getCurrencyRate() + "', '" + entry.getTimezone() + "');";

                insertStatement.execute(sql);
                insertStatement.close();
                connection.commit();

            }
        }
    }
}
