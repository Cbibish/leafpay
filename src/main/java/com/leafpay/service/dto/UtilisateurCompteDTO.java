package com.leafpay.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.leafpay.domain.UtilisateurCompte} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class UtilisateurCompteDTO implements Serializable {

    private Long id;

    @Size(max = 100)
    private String roleUtilisateurSurCeCompte;

    private UtilisateurDTO utilisateur;

    private CompteDTO compte;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRoleUtilisateurSurCeCompte() {
        return roleUtilisateurSurCeCompte;
    }

    public void setRoleUtilisateurSurCeCompte(String roleUtilisateurSurCeCompte) {
        this.roleUtilisateurSurCeCompte = roleUtilisateurSurCeCompte;
    }

    public UtilisateurDTO getUtilisateur() {
        return utilisateur;
    }

    public void setUtilisateur(UtilisateurDTO utilisateur) {
        this.utilisateur = utilisateur;
    }

    public CompteDTO getCompte() {
        return compte;
    }

    public void setCompte(CompteDTO compte) {
        this.compte = compte;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof UtilisateurCompteDTO)) {
            return false;
        }

        UtilisateurCompteDTO utilisateurCompteDTO = (UtilisateurCompteDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, utilisateurCompteDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "UtilisateurCompteDTO{" +
            "id=" + getId() +
            ", roleUtilisateurSurCeCompte='" + getRoleUtilisateurSurCeCompte() + "'" +
            ", utilisateur=" + getUtilisateur() +
            ", compte=" + getCompte() +
            "}";
    }
}
