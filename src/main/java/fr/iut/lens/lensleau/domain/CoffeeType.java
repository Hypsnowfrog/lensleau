package fr.iut.lens.lensleau.domain;


import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;
import java.util.Objects;

/**
 * A CoffeeType.
 */
@Entity
@Table(name = "coffee_type")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class CoffeeType implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "coffee_type")
    private String coffeeType;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCoffeeType() {
        return coffeeType;
    }

    public CoffeeType coffeeType(String coffeeType) {
        this.coffeeType = coffeeType;
        return this;
    }

    public void setCoffeeType(String coffeeType) {
        this.coffeeType = coffeeType;
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
        CoffeeType coffeeType = (CoffeeType) o;
        if (coffeeType.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), coffeeType.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "CoffeeType{" +
            "id=" + getId() +
            ", coffeeType='" + getCoffeeType() + "'" +
            "}";
    }
}
