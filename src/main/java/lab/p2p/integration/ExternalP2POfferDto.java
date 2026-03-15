package lab.p2p.integration;

import java.math.BigDecimal;

public class ExternalP2POfferDto {

    private String type;
    private BigDecimal price;
    private BigDecimal minLimit;
    private BigDecimal maxLimit;
    private String fiatCurrency;
    private String paymentMethod;
    private String merchantName;
    private Integer merchantOrdersCount;
    private Double merchantSuccessPercent;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public BigDecimal getMinLimit() {
        return minLimit;
    }

    public void setMinLimit(BigDecimal minLimit) {
        this.minLimit = minLimit;
    }

    public BigDecimal getMaxLimit() {
        return maxLimit;
    }

    public void setMaxLimit(BigDecimal maxLimit) {
        this.maxLimit = maxLimit;
    }

    public String getFiatCurrency() {
        return fiatCurrency;
    }

    public void setFiatCurrency(String fiatCurrency) {
        this.fiatCurrency = fiatCurrency;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getMerchantName() {
        return merchantName;
    }

    public void setMerchantName(String merchantName) {
        this.merchantName = merchantName;
    }

    public Integer getMerchantOrdersCount() {
        return merchantOrdersCount;
    }

    public void setMerchantOrdersCount(Integer merchantOrdersCount) {
        this.merchantOrdersCount = merchantOrdersCount;
    }

    public Double getMerchantSuccessPercent() {
        return merchantSuccessPercent;
    }

    public void setMerchantSuccessPercent(Double merchantSuccessPercent) {
        this.merchantSuccessPercent = merchantSuccessPercent;
    }
}
