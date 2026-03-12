package lab.p2p.dto;

import java.math.BigDecimal;

public class SpreadDTO {

    private String fiatCurrency;

    private BigDecimal monoPrice;
    private BigDecimal abankPrice;

    private BigDecimal spread;
    private Double spreadPercent;

    public String getFiatCurrency() {
        return fiatCurrency;
    }

    public void setFiatCurrency(String fiatCurrency) {
        this.fiatCurrency = fiatCurrency;
    }

    public BigDecimal getMonoPrice() {
        return monoPrice;
    }

    public void setMonoPrice(BigDecimal monoPrice) {
        this.monoPrice = monoPrice;
    }

    public BigDecimal getAbankPrice() {
        return abankPrice;
    }

    public void setAbankPrice(BigDecimal abankPrice) {
        this.abankPrice = abankPrice;
    }

    public BigDecimal getSpread() {
        return spread;
    }

    public void setSpread(BigDecimal spread) {
        this.spread = spread;
    }

    public Double getSpreadPercent() {
        return spreadPercent;
    }

    public void setSpreadPercent(Double spreadPercent) {
        this.spreadPercent = spreadPercent;
    }
}

