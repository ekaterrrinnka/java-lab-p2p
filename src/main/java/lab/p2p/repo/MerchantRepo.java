package lab.p2p.repo;

import lab.p2p.model.Merchant;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MerchantRepo extends JpaRepository<Merchant, Long> {
}

