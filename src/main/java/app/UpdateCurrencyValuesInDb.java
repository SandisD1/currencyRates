package app;

import app.dto.CurrencyEntry;
import app.util.DatabaseConnection;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UpdateCurrencyValuesInDb {
    public static void main(String[] args) throws ParserConfigurationException, IOException, SAXException, SQLException {

        Connection dbConnection = DatabaseConnection.getConnection();

        createCurrencyTableIfNotExists(dbConnection);

        String url = "https://www.bank.lv/vk/ecb_rss.xml";
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();
        Document doc = db.parse(new URL(url).openStream());

        NodeList list = doc.getElementsByTagName("item");

        for (int i = 0; i < list.getLength(); i++) {

            Node node = list.item(i);

            if (node.getNodeType() == Node.ELEMENT_NODE) {

                Element element = (Element) node;

                String description = element.getElementsByTagName("description").item(0).getTextContent();
                String pubDate = element.getElementsByTagName("pubDate").item(0).getTextContent();

                Pattern p = Pattern.compile("[A-Z]{3}\\s[0-9]*\\.[0-9]{8}");
                Matcher m = p.matcher(description);
                ZonedDateTime postDate = ZonedDateTime.parse(pubDate, DateTimeFormatter.RFC_1123_DATE_TIME);
                ZoneId zone = postDate.getZone();
                String zoneid = zone.toString();

                Timestamp ts = Timestamp.valueOf(postDate.toLocalDateTime());
                while (m.find()) {
                    String[] entry = m.group().split(" ");

                    CurrencyEntry currencyEntry = new CurrencyEntry(ts, entry[0], entry[1], zoneid);
                    insertRate(currencyEntry, dbConnection);
                }
            }
        }
        System.out.println("Currency Rates Updated");

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

    public static void insertRate(CurrencyEntry entry, Connection conn) {

        try {
            String sqlReq = "Select * from currency c where c.currency_code=? && c.entry_date=?";
            PreparedStatement pstmt = conn.prepareStatement(sqlReq);
            pstmt.setString(1, entry.getCurrencyCode());
            pstmt.setTimestamp(2, entry.getEntryDate());
            ResultSet rs = pstmt.executeQuery();

            if (!rs.next()) {
                Statement stmt = conn.createStatement();

                String sql2 = "INSERT INTO currency (entry_date,currency_code,currency_rate,timezone)" + "VALUES ('" + entry.getEntryDate() + "', '" + entry.getCurrencyCode() + "', '" + entry.getCurrencyRate() + "', '" + entry.getTimezone() + "');";

                stmt.execute(sql2);
                stmt.close();
                conn.commit();
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
