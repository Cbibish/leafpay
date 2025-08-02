package com.leafpay.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Objects;
import java.util.Set;

/**
 * A DTO for the {@link com.leafpay.domain.Utilisateur} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class UtilisateurDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(max = 100)
    private String nom;

    @NotNull
    @Size(max = 100)
    private String prenom;

    @NotNull
    @Size(max = 255)
    private String email;

    @NotNull
    @Size(max = 255)
    private String motDePasse;

    private LocalDate dateNaissance;

    @Size(max = 255)
    private String typeJustificatifAge;

    @Size(max = 50)
    private String statut;

    private Instant dateCreation;

    private RoleDTO role;

    private Set<String> authorities;

public Set<String> getAuthorities() {
    return authorities;
}

public void setAuthorities(Set<String> authorities) {
    this.authorities = authorities;
}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMotDePasse() {
        return motDePasse;
    }

    public void setMotDePasse(String motDePasse) {
        this.motDePasse = motDePasse;
    }

    public LocalDate getDateNaissance() {
        return dateNaissance;
    }

    public void setDateNaissance(LocalDate dateNaissance) {
        this.dateNaissance = dateNaissance;
    }

    public String getTypeJustificatifAge() {
        return typeJustificatifAge;
    }

    public void setTypeJustificatifAge(String typeJustificatifAge) {
        this.typeJustificatifAge = typeJustificatifAge;
    }

    public String getStatut() {
        return statut;
    }

    public void setStatut(String statut) {
        this.statut = statut;
    }

    public Instant getDateCreation() {
        return dateCreation;
    }

    public void setDateCreation(Instant dateCreation) {
        this.dateCreation = dateCreation;
    }

    public RoleDTO getRole() {
        return role;
    }

    public void setRole(RoleDTO role) {
        this.role = role;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof UtilisateurDTO)) {
            return false;
        }

        UtilisateurDTO utilisateurDTO = (UtilisateurDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, utilisateurDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "UtilisateurDTO{" +
                "id=" + getId() +
                ", nom='" + getNom() + "'" +
                ", prenom='" + getPrenom() + "'" +
                ", email='" + getEmail() + "'" +
                ", motDePasse='" + getMotDePasse() + "'" +
                ", dateNaissance='" + getDateNaissance() + "'" +
                ", typeJustificatifAge='" + getTypeJustificatifAge() + "'" +
                ", statut='" + getStatut() + "'" +
                ", dateCreation='" + getDateCreation() + "'" +
                ", role=" + getRole() +
                "}";
    }
}
