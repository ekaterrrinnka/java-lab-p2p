package lab.p2p.repo;

import lab.p2p.model.PriceHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PriceHistoryRepo extends JpaRepository<PriceHistory, Long> {
}

