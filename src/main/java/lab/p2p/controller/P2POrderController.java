package lab.p2p.controller;

import lab.p2p.dto.P2PAdDTO;
import lab.p2p.dto.SpreadDTO;
import lab.p2p.model.Merchant;
import lab.p2p.model.P2POrder;
import lab.p2p.repo.MerchantRepo;
import lab.p2p.service.P2POrderService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/orders")
public class P2POrderController {

    private final P2POrderService orderService;
    private final MerchantRepo merchantRepo;

    public P2POrderController(P2POrderService orderService, MerchantRepo merchantRepo) {
        this.orderService = orderService;
        this.merchantRepo = merchantRepo;
    }

    @GetMapping
    public List<P2PAdDTO> getAll() {
        return orderService.getAll()
                .stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<P2PAdDTO> getById(@PathVariable Long id) {
        P2POrder order = orderService.getById(id);
        return ResponseEntity.ok(toDto(order));
    }

    @PostMapping
    public ResponseEntity<P2PAdDTO> create(@RequestBody P2PAdDTO dto) {
        P2POrder saved = orderService.save(fromDto(dto));
        return ResponseEntity.ok(toDto(saved));
    }

    @PutMapping("/{id}")
    public ResponseEntity<P2PAdDTO> update(@PathVariable Long id, @RequestBody P2PAdDTO dto) {
        P2POrder existing = orderService.getById(id);

        existing.setType(dto.getType());
        existing.setPrice(dto.getPrice());
        existing.setMinLimit(dto.getMinLimit());
        existing.setMaxLimit(dto.getMaxLimit());
        existing.setFiatCurrency(dto.getFiatCurrency());
        existing.setPaymentMethod(dto.getPaymentMethod());

        if (dto.getMerchantId() != null) {
            Merchant merchant = merchantRepo.findById(dto.getMerchantId())
                    .orElseThrow(() -> new IllegalArgumentException("Merchant not found: " + dto.getMerchantId()));
            existing.setMerchant(merchant);
        }

        P2POrder saved = orderService.save(existing);
        return ResponseEntity.ok(toDto(saved));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        orderService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/filter")
    public List<P2PAdDTO> filter(@RequestParam(name = "minLimit", required = false) BigDecimal minLimit) {
        return orderService.filter(minLimit)
                .stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/spread/mono-abank")
    public SpreadDTO getMonoAbankSpread(
            @RequestParam(name = "fiat", defaultValue = "UAH") String fiatCurrency
    ) {
        return orderService.calculateMonoAbankSpread(fiatCurrency);
    }

    private P2PAdDTO toDto(P2POrder order) {
        P2PAdDTO dto = new P2PAdDTO();
        dto.setId(order.getId());
        dto.setType(order.getType());
        dto.setPrice(order.getPrice());
        dto.setMinLimit(order.getMinLimit());
        dto.setMaxLimit(order.getMaxLimit());
        dto.setFiatCurrency(order.getFiatCurrency());
        dto.setPaymentMethod(order.getPaymentMethod());

        Merchant merchant = order.getMerchant();
        if (merchant != null) {
            dto.setMerchantId(merchant.getId());
            dto.setMerchantName(merchant.getName());
            dto.setMerchantOrdersCount(merchant.getOrdersCount());
            dto.setMerchantSuccessPercent(merchant.getSuccessPercent());
        }

        return dto;
    }

    private P2POrder fromDto(P2PAdDTO dto) {
        P2POrder order = new P2POrder();
        order.setType(dto.getType());
        order.setPrice(dto.getPrice());
        order.setMinLimit(dto.getMinLimit());
        order.setMaxLimit(dto.getMaxLimit());
        order.setFiatCurrency(dto.getFiatCurrency());
        order.setPaymentMethod(dto.getPaymentMethod());

        if (dto.getMerchantId() != null) {
            Merchant merchant = merchantRepo.findById(dto.getMerchantId())
                    .orElseThrow(() -> new IllegalArgumentException("Merchant not found: " + dto.getMerchantId()));
            order.setMerchant(merchant);
        }

        return order;
    }
}

