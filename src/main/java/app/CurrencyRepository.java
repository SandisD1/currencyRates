package app;

import app.dto.CurrencyOutput;
import app.util.DatabaseConnection;

import java.sql.*;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class CurrencyRepository {

    public CurrencyRepository() {
        super();
    }

    public List<CurrencyOutput> getLatestRates() {

        List<CurrencyOutput> latestRates = new ArrayList<>();
        String sqlReq = "Select * from currency c where c.entry_date=(Select max(entry_date) from currency c)";

        try (Connection session = DatabaseConnection.getConnection();
             PreparedStatement queryStatement = session.prepareStatement(sqlReq)) {

            fillRetrievedRates(queryStatement, latestRates);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return latestRates;

    }

    public List<CurrencyOutput> getRatesForRequestedCurrency(String currencyCode) {
        List<CurrencyOutput> ratesForRequestedCurrency = new ArrayList<>();

        String sqlReq = "Select * from currency c where c.currency_code=?";

        try (Connection session = DatabaseConnection.getConnection();
             PreparedStatement queryStatement = session.prepareStatement(sqlReq)) {

            queryStatement.setString(1, currencyCode);

            fillRetrievedRates(queryStatement, ratesForRequestedCurrency);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return ratesForRequestedCurrency;

    }

    private void fillRetrievedRates(PreparedStatement preparedStatement, List<CurrencyOutput> outputContainer) {

        try (ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {

                Timestamp date = resultSet.getTimestamp("entry_date");
                String code = resultSet.getString("currency_code");
                String rate = resultSet.getString("currency_rate");
                String timezone = resultSet.getString("timezone");

                ZoneId zoneId = ZoneId.ofOffset("GMT", ZoneOffset.of(timezone));
                ZonedDateTime entryTime = date.toLocalDateTime().atZone(zoneId);

                String formatted = entryTime.format(DateTimeFormatter.RFC_1123_DATE_TIME);

                outputContainer.add(new CurrencyOutput(formatted, code, rate));

            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }
}
