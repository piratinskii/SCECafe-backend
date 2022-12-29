package il.sce.scecafe.entity;

import jakarta.persistence.*;

import java.util.List;

@Entity
public class Tables {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @OneToOne
    @JoinColumn(name = "orderID")
    private Orders order;

    public Tables() {
    }

    public Tables(Long id, Orders order) {
        this.id = id;
        this.order = order;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Orders getOrder() {
        return order;
    }

    public void setOrder(Orders order) {
        this.order = order;
    }
}
