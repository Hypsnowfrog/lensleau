package fr.iut.lens.lensleau.domain;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Cascade;

import javax.persistence.*;

import java.io.Serializable;
import java.util.Objects;

/**
 * A Stock.
 */
@Entity
@Table(name = "stock")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Stock implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "quantity")
    private Integer quantity;

    @ManyToOne
    @Cascade(org.hibernate.annotations.CascadeType.ALL)
    @JsonIgnoreProperties("stocks")
    private User jhi_user;

    @ManyToOne
    @Cascade(org.hibernate.annotations.CascadeType.ALL)
    @JsonIgnoreProperties("stocks")
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

    public Stock quantity(Integer quantity) {
        this.quantity = quantity;
        return this;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public User getJhi_user() {
        return jhi_user;
    }

    public Stock jhi_user(User user) {
        this.jhi_user = user;
        return this;
    }

    public void setJhi_user(User user) {
        this.jhi_user = user;
    }

    public CoffeeType getCoffee_type() {
        return coffee_type;
    }

    public Stock coffee_type(CoffeeType coffeeType) {
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
        Stock stock = (Stock) o;
        if (stock.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), stock.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Stock{" +
            "id=" + getId() +
            ", quantity=" + getQuantity() +
            "}";
    }
}
