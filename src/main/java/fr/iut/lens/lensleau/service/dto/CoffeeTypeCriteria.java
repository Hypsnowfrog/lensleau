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
 * Criteria class for the CoffeeType entity. This class is used in CoffeeTypeResource to
 * receive all the possible filtering options from the Http GET request parameters.
 * For example the following could be a valid requests:
 * <code> /coffee-types?id.greaterThan=5&amp;attr1.contains=something&amp;attr2.specified=false</code>
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class CoffeeTypeCriteria implements Serializable {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter coffeeType;

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getCoffeeType() {
        return coffeeType;
    }

    public void setCoffeeType(StringFilter coffeeType) {
        this.coffeeType = coffeeType;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final CoffeeTypeCriteria that = (CoffeeTypeCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(coffeeType, that.coffeeType);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        coffeeType
        );
    }

    @Override
    public String toString() {
        return "CoffeeTypeCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (coffeeType != null ? "coffeeType=" + coffeeType + ", " : "") +
            "}";
    }

}
