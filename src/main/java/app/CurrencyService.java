package app;

import app.dto.CurrencyOutput;

import java.util.List;

public class CurrencyService {
    private final CurrencyRepository currencyRepository;

    public CurrencyService() {
        this.currencyRepository = new CurrencyRepository();
    }

    public List<CurrencyOutput> getLatestRates() {

        return this.currencyRepository.getLatestRates();
    }

    public List<CurrencyOutput> getRatesForRequestedCurrency(String currencyCode) {

        return this.currencyRepository.getRatesForRequestedCurrency(currencyCode.toUpperCase());
    }

}
