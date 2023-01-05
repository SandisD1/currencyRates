package app;

import app.dto.CurrencyOutput;

import java.sql.SQLException;
import java.util.List;

public class CurrencyService {
    private final CurrencyRepository currencyRepository;

    public CurrencyService() {
        this.currencyRepository = new CurrencyRepository();
    }

    public List<CurrencyOutput> getLatestRates() throws SQLException {
        return this.currencyRepository.getLatestRates();
    }

    public List<CurrencyOutput> getRatesForRequestedCurrency(String currencyCode) throws SQLException {
        return this.currencyRepository.getRatesForRequestedCurrency(currencyCode.toUpperCase());
    }

}
