package app;

import io.javalin.Javalin;

public class Main {
    public static void main(String[] args) {
        if (args.length > 0) {
            if (args[0].equals("update")) {
                UpdateCurrencyValuesInDb.update();
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