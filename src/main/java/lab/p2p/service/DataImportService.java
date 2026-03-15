package lab.p2p.service;

import lab.p2p.integration.ExternalP2PClient;
import lab.p2p.integration.ExternalP2POfferDto;
import lab.p2p.model.Merchant;
import lab.p2p.model.P2POrder;
import lab.p2p.repo.MerchantRepo;
import lab.p2p.repo.P2POrderRepo;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DataImportService {

    private final ExternalP2PClient externalClient;
    private final MerchantRepo merchantRepo;
    private final P2POrderRepo orderRepo;

    public DataImportService(ExternalP2PClient externalClient,
                             MerchantRepo merchantRepo,
                             P2POrderRepo orderRepo) {
        this.externalClient = externalClient;
        this.merchantRepo = merchantRepo;
        this.orderRepo = orderRepo;
    }

    public int importFromExternal() {
        List<ExternalP2POfferDto> offers = externalClient.fetchOffers();
        int count = 0;

        for (ExternalP2POfferDto dto : offers) {
            Merchant merchant = merchantRepo.findByName(dto.getMerchantName())
                    .orElseGet(() -> {
                        Merchant m = new Merchant();
                        m.setName(dto.getMerchantName());
                        m.setOrdersCount(dto.getMerchantOrdersCount());
                        m.setSuccessPercent(dto.getMerchantSuccessPercent());
                        return merchantRepo.save(m);
                    });

            P2POrder order = new P2POrder();
            order.setType(dto.getType());
            order.setPrice(dto.getPrice());
            order.setMinLimit(dto.getMinLimit());
            order.setMaxLimit(dto.getMaxLimit());
            order.setFiatCurrency(dto.getFiatCurrency());
            order.setPaymentMethod(dto.getPaymentMethod());
            order.setMerchant(merchant);
            orderRepo.save(order);
            count++;
        }

        return count;
    }
}
