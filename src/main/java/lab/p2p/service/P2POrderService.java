package lab.p2p.service;

import lab.p2p.model.P2POrder;
import lab.p2p.repo.P2POrderRepo;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

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
}

