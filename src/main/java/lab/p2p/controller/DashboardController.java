package lab.p2p.controller;

import lab.p2p.dto.SpreadDTO;
import lab.p2p.model.P2POrder;
import lab.p2p.service.P2POrderService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class DashboardController {

    private final P2POrderService orderService;

    public DashboardController(P2POrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping("/")
    public String index(Model model) {
        List<P2POrder> orders = orderService.getAll();
        SpreadDTO spread = orderService.calculateMonoAbankSpread("UAH");

        model.addAttribute("orders", orders);
        model.addAttribute("spread", spread);

        return "index";
    }
}

