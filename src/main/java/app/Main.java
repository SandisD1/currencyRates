package app;

import io.javalin.Javalin;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.sql.SQLException;

public class Main {
    public static void main(String[] args) throws ParserConfigurationException, IOException, SAXException, SQLException {
        if (args.length > 0) {
            if (args[0].equals("update")) {
                UpdateCurrencyValuesInDb.main(new String[0]);
            }
            if (args[0].equals("runApp")) {
                var app = Javalin.create()
                        .get("/", ctx -> ctx.result("Blank Page"));

                new CurrencyController().mapEndpoints(app);

                app.start(7070);
            }
        }
    }

}