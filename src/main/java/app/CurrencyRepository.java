package app;

import app.dto.CurrencyOutput;
import app.util.DatabaseConnection;
import org.mariadb.jdbc.Connection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class CurrencyRepository {
    private Connection connection;

    public CurrencyRepository() {
        super();
    }

    public List<CurrencyOutput> getLatestRates() throws SQLException {
        List<CurrencyOutput> latestRates = new ArrayList<>();
        Connection session = (Connection) DatabaseConnection.getConnection();

        String sqlReq = "Select * from currency c where c.entry_date=(Select max(entry_date) from currency c)";
        PreparedStatement pstmt = session.prepareStatement(sqlReq);

        ResultSet resultSet = pstmt.executeQuery();

        while (resultSet.next()) {
            Timestamp date = resultSet.getTimestamp("entry_date");
            String code = resultSet.getString("currency_code");
            String rate = resultSet.getString("currency_rate");
            String timezone = resultSet.getString("timezone");
            ZoneId zoneId = ZoneId.ofOffset("GMT", ZoneOffset.of(timezone));
            ZonedDateTime entryTime = date.toLocalDateTime().atZone(zoneId);

            String formated = entryTime.format(DateTimeFormatter.RFC_1123_DATE_TIME);

            latestRates.add(new CurrencyOutput(formated, code, rate));
        }

        return latestRates;

    }

    public List<CurrencyOutput> getRatesForRequestedCurrency(String currencyCode) throws SQLException {
        List<CurrencyOutput> ratesForRequestedCurrency = new ArrayList<>();
        Connection session = (Connection) DatabaseConnection.getConnection();

        String sqlReq = "Select * from currency c where c.currency_code=?";
        PreparedStatement pstmt = session.prepareStatement(sqlReq);
        pstmt.setString(1, currencyCode);

        ResultSet resultSet = pstmt.executeQuery();

        while (resultSet.next()) {
            Timestamp date = resultSet.getTimestamp("entry_date");
            String code = resultSet.getString("currency_code");
            String rate = resultSet.getString("currency_rate");
            String timezone = resultSet.getString("timezone");

            ZoneId zoneId = ZoneId.ofOffset("GMT", ZoneOffset.of(timezone));
            ZonedDateTime entryTime = date.toLocalDateTime().atZone(zoneId);

            String formatted = entryTime.format(DateTimeFormatter.RFC_1123_DATE_TIME);

            ratesForRequestedCurrency.add(new CurrencyOutput(formatted, code, rate));
        }

        return ratesForRequestedCurrency;

    }


}
