package app;

import app.dto.CurrencyEntry;
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
import java.sql.Timestamp;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CurrencyValuesInput {

    public static List<CurrencyEntry> readCurrencyValues(String inputApiUrl) {
        List<CurrencyEntry> parsedEntries = new ArrayList<>();

        NodeList nodeList;

        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(new URL(inputApiUrl).openStream());
            nodeList = doc.getElementsByTagName("item");
        } catch (ParserConfigurationException | IOException | SAXException e) {
            throw new RuntimeException(e);
        }

        for (int i = 0; i < nodeList.getLength(); i++) {

            Node node = nodeList.item(i);

            if (node.getNodeType() == Node.ELEMENT_NODE) {

                Element element = (Element) node;

                String description = element.getElementsByTagName("description").item(0).getTextContent();
                String pubDate = element.getElementsByTagName("pubDate").item(0).getTextContent();

                Pattern p = Pattern.compile("[A-Z]{3}\\s[0-9]*\\.[0-9]{8}");
                Matcher m = p.matcher(description);
                ZonedDateTime postDate = ZonedDateTime.parse(pubDate, DateTimeFormatter.RFC_1123_DATE_TIME);
                ZoneId zone = postDate.getZone();
                String zoneId = zone.toString();

                Timestamp ts = Timestamp.valueOf(postDate.toLocalDateTime());

                while (m.find()) {
                    String[] entry = m.group().split(" ");

                    CurrencyEntry currencyEntry = new CurrencyEntry(ts, entry[0], entry[1], zoneId);

                    parsedEntries.add(currencyEntry);

                }
            }
        }
        return parsedEntries;
    }
}
