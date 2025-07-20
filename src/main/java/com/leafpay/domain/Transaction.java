package com.leafpay.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Transaction.
 */
@Entity
@Table(name = "transaction")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Transaction implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "montant", precision = 21, scale = 2)
    private BigDecimal montant;

    @Size(max = 50)
    @Column(name = "type_transaction", length = 50)
    private String typeTransaction;

    @Column(name = "date_transaction")
    private Instant dateTransaction;

    @Size(max = 50)
    @Column(name = "statut", length = 50)
    private String statut;

    @Size(max = 255)
    @Column(name = "moyen_validation", length = 255)
    private String moyenValidation;

    @Lob
    @Column(name = "justificatif")
    private String justificatif;

    @ManyToOne(fetch = FetchType.LAZY)
    private Compte compteSource;

    @ManyToOne(fetch = FetchType.LAZY)
    private Compte compteDestination;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Transaction id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getMontant() {
        return this.montant;
    }

    public Transaction montant(BigDecimal montant) {
        this.setMontant(montant);
        return this;
    }

    public void setMontant(BigDecimal montant) {
        this.montant = montant;
    }

    public String getTypeTransaction() {
        return this.typeTransaction;
    }

    public Transaction typeTransaction(String typeTransaction) {
        this.setTypeTransaction(typeTransaction);
        return this;
    }

    public void setTypeTransaction(String typeTransaction) {
        this.typeTransaction = typeTransaction;
    }

    public Instant getDateTransaction() {
        return this.dateTransaction;
    }

    public Transaction dateTransaction(Instant dateTransaction) {
        this.setDateTransaction(dateTransaction);
        return this;
    }

    public void setDateTransaction(Instant dateTransaction) {
        this.dateTransaction = dateTransaction;
    }

    public String getStatut() {
        return this.statut;
    }

    public Transaction statut(String statut) {
        this.setStatut(statut);
        return this;
    }

    public void setStatut(String statut) {
        this.statut = statut;
    }

    public String getMoyenValidation() {
        return this.moyenValidation;
    }

    public Transaction moyenValidation(String moyenValidation) {
        this.setMoyenValidation(moyenValidation);
        return this;
    }

    public void setMoyenValidation(String moyenValidation) {
        this.moyenValidation = moyenValidation;
    }

    public String getJustificatif() {
        return this.justificatif;
    }

    public Transaction justificatif(String justificatif) {
        this.setJustificatif(justificatif);
        return this;
    }

    public void setJustificatif(String justificatif) {
        this.justificatif = justificatif;
    }

    public Compte getCompteSource() {
        return this.compteSource;
    }

    public void setCompteSource(Compte compte) {
        this.compteSource = compte;
    }

    public Transaction compteSource(Compte compte) {
        this.setCompteSource(compte);
        return this;
    }

    public Compte getCompteDestination() {
        return this.compteDestination;
    }

    public void setCompteDestination(Compte compte) {
        this.compteDestination = compte;
    }

    public Transaction compteDestination(Compte compte) {
        this.setCompteDestination(compte);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Transaction)) {
            return false;
        }
        return getId() != null && getId().equals(((Transaction) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Transaction{" +
            "id=" + getId() +
            ", montant=" + getMontant() +
            ", typeTransaction='" + getTypeTransaction() + "'" +
            ", dateTransaction='" + getDateTransaction() + "'" +
            ", statut='" + getStatut() + "'" +
            ", moyenValidation='" + getMoyenValidation() + "'" +
            ", justificatif='" + getJustificatif() + "'" +
            "}";
    }
}
