package com.leafpay.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Compte.
 */
@Entity
@Table(name = "compte")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Compte implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Size(max = 50)
    @Column(name = "type_compte", length = 50)
    private String typeCompte;

    @Column(name = "solde", precision = 21, scale = 2)
    private BigDecimal solde;

    @Column(name = "plafond_transaction", precision = 21, scale = 2)
    private BigDecimal plafondTransaction;

    @Column(name = "limite_retraits_mensuels")
    private Integer limiteRetraitsMensuels;

    @Column(name = "taux_interet", precision = 21, scale = 2)
    private BigDecimal tauxInteret;

    @Column(name = "date_ouverture")
    private Instant dateOuverture;

    @Column(name = "date_fermeture")
    private Instant dateFermeture;

    @Size(max = 50)
    @Column(name = "statut", length = 50)
    private String statut;

    @Size(max = 34)
    @Column(name = "iban", length = 34)
    private String iban;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Compte id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTypeCompte() {
        return this.typeCompte;
    }

    public Compte typeCompte(String typeCompte) {
        this.setTypeCompte(typeCompte);
        return this;
    }

    public void setTypeCompte(String typeCompte) {
        this.typeCompte = typeCompte;
    }

    public BigDecimal getSolde() {
        return this.solde;
    }

    public Compte solde(BigDecimal solde) {
        this.setSolde(solde);
        return this;
    }

    public void setSolde(BigDecimal solde) {
        this.solde = solde;
    }

    public BigDecimal getPlafondTransaction() {
        return this.plafondTransaction;
    }

    public Compte plafondTransaction(BigDecimal plafondTransaction) {
        this.setPlafondTransaction(plafondTransaction);
        return this;
    }

    public void setPlafondTransaction(BigDecimal plafondTransaction) {
        this.plafondTransaction = plafondTransaction;
    }

    public Integer getLimiteRetraitsMensuels() {
        return this.limiteRetraitsMensuels;
    }

    public Compte limiteRetraitsMensuels(Integer limiteRetraitsMensuels) {
        this.setLimiteRetraitsMensuels(limiteRetraitsMensuels);
        return this;
    }

    public void setLimiteRetraitsMensuels(Integer limiteRetraitsMensuels) {
        this.limiteRetraitsMensuels = limiteRetraitsMensuels;
    }

    public BigDecimal getTauxInteret() {
        return this.tauxInteret;
    }

    public Compte tauxInteret(BigDecimal tauxInteret) {
        this.setTauxInteret(tauxInteret);
        return this;
    }

    public void setTauxInteret(BigDecimal tauxInteret) {
        this.tauxInteret = tauxInteret;
    }

    public Instant getDateOuverture() {
        return this.dateOuverture;
    }

    public Compte dateOuverture(Instant dateOuverture) {
        this.setDateOuverture(dateOuverture);
        return this;
    }

    public void setDateOuverture(Instant dateOuverture) {
        this.dateOuverture = dateOuverture;
    }

    public Instant getDateFermeture() {
        return this.dateFermeture;
    }

    public Compte dateFermeture(Instant dateFermeture) {
        this.setDateFermeture(dateFermeture);
        return this;
    }

    public void setDateFermeture(Instant dateFermeture) {
        this.dateFermeture = dateFermeture;
    }

    public String getStatut() {
        return this.statut;
    }

    public Compte statut(String statut) {
        this.setStatut(statut);
        return this;
    }

    public void setStatut(String statut) {
        this.statut = statut;
    }

    public String getIban() {
        return this.iban;
    }

    public Compte iban(String iban) {
        this.setIban(iban);
        return this;
    }

    public void setIban(String iban) {
        this.iban = iban;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Compte)) {
            return false;
        }
        return getId() != null && getId().equals(((Compte) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Compte{" +
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
