package com.alphadevs.wikunum.services.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import org.springdoc.api.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.alphadevs.wikunum.services.domain.Payments} entity. This class is used
 * in {@link com.alphadevs.wikunum.services.web.rest.PaymentsResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /payments?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PaymentsCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter notes;

    private InstantFilter transactionDate;

    private DoubleFilter cashAmount;

    private DoubleFilter creditAmount;

    private DoubleFilter creditCardAmount;

    private StringFilter transactionID;

    private StringFilter locationCode;

    private StringFilter tenantCode;

    private Boolean distinct;

    public PaymentsCriteria() {}

    public PaymentsCriteria(PaymentsCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.notes = other.notes == null ? null : other.notes.copy();
        this.transactionDate = other.transactionDate == null ? null : other.transactionDate.copy();
        this.cashAmount = other.cashAmount == null ? null : other.cashAmount.copy();
        this.creditAmount = other.creditAmount == null ? null : other.creditAmount.copy();
        this.creditCardAmount = other.creditCardAmount == null ? null : other.creditCardAmount.copy();
        this.transactionID = other.transactionID == null ? null : other.transactionID.copy();
        this.locationCode = other.locationCode == null ? null : other.locationCode.copy();
        this.tenantCode = other.tenantCode == null ? null : other.tenantCode.copy();
        this.distinct = other.distinct;
    }

    @Override
    public PaymentsCriteria copy() {
        return new PaymentsCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public LongFilter id() {
        if (id == null) {
            id = new LongFilter();
        }
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getNotes() {
        return notes;
    }

    public StringFilter notes() {
        if (notes == null) {
            notes = new StringFilter();
        }
        return notes;
    }

    public void setNotes(StringFilter notes) {
        this.notes = notes;
    }

    public InstantFilter getTransactionDate() {
        return transactionDate;
    }

    public InstantFilter transactionDate() {
        if (transactionDate == null) {
            transactionDate = new InstantFilter();
        }
        return transactionDate;
    }

    public void setTransactionDate(InstantFilter transactionDate) {
        this.transactionDate = transactionDate;
    }

    public DoubleFilter getCashAmount() {
        return cashAmount;
    }

    public DoubleFilter cashAmount() {
        if (cashAmount == null) {
            cashAmount = new DoubleFilter();
        }
        return cashAmount;
    }

    public void setCashAmount(DoubleFilter cashAmount) {
        this.cashAmount = cashAmount;
    }

    public DoubleFilter getCreditAmount() {
        return creditAmount;
    }

    public DoubleFilter creditAmount() {
        if (creditAmount == null) {
            creditAmount = new DoubleFilter();
        }
        return creditAmount;
    }

    public void setCreditAmount(DoubleFilter creditAmount) {
        this.creditAmount = creditAmount;
    }

    public DoubleFilter getCreditCardAmount() {
        return creditCardAmount;
    }

    public DoubleFilter creditCardAmount() {
        if (creditCardAmount == null) {
            creditCardAmount = new DoubleFilter();
        }
        return creditCardAmount;
    }

    public void setCreditCardAmount(DoubleFilter creditCardAmount) {
        this.creditCardAmount = creditCardAmount;
    }

    public StringFilter getTransactionID() {
        return transactionID;
    }

    public StringFilter transactionID() {
        if (transactionID == null) {
            transactionID = new StringFilter();
        }
        return transactionID;
    }

    public void setTransactionID(StringFilter transactionID) {
        this.transactionID = transactionID;
    }

    public StringFilter getLocationCode() {
        return locationCode;
    }

    public StringFilter locationCode() {
        if (locationCode == null) {
            locationCode = new StringFilter();
        }
        return locationCode;
    }

    public void setLocationCode(StringFilter locationCode) {
        this.locationCode = locationCode;
    }

    public StringFilter getTenantCode() {
        return tenantCode;
    }

    public StringFilter tenantCode() {
        if (tenantCode == null) {
            tenantCode = new StringFilter();
        }
        return tenantCode;
    }

    public void setTenantCode(StringFilter tenantCode) {
        this.tenantCode = tenantCode;
    }

    public Boolean getDistinct() {
        return distinct;
    }

    public void setDistinct(Boolean distinct) {
        this.distinct = distinct;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final PaymentsCriteria that = (PaymentsCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(notes, that.notes) &&
            Objects.equals(transactionDate, that.transactionDate) &&
            Objects.equals(cashAmount, that.cashAmount) &&
            Objects.equals(creditAmount, that.creditAmount) &&
            Objects.equals(creditCardAmount, that.creditCardAmount) &&
            Objects.equals(transactionID, that.transactionID) &&
            Objects.equals(locationCode, that.locationCode) &&
            Objects.equals(tenantCode, that.tenantCode) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            notes,
            transactionDate,
            cashAmount,
            creditAmount,
            creditCardAmount,
            transactionID,
            locationCode,
            tenantCode,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PaymentsCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (notes != null ? "notes=" + notes + ", " : "") +
            (transactionDate != null ? "transactionDate=" + transactionDate + ", " : "") +
            (cashAmount != null ? "cashAmount=" + cashAmount + ", " : "") +
            (creditAmount != null ? "creditAmount=" + creditAmount + ", " : "") +
            (creditCardAmount != null ? "creditCardAmount=" + creditCardAmount + ", " : "") +
            (transactionID != null ? "transactionID=" + transactionID + ", " : "") +
            (locationCode != null ? "locationCode=" + locationCode + ", " : "") +
            (tenantCode != null ? "tenantCode=" + tenantCode + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
