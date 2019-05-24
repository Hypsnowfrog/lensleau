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
import io.github.jhipster.service.filter.ZonedDateTimeFilter;

/**
 * Criteria class for the Order entity. This class is used in OrderResource to
 * receive all the possible filtering options from the Http GET request parameters.
 * For example the following could be a valid requests:
 * <code> /orders?id.greaterThan=5&amp;attr1.contains=something&amp;attr2.specified=false</code>
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class OrderCriteria implements Serializable {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private IntegerFilter quantity;

    private ZonedDateTimeFilter date;

    private LongFilter exporterId;

    private LongFilter importerId;

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

    public ZonedDateTimeFilter getDate() {
        return date;
    }

    public void setDate(ZonedDateTimeFilter date) {
        this.date = date;
    }

    public LongFilter getExporterId() {
        return exporterId;
    }

    public void setExporterId(LongFilter exporterId) {
        this.exporterId = exporterId;
    }

    public LongFilter getImporterId() {
        return importerId;
    }

    public void setImporterId(LongFilter importerId) {
        this.importerId = importerId;
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
        final OrderCriteria that = (OrderCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(quantity, that.quantity) &&
            Objects.equals(date, that.date) &&
            Objects.equals(exporterId, that.exporterId) &&
            Objects.equals(importerId, that.importerId) &&
            Objects.equals(coffee_typeId, that.coffee_typeId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        quantity,
        date,
        exporterId,
        importerId,
        coffee_typeId
        );
    }

    @Override
    public String toString() {
        return "OrderCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (quantity != null ? "quantity=" + quantity + ", " : "") +
                (date != null ? "date=" + date + ", " : "") +
                (exporterId != null ? "exporterId=" + exporterId + ", " : "") +
                (importerId != null ? "importerId=" + importerId + ", " : "") +
                (coffee_typeId != null ? "coffee_typeId=" + coffee_typeId + ", " : "") +
            "}";
    }

}
