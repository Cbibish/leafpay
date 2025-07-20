package com.leafpay.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link com.leafpay.domain.Compte} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class CompteDTO implements Serializable {

    private Long id;

    @Size(max = 50)
    private String typeCompte;

    private BigDecimal solde;

    private BigDecimal plafondTransaction;

    private Integer limiteRetraitsMensuels;

    private BigDecimal tauxInteret;

    private Instant dateOuverture;

    private Instant dateFermeture;

    @Size(max = 50)
    private String statut;

    @Size(max = 34)
    private String iban;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTypeCompte() {
        return typeCompte;
    }

    public void setTypeCompte(String typeCompte) {
        this.typeCompte = typeCompte;
    }

    public BigDecimal getSolde() {
        return solde;
    }

    public void setSolde(BigDecimal solde) {
        this.solde = solde;
    }

    public BigDecimal getPlafondTransaction() {
        return plafondTransaction;
    }

    public void setPlafondTransaction(BigDecimal plafondTransaction) {
        this.plafondTransaction = plafondTransaction;
    }

    public Integer getLimiteRetraitsMensuels() {
        return limiteRetraitsMensuels;
    }

    public void setLimiteRetraitsMensuels(Integer limiteRetraitsMensuels) {
        this.limiteRetraitsMensuels = limiteRetraitsMensuels;
    }

    public BigDecimal getTauxInteret() {
        return tauxInteret;
    }

    public void setTauxInteret(BigDecimal tauxInteret) {
        this.tauxInteret = tauxInteret;
    }

    public Instant getDateOuverture() {
        return dateOuverture;
    }

    public void setDateOuverture(Instant dateOuverture) {
        this.dateOuverture = dateOuverture;
    }

    public Instant getDateFermeture() {
        return dateFermeture;
    }

    public void setDateFermeture(Instant dateFermeture) {
        this.dateFermeture = dateFermeture;
    }

    public String getStatut() {
        return statut;
    }

    public void setStatut(String statut) {
        this.statut = statut;
    }

    public String getIban() {
        return iban;
    }

    public void setIban(String iban) {
        this.iban = iban;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CompteDTO)) {
            return false;
        }

        CompteDTO compteDTO = (CompteDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, compteDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CompteDTO{" +
            "id=" + getId() +
            ", typeCompte='" + getTypeCompte() + "'" +
            ", solde=" + getSolde() +
            ", plafondTransaction=" + getPlafondTransaction() +
            ", limiteRetraitsMensuels=" + getLimiteRetraitsMensuels() +
            ", tauxInteret=" + getTauxInteret() +
            ", dateOuverture='" + getDateOuverture() + "'" +
            ", dateFermeture='" + getDateFermeture() + "'" +
            ", statut='" + getStatut() + "'" +
            ", iban='" + getIban() + "'" +
            "}";
    }
}
