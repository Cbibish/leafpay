package com.leafpay.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Log.
 */
@Entity
@Table(name = "log")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Log implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Size(max = 255)
    @Column(name = "action", length = 255)
    private String action;

    @Column(name = "timestamp")
    private Instant timestamp;

    @Size(max = 50)
    @Column(name = "ip_utilisateur", length = 50)
    private String ipUtilisateur;

    @Size(max = 50)
    @Column(name = "resultat", length = 50)
    private String resultat;

    @Lob
    @Column(name = "description")
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "setRole", "logs", "alerteSecurites" }, allowSetters = true)
    private Utilisateur utilisateur;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Log id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAction() {
        return this.action;
    }

    public Log action(String action) {
        this.setAction(action);
        return this;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public Instant getTimestamp() {
        return this.timestamp;
    }

    public Log timestamp(Instant timestamp) {
        this.setTimestamp(timestamp);
        return this;
    }

    public void setTimestamp(Instant timestamp) {
        this.timestamp = timestamp;
    }

    public String getIpUtilisateur() {
        return this.ipUtilisateur;
    }

    public Log ipUtilisateur(String ipUtilisateur) {
        this.setIpUtilisateur(ipUtilisateur);
        return this;
    }

    public void setIpUtilisateur(String ipUtilisateur) {
        this.ipUtilisateur = ipUtilisateur;
    }

    public String getResultat() {
        return this.resultat;
    }

    public Log resultat(String resultat) {
        this.setResultat(resultat);
        return this;
    }

    public void setResultat(String resultat) {
        this.resultat = resultat;
    }

    public String getDescription() {
        return this.description;
    }

    public Log description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Utilisateur getUtilisateur() {
        return this.utilisateur;
    }

    public void setUtilisateur(Utilisateur utilisateur) {
        this.utilisateur = utilisateur;
    }

    public Log utilisateur(Utilisateur utilisateur) {
        this.setUtilisateur(utilisateur);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Log)) {
            return false;
        }
        return getId() != null && getId().equals(((Log) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Log{" +
            "id=" + getId() +
            ", action='" + getAction() + "'" +
            ", timestamp='" + getTimestamp() + "'" +
            ", ipUtilisateur='" + getIpUtilisateur() + "'" +
            ", resultat='" + getResultat() + "'" +
            ", description='" + getDescription() + "'" +
            "}";
    }
}
