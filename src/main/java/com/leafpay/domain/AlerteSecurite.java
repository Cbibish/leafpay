package com.leafpay.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A AlerteSecurite.
 */
@Entity
@Table(name = "alerte_securite")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class AlerteSecurite implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Size(max = 100)
    @Column(name = "type_alerte", length = 100)
    private String typeAlerte;

    @Size(max = 50)
    @Column(name = "niveau_severite", length = 50)
    private String niveauSeverite;

    @Column(name = "timestamp")
    private Instant timestamp;

    @Column(name = "est_traitee")
    private Boolean estTraitee;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "setRole", "logs", "alerteSecurites" }, allowSetters = true)
    private Utilisateur utilisateur;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public AlerteSecurite id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTypeAlerte() {
        return this.typeAlerte;
    }

    public AlerteSecurite typeAlerte(String typeAlerte) {
        this.setTypeAlerte(typeAlerte);
        return this;
    }

    public void setTypeAlerte(String typeAlerte) {
        this.typeAlerte = typeAlerte;
    }

    public String getNiveauSeverite() {
        return this.niveauSeverite;
    }

    public AlerteSecurite niveauSeverite(String niveauSeverite) {
        this.setNiveauSeverite(niveauSeverite);
        return this;
    }

    public void setNiveauSeverite(String niveauSeverite) {
        this.niveauSeverite = niveauSeverite;
    }

    public Instant getTimestamp() {
        return this.timestamp;
    }

    public AlerteSecurite timestamp(Instant timestamp) {
        this.setTimestamp(timestamp);
        return this;
    }

    public void setTimestamp(Instant timestamp) {
        this.timestamp = timestamp;
    }

    public Boolean getEstTraitee() {
        return this.estTraitee;
    }

    public AlerteSecurite estTraitee(Boolean estTraitee) {
        this.setEstTraitee(estTraitee);
        return this;
    }

    public void setEstTraitee(Boolean estTraitee) {
        this.estTraitee = estTraitee;
    }

    public Utilisateur getUtilisateur() {
        return this.utilisateur;
    }

    public void setUtilisateur(Utilisateur utilisateur) {
        this.utilisateur = utilisateur;
    }

    public AlerteSecurite utilisateur(Utilisateur utilisateur) {
        this.setUtilisateur(utilisateur);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AlerteSecurite)) {
            return false;
        }
        return getId() != null && getId().equals(((AlerteSecurite) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AlerteSecurite{" +
            "id=" + getId() +
            ", typeAlerte='" + getTypeAlerte() + "'" +
            ", niveauSeverite='" + getNiveauSeverite() + "'" +
            ", timestamp='" + getTimestamp() + "'" +
            ", estTraitee='" + getEstTraitee() + "'" +
            "}";
    }
}
