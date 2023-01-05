package app;

import app.dto.CurrencyOutput;
import io.javalin.Javalin;
import io.javalin.http.Handler;

import java.util.List;

public class CurrencyController {
    private CurrencyService currencyService;

    public CurrencyController() {
        this.currencyService = new CurrencyService();
    }

    private final Handler getLatestRates = ctx -> {
        List<CurrencyOutput> latestEntries = currencyService.getLatestRates();

        ctx.json(latestEntries);
    };

    private final Handler getRatesForRequestedCurrency = ctx -> {
        String currencyCode = ctx.pathParam("cur");
        List<CurrencyOutput> entriesForRequested = currencyService.getRatesForRequestedCurrency(currencyCode);

        ctx.json(entriesForRequested);
    };

    public void mapEndpoints(Javalin app) {
        app.get("/latest", getLatestRates);
        app.get("/{cur}", getRatesForRequestedCurrency);
    }

}
