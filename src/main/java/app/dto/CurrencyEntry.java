package app.dto;

import java.sql.Timestamp;
import java.util.Objects;

public class CurrencyEntry {

    private final Timestamp entryDate;
    private final String currencyCode;
    private final String currencyRate;
    private final String timezone;

    public CurrencyEntry(Timestamp entryDate, String currencyCode, String currencyRate, String timezone) {
        this.entryDate = entryDate;
        this.currencyCode = currencyCode;
        this.currencyRate = currencyRate;
        this.timezone = timezone;
    }

    public Timestamp getEntryDate() {
        return entryDate;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public String getCurrencyRate() {
        return currencyRate;
    }

    public String getTimezone() {
        return timezone;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CurrencyEntry that = (CurrencyEntry) o;
        return Objects.equals(entryDate, that.entryDate) && Objects.equals(currencyCode, that.currencyCode) && Objects.equals(currencyRate, that.currencyRate) && Objects.equals(timezone, that.timezone);
    }

    @Override
    public int hashCode() {
        return Objects.hash(entryDate, currencyCode, currencyRate, timezone);
    }
}
