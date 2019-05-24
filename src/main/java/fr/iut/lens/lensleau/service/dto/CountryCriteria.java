package fr.iut.lens.lensleau.service.dto;

import java.io.Serializable;
import java.util.Objects;
import io.github.jhipster.service.filter.BooleanFilter;
import io.github.jhipster.service.filter.DoubleFilter;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.FloatFilter;
import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;

/**
 * Criteria class for the Country entity. This class is used in CountryResource to
 * receive all the possible filtering options from the Http GET request parameters.
 * For example the following could be a valid requests:
 * <code> /countries?id.greaterThan=5&amp;attr1.contains=something&amp;attr2.specified=false</code>
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class CountryCriteria implements Serializable {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter name;

    private StringFilter description;

    private StringFilter capital;

    private IntegerFilter population;

    private IntegerFilter surface;

    private IntegerFilter coffeeProduction;

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getName() {
        return name;
    }

    public void setName(StringFilter name) {
        this.name = name;
    }

    public StringFilter getDescription() {
        return description;
    }

    public void setDescription(StringFilter description) {
        this.description = description;
    }

    public StringFilter getCapital() {
        return capital;
    }

    public void setCapital(StringFilter capital) {
        this.capital = capital;
    }

    public IntegerFilter getPopulation() {
        return population;
    }

    public void setPopulation(IntegerFilter population) {
        this.population = population;
    }

    public IntegerFilter getSurface() {
        return surface;
    }

    public void setSurface(IntegerFilter surface) {
        this.surface = surface;
    }

    public IntegerFilter getCoffeeProduction() {
        return coffeeProduction;
    }

    public void setCoffeeProduction(IntegerFilter coffeeProduction) {
        this.coffeeProduction = coffeeProduction;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final CountryCriteria that = (CountryCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(name, that.name) &&
            Objects.equals(description, that.description) &&
            Objects.equals(capital, that.capital) &&
            Objects.equals(population, that.population) &&
            Objects.equals(surface, that.surface) &&
            Objects.equals(coffeeProduction, that.coffeeProduction);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        name,
        description,
        capital,
        population,
        surface,
        coffeeProduction
        );
    }

    @Override
    public String toString() {
        return "CountryCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (name != null ? "name=" + name + ", " : "") +
                (description != null ? "description=" + description + ", " : "") +
                (capital != null ? "capital=" + capital + ", " : "") +
                (population != null ? "population=" + population + ", " : "") +
                (surface != null ? "surface=" + surface + ", " : "") +
                (coffeeProduction != null ? "coffeeProduction=" + coffeeProduction + ", " : "") +
            "}";
    }

}
