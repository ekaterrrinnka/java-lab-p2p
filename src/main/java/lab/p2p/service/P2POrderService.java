package lab.p2p.service;

import lab.p2p.dto.SpreadDTO;
import lab.p2p.model.P2POrder;
import lab.p2p.repo.P2POrderRepo;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Optional;

@Service
public class P2POrderService {

    private final P2POrderRepo orderRepo;

    public P2POrderService(P2POrderRepo orderRepo) {
        this.orderRepo = orderRepo;
    }

    public List<P2POrder> getAll() {
        return orderRepo.findAll();
    }

    public List<P2POrder> getWithMinLimitFrom(BigDecimal minLimit) {
        return orderRepo.findByMinLimitGreaterThanEqual(minLimit);
    }

    public P2POrder save(P2POrder order) {
        return orderRepo.save(order);
    }

    public void deleteById(Long id) {
        orderRepo.deleteById(id);
    }

    public P2POrder getById(Long id) {
        return orderRepo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Order not found: " + id));
    }

    public List<P2POrder> filter(BigDecimal minLimit) {
        if (minLimit == null) {
            return getAll();
        }
        return getWithMinLimitFrom(minLimit);
    }

    public SpreadDTO calculateMonoAbankSpread(String fiatCurrency) {
        List<P2POrder> orders = orderRepo.findAll();

        Optional<BigDecimal> monoPriceOpt = orders.stream()
                .filter(o -> o.getPaymentMethod() != null)
                .filter(o -> o.getFiatCurrency() != null)
                .filter(o -> o.getFiatCurrency().equalsIgnoreCase(fiatCurrency))
                .filter(o -> o.getPaymentMethod().toLowerCase().contains("mono"))
                .map(P2POrder::getPrice)
                .max(BigDecimal::compareTo);

        Optional<BigDecimal> abankPriceOpt = orders.stream()
                .filter(o -> o.getPaymentMethod() != null)
                .filter(o -> o.getFiatCurrency() != null)
                .filter(o -> o.getFiatCurrency().equalsIgnoreCase(fiatCurrency))
                .filter(o -> o.getPaymentMethod().toLowerCase().contains("abank"))
                .map(P2POrder::getPrice)
                .min(BigDecimal::compareTo);

        SpreadDTO dto = new SpreadDTO();
        dto.setFiatCurrency(fiatCurrency);

        if (monoPriceOpt.isEmpty() || abankPriceOpt.isEmpty()) {
            return dto;
        }

        BigDecimal monoPrice = monoPriceOpt.get();
        BigDecimal abankPrice = abankPriceOpt.get();

        dto.setMonoPrice(monoPrice);
        dto.setAbankPrice(abankPrice);

        BigDecimal spread = monoPrice.subtract(abankPrice);
        dto.setSpread(spread);

        if (abankPrice.compareTo(BigDecimal.ZERO) > 0) {
            BigDecimal percent = spread
                    .divide(abankPrice, 6, RoundingMode.HALF_UP)
                    .multiply(BigDecimal.valueOf(100));
            dto.setSpreadPercent(percent.doubleValue());
        }

        return dto;
    }
}

