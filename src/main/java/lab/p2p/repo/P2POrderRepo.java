package lab.p2p.repo;

import lab.p2p.model.P2POrder;
import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigDecimal;
import java.util.List;

public interface P2POrderRepo extends JpaRepository<P2POrder, Long> {

    List<P2POrder> findByMinLimitGreaterThanEqual(BigDecimal minLimit);
}

