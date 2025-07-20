package com.leafpay.service.dto;

import jakarta.persistence.Lob;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link com.leafpay.domain.Transaction} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class TransactionDTO implements Serializable {

    private Long id;

    private BigDecimal montant;

    @Size(max = 50)
    private String typeTransaction;

    private Instant dateTransaction;

    @Size(max = 50)
    private String statut;

    @Size(max = 255)
    private String moyenValidation;

    @Lob
    private String justificatif;

    private CompteDTO compteSource;

    private CompteDTO compteDestination;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getMontant() {
        return montant;
    }

    public void setMontant(BigDecimal montant) {
        this.montant = montant;
    }

    public String getTypeTransaction() {
        return typeTransaction;
    }

    public void setTypeTransaction(String typeTransaction) {
        this.typeTransaction = typeTransaction;
    }

    public Instant getDateTransaction() {
        return dateTransaction;
    }

    public void setDateTransaction(Instant dateTransaction) {
        this.dateTransaction = dateTransaction;
    }

    public String getStatut() {
        return statut;
    }

    public void setStatut(String statut) {
        this.statut = statut;
    }

    public String getMoyenValidation() {
        return moyenValidation;
    }

    public void setMoyenValidation(String moyenValidation) {
        this.moyenValidation = moyenValidation;
    }

    public String getJustificatif() {
        return justificatif;
    }

    public void setJustificatif(String justificatif) {
        this.justificatif = justificatif;
    }

    public CompteDTO getCompteSource() {
        return compteSource;
    }

    public void setCompteSource(CompteDTO compteSource) {
        this.compteSource = compteSource;
    }

    public CompteDTO getCompteDestination() {
        return compteDestination;
    }

    public void setCompteDestination(CompteDTO compteDestination) {
        this.compteDestination = compteDestination;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TransactionDTO)) {
            return false;
        }

        TransactionDTO transactionDTO = (TransactionDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, transactionDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TransactionDTO{" +
            "id=" + getId() +
            ", montant=" + getMontant() +
            ", typeTransaction='" + getTypeTransaction() + "'" +
            ", dateTransaction='" + getDateTransaction() + "'" +
            ", statut='" + getStatut() + "'" +
            ", moyenValidation='" + getMoyenValidation() + "'" +
            ", justificatif='" + getJustificatif() + "'" +
            ", compteSource=" + getCompteSource() +
            ", compteDestination=" + getCompteDestination() +
            "}";
    }
}
