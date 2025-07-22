package com.leafpay.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Utilisateur.
 */
@Entity
@Table(name = "utilisateur")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Utilisateur implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(max = 100)
    @Column(name = "nom", length = 100, nullable = false)
    private String nom;

    @NotNull
    @Size(max = 100)
    @Column(name = "prenom", length = 100, nullable = false)
    private String prenom;

    @NotNull
    @Size(max = 255)
    @Column(name = "email", length = 255, nullable = false)
    private String email;

    @NotNull
    @Size(max = 255)
    @Column(name = "mot_de_passe", length = 255, nullable = false)
    private String motDePasse;

    @Column(name = "date_naissance")
    private LocalDate dateNaissance;

    @Size(max = 255)
    @Column(name = "type_justificatif_age", length = 255)
    private String typeJustificatifAge;

    @Size(max = 50)
    @Column(name = "statut", length = 50)
    private String statut;

    @Column(name = "date_creation")
    private Instant dateCreation;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_role_id") // add this for clarity, specifying the FK column name
    private Role role;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "utilisateur")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "utilisateur" }, allowSetters = true)
    private Set<Log> logs = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "utilisateur")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "utilisateur" }, allowSetters = true)
    private Set<AlerteSecurite> alerteSecurites = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Utilisateur id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNom() {
        return this.nom;
    }

    public Utilisateur nom(String nom) {
        this.setNom(nom);
        return this;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return this.prenom;
    }

    public Utilisateur prenom(String prenom) {
        this.setPrenom(prenom);
        return this;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getEmail() {
        return this.email;
    }

    public Utilisateur email(String email) {
        this.setEmail(email);
        return this;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMotDePasse() {
        return this.motDePasse;
    }

    public Utilisateur motDePasse(String motDePasse) {
        this.setMotDePasse(motDePasse);
        return this;
    }

    public void setMotDePasse(String motDePasse) {
        this.motDePasse = motDePasse;
    }

    public LocalDate getDateNaissance() {
        return this.dateNaissance;
    }

    public Utilisateur dateNaissance(LocalDate dateNaissance) {
        this.setDateNaissance(dateNaissance);
        return this;
    }

    public void setDateNaissance(LocalDate dateNaissance) {
        this.dateNaissance = dateNaissance;
    }

    public String getTypeJustificatifAge() {
        return this.typeJustificatifAge;
    }

    public Utilisateur typeJustificatifAge(String typeJustificatifAge) {
        this.setTypeJustificatifAge(typeJustificatifAge);
        return this;
    }

    public void setTypeJustificatifAge(String typeJustificatifAge) {
        this.typeJustificatifAge = typeJustificatifAge;
    }

    public String getStatut() {
        return this.statut;
    }

    public Utilisateur statut(String statut) {
        this.setStatut(statut);
        return this;
    }

    public void setStatut(String statut) {
        this.statut = statut;
    }

    public Instant getDateCreation() {
        return this.dateCreation;
    }

    public Utilisateur dateCreation(Instant dateCreation) {
        this.setDateCreation(dateCreation);
        return this;
    }

    public void setDateCreation(Instant dateCreation) {
        this.dateCreation = dateCreation;
    }

    public Role getRole() {
        return this.role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public Utilisateur role(Role role) {
        this.setRole(role);
        return this;
    }

    public Set<Log> getLogs() {
        return this.logs;
    }

    public void setLogs(Set<Log> logs) {
        if (this.logs != null) {
            this.logs.forEach(i -> i.setUtilisateur(null));
        }
        if (logs != null) {
            logs.forEach(i -> i.setUtilisateur(this));
        }
        this.logs = logs;
    }

    public Utilisateur logs(Set<Log> logs) {
        this.setLogs(logs);
        return this;
    }

    public Utilisateur addLog(Log log) {
        this.logs.add(log);
        log.setUtilisateur(this);
        return this;
    }

    public Utilisateur removeLog(Log log) {
        this.logs.remove(log);
        log.setUtilisateur(null);
        return this;
    }

    public Set<AlerteSecurite> getAlerteSecurites() {
        return this.alerteSecurites;
    }

    public void setAlerteSecurites(Set<AlerteSecurite> alerteSecurites) {
        if (this.alerteSecurites != null) {
            this.alerteSecurites.forEach(i -> i.setUtilisateur(null));
        }
        if (alerteSecurites != null) {
            alerteSecurites.forEach(i -> i.setUtilisateur(this));
        }
        this.alerteSecurites = alerteSecurites;
    }

    public Utilisateur alerteSecurites(Set<AlerteSecurite> alerteSecurites) {
        this.setAlerteSecurites(alerteSecurites);
        return this;
    }

    public Utilisateur addAlerteSecurite(AlerteSecurite alerteSecurite) {
        this.alerteSecurites.add(alerteSecurite);
        alerteSecurite.setUtilisateur(this);
        return this;
    }

    public Utilisateur removeAlerteSecurite(AlerteSecurite alerteSecurite) {
        this.alerteSecurites.remove(alerteSecurite);
        alerteSecurite.setUtilisateur(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Utilisateur)) {
            return false;
        }
        return getId() != null && getId().equals(((Utilisateur) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Utilisateur{" +
            "id=" + getId() +
            ", nom='" + getNom() + "'" +
            ", prenom='" + getPrenom() + "'" +
            ", email='" + getEmail() + "'" +
            ", motDePasse='" + getMotDePasse() + "'" +
            ", dateNaissance='" + getDateNaissance() + "'" +
            ", typeJustificatifAge='" + getTypeJustificatifAge() + "'" +
            ", statut='" + getStatut() + "'" +
            ", dateCreation='" + getDateCreation() + "'" +
            "}";
    }
}
