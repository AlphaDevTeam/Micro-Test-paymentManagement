package com.alphadevs.wikunum.services.domain;

import java.io.Serializable;
import java.time.Instant;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Payments.
 */
@Entity
@Table(name = "payments")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Payments implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "notes")
    private String notes;

    @Column(name = "transaction_date")
    private Instant transactionDate;

    @Column(name = "cash_amount")
    private Double cashAmount;

    @Column(name = "credit_amount")
    private Double creditAmount;

    @Column(name = "credit_card_amount")
    private Double creditCardAmount;

    @NotNull
    @Column(name = "transaction_id", nullable = false)
    private String transactionID;

    @NotNull
    @Column(name = "location_code", nullable = false)
    private String locationCode;

    @NotNull
    @Column(name = "tenant_code", nullable = false)
    private String tenantCode;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Payments id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNotes() {
        return this.notes;
    }

    public Payments notes(String notes) {
        this.setNotes(notes);
        return this;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public Instant getTransactionDate() {
        return this.transactionDate;
    }

    public Payments transactionDate(Instant transactionDate) {
        this.setTransactionDate(transactionDate);
        return this;
    }

    public void setTransactionDate(Instant transactionDate) {
        this.transactionDate = transactionDate;
    }

    public Double getCashAmount() {
        return this.cashAmount;
    }

    public Payments cashAmount(Double cashAmount) {
        this.setCashAmount(cashAmount);
        return this;
    }

    public void setCashAmount(Double cashAmount) {
        this.cashAmount = cashAmount;
    }

    public Double getCreditAmount() {
        return this.creditAmount;
    }

    public Payments creditAmount(Double creditAmount) {
        this.setCreditAmount(creditAmount);
        return this;
    }

    public void setCreditAmount(Double creditAmount) {
        this.creditAmount = creditAmount;
    }

    public Double getCreditCardAmount() {
        return this.creditCardAmount;
    }

    public Payments creditCardAmount(Double creditCardAmount) {
        this.setCreditCardAmount(creditCardAmount);
        return this;
    }

    public void setCreditCardAmount(Double creditCardAmount) {
        this.creditCardAmount = creditCardAmount;
    }

    public String getTransactionID() {
        return this.transactionID;
    }

    public Payments transactionID(String transactionID) {
        this.setTransactionID(transactionID);
        return this;
    }

    public void setTransactionID(String transactionID) {
        this.transactionID = transactionID;
    }

    public String getLocationCode() {
        return this.locationCode;
    }

    public Payments locationCode(String locationCode) {
        this.setLocationCode(locationCode);
        return this;
    }

    public void setLocationCode(String locationCode) {
        this.locationCode = locationCode;
    }

    public String getTenantCode() {
        return this.tenantCode;
    }

    public Payments tenantCode(String tenantCode) {
        this.setTenantCode(tenantCode);
        return this;
    }

    public void setTenantCode(String tenantCode) {
        this.tenantCode = tenantCode;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Payments)) {
            return false;
        }
        return id != null && id.equals(((Payments) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Payments{" +
            "id=" + getId() +
            ", notes='" + getNotes() + "'" +
            ", transactionDate='" + getTransactionDate() + "'" +
            ", cashAmount=" + getCashAmount() +
            ", creditAmount=" + getCreditAmount() +
            ", creditCardAmount=" + getCreditCardAmount() +
            ", transactionID='" + getTransactionID() + "'" +
            ", locationCode='" + getLocationCode() + "'" +
            ", tenantCode='" + getTenantCode() + "'" +
            "}";
    }
}
