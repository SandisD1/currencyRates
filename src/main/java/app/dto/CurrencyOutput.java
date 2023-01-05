package app.dto;

import java.util.Objects;

public class CurrencyOutput {

    private final String entryDate;
    private final String currencyCode;
    private final String currencyRate;

    public CurrencyOutput(String entryDate, String currencyCode, String currencyRate) {
        this.entryDate = entryDate;
        this.currencyCode = currencyCode;
        this.currencyRate = currencyRate;
    }

    public String getEntryDate() {
        return entryDate;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public String getCurrencyRate() {
        return currencyRate;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CurrencyOutput that = (CurrencyOutput) o;
        return Objects.equals(entryDate, that.entryDate) && Objects.equals(currencyCode, that.currencyCode) && Objects.equals(currencyRate, that.currencyRate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(entryDate, currencyCode, currencyRate);
    }
}
