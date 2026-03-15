package lab.p2p.integration;

import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Component
public class ExternalP2PClient {

    public List<ExternalP2POfferDto> fetchOffers() {
        List<ExternalP2POfferDto> list = new ArrayList<>();

        ExternalP2POfferDto o1 = new ExternalP2POfferDto();
        o1.setType("BUY");
        o1.setPrice(new BigDecimal("41.20"));
        o1.setMinLimit(new BigDecimal("500"));
        o1.setMaxLimit(new BigDecimal("50000"));
        o1.setFiatCurrency("UAH");
        o1.setPaymentMethod("MONOBANK");
        o1.setMerchantName("CryptoPro");
        o1.setMerchantOrdersCount(120);
        o1.setMerchantSuccessPercent(98.5);
        list.add(o1);

        ExternalP2POfferDto o2 = new ExternalP2POfferDto();
        o2.setType("SELL");
        o2.setPrice(new BigDecimal("41.80"));
        o2.setMinLimit(new BigDecimal("1000"));
        o2.setMaxLimit(new BigDecimal("100000"));
        o2.setFiatCurrency("UAH");
        o2.setPaymentMethod("ABANK");
        o2.setMerchantName("BitSeller");
        o2.setMerchantOrdersCount(89);
        o2.setMerchantSuccessPercent(97.0);
        list.add(o2);

        ExternalP2POfferDto o3 = new ExternalP2POfferDto();
        o3.setType("BUY");
        o3.setPrice(new BigDecimal("41.00"));
        o3.setMinLimit(new BigDecimal("200"));
        o3.setMaxLimit(new BigDecimal("20000"));
        o3.setFiatCurrency("UAH");
        o3.setPaymentMethod("MONOBANK");
        o3.setMerchantName("CryptoPro");
        o3.setMerchantOrdersCount(120);
        o3.setMerchantSuccessPercent(98.5);
        list.add(o3);

        return list;
    }
}
