package fr.iut.lens.lensleau.domain;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Cascade;

import javax.persistence.*;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * A Order.
 */
@Entity
@Table(name = "jhi_order")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Order implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "quantity")
    private Integer quantity;

    @Column(name = "jhi_date")
    private ZonedDateTime date;

    @ManyToOne
    @Cascade(org.hibernate.annotations.CascadeType.ALL)
    @JsonIgnoreProperties("orders")
    private User exporter;

    @ManyToOne
    @Cascade(org.hibernate.annotations.CascadeType.ALL)
    @JsonIgnoreProperties("orders")
    private User importer;

    @ManyToOne
    @Cascade(org.hibernate.annotations.CascadeType.ALL)
    @JsonIgnoreProperties("orders")
    private CoffeeType coffee_type;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public Order quantity(Integer quantity) {
        this.quantity = quantity;
        return this;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public ZonedDateTime getDate() {
        return date;
    }

    public Order date(ZonedDateTime date) {
        this.date = date;
        return this;
    }

    public void setDate(ZonedDateTime date) {
        this.date = date;
    }

    public User getExporter() {
        return exporter;
    }

    public Order exporter(User user) {
        this.exporter = user;
        return this;
    }

    public void setExporter(User user) {
        this.exporter = user;
    }

    public User getImporter() {
        return importer;
    }

    public Order importer(User user) {
        this.importer = user;
        return this;
    }

    public void setImporter(User user) {
        this.importer = user;
    }

    public CoffeeType getCoffee_type() {
        return coffee_type;
    }

    public Order coffee_type(CoffeeType coffeeType) {
        this.coffee_type = coffeeType;
        return this;
    }

    public void setCoffee_type(CoffeeType coffeeType) {
        this.coffee_type = coffeeType;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Order order = (Order) o;
        if (order.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), order.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Order{" +
            "id=" + getId() +
            ", quantity=" + getQuantity() +
            ", date='" + getDate() + "'" +
            "}";
    }
}
