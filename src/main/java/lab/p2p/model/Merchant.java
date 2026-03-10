package lab.p2p.model;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
public class Merchant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private Integer ordersCount;
    private Double successPercent;

    @OneToMany(mappedBy = "merchant", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<P2POrder> orders = new ArrayList<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getOrdersCount() {
        return ordersCount;
    }

    public void setOrdersCount(Integer ordersCount) {
        this.ordersCount = ordersCount;
    }

    public Double getSuccessPercent() {
        return successPercent;
    }

    public void setSuccessPercent(Double successPercent) {
        this.successPercent = successPercent;
    }

    public List<P2POrder> getOrders() {
        return orders;
    }

    public void setOrders(List<P2POrder> orders) {
        this.orders = orders;
    }
}

