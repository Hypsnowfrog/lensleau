package fr.iut.lens.lensleau.domain;


import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;
import java.util.Objects;

/**
 * A Country.
 */
@Entity
@Table(name = "country")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Country implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "capital")
    private String capital;

    @Column(name = "population")
    private Integer population;

    @Column(name = "surface")
    private Integer surface;

    @Column(name = "coffee_production")
    private Integer coffeeProduction;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public Country name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public Country description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCapital() {
        return capital;
    }

    public Country capital(String capital) {
        this.capital = capital;
        return this;
    }

    public void setCapital(String capital) {
        this.capital = capital;
    }

    public Integer getPopulation() {
        return population;
    }

    public Country population(Integer population) {
        this.population = population;
        return this;
    }

    public void setPopulation(Integer population) {
        this.population = population;
    }

    public Integer getSurface() {
        return surface;
    }

    public Country surface(Integer surface) {
        this.surface = surface;
        return this;
    }

    public void setSurface(Integer surface) {
        this.surface = surface;
    }

    public Integer getCoffeeProduction() {
        return coffeeProduction;
    }

    public Country coffeeProduction(Integer coffeeProduction) {
        this.coffeeProduction = coffeeProduction;
        return this;
    }

    public void setCoffeeProduction(Integer coffeeProduction) {
        this.coffeeProduction = coffeeProduction;
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
        Country country = (Country) o;
        if (country.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), country.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Country{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", description='" + getDescription() + "'" +
            ", capital='" + getCapital() + "'" +
            ", population=" + getPopulation() +
            ", surface=" + getSurface() +
            ", coffeeProduction=" + getCoffeeProduction() +
            "}";
    }
}
