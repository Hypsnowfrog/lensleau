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
 * Criteria class for the Stock entity. This class is used in StockResource to
 * receive all the possible filtering options from the Http GET request parameters.
 * For example the following could be a valid requests:
 * <code> /stocks?id.greaterThan=5&amp;attr1.contains=something&amp;attr2.specified=false</code>
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class StockCriteria implements Serializable {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private IntegerFilter quantity;

    private LongFilter jhi_userId;

    private LongFilter coffee_typeId;

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public IntegerFilter getQuantity() {
        return quantity;
    }

    public void setQuantity(IntegerFilter quantity) {
        this.quantity = quantity;
    }

    public LongFilter getJhi_userId() {
        return jhi_userId;
    }

    public void setJhi_userId(LongFilter jhi_userId) {
        this.jhi_userId = jhi_userId;
    }

    public LongFilter getCoffee_typeId() {
        return coffee_typeId;
    }

    public void setCoffee_typeId(LongFilter coffee_typeId) {
        this.coffee_typeId = coffee_typeId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final StockCriteria that = (StockCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(quantity, that.quantity) &&
            Objects.equals(jhi_userId, that.jhi_userId) &&
            Objects.equals(coffee_typeId, that.coffee_typeId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        quantity,
        jhi_userId,
        coffee_typeId
        );
    }

    @Override
    public String toString() {
        return "StockCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (quantity != null ? "quantity=" + quantity + ", " : "") +
                (jhi_userId != null ? "jhi_userId=" + jhi_userId + ", " : "") +
                (coffee_typeId != null ? "coffee_typeId=" + coffee_typeId + ", " : "") +
            "}";
    }

}
